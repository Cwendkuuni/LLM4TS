import org.apache.commons.compress.archivers.cpio.CpioArchiveEntry;
import org.apache.commons.compress.archivers.cpio.CpioArchiveOutputStream;
import org.apache.commons.compress.utils.ArchiveUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

public class CpioArchiveOutputStreamTest {

    private OutputStream outputStream;
    private CpioArchiveOutputStream cpioArchiveOutputStream;

    @Before
    public void setUp() throws IOException {
        outputStream = new ByteArrayOutputStream();
        cpioArchiveOutputStream = new CpioArchiveOutputStream(outputStream, CpioConstants.FORMAT_NEW);
    }

    @After
    public void tearDown() throws IOException {
        cpioArchiveOutputStream.close();
    }

    @Test
    public void testPutArchiveEntryWithValidEntry() throws IOException {
        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setName("testfile");
        entry.setFileSize(10);
        entry.setMode(CpioConstants.C_ISREG);
        cpioArchiveOutputStream.putArchiveEntry(entry);

        byte[] header = new byte[1024];
        cpioArchiveOutputStream.write(header);
        assertEquals(1024, cpioArchiveOutputStream.written);
        assertEquals(ArchiveUtils.toAsciiBytes(CpioConstants.MAGIC_NEW), Arrays.copyOfRange(header, 0, ArchiveUtils.MAGIC_NEW.length));
        assertTrue(Arrays.equals(ArchiveUtils.toAsciiBytes(ArchiveUtils.COPY_FILE_MAGIC), Arrays.copyOfRange(header, 36, 38)));
        assertEquals(ArchiveUtils.toAsciiBytes("testfile"), Arrays.copyOfRange(header, 86, 98));
        assertEquals(10, ArchiveUtils.toAsciiLong(Arrays.copyOfRange(header, 100, 112), 16));
    }

    @Test
    public void testPutArchiveEntryWithDuplicateEntry() throws IOException {
        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setName("testfile");
        cpioArchiveOutputStream.putArchiveEntry(entry);
        cpioArchiveOutputStream.putArchiveEntry(entry);

        Map<String, CpioArchiveEntry> names = cpioArchiveOutputStream.names;
        assertEquals(1, names.size());
        assertEquals(entry, names.get("testfile"));
    }

    @Test
    public void testPutArchiveEntryWithInvalidFormat() throws IOException {
        assertThrows(IllegalArgumentException.class, () -> {
            CpioArchiveOutputStream cpioArchiveOutputStream = new CpioArchiveOutputStream(outputStream, (short) 0xFF);
        });
    }

    @Test
    public void testPutArchiveEntryWithNullEntry() {
        assertThrows(NullPointerException.class, () -> {
            cpioArchiveOutputStream.putArchiveEntry(null);
        });
    }

    @Test
    public void testPutArchiveEntryWithClassCastException() throws IOException {
        ArchiveEntry entry = new File("test.txt");
        assertThrows(ClassCastException.class, () -> {
            cpioArchiveOutputStream.putArchiveEntry(entry);
        });
    }

    @Test
    public void testCloseArchiveEntry() throws IOException {
        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setName("testfile");
        cpioArchiveOutputStream.putArchiveEntry(entry);

        byte[] header = new byte[1024];
        cpioArchiveOutputStream.write(header);
        assertEquals(1024, cpioArchiveOutputStream.written);
        cpioArchiveOutputStream.closeArchiveEntry();
        assertNull(cpioArchiveOutputStream.entry);
        assertEquals(0, cpioArchiveOutputStream.written);
    }

    @Test
    public void testWrite() throws IOException {
        byte[] data = "test data".getBytes();
        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setName("testfile");
        cpioArchiveOutputStream.putArchiveEntry(entry);

        cpioArchiveOutputStream.write(data, 0, data.length);
        byte[] outputData = ((ByteArrayOutputStream) outputStream).toByteArray();
        assertEquals(data.length, cpioArchiveOutputStream.written);
        assertEquals(data, Arrays.copyOfRange(outputData, 1024, 1024 + data.length));
    }

