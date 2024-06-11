
package org.apache.commons.compress.changes;

import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.archivers.ArchiveOutputStream;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedHashSet;
import java.util.Set;

import static org.mockito.Mockito.*;

public class ChangeSetPerformerTest {
    private ChangeSetPerformer performer;
    
    @Mock
    private ChangeSet changeSet;
    
    @Mock
    private ArchiveInputStream in;
    
    @Mock
    private ArchiveOutputStream out;
    
    @Before
    public void setUp() {
        changeSet = mock(ChangeSet.class);
        performer = new ChangeSetPerformer(changeSet);
    }
    
    @Test
    public void testPerform_withDeleteChangeType_shouldNotCopyEntry() throws IOException {
        Set<Change> changes = new LinkedHashSet<>();
        ArchiveEntry entry = mock(ArchiveEntry.class);
        Change change = mock(Change.class);
        
        changes.add(change);
        when(change.getType()).thenReturn(Change.TYPE_DELETE);
        when(change.targetFile()).thenReturn("testFile");
        when(in.getNextEntry()).thenReturn(entry, null);
        when(changeSet.getChanges()).thenReturn(changes);
        
        performer.perform(in, out);
        
        verify(out, never()).putArchiveEntry(entry);
        verify(out, never()).closeArchiveEntry();
    }
    
    @Test
    public void testPerform_withDeleteDirChangeType_shouldNotCopyEntry() throws IOException {
        Set<Change> changes = new LinkedHashSet<>();
        ArchiveEntry entry = mock(ArchiveEntry.class);
        Change change = mock(Change.class);
        
        changes.add(change);
        when(change.getType()).thenReturn(Change.TYPE_DELETE_DIR);
        when(change.targetFile()).thenReturn("testDir");
        when(entry.getName()).thenReturn("testDir/file");
        when(in.getNextEntry()).thenReturn(entry, null);
        when(changeSet.getChanges()).thenReturn(changes);
        
        performer.perform(in, out);
        
        verify(out, never()).putArchiveEntry(entry);
        verify(out, never()).closeArchiveEntry();
    }
    
    @Test
    public void testPerform_withAddReplaceChangeType_shouldCopyEntry() throws IOException {
        Set<Change> changes = new LinkedHashSet<>();
        ArchiveEntry entry = mock(ArchiveEntry.class);
        Change change = mock(Change.class);
        
        changes.add(change);
        when(change.getType()).thenReturn(Change.TYPE_ADD);
        when(change.isReplaceMode()).thenReturn(true);
        when(change.getEntry()).thenReturn(entry);
        when(change.getInput()).thenReturn(mock(InputStream.class));
        when(in.getNextEntry()).thenReturn(null);
        when(changeSet.getChanges()).thenReturn(changes);
        
        performer.perform(in, out);
        
        verify(out, times(1)).putArchiveEntry(entry);
        verify(out, times(1)).closeArchiveEntry();
    }
    
    // Add more test cases for each method in the ChangeSetPerformer class
}
