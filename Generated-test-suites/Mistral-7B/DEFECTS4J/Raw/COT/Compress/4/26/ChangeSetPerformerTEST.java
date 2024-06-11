package org.apache.commons.compress.changes;

import static org.junit.Assert.*;

import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.archivers.ArchiveOutputStream;
import org.apache.commons.compress.utils.IOUtils;
import org.junit.Test;

import org.apache.commons.compress.changes.Change;
import org.apache.commons.compress.changes.ChangeSet;
import org.apache.commons.compress.changes.ChangeSetEntry;
import org.apache.commons.compress.changes.ChangeSetResults;
import org.apache.commons.compress.changes.ChangeSetperformer;

import java.io.*;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * Test class for ChangeSetPerformer
 */
public class ChangeSetPerformerTest {
    @Test
    public void testChangeSetPerformer_EmptyChangeSet() throws Exception {
        ChangeSet changeSet = new ChangeSet();
        ChangeSetPerformer performer = new ChangeSetPerformer(changeSet);
        ArchiveInputStream input = createMockInputStream();
        ArchiveOutputStream output = createMockOutputStream();

        ChangeSetResults result = performer.perform(input, output);

        assertTrue(result.isEmpty());
    }

    @Test
    public void testChangeSetPerformer_Addition() throws Exception {
        ChangeSet changeSet = new ChangeSet();
        ChangeSetEntry entry = new ChangeSetEntry("test.txt", new byte[10], null, false);
        changeSet.addChange(new Change(Change.TYPE_ADD, "test.txt", null, null));
        ChangeSetPerformer performer = new ChangeSetPerformer(changeSet);
        ArchiveInputStream input = createMockInputStream(entry);
        ArchiveOutputStream output = createMockOutputStream();

        ChangeSetResults result = performer.perform(input, output);

        assertEquals(1, result.size());
        assertEquals("test.txt", result.get(0).getName());
    }

    @Test
    public void testChangeSetPerformer_Deletion() throws Exception {
        ChangeSet changeSet = new ChangeSet();
        ChangeSetEntry entry = new ChangeSetEntry("test.txt", null, null, false);
        changeSet.addChange(new Change(Change.TYPE_DELETE, "test.txt", null, null));
        ChangeSetPerformer performer = new ChangeSetPerformer(changeSet);
        ArchiveInputStream input = createMockInputStream();
        ArchiveOutputStream output = createMockOutputStream();

        ChangeSetResults result = performer.perform(input, output);

        assertEquals(1, result.getDeleted().size());
        assertEquals("test.txt", result.getDeleted().get(0));
    }

    @Test
    public void testChangeSetPerformer_AdditionAndDeletion() throws Exception {
        ChangeSet changeSet = new ChangeSet();
        ChangeSetEntry addEntry = new ChangeSetEntry("test.txt1", new byte[10], null, false);
        ChangeSetEntry delEntry = new ChangeSetEntry("test.txt2", null, null, false);
        changeSet.addChange(new Change(Change.TYPE_ADD, "test.txt1", null, null));
        changeSet.addChange(new Change(Change.TYPE_DELETE, "test.txt2", null, null));
        ChangeSetPerformer performer = new ChangeSetPerformer(changeSet);
        ArchiveInputStream input = createMockInputStream(addEntry, delEntry);
        ArchiveOutputStream output = createMockOutputStream();

        ChangeSetResults result = performer.perform(input, output);

        assertEquals(1, result.getAddedFromChangeSet().size());
        assertEquals("test.txt1", result.getAddedFromChangeSet().get(0));
        assertEquals(1, result.getDeleted().size());
        assertEquals("test.txt2", result.getDeleted().get(0));
    }

    @Test(expected = IOException.class)
    public void testChangeSetPerformer_DeletionBeforeAddition() throws Exception {
        ChangeSet changeSet = new ChangeSet();
        ChangeSetEntry addEntry = new ChangeSetEntry("test.txt1", new byte[10], null, false);
        ChangeSetEntry delEntry = new ChangeSetEntry("test.txt1", null, null, false);
        changeSet.addChange(new Change(Change.TYPE_DELETE, "test.txt1", null, null));
        changeSet.addChange(new Change(Change.TYPE_ADD, "test.txt1", null, null));
        ChangeSetPerformer performer = new ChangeSetPerformer(changeSet);
        ArchiveInputStream input = createMockInputStream();
        ArchiveOutputStream output = createMockOutputStream();

        performer.perform(input, output);
    }

