import org.apache.commons.compress.archivers.CpioArchiveEntry;
import org.apache.commons.compress.archivers.CpioConstants;
import org.apache.commons.compress.archivers.cpio.CpioArchiveOutputStream;
import org.apache.commons.compress.archivers.cpio.CpioUtil;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;

public class CpioArchiveOutputStreamTest {

    private static final String TEST_FILE_NAME = "test.cpio";

    @Test
    public void testConstructorWithNewFormat() throws IOException {
        OutputStream outputStream = new ByteArrayOutputStream();
        CpioArchiveOutputStream cpioArchiveOutputStream = new CpioArchiveOutputStream(outputStream, CpioConstants.FORMAT_NEW);
        Assert.assertEquals(CpioConstants.FORMAT_NEW, cpioArchiveOutputStream.getFormat());
    }

    @Test
    public void testConstructorWithDefaultFormat() throws IOException {
        OutputStream outputStream = new ByteArrayOutputStream();
        CpioArchiveOutputStream cpioArchiveOutputStream = new CpioArchiveOutputStream(outputStream);
        Assert.assertEquals(CpioConstants.FORMAT_NEW, cpioArchiveOutputStream.getFormat());
    }

    @Test
    public void testPutNextEntryWithValidEntry() throws IOException {
        OutputStream outputStream = new ByteArrayOutputStream();
        CpioArchiveOutputStream cpioArchiveOutputStream = new CpioArchiveOutputStream(outputStream);

        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setName("testfile");
        entry.setFormat(CpioConstants.FORMAT_NEW);

        cpioArchiveOutputStream.putNextEntry(entry);

        Assert.assertNotNull(cpioArchiveOutputStream.getCpioEntry());
        Assert.assertEquals(entry, cpioArchiveOutputStream.getCpioEntry());
    }

    @Test(expected = IOException.class)
    public void testPutNextEntryWithNullEntry() throws IOException {
        OutputStream outputStream = new ByteArrayOutputStream();
        CpioArchiveOutputStream cpioArchiveOutputStream = new CpioArchiveOutputStream(outputStream);

        cpioArchiveOutputStream.putNextEntry(null);
    }

    @Test
    public void testWriteHeaderWithValidEntry() throws IOException {
        OutputStream outputStream = new ByteArrayOutputStream();
        CpioArchiveOutputStream cpioArchiveOutputStream = new CpioArchiveOutputStream(outputStream);

        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setName("testfile");
        entry.setFormat(CpioConstants.FORMAT_NEW);

        cpioArchiveOutputStream.putNextEntry(entry);

        cpioArchiveOutputStream.writeHeader(entry);

        byte[] expectedMagic = CpioConstants.MAGIC_NEW;
        byte[] expectedEntry = entry.getName().getBytes();

        byte[] actualBytes = outputStream.toByteArray();
        int index = 0;

        for (int i = 0; i < expectedMagic.length; i++) {
            Assert.assertEquals(expectedMagic[i], actualBytes[index++]);
        }

        for (int i = 0; i < expectedEntry.length; i++) {
            Assert.assertEquals(expectedEntry[i], actualBytes[index++]);
        }

        // Test padding
        int paddingSize = entry.getName().length() + 1;
        for (int i = 0; i < paddingSize; i++) {
            Assert.assertEquals(0, actualBytes[index++]);
        }
    }

    @Test
    public void testWrite() throws IOException {
        OutputStream outputStream = new ByteArrayOutputStream();
        CpioArchiveOutputStream cpioArchiveOutputStream = new CpioArchiveOutputStream(outputStream);

        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setName("testfile");
        entry.setFormat(CpioConstants.FORMAT_NEW);

        cpioArchiveOutputStream.putNextEntry(entry);

        byte[] data = "test data".getBytes();
        int length = data.length;

        cpioArchiveOutputStream.write(data, 0, length);

        byte[] expectedData = data;

        byte[] actualBytes = outputStream.toByteArray();
        int index = cpioArchiveOutputStream.getHeaderSize();

        for (int i = 0; i < length; i++) {
            Assert.assertEquals(expectedData[i], actualBytes[index++]);
        }
    }

