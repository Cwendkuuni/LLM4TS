import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

public class ArArchiveInputStreamTest {

    private ArArchiveInputStream arInputStream;
    private byte[] arEntryHeader;
    private byte[] arEntryTrailer;

    @Before
    public void setUp() {
        arEntryHeader = "TestArHeader".getBytes();
        arEntryTrailer = "TestArTrailer".getBytes();
        arInputStream = new ArArchiveInputStream(null) {
            private int readCount = 0;
            @Override
            public int read() throws IOException {
                if (readCount == 0) {
                    readCount++;
                    return arEntryHeader[0];
                } else if (readCount == arEntryHeader.length - 1) {
                    readCount++;
                    return arEntryHeader[arEntryHeader.length - 1];
                } else if (readCount == arEntryHeader.length) {
                    readCount++;
                    return arEntryTrailer[0];
                } else if (readCount == arEntryHeader.length + arEntryTrailer.length - 1) {
                    readCount++;
                    return arEntryTrailer[arEntryTrailer.length - 1];
                } else {
                    readCount++;
                    return 'a'; // Arbitrary byte value
                }
            }
            @Override
            public int read(byte[] b, int off, int len) throws IOException {
                for (int i = off; i < off + len; i++) {
                    if (i < arEntryHeader.length) {
                        b[i] = arEntryHeader[i];
                    } else if (i < arEntryHeader.length + arEntryTrailer.length) {
                        b[i] = arEntryTrailer[i - arEntryHeader.length];
                    } else {
                        b[i] = 'a'; // Arbitrary byte value
                    }
                }
                return len;
            }
        };
    }

    @Test
    public void testGetNextArEntryValidHeader() throws IOException {
        ArArchiveEntry entry = arInputStream.getNextArEntry();
        assertNotNull(entry);
        assertEquals("TestArHeaderTest", entry.getName());
        assertEquals(arEntryTrailer.length, entry.getLength());
    }

    @Test
    public void testGetNextArEntryInvalidHeader() throws IOException {
        arEntryHeader[0] = 'X';
        try {
            arInputStream.getNextArEntry();
            fail("IOException not thrown");
        } catch (IOException e) {
            assertEquals("invalid header TestArHeader", e.getMessage());
        }
    }

    @Test
    public void testGetNextArEntryEOF() throws IOException {
        arInputStream.read(); // Simulate reading beyond EOF
        assertNull(arInputStream.getNextArEntry());
    }

    @Test
    public void testGetNextArEntryIncompleteEntry() throws IOException {
        arEntryHeader[0] = 'X';
        try {
            arInputStream.getNextArEntry();
            fail("IOException not thrown");
        } catch (IOException e) {
            assertEquals("failed to read entry header", e.getMessage());
        }
    }

    @Test
    public void testGetNextEntry() throws IOException {
        ArArchiveEntry entry = arInputStream.getNextEntry();
        assertNotNull(entry);
        assertEquals("TestArHeaderTest", entry.getName());
        assertEquals(arEntryTrailer.length, entry.getLength());
    }

    @Test
    public void testClose() throws IOException {
        arInputStream.close();
        assertTrue(arInputStream.closed);
    }

    @Test
    public void testRead() throws IOException {
        int byteRead = arInputStream.read();
        assertEquals(arEntryHeader[0], byteRead);
    }

    @Test
    public void testReadByteArray() throws IOException {
        byte[] byteArray = new byte[arEntryHeader.length];
        int bytesRead = arInputStream.read(byteArray);
        assertEquals(arEntryHeader.length, bytesRead);
        assertArrayEquals(arEntryHeader, byteArray);
    }

    @Test
    public void testReadByteArrayOffset() throws IOException {
        byte[] byteArray = new byte[arEntryHeader.length];
        int bytesRead = arInputStream.read(byteArray, 1, arEntryHeader.length - 2);
        assertEquals(arEntryHeader.length - 2, bytesRead);
        for (int i = 0; i < bytesRead; i++) {
            assertEquals(arEntryHeader[i + 1], byteArray[i + 1]);
        }
    }

    @Test
    public void testReadByteArrayLen() throws IOException {
        byte[] byteArray = new byte[arEntryHeader.length];
        int bytesRead = arInputStream.read(byteArray, 0, arEntryHeader.length);
        assertEquals(arEntryHeader.length, bytesRead);
        assertArrayEquals(arEntryHeader, byteArray);
    }

    @Test
    public void testMatchesValidSignature() {
        byte[] validSignature = {0x21, 0x3c, 0x61, 0x72, 0x63, 0x68, 0x3e, 0x0a};
        assertTrue(ArArchiveInputStream.matches(validSignature, validSignature.length));
    }

    @Test
    public void testMatchesInvalidSignature() {
        byte[] invalidSignature = {0x21, 0x3c, 0x61, 0x72, 0x63, 0x68, 0x3e};
        assertFalse(ArArchiveInputStream.matches(invalidSignature, invalidSignature.length));
    }

}