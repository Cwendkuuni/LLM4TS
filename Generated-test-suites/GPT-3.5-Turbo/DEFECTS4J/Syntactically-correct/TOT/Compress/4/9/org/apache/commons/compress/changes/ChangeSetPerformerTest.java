import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedHashSet;
import java.util.Set;
import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.archivers.ArchiveOutputStream;
import org.apache.commons.compress.changes.Change;
import org.apache.commons.compress.changes.ChangeSet;
import org.apache.commons.compress.changes.ChangeSetPerformer;
import org.apache.commons.compress.changes.ChangeSetResults;
import org.apache.commons.compress.utils.IOUtils;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class ChangeSetPerformerTest {
    private ChangeSetPerformer performer;
    
    @Before
    public void setUp() {
        ChangeSet changeSet = mock(ChangeSet.class);
        when(changeSet.getChanges()).thenReturn(new LinkedHashSet<>());
        performer = new ChangeSetPerformer(changeSet);
    }
    
    @Test
    public void testPerform_addReplaceMode_shouldPerformChangeAndAddToResults() throws IOException {
        // Arrange
        ArchiveInputStream in = mock(ArchiveInputStream.class);
        ArchiveOutputStream out = mock(ArchiveOutputStream.class);
        ChangeSetResults results = mock(ChangeSetResults.class);
        Change change = mock(Change.class);
        when(change.type()).thenReturn(Change.TYPE_ADD);
        when(change.isReplaceMode()).thenReturn(true);
        when(change.getInput()).thenReturn(mock(InputStream.class));
        when(change.getEntry()).thenReturn(mock(ArchiveEntry.class));
        Set<Change> changes = new LinkedHashSet<>();
        changes.add(change);
        when(results.hasBeenAdded(any(String.class))).thenReturn(false);
        when(results.addedFromChangeSet(any(String.class))).thenReturn(false);
        when(results.addedFromChangeSet(any(String.class))).thenReturn(results);
        ChangeSet changeSet = mock(ChangeSet.class);
        when(changeSet.getChanges()).thenReturn(changes);
        performer = new ChangeSetPerformer(changeSet);
        IOUtils.copy(Mockito.any(InputStream.class), Mockito.any(ArchiveOutputStream.class));

        // Act
        ChangeSetResults result = performer.perform(in, out);

        // Assert
        assertEquals(result, results);
        verify(changeSet, times(1)).getChanges();
        verify(change, times(1)).type();
        verify(change, times(1)).isReplaceMode();
        verify(change, times(1)).getInput();
        verify(change, times(1)).getEntry();
        verify(change, times(1)).type();
        verify(results, times(2)).addedFromChangeSet(any(String.class));
        verify(IOUtils, times(1)).copy(Mockito.any(InputStream.class), Mockito.any(ArchiveOutputStream.class));
    }
    
    @Test
    public void testPerform_deleteEntry_shouldPerformChangeAndAddToResults() throws IOException {
        // Arrange
        ArchiveInputStream in = mock(ArchiveInputStream.class);
        ArchiveOutputStream out = mock(ArchiveOutputStream.class);
        ChangeSetResults results = mock(ChangeSetResults.class);
        Change change = mock(Change.class);
        ArchiveEntry entry = mock(ArchiveEntry.class);
        when(entry.getName()).thenReturn("entryName");
        when(change.type()).thenReturn(Change.TYPE_DELETE);
        when(change.targetFile()).thenReturn("entryName");
        Set<Change> changes = new LinkedHashSet<>();
        changes.add(change);
        when(results.hasBeenAdded(any(String.class))).thenReturn(false);
        when(results.deleted(any(String.class))).thenReturn(false);
        when(results.deleted(any(String.class))).thenReturn(results);
        ChangeSet changeSet = mock(ChangeSet.class);
        when(changeSet.getChanges()).thenReturn(changes);
        performer = new ChangeSetPerformer(changeSet);

        // Act
        ChangeSetResults result = performer.perform(in, out);
        
        // Assert
        assertEquals(result, results);
        verify(changeSet, times(1)).getChanges();
        verify(change, times(1)).type();
        verify(entry, times(1)).getName();
        verify(change, times(1)).targetFile();
        verify(results, times(2)).deleted(any(String.class));
    }
    
    @Test
    public void testPerform_deleteDirectory_shouldPerformChangeAndAddToResults() throws IOException {
        // Arrange
        ArchiveInputStream in = mock(ArchiveInputStream.class);
        ArchiveOutputStream out = mock(ArchiveOutputStream.class);
        ChangeSetResults results = mock(ChangeSetResults.class);
        Change change = mock(Change.class);
        ArchiveEntry entry = mock(ArchiveEntry.class);
        when(entry.getName()).thenReturn("dir/entryName");
        when(change.type()).thenReturn(Change.TYPE_DELETE_DIR);
        when(change.targetFile()).thenReturn("dir");
        Set<Change> changes = new LinkedHashSet<>();
        changes.add(change);
        when(results.hasBeenAdded(any(String.class))).thenReturn(false);
        when(results.deleted(any(String.class))).thenReturn(false);
        when(results.deleted(any(String.class))).thenReturn(results);
        ChangeSet changeSet = mock(ChangeSet.class);
        when(changeSet.getChanges()).thenReturn(changes);
        performer = new ChangeSetPerformer(changeSet);

        // Act
        ChangeSetResults result = performer.perform(in, out);
        
        // Assert
        assertEquals(result, results);
        verify(changeSet, times(1)).getChanges();
        verify(change, times(1)).type();
        verify(entry, times(1)).getName();
        verify(change, times(1)).targetFile();
        verify(results, times(2)).deleted(any(String.class));
    }
    
    @Test
    public void testIsDeletedLater_entryIsNull_shouldReturnFalse() {
        // Arrange
        Set<Change> workingSet = new LinkedHashSet<>();
        ArchiveEntry entry = null;

        // Act
        boolean result = performer.isDeletedLater(workingSet, entry);

        // Assert
        assertFalse(result);
    }
    
    @Test
    public void testIsDeletedLater_noWorkingSet_shouldReturnFalse() {
        // Arrange
        Set<Change> workingSet = new LinkedHashSet<>();
        ArchiveEntry entry = mock(ArchiveEntry.class);

        // Act
        boolean result = performer.isDeletedLater(workingSet, entry);

        // Assert
        assertFalse(result);
    }
    
    @Test
    public void testIsDeletedLater_entryNotDeletedLater_shouldReturnFalse() {
        // Arrange
        Set<Change> workingSet = new LinkedHashSet<>();
        workingSet.add(mock(Change.class));
        ArchiveEntry entry = mock(ArchiveEntry.class);

        // Act
        boolean result = performer.isDeletedLater(workingSet, entry);

        // Assert
        assertFalse(result);
    }
    
    @Test
    public void testIsDeletedLater_entryDeletedLater_shouldReturnTrue() {
        // Arrange
        Set<Change> workingSet = new LinkedHashSet<>();
        Change change = mock(Change.class);
        ArchiveEntry entry = mock(ArchiveEntry.class);
        when(entry.getName()).thenReturn("entryName");
        when(change.type()).thenReturn(Change.TYPE_DELETE);
        when(change.targetFile()).thenReturn("entryName");
        workingSet.add(change);

        // Act
        boolean result = performer.isDeletedLater(workingSet, entry);

        // Assert
        assertTrue(result);
    }
    
    @Test
    public void testIsDeletedLater_entryDeletedDirectoryLater_shouldReturnTrue() {
        // Arrange
        Set<Change> workingSet = new LinkedHashSet<>();
        Change change = mock(Change.class);
        ArchiveEntry entry = mock(ArchiveEntry.class);
        when(entry.getName()).thenReturn("dir/entryName");
        when(change.type()).thenReturn(Change.TYPE_DELETE_DIR);
        when(change.targetFile()).thenReturn("dir");
        workingSet.add(change);

        // Act
        boolean result = performer.isDeletedLater(workingSet, entry);

        // Assert
        assertTrue(result);
    }
    
    @Test
    public void testCopyStream_shouldCopyStreamToOutputStream() throws IOException {
        // Arrange
        InputStream in = mock(InputStream.class);
        ArchiveOutputStream out = mock(ArchiveOutputStream.class);
        ArchiveEntry entry = mock(ArchiveEntry.class);
        doNothing().when(out).putArchiveEntry(Mockito.any(ArchiveEntry.class));
        doNothing().when(IOUtils).copy(Mockito.any(InputStream.class), Mockito.any(ArchiveOutputStream.class));
        doNothing().when(out).closeArchiveEntry();

        // Act
        performer.copyStream(in, out, entry);

        // Assert
        verify(out, times(1)).putArchiveEntry(Mockito.any(ArchiveEntry.class));
        verify(IOUtils, times(1)).copy(Mockito.any(InputStream.class), Mockito.any(ArchiveOutputStream.class));
        verify(out, times(1)).closeArchiveEntry();
    }
}