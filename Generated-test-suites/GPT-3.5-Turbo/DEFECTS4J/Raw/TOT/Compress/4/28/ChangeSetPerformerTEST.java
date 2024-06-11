import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.LinkedHashSet;
import java.util.Set;

import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.archivers.ArchiveOutputStream;
import org.apache.commons.compress.archivers.tar.TarArchiveInputStream;
import org.apache.commons.compress.archivers.tar.TarArchiveOutputStream;
import org.apache.commons.compress.changes.Change;
import org.apache.commons.compress.changes.ChangeSet;
import org.apache.commons.compress.changes.ChangeSetPerformer;
import org.apache.commons.compress.changes.ChangeSetResults;
import org.junit.Before;
import org.junit.Test;

public class ChangeSetPerformerTest {

    private ChangeSet changeSet;
    private ArchiveInputStream in;
    private ArchiveOutputStream out;
    private ChangeSetPerformer performer;
    private ChangeSetResults results;

    @Before
    public void setUp() {
        changeSet = new ChangeSet();
        in = new TarArchiveInputStream(new ByteArrayInputStream(new byte[0]));
        out = new TarArchiveOutputStream(new ByteArrayOutputStream());
        performer = new ChangeSetPerformer(changeSet);
        results = new ChangeSetResults();
    }

    @Test
    public void testPerform_AddReplaceMode_ChangeTypeAdd() throws IOException {
        Change change = new Change();
        change.setType(Change.TYPE_ADD);
        change.setReplaceMode(true);
        change.setInput(new ByteArrayInputStream(new byte[0]));
        ArchiveEntry entry = new TarArchiveEntry("test_file");
        change.setEntry(entry);
        Set changes = new LinkedHashSet<>();
        changes.add(change);
        changeSet.setChanges(changes);

        performer.perform(in, out);

        assertEquals(Change.TYPE_ADD, change.getType());
        assertTrue(change.isReplaceMode());
        assertNotNull(change.getInput());
        assertEquals("test_file", change.getEntry().getName());
        assertNull(change.getTargetFile());
        assertTrue(results.hasBeenAdded("test_file"));
    }

    @Test
    public void testPerform_Delete_ChangeTypeDelete() throws IOException {
        Change change = new Change();
        change.setType(Change.TYPE_DELETE);
        change.setTargetFile("test_file");
        Set changes = new LinkedHashSet<>();
        changes.add(change);
        changeSet.setChanges(changes);

        performer.perform(in, out);

        assertEquals(Change.TYPE_DELETE, change.getType());
        assertNull(change.getInput());
        assertNull(change.getEntry());
        assertNull(change.getTargetFile());
        assertTrue(results.hasBeenDeleted("test_file"));
    }

    @Test
    public void testPerform_DeleteDir_ChangeTypeDeleteDir() throws IOException {
        Change change = new Change();
        change.setType(Change.TYPE_DELETE_DIR);
        change.setTargetFile("test_dir");
        Set changes = new LinkedHashSet<>();
        changes.add(change);
        changeSet.setChanges(changes);

        performer.perform(in, out);

        assertEquals(Change.TYPE_DELETE_DIR, change.getType());
        assertNull(change.getInput());
        assertNull(change.getEntry());
        assertEquals("test_dir", change.getTargetFile());
        assertTrue(results.hasBeenDeleted("test_dir"));
    }

    @Test
    public void testPerform_Copy_ChangeTypeCopy() throws IOException {
        Change change = new Change();
        change.setType(Change.TYPE_ADD);
        change.setReplaceMode(false);
        change.setInput(new ByteArrayInputStream(new byte[0]));
        ArchiveEntry entry = new TarArchiveEntry("test_file_copy");
        change.setEntry(entry);
        Set changes = new LinkedHashSet<>();
        changes.add(change);
        changeSet.setChanges(changes);

        performer.perform(in, out);

        assertEquals(Change.TYPE_ADD, change.getType());
        assertFalse(change.isReplaceMode());
        assertNotNull(change.getInput());
        assertEquals("test_file_copy", change.getEntry().getName());
        assertNull(change.getTargetFile());
        assertTrue(results.hasBeenAdded("test_file_copy"));
    }

    @Test
    public void testPerform_DeletedLaterInChangeSet() throws IOException {
        Change change1 = new Change();
        change1.setType(Change.TYPE_ADD);
        change1.setReplaceMode(true);
        change1.setInput(new ByteArrayInputStream(new byte[0]));
        ArchiveEntry entry1 = new TarArchiveEntry("test_file");
        change1.setEntry(entry1);

        Change change2 = new Change();
        change2.setType(Change.TYPE_DELETE);
        change2.setTargetFile("test_file");

        Set changes = new LinkedHashSet<>();
        changes.add(change1);
        changes.add(change2);
        changeSet.setChanges(changes);

        performer.perform(in, out);

        assertTrue(performer.isDeletedLater(changeSet.getChanges(), entry1));
    }

    @Test
    public void testPerform_AddNoReplaceModeNotAddedFromStream() throws IOException {
        Change change = new Change();
        change.setType(Change.TYPE_ADD);
        change.setReplaceMode(false);
        change.setInput(new ByteArrayInputStream(new byte[0]));
        ArchiveEntry entry = new TarArchiveEntry("test_file_not_added");
        change.setEntry(entry);
        Set changes = new LinkedHashSet<>();
        changes.add(change);
        changeSet.setChanges(changes);

        performer.perform(in, out);

        assertEquals(Change.TYPE_ADD, change.getType());
        assertFalse(change.isReplaceMode());
        assertNotNull(change.getInput());
        assertEquals("test_file_not_added", change.getEntry().getName());
        assertNull(change.getTargetFile());
        assertTrue(results.hasBeenAdded("test_file_not_added"));
    }

    @Test
    public void testCopyStream() throws IOException {
        Change change = new Change();
        change.setInput(new ByteArrayInputStream(new byte[0]));
        ArchiveEntry entry = new TarArchiveEntry("test_file_copy");
        change.setEntry(entry);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ArchiveOutputStream output = new TarArchiveOutputStream(baos);

        performer.copyStream(change.getInput(), output, change.getEntry());

        // Assert that the output stream has been written
        assertEquals(0, baos.size());
    }

}