import static org.junit.Assert.*;
import org.junit.Test;
import java.io.ByteArrayInputStream;
import java.io.IOException;

public class ArArchiveInputStreamTest {

    @Test
    public void testGetNextArEntry() {
        ByteArrayInputStream input = new ByteArrayInputStream("!<arch>\n".getBytes());
        ArArchiveInputStream stream = new ArArchiveInputStream(input);

        try {
            ArArchiveEntry entry = stream.getNextArEntry();
            // Assert that the entry is not null
            assertNotNull(entry);

            // Assert that the entry name is correct
            assertEquals("", entry.getName());

            // Assert that the entry length is correct
            assertEquals(0, entry.getLength());

            // Assert that there are no more entries
            assertNull(stream.getNextArEntry());

        } catch (IOException e) {
            fail("IOException occurred");
        } finally {
            try {
                stream.close();
            } catch (IOException e) {
                fail("IOException occurred while closing stream");
            }
        }
    }

    @Test
    public void testGetNextEntry() {
        ByteArrayInputStream input = new ByteArrayInputStream("!<arch>\n".getBytes());
        ArArchiveInputStream stream = new ArArchiveInputStream(input);

        try {
            ArchiveEntry entry = stream.getNextEntry();
            // Assert that the entry is not null
            assertNotNull(entry);

            // Assert that the entry name is correct
            assertEquals("", entry.getName());

            // Assert that the entry length is correct
            assertEquals(0, entry.getLength());

            // Assert that there are no more entries
            assertNull(stream.getNextEntry());

        } catch (IOException e) {
            fail("IOException occurred");
        } finally {
            try {
                stream.close();
            } catch (IOException e) {
                fail("IOException occurred while closing stream");
            }
        }
    }

    @Test
    public void testClose() {
        ByteArrayInputStream input = new ByteArrayInputStream("!<arch>\n".getBytes());
        ArArchiveInputStream stream = new ArArchiveInputStream(input);

        try {
            assertFalse(stream.isClosed());

            stream.close();

            assertTrue(stream.isClosed());

        } catch (IOException e) {
            fail("IOException occurred");
        }
    }

    @Test
    public void testRead() {
        ByteArrayInputStream input = new ByteArrayInputStream("!<arch>".getBytes());
        ArArchiveInputStream stream = new ArArchiveInputStream(input);

        try {
            int data = stream.read();
            // Assert that the data read is correct
            assertEquals(33, data);

        } catch (IOException e) {
            fail("IOException occurred");
        } finally {
            try {
                stream.close();
            } catch (IOException e) {
                fail("IOException occurred while closing stream");
            }
        }
    }

    @Test
    public void testReadByteArray() {
        ByteArrayInputStream input = new ByteArrayInputStream("!<arch>".getBytes());
        ArArchiveInputStream stream = new ArArchiveInputStream(input);

        try {
            byte[] data = new byte[8];
            stream.read(data);
            // Assert that the data read is correct
            assertArrayEquals(new byte[]{33, 60, 97, 114, 99, 104, 62, 10}, data);

        } catch (IOException e) {
            fail("IOException occurred");
        } finally {
            try {
                stream.close();
            } catch (IOException e) {
                fail("IOException occurred while closing stream");
            }
        }
    }

    @Test
    public void testReadByteArrayIntInt() {
        ByteArrayInputStream input = new ByteArrayInputStream("!<arch>".getBytes());
        ArArchiveInputStream stream = new ArArchiveInputStream(input);

        try {
            byte[] data = new byte[8];
            stream.read(data, 0, 5);
            // Assert that the data read is correct
            assertArrayEquals(new byte[]{33, 60, 97, 114, 99, 0, 0, 0}, data);

        } catch (IOException e) {
            fail("IOException occurred");
        } finally {
            try {
                stream.close();
            } catch (IOException e) {
                fail("IOException occurred while closing stream");
            }
        }
    }

    @Test
    public void testMatches() {
        byte[] signature = new byte[]{33, 60, 97, 114, 99, 104, 62, 10};
        int length = 8;
        
        boolean matches = ArArchiveInputStream.matches(signature, length);
        // Assert that the matches is true
        assertTrue(matches);
    }
}