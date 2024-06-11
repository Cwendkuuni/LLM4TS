import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.archivers.ArchiveOutputStream;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;

public class ChangeSetPerformerTest {
    
    private ChangeSetPerformer performer;
    @Mock
    private ArchiveInputStream mockInputStream;
    @Mock
    private ArchiveOutputStream mockOutputStream;
    @Mock
    private ChangeSet mockChangeSet;
    @Mock
    private Change mockChange;
    @Mock
    private InputStream mockInputStream2;
    private ArchiveEntry mockEntry;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        performer = new ChangeSetPerformer(mockChangeSet);
    }

    @Test
    public void testPerform() throws IOException {
        Set<Change> changes = new LinkedHashSet<>();
        changes.add(mockChange);
        when(mockChange.type()).thenReturn(Change.TYPE_ADD);
        when(mockChange.isReplaceMode()).thenReturn(true);
        when(mockChange.getInput()).thenReturn(mockInputStream2);
        when(mockChange.getEntry()).thenReturn(mockEntry);
        when(mockChangeSet.getChanges()).thenReturn(changes);

        ChangeSetResults results = performer.perform(mockInputStream, mockOutputStream);

        verify(mockChange, times(1)).type();
        verify(mockChange, times(1)).isReplaceMode();
        verify(mockChange, times(1)).getInput();
        verify(mockChange, times(1)).getEntry();
        verify(mockChangeSet, times(1)).getChanges();
        verify(mockInputStream, times(1)).getNextEntry();
        verify(mockChangeSet, times(1)).getChanges();

        assertTrue(results.getAddedFromChangeSet().contains(mockEntry.getName()));
        assertFalse(results.getDeleted().contains(mockEntry.getName()));
        assertFalse(results.getAddedFromStream().contains(mockEntry.getName()));
    }

    @Test
    public void testIsDeletedLater() {
        Set<Change> workingSet = new HashSet<>();
        workingSet.add(mockChange);
        String source = "source";
        when(mockChange.type()).thenReturn(Change.TYPE_DELETE);
        when(mockChange.targetFile()).thenReturn(source);

        boolean result = performer.isDeletedLater(workingSet, mockEntry);

        verify(mockChange, times(1)).type();
        verify(mockChange, times(2)).targetFile();

        assertTrue(result);
    }

    @Test
    public void testCopyStream() throws IOException {
        ArchiveEntry mockEntry = mock(ArchiveEntry.class);
        when(mockEntry.getName()).thenReturn("entry");

        performer.copyStream(mockInputStream2, mockOutputStream, mockEntry);

        verify(mockOutputStream, times(1)).putArchiveEntry(mockEntry);
        verify(mockInputStream2, times(1)).close();
        verify(mockOutputStream, times(1)).closeArchiveEntry();
    }
}