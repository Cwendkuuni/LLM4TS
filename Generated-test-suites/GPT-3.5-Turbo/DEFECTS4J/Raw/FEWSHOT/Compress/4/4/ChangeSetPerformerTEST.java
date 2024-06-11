import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.archivers.ArchiveOutputStream;
import org.junit.Before;
import org.junit.Test;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;

public class ChangeSetPerformerTest {
    private ChangeSet changeSet;
    private ArchiveInputStream mockIn;
    private ArchiveOutputStream mockOut;
    private Change change1;
    private Change change2;
    private Set<Change> changes;
    private ChangeSetPerformer performer;

    @Before
    public void setUp() {
        mockIn = mock(ArchiveInputStream.class);
        mockOut = mock(ArchiveOutputStream.class);
        changeSet = mock(ChangeSet.class);
        change1 = mock(Change.class);
        change2 = mock(Change.class);
        changes = new LinkedHashSet<>();
        changes.add(change1);
        changes.add(change2);
        
        when(changeSet.getChanges()).thenReturn(changes);
        
        performer = new ChangeSetPerformer(changeSet);
    }

    @Test
    public void testPerform() throws IOException {
        ArchiveEntry entry1 = mock(ArchiveEntry.class);
        ArchiveEntry entry2 = mock(ArchiveEntry.class);
        ArchiveEntry entry3 = mock(ArchiveEntry.class);
        
        Iterator<Change> changeIterator = changes.iterator();
        when(change1.type()).thenReturn(Change.TYPE_ADD);
        when(change1.isReplaceMode()).thenReturn(true);
        when(change1.getInput()).thenReturn(mock(InputStream.class));
        when(change1.getEntry()).thenReturn(entry1);
        when(change2.type()).thenReturn(Change.TYPE_ADD);
        when(change2.isReplaceMode()).thenReturn(false);
        when(change2.getInput()).thenReturn(mock(InputStream.class));
        when(change2.getEntry()).thenReturn(entry2);
        
        when(mockIn.getNextEntry()).thenReturn(entry1).thenReturn(entry2).thenReturn(entry3).thenReturn(null);
        when(mockOut.putArchiveEntry(eq(entry1))).thenReturn(mockOut);
        when(mockOut.putArchiveEntry(eq(entry2))).thenReturn(mockOut);
        when(mockOut.closeArchiveEntry()).thenReturn(mockOut);
        
        ChangeSetResults results = performer.perform(mockIn, mockOut);
        
        verify(mockIn, times(4)).getNextEntry();
        verify(mockOut, times(3)).putArchiveEntry(any(ArchiveEntry.class));
        verify(mockOut, times(3)).closeArchiveEntry();
        verify(change1).getInput();
        verify(change2).getInput();
        verify(results).addedFromChangeSet(entry2.getName());
        verify(results, never()).addedFromStream(anyString());
        verify(results, never()).deleted(anyString());
        verify(results, never()).hasBeenAdded(anyString());

    }

    @Test
    public void testIsDeletedLater() {
        Set<Change> emptySet = new LinkedHashSet<>();
        ArchiveEntry entry = mock(ArchiveEntry.class);
        when(entry.getName()).thenReturn("entry1");

        boolean result1 = performer.isDeletedLater(emptySet, entry);
        assertEquals(false, result1);
        
        when(change1.type()).thenReturn(Change.TYPE_DELETE);
        when(change1.targetFile()).thenReturn("entry1");
        when(change2.type()).thenReturn(Change.TYPE_DELETE_DIR);
        when(change2.targetFile()).thenReturn("dir1");

        boolean result2 = performer.isDeletedLater(changes, entry);
        assertEquals(true, result2);

        when(change1.type()).thenReturn(Change.TYPE_DELETE);
        when(change1.targetFile()).thenReturn("entry2");
        when(change2.type()).thenReturn(Change.TYPE_DELETE_DIR);
        when(change2.targetFile()).thenReturn("dir1");

        boolean result3 = performer.isDeletedLater(changes, entry);
        assertEquals(false, result3);

        when(change1.type()).thenReturn(Change.TYPE_DELETE);
        when(change1.targetFile()).thenReturn("entry1");
        when(change2.type()).thenReturn(Change.TYPE_DELETE_DIR);
        when(change2.targetFile()).thenReturn("dir1");

        boolean result4 = performer.isDeletedLater(changes, entry);
        assertEquals(true, result4);
    }

    @Test
    public void testCopyStream() throws IOException {
        InputStream in = mock(InputStream.class);
        ArchiveEntry entry = mock(ArchiveEntry.class);

        performer.copyStream(in, mockOut, entry);

        verify(mockOut).putArchiveEntry(entry);
        verify(mockOut).closeArchiveEntry();

    }

    
}