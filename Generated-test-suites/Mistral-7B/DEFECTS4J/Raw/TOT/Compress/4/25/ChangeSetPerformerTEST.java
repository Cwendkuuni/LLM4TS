import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.archivers.ArchiveOutputStream;
import org.apache.commons.compress.utils.IOUtils;
import org.apache.commons.compress.compressors.gzip.GzipCompressorInputStream;
import org.apache.commons.compress.compressors.gzip.GzipCompressorOutputStream;
import org.apache.commons.compress.utils.IOUtils;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;

public class ChangeSetPerformerTest {

    @Test
    public void testPerform_NoChanges() throws IOException {
        Set<Change> changeSet = Collections.emptySet();
        ChangeSetPerformer performer = new ChangeSetPerformer(new ChangeSet(changeSet));
        ArchiveInputStream input = createTestArchive("test-no-change.tar.gz");
        ArchiveOutputStream output = new ArchiveOutputStream(new ByteArrayOutputStream());
        ChangeSetResults result = performer.perform(input, output);
        
        Assert.assertEquals(0, result.getAddedFromChangeSet().size());
        Assert.assertEquals(0, result.getDeleted().size());
        Assert.assertEquals(0, result.getAddedFromStream().size());

        input.close();
        output.close();
    }

    @Test
    public void testPerform_SingleAdd() throws IOException {
        Set<Change> changeSet = new LinkedHashSet<>();
        changeSet.add(new Change("file1.txt", Change.TYPE_ADD, false));
        ChangeSetPerformer performer = new ChangeSetPerformer(new ChangeSet(changeSet));
        ArchiveInputStream input = createTestArchive("test-single-add.tar.gz");
        ArchiveOutputStream output = new ArchiveOutputStream(new ByteArrayOutputStream());
        ChangeSetResults result = performer.perform(input, output);

        Assert.assertEquals(1, result.getAddedFromChangeSet().size());
        Assert.assertEquals("file1.txt", result.getAddedFromChangeSet().iterator().next());

        byte[] expectedContent = "Single Add Test".getBytes();
        ArchiveEntry entry = new TestArchiveEntry("file1.txt", "test-single-add.tar.gz");
        Assert.assertEquals(expectedContent.length, output.getSize());
        InputStream testInputStream = new ByteArrayInputStream(output.toByteArray());
        Assert.assertArrayEquals(expectedContent, IOUtils.toByteArray(testInputStream));

        input.close();
        output.close();
    }

    @Test
    public void testPerform_SingleDelete() throws IOException {
        Set<Change> changeSet = new LinkedHashSet<>();
        changeSet.add(new Change("file1.txt", Change.TYPE_DELETE, false));
        ChangeSetPerformer performer = new ChangeSetPerformer(new ChangeSet(changeSet));
        ArchiveInputStream input = createTestArchive("test-single-delete.tar.gz");
        ArchiveOutputStream output = new ArchiveOutputStream(new ByteArrayOutputStream());
        ChangeSetResults result = performer.perform(input, output);

        Assert.assertEquals(0, result.getAddedFromChangeSet().size());
        Assert.assertEquals(1, result.getDeleted().size());
        Assert.assertEquals("file1.txt", result.getDeleted().iterator().next());

        input.close();
        output.close();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testPerform_InvalidChangeType() throws IOException {
        Set<Change> changeSet = new LinkedHashSet<>();
        changeSet.add(new Change("file1.txt", 4, false));
        ChangeSetPerformer performer = new ChangeSetPerformer(new ChangeSet(changeSet));
        ArchiveInputStream input = createTestArchive("test-invalid-change-type.tar.gz");
        ArchiveOutputStream output = new ArchiveOutputStream(new ByteArrayOutputStream());

        performer.perform(input, output);
    }

    @Test(expected = IOException.class)
    public void testPerform_ErrorDuringPerform() throws IOException {
        Set<Change> changeSet = new LinkedHashSet<>();
        changeSet.add(new Change("file1.txt", Change.TYPE_ADD, false));
        ChangeSetPerformer performer = new ChangeSetPerformer(new ChangeSet(changeSet));
        ArchiveInputStream input = new ArchiveInputStream(new GzipCompressorInputStream(new ByteArrayInputStream(new byte[0])));
        ArchiveOutputStream output = new ArchiveOutputStream(new ByteArrayOutputStream());

        performer.perform(input, output);
    }

    @Test
    public void testPerform_MultipleChanges() throws IOException {
        Set<Change> changeSet = new LinkedHashSet<>();
        changeSet.add(new Change("file1.txt", Change.TYPE_ADD, false));
        changeSet.add(new Change("file2.txt", Change.TYPE_DELETE, false));
        ChangeSetPerformer performer = new ChangeSetPerformer(new ChangeSet(changeSet));
        ArchiveInputStream input = createTestArchive("test-multiple-changes.tar.gz");
        ArchiveOutputStream output = new ArchiveOutputStream(new ByteArrayOutputStream());
        ChangeSetResults result = performer.perform(input, output);

        Assert.assertEquals(1, result.getAddedFromChangeSet().size());
        Assert.assertEquals("file1.txt", result.getAddedFromChangeSet().iterator().next());
        Assert.assertEquals(1, result.getDeleted().size());
        Assert.assertEquals("file2.txt", result.getDeleted().iterator().next());

        byte[] expectedContent1 = "Single Add Test".getBytes();
        ArchiveEntry entry1 = new TestArchiveEntry("file1.txt", "test-multiple-changes.tar.gz");
        Assert.assertEquals(expectedContent1.length, output.getSize());
        InputStream testInputStream1 = new ByteArrayInputStream(output.toByteArray());
        Assert.assertArrayEquals(expectedContent1, IOUtils.toByteArray(testInputStream1));

        Assert.assertEquals(0, result.getAddedFromStream().size());

        input.close();
        output.close();
    }

    private ArchiveInputStream createTestArchive(String archiveName) throws IOException {
        InputStream testInputStream = getClass().getResourceAsStream(archiveName);
        return new ArchiveInputStream(new GzipCompressorInputStream(testInputStream));
    }

    private static class TestArchiveEntry extends ArchiveEntry {
        TestArchiveEntry(String name, String archiveName) {
            super(name, archiveName);
        }

        @Override
        public long getSize() {
            return 10;
        }

        @Override
        public boolean isDirectory() {
            return false;
        }
    }
}