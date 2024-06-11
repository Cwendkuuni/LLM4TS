import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.io.*;
import java.util.LinkedHashSet;
import java.util.Set;

public class ChangeSetPerformerTest {
    
    @Mock
    private ArchiveInputStream mockArchiveInputStream;
    @Mock
    private ArchiveOutputStream mockArchiveOutputStream;
    @Mock
    private ChangeSet mockChangeSet;
    @Mock
    private Change mockChange;
    @Mock
    private ChangeSetResults mockChangeSetResults;
    @Mock
    private InputStream mockInputStream;
    @Mock
    private ArchiveEntry mockArchiveEntry;
    
    private ChangeSetPerformer changeSetPerformer;
    
    @Before
    public void setUp() {
        mockArchiveInputStream = Mockito.mock(ArchiveInputStream.class);
        mockArchiveOutputStream = Mockito.mock(ArchiveOutputStream.class);
        mockChangeSet = Mockito.mock(ChangeSet.class);
        mockChange = Mockito.mock(Change.class);
        mockChangeSetResults = Mockito.mock(ChangeSetResults.class);
        mockInputStream = Mockito.mock(InputStream.class);
        mockArchiveEntry = Mockito.mock(ArchiveEntry.class);
        
        Set<Change> changes = new LinkedHashSet<>();
        changes.add(mockChange);
        
        Mockito.when(mockChangeSet.getChanges()).thenReturn(changes);
        changeSetPerformer = new ChangeSetPerformer(mockChangeSet);
    }
    
    @Test
    public void testPerform() throws IOException {
        Mockito.when(mockChange.type()).thenReturn(Change.TYPE_ADD);
        Mockito.when(mockChange.isReplaceMode()).thenReturn(true);
        Mockito.when(mockChange.getInput()).thenReturn(mockInputStream);
        Mockito.when(mockChange.getEntry()).thenReturn(mockArchiveEntry);
        
        Mockito.when(mockArchiveInputStream.getNextEntry()).thenReturn(mockArchiveEntry, null);
        Mockito.when(mockArchiveEntry.getName()).thenReturn("file1");
        Mockito.when(mockChange.targetFile()).thenReturn("file1");
        Mockito.when(mockChange.type()).thenReturn(Change.TYPE_DELETE);
        Mockito.when(mockChange.targetFile()).thenReturn("file1");
        
        ChangeSetResults result = changeSetPerformer.perform(mockArchiveInputStream, mockArchiveOutputStream);
        
        Mockito.verify(mockArchiveOutputStream).putArchiveEntry(mockArchiveEntry);
        Mockito.verify(mockArchiveOutputStream).closeArchiveEntry();
        Mockito.verify(mockChangeSetResults).addedFromChangeSet("file1");
        Mockito.verify(mockChangeSetResults).deleted("file1");
        Mockito.verify(mockChangeSetResults).addedFromStream("file1");
        Mockito.assertFalse(result.hasBeenAdded("file1"));
    }
    
    @Test
    public void testPerform_WithDeleteDirChange() throws IOException {
        Mockito.when(mockChange.type()).thenReturn(Change.TYPE_ADD);
        Mockito.when(mockChange.isReplaceMode()).thenReturn(true);
        Mockito.when(mockChange.getInput()).thenReturn(mockInputStream);
        Mockito.when(mockChange.getEntry()).thenReturn(mockArchiveEntry);
        
        Mockito.when(mockArchiveInputStream.getNextEntry()).thenReturn(mockArchiveEntry, null);
        Mockito.when(mockArchiveEntry.getName()).thenReturn("dir/file1");
        Mockito.when(mockChange.targetFile()).thenReturn("dir");
        Mockito.when(mockChange.type()).thenReturn(Change.TYPE_DELETE_DIR);
        Mockito.when(mockChange.targetFile()).thenReturn("dir");
        
        ChangeSetResults result = changeSetPerformer.perform(mockArchiveInputStream, mockArchiveOutputStream);
        
        Mockito.verify(mockArchiveOutputStream).putArchiveEntry(mockArchiveEntry);
        Mockito.verify(mockArchiveOutputStream).closeArchiveEntry();
        Mockito.verify(mockChangeSetResults).addedFromChangeSet("dir/file1");
        Mockito.verify(mockChangeSetResults).deleted("dir/file1");
        Mockito.verify(mockChangeSetResults).addedFromStream("dir/file1");
        Mockito.assertFalse(result.hasBeenAdded("dir/file1"));
    }
    
