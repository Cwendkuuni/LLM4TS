import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.archivers.dump.DumpArchiveConstants;
import org.apache.commons.compress.archivers.dump.DumpArchiveEntry;
import org.apache.commons.compress.archivers.dump.DumpArchiveInputStream;
import org.apache.commons.compress.archivers.dump.DumpArchiveSummary;
import org.apache.commons.compress.archivers.zip.ZipEncoding;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;

public class DumpArchiveInputStreamTest {

    private DumpArchiveInputStream inputStream;
    private ByteArrayOutputStream outputStream;
    private byte[] expectedHeader;
    private byte[] expectedData;
    private ZipEncoding zipEncoding;

    @Before
    public void setup() {
        expectedHeader = DumpArchiveUtil.generateExpectedHeader(4, 16, 16, "test.dat");
        expectedData = new byte[]{"Test Data".getBytes()};
        zipEncoding = new ZipEncoding();
    }

    @Test
    public void testGetCount() throws IOException {
        inputStream = new DumpArchiveInputStream(new ByteArrayInputStream(expectedHeader), zipEncoding);
        Assert.assertEquals(expectedHeader.length, inputStream.getCount());
    }

    @Test
    public void testGetBytesRead() throws IOException {
        inputStream = new DumpArchiveInputStream(new ByteArrayInputStream(expectedHeader), zipEncoding);
        Assert.assertEquals(expectedHeader.length, inputStream.getBytesRead());
    }

    @Test
    public void testGetSummary() throws IOException {
        inputStream = new DumpArchiveInputStream(new ByteArrayInputStream(expectedHeader), zipEncoding);
        DumpArchiveSummary summary = inputStream.getSummary();
        Assert.assertNotNull(summary);
        Assert.assertEquals(DumpArchiveConstants.MAGIC, summary.getMagic());
        Assert.assertEquals(4, summary.getNumFiles());
        Assert.assertEquals(16, summary.getVersionMajor());
        Assert.assertEquals(16, summary.getVersionMinor());
    }

    @Test
    public void testGetNextEntry() throws IOException {
        inputStream = new DumpArchiveInputStream(new ByteArrayInputStream(expectedHeader), zipEncoding);
        DumpArchiveEntry nextEntry = inputStream.getNextEntry();
        Assert.assertNotNull(nextEntry);
        Assert.assertEquals("test.dat", nextEntry.getName());
        Assert.assertFalse(nextEntry.isDirectory());
    }

    @Test
    public void testRead() throws IOException {
        inputStream = new DumpArchiveInputStream(new ByteArrayInputStream(expectedHeader), zipEncoding);
        outputStream = new ByteArrayOutputStream();

        byte[] buffer = new byte[expectedData[0] == 0 ? 1 : expectedData.length];
        int bytesRead;

        // read all data
        while ((bytesRead = inputStream.read(buffer)) != -1) {
            outputStream.write(buffer, 0, bytesRead);
        }

        Assert.assertEquals(expectedData.length, outputStream.size());
        Assert.assertArrayEquals(expectedData, outputStream.toByteArray());
    }

    @Test
    public void testEmptyInputStream() throws IOException {
        inputStream = new DumpArchiveInputStream(new ByteArrayInputStream(new byte[0]), zipEncoding);
        Assert.assertNull(inputStream.getNextEntry());
    }

    @Test(expected = EOFException.class)
    public void testReadPastEnd() throws IOException {
        inputStream = new DumpArchiveInputStream(new ByteArrayInputStream(expectedHeader), zipEncoding);
        byte[] buffer = new byte[1];

        inputStream.read(buffer);
        inputStream.read(buffer);
        inputStream.read(buffer);
        inputStream.read(buffer);
        inputStream.read(buffer);

        inputStream.read(buffer); // should throw EOFException
    }

    @Test
    public void testReadWithBufferTooSmall() throws IOException {
        inputStream = new DumpArchiveInputStream(new ByteArrayInputStream(expectedHeader), zipEncoding);
        byte[] buffer = new byte[1];

        int bytesRead = inputStream.read(buffer);
        Assert.assertEquals(expectedData[0], bytesRead);
        Assert.assertArrayEquals(Arrays.copyOfRange(expectedData, 0, bytesRead), buffer);
    }

    @Test
    public void testReadWithInternalBuffer() throws IOException {
        inputStream = new DumpArchiveInputStream(new ByteArrayInputStream(expectedHeader), zipEncoding);
        byte[] buffer = new byte[expectedData.length];
        int bytesRead = inputStream.read(buffer);
        Assert.assertEquals(expectedData.length, bytesRead);
        Assert.assertArrayEquals(expectedData, buffer);
    }

    @Test
    public void testReadBlockWithInternalBuffer() throws IOException {
        inputStream = new DumpArchiveInputStream(new ByteArrayInputStream(expectedHeader), zipEncoding);
        byte[] buffer = new byte[expectedData.length];

        int bytesRead = inputStream.read(buffer, 0, buffer.length);
        Assert.assertEquals(expectedData.length, bytesRead);
        Assert.assertArrayEquals(expectedData, buffer);

        int offset = 2;
        int length = 3;
        bytesRead = inputStream.read(buffer, offset, length);
        Assert.assertEquals(3, bytesRead);
        Assert.assertArrayEquals(Arrays.copyOfRange(expectedData, offset, offset + length), buffer, offset, length);
    }

    @Test
    public void testReadWithEmptyBuffer() throws IOException {
        inputStream = new DumpArchiveInputStream(new ByteArrayInputStream(expectedHeader), zipEncoding);
        byte[] buffer = new byte[0];

        int bytesRead = inputStream.read(buffer);
        Assert.assertEquals(0, bytesRead);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testReadWithNegativeOffset() throws IOException {
        inputStream = new DumpArchiveInputStream(new ByteArrayInputStream(expectedHeader), zipEncoding);
        byte[] buffer = new byte[1];

        inputStream.read(buffer, -1, 1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testReadWithNegativeLength() throws IOException {
        inputStream = new DumpArchiveInputStream(new ByteArrayInputStream(expectedHeader), zipEncoding);
        byte[] buffer = new byte[1];

        inputStream.read(buffer, 0, -1);
    }

    @Test(expected = IOException.class)
    public void testReadWithNullBuffer() throws IOException {
        inputStream = new DumpArchiveInputStream(new ByteArrayInputStream(expectedHeader), zipEncoding);
        byte[] buffer = null;

        inputStream.read(buffer);
    }

    @Test(expected = IOException.class)
    public void testReadWithNullBufferAndNegativeOffset() throws IOException {
        inputStream = new DumpArchiveInputStream(new ByteArrayInputStream(expectedHeader), zipEncoding);
        byte[] buffer = null;

        inputStream.read(buffer, -1, 1);
    }

    @Test(expected = IOException.class)
    public void testReadWithNullBufferAndNegativeLength() throws IOException {
        inputStream = new DumpArchiveInputStream(new ByteArrayInputStream(expectedHeader), zipEncoding);
        byte[] buffer = null;

        inputStream.read(buffer, 0, -1);
    }
}