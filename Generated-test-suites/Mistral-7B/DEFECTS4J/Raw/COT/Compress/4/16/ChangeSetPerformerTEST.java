package org.apache.commons.compress.changes;

import static org.junit.Assert.*;

import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.archivers.ArchiveOutputStream;
import org.apache.commons.compress.archivers.zip.ZipConstants;
import org.apache.commons.compress.archivers.zip.ZipEntry;
import org.apache.commons.compress.archivers.zip.ZipFile;
import org.apache.commons.compress.utils.IOUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class ChangeSetPerformerTest {

 private ChangeSet changeSet;
 private ChangeSetPerformer performer;
 private ArchiveInputStream input;
 private ArchiveOutputStream output;
 private ZipFile zipFile;
 private String testFile;

 @Before
 public void setUp() throws Exception {
 changeSet = new ChangeSet();
 performer = new ChangeSetPerformer(changeSet);
 testFile = "testFile.txt";

 // Create test file for testing
 ZipFile testZip = new ZipFile("test.zip");
 output = new ArchiveOutputStream("test.zip", ZipConstants.UTF_8);
 ArchiveEntry testEntry = new ZipEntry(testFile);
 testEntry.setSize(10);
 output.putNextEntry(testEntry);
 String testContent = "Test Content";
 IOUtils.write(testContent.getBytes(), output);
 output.closeArchiveEntry();
 output.finish();
 testZip.close();
 input = new ArchiveInputStream(new FileInputStream("test.zip"));
 }

 @After
 public void tearDown() throws Exception {
 IOUtils.closeQuietly(input);
 IOUtils.closeQuietly(output);
 if (changeSet != null) {
 changeSet.clear();
 }
 if (zipFile != null) {
 zipFile.close();
 }
 File testFile = new File("test.zip");
 testFile.delete();
 }

 /**
 * Test constructor
 */
 @Test
 public void testChangeSetPerformerConstruction() {
 performer = new ChangeSetPerformer(changeSet);
 assertNotNull(performer);
 }

 /**
 * Test perform method with empty change set
 */
 @Test
 public void testPerformWithEmptyChangeSet() throws Exception {
 ChangeSetResults result = performer.perform(input, output);
 assertNotNull(result);
 assertEquals(0, result.getAddedFromChangeSet().size());
 assertEquals(0, result.getDeleted().size());
 }

 /**
 * Test perform method with change set having only add change
 */
 @Test
 public void testPerformWithSingleAddChange() throws Exception {
 Change addChange = new Change(Change.TYPE_ADD, testFile, null, null, null);
 changeSet.addChange(addChange);
 ChangeSetResults result = performer.perform(input, output);
 assertNotNull(result);
 assertEquals(1, result.getAddedFromChangeSet().size());
 assertEquals(testFile, result.getAddedFromChangeSet().iterator().next());
 }

 /**
 * Test perform method with change set having only delete change
 */
 @Test
 public void testPerformWithSingleDeleteChange() throws Exception {
 Change deleteChange = new Change(Change.TYPE_DELETE, testFile, null, null, null);
 changeSet.addChange(deleteChange);
 ChangeSetResults result = performer.perform(input, output);
 assertNotNull(result);
 assertEquals(0, result.getAddedFromChangeSet().size());
 assertEquals(1, result.getDeleted().size());
 assertEquals(testFile, result.getDeleted().iterator().next());
 }

 /**
 * Test perform method with change set having add and delete change
 */
 @Test
 public void testPerformWithAddAndDeleteChange() throws Exception {
 Change addChange = new Change(Change.TYPE_ADD, testFile, null, null, null);
 Change deleteChange = new Change(Change.TYPE_DELETE, testFile, null, null, null);
 changeSet.addChange(addChange);
 changeSet.addChange(deleteChange);
 ChangeSetResults result = performer.perform(input, output);
 assertNotNull(result);
 assertEquals(0, result.getAddedFromChangeSet().size());
 assertEquals(1, result.getDeleted().size());
 assertEquals(testFile, result.getDeleted().iterator().next());
 }

 /**
 * Test perform method with change set having add change with replace mode
 */
 @Test
 public void testPerformWithAddReplaceChange() throws Exception {
 Change addChange = new Change(Change.TYPE_ADD, testFile, "testFileReplace.txt", null, true);
 changeSet.addChange(addChange);
 ChangeSetResults result = performer.perform(input, output);
 assertNotNull(result);
 assertEquals(1, result.getAddedFromChangeSet().size());
 assertEquals(testFile, result.getAddedFromChangeSet().iterator().next());
 }

 /**
 * Test perform method with change set having add change without replace mode
 */
 @Test
 public void testPerformWithAddChangeWithoutReplaceMode() throws Exception {
 Change addChange = new Change(Change.TYPE_ADD, testFile, null, null, null);
 Change addChange2 = new Change(Change.TYPE_ADD, "testFile2.txt", null, null, null);
 changeSet.addChange(addChange);
 changeSet.addChange(addChange2);
 ChangeSetResults result = performer.perform(input, output);
 assertNotNull(result);
 assertEquals(2, result.getAddedFromChangeSet().size());
 assertEquals(testFile, result.getAddedFromChangeSet().iterator().next());
 assertEquals("testFile2.txt", result.getAddedFromStream().iterator().next());
 }

 /**
 * Test perform method with change set having delete change later in the set
 */
 @Test
 public void testPerformWithDeleteChangeLater() throws Exception {
 Change addChange = new Change(Change.TYPE_ADD, testFile, null, null, null);
 Change deleteChange = new Change(Change.TYPE_DELETE, testFile, null, null, null);
 Change deleteChangeLater = new Change(Change.TYPE_DELETE, testFile, null, null, null);
 changeSet.addChange(addChange);
 changeSet.addChange(deleteChange);
 changeSet.addChange(deleteChangeLater);
 ChangeSetResults result = performer.perform(input, output);
 assertNotNull(result);
 assertEquals(0, result.getAddedFromChangeSet().size());
 assertEquals(1, result.getDeleted().size());
 assertEquals(testFile, result.getDeleted().iterator().next());
 }

 /**
 * Test perform method with change set having add and delete changes for directory
 */
 @Test
 public void testPerformWithAddAndDeleteDirectoryChange() throws Exception {
 String testDirectory = "testDirectory";
 Change addChange = new Change(Change.TYPE_ADD, testDirectory, null, null, null);
 Change deleteChange = new Change(Change.TYPE_DELETE_DIR, testDirectory, null, null, null);
 changeSet.addChange(addChange);
 changeSet.addChange(deleteChange);
 ChangeSetResults result = performer.perform(input, output);
 assertNotNull(result);
 assertEquals(0, result.getAddedFromChangeSet().size());
 assertEquals(0, result.getDeleted().size());
 }

 /**
 * Test perform method with change set having add and delete changes for file inside directory
 */
 @Test
 public void testPerformWithAddAndDeleteChangeForFileInDirectory() throws Exception {
 String testDirectory = "testDirectory";
 String testFile = "testFile.txt";
 Change addChange = new Change(Change.TYPE_ADD, testDirectory, testFile, null, null);
 Change deleteChange = new Change(Change.TYPE_DELETE, testDirectory + "/" + testFile, null, null, null);
 changeSet.addChange(addChange);
 changeSet.addChange(deleteChange);
 ChangeSetResults result = performer.perform(input, output);
 assertNotNull(result);
 assertEquals(0, result.getAddedFromChangeSet().size());
 assertEquals(1, result.getDeleted().size());
 assertEquals(testDirectory + "/" + testFile, result.getDeleted().iterator().next());
 }
}