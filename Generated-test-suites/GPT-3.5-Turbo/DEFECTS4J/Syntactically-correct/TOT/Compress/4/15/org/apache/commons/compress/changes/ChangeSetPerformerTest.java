import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.archivers.ArchiveOutputStream;
import org.junit.Test;

public class ChangeSetPerformerTest {

    @Test
    public void testPerform_AddReplaceMode() {
        ChangeSet changeSet = new ChangeSet();
        ArchiveEntry entry = new ArchiveEntry();
        ByteArrayInputStream in = new ByteArrayInputStream("test".getBytes());
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        changeSet.addChange(new Change(entry, in, true));
        ChangeSetPerformer performer = new ChangeSetPerformer(changeSet);

        try {
            ArchiveInputStream archiveIn = new ArchiveInputStream();
            ArchiveOutputStream archiveOut = new ArchiveOutputStream();

            ChangeSetResults results = performer.perform(archiveIn, archiveOut);

            assertTrue(results.hasBeenAdded(entry.getName()));
        } catch (IOException e) {
            fail("IOException occured");
        }
    }

    @Test
    public void testPerform_AddNotReplaceMode() {
        ChangeSet changeSet = new ChangeSet();
        ArchiveEntry entry = new ArchiveEntry();
        ByteArrayInputStream in = new ByteArrayInputStream("test".getBytes());
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        changeSet.addChange(new Change(entry, in, false));
        ChangeSetPerformer performer = new ChangeSetPerformer(changeSet);

        try {
            ArchiveInputStream archiveIn = new ArchiveInputStream();
            ArchiveOutputStream archiveOut = new ArchiveOutputStream();

            ChangeSetResults results = performer.perform(archiveIn, archiveOut);

            assertTrue(results.hasBeenAdded(entry.getName()));
        } catch (IOException e) {
            fail("IOException occured");
        }
    }

    @Test
    public void testPerform_DeleteFile() {
        ChangeSet changeSet = new ChangeSet();
        ArchiveEntry entry = new ArchiveEntry();
        ByteArrayInputStream in = new ByteArrayInputStream("test".getBytes());
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        changeSet.addChange(new Change(entry, in, true));
        changeSet.addChange(new Change(Change.TYPE_DELETE, entry.getName()));
        ChangeSetPerformer performer = new ChangeSetPerformer(changeSet);

        try {
            ArchiveInputStream archiveIn = new ArchiveInputStream();
            ArchiveOutputStream archiveOut = new ArchiveOutputStream();

            ChangeSetResults results = performer.perform(archiveIn, archiveOut);

            assertTrue(results.hasBeenDeleted(entry.getName()));
        } catch (IOException e) {
            fail("IOException occured");
        }
    }

    @Test
    public void testPerform_DeleteDirectory() {
        ChangeSet changeSet = new ChangeSet();
        ArchiveEntry entry = new ArchiveEntry();
        ByteArrayInputStream in = new ByteArrayInputStream("test".getBytes());
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        changeSet.addChange(new Change(entry, in, true));
        changeSet.addChange(new Change(Change.TYPE_DELETE_DIR, entry.getName()));
        ChangeSetPerformer performer = new ChangeSetPerformer(changeSet);

        try {
            ArchiveInputStream archiveIn = new ArchiveInputStream();
            ArchiveOutputStream archiveOut = new ArchiveOutputStream();

            ChangeSetResults results = performer.perform(archiveIn, archiveOut);

            assertTrue(results.hasBeenDeleted(entry.getName()));
        } catch (IOException e) {
            fail("IOException occured");
        }
    }

    @Test
    public void testPerform_CopyStream() {
        ChangeSet changeSet = new ChangeSet();
        ArchiveEntry entry = new ArchiveEntry();
        ByteArrayInputStream in = new ByteArrayInputStream("test".getBytes());
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        changeSet.addChange(new Change(entry, in, true));
        ChangeSetPerformer performer = new ChangeSetPerformer(changeSet);

        try {
            ArchiveInputStream archiveIn = new ArchiveInputStream();
            ArchiveOutputStream archiveOut = new ArchiveOutputStream();

            ChangeSetResults results = performer.perform(archiveIn, archiveOut);

            // Check if the stream is copied correctly
            byte[] byteArray = out.toByteArray();
            String content = new String(byteArray);

            assertEquals("test", content);
        } catch (IOException e) {
            fail("IOException occured");
        }
    }

    @Test
    public void testIsDeletedLater_Deleted() {
        ChangeSet changeSet = new ChangeSet();
        ArchiveEntry entry = new ArchiveEntry();
        ByteArrayInputStream in = new ByteArrayInputStream("test".getBytes());
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        changeSet.addChange(new Change(Change.TYPE_DELETE, entry.getName()));
        ChangeSetPerformer performer = new ChangeSetPerformer(changeSet);

        assertTrue(performer.isDeletedLater(changeSet.getChanges(), entry));
    }

    @Test
    public void testIsDeletedLater_DeletedDirectory() {
        ChangeSet changeSet = new ChangeSet();
        ArchiveEntry entry = new ArchiveEntry();
        ByteArrayInputStream in = new ByteArrayInputStream("test".getBytes());
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        changeSet.addChange(new Change(Change.TYPE_DELETE_DIR, entry.getName()));
        ChangeSetPerformer performer = new ChangeSetPerformer(changeSet);

        assertTrue(performer.isDeletedLater(changeSet.getChanges(), entry));
    }

    @Test
    public void testIsDeletedLater_NotDeleted() {
        ChangeSet changeSet = new ChangeSet();
        ArchiveEntry entry = new ArchiveEntry();
        ByteArrayInputStream in = new ByteArrayInputStream("test".getBytes());
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        changeSet.addChange(new Change(Change.TYPE_ADD, entry.getName()));
        ChangeSetPerformer performer = new ChangeSetPerformer(changeSet);

        assertFalse(performer.isDeletedLater(changeSet.getChanges(), entry));
    }

    @Test
    public void testCopyStream() {
        ChangeSet changeSet = new ChangeSet();
        ArchiveEntry entry = new ArchiveEntry();
        ByteArrayInputStream in = new ByteArrayInputStream("test".getBytes());
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        ChangeSetPerformer performer = new ChangeSetPerformer(changeSet);

        try {
            performer.copyStream(in, out, entry);

            // Check if the stream is copied correctly
            byte[] byteArray = out.toByteArray();
            String content = new String(byteArray);

            assertEquals("test", content);
        } catch (IOException e) {
            fail("IOException occured");
        }
    }
}