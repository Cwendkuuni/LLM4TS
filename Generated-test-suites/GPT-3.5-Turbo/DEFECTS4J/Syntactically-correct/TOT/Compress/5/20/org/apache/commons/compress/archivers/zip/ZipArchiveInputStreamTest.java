import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.zip.ZipException;

public class ZipArchiveInputStreamTest {

    private ZipArchiveInputStream zip;

    @Before
    public void setUp() {
        byte[] data = getTestZipFileData();
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(data);
        zip = new ZipArchiveInputStream(byteArrayInputStream);
    }

    @Test
    public void testGetNextZipEntry() throws IOException {
        assertNotNull(zip.getNextZipEntry());
        assertNull(zip.getNextZipEntry());
    }

    @Test
    public void testGetNextEntry() throws IOException {
        assertNotNull(zip.getNextEntry());
        assertNull(zip.getNextEntry());
    }

    @Test
    public void testRead() throws IOException {
        byte[] buffer = new byte[10];
        assertEquals(10, zip.read(buffer));
        assertEquals(-1, zip.read(buffer));
    }

    @Test(expected = IOException.class)
    public void testReadWhenStreamClosed() throws IOException {
        zip.close();
        zip.read(new byte[10]);
    }

    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void testReadWithInvalidBufferSize() throws IOException {
        zip.read(new byte[20], 0, 30);
    }

    @Test
    public void testSkip() throws IOException {
        assertEquals(10, zip.skip(10));
        assertEquals(0, zip.skip(10));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSkipWithNegativeValue() throws IOException {
        zip.skip(-10);
    }

    @Test
    public void testMatches() {
        assertTrue(ZipArchiveInputStream.matches(getTestZipFileData(), getTestZipFileData().length));
        assertFalse(ZipArchiveInputStream.matches(new byte[10], 10));
    }

    @Test
    public void testClose() throws IOException {
        zip.close();
        zip.close();
    }

    private byte[] getTestZipFileData() {
        // Code to generate a sample test zip file data
    }
}