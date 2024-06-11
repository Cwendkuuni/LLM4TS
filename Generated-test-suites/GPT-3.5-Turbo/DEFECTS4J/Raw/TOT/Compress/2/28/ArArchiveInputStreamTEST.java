import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import org.apache.commons.compress.archivers.ar.ArArchiveInputStream;

public class ArArchiveInputStreamTest {

    private ArArchiveInputStream arInputStream;
    private byte[] header;
    private byte[] expectedEntryBytes;
    private byte[] expectedTrailer;

    @Before
    public void setUp() throws IOException {
        header = "#!arch\n".getBytes();
        expectedTrailer = "trailer\n".getBytes();
        expectedEntryBytes = new byte[100];
        arInputStream = new ArArchiveInputStream(new ByteArrayInputStream(header));
    }
    
    @Test
    public void testGetNextArEntry_ShouldReturnValidEntry() throws IOException {
        arInputStream.read(expectedEntryBytes);
        ByteArrayInputStream expectedEntry = new ByteArrayInputStream(expectedEntryBytes);
        
        ArArchiveEntry entry = arInputStream.getNextArEntry();
        
        assertNotNull(entry);
        assertEquals("entry", entry.getName());
        assertEquals(expectedEntry.available(), entry.getSize());
    }

    @Test
    public void testGetNextArEntry_ShouldReturnNullAtEOF() throws IOException {
        ArArchiveEntry entry = arInputStream.getNextArEntry();

        assertNull(entry);
    }

    @Test(expected = IOException.class)
    public void testGetNextArEntry_ShouldThrowIOException_WhenHeaderInvalid() throws IOException {
        byte[] invalidHeader = "invalid\n".getBytes();
        ByteArrayInputStream invalidInputStream = new ByteArrayInputStream(invalidHeader);
        ArArchiveInputStream invalidArInputStream = new ArArchiveInputStream(invalidInputStream);
        
        invalidArInputStream.getNextArEntry();
    }

    @Test(expected = IOException.class)
    public void testGetNextArEntry_ShouldThrowIOException_WhenFailedToReadHeader() throws IOException {
        ByteArrayInputStream inputStream = new ByteArrayInputStream(new byte[5]);
        ArArchiveInputStream arInputStream = new ArArchiveInputStream(inputStream);
        
        arInputStream.getNextArEntry();
    }

    @Test(expected = IOException.class)
    public void testGetNextArEntry_ShouldThrowIOException_WhenFailedToReadEntryHeader() throws IOException {
        arInputStream.read(expectedEntryBytes);
        ByteArrayInputStream inputStream = new ByteArrayInputStream(expectedTrailer);
        ArArchiveInputStream arInputStream = new ArArchiveInputStream(inputStream);
        
        arInputStream.getNextArEntry();
    }

    @Test
    public void testGetNextEntry_ShouldCallGetNextArEntry() throws IOException {
        ArArchiveEntry entry = arInputStream.getNextEntry();

        assertNotNull(entry);
    }

    @Test
    public void testClose_ShouldCloseInputStream() throws IOException {
        arInputStream.close();

        assertTrue(arInputStream.closed);
    }

    @Test
    public void testRead_ShouldReturnMinusOneAtEOF() throws IOException {
        ByteArrayInputStream emptyInputStream = new ByteArrayInputStream(new byte[0]);
        ArArchiveInputStream arInputStream = new ArArchiveInputStream(emptyInputStream);

        assertEquals(-1, arInputStream.read());
    }

    @Test
    public void testRead_ShouldUpdateOffsetOnSuccessfulRead() throws IOException {
        arInputStream.read(expectedEntryBytes, 0, 5);

        assertEquals(5, arInputStream.offset);
    }
    
    @Test
    public void testMatches_ShouldReturnTrueForValidSignature() {
        byte[] signature = {0x21, 0x3c, 0x61, 0x72, 0x63, 0x68, 0x3e, 0x0a};

        assertTrue(ArArchiveInputStream.matches(signature, signature.length));
    }

    @Test
    public void testMatches_ShouldReturnFalseForInvalidSignature() {
        byte[] signature = {0x21, 0x3c, 0x61, 0x72, 0x63, 0x68, 0x3e, 0x09};

        assertFalse(ArArchiveInputStream.matches(signature, signature.length));
    }

}