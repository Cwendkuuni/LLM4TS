import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.archivers.ArchiveOutputStream;
import org.apache.commons.compress.utils.IOUtils;
import org.apache.commons.compress.utils.TestUtils;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.Set;

import static org.apache.commons.compress.utils.TestUtils.createTestFile;
import static org.apache.commons.compress.utils.TestUtils.getTestFile;

public class ChangeSetPerformerTest {

    @Test
    public void testPerform() throws Exception {
        ChangeSet changeSet = createChangeSet();
        ChangeSetPerformer performer = new ChangeSetPerformer(changeSet);

        InputStream input = getTestFile("test.zip");
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ArchiveOutputStream archiveOutputStream = new ArchiveOutputStream(outputStream);
        ArchiveInputStream archiveInputStream = new ArchiveInputStream(input);

        ChangeSetResults results = performer.perform(archiveInputStream, archiveOutputStream);

        Assert.assertNotNull(results);
        Assert.assertEquals(ChangeSetResults.STATUS_OK, results.status());
        Assert.assertEquals(3, results.getAdded().size());
        Assert.assertEquals(1, results.getDeleted().size());

        ArchiveInputStream newArchiveInputStream = new ArchiveInputStream(new ByteArrayInputStream(outputStream.toByteArray()));
        ArchiveEntry entry;
        while ((entry = newArchiveInputStream.getNextEntry()) != null) {
            if (entry.getName().equals("file2.txt")) {
                TestUtils.assertInputStreamsEqual(getTestFile("file2.txt"), newArchiveInputStream);
                break;
            }
        }

        Assert.assertFalse(newArchiveInputStream.getNextEntry() != null);
        newArchiveInputStream.close();
        archiveOutputStream.finish();
        archiveOutputStream.close();
        outputStream.close();
        input.close();
    }

    @Test
    public void testPerformWhenChangeSetIsEmpty() throws Exception {
        ChangeSet changeSet = new ChangeSet();
        ChangeSetPerformer performer = new ChangeSetPerformer(changeSet);

        InputStream input = getTestFile("test.zip");
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ArchiveOutputStream archiveOutputStream = new ArchiveOutputStream(outputStream);
        ArchiveInputStream archiveInputStream = new ArchiveInputStream(input);

        ChangeSetResults results = performer.perform(archiveInputStream, archiveOutputStream);

        Assert.assertNotNull(results);
        Assert.assertEquals(ChangeSetResults.STATUS_OK, results.status());
        Assert.assertEquals(0, results.getAdded().size());
        Assert.assertEquals(0, results.getDeleted().size());

        archiveOutputStream.finish();
        archiveOutputStream.close();
        outputStream.close();
        input.close();
    }

    @Test
    public void testPerformWhenInputIsEmpty() throws Exception {
        ChangeSet changeSet = createChangeSet();
        ChangeSetPerformer performer = new ChangeSetPerformer(changeSet);

        ByteArrayInputStream inputStream = new ByteArrayInputStream(new byte[0]);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ArchiveOutputStream archiveOutputStream = new ArchiveOutputStream(outputStream);
        ArchiveInputStream archiveInputStream = new ArchiveInputStream(inputStream);

        ChangeSetResults results = performer.perform(archiveInputStream, archiveOutputStream);

        Assert.assertNotNull(results);
        Assert.assertEquals(ChangeSetResults.STATUS_OK, results.status());
        Assert.assertEquals(0, results.getAdded().size());
        Assert.assertEquals(0, results.getDeleted().size());

        archiveOutputStream.finish();
        archiveOutputStream.close();
        outputStream.close();
        inputStream.close();
    }

    @Test
    public void testPerformWhenChangeSetHasChanges() throws Exception {
        ChangeSet changeSet = createChangeSet();
        ChangeSetPerformer performer = new ChangeSetPerformer(changeSet);

        InputStream input = getTestFile("test.zip");
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ArchiveOutputStream archiveOutputStream = new ArchiveOutputStream(outputStream);
        ArchiveInputStream archiveInputStream = new ArchiveInputStream(input);

        ChangeSetResults results = performer.perform(archiveInputStream, archiveOutputStream);

        Assert.assertNotNull(results);
        Assert.assertEquals(ChangeSetResults.STATUS_OK, results.status());
        Assert.assertEquals(2, results.getAdded().size());
        Assert.assertEquals(1, results.getDeleted().size());

        archiveOutputStream.finish();
        archiveOutputStream.close();
        outputStream.close();
        input.close();

        InputStream referenceInput = getTestFile("test2.zip");
        ByteArrayOutputStream referenceOutput = new ByteArrayOutputStream();
        ArchiveOutputStream referenceArchiveOutputStream = new ArchiveOutputStream(referenceOutput);
        ArchiveInputStream referenceArchiveInputStream = new ArchiveInputStream(referenceInput);

        ChangeSetPerformer referencePerformer = new ChangeSetPerformer(createChangeSet());
        referencePerformer.perform(referenceArchiveInputStream, referenceArchiveOutputStream);

        Assert.assertArraysEqual(referenceOutput.toByteArray(), outputStream.toByteArray());

        archiveOutputStream.close();
        outputStream.close();
        input.close();
        referenceArchiveInputStream.close();
        referenceArchiveOutputStream.close();
        referenceOutput.close();
    }

    private ChangeSet createChangeSet() throws Exception {
        ChangeSet changeSet = new ChangeSet();
        Set<Change> changes = changeSet.getChanges();

        changes.add(new Change(Change.TYPE_ADD, "file1.txt", null, getTestFile("file1.txt"), null, false));
        changes.add(new Change(Change.TYPE_ADD, "file2.txt", null, getTestFile("file2.txt"), null, true, "file1.txt"));
        changes.add(new Change(Change.TYPE_DELETE, "file3.txt", null, null, null, false));

        return changeSet;
    }
}