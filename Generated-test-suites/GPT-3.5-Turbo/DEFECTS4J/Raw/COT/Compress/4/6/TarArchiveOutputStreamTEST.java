
import org.junit.Test;
import org.apache.commons.compress.archivers.tar.TarArchiveOutputStream;
import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.ArchiveEntry;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.File;
import org.junit.Assert;

public class TarArchiveOutputStreamTest {

    @Test
    public void testPutArchiveEntry() throws IOException {
        OutputStream os = new ByteArrayOutputStream();
        TarArchiveOutputStream tarOutput = new TarArchiveOutputStream(os);
        File file = new File("test.txt");
        ArchiveEntry entry = new TarArchiveEntry(file, "test.txt");
        tarOutput.putArchiveEntry(entry);
        tarOutput.closeArchiveEntry();
    }
    
    @Test
    public void testCloseArchiveEntry() throws IOException {
        OutputStream os = new ByteArrayOutputStream();
        TarArchiveOutputStream tarOutput = new TarArchiveOutputStream(os);
        File file = new File("test.txt");
        ArchiveEntry entry = new TarArchiveEntry(file, "test.txt");
        tarOutput.putArchiveEntry(entry);
        tarOutput.closeArchiveEntry();
    }
    
    @Test
    public void testWrite() throws IOException {
        OutputStream os = new ByteArrayOutputStream();
        TarArchiveOutputStream tarOutput = new TarArchiveOutputStream(os);
        File file = new File("test.txt");
        ArchiveEntry entry = new TarArchiveEntry(file, "test.txt");
        tarOutput.putArchiveEntry(entry);
        byte[] data = "This is a test".getBytes();
        tarOutput.write(data);
        tarOutput.closeArchiveEntry();
    }
    
    @Test
    public void testWriteWithOffset() throws IOException {
        OutputStream os = new ByteArrayOutputStream();
        TarArchiveOutputStream tarOutput = new TarArchiveOutputStream(os);
        File file = new File("test.txt");
        ArchiveEntry entry = new TarArchiveEntry(file, "test.txt");
        tarOutput.putArchiveEntry(entry);
        byte[] data = "This is a test".getBytes();
        tarOutput.write(data, 5, 7);
        tarOutput.closeArchiveEntry();
    }
    
    @Test
    public void testFinish() throws IOException {
        OutputStream os = new ByteArrayOutputStream();
        TarArchiveOutputStream tarOutput = new TarArchiveOutputStream(os);
        tarOutput.finish();
    }
    
    @Test
    public void testClose() throws IOException {
        OutputStream os = new ByteArrayOutputStream();
        TarArchiveOutputStream tarOutput = new TarArchiveOutputStream(os);
        tarOutput.close();
    }
    
    @Test
    public void testSetLongFileMode() {
        OutputStream os = new ByteArrayOutputStream();
        TarArchiveOutputStream tarOutput = new TarArchiveOutputStream(os);
        tarOutput.setLongFileMode(0);
    }
    
    @Test
    public void testGetRecordSize() {
        OutputStream os = new ByteArrayOutputStream();
        TarArchiveOutputStream tarOutput = new TarArchiveOutputStream(os);
        int recordSize = tarOutput.getRecordSize();
        Assert.assertEquals(recordSize, 1024);
    }
    
    @Test
    public void testCreateArchiveEntry() throws IOException {
        OutputStream os = new ByteArrayOutputStream();
        TarArchiveOutputStream tarOutput = new TarArchiveOutputStream(os);
        File file = new File("test.txt");
        ArchiveEntry entry = tarOutput.createArchiveEntry(file, "test.txt");
        Assert.assertNotNull(entry);
        Assert.assertEquals(entry.getName(), "test.txt");
    }
}

