import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.LinkedHashSet;
import java.util.Set;
import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveInputStream;
import org.apache.commons.compress.archivers.tar.TarArchiveOutputStream;
import org.apache.commons.compress.changes.Change;
import org.apache.commons.compress.changes.ChangeSet;
import org.apache.commons.compress.changes.ChangeSetPerformer;
import org.apache.commons.compress.utils.IOUtils;

public class ChangeSetPerformerTest {

    private ChangeSetPerformer performer;
    private ChangeSet changeSet;

    @Before
    public void setUp() {
        changeSet = new ChangeSet();
        performer = new ChangeSetPerformer(changeSet);
    }

    @Test
    public void testPerform() throws IOException {
        // Create input and output streams for testing
        ByteArrayInputStream in = new ByteArrayInputStream("Test content".getBytes());
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        // Add a change to the change set
        TarArchiveEntry entry = new TarArchiveEntry("file.txt");
        changeSet.add(new Change(Change.TYPE_ADD, entry, in));

        // Perform the changes
        performer.perform(new TarArchiveInputStream(in), new TarArchiveOutputStream(out));

        // Check if the file was added successfully
        TarArchiveInputStream resultIn = new TarArchiveInputStream(new ByteArrayInputStream(out.toByteArray()));
        ArchiveEntry resultEntry = resultIn.getNextEntry();
        assertEquals("file.txt", resultEntry.getName());
        assertEquals("Test content", IOUtils.toString(resultIn));

        // Check if there are no more entries
        assertNull(resultIn.getNextEntry());
    }

    @Test
    public void testPerformWithDeleteChange() throws IOException {
        // Create input and output streams for testing
        ByteArrayInputStream in = new ByteArrayInputStream("Test content".getBytes());
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        // Add a change to delete a file
        changeSet.add(new Change(Change.TYPE_DELETE, new TarArchiveEntry("file.txt"), null));

        // Add a file to the input stream
        TarArchiveOutputStream tarOut = new TarArchiveOutputStream(out);
        TarArchiveEntry entry = new TarArchiveEntry("file.txt");
        tarOut.putArchiveEntry(entry);
        tarOut.write("Test content".getBytes());
        tarOut.closeArchiveEntry();
        tarOut.close();

        // Perform the changes
        performer.perform(new TarArchiveInputStream(new ByteArrayInputStream(out.toByteArray())), new TarArchiveOutputStream(out));

        // Check if the file was deleted successfully
        TarArchiveInputStream resultIn = new TarArchiveInputStream(new ByteArrayInputStream(out.toByteArray()));
        assertNull(resultIn.getNextEntry());
    }

    @Test
    public void testPerformWithDeleteDirChange() throws IOException {
        // Create input and output streams for testing
        ByteArrayInputStream in = new ByteArrayInputStream("Test content".getBytes());
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        // Add a change to delete a directory
        changeSet.add(new Change(Change.TYPE_DELETE_DIR, new TarArchiveEntry("dir/"), null));

        // Add a file in the directory to the input stream
        TarArchiveOutputStream tarOut = new TarArchiveOutputStream(out);
        TarArchiveEntry entry = new TarArchiveEntry("dir/file.txt");
        tarOut.putArchiveEntry(entry);
        tarOut.write("Test content".getBytes());
        tarOut.closeArchiveEntry();
        tarOut.close();

        // Perform the changes
        performer.perform(new TarArchiveInputStream(new ByteArrayInputStream(out.toByteArray())), new TarArchiveOutputStream(out));

        // Check if the directory was deleted successfully
        TarArchiveInputStream resultIn = new TarArchiveInputStream(new ByteArrayInputStream(out.toByteArray()));
        assertNull(resultIn.getNextEntry());
    }

    @Test
    public void testPerformWithAddReplaceChange() throws IOException {
        // Create input and output streams for testing
        ByteArrayInputStream in = new ByteArrayInputStream("Test content 2".getBytes());
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        // Add a change to add and replace a file
        changeSet.add(new Change(Change.TYPE_ADD | Change.MODE_REPLACE, new TarArchiveEntry("file.txt"), in));

        // Add a file to the input stream
        TarArchiveOutputStream tarOut = new TarArchiveOutputStream(out);
        TarArchiveEntry entry = new TarArchiveEntry("file.txt");
        tarOut.putArchiveEntry(entry);
        tarOut.write("Test content 1".getBytes());
        tarOut.closeArchiveEntry();
        tarOut.close();

        // Perform the changes
        performer.perform(new TarArchiveInputStream(new ByteArrayInputStream(out.toByteArray())), new TarArchiveOutputStream(out));

        // Check if the file was added and replaced successfully
        TarArchiveInputStream resultIn = new TarArchiveInputStream(new ByteArrayInputStream(out.toByteArray()));
        ArchiveEntry resultEntry = resultIn.getNextEntry();
        assertEquals("file.txt", resultEntry.getName());
        assertEquals("Test content 2", IOUtils.toString(resultIn));

        // Check if there are no more entries
        assertNull(resultIn.getNextEntry());
    }
}

