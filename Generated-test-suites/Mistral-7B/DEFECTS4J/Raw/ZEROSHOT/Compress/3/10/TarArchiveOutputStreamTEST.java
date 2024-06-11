import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarConstants;
import org.apache.commons.compress.archivers.tar.TarOutputStream;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertArrayEquals;

/**
 * TarArchiveOutputStreamTest class
 */
public class TarArchiveOutputStreamTest {

    private OutputStream outputStream;
    private TarOutputStream tarOutputStream;
    private ByteArrayOutputStream byteArrayOutputStream;
    private ByteArrayInputStream byteArrayInputStream;

    @Before
    public void setUp() throws Exception {
        outputStream = new ByteArrayOutputStream();
        tarOutputStream = new TarArchiveOutputStream(outputStream);
        byteArrayOutputStream = new ByteArrayOutputStream();
        byteArrayInputStream = new ByteArrayInputStream(new byte[0]);
    }

    @After
    public void tearDown() throws Exception {
        tarOutputStream.finish();
        tarOutputStream.close();
    }

    /**
     * Test constructor TarArchiveOutputStream
     */
    @Test
    public void testTarArchiveOutputStreamConstructor() {
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(new ByteArrayOutputStream());
        assertEquals("TarOutputStream", tarArchiveOutputStream.getClass().getName());
    }

    /**
     * Test constructor TarArchiveOutputStream with OutputStream
     */
    @Test
    public void testTarArchiveOutputStreamConstructorOutputStream() {
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(new ByteArrayOutputStream(), 1024, 512);
        assertEquals("TarOutputStream", tarArchiveOutputStream.getClass().getName());
    }

    /**
     * Test constructor TarArchiveOutputStream with OutputStream, blockSize, and recordSize
     */
    @Test
    public void testTarArchiveOutputStreamConstructorOutputStreamBlockSizeRecordSize() {
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(new ByteArrayOutputStream(), 1024, 512);
        assertEquals("TarOutputStream", tarArchiveOutputStream.getClass().getName());
    }

