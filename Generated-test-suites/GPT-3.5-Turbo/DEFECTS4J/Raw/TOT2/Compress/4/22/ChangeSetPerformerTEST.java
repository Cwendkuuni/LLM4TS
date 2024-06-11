import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import java.io.IOException;
import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.archivers.ArchiveOutputStream;

public class ChangeSetPerformerTest {
    private ChangeSet changeSet;
    private ArchiveInputStream in;
    private ArchiveOutputStream out;
    
    @Before
    public void setUp() {
        changeSet = new ChangeSet();
        in = Mockito.mock(ArchiveInputStream.class);
        out = Mockito.mock(ArchiveOutputStream.class);
    }

    @Test
    public void testPerform() throws IOException {
        ChangeSetPerformer performer = new ChangeSetPerformer(changeSet);
        ChangeSetResults results = performer.perform(in, out);
        
        // Add test cases for typical use cases here
        
        // Add test cases for edge cases here
        
        // Add test cases for exception handling scenarios here
    }

    @Test
    public void testIsDeletedLater() {
        ChangeSetPerformer performer = new ChangeSetPerformer(changeSet);
        
        // Add test cases for isDeletedLater method
    }

    @Test
    public void testCopyStream() throws IOException {
        ChangeSetPerformer performer = new ChangeSetPerformer(changeSet);
        InputStream input = Mockito.mock(InputStream.class);
        ArchiveOutputStream archiveOut = Mockito.mock(ArchiveOutputStream.class);
        ArchiveEntry entry = Mockito.mock(ArchiveEntry.class);
        
        performer.copyStream(input, archiveOut, entry);
        
        // Add test cases for copyStream method
    }
}