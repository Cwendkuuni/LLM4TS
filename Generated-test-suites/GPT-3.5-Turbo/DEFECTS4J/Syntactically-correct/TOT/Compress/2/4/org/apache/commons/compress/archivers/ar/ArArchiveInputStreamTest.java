import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.compress.archivers.ar.ArArchiveEntry;
import org.apache.commons.compress.archivers.ar.ArArchiveInputStream;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class ArArchiveInputStreamTest {

    private ArArchiveInputStream arInputStream;
    private byte[] arHeader;
    private byte[] arEntryHeader;
    private byte[] arEntryData;
    private byte[] arTrailer;

    @Before
    public void setUp() throws Exception {
        // Set up mock AR archive input stream
        arHeader = "!<arch>\n".getBytes();
        arEntryHeader = "testFile.txt   1634590743  1000  1000  644  10      ".getBytes();
        arEntryData = "Test data.".getBytes();
        arTrailer = "`\n".getBytes();
        byte[] arArchiveData = new byte[arHeader.length + arEntryHeader.length + arEntryData.length +
                arTrailer.length];
        System.arraycopy(arHeader, 0, arArchiveData, 0, arHeader.length);
        System.arraycopy(arEntryHeader, 0, arArchiveData, arHeader.length, arEntryHeader.length);
        System.arraycopy(arEntryData, 0, arArchiveData, arHeader.length + arEntryHeader.length, arEntryData.length);
        System.arraycopy(arTrailer, 0, arArchiveData, arHeader.length + arEntryHeader.length + arEntryData.length,
                arTrailer.length);
        InputStream inputStream = new ByteArrayInputStream(arArchiveData);
        arInputStream = new ArArchiveInputStream(inputStream);
    }

    @Test
    public void testGetNextArEntry() throws IOException {
        // Test for typical use case
        ArArchiveEntry entry = arInputStream.getNextArEntry();
        assertNotNull(entry);
        assertEquals("testFile.txt", entry.getName());
        assertEquals(1634590743, entry.getLastModifiedDate());
        assertEquals(1000, entry.getUserId());
        assertEquals(1000, entry.getGroupId());
        assertEquals(644, entry.getFileMode());
        assertEquals(10, entry.getLength());
        // Test for edge case with empty AR entry
        ArArchiveEntry emptyEntry = arInputStream.getNextArEntry();
        assertNotNull(emptyEntry);
        assertEquals("", emptyEntry.getName());
        assertEquals(0, emptyEntry.getLastModifiedDate());
        assertEquals(0, emptyEntry.getUserId());
        assertEquals(0, emptyEntry.getGroupId());
        assertEquals(0, emptyEntry.getFileMode());
        assertEquals(0, emptyEntry.getLength());
        // Test for error scenario with invalid AR header
        arInputStream.read(); // Skip a byte to make the header invalid
        try {
            arInputStream.getNextArEntry();
            fail("IOException not thrown for invalid AR header");
        } catch (IOException e) {
            assertEquals("failed to read header", e.getMessage());
        }
    }

    @Test
    public void testGetNextEntry() throws IOException {
        // Test for typical use case
        ArArchiveEntry entry = arInputStream.getNextEntry();
        assertNotNull(entry);
        assertEquals("testFile.txt", entry.getName());
        assertEquals(1634590743, entry.getLastModifiedDate());
        assertEquals(1000, entry.getUserId());
        assertEquals(1000, entry.getGroupId());
        assertEquals(644, entry.getFileMode());
        assertEquals(10, entry.getLength());
        // Test for edge case with empty AR entry
        ArArchiveEntry emptyEntry = arInputStream.getNextEntry();
        assertNotNull(emptyEntry);
        assertEquals("", emptyEntry.getName());
        assertEquals(0, emptyEntry.getLastModifiedDate());
        assertEquals(0, emptyEntry.getUserId());
        assertEquals(0, emptyEntry.getGroupId());
        assertEquals(0, emptyEntry.getFileMode());
        assertEquals(0, emptyEntry.getLength());
        // Test for error scenario with invalid AR header
        arInputStream.read(); // Skip a byte to make the header invalid
        try {
            arInputStream.getNextEntry();
            fail("IOException not thrown for invalid AR header");
        } catch (IOException e) {
            assertEquals("failed to read header", e.getMessage());
        }
    }

    @Test
    public void testClose() throws IOException {
        assertFalse(arInputStream.isClosed());
        arInputStream.close();
        assertTrue(arInputStream.isClosed());
    }

    @Test
    public void testReadByte() throws IOException {
        int data = arInputStream.read();
        assertEquals('!', data);
        byte[] buffer = new byte[10];
        int bytesRead = arInputStream.read(buffer);
        assertEquals(10, bytesRead);
        assertEquals(arEntryData[0], buffer[0]);
        assertEquals(arEntryData[9], buffer[9]);
        assertEquals(-1, arInputStream.read());
    }

    @Test
    public void testReadByteArray() throws IOException {
        byte[] buffer = new byte[arEntryData.length + 5];
        int bytesRead = arInputStream.read(buffer);
        assertEquals(arEntryData.length, bytesRead);
        assertEquals(arEntryData[0], buffer[0]);
        assertEquals(arEntryData[9], buffer[9]);
        assertEquals(-1, arInputStream.read(buffer));
    }

    @Test
    public void testReadByteArrayWithOffsetAndLength() throws IOException {
        byte[] buffer = new byte[arEntryData.length + 5];
        int bytesRead = arInputStream.read(buffer, 1, arEntryData.length);
        assertEquals(arEntryData.length, bytesRead);
        assertEquals(0, buffer[0]);
        assertEquals(arEntryData[0], buffer[1]);
        assertEquals(arEntryData[9], buffer[10]);
        assertEquals(-1, arInputStream.read(buffer, 1, 1));
        assertEquals(0, buffer[1]);
    }

    @Test
    public void testMatches() {
        byte[] signature = new byte[8];
        System.arraycopy("!<arch>\n".getBytes(), 0, signature, 0, 8);
        assertTrue(ArArchiveInputStream.matches(signature, 8));
        byte[] invalidSignature = new byte[8];
        assertFalse(ArArchiveInputStream.matches(invalidSignature, 8));
        byte[] shortSignature = new byte[7];
        assertFalse(ArArchiveInputStream.matches(shortSignature, 7));
    }

    @After
    public void tearDown() throws IOException {
        arInputStream.close();
    }

}