import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarConstants;
import org.apache.commons.compress.archivers.tar.TarBuffer;
import org.apache.commons.compress.archivers.tar.TarArchiveOutputStream;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class TarArchiveOutputStreamTest {

    @Test
    public void testConstructor() {
        TarArchiveOutputStream tarOutputStream = new TarArchiveOutputStream(new ByteArrayOutputStream());
        Assert.assertNull(tarOutputStream.getLongFileMode());
        Assert.assertNull(tarOutputStream.getBuffer());
        Assert.assertFalse(tarOutputStream.isClosed());
    }

    @Test
    public void testSetLongFileMode() {
        TarArchiveOutputStream tarOutputStream = new TarArchiveOutputStream(new ByteArrayOutputStream());
        tarOutputStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_TRUNCATE);
        Assert.assertEquals(TarArchiveOutputStream.LONGFILE_TRUNCATE, tarOutputStream.getLongFileMode());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetLongFileModeWithInvalidMode() {
        TarArchiveOutputStream tarOutputStream = new TarArchiveOutputStream(new ByteArrayOutputStream());
        tarOutputStream.setLongFileMode(3);
    }

    @Test
    public void testFinish() throws IOException {
        TarArchiveOutputStream tarOutputStream = createTestStream();
        tarOutputStream.finish();
        TarEntry expectedEOFEntry = new TarEntry(TarConstants.NAME_EOF, TarConstants.FILETYPE_NULL);
        TarEntry actualEOFEntry = tarOutputStream.getNextEntry();
        Assert.assertEquals(expectedEOFEntry, actualEOFEntry);
        Assert.assertEquals(2 * TarConstants.RECORD_SIZE, tarOutputStream.getNextEntry().getSize());
    }

    @Test
    public void testClose() throws IOException {
        TarArchiveOutputStream tarOutputStream = createTestStream();
        tarOutputStream.finish();
        tarOutputStream.close();
        Assert.assertTrue(tarOutputStream.isClosed());
    }

    @Test(expected = IllegalStateException.class)
    public void testPutArchiveEntryAfterClose() throws IOException {
        TarArchiveOutputStream tarOutputStream = createTestStream();
        tarOutputStream.finish();
        tarOutputStream.close();
        tarOutputStream.putArchiveEntry(new TarArchiveEntry("test.txt"));
    }

    @Test(expected = IllegalStateException.class)
    public void testPutArchiveEntryBeforeFinish() throws IOException {
        TarArchiveOutputStream tarOutputStream = createTestStream();
        tarOutputStream.putArchiveEntry(new TarArchiveEntry("test.txt"));
    }

    @Test
    public void testPutArchiveEntryWithValidEntry() throws IOException {
        TarArchiveOutputStream tarOutputStream = createTestStream();
        TarArchiveEntry archiveEntry = new TarArchiveEntry("test.txt");
        tarOutputStream.putArchiveEntry(archiveEntry);
        TarEntry actualEntry = tarOutputStream.getNextEntry();
        Assert.assertEquals(archiveEntry, actualEntry);
        tarOutputStream.write(new byte[10]);
        tarOutputStream.closeArchiveEntry();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testPutArchiveEntryWithNullArchiveEntry() throws IOException {
        TarArchiveOutputStream tarOutputStream = createTestStream();
        tarOutputStream.putArchiveEntry(null);
    }

    @Test(expected = IOException.class)
    public void testPutArchiveEntryWithInvalidArchiveEntry() throws IOException {
        TarArchiveOutputStream tarOutputStream = createTestStream();
        TarEntry archiveEntry = new TarEntry("test.txt", 0, 0);
        tarOutputStream.putArchiveEntry(archiveEntry);
    }

    @Test
    public void testCloseArchiveEntry() throws IOException {
        TarArchiveOutputStream tarOutputStream = createTestStream();
        TarArchiveEntry archiveEntry = new TarArchiveEntry("test.txt");
        tarOutputStream.putArchiveEntry(archiveEntry);
        tarOutputStream.write(new byte[10]);
        TarEntry actualEntry = tarOutputStream.getNextEntry();
        Assert.assertFalse(tarOutputStream.isWritingToEntry());
        tarOutputStream.closeArchiveEntry();
        Assert.assertTrue(tarOutputStream.isWritingToEntry());
        tarOutputStream.write(new byte[10]);
        Assert.assertFalse(tarOutputStream.isWritingToEntry());
    }

    @Test
    public void testWrite() throws IOException {
        TarArchiveOutputStream tarOutputStream = createTestStream();
        byte[] data = new byte[10];
        tarOutputStream.write(data);
        Assert.assertEquals(data.length, tarOutputStream.getNextEntry().getSize());
        Assert.assertArrayEquals(data, tarOutputStream.readAllBytes());
    }

    @Test
    public void testWriteWithPartialData() throws IOException {
        TarArchiveOutputStream tarOutputStream = createTestStream();
        byte[] data = new byte[10];
        tarOutputStream.write(data, 0, 5);
        tarOutputStream.write(data, 5, 5);
        Assert.assertEquals(10, tarOutputStream.getNextEntry().getSize());
        Assert.assertArrayEquals(data, tarOutputStream.readAllBytes());
    }

    @Test(expected = IOException.class)
    public void testWriteWithNegativeOffset() throws IOException {
        TarArchiveOutputStream tarOutputStream = createTestStream();
        byte[] data = new byte[10];
        tarOutputStream.write(data, -1, 5);
    }

    @Test(expected = IOException.class)
    public void testWriteWithNegativeNumberOfBytes() throws IOException {
        TarArchiveOutputStream tarOutputStream = createTestStream();
        byte[] data = new byte[10];
        tarOutputStream.write(data, 0, -5);
    }

    @Test(expected = IOException.class)
    public void testWriteWithNullData() throws IOException {
        TarArchiveOutputStream tarOutputStream = createTestStream();
        tarOutputStream.write(null);
    }

    @Test(expected = IOException.class)
    public void testWriteWithDataLongerThanBuffer() throws IOException {
        TarArchiveOutputStream tarOutputStream = createTestStream();
        byte[] data = new byte[TarConstants.RECORD_SIZE + 1];
        tarOutputStream.write(data);
    }

    @Test
    public void testReadAllBytes() throws IOException {
        TarArchiveOutputStream tarOutputStream = createTestStream();
        byte[] data = new byte[10];
        tarOutputStream.write(data);
        tarOutputStream.closeArchiveEntry();
        tarOutputStream.finish();
        tarOutputStream.close();
        Assert.assertArrayEquals(data, tarOutputStream.readAllBytes());
    }

    @Test
    public void testReadAllBytesWithEmptyStream() throws IOException {
        TarArchiveOutputStream tarOutputStream = createTestStream();
        tarOutputStream.finish();
        tarOutputStream.close();
        Assert.assertArrayEquals(new byte[0], tarOutputStream.readAllBytes());
    }

    @Test(expected = IOException.class)
    public void testReadAllBytesWithNotClosedStream() throws IOException {
        TarArchiveOutputStream tarOutputStream = createTestStream();
        tarOutputStream.write(new byte[10]);
        Assert.assertThrows(IOException.class, () -> tarOutputStream.readAllBytes());
    }

    @Test
    public void testCreateArchiveEntry() throws IOException {
        TarArchiveOutputStream tarOutputStream = createTestStream();
        File file = new File("test.txt");
        TarArchiveEntry archiveEntry = tarOutputStream.createArchiveEntry(file, "test.txt");
        Assert.assertEquals("test.txt", archiveEntry.getName());
        TarEntry actualEntry = tarOutputStream.putArchiveEntry(archiveEntry);
        Assert.assertEquals(archiveEntry, actualEntry);
    }

    @Test(expected = NullPointerException.class)
    public void testCreateArchiveEntryWithNullFile() throws IOException {
        TarArchiveOutputStream tarOutputStream = createTestStream();
        TarArchiveEntry archiveEntry = tarOutputStream.createArchiveEntry(null, "test.txt");
    }

    @Test(expected = NullPointerException.class)
    public void testCreateArchiveEntryWithNullFileName() throws IOException {
        TarArchiveOutputStream tarOutputStream = createTestStream();
        TarArchiveEntry archiveEntry = tarOutputStream.createArchiveEntry(new File("test.txt"), null);
    }

    @Test
    public void testFlush() throws IOException {
        TarArchiveOutputStream tarOutputStream = createTestStream();
        byte[] data = new byte[10];
        tarOutputStream.write(data);
        tarOutputStream.flush();
        tarOutputStream.closeArchiveEntry();
        tarOutputStream.finish();
        tarOutputStream.close();
        TarEntry expectedEOFEntry = new TarEntry(TarConstants.NAME_EOF, TarConstants.FILETYPE_NULL);
        TarEntry actualEOFEntry = tarOutputStream.getNextEntry();
        Assert.assertEquals(expectedEOFEntry, actualEOFEntry);
        Assert.assertEquals(2 * TarConstants.RECORD_SIZE, tarOutputStream.getNextEntry().getSize());
        Assert.assertArrayEquals(data, tarOutputStream.readAllBytes());
    }

    private TarArchiveOutputStream createTestStream() throws IOException {
        TarArchiveOutputStream tarOutputStream = new TarArchiveOutputStream(new ByteArrayOutputStream());
        return tarOutputStream;
    }
}