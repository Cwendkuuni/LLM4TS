import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarConstants;
import org.apache.commons.compress.archivers.tar.TarBuffer;
import org.apache.commons.compress.archivers.tar.TarArchiveOutputStream;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class TarArchiveOutputStreamTest {

    private ByteArrayOutputStream out;
    private TarArchiveOutputStream tarOutputStream;

    @Before
    public void setUp() throws Exception {
        out = new ByteArrayOutputStream();
        tarOutputStream = new TarArchiveOutputStream(out);
    }

    @After
    public void tearDown() throws Exception {
        tarOutputStream.close();
    }

    @Test
    public void testConstructor() {
        TarArchiveOutputStream tarOutputStream = new TarArchiveOutputStream(new ByteArrayOutputStream());
        assertNotNull(tarOutputStream);
    }

    @Test
    public void testSetLongFileMode() throws IOException {
        TarArchiveOutputStream tarOutputStream = new TarArchiveOutputStream(new ByteArrayOutputStream());
        tarOutputStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_TRUNCATE);
        assertEquals(TarArchiveOutputStream.LONGFILE_TRUNCATE, tarOutputStream.getLongFileMode());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetLongFileModeWithInvalidMode() throws IOException {
        TarArchiveOutputStream tarOutputStream = new TarArchiveOutputStream(new ByteArrayOutputStream());
        tarOutputStream.setLongFileMode(3);
    }

    @Test
    public void testFinish() throws IOException {
        TarArchiveEntry entry = new TarArchiveEntry("test.txt");
        tarOutputStream.putArchiveEntry(entry);
        tarOutputStream.finish();
        assertEquals(TarConstants.MAGIC, out.read(0, TarConstants.MAGIC_LENGTH, 0));
        assertEquals(TarConstants.MAGIC, out.read(0, TarConstants.MAGIC_LENGTH, TarConstants.MAGIC_LENGTH));
        assertEquals(1, out.size());
    }

    @Test(expected = IOException.class)
    public void testFinishWithUnclosedEntry() throws IOException {
        TarArchiveEntry entry = new TarArchiveEntry("test.txt");
        tarOutputStream.putArchiveEntry(entry);
        tarOutputStream.finish(); // This should close the entry
        tarOutputStream.write(new byte[10]); // This should open a new entry
        tarOutputStream.finish();
    }

    @Test
    public void testClose() throws IOException {
        TarArchiveEntry entry = new TarArchiveEntry("test.txt");
        tarOutputStream.putArchiveEntry(entry);
        tarOutputStream.close();
        assertEquals(1, out.size());
    }

    @Test(expected = IOException.class)
    public void testWriteWithInvalidOffsetOrLength() throws IOException {
        TarArchiveEntry entry = new TarArchiveEntry("test.txt");
        tarOutputStream.putArchiveEntry(entry);
        tarOutputStream.write(new byte[1], -1, 1);
    }

    @Test
    public void testWrite() throws IOException {
        TarArchiveEntry entry = new TarArchiveEntry("test.txt");
        tarOutputStream.putArchiveEntry(entry);
        byte[] data = "Test data".getBytes(StandardCharsets.UTF_8);
        tarOutputStream.write(data, 0, data.length);
        byte[] expectedData = new byte[data.length + 512]; // 512 is a record size
        System.arraycopy(data, 0, expectedData, 0, data.length);
        assertArrayEquals(expectedData, out.toByteArray());
    }

    @Test
    public void testWriteLargeFile() throws IOException {
        TarArchiveEntry entry = new TarArchiveEntry("test.txt");
        tarOutputStream.putArchiveEntry(entry);
        byte[] largeData = new byte[TarConstants.NAMELEN * 2];
        tarOutputStream.write(largeData, 0, largeData.length);
        byte[] expectedData = new byte[largeData.length + 512]; // 512 is a record size
        System.arraycopy(largeData, 0, expectedData, 0, largeData.length);
        assertArrayEquals(expectedData, out.toByteArray());
    }

    @Test(expected = IOException.class)
    public void testWriteWithUnclosedEntry() throws IOException {
        TarArchiveEntry entry = new TarArchiveEntry("test.txt");
        tarOutputStream.putArchiveEntry(entry);
        byte[] data = "Test data".getBytes(StandardCharsets.UTF_8);
        tarOutputStream.write(data, 0, data.length);
        tarOutputStream.write(new byte[1]); // This should open a new entry
        tarOutputStream.write(data, 0, data.length);
    }

    @Test(expected = IOException.class)
    public void testCloseArchiveEntryWithUnwrittenData() throws IOException {
        TarArchiveEntry entry = new TarArchiveEntry("test.txt");
        tarOutputStream.putArchiveEntry(entry);
        tarOutputStream.closeArchiveEntry();
        tarOutputStream.write(new byte[1]); // This should open a new entry
        tarOutputStream.closeArchiveEntry();
    }

    @Test
    public void testCloseArchiveEntryWithWrittenData() throws IOException {
        TarArchiveEntry entry = new TarArchiveEntry("test.txt");
        tarOutputStream.putArchiveEntry(entry);
        byte[] data = "Test data".getBytes(StandardCharsets.UTF_8);
        tarOutputStream.write(data, 0, data.length);
        tarOutputStream.closeArchiveEntry();
        assertEquals(1, out.size());
    }

    @Test
    public void testPutArchiveEntryWithLongFileName() throws IOException {
        TarArchiveEntry entry = new TarArchiveEntry("long_filename_" + TarConstants.NAMELEN + "long.txt");
        tarOutputStream.putArchiveEntry(entry);
        byte[] expectedLongFileName = new byte[TarConstants.NAMELEN * 2 + 512]; // 512 is a record size
        for (int i = 0; i < TarConstants.NAMELEN * 2; i++) {
            expectedLongFileName[i] = (byte) ('a' + i % 26);
        }
        assertArrayEquals(expectedLongFileName, out.toByteArray());
    }

    @Test(expected = IOException.class)
    public void testPutArchiveEntryWithLongFileNameAndErrorLongFileMode() throws IOException {
        TarArchiveOutputStream tarOutputStream = new TarArchiveOutputStream(new ByteArrayOutputStream());
        tarOutputStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_ERROR);
        TarArchiveEntry entry = new TarArchiveEntry("long_filename_" + TarConstants.NAMELEN + "long.txt");
        tarOutputStream.putArchiveEntry(entry);
    }

    @Test(expected = IOException.class)
    public void testPutArchiveEntryWithLongFileNameAndTruncateLongFileMode() throws IOException {
        TarArchiveOutputStream tarOutputStream = new TarArchiveOutputStream(new ByteArrayOutputStream());
        tarOutputStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_TRUNCATE);
        TarArchiveEntry entry = new TarArchiveEntry("long_filename_" + TarConstants.NAMELEN + "long.txt");
        tarOutputStream.putArchiveEntry(entry);
    }

    @Test
    public void testPutArchiveEntryWithLongFileNameAndGNULongFileMode() throws IOException {
        TarArchiveOutputStream tarOutputStream = new TarArchiveOutputStream(new ByteArrayOutputStream());
        tarOutputStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_GNU);
        TarArchiveEntry entry = new TarArchiveEntry("long_filename_" + TarConstants.NAMELEN + "long.txt");
        tarOutputStream.putArchiveEntry(entry);
        byte[] expectedLongFileName = new byte[TarConstants.NAMELEN * 2 + 1024]; // 1024 is a record size in GNU mode
        for (int i = 0; i < TarConstants.NAMELEN * 2; i++) {
            expectedLongFileName[i] = (byte) ('a' + i % 26);
        }
        byte[] longLinkName = "long_link_filename_" + TarConstants.NAMELEN + "long.tar".getBytes(StandardCharsets.UTF_8);
        System.arraycopy(longLinkName, 0, expectedLongFileName, TarConstants.NAMELEN * 2, longLinkName.length);
        assertArrayEquals(expectedLongFileName, out.toByteArray());
    }

    private void assertArrayEquals(byte[] expected, byte[] actual) {
        assertEquals(expected.length, actual.length);
        for (int i = 0; i < expected.length; i++) {
            assertEquals(expected[i], actual[i]);
        }
    }
}