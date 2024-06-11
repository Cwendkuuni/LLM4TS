import org.junit.Test;
import org.junit.Before;
import static org.junit.Assert.*;
import java.io.*;
import org.apache.commons.compress.archivers.ArchiveEntry;

public class TarArchiveOutputStreamTest {

  private OutputStream outputStream;
  private TarArchiveOutputStream tarArchiveOutputStream;
  
  @Before
  public void setup() {
    outputStream = new ByteArrayOutputStream();
    tarArchiveOutputStream = new TarArchiveOutputStream(outputStream);
  }

  @Test
  public void testPutArchiveEntry_ValidEntry_Success() throws IOException {
    // Arrange
    ArchiveEntry entry = new TarArchiveEntry("testFile.txt");
    // Act
    tarArchiveOutputStream.putArchiveEntry(entry);
    // Assert
    // No exception should be thrown
  }

  @Test(expected = RuntimeException.class)
  public void testPutArchiveEntry_InvalidFileNameTooLong_ThrowException() throws IOException {
    // Arrange
    ArchiveEntry entry = new TarArchiveEntry("ThisIsAFileNameThatIsTooLongAndShouldThrowAnException.txt");
    // Act
    tarArchiveOutputStream.putArchiveEntry(entry);
    // Assert
    // Exception should be thrown
  }

  @Test
  public void testCloseArchiveEntry_EntryClosed_Success() throws IOException {
    // Arrange
    ArchiveEntry entry = new TarArchiveEntry("testFile.txt");
    tarArchiveOutputStream.putArchiveEntry(entry);
    // Act
    tarArchiveOutputStream.closeArchiveEntry();
    // Assert
    // No exception should be thrown
  }

  @Test(expected = IOException.class)
  public void testCloseArchiveEntry_EntryClosedBeforeBytesWritten_ThrowException() throws IOException {
    // Arrange
    ArchiveEntry entry = new TarArchiveEntry("testFile.txt");
    tarArchiveOutputStream.putArchiveEntry(entry);
    // Act
    tarArchiveOutputStream.closeArchiveEntry();
    tarArchiveOutputStream.write(new byte[10]);
    // Assert
    // Exception should be thrown
  }

  @Test
  public void testWrite_BytesWritten_Success() throws IOException {
    // Arrange
    ArchiveEntry entry = new TarArchiveEntry("testFile.txt");
    tarArchiveOutputStream.putArchiveEntry(entry);
    byte[] bytes = "Hello, World!".getBytes();
    // Act
    tarArchiveOutputStream.write(bytes);
    // Assert
    // No exception should be thrown
  }

  @Test(expected = IOException.class)
  public void testWrite_BytesWrittenExceedsEntrySize_ThrowException() throws IOException {
    // Arrange
    ArchiveEntry entry = new TarArchiveEntry("testFile.txt");
    tarArchiveOutputStream.putArchiveEntry(entry);
    byte[] bytes = new byte[1000];
    // Act
    tarArchiveOutputStream.write(bytes);
    // Assert
    // Exception should be thrown
  }

  @Test
  public void testFinish_ArchiveFinished_Success() throws IOException {
    // Arrange
    ArchiveEntry entry = new TarArchiveEntry("testFile.txt");
    tarArchiveOutputStream.putArchiveEntry(entry);
    tarArchiveOutputStream.write("Hello, World!".getBytes());
    tarArchiveOutputStream.closeArchiveEntry();
    // Act
    tarArchiveOutputStream.finish();
    // Assert
    // No exception should be thrown
  }

  @Test
  public void testClose_ArchiveClosed_Success() throws IOException {
    // Arrange
    ArchiveEntry entry = new TarArchiveEntry("testFile.txt");
    tarArchiveOutputStream.putArchiveEntry(entry);
    tarArchiveOutputStream.write("Hello, World!".getBytes());
    tarArchiveOutputStream.closeArchiveEntry();
    // Act
    tarArchiveOutputStream.close();
    // Assert
    // No exception should be thrown
  }

  @Test
  public void testSetLongFileMode_ValidMode_Success() {
    // Arrange
    int mode = TarArchiveOutputStream.LONGFILE_TRUNCATE;
    // Act
    tarArchiveOutputStream.setLongFileMode(mode);
    // Assert
    // No exception should be thrown
  }

}