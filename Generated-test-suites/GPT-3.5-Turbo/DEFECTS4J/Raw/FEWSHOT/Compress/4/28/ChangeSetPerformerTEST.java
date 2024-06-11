package org.apache.commons.compress.changes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;

import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.archivers.ArchiveOutputStream;
import org.apache.commons.compress.utils.IOUtils;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class ChangeSetPerformerTest {
    @Mock
    private ChangeSet mockChangeSet;
    @Mock
    private ArchiveInputStream mockArchiveInputStream;
    @Mock
    private ArchiveOutputStream mockArchiveOutputStream;
    @Mock
    private ChangeSetResults mockChangeSetResults;
    @Mock
    private Change mockChange;
    @Mock
    private ArchiveEntry mockArchiveEntry;
    @Mock
    private InputStream mockInputStream;
    
    private ChangeSetPerformer changeSetPerformer;
    private LinkedHashSet<Change> changes;
    
    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        
        changes = new LinkedHashSet<Change>();
        changes.add(mockChange);
        
        when(mockChangeSet.getChanges()).thenReturn(changes);
        when(mockChangeSetResults.hasBeenAdded(anyString())).thenReturn(false);
        
        changeSetPerformer = new ChangeSetPerformer(mockChangeSet);
    }
    
    @Test
    public void testConstructor() {
        assertEquals(changes, changeSetPerformer.changes);
    }
    
    @Test
    public void testPerform() throws IOException {
        when(mockChange.type()).thenReturn(Change.TYPE_ADD);
        when(mockChange.isReplaceMode()).thenReturn(true);
        when(mockChange.getInput()).thenReturn(mockInputStream);
        when(mockChange.getEntry()).thenReturn(mockArchiveEntry);
        when(mockArchiveInputStream.getNextEntry()).thenReturn(mockArchiveEntry, (ArchiveEntry) null);
        
        ChangeSetResults results = changeSetPerformer.perform(mockArchiveInputStream, mockArchiveOutputStream);
        
        verify(mockArchiveOutputStream).putArchiveEntry(mockArchiveEntry);
        verify(mockArchiveOutputSteram).closeArchiveEntry();
        verify(mockChange).getInput();
        verify(mockChangeSetResults).addedFromChangeSet(mockArchiveEntry.getName());
        
        assertEquals(results, mockChangeSetResults);
    }
    
    @Test
    public void testPerformWithDeletedEntry() throws IOException {
        when(mockChange.type()).thenReturn(Change.TYPE_DELETE);
        when(mockChange.targetFile()).thenReturn("testfile.txt");
        when(mockArchiveInputStream.getNextEntry()).thenReturn(mockArchiveEntry, (ArchiveEntry) null);
        
        ChangeSetResults results = changeSetPerformer.perform(mockArchiveInputStream, mockArchiveOutputStream);
        
        verify(mockChange).type();
        verify(mockChange).targetFile();
        verify(mockArchiveInputStream).getNextEntry();
        verify(mockChangeSetResults).deleted(mockArchiveEntry.getName());
        
        assertEquals(results, mockChangeSetResults);
    }
    
    @Test
    public void testPerformWithDeletedDir() throws IOException {
        when(mockChange.type()).thenReturn(Change.TYPE_DELETE_DIR);
        when(mockChange.targetFile()).thenReturn("testdir");
        when(mockArchiveEntry.getName()).thenReturn("testdir/file.txt");
        when(mockArchiveInputStream.getNextEntry()).thenReturn(mockArchiveEntry, (ArchiveEntry) null);
        
        ChangeSetResults results = changeSetPerformer.perform(mockArchiveInputStream, mockArchiveOutputStream);
        
        verify(mockChange).type();
        verify(mockChange).targetFile();
        verify(mockArchiveInputStream).getNextEntry();
        verify(mockChangeSetResults).deleted(mockArchiveEntry.getName());
        
        assertEquals(results, mockChangeSetResults);
    }
    
    @Test
    public void testIsDeletedLater() {
        Set<Change> workingSet = new LinkedHashSet<Change>();
        workingSet.add(mockChange);
        
        when(mockChange.type()).thenReturn(Change.TYPE_DELETE);
        when(mockChange.targetFile()).thenReturn("testfile.txt");
        when(mockArchiveEntry.getName()).thenReturn("testfile.txt");
        when(mockIterator.hasNext()).thenReturn(true, false);
        when(mockIterator.next()).thenReturn(mockChange);
        
        boolean result = changeSetPerformer.isDeletedLater(workingSet, mockArchiveEntry);
        
        verify(mockChange).type();
        verify(mockChange).targetFile();
        verify(mockIterator, times(2)).hasNext();
        verify(mockIterator).next();
        
        assertEquals(result, true);
    }
    
    @Test
    public void testCopyStream() throws IOException {
        changeSetPerformer.copyStream(mockInputStream, mockArchiveOutputStream, mockArchiveEntry);
        
        verify(mockArchiveOutputStream).putArchiveEntry(mockArchiveEntry);
        verify(mockArchiveOutputStream).closeArchiveEntry();
        verify(IOUtils).copy(mockInputStream, mockArchiveOutputStream);
    }
}