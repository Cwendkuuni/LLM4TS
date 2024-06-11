import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarConstants;
import org.apache.commons.compress.archivers.tar.TarArchiveOutputStream;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

public class TarArchiveOutputStreamTest {

    private TarArchiveOutputStream outputStream;
    private ByteArrayOutputStream byteArrayOutputStream;

    @Before
    public void setUp() throws IOException {
        byteArrayOutputStream = new ByteArrayOutputStream();
        outputStream = new TarArchiveOutputStream(byteArrayOutputStream);
    }

    @After
    public void tearDown() throws IOException {
        outputStream.close();
    }

    @Test
    public void testConstructorWithOutputStream() {
        assertNotNull(outputStream);
    }

    @Test
    public void testConstructorWithOutputStreamBlockSize() {
        assertNotNull(outputStream);
    }

    @Test
    public void testConstructorWithOutputStreamBlockSizeRecordSize() {
        assertNotNull(outputStream);
    }

    @Test
    public void testSetLongFileMode() {
        outputStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_TRUNCATE);
        assertEquals(TarArchiveOutputStream.LONGFILE_TRUNCATE, outputStream.getLongFileMode());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetLongFileModeInvalidMode() {
        outputStream.setLongFileMode(3);
    }

    @Test
    public void testFinish() throws IOException {
        outputStream.write(new byte[1]);
        outputStream.finish();
        byte[] actualBytes = byteArrayOutputStream.toByteArray();
        byte[] expectedBytes = new byte[ TarConstants.HEADER_LENGTH * 2 ];
        assertEquals(expectedBytes.length, actualBytes.length);
        for (int i = 0; i < expectedBytes.length; i++) {
            expectedBytes[i] = (byte) 0;
        }
        assertArrayEquals(expectedBytes, actualBytes);
    }

    @Test
    public void testClose() throws IOException {
        outputStream.write(new byte[1]);
        outputStream.close();
        byte[] actualBytes = byteArrayOutputStream.toByteArray();
        byte[] expectedBytes = new byte[ TarConstants.HEADER_LENGTH * 2 ];
        assertEquals(expectedBytes.length, actualBytes.length);
        for (int i = 0; i < expectedBytes.length; i++) {
            expectedBytes[i] = (byte) 0;
        }
        assertArrayEquals(expectedBytes, actualBytes);
    }

    @Test
    public void testGetRecordSize() {
        int recordSize = outputStream.getRecordSize();
        assertEquals(TarConstants.RECORD_SIZE, recordSize);
    }

    @Test
    public void testPutArchiveEntry() throws IOException {
        TarArchiveEntry entry = new TarArchiveEntry("test.txt", "test content");
        outputStream.putArchiveEntry(entry);
        byte[] actualBytes = byteArrayOutputStream.toByteArray();
        byte[] expectedHeaderBytes = TarArchiveEntry.createHeaderBytes(entry);
        byte[] expectedContentBytes = "test content".getBytes(StandardCharsets.UTF_8);
        byte[] expectedBytes = new byte[TarConstants.HEADER_LENGTH + expectedHeaderBytes.length + expectedContentBytes.length];
        System.arraycopy(expectedHeaderBytes, 0, expectedBytes, 0, TarConstants.HEADER_LENGTH);
        System.arraycopy(expectedContentBytes, 0, expectedBytes, TarConstants.HEADER_LENGTH, expectedContentBytes.length);
        assertArrayEquals(expectedBytes, actualBytes);
    }

    @Test(expected = IllegalStateException.class)
    public void testPutArchiveEntryWithoutCloseArchiveEntry() throws IOException {
        TarArchiveEntry entry = new TarArchiveEntry("test.txt", "test content");
        outputStream.putArchiveEntry(entry);
    }

    @Test
    public void testCloseArchiveEntry() throws IOException {
        TarArchiveEntry entry = new TarArchiveEntry("test.txt", "test content".getBytes(StandardCharsets.UTF_8));
        outputStream.putArchiveEntry(entry);
        outputStream.closeArchiveEntry();
        byte[] actualBytes = byteArrayOutputStream.toByteArray();
        byte[] expectedBytes = new byte[TarConstants.HEADER_LENGTH + "test content".length()];
        assertArrayEquals(expectedBytes, actualBytes);
    }

    @Test
    public void testWrite() throws IOException {
        byte[] input = new byte[] {1, 2, 3};
        TarArchiveEntry entry = new TarArchiveEntry("test.txt");
        outputStream.putArchiveEntry(entry);
        outputStream.write(input, 0, input.length);
        outputStream.closeArchiveEntry();
        byte[] actualBytes = byteArrayOutputStream.toByteArray();
        byte[] expectedHeaderBytes = TarArchiveEntry.createHeaderBytes(entry);
        byte[] expectedContentBytes = new byte[input.length];
        System.arraycopy(expectedHeaderBytes, 0, expectedBytes, 0, TarConstants.HEADER_LENGTH + input.length);
        System.arraycopy(input, 0, expectedContentBytes, TarConstants.HEADER_LENGTH, input.length);
        assertArrayEquals(expectedBytes, actualBytes);
    }

