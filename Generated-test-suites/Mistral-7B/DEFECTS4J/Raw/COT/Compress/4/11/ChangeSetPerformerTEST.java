package org.apache.commons.compress.changes;

import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.archivers.ArchiveOutputStream;
import org.apache.commons.compress.utils.IOUtils;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.Set;

import static org.junit.Assert.*;

public class ChangeSetPerformerTest {

    private ChangeSet changeSet;
    private ChangeSetPerformer changeSetPerformer;
    private ArchiveInputStream inputMock;
    private ArchiveOutputStream outputMock;

    @Before
    public void setUp() throws Exception {
        changeSet = Mockito.mock(ChangeSet.class);
        changeSetPerformer = new ChangeSetPerformer(changeSet);

        inputMock = Mockito.mock(ArchiveInputStream.class);
        outputMock = Mockito.mock(ArchiveOutputStream.class);
    }

    @Test
    public void testPerform_addReplaceModeChange() throws Exception {
        // given
        ArchiveEntry entry = Mockito.mock(ArchiveEntry.class);
        Mockito.when(changeSet.getChanges()).thenReturn(new LinkedHashSet<>(Arrays.asList(
                Mockito.argThat(change -> change.type() == Change.TYPE_ADD && change.isReplaceMode())
        )));
        Mockito.when(entry.getName()).thenReturn("test.txt");
        Mockito.when(inputMock.getNextEntry()).thenReturn(entry);

        ByteArrayOutputStream output = new ByteArrayOutputStream();
        Mockito.when(outputMock.getOutputStream()).thenReturn(output);

        // when
        ChangeSetResults results = changeSetPerformer.perform(inputMock, outputMock);

        // then
        Mockito.verify(outputMock, Mockito.times(1)).putArchiveEntry(entry);
        Mockito.verify(outputMock, Mockito.times(1)).closeArchiveEntry();
        Mockito.verify(changeSet, Mockito.times(1)).getChanges();
        assertFalse(results.hasBeenAdded("test.txt"));
        assertTrue(results.addedFromChangeSet("test.txt"));
    }

    @Test(expected = IOException.class)
    public void testPerform_deleteChange() throws Exception {
        // given
        ArchiveEntry entry = Mockito.mock(ArchiveEntry.class);
        Mockito.when(changeSet.getChanges()).thenReturn(new LinkedHashSet<>(Arrays.asList(
                Mockito.argThat(change -> change.type() == Change.TYPE_DELETE)
        )));
        Mockito.when(entry.getName()).thenReturn("test.txt");
        Mockito.when(inputMock.getNextEntry()).thenReturn(entry);

        // when
        changeSetPerformer.perform(inputMock, outputMock);

        // then
        Mockito.verify(inputMock, Mockito.times(1)).getNextEntry();
        Mockito.verify(changeSet, Mockito.times(1)).getChanges();
        throw new IOException();
    }

    @Test
    public void testPerform_addNonReplaceModeChange() throws Exception {
        // given
        ArchiveEntry entry = Mockito.mock(ArchiveEntry.class);
        Mockito.when(changeSet.getChanges()).thenReturn(new LinkedHashSet<>(Arrays.asList(
                Mockito.argThat(change -> change.type() == Change.TYPE_ADD && !change.isReplaceMode())
        )));
        Mockito.when(entry.getName()).thenReturn("test.txt");
        Mockito.when(inputMock.getNextEntry()).thenReturn(null);

        ByteArrayOutputStream output = new ByteArrayOutputStream();
        Mockito.when(outputMock.getOutputStream()).thenReturn(output);

        // when
        ChangeSetResults results = changeSetPerformer.perform(inputMock, outputMock);

        // then
        Mockito.verify(outputMock, Mockito.times(1)).putArchiveEntry(entry);
        Mockito.verify(outputMock, Mockito.times(1)).closeArchiveEntry();
        Mockito.verify(changeSet, Mockito.times(1)).getChanges();
        assertFalse(results.hasBeenAdded("test.txt"));
        assertTrue(results.addedFromChangeSet("test.txt"));
    }

    @Test
    public void testPerform_entryAlreadyAdded() throws Exception {
        // given
        ArchiveEntry entry = Mockito.mock(ArchiveEntry.class);
        Mockito.when(changeSet.getChanges()).thenReturn(new LinkedHashSet<>(Arrays.asList(
                Mockito.argThat(change -> change.type() == Change.TYPE_ADD && change.isReplaceMode()),
                Mockito.argThat(change -> change.type() == Change.TYPE_ADD && !change.isReplaceMode())
        )));
        Mockito.when(entry.getName()).thenReturn("test.txt");
        Mockito.when(inputMock.getNextEntry()).thenReturn(entry);

        ByteArrayOutputStream output = new ByteArrayOutputStream();
        Mockito.when(outputMock.getOutputStream()).thenReturn(output);

        // when
        ChangeSetResults results = changeSetPerformer.perform(inputMock, outputMock);

        // then
        Mockito.verify(outputMock, Mockito.times(1)).putArchiveEntry(entry);
        Mockito.verify(outputMock, Mockito.times(1)).closeArchiveEntry();
        Mockito.verify(changeSet, Mockito.times(1)).getChanges();
        assertFalse(results.hasBeenAdded("test.txt"));
        assertFalse(results.addedFromChangeSet("test.txt"));
        assertTrue(results.addedFromStream("test.txt"));
    }

    @Test
    public void testPerform_entryDeletedLater() throws Exception {
        // given
        ArchiveEntry entry = Mockito.mock(ArchiveEntry.class);
        Mockito.when(changeSet.getChanges()).thenReturn(new LinkedHashSet<>(Arrays.asList(
                Mockito.argThat(change -> change.type() == Change.TYPE_ADD && change.isReplaceMode()),
                Mockito.argThat(change -> change.type() == Change.TYPE_DELETE && entry.getName().equals(change.targetFile()))
        )));
        Mockito.when(entry.getName()).thenReturn("test.txt");
        Mockito.when(inputMock.getNextEntry()).thenReturn(entry);

        ByteArrayOutputStream output = new ByteArrayOutputStream();
        Mockito.when(outputMock.getOutputStream()).thenReturn(output);

        // when
        ChangeSetResults results = changeSetPerformer.perform(inputMock, outputMock);

        // then
        Mockito.verify(outputMock, Mockito.times(0)).putArchiveEntry(entry);
        Mockito.verify(changeSet, Mockito.times(1)).getChanges();
        assertFalse(results.hasBeenAdded("test.txt"));
        assertFalse(results.addedFromChangeSet("test.txt"));
    }
}