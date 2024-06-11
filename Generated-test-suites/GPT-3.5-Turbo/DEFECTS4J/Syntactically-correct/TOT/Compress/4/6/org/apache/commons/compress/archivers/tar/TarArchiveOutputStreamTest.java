import org.junit.Assert;
import org.junit.Test;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;

public class TarArchiveOutputStreamTest {

    @Test
    public void putArchiveEntry_ValidEntry_ShouldWriteEntryHeaderToOutputStream() throws IOException {
        // Arrange
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        TarArchiveOutputStream tarOS = new TarArchiveOutputStream(baos);
        String filename = "testfile.txt";
        long fileSize = 1024; // 1 KB
        File file = new File(filename);
        TarArchiveEntry entry = new TarArchiveEntry(file, filename);
        entry.setSize(fileSize);

        // Act
        tarOS.putArchiveEntry(entry);

        // Assert
        byte[] headerBytes = baos.toByteArray();
        String headerString = new String(headerBytes);
        Assert.assertTrue(headerString.contains(filename));
        Assert.assertTrue(headerString.contains(Long.toString(fileSize)));
    }

    @Test
    public void closeArchiveEntry_EntryNotClosedBefore_ShouldThrowIOException() throws IOException {
        // Arrange
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        TarArchiveOutputStream tarOS = new TarArchiveOutputStream(baos);
        TarArchiveEntry entry = new TarArchiveEntry("testfile.txt");
        tarOS.putArchiveEntry(entry);

        // Act & Assert
        Assert.assertThrows(IOException.class, () -> tarOS.closeArchiveEntry());
    }

    @Test
    public void setLongFileMode_ValidMode_ShouldSetLongFileMode() {
        // Arrange
        OutputStream os = new ByteArrayOutputStream();
        TarArchiveOutputStream tarOS = new TarArchiveOutputStream(os);
        int mode = TarArchiveOutputStream.LONGFILE_TRUNCATE;

        // Act
        tarOS.setLongFileMode(mode);

        // Assert
        Assert.assertEquals(mode, tarOS.getLongFileMode());
    }

    @Test
    public void finish_ArchiveContainsUnclosedEntries_ShouldThrowIOException() throws IOException {
        // Arrange
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        TarArchiveOutputStream tarOS = new TarArchiveOutputStream(baos);
        TarArchiveEntry entry = new TarArchiveEntry("testfile.txt");
        tarOS.putArchiveEntry(entry);
        tarOS.closeArchiveEntry();

        // Act & Assert
        Assert.assertThrows(IOException.class, () -> tarOS.finish());
    }

    @Test
    public void write_ValidBytes_ShouldWriteBytesToOutputStream() throws IOException {
        // Arrange
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        TarArchiveOutputStream tarOS = new TarArchiveOutputStream(baos);
        byte[] bytes = "This is a test".getBytes();

        // Act
        tarOS.write(bytes);

        // Assert
        byte[] outputBytes = baos.toByteArray();
        String outputString = new String(outputBytes);
        Assert.assertEquals("This is a test", outputString);
    }

    @Test
    public void write_BytesExceedEntrySize_ShouldThrowIOException() throws IOException {
        // Arrange
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        TarArchiveOutputStream tarOS = new TarArchiveOutputStream(baos);
        TarArchiveEntry entry = new TarArchiveEntry("testfile.txt");
        entry.setSize(10);
        tarOS.putArchiveEntry(entry);
        byte[] bytes = "This is a test".getBytes();

        // Act & Assert
        Assert.assertThrows(IOException.class, () -> tarOS.write(bytes));
    }

}