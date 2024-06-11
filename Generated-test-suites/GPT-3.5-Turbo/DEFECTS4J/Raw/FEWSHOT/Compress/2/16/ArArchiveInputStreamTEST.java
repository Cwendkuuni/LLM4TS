package org.apache.commons.compress.archivers.ar;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import org.apache.commons.compress.archivers.ArchiveEntry;
import org.junit.Before;
import org.junit.Test;

public class ArArchiveInputStreamTest {
    private ArArchiveInputStream arInput;
    private byte[] arInputData;
    
    @Before
    public void setup() {
       arInputData = new byte[]{0x21, 0x3c, 0x61, 0x72, 0x63, 0x68, 0x3e, 0x0a};
       InputStream inputStream = new ByteArrayInputStream(arInputData);
       arInput = new ArArchiveInputStream(inputStream);
    }
    
    @Test
    public void testGetNextArEntry() throws IOException {
        ArArchiveEntry entry = arInput.getNextArEntry();
        assertEquals("header", ArArchiveEntry.HEADER, new String(arInputData));
        assertNull("entry", entry);
    }
    
    @Test
    public void testGetNextEntry() throws IOException {
        ArchiveEntry entry = arInput.getNextEntry();
        assertEquals("header", ArArchiveEntry.HEADER, new String(arInputData));
        assertNull("entry", entry);
    }
    
    @Test
    public void testClose() throws IOException {
        arInput.close();
        assertTrue("Closed", arInput.isClosed());
    }
    
    @Test
    public void testRead() throws IOException {
        int bytesRead = arInput.read();
        assertEquals("bytesRead", 0x21, bytesRead);
        assertEquals("Offset", 1, arInput.getOffset());
    }
    
    @Test
    public void testReadByteArray() throws IOException {
        byte[] bytes = new byte[10];
        int bytesRead = arInput.read(bytes);
        assertEquals("bytesRead", 8, bytesRead);
        assertEquals("Offset", 8, arInput.getOffset());
    }
    
    @Test
    public void testReadByteArrayWithOffset() throws IOException {
        byte[] bytes = new byte[10];
        int bytesRead = arInput.read(bytes, 1, 8);
        assertEquals("bytesRead", 8, bytesRead);
        assertEquals("Offset", 8, arInput.getOffset());
        assertEquals("firstByte", 0x21, bytes[1]);
    }
    
    @Test
    public void testMatches() {
        byte[] signature = new byte[]{0x21, 0x3c, 0x61, 0x72, 0x63, 0x68, 0x3e, 0x0a};
        boolean result = ArArchiveInputStream.matches(signature, 8);
        assertTrue("match", result);
    }
}