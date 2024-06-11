import static org.junit.Assert.*;
import org.junit.Test;
import org.junit.Before;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;

public class ChangeSetPerformerTest {
    
    private ChangeSetPerformer changeSetPerformer;
    
    @Before
    public void setUp() {
        ChangeSet changeSet = new ChangeSet();
        changeSet.addChange(createAddChange());
        changeSetPerformer = new ChangeSetPerformer(changeSet);
    }
    
    @Test
    public void perform_AddChangeAndCopyStream_Success() {
        ArchiveInputStream in = createMockArchiveInputStream();
        ArchiveOutputStream out = createMockArchiveOutputStream();
        ChangeSetResults results = changeSetPerformer.perform(in, out);
        
        assertEquals(1, results.getAddedFromChangeSet().size());
    }
    
    @Test
    public void perform_DeleteChangeAndCopyStream_Success() {
        ChangeSet changeSet = new ChangeSet();
        changeSet.addChange(createDeleteChange());
        ChangeSetPerformer changeSetPerformer = new ChangeSetPerformer(changeSet);
        
        ArchiveInputStream in = createMockArchiveInputStream();
        ArchiveOutputStream out = createMockArchiveOutputStream();
        ChangeSetResults results = changeSetPerformer.perform(in, out);
        
        assertEquals(1, results.getDeleted().size());
    }
    
    @Test
    public void perform_AddChangeNoReplaceMode_CopySuccessful() {
        ChangeSet changeSet = new ChangeSet();
        changeSet.addChange(createAddChangeNoReplaceMode());
        ChangeSetPerformer changeSetPerformer = new ChangeSetPerformer(changeSet);
        
        ArchiveInputStream in = createMockArchiveInputStream();
        ArchiveOutputStream out = createMockArchiveOutputStream();
        ChangeSetResults results = changeSetPerformer.perform(in, out);
        
        assertEquals(1, results.getAddedFromChangeSet().size());
    }
    
    @Test
    public void isDeletedLater_EntryAlreadyDeleted_True() {
        Set workingSet = new LinkedHashSet<>();
        ArchiveEntry entry = createMockArchiveEntry("file.txt");
        workingSet.add(createDeleteChange("file.txt"));
        
        assertTrue(changeSetPerformer.isDeletedLater(workingSet, entry));
    }
    
    @Test
    public void copyStream_Success() {
        InputStream in = new ByteArrayInputStream("test data".getBytes());
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        
        ArchiveEntry entry = createMockArchiveEntry("test.txt");
        try {
            changeSetPerformer.copyStream(in, out, entry);
        } catch (IOException e) {
            fail("Exception thrown while copying stream");
        }
        
        assertEquals(9, out.toByteArray().length);
    }
    
    // Helper methods
    private Change createAddChange() {
        return new Change(Change.TYPE_ADD, true);
    }
    
    private Change createDeleteChange() {
        return new Change(Change.TYPE_DELETE, "file.txt");
    }
    
    private Change createAddChangeNoReplaceMode() {
        return new Change(Change.TYPE_ADD, false);
    }
    
    private Change createDeleteChange(String target) {
        return new Change(Change.TYPE_DELETE, target);
    }
    
    private ArchiveInputStream createMockArchiveInputStream() {
        // Mock implementation
        return null;
    }
    
    private ArchiveOutputStream createMockArchiveOutputStream() {
        // Mock implementation
        return null;
    }
    
    private ArchiveEntry createMockArchiveEntry(String name) {
        // Mock implementation
        return null;
    }
}