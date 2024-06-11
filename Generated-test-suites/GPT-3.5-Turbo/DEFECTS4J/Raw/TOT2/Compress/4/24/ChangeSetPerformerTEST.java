import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.LinkedHashSet;
import java.util.Set;

import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.archivers.ArchiveOutputStream;
import org.junit.Test;

public class ChangeSetPerformerTest {

    @Test
    public void testPerform() throws IOException {
        // Create a sample ChangeSet
        ChangeSet changeSet = new ChangeSet();
        
        // Add some changes to the ChangeSet
        Change change1 = new Change(Change.TYPE_ADD, true);
        changeSet.addChange(change1);
        
        // Mocking ArchiveInputStream
        ArchiveInputStream in = mock(ArchiveInputStream.class);
        
        // Mocking ArchiveOutputStream
        ArchiveOutputStream out = mock(ArchiveOutputStream.class);
        
        // Create a ChangeSetPerformer with the created ChangeSet
        ChangeSetPerformer performer = new ChangeSetPerformer(changeSet);
        
        // Perform the changes and get the results
        ChangeSetResults results = performer.perform(in, out);
        
        // Verify that the added entry was processed correctly
        verify(out).putArchiveEntry(change1.getEntry());
    }
    
    // Add more test methods for other public methods in the ChangeSetPerformer class
    
    @Test
    public void testIsDeletedLater() {
        ChangeSet changeSet = new ChangeSet();
        Set changes = new LinkedHashSet();
        
        // Add a delete change to the ChangeSet
        Change deleteChange = new Change(Change.TYPE_DELETE, false);
        
        // Add entry to deleteChange
        ArchiveEntry entry = mock(ArchiveEntry.class);
        when(entry.getName()).thenReturn("testFile.txt");
        
        deleteChange.setEntry(entry);
        changeSet.addChange(deleteChange);
        changes.add(deleteChange);
        
        // Create a ChangeSetPerformer with the created ChangeSet
        ChangeSetPerformer performer = new ChangeSetPerformer(changeSet);
        
        // Test if the entry is deleted later
        boolean isDeleted = performer.isDeletedLater(changes, entry);
        
        assertEquals(true, isDeleted);
    }
    
    @Test
    public void testCopyStream() throws IOException {
        // Create a sample InputStream
        ByteArrayInputStream in = new ByteArrayInputStream("test data".getBytes());
        
        // Create a mock ArchiveOutputStream
        ArchiveOutputStream out = mock(ArchiveOutputStream.class);
        
        // Create a sample ArchiveEntry
        ArchiveEntry entry = mock(ArchiveEntry.class);
        
        // Create a ChangeSetPerformer
        ChangeSetPerformer performer = new ChangeSetPerformer(new ChangeSet());
        
        // Test copying stream
        performer.copyStream(in, out, entry);
        
        // Verify that the stream was copied correctly
        verify(out).putArchiveEntry(entry);
    }
}