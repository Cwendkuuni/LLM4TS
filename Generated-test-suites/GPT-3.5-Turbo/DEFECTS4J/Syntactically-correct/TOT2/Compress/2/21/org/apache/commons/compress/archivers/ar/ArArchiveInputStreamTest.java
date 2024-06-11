import static org.junit.Assert.*;
import org.junit.Test;
import org.junit.Before;
import java.io.ByteArrayInputStream;
import java.io.IOException;

public class ArArchiveInputStreamTest {

    private ArArchiveInputStream arInputStream;

    @Before
    public void setUp() {
        byte[] testData = {0x21, 0x3c, 0x61, 0x72, 0x63, 0x68, 0x3e, 0x0a};
        ByteArrayInputStream input = new ByteArrayInputStream(testData);
        arInputStream = new ArArchiveInputStream(input);
    }

    @Test
    public void testGetNextArEntry() throws IOException {
        assertNotNull(arInputStream.getNextArEntry());
    }

    @Test
    public void testGetNextEntry() throws IOException {
        assertNotNull(arInputStream.getNextEntry());
    }

    @Test
    public void testRead() throws IOException {
        assertEquals(33, arInputStream.read());
    }

    @Test
    public void testReadByteArray() throws IOException {
        byte[] buffer = new byte[8];
        assertEquals(8, arInputStream.read(buffer));
    }

    @Test
    public void testReadByteArrayOffsetLength() throws IOException {
        byte[] buffer = new byte[8];
        assertEquals(8, arInputStream.read(buffer, 0, 8));
    }

    @Test
    public void testMatches() {
        byte[] signature = {0x21, 0x3c, 0x61, 0x72, 0x63, 0x68, 0x3e, 0x0a};
        assertTrue(ArArchiveInputStream.matches(signature, 8));
    }
}