import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.archivers.ArchiveOutputStream;
import org.apache.commons.compress.utils.IOUtils;
import org.apache.commons.compress.utils.test.TestFiles;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class ChangeSetPerformerTest {

 private ChangeSet changeSet;
 private ChangeSetPerformer performer;
 private ArchiveInputStream in;
 private ArchiveOutputStream out;
 private ByteArrayOutputStream outStream;
 private ByteArrayInputStream inStream;
 private Set expectedChanges;

 @Before
 public void setUp() throws IOException {
 changeSet = new ChangeSet();
 changeSet.addChange(new Change(Change.TYPE_ADD, TestFiles.getInputStream("src/test/resources/testFile.txt"), "testFile.txt"));
 changeSet.addChange(new Change(Change.TYPE_DELETE, "testFile.txt"));
 performer = new ChangeSetPerformer(changeSet);
 in = new ArchiveInputStream(new File("src/test/resources/testArchive.tar.gz").getInputStream());
 out = new ArchiveOutputStream("test.tar.gz");
 outStream = new ByteArrayOutputStream();
 inStream = new ByteArrayInputStream(new byte[0]);
 }

 @After
 public void tearDown() throws IOException {
 IOUtils.closeQuietly(in);
 IOUtils.closeQuietly(out);
 IOUtils.closeQuietly(inStream);
 IOUtils.closeQuietly(outStream);
 }

 @Test
 public void testPerformAddReplaceMode() throws IOException {
 // Given
 ByteArrayOutputStream outTemp = new ByteArrayOutputStream();
 ArchiveOutputStream outTempStream = new ArchiveOutputStream(outTemp);
 Change change = changeSet.getChanges().iterator().next();
 performer.perform(in, outTempStream);
 // When
 InputStream input = new ByteArrayInputStream(outTemp.toByteArray());
 ChangeSetResults results = performer.perform(input, new ArchiveOutputStream("test2.tar.gz"));
 // Then
 Set actualChanges = results.getChanges();
 assertEquals(1, actualChanges.size());
 Change actualChange = (Change) actualChanges.iterator().next();
 assertEquals(Change.TYPE_ADD, actualChange.type());
 assertNull(actualChange.getTargetFile());
 assertEquals("testFile.txt", actualChange.getInput().getName());
 }

 @Test
 public void testPerformAddNotReplaceMode() throws IOException {
 // Given
 ByteArrayOutputStream outTemp = new ByteArrayOutputStream();
 ArchiveOutputStream outTempStream = new ArchiveOutputStream(outTemp);
 Change change = changeSet.getChanges().iterator().next();
 performer.perform(in, outTempStream);
 performer.perform(inStream, out);
 // When
 ChangeSetResults results = performer.perform(in, out);
 // Then
 Set actualChanges = results.getChanges();
 assertEquals(1, actualChanges.size());
 Change actualChange = (Change) actualChanges.iterator().next();
 assertEquals(Change.TYPE_ADD, actualChange.type());
 assertNull(actualChange.getTargetFile());
 assertEquals("testFile.txt", actualChange.getInput().getName());
 }

 @Test
 public void testPerformDelete() throws IOException {
 // Given
 ByteArrayOutputStream outTemp = new ByteArrayOutputStream();
 ArchiveOutputStream outTempStream = new ArchiveOutputStream(outTemp);
 performer.perform(in, outTempStream);
 // When
 ChangeSetResults results = performer.perform(in, out);
 // Then
 Set actualChanges = results.getChanges();
 assertEquals(0, actualChanges.size());
 }

 @Test(expected = IOException.class)
 public void testPerformError() throws IOException {
 // Given
 ArchiveInputStream inputWithError = new ArchiveInputStream(new ByteArrayInputStream(new byte[0]));
 // When, Then
 assertThrows(IOException.class, () -> performer.perform(inputWithError, out));
 }

 @Test
 public void testPerformNewFileAdded() throws IOException {
 // Given
 ChangeSet newChangeSet = new ChangeSet();
 newChangeSet.addChange(new Change(Change.TYPE_ADD, new ByteArrayInputStream("newFile".getBytes()), "newFile.txt"));
 ChangeSetPerformer newPerformer = new ChangeSetPerformer(newChangeSet);
 // When
 ByteArrayOutputStream outTemp = new ByteArrayOutputStream();
 ArchiveOutputStream outTempStream = new ArchiveOutputStream(outTemp);
 newPerformer.perform(in, outTempStream);
 // Then
 InputStream input = new ByteArrayInputStream(outTemp.toByteArray());
 ChangeSetResults results = newPerformer.perform(input, new ArchiveOutputStream("test2.tar.gz"));
 Set actualChanges = results.getChanges();
 assertEquals(1, actualChanges.size());
 Change actualChange = (Change) actualChanges.iterator().next();
 assertEquals(Change.TYPE_ADD, actualChange.type());
 assertEquals("newFile.txt", actualChange.getInput().getName());
 }

 @Test
 public void testPerformFileDeletedLater() throws IOException {
 // Given
 Set expectedChanges = new HashSet(Arrays.asList(new Change(Change.TYPE_ADD, TestFiles.getInputStream("src/test/resources/testFile.txt"), "testFile.txt"), new Change(Change.TYPE_DELETE, "testFile.txt")));
 ChangeSet changeSet = new ChangeSet(expectedChanges);
 ChangeSetPerformer performer = new ChangeSetPerformer(changeSet);
 // When
 ByteArrayOutputStream outTemp = new ByteArrayOutputStream();
 ArchiveOutputStream outTempStream = new ArchiveOutputStream(outTemp);
 performer.perform(in, outTempStream);
 // Then
 Set actualChanges = new HashSet(performer.getChanges());
 assertEquals(expectedChanges, actualChanges);
 }
}