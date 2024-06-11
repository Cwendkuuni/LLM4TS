import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class ArArchiveInputStreamTest {

    private ArArchiveInputStream inputStream;

    @Before
    public void setUp() throws Exception {
        inputStream = new ArArchiveInputStream(null);
    }

    @After
    public void tearDown() throws Exception {
        inputStream = null;
    }

    @Test
    public void testGetNextArEntry_NormalCase() throws Exception {
        ArArchiveEntry entry = inputStream.getNextArEntry();
        assertNull(entry);
    }

    @Test(expected = IOException.class)
    public void testGetNextArEntry_FailedToReadHeader() throws Exception {
        // Set up invalid header
        inputStream.read(new byte[8]);
        inputStream.getNextArEntry();
    }

    @Test(expected = IOException.class)
    public void testGetNextArEntry_InvalidHeader() throws Exception {
        // Set up invalid header
        byte[] header = "INVALID".getBytes();
        inputStream.read(header);
        inputStream.getNextArEntry();
    }

    @Test(expected = IOException.class)
    public void testGetNextArEntry_FailedToReadEntryHeader() throws Exception {
        // Set up valid header
        byte[] header = ArArchiveEntry.HEADER.getBytes();
        inputStream.read(header);

        // Set up invalid entry header
        inputStream.read(new byte[8]);

        inputStream.getNextArEntry();
    }

    @Test
    public void testGetNextArEntry_ReadEntryWithOddOffset() throws Exception {
        // Set up valid header
        byte[] header = ArArchiveEntry.HEADER.getBytes();
        inputStream.read(header);

        // Set up valid entry
        byte[] entryBytes = "file    ".getBytes();
        inputStream.read(entryBytes);

        // Set up valid trailer
        byte[] trailer = ArArchiveEntry.TRAILER.getBytes();
        inputStream.read(trailer);

        ArArchiveEntry entry = inputStream.getNextArEntry();
        assertNotNull(entry);
        assertEquals("file", entry.getName());
    }

    @Test
    public void testGetNextArEntry_ReadEntryWithEvenOffset() throws Exception {
        // Set up valid header
        byte[] header = ArArchiveEntry.HEADER.getBytes();
        inputStream.read(header);

        // Set up valid entry with even offset
        byte[] entryBytes = "file    ".getBytes();
        inputStream.read(entryBytes);
        inputStream.read(); // Additional read to make offset even

        // Set up valid trailer
        byte[] trailer = ArArchiveEntry.TRAILER.getBytes();
        inputStream.read(trailer);

        ArArchiveEntry entry = inputStream.getNextArEntry();
        assertNotNull(entry);
        assertEquals("file", entry.getName());
    }

    @Test(expected = IOException.class)
    public void testGetNextArEntry_ReadEntryWithInvalidTrailer() throws Exception {
        // Set up valid header
        byte[] header = ArArchiveEntry.HEADER.getBytes();
        inputStream.read(header);

        // Set up valid entry
        byte[] entryBytes = "file    ".getBytes();
        inputStream.read(entryBytes);

        // Set up invalid trailer
        byte[] trailer = "INVALID".getBytes();
        inputStream.read(trailer);

        ArArchiveEntry entry = inputStream.getNextArEntry();
    }

    @Test
    public void testGetNextEntry_NormalCase() throws Exception {
        ArchiveEntry entry = inputStream.getNextEntry();
        assertNull(entry);
    }

    @Test
    public void testClose() throws Exception {
        inputStream.close();
        assertTrue(inputStream.isClosed());
    }

    @Test
    public void testRead() throws Exception {
        byte[] buffer = new byte[4];
        int bytesRead = inputStream.read(buffer);
        assertEquals(0, bytesRead);
    }

    @Test
    public void testReadWithOffsetAndLength() throws Exception {
        byte[] buffer = new byte[8];
        int bytesRead = inputStream.read(buffer, 2, 4);
        assertEquals(0, bytesRead);
        assertArrayEquals(new byte[8], buffer);
    }

    @Test
    public void testMatches_ValidSignature() {
        byte[] signature = { 0x21, 0x3c, 0x61, 0x72, 0x63, 0x68, 0x3e, 0x0a };
        boolean result = ArArchiveInputStream.matches(signature, 8);
        assertTrue(result);
    }

    @Test
    public void testMatches_ShorterSignature() {
        byte[] signature = { 0x21, 0x3c, 0x61, 0x72 };
        boolean result = ArArchiveInputStream.matches(signature, 4);
        assertFalse(result);
    }

    @Test
    public void testMatches_InvalidSignature() {
        byte[] signature = { 0x21, 0x3c, 0x61, 0x72, 0x63, 0x4E, 0x3e, 0x0a };
        boolean result = ArArchiveInputStream.matches(signature, 8);
        assertFalse(result);
    }

}