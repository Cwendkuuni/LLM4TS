import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class ZipArchiveInputStreamTest {

    private static final String ENCODING = "UTF-8";
    private static final boolean USE_UNICODE_EXTRA_FIELDS = true;

    private ZipArchiveInputStream zipInputStream;

    @Before
    public void setUp() throws Exception {
        InputStream inputFileStream = getClass().getResourceAsStream("testZipFile.zip");
        zipInputStream = new ZipArchiveInputStream(inputFileStream, ENCODING, USE_UNICODE_EXTRA_FIELDS);
    }

    @After
    public void tearDown() throws Exception {
        zipInputStream.close();
    }

    @Test
    public void testGetNextZipEntry() throws IOException {
        ZipArchiveEntry expectedEntry1 = new ZipArchiveEntry();
        expectedEntry1.setName("test1.txt");
        expectedEntry1.setMethod(ZipArchiveOutputStream.STORED);
        expectedEntry1.setSize(10);

        ZipArchiveEntry expectedEntry2 = new ZipArchiveEntry();
        expectedEntry2.setName("test2.txt");
        expectedEntry2.setMethod(ZipArchiveOutputStream.DEFLATED);
        expectedEntry2.setSize(20);

        ZipArchiveEntry entry1 = zipInputStream.getNextZipEntry();
        assertEquals(expectedEntry1.getName(), entry1.getName());
        assertEquals(expectedEntry1.getMethod(), entry1.getMethod());
        assertEquals(expectedEntry1.getSize(), entry1.getSize());

        ZipArchiveEntry entry2 = zipInputStream.getNextZipEntry();
        assertEquals(expectedEntry2.getName(), entry2.getName());
        assertEquals(expectedEntry2.getMethod(), entry2.getMethod());
        assertEquals(expectedEntry2.getSize(), entry2.getSize());
    }

    @Test
    public void testRead() throws IOException {
        ZipArchiveEntry entry = zipInputStream.getNextZipEntry();
        assertNotNull(entry);

        byte[] buffer = new byte[20];
        int bytesRead = zipInputStream.read(buffer);

        assertEquals(entry.getSize(), bytesRead);
    }
    
    @Test
    public void testSkip() throws IOException {
        ZipArchiveEntry entry = zipInputStream.getNextZipEntry();
        assertNotNull(entry);
        
        long skipped = zipInputStream.skip(entry.getSize() - 1);
        
        assertEquals(entry.getSize() - 1, skipped);
    }

    @Test
    public void testClose() throws IOException {
        ZipArchiveEntry entry = zipInputStream.getNextZipEntry();
        assertNotNull(entry);

        zipInputStream.close();

        try {
            zipInputStream.getNextZipEntry();
            fail("Expected IOException");
        } catch (IOException e) {
            // expected
        }
    }
}