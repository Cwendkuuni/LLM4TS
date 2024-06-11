import static org.junit.Assert.*;
import org.junit.Test;
import java.io.File;
import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.io.IOException;

public class TarArchiveOutputStreamTest {

    // Test putArchiveEntry method
    @Test
    public void testPutArchiveEntry() throws IOException {
        TarArchiveOutputStream tarOS = new TarArchiveOutputStream(new ByteArrayOutputStream());
        TarArchiveEntry entry = new TarArchiveEntry("test.txt");
        
        tarOS.putArchiveEntry(entry);
        
        assertEquals("test.txt", tarOS.currName);
    }

    // Test closeArchiveEntry method
    @Test
    public void testCloseArchiveEntry() throws IOException {
        TarArchiveOutputStream tarOS = new TarArchiveOutputStream(new ByteArrayOutputStream());
        TarArchiveEntry entry = new TarArchiveEntry("test.txt");
        
        tarOS.putArchiveEntry(entry);
        tarOS.closeArchiveEntry();
        
        assertEquals(0, tarOS.assemLen);
    }

    // Test write method
    @Test
    public void testWrite() throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        TarArchiveOutputStream tarOS = new TarArchiveOutputStream(baos);
        TarArchiveEntry entry = new TarArchiveEntry("test.txt");
        
        tarOS.putArchiveEntry(entry);
        tarOS.write("test data".getBytes());
        tarOS.closeArchiveEntry();
        tarOS.close();
        
        assertEquals("test data", new String(baos.toByteArray()));
    }

    // Test finish method
    @Test
    public void testFinish() throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        TarArchiveOutputStream tarOS = new TarArchiveOutputStream(baos);
        TarArchiveEntry entry = new TarArchiveEntry("test.txt");
        
        tarOS.putArchiveEntry(entry);
        tarOS.finish();
        tarOS.close();
        
        assertEquals(1024, baos.toByteArray().length);
    }

    // Test setLongFileMode method
    @Test
    public void testSetLongFileMode() {
        TarArchiveOutputStream tarOS = new TarArchiveOutputStream(new ByteArrayOutputStream());
        
        tarOS.setLongFileMode(TarArchiveOutputStream.LONGFILE_ERROR);
        assertEquals(TarArchiveOutputStream.LONGFILE_ERROR, tarOS.longFileMode);
    }

    // Test getRecordSize method
    @Test
    public void testGetRecordSize() {
        TarArchiveOutputStream tarOS = new TarArchiveOutputStream(new ByteArrayOutputStream());
        
        assertEquals(TarBuffer.DEFAULT_RCDSIZE, tarOS.getRecordSize());
    }

    // Test close method
    @Test
    public void testClose() throws IOException {
        TarArchiveOutputStream tarOS = new TarArchiveOutputStream(new ByteArrayOutputStream());
        
        tarOS.close();
        
        assertTrue(tarOS.closed);
    }
}