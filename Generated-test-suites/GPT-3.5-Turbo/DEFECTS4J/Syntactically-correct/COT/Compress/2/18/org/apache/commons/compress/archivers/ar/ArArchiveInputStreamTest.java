import static org.junit.Assert.*;
import org.junit.Test;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import static org.mockito.Mockito.mock;

public class ArArchiveInputStreamTest {

    @Test
    public void testGetNextArEntry() throws IOException {
        byte[] entryBytes = "header name lastmodified userid groupid filemode length trailer".getBytes();
        ByteArrayInputStream inputStream = new ByteArrayInputStream(entryBytes);
        ArArchiveInputStream archiveInputStream = new ArArchiveInputStream(inputStream);

        ArArchiveEntry entry = archiveInputStream.getNextArEntry();

        assertEquals("name", entry.getName());
        assertEquals(7, entry.getSize());
    }

    @Test
    public void testGetNextEntry() throws IOException {
        byte[] entryBytes = "header name lastmodified userid groupid filemode length trailer".getBytes();
        ByteArrayInputStream inputStream = new ByteArrayInputStream(entryBytes);
        ArArchiveInputStream archiveInputStream = new ArArchiveInputStream(inputStream);

        ArchiveEntry entry = archiveInputStream.getNextEntry();

        assertNotNull(entry);
    }

    @Test
    public void testClose() throws IOException {
        ByteArrayInputStream inputStream = mock(ByteArrayInputStream.class);
        ArArchiveInputStream archiveInputStream = new ArArchiveInputStream(inputStream);

        archiveInputStream.close();

        assertTrue(archiveInputStream.isClosed());
    }

    @Test
    public void testRead() throws IOException {
        byte[] bytes = "test".getBytes();
        ByteArrayInputStream inputStream = new ByteArrayInputStream(bytes);
        ArArchiveInputStream archiveInputStream = new ArArchiveInputStream(inputStream);

        int byteRead = archiveInputStream.read();

        assertEquals('t', byteRead);
    }

    @Test
    public void testReadByteArray() throws IOException {
        byte[] bytes = "test".getBytes();
        ByteArrayInputStream inputStream = new ByteArrayInputStream(bytes);
        ArArchiveInputStream archiveInputStream = new ArArchiveInputStream(inputStream);

        byte[] buffer = new byte[4];
        int bytesRead = archiveInputStream.read(buffer);

        assertEquals(4, bytesRead);
        assertArrayEquals("test".getBytes(), buffer);
    }

    @Test
    public void testReadByteArrayWithOffsetAndLength() throws IOException {
        byte[] bytes = "test".getBytes();
        ByteArrayInputStream inputStream = new ByteArrayInputStream(bytes);
        ArArchiveInputStream archiveInputStream = new ArArchiveInputStream(inputStream);

        byte[] buffer = new byte[4];
        int bytesRead = archiveInputStream.read(buffer, 0, 2);

        assertEquals(2, bytesRead);
        assertEquals('t', buffer[0]);
        assertEquals('e', buffer[1]);
    }

    @Test
    public void testMatches() {
        byte[] signature = {(byte)0x21, (byte)0x3c, (byte)0x61, (byte)0x72, (byte)0x63, (byte)0x68, (byte)0x3e, (byte)0x0a};

        assertTrue(ArArchiveInputStream.matches(signature, 8));
    }
}