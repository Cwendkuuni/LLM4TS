import static org.junit.Assert.*;
import org.junit.Test;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class ZipArchiveInputStreamTest {

    @Test
    public void testGetNextZipEntry() throws IOException {
        byte[] zipFileData = createZipFileData();
        InputStream inputStream = new ByteArrayInputStream(zipFileData);
        ZipArchiveInputStream archiveInputStream = new ZipArchiveInputStream(inputStream);
        
        ZipArchiveEntry entry = archiveInputStream.getNextZipEntry();
        assertEquals("file1.txt", entry.getName());
        assertEquals(10, entry.getSize());
        assertEquals(0, entry.getMethod());
        
        entry = archiveInputStream.getNextZipEntry();
        assertEquals("file2.txt", entry.getName());
        assertEquals(20, entry.getSize());
        assertEquals(0, entry.getMethod());
        
        entry = archiveInputStream.getNextZipEntry();
        assertNull(entry);
        
        archiveInputStream.close();
    }
    
    @Test
    public void testRead() throws IOException {
        byte[] zipFileData = createZipFileData();
        InputStream inputStream = new ByteArrayInputStream(zipFileData);
        ZipArchiveInputStream archiveInputStream = new ZipArchiveInputStream(inputStream);
        
        ZipArchiveEntry entry = archiveInputStream.getNextZipEntry();
        
        byte[] buffer = new byte[10];
        
        int bytesRead = archiveInputStream.read(buffer, 0, 10);
        assertEquals(10, bytesRead);
        assertEquals("file1.txt", new String(buffer, StandardCharsets.UTF_8));
        
        archiveInputStream.close();
    }
    
    private byte[] createZipFileData() {
        byte[] file1Data = "file1.txt".getBytes(StandardCharsets.UTF_8);
        byte[] file2Data = "file2.txt".getBytes(StandardCharsets.UTF_8);
        
        byte[] zipFileData = new byte[30 + file1Data.length + file2Data.length];
        
        // Local file header - file1.txt
        zipFileData[0] = 0x50; // signature - 0x04034b50
        zipFileData[1] = 0x4B;
        zipFileData[2] = 0x03;
        zipFileData[3] = 0x04;
        
        zipFileData[26] = (byte) (file1Data.length & 0xFF); // filename length - 10
        zipFileData[27] = (byte) ((file1Data.length >> 8) & 0xFF);
        
        System.arraycopy(file1Data, 0, zipFileData, 30, file1Data.length);
        
        // Local file header - file2.txt
        zipFileData[30 + file1Data.length] = 0x50; // signature - 0x04034b50
        zipFileData[31 + file1Data.length] = 0x4B;  
        zipFileData[32 + file1Data.length] = 0x03;
        zipFileData[33 + file1Data.length] = 0x04;
        
        zipFileData[56 + file1Data.length] = (byte) (file2Data.length & 0xFF); // filename length - 10
        zipFileData[57 + file1Data.length] = (byte) ((file2Data.length >> 8) & 0xFF);
        
        System.arraycopy(file2Data, 0, zipFileData, 60 + file1Data.length, file2Data.length);
        
        return zipFileData;
    }
}