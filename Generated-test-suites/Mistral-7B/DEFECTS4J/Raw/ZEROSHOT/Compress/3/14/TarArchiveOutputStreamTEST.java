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
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

public class TarArchiveOutputStreamTest {

    private ByteArrayOutputStream outputStream;
    private TarArchiveOutputStream tarArchiveOutputStream;

    @Before
    public void setUp() throws Exception {
        outputStream = new ByteArrayOutputStream();
        tarArchiveOutputStream = new TarArchiveOutputStream(outputStream, TarBuffer.DEFAULT_BLKSIZE, TarBuffer.DEFAULT_RCDSIZE);
    }

    @After
    public void tearDown() throws Exception {
        tarArchiveOutputStream.close();
    }

    @Test
    public void testConstructor() {
        TarArchiveOutputStream tarArchiveOutputStream1 = new TarArchiveOutputStream(new ByteArrayOutputStream());
        TarArchiveOutputStream tarArchiveOutputStream2 = new TarArchiveOutputStream(new ByteArrayOutputStream(), TarBuffer.DEFAULT_BLKSIZE);
        TarArchiveOutputStream tarArchiveOutputStream3 = new TarArchiveOutputStream(new ByteArrayOutputStream(), TarBuffer.DEFAULT_BLKSIZE, TarBuffer.DEFAULT_RCDSIZE);

        assertNull(tarArchiveOutputStream1.getBuffer());
        assertSame(TarBuffer.DEFAULT_BLKSIZE, tarArchiveOutputStream2.getBuffer().getBlockSize());
        assertSame(TarBuffer.DEFAULT_RCDSIZE, tarArchiveOutputStream3.getBuffer().getRecordSize());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWithNegativeBlockSize() {
        new TarArchiveOutputStream(new ByteArrayOutputStream(), -1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWithNegativeRecordSize() {
        new TarArchiveOutputStream(new ByteArrayOutputStream(), TarBuffer.DEFAULT_BLKSIZE, -1);
    }

    @Test
    public void testSetLongFileMode() {
        tarArchiveOutputStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_TRUNCATE);
        assertEquals(TarArchiveOutputStream.LONGFILE_TRUNCATE, tarArchiveOutputStream.getLongFileMode());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetLongFileModeWithInvalidMode() {
        tarArchiveOutputStream.setLongFileMode(4);
    }

    @Test
    public void testFinish() throws IOException {
        tarArchiveOutputStream.putNextEntry(new TarArchiveEntry("test.txt"));
        tarArchiveOutputStream.write("Hello World!".getBytes(StandardCharsets.UTF_8));
        tarArchiveOutputStream.closeArchiveEntry();
        tarArchiveOutputStream.finish();

        byte[] expected = createExpectedTarContent("test.txt", "Hello World!");
        byte[] actual = outputStream.toByteArray();

        assertArrayEquals(expected, actual);
    }

    @Test(expected = IOException.class)
    public void testFinishWithActiveEntry() throws IOException {
        tarArchiveOutputStream.putNextEntry(new TarArchiveEntry("test.txt"));
        tarArchiveOutputStream.write("Hello World!".getBytes(StandardCharsets.UTF_8));
        tarArchiveOutputStream.finish();
        tarArchiveOutputStream.write("Another text!".getBytes(StandardCharsets.UTF_8));
    }

    @Test(expected = IOException.class)
    public void testFinishWithClosedStream() throws IOException {
        tarArchiveOutputStream.finish();
        tarArchiveOutputStream.close();
        tarArchiveOutputStream.write(1);
    }

    @Test
    public void testClose() throws IOException {
        tarArchiveOutputStream.putNextEntry(new TarArchiveEntry("test.txt"));
        tarArchiveOutputStream.write("Hello World!".getBytes(StandardCharsets.UTF_8));
        tarArchiveOutputStream.closeArchiveEntry();
        tarArchiveOutputStream.close();

        byte[] expected = createExpectedTarContent("test.txt", "Hello World!");
        byte[] actual = outputStream.toByteArray();

        assertArrayEquals(expected, actual);
        assertFalse(tarArchiveOutputStream.isOpen());
    }

    @Test(expected = IOException.class)
    public void testCloseWithActiveEntry() throws IOException {
        tarArchiveOutputStream.putNextEntry(new TarArchiveEntry("test.txt"));
        tarArchiveOutputStream.write("Hello World!".getBytes(StandardCharsets.UTF_8));
        tarArchiveOutputStream.close();
    }

    @Test(expected = IOException.class)
    public void testCloseWithClosedStream() throws IOException {
        tarArchiveOutputStream.close();
        tarArchiveOutputStream.close();
    }

    @Test(expected = IOException.class)
    public void testPutArchiveEntryWithNullEntry() throws IOException {
        tarArchiveOutputStream.putArchiveEntry(null);
    }

    @Test(expected = IOException.class)
    public void testPutArchiveEntryWithInvalidArchiveEntryClass() throws IOException {
        tarArchiveOutputStream.putArchiveEntry(new Object());
    }

    @Test(expected = IOException.class)
    public void testPutArchiveEntryWithSameNameTwice() throws IOException {
        tarArchiveOutputStream.putNextEntry(new TarArchiveEntry("test.txt"));
        tarArchiveOutputStream.write("Hello World!".getBytes(StandardCharsets.UTF_8));
        tarArchiveOutputStream.closeArchiveEntry();
        tarArchiveOutputStream.putNextEntry(new TarArchiveEntry("test.txt"));
    }

    @Test(expected = IOException.class)
    public void testPutArchiveEntryWithLongFileName() throws IOException {
        String longFileName = new String(new char[TarConstants.NAMELEN + 1]);
        tarArchiveOutputStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_GNU);
        tarArchiveOutputStream.putNextEntry(new TarArchiveEntry(longFileName));
    }

    @Test(expected = IOException.class)
    public void testPutArchiveEntryWithLongFileNameAndLongFileModeError() throws IOException {
        String longFileName = new String(new char[TarConstants.NAMELEN + 1]);
        tarArchiveOutputStream.putNextEntry(new TarArchiveEntry(longFileName));
    }

    @Test
    public void testPutNextEntry() throws IOException {
        tarArchiveOutputStream.putNextEntry(new TarArchiveEntry("test.txt"));
        assertEquals("test.txt", tarArchiveOutputStream.getCurrentEntryName());
    }

    @Test
    public void testGetCurrentEntryName() throws IOException {
        tarArchiveOutputStream.putNextEntry(new TarArchiveEntry("test.txt"));
        assertEquals("test.txt", tarArchiveOutputStream.getCurrentEntryName());

        tarArchiveOutputStream.closeArchiveEntry();
        assertNull(tarArchiveOutputStream.getCurrentEntryName());
    }

    @Test
    public void testWrite() throws IOException {
        byte[] data = "Hello World!".getBytes(StandardCharsets.UTF_8);
        tarArchiveOutputStream.putNextEntry(new TarArchiveEntry("test.txt"));
        tarArchiveOutputStream.write(data);
        tarArchiveOutputStream.closeArchiveEntry();

        byte[] expected = createExpectedTarContent("test.txt", "Hello World!");
        byte[] actual = outputStream.toByteArray();

        assertArrayEquals(expected, actual);
    }

    @Test
    public void testWriteWithActiveEntry() throws IOException {
        byte[] data = "Hello World!".getBytes(StandardCharsets.UTF_8);
        tarArchiveOutputStream.putNextEntry(new TarArchiveEntry("test.txt"));
        tarArchiveOutputStream.write(data, 1, 3);
        tarArchiveOutputStream.write(data, 4, 4);
        tarArchiveOutputStream.closeArchiveEntry();

        byte[] expected = createExpectedTarContent("test.txt", "World!");
        byte[] actual = outputStream.toByteArray();

        assertArrayEquals(expected, actual);
    }

    @Test
    public void testWriteWithClosedStream() throws IOException {
        byte[] data = "Hello World!".getBytes(StandardCharsets.UTF_8);
        tarArchiveOutputStream.write(data);
    }

    @Test
    public void testWriteWithZeroLength() throws IOException {
        tarArchiveOutputStream.write(new byte[0]);

        byte[] expected = new byte[0];
        byte[] actual = outputStream.toByteArray();

        assertArrayEquals(expected, actual);
    }

    @Test
    public void testWriteWithMultipleEntries() throws IOException {
        tarArchiveOutputStream.putNextEntry(new TarArchiveEntry("test1.txt"));
        tarArchiveOutputStream.write("Hello World!".getBytes(StandardCharsets.UTF_8));
        tarArchiveOutputStream.closeArchiveEntry();

        tarArchiveOutputStream.putNextEntry(new TarArchiveEntry("test2.txt"));
        tarArchiveOutputStream.write("Another Text!".getBytes(StandardCharsets.UTF_8));
        tarArchiveOutputStream.closeArchiveEntry();

        byte[] expected = createExpectedTarContent("test1.txt", "Hello World!", "test2.txt", "Another Text!");
        byte[] actual = outputStream.toByteArray();

        assertArrayEquals(expected, actual);
    }

    @Test
    public void testCreateArchiveEntryWithNullFile() throws IOException {
        tarArchiveOutputStream.createArchiveEntry(null, "test.txt");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateArchiveEntryWithNullName() throws IOException {
        tarArchiveOutputStream.createArchiveEntry(new File("test.txt"), null);
    }

    @Test
    public void testCreateArchiveEntry() throws IOException {
        File file = new File("test.txt");
        TarArchiveEntry archiveEntry = tarArchiveOutputStream.createArchiveEntry(file, "test.txt");
        assertEquals("test.txt", archiveEntry.getName());
    }

    @Test
    public void testCreateArchiveEntryWithLongFileName() throws IOException {
        File file = new File("test" + new String(new char[TarConstants.NAMELEN + 1]));
        TarArchiveEntry archiveEntry = tarArchiveOutputStream.createArchiveEntry(file, "test" + new String(new char[TarConstants.NAMELEN + 1]));
        assertEquals("test" + new String(new char[TarConstants.NAMELEN + 1]), archiveEntry.getName());
    }

    @Test
    public void testFlush() throws IOException {
        tarArchiveOutputStream.putNextEntry(new TarArchiveEntry("test.txt"));
        tarArchiveOutputStream.write("Hello World!".getBytes(StandardCharsets.UTF_8));
        tarArchiveOutputStream.flush();

        byte[] expected = createExpectedTarContent("test.txt", "Hello World!");
        byte[] actual = outputStream.toByteArray();

        assertArrayEquals(expected, actual);
    }

    private byte[] createExpectedTarContent(String fileName, String content) {
        byte[] header = TarConstants.createTarHeaderRecord("test.txt", (int) fileName.length(), 0, 0, 0, 0, TarConstants.STANDARD, 1);
        byte[] record = new byte[TarConstants.HEADER_SIZE + content.length()];

        System.arraycopy(header, 0, record, 0, TarConstants.HEADER_SIZE);
        System.arraycopy(content.getBytes(StandardCharsets.UTF_8), 0, record, TarConstants.HEADER_SIZE, content.length());

        return new byte[][]{header, record, TarConstants.createTarHeaderRecord("", 0, 0, 0, 0, 0, TarConstants.STDOUT, 0),
                TarConstants.createTarHeaderRecord("", 0, 0, 0, 0, 0, TarConstants.STDOUT, 0)};
    }

    private void assertArrayEquals(byte[] expected, byte[] actual) {
        assertTrue(compareBytes(expected, actual));
    }

    private static boolean compareBytes(byte[] a, byte[] b) {
        if (a.length != b.length) {
            return false;
        }
        for (int i = 0; i < a.length; i++) {
            if (a[i] != b[i]) {
                return false;
            }
        }
        return true;
    }
}