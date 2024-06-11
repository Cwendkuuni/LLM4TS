import static org.junit.Assert.*;
import org.junit.*;
import java.io.*;

public class ZipArchiveInputStreamTest {

    private ZipArchiveInputStream zipInputStream;

    @Before
    public void setUp() throws Exception {
        InputStream inputStream = new FileInputStream("test.zip");
        zipInputStream = new ZipArchiveInputStream(inputStream);
    }

    @After
    public void tearDown() throws Exception {
        zipInputStream.close();
    }

    @Test
    public void testGetNextZipEntry() throws IOException {
        ZipArchiveEntry entry = zipInputStream.getNextZipEntry();

        assertNotNull(entry);
        assertEquals("file1.txt", entry.getName());
        assertEquals(10, entry.getSize());
        assertEquals(0x00000003, entry.getMethod());
        assertFalse(entry.isDirectory());

        entry = zipInputStream.getNextZipEntry();

        assertNotNull(entry);
        assertEquals("file2.txt", entry.getName());
        assertEquals(20, entry.getSize());
        assertEquals(0x00000003, entry.getMethod());
        assertFalse(entry.isDirectory());

        entry = zipInputStream.getNextZipEntry();

        assertNull(entry);
    }

    @Test
    public void testRead() throws IOException {
        zipInputStream.getNextZipEntry();

        byte[] buffer = new byte[10];
        int bytesRead = zipInputStream.read(buffer);

        assertEquals(10, bytesRead);
        assertEquals("Hello", new String(buffer));

        bytesRead = zipInputStream.read(buffer);

        assertEquals(0, bytesRead);
    }

    @Test
    public void testClose() throws IOException {
        assertFalse(zipInputStream.isClosed());

        zipInputStream.close();

        assertTrue(zipInputStream.isClosed());
    }

    @Test
    public void testSkip() throws IOException {
        zipInputStream.getNextZipEntry();

        long skipped = zipInputStream.skip(5);

        assertEquals(5, skipped);

        skipped = zipInputStream.skip(10);

        assertEquals(5, skipped);
    }

}