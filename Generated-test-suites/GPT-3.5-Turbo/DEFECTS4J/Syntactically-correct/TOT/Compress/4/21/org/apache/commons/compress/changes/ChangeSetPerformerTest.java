
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.archivers.ArchiveOutputStream;
import org.apache.commons.compress.changes.Change;
import org.apache.commons.compress.changes.ChangeSet;
import org.apache.commons.compress.changes.ChangeSetPerformer;
import org.junit.Before;
import org.junit.Test;

public class ChangeSetPerformerTest {

    private ChangeSetPerformer performer;
    private ArchiveInputStream in;
    private ArchiveOutputStream out;

    @Before
    public void setup() {
        ChangeSet changeSet = mock(ChangeSet.class);
        Change change1 = mock(Change.class);
        when(change1.type()).thenReturn(Change.TYPE_ADD);
        when(change1.isReplaceMode()).thenReturn(true);
        when(change1.getEntry()).thenReturn(mock(ArchiveEntry.class));
        
        Change change2 = mock(Change.class);
        when(change2.type()).thenReturn(Change.TYPE_DELETE);
        when(change2.targetFile()).thenReturn("test-file.txt");
        
        Change change3 = mock(Change.class);
        when(change3.type()).thenReturn(Change.TYPE_DELETE_DIR);
        when(change3.targetFile()).thenReturn("test-dir");
        
        when(changeSet.getChanges()).thenReturn(List.of(change1, change2, change3));
        performer = new ChangeSetPerformer(changeSet);
        in = mock(ArchiveInputStream.class);
        out = mock(ArchiveOutputStream.class);
    }

    @Test
    public void testPerform_withAddAndReplaceMode_onlyAddsEntryToOutputStream() throws IOException {
        ArchiveEntry entry = mock(ArchiveEntry.class);
        ByteArrayInputStream input = new ByteArrayInputStream("test".getBytes());
        
        performer.perform(in, out);
        
        verify(out, times(1)).putArchiveEntry(entry);
        verify(out, times(1)).closeArchiveEntry();
    }
    
    @Test
    public void testPerform_withDeleteEntry_removesEntryFromInputStream() throws IOException {
        ArchiveEntry entry = mock(ArchiveEntry.class);
        when(entry.getName()).thenReturn("test-file.txt");
        when(in.getNextEntry()).thenReturn(entry, null);
        
        performer.perform(in, out);
        
        verify(in, times(1)).getNextEntry();
        verify(out, never()).putArchiveEntry(entry);
        verify(out, never()).closeArchiveEntry();
    }
    
    @Test
    public void testPerform_withDeleteDirectory_removesEntriesInDirectoryFromInputStream() throws IOException {
        ArchiveEntry entry1 = mock(ArchiveEntry.class);
        ArchiveEntry entry2 = mock(ArchiveEntry.class);
        when(entry1.getName()).thenReturn("test-dir/file1.txt");
        when(entry2.getName()).thenReturn("test-dir/file2.txt");
        when(in.getNextEntry()).thenReturn(entry1, entry2, null);
        
        performer.perform(in, out);
        
        verify(in, times(3)).getNextEntry();
        verify(out, never()).putArchiveEntry(entry1);
        verify(out, never()).putArchiveEntry(entry2);
        verify(out, never()).closeArchiveEntry();
    }
    
    @Test
    public void testPerform_withDeletedLaterEntry_copiesEntryToOutputStream() throws IOException {
        ArchiveEntry entry1 = mock(ArchiveEntry.class);
        when(entry1.getName()).thenReturn("test-entry.txt");
        when(in.getNextEntry()).thenReturn(entry1, null);
        
        performer.perform(in, out);
        
        verify(in, times(1)).getNextEntry();
        verify(out, times(1)).putArchiveEntry(entry1);
        verify(out, times(1)).closeArchiveEntry();
    }
    
    @Test
    public void testPerform_withDeletedLaterDirectory_copiesEntriesToOutputStream() throws IOException {
        ArchiveEntry entry1 = mock(ArchiveEntry.class);
        ArchiveEntry entry2 = mock(ArchiveEntry.class);
        when(entry1.getName()).thenReturn("test-dir/file1.txt");
        when(entry2.getName()).thenReturn("test-dir/file2.txt");
        when(in.getNextEntry()).thenReturn(entry1, entry2, null);
        
        performer.perform(in, out);
        
        verify(in, times(3)).getNextEntry();
        verify(out, times(1)).putArchiveEntry(entry1);
        verify(out, times(1)).putArchiveEntry(entry2);
        verify(out, times(2)).closeArchiveEntry();
    }
    
