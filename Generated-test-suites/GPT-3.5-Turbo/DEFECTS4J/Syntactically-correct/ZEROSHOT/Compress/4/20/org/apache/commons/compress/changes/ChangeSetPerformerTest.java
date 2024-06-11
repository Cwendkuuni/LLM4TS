import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.LinkedHashSet;
import java.util.Set;

import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.archivers.ArchiveOutputStream;
import org.junit.Before;
import org.junit.Test;

public class ChangeSetPerformerTest {

    private ChangeSetPerformer performer;
    private ArchiveInputStream in;
    private ArchiveOutputStream out;

    @Before
    public void setUp() throws IOException {
        ChangeSet changeSet = new ChangeSet();
        Set<Change> changes = new LinkedHashSet<>();
        changes.add(new Change(Change.TYPE_ADD, "file1", false, new ByteArrayInputStream(new byte[0])));
        changes.add(new Change(Change.TYPE_DELETE, "file2", false, null));
        changes.add(new Change(Change.TYPE_DELETE_DIR, "dir", false, null));
        changes.add(new Change(Change.TYPE_ADD, "file3", true, new ByteArrayInputStream(new byte[0])));
        changeSet.setChanges(changes);

        performer = new ChangeSetPerformer(changeSet);
        in = new ArchiveInputStream() {
            @Override
            public ArchiveEntry getNextEntry() throws IOException {
                return null;
            }
        };
        out = new ArchiveOutputStream() {
            @Override
            public void putArchiveEntry(ArchiveEntry entry) throws IOException {
            }

            @Override
            public void closeArchiveEntry() throws IOException {
            }

            @Override
            public void write(byte[] b, int off, int len) throws IOException {
            }

            @Override
            public void finish() throws IOException {
            }

            @Override
            public void close() throws IOException {
            }
        };
    }

    @Test
    public void testPerform() throws IOException {
        ChangeSetResults results = performer.perform(in, out);
        assertNotNull(results);
    }

    @Test
    public void testIsDeletedLater() {
        Set<Change> workingSet = new LinkedHashSet<>();
        workingSet.add(new Change(Change.TYPE_DELETE, "file2", false, null));
        workingSet.add(new Change(Change.TYPE_DELETE_DIR, "dir", false, null));

        ArchiveEntry entry1 = new ArchiveEntry() {
            @Override
            public String getName() {
                return "file1";
            }
        };
        ArchiveEntry entry2 = new ArchiveEntry() {
            @Override
            public String getName() {
                return "file2";
            }
        };
        ArchiveEntry entry3 = new ArchiveEntry() {
            @Override
            public String getName() {
                return "dir/file";
            }
        };

        assertFalse(performer.isDeletedLater(workingSet, entry1));
        assertTrue(performer.isDeletedLater(workingSet, entry2));
        assertTrue(performer.isDeletedLater(workingSet, entry3));
    }

    @Test
    public void testCopyStream() throws IOException {
        ArchiveEntry entry = new ArchiveEntry() {
            @Override
            public String getName() {
                return "file";
            }
        };

        performer.copyStream(new ByteArrayInputStream(new byte[0]), out, entry);
    }

}