    @Test
    public void testPerform_WithNoChanges() throws IOException {
        Mockito.when(mockArchiveInputStream.getNextEntry()).thenReturn(null);
        
        ChangeSetResults result = changeSetPerformer.perform(mockArchiveInputStream, mockArchiveOutputStream);
        
        Mockito.verify(mockArchiveOutputStream, Mockito.never()).putArchiveEntry(Mockito.any(ArchiveEntry.class));
        Mockito.verify(mockArchiveOutputStream, Mockito.never()).closeArchiveEntry();
        Mockito.verify(mockChangeSetResults, Mockito.never()).addedFromChangeSet(Mockito.anyString());
        Mockito.verify(mockChangeSetResults, Mockito.never()).deleted(Mockito.anyString());
        Mockito.verify(mockChangeSetResults, Mockito.never()).addedFromStream(Mockito.anyString());
        assertTrue(result.isEmpty());
    }
    
    @Test
    public void testPerform_WithNoChangesInChangeSet() throws IOException {
        Set<Change> changes = new LinkedHashSet<>();
        Mockito.when(mockChangeSet.getChanges()).thenReturn(changes);
        
        Mockito.when(mockArchiveInputStream.getNextEntry()).thenReturn(mockArchiveEntry, null);
        Mockito.when(mockArchiveEntry.getName()).thenReturn("file1");
        
        ChangeSetResults result = changeSetPerformer.perform(mockArchiveInputStream, mockArchiveOutputStream);
        
        Mockito.verify(mockArchiveOutputStream, Mockito.never()).putArchiveEntry(Mockito.any(ArchiveEntry.class));
        Mockito.verify(mockArchiveOutputStream, Mockito.never()).closeArchiveEntry();
        Mockito.verify(mockChangeSetResults, Mockito.never()).addedFromChangeSet(Mockito.anyString());
        Mockito.verify(mockChangeSetResults, Mockito.never()).deleted(Mockito.anyString());
        Mockito.verify(mockChangeSetResults, Mockito.never()).addedFromStream(Mockito.anyString());
        assertTrue(result.isEmpty());
    }
    
    @Test(expected = IOException.class)
    public void testPerform_WithIOException() throws IOException {
        Mockito.when(mockChange.type()).thenReturn(Change.TYPE_ADD);
        Mockito.when(mockChange.isReplaceMode()).thenReturn(true);
        Mockito.when(mockChange.getInput()).thenReturn(mockInputStream);
        Mockito.when(mockChange.getEntry()).thenReturn(mockArchiveEntry);
        
        Mockito.when(mockArchiveInputStream.getNextEntry()).thenReturn(mockArchiveEntry);
        Mockito.when(mockArchiveEntry.getName()).thenReturn("file1");
        Mockito.when(mockChange.targetFile()).thenReturn("file1");
        
        Mockito.doThrow(new IOException()).when(mockArchiveOutputStream).putArchiveEntry(mockArchiveEntry);
        
        changeSetPerformer.perform(mockArchiveInputStream, mockArchiveOutputStream);
    }
    
    @Test
    public void testIsDeletedLater_WhenDeletedLater_ReturnsTrue() {
        Set<Change> workingSet = new LinkedHashSet<>();
        workingSet.add(mockChange);
        
        Mockito.when(mockChange.type()).thenReturn(Change.TYPE_DELETE);
        Mockito.when(mockChange.targetFile()).thenReturn("file1");
        Mockito.when(mockArchiveEntry.getName()).thenReturn("file1");
        
        assertTrue(changeSetPerformer.isDeletedLater(workingSet, mockArchiveEntry));
    }
    
    @Test
    public void testIsDeletedLater_WhenDeletedDirLater_ReturnsTrue() {
        Set<Change> workingSet = new LinkedHashSet<>();
        workingSet.add(mockChange);
        
        Mockito.when(mockChange.type()).thenReturn(Change.TYPE_DELETE_DIR);
        Mockito.when(mockChange.targetFile()).thenReturn("dir");
        Mockito.when(mockArchiveEntry.getName()).thenReturn("dir/file1");
        
        assertTrue(changeSetPerformer.isDeletedLater(workingSet, mockArchiveEntry));
    }
    
    @Test
    public void testIsDeletedLater_WhenNoDeletionLater_ReturnsFalse() {
        Set<Change> workingSet = new LinkedHashSet<>();
        workingSet.add(mockChange);
        
        Mockito.when(mockChange.type()).thenReturn(Change.TYPE_DELETE);
        Mockito.when(mockChange.targetFile()).thenReturn("file1");
        Mockito.when(mockArchiveEntry.getName()).thenReturn("file2");
        
        assertFalse(changeSetPerformer.isDeletedLater(workingSet, mockArchiveEntry));
    }
    
    @Test
    public void testCopyStream() throws IOException {
        changeSetPerformer.copyStream(mockInputStream, mockArchiveOutputStream, mockArchiveEntry);
        Mockito.verify(mockArchiveOutputStream).putArchiveEntry(mockArchiveEntry);
        Mockito.verify(mockArchiveOutputStream).closeArchiveEntry();
        Mockito.verify(mockInputStream).close();
        Mockito.verify(mockArchiveOutputStream).flush();
    }
}