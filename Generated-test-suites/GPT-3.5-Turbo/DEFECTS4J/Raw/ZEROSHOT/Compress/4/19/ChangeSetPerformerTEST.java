import org.junit.Before;
import org.junit.Test;
import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.archivers.ArchiveOutputStream;

import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;

import static org.mockito.Mockito.*;

public class ChangeSetPerformerTest {

    private ChangeSetPerformer performer;
    private ArchiveInputStream mockInputStream;
    private ArchiveOutputStream mockOutputStream;
    private ChangeSet mockChangeSet;
    private Set changes;

    @Before
    public void setUp() {
        mockChangeSet = mock(ChangeSet.class);
        mockInputStream = mock(ArchiveInputStream.class);
        mockOutputStream = mock(ArchiveOutputStream.class);
        changes = new LinkedHashSet<>();
        when(mockChangeSet.getChanges()).thenReturn(changes);
        performer = new ChangeSetPerformer(mockChangeSet);
    }

    @Test
    public void testPerform_AddReplaceMode() throws IOException {
        Change change = mock(Change.class);
        ArchiveEntry mockEntry = mock(ArchiveEntry.class);
        InputStream mockInput = mock(InputStream.class);
        
        when(change.type()).thenReturn(Change.TYPE_ADD);
        when(change.isReplaceMode()).thenReturn(true);
        when(change.getInput()).thenReturn(mockInput);
        when(change.getEntry()).thenReturn(mockEntry);
        
        changes.add(change);

        performer.perform(mockInputStream, mockOutputStream);

        verify(mockOutputStream).putArchiveEntry(mockEntry);
        verify(mockOutputStream).closeArchiveEntry();
    }
    
    @Test
    public void testPerform_Delete() throws IOException {
        Change change = mock(Change.class);
        ArchiveEntry mockEntry = mock(ArchiveEntry.class);

        when(mockInputStream.getNextEntry()).thenReturn(mockEntry, (ArchiveEntry) null);
        when(mockEntry.getName()).thenReturn("test_entry");
        when(change.type()).thenReturn(Change.TYPE_DELETE);
        when(change.targetFile()).thenReturn("test_entry");

        changes.add(change);

        performer.perform(mockInputStream, mockOutputStream);

        verify(mockOutputStream, never()).putArchiveEntry(any(ArchiveEntry.class));
        verify(mockOutputStream, never()).closeArchiveEntry();
    }

    @Test
    public void testPerform_DeleteDir() throws IOException {
        Change change = mock(Change.class);
        ArchiveEntry mockEntry = mock(ArchiveEntry.class);

        when(mockInputStream.getNextEntry()).thenReturn(mockEntry, (ArchiveEntry) null);
        when(mockEntry.getName()).thenReturn("test_entry/test_file");
        when(change.type()).thenReturn(Change.TYPE_DELETE_DIR);
        when(change.targetFile()).thenReturn("test_entry");

        changes.add(change);

        performer.perform(mockInputStream, mockOutputStream);

        verify(mockOutputStream, never()).putArchiveEntry(any(ArchiveEntry.class));
        verify(mockOutputStream, never()).closeArchiveEntry();
    }

    @Test
    public void testPerform_CopyStream() throws IOException {
        Change change = mock(Change.class);
        ArchiveEntry mockEntry = mock(ArchiveEntry.class);

        when(mockInputStream.getNextEntry()).thenReturn(mockEntry, (ArchiveEntry) null);
        when(mockEntry.getName()).thenReturn("test_entry");
        when(change.type()).thenReturn(Change.TYPE_ADD);
        when(change.isReplaceMode()).thenReturn(false);
        when(change.getEntry()).thenReturn(mockEntry);
        
        changes.add(change);

        performer.perform(mockInputStream, mockOutputStream);

        verify(mockOutputStream).putArchiveEntry(mockEntry);
        verify(mockOutputStream).closeArchiveEntry();
    }
}