import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveOutputStream;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

import static org.junit.Assert.*;

public class TarArchiveOutputStreamTest {
    
    private TarArchiveOutputStream tarArchiveOutputStream;
    private ByteArrayOutputStream outputStream;
    
    @Before
    public void setUp() {
        outputStream = new ByteArrayOutputStream();
        tarArchiveOutputStream = new TarArchiveOutputStream(outputStream);
    }
    
    @After
    public void tearDown() throws IOException {
        tarArchiveOutputStream.close();
    }
    
    @Test
    public void testPutArchiveEntry_success() throws IOException {
        // Arrange
        TarArchiveEntry entry = new TarArchiveEntry("testFile.txt");
        
        // Act
        tarArchiveOutputStream.putArchiveEntry(entry);
        
        // Assert
        assertNotNull(tarArchiveOutputStream.getCurrentEntry());
        assertEquals("testFile.txt", tarArchiveOutputStream.getCurrentEntry().getName());
    }
    
    @Test
    public void testCloseArchiveEntry_success() throws IOException {
        // Arrange
        TarArchiveEntry entry = new TarArchiveEntry("testFile.txt");
        tarArchiveOutputStream.putArchiveEntry(entry);
        
        // Act
        tarArchiveOutputStream.closeArchiveEntry();
        
        // Assert
        assertNull(tarArchiveOutputStream.getCurrentEntry());
        assertFalse(tarArchiveOutputStream.haveUnclosedEntry);
    }
    
    @Test(expected = IOException.class)
    public void testCloseArchiveEntry_entryNotClosed_failure() throws IOException {
        // Arrange
        TarArchiveEntry entry = new TarArchiveEntry("testFile.txt");
        tarArchiveOutputStream.putArchiveEntry(entry);
        
        // Act
        tarArchiveOutputStream.close(); // Closing the archive without closing the entry
        
        // Assert
        // Expect IOException
    }
    
    @Test
    public void testWrite_success() throws IOException {
        // Arrange
        TarArchiveEntry entry = new TarArchiveEntry("testFile.txt");
        tarArchiveOutputStream.putArchiveEntry(entry);
        byte[] data = "Some test data".getBytes();
        
        // Act
        tarArchiveOutputStream.write(data);
        
        // Assert
        assertEquals(data.length, tarArchiveOutputStream.getCurrentEntry().getSize());
        assertEquals(data.length, tarArchiveOutputStream.getCurrentEntry().getBytesWritten());
    }
    
    @Test(expected = IOException.class)
    public void testWrite_exceedsEntrySize_failure() throws IOException {
        // Arrange
        TarArchiveEntry entry = new TarArchiveEntry("testFile.txt");
        tarArchiveOutputStream.putArchiveEntry(entry);
        byte[] data = new byte[entry.getRecordSize() + 1];
        
        // Act
        tarArchiveOutputStream.write(data);
        
        // Assert
        // Expect IOException
    }
    
    @Test
    public void testWrite_multipleCalls_success() throws IOException {
        // Arrange
        TarArchiveEntry entry = new TarArchiveEntry("testFile.txt");
        tarArchiveOutputStream.putArchiveEntry(entry);
        byte[] data = "Some test data".getBytes();
        
        // Act
        tarArchiveOutputStream.write(data, 0, 5);
        tarArchiveOutputStream.write(data, 5, 5);
        
        // Assert
        assertEquals(data.length, tarArchiveOutputStream.getCurrentEntry().getSize());
        assertEquals(data.length, tarArchiveOutputStream.getCurrentEntry().getBytesWritten());
    }
    
    @Test(expected = IOException.class)
    public void testWrite_invalidOffset_failure() throws IOException {
        // Arrange
        TarArchiveEntry entry = new TarArchiveEntry("testFile.txt");
        tarArchiveOutputStream.putArchiveEntry(entry);
        byte[] data = "Some test data".getBytes();
        
        // Act
        tarArchiveOutputStream.write(data, -1, 5);
        
        // Assert
        // Expect IOException
    }
    
    @Test(expected = IOException.class)
    public void testWrite_invalidLength_failure() throws IOException {
        // Arrange
        TarArchiveEntry entry = new TarArchiveEntry("testFile.txt");
        tarArchiveOutputStream.putArchiveEntry(entry);
        byte[] data = "Some test data".getBytes();
        
        // Act
        tarArchiveOutputStream.write(data, 0, -1);
        
        // Assert
        // Expect IOException
    }
    
    @Test
    public void testFinish_success() throws IOException {
        // Arrange
        TarArchiveEntry entry = new TarArchiveEntry("testFile.txt");
        tarArchiveOutputStream.putArchiveEntry(entry);
        tarArchiveOutputStream.write("Some test data".getBytes());
        
        // Act
        tarArchiveOutputStream.finish();
        
        // Assert
        assertEquals(1024, outputStream.size());
    }
    
    @Test(expected = IOException.class)
    public void testFinish_entryNotClosed_failure() throws IOException {
        // Arrange
        TarArchiveEntry entry = new TarArchiveEntry("testFile.txt");
        tarArchiveOutputStream.putArchiveEntry(entry);
        tarArchiveOutputStream.write("Some test data".getBytes());
        
        // Act
        tarArchiveOutputStream.close(); // Closing the archive without closing the entry
        tarArchiveOutputStream.finish();
        
        // Assert
        // Expect IOException
    }
    
    @Test
    public void testClose_success() throws IOException {
        // Arrange
        TarArchiveEntry entry = new TarArchiveEntry("testFile.txt");
        tarArchiveOutputStream.putArchiveEntry(entry);
        tarArchiveOutputStream.write("Some test data".getBytes());
        
        // Act
        tarArchiveOutputStream.close();
        
        // Assert
        assertTrue(tarArchiveOutputStream.isClosed());
    }
    
    @Test(expected = IOException.class)
    public void testClose_entryNotClosed_failure() throws IOException {
        // Arrange
        TarArchiveEntry entry = new TarArchiveEntry("testFile.txt");
        tarArchiveOutputStream.putArchiveEntry(entry);
        tarArchiveOutputStream.write("Some test data".getBytes());
        
        // Act
        tarArchiveOutputStream.closeArchiveEntry(); // Closing the entry without closing the archive
        tarArchiveOutputStream.close();
        
        // Assert
        // Expect IOException
    }
    
    @Test
    public void testCreateArchiveEntry_success() throws IOException {
        // Arrange
        File inputFile = new File("testFile.txt");
        
        // Act
        ArchiveEntry entry = tarArchiveOutputStream.createArchiveEntry(inputFile, "testFile.txt");
        
        // Assert
        assertNotNull(entry);
        assertTrue(entry instanceof TarArchiveEntry);
        assertEquals("testFile.txt", entry.getName());
        assertEquals(0, entry.getSize());
    }
}