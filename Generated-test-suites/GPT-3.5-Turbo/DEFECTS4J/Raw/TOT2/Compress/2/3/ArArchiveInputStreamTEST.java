import static org.junit.Assert.*;
import org.junit.Test;

public class ArArchiveInputStreamTest {

    @Test
    public void testGetNextArEntry() {
        // Test normal case
        ArArchiveInputStream ais = new ArArchiveInputStream(null);
        assertNotNull(ais.getNextArEntry());

        // Test case when header cannot be read
        ArArchiveInputStream ais2 = new ArArchiveInputStream(null) {
            @Override
            public int read(byte[] b) throws IOException {
                return -1;
            }
        };
        try {
            ais2.getNextArEntry();
            fail("IOException expected");
        } catch (IOException e) {
            // Expected
        }
    }

    @Test
    public void testGetNextEntry() {
        // Test normal case
        ArArchiveInputStream ais = new ArArchiveInputStream(null);
        assertNotNull(ais.getNextEntry());

        // Test case when input stream is closed
        IOException expectedException = null;
        ArArchiveInputStream ais2 = new ArArchiveInputStream(null) {
            @Override
            public ArchiveEntry getNextEntry() throws IOException {
                throw new IOException("Input stream closed");
            }
        };
        try {
            ais2.getNextEntry();
        } catch (IOException e) {
            expectedException = e;
        }
        assertEquals("Input stream closed", expectedException.getMessage());
    }

    @Test
    public void testClose() throws IOException {
        // Test case where close() is called only once
        ArArchiveInputStream ais = new ArArchiveInputStream(null);
        ais.close();        
    }

    @Test
    public void testRead() throws IOException {
        // Test normal case
        ArArchiveInputStream ais = new ArArchiveInputStream(null);
        assertEquals(1, ais.read());

        // Test case when reading fails
        ArArchiveInputStream ais2 = new ArArchiveInputStream(null) {
            @Override
            public int read() throws IOException {
                throw new IOException("Failed to read");
            }
        };
        try {
            ais2.read();
            fail("IOException expected");
        } catch (IOException e) {
            // Expected
        }
    }

    @Test
    public void testMatches() {
        // Test case where signature matches
        assertTrue(ArArchiveInputStream.matches(new byte[]{0x3c, 0x21, 0x72, 0x61, 0x68, 0x63, 0x3e, 0x0a}, 8));

        // Test case where signature does not match
        assertFalse(ArArchiveInputStream.matches(new byte[]{0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00}, 8));

        // Test case where length is less than 8
        assertFalse(ArArchiveInputStream.matches(new byte[]{0x3c, 0x21, 0x72, 0x61, 0x68, 0x63, 0x3e, 0x0a}, 4));
    }

}