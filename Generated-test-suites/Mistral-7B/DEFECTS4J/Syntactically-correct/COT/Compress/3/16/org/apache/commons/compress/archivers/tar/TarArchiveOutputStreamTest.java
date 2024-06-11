import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarConstants;
import org.apache.commons.compress.archivers.tar.TarBuffer;
import org.apache.commons.compress.archivers.tar.TarArchiveOutputStream;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

/**
 * TarArchiveOutputStreamTest.java
 */
public class TarArchiveOutputStreamTest {

    @Test
    public void testConstructor() throws IOException {
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(new ByteArrayOutputStream());
        Assert.assertNotNull(tarArchiveOutputStream);
    }

    @Test
    public void testSetLongFileMode() throws IOException {
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(new ByteArrayOutputStream(), TarBuffer.DEFAULT_BLKSIZE, TarBuffer.DEFAULT_RCDSIZE);
        tarArchiveOutputStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_TRUNCATE);
        Assert.assertEquals(TarArchiveOutputStream.LONGFILE_TRUNCATE, tarArchiveOutputStream.getLongFileMode());
    }

    @Test(expected = RuntimeException.class)
    public void testPutArchiveEntryWithFileNameLongerThanNameLen() throws IOException {
        String longFileName = "a".repeat(TarConstants.NAMELEN + 1);
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(new ByteArrayOutputStream(), TarBuffer.DEFAULT_BLKSIZE, TarBuffer.DEFAULT_RCDSIZE);
        TarArchiveEntry archiveEntry = new TarArchiveEntry(new File("testFile"));
        archiveEntry.setName(longFileName);
        tarArchiveOutputStream.putArchiveEntry(archiveEntry);
    }

    @Test
    public void testPutArchiveEntry() throws IOException {
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(new ByteArrayOutputStream(), TarBuffer.DEFAULT_BLKSIZE, TarBuffer.DEFAULT_RCDSIZE);
        TarArchiveEntry archiveEntry = new TarArchiveEntry(new File("testFile"));
        tarArchiveOutputStream.putArchiveEntry(archiveEntry);
        byte[] expectedRecord = archiveEntry.getHeaderRecord();
        Assert.assertEquals(expectedRecord, tarArchiveOutputStream.recordBuf);
    }

    @Test(expected = IOException.class)
    public void testCloseArchiveEntryWithoutPutArchiveEntry() throws IOException {
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(new ByteArrayOutputStream(), TarBuffer.DEFAULT_BLKSIZE, TarBuffer.DEFAULT_RCDSIZE);
        tarArchiveOutputStream.closeArchiveEntry();
    }

    @Test
    public void testWrite() throws IOException {
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(new ByteArrayOutputStream(), TarBuffer.DEFAULT_BLKSIZE, TarBuffer.DEFAULT_RCDSIZE);
        TarArchiveEntry archiveEntry = new TarArchiveEntry(new File("testFile"));
        tarArchiveOutputStream.putArchiveEntry(archiveEntry);
        byte[] testData = "Hello World".getBytes(StandardCharsets.UTF_8);
        tarArchiveOutputStream.write(testData, 0, testData.length);
        InputStream inputStream = new ByteArrayInputStream(tarArchiveOutputStream.buffer.getBuffer());
        byte[] actualData = new byte[testData.length];
        inputStream.read(actualData);
        Assert.assertArrayEquals(testData, actualData);
    }

    @Test
    public void testFinish() throws IOException {
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(new ByteArrayOutputStream(), TarBuffer.DEFAULT_BLKSIZE, TarBuffer.DEFAULT_RCDSIZE);
        TarArchiveEntry archiveEntry = new TarArchiveEntry(new File("testFile"));
        tarArchiveOutputStream.putArchiveEntry(archiveEntry);
        tarArchiveOutputStream.write(new byte[512], 0, 512); // EOF record
        tarArchiveOutputStream.write(new byte[512], 0, 512); // EOF record
        tarArchiveOutputStream.finish();
        InputStream inputStream = new ByteArrayInputStream(tarArchiveOutputStream.buffer.getBuffer());
        byte[] actualData = new byte[1024];
        int bytesRead = inputStream.read(actualData, 0, actualData.length);
        Assert.assertEquals(2 * 512, bytesRead);
    }

    @Test
    public void testClose() throws IOException {
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(new ByteArrayOutputStream(), TarBuffer.DEFAULT_BLKSIZE, TarBuffer.DEFAULT_RCDSIZE);
        TarArchiveEntry archiveEntry = new TarArchiveEntry(new File("testFile"));
        tarArchiveOutputStream.putArchiveEntry(archiveEntry);
        tarArchiveOutputStream.write(new byte[512], 0, 512); // EOF record
        tarArchiveOutputStream.write(new byte[512], 0, 512); // EOF record
        tarArchiveOutputStream.finish();
        tarArchiveOutputStream.close();
        InputStream inputStream = new ByteArrayInputStream(tarArchiveOutputStream.buffer.getBuffer());
        byte[] actualData = new byte[1024];
        int bytesRead = inputStream.read(actualData, 0, actualData.length);
        Assert.assertEquals(2 * 512, bytesRead);
    }
}