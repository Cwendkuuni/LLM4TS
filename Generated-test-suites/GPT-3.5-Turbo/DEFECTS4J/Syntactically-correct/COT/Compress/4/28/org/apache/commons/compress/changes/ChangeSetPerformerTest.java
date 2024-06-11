
import org.junit.Test;
import static org.junit.Assert.*;

public class ChangeSetPerformerTest {
    
    /** Test case for the perform method */
    @Test
    public void testPerform() throws IOException {
        // Create test input and output streams
        InputStream in = new ByteArrayInputStream("test input".getBytes());
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        
        // Create a ChangeSet with some changes
        ChangeSet changeSet = new ChangeSet();
        changeSet.addChange(new Change(Change.TYPE_ADD, "file1", false));
        changeSet.addChange(new Change(Change.TYPE_DELETE, "file2", false));
        
        // Create a ChangeSetPerformer and perform the changes
        ChangeSetPerformer performer = new ChangeSetPerformer(changeSet);
        ChangeSetResults results = performer.perform(in, out);
        
        // Assert that the perform method works correctly
        assertTrue(results.hasBeenAdded("file1"));
        assertTrue(results.deleted("file2"));
    }
    
    /** Test case for the isDeletedLater method */
    @Test
    public void testIsDeletedLater() {
        // Create a workingSet with some changes
        Set<Change> workingSet = new LinkedHashSet<>();
        workingSet.add(new Change(Change.TYPE_DELETE, "file1", false));
        workingSet.add(new Change(Change.TYPE_DELETE_DIR, "dir1", false));
        
        // Create an ArchiveEntry
        ArchiveEntry entry = new ArchiveEntry();
        entry.setName("file1");
        
        // Create a ChangeSetPerformer and call the isDeletedLater method
        ChangeSetPerformer performer = new ChangeSetPerformer();
        boolean result = performer.isDeletedLater(workingSet, entry);
        
        // Assert that the isDeletedLater method works correctly
        assertTrue(result);
    }
    
    /** Test case for the copyStream method */
    @Test
    public void testCopyStream() {
        // Create test input and output streams
        InputStream in = new ByteArrayInputStream("test input".getBytes());
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        
        // Create an ArchiveEntry
        ArchiveEntry entry = new ArchiveEntry();
        entry.setName("file1");
        
        // Create a ChangeSetPerformer and call the copyStream method
        ChangeSetPerformer performer = new ChangeSetPerformer();
        performer.copyStream(in, out, entry);
        
        // Assert that the copyStream method works correctly
        assertEquals("test input", out.toString());
    }
    
}
