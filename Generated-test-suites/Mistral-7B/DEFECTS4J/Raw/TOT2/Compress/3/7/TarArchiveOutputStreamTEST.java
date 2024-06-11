import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveOutputStream;
import org.apache.commons.compress.archivers.tar.TarConstants;
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
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

public class TarArchiveOutputStreamTest {

    private OutputStream outputStream;
    private TarArchiveOutputStream tarArchiveOutputStream;

    @Before
    public void setUp() throws Exception {
        outputStream = new ByteArrayOutputStream();
        tarArchiveOutputStream = new TarArchiveOutputStream(outputStream, TarConstants.DEFAULT_BLKSIZE, TarConstants.DEFAULT_RCDSIZE);
    }

    @After
    public void tearDown() throws Exception {
        tarArchiveOutputStream.close();
    }

    @Test
    public void testConstructor() throws IOException {
        // Test default constructor
        tarArchiveOutputStream = new TarArchiveOutputStream(new ByteArrayOutputStream());

        // Test constructor with block size and record size
        tarArchiveOutputStream = new TarArchiveOutputStream(new ByteArrayOutputStream(), TarConstants.DEFAULT_BLKSIZE, TarConstants.DEFAULT_RCDSIZE);

        // Test constructor with custom block size and record size
        tarArchiveOutputStream = new TarArchiveOutputStream(new ByteArrayOutputStream(), 512, 512);
    }

    @Test
    public void testSetLongFileMode() throws IOException {
        tarArchiveOutputStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_TRUNCATE);
        assertFalse(tarArchiveOutputStream.isLongFileNameError());
        assertTrue(tarArchiveOutputStream.isLongFileNameTruncated());

        tarArchiveOutputStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_GNU);
        assertFalse(tarArchiveOutputStream.isLongFileNameTruncated());
        assertTrue(tarArchiveOutputStream.isLongFileNameGNU());

        tarArchiveOutputStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_ERROR);
        assertFalse(tarArchiveOutputStream.isLongFileNameTruncated());
        assertFalse(tarArchiveOutputStream.isLongFileNameGNU());
        assertTrue(tarArchiveOutputStream.isLongFileNameError());
    }

    @Test
    public void testFinish() throws IOException {
        tarArchiveOutputStream.finish();
        assertEquals(0, outputStream.size());

        tarArchiveOutputStream.putArchiveEntry(new TarArchiveEntry(new File("test.txt")));
        tarArchiveOutputStream.write("test contents".getBytes(StandardCharsets.UTF_8));
        tarArchiveOutputStream.closeArchiveEntry();
        tarArchiveOutputStream.finish();

        byte[] expectedTarRecord = new byte[TarConstants.HEADER_SIZE + 5 * TarConstants.RECORD_SIZE];
        Arrays.fill(expectedTarRecord, (byte) 0);

        byte[] eofRecord = new byte[2 * TarConstants.RECORD_SIZE];
        Arrays.fill(eofRecord, (byte) 0);

        byte[] expectedEntryData = "test contents".getBytes(StandardCharsets.UTF_8);

        byte[] tarOutput = ((ByteArrayOutputStream) outputStream).toByteArray();
        assertArrayEquals(expectedTarRecord, tarOutput, 0, expectedTarRecord.length);
        assertArrayEquals(eofRecord, Arrays.copyOfRange(tarOutput, tarOutput.length - 2 * TarConstants.RECORD_SIZE, tarOutput.length), 0, eofRecord.length);
    }

    @Test
    public void testPutArchiveEntry() throws IOException {
        String fileName = "test.txt";
        String expectedFileName = fileName.substring(0, TarConstants.NAMELEN);
        byte[] expectedEntryHeader = new TarArchiveEntry(new File(fileName)).getHeader();

        tarArchiveOutputStream.putArchiveEntry(new TarArchiveEntry(new File(fileName)));

        byte[] tarOutput = ((ByteArrayOutputStream) outputStream).toByteArray();
        byte[] record = new byte[TarConstants.HEADER_SIZE + TarConstants.RECORD_SIZE];

        System.arraycopy(expectedEntryHeader, 0, record, 0, expectedEntryHeader.length);
        assertArrayEquals(record, Arrays.copyOfRange(tarOutput, 0, TarConstants.HEADER_SIZE + TarConstants.RECORD_SIZE), 0, record.length);
    }

    @Test
    public void testCloseArchiveEntry() throws IOException {
        byte[] entryData = "test data".getBytes(StandardCharsets.UTF_8);
        tarArchiveOutputStream.putArchiveEntry(new TarArchiveEntry("test.txt"));
        tarArchiveOutputStream.write(entryData);

        tarArchiveOutputStream.closeArchiveEntry();

        byte[] tarOutput = ((ByteArrayOutputStream) outputStream).toByteArray();
        byte[] expectedRecord = new byte[TarConstants.HEADER_SIZE + TarConstants.RECORD_SIZE];
        System.arraycopy(tarArchiveOutputStream.getHeaderForCurrentEntry(), 0, expectedRecord, 0, expectedRecord.length);
        assertArrayEquals(expectedRecord, Arrays.copyOfRange(tarOutput, 0, TarConstants.HEADER_SIZE + TarConstants.RECORD_SIZE), 0, expectedRecord.length);
    }

    @Test
    public void testWrite() throws IOException {
        byte[] testData = new byte[]{'T', 'e', 's', 't'};
        ArchiveEntry archiveEntry = new TarArchiveEntry("test.txt");
        tarArchiveOutputStream.putArchiveEntry(archiveEntry);
        tarArchiveOutputStream.write(testData);
        tarArchiveOutputStream.closeArchiveEntry();

        byte[] tarOutput = ((ByteArrayOutputStream) outputStream).toByteArray();
        byte[] expectedRecord = new byte[TarConstants.HEADER_SIZE + TarConstants.RECORD_SIZE];
        System.arraycopy(tarArchiveOutputStream.getHeaderForCurrentEntry(), 0, expectedRecord, 0, expectedRecord.length);
        System.arraycopy(testData, 0, Arrays.copyOfRange(expectedRecord, TarConstants.HEADER_SIZE, TarConstants.HEADER_SIZE + testData.length), 0, testData.length);

        assertArrayEquals(expectedRecord, Arrays.copyOfRange(tarOutput, 0, TarConstants.HEADER_SIZE + TarConstants.RECORD_SIZE), 0, expectedRecord.length);
    }

    @Test
    public void testWriteWithAssembly() throws IOException {
        byte[] testData = new byte[]{'T', 'e', 's', 't', ' ', 'D', 'a', 't', 'a'};
        ArchiveEntry archiveEntry = new TarArchiveEntry("test.txt");
        tarArchiveOutputStream.putArchiveEntry(archiveEntry);

        int numBytesToWrite = testData.length;

        for (int i = 0; i < numBytesToWrite; i += TarConstants.RECORD_SIZE - (numBytesToWrite % TarConstants.RECORD_SIZE)) {
            tarArchiveOutputStream.write(Arrays.copyOfRange(testData, i, i + TarConstants.RECORD_SIZE));
        }

        byte[] tarOutput = ((ByteArrayOutputStream) outputStream).toByteArray();

        for (int i = 0; i < numBytesToWrite; i += TarConstants.RECORD_SIZE) {
            byte[] expectedRecord = new byte[TarConstants.HEADER_SIZE + TarConstants.RECORD_SIZE];
            System.arraycopy(tarArchiveOutputStream.getHeaderForCurrentEntry(), 0, expectedRecord, 0, expectedRecord.length);
            System.arraycopy(Arrays.copyOfRange(testData, i, i + TarConstants.RECORD_SIZE), Arrays.copyOfRange(expectedRecord, TarConstants.HEADER_SIZE, TarConstants.HEADER_SIZE + TarConstants.RECORD_SIZE), 0, TarConstants.RECORD_SIZE);

            assertArrayEquals(expectedRecord, Arrays.copyOfRange(tarOutput, i * TarConstants.RECORD_SIZE, (i + 1) * TarConstants.RECORD_SIZE), 0, expectedRecord.length);
        }

        tarArchiveOutputStream.closeArchiveEntry();
    }

    @Test
    public void testWriteAfterCloseArchiveEntry() throws IOException {
        ArchiveEntry archiveEntry = new TarArchiveEntry("test.txt");
        tarArchiveOutputStream.putArchiveEntry(archiveEntry);
        tarArchiveOutputStream.write("test data".getBytes(StandardCharsets.UTF_8));
        tarArchiveOutputStream.closeArchiveEntry();

        assertThrows(IOException.class, () -> tarArchiveOutputStream.write("test data 2".getBytes(StandardCharsets.UTF_8)));
    }

    @Test
    public void testWriteAfterFinish() throws IOException {
        ArchiveEntry archiveEntry = new TarArchiveEntry("test.txt");
        tarArchiveOutputStream.putArchiveEntry(archiveEntry);
        tarArchiveOutputStream.write("test data".getBytes(StandardCharsets.UTF_8));
        tarArchiveOutputStream.finish();

        assertThrows(IOException.class, () -> tarArchiveOutputStream.write("test data 2".getBytes(StandardCharsets.UTF_8)));
    }

    @Test
    public void testWriteLongFileName() throws IOException {
        String fileName = "test_long_filename_" + new String(new char[TarConstants.NAMELEN + 10]) + ".txt";
        TarArchiveEntry entry = new TarArchiveEntry(new File(fileName));

        tarArchiveOutputStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_GNU);
        tarArchiveOutputStream.putArchiveEntry(entry);

        byte[] record = new byte[TarConstants.HEADER_SIZE + TarConstants.RECORD_SIZE];
        Arrays.fill(record, (byte) 0);

        byte[] expectedRecord = new byte[TarConstants.HEADER_SIZE + TarConstants.RECORD_SIZE];
        System.arraycopy(entry.getHeader(), 0, expectedRecord, 0, expectedRecord.length);
        System.arraycopy(fileName.getBytes(StandardCharsets.UTF_8), 0, Arrays.copyOfRange(expectedRecord, TarConstants.HEADER_SIZE, TarConstants.HEADER_SIZE + fileName.length()), 0, fileName.length());

        assertEquals(expectedRecord, record);

        tarArchiveOutputStream.closeArchiveEntry();
    }

    @Test
    public void testWriteLongFileNameError() throws IOException {
        String fileName = "test_long_filename_" + new String(new char[TarConstants.NAMELEN + 10]) + ".txt";

        // Test without setting long file mode
        tarArchiveOutputStream.putArchiveEntry(new TarArchiveEntry(new File(fileName)));
        assertThrows(RuntimeException.class, () -> tarArchiveOutputStream.finish());

        // Test with setting long file mode to error
        tarArchiveOutputStream = new TarArchiveOutputStream(new ByteArrayOutputStream(), TarConstants.DEFAULT_BLKSIZE, TarConstants.DEFAULT_RCDSIZE);
        tarArchiveOutputStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_ERROR);
        tarArchiveOutputStream.putArchiveEntry(new TarArchiveEntry(new File(fileName)));
        assertThrows(RuntimeException.class, () -> tarArchiveOutputStream.finish());
    }

    @Test
    public void testWriteLongFileNameTruncate() throws IOException {
        String fileName = "test_long_filename_" + new String(new char[TarConstants.NAMELEN + 10]) + ".txt";

        // Test with setting long file mode to truncate
        tarArchiveOutputStream = new TarArchiveOutputStream(new ByteArrayOutputStream(), TarConstants.DEFAULT_BLKSIZE, TarConstants.DEFAULT_RCDSIZE);
        tarArchiveOutputStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_TRUNCATE);
        TarArchiveEntry entry = new TarArchiveEntry(new File(fileName));
        tarArchiveOutputStream.putArchiveEntry(entry);
        byte[] record = new byte[TarConstants.HEADER_SIZE + TarConstants.RECORD_SIZE];
        Arrays.fill(record, (byte) 0);

        byte[] expectedRecord = new byte[TarConstants.HEADER_SIZE + TarConstants.RECORD_SIZE];
        System.arraycopy(entry.getHeader(), 0, expectedRecord, 0, expectedRecord.length);
        Arrays.fill(Arrays.copyOfRange(expectedRecord, TarConstants.HEADER_SIZE, TarConstants.HEADER_SIZE + fileName.length()), (byte) '.'); // Replace long file name characters with dots

        assertEquals(expectedRecord, record);

        tarArchiveOutputStream.closeArchiveEntry();
    }
}