import static org.junit.Assert.*;
import org.junit.Test;
import org.apache.commons.compress.archivers.tar.TarArchiveOutputStream;
import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class TarArchiveOutputStreamTest {

    @Test
    public void testSetLongFileMode() {
        TarArchiveOutputStream tarOutput = new TarArchiveOutputStream(new ByteArrayOutputStream());
        tarOutput.setLongFileMode(TarArchiveOutputStream.LONGFILE_ERROR);
        assertEquals(TarArchiveOutputStream.LONGFILE_ERROR, tarOutput.getLongFileMode());
    }

    @Test
    public void testPutArchiveEntry() throws IOException {
        TarArchiveOutputStream tarOutput = new TarArchiveOutputStream(new ByteArrayOutputStream());
        TarArchiveEntry entry = new TarArchiveEntry("test-file.txt");
        tarOutput.putArchiveEntry(entry);
        tarOutput.write("test".getBytes());
        tarOutput.closeArchiveEntry();
        assertEquals("test-file.txt", entry.getName());
        assertEquals(4, entry.getSize());
    }

    @Test
    public void testCloseArchiveEntry() throws IOException {
        TarArchiveOutputStream tarOutput = new TarArchiveOutputStream(new ByteArrayOutputStream());
        TarArchiveEntry entry = new TarArchiveEntry("test-file.txt");
        tarOutput.putArchiveEntry(entry);
        tarOutput.write("test".getBytes());
        tarOutput.closeArchiveEntry();
        assertFalse(tarOutput.haveUnclosedEntry);
    }

    @Test
    public void testWrite() throws IOException {
        TarArchiveOutputStream tarOutput = new TarArchiveOutputStream(new ByteArrayOutputStream());
        TarArchiveEntry entry = new TarArchiveEntry("test-file.txt");
        tarOutput.putArchiveEntry(entry);
        tarOutput.write("test".getBytes(), 0, 2);
        tarOutput.write("test".getBytes(), 2, 2);
        tarOutput.closeArchiveEntry();
        assertEquals(4, entry.getSize());
    }

    @Test
    public void testFinish() throws IOException {
        TarArchiveOutputStream tarOutput = new TarArchiveOutputStream(new ByteArrayOutputStream());
        tarOutput.finish();
        // Test that finish doesn't throw an exception when there are no unclosed entries
    }

    @Test
    public void testClose() throws IOException {
        ByteArrayOutputStream byteArrayOutput = new ByteArrayOutputStream();
        TarArchiveOutputStream tarOutput = new TarArchiveOutputStream(byteArrayOutput);
        tarOutput.close();
        // Test that close doesn't throw an exception
    }

}