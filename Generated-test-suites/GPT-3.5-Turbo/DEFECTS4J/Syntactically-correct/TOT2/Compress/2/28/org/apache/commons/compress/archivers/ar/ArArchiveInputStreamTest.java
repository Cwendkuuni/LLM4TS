import static org.junit.Assert.*;
import org.junit.Test;
import org.mockito.Mockito;
import java.io.ByteArrayInputStream;
import java.io.IOException;

public class ArArchiveInputStreamTest {

    @Test
    public void testGetNextArEntry() throws IOException {
        byte[] testData = "myTestArchiveData".getBytes();
        ByteArrayInputStream inputStream = new ByteArrayInputStream(testData);
        ArArchiveInputStream archiveInputStream = new ArArchiveInputStream(inputStream);

        ArArchiveEntry entry = archiveInputStream.getNextArEntry();

        assertEquals("myTestArchiveData", entry.getName());
        assertEquals(16, entry.getSize());
    }

    @Test
    public void testGetNextArEntryWhenEOF() throws IOException {
        ByteArrayInputStream inputStream = Mockito.mock(ByteArrayInputStream.class);
        Mockito.when(inputStream.available()).thenReturn(0);
        ArArchiveInputStream archiveInputStream = new ArArchiveInputStream(inputStream);

        ArArchiveEntry entry = archiveInputStream.getNextArEntry();

        assertNull(entry);
    }

    @Test(expected = IOException.class)
    public void testGetNextArEntryInvalidHeader() throws IOException {
        String testData = "notValidHeader";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(testData.getBytes());
        ArArchiveInputStream archiveInputStream = new ArArchiveInputStream(inputStream);

        archiveInputStream.getNextArEntry();
    }

    @Test
    public void testGetNextEntry() throws IOException {
        byte[] testData = "myTestArchiveData".getBytes();
        ByteArrayInputStream inputStream = new ByteArrayInputStream(testData);
        ArArchiveInputStream archiveInputStream = new ArArchiveInputStream(inputStream);

        ArchiveEntry entry = archiveInputStream.getNextEntry();

        assertNotNull(entry);
    }

    @Test
    public void testClose() throws IOException {
        ByteArrayInputStream inputStream = new ByteArrayInputStream(new byte[0]);
        ArArchiveInputStream archiveInputStream = new ArArchiveInputStream(inputStream);

        archiveInputStream.close();

        assertTrue(archiveInputStream.isClosed());
    }

    @Test
    public void testRead() throws IOException {
        ByteArrayInputStream inputStream = new ByteArrayInputStream("test data".getBytes());
        ArArchiveInputStream archiveInputStream = new ArArchiveInputStream(inputStream);

        int data = archiveInputStream.read();

        assertEquals('t', data);
    }

    @Test
    public void testReadBuffer() throws IOException {
        byte[] testData = "test data for buffer".getBytes();
        ByteArrayInputStream inputData = new ByteArrayInputStream(testData);
        byte[] buffer = new byte[4];
        ArArchiveInputStream archiveInputStream = new ArArchiveInputStream(inputData);

        int bytesRead = archiveInputStream.read(buffer, 0, 4);

        assertEquals(4, bytesRead);
        assertArrayEquals("test".getBytes(), buffer);
    }

    @Test
    public void testMatchesValidSignature() {
        byte[] validSignature = {0x21, 0x3c, 0x61, 0x72, 0x63, 0x68, 0x3e, 0x0a};
        boolean result = ArArchiveInputStream.matches(validSignature, 8);

        assertTrue(result);
    }

    @Test
    public void testMatchesInvalidSignature() {
        byte[] invalidSignature = {0x21, 0x3c, 0x61, 0x72, 0x63, 0x68, 0x3e};
        boolean result = ArArchiveInputStream.matches(invalidSignature, 7);

        assertFalse(result);
    }
}