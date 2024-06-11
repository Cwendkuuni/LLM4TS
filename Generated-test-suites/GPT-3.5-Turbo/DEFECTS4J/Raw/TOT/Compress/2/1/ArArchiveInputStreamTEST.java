import org.junit.Before;
import org.junit.Test;
import java.io.ByteArrayInputStream;
import java.io.EOFException;
import java.io.IOException;
import static org.junit.Assert.*;

public class ArArchiveInputStreamTest {

    private ArArchiveInputStream archiveInputStream;

    @Before
    public void setUp() {
        byte[] archiveData = new byte[30];
        archiveInputStream = new ArArchiveInputStream(new ByteArrayInputStream(archiveData));
    }

    @Test
    public void testGetNextArEntry_Success() throws IOException {
        ArArchiveEntry entry = archiveInputStream.getNextArEntry();
        assertNotNull("ArArchiveEntry should not be null", entry);
    }

    @Test(expected = IOException.class)
    public void testGetNextArEntry_InvalidHeaderIOException() throws IOException {
        byte[] invalidHeader = {0x21, 0x3c, 0x99, 0x72, 0x63, 0x68, 0x3e, 0x0a};
        ByteArrayInputStream inputStream = new ByteArrayInputStream(invalidHeader);
        ArArchiveInputStream archiveInputStream = new ArArchiveInputStream(inputStream);
        archiveInputStream.getNextArEntry();
    }

    @Test
    public void testGetNextArEntry_EOFException() throws IOException {
        // Simulate end of file by closing the input stream
        archiveInputStream.close();
        ArArchiveEntry entry = archiveInputStream.getNextArEntry();
        assertNull("ArArchiveEntry should be null when end of file is reached", entry);
    }

    @Test
    public void testGetNextArEntry_FileOffset() throws IOException {
        long previousOffset = archiveInputStream.getOffset();
        archiveInputStream.getNextArEntry();
        long currentOffset = archiveInputStream.getOffset();
        assertTrue("Offset should increase after calling getNextArEntry()", currentOffset > previousOffset);
    }

    @Test(expected = IOException.class)
    public void testRead_IOException() throws IOException {
        archiveInputStream.close();
        archiveInputStream.read();
    }

    @Test
    public void testRead_Success() throws IOException {
        int data = archiveInputStream.read();
        assertNotEquals("Read data should not be -1", -1, data);
    }

    @Test
    public void testReadBuffer_Success() throws IOException {
        byte[] buffer = new byte[10];
        int bytesRead = archiveInputStream.read(buffer);
        assertNotEquals("Bytes read should not be -1", -1, bytesRead);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testReadBuffer_InvalidOffset() throws IOException {
        byte[] buffer = new byte[10];
        archiveInputStream.read(buffer, -1, buffer.length);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testReadBuffer_InvalidLength() throws IOException {
        byte[] buffer = new byte[10];
        archiveInputStream.read(buffer, 0, buffer.length + 1);
    }

    @Test(expected = EOFException.class)
    public void testReadBuffer_EOFException() throws IOException {
        byte[] buffer = new byte[10];
        // Simulate end of file by closing the input stream
        archiveInputStream.close();
        archiveInputStream.read(buffer);
    }

    @Test
    public void testReadBufferOffsetLength_Success() throws IOException {
        byte[] buffer = new byte[10];
        int bytesRead = archiveInputStream.read(buffer, 0, buffer.length);
        assertNotEquals("Bytes read should not be -1", -1, bytesRead);
    }

    @Test(expected = EOFException.class)
    public void testReadBufferOffsetLength_EOFException() throws IOException {
        byte[] buffer = new byte[10];
        // Simulate end of file by closing the input stream
        archiveInputStream.close();
        archiveInputStream.read(buffer, 0, buffer.length);
    }

    @Test
    public void testMatches_True() {
        byte[] signature = {0x21, 0x3c, 0x61, 0x72, 0x63, 0x68, 0x3e, 0x0a};
        assertTrue("Signature should match", ArArchiveInputStream.matches(signature, signature.length));
    }

    @Test
    public void testMatches_False() {
        byte[] signature = {0x21, 0x3c, 0x99, 0x72, 0x63, 0x68, 0x3e, 0x0a};
        assertFalse("Signature should not match", ArArchiveInputStream.matches(signature, signature.length));
    }

    @Test
    public void testMatches_ShortSignature() {
        byte[] signature = {0x21, 0x3c};
        assertFalse("Signature should not match", ArArchiveInputStream.matches(signature, signature.length));
    }

    @Test
    public void testMatches_NullSignature() {
        assertFalse("Signature should not match when null", ArArchiveInputStream.matches(null, 8));
    }
}