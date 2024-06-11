import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveOutputStream;
import org.apache.commons.compress.archivers.tar.TarConstants;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class TarArchiveOutputStreamTest {

    private static ByteArrayOutputStream out;
    private static TarArchiveOutputStream tarOut;

    @BeforeClass
    public static void setUp() throws IOException {
        out = new ByteArrayOutputStream();
        tarOut = new TarArchiveOutputStream(out);
    }

    @AfterClass
    public static void tearDown() throws IOException {
        tarOut.close();
    }

    // Test for basic functionality of putArchiveEntry method
    @Test
    public void testPutArchiveEntry() throws IOException {
        File testFile = new File("testFile.txt");
        TarArchiveEntry entry = new TarArchiveEntry("testFile.txt");
        entry.setSize(10);

        tarOut.putArchiveEntry(entry);
        tarOut.write("Hello World!".getBytes(StandardCharsets.UTF_8));
        tarOut.closeArchiveEntry();

        byte[] expectedHeader = "ustar\00001\00000\000\000\000\000" +
                TarConstants.NAME + TarConstants.FILE_TYPE +
                TarConstants.MODE + TarConstants.UID + TarConstants.GID +
                TarConstants.SIZE + TarConstants.MTIME +
                TarConstants.NAME + TarConstants.NAME + TarConstants.FILE_TYPE +
                TarConstants.MTIME + TarConstants.CHECKSUM + TarConstants.NAME + "\0";
        byte[] expectedData = "Hello World!".getBytes(StandardCharsets.UTF_8);

        byte[] actualData = out.toByteArray();

        byte[] header = extractHeader(actualData, 0);
        Assert.assertArrayEquals(expectedHeader, header);
        Assert.assertArrayEquals(expectedData, extractData(actualData, 0));
    }

    // Test for long file name handling
    @Test
    public void testLongFileName() throws IOException {
        String longFileName = "a/very/long/filename/testFile.txt";
        TarArchiveEntry entry = new TarArchiveEntry(longFileName);
        entry.setSize(10);

        tarOut.setLongFileMode(TarArchiveOutputStream.LONGFILE_GNU);
        tarOut.putArchiveEntry(entry);
        tarOut.write("Hello World!".getBytes(StandardCharsets.UTF_8));
        tarOut.closeArchiveEntry();

        byte[] expectedHeader = "ustar\00001\00000\000\000\000\000" +
                TarConstants.GNU_LONGLINK + TarConstants.NAME +
                TarConstants.FILE_TYPE + TarConstants.MODE + TarConstants.UID + TarConstants.GID +
                TarConstants.SIZE + TarConstants.MTIME +
                TarConstants.NAME + TarConstants.NAME + TarConstants.NAME + TarConstants.SIZE +
                TarConstants.MTIME + TarConstants.NAME + TarConstants.NAME + TarConstants.FILE_TYPE +
                TarConstants.MTIME + TarConstants.CHECKSUM + TarConstants.NAME + "\0";
        byte[] expectedData = "Hello World!".getBytes(StandardCharsets.UTF_8);

        byte[] actualData = out.toByteArray();

        Assert.assertArrayEquals(expectedHeader, extractHeader(actualData, 0));
        Assert.assertArrayEquals(expectedData, extractData(actualData, 0));
    }

    // Test for setting longFileMode in the constructor
    @Test
    public void testLongFileModeInConstructor() throws IOException {
        String longFileName = "a/very/long/filename/testFile.txt";
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        TarArchiveOutputStream tarOut = new TarArchiveOutputStream(out, 0, 0, TarArchiveOutputStream.LONGFILE_GNU);

        TarArchiveEntry entry = new TarArchiveEntry(longFileName);
        entry.setSize(10);

        tarOut.putArchiveEntry(entry);
        tarOut.write("Hello World!".getBytes(StandardCharsets.UTF_8));
        tarOut.closeArchiveEntry();

        byte[] expectedHeader = "ustar\00001\00000\000\000\000\000" +
                TarConstants.GNU_LONGLINK + TarConstants.NAME +
                TarConstants.FILE_TYPE + TarConstants.MODE + TarConstants.UID + TarConstants.GID +
                TarConstants.SIZE + TarConstants.MTIME +
                TarConstants.NAME + TarConstants.NAME + TarConstants.NAME + TarConstants.SIZE +
                TarConstants.MTIME + TarConstants.CHECKSUM + TarConstants.NAME + "\0";
        byte[] expectedData = "Hello World!".getBytes(StandardCharsets.UTF_8);

        byte[] actualData = out.toByteArray();

        Assert.assertArrayEquals(expectedHeader, extractHeader(actualData, 0));
        Assert.assertArrayEquals(expectedData, extractData(actualData, 0));
    }

    // Test for closing archive without writing all the data
    @Test
    public void testUnclosedEntry() throws IOException {
        File testFile = new File("testFile.txt");
        TarArchiveEntry entry = new TarArchiveEntry("testFile.txt");
        entry.setSize(10);

        tarOut.putArchiveEntry(entry);
        tarOut.write("Hello World!".getBytes(StandardCharsets.UTF_8), 0, 5);

        try {
            tarOut.close();
        } catch (IOException e) {
            Assert.assertEquals("This archives contains unclosed entries.", e.getMessage());
        }

        Assert.assertEquals(5, out.size());
    }

    // Test for writing beyond the specified size
    @Test(expected = IOException.class)
    public void testWriteBeyondSpecifiedSize() throws IOException {
        File testFile = new File("testFile.txt");
        TarArchiveEntry entry = new TarArchiveEntry("testFile.txt");
        entry.setSize(10);

        tarOut.putArchiveEntry(entry);

        tarOut.write("Hello World!".getBytes(StandardCharsets.UTF_8), 0, 11);
    }

    private byte[] extractHeader(byte[] data, int offset) {
        int headerLen = TarConstants.HEADER_LENGTH;
        return extractBytes(data, offset, headerLen);
    }

    private byte[] extractData(byte[] data, int offset) {
        int size = ((int) extractLong(data, offset + TarConstants.SIZE) & 0xFFFFFFFFL);
        return extractBytes(data, offset + TarConstants.HEADER_LENGTH, size);
    }

    private byte[] extractBytes(byte[] data, int offset, int length) {
        byte[] result = new byte[length];
        System.arraycopy(data, offset, result, 0, length);
        return result;
    }

    private long extractLong(byte[] data, int offset) {
        long result = 0;
        for (int i = 0; i < 8; i++) {
            result |= (long) ((data[offset + i] & 0xFF) << (i * 8));
        }
        return result;
    }
}