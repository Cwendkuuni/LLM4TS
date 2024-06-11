import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.archivers.ArchiveOutputStream;
import org.apache.commons.compress.utils.IOUtils;
import org.apache.commons.compress.changes.Change;
import org.apache.commons.compress.changes.ChangeSet;
import org.apache.commons.compress.changes.ChangeSetPerformer;
import org.apache.commons.compress.changes.ChangeSetResults;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.io.*;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.*;

public class ChangeSetPerformerTest {

 private ChangeSet changeSet;
 private ChangeSetPerformer performer;
 private ArchiveInputStream inputStreamMock;
 private ArchiveOutputStream outputStreamMock;

 @Before
 public void setUp() {
 changeSet = Mockito.mock(ChangeSet.class);
 performer = new ChangeSetPerformer(changeSet);
 inputStreamMock = Mockito.mock(ArchiveInputStream.class);
 outputStreamMock = Mockito.mock(ArchiveOutputStream.class);
 }

 @After
 public void tearDown() throws IOException {
 IOUtils.closeQuietly(inputStreamMock);
 IOUtils.closeQuietly(outputStreamMock);
 }

 @Test
 public void testPerform_NoChanges_ReturnsEmptyResult() throws IOException {
 ChangeSet emptyChangeSet = new ChangeSet();
 ChangeSetPerformer performer = new ChangeSetPerformer(emptyChangeSet);
 ChangeSetResults result = performer.perform(inputStreamMock, outputStreamMock);
 assertTrue(result.isEmpty());
 }

 @Test
 public void testPerform_AddFile_CopiesFileToOutputStream() throws IOException {
 Change change = new Change("add", "test.txt", null, null, null, false);
 ChangeSet changeSet = new ChangeSet();
 changeSet.addChange(change);
 ChangeSetPerformer performer = new ChangeSetPerformer(changeSet);

 byte[] inputFileContent = "test content".getBytes();
 byte[] expectedOutputFileContent = "test content".getBytes();
 InputStream inputStream = new ByteArrayInputStream(inputFileContent);
 ArchiveEntry entry = Mockito.mock(ArchiveEntry.class);
 Mockito.when(entry.getName()).thenReturn("test.txt");
 Mockito.when(inputStreamMock.getNextEntry()).thenReturn(entry);

 ArchiveOutputStream outputStream = Mockito.mock(ArchiveOutputStream.class);
 File outputFile = new File("output.tar");
 Mockito.when(outputStreamMock.getArchiveEntry()).thenReturn(entry);
 Mockito.when(outputStreamMock.getOutputStream()).thenReturn(new FileOutputStream(outputFile));

 ChangeSetResults result = performer.perform(inputStreamMock, outputStreamMock);
 verify(outputStream).putArchiveEntry(entry);
 verify(outputStream).write(expectedOutputFileContent);
 verify(outputStream).closeArchiveEntry();

 byte[] outputFileContent = new File("output.tar").length() > 0 ? new byte[new File("output.tar").length()] : new byte[0];
 byte[] resultContent = result.getContent();
 assertArrayEquals(expectedOutputFileContent, resultContent);
 }

 @Test
 public void testPerform_DeleteFile_DeletesFileFromOutputStream() throws IOException {
 Change change = new Change("delete", null, "test.txt", null, null, false);
 ChangeSet changeSet = new ChangeSet();
 changeSet.addChange(change);
 ChangeSetPerformer performer = new ChangeSetPerformer(changeSet);

 ArchiveEntry entry = Mockito.mock(ArchiveEntry.class);
 Mockito.when(entry.getName()).thenReturn("test.txt");
 Mockito.when(inputStreamMock.getNextEntry()).thenReturn(entry);

 ArchiveOutputStream outputStream = Mockito.mock(ArchiveOutputStream.class);
 File outputFile = new File("output.tar");
 Mockito.when(outputStreamMock.getArchiveEntry()).thenReturn(null);
 Mockito.when(outputStreamMock.getOutputStream()).thenReturn(new FileOutputStream(outputFile));

 ChangeSetResults result = performer.perform(inputStreamMock, outputStreamMock);
 verify(outputStream, times(0)).putArchiveEntry(entry);
 assertTrue(result.getDeletedFiles().contains("test.txt"));
 }

