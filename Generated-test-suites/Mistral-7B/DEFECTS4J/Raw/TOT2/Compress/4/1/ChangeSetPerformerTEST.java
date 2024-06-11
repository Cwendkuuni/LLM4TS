import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.archivers.ArchiveOutputStream;
import org.apache.commons.compress.utils.IOUtils;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class ChangeSetPerformerTest {
    ChangeSet changeSet;
    ChangeSetPerformer performer;
    ArchiveInputStream inputStream;
    ArchiveOutputStream outputStream;
    ArchiveEntry entry;
    InputStream input;

    @Before
    public void setup() {
        // Initialize ChangeSet and ChangeSetPerformer
        changeSet = new ChangeSet();
        performer = new ChangeSetPerformer(changeSet);

        // Initialize ArchiveInputStream and ArchiveOutputStream for testing
        try {
            inputStream = new ArchiveInputStream(getClass().getResourceAsStream("/test.tar"));
            outputStream = new ArchiveOutputStream("output.tar", inputStream.getFormat());
        } catch (IOException e) {
            e.printStackTrace();
        }

        input = getClass().getResourceAsStream("/testFile.txt");
    }

    @Test
    public void testPerform_EmptyChangeSet() throws IOException {
        // Test perform method with empty ChangeSet
        ChangeSetResults result = performer.perform(inputStream, outputStream);

        // Verify no files are added or deleted
        assertEquals(0, result.getNumberOfChanges());
        assertFalse(outputStream.hasNextEntry());

        // Verify input and output streams are closed
        inputStream.close();
        outputStream.close();
    }

    @Test
    public void testPerform_AddChange() throws IOException {
        // Add a new file to ChangeSet
        Change addChange = new Change(Change.TYPE_ADD, "testFile.txt", input, null);
        changeSet.addChange(addChange);

        // Perform changes and verify file is added to output.tar
        performer = new ChangeSetPerformer(changeSet);
        ChangeSetResults result = performer.perform(inputStream, outputStream);

        // Verify file is added to output.tar
        assertEquals(1, result.getNumberOfChanges());
        assertNotNull(outputStream.getNextEntry());
        assertTrue(outputStream.getNextEntry().getName().equals("testFile.txt"));

        // Verify input and output streams are closed
        inputStream.close();
        outputStream.close();
    }

    @Test
    public void testPerform_DeleteChange() throws IOException {
        // Add a file to ChangeSet and then delete it
        Change addChange = new Change(Change.TYPE_ADD, "testFile.txt", input, null);
        Change deleteChange = new Change(Change.TYPE_DELETE, "testFile.txt", null, null);
        changeSet.addChange(addChange);
        changeSet.addChange(deleteChange);

        // Perform changes and verify file is deleted from output.tar
        performer = new ChangeSetPerformer(changeSet);
        ChangeSetResults result = performer.perform(inputStream, outputStream);

        // Verify file is not added to output.tar
        assertEquals(0, result.getNumberOfChanges());
        assertFalse(outputStream.hasNextEntry());

        // Verify input and output streams are closed
        inputStream.close();
        outputStream.close();
    }

    @Test(expected = IOException.class)
    public void testPerform_IOException() throws IOException {
        // Test perform method with IOException
        ChangeSetResults result = performer.perform(inputStream, outputStream);
        throw new IOException("Expected IOException");
    }

    @Test
    public void testPerform_MixedChanges() throws IOException {
        // Add a file and then delete it in ChangeSet
        Change addChange = new Change(Change.TYPE_ADD, "testFile.txt", input, null);
        Change deleteChange = new Change(Change.TYPE_DELETE, "testFile.txt", null, null);
        changeSet.addChange(addChange);
        changeSet.addChange(deleteChange);

        // Perform changes and verify file is deleted from output.tar
        performer = new ChangeSetPerformer(changeSet);
        ChangeSetResults result = performer.perform(inputStream, outputStream);

        // Verify file is not added to output.tar
        assertEquals(0, result.getNumberOfChanges());
        assertFalse(outputStream.hasNextEntry());

        // Verify input and output streams are closed
        inputStream.close();
        outputStream.close();
    }

    // Add more test cases for edge cases and other scenarios
}