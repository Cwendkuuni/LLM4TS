import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import org.junit.Before;
import org.junit.Test;

public class ArArchiveInputStreamTest {

    private ArArchiveInputStream arInputStream;
    private ByteArrayInputStream byteArrayInputStream;
    
    
    @Before
    public void setUp() {
        byte[] arHeader = "!<arch>\n".getBytes();
        byte[] file1 = "file1             ".getBytes();
        byte[] lastModified1 = "1432787615000".getBytes();
        byte[] userId1 = "1000  ".getBytes();
        byte[] groupId1 = "1000  ".getBytes();
        byte[] fileMode1 = "100644 ".getBytes();
        byte[] fileLength1 = "10      ".getBytes();
        byte[] arTrailer = "`\n".getBytes();
        byte[] data1 = "file1 data".getBytes();
        
        byte[] file2 = "file2             ".getBytes();
        byte[] lastModified2 = "1432787625000".getBytes();
        byte[] userId2 = "1000  ".getBytes();
        byte[] groupId2 = "1000  ".getBytes();
        byte[] fileMode2 = "100644 ".getBytes();
        byte[] fileLength2 = "10      ".getBytes();
        byte[] data2 = "file2 data".getBytes();
        
        byte[] inputBytes = concatenateArrays(arHeader, file1, lastModified1, userId1, groupId1, 
                fileMode1, fileLength1, arTrailer, data1, arHeader, file2, lastModified2, 
                userId2, groupId2, fileMode2, fileLength2, arTrailer, data2);

        byteArrayInputStream = new ByteArrayInputStream(inputBytes);
        arInputStream = new ArArchiveInputStream(byteArrayInputStream);
    }

    @Test
    public void testGetNextArEntry() throws IOException {
        ArArchiveEntry entry1 = arInputStream.getNextArEntry();
        assertEquals("file1", entry1.getName());
        assertEquals(10, entry1.getSize());
        
        ArArchiveEntry entry2 = arInputStream.getNextArEntry();
        assertEquals("file2", entry2.getName());
        assertEquals(10, entry2.getSize());
        
        assertNull(arInputStream.getNextArEntry());
    }
    
    @Test
    public void testGetNextEntry() throws IOException {
        ArchiveEntry entry1 = arInputStream.getNextEntry();
        assertTrue(entry1 instanceof ArArchiveEntry);
        assertEquals("file1", entry1.getName());
        assertEquals(10, entry1.getSize());
        
        ArchiveEntry entry2 = arInputStream.getNextEntry();
        assertTrue(entry2 instanceof ArArchiveEntry);
        assertEquals("file2", entry2.getName());
        assertEquals(10, entry2.getSize());
        
        assertNull(arInputStream.getNextEntry());
    }
    
    @Test
    public void testClose() throws IOException {
        assertFalse(arInputStream.isClosed());
        
        arInputStream.close();
        
        assertTrue(arInputStream.isClosed());
    }
    
    @Test
    public void testRead() throws IOException {
        byte[] buffer = new byte[4096];
        
        assertEquals(35, arInputStream.read(buffer)); // Reading ar header
        
        for (int i = 0; i < 10; i++) {
            assertEquals('f', (char) arInputStream.read());
        }
        
        assertEquals(35, arInputStream.read(buffer)); // Reading ar trailer
        
        assertEquals(-1, arInputStream.read()); // Reached end of stream
        
        assertFalse(arInputStream.isClosed());
        
        arInputStream.close();
        
        assertTrue(arInputStream.isClosed());
    }
    
    @Test
    public void testReadByteArray() throws IOException {
        byte[] buffer = new byte[4096];
        byte[] expectedData1 = "file1 data".getBytes();
        
        arInputStream.getNextArEntry(); // Read the first entry header
        
        assertEquals(expectedData1.length, arInputStream.read(buffer));
        assertArrayEquals(expectedData1, buffer);
        
        assertFalse(arInputStream.isClosed());
        
        arInputStream.close();
        
        assertTrue(arInputStream.isClosed());
    }
    
    @Test
    public void testReadByteArrayWithOffset() throws IOException {
        byte[] buffer = new byte[4096];
        byte[] expectedData1 = "fil".getBytes();
        
        arInputStream.getNextArEntry(); // Read the first entry header
        
        assertEquals(2, arInputStream.read(buffer, 10, 2));
        assertArrayEquals(expectedData1, buffer);
        
        assertFalse(arInputStream.isClosed());
        
        arInputStream.close();
        
        assertTrue(arInputStream.isClosed());
    }
    
    @Test
    public void testReadByteArrayWithOffsetAndLength() throws IOException {
        byte[] buffer = new byte[4096];
        byte[] expectedData1 = "file1 ".getBytes();
        
        arInputStream.getNextArEntry(); // Read the first entry header
        
        assertEquals(6, arInputStream.read(buffer, 10, 6));
        assertArrayEquals(expectedData1, buffer);
        
        assertFalse(arInputStream.isClosed());
        
        arInputStream.close();
        
        assertTrue(arInputStream.isClosed());
    }
    
    @Test
    public void testReadByteArrayWithOffsetAndLengthBoundary() throws IOException {
        byte[] buffer = new byte[4096];
        byte[] expectedData1 = "file1".getBytes();
        
        arInputStream.getNextArEntry(); // Read the first entry header
        
        assertEquals(5, arInputStream.read(buffer, 10, 5));
        assertArrayEquals(expectedData1, buffer);
        
        assertFalse(arInputStream.isClosed());
        
        arInputStream.close();
        
        assertTrue(arInputStream.isClosed());
    }
    
    @Test
    public void testReadByteArrayWithOffsetAndLengthOverflow() throws IOException {
        byte[] buffer = new byte[4096];
        byte[] expectedData1 = "file1 ".getBytes();
        
        arInputStream.getNextArEntry(); // Read the first entry header
        
        assertEquals(6, arInputStream.read(buffer, 10, 10));
        assertArrayEquals(expectedData1, buffer);
        
        assertFalse(arInputStream.isClosed());
        
        arInputStream.close();
        
        assertTrue(arInputStream.isClosed());
    }
    
    @Test
    public void testMatchesWithValidSignature() {
        byte[] signature = {0x21, 0x3c, 0x61, 0x72, 0x63, 0x68, 0x3e, 0x0a};
        int length = 8;
        
        assertTrue(ArArchiveInputStream.matches(signature, length));
    }
    
    @Test
    public void testMatchesWithInvalidSignature() {
        byte[] signature = {0x21, 0x3c, 0x61, 0x72, 0x63, 0x68, 0x3e, 0x0b};
        int length = 8;
        
        assertFalse(ArArchiveInputStream.matches(signature, length));
    }
    
    @Test
    public void testMatchesWithInvalidSignatureLength() {
        byte[] signature = {0x21, 0x3c, 0x61, 0x72, 0x63, 0x68};
        int length = 6;
        
        assertFalse(ArArchiveInputStream.matches(signature, length));
    }
    
    private byte[] concatenateArrays(byte[]... arrays) {
        int length = 0;
        for (byte[] array : arrays) {
            length += array.length;
        }
        byte[] result = new byte[length];
        int offset = 0;
        for (byte[] array : arrays) {
            System.arraycopy(array, 0, result, offset, array.length);
            offset += array.length;
        }
        return result;
    }
}