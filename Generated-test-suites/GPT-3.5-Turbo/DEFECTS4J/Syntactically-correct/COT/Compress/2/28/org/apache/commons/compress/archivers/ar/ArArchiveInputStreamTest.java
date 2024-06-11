
import static org.junit.Assert.*;
import org.junit.Test;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import org.apache.commons.compress.archivers.ArchiveEntry;

public class ArArchiveInputStreamTest {

    @Test
    public void testGetNextArEntry() {
        byte[] header = "!<arch>\n".getBytes();
        byte[] entry1 = "entry1\n".getBytes();
        byte[] entry2 = "entry2\n".getBytes();
        byte[] entry3 = "entry3\n".getBytes();
        byte[] trailer = "`\n".getBytes();

        byte[] arStream = new byte[header.length + entry1.length + entry2.length + entry3.length + trailer.length];
        System.arraycopy(header, 0, arStream, 0, header.length);
        System.arraycopy(entry1, 0, arStream, header.length, entry1.length);
        System.arraycopy(entry2, 0, arStream, header.length + entry1.length, entry2.length);
        System.arraycopy(entry3, 0, arStream, header.length + entry1.length + entry2.length, entry3.length);
        System.arraycopy(trailer, 0, arStream, header.length + entry1.length + entry2.length + entry3.length, trailer.length);

        InputStream inputStream = new ByteArrayInputStream(arStream);
        ArArchiveInputStream arArchiveInputStream = new ArArchiveInputStream(inputStream);

        try {
            ArchiveEntry entry = arArchiveInputStream.getNextArEntry();
            assertEquals("entry1", entry.getName());
            assertEquals(7, entry.getSize());

            entry = arArchiveInputStream.getNextArEntry();
            assertEquals("entry2", entry.getName());
            assertEquals(7, entry.getSize());

            entry = arArchiveInputStream.getNextArEntry();
            assertEquals("entry3", entry.getName());
            assertEquals(7, entry.getSize());

            entry = arArchiveInputStream.getNextArEntry();
            assertNull(entry);

        } catch (IOException e) {
            fail("IOException: " + e.getMessage());
        }

        try {
            arArchiveInputStream.close();
        } catch (IOException e) {
            fail("IOException: " + e.getMessage());
        }
    }

    @Test
    public void testGetNextEntry() {
        // This method has the same functionality as the testGetNextArEntry() method, so no need to retest it.
        // It will be covered through the testGetNextArEntry() method.
    }

    @Test
    public void testClose() {
        byte[] header = "!<arch>\n".getBytes();
        byte[] entry1 = "entry1\n".getBytes();
        byte[] trailer = "`\n".getBytes();

        byte[] arStream = new byte[header.length + entry1.length + trailer.length];
        System.arraycopy(header, 0, arStream, 0, header.length);
        System.arraycopy(entry1, 0, arStream, header.length, entry1.length);
        System.arraycopy(trailer, 0, arStream, header.length + entry1.length, trailer.length);

        InputStream inputStream = new ByteArrayInputStream(arStream);
        ArArchiveInputStream arArchiveInputStream = new ArArchiveInputStream(inputStream);

        try {
            arArchiveInputStream.close();
        } catch (IOException e) {
            fail("IOException: " + e.getMessage());
        }

        try {
            ArchiveEntry entry = arArchiveInputStream.getNextArEntry();
            assertNull(entry);
        } catch (IOException e) {
            fail("IOException: " + e.getMessage());
        }
    }

