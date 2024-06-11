import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.EOFException;
import java.io.IOException;
import java.util.zip.ZipException;

import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class ZipArchiveInputStreamTest {
    private ZipArchiveInputStream zipInputStream;
    
    @Before
    public void setUp() {
        byte[] data = new byte[100];
        ByteArrayInputStream inputStream = new ByteArrayInputStream(data);
        zipInputStream = new ZipArchiveInputStream(inputStream);
    }
    
    @After
    public void tearDown() throws IOException {
        zipInputStream.close();
    }
    
    @Test
    public void testGetNextZipEntry() throws IOException {
        ArchiveEntry entry = zipInputStream.getNextZipEntry();
        assertNull(entry);
    }
    
    @Test
    public void testRead() throws IOException {
        byte[] buffer = new byte[10];
        int bytesRead = zipInputStream.read(buffer, 0, 10);
        assertEquals(10, bytesRead);
    }
    
    @Test(expected = IOException.class)
    public void testReadClosedStream() throws IOException {
        zipInputStream.close();
        byte[] buffer = new byte[10];
        int bytesRead = zipInputStream.read(buffer, 0, 10); // should throw IOException
    }
    
    @Test
    public void testClose() throws IOException {
        zipInputStream.close();
        assertTrue(zipInputStream.isClosed());
    }
    
    @Test
    public void testSkip() throws IOException {
        long bytesSkipped = zipInputStream.skip(50);
        assertEquals(50, bytesSkipped);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testSkipNegativeValue() throws IOException {
        long bytesSkipped = zipInputStream.skip(-50); // should throw IllegalArgumentException
    }
    
    @Test
    public void testMatchesNormalFile() {
        byte[] signature = {0x50, 0x4B, 0x03, 0x04};
        boolean matches = ZipArchiveInputStream.matches(signature, signature.length);
        assertTrue(matches);
    }
    
    @Test
    public void testMatchesEmptyZip() {
        byte[] signature = {0x50, 0x4B, 0x05, 0x06};
        boolean matches = ZipArchiveInputStream.matches(signature, signature.length);
        assertFalse(matches);
    }
    
    @Test
    public void testCloseEntry() throws IOException {
        zipInputStream.getNextZipEntry();
        zipInputStream.closeEntry();
        assertTrue(zipInputStream.isClosed());
    }
    
    @Test(expected = IOException.class)
    public void testCloseEntryClosedStream() throws IOException {
        zipInputStream.close();
        zipInputStream.closeEntry(); // should throw IOException
    }
}