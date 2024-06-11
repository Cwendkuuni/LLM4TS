import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.archivers.ArchiveOutputStream;
import org.apache.commons.compress.changes.Change;
import org.apache.commons.compress.changes.ChangeSet;
import org.apache.commons.compress.changes.ChangeSetResults;
import org.junit.Before;
import org.junit.Test;

public class ChangeSetPerformerTest {
    
    private ChangeSetPerformer performer;
    private ChangeSetResults results;
    
    @Before
    public void setUp() {
        ChangeSet changeSet = new ChangeSet();
        Set<Change> changes = new HashSet<>();
        changes.add(new Change(Change.TYPE_ADD, "file1", null));
        changes.add(new Change(Change.TYPE_ADD, "file2", null));
        changeSet.setChanges(changes);
        performer = new ChangeSetPerformer(changeSet);
    }
    
    @Test
    public void testPerform() throws IOException {
        InputStream in = new ByteArrayInputStream("test input".getBytes());
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ArchiveInputStream archiveIn = null;
        ArchiveOutputStream archiveOut = null;
        try {
            // Create ArchiveInputStream and ArchiveOutputStream if needed
            archiveIn = null;
            archiveOut = null;
            
            results = performer.perform(archiveIn, archiveOut);
        } finally {
            if (archiveIn != null) {
                archiveIn.close();
            }
            if (archiveOut != null) {
                archiveOut.close();
            }
        }
        
        // Assert the results
        assertEquals(2, results.getAddedFromChangeSet().size());
        assertTrue(results.getAddedFromChangeSet().contains("file1"));
        assertTrue(results.getAddedFromChangeSet().contains("file2"));
    }
    
    @Test
    public void testIsDeletedLater() {
        Set<Change> workingSet = new HashSet<>();
        workingSet.add(new Change(Change.TYPE_DELETE, "file3", null));
        workingSet.add(new Change(Change.TYPE_ADD, "file4", null));
        
        ArchiveEntry entry = new ArchiveEntry() {
            public String getName() { return "file4"; }
            // Other methods of ArchiveEntry
        };
        
        boolean deletedLater = performer.isDeletedLater(workingSet, entry);
        assertTrue(deletedLater);
    }
    
    @Test
    public void testCopyStream() throws IOException {
        InputStream in = new ByteArrayInputStream("test input".getBytes());
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ArchiveEntry entry = new ArchiveEntry() {
            public String getName() { return "file"; }
            // Other methods of ArchiveEntry
        };
        performer.copyStream(in, out, entry);
        // Assert the copied data
        assertEquals("test input", out.toString());
    }
}