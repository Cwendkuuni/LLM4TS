import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.archivers.ArchiveOutputStream;
import org.apache.commons.compress.utils.IOUtils;
import org.apache.commons.compress.changeSets.Change;
import org.apache.commons.compress.changeSets.ChangeSet;
import org.apache.commons.compress.changeSets.ChangeSetResults;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.Assert;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public class ChangeSetPerformerTest {

    private ChangeSet changeSet;
    private Set changes;
    private ArchiveInputStream archiveInput;
    private ArchiveOutputStream archiveOutput;
    private ByteArrayOutputStream outputStream;

    @Before
    public void setup() throws IOException {
        changes = new LinkedHashSet<>();
        ChangeSet.Builder builder = new ChangeSet.Builder();
        Change addChange = new Change("src/test/java/testFile.txt", "testContent", Change.TYPE_ADD, null, true);
        Change deleteChange = new Change("src/test/java/testFile.txt", null, Change.TYPE_DELETE, null, false);
        Change addReplaceChange = new Change("src/test/java/testFileReplace.txt", "testContent", Change.TYPE_ADD, null, true, true);

        builder.addChange(addChange);
        builder.addChange(deleteChange);
        builder.addChange(addReplaceChange);
        this.changeSet = builder.toChangeSet();
        this.changes = changeSet.getChanges();
        this.archiveInput = createTestInput();
        this.archiveOutput = createTestOutput();
        this.outputStream = new ByteArrayOutputStream();
    }

    @After
    public void teardown() throws IOException {
        IOUtils.closeQuietly(archiveInput);
        IOUtils.closeQuietly(archiveOutput);
        IOUtils.closeQuietly(outputStream);
    }

    @Test
    public void testPerform() throws IOException {
        ChangeSetResults results = changeSetPerformer.perform(archiveInput, archiveOutput);
        Assert.assertEquals("Unexpected number of added files from ChangeSet.", 1, results.getAddedFromChangeSet().size());
        Assert.assertEquals("Unexpected number of deleted files.", 0, results.getDeleted().size());
        Assert.assertEquals("Unexpected number of added files from Stream.", 1, results.getAddedFromStream().size());

        List<String> addedFromChangeSet = results.getAddedFromChangeSet();
        Assert.assertEquals("Unexpected added file from ChangeSet.", "testFileReplace.txt", addedFromChangeSet.get(0));

        List<String> addedFromStream = results.getAddedFromStream();
        Assert.assertEquals("Unexpected added file from Stream.", "testFile.txt", addedFromStream.get(0));
    }

    @Test
    public void testPerformWithReplaceMode() throws IOException {
        Change replaceChange = new Change("src/test/java/testFile.txt", "testContent", Change.TYPE_ADD, null, true, true);
        changeSet = new ChangeSet();
        changeSet.setChanges(new LinkedHashSet<>(Arrays.asList(replaceChange)));
        ChangeSetPerformer changeSetPerformer = new ChangeSetPerformer(changeSet);
        ChangeSetResults results = changeSetPerformer.perform(archiveInput, archiveOutput);
        Assert.assertEquals("Unexpected number of added files from ChangeSet.", 1, results.getAddedFromChangeSet().size());
        Assert.assertEquals("Unexpected number of deleted files.", 0, results.getDeleted().size());
        Assert.assertEquals("Unexpected number of added files from Stream.", 0, results.getAddedFromStream().size());

        List<String> addedFromChangeSet = results.getAddedFromChangeSet();
        Assert.assertEquals("Unexpected added file from ChangeSet.", "testFile.txt", addedFromChangeSet.get(0));

        ArchiveEntry entry = archiveInput.getNextEntry();
        Assert.assertNotNull("Unexpected, no entry found.", entry);
        Assert.assertEquals("Unexpected name of entry.", "testFile.txt", entry.getName());

        byte[] data = outputStream.toByteArray();
        Assert.assertArrayEquals("Unexpected data in output stream.", new byte[]{"testContent".getBytes()}, Arrays.copyOfRange(data, 0, data.length));
    }

    @Test(expected = IOException.class)
    public void testPerformWithConflictingChanges() throws IOException {
        Change addChange = new Change("src/test/java/testFile.txt", "testContent", Change.TYPE_ADD, null, true);
        Change deleteChange = new Change("src/test/java/testFile.txt", null, Change.TYPE_DELETE, null, false);
        changeSet = new ChangeSet();
        changeSet.setChanges(new LinkedHashSet<>(Arrays.asList(addChange, deleteChange)));
        ChangeSetPerformer changeSetPerformer = new ChangeSetPerformer(changeSet);
        changeSetPerformer.perform(archiveInput, archiveOutput);
    }

    @Test(expected = IOException.class)
    public void testPerformWithIncompatibleChangeTypes() throws IOException {
        Change addChange = new Change("src/test/java/testFile.txt", "testContent", Change.TYPE_DELETE, null, false);
        ChangeSetPerformer changeSetPerformer = new ChangeSetPerformer(changeSet);
        changeSetPerformer.perform(archiveInput, archiveOutput);
    }

    private ArchiveInputStream createTestInput() throws IOException {
        ByteArrayOutputStream inputStream = new ByteArrayOutputStream();
        ArchiveOutputStream archiveOutput = new ArchiveOutputStream("Memory:test.tar", new GzipCompressorOutputStream(inputStream));
        archiveOutput.putNextEntry(new JtarEntry("testFile.txt", "testContent".getBytes()));
        archiveOutput.closeArchiveEntry();
        archiveOutput.finish();
        IOUtils.closeQuietly(archiveOutput);
        return new ArchiveInputStream(new ByteArrayInputStream(inputStream.toByteArray()));
    }

    private ArchiveOutputStream createTestOutput() throws IOException {
        return new ArchiveOutputStream("Memory:test.tar", new GzipCompressorOutputStream(outputStream));
    }
}