import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarConstants;
import org.apache.commons.compress.archivers.tar.TarBuffer;
import org.apache.commons.compress.archivers.tar.TarArchiveOutputStream;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * JUnit test file for TarArchiveOutputStream class.
 */
public class TarArchiveOutputStreamTest {

    @Test
    public void testTarArchiveOutputStream() throws IOException {
        // Test creating a new TarArchiveOutputStream
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(outputStream, TarBuffer.DEFAULT_BLKSIZE, TarBuffer.DEFAULT_RCDSIZE);

        // Test setting longFileMode
        tarArchiveOutputStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_TRUNCATE);

        // Test writing a file entry
        File testFile = new File("testFile.txt");
        String testFileName = "testFileName.txt";
        TarArchiveEntry testArchiveEntry = tarArchiveOutputStream.createArchiveEntry(testFile, testFileName);
        tarArchiveOutputStream.putArchiveEntry(testArchiveEntry);
        byte[] fileContent = "Test content".getBytes();
        tarArchiveOutputStream.write(fileContent, 0, fileContent.length);
        tarArchiveOutputStream.closeArchiveEntry();

        // Test writing an EOF record
        tarArchiveOutputStream.finish();

        // Test reading the written archive
        TarArchiveInputStream tarArchiveInputStream = new TarArchiveInputStream(new ByteArrayInputStream(outputStream.toByteArray()));
        TarArchiveEntry readEntry = tarArchiveInputStream.getNextEntry();
        Assert.assertEquals(testFileName, readEntry.getName());
        byte[] readContent = new byte[fileContent.length];
        int readLength = tarArchiveInputStream.read(readContent, 0, readContent.length);
        Assert.assertEquals(fileContent.length, readLength);
        Assert.assertArrayEquals(fileContent, readContent);

        // Test reading the EOF record
        readEntry = tarArchiveInputStream.getNextEntry();
        Assert.assertNull(readEntry);

        // Close the input stream
        tarArchiveInputStream.close();
    }

    @Test(expected = IOException.class)
    public void testLongFileNameError() throws IOException {
        // Test long file name error
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(outputStream, TarBuffer.DEFAULT_BLKSIZE, TarBuffer.DEFAULT_RCDSIZE);
        TarArchiveEntry testArchiveEntry = new TarArchiveEntry("test/test/test/test/test/test/test/test/test/test/test/test/test/test/test/test/test/test/test/test/test/test/test/test/test/test/test/test/testFileName.txt");
        tarArchiveOutputStream.putArchiveEntry(testArchiveEntry);
        tarArchiveOutputStream.finish();
    }

    @Test(expected = IOException.class)
    public void testLongFileNameTruncate() throws IOException {
        // Test long file name truncation
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(outputStream, TarBuffer.DEFAULT_BLKSIZE, TarBuffer.DEFAULT_RCDSIZE);
        TarArchiveEntry testArchiveEntry = new TarArchiveEntry("test/test/test/test/test/test/test/test/test/test/test/test/test/test/test/test/test/test/test/test/test/test/test/testFileName.txt");
        tarArchiveOutputStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_TRUNCATE);
        tarArchiveOutputStream.putArchiveEntry(testArchiveEntry);
        tarArchiveOutputStream.finish();
    }

    @Test(expected = IOException.class)
    public void testLongFileNameGNU() throws IOException {
        // Test long file name using GNU extensions
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(outputStream, TarBuffer.DEFAULT_BLKSIZE, TarBuffer.DEFAULT_RCDSIZE);
        TarArchiveEntry testArchiveEntry = new TarArchiveEntry("test/test/test/test/test/test/test/test/test/test/test/test/test/test/test/test/test/test/test/test/test/test/test/testFileName.txt", TarConstants.GNU_LONGLINK, TarConstants.LF_GNUTYPE_LONGNAME);
        tarArchiveOutputStream.putArchiveEntry(testArchiveEntry);
        tarArchiveOutputStream.write("test/test/test/test/test/test/test/test/test/test/test/test/test/test/test/test/test/test/test/test/test/test/test/testFileName.txt".getBytes());
        tarArchiveOutputStream.write((byte) 0); // NUL terminator
        tarArchiveOutputStream.closeArchiveEntry();
        tarArchiveOutputStream.finish();
    }
}