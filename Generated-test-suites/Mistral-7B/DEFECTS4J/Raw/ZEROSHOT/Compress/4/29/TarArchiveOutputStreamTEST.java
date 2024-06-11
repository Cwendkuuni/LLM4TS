import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarConstants;
import org.apache.commons.compress.archivers.tar.TarBuffer;
import org.apache.commons.compress.archivers.tar.TarArchiveOutputStream;
import org.apache.commons.compress.utils.IOUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

/**
 * TarArchiveOutputStreamTest.
 */
public class TarArchiveOutputStreamTest {

    private OutputStream outputStream;
    private TarArchiveOutputStream tarArchiveOutputStream;

    @Before
    public void setUp() throws Exception {
        outputStream = new ByteArrayOutputStream();
        tarArchiveOutputStream = new TarArchiveOutputStream(outputStream);
    }

    @After
    public void tearDown() throws Exception {
        tarArchiveOutputStream.close();
        outputStream.close();
    }

    @Test
    public void testConstructorWithOutputStream() {
        tarArchiveOutputStream = new TarArchiveOutputStream(outputStream);
        assertSame(outputStream, tarArchiveOutputStream.getOutputStream());
    }

    @Test
    public void testConstructorWithOutputStreamBlockSize() {
        tarArchiveOutputStream = new TarArchiveOutputStream(outputStream, TarBuffer.DEFAULT_BLKSIZE);
        assertSame(outputStream, tarArchiveOutputStream.getOutputStream());
    }

    @Test
    public void testConstructorWithOutputStreamBlockSizeRecordSize() {
        tarArchiveOutputStream = new TarArchiveOutputStream(outputStream, TarBuffer.DEFAULT_BLKSIZE, TarBuffer.DEFAULT_RCDSIZE);
        assertSame(outputStream, tarArchiveOutputStream.getOutputStream());
    }

    @Test(expected = IllegalStateException.class)
    public void testConstructorWithNullOutputStream() {
        new TarArchiveOutputStream(null);
    }

