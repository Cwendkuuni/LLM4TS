import static org.junit.Assert.*;
import org.junit.Test;
import org.junit.Before;
import org.junit.After;
import java.io.ByteArrayInputStream;
import java.io.IOException;

public class ArArchiveInputStreamTest {

    private ArArchiveInputStream arInputStream;

    @Before
    public void setUp() {
        arInputStream = new ArArchiveInputStream(new ByteArrayInputStream(new byte[0]));
    }

    @After
    public void tearDown() throws IOException {
        arInputStream.close();
    }

    @Test
    public void testGetNextArEntry() {
        try {
            ArArchiveEntry entry = arInputStream.getNextArEntry();
            assertNull(entry);
        } catch (IOException e) {
            fail("IOException thrown when not expected");
        }
    }

    @Test
    public void testGetNextEntry() {
        try {
            ArchiveEntry entry = arInputStream.getNextEntry();
            assertNull(entry);
        } catch (IOException e) {
            fail("IOException thrown when not expected");
        }
    }

    @Test
    public void testRead() {
        try {
            int readByte = arInputStream.read();
            assertEquals(-1, readByte); // Empty InputStream should return -1
        } catch (IOException e) {
            fail("IOException thrown when not expected");
        }
    }

    @Test
    public void testReadByteArray() {
        try {
            byte[] buf = new byte[10]; // Reading into an empty byte array
            int bytesRead = arInputStream.read(buf);
            assertEquals(-1, bytesRead); // Empty InputStream should return -1
        } catch (IOException e) {
            fail("IOException thrown when not expected");
        }
    }

    @Test
    public void testReadByteArrayOffsetLength() {
        try {
            byte[] buf = new byte[10]; // Reading into an empty byte array with offset and length
            int bytesRead = arInputStream.read(buf, 2, 5);
            assertEquals(-1, bytesRead); // Empty InputStream should return -1
        } catch (IOException e) {
            fail("IOException thrown when not expected");
        }
    }

    @Test
    public void testMatches() {
        byte[] signature = {0x21, 0x3c, 0x61, 0x72, 0x63, 0x68, 0x3e, 0x0a};
        assertTrue(ArArchiveInputStream.matches(signature, 8)); // Matching signature
    }
}