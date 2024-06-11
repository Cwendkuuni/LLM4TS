import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveOutputStream;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

public class TarArchiveOutputStreamTest {

    private ByteArrayOutputStream outputStream;
    private TarArchiveOutputStream tarArchiveOutputStream;

    @Before
    public void setUp() {
        outputStream = new ByteArrayOutputStream();
        tarArchiveOutputStream = new TarArchiveOutputStream(outputStream);
    }

    @After
    public void tearDown() throws IOException {
        tarArchiveOutputStream.close();
    }

    @Test
    public void testConstructor_Empty() {
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(new ByteArrayOutputStream());
        tarArchiveOutputStream.close();
    }

    @Test
    public void testConstructor_BlockSize_RecordSize() {
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(new ByteArrayOutputStream(), 512, 1024);
        tarArchiveOutputStream.close();
    }

    @Test
    public void testSetLongFileMode_LONGFILE_ERROR() {
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(new ByteArrayOutputStream());
        tarArchiveOutputStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_ERROR);
        tarArchiveOutputStream.close();
    }

    @Test
    public void testSetLongFileMode_LONGFILE_TRUNCATE() {
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(new ByteArrayOutputStream());
        tarArchiveOutputStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_TRUNCATE);
        tarArchiveOutputStream.close();
    }

    @Test
    public void testSetLongFileMode_LONGFILE_GNU() {
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(new ByteArrayOutputStream());
        tarArchiveOutputStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_GNU);
        tarArchiveOutputStream.close();
    }

    @Test
    public void testFinish() throws IOException {
        TarArchiveEntry tarArchiveEntry = new TarArchiveEntry("test.txt");
        tarArchiveOutputStream.putArchiveEntry(tarArchiveEntry);
        tarArchiveOutputStream.finish();

        byte[] expectedEofRecord = new byte[2 * 512];
        for (int i = 0; i < expectedEofRecord.length; ++i) {
            expectedEofRecord[i] = (byte) 0;
        }

        byte[] actualBytes = outputStream.toByteArray();
        byte[] expectedBytes = new byte[tarArchiveOutputStream.getRecordSize() * 3];
        System.arraycopy(tarArchiveEntry.getHeaderRecord(), 0, expectedBytes, 0, tarArchiveEntry.getHeaderRecord().length);
        System.arraycopy(expectedBytes, 0, expectedBytes, 0, tarArchiveEntry.getHeaderRecord().length);
        System.arraycopy(expectedEofRecord, 0, expectedBytes, tarArchiveEntry.getHeaderRecord().length, expectedEofRecord.length);

        assertArrayEquals(expectedBytes, actualBytes);

        tarArchiveOutputStream.close();
    }

    @Test
    public void testClose() throws IOException {
        TarArchiveEntry tarArchiveEntry = new TarArchiveEntry("test.txt");
        tarArchiveOutputStream.putArchiveEntry(tarArchiveEntry);
        tarArchiveOutputStream.closeArchiveEntry();
        tarArchiveOutputStream.close();

        byte[] expectedBytes = new byte[tarArchiveEntry.getHeaderRecord().length];
        System.arraycopy(tarArchiveEntry.getHeaderRecord(), 0, expectedBytes, 0, tarArchiveEntry.getHeaderRecord().length);

        byte[] actualBytes = outputStream.toByteArray();

        assertArrayEquals(expectedBytes, actualBytes);
    }

    @Test
    public void testClose_UnclosedEntry() throws IOException {
        TarArchiveEntry tarArchiveEntry = new TarArchiveEntry("test.txt");
        tarArchiveOutputStream.putArchiveEntry(tarArchiveEntry);
        tarArchiveOutputStream.finish();

        assertThrows(IOException.class, () -> tarArchiveOutputStream.closeArchiveEntry());
        tarArchiveOutputStream.close();
    }

    @Test
    public void testPutArchiveEntry_NullArchiveEntry() {
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(new ByteArrayOutputStream());

        assertThrows(NullPointerException.class, () -> tarArchiveOutputStream.putArchiveEntry(null));
        tarArchiveOutputStream.close();
    }

    @Test
    public void testPutArchiveEntry_NonTarArchiveEntry() {
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(new ByteArrayOutputStream());

        ArchiveEntry archiveEntry = new JarFileEntry("test.java");

        assertThrows(ClassCastException.class, () -> tarArchiveOutputStream.putArchiveEntry(archiveEntry));
        tarArchiveOutputStream.close();
    }

    @Test
    public void testWrite_NullByteArray() {
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(new ByteArrayOutputStream());
        TarArchiveEntry tarArchiveEntry = new TarArchiveEntry("test.txt");

        tarArchiveOutputStream.putArchiveEntry(tarArchiveEntry);

        assertThrows(NullPointerException.class, () -> tarArchiveOutputStream.write(null, 0, 0));
        tarArchiveOutputStream.close();
    }

    @Test
    public void testWrite_NegativeOffset() {
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(new ByteArrayOutputStream());
        TarArchiveEntry tarArchiveEntry = new TarArchiveEntry("test.txt");

        tarArchiveOutputStream.putArchiveEntry(tarArchiveEntry);

        byte[] byteArray = new byte[10];

        assertThrows(IndexOutOfBoundsException.class, () -> tarArchiveOutputStream.write(byteArray, -1, 1));
        tarArchiveOutputStream.close();
    }

    @Test
    public void testWrite_NegativeLength() {
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(new ByteArrayOutputStream());
        TarArchiveEntry tarArchiveEntry = new TarArchiveEntry("test.txt");

        tarArchiveOutputStream.putArchiveEntry(tarArchiveEntry);

        byte[] byteArray = new byte[10];

        assertThrows(NegativeArraySizeException.class, () -> tarArchiveOutputStream.write(byteArray, 0, -1));
        tarArchiveOutputStream.close();
    }

    @Test
    public void testWrite_NotEnoughBytesToWrite() throws IOException {
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(new ByteArrayOutputStream());
        TarArchiveEntry tarArchiveEntry = new TarArchiveEntry("test.txt");

        tarArchiveOutputStream.putArchiveEntry(tarArchiveEntry);

        byte[] byteArray = new byte[10];

        tarArchiveOutputStream.write(byteArray, 0, byteArray.length);

        assertFalse(tarArchiveOutputStream.haveUnclosedEntry);
        tarArchiveOutputStream.close();
    }

    @Test
    public void testWrite_EnoughBytesToWrite() throws IOException {
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(new ByteArrayOutputStream());
        TarArchiveEntry tarArchiveEntry = new TarArchiveEntry("test.txt");

        tarArchiveOutputStream.putArchiveEntry(tarArchiveEntry);

        byte[] byteArray = new byte[10];

        tarArchiveOutputStream.write(byteArray, 0, byteArray.length);

        assertTrue(tarArchiveOutputStream.haveUnclosedEntry);
        tarArchiveOutputStream.closeArchiveEntry();
        tarArchiveOutputStream.close();
    }

    @Test
    public void testWrite_WriteAllBytes() throws IOException {
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(new ByteArrayOutputStream());
        TarArchiveEntry tarArchiveEntry = new TarArchiveEntry("test.txt");

        tarArchiveOutputStream.putArchiveEntry(tarArchiveEntry);

        byte[] byteArray = new byte[10];

        tarArchiveOutputStream.write(byteArray, 0, byteArray.length);
        byte[] expectedBytes = new byte[tarArchiveEntry.getSize()];

        System.arraycopy(byteArray, 0, expectedBytes, 0, byteArray.length);

        tarArchiveOutputStream.write(byteArray, 0, byteArray.length);
        tarArchiveOutputStream.write(byteArray, 0, byteArray.length);

        byte[] actualBytes = outputStream.toByteArray();

        assertArrayEquals(expectedBytes, actualBytes);

        tarArchiveOutputStream.close();
    }

    @Test
    public void testCreateArchiveEntry_NullFile() {
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(new ByteArrayOutputStream());

        assertThrows(NullPointerException.class, () -> tarArchiveOutputStream.createArchiveEntry(null, "test.txt"));
        tarArchiveOutputStream.close();
    }

    @Test
    public void testCreateArchiveEntry_EmptyFileName() {
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(new ByteArrayOutputStream());

        assertThrows(IllegalArgumentException.class, () -> tarArchiveOutputStream.createArchiveEntry(new File(""), ""));
        tarArchiveOutputStream.close();
    }

    @Test
    public void testCreateArchiveEntry_ValidFileName() {
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(new ByteArrayOutputStream());

        File testFile = new File("test.txt");

        TarArchiveEntry archivedFile = tarArchiveOutputStream.createArchiveEntry(testFile, "test.txt");

        assertEquals("test.txt", archivedFile.getName());

        tarArchiveOutputStream.close();
    }

    private void assertArrayEquals(byte[] expected, byte[] actual) {
        assertEquals(expected.length, actual.length);

        for (int i = 0; i < expected.length; ++i) {
            assertEquals(expected[i], actual[i]);
        }
    }
}