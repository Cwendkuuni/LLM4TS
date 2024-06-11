import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarBuffer;
import org.apache.commons.compress.archivers.tar.TarConstants;
import org.apache.commons.compress.archivers.tar.TarArchiveOutputStream;
import org.apache.commons.compress.utils.IOUtils;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;

/**
 * Test class for TarArchiveOutputStream
 */
public class TarArchiveOutputStreamTest {

    private OutputStream outputStream;
    private TarArchiveOutputStream tarArchiveOutputStream;
    private byte[] testData = "Test data".getBytes();

    @Before
    public void setup() throws IOException {
        outputStream = new ByteArrayOutputStream(1024);
        tarArchiveOutputStream = new TarArchiveOutputStream(outputStream, TarBuffer.DEFAULT_BLKSIZE, TarBuffer.DEFAULT_RCDSIZE);
        tarArchiveOutputStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_GNU);
    }

    @After
    public void tearDown() throws IOException {
        tarArchiveOutputStream.close();
        outputStream.close();
    }

    @Test
    public void testPutArchiveEntry() throws IOException {
        TarArchiveEntry archiveEntry = new TarArchiveEntry("test.txt", "Test file");
        tarArchiveOutputStream.putArchiveEntry(archiveEntry);
        tarArchiveOutputStream.write(testData, 0, testData.length);
        tarArchiveOutputStream.closeArchiveEntry();

        byte[] expectedHeader = new byte[TarConstants.REC_MAGIC_SIZE + TarConstants.NAMELEN + 12];
        System.arraycopy(TarConstants.STANDARD_MAGIC, 0, expectedHeader, 0, TarConstants.REC_MAGIC_SIZE);
        archiveEntry.writeHeader(expectedHeader);

        byte[] expectedData = new byte[testData.length];
        System.arraycopy(testData, 0, expectedData, 0, testData.length);

        byte[] expectedEOF = new byte[TarConstants.REC_MAGIC_SIZE];
        System.arraycopy(TarConstants.EOF_MAGIC, 0, expectedEOF, 0, TarConstants.REC_MAGIC_SIZE);

        byte[] actualData = ((ByteArrayOutputStream) outputStream).toByteArray();
        Assert.assertArrayEquals("Test data not written correctly", expectedData,
                new ByteArrayInputStream(actualData).readAllBytes());

        byte[] actualHeader = new byte[expectedHeader.length];
        System.arraycopy(actualData, 0, actualHeader, 0, expectedHeader.length);
        Assert.assertArrayEquals("Header not written correctly", expectedHeader, actualHeader);

        byte[] actualEOF = new byte[expectedEOF.length];
        System.arraycopy(actualData, actualData.length - TarConstants.REC_MAGIC_SIZE, actualEOF, 0, expectedEOF.length);
        Assert.assertArrayEquals("EOF not written correctly", expectedEOF, actualEOF);
    }

    @Test(expected = IOException.class)
    public void testPutArchiveEntryLongFileName() throws IOException {
        TarArchiveEntry archiveEntry = new TarArchiveEntry("test/long/path/file.txt");
        tarArchiveOutputStream.putArchiveEntry(archiveEntry);
        tarArchiveOutputStream.write(testData, 0, testData.length);
        tarArchiveOutputStream.closeArchiveEntry();
    }

    @Test(expected = IOException.class)
    public void testCloseWithoutPutArchiveEntry() throws IOException {
        tarArchiveOutputStream.close();
    }

    @Test(expected = IOException.class)
    public void testWriteWithoutPutArchiveEntry() throws IOException {
        tarArchiveOutputStream.write(testData, 0, testData.length);
    }

    @Test
    public void testFinish() throws IOException {
        TarArchiveEntry archiveEntry = new TarArchiveEntry("test.txt", "Test file");
        tarArchiveOutputStream.putArchiveEntry(archiveEntry);
        tarArchiveOutputStream.write(testData, 0, testData.length);
        tarArchiveOutputStream.closeArchiveEntry();
        tarArchiveOutputStream.finish();

        byte[] expectedEOF = new byte[TarConstants.REC_MAGIC_SIZE];
        System.arraycopy(TarConstants.EOF_MAGIC, 0, expectedEOF, 0, TarConstants.REC_MAGIC_SIZE);

        byte[] actualData = ((ByteArrayOutputStream) outputStream).toByteArray();
        byte[] actualEOF = new byte[actualData.length - TarConstants.REC_MAGIC_SIZE];
        System.arraycopy(actualData, actualData.length - TarConstants.REC_MAGIC_SIZE, actualEOF, 0, expectedEOF.length);

        Assert.assertArrayEquals("EOF not written correctly", expectedEOF, actualEOF);
    }

    @Test
    public void testClose() throws IOException {
        TarArchiveEntry archiveEntry = new TarArchiveEntry("test.txt", "Test file");
        tarArchiveOutputStream.putArchiveEntry(archiveEntry);
        tarArchiveOutputStream.write(testData, 0, testData.length);
        tarArchiveOutputStream.closeArchiveEntry();
        tarArchiveOutputStream.close();

        Assert.assertFalse("Stream should be closed", tarArchiveOutputStream.isOpen());
    }

    @Test
    public void testWriteLargeFile() throws IOException {
        TarArchiveEntry archiveEntry = new TarArchiveEntry("test_large.txt");
        tarArchiveOutputStream.putArchiveEntry(archiveEntry);

        byte[] data = new byte[1024 * 1024]; // 1MB
        tarArchiveOutputStream.write(data, 0, data.length);
        tarArchiveOutputStream.closeArchiveEntry();

        byte[] actualData = ((ByteArrayOutputStream) outputStream).toByteArray();
        Assert.assertEquals("Unexpected size of output", data.length,
                actualData.length);
    }
}