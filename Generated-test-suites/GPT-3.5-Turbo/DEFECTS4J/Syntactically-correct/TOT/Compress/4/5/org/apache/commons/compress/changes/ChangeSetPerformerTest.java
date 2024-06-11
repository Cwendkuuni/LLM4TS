import org.junit.Test;
import static org.junit.Assert.*;
import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.archivers.ArchiveOutputStream;
import org.apache.commons.compress.changes.Change;
import org.apache.commons.compress.changes.ChangeSet;
import org.apache.commons.compress.test.TestUtils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class ChangeSetPerformerTest {

    @Test
    public void testPerform_AddAndReplaceMode_AddedAndReplaced() throws IOException {
        // Prepare input streams
        ByteArrayInputStream in = new ByteArrayInputStream("Original content".getBytes());
        ByteArrayInputStream changeInput = new ByteArrayInputStream("New content".getBytes());

        // Prepare output stream
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        // Prepare change set
        ChangeSet changeSet = new ChangeSet();
        ArchiveEntry entry = TestUtils.createEntry("file.txt", "New content".getBytes());
        Change change = new Change(Change.TYPE_ADD, entry, changeInput, true);
        changeSet.addChange(change);

        // Perform changes
        ArchiveInputStream archiveInputStream = TestUtils.createArchiveInputStream(in);
        ArchiveOutputStream archiveOutputStream = TestUtils.createArchiveOutputStream(out);
        ChangeSetPerformer changeSetPerformer = new ChangeSetPerformer(changeSet);
        changeSetPerformer.perform(archiveInputStream, archiveOutputStream);

        // Verify the result
        assertEquals("New content", out.toString());
    }

    @Test
    public void testPerform_AddAndNotReplaceMode_Added() throws IOException {
        // Prepare input streams
        ByteArrayInputStream in = new ByteArrayInputStream("Original content".getBytes());
        ByteArrayInputStream changeInput = new ByteArrayInputStream("New content".getBytes());

        // Prepare output stream
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        // Prepare change set
        ChangeSet changeSet = new ChangeSet();
        ArchiveEntry entry = TestUtils.createEntry("file.txt", "New content".getBytes());
        Change change = new Change(Change.TYPE_ADD, entry, changeInput, false);
        changeSet.addChange(change);

        // Perform changes
        ArchiveInputStream archiveInputStream = TestUtils.createArchiveInputStream(in);
        ArchiveOutputStream archiveOutputStream = TestUtils.createArchiveOutputStream(out);
        ChangeSetPerformer changeSetPerformer = new ChangeSetPerformer(changeSet);
        changeSetPerformer.perform(archiveInputStream, archiveOutputStream);

        // Verify the result
        assertEquals("New content", out.toString());
    }

    @Test
    public void testPerform_DeleteFile_Deleted() throws IOException {
        // Prepare input streams
        ByteArrayInputStream in = new ByteArrayInputStream("Original content".getBytes());

        // Prepare output stream
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        // Prepare change set
        ChangeSet changeSet = new ChangeSet();
        Change change = new Change(Change.TYPE_DELETE, "file.txt");
        changeSet.addChange(change);

        // Perform changes
        ArchiveInputStream archiveInputStream = TestUtils.createArchiveInputStream(in);
        ArchiveOutputStream archiveOutputStream = TestUtils.createArchiveOutputStream(out);
        ChangeSetPerformer changeSetPerformer = new ChangeSetPerformer(changeSet);
        changeSetPerformer.perform(archiveInputStream, archiveOutputStream);

        // Verify the result
        assertEquals("", out.toString());
    }

    @Test
    public void testPerform_DeleteDirectory_Deleted() throws IOException {
        // Prepare input streams
        ByteArrayInputStream in = new ByteArrayInputStream("Original content".getBytes());

        // Prepare output stream
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        // Prepare change set
        ChangeSet changeSet = new ChangeSet();
        Change change = new Change(Change.TYPE_DELETE_DIR, "dir");
        changeSet.addChange(change);

        // Perform changes
        ArchiveInputStream archiveInputStream = TestUtils.createArchiveInputStream(in);
        ArchiveOutputStream archiveOutputStream = TestUtils.createArchiveOutputStream(out);
        ChangeSetPerformer changeSetPerformer = new ChangeSetPerformer(changeSet);
        changeSetPerformer.perform(archiveInputStream, archiveOutputStream);

        // Verify the result
        assertEquals("", out.toString());
    }

    @Test
    public void testPerform_DeleteFileDeletedLater_NotDeleted() throws IOException {
        // Prepare input streams
        ByteArrayInputStream in = new ByteArrayInputStream("Original content".getBytes());

        // Prepare output stream
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        // Prepare change set
        ChangeSet changeSet = new ChangeSet();
        Change change1 = new Change(Change.TYPE_ADD, TestUtils.createEntry("file.txt", "New content".getBytes()), new ByteArrayInputStream("New content".getBytes()), true);
        Change change2 = new Change(Change.TYPE_DELETE, "file.txt");
        changeSet.addChange(change1);
        changeSet.addChange(change2);

        // Perform changes
        ArchiveInputStream archiveInputStream = TestUtils.createArchiveInputStream(in);
        ArchiveOutputStream archiveOutputStream = TestUtils.createArchiveOutputStream(out);
        ChangeSetPerformer changeSetPerformer = new ChangeSetPerformer(changeSet);
        changeSetPerformer.perform(archiveInputStream, archiveOutputStream);

        // Verify the result
        assertEquals("New content", out.toString());
    }

    @Test
    public void testPerform_AddFileDeletedLater_NotAdded() throws IOException {
        // Prepare input streams
        ByteArrayInputStream in = new ByteArrayInputStream("Original content".getBytes());

        // Prepare output stream
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        // Prepare change set
        ChangeSet changeSet = new ChangeSet();
        Change change1 = new Change(Change.TYPE_ADD, TestUtils.createEntry("file.txt", "New content".getBytes()), new ByteArrayInputStream("New content".getBytes()), true);
        Change change2 = new Change(Change.TYPE_DELETE, "file.txt");
        changeSet.addChange(change2);
        changeSet.addChange(change1);

        // Perform changes
        ArchiveInputStream archiveInputStream = TestUtils.createArchiveInputStream(in);
        ArchiveOutputStream archiveOutputStream = TestUtils.createArchiveOutputStream(out);
        ChangeSetPerformer changeSetPerformer = new ChangeSetPerformer(changeSet);
        changeSetPerformer.perform(archiveInputStream, archiveOutputStream);

        // Verify the result
        assertEquals("", out.toString());
    }

    @Test
    public void testPerform_AddFileDeletedDirectory_NotAdded() throws IOException {
        // Prepare input streams
        ByteArrayInputStream in = new ByteArrayInputStream("Original content".getBytes());

        // Prepare output stream
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        // Prepare change set
        ChangeSet changeSet = new ChangeSet();
        Change change1 = new Change(Change.TYPE_ADD, TestUtils.createEntry("dir/file.txt", "New content".getBytes()), new ByteArrayInputStream("New content".getBytes()), true);
        Change change2 = new Change(Change.TYPE_DELETE_DIR, "dir");
        changeSet.addChange(change2);
        changeSet.addChange(change1);

        // Perform changes
        ArchiveInputStream archiveInputStream = TestUtils.createArchiveInputStream(in);
        ArchiveOutputStream archiveOutputStream = TestUtils.createArchiveOutputStream(out);
        ChangeSetPerformer changeSetPerformer = new ChangeSetPerformer(changeSet);
        changeSetPerformer.perform(archiveInputStream, archiveOutputStream);

        // Verify the result
        assertEquals("", out.toString());
    }

    @Test
    public void testPerform_AddFileDeletedLater_AddedOnce() throws IOException {
        // Prepare input streams
        ByteArrayInputStream in = new ByteArrayInputStream("Original content".getBytes());

        // Prepare output stream
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        // Prepare change set
        ChangeSet changeSet = new ChangeSet();
        Change change1 = new Change(Change.TYPE_ADD, TestUtils.createEntry("file.txt", "New content".getBytes()), new ByteArrayInputStream("New content".getBytes()), true);
        Change change2 = new Change(Change.TYPE_DELETE, "file.txt");
        Change change3 = new Change(Change.TYPE_ADD, TestUtils.createEntry("file.txt", "Another new content".getBytes()), new ByteArrayInputStream("Another new content".getBytes()), true);
        changeSet.addChange(change1);
        changeSet.addChange(change2);
        changeSet.addChange(change3);

        // Perform changes
        ArchiveInputStream archiveInputStream = TestUtils.createArchiveInputStream(in);
        ArchiveOutputStream archiveOutputStream = TestUtils.createArchiveOutputStream(out);
        ChangeSetPerformer changeSetPerformer = new ChangeSetPerformer(changeSet);
        changeSetPerformer.perform(archiveInputStream, archiveOutputStream);

        // Verify the result
        assertEquals("Another new content", out.toString());
    }
}