    @Test
    public void testChangeSetPerformer_MultipleAdditions() throws Exception {
        ChangeSet changeSet = new ChangeSet();
        ChangeSetEntry entry1 = new ChangeSetEntry("test1.txt", new byte[10], null, false);
        ChangeSetEntry entry2 = new ChangeSetEntry("test2.txt", new byte[10], null, false);
        changeSet.addChange(new Change(Change.TYPE_ADD, "test1.txt", null, null));
        changeSet.addChange(new Change(Change.TYPE_ADD, "test2.txt", null, null));
        ChangeSetPerformer performer = new ChangeSetPerformer(changeSet);
        ArchiveInputStream input = createMockInputStream(entry1, entry2);
        ArchiveOutputStream output = createMockOutputStream();

        ChangeSetResults result = performer.perform(input, output);

        assertEquals(2, result.size());
        assertEquals("test1.txt", result.get(0).getName());
        assertEquals("test2.txt", result.get(1).getName());
    }

    @Test
    public void testChangeSetPerformer_MultipleAdditionsAndDeletions() throws Exception {
        ChangeSet changeSet = new ChangeSet();
        ChangeSetEntry addEntry1 = new ChangeSetEntry("test1.txt", new byte[10], null, false);
        ChangeSetEntry addEntry2 = new ChangeSetEntry("test2.txt", new byte[10], null, false);
        ChangeSetEntry delEntry = new ChangeSetEntry("test1.txt", null, null, false);
        changeSet.addChange(new Change(Change.TYPE_ADD, "test1.txt", null, null));
        changeSet.addChange(new Change(Change.TYPE_ADD, "test2.txt", null, null));
        changeSet.addChange(new Change(Change.TYPE_DELETE, "test1.txt", null, null));
        ChangeSetPerformer performer = new ChangeSetPerformer(changeSet);
        ArchiveInputStream input = createMockInputStream(addEntry1, addEntry2, delEntry);
        ArchiveOutputStream output = createMockOutputStream();

        ChangeSetResults result = performer.perform(input, output);

        assertEquals(1, result.getDeleted().size());
        assertEquals("test1.txt", result.getDeleted().get(0));
        assertEquals(2, result.getAddedFromChangeSet().size());
        assertEquals("test1.txt", result.getAddedFromChangeSet().get(0).getName());
        assertEquals("test2.txt", result.getAddedFromChangeSet().get(1).getName());
    }

    @Test(expected = IOException.class)
    public void testChangeSetPerformer_InvalidChangeSet() throws Exception {
        ChangeSet changeSet = new ChangeSet();
        ChangeSetEntry entry = new ChangeSetEntry("test.txt", null, null, false);
        changeSet.addChange(new Change(Change.TYPE_INVALID, "test.txt", null, null));
        ChangeSetPerformer performer = new ChangeSetPerformer(changeSet);
        ArchiveInputStream input = createMockInputStream(entry);
        ArchiveOutputStream output = createMockOutputStream();

        performer.perform(input, output);
    }

    private ArchiveInputStream createMockInputStream() throws IOException {
        return createMockInputStream((ArchiveEntry[]) null);
    }

    private ArchiveInputStream createMockInputStream(ArchiveEntry... entries) throws IOException {
        ArchiveInputStream mockInput = mock(ArchiveInputStream.class);
        when(mockInput.getNextEntry()).thenReturn(entries == null ? null : entries[0]);
        when(mockInput.closeEntry()).thenReturn(true);
        when(mockInput.readAllBytes()).thenReturn(new byte[0]);
        when(mockInput.available()).thenReturn(0);
        return mockInput;
    }

    private ArchiveOutputStream createMockOutputStream() throws IOException {
        ArchiveOutputStream mockOutput = mock(ArchiveOutputStream.class);
        when(mockOutput.putArchiveEntry(any(ArchiveEntry.class)))
                .thenAnswer(i -> mockOutput);
        when(mockOutput.closeArchiveEntry()).thenReturn(true);
        when(mockOutput.finish()).thenReturn(true);
        return mockOutput;
    }

    private ArchiveOutputStream createMockOutputStream(ArchiveEntry entry) throws IOException {
        ArchiveOutputStream mockOutput = createMockOutputStream();
        when(mockOutput.putArchiveEntry(entry)).thenReturn(mockOutput);
        return mockOutput;
    }
}