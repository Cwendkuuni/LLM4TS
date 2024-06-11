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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.archivers.ArchiveOutputStream;
import org.apache.commons.compress.archivers.tar.TarEntry;
import org.apache.commons.compress.utils.IOUtils;
import org.junit.Before;
import org.junit.Test;

public class ChangeSetPerformerTest {

    private ChangeSet mockChangeSet;
    private ChangeSetPerformer performer;
    private ArchiveEntry mockEntry;
    private InputStream mockInputStream;
    private ArchiveInputStream mockArchiveInputStream;
    private ArchiveOutputStream mockArchiveOutputStream;
    private Change mockChangeAdd;
    private Change mockChangeDelete;
    private Change mockChangeDeleteDir;
    private Change mockChangeAddReplace;

    @Before
    public void setup() {
        mockChangeSet = mock(ChangeSet.class);
        performer = new ChangeSetPerformer(mockChangeSet);

        mockEntry = mock(ArchiveEntry.class);
        mockArchiveInputStream = mock(ArchiveInputStream.class);
        mockArchiveOutputStream = mock(ArchiveOutputStream.class);

        mockInputStream = new ByteArrayInputStream(new byte[0]);

        mockChangeAdd = new Change(Change.TYPE_ADD, "file1.txt", null, mockInputStream);
        mockChangeDelete = new Change(Change.TYPE_DELETE, "file1.txt", null, null);
        mockChangeDeleteDir = new Change(Change.TYPE_DELETE_DIR, "dir1/", null, null);
        mockChangeAddReplace = new Change(Change.TYPE_ADD, "file1.txt", "file1_original.txt", mockInputStream);
    }

    @Test
    public void testPerform_Add_ReplaceMode() {
        Set mockChanges = new HashSet();
        mockChanges.add(mockChangeAddReplace);
        when(mockChangeSet.getChanges()).thenReturn(mockChanges);

        ChangeSetResults results = performer.perform(mockArchiveInputStream, mockArchiveOutputStream);

        assertTrue(results.hasBeenAdded("file1.txt"));
        verifyCopyStream(mockInputStream, mockArchiveOutputStream, mockChangeAddReplace.getEntry());
    }

    @Test
    public void testPerform_Add_NotReplaceMode() {
        Set mockChanges = new HashSet();
        mockChanges.add(mockChangeAdd);
        when(mockChangeSet.getChanges()).thenReturn(mockChanges);

        ChangeSetResults results = performer.perform(mockArchiveInputStream, mockArchiveOutputStream);

        assertTrue(results.hasBeenAdded("file1.txt"));
        verifyCopyStream(mockInputStream, mockArchiveOutputStream, mockChangeAdd.getEntry());
    }

    @Test
    public void testPerform_Delete() {
        Set mockChanges = new HashSet();
        mockChanges.add(mockChangeDelete);
        when(mockChangeSet.getChanges()).thenReturn(mockChanges);

        ChangeSetResults results = performer.perform(mockArchiveInputStream, mockArchiveOutputStream);

        assertFalse(results.hasBeenAdded("file1.txt"));
        assertNull(mockArchiveOutputStream.getNextEntry());
    }

    @Test
    public void testPerform_DeleteDir() {
        Set mockChanges = new HashSet();
        mockChanges.add(mockChangeDeleteDir);
        when(mockChangeSet.getChanges()).thenReturn(mockChanges);

        ArchiveEntry mockArchiveEntry = mock(ArchiveEntry.class);
        when(mockArchiveInputStream.getNextEntry()).thenReturn(mockArchiveEntry);
        when(mockArchiveEntry.getName()).thenReturn("dir1/file1.txt");

        ChangeSetResults results = performer.perform(mockArchiveInputStream, mockArchiveOutputStream);

        assertFalse(results.hasBeenAdded("file1.txt"));
        assertNull(mockArchiveOutputStream.getNextEntry());
    }

    @Test
    public void testPerform_Add_AlreadyAdded() {
        Set mockChanges = new HashSet();
        mockChanges.add(mockChangeAdd);
        mockChanges.add(mockChangeAdd);
        when(mockChangeSet.getChanges()).thenReturn(mockChanges);

        ChangeSetResults results = performer.perform(mockArchiveInputStream, mockArchiveOutputStream);

        assertTrue(results.hasBeenAdded("file1.txt"));
        verify(mockArchiveOutputStream, never()).putArchiveEntry(mockChangeAdd.getEntry());
        verify(mockArchiveOutputStream, never()).writeTo(mockChangeAdd.getInput());
    }

    @Test
    public void testPerform_Add_AlreadyAdded_ReplaceMode() {
        Set mockChanges = new HashSet();
        mockChanges.add(mockChangeAddReplace);
        mockChanges.add(mockChangeAddReplace);
        when(mockChangeSet.getChanges()).thenReturn(mockChanges);

        ChangeSetResults results = performer.perform(mockArchiveInputStream, mockArchiveOutputStream);

        assertTrue(results.hasBeenAdded("file1.txt"));
        verifyCopyStream(mockInputStream, mockArchiveOutputStream, mockChangeAddReplace.getEntry());
    }

