import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import java.io.ByteArrayInputStream;
import java.io.IOException;

public class ArArchiveInputStreamTest {

    private ArArchiveInputStream arInputStream;

    @Before
    public void setUp() {
        ByteArrayInputStream inputStream = new ByteArrayInputStream(new byte[0]);
        arInputStream = new ArArchiveInputStream(inputStream);
    }

    @Test
    public void testGetNextArEntry_WhenValidEntry_ReturnsCorrectEntry() throws IOException {
        byte[] expectedHeader = "header".getBytes();
        byte[] expectedTrailer = "trailer".getBytes();
        byte[] expectedName = "testfile.txt".getBytes();
        byte[] expectedLastModified = "2020-01-01".getBytes();
        byte[] expectedUserId = "1234".getBytes();
        byte[] expectedGroupId = "5678".getBytes();
        byte[] expectedFileMode = "rw-rw-rw-".getBytes();
        byte[] expectedLength = "1024".getBytes();

        byte[] entryBytes = new byte[expectedHeader.length + expectedName.length + expectedLastModified.length +
                                    expectedUserId.length + expectedGroupId.length + expectedFileMode.length +
                                    expectedLength.length + expectedTrailer.length];

        System.arraycopy(expectedHeader, 0, entryBytes, 0, expectedHeader.length);
        System.arraycopy(expectedName, 0, entryBytes, expectedHeader.length, expectedName.length);
        System.arraycopy(expectedLastModified, 0, entryBytes, expectedHeader.length+expectedName.length, expectedLastModified.length);
        System.arraycopy(expectedUserId, 0, entryBytes, expectedHeader.length+expectedName.length+expectedLastModified.length, expectedUserId.length);
        System.arraycopy(expectedGroupId, 0, entryBytes, expectedHeader.length+expectedName.length+expectedLastModified.length+expectedUserId.length, expectedGroupId.length);
        System.arraycopy(expectedFileMode, 0, entryBytes, expectedHeader.length+expectedName.length+expectedLastModified.length+expectedUserId.length+expectedGroupId.length, expectedFileMode.length);
        System.arraycopy(expectedLength, 0, entryBytes, expectedHeader.length+expectedName.length+expectedLastModified.length+expectedUserId.length+expectedGroupId.length+expectedFileMode.length, expectedLength.length);
        System.arraycopy(expectedTrailer, 0, entryBytes, expectedHeader.length+expectedName.length+expectedLastModified.length+expectedUserId.length+expectedGroupId.length+expectedFileMode.length+expectedLength.length, expectedTrailer.length);

        ByteArrayInputStream inputStream = new ByteArrayInputStream(entryBytes);
        arInputStream = new ArArchiveInputStream(inputStream);

        ArArchiveEntry entry = arInputStream.getNextArEntry();

        assertEquals("testfile.txt", entry.getName());
        assertEquals(1024, entry.getLength());
    }

    @Test
    public void testGetNextArEntry_WhenInvalidHeader_ThrowsIOException() throws IOException {
        byte[] invalidHeader = "invalid".getBytes();

        ByteArrayInputStream inputStream = new ByteArrayInputStream(invalidHeader);
        arInputStream = new ArArchiveInputStream(inputStream);

        try {
            arInputStream.getNextArEntry();
            fail("Expected IOException to be thrown");
        } catch (IOException ex) {
            assertEquals("invalid header invalid", ex.getMessage());
        }
    }

    @Test
    public void testGetNextArEntry_WhenEOFReached_ReturnsNull() throws IOException {
        ByteArrayInputStream inputStream = new ByteArrayInputStream(new byte[0]);
        arInputStream = new ArArchiveInputStream(inputStream);

        ArArchiveEntry entry = arInputStream.getNextArEntry();

        assertNull(entry);
    }

    @Test
    public void testGetNextArEntry_WhenIncompleteEntry_ThrowsIOException() throws IOException {
        byte[] expectedHeader = "header".getBytes();
        byte[] expectedName = "testfile.txt".getBytes();

        byte[] entryBytes = new byte[expectedHeader.length + expectedName.length];

        System.arraycopy(expectedHeader, 0, entryBytes, 0, expectedHeader.length);
        System.arraycopy(expectedName, 0, entryBytes, expectedHeader.length, expectedName.length);

        ByteArrayInputStream inputStream = new ByteArrayInputStream(entryBytes);
        arInputStream = new ArArchiveInputStream(inputStream);

        try {
            arInputStream.getNextArEntry();
            fail("Expected IOException to be thrown");
        } catch (IOException ex) {
            assertEquals("failed to read entry header", ex.getMessage());
        }
    }

    @Test
    public void testGetNextArEntry_WhenInvalidEntryHeader_ThrowsIOException() throws IOException {
        byte[] expectedHeader = "header".getBytes();
        byte[] expectedName = "testfile.txt".getBytes();
        byte[] expectedTrailer = "invalid".getBytes();

        byte[] entryBytes = new byte[expectedHeader.length + expectedName.length + expectedTrailer.length];

        System.arraycopy(expectedHeader, 0, entryBytes, 0, expectedHeader.length);
        System.arraycopy(expectedName, 0, entryBytes, expectedHeader.length, expectedName.length);
        System.arraycopy(expectedTrailer, 0, entryBytes, expectedHeader.length+expectedName.length, expectedTrailer.length);

        ByteArrayInputStream inputStream = new ByteArrayInputStream(entryBytes);
        arInputStream = new ArArchiveInputStream(inputStream);

        try {
            arInputStream.getNextArEntry();
            fail("Expected IOException to be thrown");
        } catch (IOException ex) {
            assertEquals("invalid entry header. not read the content?", ex.getMessage());
        }
    }

