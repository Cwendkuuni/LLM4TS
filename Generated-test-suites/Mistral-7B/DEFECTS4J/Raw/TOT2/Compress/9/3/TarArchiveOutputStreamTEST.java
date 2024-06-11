import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveOutputStream;
import org.apache.commons.compress.utils.IOUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class TarArchiveOutputStreamTest {

    private OutputStream outputStream;
    private TarArchiveOutputStream tarOutputStream;

    @Before
    public void setUp() throws IOException {
        outputStream = new ByteArrayOutputStream();
        tarOutputStream = new TarArchiveOutputStream(outputStream, 512, 512);
    }

    @After
    public void tearDown() throws IOException {
        tarOutputStream.finish();
        tarOutputStream.close();
        IOUtils.closeQuietly(outputStream);
    }

    @Test
    public void testConstructor() throws IOException {
        TarArchiveOutputStream tarOutputStream = new TarArchiveOutputStream(new ByteArrayOutputStream());
        assertNotNull(tarOutputStream);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorNullOutputStream() throws IOException {
        new TarArchiveOutputStream(null);
    }

    @Test
    public void testSetLongFileMode() throws IOException {
        tarOutputStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_TRUNCATE);
        TarArchiveEntry entry = new TarArchiveEntry("test.txt");
        byte[] testData = "Test data".getBytes(StandardCharsets.UTF_8);

        tarOutputStream.putArchiveEntry(entry);
        tarOutputStream.write(testData, 0, testData.length);
        tarOutputStream.closeArchiveEntry();

        byte[] archiveData = ((ByteArrayOutputStream) outputStream).toByteArray();
        int longFileNameLength = "test.txt".getBytes(StandardCharsets.UTF_8).length;

        // Long file name is truncated
        assertFalse(new String(archiveData, 0, archiveData.length - longFileNameLength).contains("test.txt"));

        tarOutputStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_GNU);

        TarArchiveEntry longLinkEntry = new TarArchiveEntry("test.txt", "link.txt");

        tarOutputStream.putArchiveEntry(longLinkEntry);
        tarOutputStream.write(Long.toString(longLinkEntry.getSize()).getBytes(StandardCharsets.UTF_8));
        tarOutputStream.write(0);
        tarOutputStream.write("link.txt".getBytes(StandardCharsets.UTF_8));
        tarOutputStream.write(testData, 0, testData.length);
        tarOutputStream.closeArchiveEntry();

        // Long file name is preserved
        assertTrue(new String(archiveData, 0, archiveData.length - longFileNameLength).contains("link.txt"));
    }

    @Test
    public void testFinish() throws IOException {
        TarArchiveEntry entry = new TarArchiveEntry("test.txt");
        byte[] testData = "Test data".getBytes(StandardCharsets.UTF_8);

        tarOutputStream.putArchiveEntry(entry);
        tarOutputStream.write(testData, 0, testData.length);
        tarOutputStream.closeArchiveEntry();

        tarOutputStream.finish();

        byte[] archiveData = ((ByteArrayOutputStream) outputStream).toByteArray();

        int recordSize = tarOutputStream.getRecordSize();
        int eofRecordLength = recordSize * 2;

        byte[] expectedEofRecord = new byte[eofRecordLength];
        for (int i = 0; i < eofRecordLength; ++i) {
            expectedEofRecord[i] = 0;
        }

        byte[] expectedEntryData = new byte[recordSize + testData.length];
        System.arraycopy(entry.getHeaderRecord(), 0, expectedEntryData, 0, recordSize);
        System.arraycopy(testData, 0, expectedEntryData, recordSize, testData.length);

        byte[] expectedArchiveData = new byte[archiveData.length - eofRecordLength + expectedEntryData.length];
        System.arraycopy(expectedEofRecord, 0, expectedArchiveData, 0, eofRecordLength);
        System.arraycopy(expectedEntryData, 0, expectedArchiveData, eofRecordLength, expectedEntryData.length);

        assertArrayEquals(expectedArchiveData, archiveData);
    }

    @Test(expected = IOException.class)
    public void testFinishAlreadyFinished() throws IOException {
        tarOutputStream.finish();
        tarOutputStream.finish();
    }

    @Test(expected = IOException.class)
    public void testFinishWithUnclosedEntry() throws IOException {
        TarArchiveEntry entry = new TarArchiveEntry("test.txt");
        byte[] testData = "Test data".getBytes(StandardCharsets.UTF_8);

        tarOutputStream.putArchiveEntry(entry);
        tarOutputStream.write(testData, 0, testData.length);

        tarOutputStream.finish();

        tarOutputStream.closeArchiveEntry();
    }

    @Test(expected = IOException.class)
    public void testCloseWithUnfinishedArchive() throws IOException {
        tarOutputStream.close();
        tarOutputStream.write(new byte[1]);
    }

    @Test
    public void testClose() throws IOException {
        TarArchiveEntry entry = new TarArchiveEntry("test.txt");
        byte[] testData = "Test data".getBytes(StandardCharsets.UTF_8);

        tarOutputStream.putArchiveEntry(entry);
        tarOutputStream.write(testData, 0, testData.length);
        tarOutputStream.closeArchiveEntry();

        tarOutputStream.close();

        byte[] archiveData = ((ByteArrayOutputStream) outputStream).toByteArray();

        int recordSize = tarOutputStream.getRecordSize();
        int eofRecordLength = recordSize * 2;

        byte[] expectedEofRecord = new byte[eofRecordLength];
        for (int i = 0; i < eofRecordLength; ++i) {
            expectedEofRecord[i] = 0;
        }

        byte[] expectedEntryData = new byte[recordSize + testData.length];
        System.arraycopy(entry.getHeaderRecord(), 0, expectedEntryData, 0, recordSize);
        System.arraycopy(testData, 0, expectedEntryData, recordSize, testData.length);

        byte[] expectedArchiveData = new byte[archiveData.length - eofRecordLength + expectedEntryData.length];
        System.arraycopy(expectedEofRecord, 0, expectedArchiveData, 0, eofRecordLength);
        System.arraycopy(expectedEntryData, 0, expectedArchiveData, eofRecordLength, expectedEntryData.length);

        assertArrayEquals(expectedArchiveData, archiveData);
    }

    @Test
    public void testWrite() throws IOException {
        TarArchiveEntry entry = new TarArchiveEntry("test.txt");
        byte[] testData = "Test data".getBytes(StandardCharsets.UTF_8);

        tarOutputStream.putArchiveEntry(entry);
        tarOutputStream.write(testData, 0, testData.length);
        tarOutputStream.closeArchiveEntry();

        byte[] archiveData = ((ByteArrayOutputStream) outputStream).toByteArray();
        byte[] expectedEntryData = new byte[recordSize + testData.length];
        System.arraycopy(testData, 0, expectedEntryData, recordSize, testData.length);
        System.arraycopy(entry.getHeaderRecord(), 0, expectedEntryData, 0, recordSize);

        assertArrayEquals(expectedEntryData, new ArraySection(archiveData, 0, archiveData.length - testData.length));
    }

    @Test
    public void testWriteWithUnclosedEntry() throws IOException {
        TarArchiveEntry entry = new TarArchiveEntry("test.txt");
        byte[] testData = "Test data".getBytes(StandardCharsets.UTF_8);

        tarOutputStream.putArchiveEntry(entry);
        tarOutputStream.write(testData, 0, testData.length);

        byte[] archiveData = ((ByteArrayOutputStream) outputStream).toByteArray();

        assertEquals(testData.length, archiveData.length % recordSize);

        byte[] expectedEntryData = new byte[recordSize + testData.length];
        System.arraycopy(testData, 0, expectedEntryData, recordSize, testData.length);
        System.arraycopy(entry.getHeaderRecord(), 0, expectedEntryData, 0, recordSize);

        if (expectedEntryData.length % recordSize != 0) {
            int paddingSize = recordSize - (expectedEntryData.length % recordSize);
            byte[] padding = new byte[paddingSize];
            System.arraycopy(padding, 0, expectedEntryData, expectedEntryData.length, paddingSize);
        }

        assertArrayEquals(expectedEntryData, new ArraySection(archiveData, 0, archiveData.length));
    }

    @Test
    public void testFlush() throws IOException {
        TarArchiveEntry entry = new TarArchiveEntry("test.txt");
        byte[] testData = "Test data".getBytes(StandardCharsets.UTF_8);

        tarOutputStream.putArchiveEntry(entry);
        tarOutputStream.write(testData, 0, testData.length);

        tarOutputStream.flush();

        byte[] archiveData = ((ByteArrayOutputStream) outputStream).toByteArray();
        byte[] expectedEntryData = new byte[recordSize + testData.length];
        System.arraycopy(testData, 0, expectedEntryData, recordSize, testData.length);
        System.arraycopy(entry.getHeaderRecord(), 0, expectedEntryData, 0, recordSize);

        if (expectedEntryData.length % recordSize != 0) {
            int paddingSize = recordSize - (expectedEntryData.length % recordSize);
            byte[] padding = new byte[paddingSize];
            System.arraycopy(padding, 0, expectedEntryData, expectedEntryData.length, paddingSize);
        }

        assertArrayEquals(expectedEntryData, new ArraySection(archiveData, 0, archiveData.length));
    }

    @Test
    public void testCreateArchiveEntry() throws IOException {
        File file = new File("test.txt");
        byte[] testData = "Test data".getBytes(StandardCharsets.UTF_8);

        TarArchiveEntry entry = tarOutputStream.createArchiveEntry(file, "test.txt");

        tarOutputStream.putArchiveEntry(entry);
        tarOutputStream.write(testData, 0, testData.length);
        tarOutputStream.closeArchiveEntry();

        byte[] archiveData = ((ByteArrayOutputStream) outputStream).toByteArray();
        byte[] expectedEntryData = new byte[recordSize + testData.length];
        System.arraycopy(testData, 0, expectedEntryData, recordSize, testData.length);
        System.arraycopy(entry.getHeaderRecord(), 0, expectedEntryData, 0, recordSize);

        if (expectedEntryData.length % recordSize != 0) {
            int paddingSize = recordSize - (expectedEntryData.length % recordSize);
            byte[] padding = new byte[paddingSize];
            System.arraycopy(padding, 0, expectedEntryData, expectedEntryData.length, paddingSize);
        }

        assertArrayEquals(expectedEntryData, new ArraySection(archiveData, 0, archiveData.length));

        IOUtils.deleteQuietly(file);
    }

    private static byte[] newArraySection(byte[] array, int offset, int length) {
        byte[] result = new byte[length];
        System.arraycopy(array, offset, result, 0, length);
        return result;
    }

    private static byte[] newArraySection(byte[] array) {
        return newArraySection(array, 0, array.length);
    }

    private static byte[] newArraySection(byte[] array, int offset) {
        return newArraySection(array, offset, array.length - offset);
    }

    private static class ArraySection {
        private final byte[] array;
        private final int offset;
        private final int length;

        public ArraySection(byte[] array, int offset, int length) {
            this.array = array;
            this.offset = offset;
            this.length = length;
        }

        public byte[] getBytes() {
            return newArraySection(array, offset, length);
        }

        public void assertArrayEquals(byte[] expected) {
            assertEquals(expected.length, length);
            assertArrayEquals(expected, newArraySection(array, offset, length));
        }
    }
}