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
import java.util.Arrays;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

public class TarArchiveOutputStreamTest {

    private ByteArrayOutputStream outputStream;
    private TarArchiveOutputStream tarOutputStream;

    @Before
    public void setUp() throws Exception {
        outputStream = new ByteArrayOutputStream();
        tarOutputStream = new TarArchiveOutputStream(outputStream);
    }

    @After
    public void tearDown() throws Exception {
        tarOutputStream.close();
    }

    @Test
    public void testConstructorWithOutputStream() {
        // basic functionality test
        ArchiveEntry entry = new TarArchiveEntry("test.txt");
        tarOutputStream.putArchiveEntry(entry);
        tarOutputStream.write("Hello, World!".getBytes());
        tarOutputStream.closeArchiveEntry();
        tarOutputStream.finish();

        byte[] expectedHeader = entry.getHeaderBytes();
        byte[] expectedContent = "Hello, World!".getBytes();

        byte[] actual = outputStream.toByteArray();
        byte[] actualHeader = Arrays.copyOfRange(actual, 0, expectedHeader.length);
        byte[] actualContentStart = Arrays.copyOfRange(actual, expectedHeader.length, expectedHeader.length + expectedContent.length);

        assertEquals(expectedContent, Arrays.copyOfRange(actualContentStart, 0, expectedContent.length));
        assertEquals(expectedHeader, actualHeader);
    }

    @Test
    public void testConstructorWithOutputStreamBlockSize() {
        // basic functionality test with custom block size
        int blockSize = 128;
        OutputStream outputStream = new ByteArrayOutputStream(1024);
        TarArchiveOutputStream tarOutputStream = new TarArchiveOutputStream(outputStream, blockSize);

        ArchiveEntry entry = new TarArchiveEntry("test.txt");
        tarOutputStream.putArchiveEntry(entry);
        tarOutputStream.write("Hello, World!".getBytes(), 0, "Hello, World!".getBytes().length);
        tarOutputStream.closeArchiveEntry();
        tarOutputStream.finish();

        byte[] expectedHeader = entry.getHeaderBytes();
        byte[] expectedContent = "Hello, World!".getBytes();

        byte[] actual = outputStream.toByteArray();
        byte[] actualHeader = Arrays.copyOfRange(actual, 0, expectedHeader.length);
        byte[] actualContentStart = Arrays.copyOfRange(actual, expectedHeader.length, expectedHeader.length + expectedContent.length);

        assertEquals(expectedContent, Arrays.copyOfRange(actualContentStart, 0, expectedContent.length));
        assertEquals(expectedHeader, actualHeader);

        assertFalse(outputStream.toByteArray().length % blockSize == 0);
    }

    @Test
    public void testConstructorWithOutputStreamBlockSizeRecordSize() {
        // basic functionality test with custom block size and record size
        int blockSize = 128;
        int recordSize = 512;
        OutputStream outputStream = new ByteArrayOutputStream(1024);
        TarArchiveOutputStream tarOutputStream = new TarArchiveOutputStream(outputStream, blockSize, recordSize);

        ArchiveEntry entry = new TarArchiveEntry("test.txt");
        tarOutputStream.putArchiveEntry(entry);
        tarOutputStream.write("Hello, World!".getBytes(), 0, "Hello, World!".getBytes().length);
        tarOutputStream.closeArchiveEntry();
        tarOutputStream.finish();

        byte[] expectedHeader = entry.getHeaderBytes();
        byte[] expectedContent = "Hello, World!".getBytes();

        byte[] actual = outputStream.toByteArray();
        byte[] actualHeader = Arrays.copyOfRange(actual, 0, expectedHeader.length);
        byte[] actualContentStart = Arrays.copyOfRange(actual, expectedHeader.length, expectedHeader.length + expectedContent.length);

        assertEquals(expectedContent, Arrays.copyOfRange(actualContentStart, 0, expectedContent.length));
        assertEquals(expectedHeader, actualHeader);
        assertTrue(outputStream.toByteArray().length % recordSize == 0);
    }

