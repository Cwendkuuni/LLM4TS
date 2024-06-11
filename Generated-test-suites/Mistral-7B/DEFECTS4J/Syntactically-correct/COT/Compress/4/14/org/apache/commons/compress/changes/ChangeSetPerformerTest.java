package org.apache.commons.compress.changes;

import static org.junit.Assert.*;

import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.archivers.ArchiveOutputStream;
import org.apache.commons.compress.utils.IOUtils;
import org.apache.commons.compress.utils.TestUtils;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;

public class ChangeSetPerformerTest {

    @Test
    public void testConstructor() {
        ChangeSet changeSet = new ChangeSet();
        ChangeSetPerformer performer = new ChangeSetPerformer(changeSet);
        assertNotNull(performer);
    }

    @Test
    public void testPerform_NoChanges() throws IOException {
        ChangeSet changeSet = new ChangeSet();
        ArchiveInputStream input = TestUtils.createTestArchive();
        ArchiveOutputStream output = new ArchiveOutputStream("temp.tar.gz");
        ChangeSetPerformer performer = new ChangeSetPerformer(changeSet);
        ChangeSetResults result = performer.perform(input, output);
        assertFalse(result.hasAnyChanges());
    }

    @Test
    public void testPerform_SimpleAdd() throws IOException {
        ChangeSet changeSet = new ChangeSet();
        ArchiveInputStream input = TestUtils.createTestArchive();
        ArchiveOutputStream output = new ArchiveOutputStream("temp.tar.gz");

        Change change = new Change(Change.TYPE_ADD, "test/SimpleAdd.txt", null, null);
        changeSet.addChange(change);

        ChangeSetPerformer performer = new ChangeSetPerformer(changeSet);
        ChangeSetResults result = performer.perform(input, output);

        assertTrue(result.hasChanges());
        ArchiveEntry entry = output.getNextArchiveEntry();
        assertEquals("test/SimpleAdd.txt", entry.getName());
    }

    @Test
    public void testPerform_SimpleDelete() throws IOException {
        ChangeSet changeSet = new ChangeSet();
        ArchiveInputStream input = TestUtils.createTestArchive();
        ArchiveOutputStream output = new ArchiveOutputStream("temp.tar.gz");

        Change change = new Change(Change.TYPE_DELETE, "test/SimpleAdd.txt", null, null);
        changeSet.addChange(change);

        ChangeSetPerformer performer = new ChangeSetPerformer(changeSet);
        ChangeSetResults result = performer.perform(input, output);

        assertTrue(result.hasChanges());
        assertNull(output.getNextArchiveEntry());
    }

    @Test
    public void testPerform_SimpleReplace() throws IOException {
        ChangeSet changeSet = new ChangeSet();
        ArchiveInputStream input = TestUtils.createTestArchive();
        ArchiveOutputStream output = new ArchiveOutputStream("temp.tar.gz");

        Change change = new Change(Change.TYPE_ADD, "test/SimpleAdd.txt", "test/SimpleReplace.txt", null);
        changeSet.addChange(change);

        ChangeSetPerformer performer = new ChangeSetPerformer(changeSet);
        ChangeSetResults result = performer.perform(input, output);

        assertTrue(result.hasChanges());
        ArchiveEntry entry = output.getNextArchiveEntry();
        assertEquals("test/SimpleReplace.txt", entry.getName());
    }

    @Test
    public void testPerform_MultipleChanges() throws IOException {
        ChangeSet changeSet = new ChangeSet();

        Change add1 = new Change(Change.TYPE_ADD, "test/SimpleAdd1.txt", null, null);
        Change add2 = new Change(Change.TYPE_ADD, "test/SimpleAdd2.txt", null, null);
        Change delete = new Change(Change.TYPE_DELETE, "test/SimpleAdd1.txt", null, null);
        Change replace = new Change(Change.TYPE_ADD, "test/SimpleReplace.txt", "test/SimpleAdd1.txt", null);

        changeSet.addChange(add1);
        changeSet.addChange(add2);
        changeSet.addChange(delete);
        changeSet.addChange(replace);

        ArchiveInputStream input = TestUtils.createTestArchive();
        ArchiveOutputStream output = new ArchiveOutputStream("temp.tar.gz");

        ChangeSetPerformer performer = new ChangeSetPerformer(changeSet);
        ChangeSetResults result = performer.perform(input, output);

        assertTrue(result.hasChanges());
        ArchiveEntry entry = output.getNextArchiveEntry();
        assertEquals("test/SimpleReplace.txt", entry.getName());
        assertNull(output.getNextArchiveEntry());

        input.close();
        output.close();

        changeSet.addChange(new Change(Change.TYPE_DELETE, "test/SimpleReplace.txt", null, null));

        performer = new ChangeSetPerformer(changeSet);
        result = performer.perform(input, output);

        assertTrue(result.hasChanges());
        assertNull(output.getNextArchiveEntry());
    }

