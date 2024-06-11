import org.junit.Test;
import static org.junit.Assert.*;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import org.apache.commons.compress.archivers.ArchiveEntry;

public class TarArchiveOutputStreamTest {

    @Test
    public void testPutArchiveEntry() throws IOException {
        OutputStream os = new ByteArrayOutputStream();
        TarArchiveOutputStream tarOs = new TarArchiveOutputStream(os);

        ArchiveEntry entry = new TarArchiveEntry("file.txt");
        tarOs.putArchiveEntry(entry);
        tarOs.closeArchiveEntry();

        assertEquals(0, tarOs.getRecordSize());
    }

    @Test
    public void testCloseArchiveEntry() throws IOException {
        OutputStream os = new ByteArrayOutputStream();
        TarArchiveOutputStream tarOs = new TarArchiveOutputStream(os);

        ArchiveEntry entry = new TarArchiveEntry("file.txt");
        tarOs.putArchiveEntry(entry);
        tarOs.closeArchiveEntry();

        assertFalse(tarOs.haveUnclosedEntry);
    }

    @Test
    public void testWrite() throws IOException {
        OutputStream os = new ByteArrayOutputStream();
        TarArchiveOutputStream tarOs = new TarArchiveOutputStream(os);

        ArchiveEntry entry = new TarArchiveEntry("file.txt");
        tarOs.putArchiveEntry(entry);
        tarOs.write("content".getBytes());
        tarOs.closeArchiveEntry();
        tarOs.close();

        assertEquals("content", os.toString());
    }

    @Test
    public void testFinish() throws IOException {
        OutputStream os = new ByteArrayOutputStream();
        TarArchiveOutputStream tarOs = new TarArchiveOutputStream(os);

        ArchiveEntry entry = new TarArchiveEntry("file.txt");
        tarOs.putArchiveEntry(entry);
        tarOs.write("content".getBytes());
        tarOs.closeArchiveEntry();
        tarOs.finish();
        tarOs.close();

        assertEquals("content\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0", os.toString());
    }

    @Test
    public void testClose() throws IOException {
        OutputStream os = new ByteArrayOutputStream();
        TarArchiveOutputStream tarOs = new TarArchiveOutputStream(os);

        ArchiveEntry entry = new TarArchiveEntry("file.txt");
        tarOs.putArchiveEntry(entry);
        tarOs.write("content".getBytes());
        tarOs.closeArchiveEntry();
        tarOs.close();

        assertTrue(tarOs.closed);
    }

    @Test
    public void testSetLongFileMode() throws IOException {
        OutputStream os = new ByteArrayOutputStream();
        TarArchiveOutputStream tarOs = new TarArchiveOutputStream(os);

        tarOs.setLongFileMode(TarArchiveOutputStream.LONGFILE_ERROR);

        assertEquals(TarArchiveOutputStream.LONGFILE_ERROR, tarOs.longFileMode);
    }
}