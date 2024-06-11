import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.archivers.ArchiveOutputStream;
import org.apache.commons.compress.utils.IOUtils;
import org.apache.commons.compress.change.Change;
import org.apache.commons.compress.change.ChangeSet;
import org.apache.commons.compress.change.ChangeSetResults;
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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class ChangeSetPerformerTest {

    private ChangeSet changeSet;
    private ChangeSetPerformer performer;
    private ArchiveInputStream inputStreamMock;
    private ArchiveOutputStream outputStreamMock;

    @Before
    public void setup() {
        changeSet = Mockito.spy(new ChangeSet(Arrays.asList(
                new Change("file1.txt", Change.TYPE_ADD, "file1.txt", null),
                new Change("file2.txt", Change.TYPE_DELETE, null, "file2.txt"))));
        performer = new ChangeSetPerformer(changeSet);

        inputStreamMock = Mockito.mock(ArchiveInputStream.class);
        outputStreamMock = Mockito.mock(ArchiveOutputStream.class);
    }

    @Test
    public void testPerformAddFileFromChangeSet() throws IOException {
        // Given
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        Mockito.when(outputStreamMock.getArchiveEntry()).thenReturn(new ArchiveEntry("file1.txt"));
        Mockito.when(outputStreamMock.writeTo(ArgumentMatchers.anyOutputStream())).thenReturn(0);
        Mockito.when(outputStreamMock.close()).thenReturn(true);

        // When
        ChangeSetResults result = performer.perform(inputStreamMock, outputStreamMock);

        // Then
        Mockito.verify(outputStreamMock, Mockito.times(1)).writeTo(ArgumentMatchers.isA(OutputStream.class));
        assertEquals(ChangeSetResults.ChangeStatus.ADDED, result.statusForEntry("file1.txt"));
    }

    @Test
    public void testPerformDeleteFileFromChangeSet() throws IOException {
        // Given
        ByteArrayInputStream inputStream = new ByteArrayInputStream("Test Data".getBytes());
        Mockito.when(inputStreamMock.getNextEntry()).thenReturn(new ArchiveEntry("file2.txt"));
        Mockito.when(inputStreamMock.readAllBytes()).thenReturn(new byte[0]);
        Mockito.when(inputStreamMock.close()).thenReturn(true);

        // When
        performer.perform(inputStreamMock, outputStreamMock);

        // Then
        Mockito.verify(outputStreamMock, Mockito.never()).writeTo(ArgumentMatchers.isA(OutputStream.class));
        assertNull(changeSet.getChangeByTarget("file2.txt"));
    }

    @Test(expected = IOException.class)
    public void testPerformFileIsDeletedLater() throws IOException {
        // Given
        ByteArrayInputStream inputStream = new ByteArrayInputStream("Test Data".getBytes());
        Mockito.when(inputStreamMock.getNextEntry()).thenReturn(new ArchiveEntry("file1.txt"));
        Mockito.when(inputStreamMock.readAllBytes()).thenReturn(new byte[0]);
        Mockito.when(inputStreamMock.close()).thenReturn(true);

        // When
        performer.perform(inputStreamMock, outputStreamMock);

        // Then
        Mockito.verify(inputStreamMock, Mockito.times(1)).close();
        Mockito.verify(outputStreamMock, Mockito.never()).writeTo(ArgumentMatchers.isA(OutputStream.class));
        assertEquals(ChangeSetResults.ChangeStatus.DELETED, performer.getResults().statusForEntry("file1.txt"));

        // Given - Simulate that file1.txt is added again
        Mockito.when(inputStreamMock.getNextEntry()).thenReturn(new ArchiveEntry("file1.txt"));
        Mockito.when(inputStreamMock.readAllBytes()).thenReturn(new byte[0]);
        Mockito.when(inputStreamMock.close()).thenReturn(true);

        // When
        performer.perform(inputStreamMock, outputStreamMock);

        // Then
        Mockito.verify(inputStreamMock, Mockito.times(2)).close();
        Mockito.verify(outputStreamMock, Mockito.times(1)).writeTo(ArgumentMatchers.isA(OutputStream.class));
        assertEquals(ChangeSetResults.ChangeStatus.ADDED, performer.getResults().statusForEntry("file1.txt"));
    }

    @Test
    public void testPerformAddFileFromStream() throws IOException {
        // Given
        ByteArrayOutputStream inputStream = new ByteArrayOutputStream();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        Mockito.when(inputStreamMock.getArchiveEntry()).thenReturn(new ArchiveEntry("file1.txt"));
        Mockito.when(outputStreamMock.getArchiveEntry()).thenReturn(new ArchiveEntry("file1.txt"));
        Mockito.when(outputStreamMock.writeTo(ArgumentMatchers.anyOutputStream())).thenReturn(0);
        Mockito.when(outputStreamMock.close()).thenReturn(true);
        Mockito.when(inputStreamMock.readAllBytes()).thenReturn(new byte[0]);
        Mockito.when(inputStreamMock.close()).thenReturn(true);

        // When
        performer.perform(inputStreamMock, outputStreamMock);

        // Then
        Mockito.verify(outputStreamMock, Mockito.times(1)).writeTo(ArgumentMatchers.isA(OutputStream.class));
        Mockito.verify(inputStreamMock, Mockito.never()).close();
        assertEquals(ChangeSetResults.ChangeStatus.ADDED, performer.getResults().statusForEntry("file1.txt"));
    }

    @Test
    public void testPerformFileIsAlreadyAdded() throws IOException {
        // Given
        ByteArrayInputStream inputStream = new ByteArrayInputStream("Test Data".getBytes());
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        Mockito.when(inputStreamMock.getNextEntry()).thenReturn(new ArchiveEntry("file1.txt"));
        Mockito.when(inputStreamMock.readAllBytes()).thenReturn(new byte[0]);
        Mockito.when(outputStreamMock.getArchiveEntry()).thenReturn(new ArchiveEntry("file1.txt"));
        Mockito.when(outputStreamMock.writeTo(ArgumentMatchers.anyOutputStream())).thenReturn(0);
        Mockito.when(outputStreamMock.close()).thenReturn(true);
        Mockito.when(inputStreamMock.close()).thenReturn(true);

        performer.perform(inputStreamMock, outputStreamMock);
        performer.perform(inputStreamMock, outputStreamMock);

        // Then
        Mockito.verify(inputStreamMock, Mockito.times(1)).close();
        Mockito.verify(outputStreamMock, Mockito.times(1)).writeTo(ArgumentMatchers.isA(OutputStream.class));
        assertEquals(ChangeSetResults.ChangeStatus.ADDED, performer.getResults().statusForEntry("file1.txt"));
    }

    @Test
    public void testPerformDirectoryIsDeleted() throws IOException {
        // Given
        ByteArrayInputStream inputStream = new ByteArrayInputStream("Test Data".getBytes());
        Mockito.when(inputStreamMock.getNextEntry()).thenReturn(new ArchiveEntry("dir1/file1.txt"));
        Mockito.when(inputStreamMock.getNextEntry()).thenReturn(new ArchiveEntry("dir1/file2.txt"));
        Mockito.when(inputStreamMock.readAllBytes()).thenReturn(new byte[0]);
        Mockito.when(inputStreamMock.close()).thenReturn(true);

        // When
        performer.perform(inputStreamMock, outputStreamMock);

        // Then
        Mockito.verify(outputStreamMock, Mockito.never()).writeTo(ArgumentMatchers.isA(OutputStream.class));
        assertNull(changeSet.getChangeByTarget("dir1"));
    }

    @Test
    public void testPerformDirectoryIsAdded() throws IOException {
        // Given
        ByteArrayInputStream inputStream = new ByteArrayInputStream("Test Data".getBytes());
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        Mockito.when(inputStreamMock.getNextEntry()).thenReturn(new ArchiveEntry("dir1/"));
        Mockito.when(inputStreamMock.readAllBytes()).thenReturn(new byte[0]);
        Mockito.when(outputStreamMock.getArchiveEntry()).thenReturn(new ArchiveEntry("dir1/"));
        Mockito.when(outputStreamMock.writeTo(ArgumentMatchers.anyOutputStream())).thenReturn(0);
        Mockito.when(outputStreamMock.close()).thenReturn(true);
        Mockito.when(inputStreamMock.close()).thenReturn(true);

        // When
        performer.perform(inputStreamMock, outputStreamMock);

        // Then
        Mockito.verify(outputStreamMock, Mockito.times(1)).writeTo(ArgumentMatchers.isA(OutputStream.class));
        Mockito.verify(inputStreamMock, Mockito.never()).close();
        assertEquals(ChangeSetResults.ChangeStatus.ADDED, performer.getResults().statusForEntry("dir1"));
    }
}