    @Test
    public void testGetNextEntry_WhenValidEntry_ReturnsCorrectEntry() throws IOException {
        byte[] expectedName = "testfile.txt".getBytes();
        byte[] expectedLength = "1024".getBytes();

        byte[] entryBytes = new byte[expectedName.length + expectedLength.length];

        System.arraycopy(expectedName, 0, entryBytes, 0, expectedName.length);
        System.arraycopy(expectedLength, 0, entryBytes, expectedName.length, expectedLength.length);

        ByteArrayInputStream inputStream = new ByteArrayInputStream(entryBytes);
        arInputStream = new ArArchiveInputStream(inputStream);

        ArchiveEntry entry = arInputStream.getNextEntry();

        assertTrue(entry instanceof ArArchiveEntry);
        assertEquals("testfile.txt", ((ArArchiveEntry) entry).getName());
        assertEquals(1024, ((ArArchiveEntry) entry).getLength());
    }

    @Test
    public void testClose_WhenInputStreamOpen_ClosesInputStream() throws IOException {
        ByteArrayInputStream inputStream = new ByteArrayInputStream(new byte[0]);
        arInputStream = new ArArchiveInputStream(inputStream);

        arInputStream.close();

        assertTrue(arInputStream.isClosed());
    }

    @Test
    public void testClose_WhenInputStreamClosed_DoesNotThrowException() throws IOException {
        ByteArrayInputStream inputStream = new ByteArrayInputStream(new byte[0]);
        arInputStream = new ArArchiveInputStream(inputStream);

        arInputStream.close();

        try {
            arInputStream.close();
        } catch (IOException ex) {
            fail("Unexpected IOException thrown");
        }
    }

    @Test
    public void testRead_WhenDataAvailable_ReturnsNextByte() throws IOException {
        ByteArrayInputStream inputStream = new ByteArrayInputStream(new byte[]{1, 2, 3});
        arInputStream = new ArArchiveInputStream(inputStream);

        int data = arInputStream.read();

        assertEquals(1, data);
        assertEquals(1, arInputStream.getOffset());
    }

    @Test
    public void testRead_WhenNoDataAvailable_ReturnsMinusOne() throws IOException {
        ByteArrayInputStream inputStream = new ByteArrayInputStream(new byte[0]);
        arInputStream = new ArArchiveInputStream(inputStream);

        int data = arInputStream.read();

        assertEquals(-1, data);
        assertEquals(0, arInputStream.getOffset());
    }

    @Test
    public void testRead_WhenDataAvailable_ReturnsNumberOfBytesRead() throws IOException {
        byte[] buffer = new byte[5];
        ByteArrayInputStream inputStream = new ByteArrayInputStream(new byte[]{1, 2, 3});
        arInputStream = new ArArchiveInputStream(inputStream);

        int bytesRead = arInputStream.read(buffer);

        assertEquals(3, bytesRead);
        assertEquals(3, buffer[0]);
        assertEquals(1, arInputStream.getOffset());
    }

    @Test
    public void testRead_WhenNoDataAvailable_ReturnsMinusOneBytesRead() throws IOException {
        byte[] buffer = new byte[5];
        ByteArrayInputStream inputStream = new ByteArrayInputStream(new byte[0]);
        arInputStream = new ArArchiveInputStream(inputStream);

        int bytesRead = arInputStream.read(buffer);

        assertEquals(-1, bytesRead);
        assertEquals(0, arInputStream.getOffset());
    }

    @Test
    public void testRead_WithOffsetAndLength_WhenDataAvailable_ReturnsNumberOfBytesRead() throws IOException {
        byte[] buffer = new byte[5];
        ByteArrayInputStream inputStream = new ByteArrayInputStream(new byte[]{1, 2, 3});
        arInputStream = new ArArchiveInputStream(inputStream);

        int bytesRead = arInputStream.read(buffer, 1, 2);

        assertEquals(2, bytesRead);
        assertEquals(0, buffer[0]);
        assertEquals(1, buffer[1]);
        assertEquals(2, arInputStream.getOffset());
    }

    @Test
    public void testRead_WithOffsetAndLength_WhenNoDataAvailable_ReturnsMinusOneBytesRead() throws IOException {
        byte[] buffer = new byte[5];
        ByteArrayInputStream inputStream = new ByteArrayInputStream(new byte[0]);
        arInputStream = new ArArchiveInputStream(inputStream);

        int bytesRead = arInputStream.read(buffer, 1, 2);

        assertEquals(-1, bytesRead);
        assertEquals(0, buffer[0]);
        assertEquals(0, arInputStream.getOffset());
    }

    @Test
    public void testMatches_WhenSignatureMatches_ReturnsTrue() {
        byte[] signature = {(byte) 0x21, (byte) 0x3c, (byte) 0x61, (byte) 0x72, (byte) 0x63, (byte) 0x68, (byte) 0x3e, (byte) 0x0a};

        assertTrue(ArArchiveInputStream.matches(signature, 8));
    }

    @Test
    public void testMatches_WhenSignatureDoesNotMatch_ReturnsFalse() {
        byte[] signature = {(byte) 0x00, (byte) 0x3c, (byte) 0x61, (byte) 0x72, (byte) 0x63, (byte) 0x68, (byte) 0x3e, (byte) 0x0a};

        assertFalse(ArArchiveInputStream.matches(signature, 8));
    }

}