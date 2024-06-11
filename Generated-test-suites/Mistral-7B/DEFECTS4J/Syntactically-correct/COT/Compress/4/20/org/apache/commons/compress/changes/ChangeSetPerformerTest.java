import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.archivers.ArchiveOutputStream;
import org.apache.commons.compress.utils.IOUtils;
import org.apache.commons.compress.changes.Change;
import org.apache.commons.compress.changes.ChangeSet;
import org.apache.commons.compress.changes.ChangeSetResults;
import org.apache.commons.compress.changes.ChangeSetPerformer;
import org.junit.Test;
import static org.junit.Assert.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;

public class ChangeSetPerformerTest {

 private ChangeSet createChangeSet() {
 ChangeSet changeSet = new ChangeSet();
 Change addChange = new Change("add.txt", "add.txt", Change.TYPE_ADD, true);
 Change deleteChange = new Change("delete.txt", "delete.txt", Change.TYPE_DELETE, false);
 Change addReplaceChange = new Change("replace.txt", "delete.txt", Change.TYPE_ADD, true, "replace.txt");
 ChangeSetResults result = new ChangeSetResults();
 changeSet.performChanges(result);
 changeSet.addChange(addChange);
 changeSet.addChange(deleteChange);
 changeSet.addChange(addReplaceChange);
 return changeSet;
 }

 @Test
 public void testChangeSetPerformer_constructor() {
 ChangeSet changeSet = createChangeSet();
 ChangeSetPerformer performer = new ChangeSetPerformer(changeSet);
 }

 @Test
 public void testPerform_noChanges() throws IOException {
 ArchiveInputStream in = new ArchiveInputStream(new FileInputStream(new File("test.tar")));
 ArchiveOutputStream out = new ArchiveOutputStream(new FileOutputStream(new File("test_out.tar")));
 ChangeSetPerformer performer = new ChangeSetPerformer(new ChangeSet());
 ChangeSetResults result = performer.perform(in, out);
 // No changes were made, so the result should be empty
 assertTrue(result.isEmpty());
 }

 @Test
 public void testPerform_addFile() throws IOException {
 File inputFile = new File("add.txt");
 FileOutputStream outputFile = new FileOutputStream("test_out.tar");
 ArchiveOutputStream archiveOutput = new ArchiveOutputStream(outputFile);
 ArchiveInputStream archiveInput = new ArchiveInputStream(new FileInputStream("test.tar"));
 ChangeSetPerformer performer = new ChangeSetPerformer(createChangeSet());
 ChangeSetResults result = performer.perform(archiveInput, archiveOutput);
 // Check if the added file was added to the output
 String addedFileName = "add.txt";
 assertTrue(result.hasBeenAdded(addedFileName));
 // Check if the file was added to the archive output
 ArchiveEntry entry = archiveInput.getNextEntry();
 assertNotNull(entry);
 assertEquals(addedFileName, entry.getName());
 assertFalse(result.hasBeenDeleted(addedFileName));
 }

 @Test
 public void testPerform_deleteFile() throws IOException {
 File inputFile = new File("test.tar");
 FileOutputStream outputFile = new FileOutputStream("test_out.tar");
 ArchiveOutputStream archiveOutput = new ArchiveOutputStream(outputFile);
 ArchiveInputStream archiveInput = new ArchiveInputStream(new FileInputStream("test.tar"));
 ChangeSetPerformer performer = new ChangeSetPerformer(createChangeSet());
 ChangeSetResults result = performer.perform(archiveInput, archiveOutput);
 // Check if the deleted file was deleted from the output
 String deletedFileName = "delete.txt";
 assertFalse(result.hasBeenAdded(deletedFileName));
 assertFalse(result.hasBeenAdded(deletedFileName + "/"));
 // Check if the file was deleted from the archive output
 ArchiveEntry entry = archiveInput.getNextEntry();
 assertNotNull(entry);
 assertEquals(deletedFileName, entry.getName());
 }

