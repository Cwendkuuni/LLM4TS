import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.LinkedHashSet;
import java.util.Set;

import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.archivers.ArchiveOutputStream;
import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipArchiveOutputStream;
import org.apache.commons.compress.changes.Change;
import org.apache.commons.compress.changes.ChangeSet;
import org.apache.commons.compress.utils.IOUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class ChangeSetPerformerTest {

    private ChangeSetPerformer performer;
    private ChangeSet changeSet;
    private ArchiveInputStream in;
    private ArchiveOutputStream out;
    private ByteArrayOutputStream outStream;

    @Before
    public void setUp() throws IOException {
        changeSet = new ChangeSet();
        in = null;

        // Create a dummy ZIP file inputStream
        byte[] dummyData = new byte[1024];
        in = new ArchiveInputStream(new ByteArrayInputStream(dummyData));

        outStream = new ByteArrayOutputStream();
        out = new ZipArchiveOutputStream(outStream);

        // Add some changes to the changeSet
        Set<Change> changes = new LinkedHashSet<>();
        Change change1 = new Change(Change.TYPE_ADD, "file1.txt", false);
        Change change2 = new Change(Change.TYPE_DELETE, "file2.txt", false);
        Change change3 = new Change(Change.TYPE_DELETE_DIR, "dir1", false);
        changes.add(change1);
        changes.add(change2);
        changes.add(change3);

        changeSet.setChanges(changes);
        performer = new ChangeSetPerformer(changeSet);
    }

    @After
    public void tearDown() throws IOException {
        if (in != null) {
            in.close();
        }
        if (out != null) {
            out.close();
        }
        outStream.close();
    }

    @Test
    public void testPerform_addAndReplaceMode() throws IOException {
        // Prepare the input stream for change1
        byte[] data1 = "test data 1".getBytes();
        ByteArrayInputStream input1 = new ByteArrayInputStream(data1);
        changeSet.getChange("file1.txt").setInput(input1);

        ChangeSetResults results = performer.perform(in, out);

        // Verify that the file1.txt has been added and replaced in the output stream
        assertTrue(results.hasBeenAdded("file1.txt"));
        assertFalse(results.hasBeenAdded("file2.txt"));
        assertFalse(results.hasBeenAdded("dir1"));

        // Verify that the file1.txt content is correct
        ArchiveEntry entry = ((ArchiveOutputStream) out).getArchiveEntries().get(0);
        assertEquals("file1.txt", entry.getName());
        byte[] outputData = IOUtils.toByteArray(entry.getData());
        assertArrayEquals(data1, outputData);
    }

    @Test
    public void testPerform_deleteFile() throws IOException {
        // Prepare the input stream for change2
        byte[] data2 = "test data 2".getBytes();
        ByteArrayInputStream input2 = new ByteArrayInputStream(data2);
        changeSet.getChange("file2.txt").setInput(input2);

        // Prepare some existing files in the input stream
        ArchiveEntry existingFile = new ZipArchiveEntry("file2.txt");
        ((ArchiveOutputStream) in).putArchiveEntry(existingFile);
        IOUtils.copy(new ByteArrayInputStream(data2), (ArchiveOutputStream) in);
        ((ArchiveOutputStream) in).closeArchiveEntry();

        ChangeSetResults results = performer.perform(in, out);

        // Verify that the file2.txt has been deleted from the input stream
        assertTrue(results.hasBeenAdded("file1.txt"));
        assertTrue(results.hasBeenDeleted("file2.txt"));
        assertFalse(results.hasBeenAdded("dir1"));

        // Verify that the output stream does not contain the file2.txt entry
        ArchiveEntry entry = ((ArchiveOutputStream) out).getArchiveEntries()
                .stream()
                .filter(e -> e.getName().equals("file2.txt"))
                .findFirst()
                .orElse(null);
        assertNull(entry);
    }

    @Test
    public void testPerform_deleteDirectory() throws IOException {
        // Prepare the input stream for change3
        changeSet.getChange("dir1").setInput(null);

        // Prepare some existing files in the input stream
        ArchiveEntry existingFile1 = new ZipArchiveEntry("dir1/file1.txt");
        ((ArchiveOutputStream) in).putArchiveEntry(existingFile1);
        IOUtils.copy(new ByteArrayInputStream("test data 1".getBytes()), (ArchiveOutputStream) in);
        ((ArchiveOutputStream) in).closeArchiveEntry();

        ArchiveEntry existingFile2 = new ZipArchiveEntry("dir1/file2.txt");
        ((ArchiveOutputStream) in).putArchiveEntry(existingFile2);
        IOUtils.copy(new ByteArrayInputStream("test data 2".getBytes()), (ArchiveOutputStream) in);
        ((ArchiveOutputStream) in).closeArchiveEntry();

        ChangeSetResults results = performer.perform(in, out);

        // Verify that all files in dir1 have been deleted from the input stream
        assertTrue(results.hasBeenAdded("file1.txt"));
        assertTrue(results.hasBeenAdded("file2.txt"));
        assertTrue(results.hasBeenDeleted("dir1/file1.txt"));
        assertTrue(results.hasBeenDeleted("dir1/file2.txt"));
        assertTrue(results.hasBeenDeleted("dir1"));

        // Verify that the output stream does not contain the dir1 entries
        ArchiveEntry entry1 = ((ArchiveOutputStream) out).getArchiveEntries()
                .stream()
                .filter(e -> e.getName().equals("dir1/file1.txt"))
                .findFirst()
                .orElse(null);
        assertNull(entry1);

        ArchiveEntry entry2 = ((ArchiveOutputStream) out).getArchiveEntries()
                .stream()
                .filter(e -> e.getName().equals("dir1/file2.txt"))
                .findFirst()
                .orElse(null);
        assertNull(entry2);

        ArchiveEntry entry3 = ((ArchiveOutputStream) out).getArchiveEntries()
                .stream()
                .filter(e -> e.getName().equals("dir1"))
                .findFirst()
                .orElse(null);
        assertNull(entry3);
    }

    @Test
    public void testPerform_addWithoutReplaceMode() throws IOException {
        // Prepare the input stream for change1
        byte[] data1 = "test data 1".getBytes();
        ByteArrayInputStream input1 = new ByteArrayInputStream(data1);
        changeSet.getChange("file1.txt").setInput(input1);

        // Prepare existing file in the output stream
        ArchiveEntry existingFile = new ZipArchiveEntry("file1.txt");
        ((ArchiveOutputStream) out).putArchiveEntry(existingFile);
        IOUtils.copy(new ByteArrayInputStream("existing file".getBytes()), (ArchiveOutputStream) out);
        ((ArchiveOutputStream) out).closeArchiveEntry();

        ChangeSetResults results = performer.perform(in, out);

        // Verify that the file1.txt has been added and replaced in the output stream
        assertTrue(results.hasBeenAdded("file1.txt"));
        assertFalse(results.hasBeenAdded("file2.txt"));
        assertFalse(results.hasBeenAdded("dir1"));

        // Verify that the output stream contains the new file1.txt along with the existing file
        ArchiveEntry entry1 = ((ArchiveOutputStream) out).getArchiveEntries()
                .stream()
                .filter(e -> e.getName().equals("file1.txt"))
                .findFirst()
                .orElse(null);
        assertNotNull(entry1);
        byte[] outputData1 = IOUtils.toByteArray(entry1.getData());
        assertArrayEquals(data1, outputData1);

        ArchiveEntry entry2 = ((ArchiveOutputStream) out).getArchiveEntries()
                .stream()
                .filter(e -> e.getName().equals("file1.txt"))
                .findFirst()
                .orElse(null);
        assertNotNull(entry2);
        byte[] outputData2 = IOUtils.toByteArray(entry2.getData());
        assertArrayEquals("existing file".getBytes(), outputData2);
    }

}