import static org.junit.Assert.*;
import org.junit.Test;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

public class ArArchiveInputStreamTest {

    @Test
    public void testGetNextArEntry_validInput_returnsValidArEntry() throws IOException {
        // Arrange
        byte[] arEntryData = new byte[52];
        arEntryData[2] = 1; // set an arbitrary value for testing
        ByteArrayInputStream inputStream = new ByteArrayInputStream(arEntryData);
        ArArchiveInputStream arInputStream = new ArArchiveInputStream(inputStream);
        
        // Act
        ArArchiveEntry arEntry = arInputStream.getNextArEntry();
        
        // Assert
        assertNotNull(arEntry);
        assertEquals(1, arEntryData[2]);
    }
    
    @Test
    public void testGetNextArEntry_noMoreEntries_returnsNull() throws IOException {
        // Arrange
        byte[] arEntryData = new byte[52];
        ByteArrayInputStream inputStream = new ByteArrayInputStream(arEntryData);
        ArArchiveInputStream arInputStream = new ArArchiveInputStream(inputStream);
        
        // Act
        ArArchiveEntry arEntry = arInputStream.getNextArEntry();
        
        // Assert
        assertNull(arEntry);
    }
    
    @Test(expected = IOException.class)
    public void testGetNextArEntry_invalidHeader_throwsIOException() throws IOException {
        // Arrange
        byte[] arEntryData = new byte[52];
        ByteArrayInputStream inputStream = new ByteArrayInputStream(arEntryData);
        ArArchiveInputStream arInputStream = new ArArchiveInputStream(inputStream);
        
        // Act
        arInputStream.getNextArEntry();
        
        // Assert
        // Expected IOException to be thrown
    }
    
    @Test
    public void testGetNextEntry_validInput_returnsValidArEntry() throws IOException {
        // Arrange
        byte[] arEntryData = new byte[52];
        arEntryData[2] = 1; // set an arbitrary value for testing
        ByteArrayInputStream inputStream = new ByteArrayInputStream(arEntryData);
        ArArchiveInputStream arInputStream = new ArArchiveInputStream(inputStream);
        
        // Act
        ArchiveEntry archiveEntry = arInputStream.getNextEntry();
        
        // Assert
        assertNotNull(archiveEntry);
        assertEquals(1, arEntryData[2]);
    }
    
    @Test
    public void testGetNextEntry_noMoreEntries_returnsNull() throws IOException {
        // Arrange
        byte[] arEntryData = new byte[52];
        ByteArrayInputStream inputStream = new ByteArrayInputStream(arEntryData);
        ArArchiveInputStream arInputStream = new ArArchiveInputStream(inputStream);
        
        // Act
        ArchiveEntry archiveEntry = arInputStream.getNextEntry();
        
        // Assert
        assertNull(archiveEntry);
    }
    
    @Test
    public void testClose_inputStreamNotClosed_closesInputStream() throws IOException {
        // Arrange
        byte[] arEntryData = new byte[52];
        ByteArrayInputStream inputStream = new ByteArrayInputStream(arEntryData);
        ArArchiveInputStream arInputStream = new ArArchiveInputStream(inputStream);
        
        // Act
        arInputStream.close();
        
        // Assert
        assertTrue(arInputStream.isClosed());
        assertTrue(inputStream.isClosed());
    }
    
    @Test
    public void testRead_availableDataInInputStream_returnsCorrectData() throws IOException {
        // Arrange
        byte[] arEntryData = new byte[52];
        arEntryData[2] = 1; // set an arbitrary value for testing
        ByteArrayInputStream inputStream = new ByteArrayInputStream(arEntryData);
        ArArchiveInputStream arInputStream = new ArArchiveInputStream(inputStream);
        byte[] readData = new byte[52];
        
        // Act
        int bytesRead = arInputStream.read(readData);
        
        // Assert
        assertEquals(52, bytesRead);
        assertEquals(1, readData[2]);
    }
    
    @Test
    public void testRead_noDataInInputStream_returnsNegativeOne() throws IOException {
        // Arrange
        byte[] arEntryData = new byte[0];
        ByteArrayInputStream inputStream = new ByteArrayInputStream(arEntryData);
        ArArchiveInputStream arInputStream = new ArArchiveInputStream(inputStream);
        byte[] readData = new byte[1];
        
        // Act
        int bytesRead = arInputStream.read(readData);
        
        // Assert
        assertEquals(-1, bytesRead);
    }
    
    @Test
    public void testMatches_signatureWithLengthLessThan8_returnsFalse() {
        // Arrange
        byte[] signature = new byte[7];
        
        // Act
        boolean result = ArArchiveInputStream.matches(signature, 7);
        
        // Assert
        assertFalse(result);
    }
    
    @Test
    public void testMatches_signatureWithInvalidData_returnsFalse() {
        // Arrange
        byte[] signature = new byte[8];
        // set an arbitrary value for testing
        signature[6] = 1;
        
        // Act
        boolean result = ArArchiveInputStream.matches(signature, 8);
        
        // Assert
        assertFalse(result);
    }
    
    @Test
    public void testMatches_validSignature_returnsTrue() {
        // Arrange
        byte[] signature = new byte[8];
        // set valid AR signature values
        signature[0] = 0x21;
        signature[1] = 0x3c;
        signature[2] = 0x61;
        signature[3] = 0x72;
        signature[4] = 0x63;
        signature[5] = 0x68;
        signature[6] = 0x3e;
        signature[7] = 0x0a;
        
        // Act
        boolean result = ArArchiveInputStream.matches(signature, 8);
        
        // Assert
        assertTrue(result);
    }
}