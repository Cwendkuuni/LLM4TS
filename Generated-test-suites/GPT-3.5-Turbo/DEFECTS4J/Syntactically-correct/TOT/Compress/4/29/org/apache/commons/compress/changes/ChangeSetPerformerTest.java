
import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.archivers.ArchiveOutputStream;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class ChangeSetPerformerTest {

    @Mock
    private ChangeSet changeSet;
    @Mock
    private ArchiveInputStream archiveInputStream;
    @Mock
    private ArchiveOutputStream archiveOutputStream;
    @Mock
    private Change change;
    @Mock
    private ArchiveEntry archiveEntry;
    @Mock
    private InputStream inputStream;
    @Mock
    private ChangeSetResults changeSetResults;

    private ChangeSetPerformer changeSetPerformer;
    private Set<Change> changes;

    @Before
    public void setUp() throws Exception {
        changes = new LinkedHashSet<>();
        changes.add(change);
        when(changeSet.getChanges()).thenReturn(changes);
        changeSetPerformer = new ChangeSetPerformer(changeSet);
    }

    @Test
    public void testPerform_AddReplaceMode_ChangesApplied() throws IOException {
        // Arrange
        when(change.type()).thenReturn(Change.TYPE_ADD);
        when(change.isReplaceMode()).thenReturn(true);
        when(change.getInput()).thenReturn(inputStream);
        when(change.getEntry()).thenReturn(archiveEntry);

        // Act
        ChangeSetResults results = changeSetPerformer.perform(archiveInputStream, archiveOutputStream);

        // Assert
        verify(archiveOutputStream, times(1)).putArchiveEntry(archiveEntry);
        verify(archiveOutputStream, times(1)).closeArchiveEntry();
        verify(changeSetResults, times(1)).addedFromChangeSet(archiveEntry.getName());
        Assert.assertTrue(results.hasBeenAdded(archiveEntry.getName()));
    }

    @Test
    public void testPerform_DeleteFile_ChangesApplied() throws IOException {
        // Arrange
        String targetFile = "testFile";
        String entryName = "testEntry";
        when(change.type()).thenReturn(Change.TYPE_DELETE);
        when(change.targetFile()).thenReturn(targetFile);
        when(archiveEntry.getName()).thenReturn(entryName);

        // Act
        ChangeSetResults results = changeSetPerformer.perform(archiveInputStream, archiveOutputStream);

        // Assert
        verify(changeSetResults, times(1)).deleted(entryName);
        Assert.assertTrue(results.hasBeenDeleted(entryName));
    }

    @Test
    public void testPerform_DeleteDirectory_ChangesApplied() throws IOException {
        // Arrange
        String targetDir = "testDir";
        String entryName = "testEntry/";
        when(change.type()).thenReturn(Change.TYPE_DELETE_DIR);
        when(change.targetFile()).thenReturn(targetDir);
        when(archiveEntry.getName()).thenReturn(entryName);

        // Act
        ChangeSetResults results = changeSetPerformer.perform(archiveInputStream, archiveOutputStream);

        // Assert
        verify(changeSetResults, times(1)).deleted(entryName);
        Assert.assertTrue(results.hasBeenDeleted(entryName));
    }

    @Test
    public void testPerform_CopyStreamWithEntry_NotDeletedLaterAndNotAlreadyAdded() throws IOException {
        // Arrange
        String entryName = "testEntry";
        when(archiveEntry.getName()).thenReturn(entryName);
        when(change.type()).thenReturn(Change.TYPE_OTHER);
        when(archiveInputStream.getNextEntry()).thenReturn(archiveEntry);
        when(change.getInput()).thenReturn(inputStream);

        // Act
        ChangeSetResults results = changeSetPerformer.perform(archiveInputStream, archiveOutputStream);

        // Assert
        verify(archiveOutputStream, times(1)).putArchiveEntry(archiveEntry);
        verify(archiveOutputStream, times(1)).closeArchiveEntry();
        verifyChangeSetResultsAddedFromStream(entryName);
        Assert.assertTrue(results.hasBeenAdded(entryName));
    }

    @Test
    public void testPerform_CopyStreamWithEntry_DeletedLater_NotAdded() throws IOException {
        // Arrange
        String entryName = "testEntry";
        when(archiveEntry.getName()).thenReturn(entryName);
        when(change.type()).thenReturn(Change.TYPE_OTHER);
        when(archiveInputStream.getNextEntry()).thenReturn(archiveEntry);
        when(change.getInput()).thenReturn(inputStream);
        changes.add(change);

        // Act
        ChangeSetResults results = changeSetPerformer.perform(archiveInputStream, archiveOutputStream);

        // Assert
        verifyNoMoreInteractions(archiveOutputStream);
        verifyNoMoreInteractions(changeSetResults);
        Assert.assertFalse(results.hasBeenAdded(entryName));
    }

    @Test
    public void testPerform_AddNonReplaceMode_NotDeletedLater_NotAlreadyAdded() throws IOException {
        // Arrange
        String entryName = "testEntry";
        when(change.type()).thenReturn(Change.TYPE_ADD);
        when(change.isReplaceMode()).thenReturn(false);
        when(change.getEntry()).thenReturn(archiveEntry);
        when(change.getInput()).thenReturn(inputStream);
        when(archiveEntry.getName()).thenReturn(entryName);

        // Act
        ChangeSetResults results = changeSetPerformer.perform(archiveInputStream, archiveOutputStream);

        // Assert
        verify(archiveOutputStream, times(1)).putArchiveEntry(archiveEntry);
        verify(archiveOutputStream, times(1)).closeArchiveEntry();
        verify(changeSetResults, times(1)).addedFromChangeSet(entryName);
        Assert.assertTrue(results.hasBeenAdded(entryName));
    }

    private void verifyChangeSetResultsAddedFromStream(String entryName) {
        verify(changeSetResults, times(1)).addedFromStream(entryName);
        Iterator<Change> it = changes.iterator();
        while (it.hasNext()) {
            Change change = it.next();
            if (change.type() == Change.TYPE_DELETE || change.type() == Change.TYPE_DELETE_DIR) {
                verifyNoMoreInteractions(changeSetResults);
                return;
            }
        }
    }
}
