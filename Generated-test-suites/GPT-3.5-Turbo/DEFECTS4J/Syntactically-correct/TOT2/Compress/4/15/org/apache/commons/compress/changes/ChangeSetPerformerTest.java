import static org.junit.Assert.*;
import org.junit.Test;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.archivers.ArchiveOutputStream;
import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveInputStream;
import org.apache.commons.compress.archivers.tar.TarArchiveOutputStream;
import org.apache.commons.compress.changes.Change;
import org.apache.commons.compress.changes.ChangeSet;
import org.apache.commons.compress.changes.ChangeSetPerformer;
import org.junit.*;

public class ChangeSetPerformerTest {

    @Test
    public void perform_WhenAddingFileToOutputStream_AddsFile() throws IOException {
        ChangeSet changeSet = new ChangeSet();
        ArchiveEntry entry = new TarArchiveEntry("test.txt");
        ByteArrayInputStream input = new ByteArrayInputStream("Hello, World!".getBytes());
        changeSet.add(new Change(Change.TYPE_ADD, entry, input));
        
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ArchiveInputStream in = new TarArchiveInputStream(new ByteArrayInputStream(new byte[0]));
        ArchiveOutputStream out = new TarArchiveOutputStream(outputStream);

        ChangeSetPerformer performer = new ChangeSetPerformer(changeSet);
        ChangeSetResults results = performer.perform(in, out);

        assertTrue(results.hasBeenAdded("test.txt"));
    }

    @Test
    public void perform_WhenDeletingFileFromOutputStream_DeletesFile() throws IOException {
        ChangeSet changeSet = new ChangeSet();
        ArchiveEntry entry = new TarArchiveEntry("test.txt");
        changeSet.add(new Change(Change.TYPE_DELETE, "test.txt"));
        
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ArchiveInputStream in = new TarArchiveInputStream(new ByteArrayInputStream(new byte[0]));
        ArchiveOutputStream out = new TarArchiveOutputStream(outputStream);

        ChangeSetPerformer performer = new ChangeSetPerformer(changeSet);
        ChangeSetResults results = performer.perform(in, out);

        assertTrue(results.hasBeenDeleted("test.txt"));
    }

    @Test
    public void perform_WhenAddingDuplicateFile_DoesNotAddDuplicate() throws IOException {
        ChangeSet changeSet = new ChangeSet();
        ArchiveEntry entry = new TarArchiveEntry("test.txt");
        ByteArrayInputStream input = new ByteArrayInputStream("Hello, World!".getBytes());
        changeSet.add(new Change(Change.TYPE_ADD, entry, input));

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ArchiveInputStream in = new TarArchiveInputStream(new ByteArrayInputStream(new byte[0]));
        ArchiveOutputStream out = new TarArchiveOutputStream(outputStream);

        ChangeSetPerformer performer = new ChangeSetPerformer(changeSet);
        ChangeSetResults results = performer.perform(in, out);

        changeSet.add(new Change(Change.TYPE_ADD, entry, input));

        performer = new ChangeSetPerformer(changeSet);
        results = performer.perform(in, out);

        assertEquals(1, results.getAddedCount("test.txt"));
    }

}