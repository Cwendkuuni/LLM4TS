package org.apache.commons.compress.changes; // make sure to import any necessary classes

import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.archivers.ArchiveOutputStream;
import org.apache.commons.compress.utils.IOUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.io.*;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;

public class ChangeSetPerformerTest {
    private ChangeSet changeSet;
    private ChangeSetPerformer performer;
    private ArchiveInputStream inputMock;
    private ArchiveOutputStream outputMock;

    @Before
    public void setUp() {
        changeSet = Mockito.mock(ChangeSet.class);
        performer = new ChangeSetPerformer(changeSet);

        inputMock = Mockito.mock(ArchiveInputStream.class);
        outputMock = Mockito.mock(ArchiveOutputStream.class);
    }

    @Test
    public void testPerform_EmptyChangeSet() throws Exception {
        Set<Change> emptySet = Collections.emptySet();
        Mockito.when(changeSet.getChanges()).thenReturn(emptySet);

        ChangeSetResults result = performer.perform(inputMock, outputMock);
        Assert.assertNotNull(result);
        Assert.assertTrue(result.isEmpty());
    }

    @Test
    public void testPerform_BasicAddition() throws Exception {
        Set<Change> changes = new LinkedHashSet<>();
        Change addChange = new Change("test", Change.TYPE_ADD, "test.txt", null, null, null);
        changes.add(addChange);
        Mockito.when(changeSet.getChanges()).thenReturn(changes);

        InputStream inputMockSource = Mockito.mock(InputStream.class);
        ArchiveEntry entryMock = Mockito.mock(ArchiveEntry.class);
        Mockito.when(inputMock.getNextEntry()).thenReturn(entryMock);
        Mockito.when(inputMock.readAllBytes()).thenAnswer(new Answer<byte[]>() {
            @Override
            public byte[] answer(InvocationOnMock invocation) throws IOException {
                return new byte[0];
            }
        });

        Mockito.when(changeSet.getChangeByName(ArgumentMatchers.eq("test"))).thenReturn(addChange);

        ChangeSetResults result = performer.perform(inputMock, outputMock);
        Assert.assertEquals(1, result.getAdded().size());
        Assert.assertTrue(result.getAdded().contains("test.txt"));
    }

    @Test
    public void testPerform_BasicDeletion() throws Exception {
        Set<Change> changes = new LinkedHashSet<>();
        Change deleteChange = new Change("test", Change.TYPE_DELETE, null, null, "test.txt", null);
        changes.add(deleteChange);
        Mockito.when(changeSet.getChanges()).thenReturn(changes);

        ArchiveEntry entryMock = Mockito.mock(ArchiveEntry.class);
        Mockito.when(inputMock.getNextEntry()).thenReturn(entryMock);
        Mockito.when(inputMock.readAllBytes()).thenAnswer(new Answer<byte[]>() {
            @Override
            public byte[] answer(InvocationOnMock invocation) throws IOException {
                return new byte[0];
            }
        });

        Change setChange = Mockito.mock(Change.class);
        Mockito.when(changeSet.getChangeByName("test")).thenReturn(setChange);
        Mockito.when(setChange.type()).thenReturn(Change.TYPE_ADD);

        Mockito.doNothing().when(performer).copyStream(ArgumentMatchers.any(InputStream.class), ArgumentMatchers.any(ArchiveOutputStream.class), ArgumentMatchers.any(ArchiveEntry.class));

        ChangeSetResults result = performer.perform(inputMock, outputMock);
        Assert.assertEquals(0, result.getAdded().size());
        Assert.assertEquals(1, result.getDeleted().size());
        Assert.assertTrue(result.getDeleted().contains("test.txt"));
    }

    @Test(expected = IOException.class)
    public void testPerform_IOException() throws Exception {
        Set<Change> changes = new LinkedHashSet<>();
        Change addChange = new Change("test", Change.TYPE_ADD, "test.txt", null, null, null);
        changes.add(addChange);
        Mockito.when(changeSet.getChanges()).thenReturn(changes);

        InputStream inputMockSource = Mockito.mock(InputStream.class);
        ArchiveEntry entryMock = Mockito.mock(ArchiveEntry.class);
        Mockito.when(inputMock.getNextEntry()).thenReturn(entryMock);
        Mockito.when(inputMock.readAllBytes()).thenThrow(new IOException());

        ChangeSetResults result = performer.perform(inputMock, outputMock);
        Assert.fail("Expected IOException but got " + result);
    }
}