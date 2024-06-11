import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveOutputStream;
import org.apache.commons.compress.utils.IOUtils;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

public class TarArchiveOutputStreamTest {

    private OutputStream outputStream;
    private TarArchiveOutputStream tarArchiveOutputStream;

    @Before
    public void setup() throws Exception {
        outputStream = new ByteArrayOutputStream();
        tarArchiveOutputStream = new TarArchiveOutputStream(outputStream);
    }

    @After
    public void teardown() throws Exception {
        tarArchiveOutputStream.close();
    }

    /**
     * Test putArchiveEntry method with valid arguments and no exceptions thrown.
     */
    @Test
    public void testPutArchiveEntryValid() throws IOException {
        TarArchiveEntry entry = new TarArchiveEntry("test.txt", "Test file content.");
        tarArchiveOutputStream.putArchiveEntry(entry);
        tarArchiveOutputStream.write("Hello, World!".getBytes());
        tarArchiveOutputStream.closeArchiveEntry();

        byte[] expectedHeader = Arrays.copyOfRange(entry.getHeader(), 0, TarConstants.HEADER_LENGTH);
        byte[] expectedContent = "Hello, World!".getBytes();

        byte[] actual = outputStream.toByteArray();

        Assert.assertArrayEquals(expectedHeader, Arrays.copyOfRange(actual, 0, TarConstants.HEADER_LENGTH));
        Assert.assertArrayEquals(expectedContent, Arrays.copyOfRange(actual, TarConstants.HEADER_LENGTH, TarConstants.HEADER_LENGTH + expectedContent.length));
    }

    /**
     * Test putArchiveEntry method with a null ArchiveEntry argument.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testPutArchiveEntryNullEntry() throws IOException {
        tarArchiveOutputStream.putArchiveEntry(null);
    }

    /**
     * Test putArchiveEntry method with an invalid record size.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testPutArchiveEntryInvalidRecordSize() throws IOException {
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(outputStream, 5);
        TarArchiveEntry entry = new TarArchiveEntry("test.txt", "Test file content.");
        tarArchiveOutputStream.putArchiveEntry(entry);

        try {
            tarArchiveOutputStream.write("Hello, World!".getBytes());
            tarArchiveOutputStream.closeArchiveEntry();
        } finally {
            tarArchiveOutputStream.close();
        }

        byte[] actual = outputStream.toByteArray();
        byte[] expected = Arrays.copyOfRange(entry.getHeader(), 0, TarConstants.HEADER_LENGTH);
        Assert.assertArrayEquals(expected, Arrays.copyOfRange(actual, 0, TarConstants.HEADER_LENGTH));
        Assert.assertEquals(0, actual.length - TarConstants.HEADER_LENGTH);
    }

    /**
     * Test putArchiveEntry method with a long file name.
     */
    @Test
    public void testPutArchiveEntryLongFileName() throws IOException {
        String longFileName = "a/long/path/test.txt";
        TarArchiveEntry entry = new TarArchiveEntry(longFileName, "Test file content.");
        tarArchiveOutputStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_GNU);
        tarArchiveOutputStream.putArchiveEntry(entry);
        tarArchiveOutputStream.write("Hello, World!".getBytes());
        tarArchiveOutputStream.closeArchiveEntry();

        byte[] expectedHeader = Arrays.copyOfRange(entry.getHeader(), 0, TarConstants.HEADER_LENGTH);
        byte[] expectedContent = "Hello, World!".getBytes();

        byte[] actual = outputStream.toByteArray();
        int longFileNameLength = longFileName.getBytes(StandardCharsets.UTF_8).length;

