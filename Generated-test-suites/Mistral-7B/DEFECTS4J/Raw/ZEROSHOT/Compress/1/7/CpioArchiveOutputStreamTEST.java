import org.apache.commons.compress.archivers.cpio.CpioArchiveEntry;
import org.apache.commons.compress.archivers.cpio.CpioConstants;
import org.apache.commons.compress.archivers.cpio.CpioArchiveOutputStream;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

/**
 * Test class for {@link CpioArchiveOutputStream}
 */
public class CpioArchiveOutputStreamTest {

    private CpioArchiveOutputStream outputStream;
    private ByteArrayOutputStream byteArrayOutputStream;

    @Before
    public void setUp() throws IOException {
        byteArrayOutputStream = new ByteArrayOutputStream();
        outputStream = new CpioArchiveOutputStream(byteArrayOutputStream, CpioConstants.FORMAT_NEW);
    }

    @After
    public void tearDown() throws IOException {
        outputStream.close();
    }

    /**
     * Test constructor
     */
    @Test
    public void testConstructor() {
        // nothing to test here
    }

    /**
     * Test setFormat method
     */
    @Test
    public void testSetFormat() {
        outputStream.setFormat((short) 0xEF); // invalid value
        assertEquals(CpioConstants.FORMAT_NEW, outputStream.getFormat());

        outputStream.setFormat(CpioConstants.FORMAT_OLD_ASCII);
        assertEquals(CpioConstants.FORMAT_OLD_ASCII, outputStream.getFormat());
    }

    /**
     * Test putNextEntry method
     * @throws IOException
     */
    @Test
    public void testPutNextEntry() throws IOException {
        // Test putNextEntry without parameters
        outputStream.putNextEntry(null);
        assertNull(outputStream.getCpioEntry());

        // Test putNextEntry with null cpioEntry
        assertFalse(outputStream.putNextEntry(null));

        // Test putNextEntry with a valid cpioEntry
        CpioArchiveEntry cpioEntry = new CpioArchiveEntry();
        cpioEntry.setName("TestFile.txt");
        cpioEntry.setSize(12345L);
        cpioEntry.setFormat(CpioConstants.FORMAT_NEW);
        assertTrue(outputStream.putNextEntry(cpioEntry));
        assertSame(cpioEntry, outputStream.getCpioEntry());
        assertEquals("TestFile.txt", outputStream.getCpioEntry().getName());
        assertEquals(12345L, outputStream.getCpioEntry().getSize());
        assertEquals(CpioConstants.FORMAT_NEW, outputStream.getCpioEntry().getFormat());
    }

    /**
     * Test closeArchiveEntry method
     * @throws IOException
     */
    @Test
    public void testCloseArchiveEntry() throws IOException {
        // Test closeArchiveEntry without a previous entry
        outputStream.closeArchiveEntry();
        assertNull(outputStream.getCpioEntry());

        // Test closeArchiveEntry with a previous entry
        CpioArchiveEntry entry = new CpioArchiveEntry();
        outputStream.putNextEntry(entry);
        outputStream.write(new byte[]{(byte) 0x01, (byte) 0x02, (byte) 0x03});
        outputStream.closeArchiveEntry();
        assertNull(outputStream.getCpioEntry());
    }

    /**
     * Test write method
     * @throws IOException
     */
    @Test
    public void testWrite() throws IOException {
        // Test write without a previous entry
        byte[] data = new byte[]{0x01, 0x02, 0x03};
        outputStream.write(data);
        byte[] result = byteArrayOutputStream.toByteArray();
        assertEquals(3, result.length);
        assertArrayEquals(data, result);

        // Test write with a previous entry
        CpioArchiveEntry entry = new CpioArchiveEntry();
        outputStream.putNextEntry(entry);
        outputStream.write(data);
        outputStream.closeArchiveEntry();
        result = byteArrayOutputStream.toByteArray();
        assertEquals(3 + entry.getHeaderSize(), result.length);
    }