    @Test
    public void testPerform_Delete_AlreadyDeleted() {
        Set mockChanges = new HashSet();
        mockChanges.add(mockChangeDelete);
        mockChanges.add(mockChangeDelete);
        when(mockChangeSet.getChanges()).thenReturn(mockChanges);

        ChangeSetResults results = performer.perform(mockArchiveInputStream, mockArchiveOutputStream);

        assertFalse(results.hasBeenAdded("file1.txt"));
        verify(mockArchiveOutputStream, never()).putArchiveEntry(mockChangeDelete.getEntry());
        verify(mockArchiveOutputStream, never()).writeTo(mockChangeDelete.getInput());
    }

    @Test
    public void testPerform_Delete_AlreadyDeleted_Dir() {
        Set mockChanges = new HashSet();
        mockChanges.add(mockChangeDeleteDir);
        mockChanges.add(mockChangeDeleteDir);
        when(mockChangeSet.getChanges()).thenReturn(mockChanges);

        ArchiveEntry mockArchiveEntry = mock(ArchiveEntry.class);
        when(mockArchiveInputStream.getNextEntry()).thenReturn(mockArchiveEntry);
        when(mockArchiveEntry.getName()).thenReturn("dir1/file1.txt");

        ChangeSetResults results = performer.perform(mockArchiveInputStream, mockArchiveOutputStream);

        assertFalse(results.hasBeenAdded("file1.txt"));
        verify(mockArchiveOutputStream, never()).putArchiveEntry(mockChangeDeleteDir.getEntry());
        verify(mockArchiveOutputStream, never()).writeTo(mockChangeDeleteDir.getInput());
    }

    @Test
    public void testPerform_Delete_AlreadyDeleted_FileInsideDir() {
        Set mockChanges = new HashSet();
        mockChanges.add(mockChangeDeleteDir);
        mockChanges.add(mockChangeDelete);
        when(mockChangeSet.getChanges()).thenReturn(mockChanges);

        ArchiveEntry mockArchiveEntryDir = mock(ArchiveEntry.class);
        ArchiveEntry mockArchiveEntryFile = mock(ArchiveEntry.class);
        when(mockArchiveInputStream.getNextEntry()).thenReturn(mockArchiveEntryDir);
        when(mockArchiveInputStream.getNextEntry()).thenReturn(mockArchiveEntryFile);
        when(mockArchiveEntryDir.getName()).thenReturn("dir1/");
        when(mockArchiveEntryFile.getName()).thenReturn("file1.txt");

        ChangeSetResults results = performer.perform(mockArchiveInputStream, mockArchiveOutputStream);

        assertFalse(results.hasBeenAdded("file1.txt"));
        verify(mockArchiveOutputStream, never()).putArchiveEntry(mockChangeDeleteDir.getEntry());
        verify(mockArchiveOutputStream, never()).putArchiveEntry(mockChangeDelete.getEntry());
        verify(mockArchiveOutputStream, never()).writeTo(mockChangeDelete.getInput());
    }

    @Test
    public void testPerform_Add_DeleteLater() {
        Set mockChanges = new HashSet();
        mockChanges.add(mockChangeAdd);
        mockChanges.add(mockChangeDelete);
        when(mockChangeSet.getChanges()).thenReturn(mockChanges);

        ArchiveEntry mockArchiveEntry = mock(ArchiveEntry.class);
        when(mockArchiveInputStream.getNextEntry()).thenReturn(mockArchiveEntry);
        when(mockArchiveEntry.getName()).thenReturn("file1.txt");

        ChangeSetResults results = performer.perform(mockArchiveInputStream, mockArchiveOutputStream);

        assertFalse(results.hasBeenAdded("file1.txt"));
        assertNull(mockArchiveOutputStream.getNextEntry());
    }

    @Test
    public void testPerform_Add_DeleteLater_Dir() {
        Set mockChanges = new HashSet();
        mockChanges.add(mockChangeAdd);
        mockChanges.add(mockChangeDeleteDir);
        when(mockChangeSet.getChanges()).thenReturn(mockChanges);

        ArchiveEntry mockArchiveEntryDir = mock(ArchiveEntry.class);
        when(mockArchiveInputStream.getNextEntry()).thenReturn(mockArchiveEntryDir);
        when(mockArchiveEntryDir.getName()).thenReturn("dir1/");

        ChangeSetResults results = performer.perform(mockArchiveInputStream, mockArchiveOutputStream);

        assertFalse(results.hasBeenAdded("file1.txt"));
        assertNull(mockArchiveOutputStream.getNextEntry());
    }

