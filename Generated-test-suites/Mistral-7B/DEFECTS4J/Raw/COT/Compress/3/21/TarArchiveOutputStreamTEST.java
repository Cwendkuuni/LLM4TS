import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarConstants;
import org.apache.commons.compress.archivers.tar.TarBuffer;
import org.apache.commons.compress.archivers.tar.TarArchiveOutputStream;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * TarArchiveOutputStreamTest
 */
public class TarArchiveOutputStreamTest {

    private OutputStream outputStream;
    private TarArchiveOutputStream tarArchiveOutputStream;

    @Before
    public void setUp() throws Exception {
        outputStream = new ByteArrayOutputStream();
        tarArchiveOutputStream = new TarArchiveOutputStream(outputStream);
    }

    @Test
    public void testConstructorWithOutputStream() throws Exception {
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(outputStream);
        Assert.assertNotNull(tarArchiveOutputStream);
    }

    @Test
    public void testConstructorWithOutputStreamAndBlockSize() throws Exception {
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(outputStream, TarBuffer.DEFAULT_BLKSIZE);
        Assert.assertNotNull(tarArchiveOutputStream);
    }

    @Test
    public void testConstructorWithOutputStreamAndBlockSizeAndRecordSize() throws Exception {
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(outputStream, TarBuffer.DEFAULT_BLKSIZE, TarBuffer.DEFAULT_RCDSIZE);
        Assert.assertNotNull(tarArchiveOutputStream);
    }

    @Test
    public void testSetLongFileMode() throws Exception {
        tarArchiveOutputStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_TRUNCATE);
    }

    @Test(expected = RuntimeException.class)
    public void testPutArchiveEntryWithTooLongFileName() throws Exception {
        ArchiveEntry archiveEntry = new TarArchiveEntry("filename".getBytes(java.nio.charset.StandardCharsets.UTF_8), "test.txt");
        tarArchiveOutputStream.putArchiveEntry(archiveEntry);
    }

    @Test
    public void testPutArchiveEntry() throws Exception {
        ArchiveEntry archiveEntry = new TarArchiveEntry("test.txt");
        tarArchiveOutputStream.putArchiveEntry(archiveEntry);
        tarArchiveOutputStream.write("Test file content".getBytes());
        tarArchiveOutputStream.closeArchiveEntry();
    }

    @Test
    public void testWrite() throws Exception {
        ArchiveEntry archiveEntry = new TarArchiveEntry("test.txt");
        tarArchiveOutputStream.putArchiveEntry(archiveEntry);
        byte[] content = "Test file content".getBytes();
        tarArchiveOutputStream.write(content);
        tarArchiveOutputStream.closeArchiveEntry();
        tarArchiveOutputStream.finish();

        byte[] expected = new byte[recordSize() + content.length];
        System.arraycopy(content, 0, expected, 0, content.length);

        byte[] result = ((ByteArrayOutputStream) outputStream).toByteArray();
        Assert.assertArrayEquals(expected, result);
    }

    @Test
    public void testClose() throws Exception {
        ArchiveEntry archiveEntry = new TarArchiveEntry("test.txt");
        tarArchiveOutputStream.putArchiveEntry(archiveEntry);
        tarArchiveOutputStream.write("Test file content".getBytes());
        tarArchiveOutputStream.closeArchiveEntry();
        tarArchiveOutputStream.close();

        Assert.assertThrows(IOException.class, () -> tarArchiveOutputStream.write("Test file content".getBytes()));
    }

    @Test
    public void testFinish() throws Exception {
        ArchiveEntry archiveEntry = new TarArchiveEntry("test.txt");
        tarArchiveOutputStream.putArchiveEntry(archiveEntry);
        tarArchiveOutputStream.write("Test file content".getBytes());
        tarArchiveOutputStream.finish();

        byte[] expected = new byte[2 * TarConstants.RECORD_LENGTH];
        Assert.assertEquals(expected.length, outputStream.size());
    }

    private int recordSize() {
        return tarArchiveOutputStream.getRecordSize();
    }
}