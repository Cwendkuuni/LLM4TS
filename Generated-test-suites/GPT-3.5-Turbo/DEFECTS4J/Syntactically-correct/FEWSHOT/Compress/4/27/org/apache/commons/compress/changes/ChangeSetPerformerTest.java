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
import org.apache.commons.compress.archivers.ArchiveOutputStream.*;
import org.apache.commons.compress.utils.IOUtils;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class ChangeSetPerformerTest {

    @Mock
    private ChangeSet changeSet;
    
    @Mock
    private ArchiveInputStream in;
    
    @Mock
    private ArchiveOutputStream out;
    
    @Mock
    private ChangeSetResults results;

    @InjectMocks
    private ChangeSetPerformer performer;

    @Before
    public void setup(){
        MockitoAnnotations.initMocks(this);
    }
    
    @Test
    public void testConstructor() {
        Set changes = new LinkedHashSet();
        when(changeSet.getChanges()).thenReturn(changes);
        
        ChangeSetPerformer testPerformer = new ChangeSetPerformer(changeSet);
        
        assertEquals(changes, testPerformer.changes);
        verify(changeSet).getChanges();
    }
    
    @Test
    public void testPerform() throws IOException {
        Set changes = new LinkedHashSet();
        when(changeSet.getChanges()).thenReturn(changes);
        
        Change change = mock(Change.class);
        InputStream inputStream = mock(InputStream.class);
        ArchiveEntry entry = mock(ArchiveEntry.class);
        
        when(change.type()).thenReturn(Change.TYPE_ADD);
        when(change.isReplaceMode()).thenReturn(true);
        when(change.getInput()).thenReturn(inputStream);
        when(change.getEntry()).thenReturn(entry);
        changes.add(change);
        
        performer.perform(in, out);
        
        verify(changeSet, times(2)).getChanges();
        verify(change).type();
        verify(change).isReplaceMode();
        verify(change).getInput();
        verify(change).getEntry();
        verify(results).addedFromChangeSet(entry.getName());
        verify(IOUtils).copy(inputStream, out);
        verify(out).closeArchiveEntry();
        verifyNoMoreInteractions(changeSet, change, results, IOUtils, out);
    }

    @Test
    public void testPerform_withNullChangeSet() throws IOException {
        ChangeSetPerformer testPerformer = new ChangeSetPerformer(null);
        
        testPerformer.perform(in, out);
        
        verifyNoMoreInteractions(changeSet, results, IOUtils, out);
    }
    
    @Test
    public void testPerform_withNextEntryNull() throws IOException {
        Set changes = new LinkedHashSet();
        when(changeSet.getChanges()).thenReturn(changes);
        
        Change change = mock(Change.class);
        InputStream inputStream = mock(InputStream.class);
        
        when(change.type()).thenReturn(Change.TYPE_ADD);
        when(change.isReplaceMode()).thenReturn(false);
        when(change.getInput()).thenReturn(inputStream);
        when(change.getEntry()).thenReturn(null);
        when(in.getNextEntry()).thenReturn(null);
        changes.add(change);
        
        ChangeSetPerformer testPerformer = new ChangeSetPerformer(changeSet);
        
        testPerformer.perform(in, out);
        
        verify(changeSet).getChanges();
        verify(change, never()).type();
        verify(change, never()).isReplaceMode();
        verify(change, never()).getInput();
        verify(change, never()).getEntry();
        verify(results, never()).addedFromChangeSet(anyString());
        verify(in).getNextEntry();
        verify(IOUtils, never()).copy(any(InputStream.class), any(ArchiveOutputStream.class));
        verify(out, never()).closeArchiveEntry();
        verifyNoMoreInteractions(changeSet, change, results, in, IOUtils, out);
    }

    @Test
    public void testPerform_withDeletedEntry() throws IOException {
        Set changes = new LinkedHashSet();
        when(changeSet.getChanges()).thenReturn(changes);
        
        Change change = mock(Change.class);
        InputStream inputStream = mock(InputStream.class);
        ArchiveEntry entry = mock(ArchiveEntry.class);
        ArchiveEntry deletedEntry = mock(ArchiveEntry.class);
        
        when(change.type()).thenReturn(Change.TYPE_ADD);
        when(change.isReplaceMode()).thenReturn(false);
        when(change.getInput()).thenReturn(inputStream);
        when(change.getEntry()).thenReturn(entry);
        when(in.getNextEntry()).thenReturn(deletedEntry, null);
        when(deletedEntry.getName()).thenReturn("deletedEntry");
        when(entry.getName()).thenReturn("entry");
        when(change.targetFile()).thenReturn("deletedEntry");
        changes.add(change);
        
        ChangeSetPerformer testPerformer = new ChangeSetPerformer(changeSet);
        
        testPerformer.perform(in, out);
        
        verify(changeSet, times(2)).getChanges();
        verify(change).type();
        verify(change).isReplaceMode();
        verify(change).getInput();
        verify(change).getEntry();
        verify(change).targetFile();
        verify(results).deleted("deletedEntry");
        verify(in, times(2)).getNextEntry();
        verify(IOUtils, never()).copy(any(InputStream.class), any(ArchiveOutputStream.class));
        verify(out, never()).closeArchiveEntry();
        verifyNoMoreInteractions(changeSet, change, results, in, IOUtils, out);
    }
    
    @Test
    public void testPerform_withCopyEntry() throws IOException {
        Set changes = new LinkedHashSet();
        when(changeSet.getChanges()).thenReturn(changes);
        
        Change change = mock(Change.class);
        InputStream inputStream = mock(InputStream.class);
        ArchiveEntry entry = mock(ArchiveEntry.class);
        
        when(change.type()).thenReturn(Change.TYPE_ADD);
        when(change.isReplaceMode()).thenReturn(false);
        when(change.getInput()).thenReturn(inputStream);
        when(change.getEntry()).thenReturn(entry);
        when(in.getNextEntry()).thenReturn(entry, null);
        when(entry.getName()).thenReturn("entry");
        changes.add(change);
        
        ChangeSetPerformer testPerformer = new ChangeSetPerformer(changeSet);
        
        testPerformer.perform(in, out);
        
        verify(changeSet, times(2)).getChanges();
        verify(change).type();
        verify(change).isReplaceMode();
        verify(change).getInput();
        verify(change).getEntry();
        verify(results).addedFromStream("entry");
        verify(in, times(2)).getNextEntry();
        verify(IOUtils).copy(in, out);
        verify(out).closeArchiveEntry();
        verifyNoMoreInteractions(changeSet, change, results, in, IOUtils, out);
    }

    @Test
    public void testIsDeletedLater_withEmptyWorkingSet() {
        Set workingSet = new LinkedHashSet();
        ArchiveEntry entry = mock(ArchiveEntry.class);
        
        boolean deletedLater = performer.isDeletedLater(workingSet, entry);
        
        assertEquals(false, deletedLater);
        verify(entry, never()).getName();
    }
    
    @Test
    public void testIsDeletedLater_withDeletedEntry() {
        Set workingSet = new LinkedHashSet();
        ArchiveEntry entry = mock(ArchiveEntry.class);
        Change change = mock(Change.class);
        
        when(change.type()).thenReturn(Change.TYPE_DELETE);
        when(change.targetFile()).thenReturn("entry");
        workingSet.add(change);
        when(entry.getName()).thenReturn("entry");
        
        boolean deletedLater = performer.isDeletedLater(workingSet, entry);
        
        assertEquals(true, deletedLater);
        verify(entry).getName();
        verify(change).type();
        verify(change).targetFile();
    }
    
    @Test
    public void testIsDeletedLater_withNonDeletedEntry() {
        Set workingSet = new LinkedHashSet();
        ArchiveEntry entry = mock(ArchiveEntry.class);
        Change change = mock(Change.class);
        
        when(change.type()).thenReturn(Change.TYPE_DELETE);
        when(change.targetFile()).thenReturn("deletedEntry");
        workingSet.add(change);
        when(entry.getName()).thenReturn("entry");
        
        boolean deletedLater = performer.isDeletedLater(workingSet, entry);
        
        assertEquals(false, deletedLater);
        verify(entry).getName();
        verify(change).type();
        verify(change).targetFile();
    }

    @Test
    public void testCopyStream() throws IOException {
        InputStream input = mock(InputStream.class);
        ArchiveEntry entry = mock(ArchiveEntry.class);
        
        performer.copyStream(input, out, entry);
        
        verify(out).putArchiveEntry(entry);
        verify(IOUtils).copy(input, out);
        verify(out).closeArchiveEntry();
        verifyNoMoreInteractions(IOUtils, out);
    }
}