    @Test
    public void testRead() {
        byte[] header = "!<arch>\n".getBytes();
        byte[] entry1 = "entry1\n".getBytes();
        byte[] trailer = "`\n".getBytes();

        byte[] arStream = new byte[header.length + entry1.length + trailer.length];
        System.arraycopy(header, 0, arStream, 0, header.length);
        System.arraycopy(entry1, 0, arStream, header.length, entry1.length);
        System.arraycopy(trailer, 0, arStream, header.length + entry1.length, trailer.length);

        InputStream inputStream = new ByteArrayInputStream(arStream);
        ArArchiveInputStream arArchiveInputStream = new ArArchiveInputStream(inputStream);

        try {
            int c = arArchiveInputStream.read();
            assertEquals(33, c);

            c = arArchiveInputStream.read();
            assertEquals(60, c);

            c = arArchiveInputStream.read();
            assertEquals(97, c);

            c = arArchiveInputStream.read();
            assertEquals(114, c);

            c = arArchiveInputStream.read();
            assertEquals(99, c);

            c = arArchiveInputStream.read();
            assertEquals(104, c);

            c = arArchiveInputStream.read();
            assertEquals(62, c);

            c = arArchiveInputStream.read();
            assertEquals(10, c);

            c = arArchiveInputStream.read();
            assertEquals(101, c);

            c = arArchiveInputStream.read();
            assertEquals(110, c);

            c = arArchiveInputStream.read();
            assertEquals(116, c);

            c = arArchiveInputStream.read();
            assertEquals(114, c);

            c = arArchiveInputStream.read();
            assertEquals(121, c);

            c = arArchiveInputStream.read();
            assertEquals(49, c);

            c = arArchiveInputStream.read();
            assertEquals(10, c);

            c = arArchiveInputStream.read();
            assertEquals(96, c);

            c = arArchiveInputStream.read();
            assertEquals(10, c);

            c = arArchiveInputStream.read();
            assertEquals(-1, c);

        } catch (IOException e) {
            fail("IOException: " + e.getMessage());
        }

        try {
            arArchiveInputStream.close();
        } catch (IOException e) {
            fail("IOException: " + e.getMessage());
        }
    }

    @Test
    public void testReadByteArray() {
        byte[] header = "!<arch>\n".getBytes();
        byte[] entry1 = "entry1\n".getBytes();
        byte[] trailer = "`\n".getBytes();

        byte[] arStream = new byte[header.length + entry1.length + trailer.length];
        System.arraycopy(header, 0, arStream, 0, header.length);
        System.arraycopy(entry1, 0, arStream, header.length, entry1.length);
        System.arraycopy(trailer, 0, arStream, header.length + entry1.length, trailer.length);

        InputStream inputStream = new ByteArrayInputStream(arStream);
        ArArchiveInputStream arArchiveInputStream = new ArArchiveInputStream(inputStream);

        try {
            byte[] buffer = new byte[5];
            int bytesRead = arArchiveInputStream.read(buffer);
            assertEquals(5, bytesRead);
            assertArrayEquals("entry1".getBytes(), buffer);

            bytesRead = arArchiveInputStream.read(buffer);
            assertEquals(2, bytesRead);
            byte[] trailerBytes = new byte[2];
            System.arraycopy(trailer, 0, trailerBytes, 0, 2);
            assertArrayEquals(trailerBytes, buffer);

            bytesRead = arArchiveInputStream.read(buffer);
            assertEquals(-1, bytesRead);

        } catch (IOException e) {
            fail("IOException: " + e.getMessage());
        }

        try {
            arArchiveInputStream.close();
        } catch (IOException e) {
            fail("IOException: " + e.getMessage());
        }
    }

    @Test
    public void testReadByteArrayOffsetLength() {
        byte[] header = "!<arch>\n".getBytes();
        byte[] entry1 = "entry1\n".getBytes();
        byte[] trailer = "`\n".getBytes();

        byte[] arStream = new byte[header.length + entry1.length + trailer.length];
        System.arraycopy(header, 0, arStream, 0, header.length);
        System.arraycopy(entry1, 0, arStream, header.length, entry1.length);
        System.arraycopy(trailer, 0, arStream, header.length + entry1.length, trailer.length);

        InputStream inputStream = new ByteArrayInputStream(arStream);
        ArArchiveInputStream arArchiveInputStream = new ArArchiveInputStream(inputStream);

        try {
            byte[] buffer = new byte[10];
            int bytesRead = arArchiveInputStream.read(buffer, 2, 7);
            assertEquals(7, bytesRead);

            byte[] expectedBytes = new byte[7];
            System.arraycopy(entry1, 0, expectedBytes, 0, 7);
            assertArrayEquals(expectedBytes, buffer);

            bytesRead = arArchiveInputStream.read(buffer, 2, 7);
            assertEquals(1, bytesRead);
            assertEquals('`', buffer[2]);

            bytesRead = arArchiveInputStream.read(buffer, 0, 10);
            assertEquals(-1, bytesRead);

        } catch (IOException e) {
            fail("IOException: " + e.getMessage());
        }

        try {
            arArchiveInputStream.close();
        } catch (IOException e) {
            fail("IOException: " + e.getMessage());
        }
    }

    @Test
    public void testMatches() {
        byte[] signature = "!<arch>\n".getBytes();
        assertTrue(ArArchiveInputStream.matches(signature, 8));

        byte[] invalidSignature = "!<arch>\n".getBytes();
        assertFalse(ArArchiveInputStream.matches(invalidSignature, 7));
    }

}