    @Test
    public void testWriteWithBuffer() throws IOException {
        byte[] input = new byte[] {1, 2, 3};
        outputStream.putArchiveEntry(new TarArchiveEntry("test.txt"));
        outputStream.write(input);
        outputStream.write(input);
        outputStream.closeArchiveEntry();
        byte[] actualBytes = byteArrayOutputStream.toByteArray();
        byte[] expectedBytes = new byte[TarConstants.HEADER_LENGTH + 2 * input.length];
        System.arraycopy(TarArchiveEntry.createHeaderBytes(new TarArchiveEntry("test.txt")), 0, expectedBytes, 0, TarConstants.HEADER_LENGTH);
        System.arraycopy(input, 0, expectedBytes, TarConstants.HEADER_LENGTH, input.length * 2);
        assertArrayEquals(expectedBytes, actualBytes);
    }

    @Test(expected = IOException.class)
    public void testWriteWithoutEntry() throws IOException {
        outputStream.write(new byte[1]);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testWriteNegativeOffset() throws IOException {
        byte[] input = new byte[] {1, 2, 3};
        outputStream.write(input, -1, input.length);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testWriteNegativeLength() throws IOException {
        byte[] input = new byte[] {1, 2, 3};
        outputStream.write(input, 0, -1);
    }

    @Test
    public void testWriteWithLongFileMode() throws IOException {
        byte[] longName = new byte[TarConstants.NAMELEN + 1];
        for (int i = 0; i < TarConstants.NAMELEN; i++) {
            longName[i] = (byte) ('a' + i);
        }
        longName[TarConstants.NAMELEN] = 0;
        outputStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_GNU);
        TarArchiveEntry entry = new TarArchiveEntry("long name", longName);
        outputStream.putArchiveEntry(entry);
        outputStream.write(longName);
        outputStream.closeArchiveEntry();
        byte[] actualBytes = byteArrayOutputStream.toByteArray();
        byte[] expectedHeaderBytes = TarArchiveEntry.createHeaderBytes(entry);
        byte[] expectedContentBytes = longName;
        byte[] expectedBytes = new byte[TarConstants.HEADER_LENGTH + longName.length];
        System.arraycopy(expectedHeaderBytes, 0, expectedBytes, 0, TarConstants.HEADER_LENGTH);
        System.arraycopy(longName, 0, expectedBytes, TarConstants.HEADER_LENGTH, longName.length);
        assertArrayEquals(expectedBytes, actualBytes);
    }

    @Test(expected = RuntimeException.class)
    public void testPutArchiveEntryWithLongFileNameWithoutLongFileMode() throws IOException {
        byte[] longName = new byte[TarConstants.NAMELEN + 1];
        for (int i = 0; i < TarConstants.NAMELEN; i++) {
            longName[i] = (byte) ('a' + i);
        }
        longName[TarConstants.NAMELEN] = 0;
        TarArchiveEntry entry = new TarArchiveEntry("long name", longName);
        outputStream.putArchiveEntry(entry);
    }

    @Test(expected = IOException.class)
    public void testFinishWithoutPutArchiveEntry() throws IOException {
        outputStream.finish();
    }

    @Test(expected = IOException.class)
    public void testCloseWithoutFinish() throws IOException {
        outputStream.close();
    }

    @Test
    public void testCreateArchiveEntry() throws IOException {
        File inputFile = new File("test.txt");
        TarArchiveEntry entry = outputStream.createArchiveEntry(inputFile, "test.txt");
        assertNotNull(entry);
    }

    @Test(expected = NullPointerException.class)
    public void testCreateArchiveEntryWithNullFile() throws IOException {
        outputStream.createArchiveEntry(null, "test.txt");
    }

    @Test
    public void testCreateArchiveEntryWithEmptyFileName() throws IOException {
        outputStream.createArchiveEntry(new File(""), "test.txt");
        TarArchiveEntry entry = outputStream.getNextEntry();
        assertEquals("", entry.getName());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateArchiveEntryWithLongFileName() throws IOException {
        File inputFile = new File("long name");
        outputStream.createArchiveEntry(inputFile, "long name");
    }

    @Test
    public void testGetNextEntry() throws IOException {
        TarArchiveEntry entry = new TarArchiveEntry("test.txt");
        outputStream.putArchiveEntry(entry);
        outputStream.closeArchiveEntry();
        TarArchiveEntry nextEntry = outputStream.getNextEntry();
        assertNotNull(nextEntry);
        assertEquals("test.txt", nextEntry.getName());
    }

    @Test
    public void testGetNextEntryAfterFinish() throws IOException {
        outputStream.finish();
        TarArchiveEntry nextEntry = outputStream.getNextEntry();
        assertNull(nextEntry);
    }

    @Test
    public void testGetNextEntryAfterClose() throws IOException {
        outputStream.close();
        TarArchiveEntry nextEntry = outputStream.getNextEntry();
        assertNull(nextEntry);
    }

    @Test
    public void testFlush() throws IOException {
        outputStream.flush();
        byte[] actualBytes = byteArrayOutputStream.toByteArray();
        byte[] expectedBytes = new byte[TarConstants.HEADER_LENGTH];
        assertArrayEquals(expectedBytes, actualBytes);
    }

    private void assertArrayEquals(byte[] expectedBytes, byte[] actualBytes) {
        for (int i = 0; i < expectedBytes.length; i++) {
            assertEquals(expectedBytes[i], actualBytes[i]);
        }
    }
}