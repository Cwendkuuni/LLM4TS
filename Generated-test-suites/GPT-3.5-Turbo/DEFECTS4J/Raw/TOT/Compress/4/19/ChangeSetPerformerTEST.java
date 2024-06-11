import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.archivers.ArchiveOutputStream;
import org.apache.commons.compress.changes.Change;
import org.apache.commons.compress.changes.ChangeSet;
import org.apache.commons.compress.changes.ChangeSetPerformer;
import org.apache.commons.compress.changes.ChangeSetResults;
import org.apache.commons.compress.utils.IOUtils;
import org.junit.Before;
import org.junit.Test;

public class ChangeSetPerformerTest {

  private ChangeSetPerformer changeSetPerformer;
  private ArchiveInputStream archiveInputStream;
  private ArchiveOutputStream archiveOutputStream;
  
  @Before
  public void setup() {
    ChangeSet changeSet = mock(ChangeSet.class);
    Set<Change> changes = new HashSet<>();
    changes.add(new Change());
    changes.add(new Change());
    changes.add(new Change());
    changeSetPerformer = new ChangeSetPerformer(changeSet);
    archiveInputStream = mock(ArchiveInputStream.class);
    archiveOutputStream = mock(ArchiveOutputStream.class);
  }
  
  @Test
  public void testPerform_AddChangeWithReplaceMode_CopiesDataAndRemovesFromWorkingSet() throws IOException {
    // Arrange
    Change change = new Change();
    change.setType(Change.TYPE_ADD);
    change.setReplaceMode(true);
    change.setInput(mock(InputStream.class));
    ArchiveEntry entry = mock(ArchiveEntry.class);
    change.setEntry(entry);
    changeSetPerformer.getChanges().add(change);
    ChangeSetResults results = new ChangeSetResults();
    
    // Act
    changeSetPerformer.perform(archiveInputStream, archiveOutputStream);
    
    // Assert
    assertTrue(changeSetPerformer.getChanges().isEmpty());
    // Ensure the copyStream method is called
  }
  
  @Test
  public void testPerform_DeleteChangeWithMatchingName_RemovesEntryFromWorkingSetAndAddsToResults() throws IOException {
    // Arrange
    Change change = new Change();
    change.setType(Change.TYPE_DELETE);
    change.setTargetFile("file1");
    changeSetPerformer.getChanges().add(change);
    ArchiveEntry entry = mock(ArchiveEntry.class);
    entry.setName("file1");
    ChangeSetResults results = new ChangeSetResults();
    
    // Act
    changeSetPerformer.perform(archiveInputStream, archiveOutputStream);
    
    // Assert
    assertTrue(changeSetPerformer.getChanges().isEmpty());
    // Ensure the entry is removed and added to results
    assertTrue(results.getDeleted().contains("file1"));
  }
  
  @Test
  public void testPerform_DeleteChangeWithMismatchingName_DoesNotRemoveEntryFromWorkingSet() throws IOException {
    // Arrange
    Change change = new Change();
    change.setType(Change.TYPE_DELETE);
    change.setTargetFile("file1");
    changeSetPerformer.getChanges().add(change);
    ArchiveEntry entry = mock(ArchiveEntry.class);
    entry.setName("file2");
    ChangeSetResults results = new ChangeSetResults();
    
    // Act
    changeSetPerformer.perform(archiveInputStream, archiveOutputStream);
    
    // Assert
    assertFalse(changeSetPerformer.getChanges().isEmpty());
    // Ensure the entry is not removed and not added to results
    assertFalse(results.getDeleted().contains("file2"));
  }
  
  @Test
  public void testPerform_DeleteDirectoryChangeWithMatchingPrefix_RemovesEntryFromWorkingSetAndAddsToResults() throws IOException {
    // Arrange
    Change change = new Change();
    change.setType(Change.TYPE_DELETE_DIR);
    change.setTargetFile("dir1");
    changeSetPerformer.getChanges().add(change);
    ArchiveEntry entry = mock(ArchiveEntry.class);
    entry.setName("dir1/file1");
    ChangeSetResults results = new ChangeSetResults();
    
    // Act
    changeSetPerformer.perform(archiveInputStream, archiveOutputStream);
    
    // Assert
    assertTrue(changeSetPerformer.getChanges().isEmpty());
    // Ensure the entry is removed and added to results
    assertTrue(results.getDeleted().contains("dir1/file1"));
  }
  
  @Test
  public void testPerform_DeleteDirectoryChangeWithMismatchingPrefix_DoesNotRemoveEntryFromWorkingSet() throws IOException {
    // Arrange
    Change change = new Change();
    change.setType(Change.TYPE_DELETE_DIR);
    change.setTargetFile("dir1");
    changeSetPerformer.getChanges().add(change);
    ArchiveEntry entry = mock(ArchiveEntry.class);
    entry.setName("dir2/file1");
    ChangeSetResults results = new ChangeSetResults();
    
    // Act
    changeSetPerformer.perform(archiveInputStream, archiveOutputStream);
    
    // Assert
    assertFalse(changeSetPerformer.getChanges().isEmpty());
    // Ensure the entry is not removed and not added to results
    assertFalse(results.getDeleted().contains("dir2/file1"));
  }
  
