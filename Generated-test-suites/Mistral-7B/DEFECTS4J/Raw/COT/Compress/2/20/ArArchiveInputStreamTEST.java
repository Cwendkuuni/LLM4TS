import org.apache.commons.compress.archivers.ArArchiveEntry;
import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.archivers.ar.ArArchiveInputStream;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Test class for ArArchiveInputStream.
 */
public class ArArchiveInputStreamTest {

    private InputStream inputStream;
    private ArArchiveInputStream arInputStream;

    @Before
    public void setUp() {
        String testData = "00 3c 21 72 61 68 63 0a 63 61 6c 65 74 65 20 64 6f 6d 70 6c 65 0a 41 63 63 65 70 74 69 6f 6e " +
                "0a 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 61 72 63 68 69 73 74 65 00 00 00 01 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 3e";

        inputStream = new ByteArrayInputStream(testData.getBytes());
        arInputStream = new ArArchiveInputStream(inputStream);
    }

    @After
    public void tearDown() throws IOException {
        if (arInputStream != null) {
            arInputStream.close();
        }
        if (inputStream != null) {
            inputStream.close();
        }
    }

    /**
     * Test method for {@link ArArchiveInputStream#ArArchiveInputStream(InputStream)}.
     */
    @Test
    public void testArArchiveInputStream() {
        // Create an instance of ArArchiveInputStream
        ArArchiveInputStream arInputStream = new ArArchiveInputStream(inputStream);
        assertNotNull(arInputStream);
    }

    /**
     * Test method for {@link ArArchiveInputStream#getNextArEntry()}.
     */
    @Test
    public void testGetNextArEntry() throws IOException {
        ArArchiveEntry entry = arInputStream.getNextArEntry();
        assertNotNull(entry);
    }

    /**
     * Test method for {@link ArArchiveInputStream#getNextArEntry()} with null return value.
     */
    @Test
    public void testGetNextArEntryNull() throws IOException {
        ArArchiveEntry entry = arInputStream.getNextArEntry();
        assertNull(entry);
    }

    /**
     * Test method for {@link ArArchiveInputStream#getNextArEntry()} with empty input stream.
     */
    @Test(expected = IOException.class)
    public void testGetNextArEntryEmptyStream() throws IOException {
        ArArchiveInputStream arInputStream = new ArArchiveInputStream(new ByteArrayInputStream(new byte[0]));
        arInputStream.getNextArEntry();
    }

    /**
     * Test method for {@link ArArchiveInputStream#getNextEntry()}.
     */
    @Test
    public void testGetNextEntry() throws IOException {
        ArchiveEntry entry = arInputStream.getNextEntry();
        assertNotNull(entry);
        assertEquals(entry.getClass(), ArArchiveEntry.class);
    }

    /**
     * Test method for {@link ArArchiveInputStream#getNextEntry()} with null return value.
     */
    @Test
    public void testGetNextEntryNull() throws IOException {
        ArchiveEntry entry = arInputStream.getNextEntry();
        assertNull(entry);
    }

    /**
     * Test method for {@link ArArchiveInputStream#getNextEntry()} with empty input stream.
     */
    @Test(expected = IOException.class)
    public void testGetNextEntryEmptyStream() throws IOException {
        ArArchiveInputStream arInputStream = new ArArchiveInputStream(new ByteArrayInputStream(new byte[0]));
        arInputStream.getNextEntry();
    }

    /**
     * Test method for {@link ArArchiveInputStream#close()}.
     */
    @Test
    public void testClose() throws IOException {
        ArArchiveInputStream arInputStream = new ArArchiveInputStream(inputStream);
        arInputStream.close();
        assertFalse(arInputStream.isOpen());
    }

    /**
     * Test method for {@link ArArchiveInputStream#isOpen()}.
     */
    @Test
    public void testIsOpen() throws IOException {
        ArArchiveInputStream arInputStream = new ArArchiveInputStream(inputStream);
        assertTrue(arInputStream.isOpen());
    }

    /**
     * Test method for {@link ArArchiveInputStream#matches(byte[], int)}.
     */
    @Test
    public void testMatches() {
        byte[] signature = new byte[32];
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        outputStream.write(ArArchiveEntry.HEADER);
        outputStream.write(ArArchiveEntry.TRAILER);
        outputStream.writeTo(inputStream);

        int length = inputStream.available();
        inputStream.read(signature, 0, signature.length);

        assertTrue(ArArchiveInputStream.matches(signature, length));
    }

    /**
     * Test method for {@link ArArchiveInputStream#matches(byte[], int)} with invalid signature.
     */
    @Test
    public void testMatchesInvalidSignature() {
        byte[] signature = new byte[32];
        assertFalse(ArArchiveInputStream.matches(signature, 16));
    }
}