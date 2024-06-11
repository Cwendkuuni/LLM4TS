import org.junit.Before;
import org.junit.Test;
import org.apache.commons.compress.archivers.cpio.CpioArchiveOutputStream;
import org.apache.commons.compress.archivers.cpio.CpioArchiveEntry;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import static org.junit.Assert.*;

public class CpioArchiveOutputStreamTest {

    private CpioArchiveOutputStream cpioArchiveOutputStream;
    private ByteArrayOutputStream byteArrayOutputStream;

    @Before
    public void setUp() {
        byteArrayOutputStream = new ByteArrayOutputStream();
        cpioArchiveOutputStream = new CpioArchiveOutputStream(byteArrayOutputStream);
    }

    @Test
    public void testPutNextEntry() throws IOException {
        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setName("testfile");
        entry.setFileSize(5);

        cpioArchiveOutputStream.putNextEntry(entry);

        CpioArchiveEntry result = cpioArchiveOutputStream.getCpioEntry();
        assertNotNull(result);
        assertEquals("testfile", result.getName());
        assertEquals(5, result.getFileSize());
    }

    @Test(expected = IOException.class)
    public void testPutNextEntryThrowsExceptionWhenStreamClosed() throws IOException {
        cpioArchiveOutputStream.close();

        CpioArchiveEntry entry = new CpioArchiveEntry();
        cpioArchiveOutputStream.putNextEntry(entry);
    }

    @Test
    public void testCloseArchiveEntry() throws IOException {
        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setName("testfile");
        entry.setFileSize(5);

        cpioArchiveOutputStream.putNextEntry(entry);
        cpioArchiveOutputStream.closeArchiveEntry();

        assertNull(cpioArchiveOutputStream.getCpioEntry());
    }

    @Test(expected = IOException.class)
    public void testCloseArchiveEntryThrowsExceptionWhenEntrySizeMismatch() throws IOException {
        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setName("testfile");
        entry.setFileSize(5);

        cpioArchiveOutputStream.putNextEntry(entry);

        byte[] data = "123456".getBytes();
        cpioArchiveOutputStream.write(data, 0, data.length);
        cpioArchiveOutputStream.closeArchiveEntry();
    }

    @Test
    public void testWrite() throws IOException {
        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setFileSize(5);

        cpioArchiveOutputStream.putNextEntry(entry);

        byte[] data = "12345".getBytes();
        cpioArchiveOutputStream.write(data, 0, data.length);
        cpioArchiveOutputStream.closeArchiveEntry();

        byte[] result = byteArrayOutputStream.toByteArray();
        assertArrayEquals(data, result);
    }

    @Test(expected = IOException.class)
    public void testWriteThrowsExceptionWhenNoCurrentEntry() throws IOException {
        byte[] data = "12345".getBytes();
        cpioArchiveOutputStream.write(data, 0, data.length);
    }

    @Test(expected = IOException.class)
    public void testWriteThrowsExceptionWhenWritingPastEndOfEntry() throws IOException {
        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setFileSize(5);

        cpioArchiveOutputStream.putNextEntry(entry);

        byte[] data = "123456".getBytes();
        cpioArchiveOutputStream.write(data, 0, data.length);
    }

    @Test
    public void testFinish() throws IOException {
        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setName("testfile");
        entry.setFileSize(5);

        cpioArchiveOutputStream.putNextEntry(entry);
        cpioArchiveOutputStream.finish();

        byte[] result = byteArrayOutputStream.toByteArray();
        assertNotNull(result);
        assertTrue(result.length > 0);
    }

    @Test
    public void testClose() throws IOException {
        assertFalse(cpioArchiveOutputStream.isClosed());

        cpioArchiveOutputStream.close();

        assertTrue(cpioArchiveOutputStream.isClosed());
    }
}