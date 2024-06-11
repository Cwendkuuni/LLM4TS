import static org.junit.Assert.*;
import org.junit.Test;
import org.mockito.Mockito;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.IOException;
import org.apache.commons.compress.changes.Change;
import org.apache.commons.compress.changes.ChangeSet;
import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.archivers.ArchiveOutputStream;

public class ChangeSetPerformerTest {

    @Test
    public void testPerform() {
        // Create a mock ChangeSet
        ChangeSet changeSet = Mockito.mock(ChangeSet.class);
        Set<Change> changes = new LinkedHashSet<>();
        // Add some changes
        changes.add(new Change(Change.TYPE_ADD, "file1", true));
        changes.add(new Change(Change.TYPE_ADD, "file2", false));
        Mockito.when(changeSet.getChanges()).thenReturn(changes);

        // Create a ChangeSetPerformer
        ChangeSetPerformer performer = new ChangeSetPerformer(changeSet);

        // Mocked InputStream
        InputStream mockedIn = new ByteArrayInputStream(new byte[0]);
        // Mocked ArchiveEntry
        ArchiveEntry mockedEntry = Mockito.mock(ArchiveEntry.class);
        Mockito.when(mockedEntry.getName()).thenReturn("file1");

        // Mocked ArchiveInputStream and ArchiveOutputStream
        ArchiveInputStream mockedInStream = Mockito.mock(ArchiveInputStream.class);
        ArchiveOutputStream mockedOutStream = Mockito.mock(ArchiveOutputStream.class);

        // Perform the method and assert the results
        try {
            ChangeSetResults results = performer.perform(mockedInStream, mockedOutStream);
            assertNotNull(results);
            assertTrue(results.hasBeenAdded("file1"));
            assertFalse(results.hasBeenAdded("file2"));
        } catch (IOException e) {
            fail("IOException should not be thrown during perform");
        }
    }

    @Test
    public void testIsDeletedLater() {
        // Create a mock ChangeSet
        ChangeSet changeSet = Mockito.mock(ChangeSet.class);
        Set<Change> changes = new LinkedHashSet<>();
        // Add a change for deletion
        changes.add(new Change(Change.TYPE_DELETE, "file1", false));
        Mockito.when(changeSet.getChanges()).thenReturn(changes);

        // Create a ChangeSetPerformer
        ChangeSetPerformer performer = new ChangeSetPerformer(changeSet);

        // Mocked ArchiveEntry
        ArchiveEntry mockedEntry = Mockito.mock(ArchiveEntry.class);
        Mockito.when(mockedEntry.getName()).thenReturn("file1");

        // Perform the method and assert the result
        assertTrue(performer.isDeletedLater(changes, mockedEntry));
    }

    @Test
    public void testCopyStream() {
        // Create a ChangeSetPerformer
        ChangeSetPerformer performer = new ChangeSetPerformer(new ChangeSet(new LinkedHashSet<>()));

        // Mocked InputStream and ArchiveEntry
        InputStream mockedIn = new ByteArrayInputStream(new byte[0]);
        ArchiveOutputStream mockedOut = Mockito.mock(ArchiveOutputStream.class);
        ArchiveEntry mockedEntry = Mockito.mock(ArchiveEntry.class);

        // Perform the method and assert no exceptions are thrown
        try {
            performer.copyStream(mockedIn, mockedOut, mockedEntry);
        } catch (IOException e) {
            fail("IOException should not be thrown during copyStream");
        }
    }

}