    /**
     * Test method setLongFileMode
     */
    @Test
    public void testSetLongFileMode() {
        tarOutputStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_TRUNCATE);
        assertEquals(TarArchiveOutputStream.LONGFILE_TRUNCATE, tarOutputStream.getLongFileMode());
    }

    /**
     * Test method finish
     */
    @Test
    public void testFinish() throws IOException {
        TarArchiveEntry tarArchiveEntry = new TarArchiveEntry("test.txt");
        tarOutputStream.putNextEntry(tarArchiveEntry);
        tarOutputStream.write(new byte[]{'H', 'e', 'l', 'l', 'o'});
        tarOutputStream.closeArchiveEntry();
        tarOutputStream.finish();
        byte[] expectedEOFHeader = new byte[TarConstants.RECORD_LENGTH];
        byte[] expectedEOFHeader2 = new byte[TarConstants.RECORD_LENGTH];
        for (int i = 0; i < expectedEOFHeader.length; i++) {
            expectedEOFHeader[i] = 0;
            expectedEOFHeader2[i] = 0;
        }
        byte[] actualEOFHeader = new byte[outputStream.size()];
        outputStream.read(actualEOFHeader);
        assertArrayEquals(expectedEOFHeader, actualEOFHeader);
        assertArrayEquals(expectedEOFHeader2, actualEOFHeader.clone());
    }

    /**
     * Test method close
     */
    @Test
    public void testClose() throws IOException {
        TarArchiveEntry tarArchiveEntry = new TarArchiveEntry("test.txt");
        tarOutputStream.putNextEntry(tarArchiveEntry);
        tarOutputStream.write(new byte[]{'H', 'e', 'l', 'l', 'o'});
        tarOutputStream.closeArchiveEntry();
        tarOutputStream.close();
    }

    /**
     * Test method getRecordSize
     */
    @Test
    public void testGetRecordSize() {
        assertEquals(512, tarOutputStream.getRecordSize());
    }

    /**
     * Test method putArchiveEntry
     */
    @Test
    public void testPutArchiveEntry() throws IOException {
        TarArchiveEntry tarArchiveEntry = new TarArchiveEntry("test.txt");
        tarOutputStream.putArchiveEntry(tarArchiveEntry);
        byte[] expectedHeader = new byte[TarConstants.HEADER_LENGTH];
        byte[] expectedName = "test.txt".getBytes();
        byte[] expectedSizeAndMode = new byte[]{0x00, 0x05, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00};
        for (int i = 0; i < expectedHeader.length; i++) {
            expectedHeader[i] = 0;
        }
        tarArchiveEntry.writeHeader(expectedHeader);
        tarOutputStream.write(expectedName);
        tarOutputStream.write(expectedSizeAndMode);

        byte[] actualHeader = new byte[outputStream.size()];
        outputStream.read(actualHeader);
        assertArrayEquals(expectedHeader, actualHeader);
        assertEquals("test.txt", new String(byteArrayOutputStream.toByteArray()));
    }

    /**
     * Test method closeArchiveEntry
     */
    @Test
    public void testCloseArchiveEntry() throws IOException {
        TarArchiveEntry tarArchiveEntry = new TarArchiveEntry("test.txt");
        tarOutputStream.putNextEntry(tarArchiveEntry);
        tarOutputStream.write(new byte[]{'H', 'e', 'l', 'l', 'o'});
        tarOutputStream.closeArchiveEntry();
        byte[] expectedEOFHeader = new byte[TarConstants.RECORD_LENGTH];
        byte[] expectedEOFHeader2 = new byte[TarConstants.RECORD_LENGTH];
        for (int i = 0; i < expectedEOFHeader.length; i++) {
            expectedEOFHeader[i] = 0;
            expectedEOFHeader2[i] = 0;
        }
        byte[] actualEOFHeader = new byte[outputStream.size()];
        outputStream.read(actualEOFHeader);
        assertArrayEquals(expectedEOFHeader, actualEOFHeader);
        assertArrayEquals(expectedEOFHeader2, actualEOFHeader.clone());
    }

    /**
     * Test method write
     */
    @Test
    public void testWrite() throws IOException {
        TarArchiveEntry tarArchiveEntry = new TarArchiveEntry("test.txt");
        tarOutputStream.putNextEntry(tarArchiveEntry);
        byte[] data = {"H", "e", "l", "l", "o"};
        tarOutputStream.write(data);
        byte[] expectedData = new byte[data.length];
        System.arraycopy(data, 0, expectedData, 0, data.length);
        byte[] actualData = new byte[outputStream.size()];
        outputStream.read(actualData);
        assertArrayEquals(expectedData, actualData);
    }

    /**
     * Test method putNextEntry with directory entry
     */
    @Test(expected = IllegalArgumentException.class)
    public void testPutNextEntryWithDirectoryEntry() throws IOException {
        TarArchiveEntry tarArchiveEntry = new TarArchiveEntry(new File("test.txt"), "test/");
        tarOutputStream.putNextEntry(tarArchiveEntry);
    }

    /**
     * Test method putNextEntry with existing name
     */
    @Test(expected = IOException.class)
    public void testPutNextEntryWithExistingName() throws IOException {
        TarArchiveEntry tarArchiveEntry1 = new TarArchiveEntry("test.txt");
        TarArchiveEntry tarArchiveEntry2 = new TarArchiveEntry("test.txt");
        tarOutputStream.putNextEntry(tarArchiveEntry1);
        tarOutputStream.putNextEntry(tarArchiveEntry2);
    }

    /**
     * Test method write with existing name
     */
    @Test(expected = IOException.class)
    public void testWriteWithExistingName() throws IOException {
        TarArchiveEntry tarArchiveEntry1 = new TarArchiveEntry("test.txt");
        TarArchiveEntry tarArchiveEntry2 = new TarArchiveEntry("test.txt");
        tarOutputStream.putNextEntry(tarArchiveEntry1);
        tarOutputStream.write(new byte[]{'H', 'e', 'l', 'l', 'o'});
        tarOutputStream.putNextEntry(tarArchiveEntry2);
    }
}