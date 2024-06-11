import org.apache.commons.compress.archivers.CpioArchiveEntry;
import org.apache.commons.compress.archivers.cpio.CpioConstants;
import org.apache.commons.compress.archivers.cpio.CpioArchiveOutputStream;
import org.apache.commons.compress.utils.CpioUtil;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

/**
 * Test class for {@link CpioArchiveOutputStream}
 */
public class CpioArchiveOutputStreamTest {

    private OutputStream outputStream;
    private CpioArchiveOutputStream cpioOutputStream;

    @Before
    public void setUp() {
        outputStream = new ByteArrayOutputStream();
        cpioOutputStream = new CpioArchiveOutputStream(outputStream);
    }

    @After
    public void tearDown() throws IOException {
        cpioOutputStream.close();
    }

    @Test
    public void testCtor() {
        // Default constructor
        cpioOutputStream = new CpioArchiveOutputStream(outputStream);
        assertEquals(CpioConstants.FORMAT_NEW, cpioOutputStream.getFormat());

        // Constructor with format
        cpioOutputStream = new CpioArchiveOutputStream(outputStream,
                CpioConstants.FORMAT_OLD_BINARY);
        assertEquals(CpioConstants.FORMAT_OLD_BINARY, cpioOutputStream.getFormat());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCtorWithInvalidFormat() {
        cpioOutputStream = new CpioArchiveOutputStream(outputStream, (short) 0xFF);
    }

    @Test
    public void testPutNextEntry() throws IOException {
        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setName("test.txt");
        entry.setSize(10);
        cpioOutputStream.putNextEntry(entry);

        byte[] header = outputStream.toByteArray();
        assertEquals(CpioConstants.MAGIC_NEW.length, header.length);
        assertEquals(CpioConstants.MAGIC_NEW[0], header[0]);
        assertEquals(CpioConstants.MAGIC_NEW[1], header[1]);
        assertEquals(CpioConstants.MAGIC_NEW[2], header[2]);

        assertEquals(8, header.length + 1);
        assertEquals('t', header[8]);
        assertEquals('e', header[9]);
        assertEquals('s', header[10]);
        assertEquals('t', header[11]);
        assertEquals('.', header[12]);
        assertEquals('t', header[13]);
        assertEquals('x', header[14]);
        assertEquals('t', header[15]);

        assertEquals(0, cpioOutputStream.written);
        assertNull(cpioOutputStream.cpioEntry);

        cpioOutputStream.write("Hello World!".getBytes());
        assertEquals(10, cpioOutputStream.written);
        assertSame(entry, cpioOutputStream.cpioEntry);

        byte[] data = outputStream.toByteArray();
        assertEquals(11, data.length);
        assertEquals(10, cpioOutputStream.cpioEntry.getSize());
        assertEquals("Hello World!", new String(data, 8, 10));
    }

    @Test(expected = IOException.class)
    public void testPutNextEntryWithDuplicateEntry() throws IOException {
        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setName("test.txt");
        entry.setSize(10);
        cpioOutputStream.putNextEntry(entry);
        cpioOutputStream.putNextEntry(entry);
    }

    @Test
    public void testPutNextEntryWithNoName() throws IOException {
        CpioArchiveEntry entry = new CpioArchiveEntry();
        cpioOutputStream.putNextEntry(entry);
        assertNull(cpioOutputStream.cpioEntry);
    }

    @Test
    public void testPutNextEntryWithNoSize() throws IOException {
        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setName("test.txt");
        cpioOutputStream.putNextEntry(entry);
        assertNull(cpioOutputStream.cpioEntry);
    }

    @Test
    public void testCloseArchiveEntry() throws IOException {
        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setSize(10);
        cpioOutputStream.putNextEntry(entry);
        cpioOutputStream.write("Hello World!".getBytes());
        cpioOutputStream.closeArchiveEntry();

        byte[] header = outputStream.toByteArray();
        assertEquals(11, header.length);
        assertEquals(CpioConstants.MAGIC_NEW[0], header[0]);
        assertEquals(CpioConstants.MAGIC_NEW[1], header[1]);
        assertEquals(CpioConstants.MAGIC_NEW[2], header[2]);

        byte[] data = new byte[10];
        System.arraycopy(outputStream.toByteArray(), 8, data, 0, 10);
        assertEquals("Hello World!", new String(data));

        assertEquals(0, cpioOutputStream.written);
        assertNull(cpioOutputStream.cpioEntry);
    }

    @Test(expected = IOException.class)
    public void testCloseArchiveEntryWithActiveEntry() throws IOException {
        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setSize(10);
        cpioOutputStream.putNextEntry(entry);
        cpioOutputStream.write("Hello World!".getBytes());
        cpioOutputStream.finish();

        cpioOutputStream.closeArchiveEntry();
    }

    @Test
    public void testWrite() throws IOException {
        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setName("test.txt");
        entry.setSize(10);
        cpioOutputStream.putNextEntry(entry);
        cpioOutputStream.write("Hello World!".getBytes());

        byte[] data = outputStream.toByteArray();
        assertEquals(11, data.length);
        assertEquals("Hello World!", new String(data, 8, 10));

        assertEquals(10, cpioOutputStream.written);
    }

    @Test
    public void testWriteWithMoreDataThanSize() throws IOException {
        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setName("test.txt");
        entry.setSize(5);
        cpioOutputStream.putNextEntry(entry);
        cpioOutputStream.write("Hello World!".getBytes());

        byte[] data = outputStream.toByteArray();
        assertEquals(11, data.length);
        assertEquals("Hello World!", new String(data, 8, 11));

        assertEquals(11, cpioOutputStream.written);
    }

    @Test(expected = IOException.class)
    public void testWriteWithLessDataThanSize() throws IOException {
        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setName("test.txt");
        entry.setSize(10);
        cpioOutputStream.putNextEntry(entry);
        cpioOutputStream.write(new byte[5]);
    }

    @Test
    public void testWriteWithSwappedHalfWords() throws IOException {
        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setName("test.bin");
        entry.setSize(10);
        entry.setFormat(CpioConstants.FORMAT_OLD_BINARY);
        cpioOutputStream.putNextEntry(entry);

        byte[] data = new byte[10];
        for (int i = 0; i < 10; i++) {
            data[i] = (byte) (i * 0x10 + 0x10);
        }

        cpioOutputStream.write(data, 0, 10);

        byte[] output = outputStream.toByteArray();
        assertEquals(22, output.length);

        byte[] header = new byte[12];
        System.arraycopy(output, 0, header, 0, 12);

        byte[] data2 = new byte[10];
        System.arraycopy(output, 12, data2, 0, 10);

        byte[] expectedData = new byte[10];
        for (int i = 0; i < 10; i++) {
            expectedData[i] = (byte) (i * 0x10 + 0x10);
        }

        assertEquals(CpioConstants.MAGIC_OLD_BINARY, header);
        assertEquals(CpioConstants.MAGIC_OLD_BINARY.length, header.length);
        assertEquals(expectedData, data2);
    }

    @Test(expected = IOException.class)
    public void testWriteWithSwappedHalfWordsAndInvalidFormat() throws IOException {
        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setName("test.bin");
        entry.setSize(10);
        cpioOutputStream.putNextEntry(entry);

        byte[] data = new byte[10];
        for (int i = 0; i < 10; i++) {
            data[i] = (byte) (i * 0x10 + 0x10);
        }

        cpioOutputStream.setFormat(CpioConstants.FORMAT_OLD_ASCII);
        cpioOutputStream.write(data, 0, 10);
    }

    @Test
    public void testFinish() throws IOException {
        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setName("test.txt");
        entry.setSize(10);
        cpioOutputStream.putNextEntry(entry);
        cpioOutputStream.write("Hello World!".getBytes());
        cpioOutputStream.finish();

        byte[] output = outputStream.toByteArray();
        assertEquals(21, output.length);

        byte[] header = new byte[8];
        System.arraycopy(output, 0, header, 0, 8);

        byte[] trailer = new byte[3];
        System.arraycopy(output, 13, trailer, 0, 3);

        byte[] data = new byte[10];
        System.arraycopy(output, 8, data, 0, 10);

        assertEquals(CpioConstants.MAGIC_NEW, header);
        assertEquals(CpioConstants.MAGIC_NEW.length, header.length);
        assertEquals("Hello World!", new String(data));
        assertEquals(CpioConstants.TRAILER, trailer);

        assertFalse(cpioOutputStream.finished);
        assertEquals(10, cpioOutputStream.written);
        assertNull(cpioOutputStream.cpioEntry);
    }

    @Test
    public void testFinishTwice() throws IOException {
        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setName("test.txt");
        entry.setSize(10);
        cpioOutputStream.putNextEntry(entry);
        cpioOutputStream.write("Hello World!".getBytes());
        cpioOutputStream.finish();
        cpioOutputStream.finish();

        byte[] output = outputStream.toByteArray();
        assertEquals(21, output.length);

        byte[] header = new byte[8];
        System.arraycopy(output, 0, header, 0, 8);

        byte[] trailer = new byte[3];
        System.arraycopy(output, 13, trailer, 0, 3);

        byte[] data = new byte[10];
        System.arraycopy(output, 8, data, 0, 10);

        assertEquals(CpioConstants.MAGIC_NEW, header);
        assertEquals(CpioConstants.MAGIC_NEW.length, header.length);
        assertEquals("Hello World!", new String(data));
        assertEquals(CpioConstants.TRAILER, trailer);

        assertTrue(cpioOutputStream.finished);
        assertEquals(10, cpioOutputStream.written);
        assertNull(cpioOutputStream.cpioEntry);
    }
}