 @Test
 public void testPerform_AddFileWithReplaceMode_CopiesFileToOutputStreamAndDeletesExistingFile() throws IOException {
 Change change = new Change("add", "test.txt", null, null, null, true);
 ChangeSet changeSet = new ChangeSet();
 changeSet.addChange(change);
 ChangeSetPerformer performer = new ChangeSetPerformer(changeSet);

 byte[] inputFileContent = "test content".getBytes();
 byte[] expectedOutputFileContent = "test content".getBytes();
 InputStream inputStream = new ByteArrayInputStream(inputFileContent);
 ArchiveEntry entry = Mockito.mock(ArchiveEntry.class);
 Mockito.when(entry.getName()).thenReturn("test.txt");
 Mockito.when(inputStreamMock.getNextEntry()).thenReturn(entry);
 ArchiveEntry existingEntry = Mockito.mock(ArchiveEntry.class);
 Mockito.when(existingEntry.getName()).thenReturn("test.txt");
 Mockito.when(inputStreamMock.getNextEntry()).thenReturn(existingEntry);

 ArchiveOutputStream outputStream = Mockito.mock(ArchiveOutputStream.class);
 File outputFile = new File("output.tar");
 Mockito.when(outputStreamMock.getArchiveEntry()).thenReturn(entry);
 Mockito.when(outputStreamMock.getOutputStream()).thenReturn(new FileOutputStream(outputFile));

 ChangeSetResults result = performer.perform(inputStreamMock, outputStreamMock);
 verify(outputStream).putArchiveEntry(entry);
 verify(outputStream).write(expectedOutputFileContent);
 verify(outputStream).closeArchiveEntry();
 verify(outputStreamMock, times(1)).deleteEntry(existingEntry);
 byte[] outputFileContent = result.getContent();
 assertArrayEquals(expectedOutputFileContent, outputFileContent);
 }

 @Test
 public void testPerform_DeleteDirectory_DeletesDirectoryAndAllItsContent() throws IOException {
 Change change = new Change("delete", null, "test", null, null, false);
 ChangeSet changeSet = new ChangeSet();
 changeSet.addChange(change);
 ChangeSetPerformer performer = new ChangeSetPerformer(changeSet);

 ArchiveEntry directoryEntry = Mockito.mock(ArchiveEntry.class);
 Mockito.when(directoryEntry.getName()).thenReturn("test/");
 Mockito.when(inputStreamMock.getNextEntry()).thenReturn(directoryEntry);

 ArchiveEntry fileEntry1 = Mockito.mock(ArchiveEntry.class);
 Mockito.when(fileEntry1.getName()).thenReturn("test/file1.txt");
 ArchiveEntry fileEntry2 = Mockito.mock(ArchiveEntry.class);
 Mockito.when(fileEntry2.getName()).thenReturn("test/file2.txt");
 Mockito.when(inputStreamMock.getNextEntry()).thenReturn(fileEntry1);
 Mockito.when(inputStreamMock.getNextEntry()).thenReturn(fileEntry2);
 Mockito.when(inputStreamMock.getNextEntry()).thenReturn(null);

 ArchiveOutputStream outputStream = Mockito.mock(ArchiveOutputStream.class);
 Mockito.when(outputStreamMock.getArchiveEntry()).thenReturn(null);
 Mockito.when(outputStreamMock.getOutputStream()).thenReturn(new FileOutputStream("output.tar"));

 ChangeSetResults result = performer.perform(inputStreamMock, outputStreamMock);
 verify(outputStreamMock, times(1)).closeArchiveEntry();
 verify(outputStreamMock, times(1)).deleteEntry(directoryEntry);
 verify(outputStreamMock, times(1)).deleteEntry(fileEntry1);
 verify(outputStreamMock, times(1)).deleteEntry(fileEntry2);
 }

