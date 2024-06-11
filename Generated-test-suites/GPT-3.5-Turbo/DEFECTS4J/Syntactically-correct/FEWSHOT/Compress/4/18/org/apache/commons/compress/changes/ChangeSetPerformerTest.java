import static org.junit.Assert.*;
import org.junit.Test;
import org.apache.commons.compress.changes.ChangeSet;
import org.apache.commons.compress.changes.ChangeSetPerformer;
import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.archivers.ArchiveOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.Set;
import java.util.LinkedHashSet;
import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.utils.IOUtils;

public class ChangeSetPerformerTest {
    
    private ChangeSetPerformer performer;
    private ArchiveInputStream in;
    private ArchiveOutputStream out;
    
    @Test
    public void testConstructor() {
        ChangeSet changeSet = new ChangeSet();
        performer = new ChangeSetPerformer(changeSet);
        
        Set changes = changeSet.getChanges();
        Set performerChanges = performer.getChanges();
        
        assertEquals(changes, performerChanges);
    }
    
    @Test
    public void testPerform() throws IOException {
        ChangeSet changeSet = new ChangeSet();
        performer = new ChangeSetPerformer(changeSet);
        in = new ArchiveInputStream();
        out = new ArchiveOutputStream();
        
        ChangeSetResults results = performer.perform(in, out);
        
        assertNotNull(results);
    }
    
    @Test
    public void testIsDeletedLater() {
        ChangeSet changeSet = new ChangeSet();
        performer = new ChangeSetPerformer(changeSet);
        Set workingSet = new LinkedHashSet();
        ArchiveEntry entry = new ArchiveEntry();
        
        boolean deletedLater = performer.isDeletedLater(workingSet, entry);
        
        assertFalse(deletedLater);
    }
    
    @Test
    public void testCopyStream() throws IOException {
        ChangeSet changeSet = new ChangeSet();
        performer = new ChangeSetPerformer(changeSet);
        InputStream inputStream = new InputStream();
        ArchiveOutputStream out = new ArchiveOutputStream();
        ArchiveEntry entry = new ArchiveEntry();
        
        performer.copyStream(inputStream, out, entry);
        
        // Assert something or perform additional checks if needed
    }
}