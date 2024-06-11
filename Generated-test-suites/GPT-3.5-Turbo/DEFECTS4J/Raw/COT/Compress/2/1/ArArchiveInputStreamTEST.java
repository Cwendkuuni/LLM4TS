import static org.junit.Assert.*;
import org.junit.Test;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.ar.ArArchiveEntry;
import org.apache.commons.compress.archivers.ar.ArArchiveInputStream;

public class ArArchiveInputStreamTest {

    @Test
    public void testGetNextArEntry() throws IOException {
        String testFileContents = "!<arch>\n";
        byte[] testFileBytes = testFileContents.getBytes();
        ByteArrayInputStream testInput = new ByteArrayInputStream(testFileBytes);
        ArArchiveInputStream arInputStream = new ArArchiveInputStream(testInput);

        ArArchiveEntry entry = arInputStream.getNextArEntry();
        assertNull(entry);
    }

    @Test
    public void testGetNextEntry() throws IOException {
        String testFileContents = "!<arch>\n";
        byte[] testFileBytes = testFileContents.getBytes();
        ByteArrayInputStream testInput = new ByteArrayInputStream(testFileBytes);
        ArArchiveInputStream arInputStream = new ArArchiveInputStream(testInput);

        ArchiveEntry entry = arInputStream.getNextEntry();
        assertNull(entry);
    }

    @Test
    public void testClose() throws IOException {
        String testFileContents = "!<arch>\n";
        byte[] testFileBytes = testFileContents.getBytes();
        ByteArrayInputStream testInput = new ByteArrayInputStream(testFileBytes);
        ArArchiveInputStream arInputStream = new ArArchiveInputStream(testInput);

        arInputStream.close();
        assertTrue(arInputStream.isClosed());
    }

    @Test
    public void testRead_singleByte() throws IOException {
        String testFileContents = "!<arch>\n";
        byte[] testFileBytes = testFileContents.getBytes();
        ByteArrayInputStream testInput = new ByteArrayInputStream(testFileBytes);
        ArArchiveInputStream arInputStream = new ArArchiveInputStream(testInput);

        assertEquals(60, arInputStream.read());
    }

    @Test
    public void testRead_byteArray() throws IOException {
        String testFileContents = "!<arch>\n";
        byte[] testFileBytes = testFileContents.getBytes();
        ByteArrayInputStream testInput = new ByteArrayInputStream(testFileBytes);
        ArArchiveInputStream arInputStream = new ArArchiveInputStream(testInput);

        byte[] buffer = new byte[8];
        assertEquals(8, arInputStream.read(buffer));
        assertArrayEquals("!<arch>\n".getBytes(), buffer);
    }

    @Test
    public void testMatches() {
        byte[] signature = new byte[] { 0x21, 0x3c, 0x61, 0x72, 0x63, 0x68, 0x3e, 0x0a };
        assertTrue(ArArchiveInputStream.matches(signature, 8));
    }

}