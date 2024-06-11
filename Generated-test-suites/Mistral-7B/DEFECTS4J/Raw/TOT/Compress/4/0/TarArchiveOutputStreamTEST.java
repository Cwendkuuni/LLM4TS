import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveOutputStream;
import org.apache.commons.compress.archivers.tar.TarConstants;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

public class TarArchiveOutputStreamTest {

    private static final int RECORD_SIZE = 512;
    private static final int BLOCK_SIZE = 512;

    private File tempFile;
    private OutputStream out;
    private TarArchiveOutputStream tarOut;

    @Before
    public void setUp() throws Exception {
        tempFile = Files.createTempFile("junit-", ".tar").toFile();
        out = new FileOutputStream(tempFile);
        tarOut = new TarArchiveOutputStream(out, BLOCK_SIZE, RECORD_SIZE);
    }

    @After
    public void tearDown() throws Exception {
        tarOut.close();
        out.close();
        Files.deleteIfExists(tempFile.toPath());
    }

    @Test
    public void testConstructor() {
        TarArchiveOutputStream tao = new TarArchiveOutputStream(new ByteArrayOutputStream());
        assertNull(tao.getRecordSize());
        assertNull(tao.getBuffer());
        assertNull(tao.getAssemBuf());
        assertNull(tao.getRecordBuf());
        assertNull(tao.getAssemLen());
        assertNull(tao.getBuffer());
        assertNull(tao.getLongFileMode());
        assertNull(tao.getOut());
        assertFalse(tao.isClosed());
        assertFalse(tao.hasUnclosedEntry());

        tao = new TarArchiveOutputStream(new ByteArrayOutputStream(), RECORD_SIZE);
        assertNotNull(tao.getRecordSize());
        assertNotNull(tao.getBuffer());
        assertNotNull(tao.getAssemBuf());
        assertNotNull(tao.getRecordBuf());
        assertNull(tao.getAssemLen());
        assertNull(tao.getBuffer());
        assertNull(tao.getLongFileMode());
        assertNull(tao.getOut());
        assertFalse(tao.isClosed());
        assertFalse(tao.hasUnclosedEntry());
    }

    @Test
    public void testSetLongFileMode() {
        tarOut.setLongFileMode(TarArchiveOutputStream.LONGFILE_GNU);
        assertEquals(TarArchiveOutputStream.LONGFILE_GNU, tarOut.getLongFileMode());

        tarOut.setLongFileMode(TarArchiveOutputStream.LONGFILE_TRUNCATE);
        assertEquals(TarArchiveOutputStream.LONGFILE_TRUNCATE, tarOut.getLongFileMode());

        tarOut.setLongFileMode(TarArchiveOutputStream.LONGFILE_ERROR);
        assertEquals(TarArchiveOutputStream.LONGFILE_ERROR, tarOut.getLongFileMode());

        assertThrows(IllegalArgumentException.class, () -> tarOut.setLongFileMode(4));
    }

    @Test
    public void testFinish() throws IOException {
        TarArchiveEntry entry = new TarArchiveEntry("test.txt");
        tarOut.putArchiveEntry(entry);
        tarOut.finish();

        byte[] expectedBytes = new byte[2 * RECORD_SIZE];
        for (int i = 0; i < expectedBytes.length; i++) {
            expectedBytes[i] = (byte) 0;
        }
        byte[] readBytes = new byte[expectedBytes.length];
        Files.readAllBytes(tempFile.toPath(), readBytes);
        for (int i = 0; i < expectedBytes.length; i++) {
            assertEquals(expectedBytes[i], readBytes[i]);
        }

        tarOut.close();
    }

    @Test
    public void testClose() throws Exception {
        TarArchiveEntry entry = new TarArchiveEntry("test.txt");
        tarOut.putArchiveEntry(entry);
        tarOut.write(new byte[]{'a', 'b', 'c'}, 0, 3);
        tarOut.closeArchiveEntry();
        tarOut.close();

        byte[] expectedBytes = new byte[entry.getSize() + 1 + RECORD_SIZE];
        System.arraycopy(entry.getHeader(TarConstants.STANDARD_HEADER_FORMAT).getHeader(), 0, expectedBytes, 0,
                entry.getHeader(TarConstants.STANDARD_HEADER_FORMAT).getHeader().length);
        expectedBytes[expectedBytes.length - 1] = 0;
        byte[] readBytes = new byte[expectedBytes.length];
        Files.readAllBytes(tempFile.toPath(), readBytes);
        for (int i = 0; i < expectedBytes.length; i++) {
            assertEquals(expectedBytes[i], readBytes[i]);
        }
        Files.deleteIfExists(tempFile.toPath());

        entry = new TarArchiveEntry("test2.txt");
        tarOut.putArchiveEntry(entry);
        tarOut.write(new byte[]{'d', 'e', 'f'}, 0, 3);
        tarOut.closeArchiveEntry();
        tarOut.close();

        expectedBytes = new byte[entry.getSize() + 1 + RECORD_SIZE];
        System.arraycopy(entry.getHeader(TarConstants.STANDARD_HEADER_FORMAT).getHeader(), 0, expectedBytes, 0,
                entry.getHeader(TarConstants.STANDARD_HEADER_FORMAT).getHeader().length);
        expectedBytes[expectedBytes.length - 1] = 0;
        readBytes = new byte[expectedBytes.length];
        Files.readAllBytes(tempFile.toPath(), readBytes);
        for (int i = 0; i < expectedBytes.length; i++) {
            assertEquals(expectedBytes[i], readBytes[i]);
        }
        Files.deleteIfExists(tempFile.toPath());
    }