 @Test
 public void testPerform_replaceFile() throws IOException {
 File inputFile = new File("test.tar");
 File addInputFile = new File("add.txt");
 File replaceInputFile = new File("replace.txt");
 FileOutputStream outputFile = new FileOutputStream("test_out.tar");
 ArchiveOutputStream archiveOutput = new ArchiveOutputStream(outputFile);
 ArchiveInputStream archiveInput = new ArchiveInputStream(new FileInputStream("test.tar"));
 ChangeSetPerformer performer = new ChangeSetPerformer(createChangeSet());
 InputStream addStream = new FileInputStream(addInputFile);
 InputStream replaceStream = new FileInputStream(replaceInputFile);
 ChangeSetResults result = performer.perform(archiveInput, archiveOutput);
 // Check if the replaced file was replaced in the output
 String replacedFileName = "replace.txt";
 assertTrue(result.hasBeenAdded(replacedFileName));
 // Check if the original file was deleted from the archive output
 ArchiveEntry entry = archiveInput.getNextEntry();
 assertNotNull(entry);
 assertEquals("delete.txt", entry.getName());
 // Check if the new file was added to the archive output
 assertEquals(replacedFileName, archiveOutput.getNextArchiveEntry().getName());
 }

 @Test (expected = IOException.class)
 public void testPerform_exception_when_inputStreamIsClosed() throws IOException {
 File inputFile = new File("test.tar");
 // Close the input stream before passing it to performer
 new FileInputStream(inputFile).close();
 ArchiveOutputStream outputFile = new FileOutputStream("test_out.tar");
 ArchiveOutputStream archiveOutput = new ArchiveOutputStream(outputFile);
 ChangeSetPerformer performer = new ChangeSetPerformer(createChangeSet());
 ChangeSetResults result = performer.perform(new FileInputStream(inputFile), archiveOutput);
 }

 @Test (expected = IOException.class)
 public void testPerform_exception_when_outputStreamIsClosed() throws IOException {
 File inputFile = new File("test.tar");
 ArchiveInputStream archiveInput = new ArchiveInputStream(new FileInputStream(inputFile));
 // Close the output stream before passing it to performer
 new FileOutputStream("test_out.tar").close();
 ChangeSetPerformer performer = new ChangeSetPerformer(createChangeSet());
 ChangeSetResults result = performer.perform(archiveInput, new FileOutputStream("test_out.tar"));
 }

 @Test
 public void testIsDeletedLater() {
 Set<Change> changes = new LinkedHashSet<>();
 Change addChange = new Change("add.txt", "add.txt", Change.TYPE_ADD, true);
 Change deleteChange = new Change("delete.txt", "delete.txt", Change.TYPE_DELETE, false);
 Change addReplaceChange = new Change("replace.txt", "delete.txt", Change.TYPE_ADD, true, "replace.txt");
 changes.add(addChange);
 changes.add(deleteChange);
 changes.add(addReplaceChange);
 ChangeSetPerformer performer = new ChangeSetPerformer(new ChangeSet(changes));
 // The file add.txt is added before it is deleted, so it should not be marked as deleted
 assertFalse(performer.isDeletedLater(new ArchiveEntry("add.txt"), changes));
 // The file replace.txt is added after it is deleted, so it should be marked as deleted
 Change replaceChange = new Change("replace.txt", "delete.txt", Change.TYPE_ADD, true, "replace.txt");
 changes.remove(deleteChange);
 changes.add(replaceChange);
 assertTrue(performer.isDeletedLater(new ArchiveEntry("replace.txt"), changes));
 }

 @Test
 public void testIsDeletedLater_directory() {
 Set<Change> changes = new LinkedHashSet<>();
 Change addChange = new Change("add.txt", "add.txt", Change.TYPE_ADD, true);
 Change deleteDirectoryChange = new Change("deleteDir", "", Change.TYPE_DELETE_DIR, false);
 Change addReplaceChange = new Change("replace.txt", "delete.txt", Change.TYPE_ADD, true, "replace.txt");
 changes.add(addChange);
 changes.add(deleteDirectoryChange);
 changes.add(addReplaceChange);
 ChangeSetPerformer performer = new ChangeSetPerformer(new ChangeSet(changes));
 // The file add.txt is added before the directory deleteDir is deleted, so it should not be marked as deleted
 assertFalse(performer.isDeletedLater(new ArchiveEntry("add.txt"), changes));
 // The file replace.txt is added after the directory deleteDir is deleted, so it should be marked as deleted
 ArchiveEntry replaceEntry = new ArchiveEntry("replace.txt");
 replaceEntry.setName("/replaceDir/replace.txt");
 assertTrue(performer.isDeletedLater(replaceEntry, changes));
 }
}