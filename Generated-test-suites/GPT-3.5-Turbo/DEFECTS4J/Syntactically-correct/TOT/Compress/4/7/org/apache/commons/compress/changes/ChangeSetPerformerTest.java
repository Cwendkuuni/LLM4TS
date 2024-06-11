import static org.mockito.Mockito.*;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.archivers.ArchiveOutputStream;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class ChangeSetPerformerTest {

    private ChangeSetPerformer performer;
    private ChangeSet changeSet;
    private ArchiveInputStream in;
    private ArchiveOutputStream out;
    private ChangeSetResults results;

    @Before
    public void setup() {
        changeSet = mock(ChangeSet.class);
        in = mock(ArchiveInputStream.class);
        out = mock(ArchiveOutputStream.class);
        performer = new ChangeSetPerformer(changeSet);
        results = new ChangeSetResults();
    }

    @Test
    public void testPerform_AddChangeWithReplaceMode_CopiesStreamAndAddsToResults() throws IOException {
        // Arrange
        changeSet.getChanges();

        Change change = mock(Change.class);
        ArchiveEntry entry = mock(ArchiveEntry.class);
        InputStream input = mock(InputStream.class);

        Set<Change> changes = new HashSet<>();
        changes.add(change);

        when(change.type()).thenReturn(Change.TYPE_ADD);
        when(change.isReplaceMode()).thenReturn(true);
        when(change.getInput()).thenReturn(input);
        when(change.getEntry()).thenReturn(entry);

        when(changeSet.getChanges()).thenReturn(changes);
        when(in.getNextEntry()).thenReturn(null);

        // Act
        performer.perform(in, out);

        // Assert
        verify(performer).copyStream(input, out, entry);
        Assert.assertTrue(results.hasBeenAdded(entry.getName()));
        Assert.assertEquals(1, results.getAddedFromChangeSet().size());
        Assert.assertEquals(entry.getName(), results.getAddedFromChangeSet().get(0));
    }

    @Test
    public void testPerform_DeleteChangeWithMatchingName_RemovesFromWorkingSetAndAddsToResults() throws IOException {
        // Arrange
        changeSet.getChanges();

        Change change = mock(Change.class);
        ArchiveEntry entry = mock(ArchiveEntry.class);

        Set<Change> changes = new HashSet<>();
        changes.add(change);

        String targetFile = "test.txt";
        String name = "test.txt";

        when(change.type()).thenReturn(Change.TYPE_DELETE);
        when(change.targetFile()).thenReturn(targetFile);

        when(entry.getName()).thenReturn(name);
        when(changeSet.getChanges()).thenReturn(changes);

        // Act
        performer.perform(in, out);

        // Assert
        verifyZeroInteractions(in);
        Assert.assertFalse(results.hasBeenAdded(name));
        Assert.assertEquals(1, results.getDeleted().size());
        Assert.assertEquals(name, results.getDeleted().get(0));
    }

    @Test
    public void testPerform_DeleteDirChangeWithMatchingName_RemovesFromWorkingSetAndAddsToResults() throws IOException {
        // Arrange
        changeSet.getChanges();

        Change change = mock(Change.class);
        ArchiveEntry entry = mock(ArchiveEntry.class);

        Set<Change> changes = new HashSet<>();
        changes.add(change);

        String targetFile = "testDir";
        String name = "testDir/subDir1";

        when(change.type()).thenReturn(Change.TYPE_DELETE_DIR);
        when(change.targetFile()).thenReturn(targetFile);

        when(entry.getName()).thenReturn(name);
        when(changeSet.getChanges()).thenReturn(changes);

        // Act
        performer.perform(in, out);

        // Assert
        verifyZeroInteractions(in);
        Assert.assertFalse(results.hasBeenAdded(name));
        Assert.assertEquals(1, results.getDeleted().size());
        Assert.assertEquals(name, results.getDeleted().get(0));
    }

    @Test
    public void testPerform_CopyEntry_CopiesToOutputStreamAndAddsToResults() throws IOException {
        // Arrange
        changeSet.getChanges();

        Change change = mock(Change.class);
        ArchiveEntry entry = mock(ArchiveEntry.class);

        Set<Change> changes = new HashSet<>();
        changes.add(change);

        when(change.type()).thenReturn(Change.TYPE_COPY);

        when(entry.getName()).thenReturn("test.txt");
        when(changeSet.getChanges()).thenReturn(changes);
        when(in.getNextEntry()).thenReturn(entry, null);

        // Act
        performer.perform(in, out);

        // Assert
        verify(performer).copyStream(in, out, entry);
        Assert.assertTrue(results.hasBeenAdded(entry.getName()));
        Assert.assertEquals(1, results.getAddedFromStream().size());
        Assert.assertEquals(entry.getName(), results.getAddedFromStream().get(0));
    }

    @Test
    public void testPerform_AddChangeWithNoReplaceMode_CopiesStreamAndAddsToResults() throws IOException {
        // Arrange
        changeSet.getChanges();

        Change change = mock(Change.class);
        ArchiveEntry entry = mock(ArchiveEntry.class);
        InputStream input = mock(InputStream.class);

        Set<Change> changes = new HashSet<>();
        changes.add(change);

        when(change.type()).thenReturn(Change.TYPE_ADD);
        when(change.isReplaceMode()).thenReturn(false);
        when(change.getInput()).thenReturn(input);
        when(change.getEntry()).thenReturn(entry);

        when(changeSet.getChanges()).thenReturn(changes);
        when(in.getNextEntry()).thenReturn(null);

        // Act
        performer.perform(in, out);

        // Assert
        verify(performer).copyStream(input, out, entry);
        Assert.assertTrue(results.hasBeenAdded(entry.getName()));
        Assert.assertEquals(1, results.getAddedFromChangeSet().size());
        Assert.assertEquals(entry.getName(), results.getAddedFromChangeSet().get(0));
    }

    @Test
    public void testIsDeletedLater_EntryDeletedLater_ReturnsTrue() {
        // Arrange
        Set<Change> workingSet = new HashSet<>();
        ArchiveEntry entry = mock(ArchiveEntry.class);
        String source = "test.txt";
        String target = "test.txt";

        Change change = mock(Change.class);
        when(change.type()).thenReturn(Change.TYPE_DELETE);
        when(change.targetFile()).thenReturn(target);

        workingSet.add(change);

        // Act
        boolean result = performer.isDeletedLater(workingSet, entry);

        // Assert
        Assert.assertTrue(result);
    }

    @Test
    public void testIsDeletedLater_DirectoryDeletedLater_ReturnsTrue() {
        // Arrange
        Set<Change> workingSet = new HashSet<>();
        ArchiveEntry entry = mock(ArchiveEntry.class);

        String source = "testDir/subDir1";
        String target = "testDir";

        when(entry.getName()).thenReturn(source);

        Change change = mock(Change.class);
        when(change.type()).thenReturn(Change.TYPE_DELETE_DIR);
        when(change.targetFile()).thenReturn(target);

        workingSet.add(change);

        // Act
        boolean result = performer.isDeletedLater(workingSet, entry);

        // Assert
        Assert.assertTrue(result);
    }

    @Test
    public void testIsDeletedLater_EntryNotDeletedLater_ReturnsFalse() {
        // Arrange
        Set<Change> workingSet = new HashSet<>();
        ArchiveEntry entry = mock(ArchiveEntry.class);
        String source = "test.txt";
        String target = "test2.txt";

        Change change = mock(Change.class);
        when(change.type()).thenReturn(Change.TYPE_DELETE);
        when(change.targetFile()).thenReturn(target);

        workingSet.add(change);

        // Act
        boolean result = performer.isDeletedLater(workingSet, entry);

        // Assert
        Assert.assertFalse(result);
    }

    @Test
    public void testIsDeletedLater_DirectoryNotDeletedLater_ReturnsFalse() {
        // Arrange
        Set<Change> workingSet = new HashSet<>();
        ArchiveEntry entry = mock(ArchiveEntry.class);

        String source = "testDir/subDir1";
        String target = "testDir2";

        when(entry.getName()).thenReturn(source);

        Change change = mock(Change.class);
        when(change.type()).thenReturn(Change.TYPE_DELETE_DIR);
        when(change.targetFile()).thenReturn(target);

        workingSet.add(change);

        // Act
        boolean result = performer.isDeletedLater(workingSet, entry);

        // Assert
        Assert.assertFalse(result);
    }

    @Test
    public void testCopyStream_CopiesInputStreamToOutputStream() throws IOException {
        // Arrange
        InputStream in = mock(InputStream.class);
        ArchiveOutputStream out = mock(ArchiveOutputStream.class);
        ArchiveEntry entry = mock(ArchiveEntry.class);

        // Act
        performer.copyStream(in, out, entry);

        // Assert
        verify(out).putArchiveEntry(entry);
        verify(out).closeArchiveEntry();
    }

}