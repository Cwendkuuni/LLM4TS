import static org.junit.Assert.*;
import org.junit.Test;
import org.junit.Before;
import java.io.*;
import org.apache.commons.compress.archivers.*;
import org.apache.commons.compress.archivers.zip.*;

public class ChangeSetPerformerTest {

    private static final String TEST_FILE_NAME = "testFile.txt";
    private static final String ZIP_FILE_NAME = "test.zip";

    @Before
    public void createTestFile() throws IOException {
        File file = new File(TEST_FILE_NAME);
        FileWriter writer = new FileWriter(file);
        writer.write("This is a test file.");
        writer.close();
    }

    @Test
    public void testPerform_AddReplaceMode() throws IOException {
        ChangeSet changeSet = new ChangeSet();
        Change change = new Change(Change.TYPE_ADD, true);
        change.setEntry(new ZipArchiveEntry(TEST_FILE_NAME));
        change.setInput(new FileInputStream(TEST_FILE_NAME));
        changeSet.addChange(change);

        ArchiveInputStream in = new ZipArchiveInputStream(new FileInputStream(ZIP_FILE_NAME));
        ArchiveOutputStream out = new ZipArchiveOutputStream(new FileOutputStream(ZIP_FILE_NAME));
        ChangeSetPerformer changeSetPerformer = new ChangeSetPerformer(changeSet);

        ChangeSetResults results = changeSetPerformer.perform(in, out);

        assertNotNull(results);
        assertTrue(results.hasBeenAdded(TEST_FILE_NAME));
        assertEquals(1, results.getAddedFromChangeSet().size());
        assertEquals(TEST_FILE_NAME, results.getAddedFromChangeSet().get(0));
    }

    @Test
    public void testPerform_AddNoReplaceMode() throws IOException {
        ChangeSet changeSet = new ChangeSet();
        Change change = new Change(Change.TYPE_ADD, false);
        change.setEntry(new ZipArchiveEntry(TEST_FILE_NAME));
        change.setInput(new FileInputStream(TEST_FILE_NAME));
        changeSet.addChange(change);

        ArchiveInputStream in = new ZipArchiveInputStream(new FileInputStream(ZIP_FILE_NAME));
        ArchiveOutputStream out = new ZipArchiveOutputStream(new FileOutputStream(ZIP_FILE_NAME));
        ChangeSetPerformer changeSetPerformer = new ChangeSetPerformer(changeSet);

        ChangeSetResults results = changeSetPerformer.perform(in, out);

        assertNotNull(results);
        assertTrue(results.hasBeenAdded(TEST_FILE_NAME));
        assertEquals(1, results.getAddedFromChangeSet().size());
        assertEquals(TEST_FILE_NAME, results.getAddedFromChangeSet().get(0));
    }

    @Test
    public void testPerform_DeleteFile() throws IOException {
        ChangeSet changeSet = new ChangeSet();
        Change change = new Change(Change.TYPE_DELETE);
        change.setTargetFile(TEST_FILE_NAME);
        changeSet.addChange(change);

        ZipArchiveOutputStream zipOut = new ZipArchiveOutputStream(new FileOutputStream(ZIP_FILE_NAME));
        ZipArchiveEntry entry = new ZipArchiveEntry(TEST_FILE_NAME);
        entry.setSize(10);
        zipOut.putArchiveEntry(entry);
        zipOut.write("1234567890".getBytes());
        zipOut.closeArchiveEntry();
        zipOut.close();

        ArchiveInputStream in = new ZipArchiveInputStream(new FileInputStream(ZIP_FILE_NAME));
        ArchiveOutputStream out = new ZipArchiveOutputStream(new FileOutputStream("output.zip"));
        ChangeSetPerformer changeSetPerformer = new ChangeSetPerformer(changeSet);

        ChangeSetResults results = changeSetPerformer.perform(in, out);

        assertNotNull(results);
        assertTrue(results.getDeleted().contains(TEST_FILE_NAME));
        assertEquals(1, results.getDeleted().size());
        assertFalse(results.hasBeenAdded(TEST_FILE_NAME));
    }

    @Test
    public void testPerform_DeleteDirectory() throws IOException {
        ChangeSet changeSet = new ChangeSet();
        Change change = new Change(Change.TYPE_DELETE_DIR);
        change.setTargetFile("test");
        changeSet.addChange(change);

        ZipArchiveOutputStream zipOut = new ZipArchiveOutputStream(new FileOutputStream(ZIP_FILE_NAME));
        ZipArchiveEntry entry = new ZipArchiveEntry("test/testFile.txt");
        entry.setSize(10);
        zipOut.putArchiveEntry(entry);
        zipOut.write("1234567890".getBytes());
        zipOut.closeArchiveEntry();
        zipOut.close();

        ArchiveInputStream in = new ZipArchiveInputStream(new FileInputStream(ZIP_FILE_NAME));
        ArchiveOutputStream out = new ZipArchiveOutputStream(new FileOutputStream("output.zip"));
        ChangeSetPerformer changeSetPerformer = new ChangeSetPerformer(changeSet);

        ChangeSetResults results = changeSetPerformer.perform(in, out);

        assertNotNull(results);
        assertTrue(results.getDeleted().contains("test/testFile.txt"));
        assertEquals(1, results.getDeleted().size());
        assertFalse(results.hasBeenAdded("test/testFile.txt"));
    }
}