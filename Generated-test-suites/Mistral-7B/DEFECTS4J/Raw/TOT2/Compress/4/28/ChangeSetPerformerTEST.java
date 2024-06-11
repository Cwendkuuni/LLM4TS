import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.archivers.ArchiveOutputStream;
import org.apache.commons.compress.utils.IOUtils;
import org.apache.commons.compress.utils.TestUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.*;

@RunWith(MockitoJUnitRunner.class)
public class ChangeSetPerformerTest {
    private ChangeSetPerformer performer;
    private ChangeSet changeSet;
    private ArchiveInputStream inputMock;
    private ArchiveOutputStream outputMock;

    @Before
    public void setUp() {
        changeSet = new ChangeSet();
        performer = new ChangeSetPerformer(changeSet);
        inputMock = Mockito.mock(ArchiveInputStream.class);
        outputMock = Mockito.mock(ArchiveOutputStream.class);
    }

    @Test
    public void testPerform_BasicUseCase() throws IOException {
        // Given
        Set<Change> changes = new HashSet<>();
        Change addChange = new AddChange("testFile.txt", new ByteArrayInputStream(new byte[0]), null);
        Change deleteChange = new DeleteChange("testFile.txt", null, null);
        changes.add(addChange);
        changes.add(deleteChange);
        changeSet.setChanges(changes);
        Mockito.when(inputMock.getNextEntry()).thenReturn(new ArchiveEntry("testFile.txt"));
        Mockito.when(outputMock.putArchiveEntry(ArgumentMatchers.any(ArchiveEntry.class))).thenReturn(true);
        Mockito.when(outputMock.closeArchiveEntry()).thenReturn(true);
        Mockito.when(outputMock.finishStream()).thenReturn(true);

        // When
        ChangeSetResults results = performer.perform(inputMock, outputMock);

        // Then
        assertTrue(results.isSuccess());
        assertTrue(results.containsResult("testFile.txt", ChangeSetResults.ResultType.ADDED_FROM_CHANGE_SET));
        assertTrue(results.containsResult("testFile.txt", ChangeSetResults.ResultType.DELETED));
        Mockito.verify(outputMock, Mockito.atLeastOnce()).putArchiveEntry(ArgumentMatchers.any(ArchiveEntry.class));
        Mockito.verify(outputMock, Mockito.atLeastOnce()).finishStream();
    }

    @Test
    public void testPerform_NoChanges() throws IOException {
        // Given
        Set<Change> changes = new HashSet<>();
        changeSet.setChanges(changes);
        Mockito.when(inputMock.getNextEntry()).thenReturn(null);

        // When
        ChangeSetResults results = performer.perform(inputMock, outputMock);

        // Then
        assertTrue(results.isSuccess());
        assertTrue(results.isEmpty());
        Mockito.verify(outputMock, Mockito.never()).putArchiveEntry(ArgumentMatchers.any(ArchiveEntry.class));
    }

    @Test
    public void testIsDeletedLater_BasicUseCase() {
        // Given
        Set<Change> changes = new HashSet<>();
        Change addChange = new AddChange("testFile.txt", null, null);
        Change deleteChange1 = new DeleteChange("testFile.txt", null, null);
        Change deleteChange2 = new DeleteChange("testFile.txt", null, null);
        changes.add(addChange);
        changes.add(deleteChange1);
        changes.add(deleteChange2);

        // When
        boolean result = performer.isDeletedLater(changes, new ArchiveEntry("testFile.txt"));

        // Then
        assertTrue(result);
    }

    @Test
    public void testIsDeletedLater_NoMatch() {
        // Given
        Set<Change> changes = new HashSet<>();
        Change addChange = new AddChange("testFile.txt", null, null);
        Change deleteChange = new DeleteChange("testDir/testFile.txt", null, null);
        changes.add(addChange);
        changes.add(deleteChange);

        // When
        boolean result = performer.isDeletedLater(changes, new ArchiveEntry("testFile.txt"));

        // Then
        assertFalse(result);
    }

    @Test(expected = IOException.class)
    public void testPerform_IOException() throws IOException {
        // Given
        Set<Change> changes = new HashSet<>();
        Change addChange = new AddChange("testFile.txt", new ByteArrayInputStream(new byte[0]), null);
        Change deleteChange = new DeleteChange("testFile.txt", null, null);
        changes.add(addChange);
        changes.add(deleteChange);
        changeSet.setChanges(changes);
        Mockito.when(inputMock.getNextEntry()).thenThrow(new IOException());

        // When & Then
        performer.perform(inputMock, outputMock);
        fail("An IOException should be thrown");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testPerform_NullInput() {
        // Given
        Set<Change> changes = new HashSet<>();
        changeSet.setChanges(changes);
        Mockito.when(inputMock.getNextEntry()).thenReturn(null);

        // When & Then
        try {
            performer.perform(null, outputMock);
        } catch (IOException e) {
            // Ignore
        }
        assertThrows(IllegalArgumentException.class, () -> performer.perform(inputMock, null));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testPerform_NullOutput() {
        // Given
        Set<Change> changes = new HashSet<>();
        changeSet.setChanges(changes);
        ArchiveOutputStream output = null;
        Mockito.when(inputMock.getNextEntry()).thenReturn(new ArchiveEntry("testFile.txt"));

        // When & Then
        try {
            performer.perform(inputMock, output);
        } catch (IOException e) {
            // Ignore
        }
        assertThrows(IllegalArgumentException.class, () -> performer.perform(inputMock, outputMock));
    }

    @Test(expected = IllegalStateException.class)
    public void testPerform_FinishedStream() throws IOException {
        // Given
        Set<Change> changes = new HashSet<>();
        Change addChange = new AddChange("testFile.txt", new ByteArrayInputStream(new byte[0]), null);
        Change deleteChange = new DeleteChange("testFile.txt", null, null);
        changes.add(addChange);
        changes.add(deleteChange);
        changeSet.setChanges(changes);
        Mockito.when(inputMock.getNextEntry()).thenReturn(new ArchiveEntry("testFile.txt"));

        // When
        performer.perform(inputMock, outputMock);
        performer.perform(inputMock, outputMock);

        // Then
        Mockito.verify(outputMock, Mockito.times(1)).putArchiveEntry(ArgumentMatchers.any(ArchiveEntry.class));
        Mockito.verify(outputMock, Mockito.times(1)).finishStream();
        assertThrows(IllegalStateException.class, () -> performer.perform(inputMock, outputMock));
    }
}