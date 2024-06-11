import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.archivers.ArchiveOutputStream;
import org.apache.commons.compress.changes.Change;
import org.apache.commons.compress.changes.ChangeSet;
import org.apache.commons.compress.utils.IOUtils;
import org.junit.Before;
import org.junit.Test;

public class ChangeSetPerformerTest {

    private ChangeSetPerformer performer;
    private ArchiveInputStream in;
    private ArchiveOutputStream out;
    
    private ChangeSet changeSet;
    private ArchiveEntry entry;
    
    @Before
    public void setup() throws IOException {
        changeSet = mock(ChangeSet.class);
        entry = mock(ArchiveEntry.class);
        Set<Change> changes = new LinkedHashSet<>();
        changes.add(change);
        
        when(changeSet.getChanges()).thenReturn(changes);
        
        performer = new ChangeSetPerformer(changeSet);
        in = mock(ArchiveInputStream.class);
        out = mock(ArchiveOutputStream.class);
    }
    
    @Test
    public void testPerform_AddReplaceMode_CopiesEntryToOutputStream() throws IOException {
        Change change = mock(Change.class);
        when(change.type()).thenReturn(Change.TYPE_ADD);
        when(change.isReplaceMode()).thenReturn(true);
        when(change.getInput()).thenReturn(getInputStream());
        when(change.getEntry()).thenReturn(entry);
        when(entry.getName()).thenReturn("entryName");
        
        when(in.getNextEntry()).thenReturn(null);
        
        ChangeSetResults results = performer.perform(in, out);
        
        verify(out, times(1)).putArchiveEntry(entry);
        verify(out, times(1)).closeArchiveEntry();
        assertEquals(1, results.getAddedFromChangeSet().size());
        assertEquals("entryName", results.getAddedFromChangeSet().get(0));
    }
    
    @Test
    public void testPerform_DeleteEntry_RemovesEntryFromWorkingSet() throws IOException {
        Change change = mock(Change.class);
        when(change.type()).thenReturn(Change.TYPE_DELETE);
        when(change.targetFile()).thenReturn("entryNameToDelete");
        when(in.getNextEntry()).thenReturn(entry, null);
        when(entry.getName()).thenReturn("entryNameToDelete");
        
        ChangeSetResults results = performer.perform(in, out);
        
        verify(in, times(2)).getNextEntry();
        assertEquals(1, results.getDeleted().size());
        assertTrue(results.getDeleted().contains("entryNameToDelete"));
    }
    
    @Test
    public void testPerform_DeleteDir_RecursiveDeletionOfEntries() throws IOException {
        Change change = mock(Change.class);
        when(change.type()).thenReturn(Change.TYPE_DELETE_DIR);
        when(change.targetFile()).thenReturn("dirNameToDelete");
        
        ArchiveEntry entry1 = mock(ArchiveEntry.class);
        when(entry1.getName()).thenReturn("dirNameToDelete/");
        ArchiveEntry entry2 = mock(ArchiveEntry.class);
        when(entry2.getName()).thenReturn("dirNameToDelete/file1");
        ArchiveEntry entry3 = mock(ArchiveEntry.class);
        when(entry3.getName()).thenReturn("dirNameToDelete/file2");
        
        when(in.getNextEntry()).thenReturn(entry1, entry2, entry3, null);
        
        ChangeSetResults results = performer.perform(in, out);
        
        verify(in, times(4)).getNextEntry();
        
        assertEquals(3, results.getDeleted().size());
        assertTrue(results.getDeleted().contains("dirNameToDelete/"));
        assertTrue(results.getDeleted().contains("dirNameToDelete/file1"));
        assertTrue(results.getDeleted().contains("dirNameToDelete/file2"));
    }
    
    @Test
    public void testPerform_CopyEntry_CopiesEntryToOutputStream() throws IOException {
        Change change = mock(Change.class);
        when(change.type()).thenReturn(Change.TYPE_COPY);
        when(change.targetFile()).thenReturn("entryNameToCopy");
        when(in.getNextEntry()).thenReturn(entry, null);
        when(entry.getName()).thenReturn("entryNameToCopy");
        
        ChangeSetResults results = performer.perform(in, out);
        
        verify(out, times(1)).putArchiveEntry(entry);
        verify(out, times(1)).closeArchiveEntry();
        assertEquals(1, results.getAddedFromStream().size());
        assertEquals("entryNameToCopy", results.getAddedFromStream().get(0));
    }
    
    @Test
    public void testPerform_AddEntry_AddsEntryToOutputStream() throws IOException {
        Change change = mock(Change.class);
        when(change.type()).thenReturn(Change.TYPE_ADD);
        when(change.isReplaceMode()).thenReturn(false);
        when(change.getInput()).thenReturn(getInputStream());
        when(change.getEntry()).thenReturn(entry);
        when(entry.getName()).thenReturn("entryNameToAdd");
        
        when(in.getNextEntry()).thenReturn(null);
        
        ChangeSetResults results = performer.perform(in, out);
        
        verify(out, times(1)).putArchiveEntry(entry);
        verify(out, times(1)).closeArchiveEntry();
        assertEquals(1, results.getAddedFromChangeSet().size());
        assertEquals("entryNameToAdd", results.getAddedFromChangeSet().get(0));
    }
    
    private InputStream getInputStream() {
        return new ByteArrayInputStream("testData".getBytes());
    }
}