import static org.junit.Assert.*;
import org.junit.Test;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.archivers.ArchiveOutputStream;

public class ChangeSetPerformerTest {

    @Test
    public void testPerform_AddReplaceMode() throws IOException {
        ChangeSet changeSet = new ChangeSet();
        ChangeSetPerformer performer = new ChangeSetPerformer(changeSet);

        ArchiveInputStream in = null; // initialize input stream
        ArchiveOutputStream out = null; // initialize output stream

        // Add entry with replace mode
        ArchiveEntry entry = null; // initialize entry
        changeSet.addChange(Change.TYPE_ADD, entry, new ByteArrayInputStream(new byte[0]));

        ChangeSetResults results = performer.perform(in, out);

        // Assert that the added entry with replace mode was processed
        assertTrue(results.hasBeenAdded(entry.getName()));
    }

    @Test
    public void testPerform_DeleteEntry() throws IOException {
        ChangeSet changeSet = new ChangeSet();
        ChangeSetPerformer performer = new ChangeSetPerformer(changeSet);

        ArchiveInputStream in = null; // initialize input stream
        ArchiveOutputStream out = null; // initialize output stream

        // Delete entry
        ArchiveEntry entry = null; // initialize entry
        changeSet.addChange(Change.TYPE_DELETE, entry);

        ChangeSetResults results = performer.perform(in, out);

        // Assert that the deleted entry was processed
        assertTrue(results.hasBeenDeleted(entry.getName()));
    }

    @Test
    public void testPerform_DeleteDirEntry() throws IOException {
        ChangeSet changeSet = new ChangeSet();
        ChangeSetPerformer performer = new ChangeSetPerformer(changeSet);

        ArchiveInputStream in = null; // initialize input stream
        ArchiveOutputStream out = null; // initialize output stream

        // Delete directory entry
        ArchiveEntry entry = null; // initialize entry
        changeSet.addChange(Change.TYPE_DELETE_DIR, entry);

        ChangeSetResults results = performer.perform(in, out);

        // Assert that the deleted directory entry was processed
        assertTrue(results.hasBeenDeleted(entry.getName()));
    }

    @Test
    public void testIsDeletedLater_NoDeletionLater() {
        ChangeSet changeSet = new ChangeSet();
        ChangeSetPerformer performer = new ChangeSetPerformer(changeSet);

        Set workingSet = new LinkedHashSet();

        ArchiveEntry entry = null; // initialize entry
        
        boolean result = performer.isDeletedLater(workingSet, entry);

        // Assert that there is no deletion later in the working set
        assertFalse(result);
    }

    @Test
    public void testIsDeletedLater_DeletionLater() {
        ChangeSet changeSet = new ChangeSet();
        ChangeSetPerformer performer = new ChangeSetPerformer(changeSet);

        Set workingSet = new LinkedHashSet();
        
        ArchiveEntry entry = null; // initialize entry
        changeSet.addChange(Change.TYPE_DELETE, entry);

        boolean result = performer.isDeletedLater(workingSet, entry);

        // Assert that there is a deletion for the entry later in the working set
        assertTrue(result);
    }

    @Test
    public void testCopyStream() throws IOException {
        ChangeSetPerformer performer = new ChangeSetPerformer(new ChangeSet());
        
        ByteArrayInputStream in = new ByteArrayInputStream(new byte[0]);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ArchiveEntry entry = null; // initialize entry

        performer.copyStream(in, new ArchiveOutputStream(out), entry);

        // Assert that the stream was successfully copied
        assertNotNull(out.toByteArray());
    }
}