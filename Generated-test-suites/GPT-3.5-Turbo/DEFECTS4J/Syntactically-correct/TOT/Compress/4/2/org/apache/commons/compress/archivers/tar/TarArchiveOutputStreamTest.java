import static org.junit.Assert.*;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;

import org.apache.commons.compress.archivers.ArchiveEntry;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class TarArchiveOutputStreamTest {
  
  private TarArchiveOutputStream tarOutputStream;
  private ByteArrayOutputStream output;
  
  @Before
  public void setUp() {
    output = new ByteArrayOutputStream();
    tarOutputStream = new TarArchiveOutputStream(output);
  }
  
  @After
  public void tearDown() throws IOException {
    tarOutputStream.close();
    output.close();
  }
  
  @Test
  public void testPutArchiveEntry_ValidEntry() throws IOException {
    // Arrange
    File file = new File("testFile.txt");
    String entryName = "testFile.txt";
    long expectedSize = file.length();
    boolean isDirectory = false;
    
    // Act
    tarOutputStream.putArchiveEntry(new TarArchiveEntry(file, entryName));
    tarOutputStream.closeArchiveEntry();
    
    // Assert
    assertTrue(tarOutputStream.haveUnclosedEntry);
    assertEquals(entryName, tarOutputStream.currName);
    assertEquals(expectedSize, tarOutputStream.currSize);
    assertEquals(file.isDirectory(), isDirectory);
  }
  
  @Test(expected = RuntimeException.class)
  public void testPutArchiveEntry_LongFileNameWithTruncateMode() throws IOException {
    // Arrange
    File file = new File("longFileNameThatExceedsMaxLength" + ".txt");
    String entryName = file.getName();
    int longFileMode = TarArchiveOutputStream.LONGFILE_TRUNCATE;
    
    // Act
    tarOutputStream.setLongFileMode(longFileMode);
    tarOutputStream.putArchiveEntry(new TarArchiveEntry(file, entryName));
  }
  
  @Test
  public void testCloseArchiveEntry_EntryClosed() throws IOException {
    // Arrange
    String entryName = "testFile.txt";
    tarOutputStream.putArchiveEntry(new TarArchiveEntry(new File(entryName), entryName));
    
    // Act
    tarOutputStream.closeArchiveEntry();
    
    // Assert
    assertFalse(tarOutputStream.haveUnclosedEntry);
  }
  
  @Test(expected = IOException.class)
  public void testCloseArchiveEntry_EntryNotClosed() throws IOException {
    // Arrange
    tarOutputStream.putArchiveEntry(new TarArchiveEntry(new File("testFile.txt"), "testFile.txt"));
    
    // Act
    tarOutputStream.close();
  }
  
  @Test
  public void testWrite_BytesWrittenToOutput() throws IOException {
    // Arrange
    String content = "This is the content of the file";
    tarOutputStream.putArchiveEntry(new TarArchiveEntry(new File("testFile.txt"), "testFile.txt"));
    
    // Act
    tarOutputStream.write(content.getBytes());
    tarOutputStream.closeArchiveEntry();
    tarOutputStream.close();
    String outputContent = output.toString().substring(0, output.size() - 512 - 512);
    
    // Assert
    assertEquals(content, outputContent);
  }
  
  @Test(expected = IOException.class)
  public void testWrite_BytesExceedingSizeLimit() throws IOException {
    // Arrange
    String content = "This is the content of the file";
    String entryName = "testFile.txt";
    tarOutputStream.putArchiveEntry(new TarArchiveEntry(new File(entryName), entryName, content.length() - 5));
    
    // Act
    tarOutputStream.write(content.getBytes());
    tarOutputStream.close();
  }
  
  @Test
  public void testFinish_ArchiveFinished() throws IOException {
    // Arrange
    File file = new File("testFile.txt");
    String entryName = "testFile.txt";
    tarOutputStream.putArchiveEntry(new TarArchiveEntry(file, entryName));
    tarOutputStream.write("This is the content of the file".getBytes());
    tarOutputStream.closeArchiveEntry();
    
    // Act
    tarOutputStream.finish();
    
    // Assert
    assertEquals(2 * 512, output.size() - (output.size() / 512) * 512);
  }
  
  @Test(expected = IOException.class)
  public void testFinish_UnclosedEntryExists() throws IOException {
    // Arrange
    tarOutputStream.putArchiveEntry(new TarArchiveEntry(new File("testFile.txt"), "testFile.txt"));
    
    // Act
    tarOutputStream.finish();
  }
  
  @Test
  public void testClose_OutputStreamClosed() throws IOException {
    // Arrange
    OutputStream outputStream = tarOutputStream.out;
    
    // Act
    tarOutputStream.close();
    
    // Assert
    assertTrue(tarOutputStream.closed);
    assertTrue(outputStream.closed);
  }
  
  @Test
  public void testGetRecordSize_DefaultRecordSizeReturned() {
    // Arrange
    int expectedRecordSize = TarBuffer.DEFAULT_RCDSIZE;
    
    // Act
    int recordSize = tarOutputStream.getRecordSize();
    
    // Assert
    assertEquals(expectedRecordSize, recordSize);
  }
  
  @Test
  public void testSetLongFileMode_LongFileModeSet() {
    // Arrange
    int longFileMode = TarArchiveOutputStream.LONGFILE_GNU;
    
    // Act
    tarOutputStream.setLongFileMode(longFileMode);
    
    // Assert
    assertEquals(longFileMode, tarOutputStream.longFileMode);
  }
  
  @Test
  public void testCreateArchiveEntry_ArchiveEntryCreated() throws IOException {
    // Arrange
    File inputFile = new File("testFile.txt");
    String entryName = "testFile.txt";
    
    // Act
    ArchiveEntry archiveEntry = tarOutputStream.createArchiveEntry(inputFile, entryName);
    
    // Assert
    assertEquals(inputFile, ((TarArchiveEntry) archiveEntry).getFile());
    assertEquals(entryName, ((TarArchiveEntry) archiveEntry).getName());
  }
}