    @Test(expected = IllegalStateException.class)
    public void testPerform_AfterFinished() throws IOException {
        ChangeSet changeSet = new ChangeSet();
        ArchiveInputStream input = TestUtils.createTestArchive();
        ArchiveOutputStream output = new ArchiveOutputStream("temp.tar.gz");

        ChangeSetPerformer performer = new ChangeSetPerformer(changeSet);
        performer.perform(input, output);
        performer.perform(input, output);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testPerform_NullChangeSet() throws IOException {
        ArchiveInputStream input = TestUtils.createTestArchive();
        ArchiveOutputStream output = new ArchiveOutputStream("temp.tar.gz");

        ChangeSetPerformer performer = new ChangeSetPerformer(null);
        performer.perform(input, output);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testPerform_NullChange() throws IOException {
        ChangeSet changeSet = new ChangeSet();
        ArchiveInputStream input = TestUtils.createTestArchive();
        ArchiveOutputStream output = new ArchiveOutputStream("temp.tar.gz");

        ChangeSetPerformer performer = new ChangeSetPerformer(changeSet);
        performer.perform(input, output, null);
    }

    @Test(expected = IOException.class)
    public void testPerform_NullInput() throws IOException {
        ChangeSet changeSet = new ChangeSet();
        ArchiveOutputStream output = new ArchiveOutputStream("temp.tar.gz");

        ChangeSetPerformer performer = new ChangeSetPerformer(changeSet);
        performer.perform(null, output);
    }

    @Test(expected = IOException.class)
    public void testPerform_NullOutput() throws IOException {
        ChangeSet changeSet = new ChangeSet();
        ArchiveInputStream input = TestUtils.createTestArchive();

        ChangeSetPerformer performer = new ChangeSetPerformer(changeSet);
        performer.perform(input, null);
    }

    @Test
    public void testCopyStream() throws IOException {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        ByteArrayInputStream input = new ByteArrayInputStream("Hello World".getBytes());
        ArchiveEntry entry = new ArchiveEntry("test.txt");

        ChangeSetPerformer.copyStream(input, output, entry);

        assertArrayEquals(
                "Hello World".getBytes(),
                output.toByteArray());
    }

    @Test
    public void testIsDeletedLater_NoLaterDeletions() {
        Set<Change> changes = new LinkedHashSet<>();
        changes.add(new Change(Change.TYPE_ADD, "test.txt", null, null));

        assertFalse(ChangeSetPerformer.isDeletedLater(changes, null));
    }

    @Test
    public void testIsDeletedLater_LaterDeletion() {
        Set<Change> changes = new LinkedHashSet<>();
        changes.add(new Change(Change.TYPE_ADD, "test.txt", null, null));
        changes.add(new Change(Change.TYPE_DELETE, "test.txt", null, null));

        assertTrue(ChangeSetPerformer.isDeletedLater(changes, null));
    }

    @Test
    public void testIsDeletedLater_LaterDirectoryDeletion() {
        Set<Change> changes = new LinkedHashSet<>();
        changes.add(new Change(Change.TYPE_ADD, "test/subdir/file.txt", null, null));
        changes.add(new Change(Change.TYPE_DELETE_DIR, "test/subdir", null, null));

        assertTrue(ChangeSetPerformer.isDeletedLater(changes, new ArchiveEntry("test/subdir/file.txt")));
    }

    @Test
    public void testIsDeletedLater_NoLaterDeletion() {
        Set<Change> changes = new LinkedHashSet<>();
        changes.add(new Change(Change.TYPE_ADD, "test/subdir/file.txt", null, null));
        changes.add(new Change(Change.TYPE_ADD, "test/subdir2/file.txt", null, null));

        assertFalse(ChangeSetPerformer.isDeletedLater(changes, new ArchiveEntry("test/subdir/file.txt")));
    }

    @Test
    public void testIsDeletedLater_EmptySet() {
        Set<Change> changes = Collections.emptySet();

        assertFalse(ChangeSetPerformer.isDeletedLater(changes, null));
    }
}