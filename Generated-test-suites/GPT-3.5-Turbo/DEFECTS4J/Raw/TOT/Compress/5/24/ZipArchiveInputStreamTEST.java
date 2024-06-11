import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipArchiveInputStream;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class ZipArchiveInputStreamTest {

    private ZipArchiveInputStream zipInputStream;
    private ByteArrayInputStream byteArrayInputStream;
    
    @Before
    public void setUp() {
        byteArrayInputStream = new ByteArrayInputStream(new byte[0]);
        zipInputStream = new ZipArchiveInputStream(byteArrayInputStream);
    }
    
    @After
    public void tearDown() throws IOException {
        zipInputStream.close();
    }
    
    @Test
    public void testGetNextZipEntry_ReturnsZipArchiveEntry() throws IOException {
        ZipArchiveEntry entry = zipInputStream.getNextZipEntry();
        assertNotNull(entry);
        assertEquals(0, entry.getPlatform());
    }
    
    @Test
    public void testGetNextZipEntry_ReturnsNullWhenClosed() throws IOException {
        zipInputStream.close();
        assertNull(zipInputStream.getNextZipEntry());
    }
    
    @Test
    public void testGetNextZipEntry_ReturnsNullWhenHitCentralDirectory() throws IOException {
        zipInputStream.getNextZipEntry(); // Read the first entry
        assertNull(zipInputStream.getNextZipEntry());
    }
    
    @Test
    public void testRead_ReadsBytesFromInputStream() throws IOException {
        byte[] buffer = new byte[10];
        assertNotEquals(-1, zipInputStream.read(buffer));
    }
    
    @Test
    public void testRead_ReturnsNegativeOneWhenClosed() throws IOException {
        zipInputStream.close();
        assertEquals(-1, zipInputStream.read(new byte[10]));
    }
    
    @Test
    public void testRead_ReturnsNegativeOneWhenInflaterFinished() throws IOException {
        byte[] buffer = new byte[10];
        zipInputStream.getNextZipEntry(); // Read the first entry
        assertEquals(-1, zipInputStream.read(buffer));
    }
    
    @Test
    public void testClose_ClosesInputStream() throws IOException {
        ByteArrayInputStream spyInputStream = spy(byteArrayInputStream);
        zipInputStream = new ZipArchiveInputStream(spyInputStream);
        zipInputStream.close();
        verify(spyInputStream).close();
    }
    
    @Test
    public void testSkip_SkipsBytes() throws IOException {
        zipInputStream.getNextZipEntry(); // Read the first entry
        long skipped = zipInputStream.skip(5);
        assertEquals(5, skipped);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testSkip_ThrowsIllegalArgumentExceptionForNegativeValue() throws IOException {
        zipInputStream.skip(-5);
    }
    
    @Test
    public void testCloseEntry_ThrowsIOExceptionWhenClosed() throws IOException {
        zipInputStream.close();
        try {
            zipInputStream.closeEntry();
            fail("Expected IOException to be thrown");
        } catch (IOException e) {
            assertEquals("The stream is closed", e.getMessage());
        }
    }
    
    @Test
    public void testCloseEntry_ResetsInputStreamWhenHasDataDescriptor() throws IOException {
        ZipArchiveEntry entry = mock(ZipArchiveEntry.class);
        when(entry.getMethod()).thenReturn(ZipArchiveInputStream.STORED);
        when(entry.getSize()).thenReturn((long) 10);
        zipInputStream.current = entry;
        zipInputStream.hasDataDescriptor = true;
        zipInputStream.readBytesOfEntry = 5;
        zipInputStream.offsetInBuffer = 5;
        zipInputStream.bytesReadFromStream = 15;
        zipInputStream.lengthOfLastRead = 10;
        
        zipInputStream.closeEntry();
        
        assertEquals(0, zipInputStream.readBytesOfEntry);
        assertEquals(0, zipInputStream.offsetInBuffer);
        assertEquals(0, zipInputStream.bytesReadFromStream);
        assertEquals(0, zipInputStream.lengthOfLastRead);
        verify(zipInputStream.inf).reset();
        verify(zipInputStream.in).unread(zipInputStream.buf, 5, 10);
    }

    @Test
    public void testMatches_ReturnsTrueForLFHSig() {
        byte[] signature = {0x04, 0x03, 0x4b, 0x50};
        assertTrue(ZipArchiveInputStream.matches(signature, signature.length));
    }
    
    @Test
    public void testMatches_ReturnsTrueForEOCDSig() {
        byte[] signature = {0x50, 0x4b, 0x05, 0x06};
        assertTrue(ZipArchiveInputStream.matches(signature, signature.length));
    }
    
    @Test
    public void testMatches_ReturnsFalseForInvalidSignature() {
        byte[] signature = {0x01, 0x02, 0x03, 0x04};
        assertFalse(ZipArchiveInputStream.matches(signature, signature.length));
    }

}