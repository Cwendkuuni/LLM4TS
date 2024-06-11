import static org.junit.Assert.assertEquals;

import java.io.ByteArrayInputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.PushbackInputStream;
import java.util.zip.CRC32;
import java.util.zip.DataFormatException;
import java.util.zip.Inflater;
import java.util.zip.ZipException;

import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.junit.Before;
import org.junit.Test;

public class ZipArchiveInputStreamTest {
    
    private ZipArchiveInputStream zipArchiveInputStream;
    
    @Before
    public void setup() {
        byte[] bytes = new byte[100];
        InputStream inputStream = new ByteArrayInputStream(bytes);
        zipArchiveInputStream = new ZipArchiveInputStream(inputStream);
    }
    
    @Test
    public void getNextZipEntryTest() throws IOException {
        ArchiveEntry entry = zipArchiveInputStream.getNextZipEntry();
        assertEquals(null, entry);
    }
    
    @Test
    public void getNextEntryTest() throws IOException {
        ArchiveEntry entry = zipArchiveInputStream.getNextEntry();
        assertEquals(null, entry);
    }
    
    @Test
    public void readTest() throws IOException {
        byte[] buffer = new byte[10];
        int start = 0;
        int length = 10;
        int result = zipArchiveInputStream.read(buffer, start, length);
        assertEquals(-1, result);
    }
    
    @Test(expected = IOException.class)
    public void closeTest() throws IOException {
        zipArchiveInputStream.close();
        zipArchiveInputStream.getNextEntry();
    }
    
    @Test
    public void skipTest() throws IOException {
        long value = 10;
        long result = zipArchiveInputStream.skip(value);
        assertEquals(10, result);
    }
    
    @Test
    public void matchesTest() {
        byte[] signature = {80, 75, 3, 4, 20, 0, 0, 0, 8, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
        int length = 20;
        boolean result = ZipArchiveInputStream.matches(signature, length);
        assertEquals(true, result);
    }
    
    @Test
    public void fillTest() throws IOException {
        zipArchiveInputStream.fill();
    }
    
    @Test(expected = IOException.class)
    public void closeEntryTest() throws IOException {
        zipArchiveInputStream.closeEntry();
        zipArchiveInputStream.getNextEntry();
    }
    
    @Test
    public void readFullyTest() throws IOException {
        byte[] b = new byte[10];
        zipArchiveInputStream.readFully(b);
        byte[] expected = new byte[10];
        assertEquals(expected, b);
    }
}