 @Test
 public void testPerform_AddFilesWithReplaceMode_CopiesFilesToOutputStreamAndDeletesExistingFiles() throws IOException {
 Change change1 = new Change("add", "test1.txt", null, null, null, true);
 Change change2 = new Change("add", "test2.txt", null, null, null, true);
 ChangeSet changeSet = new ChangeSet();
 changeSet.addAll(Arrays.asList(change1, change2));
 ChangeSetPerformer performer = new ChangeSetPerformer(changeSet);

 byte[] inputFileContent1 = "test content 1".getBytes();
 byte[] inputFileContent2 = "test content 2".getBytes();
 InputStream inputStream = new ByteArrayInputStream(new byte[0]);
 ArchiveEntry entry1 = Mockito.mock(ArchiveEntry.class);
 Mockito.when(entry1.getName()).thenReturn("test1.txt");
 Mockito.when(inputStreamMock.getNextEntry()).thenReturn(entry1);
 InputStream inputStream1 = new ByteArrayInputStream(inputFileContent1);
 Mockito.when(change1.getInput()).thenReturn(inputStream1);

 ArchiveEntry entry2 = Mockito.mock(ArchiveEntry.class);
 Mockito.when(entry2.getName()).thenReturn("test2.txt");
 Mockito.when(inputStreamMock.getNextEntry()).thenReturn(entry2);
 InputStream inputStream2 = new ByteArrayInputStream(inputFileContent2);
 Mockito.when(change2.getInput()).thenReturn(inputStream2);

 ArchiveOutputStream outputStream = Mockito.mock(ArchiveOutputStream.class);
 File outputFile = new File("output.tar");
 Mockito.when(outputStreamMock.getArchiveEntry()).thenReturn(entry1);
 Mockito.when(outputStreamMock.getOutputStream()).thenReturn(new FileOutputStream(outputFile));

 ChangeSetResults result = performer.perform(inputStreamMock, outputStreamMock);
 verify(outputStream).putArchiveEntry(entry1);
 verify(outputStream).write(inputFileContent1);
 verify(outputStream).closeArchiveEntry();
 verify(outputStreamMock, times(1)).deleteEntry(entry1);

 byte[] outputFileContent = result.getContent();
 assertEquals(inputFileContent1.length + inputFileContent2.length, outputFileContent.length);
 }

 @Test
 public void testPerform_AddFilesWithReplaceModeAndDifferentEntries_CopiesFilesToOutputStreamAndDeletesExistingFiles() throws IOException {
 Change change1 = new Change("add", "test1.txt", null, null, null, true);
 Change change2 = new Change("add", "test2.txt", null, null, null, true);
 ChangeSet changeSet = new ChangeSet();
 changeSet.addAll(Arrays.asList(change1, change2));
 ChangeSetPerformer performer = new ChangeSetPerformer(changeSet);

 byte[] inputFileContent1 = "test content 1".getBytes();
 byte[] inputFileContent2 = "test content 2".getBytes();
 InputStream inputStream = new ByteArrayInputStream(new byte[0]);
 ArchiveEntry entry1 = Mockito.mock(ArchiveEntry.class);
 Mockito.when(entry1.getName()).thenReturn("test1.txt");
 Mockito.when(inputStreamMock.getNextEntry()).thenReturn(entry1);
 InputStream inputStream1 = new ByteArrayInputStream(inputFileContent1);
 Mockito.when(change1.getInput()).thenReturn(inputStream1);

 ArchiveEntry entry2 = Mockito.mock(ArchiveEntry.class);
 Mockito.when(entry2.getName()).thenReturn("test2.txt");
 Mockito.when(inputStreamMock.getNextEntry()).thenReturn(entry2);
 InputStream inputStream2 = new ByteArrayInputStream(inputFileContent2);
 Mockito.when(change2.getInput()).thenReturn(inputStream2);

 ArchiveEntry existingEntry1 = Mockito.mock(ArchiveEntry.class);
 Mockito.when(existingEntry1.getName()).thenReturn("test1.txt");
 Mockito.when(inputStreamMock.getNextEntry()).thenReturn(existingEntry1);

 ArchiveOutputStream outputStream = Mockito.mock(ArchiveOutputStream.class);
 File outputFile = new File("output.tar");
 Mockito.when(outputStreamMock.getArchiveEntry()).thenReturn(entry1);
 Mockito.when(outputStreamMock.getOutputStream()).thenReturn(new FileOutputStream(outputFile));

 ChangeSetResults result = performer.perform(inputStreamMock, outputStreamMock);
 verify(outputStream).putArchiveEntry(entry1);
 verify(outputStream).write(inputFileContent1);
 verify(outputStream).closeArchiveEntry();
 verify(outputStreamMock, times(1)).deleteEntry(existingEntry1);

 byte[] outputFileContent = result.getContent();
 assertEquals(inputFileContent1.length + inputFileContent2.length, outputFileContent.length);
 }
}