    @Test
    public void testCloseNoEntry() throws Exception {
        tarOut.finish();
        tarOut.close();

        assertTrue(Files.isEmpty(tempFile.toPath()));
    }

    @Test
    public void testWrite() throws IOException {
        TarArchiveEntry entry = new TarArchiveEntry("test.txt");
        tarOut.putArchiveEntry(entry);
        tarOut.write(new byte[]{'h', 'e', 'l', 'l', 'o'}, 0, 5);
        tarOut.closeArchiveEntry();
        tarOut.finish();
        tarOut.close();

        byte[] expectedBytes = new byte[entry.getSize() + 1 + RECORD_SIZE];
        System.arraycopy(entry.getHeader(TarConstants.STANDARD_HEADER_FORMAT).getHeader(), 0, expectedBytes, 0,
                entry.getHeader(TarConstants.STANDARD_HEADER_FORMAT).getHeader().length);
        expectedBytes[expectedBytes.length - 1] = 0;
        byte[] readBytes = new byte[expectedBytes.length];
        Files.readAllBytes(tempFile.toPath(), readBytes);
        for (int i = 0; i < expectedBytes.length; i++) {
            assertEquals(expectedBytes[i], readBytes[i]);
        }
        Files.deleteIfExists(tempFile.toPath());

        assertThrows(IllegalStateException.class, () -> tarOut.write(new byte[1], 0, 1));
    }

    @Test
    public void testWriteNoEntry() throws IOException {
        tarOut.finish();
        tarOut.close();
        tarOut = new TarArchiveOutputStream(new FileOutputStream(tempFile), BLOCK_SIZE, RECORD_SIZE);

        byte[] data = "This is a test".getBytes(StandardCharsets.UTF_8);
        TarArchiveEntry entry = new TarArchiveEntry("test.txt");
        tarOut.putArchiveEntry(entry);
        tarOut.write(data, 0, data.length);
        tarOut.closeArchiveEntry();
        tarOut.finish();
        tarOut.close();

        byte[] expectedBytes = new byte[entry.getSize() + data.length + 1 + 2 * RECORD_SIZE];
        System.arraycopy(entry.getHeader(TarConstants.STANDARD_HEADER_FORMAT).getHeader(), 0, expectedBytes, 0,
                entry.getHeader(TarConstants.STANDARD_HEADER_FORMAT).getHeader().length);
        System.arraycopy(data, 0, expectedBytes, entry.getSize(), data.length);
        expectedBytes[expectedBytes.length - 1] = 0;
        byte[] readBytes = new byte[expectedBytes.length];
        Files.readAllBytes(tempFile.toPath(), readBytes);
        for (int i = 0; i < expectedBytes.length; i++) {
            assertEquals(expectedBytes[i], readBytes[i]);
        }
        Files.deleteIfExists(tempFile.toPath());

        assertThrows(IllegalStateException.class, () -> tarOut.write(new byte[1], 0, 1));
    }

    @Test
    public void testPutArchiveEntryWithInvalidFileName() throws IOException {
        TarArchiveEntry entry = new TarArchiveEntry("test.txt");
        tarOut.putArchiveEntry(entry);
        assertThrows(IOException.class, () -> tarOut.putArchiveEntry(entry));
    }

    @Test
    public void testCloseArchiveEntryWithoutPuttingIt() throws IOException {
        tarOut.closeArchiveEntry();
        assertNull(tarOut.getCurrName());
        assertFalse(tarOut.hasUnclosedEntry());
    }

    @Test
    public void testPutArchiveEntryWithEmptyName() throws IOException {
        TarArchiveEntry entry = new TarArchiveEntry("", "test.txt");
        tarOut.putArchiveEntry(entry);
        assertThrows(NullPointerException.class, () -> entry.getName());
        assertEquals("", tarOut.getCurrName());
        assertFalse(tarOut.hasUnclosedEntry());
    }

    @Test
    public void testPutArchiveEntryWithLargeFileName() throws IOException {
        String fileName = new String(new char[TarConstants.NAMELEN + 1]);
        TarArchiveEntry entry = new TarArchiveEntry(fileName, "test.txt");
        tarOut.setLongFileMode(TarArchiveOutputStream.LONGFILE_GNU);
        tarOut.putArchiveEntry(entry);
        assertEquals(fileName, tarOut.getCurrName());
        assertFalse(tarOut.hasUnclosedEntry());
    }

    @Test
    public void testPutArchiveEntryWithLargeFileNameAndTruncateMode() throws IOException {
        String fileName = new String(new char[TarConstants.NAMELEN + 1]);
        TarArchiveEntry entry = new TarArchiveEntry(fileName, "test.txt");
        tarOut.setLongFileMode(TarArchiveOutputStream.LONGFILE_TRUNCATE);
        tarOut.putArchiveEntry(entry);
        assertEquals(fileName.substring(0, TarConstants.NAMELEN), tarOut.getCurrName());
        assertFalse(tarOut.hasUnclosedEntry());
    }

    @Test
    public void testPutArchiveEntryWithLargeFileNameAndErrorMode() throws IOException {
        String fileName = new String(new char[TarConstants.NAMELEN + 1]);
        TarArchiveEntry entry = new TarArchiveEntry(fileName, "test.txt");
        tarOut.setLongFileMode(TarArchiveOutputStream.LONGFILE_ERROR);
        assertThrows(RuntimeException.class, () -> tarOut.putArchiveEntry(entry));
        assertNull(tarOut.getCurrName());
        assertFalse(tarOut.hasUnclosedEntry());
    }
}