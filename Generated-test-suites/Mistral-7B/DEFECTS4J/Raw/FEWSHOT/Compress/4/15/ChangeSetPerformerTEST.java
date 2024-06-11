package org.apache.commons.compress.changes;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.archivers.ArchiveOutputStream;
import org.apache.commons.compress.archivers.ArchiveStreamFactory;
import org.apache.commons.compress.utils.IOUtils;
import org.apache.commons.compress.utils.TestUtils;
import org.junit.Before;
import org.junit.Test;

public class ChangeSetPerformerTest {

    private ArchiveInputStream inputStream;
    private ArchiveOutputStream outputStream;
    private ChangeSetResults results;
    private ChangeSet changeSet;
    private ChangeSetPerformer performer;

    @Before
    public void setup() {
        changeSet = mock(ChangeSet.class);
        performer = new ChangeSetPerformer(changeSet);

        inputStream = mock(ArchiveInputStream.class);
        when(changeSet.getInputStream()).thenReturn(inputStream);

        outputStream = mock(ArchiveOutputStream.class);
        when(changeSet.getOutputStream()).thenReturn(outputStream);

        results = mock(ChangeSetResults.class);
    }

    @Test
    public void testPerform_noChanges() throws IOException {
        Set changes = Collections.emptySet();
        performer = new ChangeSetPerformer(new ChangeSet(changes));

        ChangeSetResults result = performer.perform(inputStream, outputStream);
        verify(inputStream).close();
        verify(outputStream).finish();
        verify(results).setResults(Collections.emptyMap());
    }

    @Test
    public void testPerform_addAndDelete() throws IOException {
        ArchiveEntry entry1 = mock(ArchiveEntry.class);
        when(inputStream.getNextEntry()).thenReturn(entry1).thenReturn(null);
        when(entry1.getName()).thenReturn("file1.txt");

        Set<Change> changes = new LinkedHashSet<>();
        Change change1 = new Change(Change.TYPE_ADD, null, "file1.txt", null, null);
        Change change2 = new Change(Change.TYPE_DELETE, null, "file1.txt", null, null);
        changes.add(change1);
        changes.add(change2);

        performer = new ChangeSetPerformer(new ChangeSet(changes));

        performer.perform(inputStream, outputStream);

        verify(inputStream, atLeastOnce()).getNextEntry();
        verify(inputStream).close();
        verify(outputStream).finish();
        verify(results).addedFromChangeSet("file1.txt");
        verify(results).deleted("file1.txt");
    }

    @Test
    public void testPerform_addWithReplace() throws IOException {
        ArchiveEntry entry1 = mock(ArchiveEntry.class);
        when(inputStream.getNextEntry()).thenReturn(entry1).thenReturn(null);
        when(entry1.getName()).thenReturn("file1.txt");

        Set<Change> changes = new LinkedHashSet<>();
        Change change1 = new Change(Change.TYPE_ADD, null, "file1.txt", null, null, true);
        changes.add(change1);

        performer = new ChangeSetPerformer(new ChangeSet(changes));

        InputStream input = new ByteArrayInputStream(new byte[0]);
        when(changeSet.getInputStream()).thenReturn(input);
        ArchiveEntry outputEntry = mock(ArchiveEntry.class);
        when(outputStream.createEntry(any(String.class))).thenReturn(outputEntry);
        when(outputStream.putNextEntry(any(ArchiveEntry.class))).thenReturn(true);
        when(outputStream.closeArchiveEntry()).thenReturn(true);

        performer.perform(inputStream, outputStream);

        verify(inputStream, atLeastOnce()).getNextEntry();
        verify(inputStream).close();
        verify(outputStream).finish();
        verify(outputEntry).setSize(0L);
        verify(results).addedFromChangeSet("file1.txt");
    }

    @Test
    public void testPerform_deleteLater() throws IOException {
        ArchiveEntry entry1 = mock(ArchiveEntry.class);
        when(inputStream.getNextEntry()).thenReturn(entry1).thenReturn(null);
        when(entry1.getName()).thenReturn("file1.txt");

        Set<Change> changes = new LinkedHashSet<>();
        Change change1 = new Change(Change.TYPE_ADD, null, "file1.txt", null, null);
        Change change2 = new Change(Change.TYPE_DELETE, null, "file1.txt", null, null);
        changes.add(change1);
        changes.add(change2);

        performer = new ChangeSetPerformer(new ChangeSet(changes));

        performer.perform(inputStream, outputStream);

        verify(inputStream, atLeastOnce()).getNextEntry();
        verify(inputStream).close();
        verify(outputStream).finish();
        verify(results).addedFromChangeSet("file1.txt");
        verify(results).deleted("file1.txt");
    }

    @Test
    public void testPerform_deletedLaterButNotAdded() throws IOException {
        ArchiveEntry entry1 = mock(ArchiveEntry.class);
        when(inputStream.getNextEntry()).thenReturn(entry1).thenReturn(null);
        when(entry1.getName()).thenReturn("file1.txt");

        Set<Change> changes = new LinkedHashSet<>();
        Change change1 = new Change(Change.TYPE_DELETE, null, "file1.txt", null, null);
        changes.add(change1);

        performer = new ChangeSetPerformer(new ChangeSet(changes));

        // No file added to the input stream, so nothing should be copied
        performer.perform(inputStream, outputStream);

        verify(inputStream, atLeastOnce()).getNextEntry();
        verify(inputStream).close();
        verify(outputStream).finish();
        verify(results).deleted("file1.txt");
        verifyNoMoreInteractions(inputStream);
        verifyNoMoreInteractions(outputStream);
    }

