import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.archivers.ArchiveOutputStream;
import org.junit.Before;
import org.junit.Test;

public class ChangeSetPerformerTest {
    
    private ChangeSetPerformer changeSetPerformer;
    private ArchiveInputStream mockArchiveInputStream;
    private ArchiveOutputStream mockArchiveOutputStream;
    
    @Before
    public void setUp() {
        ChangeSet changeSet = new ChangeSet();
        changeSet.addChange(new Change("file1.txt", Change.TYPE_ADD));
        changeSet.addChange(new Change("file2.txt", Change.TYPE_ADD));
        changeSetPerformer = new ChangeSetPerformer(changeSet);
        
        mockArchiveInputStream = mock(ArchiveInputStream.class);
        mockArchiveOutputStream = mock(ArchiveOutputStream.class);
    }
    
    @Test
    public void testPerform_AddReplaceMode() throws IOException {
        ChangeSetResults results = new ChangeSetResults();
        changeSetPerformer.perform(mockArchiveInputStream, mockArchiveOutputStream, results);
        
        assertEquals(2, results.getAddedFromChangeSet().size());
        assertTrue(results.getAddedFromChangeSet().contains("file1.txt"));
        assertTrue(results.getAddedFromChangeSet().contains("file2.txt"));
    }
    
    @Test
    public void testPerform_Delete() throws IOException {
        ChangeSet changeSet = new ChangeSet();
        changeSet.addChange(new Change("file1.txt", Change.TYPE_DELETE));
        changeSetPerformer = new ChangeSetPerformer(changeSet);
        
        ChangeSetResults results = new ChangeSetResults();
        InputStream mockInputStream = mock(InputStream.class);
        ArchiveEntry mockArchiveEntry = mock(ArchiveEntry.class);
        when(mockArchiveInputStream.getNextEntry()).thenReturn(mockArchiveEntry, (ArchiveEntry) null);
        when(mockArchiveEntry.getName()).thenReturn("file1.txt");
        when(mockArchiveEntry.isDirectory()).thenReturn(false);
        when(mockArchiveEntry.getSize()).thenReturn((long) 10);
        when(mockArchiveEntry.getLastModifiedDate()).thenReturn(new Date());
        when(mockArchiveEntry.isUnixSymlink()).thenReturn(false);
        when(mockArchiveEntry.isSymbolicLink()).thenReturn(false);
        
        changeSetPerformer.perform(mockArchiveInputStream, mockArchiveOutputStream, results);
        
        assertTrue(results.getDeleted().contains("file1.txt"));
    }
    
    @Test
    public void testPerform_DeleteDir() throws IOException {
        ChangeSet changeSet = new ChangeSet();
        changeSet.addChange(new Change("dir1", Change.TYPE_DELETE_DIR));
        changeSetPerformer = new ChangeSetPerformer(changeSet);
        
        ChangeSetResults results = new ChangeSetResults();
        InputStream mockInputStream = mock(InputStream.class);
        ArchiveEntry mockArchiveEntry = mock(ArchiveEntry.class);
        when(mockArchiveInputStream.getNextEntry()).thenReturn(mockArchiveEntry, (ArchiveEntry) null);
        when(mockArchiveEntry.getName()).thenReturn("dir1/subfile1.txt");
        when(mockArchiveEntry.isDirectory()).thenReturn(false);
        when(mockArchiveEntry.getSize()).thenReturn((long) 10);
        when(mockArchiveEntry.getLastModifiedDate()).thenReturn(new Date());
        when(mockArchiveEntry.isUnixSymlink()).thenReturn(false);
        when(mockArchiveEntry.isSymbolicLink()).thenReturn(false);
        
        changeSetPerformer.perform(mockArchiveInputStream, mockArchiveOutputStream, results);
        
        assertTrue(results.getDeleted().contains("dir1/subfile1.txt"));
    }
    
    @Test
    public void testIsDeletedLater_True() {
        Set<Change> workingSet = new HashSet<>();
        workingSet.add(new Change("file1.txt", Change.TYPE_DELETE));
        
        ArchiveEntry entry = mock(ArchiveEntry.class);
        when(entry.getName()).thenReturn("file1.txt");
        
        boolean result = changeSetPerformer.isDeletedLater(workingSet, entry);
        
        assertTrue(result);
    }
    
    @Test
    public void testIsDeletedLater_False() {
        Set<Change> workingSet = new HashSet<>();
        workingSet.add(new Change("file2.txt", Change.TYPE_DELETE));
        
        ArchiveEntry entry = mock(ArchiveEntry.class);
        when(entry.getName()).thenReturn("file1.txt");
        
        boolean result = changeSetPerformer.isDeletedLater(workingSet, entry);
        
        assertFalse(result);
    }
    
    @Test
    public void testCopyStream() throws IOException {
        InputStream mockInputStream = mock(InputStream.class);
        ArchiveOutputStream mockOutputStream = mock(ArchiveOutputStream.class);
        ArchiveEntry mockEntry = mock(ArchiveEntry.class);
        when(mockEntry.getName()).thenReturn("file1.txt");
        when(mockEntry.isDirectory()).thenReturn(false);
        when(mockEntry.getSize()).thenReturn((long) 10);
        when(mockEntry.getLastModifiedDate()).thenReturn(new Date());
        when(mockEntry.isUnixSymlink()).thenReturn(false);
        when(mockEntry.isSymbolicLink()).thenReturn(false);
        
        changeSetPerformer.copyStream(mockInputStream, mockOutputStream, mockEntry);
        
        verify(mockOutputStream).putArchiveEntry(mockEntry);
        verify(mockArchiveInputStream).copy(mockInputStream, mockOutputStream);
        verify(mockOutputStream).closeArchiveEntry();
    }
}