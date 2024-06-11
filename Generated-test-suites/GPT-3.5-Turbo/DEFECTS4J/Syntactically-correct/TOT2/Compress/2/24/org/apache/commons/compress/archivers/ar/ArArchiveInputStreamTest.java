import static org.junit.Assert.*;
import org.junit.Test;
import java.io.ByteArrayInputStream;
import java.io.IOException;

public class ArArchiveInputStreamTest {

    @Test
    public void testGetNextArEntry() throws IOException {
        byte[] testData = "!<header>".getBytes();
        ByteArrayInputStream testInput = new ByteArrayInputStream(testData);
        ArArchiveInputStream arInputStream = new ArArchiveInputStream(testInput);
        assertNotNull(arInputStream.getNextArEntry());
    }

    @Test
    public void testGetNextEntry() throws IOException {
        byte[] testData = "!<entry>".getBytes();
        ByteArrayInputStream testInput = new ByteArrayInputStream(testData);
        ArArchiveInputStream arInputStream = new ArArchiveInputStream(testInput);
        assertNotNull(arInputStream.getNextEntry());
    }

    @Test
    public void testClose() throws IOException {
        byte[] testData = "!<close>".getBytes();
        ByteArrayInputStream testInput = new ByteArrayInputStream(testData);
        ArArchiveInputStream arInputStream = new ArArchiveInputStream(testInput);
        arInputStream.close();
        assertTrue(arInputStream.isClosed());
    }

    @Test
    public void testRead() throws IOException {
        byte[] testData = "!<read>".getBytes();
        ByteArrayInputStream testInput = new ByteArrayInputStream(testData);
        ArArchiveInputStream arInputStream = new ArArchiveInputStream(testInput);
        assertEquals('<', arInputStream.read());
    }

    @Test
    public void testReadByteArray() throws IOException {
        byte[] testData = "testReadByteArray".getBytes();
        byte[] buffer = new byte[10];
        ByteArrayInputStream testInput = new ByteArrayInputStream(testData);
        ArArchiveInputStream arInputStream = new ArArchiveInputStream(testInput);
        arInputStream.read(buffer);
        assertArrayEquals("testReadByt".getBytes(), buffer);
    }

    @Test
    public void testReadByteArrayWithOffsetAndLength() throws IOException {
        byte[] testData = "testReadWithOffsetAndLength".getBytes();
        byte[] buffer = new byte[10];
        ByteArrayInputStream testInput = new ByteArrayInputStream(testData);
        ArArchiveInputStream arInputStream = new ArArchiveInputStream(testInput);
        arInputStream.read(buffer, 5, 10);
        assertArrayEquals("tion".getBytes(), buffer);
    }

    @Test
    public void testMatches() {
        byte[] signature = "!<archc>".getBytes();
        assertTrue(ArArchiveInputStream.matches(signature, signature.length));
    }
}