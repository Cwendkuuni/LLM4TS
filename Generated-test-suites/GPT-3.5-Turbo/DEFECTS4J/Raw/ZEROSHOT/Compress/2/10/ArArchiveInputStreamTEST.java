import static org.junit.Assert.*;

import org.apache.commons.compress.archivers.ArchiveEntry;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

public class ArArchiveInputStreamTest {

    private ArArchiveInputStream arInputStream;
    private InputStream input;

    @Before
    public void setUp() throws Exception {
        // Prepare input stream
        byte[] testData = {77, 90};
        input = new ByteArrayInputStream(testData);

        // Create archive input stream
        arInputStream = new ArArchiveInputStream(input);
    }

    @After
    public void tearDown() throws Exception {
        // Close archive input stream
        if(arInputStream != null) {
            arInputStream.close();
        }
        // Close input stream
        if(input != null) {
            input.close();
        }
    }

    @Test
    public void testGetNextArEntry() {
        try {
            ArchiveEntry entry = arInputStream.getNextArEntry();
            assertNull(entry);
        } catch (IOException e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    public void testGetNextEntry() {
        try {
            ArchiveEntry entry = arInputStream.getNextEntry();
            assertNull(entry);
        } catch (IOException e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    public void testClose() {
        try {
            arInputStream.close();
            assertTrue(arInputStream.isClosed());
        } catch (IOException e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    public void testRead() {
        try {
            int result = arInputStream.read();
            assertEquals(77, result);
        } catch (IOException e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    public void testReadByteArray() {
        try {
            byte[] buffer = new byte[2];
            int bytesRead = arInputStream.read(buffer);
            assertArrayEquals(new byte[] {77, 0}, buffer);
            assertEquals(1, bytesRead);
        } catch (IOException e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    public void testReadByteArrayIntInt() {
        try {
            byte[] buffer = new byte[2];
            int bytesRead = arInputStream.read(buffer, 0, 2);
            assertArrayEquals(new byte[] {77, 0}, buffer);
            assertEquals(1, bytesRead);
        } catch (IOException e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    public void testMatches() {
        byte[] signature = {33, 60, 97, 114, 99, 104, 62, 10};
        boolean result = ArArchiveInputStream.matches(signature, 8);
        assertTrue(result);
    }
}