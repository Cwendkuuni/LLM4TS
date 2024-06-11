import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.archivers.ArchiveOutputStream;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class ChangeSetPerformerTest {
    
    ChangeSetPerformer changeSetPerformer;
    
    @Before
    public void setUp() {
        ChangeSet changeSet = mock(ChangeSet.class);
        Set changes = mock(Set.class);
        when(changeSet.getChanges()).thenReturn(changes);
        changeSetPerformer = new ChangeSetPerformer(changeSet);
    }

    @Test
    public void testPerform() throws IOException {
        ArchiveInputStream in = mock(ArchiveInputStream.class);
        ArchiveOutputStream out = mock(ArchiveOutputStream.class);
        ChangeSetResults results = changeSetPerformer.perform(in, out);
        assertNotNull(results);
    }

    @Test
    public void testIsDeletedLater() {
        Set workingSet = mock(Set.class);
        ArchiveEntry entry = mock(ArchiveEntry.class);
        boolean deletedLater = changeSetPerformer.isDeletedLater(workingSet, entry);
        assertFalse(deletedLater);
    }

    @Test
    public void testCopyStream() throws IOException {
        InputStream in = mock(InputStream.class);
        ArchiveOutputStream out = mock(ArchiveOutputStream.class);
        ArchiveEntry entry = mock(ArchiveEntry.class);
        changeSetPerformer.copyStream(in, out, entry);
        verify(out).putArchiveEntry(entry);
        verify(in).close();
        verify(out).closeArchiveEntry();
    }
}