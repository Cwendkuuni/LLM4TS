import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarConstants;
import org.apache.commons.compress.utils.ArchiveUtils;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

public class TarArchiveOutputStreamTest {

    private OutputStream outputStream;
    private TarArchiveOutputStream tarArchiveOutputStream;

    @Before
    public void setUp() throws IOException {
        outputStream = new ByteArrayOutputStream();
        tarArchiveOutputStream = new TarArchiveOutputStream(outputStream);
    }

    @After
    public void tearDown() throws IOException {
        tarArchiveOutputStream.close();
    }

    @Test
    public void testConstructor() {
        assertNotNull(tarArchiveOutputStream);
    }

    @Test
    public void testSetLongFileMode() {
        tarArchiveOutputStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_TRUNCATE);
        assertEquals(TarArchiveOutputStream.LONGFILE_TRUNCATE, tarArchiveOutputStream.getLongFileMode());
    }

    @Test
    public void testFinish() throws IOException {
        tarArchiveOutputStream.finish();
        assertTrue(tarArchiveOutputStream.isFinished());

        ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
        TarArchiveEntry entry = new TarArchiveEntry("test.txt");
        ArchiveOutputStream archiveOutputStream = new TarArchiveOutputStream(inputStream);
        archiveOutputStream.putNextEntry(entry);
        archiveOutputStream.write(new byte[10]);
        archiveOutputStream.closeArchiveEntry();
        archiveOutputStream.finish();

        byte[] expectedMagicNumbers = new byte[TarConstants.MAGIC_LEN];
        inputStream.read(expectedMagicNumbers);
        assertArrayEquals(TarConstants.MAGIC, expectedMagicNumbers);

        byte[] expectedEOFRecord = new byte[2 * TarConstants.BLOCKSIZE];
        inputStream.read(expectedEOFRecord);
        assertArrayEquals(new byte[2 * TarConstants.BLOCKSIZE], expectedEOFRecord);

        inputStream.close();
    }

    @Test
    public void testClose() throws IOException {
        tarArchiveOutputStream.close();
        assertTrue(tarArchiveOutputStream.isClosed());
    }

    @Test
    public void testGetRecordSize() {
        assertEquals(TarConstants.BLOCKSIZE, tarArchiveOutputStream.getRecordSize());
    }

    @Test
    public void testPutArchiveEntry() throws IOException {
        TarArchiveEntry entry = new TarArchiveEntry("test.txt");
        tarArchiveOutputStream.putArchiveEntry(entry);

        assertNotNull(entry);
        assertEquals("test.txt", entry.getName());
        assertNull(entry.getSize());
        assertFalse(entry.isDirectory());
    }

    @Test
    public void testPutArchiveEntryLongFileName() throws IOException {
        String longFileName = "test_" + new String(new char[TarConstants.NAMELEN + 1]) + ".txt";
        TarArchiveEntry entry = new TarArchiveEntry(longFileName);
        tarArchiveOutputStream.putArchiveEntry(entry);

        assertNotNull(entry);
        assertEquals(longFileName, entry.getName());
        assertNull(entry.getSize());
        assertFalse(entry.isDirectory());

        if (tarArchiveOutputStream.getLongFileMode() != TarArchiveOutputStream.LONGFILE_GNU) {
            // Check if the long file name is truncated
            assertEquals(longFileName.substring(0, TarConstants.NAMELEN),
                    ArchiveUtils.toAsciiString(entry.getHeaderRecord().getName()));
        }
    }

    @Test
    public void testPutArchiveEntryWithSize() throws IOException {
        TarArchiveEntry entry = new TarArchiveEntry("test.txt");
        entry.setSize(5);
        tarArchiveOutputStream.putArchiveEntry(entry);

        assertNotNull(entry);
        assertEquals("test.txt", entry.getName());
        assertEquals(5, entry.getSize());
        assertFalse(entry.isDirectory());
    }

    @Test(expected = IOException.class)
    public void testPutArchiveEntryWithDirectoryEntry() throws IOException {
        TarArchiveEntry entry = new TarArchiveEntry("test_dir");
        entry.setSize(5);
        tarArchiveOutputStream.putArchiveEntry(entry);

        // This should throw an IOException since the entry is a directory
        tarArchiveOutputStream.write(new byte[5]);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testPutArchiveEntryWithNullEntry() throws IOException {
        tarArchiveOutputStream.putArchiveEntry(null);
    }

    @Test(expected = IOException.class)
    public void testPutArchiveEntryWithNullFileName() throws IOException {
        TarArchiveEntry entry = new TarArchiveEntry(null);
        tarArchiveOutputStream.putArchiveEntry(entry);
    }

    @Test(expected = ClassCastException.class)
    public void testPutArchiveEntryWithNonTarEntry() throws IOException {
        ArchiveEntry nonTarEntry = new File("test.txt");
        tarArchiveOutputStream.putArchiveEntry(nonTarEntry);
    }

    @Test
    public void testCloseArchiveEntry() throws IOException {
        TarArchiveEntry entry = new TarArchiveEntry("test.txt");
        tarArchiveOutputStream.putArchiveEntry(entry);
        tarArchiveOutputStream.write(new byte[5]);
        tarArchiveOutputStream.closeArchiveEntry();

        assertFalse(tarArchiveOutputStream.haveUnclosedEntry());
    }

    @Test(expected = IOException.class)
    public void testCloseArchiveEntryWithoutPutArchiveEntry() throws IOException {
        tarArchiveOutputStream.closeArchiveEntry();
    }

    @Test
    public void testWrite() throws IOException {
        TarArchiveEntry entry = new TarArchiveEntry("test.txt");
        tarArchiveOutputStream.putArchiveEntry(entry);

        byte[] data = new byte[5];
        tarArchiveOutputStream.write(data);

        byte[] expectedData = new byte[5];
        System.arraycopy(data, 0, expectedData, 0, 5);

        byte[] result = outputStream.toByteArray();
        byte[] inputData = new byte[result.length];
        System.arraycopy(result, 0, inputData, 0, result.length);

        ByteArrayInputStream inputStream = new ByteArrayInputStream(inputData);
        TarArchiveEntry entryFromInputStream = new TarArchiveEntry(inputStream);

        byte[] readData = new byte[5];
        int bytesRead = entryFromInputStream.getInputStream().read(readData);

        assertArrayEquals(expectedData, readData);
        assertEquals(bytesRead, 5);
    }

    @Test
    public void testWriteWithUnclosedEntry() throws IOException {
        TarArchiveEntry entry = new TarArchiveEntry("test.txt");
        tarArchiveOutputStream.putArchiveEntry(entry);

        byte[] data = new byte[5];
        tarArchiveOutputStream.write(data);

        tarArchiveOutputStream.closeArchiveEntry();

        tarArchiveOutputStream.write(new byte[5]);

        byte[] result = outputStream.toByteArray();
        byte[] inputData = new byte[result.length];
        System.arraycopy(result, 0, inputData, 0, result.length);

        ByteArrayInputStream inputStream = new ByteArrayInputStream(inputData);
        TarArchiveEntry entryFromInputStream = new TarArchiveEntry(inputStream);

        byte[] readData1 = new byte[5];
        int bytesRead1 = entryFromInputStream.getInputStream().read(readData1);

        assertArrayEquals(data, readData1);
        assertEquals(bytesRead1, 5);

        byte[] readData2 = new byte[5];
        int bytesRead2 = entryFromInputStream.getInputStream().read(readData2);

        assertArrayEquals(data, readData2);
        assertEquals(bytesRead2, 5);
    }

    @Test(expected = IOException.class)
    public void testWriteAfterFinish() throws IOException {
        tarArchiveOutputStream.finish();
        tarArchiveOutputStream.write(new byte[5]);
    }

    @Test(expected = IOException.class)
    public void testWriteWithoutOpenedEntry() throws IOException {
        tarArchiveOutputStream.write(new byte[5]);
    }

    @Test(expected = IOException.class)
    public void testWriteWithTooBigData() throws IOException {
        TarArchiveEntry entry = new TarArchiveEntry("test.txt");
        tarArchiveOutputStream.putArchiveEntry(entry);

        byte[] data = new byte[TarConstants.NAMELEN * 2 + 1];
        tarArchiveOutputStream.write(data);

        // Check if the long file name is truncated
        assertEquals(data.length - TarConstants.NAMELEN,
                ArchiveUtils.toAsciiString(entry.getHeaderRecord().getName()).length());
    }

    @Test(expected = IOException.class)
    public void testWriteWithClosedStream() throws IOException {
        tarArchiveOutputStream.finish();
        tarArchiveOutputStream.write(new byte[5]);
    }

    @Test(expected = IOException.class)
    public void testWriteWithNullData() throws IOException {
        TarArchiveEntry entry = new TarArchiveEntry("test.txt");
        tarArchiveOutputStream.putArchiveEntry(entry);

        tarArchiveOutputStream.write(null);
    }
}