    @Test
    public void testPerform_addLater() throws IOException {
        ArchiveEntry entry1 = mock(ArchiveEntry.class);
        when(inputStream.getNextEntry()).thenReturn(entry1).thenReturn(null);
        when(entry1.getName()).thenReturn("file1.txt");

        Set<Change> changes = new LinkedHashSet<>();
        Change change1 = new Change(Change.TYPE_ADD, null, "file1.txt", null, null);
        Change change2 = new Change(Change.TYPE_ADD, null, "file1.txt", null, null, true);
        changes.add(change1);
        changes.add(change2);

        performer = new ChangeSetPerformer(new ChangeSet(changes));

        InputStream input = new ByteArrayInputStream(new byte[0]);
        when(changeSet.getInputStream()).thenReturn(input);
        ArchiveEntry outputEntry = mock(ArchiveEntry.class);
        when(outputStream.createEntry(any(String.class))).thenReturn(outputEntry);
        when(outputStream.putNextEntry(any(ArchiveEntry.class))).thenReturn(true);
        when(outputStream.closeArchiveEntry()).thenReturn(true);

        performer.perform(inputStream, outputStream);

        verify(inputStream, atLeastOnce()).getNextEntry();
        verify(inputStream).close();
        verify(outputStream).finish();
        verify(outputEntry).setSize(0L);
        verifyNoMoreInteractions(inputStream);
        verifyNoMoreInteractions(outputStream);

        // The first add change has already been processed, so the second one should be ignored
        performer = new ChangeSetPerformer(new ChangeSet(changes));
        performer.perform(inputStream, outputStream);

        verifyNoMoreInteractions(inputStream);
        verifyNoMoreInteractions(outputStream);
    }

    @Test
    public void testPerform_addAndDeleteDirectory() throws IOException {
        ArchiveEntry entry1 = mock(ArchiveEntry.class);
        when(inputStream.getNextEntry()).thenReturn(entry1).thenReturn(null);
        when(entry1.getName()).thenReturn("dir1/file1.txt");

        Set<Change> changes = new LinkedHashSet<>();
        Change change1 = new Change(Change.TYPE_ADD, null, "dir1/file1.txt", null, null);
        Change change2 = new Change(Change.TYPE_DELETE_DIR, null, "dir1", null, null);
        changes.add(change1);
        changes.add(change2);

        performer = new ChangeSetPerformer(new ChangeSet(changes));

        performer.perform(inputStream, outputStream);

        verify(inputStream, atLeastOnce()).getNextEntry();
        verify(inputStream).close();
        verify(outputStream).finish();
        verify(results).addedFromChangeSet("dir1/file1.txt");
        verify(results).deleted("dir1");
    }

    @Test
    public void testPerform_addDirectory() throws IOException {
        ArchiveEntry entry1 = mock(ArchiveEntry.class);
        when(inputStream.getNextEntry()).thenReturn(entry1).thenReturn(null);
        when(entry1.getName()).thenReturn("dir1/");

        Set<Change> changes = new LinkedHashSet<>();
        Change change1 = new Change(Change.TYPE_ADD_DIR, null, "dir1", null, null);
        changes.add(change1);

        performer = new ChangeSetPerformer(new ChangeSet(changes));

        ArchiveOutputStream archiveOutputStream = mock(ArchiveOutputStream.class);
        when(changeSet.getOutputStream()).thenReturn(archiveOutputStream);
        when(archiveOutputStream.createEntry(any(String.class))).thenReturn(entry1);
        when(archiveOutputStream.putNextEntry(any(ArchiveEntry.class))).thenReturn(true);
        when(archiveOutputStream.closeArchiveEntry()).thenReturn(true);

        performer.perform(inputStream, outputStream);

        verify(inputStream, atLeastOnce()).getNextEntry();
        verify(inputStream).close();
        verify(outputStream).finish();
        verify(entry1).setSize(0L);
        verify(results).addedFromChangeSet("dir1");
    }

    @Test
    public void testPerform_addDirectoryAndFile() throws IOException {
        ArchiveEntry entry1 = mock(ArchiveEntry.class);
        when(inputStream.getNextEntry()).thenReturn(entry1).thenReturn(null);
        when(entry1.getName()).thenReturn("dir1/");

        Set<Change> changes = new LinkedHashSet<>();
        Change change1 = new Change(Change.TYPE_ADD_DIR, null, "dir1", null, null);
        Change change2 = new Change(Change.TYPE_ADD, null, "dir1/file1.txt", null, null);
        changes.add(change1);
        changes.add(change2);

        performer = new ChangeSetPerformer(new ChangeSet(changes));

        ArchiveOutputStream archiveOutputStream = mock(ArchiveOutputStream.class);
        when(changeSet.getOutputStream()).thenReturn(archiveOutputStream);
        when(archiveOutputStream.createEntry(any(String.class))).thenReturn(entry1);
        when(archiveOutputStream.putNextEntry(any(ArchiveEntry.class))).thenReturn(true);
        when(archiveOutputStream.closeArchiveEntry()).thenReturn(true);

        performer.perform(inputStream, outputStream);

        verify(inputStream, atLeastOnce()).getNextEntry();
        verify(inputStream).close();
        verify(outputStream).finish();
        verify(entry1).setSize(0L);
        verify(results).addedFromChangeSet("dir1");
        verify(results).addedFromChangeSet("dir1/file1.txt");
    }

    private void verifyNoMoreInteractions(Object object) {
        verify(object, never()).getNextEntry();
        verify(object, never()).close();
    }

    private void verify(Object object, int times) throws IOException {
        for (int i = 0; i < times; i++) {
            verify(object).getNextEntry();
        }
    }
}