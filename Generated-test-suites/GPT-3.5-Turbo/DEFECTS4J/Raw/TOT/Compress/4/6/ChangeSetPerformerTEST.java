import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import java.io.IOException;
import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.archivers.ArchiveOutputStream;

public class ChangeSetPerformerTest {

    private ChangeSetPerformer performer;
    private ArchiveInputStream in;
    private ArchiveOutputStream out;

    @Before
    public void setUp() {
        // Create a sample ChangeSet with some changes
        ChangeSet changeSet = new ChangeSet();
        changeSet.addChange(new Change(Change.TYPE_ADD, "/file1", "file1Content"));
        changeSet.addChange(new Change(Change.TYPE_DELETE, "/file2", null));

        // Initialize performer with the sample ChangeSet
        performer = new ChangeSetPerformer(changeSet);
    }

    @After
    public void tearDown() {
        // Clean up resources
        if (in != null) {
            try {
                in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (out != null) {
            try {
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Test
    public void testPerform_AddReplaceMode()
        throws IOException {
        // Create a sample ArchiveEntry
        ArchiveEntry entry = new ArchiveEntry("/testFile", 100);

        // Create a mock InputStream for the copyStream method
        InputStream in = new ByteArrayInputStream("testContent".getBytes());
        
        // Create a mock ArchiveOutputStream for the copyStream method
        ArchiveOutputStream out = Mockito.mock(ArchiveOutputStream.class);
        
        // Perform the change operation
        performer.perform(this.in, this.out);

        // Verify that the copyStream method is called with the correct parameters
        Mockito.verify(this.out).putArchiveEntry(entry);
        Mockito.verify(in).close();
        Mockito.verify(this.out).closeArchiveEntry();
    }

    @Test
    public void testPerform_Delete()
        throws IOException {
        // Create a sample ArchiveEntry
        ArchiveEntry entry = new ArchiveEntry("/file2", 200);

        // Create a mock ArchiveInputStream for the getNextEntry method
        ArchiveInputStream in = Mockito.mock(ArchiveInputStream.class);
        Mockito.when(in.getNextEntry()).thenReturn(entry).thenReturn(null);

        // Create a mock ArchiveOutputStream for the copyStream method
        ArchiveOutputStream out = Mockito.mock(ArchiveOutputStream.class);

        // Perform the change operation
        performer.perform(this.in, this.out);

        // Verify that the delete operation is performed and the entry is not copied to the output stream
        Mockito.verifyZeroInteractions(this.out);
        Mockito.verify(this.out, Mockito.never()).putArchiveEntry(entry);
        Mockito.verify(this.out, Mockito.never()).closeArchiveEntry();
    }

    @Test
    public void testPerform_DeleteDir()
        throws IOException {
        // Create a sample ArchiveEntry for the directory
        ArchiveEntry entry = new ArchiveEntry("/directory", true);

        // Create a mock ArchiveInputStream for the getNextEntry method
        ArchiveInputStream in = Mockito.mock(ArchiveInputStream.class);
        Mockito.when(in.getNextEntry()).thenReturn(entry).thenReturn(null);

        // Create a mock ArchiveOutputStream for the copyStream method
        ArchiveOutputStream out = Mockito.mock(ArchiveOutputStream.class);

        // Perform the change operation
        performer.perform(this.in, this.out);

        // Verify that the delete operation is performed for all entries inside the directory 
        // and the entry and its children are not copied to the output stream
        Mockito.verifyZeroInteractions(this.out);
        Mockito.verify(this.out, Mockito.never()).putArchiveEntry(entry);
        Mockito.verify(this.out, Mockito.never()).closeArchiveEntry();
    }
}