    /**
     * Test finish method
     * @throws IOException
     */
    @Test
    public void testFinish() throws IOException {
        // Test finish without a previous entry
        outputStream.finish();
        byte[] result = byteArrayOutputStream.toByteArray();
        assertEquals(0, result.length);

        // Test finish with a previous entry
        CpioArchiveEntry entry = new CpioArchiveEntry();
        outputStream.putNextEntry(entry);
        outputStream.write(new byte[]{(byte) 0x01, (byte) 0x02, (byte) 0x03});
        outputStream.finish();
        result = byteArrayOutputStream.toByteArray();
        assertEquals(entry.getHeaderSize() + 3, result.length);

        // Test finish with multiple entries
        CpioArchiveEntry entry1 = new CpioArchiveEntry();
        entry1.setName("Entry1.txt");
        entry1.setSize(12345L);
        outputStream.putNextEntry(entry1);
        outputStream.write(new byte[]{(byte) 0x01, (byte) 0x02, (byte) 0x03});
        CpioArchiveEntry entry2 = new CpioArchiveEntry();
        entry2.setName("Entry2.txt");
        entry2.setSize(56789L);
        outputStream.putNextEntry(entry2);
        outputStream.write(new byte[]{(byte) 0x04, (byte) 0x05, (byte) 0x06});
        outputStream.finish();
        result = byteArrayOutputStream.toByteArray();
        Map<String, CpioArchiveEntry> entries = new HashMap<>();
        entries.put("Entry1.txt", entry1);
        entries.put("Entry2.txt", entry2);
        byte[] expected = new byte[0];
        for (CpioArchiveEntry entry : entries.values()) {
            expected = byteArrayToByteArray(addEntryToByteArray(expected, entry));
        }
        addTrailerToByteArray(expected);
        assertArrayEquals(expected, result);
    }

    /**
     * Test close method
     * @throws IOException
     */
    @Test
    public void testClose() throws IOException {
        // Test close with no previous entry
        outputStream.close();
        assertNull(outputStream.getCpioEntry());
        assertFalse(outputStream.isOpen());

        // Test close with a previous entry
        CpioArchiveEntry entry = new CpioArchiveEntry();
        outputStream.putNextEntry(entry);
        outputStream.write(new byte[]{(byte) 0x01, (byte) 0x02, (byte) 0x03});
        outputStream.close();
        assertNull(outputStream.getCpioEntry());
        assertFalse(outputStream.isOpen());
    }

    /**
     * Helper method to add the entry header to the byte array
     * @param byteArray the byte array to modify
     * @param entry the entry to add
     * @return the modified byte array
     */
    private byte[] addEntryToByteArray(byte[] byteArray, CpioArchiveEntry entry) {
        byte[] entryBytes = entry.toByteArray();
        byte[] result = new byte[byteArray.length + entryBytes.length];
        System.arraycopy(byteArray, 0, result, 0, byteArray.length);
        System.arraycopy(entryBytes, 0, result, byteArray.length, entryBytes.length);
        return result;
    }

    /**
     * Helper method to add the trailer to the byte array
     * @param byteArray the byte array to modify
     */
    private void addTrailerToByteArray(byte[] byteArray) {
        byte[] trailerBytes = CpioConstants.TRAILER.getBytes();
        byte[] result = new byte[byteArray.length + trailerBytes.length];
        System.arraycopy(byteArray, 0, result, 0, byteArray.length);
        System.arraycopy(trailerBytes, 0, result, byteArray.length, trailerBytes.length);
    }

    /**
     * Helper method to convert a byte array to another byte array
     * @param byteArray the byte array to convert
     * @return the converted byte array
     */
    private byte[] byteArrayToByteArray(byte[] byteArray) {
        ByteArrayInputStream inputStream = new ByteArrayInputStream(byteArray);
        return new byte[(int) inputStream.available()];
    }

    /**
     * Helper method to compare two byte arrays
     * @param expected the expected byte array
     * @param actual the actual byte array
     */
    private void assertArrayEquals(byte[] expected, byte[] actual) {
        assertEquals(expected.length, actual.length);
        for (int i = 0; i < expected.length; i++) {
            assertEquals(expected[i], actual[i]);
        }
    }

}