    @Test
    public void testSetLongFileMode() {
        // test long file name handling with different longFileMode settings
        TarArchiveOutputStream tarOutputStream = new TarArchiveOutputStream(new ByteArrayOutputStream(1024));

        // setLongFileMode(LONGFILE_ERROR)
        ArchiveEntry entry1 = new TarArchiveEntry("test_long_name_1.txt", "test_long_name_1_content.txt");
        tarOutputStream.putArchiveEntry(entry1);
        assertThrows(IOException.class, () -> tarOutputStream.write(entry1.getName().getBytes()));
        tarOutputStream.close();

        // setLongFileMode(LONGFILE_TRUNCATE)
        TarArchiveOutputStream tarOutputStream2 = new TarArchiveOutputStream(new ByteArrayOutputStream(1024));
        tarOutputStream2.setLongFileMode(TarArchiveOutputStream.LONGFILE_TRUNCATE);
        ArchiveEntry entry2 = new TarArchiveEntry("test_long_name_2.txt", "test_long_name_2_content.txt");
        tarOutputStream2.putArchiveEntry(entry2);
        tarOutputStream2.write(entry2.getName().getBytes());
        tarOutputStream2.write("test_long_name_2_content.txt".getBytes());
        tarOutputStream2.closeArchiveEntry();
        tarOutputStream2.finish();

        byte[] expectedHeader2 = entry2.getHeaderBytes();
        byte[] expectedContent2 = "test_long_name_2_content.txt".getBytes();

        byte[] actual2 = tarOutputStream2.toByteArray();
        byte[] actualHeader2 = Arrays.copyOfRange(actual2, 0, expectedHeader2.length);
        byte[] actualContentStart2 = Arrays.copyOfRange(actual2, expectedHeader2.length, expectedHeader2.length + expectedContent2.length);

        assertEquals(expectedContent2, Arrays.copyOfRange(actualContentStart2, 0, expectedContent2.length));
        assertEquals(expectedHeader2, actualHeader2);

        // setLongFileMode(LONGFILE_GNU)
        TarArchiveOutputStream tarOutputStream3 = new TarArchiveOutputStream(new ByteArrayOutputStream(1024));
        tarOutputStream3.setLongFileMode(TarArchiveOutputStream.LONGFILE_GNU);
        ArchiveEntry entry3 = new TarArchiveEntry("test_long_name_3.txt", "test_long_name_3_content.txt");
        tarOutputStream3.putArchiveEntry(entry3);
        tarOutputStream3.write(entry3.getName().getBytes());
        tarOutputStream3.write(entry3.getName().getBytes()); // NUL terminator
        tarOutputStream3.write(entry3.getSize());
        tarOutputStream3.writeAll(new ByteArrayInputStream("test_long_name_3_content.txt".getBytes()));
        tarOutputStream3.closeArchiveEntry();
        tarOutputStream3.finish();

        byte[] expectedHeader3 = entry3.getHeaderBytes();
        byte[] expectedContent3 = "test_long_name_3_content.txt".getBytes();

        byte[] actual3 = tarOutputStream3.toByteArray();
        byte[] actualHeader3 = Arrays.copyOfRange(actual3, 0, expectedHeader3.length);
        byte[] actualContentStart3 = Arrays.copyOfRange(actual3, expectedHeader3.length, expectedHeader3.length + expectedContent3.length);

        assertEquals(expectedContent3, Arrays.copyOfRange(actualContentStart3, 0, expectedContent3.length));
        assertEquals(expectedHeader3, actualHeader3);
    }

    @Test
    public void testFinish() {
        // test end of archive markers
        TarArchiveOutputStream tarOutputStream = new TarArchiveOutputStream(new ByteArrayOutputStream(1024));

        ArchiveEntry entry1 = new TarArchiveEntry("test.txt");
        tarOutputStream.putArchiveEntry(entry1);
        tarOutputStream.write("Hello, World!".getBytes());
        tarOutputStream.closeArchiveEntry();

        tarOutputStream.finish();

        byte[] expectedEOFRecord = new byte[512 * 2]; // 2 x 512 bytes EOF record

        byte[] actual = tarOutputStream.toByteArray();
        byte[] actualEOFStart = Arrays.copyOfRange(actual, actual.length - 512, actual.length);
        byte[] actualEOFEnd = Arrays.copyOfRange(actual, actual.length - 512 * 2, actual.length);

        assertArrayEquals(expectedEOFRecord, actualEOFStart);
        assertArrayEquals(expectedEOFRecord, actualEOFEnd);
    }

    @Test
    public void testClose() throws IOException {
        // test closing the stream after writing an entry
        TarArchiveOutputStream tarOutputStream = new TarArchiveOutputStream(new ByteArrayOutputStream(1024));

        ArchiveEntry entry = new TarArchiveEntry("test.txt");
        tarOutputStream.putArchiveEntry(entry);
        tarOutputStream.write("Hello, World!".getBytes());
        tarOutputStream.closeArchiveEntry();

        assertFalse(tarOutputStream.isOpen());
        assertNull(tarOutputStream.getArchiveEntry());
        assertThrows(IOException.class, tarOutputStream::write);
    }

    @Test
    public void testWrite() {
        // test writing data to the output stream
        TarArchiveOutputStream tarOutputStream = new TarArchiveOutputStream(new ByteArrayOutputStream(1024));

        ArchiveEntry entry = new TarArchiveEntry("test.txt");
        tarOutputStream.putArchiveEntry(entry);

        byte[] data = "Hello, World!".getBytes();
        tarOutputStream.write(data, 0, data.length);

        byte[] expectedContent = data;
        byte[] actual = tarOutputStream.toByteArray();

        byte[] actualContentStart = Arrays.copyOfRange(actual, 0, data.length);

        assertEquals(expectedContent, Arrays.copyOfRange(actualContentStart, 0, data.length));
    }

    @Test
    public void testWriteWithOffsetAndLength() {
        // test writing data with an offset and length to the output stream
        TarArchiveOutputStream tarOutputStream = new TarArchiveOutputStream(new ByteArrayOutputStream(1024));

        ArchiveEntry entry = new TarArchiveEntry("test.txt");
        tarOutputStream.putArchiveEntry(entry);

        byte[] data = "Hello, World!".getBytes();
        tarOutputStream.write(data, 6, 6);

        byte[] expectedContent = Arrays.copyOfRange(data, 6, data.length);
        byte[] actual = tarOutputStream.toByteArray();

        byte[] actualContentStart = Arrays.copyOfRange(actual, 0, data.length);

        assertEquals(expectedContent, Arrays.copyOfRange(actualContentStart, 6, data.length));
    }

    @Test
    public void testWriteAll() {
        // test writing all bytes from an InputStream to the output stream
        TarArchiveOutputStream tarOutputStream = new TarArchiveOutputStream(new ByteArrayOutputStream(1024));

        ArchiveEntry entry = new TarArchiveEntry("test.txt");
        tarOutputStream.putArchiveEntry(entry);

        ByteArrayInputStream inputStream = new ByteArrayInputStream("Hello, World!".getBytes());
        tarOutputStream.writeAll(inputStream);

        byte[] expectedContent = "Hello, World!".getBytes();
        byte[] actual = tarOutputStream.toByteArray();

        byte[] actualContentStart = Arrays.copyOfRange(actual, 0, expectedContent.length);

        assertEquals(expectedContent, Arrays.copyOfRange(actualContentStart, 0, expectedContent.length));
    }
}