import org.apache.commons.compress.archivers.CpioArchiveEntry;
import org.apache.commons.compress.archivers.CpioConstants;
import org.apache.commons.compress.archivers.cpio.CpioArchiveOutputStream;
import org.apache.commons.compress.utils.CpioUtil;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

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
        if (outputStream != null) {
            outputStream.close();
        }
    }

    /**
     * Test method for {@link CpioArchiveOutputStream#CpioArchiveOutputStream(OutputStream, short)} .
     */
    @Test
    public void testConstructorWithOutputStreamAndFormat() {
        CpioArchiveOutputStream actualCpioArchiveOutputStream = new CpioArchiveOutputStream(byteArrayOutputStream, CpioConstants.FORMAT_NEW);
        assertNull(actualCpioArchiveOutputStream.getCpioEntry());
        assertEquals(CpioConstants.FORMAT_NEW, actualCpioArchiveOutputStream.getFormat());
    }

    /**
     * Test method for {@link CpioArchiveOutputStream#CpioArchiveOutputStream(OutputStream)} .
     */
    @Test
    public void testConstructorWithOutputStream() {
        CpioArchiveOutputStream actualCpioArchiveOutputStream = new CpioArchiveOutputStream(byteArrayOutputStream);
        assertNull(actualCpioArchiveOutputStream.getCpioEntry());
        assertEquals(CpioConstants.FORMAT_NEW, actualCpioArchiveOutputStream.getFormat());
    }

    /**
     * Test method for {@link CpioArchiveOutputStream#ensureOpen()} .
     */
    @Test
    public void testEnsureOpen() {
        outputStream.putNextEntry(new CpioArchiveEntry());
        outputStream.ensureOpen();
        assertThrows(IOException.class, () -> outputStream.ensureOpen());
    }

    /**
     * Test method for {@link CpioArchiveOutputStream#putNextEntry(CpioArchiveEntry)} .
     */
    @Test
    public void testPutNextEntry() throws IOException {
        outputStream.putNextEntry(new CpioArchiveEntry());
        assertNull(outputStream.getCpioEntry());

        CpioArchiveEntry expectedCpioArchiveEntry = new CpioArchiveEntry();
        outputStream.putNextEntry(expectedCpioArchiveEntry);
        CpioArchiveEntry actualCpioArchiveEntry = outputStream.getCpioEntry();
        assertEquals(expectedCpioArchiveEntry, actualCpioArchiveEntry);
    }

    /**
     * Test method for {@link CpioArchiveOutputStream#writeHeader(CpioArchiveEntry)} .
     */
    @Test
    public void testWriteHeader() throws IOException {
        CpioArchiveEntry cpioArchiveEntry = new CpioArchiveEntry();
        outputStream.putNextEntry(cpioArchiveEntry);
        outputStream.writeHeader(cpioArchiveEntry);

        byte[] expectedHeaderBytes = CpioUtil.generateCpioHeaderBytes(cpioArchiveEntry);
        byte[] actualHeaderBytes = byteArrayOutputStream.toByteArray();
        assertEquals(expectedHeaderBytes.length, actualHeaderBytes.length);
        for (int i = 0; i < expectedHeaderBytes.length; i++) {
            assertEquals(expectedHeaderBytes[i], actualHeaderBytes[i]);
        }
    }

    /**
     * Test method for {@link CpioArchiveOutputStream#closeArchiveEntry()} .
     */
    @Test
    public void testCloseArchiveEntry() throws IOException {
        CpioArchiveEntry cpioArchiveEntry = new CpioArchiveEntry();
        outputStream.putNextEntry(cpioArchiveEntry);
        outputStream.write("Test data".getBytes());
        outputStream.closeArchiveEntry();
        assertNull(outputStream.getCpioEntry());
        assertEquals(0, outputStream.getWritten());
    }

    /**
     * Test method for {@link CpioArchiveOutputStream#write(byte[], int, int)} .
     */
    @Test
    public void testWrite() throws IOException {
        CpioArchiveEntry cpioArchiveEntry = new CpioArchiveEntry();
        outputStream.putNextEntry(cpioArchiveEntry);

        byte[] data = "Test data".getBytes();
        outputStream.write(data, 0, data.length);

        byte[] expectedDataBytes = new byte[data.length];
        System.arraycopy(data, 0, expectedDataBytes, 0, data.length);
        byte[] actualDataBytes = new byte[byteArrayOutputStream.size()];
        System.arraycopy(byteArrayOutputStream.toByteArray(), 0, actualDataBytes, 0, actualDataBytes.length);

        for (int i = 0; i < data.length; i++) {
            assertEquals(expectedDataBytes[i], actualDataBytes[i]);
        }

        assertEquals(data.length, outputStream.getWritten());
    }

    /**
     * Test method for {@link CpioArchiveOutputStream#finish()} .
     */
    @Test
    public void testFinish() throws IOException {
        CpioArchiveEntry cpioArchiveEntry = new CpioArchiveEntry();
        outputStream.putNextEntry(cpioArchiveEntry);
        outputStream.write("Test data".getBytes());
        outputStream.finish();

        Map<String, CpioArchiveEntry> expectedEntryMap = new HashMap<>();
        expectedEntryMap.put(cpioArchiveEntry.getName(), cpioArchiveEntry);
        Map<String, CpioArchiveEntry> actualEntryMap = (Map<String, CpioArchiveEntry>) outputStream.getNames();
        assertEquals(expectedEntryMap, actualEntryMap);
    }

    /**
     * Test method for {@link CpioArchiveOutputStream#close()} .
     */
    @Test
    public void testClose() throws IOException {
        CpioArchiveEntry cpioArchiveEntry = new CpioArchiveEntry();
        outputStream.putNextEntry(cpioArchiveEntry);
        outputStream.write("Test data".getBytes());
        outputStream.close();

        assertNull(outputStream.getCpioEntry());
        assertEquals(0, outputStream.getWritten());
    }
}