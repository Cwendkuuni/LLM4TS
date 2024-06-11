import static org.junit.Assert.*;
import org.apache.commons.compress.archivers.ArchiveException;
import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.archivers.ArchiveOutputStream;
import org.junit.Before;
import org.junit.Test;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;

public class ChangeSetPerformerTest {

    private ChangeSet changeSet;
    private ChangeSetPerformer changeSetPerformer;

    @Before
    public void setup() {
        changeSet = new ChangeSet();
        changeSetPerformer = new ChangeSetPerformer(changeSet);
    }

    @Test
    public void testPerform_whenChangeSetIsEmpty_shouldNotMakeAnyChanges() throws IOException {
        // Arrange
        ArchiveInputStream in = null;
        ArchiveOutputStream out = null;

        // Act
        ChangeSetResults results = changeSetPerformer.perform(in, out);

        // Assert
        assertTrue(results.getAddedFiles().isEmpty());
        assertTrue(results.getDeletedFiles().isEmpty());
    }

    @Test
    public void testPerform_whenChangeSetHasAddChange_shouldAddFileToOutputStreamAndRemoveFromChangeSet() throws IOException {
        // Arrange
        InputStream input = null;
        ArchiveEntry entry = null;
        changeSet.addChange(new Change(Change.TYPE_ADD, true, input, entry));
        ArchiveInputStream in = null;
        ArchiveOutputStream out = null;

        // Act
        ChangeSetResults results = changeSetPerformer.perform(in, out);

        // Assert
        assertTrue(results.getAddedFiles().contains(entry.getName()));
        assertFalse(changeSet.getChanges().contains(entry.getName()));
    }

    @Test
    public void testPerform_whenChangeSetHasDeleteChange_shouldDeleteFileFromInputStreamAndRemoveFromChangeSet() throws IOException {
        // Arrange
        ArchiveEntry entry = null;
        changeSet.addChange(new Change(Change.TYPE_DELETE, true, entry.getName()));
        ArchiveInputStream in = null;
        ArchiveOutputStream out = null;

        // Act
        ChangeSetResults results = changeSetPerformer.perform(in, out);

        // Assert
        assertTrue(results.getDeletedFiles().contains(entry.getName()));
        assertFalse(changeSet.getChanges().contains(entry.getName()));
    }

    @Test
    public void testPerform_whenChangeSetHasDeleteDirChange_shouldDeleteDirectoryFromInputStreamAndRemoveFromChangeSet() throws IOException {
        // Arrange
        ArchiveEntry entry = null;
        changeSet.addChange(new Change(Change.TYPE_DELETE_DIR, true, entry.getName()));
        ArchiveInputStream in = null;
        ArchiveOutputStream out = null;

        // Act
        ChangeSetResults results = changeSetPerformer.perform(in, out);

        // Assert
        assertTrue(results.getDeletedFiles().contains(entry.getName()));
        assertFalse(changeSet.getChanges().contains(entry.getName()));
    }

    @Test
    public void testIsDeletedLater_whenWorkingSetIsEmpty_shouldReturnFalse() {
        // Arrange
        ArchiveEntry entry = null;
        Set<Change> workingSet = new LinkedHashSet<>();

        // Act
        boolean result = changeSetPerformer.isDeletedLater(workingSet, entry);

        // Assert
        assertFalse(result);
    }

    @Test
    public void testIsDeletedLater_whenWorkingSetHasDeleteChangeMatchingSource_shouldReturnTrue() {
        // Arrange
        ArchiveEntry entry = null;
        String source = "";
        Set<Change> workingSet = new LinkedHashSet<>();
        workingSet.add(new Change(Change.TYPE_DELETE, true, source));

        // Act
        boolean result = changeSetPerformer.isDeletedLater(workingSet, entry);

        // Assert
        assertTrue(result);
    }

    @Test
    public void testIsDeletedLater_whenWorkingSetHasDeleteDirChangeMatchingSource_shouldReturnTrue() {
        // Arrange
        ArchiveEntry entry = null;
        String source = "";
        Set<Change> workingSet = new LinkedHashSet<>();
        workingSet.add(new Change(Change.TYPE_DELETE_DIR, true, source));

        // Act
        boolean result = changeSetPerformer.isDeletedLater(workingSet, entry);

        // Assert
        assertTrue(result);
    }

    @Test
    public void testIsDeletedLater_whenWorkingSetHasDeleteChangeNotMatchingSource_shouldReturnFalse() {
        // Arrange
        ArchiveEntry entry = null;
        Set<Change> workingSet = new LinkedHashSet<>();
        workingSet.add(new Change(Change.TYPE_DELETE, true, "target"));

        // Act
        boolean result = changeSetPerformer.isDeletedLater(workingSet, entry);

        // Assert
        assertFalse(result);
    }

    @Test
    public void testIsDeletedLater_whenWorkingSetHasDeleteDirChangeNotMatchingSource_shouldReturnFalse() {
        // Arrange
        ArchiveEntry entry = null;
        Set<Change> workingSet = new LinkedHashSet<>();
        workingSet.add(new Change(Change.TYPE_DELETE_DIR, true, "target"));

        // Act
        boolean result = changeSetPerformer.isDeletedLater(workingSet, entry);

        // Assert
        assertFalse(result);
    }

    @Test
    public void testCopyStream_shouldPutEntryToOutputStreamAndCopyData() throws IOException, ArchiveException {
        // Arrange
        InputStream in = null;
        ArchiveOutputStream out = null;
        ArchiveEntry entry = null;

        // Act
        changeSetPerformer.copyStream(in, out, entry);

        // Assert
        // How to verify that putArchiveEntry() and closeArchiveEntry() have been called?
    }
}