import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarConstants;
import org.apache.commons.compress.archivers.tar.TarArchiveOutputStream;
import org.apache.commons.compress.utils.ArchiveUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.Assert.*;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class TarArchiveOutputStreamTest {

    private TarArchiveOutputStream tarOutputStream;
    private ByteArrayOutputStream outputStream;

    @Before
    public void setUp() {
        try {
            outputStream = new ByteArrayOutputStream();
            tarOutputStream = new TarArchiveOutputStream(outputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testConstructor() {
        TarArchiveOutputStream tarOutputStream = new TarArchiveOutputStream(new ByteArrayOutputStream());
        assertNotNull(tarOutputStream);
    }

    @Test
    public void testSetLongFileMode() {
        TarArchiveOutputStream tarOutputStream = new TarArchiveOutputStream(new ByteArrayOutputStream());
        tarOutputStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_TRUNCATE);
        assertEquals(TarArchiveOutputStream.LONGFILE_TRUNCATE, tarOutputStream.getLongFileMode());
    }

    @Test
    public void testFinish() throws IOException {
        tarOutputStream.finish();
        assertTrue(tarOutputStream.isFinished());
    }

    @Test(expected = IOException.class)
    public void testFinishTwice() throws IOException {
        tarOutputStream.finish();
        tarOutputStream.finish();
    }

    @Test(expected = IOException.class)
    public void testFinishWithUnclosedEntry() throws IOException {
        TarArchiveEntry entry = new TarArchiveEntry("test.txt");
        tarOutputStream.putArchiveEntry(entry);
        tarOutputStream.finish();
        assertFalse(tarOutputStream.isFinished());
        tarOutputStream.close();
        tarOutputStream.finish();
        assertTrue(tarOutputStream.isFinished());
    }

    @Test(expected = IOException.class)
    public void testFinishWithoutClosingOutputStream() throws IOException {
        tarOutputStream.finish();
    }

    @Test
    public void testGetRecordSize() {
        assertEquals(TarArchiveOutputStream.DEFAULT_RCDSIZE, tarOutputStream.getRecordSize());
    }

    @Test(expected = IOException.class)
    public void testPutArchiveEntryWithInvalidArchiveEntry() throws IOException {
        assertFalse(tarOutputStream instanceof ArchiveOutputStream);
        tarOutputStream.putArchiveEntry(null);
    }

    @Test(expected = IOException.class)
    public void testPutArchiveEntryWithNullArchiveEntryName() throws IOException {
        TarArchiveEntry archiveEntry = new TarArchiveEntry("test.txt");
        archiveEntry.setName(null);
        tarOutputStream.putArchiveEntry(archiveEntry);
    }

    @Test
    public void testPutArchiveEntry() throws IOException {
        TarArchiveEntry archiveEntry = new TarArchiveEntry("test.txt");
        tarOutputStream.putArchiveEntry(archiveEntry);
        TarArchiveEntry readEntry = (TarArchiveEntry) tarOutputStream.getNextEntry();
        assertEquals("test.txt", readEntry.getName());
    }

    @Test(expected = IOException.class)
    public void testPutArchiveEntryWithLongFileName() throws IOException {
        String longFileName = new String(new char[TarConstants.NAMELEN + 1]);
        TarArchiveEntry archiveEntry = new TarArchiveEntry(longFileName);
        tarOutputStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_GNU);
        tarOutputStream.putArchiveEntry(archiveEntry);
        TarArchiveEntry readEntry = (TarArchiveEntry) tarOutputStream.getNextEntry();
        assertNotNull(readEntry);
        assertEquals(TarConstants.GNU_LONGLINK, readEntry.getHeaderType());
    }

    @Test
    public void testPutArchiveEntryAndClose() throws IOException {
        TarArchiveEntry archiveEntry = new TarArchiveEntry("test.txt");
        tarOutputStream.putArchiveEntry(archiveEntry);
        TarArchiveEntry readEntry = (TarArchiveEntry) tarOutputStream.getNextEntry();
        assertEquals("test.txt", readEntry.getName());
        tarOutputStream.close();
    }

    @Test(expected = IOException.class)
    public void testCloseArchiveEntryWithoutPuttingOne() throws IOException {
        tarOutputStream.closeArchiveEntry();
    }

    @Test
    public void testCloseArchiveEntry() throws IOException {
        TarArchiveEntry archiveEntry = new TarArchiveEntry("test.txt");
        tarOutputStream.putArchiveEntry(archiveEntry);
        tarOutputStream.closeArchiveEntry();
    }

    @Test
    public void testWrite() throws IOException {
        byte[] data = "test data".getBytes(StandardCharsets.UTF_8);
        tarOutputStream.write(data, 0, data.length);
        byte[] readData = outputStream.toByteArray();
        assertArrayEquals(data, Arrays.copyOfRange(readData, 0, data.length));
    }

    @Test
    public void testWriteWithUnclosedEntry() throws IOException {
        byte[] data = "test data".getBytes(StandardCharsets.UTF_8);
        TarArchiveEntry archiveEntry = new TarArchiveEntry("test.txt");
        tarOutputStream.putArchiveEntry(archiveEntry);
        tarOutputStream.write(data, 0, data.length);
        tarOutputStream.closeArchiveEntry();
        byte[] readData = outputStream.toByteArray();
        assertArrayEquals(data, Arrays.copyOfRange(readData, 0, data.length));
    }

    @Test
    public void testWriteWithClosedEntry() throws IOException {
        byte[] data = "test data".getArchive("test.tar", "test.txt");
        TarArchiveEntry archiveEntry = new TarArchiveEntry("test.txt");
        tarOutputStream.putArchiveEntry(archiveEntry);
        tarOutputStream.write(data, 0, data.length);
        tarOutputStream.closeArchiveEntry();
        tarOutputStream.write(data, 0, data.length);
        byte[] readData = outputStream.toByteArray();
        assertArrayEquals(data, Arrays.copyOfRange(readData, 0, data.length));
    }

    @Test
    public void testFlush() throws IOException {
        tarOutputStream.write("test data".getBytes(StandardCharsets.UTF_8));
        tarOutputStream.flush();
    }

    @Test
    public void testCreateArchiveEntry() {
        File file = new File("test.txt");
        TarArchiveEntry archiveEntry = tarOutputStream.createArchiveEntry(file, "test.txt");
        assertEquals("test.txt", archiveEntry.getName());
    }

    @Test(expected = IOException.class)
    public void testWriteWithClosedStream() throws IOException {
        tarOutputStream.finish();
        tarOutputStream.write("test data".getBytes(StandardCharsets.UTF_8));
    }

    @Test(expected = IOException.class)
    public void testPutArchiveEntryWithExistingEntry() throws IOException {
        TarArchiveEntry archiveEntry1 = new TarArchiveEntry("test.txt");
        TarArchiveEntry archiveEntry2 = new TarArchiveEntry("test.txt");
        tarOutputStream.putArchiveEntry(archiveEntry1);
        tarOutputStream.putArchiveEntry(archiveEntry2);
    }

    @Test
    public void testClose() throws IOException {
        tarOutputStream.close();
    }
}