    @Test
    public void testPerform_withAddedDeletedEntry_copiesEntryToOutputStream() throws IOException {
        ArchiveEntry entry1 = mock(ArchiveEntry.class);
        ArchiveEntry entry2 = mock(ArchiveEntry.class);
        when(entry1.getName()).thenReturn("test-entry.txt");
        when(entry2.getName()).thenReturn("test-entry.txt");
        when(in.getNextEntry()).thenReturn(entry1, entry2, null);
        
        performer.perform(in, out);
        
        verify(in, times(2)).getNextEntry();
        verify(out, times(2)).putArchiveEntry(entry1);
        verify(out, times(2)).closeArchiveEntry();
    }
    
    @Test
    public void testPerform_withAddedEntryAndReplaceMode_copiesEntryToOutputStream() throws IOException {
        ArchiveEntry entry1 = mock(ArchiveEntry.class);
        when(entry1.getName()).thenReturn("test-entry.txt");
        when(in.getNextEntry()).thenReturn(entry1, null);
        
        performer.perform(in, out);
        
        verify(in, times(1)).getNextEntry();
        verify(out, times(1)).putArchiveEntry(entry1);
        verify(out, times(1)).closeArchiveEntry();
    }
    
    @Test
    public void testPerform_withAddedEntryAndNoReplaceMode_copiesEntryToOutputStream() throws IOException {
        ArchiveEntry entry1 = mock(ArchiveEntry.class);
        when(entry1.getName()).thenReturn("test-entry.txt");
        when(in.getNextEntry()).thenReturn(entry1, null);
        
        performer.perform(in, out);
        
        verify(in, times(1)).getNextEntry();
        verify(out, times(1)).putArchiveEntry(entry1);
        verify(out, times(1)).closeArchiveEntry();
    }
    
    @Test
    public void testIsDeletedLater_withDeletedEntry_returnsTrue() {
        Set<Change> workingSet = new LinkedHashSet<>();
        Change change = mock(Change.class);
        when(change.type()).thenReturn(Change.TYPE_DELETE);
        when(change.targetFile()).thenReturn("test-entry.txt");
        workingSet.add(change);
        ArchiveEntry entry = mock(ArchiveEntry.class);
        when(entry.getName()).thenReturn("test-entry.txt");
        
        boolean result = performer.isDeletedLater(workingSet, entry);
        
        assertTrue(result);
    }
    
    @Test
    public void testIsDeletedLater_withDeletedLaterEntry_returnsTrue() {
        Set<Change> workingSet = new LinkedHashSet<>();
        Change change1 = mock(Change.class);
        when(change1.type()).thenReturn(Change.TYPE_DELETE);
        when(change1.targetFile()).thenReturn("test-entry.txt");
        workingSet.add(change1);
        Change change2 = mock(Change.class);
        when(change2.type()).thenReturn(Change.TYPE_DELETE);
        when(change2.targetFile()).thenReturn("test-entry2.txt");
        workingSet.add(change2);
        ArchiveEntry entry = mock(ArchiveEntry.class);
        when(entry.getName()).thenReturn("test-entry.txt");
        
        boolean result = performer.isDeletedLater(workingSet, entry);
        
        assertTrue(result);
    }
    
    @Test
    public void testIsDeletedLater_withNonDeletedEntry_returnsFalse() {
        Set<Change> workingSet = new LinkedHashSet<>();
        Change change = mock(Change.class);
        when(change.type()).thenReturn(Change.TYPE_ADD);
        workingSet.add(change);
        ArchiveEntry entry = mock(ArchiveEntry.class);
        when(entry.getName()).thenReturn("test-entry.txt");
        
        boolean result = performer.isDeletedLater(workingSet, entry);
        
        assertFalse(result);
    }
    
    @Test
    public void testCopyStream_withInputStreamAndOutputStream_copiesDataToOutputStream() throws IOException {
        ByteArrayInputStream inStream = new ByteArrayInputStream("test".getBytes());
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        ArchiveEntry entry = mock(ArchiveEntry.class);
        
        performer.copyStream(inStream, outStream, entry);
        byte[] data = outStream.toByteArray();
        
        assertEquals("test", new String(data));
    }
}
