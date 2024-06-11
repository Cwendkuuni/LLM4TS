import org.junit.Test;
import static org.junit.Assert.*;

public class ChangeSetPerformerTest {

    @Test
    public void testPerform_AddReplaceMode() {
        // Create a ChangeSet with one ADD ReplaceMode change
        ChangeSet changeSet = new ChangeSet();
        Change change = new Change(Change.TYPE_ADD, true);
        change.setInput(new InputStream());
        change.setEntry(new ArchiveEntry());
        changeSet.addChange(change);
        
        // Create an ArchiveInputStream and an ArchiveOutputStream
        ArchiveInputStream in = new ArchiveInputStream();
        ArchiveOutputStream out = new ArchiveOutputStream();
        
        // Create a ChangeSetPerformer with the ChangeSet
        ChangeSetPerformer performer = new ChangeSetPerformer(changeSet);
        
        // Perform the changes using the perform method
        ChangeSetResults results = performer.perform(in, out);
        
        // Assert that the change was added and marked as added from the ChangeSet
        assertTrue(results.hasBeenAdded(change.getEntry().getName()));
        assertTrue(results.addedFromChangeSet(change.getEntry().getName()));
    }
    
    @Test
    public void testPerform_Delete() {
        // Create a ChangeSet with one DELETE change
        ChangeSet changeSet = new ChangeSet();
        Change change = new Change(Change.TYPE_DELETE);
        change.setTargetFile("file.txt");
        changeSet.addChange(change);
        
        // Create an ArchiveInputStream and an ArchiveOutputStream
        ArchiveInputStream in = new ArchiveInputStream();
        ArchiveOutputStream out = new ArchiveOutputStream();
        
        // Create a ChangeSetPerformer with the ChangeSet
        ChangeSetPerformer performer = new ChangeSetPerformer(changeSet);
        
        // Perform the changes using the perform method
        ChangeSetResults results = performer.perform(in, out);
        
        // Assert that the file was deleted
        assertTrue(results.deleted("file.txt"));
    }
    
    @Test
    public void testPerform_DeleteDir() {
        // Create a ChangeSet with one DELETE_DIR change
        ChangeSet changeSet = new ChangeSet();
        Change change = new Change(Change.TYPE_DELETE_DIR);
        change.setTargetFile("dir");
        changeSet.addChange(change);
        
        // Create an ArchiveInputStream and an ArchiveOutputStream
        ArchiveInputStream in = new ArchiveInputStream();
        ArchiveOutputStream out = new ArchiveOutputStream();
        
        // Create a ChangeSetPerformer with the ChangeSet
        ChangeSetPerformer performer = new ChangeSetPerformer(changeSet);
        
        // Perform the changes using the perform method
        ChangeSetResults results = performer.perform(in, out);
        
        // Assert that the directory and all its contents were deleted
        assertTrue(results.deleted("dir/file1.txt"));
        assertTrue(results.deleted("dir/file2.txt"));
        assertTrue(results.deleted("dir/subdir/file3.txt"));
    }
    
    @Test
    public void testPerform_Copy() {
        // Create a ChangeSet with one ADD change without ReplaceMode
        ChangeSet changeSet = new ChangeSet();
        Change change = new Change(Change.TYPE_ADD, false);
        change.setInput(new InputStream());
        change.setEntry(new ArchiveEntry());
        changeSet.addChange(change);
        
        // Create an ArchiveInputStream and an ArchiveOutputStream
        ArchiveInputStream in = new ArchiveInputStream();
        ArchiveOutputStream out = new ArchiveOutputStream();
        
        // Create a ChangeSetPerformer with the ChangeSet
        ChangeSetPerformer performer = new ChangeSetPerformer(changeSet);
        
        // Perform the changes using the perform method
        ChangeSetResults results = performer.perform(in, out);
        
        // Assert that the change was added and marked as added from the ChangeSet
        assertTrue(results.hasBeenAdded(change.getEntry().getName()));
        assertTrue(results.addedFromChangeSet(change.getEntry().getName()));
    }
    
    @Test
    public void testPerform_EmptyChangeSet() {
        // Create an empty ChangeSet
        ChangeSet changeSet = new ChangeSet();
        
        // Create an ArchiveInputStream and an ArchiveOutputStream
        ArchiveInputStream in = new ArchiveInputStream();
        ArchiveOutputStream out = new ArchiveOutputStream();
        
        // Create a ChangeSetPerformer with the ChangeSet
        ChangeSetPerformer performer = new ChangeSetPerformer(changeSet);
        
        // Perform the changes using the perform method
        ChangeSetResults results = performer.perform(in, out);
        
        // Assert that no changes were performed
        assertTrue(results.getAddedFromChangeSet().isEmpty());
        assertTrue(results.getDeleted().isEmpty());
        assertTrue(results.getAddedFromStream().isEmpty());
    }
}