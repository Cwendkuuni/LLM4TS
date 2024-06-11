import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveOutputStream;

public class TarArchiveOutputStreamTest {
    
    private TarArchiveOutputStream tarOutputStream;
    private ByteArrayOutputStream outputStream;

    @Before
    public void setUp() {
        outputStream = new ByteArrayOutputStream();
        tarOutputStream = new TarArchiveOutputStream(outputStream);
    }

    @Test
    public void testPutArchiveEntry() throws IOException {
        ArchiveEntry entry = tarOutputStream.createArchiveEntry(new File("test.txt"), "test.txt");
        tarOutputStream.putArchiveEntry(entry);
        tarOutputStream.closeArchiveEntry();
        assertNotNull(entry);
    }

    @Test
    public void testCloseArchiveEntry() throws IOException {
        tarOutputStream.putArchiveEntry(tarOutputStream.createArchiveEntry(new File("test.txt"), "test.txt"));
        tarOutputStream.closeArchiveEntry();
        assertEquals(0, outputStream.size());
    }

    @Test
    public void testWrite() throws IOException {
        byte[] data = "Hello, world!".getBytes();
        tarOutputStream.putArchiveEntry(tarOutputStream.createArchiveEntry(new File("test.txt"), "test.txt"));
        tarOutputStream.write(data, 0, data.length);
        tarOutputStream.closeArchiveEntry();
        assertEquals("Hello, world!", new String(outputStream.toByteArray()));
    }

    @Test(expected = IOException.class)
    public void testWriteExceedingSize() throws IOException {
        byte[] data = "Too long data".getBytes();
        tarOutputStream.putArchiveEntry(tarOutputStream.createArchiveEntry(new File("test.txt"), "test.txt"));
        tarOutputStream.write(data, 0, data.length);
        tarOutputStream.closeArchiveEntry();
    }

    @Test
    public void testSetLongFileMode() {
        tarOutputStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_GNU);
        assertEquals(TarArchiveOutputStream.LONGFILE_GNU, tarOutputStream.getLongFileMode());
    }

    @Test
    public void testFinish() throws IOException {
        tarOutputStream.finish();
        outputStream.close();
        assertEquals(1024, outputStream.size());
    }

    @Test
    public void testClose() throws IOException {
        tarOutputStream.close();
        assertTrue(outputStream.size() == 1024);
    }

}