    @Test
    public void testCloseArchiveEntry() throws IOException {
        OutputStream outputStream = new ByteArrayOutputStream();
        CpioArchiveOutputStream cpioArchiveOutputStream = new CpioArchiveOutputStream(outputStream);

        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setName("testfile");
        entry.setFormat(CpioConstants.FORMAT_NEW);

        cpioArchiveOutputStream.putNextEntry(entry);

        byte[] data = "test data".getBytes();
        int length = data.length;

        cpioArchiveOutputStream.write(data, 0, length);
        cpioArchiveOutputStream.closeArchiveEntry();

        byte[] expectedMagic = CpioConstants.MAGIC_NEW;
        byte[] expectedEntry = entry.getName().getBytes();
        byte[] expectedData = data;

        byte[] actualBytes = outputStream.toByteArray();
        int index = 0;

        for (int i = 0; i < expectedMagic.length; i++) {
            Assert.assertEquals(expectedMagic[i], actualBytes[index++]);
        }

        for (int i = 0; i < expectedEntry.length; i++) {
            Assert.assertEquals(expectedEntry[i], actualBytes[index++]);
        }

        for (int i = 0; i < expectedData.length; i++) {
            Assert.assertEquals(expectedData[i], actualBytes[index++]);
        }

        // Test padding
        int paddingSize = entry.getName().length() + 1;
        for (int i = 0; i < paddingSize; i++) {
            Assert.assertEquals(0, actualBytes[index++]);
        }
    }

    @Test
    public void testWriteWithDuplicateEntry() throws IOException {
        OutputStream outputStream = new ByteArrayOutputStream();
        CpioArchiveOutputStream cpioArchiveOutputStream = new CpioArchiveOutputStream(outputStream);

        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setName("testfile");
        entry.setFormat(CpioConstants.FORMAT_NEW);

        cpioArchiveOutputStream.putNextEntry(entry);
        cpioArchiveOutputStream.write(new byte[10], 0, 10);

        try {
            cpioArchiveOutputStream.putNextEntry(entry);
            cpioArchiveOutputStream.write(new byte[10], 0, 10);
        } catch (IOException e) {
            Assert.assertEquals("duplicate entry: testfile", e.getMessage());
        }
    }

    @Test
    public void testWriteWithInvalidEntrySize() throws IOException {
        OutputStream outputStream = new ByteArrayOutputStream();
        CpioArchiveOutputStream cpioArchiveOutputStream = new CpioArchiveOutputStream(outputStream);

        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setName("testfile");
        entry.setFormat(CpioConstants.FORMAT_NEW);

        cpioArchiveOutputStream.putNextEntry(entry);

        byte[] data = "test data".getBytes();
        int length = data.length;

        cpioArchiveOutputStream.write(data, 0, length + 1);

        byte[] actualBytes = outputStream.toByteArray();
        int index = cpioArchiveOutputStream.getHeaderSize();

        // Test padding
        int paddingSize = entry.getName().length() + 1;
        for (int i = 0; i < paddingSize; i++) {
            Assert.assertEquals(0, actualBytes[index++]);
        }

        // Test expected data
        for (int i = 0; i < length; i++) {
            Assert.assertEquals(data[i], actualBytes[index++]);
        }

        // Test unexpected data
        int unexpectedDataIndex = index;
        Assert.assertEquals(data[length], actualBytes[unexpectedDataIndex]);
    }

    @Test
    public void testFinish() throws IOException {
        OutputStream outputStream = new ByteArrayOutputStream();
        CpioArchiveOutputStream cpioArchiveOutputStream = new CpioArchiveOutputStream(outputStream);

        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setName("testfile");
        entry.setFormat(CpioConstants.FORMAT_NEW);

        cpioArchiveOutputStream.putNextEntry(entry);

        byte[] data = "test data".getBytes();
        int length = data.length;

        cpioArchiveOutputStream.write(data, 0, length);
        cpioArchiveOutputStream.finish();

        byte[] actualBytes = outputStream.toByteArray();
        int index = cpioArchiveOutputStream.getHeaderSize();

        for (int i = 0; i < length; i++) {
            Assert.assertEquals(data[i], actualBytes[index++]);
        }

        // Test trailer magic
        byte[] expectedTrailerMagic = CpioConstants.MAGIC_TRAILER;

        for (int i = 0; i < expectedTrailerMagic.length; i++) {
            Assert.assertEquals(expectedTrailerMagic[i], actualBytes[index++]);
        }

        // Test padding
        int paddingSize = entry.getName().length() + 1;
        for (int i = 0; i < paddingSize; i++) {
            Assert.assertEquals(0, actualBytes[index++]);
        }
    }

