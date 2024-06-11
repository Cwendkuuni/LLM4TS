import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarConstants;
import org.apache.commons.compress.archivers.tar.TarBuffer;
import org.apache.commons.compress.archivers.tar.TarArchiveOutputStream;
import org.apache.commons.compress.archivers.tar.ArchiveEntry;
import org.apache.commons.compress.archivers.tar.LongFileMode;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class TarArchiveOutputStreamTest {

    @Test
    public void testConstructor() {
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(new ByteArrayOutputStream());
        Assert.assertNotNull(tarArchiveOutputStream);
    }

    @Test
    public void testSetLongFileMode() {
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(new ByteArrayOutputStream());
        tarArchiveOutputStream.setLongFileMode(LongFileMode.LONGFILE_TRUNCATE.getValue());
        Assert.assertEquals(LongFileMode.LONGFILE_TRUNCATE.getValue(), tarArchiveOutputStream.getLongFileMode());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetInvalidLongFileMode() {
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(new ByteArrayOutputStream());
        tarArchiveOutputStream.setLongFileMode(5);
    }

    @Test
    public void testFinish() throws IOException {
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(new ByteArrayOutputStream());
        tarArchiveOutputStream.finish();
        byte[] expectedEOFRecord = new byte[TarConstants.HEADER_SIZE * 2];
        Assert.assertArrayEquals(expectedEOFRecord, tarArchiveOutputStream.getBuffer().getRecord());
    }

    @Test(expected = IllegalStateException.class)
    public void testFinishWithUnclosedEntry() throws IOException {
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(new ByteArrayOutputStream());
        ArchiveEntry entry = new TarArchiveEntry("test.txt");
        tarArchiveOutputStream.putArchiveEntry(entry);
        tarArchiveOutputStream.finish();
    }

    @Test
    public void testClose() throws IOException {
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(new ByteArrayOutputStream());
        ArchiveEntry entry = new TarArchiveEntry("test.txt");
        tarArchiveOutputStream.putArchiveEntry(entry);
        tarArchiveOutputStream.write(new byte[]{'T', 'e', 's', 't'.toString().getBytes()});
        tarArchiveOutputStream.closeArchiveEntry();
        tarArchiveOutputStream.close();
    }

    @Test(expected = IOException.class)
    public void testWriteOverflow() throws IOException {
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(new ByteArrayOutputStream());
        ArchiveEntry entry = new TarArchiveEntry("test.txt");
        tarArchiveOutputStream.putArchiveEntry(entry);
        byte[] data = new byte[TarConstants.NAMELEN * 2 + 1];
        tarArchiveOutputStream.write(data);
    }

    @Test
    public void testWrite() throws IOException {
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(new ByteArrayOutputStream());
        ArchiveEntry entry = new TarArchiveEntry("test.txt");
        tarArchiveOutputStream.putArchiveEntry(entry);
        byte[] data = "Test Data".getBytes();
        tarArchiveOutputStream.write(data);
        byte[] expectedData = new byte[data.length];
        System.arraycopy(data, 0, expectedData, 0, data.length);
        Assert.assertArrayEquals(expectedData, tarArchiveOutputStream.getBuffer().getRecord());
    }

    @Test
    public void testWritePartial() throws IOException {
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(new ByteArrayOutputStream());
        ArchiveEntry entry = new TarArchiveEntry("test.txt");
        tarArchiveOutputStream.putArchiveEntry(entry);
        byte[] data = "Test Data".getBytes();
        int numToWrite = data.length / 2;
        tarArchiveOutputStream.write(data, 0, numToWrite);
        byte[] expectedData = new byte[numToWrite];
        System.arraycopy(data, 0, expectedData, 0, numToWrite);
        Assert.assertArrayEquals(expectedData, tarArchiveOutputStream.getBuffer().getRecord());
    }

    @Test
    public void testWriteWithAssembling() throws IOException {
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(new ByteArrayOutputStream());
        ArchiveEntry entry = new TarArchiveEntry("test.txt");
        tarArchiveOutputStream.putArchiveEntry(entry);
        byte[] data = "Test Data".getBytes();
        int numToWrite = data.length / 2;
        tarArchiveOutputStream.write(data, 0, numToWrite);
        tarArchiveOutputStream.write(data, numToWrite, data.length - numToWrite);
        byte[] expectedData = new byte[data.length];
        System.arraycopy(data, 0, expectedData, 0, data.length);
        Assert.assertArrayEquals(expectedData, tarArchiveOutputStream.getBuffer().getRecord());
    }

    @Test
    public void testWriteEOFRecord() throws IOException {
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(new ByteArrayOutputStream());
        tarArchiveOutputStream.finish();
        byte[] expectedEOFRecord = new byte[TarConstants.HEADER_SIZE * 2];
        Assert.assertArrayEquals(expectedEOFRecord, tarArchiveOutputStream.getBuffer().getRecord());
    }

    @Test
    public void testWriteWithLongFileName() throws IOException {
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(new ByteArrayOutputStream());
        tarArchiveOutputStream.setLongFileMode(LongFileMode.LONGFILE_GNU.getValue());
        ArchiveEntry entry = new TarArchiveEntry("longFileName/subDirectory/test.txt");
        tarArchiveOutputStream.putArchiveEntry(entry);
        byte[] data = "Test Data".getBytes();
        tarArchiveOutputStream.write(data);
        byte[] expectedData = new byte[data.length + 1 + "longFileName/subDirectory/test.txt".getBytes().length];
        System.arraycopy(data, 0, expectedData, 0, data.length);
        System.arraycopy("longFileName/subDirectory/test.txt".getBytes(), 0, expectedData, data.length, "longFileName/subDirectory/test.txt".getBytes().length);
        Assert.assertArrayEquals(expectedData, tarArchiveOutputStream.getBuffer().getRecord());
    }

    @Test(expected = RuntimeException.class)
    public void testWriteWithLongFileNameAndTruncateMode() throws IOException {
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(new ByteArrayOutputStream());
        ArchiveEntry entry = new TarArchiveEntry("longFileName/subDirectory/test.txt");
        tarArchiveOutputStream.putArchiveEntry(entry);
        byte[] data = "Test Data".getBytes();
        tarArchiveOutputStream.write(data);
    }
}