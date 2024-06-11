import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarConstants;
import org.apache.commons.compress.archivers.tar.TarOutputStream;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

public class TarArchiveOutputStreamTest {

    private TarOutputStream tarOutputStream;
    private ByteArrayOutputStream outputStream;

    @Before
    public void setUp() {
        outputStream = new ByteArrayOutputStream();
        tarOutputStream = new TarArchiveOutputStream(outputStream);
    }

    @After
    public void tearDown() throws IOException {
        tarOutputStream.close();
    }

    @Test
    public void testConstructorWithOutputStream() {
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(outputStream);
        assert null != tarArchiveOutputStream;
    }

    @Test
    public void testConstructorWithOutputStreamBlockSize() {
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(outputStream, TarBuffer.DEFAULT_BLKSIZE);
        assert null != tarArchiveOutputStream;
    }

    @Test
    public void testConstructorWithOutputStreamBlockSizeRecordSize() {
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(outputStream, TarBuffer.DEFAULT_BLKSIZE, TarBuffer.DEFAULT_RCDSIZE);
        assert null != tarArchiveOutputStream;
    }

    @Test
    public void testSetLongFileMode() throws IOException {
        tarOutputStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_TRUNCATE);
    }

    @Test(expected = RuntimeException.class)
    public void testSetLongFileModeWithLongFileName() throws IOException {
        final String longFileName = "longFileName".repeat(TarConstants.NAMELEN + 1);
        TarArchiveEntry entry = new TarArchiveEntry(new File("testFile.txt"));
        entry.setName(longFileName);
        tarOutputStream.putArchiveEntry(entry);
        tarOutputStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_ERROR);
    }

    @Test(expected = IOException.class)
    public void testFinishWithoutPutArchiveEntry() throws IOException {
        tarOutputStream.finish();
    }

    @Test(expected = IOException.class)
    public void testFinishWithUnclosedEntry() throws IOException {
        TarArchiveEntry entry = new TarArchiveEntry(new File("testFile.txt"));
        tarOutputStream.putArchiveEntry(entry);
        tarOutputStream.finish();
        assertThrows(IOException.class, () -> tarOutputStream.close());
    }

    @Test
    public void testWriteWithSmallBuffer() throws IOException {
        byte[] buffer = new byte[10];
        tarOutputStream.write(buffer, 0, 5);
        byte[] result = outputStream.toByteArray();
        byte[] expected = new byte[5];
        System.arraycopy(buffer, 0, expected, 0, 5);
        assertEquals(expected, result, 0, 5);
    }

    @Test
    public void testWriteWithLargeBuffer() throws IOException {
        byte[] buffer = new byte[1024];
        tarOutputStream.write(buffer, 0, buffer.length);
        byte[] result = outputStream.toByteArray();
        assertEquals(buffer.length, result.length);
    }

    @Test
    public void testWriteWithAssembling() throws IOException {
        byte[] buffer = new byte[5];
        tarOutputStream.write(buffer, 0, 2);
        tarOutputStream.write(buffer, 0, 3);
        byte[] result = outputStream.toByteArray();
        byte[] expected = new byte[10];
        System.arraycopy(buffer, 0, expected, 0, 5);
        System.arraycopy(buffer, 0, expected, 5, 5);
        assertEquals(expected, result, 0, 10);
    }

    @Test(expected = IOException.class)
    public void testWriteWithSizeExceeded() throws IOException {
        TarArchiveEntry entry = new TarArchiveEntry(new File("testFile.txt"));
        entry.setSize(1024 * 1024); // 1 MB
        tarOutputStream.putArchiveEntry(entry);
        byte[] buffer = new byte[1024];
        tarOutputStream.write(buffer, 0, buffer.length);
        assertThrows(IOException.class, () -> tarOutputStream.write(buffer, 0, buffer.length));
    }

    @Test(expected = IOException.class)
    public void testWriteWithClosedEntry() throws IOException {
        TarArchiveEntry entry = new TarArchiveEntry(new File("testFile.txt"));
        tarOutputStream.putArchiveEntry(entry);
        tarOutputStream.closeArchiveEntry();
        byte[] buffer = new byte[1];
        assertThrows(IOException.class, () -> tarOutputStream.write(buffer, 0, 1));
    }

    @Test
    public void testPutArchiveEntryWithLongFileName() throws IOException {
        String longFileName = "longFileName".repeat(TarConstants.NAMELEN + 1);
        TarArchiveEntry entry = new TarArchiveEntry(new File("testFile.txt"));
        entry.setName(longFileName);
        tarOutputStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_GNU);
        tarOutputStream.putArchiveEntry(entry);
        byte[] result = outputStream.toByteArray();
        String expectedName = new String(result, 0, TarConstants.NAMELEN, StandardCharsets.UTF_8);
        assertEquals(longFileName, expectedName);
    }
}