import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import java.io.*;

public class TarArchiveOutputStreamTest {
    private static final String TEST_OUTPUT_DIR = "test_output";
    private static final String TEST_FILE_1 = "file1.txt";
    private static final String TEST_FILE_2 = "file2.txt";
    private static final String TEST_FILE_3 = "file3.txt";

    private TarArchiveOutputStream tarArchiveOutputStream;

    @Before
    public void setup() {
        try {
            OutputStream outputStream = new FileOutputStream(TEST_OUTPUT_DIR + File.separator + "archive.tar");
            tarArchiveOutputStream = new TarArchiveOutputStream(outputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void putArchiveEntry_ValidEntry_Success() {
        // Arrange
        File file = new File(TEST_OUTPUT_DIR + File.separator + TEST_FILE_1);
        byte[] fileContent = "This is file 1 content.".getBytes();
        FileOutputStream fileOutputStream = null;
        TarArchiveEntry tarArchiveEntry = null;

        try {
            fileOutputStream = new FileOutputStream(file);
            fileOutputStream.write(fileContent);
            fileOutputStream.close();
            tarArchiveEntry = new TarArchiveEntry(file, TEST_FILE_1);
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Act
        try {
            tarArchiveOutputStream.putArchiveEntry(tarArchiveEntry);
            tarArchiveOutputStream.closeArchiveEntry();
            tarArchiveOutputStream.finish();
            tarArchiveOutputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Assert
        File archiveFile = new File(TEST_OUTPUT_DIR + File.separator + "archive.tar");
        Assert.assertTrue(archiveFile.exists());
        Assert.assertTrue(archiveFile.isFile());
    }

    @Test
    public void closeArchiveEntry_IncompleteEntry_ThrowsIOException() {
        // Arrange
        File file = new File(TEST_OUTPUT_DIR + File.separator + TEST_FILE_2);
        byte[] fileContent = "This is file 2 content.".getBytes();
        FileOutputStream fileOutputStream = null;
        TarArchiveEntry tarArchiveEntry = null;

        try {
            fileOutputStream = new FileOutputStream(file);
            fileOutputStream.write(fileContent);
            fileOutputStream.close();
            tarArchiveEntry = new TarArchiveEntry(file, TEST_FILE_2);
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            tarArchiveOutputStream.putArchiveEntry(tarArchiveEntry);
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Act & Assert
        try {
            tarArchiveOutputStream.closeArchiveEntry();
            Assert.fail("Should throw IOException");
        } catch (IOException e) {
            Assert.assertEquals("entry '" + TEST_FILE_2 + "' closed at '"
                    + 0
                    + "' before the '" + fileContent.length
                    + "' bytes specified in the header were written", e.getMessage());
        }
    }

    @Test
    public void write_ValidData_Success() {
        // Arrange
        File file = new File(TEST_OUTPUT_DIR + File.separator + TEST_FILE_3);
        byte[] fileContent = "This is file 3 content.".getBytes();
        FileOutputStream fileOutputStream = null;
        TarArchiveEntry tarArchiveEntry = null;

        try {
            fileOutputStream = new FileOutputStream(file);
            fileOutputStream.write(fileContent);
            fileOutputStream.close();
            tarArchiveEntry = new TarArchiveEntry(file, TEST_FILE_3);
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            tarArchiveOutputStream.putArchiveEntry(tarArchiveEntry);
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Act
        try {
            tarArchiveOutputStream.write(fileContent);
            tarArchiveOutputStream.closeArchiveEntry();
            tarArchiveOutputStream.finish();
            tarArchiveOutputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Assert
        File archiveFile = new File(TEST_OUTPUT_DIR + File.separator + "archive.tar");
        Assert.assertTrue(archiveFile.exists());
        Assert.assertTrue(archiveFile.isFile());
    }

    @Test
    public void setLongFileMode_ValidMode_Success() {
        // Arrange
        int longFileMode = 1; 

        // Act
        tarArchiveOutputStream.setLongFileMode(longFileMode);

        // Assert
        Assert.assertEquals(longFileMode, tarArchiveOutputStream.getLongFileMode());
    }

    @Test
    public void getRecordSize_ValidSize_Success() {
        // Arrange
        int blockSize = 1024;
        int recordSize = 512;
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(new ByteArrayOutputStream(), blockSize, recordSize);

        // Act
        int actualRecordSize = tarArchiveOutputStream.getRecordSize();

        // Assert
        Assert.assertEquals(recordSize, actualRecordSize);
    }
}