    @Test
    public void testPerform_Add_DeleteLater_FileInsideDir() {
        Set mockChanges = new HashSet();
        mockChanges.add(mockChangeAdd);
        mockChanges.add(mockChangeDelete);
        when(mockChangeSet.getChanges()).thenReturn(mockChanges);

        ArchiveEntry mockArchiveEntryDir = mock(ArchiveEntry.class);
        ArchiveEntry mockArchiveEntryFile = mock(ArchiveEntry.class);
        when(mockArchiveInputStream.getNextEntry()).thenReturn(mockArchiveEntryDir);
        when(mockArchiveInputStream.getNextEntry()).thenReturn(mockArchiveEntryFile);
        when(mockArchiveEntryDir.getName()).thenReturn("dir1/");
        when(mockArchiveEntryFile.getName()).thenReturn("file1.txt");

        ChangeSetResults results = performer.perform(mockArchiveInputStream, mockArchiveOutputStream);

        assertFalse(results.hasBeenAdded("file1.txt"));
        verify(mockArchiveOutputStream, never()).putArchiveEntry(mockChangeAdd.getEntry());
        verify(mockArchiveOutputStream, never()).writeTo(mockChangeAdd.getInput());
    }

    @Test
    public void testPerform_Add_DeleteLater_DeleteFirst() {
        Set mockChanges = new HashSet();
        mockChanges.add(mockChangeDelete);
        mockChanges.add(mockChangeAdd);
        when(mockChangeSet.getChanges()).thenReturn(mockChanges);

        ArchiveEntry mockArchiveEntry = mock(ArchiveEntry.class);
        when(mockArchiveInputStream.getNextEntry()).thenReturn(mockArchiveEntry);
        when(mockArchiveEntry.getName()).thenReturn("file1.txt");

        ChangeSetResults results = performer.perform(mockArchiveInputStream, mockArchiveOutputStream);

        assertFalse(results.hasBeenAdded("file1.txt"));
        assertNull(mockArchiveOutputStream.getNextEntry());
    }

    @Test
    public void testPerform_Add_DeleteLater_AddFirst() {
        Set mockChanges = new HashSet();
        mockChanges.add(mockChangeAdd);
        mockChanges.add(mockChangeDelete);
        when(mockChangeSet.getChanges()).thenReturn(mockChanges);

        ChangeSetResults results = performer.perform(mockArchiveInputStream, mockArchiveOutputStream);

        assertTrue(results.hasBeenAdded("file1.txt"));
        verifyCopyStream(mockInputStream, mockArchiveOutputStream, mockChangeAdd.getEntry());
    }

    @Test
    public void testPerform_Add_DeleteLater_FileInsideDir_AddFirst() {
        Set mockChanges = new HashSet();
        mockChanges.add(mockChangeAdd);
        mockChanges.add(mockChangeDelete);
        when(mockChangeSet.getChanges()).thenReturn(mockChanges);

        ArchiveEntry mockArchiveEntryDir = mock(ArchiveEntry.class);
        ArchiveEntry mockArchiveEntryFile = mock(ArchiveEntry.class);
        when(mockArchiveInputStream.getNextEntry()).thenReturn(mockArchiveEntryDir);
        when(mockArchiveInputStream.getNextEntry()).thenReturn(mockArchiveEntryFile);
        when(mockArchiveEntryDir.getName()).thenReturn("dir1/");
        when(mockArchiveEntryFile.getName()).thenReturn("file1.txt");

        ChangeSetResults results = performer.perform(mockArchiveInputStream, mockArchiveOutputStream);

        assertTrue(results.hasBeenAdded("file1.txt"));
        verifyCopyStream(mockInputStream, mockArchiveOutputStream, mockChangeAdd.getEntry());
    }

    @Test
    public void testPerform_Add_DeleteLater_FileInsideDir_DeleteFirst() {
        Set mockChanges = new HashSet();
        mockChanges.add(mockChangeDelete);
        mockChanges.add(mockChangeAdd);
        when(mockChangeSet.getChanges()).thenReturn(mockChanges);

        ArchiveEntry mockArchiveEntryDir = mock(ArchiveEntry.class);
        ArchiveEntry mockArchiveEntryFile = mock(ArchiveEntry.class);
        when(mockArchiveInputStream.getNextEntry()).thenReturn(mockArchiveEntryDir);
        when(mockArchiveInputStream.getNextEntry()).thenReturn(mockArchiveEntryFile);
        when(mockArchiveEntryDir.getName()).thenReturn("dir1/");
        when(mockArchiveEntryFile.getName()).thenReturn("file1.txt");

        ChangeSetResults results = performer.perform(mockArchiveInputStream, mockArchiveOutputStream);

        assertFalse(results.hasBeenAdded("file1.txt"));
        verify(mockArchiveOutputStream, never()).putArchiveEntry(mockChangeAdd.getEntry());
        verify(mockArchiveOutputStream, never()).writeTo(mockChangeAdd.getInput());
    }

    private void verifyCopyStream(InputStream input, ArchiveOutputStream output, ArchiveEntry entry) {
        when(mockChangeSet.getChanges()).thenReturn(new HashSet());
        when(mockArchiveInputStream.getNextEntry()).thenReturn(entry);
        performer.perform(mockArchiveInputStream, mockArchiveOutputStream);

        verify(output, times(1)).putArchiveEntry(entry);
        verify(output, times(1)).writeTo(input);
        verify(output, times(1)).closeArchiveEntry();
    }
}