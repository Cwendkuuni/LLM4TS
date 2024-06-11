import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.apache.commons.compress.changes.ChangeSet;
import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.archivers.ArchiveOutputStream;
import org.mockito.Mockito;
import java.io.IOException;
import java.io.InputStream;

public class ChangeSetPerformerTest {
    private ChangeSetPerformer changeSetPerformer;
    private ChangeSet changeSet;

    @Before
    public void setUp() {
        changeSet = Mockito.mock(ChangeSet.class);
        changeSetPerformer = new ChangeSetPerformer(changeSet);
    }

    @Test
    public void testPerform() throws IOException {
        ArchiveInputStream in = Mockito.mock(ArchiveInputStream.class);
        ArchiveOutputStream out = Mockito.mock(ArchiveOutputStream.class);

        ChangeSetResults results = changeSetPerformer.perform(in, out);

        // Verify that necessary methods and assertions are called

        Mockito.verify(changeSet).getChanges();
        Mockito.verify(in).getNextEntry();
        Mockito.verify(out).putArchiveEntry(Mockito.any(ArchiveEntry.class));
        Mockito.verify(out).closeArchiveEntry();

        Mockito.verifyNoMoreInteractions(changeSet, in, out);

        // Perform additional assertions on the results object
      }

    @Test
    public void testIsDeletedLater() {
        // Write test cases to test the isDeletedLater() method
    }

    @Test
    public void testCopyStream() {
        // Write test cases to test the copyStream() method
      }
}