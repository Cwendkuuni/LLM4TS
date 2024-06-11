import org.apache.commons.compress.archivers.CpioArchiveEntry;
import org.apache.commons.compress.archivers.CpioConstants;
import org.apache.commons.compress.archivers.cpio.CpioArchiveOutputStream;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

/**
 * Test class for CpioArchiveOutputStream
 */
public class CpioArchiveOutputStreamTest {

    private ByteArrayOutputStream outputStream;
    private CpioArchiveOutputStream cpioOutputStream;

    @Before
    public void setup() {
        outputStream = new ByteArrayOutputStream();
        cpioOutputStream = new CpioArchiveOutputStream(outputStream, CpioConstants.FORMAT_NEW);
    }

    @After
    public void tearDown() throws IOException {
        cpioOutputStream.close();
    }

    /**
     * Test putNextEntry method with valid CpioArchiveEntry
     */
    @Test
    public void testPutNextEntryValidEntry() throws IOException {
        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setName("testfile");
        entry.setFileSize(10);

        cpioOutputStream.putNextEntry(entry);
        cpioOutputStream.write("Test data".getBytes(StandardCharsets.UTF_8));
        cpioOutputStream.closeArchiveEntry();

        byte[] expectedHeader = new byte[]{-1, 'C', 'P', 'I', 'O', '\0', 0, 0, 0, 0, 0, 0,
                (byte) 0xe1, (byte) 0xaf, 't', 'e', 's', 't', 'f', 'i', 'l', 'e', '\0', 10, 0, 0,
                (byte) 0x00, (byte) 0x00, 'T', 'e', 's', 't', ' ', 'd', 'a', 't', 'a', '\0'};
        byte[] expectedData = "Test data".getBytes(StandardCharsets.UTF_8);

        assertEquals("Unexpected CPIO header written", expectedHeader, outputStream.toByteArray().subArray(0, expectedHeader.length));
        assertEquals("Unexpected data written", expectedData, outputStream.toByteArray().subArray(expectedHeader.length, expectedHeader.length + expectedData.length));
    }

    /**
     * Test putNextEntry method with duplicate entry
     */
    @Test
    public void testPutNextEntryDuplicateEntry() {
        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setName("testfile");

        cpioOutputStream.putNextEntry(entry);
        cpioOutputStream.closeArchiveEntry();

        assertThrows("Duplicate entry should not be allowed", IOException.class, () -> cpioOutputStream.putNextEntry(entry));
    }

    /**
     * Test putNextEntry method with null entry
     */
    @Test
    public void testPutNextEntryNullEntry() {
        assertThrows("Null entry is not allowed", IOException.class, () -> cpioOutputStream.putNextEntry(null));
    }

    /**
     * Test write method with valid data
     */
    @Test
    public void testWriteValidData() throws IOException {
        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setName("testfile");
        entry.setFileSize(10);
        cpioOutputStream.putNextEntry(entry);

        cpioOutputStream.write("Test data".getBytes(StandardCharsets.UTF_8));
        cpioOutputStream.closeArchiveEntry();

        byte[] expectedData = "Test data".getBytes(StandardCharsets.UTF_8);
        assertEquals("Unexpected data written", expectedData, outputStream.toByteArray().subArray(outputStream.size() - expectedData.length, outputStream.size()));
    }

    /**
     * Test write method with invalid entry
     */
    @Test
    public void testWriteInvalidEntry() {
        assertThrows("Invalid entry", IOException.class, () -> cpioOutputStream.write(1));
    }

    /**
     * Test close method with valid data written
     */
    @Test
    public void testCloseWithValidData() throws IOException {
        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setName("testfile");
        entry.setFileSize(10);
        cpioOutputStream.putNextEntry(entry);
        cpioOutputStream.write("Test data".getBytes(StandardCharsets.UTF_8));
        cpioOutputStream.closeArchiveEntry();

        byte[] expectedHeader = new byte[]{-1, 'C', 'P', 'I', 'O', '\0', 0, 0, 0, 0, 0, 0,
                (byte) 0xe1, (byte) 0xaf, 't', 'e', 's', 't', 'f', 'i', 'l', 'e', '\0', 10, 0, 0,
                (byte) 0x00, (byte) 0x00, 'T', 'e', 's', 't', ' ', 'd', 'a', 't', 'a', '\0'};
        byte[] expectedData = "Test data".getBytes(StandardCharsets.UTF_8);

        assertEquals("Unexpected CPIO header written", expectedHeader, outputStream.toByteArray().subArray(0, expectedHeader.length));
        assertEquals("Unexpected data written", expectedData, outputStream.toByteArray().subArray(expectedHeader.length, expectedHeader.length + expectedData.length));

        assertNull("Unexpected entry after closing stream", cpioOutputStream.cpioEntry);
    }

    /**
     * Test close method with invalid data written
     */
    @Test
    public void testCloseWithInvalidData() {
        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setName("testfile");
        entry.setFileSize(10);
        cpioOutputStream.putNextEntry(entry);

        assertThrows("Invalid data, entry must be closed", IOException.class, () -> cpioOutputStream.close());
    }

    /**
     * Test finish method with valid data written
     */
    @Test
    public void testFinishWithValidData() throws IOException {
        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setName("testfile");
        entry.setFileSize(10);
        cpioOutputStream.putNextEntry(entry);
        cpioOutputStream.write("Test data".getBytes(StandardCharsets.UTF_8));
        cpioOutputStream.closeArchiveEntry();

        cpioOutputStream.finish();

        byte[] expectedTrailer = new byte[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};

        assertEquals("Unexpected trailer written", expectedTrailer, outputStream.toByteArray().subArray(outputStream.size() - expectedTrailer.length, outputStream.size()));

        assertNull("Unexpected entry after finishing stream", cpioOutputStream.cpioEntry);
    }

    /**
     * Test finish method with invalid data written
     */
    @Test
    public void testFinishWithInvalidData() {
        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setName("testfile");
        entry.setFileSize(10);
        cpioOutputStream.putNextEntry(entry);

        assertThrows("Invalid data, stream must be finished with a valid entry", IOException.class, () -> cpioOutputStream.finish());
    }
}