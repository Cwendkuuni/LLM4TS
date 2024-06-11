import org.junit.Test;
import static org.junit.Assert.*;

public class CpioArchiveOutputStreamTest {
    
    @Test
    public void testConstructorWithFormat() {
        // Given
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        
        // When
        CpioArchiveOutputStream cpioOutputStream = new CpioArchiveOutputStream(out, CpioArchiveOutputStream.FORMAT_NEW);
        
        // Then
        assertNotNull(cpioOutputStream);
    }
    
    @Test
    public void testConstructorWithoutFormat() {
        // Given
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        
        // When
        CpioArchiveOutputStream cpioOutputStream = new CpioArchiveOutputStream(out);
        
        // Then
        assertNotNull(cpioOutputStream);
    }
    
    @Test
    public void testPutNextEntry() {
        // Given
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        CpioArchiveOutputStream cpioOutputStream = new CpioArchiveOutputStream(out);
        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setName("testfile");
        entry.setFileSize(5);
        
        // When
        cpioOutputStream.putNextEntry(entry);
        cpioOutputStream.write("12345".getBytes());
        
        // Then
        assertNotNull(cpioOutputStream);
    }
    
    @Test
    public void testWrite() {
        // Given
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        CpioArchiveOutputStream cpioOutputStream = new CpioArchiveOutputStream(out);
        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setName("testfile");
        entry.setFileSize(5);
        cpioOutputStream.putNextEntry(entry);
        
        // When
        cpioOutputStream.write("12345".getBytes(), 0, 5);
        
        // Then
        assertNotNull(cpioOutputStream);
    }
    
    @Test
    public void testFinish() {
        // Given
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        CpioArchiveOutputStream cpioOutputStream = new CpioArchiveOutputStream(out);
        
        // When
        cpioOutputStream.finish();
        
        // Then
        assertNotNull(cpioOutputStream);
    }
    
    @Test
    public void testCloseArchiveEntry() {
        // Given
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        CpioArchiveOutputStream cpioOutputStream = new CpioArchiveOutputStream(out);
        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setName("testfile");
        entry.setFileSize(5);
        cpioOutputStream.putNextEntry(entry);
        
        // When
        cpioOutputStream.closeArchiveEntry();
        
        // Then
        assertNotNull(cpioOutputStream);
    }
    
    @Test
    public void testClose() {
        // Given
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        CpioArchiveOutputStream cpioOutputStream = new CpioArchiveOutputStream(out);
        
        // When
        cpioOutputStream.close();
        
        // Then
        assertNotNull(cpioOutputStream);
    }
}