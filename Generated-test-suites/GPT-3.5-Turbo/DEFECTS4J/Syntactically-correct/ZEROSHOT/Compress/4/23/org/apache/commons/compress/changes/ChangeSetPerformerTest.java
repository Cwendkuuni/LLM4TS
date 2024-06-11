import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.archivers.ArchiveOutputStream;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;

import static org.mockito.Mockito.*;

public class ChangeSetPerformerTest {

    private ChangeSetPerformer changeSetPerformer;

    @Mock
    private ChangeSet changeSet;

    @Mock
    private ArchiveInputStream in;

    @Mock
    private ArchiveOutputStream out;

    @Mock
    private ChangeSetResults results;

    @Mock
    private ArchiveEntry entry;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        changeSetPerformer = new ChangeSetPerformer(changeSet);
    }

    @Test
    public void testPerform() throws IOException {
        Set<Change> changes = new LinkedHashSet<>();
        changes.add(mock(Change.class));
        when(changeSet.getChanges()).thenReturn(changes);
        when(change.type()).thenReturn(Change.TYPE_ADD);
        when(change.isReplaceMode()).thenReturn(true);

        changeSetPerformer.perform(in, out);

        verify(changeSet).getChanges();
        verify(in, times(2)).getNextEntry();
        verify(out, times(1)).putArchiveEntry(entry);
        verify(out, times(1)).closeArchiveEntry();
    }

    @Test
    public void testIsDeletedLater() {
        Set<Change> workingSet = new LinkedHashSet<>();
        workingSet.add(change);

        when(change.type()).thenReturn(Change.TYPE_DELETE);
        when(change.targetFile()).thenReturn("test");

        when(entry.getName()).thenReturn("test");

        boolean result = changeSetPerformer.isDeletedLater(workingSet, entry);

        assert(result);
        verify(change, times(1)).type();
        verify(entry, times(2)).getName();
    }

    @Test
    public void testCopyStream() throws IOException {
        InputStream inputStream = mock(InputStream.class);
        when(inputStream.read()).thenReturn(-1);

        changeSetPerformer.copyStream(inputStream, out, entry);

        verify(out, times(1)).putArchiveEntry(entry);
        verify(out, times(1)).closeArchiveEntry();
        verify(out, times(1)).write(-1);
    }
}