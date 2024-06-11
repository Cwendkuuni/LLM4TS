import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.archivers.ArchiveOutputStream;
import org.junit.Before;
import org.junit.Test;

public class ChangeSetPerformerTest {

    private ChangeSetPerformer changeSetPerformer;
    private ChangeSet changeSet;

    @Before
    public void setUp() {
        changeSet = new ChangeSet();
        changeSet.addChange(new Change(Change.TYPE_ADD, "file1.txt", false, new ByteArrayInputStream("Hello".getBytes())));
        changeSet.addChange(new Change(Change.TYPE_ADD, "file2.txt", true, new ByteArrayInputStream("World".getBytes())));
        changeSetPerformer = new ChangeSetPerformer(changeSet);
    }

    @Test
    public void testPerform() throws IOException {
        ArchiveEntry entry1 = mock(ArchiveEntry.class);
        ArchiveEntry entry2 = mock(ArchiveEntry.class);
        ArchiveInputStream in = mock(ArchiveInputStream.class);
        ArchiveOutputStream out = mock(ArchiveOutputStream.class);
        
        ChangeSetResults results = changeSetPerformer.perform(in, out);
        
        assertNotNull(results);
    }

    @Test
    public void testIsDeletedLater() {
        // Edge case scenario where an entry is deleted later in the set
        Set<Change> workingSet = new LinkedHashSet<>();
        Change change = new Change(Change.TYPE_DELETE, "file1.txt", false, null);
        workingSet.add(change);

        ArchiveEntry entry = mock(ArchiveEntry.class);
        assertTrue(changeSetPerformer.isDeletedLater(workingSet, entry));
    }

    @Test
    public void testCopyStream() throws IOException {
        ByteArrayInputStream in = new ByteArrayInputStream("Test".getBytes());
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ArchiveEntry entry = mock(ArchiveEntry.class);

        changeSetPerformer.copyStream(in, new ArchiveOutputStream(out), entry);

        assertEquals("Test", out.toString());
    }
}