    @Test
    public void testSetLongFileMode() {
        tarArchiveOutputStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_TRUNCATE);
        assertEquals(TarArchiveOutputStream.LONGFILE_TRUNCATE, tarArchiveOutputStream.getLongFileMode());
    }

    @Test
    public void testFinish() throws IOException {
        ArchiveEntry archiveEntry = createTestArchiveEntry();
        tarArchiveOutputStream.putArchiveEntry(archiveEntry);
        tarArchiveOutputStream.finish();
        ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
        TarArchiveInputStream tarArchiveInputStream = new TarArchiveInputStream(inputStream);
        assertEquals(archiveEntry.getName(), tarArchiveInputStream.getNextEntry().getName());
        tarArchiveInputStream.close();
    }

    @Test(expected = IOException.class)
    public void testFinishWithUnclosedEntry() throws IOException {
        ArchiveEntry archiveEntry = createTestArchiveEntry();
        tarArchiveOutputStream.putArchiveEntry(archiveEntry);
        tarArchiveOutputStream.write(new byte[10]);
        tarArchiveOutputStream.finish();
    }

    @Test
    public void testClose() throws IOException {
        ArchiveEntry archiveEntry = createTestArchiveEntry();
        tarArchiveOutputStream.putArchiveEntry(archiveEntry);
        tarArchiveOutputStream.close();
        ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
        TarArchiveInputStream tarArchiveInputStream = new TarArchiveInputStream(inputStream);
        assertEquals(archiveEntry.getName(), tarArchiveInputStream.getNextEntry().getName());
        tarArchiveInputStream.close();
    }

    @Test(expected = IOException.class)
    public void testCloseWithUnclosedEntry() throws IOException {
        ArchiveEntry archiveEntry = createTestArchiveEntry();
        tarArchiveOutputStream.putArchiveEntry(archiveEntry);
        tarArchiveOutputStream.write(new byte[10]);
        tarArchiveOutputStream.closeArchiveEntry();
        tarArchiveOutputStream.close();
    }

    @Test
    public void testGetRecordSize() {
        assertEquals(TarBuffer.DEFAULT_RCDSIZE, tarArchiveOutputStream.getRecordSize());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testPutArchiveEntryWithNullArchiveEntry() {
        tarArchiveOutputStream.putArchiveEntry(null);
    }

    @Test(expected = ClassCastException.class)
    public void testPutArchiveEntryWithNonTarArchiveEntry() {
        ArchiveEntry archiveEntry = new ArchiveEntry("test.txt");
        tarArchiveOutputStream.putArchiveEntry(archiveEntry);
    }

    @Test
    public void testPutArchiveEntryAndCloseArchiveEntry() throws IOException {
        ArchiveEntry archiveEntry = createTestArchiveEntry();
        tarArchiveOutputStream.putArchiveEntry(archiveEntry);
        tarArchiveOutputStream.closeArchiveEntry();
        ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
        TarArchiveInputStream tarArchiveInputStream = new TarArchiveInputStream(inputStream);
        assertEquals(archiveEntry.getName(), tarArchiveInputStream.getNextEntry().getName());
        tarArchiveInputStream.close();
    }

    @Test
    public void testWriteAndCloseArchiveEntry() throws IOException {
        ArchiveEntry archiveEntry = createTestArchiveEntry();
        tarArchiveOutputStream.putArchiveEntry(archiveEntry);
        tarArchiveOutputStream.write(new byte[10]);
        tarArchiveOutputStream.closeArchiveEntry();
        ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
        TarArchiveInputStream tarArchiveInputStream = new TarArchiveInputStream(inputStream);
        assertEquals(archiveEntry.getName(), tarArchiveInputStream.getNextEntry().getName());
        byte[] expected = new byte[archiveEntry.getSize() + 10];
        System.arraycopy(tarArchiveInputStream.readAllBytes(), 0, expected, 0, expected.length);
        assertArrayEquals(expected, tarArchiveInputStream.readAllBytes());
        tarArchiveInputStream.close();
    }

    @Test
    public void testWriteAndCloseArchiveEntryWithLongFileNames() throws IOException {
        String longFileName = "testLongFileNameWithLengthGreaterThanNameLength";
        int longFileNameLength = longFileName.length();
        ArchiveEntry archiveEntry = new TarArchiveEntry(new File("test.txt"), longFileName);
        tarArchiveOutputStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_GNU);
        tarArchiveOutputStream.putArchiveEntry(archiveEntry);
        byte[] longFileNameBytes = longFileName.getBytes(StandardCharsets.UTF_8);
        tarArchiveOutputStream.write(longFileNameBytes, 0, longFileNameBytes.length);
        tarArchiveOutputStream.write(new byte[1]);
        tarArchiveOutputStream.closeArchiveEntry();
        ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
        TarArchiveInputStream tarArchiveInputStream = new TarArchiveInputStream(inputStream);
        assertEquals(archiveEntry.getName(), tarArchiveInputStream.getNextEntry().getName());
        byte[] expected = new byte[archiveEntry.getSize() + longFileNameBytes.length + 1];
        System.arraycopy(tarArchiveInputStream.readAllBytes(), 0, expected, 0, expected.length);
        assertArrayEquals(expected, tarArchiveInputStream.readAllBytes());
        tarArchiveInputStream.close();
    }

    @Test
    public void testWrite() throws IOException {
        ArchiveEntry archiveEntry = createTestArchiveEntry();
        tarArchiveOutputStream.putArchiveEntry(archiveEntry);
        byte[] data = new byte[10];
        tarArchiveOutputStream.write(data, 0, data.length);
        byte[] expected = new byte[archiveEntry.getSize() + data.length];
        System.arraycopy(tarArchiveOutputStream.getOutputStream().readAllBytes(), 0, expected, 0, expected.length);
        assertArrayEquals(expected, tarArchiveOutputStream.getOutputStream().readAllBytes());
        tarArchiveOutputStream.closeArchiveEntry();
    }

    @Test(expected = IOException.class)
    public void testWriteWithUnclosedEntry() throws IOException {
        ArchiveEntry archiveEntry = createTestArchiveEntry();
        tarArchiveOutputStream.putArchiveEntry(archiveEntry);
        tarArchiveOutputStream.write(new byte[10]);
        tarArchiveOutputStream.write(new byte[1]);
        tarArchiveOutputStream.close();
    }

    private ArchiveEntry createTestArchiveEntry() {
        File testFile = new File("test.txt");
        return new TarArchiveEntry(testFile, "test.txt");
    }

    private void assertArrayEquals(byte[] expected, byte[] actual) {
        assertEquals(expected.length, actual.length);
        for (int i = 0; i < expected.length; i++) {
            assertEquals(expected[i], actual[i]);
        }
    }
}