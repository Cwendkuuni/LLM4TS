import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveOutputStream;
import org.junit.Test;

public class TarArchiveOutputStreamTest {

    @Test
    public void testConstructor() {
        OutputStream os = new ByteArrayOutputStream();
        TarArchiveOutputStream tar = new TarArchiveOutputStream(os);
        assertNotNull(tar);
    }

    @Test
    public void testSetLongFileMode() {
        OutputStream os = new ByteArrayOutputStream();
        TarArchiveOutputStream tar = new TarArchiveOutputStream(os);
        tar.setLongFileMode(0);
        assertEquals(0, tar.longFileMode);
    }

    @Test
    public void testGetRecordSize() {
        OutputStream os = new ByteArrayOutputStream();
        TarArchiveOutputStream tar = new TarArchiveOutputStream(os);
        assertEquals(TarBuffer.DEFAULT_RCDSIZE, tar.getRecordSize());
    }

    @Test
    public void testPutArchiveEntry() throws IOException {
        OutputStream os = new ByteArrayOutputStream();
        TarArchiveOutputStream tar = new TarArchiveOutputStream(os);

        File file = new File("test.txt");
        TarArchiveEntry entry = new TarArchiveEntry(file, "test.txt");
        tar.putArchiveEntry(entry);

        assertFalse(tar.haveUnclosedEntry);
        tar.closeArchiveEntry();
        assertFalse(tar.haveUnclosedEntry);
    }

    @Test(expected = RuntimeException.class)
    public void testPutArchiveEntry_LongFileNameError() throws IOException {
        OutputStream os = new ByteArrayOutputStream();
        TarArchiveOutputStream tar = new TarArchiveOutputStream(os);
        tar.setLongFileMode(0);

        File file = new File("thisisaverylongfilename.txt");
        TarArchiveEntry entry = new TarArchiveEntry(file, "thisisaverylongfilename.txt");
        tar.putArchiveEntry(entry);
    }

    @Test
    public void testCloseArchiveEntry() throws IOException {
        OutputStream os = new ByteArrayOutputStream();
        TarArchiveOutputStream tar = new TarArchiveOutputStream(os);

        File file = new File("test.txt");
        TarArchiveEntry entry = new TarArchiveEntry(file, "test.txt");
        tar.putArchiveEntry(entry);

        tar.closeArchiveEntry();
        assertFalse(tar.haveUnclosedEntry);
    }

    @Test(expected = IOException.class)
    public void testCloseArchiveEntry_Exception() throws IOException {
        OutputStream os = new ByteArrayOutputStream();
        TarArchiveOutputStream tar = new TarArchiveOutputStream(os);

        tar.closeArchiveEntry();
    }

    @Test
    public void testWrite() throws IOException {
        OutputStream os = new ByteArrayOutputStream();
        TarArchiveOutputStream tar = new TarArchiveOutputStream(os);

        File file = new File("test.txt");
        TarArchiveEntry entry = new TarArchiveEntry(file, "test.txt");
        tar.putArchiveEntry(entry);

        byte[] content = "This is a test".getBytes("UTF-8");
        tar.write(content, 0, content.length);

        tar.closeArchiveEntry();
        assertFalse(tar.haveUnclosedEntry);
    }

    @Test
    public void testFinish() throws IOException {
        OutputStream os = new ByteArrayOutputStream();
        TarArchiveOutputStream tar = new TarArchiveOutputStream(os);

        tar.finish();

        assertEquals(2 * TarBuffer.DEFAULT_RCDSIZE, os.size());
    }

    @Test
    public void testClose() throws IOException {
        OutputStream os = new ByteArrayOutputStream();
        TarArchiveOutputStream tar = new TarArchiveOutputStream(os);

        tar.close();

        assertTrue(tar.closed);
    }

    @Test
    public void testCreateArchiveEntry() throws IOException {
        OutputStream os = new ByteArrayOutputStream();
        TarArchiveOutputStream tar = new TarArchiveOutputStream(os);

        File file = new File("test.txt");
        ArchiveEntry entry = tar.createArchiveEntry(file, "test.txt");
        assertNotNull(entry);
    }

}