import org.apache.commons.compress.archivers.zip.ZipArchiveInputStream;
import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class ZipArchiveInputStreamTest {
    private ZipArchiveInputStream zipIn;
    
    @Before
    public void setUp() throws Exception {
        zipIn = new ZipArchiveInputStream(getClass().getResourceAsStream("test.zip"));
    }
    
    @After
    public void tearDown() throws Exception {
        zipIn.close();
    }
    
    @Test
    public void testGetNextZipEntry() throws IOException {
        // Verify the entry names and sizes
        ZipArchiveEntry entry1 = zipIn.getNextZipEntry();
        assertEquals("file1.txt", entry1.getName());
        assertEquals(5, entry1.getSize());
        
        ZipArchiveEntry entry2 = zipIn.getNextZipEntry();
        assertEquals("file2.txt", entry2.getName());
        assertEquals(8, entry2.getSize());
        
        ZipArchiveEntry entry3 = zipIn.getNextZipEntry();
        assertEquals("file3.txt", entry3.getName());
        assertEquals(0, entry3.getSize());
        
        // Verify null is returned after reaching the end of the archive
        assertNull(zipIn.getNextZipEntry());
    }
    
    @Test
    public void testRead() throws IOException {
        // Read the contents of the first file
        zipIn.getNextZipEntry();
        byte[] buffer = new byte[5];
        int bytesRead = zipIn.read(buffer);
        
        assertEquals(5, bytesRead);
        assertEquals("Hello", new String(buffer, StandardCharsets.UTF_8));
    }
    
    @Test
    public void testClose() throws IOException {
        zipIn.close();
        assertNull(zipIn.getNextZipEntry());
    }
    
    @Test
    public void testSkip() throws IOException {
        // Skip the first entry
        zipIn.getNextZipEntry();
        long skipped = zipIn.skip(5);
        
        assertEquals(5, skipped);
        assertEquals("file2.txt", zipIn.getNextZipEntry().getName());
    }
}