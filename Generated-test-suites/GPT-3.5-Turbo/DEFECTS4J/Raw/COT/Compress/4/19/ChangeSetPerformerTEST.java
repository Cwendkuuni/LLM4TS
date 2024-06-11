// Generated test cases for class ChangeSetPerformer

import static org.junit.Assert.*;
import org.junit.Test;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;

import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.archivers.ArchiveOutputStream;
import org.apache.commons.compress.utils.IOUtils;

public class ChangeSetPerformerTest {

    @Test
    public void testPerform() throws IOException {
        // Create a ChangeSet with changes
        ChangeSet changeSet = new ChangeSet();
        changeSet.addChange(new Change(Change.TYPE_ADD, "file1.txt", null, null));
        changeSet.addChange(new Change(Change.TYPE_DELETE, "file2.txt", null, null));
        
        // Create an input stream with some test data
        ByteArrayInputStream in = new ByteArrayInputStream("test data".getBytes());
        
        // Create an output stream to store the modified data
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        
        // Create a ChangeSetPerformer and perform the changes
        ChangeSetPerformer performer = new ChangeSetPerformer(changeSet);
        ChangeSetResults results = performer.perform(in, out);
        
        // Assert that the changes were performed correctly
        assertTrue(results.hasBeenAdded("file1.txt"));
        assertTrue(results.hasBeenDeleted("file2.txt"));
        assertEquals("test data", out.toString());
    }

    @Test
    public void testIsDeletedLater() {
        // Create a set of changes
        LinkedHashSet<Change> changes = new LinkedHashSet<>();
        changes.add(new Change(Change.TYPE_DELETE, "file1.txt", null, null));
        changes.add(new Change(Change.TYPE_ADD, "file2.txt", null, null));
        
        // Create a ChangeSetPerformer and call the private method
        ChangeSetPerformer performer = new ChangeSetPerformer(new ChangeSet());
        boolean result = performer.isDeletedLater(changes, new ArchiveEntry("file1.txt"));
        
        // Assert that the method correctly detects that the entry is deleted later
        assertTrue(result);
    }

    @Test
    public void testCopyStream() throws IOException {
        // Create a test input stream with some data
        ByteArrayInputStream in = new ByteArrayInputStream("test data".getBytes());
        
        // Create a test output stream
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        
        // Create a mock ArchiveEntry
        ArchiveEntry entry = new ArchiveEntry();
        
        // Create a ChangeSetPerformer and call the private method
        ChangeSetPerformer performer = new ChangeSetPerformer(new ChangeSet());
        performer.copyStream(in, out, entry);
        
        // Assert that the data was copied correctly
        assertEquals("test data", out.toString());
    }
}