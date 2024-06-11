import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarConstants;
import org.apache.commons.compress.archivers.tar.TarArchiveOutputStream;
import org.apache.commons.compress.archivers.tar.TarBuffer;
import org.junit.Assert;
import org.junit.Test;

import java.io.*;
import java.nio.charset.StandardCharsets;

public class TarArchiveOutputStreamTest {

    @Test
    public void testConstructor() throws IOException {
        OutputStream os = new ByteArrayOutputStream();
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(os);
        Assert.assertNotNull(tarArchiveOutputStream);
    }

    @Test
    public void testSetLongFileMode() throws IOException {
        OutputStream os = new ByteArrayOutputStream();
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(os);

        tarArchiveOutputStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_GNU);
        Assert.assertEquals(TarArchiveOutputStream.LONGFILE_GNU, tarArchiveOutputStream.getLongFileMode());
    }

    @Test(expected = RuntimeException.class)
    public void testPutArchiveEntryWithLongFileName() throws IOException {
        OutputStream os = new ByteArrayOutputStream();
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(os);
        String longFileName = "a" + new String(new char[TarConstants.NAMELEN + 1]).replace('\0', 'a');
        TarArchiveEntry entry = new TarArchiveEntry(new File("non-existing-file")) {
            @Override
            public String getName() {
                return longFileName;
            }
        };
        tarArchiveOutputStream.putArchiveEntry(entry);
    }

    @Test
    public void testPutArchiveEntryAndCloseArchiveEntry() throws IOException {
        OutputStream os = new ByteArrayOutputStream();
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(os);
        String fileName = "testFile.txt";
        TarArchiveEntry entry = new TarArchiveEntry(new File(fileName)) {
            @Override
            public String getName() {
                return fileName;
            }
        };

        byte[] testData = "Test data".getBytes(StandardCharsets.UTF_8);
        tarArchiveOutputStream.putArchiveEntry(entry);
        tarArchiveOutputStream.write(testData, 0, testData.length);
        tarArchiveOutputStream.closeArchiveEntry();

        byte[] expectedData = new byte[testData.length + TarConstants.STANDARD_HEADER_SIZE];
        System.arraycopy(entry.getHeader(), 0, expectedData, 0, TarConstants.STANDARD_HEADER_SIZE);
        System.arraycopy(testData, 0, expectedData, TarConstants.STANDARD_HEADER_SIZE, testData.length);

        byte[] outputData = ((ByteArrayOutputStream) os).toByteArray();
        Assert.assertArrayEquals(expectedData, outputData);
    }

    @Test
    public void testWriteEOFRecord() throws IOException {
        OutputStream os = new ByteArrayOutputStream();
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(os);
        tarArchiveOutputStream.finish();

        byte[] expectedData = new byte[2 * TarConstants.STANDARD_HEADER_SIZE];
        Assert.assertArrayEquals(expectedData, ((ByteArrayOutputStream) os).toByteArray());
    }

    @Test
    public void testWriteBytes() throws IOException {
        OutputStream os = new ByteArrayOutputStream();
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(os);
        String fileName = "testFile.txt";
        TarArchiveEntry entry = new TarArchiveEntry(new File(fileName)) {
            @Override
            public String getName() {
                return fileName;
            }
        };
        tarArchiveOutputStream.putArchiveEntry(entry);

        byte[] testData = "Test data".getBytes(StandardCharsets.UTF_8);
        int totalBytesToWrite = testData.length + TarConstants.STANDARD_HEADER_SIZE;
        int chunkSize = TarConstants.DEFAULT_RCDSIZE;

        for (int i = 0; i < totalBytesToWrite; i += chunkSize) {
            int dataLength = Math.min(testData.length - i, chunkSize);
            tarArchiveOutputStream.write(testData, i, dataLength);
        }

        tarArchiveOutputStream.closeArchiveEntry();
        tarArchiveOutputStream.close();

        byte[] outputData = ((ByteArrayOutputStream) os).toByteArray();
        byte[] expectedData = new byte[testData.length + TarConstants.STANDARD_HEADER_SIZE];
        System.arraycopy(entry.getHeader(), 0, expectedData, 0, TarConstants.STANDARD_HEADER_SIZE);
        System.arraycopy(testData, 0, expectedData, TarConstants.STANDARD_HEADER_SIZE, testData.length);

        Assert.assertArrayEquals(expectedData, outputData);
    }
}