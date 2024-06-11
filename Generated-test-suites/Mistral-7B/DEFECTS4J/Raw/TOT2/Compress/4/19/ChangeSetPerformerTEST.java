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
import java.util.Collections;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;

public class ChangeSetPerformerTest {
    private ChangeSet changeSet;
    private ChangeSetPerformer changeSetPerformer;
    private ArchiveInputStream inputStreamMock;
    private ArchiveOutputStream outputStreamMock;

    @Before
    public void setUp() {
        changeSet = Mockito.mock(ChangeSet.class);
        changeSetPerformer = new ChangeSetPerformer(changeSet);
        inputStreamMock = mock(ArchiveInputStream.class);
        outputStreamMock = mock(ArchiveOutputStream.class);
    }

    @Test
    public void testPerform_NoChanges_DoesNotModifyStreams() throws IOException {
        // Given
        Mockito.when(changeSet.getChanges()).thenReturn(Collections.emptySet());

        // When
        ChangeSetResults result = changeSetPerformer.perform(inputStreamMock, outputStreamMock);

        // Then
        Mockito.verify(inputStreamMock).close();
        Mockito.verify(outputStreamMock).finish();
        assertEquals(ChangeSetResults.EMPTY_RESULT, result);
    }

    @Test
    public void testPerform_AddChange_AddsFileToStream() throws IOException {
        // Given
        ByteArrayInputStream inputStream = new ByteArrayInputStream("Test Data".getBytes());
        ArchiveEntry entry = new ArchiveEntry("test.txt");
        Mockito.when(changeSet.getChanges()).thenReturn(Collections.singleton(new Change(Change.TYPE_ADD, entry, inputStream, null)));

        // When
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ChangeSetResults result = changeSetPerformer.perform(inputStreamMock, outputStreamMock);

        // Then
        Mockito.verify(inputStreamMock).close();
        Mockito.verify(outputStreamMock).finish();
        assertEquals("Test Data", IOUtils.toString(outputStream));
        assertEquals(1, result.getAddedFromChangeSet().size());
        assertEquals("test.txt", result.getAddedFromChangeSet().iterator().next());
    }

    @Test
    public void testPerform_DeleteChange_RemovesFileFromStream() throws IOException {
        // Given
        ByteArrayInputStream inputStream = new ByteArrayInputStream("Test Data".getBytes());
        ArchiveEntry entry = new ArchiveEntry("test.txt");
        Mockito.when(changeSet.getChanges()).thenReturn(Collections.singleton(new Change(Change.TYPE_DELETE, entry, null, null)));

        // When
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ChangeSetResults result = changeSetPerformer.perform(inputStreamMock, outputStreamMock);

        // Then
        Mockito.verify(inputStreamMock, Mockito.times(2)).close();
        Mockito.verify(outputStreamMock).finish();
        assertNull(IOUtils.toString(outputStream));
        assertEquals(1, result.getDeleted().size());
        assertEquals("test.txt", result.getDeleted().iterator().next());
    }

    @Test(expected = IOException.class)
    public void testPerform_ErrorReadingInputStream_ThrowsException() throws IOException {
        // Given
        Mockito.when(changeSet.getChanges()).thenReturn(Collections.emptySet());
        Mockito.when(inputStreamMock.read()).thenThrow(new IOException("Test Error"));

        // When
        ChangeSetResults result = changeSetPerformer.perform(inputStreamMock, outputStreamMock);

        // Then
        Mockito.verify(inputStreamMock).close();
        Mockito.verify(outputStreamMock, Mockito.never()).finish();
        throw new IOException("Test Error");
    }

    @Test
    public void testPerform_ErrorWritingOutputStream_ThrowsException() throws IOException {
        // Given
        Mockito.when(changeSet.getChanges()).thenReturn(Collections.emptySet());
        Mockito.when(outputStreamMock.writeTo(ArgumentMatchers.any(ArchiveEntry.class))).thenThrow(new IOException("Test Error"));

        // When
        ChangeSetResults result = changeSetPerformer.perform(inputStreamMock, outputStreamMock);

        // Then
        Mockito.verify(inputStreamMock).close();
        Mockito.verify(outputStreamMock).finish();
        throw new IOException("Test Error");
    }

    @Test
    public void testPerform_MixedChanges_AddsAndRemovesFilesFromStream() throws IOException {
        // Given
        ByteArrayInputStream inputStream = new ByteArrayInputStream("Test Data 1".getBytes());
        ArchiveEntry entry1 = new ArchiveEntry("test1.txt");
        ByteArrayInputStream inputStream2 = new ByteArrayInputStream("Test Data 2".getBytes());
        ArchiveEntry entry2 = new ArchiveEntry("test2.txt");

        Set<Change> changes = new LinkedHashSet<>();
        changes.add(new Change(Change.TYPE_ADD, entry1, inputStream, null));
        changes.add(new Change(Change.TYPE_DELETE, entry2, null, null));

        Mockito.when(changeSet.getChanges()).thenReturn(changes);

        // When
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ChangeSetResults result = changeSetPerformer.perform(inputStreamMock, outputStreamMock);

        // Then
        Mockito.verify(inputStreamMock, Mockito.atLeastOnce()).close();
        Mockito.verify(outputStreamMock).finish();
        assertNull(IOUtils.toString(outputStream));
        assertEquals(1, result.getAddedFromChangeSet().size());
        assertEquals("test1.txt", result.getAddedFromChangeSet().iterator().next());
        assertEquals(1, result.getDeleted().size());
        assertEquals("test2.txt", result.getDeleted().iterator().next());
    }
}