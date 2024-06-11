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
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

/**
 * Test class for TarArchiveOutputStream
 */
public class TarArchiveOutputStreamTest {

    private ByteArrayOutputStream outputStream;
    private TarArchiveOutputStream tarArchiveOutputStream;
    private byte[] headerRecord;

    @Before
    public void setUp() {
        outputStream = new ByteArrayOutputStream();
        tarArchiveOutputStream = new TarArchiveOutputStream(outputStream, 512, 512);
        headerRecord = new byte[512];
    }

    @After
    public void tearDown() throws IOException {
        tarArchiveOutputStream.close();
    }

    /**
     * Test constructor
     */
    @Test
    public void testConstructor() {
        assertNotNull(tarArchiveOutputStream);
    }

    /**
     * Test putArchiveEntry method with valid arguments
     */
    @Test
    public void testPutArchiveEntryValidArguments() throws IOException {
        String entryName = "testEntry.txt";
        TarArchiveEntry entry = new TarArchiveEntry(new File("src/test/resources/test.txt"), entryName);
        tarArchiveOutputStream.putArchiveEntry(entry);
        tarArchiveOutputStream.write(new byte[]{'T', 'h', 'i', 's', ' ', 'e', 'n', 't', 'r', 'y', '\n'});
        tarArchiveOutputStream.write(new byte[]{'0', '0', '0', '0'}); // size
        tarArchiveOutputStream.closeArchiveEntry();

        byte[] expected = new byte[]{'0', '0', '0', '01', '05', '00', '54', '68', '69', '73', '20', '65', '74', '65', '72', '73', '69', '6f', '6e', '\n', '0', '0', '0', '0'};
        assertEquals(expected, headerRecord);
    }

    /**
     * Test putArchiveEntry method with invalid arguments
     */
    @Test(expected = IOException.class)
    public void testPutArchiveEntryInvalidArguments() throws IOException {
        tarArchiveOutputStream.putArchiveEntry(null);
    }

    /**
     * Test finish method
     * @throws IOException
     */
    @Test
    public void testFinish() throws IOException {
        tarArchiveOutputStream.putArchiveEntry(new TarArchiveEntry(new File("src/test/resources/test.txt"), "testEntry.txt"));
        tarArchiveOutputStream.write('T');
        tarArchiveOutputStream.write('a');
        tarArchiveOutputStream.write('r');
        tarArchiveOutputStream.write(' ');
        tarArchiveOutputStream.write('E');
        tarArchiveOutputStream.write('n');
        tarArchiveOutputStream.write('d');
        tarArchiveOutputStream.write('-');
        tarArchiveOutputStream.write('O');
        tarArchiveOutputStream.write('f');
        tarArchiveOutputStream.write(' ');
        tarArchiveOutputStream.write('A');
        tarArchiveOutputStream.write('r');
        tarArchiveOutputStream.write('c');
        tarArchiveOutputStream.write('h');
        tarArchiveOutputStream.write('i');
        tarArchiveOutputStream.write('v');
        tarArchiveOutputStream.write('e');
        tarArchiveOutputStream.write('\n');
        tarArchiveOutputStream.write('0');
        tarArchiveOutputStream.write('0');
        tarArchiveOutputStream.write('0');
        tarArchiveOutputStream.write('01');
        tarArchiveOutputStream.write('05');
        tarArchiveOutputStream.write('00');
        tarArchiveOutputStream.finish();

        byte[] expectedEOF = new byte[1024]; // 512 bytes for each EOF record
        for (int i = 0; i < 1024; i++) {
            expectedEOF[i] = '0';
        }

        byte[] buffer = outputStream.toByteArray();
        int expectedHeaderSize = 512;
        int expectedDataSize = new File("src/test/resources/test.txt").length();
        int expectedEOFSize = 1024;

        assertEquals(expectedHeaderSize + expectedDataSize + expectedEOFSize, buffer.length);
        assertArrayEquals(expectedEOF, buffer, expectedEOF.length);
    }