        Assert.assertEquals(longFileNameLength + TarConstants.HEADER_LENGTH + expectedContent.length, actual.length);
        Assert.assertArrayEquals(expectedHeader, Arrays.copyOfRange(actual, 0, TarConstants.HEADER_LENGTH));
        Assert.assertArrayEquals(expectedContent, Arrays.copyOfRange(actual, TarConstants.HEADER_LENGTH, TarConstants.HEADER_LENGTH + expectedContent.length));
    }

    /**
     * Test write method with valid arguments and no exceptions thrown.
     */
    @Test
    public void testWriteValid() throws IOException {
        TarArchiveEntry entry = new TarArchiveEntry("test.txt", "Test file content.");
        tarArchiveOutputStream.putArchiveEntry(entry);
        tarArchiveOutputStream.write("Hello, World!".getBytes());
        tarArchiveOutputStream.closeArchiveEntry();

        byte[] expectedContent = "Hello, World!".getBytes();

        byte[] actual = outputStream.toByteArray();
        int headerLength = TarConstants.HEADER_LENGTH;

        Assert.assertEquals(headerLength + expectedContent.length, actual.length);
        Assert.assertArrayEquals(expectedContent, Arrays.copyOfRange(actual, headerLength, headerLength + expectedContent.length));
    }

    /**
     * Test write method with null input data.
     */
    @Test(expected = NullPointerException.class)
    public void testWriteNullData() throws IOException {
        tarArchiveOutputStream.write(null);
    }

    /**
     * Test write method with no data to write.
     */
    @Test
    public void testWriteNoData() throws IOException {
        TarArchiveEntry entry = new TarArchiveEntry("test.txt", "Test file content.");
        tarArchiveOutputStream.putArchiveEntry(entry);
        tarArchiveOutputStream.write(new byte[0]);
        tarArchiveOutputStream.closeArchiveEntry();

        byte[] actual = outputStream.toByteArray();
        int headerLength = TarConstants.HEADER_LENGTH;

        Assert.assertEquals(headerLength, actual.length);
    }

    /**
     * Test write method with a large amount of data.
     */
    @Test
    public void testWriteLargeData() throws IOException {
        TarArchiveEntry entry = new TarArchiveEntry("test.txt", "Test file content.");
        tarArchiveOutputStream.putArchiveEntry(entry);

        byte[] data = new byte[1024];
        tarArchiveOutputStream.write(data);
        tarArchiveOutputStream.closeArchiveEntry();

        byte[] actual = outputStream.toByteArray();
        int headerLength = TarConstants.HEADER_LENGTH;
        int dataLength = data.length;

        Assert.assertEquals(headerLength + dataLength, actual.length);
        Assert.assertArrayEquals(Arrays.copyOfRange(data, 0, data.length), Arrays.copyOfRange(actual, headerLength, headerLength + dataLength));
    }

    /**
     * Test finish method with an unclosed entry.
     */
    @Test(expected = IOException.class)
    public void testFinishUnclosedEntry() throws IOException {
        TarArchiveEntry entry = new TarArchiveEntry("test.txt", "Test file content.");
        tarArchiveOutputStream.putArchiveEntry(entry);
        tarArchiveOutputStream.finish();

        tarArchiveOutputStream.write("Hello, World!".getBytes());
        tarArchiveOutputStream.closeArchiveEntry();

        try {
            tarArchiveOutputStream.finish();
        } finally {
            tarArchiveOutputStream.close();
        }
    }

    /**
     * Test finish method with no unclosed entries.
     */
    @Test
    public void testFinishNoUnclosedEntries() throws IOException {
        TarArchiveEntry entry = new TarArchiveEntry("test.txt", "Test file content.");
        tarArchiveOutputStream.putArchiveEntry(entry);
        tarArchiveOutputStream.write("Hello, World!".getBytes());
        tarArchiveOutputStream.closeArchiveEntry();

        tarArchiveOutputStream.finish();
        tarArchiveOutputStream.close();
    }

    /**
     * Test close method with an unfinished archive.
     */
    @Test(expected = IOException.class)
    public void testCloseUnfinishedArchive() throws IOException {
        TarArchiveEntry entry = new TarArchiveEntry("test.txt", "Test file content.");
        tarArchiveOutputStream.putArchiveEntry(entry);

        try {
            tarArchiveOutputStream.close();
        } finally {
            tarArchiveOutputStream.finish();
        }
    }

    /**
     * Test close method after finishing the archive.
     */
    @Test
    public void testCloseFinishedArchive() throws IOException {
        TarArchiveEntry entry = new TarArchiveEntry("test.txt", "Test file content.");
        tarArchiveOutputStream.putArchiveEntry(entry);
        tarArchiveOutputStream.write("Hello, World!".getBytes());
        tarArchiveOutputStream.closeArchiveEntry();

        tarArchiveOutputStream.finish();
        tarArchiveOutputStream.close();

        byte[] actual = outputStream.toByteArray();
        int headerLength = TarConstants.HEADER_LENGTH;
        int dataLength = "Hello, World!".getBytes().length;

        Assert.assertEquals(headerLength + dataLength, actual.length);
    }
}