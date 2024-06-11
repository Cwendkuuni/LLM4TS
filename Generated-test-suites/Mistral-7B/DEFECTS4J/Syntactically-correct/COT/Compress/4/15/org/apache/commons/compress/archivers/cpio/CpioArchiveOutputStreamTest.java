import org.apache.commons.compress.archivers.CpioArchiveEntry;
import org.apache.commons.compress.archivers.CpioConstants;
import org.apache.commons.compress.archivers.ArchiveOutputStream;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

public class CpioArchiveOutputStreamTest {

    @Test
    public void testConstructorWithFormat() throws IOException {
        OutputStream outputStream = new ByteArrayOutputStream();
        CpioArchiveOutputStream cpioArchiveOutputStream = new CpioArchiveOutputStream(outputStream, CpioConstants.FORMAT_NEW);
        Assert.assertEquals(CpioConstants.FORMAT_NEW, cpioArchiveOutputStream.getEntryFormat());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidFormatInConstructor() throws IOException {
        OutputStream outputStream = new ByteArrayOutputStream();
        CpioArchiveOutputStream cpioArchiveOutputStream = new CpioArchiveOutputStream(outputStream, (short) 123);
    }

    @Test
    public void testPutArchiveEntryWithValidEntry() throws IOException {
        OutputStream outputStream = new ByteArrayOutputStream();
        CpioArchiveOutputStream cpioArchiveOutputStream = new CpioArchiveOutputStream(outputStream);
        CpioArchiveEntry entry = new CpioArchiveEntry();
        ArchiveEntry archiveEntry = cpioArchiveOutputStream.putArchiveEntry(entry);
        Assert.assertNotNull(archiveEntry);
    }

    @Test(expected = IOException.class)
    public void testPutArchiveEntryWithNullEntry() throws IOException {
        OutputStream outputStream = new ByteArrayOutputStream();
        CpioArchiveOutputStream cpioArchiveOutputStream = new CpioArchiveOutputStream(outputStream);
        ArchiveEntry archiveEntry = cpioArchiveOutputStream.putArchiveEntry(null);
    }

    @Test(expected = IOException.class)
    public void testPutArchiveEntryWithDuplicateEntry() throws IOException {
        OutputStream outputStream = new ByteArrayOutputStream();
        CpioArchiveOutputStream cpioArchiveOutputStream = new CpioArchiveOutputStream(outputStream);
        CpioArchiveEntry entry1 = new CpioArchiveEntry();
        CpioArchiveEntry entry2 = new CpioArchiveEntry();
        cpioArchiveOutputStream.putArchiveEntry(entry1);
        cpioArchiveOutputStream.putArchiveEntry(entry2);
    }

    @Test
    public void testWriteHeaders() throws IOException {
        OutputStream outputStream = new ByteArrayOutputStream();
        CpioArchiveOutputStream cpioArchiveOutputStream = new CpioArchiveOutputStream(outputStream, CpioConstants.FORMAT_NEW);
        CpioArchiveEntry entry = new CpioArchiveEntry();
        cpioArchiveOutputStream.putArchiveEntry(entry);
        cpioArchiveOutputStream.closeArchiveEntry();
        byte[] bytes = outputStream.toByteArray();
        String expected = new String(ArchiveUtils.toAsciiBytes(MAGIC_NEW));
        Assert.assertEquals(expected.substring(0, 6), new String(bytes, 0, 6));
        expected += entry.getName();
        Assert.assertEquals(expected, new String(bytes, 6, bytes.length - 6));
    }

    @Test
    public void testWriteData() throws IOException {
        OutputStream outputStream = new ByteArrayOutputStream();
        CpioArchiveOutputStream cpioArchiveOutputStream = new CpioArchiveOutputStream(outputStream, CpioConstants.FORMAT_NEW);
        byte[] data = new byte[]{'a', 'b', 'c'};
        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setSize(data.length);
        cpioArchiveOutputStream.putArchiveEntry(entry);
        cpioArchiveOutputStream.write(data, 0, data.length);
        cpioArchiveOutputStream.closeArchiveEntry();
        byte[] bytes = outputStream.toByteArray();
        Assert.assertEquals(data.length, bytes.length - entry.getHeaderPadCount() - entry.getName().length() - 1);
        for (int i = 0; i < data.length; i++) {
            Assert.assertEquals(data[i], bytes[i + entry.getHeaderPadCount() + entry.getName().length() + 1]);
        }
    }

    @Test(expected = IOException.class)
    public void testWriteDataWithInvalidEntrySize() throws IOException {
        OutputStream outputStream = new ByteArrayOutputStream();
        CpioArchiveOutputStream cpioArchiveOutputStream = new CpioArchiveOutputStream(outputStream, CpioConstants.FORMAT_NEW);
        byte[] data = new byte[]{'a', 'b', 'c'};
        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setSize(data.length + 1);
        cpioArchiveOutputStream.putArchiveEntry(entry);
        cpioArchiveOutputStream.write(data, 0, data.length);
    }

    @Test
    public void testWriteDataWithZeroLengthEntry() throws IOException {
        OutputStream outputStream = new ByteArrayOutputStream();
        CpioArchiveOutputStream cpioArchiveOutputStream = new CpioArchiveOutputStream(outputStream, CpioConstants.FORMAT_NEW);
        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setSize(0);
        cpioArchiveOutputStream.putArchiveEntry(entry);
        cpioArchiveOutputStream.write(new byte[0], 0, 0);
        cpioArchiveOutputStream.closeArchiveEntry();
        byte[] bytes = outputStream.toByteArray();
        Assert.assertEquals(0, bytes.length);
    }

    @Test
    public void testWriteDataWithSwappedBytes() throws IOException {
        OutputStream outputStream = new ByteArrayOutputStream();
        CpioArchiveOutputStream cpioArchiveOutputStream = new CpioArchiveOutputStream(outputStream, CpioConstants.FORMAT_OLD_BINARY);
        byte[] data = new byte[]{(byte) 0x01, (byte) 0x02, (byte) 0x03};
        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setSize(data.length);
        cpioArchiveOutputStream.putArchiveEntry(entry);
        cpioArchiveOutputStream.write(data, 0, data.length);
        cpioArchiveOutputStream.closeArchiveEntry();
        byte[] bytes = outputStream.toByteArray();
        byte[] swappedData = new byte[data.length];
        for (int i = 0; i < data.length; i++) {
            swappedData[i] = data[data.length - i - 1];
        }
        Assert.assertArrayEquals(swappedData, bytes, 0, bytes.length);
    }

    @Test
    public void testFinish() throws IOException {
        OutputStream outputStream = new ByteArrayOutputStream();
        CpioArchiveOutputStream cpioArchiveOutputStream = new CpioArchiveOutputStream(outputStream, CpioConstants.FORMAT_NEW);
        CpioArchiveEntry entry = new CpioArchiveEntry();
        cpioArchiveOutputStream.putArchiveEntry(entry);
        cpioArchiveOutputStream.finish();
        cpioArchiveOutputStream.close();
        byte[] bytes = outputStream.toByteArray();
        String expected = new String(ArchiveUtils.toAsciiBytes(MAGIC_NEW_CRC));
        Assert.assertEquals(expected.substring(0, 8), new String(bytes, 0, 8));
    }

    @Test
    public void testClose() throws IOException {
        OutputStream outputStream = new ByteArrayOutputStream();
        CpioArchiveOutputStream cpioArchiveOutputStream = new CpioArchiveOutputStream(outputStream, CpioConstants.FORMAT_NEW);
        CpioArchiveEntry entry = new CpioArchiveEntry();
        cpioArchiveOutputStream.putArchiveEntry(entry);
        cpioArchiveOutputStream.finish();
        cpioArchiveOutputStream.close();
        byte[] bytes = outputStream.toByteArray();
        String expected = new String(ArchiveUtils.toAsciiBytes(MAGIC_NEW_CRC));
        Assert.assertEquals(expected.substring(0, 8), new String(bytes, 0, 8));
        Assert.assertArrayEquals(new byte[bytes.length - 8], bytes, 8, bytes.length);
    }

    @Test(expected = IOException.class)
    public void testFinishWithUnclosedEntry() throws IOException {
        OutputStream outputStream = new ByteArrayOutputStream();
        CpioArchiveOutputStream cpioArchiveOutputStream = new CpioArchiveOutputStream(outputStream, CpioConstants.FORMAT_NEW);
        cpioArchiveOutputStream.finish();
    }

    @Test
    public void testCreateArchiveEntry() throws IOException {
        File file = new File("temp.txt");
        CpioArchiveEntry entry = cpioArchiveOutputStream.createArchiveEntry(file, "temp.txt");
        Assert.assertEquals("temp.txt", entry.getName());
        Assert.assertEquals(file.length(), entry.getSize());
    }

    @Test
    public void testCreateArchiveEntryWithNullFile() throws IOException {
        CpioArchiveEntry entry = cpioArchiveOutputStream.createArchiveEntry(null, "temp.txt");
        Assert.assertNull(entry);
    }
}