    /**
     * Test close method
     * @throws IOException
     */
    @Test
    public void testClose() throws IOException {
        tarArchiveOutputStream.putArchiveEntry(new TarArchiveEntry(new File("src/test/resources/test.txt"), "testEntry.txt"));
        tarArchiveOutputStream.write('T');
        tarArchiveOutputStream.write('h');
        tarArchiveOutputStream.write('i');
        tarArchiveOutputStream.write('s');
        tarArchiveOutputStream.write(' ');
        tarArchiveOutputStream.write('e');
        tarArchiveOutputStream.write('n');
        tarArchiveOutputStream.write('t');
        tarArchiveOutputStream.write('r');
        tarArchiveOutputStream.write('y');
        tarArchiveOutputStream.write('\n');
        tarArchiveOutputStream.write('0');
        tarArchiveOutputStream.write('0');
        tarArchiveOutputStream.write('0');
        tarArchiveOutputStream.write('01');
        tarArchiveOutputStream.write('05');
        tarArchiveOutputStream.write('00');
        tarArchiveOutputStream.close();

        byte[] expected = new byte[]{'0', '0', '0', '01', '05', '00', '54', '68', '69', '73', '20', '65', '74', '65', '72', '73', '69', '6f', '6e', '\n', '0', '0', '0', '0'};
        assertEquals(expected, headerRecord);
    }

    /**
     * Test setLongFileMode method
     */
    @Test
    public void testSetLongFileMode() {
        TarArchiveOutputStream tarArchiveOutputStream1 = new TarArchiveOutputStream(new ByteArrayOutputStream());
        assertFalse(tarArchiveOutputStream1.isLongFileNameSupported());
        tarArchiveOutputStream1.setLongFileMode(TarArchiveOutputStream.LONGFILE_GNU);
        assertTrue(tarArchiveOutputStream1.isLongFileNameSupported());

        TarArchiveOutputStream tarArchiveOutputStream2 = new TarArchiveOutputStream(new ByteArrayOutputStream());
        assertFalse(tarArchiveOutputStream2.isLongFileNameSupported());
        tarArchiveOutputStream2.setLongFileMode(TarArchiveOutputStream.LONGFILE_TRUNCATE);
        assertTrue(tarArchiveOutputStream2.isLongFileNameSupported());
    }

    /**
     * Test write method
     * @throws IOException
     */
    @Test
    public void testWrite() throws IOException {
        byte[] data = "Test data".getBytes(StandardCharsets.UTF_8);
        byte[] expected = new byte[512];

        tarArchiveOutputStream.putArchiveEntry(new TarArchiveEntry(new File("src/test/resources/test.txt"), "testEntry.txt"));
        tarArchiveOutputStream.write(data);
        tarArchiveOutputStream.closeArchiveEntry();
        tarArchiveOutputStream.write(expected);
        tarArchiveOutputStream.finish();

        byte[] buffer = outputStream.toByteArray();
        int expectedHeaderSize = 512;
        int expectedDataSize = data.length;
        int expectedEOFSize = 512;

        assertEquals(expectedHeaderSize + expectedDataSize + expectedEOFSize, buffer.length);
    }

    /**
     * Test isLongFileNameSupported method
     */
    @Test
    public void testIsLongFileNameSupported() {
        TarArchiveOutputStream tarArchiveOutputStream1 = new TarArchiveOutputStream(new ByteArrayOutputStream());
        assertFalse(tarArchiveOutputStream1.isLongFileNameSupported());

        TarArchiveOutputStream tarArchiveOutputStream2 = new TarArchiveOutputStream(new ByteArrayOutputStream(), 512, 512);
        assertFalse(tarArchiveOutputStream2.isLongFileNameSupported());

        TarArchiveOutputStream tarArchiveOutputStream3 = new TarArchiveOutputStream(new ByteArrayOutputStream(), 512, 512);
        tarArchiveOutputStream3.setLongFileMode(TarArchiveOutputStream.LONGFILE_TRUNCATE);
        assertTrue(tarArchiveOutputStream3.isLongFileNameSupported());
    }

    /**
     * Test closeArchiveEntry method
     * @throws IOException
     */
    @Test(expected = IOException.class)
    public void testCloseArchiveEntryInvalidArguments() throws IOException {
        tarArchiveOutputStream.putArchiveEntry(new TarArchiveEntry(new File("src/test/resources/test.txt"), "testEntry.txt"));
        tarArchiveOutputStream.write('T');
        tarArchiveOutputStream.write('h');
        tarArchiveOutputStream.write('i');
        tarArchiveOutputStream.write('s');
        tarArchiveOutputStream.write(' ');
        tarArchiveOutputStream.write('e');
        tarArchiveOutputStream.write('n');
        tarArchiveOutputStream.write('t');
        tarArchiveOutputStream.write('r');
        tarArchiveOutputStream.write('y');
        tarArchiveOutputStream.write('\n');
        tarArchiveOutputStream.write('0');
        tarArchiveOutputStream.write('0');
        tarArchiveOutputStream.write('0');
        tarArchiveOutputStream.write('01');
        tarArchiveOutputStream.write('05');
        tarArchiveOutputStream.write('00');
        tarArchiveOutputStream.closeArchiveEntry();
        tarArchiveOutputStream.closeArchiveEntry();
    }
}