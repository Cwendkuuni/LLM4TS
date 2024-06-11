import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.archivers.ArchiveOutputStream;
import org.junit.Before;
import org.junit.Test;

public class ChangeSetPerformerTest {

    private ChangeSetPerformer performer;
    private ChangeSet changeSet;

    @Before
    public void setUp() {
        changeSet = mock(ChangeSet.class);
        performer = new ChangeSetPerformer(changeSet);
    }

    @Test
    public void testPerform_AddReplaceMode() throws IOException {
        // Mock data
        ArchiveInputStream in = mock(ArchiveInputStream.class);
        ArchiveOutputStream out = mock(ArchiveOutputStream.class);
        ArchiveEntry entry = mock(ArchiveEntry.class);

        when(changeSet.getChanges()).thenReturn(new LinkedHashSet<>());

        ChangeSetResults results = performer.perform(in, out);

        assertEquals(0, results.getAddedFromChangeSet().size());
        assertEquals(0, results.getDeleted().size());
        assertEquals(0, results.getAddedFromStream().size());
    }

    @Test
    public void testPerform_Delete() throws IOException {
        // Mock data
        ArchiveInputStream in = mock(ArchiveInputStream.class);
        ArchiveOutputStream out = mock(ArchiveOutputStream.class);
        ArchiveEntry entry = mock(ArchiveEntry.class);

        Change change = mock(Change.class);
        when(change.type()).thenReturn(Change.TYPE_DELETE);
        when(change.targetFile()).thenReturn("file.txt");

        LinkedHashSet<Change> changes = new LinkedHashSet<>();
        changes.add(change);
        when(changeSet.getChanges()).thenReturn(changes);

        ChangeSetResults results = performer.perform(in, out);

        assertEquals(0, results.getAddedFromChangeSet().size());
        assertEquals(1, results.getDeleted().size());
        assertEquals(0, results.getAddedFromStream().size());
    }

    @Test
    public void testPerform_AddFromStream() throws IOException {
        // Mock data
        ArchiveInputStream in = new ArchiveInputStream(new ByteArrayInputStream("Test".getBytes())); // Simulate input data
        ArchiveOutputStream out = mock(ArchiveOutputStream.class);
        ArchiveEntry entry = mock(ArchiveEntry.class);

        Change change = mock(Change.class);
        when(change.type()).thenReturn(Change.TYPE_ADD);
        when(change.isReplaceMode()).thenReturn(false);
        when(change.getInput()).thenReturn(in);
        when(change.getEntry()).thenReturn(entry);

        LinkedHashSet<Change> changes = new LinkedHashSet<>();
        changes.add(change);
        when(changeSet.getChanges()).thenReturn(changes);

        ChangeSetResults results = performer.perform(in, out);

        assertEquals(1, results.getAddedFromChangeSet().size());
        assertEquals(0, results.getDeleted().size());
        assertEquals(0, results.getAddedFromStream().size());
    }

    // Add more test cases for other scenarios
    
}