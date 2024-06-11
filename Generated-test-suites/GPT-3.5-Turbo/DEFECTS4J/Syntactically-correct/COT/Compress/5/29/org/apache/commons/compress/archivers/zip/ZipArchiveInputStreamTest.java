
import org.junit.*;
import static org.junit.Assert.*;

public class ZipArchiveInputStreamTest {

    // Test getNextZipEntry method
    // Test case 1: No more entries
    @Test
    public void testGetNextZipEntry_NoMoreEntries() throws Exception {
        ZipArchiveInputStream zipStream = new ZipArchiveInputStream(null);
        assertNull(zipStream.getNextZipEntry());
    }

    // Test case 2: Valid entry with standard encoding
    @Test
    public void testGetNextZipEntry_ValidEntryStandardEncoding() throws Exception {
        ZipEncoding encoding = ZipEncodingHelper.UTF8;
        ZipArchiveInputStream zipStream = new ZipArchiveInputStream(null, encoding, true);
        
        zipStream.in.read(new byte[]{80, 75, 3, 4, 0, 0, 0});
        zipStream.in.read(new byte[]{4, 0, 0, 0});
        zipStream.in.read(new byte[]{1, 0});
        zipStream.in.read(new byte[]{8, 0});
        zipStream.in.read(new byte[]{50, 62, 23, 5});
        zipStream.in.read(new byte[]{0, 0, 0, 0});
        zipStream.in.read(new byte[]{0, 0, 0, 0});
        zipStream.in.read(new byte[]{4, 0});
        zipStream.in.read(new byte[]{0, 0});
        
        ZipArchiveEntry entry = zipStream.getNextZipEntry();
        assertNotNull(entry);
        assertEquals(1, entry.getPlatform());
        assertEquals(8, entry.getMethod());
    }

    // Test case 3: Valid entry with UTF-8 encoding
    @Test
    public void testGetNextZipEntry_ValidEntryUTF8Encoding() throws Exception {
        ZipEncoding encoding = ZipEncodingHelper.UTF8;
        ZipArchiveInputStream zipStream = new ZipArchiveInputStream(null, encoding, true);
        
        zipStream.in.read(new byte[]{80, 75, 3, 4, 0, 0, 0});
        zipStream.in.read(new byte[]{4, 0, 0, 0});
        zipStream.in.read(new byte[]{1, 0, 1, 0});
        zipStream.in.read(new byte[]{8, 0});
        zipStream.in.read(new byte[]{50, 62, 23, 5});
        zipStream.in.read(new byte[]{0, 0, 0, 0});
        zipStream.in.read(new byte[]{0, 0, 0, 0});
        zipStream.in.read(new byte[]{4, 0});
        zipStream.in.read(new byte[]{0, 0});
        
        ZipArchiveEntry entry = zipStream.getNextZipEntry();
        assertNotNull(entry);
        assertEquals(1, entry.getPlatform());
        assertEquals(8, entry.getMethod());
        assertTrue(entry.getName().contains("UTF-8"));
    }

    // Test case 4: Entry with data descriptor
    @Test
    public void testGetNextZipEntry_EntryWithDataDescriptor() throws Exception {
        ZipArchiveInputStream zipStream = new ZipArchiveInputStream(null);
        
        zipStream.in.read(new byte[]{80, 75, 3, 4, 0, 0, 0});
        zipStream.in.read(new byte[]{4, 0, 8, 0});
        zipStream.in.read(new byte[]{50, 62, 23, 5});
        zipStream.in.read(new byte[]{0, 0, 0, 0});
        zipStream.in.read(new byte[]{0, 0, 0, 0});
        zipStream.in.read(new byte[]{4, 0});
        zipStream.in.read(new byte[]{0, 0});
        zipStream.in.read(new byte[]{0, 0});
        zipStream.in.read(new byte[]{0, 0});
        zipStream.in.read(new byte[]{0, 0});
        
        ZipArchiveEntry entry = zipStream.getNextZipEntry();
        assertNotNull(entry);
        assertTrue(entry.getPlatform() > 0);
        assertTrue(entry.getMethod() > 0);
        assertFalse(entry.hasDataDescriptor());
    }

    // Test read method
    // Test case 5: Empty buffer
    @Test
    public void testRead_EmptyBuffer() throws Exception {
        ZipArchiveInputStream zipStream = new ZipArchiveInputStream(null);
        byte[] buffer = new byte[0];
        assertEquals(-1, zipStream.read(buffer, 0, 0));
    }

    // Test case 6: Closed stream
    @Test(expected=IOException.class)
    public void testRead_ClosedStream() throws Exception {
        ZipArchiveInputStream zipStream = new ZipArchiveInputStream(null);
        zipStream.close();
        byte[] buffer = new byte[1024];
        zipStream.read(buffer, 0, buffer.length);
    }

    // Test case 7: Skip method with negative value
    @Test(expected=IllegalArgumentException.class)
    public void testSkip_InvalidValue() throws Exception {
        ZipArchiveInputStream zipStream = new ZipArchiveInputStream(null);
        zipStream.skip(-1);
    }

    // Test matches method
    // Test case 8: Signature matches LFH_SIG
    @Test
    public void testMatches_LFHSIG() throws Exception {
        byte[] signature = {80, 75, 3, 4, 0};
        assertTrue(ZipArchiveInputStream.matches(signature, 5));
    }

    // Test case 9: Signature matches EOCD_SIG
    @Test
    public void testMatches_EOCDSIG() throws Exception {
        byte[] signature = {80, 75, 5, 6};
        assertTrue(ZipArchiveInputStream.matches(signature, 4));
    }

    // Test case 10: Signature does not match any known signatures
    @Test
    public void testMatches_NoMatch() throws Exception {
        byte[] signature = {80, 75};
        assertFalse(ZipArchiveInputStream.matches(signature, 2));
    }
}
