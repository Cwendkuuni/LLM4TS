package org.apache.commons.compress.changes;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.Set;

import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.archivers.ArchiveOutputStream;
import org.apache.commons.compress.utils.IOUtils;
import org.apache.commons.compress.utils.TestUtils;
import org.junit.Test;

public class ChangeSetPerformerTest {

    @Test
    public void testNewInstance() {
        ChangeSet changeSet = mock(ChangeSet.class);
        ChangeSetPerformer performer = new ChangeSetPerformer(changeSet);
        assertEquals(changeSet, performer.getChangeSet());
    }

    @Test
    public void testIsThreadSafe() {
        // Create test data
        ChangeSet changeSet = mock(ChangeSet.class);
        Set changes = new LinkedHashSet();

        // Create multiple Performers and perform operations simultaneously
        ChangeSetPerformer performer1 = new ChangeSetPerformer(changeSet);
        ChangeSetPerformer performer2 = new ChangeSetPerformer(changeSet);

        // Perform operations on both performers
        perform(performer1, createInputStream(), createOutputStream());
        perform(performer2, createInputStream(), createOutputStream());

        // Verify that both performers have performed the same operations
        Set workingSet1 = performer1.getChanges();
        Set workingSet2 = performer2.getChanges();
        assertEquals(changes, workingSet1);
        assertEquals(changes, workingSet2);
    }

    @Test
    public void testPerform_noChanges() {
        ChangeSet changeSet = mock(ChangeSet.class);
        ChangeSetPerformer performer = new ChangeSetPerformer(changeSet);

        ArchiveInputStream in = mockArchiveInputStream();
        ArchiveOutputStream out = mockArchiveOutputStream();

        perform(performer, in, out);

        // Verify no entries were added or deleted
        assertArrayEquals(new byte[0][], performer.getAddedEntries().toArray());
        assertArrayEquals(new byte[0][], performer.getDeletedEntries().toArray());
    }

    @Test
    public void testPerform_singleChange_addFile() {
        ChangeSet changeSet = createChangeSet("add", "test.txt");
        ChangeSetPerformer performer = new ChangeSetPerformer(changeSet);

        ArchiveInputStream in = mockArchiveInputStream();
        ArchiveOutputStream out = mockArchiveOutputStream();

        perform(performer, in, out);

        // Verify entry was added
        byte[] expected = TestUtils.createTestFile("test.txt");
        ArchiveEntry addedEntry = performer.getAddedEntries().iterator().next();
        assertSame(addedEntry.getName(), "test.txt");
        assertArrayEquals(expected, IOUtils.toByteArray(addedEntry.getInputStream()));
    }

    @Test
    public void testPerform_singleChange_deleteFile() {
        ChangeSet changeSet = createChangeSet("delete", "test.txt");
        ChangeSetPerformer performer = new ChangeSetPerformer(changeSet);

        ArchiveInputStream in = createArchiveInputStream(new byte[0]);
        ArchiveOutputStream out = mockArchiveOutputStream();

        perform(performer, in, out);

        // Verify entry was deleted
        ArchiveEntry deletedEntry = performer.getDeletedEntries().iterator().next();
        assertEquals("test.txt", deletedEntry.getName());
    }

    @Test
    public void testPerform_multipleChanges_addFileAndDeleteFile() {
        ChangeSet changeSet = createChangeSet("add", "test1.txt", "delete", "test2.txt");
        ChangeSetPerformer performer = new ChangeSetPerformer(changeSet);

        ArchiveInputStream in = createArchiveInputStream(new byte[0]);
        ArchiveOutputStream out = mockArchiveOutputStream();

        perform(performer, in, out);

        // Verify entries were added and deleted
        ArchiveEntry addedEntry = performer.getAddedEntries().iterator().next();
        assertEquals("test1.txt", addedEntry.getName());

        ArchiveEntry deletedEntry = performer.getDeletedEntries().iterator().next();
        assertEquals("test2.txt", deletedEntry.getName());
    }

    @Test
    public void testPerform_multipleChanges_addFileWithReplaceModeAndDeleteFile() {
        ChangeSet changeSet = createChangeSet("addReplace", "test1.txt", "delete", "test2.txt");
        ChangeSetPerformer performer = new ChangeSetPerformer(changeSet);

        ArchiveInputStream in = createArchiveInputStream(new byte[0]);
        ArchiveOutputStream out = mockArchiveOutputStream();

        perform(performer, in, out);

        // Verify entries were replaced and deleted
        ArchiveEntry addedEntry = performer.getAddedEntries().iterator().next();
        assertEquals("test1.txt", addedEntry.getName());
        assertFalse(performer.getAddedEntries().hasMoreElements());

        ArchiveEntry deletedEntry = performer.getDeletedEntries().iterator().next();
        assertEquals("test2.txt", deletedEntry.getName());
        assertFalse(performer.getDeletedEntries().hasMoreElements());
    }

    @Test
    public void testPerform_multipleChanges_addFileAndDeleteFolder() {
        ChangeSet changeSet = createChangeSet("add", "test1.txt", "deleteDir", "test");
        ChangeSetPerformer performer = new ChangeSetPerformer(changeSet);

        ArchiveInputStream in = createArchiveInputStream(new byte[0]);
        ArchiveOutputStream out = mockArchiveOutputStream();

        perform(performer, in, out);

        // Verify entries were added and deleted
        ArchiveEntry addedEntry = performer.getAddedEntries().iterator().next();
        assertEquals("test1.txt", addedEntry.getName());
        assertFalse(performer.getAddedEntries().hasMoreElements());

        ArchiveEntry deletedEntry = performer.getDeletedEntries().iterator().next();
        assertEquals("test/test1.txt", deletedEntry.getName());
        assertFalse(performer.getDeletedEntries().hasMoreElements());
    }

