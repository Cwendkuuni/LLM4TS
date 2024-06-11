import org.apache.commons.compress.archivers.cpio.CpioArchiveEntry;
import org.apache.commons.compress.archivers.cpio.CpioArchiveOutputStream;
import org.apache.commons.compress.archivers.cpio.CpioConstants;
import org.apache.commons.compress.utils.CpioUtil;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class CpioArchiveOutputStreamTest {

    

    @Test
    public void testCtorWithFormatNew() throws IOException {
        OutputStream outputStream = new FileOutputStream("test.cpio");
        CpioArchiveOutputStream cpioOutputStream = new CpioArchiveOutputStream(outputStream, CpioConstants.FORMAT_NEW);
        Assert.assertEquals(CpioConstants.FORMAT_NEW, cpioOutputStream.getFormat());
    }

    @Test
    public void testCtorWithFormatNewAndOutputStream() throws IOException {
        OutputStream outputStream = new FileOutputStream("test.cpio");
        CpioArchiveOutputStream cpioOutputStream = new CpioArchiveOutputStream(outputStream);
        Assert.assertEquals(CpioConstants.FORMAT_NEW, cpioOutputStream.getFormat());
    }

    @Test
    public void testPutNextEntryWithValidEntry() throws IOException {
        OutputStream outputStream = new FileOutputStream("test.cpio");
        CpioArchiveOutputStream cpioOutputStream = new CpioArchiveOutputStream(outputStream);
        CpioArchiveEntry entry = new CpioArchiveEntry();
        cpioOutputStream.putNextEntry(entry);
        Assert.assertNull(cpioOutputStream.cpioEntry);
    }

    @Test(expected = IOException.class)
    public void testPutNextEntryWithNullEntry() throws IOException {
        OutputStream outputStream = new FileOutputStream("test.cpio");
        CpioArchiveOutputStream cpioOutputStream = new CpioArchiveOutputStream(outputStream);
        cpioOutputStream.putNextEntry(null);
    }

    @Test(expected = IOException.class)
    public void testPutNextEntryWithDuplicateEntry() throws IOException {
        OutputStream outputStream = new FileOutputStream("test.cpio");
        CpioArchiveOutputStream cpioOutputStream = new CpioArchiveOutputStream(outputStream);
        CpioArchiveEntry entry1 = new CpioArchiveEntry();
        CpioArchiveEntry entry2 = new CpioArchiveEntry();
        cpioOutputStream.putNextEntry(entry1);
        cpioOutputStream.putNextEntry(entry2);
    }

    @Test
    public void testWriteHeaderNewFormatEntry() throws IOException {
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        OutputStream outputStream = new FilterOutputStream(buffer);
        CpioArchiveOutputStream cpioOutputStream = new CpioArchiveOutputStream(outputStream, CpioConstants.FORMAT_NEW);
        CpioArchiveEntry entry = new CpioArchiveEntry();
        cpioOutputStream.putNextEntry(entry);
        cpioOutputStream.writeHeader(entry);
        cpioOutputStream.closeArchiveEntry();
        byte[] header = buffer.toByteArray();
        Assert.assertArrayEquals(CpioUtil.magicNew, Arrays.copyOfRange(header, 0, CpioUtil.magicNew.length));
        // continue testing the header bytes
    }

    @Test
    public void testWriteOldAsciiEntry() throws IOException {
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        OutputStream outputStream = new FilterOutputStream(buffer);
        CpioArchiveOutputStream cpioOutputStream = new CpioArchiveOutputStream(outputStream);
        CpioArchiveEntry entry = new CpioArchiveEntry();
        cpioOutputStream.putNextEntry(entry);
        cpioOutputStream.writeOldAsciiEntry(entry);
        cpioOutputStream.closeArchiveEntry();
        byte[] header = buffer.toByteArray();
        // continue testing the header bytes
    }

    @Test
    public void testWriteOldBinaryEntry() throws IOException {
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        OutputStream outputStream = new FilterOutputStream(buffer);
        CpioArchiveOutputStream cpioOutputStream = new CpioArchiveOutputStream(outputStream);
        CpioArchiveEntry entry = new CpioArchiveEntry();
        boolean swapHalfWord = true;
        cpioOutputStream.putNextEntry(entry);
        cpioOutputStream.writeOldBinaryEntry(entry, swapHalfWord);
        cpioOutputStream.closeArchiveEntry();
        byte[] header = buffer.toByteArray();
        // continue testing the header bytes
    }

    @Test
    public void testCloseArchiveEntry() throws IOException {
        OutputStream outputStream = new FileOutputStream("test.cpio");
        CpioArchiveOutputStream cpioOutputStream = new CpioArchiveOutputStream(outputStream);
        CpioArchiveEntry entry = new CpioArchiveEntry();
        cpioOutputStream.putNextEntry(entry);
        cpioOutputStream.closeArchiveEntry();
        Assert.assertNull(cpioOutputStream.cpioEntry);
    }

    @Test
    public void testWrite() throws IOException {
        OutputStream outputStream = new FileOutputStream("test.cpio");
        CpioArchiveOutputStream cpioOutputStream = new CpioArchiveOutputStream(outputStream);
        CpioArchiveEntry entry = new CpioArchiveEntry();
        byte[] data = new byte[1024];
        cpioOutputStream.putNextEntry(entry);
        cpioOutputStream.write(data, 0, data.length);
        cpioOutputStream.closeArchiveEntry();
        cpioOutputStream.close();
    }

    @Test
    public void testFinish() throws IOException {
        OutputStream outputStream = new FileOutputStream("test.cpio");
        CpioArchiveOutputStream cpioOutputStream = new CpioArchiveOutputStream(outputStream);
        CpioArchiveEntry entry = new CpioArchiveEntry();
        cpioOutputStream.putNextEntry(entry);
        cpioOutputStream.write(new byte[1024][], 0, 1024);
        cpioOutputStream.finish();
        cpioOutputStream.close();
    }

    @Test
    public void testClose() throws IOException {
        OutputStream outputStream = new FileOutputStream("test.cpio");
        CpioArchiveOutputStream cpioOutputStream = new CpioArchiveOutputStream(outputStream);
        cpioOutputStream.close();
        Assert.assertTrue(cpioOutputStream.isClosed());
    }

    @Test(expected = IOException.class)
    public void testPutNextEntryWithNullFormat() throws IOException {
        OutputStream outputStream = new FileOutputStream("test.cpio");
        CpioArchiveOutputStream cpioOutputStream = new CpioArchiveOutputStream(outputStream);
        CpioArchiveEntry entry = new CpioArchiveEntry();
        cpioOutputStream.putNextEntry(entry, (short) -1);
    }

    @Test(expected = IOException.class)
    public void testPutNextEntryWithInvalidFormat() throws IOException {
        OutputStream outputStream = new FileOutputStream("test.cpio");
        CpioArchiveOutputStream cpioOutputStream = new CpioArchiveOutputStream(outputStream);
        CpioArchiveEntry entry = new CpioArchiveEntry();
        cpioOutputStream.putNextEntry(entry, (short) 0xFFFF);
    }

    @Test
    public void testSetFormat() throws IOException {
        OutputStream outputStream = new FileOutputStream("test.cpio");
        CpioArchiveOutputStream cpioOutputStream = new CpioArchiveOutputStream(outputStream);
        cpioOutputStream.setFormat(CpioConstants.FORMAT_NEW);
        Assert.assertEquals(CpioConstants.FORMAT_NEW, cpioOutputStream.getFormat());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetFormatWithUnknownFormat() throws IOException {
        OutputStream outputStream = new FileOutputStream("test.cpio");
        CpioArchiveOutputStream cpioOutputStream = new CpioArchiveOutputStream(outputStream);
        cpioOutputStream.setFormat((short) 0xFFFF);
    }

    
}