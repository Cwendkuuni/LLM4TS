import org.junit.Test;
import org.junit.Assert;
import java.io.ByteArrayInputStream;
import java.io.IOException;

public class ArArchiveInputStreamTest {

    @Test
    public void testGetNextArEntry() throws IOException {
        byte[] inputBytes = {0x21, 0x3c, 0x61, 0x72, 0x63, 0x68, 0x3e, 0x0a, 
                             0x74, 0x65, 0x73, 0x74, 0x0a};
        ByteArrayInputStream inputStream = new ByteArrayInputStream(inputBytes);
        
        ArArchiveInputStream archiveInputStream = new ArArchiveInputStream(inputStream);
        ArArchiveEntry entry = archiveInputStream.getNextArEntry();
        
        Assert.assertNotNull(entry);
        Assert.assertEquals("test", entry.getName());
        Assert.assertEquals(4, entry.getLength());
    }
    
    @Test
    public void testGetNextEntry() throws IOException {
        byte[] inputBytes = {0x21, 0x3c, 0x61, 0x72, 0x63, 0x68, 0x3e, 0x0a, 
                             0x74, 0x65, 0x73, 0x74, 0x0a};
        ByteArrayInputStream inputStream = new ByteArrayInputStream(inputBytes);
        
        ArArchiveInputStream archiveInputStream = new ArArchiveInputStream(inputStream);
        ArchiveEntry entry = archiveInputStream.getNextEntry();
        
        Assert.assertNotNull(entry);
        Assert.assertTrue(entry instanceof ArArchiveEntry);
        Assert.assertEquals("test", entry.getName());
        Assert.assertEquals(4, entry.getLength());
    }
    
    @Test
    public void testClose() throws IOException {
        byte[] inputBytes = {0x21, 0x3c, 0x61, 0x72, 0x63, 0x68, 0x3e, 0x0a};
        ByteArrayInputStream inputStream = new ByteArrayInputStream(inputBytes);
        
        ArArchiveInputStream archiveInputStream = new ArArchiveInputStream(inputStream);
        
        archiveInputStream.close();
        
        Assert.assertTrue(archiveInputStream.isClosed());
    }
    
    @Test
    public void testRead() throws IOException {
        byte[] inputBytes = {0x21, 0x3c, 0x61, 0x72, 0x63, 0x68, 0x3e, 0x0a};
        ByteArrayInputStream inputStream = new ByteArrayInputStream(inputBytes);
        
        ArArchiveInputStream archiveInputStream = new ArArchiveInputStream(inputStream);
        
        int bytesRead = archiveInputStream.read();
        
        Assert.assertEquals(0x21, bytesRead);
        Assert.assertEquals(1, archiveInputStream.getOffset());
    }
    
    @Test
    public void testReadByteArray() throws IOException {
        byte[] inputBytes = {0x21, 0x3c, 0x61, 0x72, 0x63, 0x68, 0x3e, 0x0a};
        ByteArrayInputStream inputStream = new ByteArrayInputStream(inputBytes);
        
        ArArchiveInputStream archiveInputStream = new ArArchiveInputStream(inputStream);
        
        byte[] buffer = new byte[8];
        int bytesRead = archiveInputStream.read(buffer);
        
        Assert.assertEquals(8, bytesRead);
        Assert.assertEquals(8, archiveInputStream.getOffset());
        Assert.assertEquals(0x21, buffer[0]);
        Assert.assertEquals(0x3c, buffer[1]);
        Assert.assertEquals(0x61, buffer[2]);
        Assert.assertEquals(0x72, buffer[3]);
        Assert.assertEquals(0x63, buffer[4]);
        Assert.assertEquals(0x68, buffer[5]);
        Assert.assertEquals(0x3e, buffer[6]);
        Assert.assertEquals(0x0a, buffer[7]);
    }
    
    @Test
    public void testReadByteArrayWithOffset() throws IOException {
        byte[] inputBytes = {0x21, 0x3c, 0x61, 0x72, 0x63, 0x68, 0x3e, 0x0a};
        ByteArrayInputStream inputStream = new ByteArrayInputStream(inputBytes);
        
        ArArchiveInputStream archiveInputStream = new ArArchiveInputStream(inputStream);
        
        byte[] buffer = new byte[8];
        int bytesRead = archiveInputStream.read(buffer, 2, 4);
        
        Assert.assertEquals(4, bytesRead);
        Assert.assertEquals(4, archiveInputStream.getOffset());
        Assert.assertEquals(0x00, buffer[0]);
        Assert.assertEquals(0x00, buffer[1]);
        Assert.assertEquals(0x21, buffer[2]);
        Assert.assertEquals(0x3c, buffer[3]);
        Assert.assertEquals(0x00, buffer[4]);
        Assert.assertEquals(0x00, buffer[5]);
        Assert.assertEquals(0x00, buffer[6]);
        Assert.assertEquals(0x00, buffer[7]);
    }
    
    @Test
    public void testMatches() {
        byte[] signature = {0x21, 0x3c, 0x61, 0x72, 0x63, 0x68, 0x3e, 0x0a};
        int length = 8;
        
        boolean matches = ArArchiveInputStream.matches(signature, length);
        
        Assert.assertTrue(matches);
    }
}