    @Test
    public void testPerform_multipleChanges_addFileAndAddFile() {
        ChangeSet changeSet = createChangeSet("add", "test1.txt", "add", "test2.txt");
        ChangeSetPerformer performer = new ChangeSetPerformer(changeSet);

        ArchiveInputStream in = createArchiveInputStream(new byte[0]);
        ArchiveOutputStream out = mockArchiveOutputStream();

        perform(performer, in, out);

        // Verify entries were added
        ArchiveEntry addedEntry1 = performer.getAddedEntries().iterator().next();
        assertEquals("test1.txt", addedEntry1.getName());

        ArchiveEntry addedEntry2 = performer.getAddedEntries().iterator().next();
        assertEquals("test2.txt", addedEntry2.getName());
        assertFalse(performer.getAddedEntries().hasMoreElements());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testPerform_invalidChangeType() {
        ChangeSet changeSet = createChangeSet("invalid", "test.txt");
        ChangeSetPerformer performer = new ChangeSetPerformer(changeSet);

        ArchiveInputStream in = mockArchiveInputStream();
        ArchiveOutputStream out = mockArchiveOutputStream();

        perform(performer, in, out);
    }

    @Test(expected = NullPointerException.class)
    public void testPerform_nullChangeSet() {
        ChangeSetPerformer performer = new ChangeSetPerformer(null);

        ArchiveInputStream in = mockArchiveInputStream();
        ArchiveOutputStream out = mockArchiveOutputStream();

        perform(performer, in, out);
    }

    @Test(expected = NullPointerException.class)
    public void testPerform_nullArchiveInputStream() {
        ChangeSet changeSet = createChangeSet("add", "test.txt");
        ChangeSetPerformer performer = new ChangeSetPerformer(changeSet);

        ArchiveOutputStream out = mockArchiveOutputStream();

        perform(performer, null, out);
    }

    @Test(expected = NullPointerException.class)
    public void testPerform_nullArchiveOutputStream() {
        ChangeSet changeSet = createChangeSet("add", "test.txt");
        ChangeSetPerformer performer = new ChangeSetPerformer(changeSet);

        ArchiveInputStream in = mockArchiveInputStream();

        perform(performer, in, null);
    }

    @Test
    public void testIsDeletedLater() {
        ChangeSet changeSet = createChangeSet("delete", "test.txt");
        ChangeSetPerformer performer = new ChangeSetPerformer(changeSet);

        ArchiveEntry entry = mockArchiveEntry("test.txt");

        // Perform operations and check if the entry is deleted later
        perform(performer, createInputStream(), createOutputStream());
        assertTrue(performer.isDeletedLater(performer.getChanges(), entry));

        // Perform operations without a deletion change and check if the entry is not deleted later
        performer = new ChangeSetPerformer(createChangeSet());
        assertFalse(performer.isDeletedLater(performer.getChanges(), entry));
    }

    @Test
    public void testCopyStream() throws IOException {
        ByteArrayInputStream input = new ByteArrayInputStream(new byte[] { 1, 2, 3 });
        ByteArrayOutputStream output = new ByteArrayOutputStream();

        ChangeSetPerformer performer = new ChangeSetPerformer(null);
        performer.copyStream(input, performer.getChangeSetOutput(), mockArchiveEntry("test.txt"));

        byte[] expected = new byte[] { 1, 2, 3 };
        byte[] actual = IOUtils.toByteArray(performer.getChangeSetOutput().getInputStream());
        assertArrayEquals(expected, actual);
    }

    // Helper methods

    private ChangeSet createChangeSet(String... changes) {
        LinkedHashSet set = new LinkedHashSet();
        for (String change : changes) {
            set.add(createChange(change));
        }
        return new DefaultChangeSet(set);
    }

    private Change createChange(String changeType, String... args) {
        Change change = mock(Change.class);
        when(change.type()).thenReturn(Change.getType(changeType));
        when(change.targetFile()).thenReturn(args[0]);

        return change;
    }

    private InputStream createInputStream() {
        return new ByteArrayInputStream(new byte[0]);
    }

    private ArchiveInputStream mockArchiveInputStream() {
        ArchiveInputStream in = mock(ArchiveInputStream.class);
        when(in.getNextEntry()).thenReturn(mockArchiveEntry("test.txt"));
        when(in.close()).thenReturn(true);

        return in;
    }

    private ArchiveOutputStream mockArchiveOutputStream() {
        ArchiveOutputStream out = mock(ArchiveOutputStream.class);
        when(out.putArchiveEntry(mock(ArchiveEntry.class))).thenAnswer(invocation -> {
            ArchiveEntry entry = invocation.getArgument(0);
            performer.getAddedEntries().add(entry);
            return out;
        });
        when(out.closeArchiveEntry()).thenReturn(true);

        return out;
    }

    private ArchiveEntry mockArchiveEntry(String name) {
        ArchiveEntry entry = mock(ArchiveEntry.class);
        when(entry.getName()).thenReturn(name);

        return entry;
    }

    private ChangeSetResults perform(ChangeSetPerformer performer, InputStream input, OutputStream output)
            throws IOException {
        performer.perform(input, output);
        return performer.getResults();
    }
}