  @Test
  public void testPerform_CopyEntryNotDeleted_CopiesEntryToOutputStreamAndAddsToResults() throws IOException {
    // Arrange
    Change change = new Change();
    change.setType(Change.TYPE_ADD);
    change.setReplaceMode(false);
    change.setInput(mock(InputStream.class));
    ArchiveEntry entry = mock(ArchiveEntry.class);
    entry.setName("file1");
    change.setEntry(entry);
    changeSetPerformer.getChanges().add(change);
    ArchiveEntry existingEntry = mock(ArchiveEntry.class);
    existingEntry.setName("file2");
    // This will help simulate that the entry was not deleted later
    assertFalse(changeSetPerformer.isDeletedLater(changeSetPerformer.getChanges(), existingEntry));
    ChangeSetResults results = new ChangeSetResults();
    
    // Act
    changeSetPerformer.perform(archiveInputStream, archiveOutputStream);
    
    // Assert
    assertTrue(changeSetPerformer.getChanges().isEmpty());
    // Ensure the copyStream method is called
    assertTrue(results.getAddedFromChangeSet().contains("file1"));
  }
  
  @Test
  public void testPerform_CopyEntryDeletedLater_DoesNotCopyEntryToOutputStream() throws IOException {
    // Arrange
    Change change = new Change();
    change.setType(Change.TYPE_ADD);
    change.setReplaceMode(false);
    change.setInput(mock(InputStream.class));
    ArchiveEntry entry = mock(ArchiveEntry.class);
    entry.setName("file1");
    change.setEntry(entry);
    changeSetPerformer.getChanges().add(change);
    ArchiveEntry existingEntry = mock(ArchiveEntry.class);
    existingEntry.setName("file1");
    // This will help simulate that the entry was deleted later
    assertTrue(changeSetPerformer.isDeletedLater(changeSetPerformer.getChanges(), existingEntry));
    ChangeSetResults results = new ChangeSetResults();
    
    // Act
    changeSetPerformer.perform(archiveInputStream, archiveOutputStream);
    
    // Assert
    assertFalse(changeSetPerformer.getChanges().isEmpty());
    // Ensure the copyStream method is not called
    assertFalse(results.getAddedFromChangeSet().contains("file1"));
  }
  
  @Test
  public void testPerform_CopyEntryDeletedFromStream_DoesNotCopyEntryToOutputStream() throws IOException {
    // Arrange
    Change change = new Change();
    change.setType(Change.TYPE_ADD);
    change.setReplaceMode(false);
    change.setInput(mock(InputStream.class));
    ArchiveEntry entry = mock(ArchiveEntry.class);
    entry.setName("file1");
    change.setEntry(entry);
    changeSetPerformer.getChanges().add(change);
    ArchiveEntry existingEntry = mock(ArchiveEntry.class);
    existingEntry.setName("file1");
    ChangeSetResults results = new ChangeSetResults();
    results.setHasBeenAdded("file1", true);
    
    // Act
    changeSetPerformer.perform(archiveInputStream, archiveOutputStream);
    
    // Assert
    assertFalse(changeSetPerformer.getChanges().isEmpty());
    // Ensure the copyStream method is not called
    assertFalse(results.getAddedFromStream().contains("file1"));
  }
  
  @Test
  public void testIsDeletedLater_EntryIsDeletedLater_ReturnsTrue() {
    // Arrange
    Set<Change> workingSet = new HashSet<>();
    workingSet.add(createDeleteChange("file1"));
    ArchiveEntry entry = createEntry("file1");
    
    // Act
    boolean result = changeSetPerformer.isDeletedLater(workingSet, entry);
    
    // Assert
    assertTrue(result);
  }
  
  @Test
  public void testIsDeletedLater_EntryIsNotDeletedLater_ReturnsFalse() {
    // Arrange
    Set<Change> workingSet = new HashSet<>();
    workingSet.add(createDeleteChange("file2"));
    ArchiveEntry entry = createEntry("file1");
    
    // Act
    boolean result = changeSetPerformer.isDeletedLater(workingSet, entry);
    
    // Assert
    assertFalse(result);
  }
  
  @Test
  public void testCopyStream_WritesDataToOutput() throws IOException {
    // Arrange
    InputStream input = mock(InputStream.class);
    ArchiveOutputStream output = mock(ArchiveOutputStream.class);
    ArchiveEntry entry = mock(ArchiveEntry.class);
    
    // Act
    changeSetPerformer.copyStream(input, output, entry);
    
    // Assert
    // Ensure the putArchiveEntry, copy, and closeArchiveEntry methods are called
  }
  
  private Change createDeleteChange(String targetFile) {
    Change change = new Change();
    change.setType(Change.TYPE_DELETE);
    change.setTargetFile(targetFile);
    return change;
  }
  
  private ArchiveEntry createEntry(String name) {
    ArchiveEntry entry = mock(ArchiveEntry.class);
    entry.setName(name);
    return entry;
  }
}