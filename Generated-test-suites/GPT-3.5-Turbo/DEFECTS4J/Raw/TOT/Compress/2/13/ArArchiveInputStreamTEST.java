import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import static org.junit.Assert.*;

public class ArArchiveInputStreamTest {

    private ArArchiveInputStream arArchiveInputStream;

    @Before
    public void setUp() {
        byte[] data = new byte[] {0x21, 0x3c, 0x61, 0x72, 0x63, 0x68, 0x3e, 0x0a};
        ByteArrayInputStream inputStream = new ByteArrayInputStream(data);
        arArchiveInputStream = new ArArchiveInputStream(inputStream);
    }

    @Test
    public void testGetNextArEntry_ReturnsNextEntry() throws IOException {
        ArArchiveEntry expectedEntry = new ArArchiveEntry("testfile", 10);
        ArArchiveEntry actualEntry = arArchiveInputStream.getNextArEntry();
        assertEquals(expectedEntry.getName(), actualEntry.getName());
        assertEquals(expectedEntry.getLength(), actualEntry.getLength());
    }

    @Test
    public void testGetNextArEntry_ReturnsNullIfNoMoreEntries() throws IOException {
        arArchiveInputStream.getNextArEntry(); // Consume the only entry available
        assertNull(arArchiveInputStream.getNextArEntry());
    }

    @Test(expected = IOException.class)
    public void testGetNextArEntry_ThrowsIOExceptionIfFailedToReadHeader() throws IOException {
        arArchiveInputStream.read(new byte[7]);
        arArchiveInputStream.getNextArEntry();
    }

    @Test(expected = IOException.class)
    public void testGetNextArEntry_ThrowsIOExceptionIfInvalidHeader() throws IOException {
        byte[] data = new byte[] {0x21, 0x3c, 0x61, 0x72, 0x63, 0x68, 0x3e, 0x01};
        ByteArrayInputStream invalidHeaderInputStream = new ByteArrayInputStream(data);
        ArArchiveInputStream invalidHeaderArchiveInputStream = new ArArchiveInputStream(invalidHeaderInputStream);
        invalidHeaderArchiveInputStream.getNextArEntry();
    }

    @Test(expected = IOException.class)
    public void testGetNextArEntry_ThrowsIOExceptionIfFailedToReadEntryHeader() throws IOException {
        ByteArrayInputStream invalidEntryHeaderInputStream = new ByteArrayInputStream(new byte[6]);
        ArArchiveInputStream invalidEntryHeaderArchiveInputStream = new ArArchiveInputStream(invalidEntryHeaderInputStream);
        invalidEntryHeaderArchiveInputStream.getNextArEntry();
    }

    @Test(expected = IOException.class)
    public void testGetNextArEntry_ThrowsIOExceptionIfInvalidEntryHeader() throws IOException {
        byte[] data = new byte[] {0x21, 0x3c, 0x61, 0x72, 0x63, 0x68, 0x3e, 0x0a};
        ByteArrayInputStream inputStream = new ByteArrayInputStream(data);
        ArArchiveInputStream archiveInputStream = new ArArchiveInputStream(inputStream);
        archiveInputStream.getNextArEntry();
    }

    @Test
    public void testGetNextEntry_ReturnsNextEntry() throws IOException {
        ArArchiveEntry expectedEntry = new ArArchiveEntry("testfile", 10);
        ArchiveEntry actualEntry = arArchiveInputStream.getNextEntry();
        assertEquals(expectedEntry.getName(), actualEntry.getName());
        assertEquals(expectedEntry.getLength(), actualEntry.getLength());
    }

    @Test
    public void testClose_ClosesInputStream() throws IOException {
        arArchiveInputStream.close();
        assertFalse(arArchiveInputStream.input.available() > 0);
    }

    @Test
    public void testRead_ReadsSingleByte() throws IOException {
        int expectedByte = 0x21;
        int actualByte = arArchiveInputStream.read();
        assertEquals(expectedByte, actualByte);
    }

    @Test
    public void testRead_ReadsBytesIntoArray() throws IOException {
        byte[] expectedBytes = new byte[4];
        expectedBytes[0] = 0x3c;
        expectedBytes[1] = 0x61;
        expectedBytes[2] = 0x72;
        expectedBytes[3] = 0x63;
        byte[] actualBytes = new byte[4];
        arArchiveInputStream.read(actualBytes);
        assertArrayEquals(expectedBytes, actualBytes);
    }

    @Test
    public void testRead_ReadsBytesIntoArrayWithOffsetAndLength() throws IOException {
        byte[] expectedBytes = new byte[2];
        expectedBytes[0] = 0x61;
        expectedBytes[1] = 0x72;
        byte[] actualBytes = new byte[4];
        arArchiveInputStream.read(actualBytes, 1, 2);
        assertArrayEquals(expectedBytes, actualBytes);
    }

    @Test
    public void testMatches_ReturnsTrueIfSignatureMatches() {
        byte[] signature = new byte[] {0x21, 0x3c, 0x61, 0x72, 0x63, 0x68, 0x3e, 0x0a};
        assertTrue(ArArchiveInputStream.matches(signature, signature.length));
    }

    @Test
    public void testMatches_ReturnsFalseIfSignatureLengthIsLessThan8() {
        byte[] signature = new byte[] {0x21, 0x3c, 0x61, 0x72, 0x63, 0x68};
        assertFalse(ArArchiveInputStream.matches(signature, signature.length));
    }

    @Test
    public void testMatches_ReturnsFalseIfSignatureDoesNotMatch() {
        byte[] signature = new byte[] {0x21, 0x3c, 0x61, 0x72, 0x63, 0x68, 0x3f, 0x0a};
        assertFalse(ArArchiveInputStream.matches(signature, signature.length));
    }

}