    @Test
    public void testFinish() throws IOException {
        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setName("testfile");
        cpioArchiveOutputStream.putArchiveEntry(entry);
        cpioArchiveOutputStream.write("test data".getBytes());
        cpioArchiveOutputStream.finish();

        byte[] header = new byte[1024];
        byte[] data = "test data".getBytes();
        byte[] expectedOutput = new byte[1024 + data.length];
        System.arraycopy(ArchiveUtils.toAsciiBytes(CpioConstants.MAGIC_NEW), 0, expectedOutput, 0, ArchiveUtils.MAGIC_NEW.length);
        System.arraycopy(ArchiveUtils.toAsciiBytes("testfile"), 0, expectedOutput, 36, "testfile".length());
        System.arraycopy(ArchiveUtils.toAsciiBytes("testdata"), 0, expectedOutput, 86, "testdata".length());
        System.arraycopy(ArchiveUtils.toAsciiLong(new long[]{1, 0, 0, 0}, 0, 16), 100, expectedOutput, 100, 16);
        System.arraycopy(ArchiveUtils.toAsciiLong(new long[]{0, 0, 0, 10}, 0, 16), 112, expectedOutput, 112, 16);
        System.arraycopy(ArchiveUtils.toAsciiLong(new long[]{0, 0, 0, 32766}, 0, 16), 128, expectedOutput, 128, 16);
        System.arraycopy(ArchiveUtils.toAsciiLong(new long[]{0, 0, 0, 32766}, 0, 16), 144, expectedOutput, 144, 16);
        System.arraycopy(ArchiveUtils.toAsciiLong(new long[]{0, 0, 0, 32766}, 0, 16), 160, expectedOutput, 160, 16);
        System.arraycopy(ArchiveUtils.toAsciiLong(new long[]{0, 0, 0, 1}, 0, 16), 176, expectedOutput, 176, 16);
        System.arraycopy(ArchiveUtils.toAsciiLong(new long[]{0, 0, 0, 0}, 0, 16), 192, expectedOutput, 192, 16);
        System.arraycopy(ArchiveUtils.toAsciiLong(new long[]{0, 0, 0, 0}, 0, 16), 208, expectedOutput, 208, 16);
        System.arraycopy(ArchiveUtils.toAsciiLong(new long[]{0, 0, 0, 0}, 0, 16), 224, expectedOutput, 224, 16);
        System.arraycopy(ArchiveUtils.toAsciiLong(new long[]{0, 0, 0, 1}, 0, 16), 240, expectedOutput, 240, 16);
        System.arraycopy(data, 0, expectedOutput, 1024, data.length);

        byte[] outputData = ((ByteArrayOutputStream) outputStream).toByteArray();
        assertEquals(expectedOutput.length, outputData.length);
        assertTrue(Arrays.equals(expectedOutput, outputData));

        assertFalse(cpioArchiveOutputStream.finished);
        cpioArchiveOutputStream.finish();
        assertTrue(cpioArchiveOutputStream.finished);
    }

    @Test
    public void testClose() throws IOException {
        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setName("testfile");
        cpioArchiveOutputStream.putArchiveEntry(entry);
        cpioArchiveOutputStream.write("test data".getBytes());

        cpioArchiveOutputStream.close();

        byte[] header = new byte[1024];
        byte[] data = "test data".getBytes();
        byte[] expectedOutput = new byte[1024 + data.length];
        System.arraycopy(ArchiveUtils.toAsciiBytes(CpioConstants.MAGIC_NEW), 0, expectedOutput, 0, ArchiveUtils.MAGIC_NEW.length);
        System.arraycopy(ArchiveUtils.toAsciiBytes("testfile"), 0, expectedOutput, 36, "testfile".length());
        System.arraycopy(ArchiveUtils.toAsciiBytes("testdata"), 0, expectedOutput, 86, "testdata".length());
        System.arraycopy(ArchiveUtils.toAsciiLong(new long[]{1, 0, 0, 0}, 0, 16), 100, expectedOutput, 100, 16);
        System.arraycopy(ArchiveUtils.toAsciiLong(new long[]{0, 0, 0, 10}, 0, 16), 112, expectedOutput, 112, 16);
        System.arraycopy(ArchiveUtils.toAsciiLong(new long[]{0, 0, 0, 32766}, 0, 16), 128, expectedOutput, 128, 16);
        System.arraycopy(ArchiveUtils.toAsciiLong(new long[]{0, 0, 0, 32766}, 0, 16), 144, expectedOutput, 144, 16);
        System.arraycopy(ArchiveUtils.toAsciiLong(new long[]{0, 0, 0, 32766}, 0, 16), 160, expectedOutput, 160, 16);
        System.arraycopy(ArchiveUtils.toAsciiLong(new long[]{0, 0, 0, 1}, 0, 16), 176, expectedOutput, 176, 16);
        System.arraycopy(ArchiveUtils.toAsciiLong(new long[]{0, 0, 0, 0}, 0, 16), 192, expectedOutput, 192, 16);
        System.arraycopy(ArchiveUtils.toAsciiLong(new long[]{0, 0, 0, 0}, 0, 16), 208, expectedOutput, 208, 16);
        System.arraycopy(ArchiveUtils.toAsciiLong(new long[]{0, 0, 0, 0}, 0, 16), 224, expectedOutput, 224, 16);
        System.arraycopy(ArchiveUtils.toAsciiLong(new long[]{0, 0, 0, 1}, 0, 16), 240, expectedOutput, 240, 16);
        System.arraycopy(data, 0, expectedOutput, 1024, data.length);
        System.arraycopy(ArchiveUtils.toAsciiBytes(CpioConstants.CPIO_TRAILER), 0, expectedOutput, 1024 + data.length, ArchiveUtils.CPIO_TRAILER.length());

        byte[] outputData = ((ByteArrayOutputStream) outputStream).toByteArray();
        assertEquals(expectedOutput.length, outputData.length);
        assertTrue(Arrays.equals(expectedOutput, outputData));

        assertFalse(cpioArchiveOutputStream.closed);
        cpioArchiveOutputStream.close();
        assertTrue(cpioArchiveOutputStream.closed);
    }
}