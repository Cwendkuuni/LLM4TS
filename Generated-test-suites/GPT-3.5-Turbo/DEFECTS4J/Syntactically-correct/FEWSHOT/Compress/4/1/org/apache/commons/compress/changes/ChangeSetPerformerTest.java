package org.apache.commons.compress.changes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.archivers.ArchiveOutputStream;
import org.apache.commons.compress.archivers.ArchiveEntry;
import org.junit.Before;
import org.junit.Test;

public class ChangeSetPerformerTest {
    private ChangeSetPerformer performer;
    private ChangeSet changeSet;
    private ArchiveInputStream input;
    private ArchiveOutputStream output;
    
    @Before
    public void setUp() throws IOException {
        changeSet = new ChangeSet();
        performer = new ChangeSetPerformer(changeSet);
        input = new ArchiveInputStream(new ByteArrayInputStream("Test input stream".getBytes()));
        output = new ArchiveOutputStream(new ByteArrayOutputStream());
    }

    @Test
    public void testPerform_AddAndReplaceMode_CopyStreamCalled() throws IOException {
        Change change = new Change(Change.TYPE_ADD_REPLACE, "input.txt", new ByteArrayInputStream("Test".getBytes()));
        changeSet.add(change);

        ChangeSetResults results = performer.perform(input, output);

        assertTrue(results.hasBeenAdded("input.txt"));
    }
    
    @Test
    public void testPerform_AddAndNotReplaceMode_CopyStreamCalled() throws IOException {
        Change change = new Change(Change.TYPE_ADD, "input.txt", new ByteArrayInputStream("Test".getBytes()));
        changeSet.add(change);

        ChangeSetResults results = performer.perform(input, output);

        assertTrue(results.hasBeenAdded("input.txt"));
    }
    
    @Test
    public void testPerform_Add_DeleteLater_IsDeletedLaterTrue() throws IOException {
        Change change1 = new Change(Change.TYPE_ADD, "input.txt", new ByteArrayInputStream("Test".getBytes()));
        Change change2 = new Change(Change.TYPE_DELETE, "input.txt");
        changeSet.add(change1);
        changeSet.add(change2);

        boolean isDeletedLater = performer.isDeletedLater(changeSet.getChanges(), createArchiveEntry("input.txt"));

        assertTrue(isDeletedLater);
    }
    
    @Test
    public void testPerform_Add_DeleteDirLater_IsDeletedLaterTrue() throws IOException {
        Change change1 = new Change(Change.TYPE_ADD, "dir/", new ByteArrayInputStream("Test".getBytes()));
        Change change2 = new Change(Change.TYPE_DELETE_DIR, "dir");
        changeSet.add(change1);
        changeSet.add(change2);

        boolean isDeletedLater = performer.isDeletedLater(changeSet.getChanges(), createArchiveEntry("dir/file.txt"));

        assertTrue(isDeletedLater);
    }
    
    @Test
    public void testPerform_Add_DeleteLater_IsDeletedLaterFalse() throws IOException {
        Change change1 = new Change(Change.TYPE_ADD, "input.txt", new ByteArrayInputStream("Test".getBytes()));
        Change change2 = new Change(Change.TYPE_DELETE, "delete.txt");
        changeSet.add(change1);
        changeSet.add(change2);

        boolean isDeletedLater = performer.isDeletedLater(changeSet.getChanges(), createArchiveEntry("input.txt"));

        assertFalse(isDeletedLater);
    }
    
    @Test
    public void testPerform_Add_DeleteDirLater_IsDeletedLaterFalse() throws IOException {
        Change change1 = new Change(Change.TYPE_ADD, "dir/", new ByteArrayInputStream("Test".getBytes()));
        Change change2 = new Change(Change.TYPE_DELETE_DIR, "delete/");
        changeSet.add(change1);
        changeSet.add(change2);

        boolean isDeletedLater = performer.isDeletedLater(changeSet.getChanges(), createArchiveEntry("dir/file.txt"));

        assertFalse(isDeletedLater);
    }
    
    @Test
    public void testCopyStream_EntryNotNull() throws IOException {
        ArchiveEntry entry = createArchiveEntry("input.txt");

        performer.copyStream(input, output, entry);

        assertNotNull(entry);
    }
    
    @Test
    public void testCopyStream_EntryStreamCopiedToOutput() throws IOException {
        ArchiveEntry entry = createArchiveEntry("input.txt");

        performer.copyStream(input, output, entry);

        ByteArrayOutputStream baos = (ByteArrayOutputStream) output.getStream();
        assertEquals("Test input stream", baos.toString());
    }
    
    @Test
    public void testCopyStream_EntryClosed() throws IOException {
        ArchiveEntry entry = createArchiveEntry("input.txt");

        performer.copyStream(input, output, entry);

        assertTrue(entry.isClosed());
    }
    
    private ArchiveEntry createArchiveEntry(String name) {
        ArchiveEntry entry = new ArchiveEntry();
        entry.setName(name);
        return entry;
    }
}