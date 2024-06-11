import static org.junit.Assert.*;
import org.junit.Test;

public class ChangeSetPerformerTest {

    @Test
    public void testPerform_singleChange() throws IOException {
        // Given
        ChangeSet changeSet = new ChangeSet();
        Change addChange = new Change(Change.TYPE_ADD, "file1.txt");
        changeSet.add(addChange);
        
        ChangeSetPerformer performer = new ChangeSetPerformer(changeSet);
        
        InputStream originalInput = new ByteArrayInputStream("Original Data".getBytes());
        ArchiveInputStream in = new ArchiveInputStream(originalInput);
        
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        ArchiveOutputStream out = new ArchiveOutputStream(output);
        
        // When
        ChangeSetResults results = performer.perform(in, out);
        
        // Then
        ArchiveInputStream outputIn = new ArchiveInputStream(new ByteArrayInputStream(output.toByteArray()));
        assertTrue(results.hasBeenAdded("file1.txt"));
        assertFalse(results.hasBeenDeleted("file1.txt"));
        assertNotNull(outputIn.getNextEntry());
    }

    @Test
    public void testPerform_multipleChanges() throws IOException {
        // Given
        ChangeSet changeSet = new ChangeSet();
        Change addChange = new Change(Change.TYPE_ADD, "file1.txt");
        Change deleteChange = new Change(Change.TYPE_DELETE, "file2.txt");
        changeSet.add(addChange);
        changeSet.add(deleteChange);
        
        ChangeSetPerformer performer = new ChangeSetPerformer(changeSet);
        
        InputStream originalInput = new ByteArrayInputStream("Original Data".getBytes());
        ArchiveInputStream in = new ArchiveInputStream(originalInput);
        
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        ArchiveOutputStream out = new ArchiveOutputStream(output);
        
        // When
        ChangeSetResults results = performer.perform(in, out);
        
        // Then
        ArchiveInputStream outputIn = new ArchiveInputStream(new ByteArrayInputStream(output.toByteArray()));
        assertTrue(results.hasBeenAdded("file1.txt"));
        assertTrue(results.hasBeenDeleted("file2.txt"));
        assertNotNull(outputIn.getNextEntry());
    }

    @Test
    public void testPerform_addAndDeleteSameFile() throws IOException {
        // Given
        ChangeSet changeSet = new ChangeSet();
        Change addChange = new Change(Change.TYPE_ADD, "file1.txt");
        Change deleteChange = new Change(Change.TYPE_DELETE, "file1.txt");
        changeSet.add(addChange);
        changeSet.add(deleteChange);
        
        ChangeSetPerformer performer = new ChangeSetPerformer(changeSet);
        
        InputStream originalInput = new ByteArrayInputStream("Original Data".getBytes());
        ArchiveInputStream in = new ArchiveInputStream(originalInput);
        
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        ArchiveOutputStream out = new ArchiveOutputStream(output);
        
        // When
        ChangeSetResults results = performer.perform(in, out);
        
        // Then
        ArchiveInputStream outputIn = new ArchiveInputStream(new ByteArrayInputStream(output.toByteArray()));
        assertFalse(results.hasBeenAdded("file1.txt"));
        assertTrue(results.hasBeenDeleted("file1.txt"));
        assertNull(outputIn.getNextEntry());
    }
}