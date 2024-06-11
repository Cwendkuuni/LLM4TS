import org.junit.Before;
import org.junit.Test;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import static org.junit.Assert.*;

public class DumpArchiveInputStreamTest {
    private DumpArchiveInputStream inputStream;

    @Before
    public void setUp() throws IOException {
        String testInput = "This is a test input";
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(testInput.getBytes());
        inputStream = new DumpArchiveInputStream(byteArrayInputStream);
    }

    @Test
    public void testConstructor() throws IOException {
        assertNotNull(inputStream);
    }

    @Test
    public void testGetCount() throws IOException {
        assertEquals(0, inputStream.getCount());
    }

    @Test
    public void testGetBytesRead() throws IOException {
        assertEquals(0, inputStream.getBytesRead());
    }

    @Test
    public void testGetSummary() throws IOException {
        assertNotNull(inputStream.getSummary());
    }

    @Test
    public void testNextEntry() throws IOException {
        DumpArchiveEntry entry = inputStream.getNextEntry();
        assertNotNull(entry);
        assertEquals("test.txt", entry.getName());
        assertEquals(0, entry.getOffset());
        assertEquals(0, entry.getIno());
    }

    @Test
    public void testRead() throws IOException {
        byte[] buffer = new byte[1024];
        int bytesRead = inputStream.read(buffer, 0, 10);
        assertEquals(10, bytesRead);
        byte[] expectedBytes = "This is a ".getBytes();
        assertArrayEquals(expectedBytes, buffer);

        bytesRead = inputStream.read(buffer, 0, 10);
        assertEquals(8, bytesRead);
        expectedBytes = "test input".getBytes();
        assertArrayEquals(expectedBytes, buffer);
    }

    @Test
    public void testClose() throws IOException {
        inputStream.close();
        assertTrue(inputStream.isClosed());
    }

}