    @Test
    public void testClose() throws IOException {
        OutputStream outputStream = new ByteArrayOutputStream();
        CpioArchiveOutputStream cpioArchiveOutputStream = new CpioArchiveOutputStream(outputStream);

        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setName("testfile");
        entry.setFormat(CpioConstants.FORMAT_NEW);

        cpioArchiveOutputStream.putNextEntry(entry);

        byte[] data = "test data".getBytes();
        int length = data.length;

        cpioArchiveOutputStream.write(data, 0, length);
        cpioArchiveOutputStream.close();

        byte[] actualBytes = outputStream.toByteArray();
        int index = cpioArchiveOutputStream.getHeaderSize();

        for (int i = 0; i < length; i++) {
            Assert.assertEquals(data[i], actualBytes[index++]);
        }

        // Test trailer magic
        byte[] expectedTrailerMagic = CpioConstants.MAGIC_TRAILER;

        for (int i = 0; i < expectedTrailerMagic.length; i++) {
            Assert.assertEquals(expectedTrailerMagic[i], actualBytes[index++]);
        }

        // Test padding
        int paddingSize = entry.getName().length() + 1;
        for (int i = 0; i < paddingSize; i++) {
            Assert.assertEquals(0, actualBytes[index++]);
        }
    }

    @Test
    public void testWriteWithSwappedBytes() throws IOException {
        OutputStream outputStream = new ByteArrayOutputStream();
        CpioArchiveOutputStream cpioArchiveOutputStream = new CpioArchiveOutputStream(outputStream, CpioConstants.FORMAT_OLD_BINARY);

        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setName("testfile");
        entry.setFormat(CpioConstants.FORMAT_OLD_BINARY);

        cpioArchiveOutputStream.putNextEntry(entry);

        byte[] data = "test data".getBytes();
        int length = data.length;

        cpioArchiveOutputStream.write(data, 0, length);
        byte[] actualBytes = outputStream.toByteArray();

        byte[] expectedSwappedData = new byte[length];
        for (int i = 0; i < length; i++) {
            expectedSwappedData[length - i - 1] = data[i];
        }

        for (int i = 0; i < length; i++) {
            Assert.assertEquals(expectedSwappedData[i], actualBytes[i]);
        }
    }

    @Test
    public void testWriteWithSwappedBytesAndSwappedEndian() throws IOException {
        OutputStream outputStream = new ByteArrayOutputStream();
        CpioArchiveOutputStream cpioArchiveOutputStream = new CpioArchiveOutputStream(outputStream, CpioConstants.FORMAT_OLD_BINARY | CpioConstants.FORMAT_OLD_CRC);

        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setName("testfile");
        entry.setFormat(CpioConstants.FORMAT_OLD_BINARY | CpioConstants.FORMAT_OLD_CRC);

        cpioArchiveOutputStream.putNextEntry(entry);

        byte[] data = "test data".getBytes();
        int length = data.length;

        cpioArchiveOutputStream.write(data, 0, length);
        byte[] actualBytes = outputStream.toByteArray();

        byte[] expectedSwappedData = new byte[length];
        for (int i = 0; i < length; i++) {
            expectedSwappedData[length - i - 1] = data[i];
        }

        for (int i = 0; i < length; i++) {
            Assert.assertEquals(expectedSwappedData[i], actualBytes[i]);
        }

        long expectedCrc = CpioUtil.crc32(data);
        long actualCrc = CpioUtil.bytesToLong(new byte[]{actualBytes[length], (byte) 0xFF, actualBytes[length - 1], (byte) 0xFF}, false);

        Assert.assertEquals(expectedCrc, actualCrc);
    }

    @Test
    public void testPutArchiveEntry() throws IOException {
        OutputStream outputStream = new ByteArrayOutputStream();
        CpioArchiveOutputStream cpioArchiveOutputStream = new CpioArchiveOutputStream(outputStream);

        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setName("testfile");
        entry.setFormat(CpioConstants.FORMAT_NEW);

        cpioArchiveOutputStream.putArchiveEntry(entry);

        Assert.assertEquals(entry, cpioArchiveOutputStream.getCpioEntry());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testPutArchiveEntryWithNullEntry() throws IOException {
        OutputStream outputStream = new ByteArrayOutputStream();
        CpioArchiveOutputStream cpioArchiveOutputStream = new CpioArchiveOutputStream(outputStream);

        cpioArchiveOutputStream.putArchiveEntry(null);
    }
}