import static org.junit.Assert.*;
import org.junit.Test;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class ZipArchiveInputStreamTest {
    
    @Test
    public void testGetNextZipEntry_shouldReturnNextEntry() throws IOException {
        byte[] buffer = new byte[4096];
        String fileName = "test_file.zip";
        byte[] fileContent = "This is a test file.".getBytes();
        byte[] zipContent = createZipFile(fileName, fileContent);
        ByteArrayInputStream input = new ByteArrayInputStream(zipContent);
        ZipArchiveInputStream zipInput = new ZipArchiveInputStream(input);
        
        ZipEntry entry = zipInput.getNextZipEntry();
        
        assertEquals(entry.getName(), fileName);
    }
    
    @Test
    public void testRead_shouldReadFileContents() throws IOException {
        byte[] buffer = new byte[4096];
        String fileName = "test_file.zip";
        byte[] fileContent = "This is a test file.".getBytes();
        byte[] zipContent = createZipFile(fileName, fileContent);
        ByteArrayInputStream input = new ByteArrayInputStream(zipContent);
        ZipArchiveInputStream zipInput = new ZipArchiveInputStream(input);
        
        ZipEntry entry = zipInput.getNextZipEntry();
        int bytesRead = zipInput.read(buffer);
        
        String content = new String(buffer, 0, bytesRead);
        assertEquals(content, new String(fileContent));
    }
    
    @Test
    public void testRead_shouldReturnMinusOneWhenEndOfFileIsReached() throws IOException {
        byte[] buffer = new byte[4096];
        String fileName = "test_file.zip";
        byte[] fileContent = "This is a test file.".getBytes();
        byte[] zipContent = createZipFile(fileName, fileContent);
        ByteArrayInputStream input = new ByteArrayInputStream(zipContent);
        ZipArchiveInputStream zipInput = new ZipArchiveInputStream(input);
        
        ZipEntry entry = zipInput.getNextZipEntry();
        int bytesRead = zipInput.read(buffer);
        
        assertEquals(bytesRead, -1);
    }
    
    @Test
    public void testClose_shouldCloseInputStream() throws IOException {
        byte[] buffer = new byte[4096];
        String fileName = "test_file.zip";
        byte[] fileContent = "This is a test file.".getBytes();
        byte[] zipContent = createZipFile(fileName, fileContent);
        ByteArrayInputStream input = new ByteArrayInputStream(zipContent);
        ZipArchiveInputStream zipInput = new ZipArchiveInputStream(input);
        
        zipInput.close();
        
        try {
            int bytesRead = zipInput.read(buffer);
            fail("IOException not thrown");
        } catch (IOException e) {
            assertTrue(true);
        }
    }
    
    
    private byte[] createZipFile(String fileName, byte[] fileContent) throws IOException {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        ZipOutputStream zipOutput = new ZipOutputStream(output);
        ZipEntry entry = new ZipEntry(fileName);
        zipOutput.putNextEntry(entry);
        zipOutput.write(fileContent);
        zipOutput.closeEntry();
        zipOutput.close();
        return output.toByteArray();
    }
}