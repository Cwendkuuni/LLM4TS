import static org.junit.Assert.*;
import org.junit.Test;
import org.junit.Before;
import org.apache.commons.compress.changes.ChangeSet;
import org.apache.commons.compress.changes.Change;
import org.apache.commons.compress.changes.ChangeSetPerformer;
import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.archivers.ArchiveOutputStream;
import org.apache.commons.compress.changes.ChangeSetResults;
import java.io.IOException;
import java.io.InputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.HashSet;
import java.util.Iterator;

public class ChangeSetPerformerTest {
    private ChangeSet changeSet;
    private ChangeSetPerformer performer;
    
    @Before
    public void setUp() {
        changeSet = new ChangeSet();
        performer = new ChangeSetPerformer(changeSet);
    }
    
    @Test
    public void testPerform_AddAndReplace() throws IOException {
        // First, we add a change to add and replace a file
        Change change = new Change(Change.TYPE_ADD, "input_stream_replace", true);
        InputStream input = new ByteArrayInputStream("test data".getBytes());
        change.setInput(input);
        change.setEntry(new ArchiveEntry("file1"));
        changeSet.add(change);
        
        // Setting up the archive streams
        ArchiveInputStream in = new ArchiveInputStream(new ByteArrayInputStream("".getBytes()));
        ArchiveOutputStream out = new ArchiveOutputStream(new ByteArrayOutputStream());
        
        // Performing the changes
        ChangeSetResults results = performer.perform(in, out);
        
        // Asserting the results
        assertTrue(results.hasBeenAdded("file1"));
    }
    
    @Test
    public void testPerform_AddAndDelete() throws IOException {
        // Adding a change to add a file
        Change change = new Change(Change.TYPE_ADD, "input_stream_delete", false);
        InputStream input = new ByteArrayInputStream("test data".getBytes());
        change.setInput(input);
        change.setEntry(new ArchiveEntry("file2"));
        changeSet.add(change);
        
        // Adding another change to delete the file just added
        Change deleteChange = new Change(Change.TYPE_DELETE, "file2");
        changeSet.add(deleteChange);
        
        // Setting up the archive streams
        ArchiveInputStream in = new ArchiveInputStream(new ByteArrayInputStream("".getBytes()));
        ArchiveOutputStream out = new ArchiveOutputStream(new ByteArrayOutputStream());
        
        // Performing the changes
        ChangeSetResults results = performer.perform(in, out);
        
        // Asserting the results
        assertTrue(results.hasBeenDeleted("file2"));
    }
    
    @Test
    public void testPerform_AddDirectoryAndDelete() throws IOException {
        // Adding a change to add a directory
        Change change = new Change(Change.TYPE_ADD_DIR, "dir");
        change.setEntry(new ArchiveEntry("dir1/"));
        changeSet.add(change);
        
        // Adding another change to delete the directory just added
        Change deleteChange = new Change(Change.TYPE_DELETE_DIR, "dir1");
        changeSet.add(deleteChange);
        
        // Setting up the archive streams
        ArchiveInputStream in = new ArchiveInputStream(new ByteArrayInputStream("".getBytes()));
        ArchiveOutputStream out = new ArchiveOutputStream(new ByteArrayOutputStream());
        
        // Performing the changes
        ChangeSetResults results = performer.perform(in, out);
        
        // Asserting the results
        assertTrue(results.hasBeenDeleted("dir1/"));
    }
    
    @Test
    public void testPerform_EmptyStreams() throws IOException {
        // Setting up empty input and output streams
        ArchiveInputStream in = new ArchiveInputStream(new ByteArrayInputStream("".getBytes()));
        ArchiveOutputStream out = new ArchiveOutputStream(new ByteArrayOutputStream());
        
        // Performing the changes with empty streams
        ChangeSetResults results = performer.perform(in, out);
        
        // Asserting no changes have been made
        assertEquals(0, results.getNumChanges());
    }
    
    @Test
    public void testPerform_AddToChangeSetThenAddFromStream() throws IOException {
        // Adding a change to add a file in the ChangeSet
        Change change = new Change(Change.TYPE_ADD, "input_stream_add_from_change_set", false);
        InputStream input = new ByteArrayInputStream("test data".getBytes());
        change.setInput(input);
        change.setEntry(new ArchiveEntry("file3"));
        changeSet.add(change);
        
        // Setting up the archive streams
        ArchiveInputStream in = new ArchiveInputStream(new ByteArrayInputStream("".getBytes()));
        ArchiveOutputStream out = new ArchiveOutputStream(new ByteArrayOutputStream());
        
        // Performing the changes
        ChangeSetResults results = performer.perform(in, out);
        
        // Asserting the results
        assertTrue(results.hasBeenAdded("file3"));
    }
    
    @Test
    public void testIsDeletedLater_DeletedFile() {
        // Adding a change to delete a file later
        Change change = new Change(Change.TYPE_DELETE, "file4");
        changeSet.add(change);
        
        // Creating sample workingSet
        Set workingSet = new HashSet();
        workingSet.add(change);
        
        // Creating a sample ArchiveEntry
        ArchiveEntry entry = new ArchiveEntry("file4");
        
        // Checking if entry is deleted later
        assertTrue(performer.isDeletedLater(workingSet, entry));
    }
}