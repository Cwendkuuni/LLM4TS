import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveOutputStream;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class TarArchiveOutputStreamTest {
    private TarArchiveOutputStream tarOutputStream;
    private ByteArrayOutputStream byteArrayOutputStream;

    @Before
    public void setUp() throws IOException {
        byteArrayOutputStream = new ByteArrayOutputStream();
        tarOutputStream = new TarArchiveOutputStream(byteArrayOutputStream);
    }

    @After
    public void tearDown() throws IOException {
        tarOutputStream.close();
        byteArrayOutputStream.close();
    }

    @Test
    public void testSetLongFileMode() {
        tarOutputStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_ERROR);
        assertEquals(TarArchiveOutputStream.LONGFILE_ERROR, tarOutputStream.getLongFileMode());

        tarOutputStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_TRUNCATE);
        assertEquals(TarArchiveOutputStream.LONGFILE_TRUNCATE, tarOutputStream.getLongFileMode());

        tarOutputStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_GNU);
        assertEquals(TarArchiveOutputStream.LONGFILE_GNU, tarOutputStream.getLongFileMode());
    }

    @Test
    public void testPutArchiveEntry() throws IOException {
        TarArchiveEntry entry = new TarArchiveEntry("test.txt");
        tarOutputStream.putArchiveEntry(entry);
        assertTrue(tarOutputStream.haveUnclosedEntry());

        tarOutputStream.closeArchiveEntry();
        assertFalse(tarOutputStream.haveUnclosedEntry());
    }

    @Test(expected = IOException.class)
    public void testPutArchiveEntryAfterFinish() throws IOException {
        tarOutputStream.finish();
        TarArchiveEntry entry = new TarArchiveEntry("test.txt");
        tarOutputStream.putArchiveEntry(entry);
    }

    @Test(expected = IOException.class)
    public void testPutArchiveEntryWithoutClose() throws IOException {
        TarArchiveEntry entry = new TarArchiveEntry("test.txt");
        tarOutputStream.putArchiveEntry(entry);

        tarOutputStream.putArchiveEntry(entry);
    }

    @Test
    public void testCloseArchiveEntry() throws IOException {
        TarArchiveEntry entry = new TarArchiveEntry("test.txt");
        tarOutputStream.putArchiveEntry(entry);
        tarOutputStream.write(new byte[10]);
        tarOutputStream.closeArchiveEntry();

        assertNull(tarOutputStream.getCurrentEntry());
    }

    @Test(expected = IOException.class)
    public void testCloseArchiveEntryWithoutPut() throws IOException {
        tarOutputStream.closeArchiveEntry();
    }

    @Test(expected = IOException.class)
    public void testCloseArchiveEntryAfterFinish() throws IOException {
        tarOutputStream.finish();
        TarArchiveEntry entry = new TarArchiveEntry("test.txt");
        tarOutputStream.putArchiveEntry(entry);
        tarOutputStream.closeArchiveEntry();
    }

    @Test
    public void testWriteBytes() throws IOException {
        byte[] data = "This is a test".getBytes();
        tarOutputStream.write(data);

        assertEquals(data.length, tarOutputStream.getCurrentEntry().getSize());
    }

    @Test
    public void testWritePartialBytes() throws IOException {
        byte[] data = "This is a test".getBytes();
        tarOutputStream.write(data, 0, 5);

        assertEquals(5, tarOutputStream.getCount());
    }

    @Test(expected = IOException.class)
    public void testWriteBytesAfterFinish() throws IOException {
        tarOutputStream.finish();
        byte[] data = "This is a test".getBytes();
        tarOutputStream.write(data);
    }

    @Test(expected = IOException.class)
    public void testWriteBytesWithoutEntry() throws IOException {
        byte[] data = "This is a test".getBytes();
        tarOutputStream.write(data);
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
    public void testFinishWithoutCloseEntry() throws IOException {
        TarArchiveEntry entry = new TarArchiveEntry("test.txt");
        tarOutputStream.putArchiveEntry(entry);

        tarOutputStream.finish();
    }

    @Test(expected = IOException.class)
    public void testClose() throws IOException {
        tarOutputStream.close();

        assertEquals(-1, tarOutputStream.getCount());
    }
}