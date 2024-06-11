import org.junit.Test;
import java.io.FileInputStream;
import java.io.IOException;

public class ArArchiveInputStreamTest {

    @Test
    public void testGetNextArEntry() throws IOException {
        FileInputStream in = new FileInputStream("test.ar");
        ArArchiveInputStream archiveInput = new ArArchiveInputStream(in);

        ArArchiveEntry entry = archiveInput.getNextArEntry();

        // Test entry metadata
        assertEquals("testfile.txt", entry.getName());
        assertEquals(10, entry.getSize());

        archiveInput.close();
        in.close();
    }

    @Test
    public void testGetNextEntry() throws IOException {
        FileInputStream in = new FileInputStream("test.ar");
        ArArchiveInputStream archiveInput = new ArArchiveInputStream(in);

        ArchiveEntry entry = archiveInput.getNextEntry();

        // Test entry metadata
        assertEquals("testfile.txt", entry.getName());
        assertEquals(10, entry.getSize());

        archiveInput.close();
        in.close();
    }

    @Test
    public void testRead() throws IOException {
        FileInputStream in = new FileInputStream("test.ar");
        ArArchiveInputStream archiveInput = new ArArchiveInputStream(in);

        int data = archiveInput.read();

        // Test read result
        assertEquals(65, data);

        archiveInput.close();
        in.close();
    }

    @Test
    public void testReadBuffer() throws IOException {
        FileInputStream in = new FileInputStream("test.ar");
        ArArchiveInputStream archiveInput = new ArArchiveInputStream(in);

        byte[] buffer = new byte[10];
        int bytesRead = archiveInput.read(buffer);

        // Test read result
        assertEquals(10, bytesRead);
        assertEquals(65, buffer[0]);

        archiveInput.close();
        in.close();
    }

    @Test
    public void testReadBufferOffsetLength() throws IOException {
        FileInputStream in = new FileInputStream("test.ar");
        ArArchiveInputStream archiveInput = new ArArchiveInputStream(in);

        byte[] buffer = new byte[10];
        int bytesRead = archiveInput.read(buffer, 0, 10);

        // Test read result
        assertEquals(10, bytesRead);
        assertEquals(65, buffer[0]);

        archiveInput.close();
        in.close();
    }

    @Test
    public void testMatches() {
        byte[] signature = new byte[]{0x21, 0x3c, 0x61, 0x72, 0x63, 0x68, 0x3e, 0x0a};

        boolean result = ArArchiveInputStream.matches(signature, 8);

        // Test signature matching
        assertTrue(result);
    }

}