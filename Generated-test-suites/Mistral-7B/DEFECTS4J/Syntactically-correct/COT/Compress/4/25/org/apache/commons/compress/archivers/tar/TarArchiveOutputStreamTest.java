import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarConstants;
import org.apache.commons.compress.archivers.tar.TarBuffer;
import org.apache.commons.compress.archivers.tar.TarArchiveOutputStream;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * TarArchiveOutputStreamTest class for testing all methods in TarArchiveOutputStream.
 */
public class TarArchiveOutputStreamTest {

    @Test
    public void testConstructor() throws IOException {
        OutputStream outputStream = new ByteArrayOutputStream();
        TarArchiveOutputStream tarOutputStream = new TarArchiveOutputStream(outputStream);
        Assert.assertNotNull(tarOutputStream);
    }

    @Test
    public void testSetLongFileMode() throws IOException {
        OutputStream outputStream = new ByteArrayOutputStream();
        TarArchiveOutputStream tarOutputStream = new TarArchiveOutputStream(outputStream);
        tarOutputStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_TRUNCATE);
        Assert.assertEquals(TarArchiveOutputStream.LONGFILE_TRUNCATE, tarOutputStream.getLongFileMode());
    }

    @Test(expected = RuntimeException.class)
    public void testPutArchiveEntryWithLongFileNameError() throws IOException {
        String fileName = "a" + new String(new char[TarConstants.NAMELEN + 1]) + ".txt";
        OutputStream outputStream = new ByteArrayOutputStream();
        TarArchiveOutputStream tarOutputStream = new TarArchiveOutputStream(outputStream);
        ArchiveEntry archiveEntry = new TarArchiveEntry(fileName);
        tarOutputStream.putArchiveEntry(archiveEntry);
    }

    @Test
    public void testPutArchiveEntry() throws IOException {
        String fileName = "test.txt";
        OutputStream outputStream = new ByteArrayOutputStream();
        TarArchiveOutputStream tarOutputStream = new TarArchiveOutputStream(outputStream);
        ArchiveEntry archiveEntry = new TarArchiveEntry(fileName);
        tarOutputStream.putArchiveEntry(archiveEntry);
        tarOutputStream.write(fileName.getBytes(), 0, fileName.getBytes().length);
        tarOutputStream.closeArchiveEntry();

        byte[] expectedHeader = new byte[512];
        archiveEntry.writeHeader(expectedHeader);

        byte[] actualRecord = tarOutputStream.buffer.getBuffer();
        byte[] expectedRecord = new byte[512];
        System.arraycopy(expectedHeader, 0, expectedRecord, 0, 512);
        Assert.assertArrayEquals(expectedRecord, actualRecord);
    }

    @Test
    public void testPutArchiveEntryWithDirectoryFlag() throws IOException {
        String fileName = "testDir/";
        OutputStream outputStream = new ByteArrayOutputStream();
        TarArchiveOutputStream tarOutputStream = new TarArchiveOutputStream(outputStream);
        ArchiveEntry archiveEntry = new TarArchiveEntry(fileName, true);
        tarOutputStream.putArchiveEntry(archiveEntry);
        tarOutputStream.write(fileName.getBytes(), 0, fileName.getBytes().length);
        tarOutputStream.closeArchiveEntry();

        byte[] expectedHeader = new byte[512];
        archiveEntry.writeHeader(expectedHeader);

        byte[] actualRecord = tarOutputStream.buffer.getBuffer();
        byte[] expectedRecord = new byte[512];
        System.arraycopy(expectedHeader, 0, expectedRecord, 0, 512);
        Assert.assertArrayEquals(expectedRecord, actualRecord);
        Assert.assertEquals(expectedRecord[12], (byte) TarConstants.FILE_TYPE_DIR);
    }

    @Test
    public void testFinish() throws IOException {
        OutputStream outputStream = new ByteArrayOutputStream();
        TarArchiveOutputStream tarOutputStream = new TarArchiveOutputStream(outputStream);
        tarOutputStream.finish();
        byte[] expectedEOFRecord = new byte[512];
        for (int i = 0; i < expectedEOFRecord.length; ++i) {
            expectedEOFRecord[i] = 0;
        }
        byte[] actualRecord = tarOutputStream.buffer.getBuffer();
        Assert.assertArrayEquals(expectedEOFRecord, actualRecord);
    }

    @Test
    public void testClose() throws IOException {
        OutputStream outputStream = new ByteArrayOutputStream();
        TarArchiveOutputStream tarOutputStream = new TarArchiveOutputStream(outputStream);
        tarOutputStream.finish();
        tarOutputStream.close();
        byte[] expectedEOFRecord = new byte[512];
        for (int i = 0; i < expectedEOFRecord.length; ++i) {
            expectedEOFRecord[i] = 0;
        }
        byte[] actualEOFRecord1 = tarOutputStream.buffer.getBuffer();
        Assert.assertArrayEquals(expectedEOFRecord, actualEOFRecord1);

        byte[] actualEOFRecord2 = tarOutputStream.buffer.getBuffer();
        Assert.assertArrayEquals(expectedEOFRecord, actualEOFRecord2);
    }

    @Test(expected = IOException.class)
    public void testPutArchiveEntryWithoutCloseArchiveEntry() throws IOException {
        String fileName = "test.txt";
        OutputStream outputStream = new ByteArrayOutputStream();
        TarArchiveOutputStream tarOutputStream = new TarArchiveOutputStream(outputStream);
        ArchiveEntry archiveEntry = new TarArchiveEntry(fileName);
        tarOutputStream.putArchiveEntry(archiveEntry);
        tarOutputStream.write(fileName.getBytes(), 0, fileName.getBytes().length);
    }

    @Test
    public void testWrite() throws IOException {
        OutputStream outputStream = new ByteArrayOutputStream();
        TarArchiveOutputStream tarOutputStream = new TarArchiveOutputStream(outputStream);
        String data = "Hello World!";
        byte[] dataBytes = data.getBytes();
        ArchiveEntry archiveEntry = new TarArchiveEntry("test.txt");
        tarOutputStream.putArchiveEntry(archiveEntry);
        tarOutputStream.write("test.txt".getBytes(), 0, "test.txt".getBytes().length);
        tarOutputStream.write(dataBytes, 0, dataBytes.length);
        tarOutputStream.closeArchiveEntry();

        byte[] expectedHeader = new byte[512];
        archiveEntry.writeHeader(expectedHeader);

        byte[] actualRecord = tarOutputStream.buffer.getBuffer();
        byte[] expectedRecord = new byte[512];
        System.arraycopy(expectedHeader, 0, expectedRecord, 0, 512);
        Assert.assertArrayEquals(expectedRecord, actualRecord);

        byte[] expectedData = new byte[dataBytes.length];
        System.arraycopy(dataBytes, 0, expectedData, 0, dataBytes.length);
        byte[] actualData = new byte[tarOutputStream.buffer.getRemaining()];
        tarOutputStream.buffer.get(actualData);
        Assert.assertArrayEquals(expectedData, actualData);
    }
}