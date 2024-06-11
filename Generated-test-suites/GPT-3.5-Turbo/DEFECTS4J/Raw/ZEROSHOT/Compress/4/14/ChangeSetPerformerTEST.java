import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.archivers.ArchiveOutputStream;
import org.apache.commons.compress.changes.Change;
import org.apache.commons.compress.changes.ChangeSet;
import org.apache.commons.compress.changes.ChangeSetPerformer;
import org.apache.commons.compress.changes.ChangeSetResults;

public class ChangeSetPerformerTest {

    private ChangeSetPerformer changeSetPerformer;
    private ChangeSet changeSet;

    @Before
    public void setUp() {
        changeSet = new ChangeSet();
        changeSetPerformer = new ChangeSetPerformer(changeSet);
    }

    @Test
    public void testPerform_AddAndReplace() throws Exception {
        ArchiveEntry entry = new ArchiveEntry();
        entry.setName("test.txt");
        changeSet.addChange(new Change(Change.TYPE_ADD, entry).setReplaceMode(true));

        ArchiveInputStream in = new ArchiveInputStream();
        ArchiveOutputStream out = new ArchiveOutputStream();

        ChangeSetResults results = changeSetPerformer.perform(in, out);

        assertTrue(results.hasBeenAdded("test.txt"));
    }

    @Test
    public void testPerform_Delete() throws Exception {
        ArchiveEntry entry = new ArchiveEntry();
        entry.setName("test.txt");
        changeSet.addChange(new Change(Change.TYPE_DELETE, entry));

        ArchiveInputStream in = new ArchiveInputStream();
        ArchiveOutputStream out = new ArchiveOutputStream();

        ChangeSetResults results = changeSetPerformer.perform(in, out);

        assertTrue(results.isDeleted("test.txt"));
    }

    @Test
    public void testPerform_DeleteDirectory() throws Exception {
        ArchiveEntry entry = new ArchiveEntry();
        entry.setName("test/");
        changeSet.addChange(new Change(Change.TYPE_DELETE_DIR, entry));

        ArchiveInputStream in = new ArchiveInputStream();
        ArchiveOutputStream out = new ArchiveOutputStream();

        ChangeSetResults results = changeSetPerformer.perform(in, out);

        assertTrue(results.isDeleted("test/"));
    }

    @Test
    public void testPerform_CopyStream() throws Exception {
        ArchiveEntry entry = new ArchiveEntry();
        entry.setName("test.txt");

        ArchiveInputStream in = new ArchiveInputStream();
        ArchiveOutputStream out = new ArchiveOutputStream();

        changeSetPerformer.copyStream(in, out, entry);

        // Check if stream is copied successfully
        // Assertion here
    }

    @Test
    public void testIsDeletedLater() throws Exception {
        ArchiveEntry entry = new ArchiveEntry();
        entry.setName("test.txt");

        Set workingSet = new LinkedHashSet();
        workingSet.add(new Change(Change.TYPE_DELETE, entry));

        assertTrue(changeSetPerformer.isDeletedLater(workingSet, entry));
    }
}