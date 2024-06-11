import static org.junit.Assert.*;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import org.junit.Test;

public class ArArchiveInputStreamTest {

    @Test
    public void testGetNextArEntry() throws IOException {
        byte[] data = new byte[32];
        data[0] = 0x21;
        data[1] = 0x3c;
        data[2] = 0x61;
        data[3] = 0x72;
        data[4] = 0x63;
        data[5] = 0x68;
        data[6] = 0x3e;
        data[7] = 0x0a;
        ByteArrayInputStream input = new ByteArrayInputStream(data);
        ArArchiveInputStream arInputStream = new ArArchiveInputStream(input);
        ArArchiveEntry entry = arInputStream.getNextArEntry();
        assertNotNull(entry);
        assertEquals("", entry.getName());
        assertEquals(0, entry.getLength());
        input.close();
    }

    @Test
    public void testGetNextArEntryWithValidData() throws IOException {
        byte[] data = new byte[64];
        data[0] = 0x21;
        data[1] = 0x3c;
        data[2] = 0x61;
        data[3] = 0x72;
        data[4] = 0x63;
        data[5] = 0x68;
        data[6] = 0x3e;
        data[7] = 0x0a;
        data[8] = 0x74;
        data[9] = 0x65;
        data[10] = 0x73;
        data[11] = 0x74;
        data[12] = 0x20;
        data[13] = 0x64;
        data[14] = 0x61;
        data[15] = 0x74;
        data[16] = 0x61;
        data[17] = 0x20;
        data[18] = 0x31;
        data[19] = 0x32;
        data[20] = 0x33;
        data[21] = 0x34;
        data[22] = 0x35;
        data[23] = 0x36;
        data[24] = 0x37;
        data[25] = 0x38;
        data[26] = 0x39;
        data[27] = 0x30;
        data[28] = 0x0a;
        ByteArrayInputStream input = new ByteArrayInputStream(data);
        ArArchiveInputStream arInputStream = new ArArchiveInputStream(input);
        ArArchiveEntry entry = arInputStream.getNextArEntry();
        assertNotNull(entry);
        assertEquals("test data 1234567890\n", entry.getName());
        assertEquals(10, entry.getLength());
        input.close();
    }

    @Test
    public void testGetNextArEntryWithInvalidData() throws IOException {
        byte[] data = new byte[32];
        data[0] = 0x21;
        data[1] = 0x3c;
        data[2] = 0x61;
        data[3] = 0x72;
        data[4] = 0x63;
        data[5] = 0x68;
        data[6] = 0x3e;
        data[7] = 0x0a;
        data[8] = 0x74;
        data[9] = 0x65;
        data[10] = 0x73;
        data[11] = 0x74;
        data[12] = 0x20;
        data[13] = 0x64;
        data[14] = 0x61;
        data[15] = 0x74;
        ByteArrayInputStream input = new ByteArrayInputStream(data);
        ArArchiveInputStream arInputStream = new ArArchiveInputStream(input);
        try {
            arInputStream.getNextArEntry();
            fail("IOException was expected");
        } catch (IOException e) {
            assertEquals("failed to read entry header", e.getMessage());
        }
        input.close();
    }

    @Test
    public void testGetNextEntry() throws IOException {
        byte[] data = new byte[32];
        data[0] = 0x21;
        data[1] = 0x3c;
        data[2] = 0x61;
        data[3] = 0x72;
        data[4] = 0x63;
        data[5] = 0x68;
        data[6] = 0x3e;
        data[7] = 0x0a;
        ByteArrayInputStream input = new ByteArrayInputStream(data);
        ArArchiveInputStream arInputStream = new ArArchiveInputStream(input);
        ArchiveEntry entry = arInputStream.getNextEntry();
        assertNotNull(entry);
        assertEquals("", entry.getName());
        assertEquals(0, entry.getLength());
        input.close();
    }

    @Test
    public void testClose() throws IOException {
        byte[] data = new byte[32];
        data[0] = 0x21;
        data[1] = 0x3c;
        data[2] = 0x61;
        data[3] = 0x72;
        data[4] = 0x63;
        data[5] = 0x68;
        data[6] = 0x3e;
        data[7] = 0x0a;
        ByteArrayInputStream input = new ByteArrayInputStream(data);
        ArArchiveInputStream arInputStream = new ArArchiveInputStream(input);
        arInputStream.close();
        assertTrue(arInputStream.isClosed());
        input.close();
    }

    @Test
    public void testRead() throws IOException {
        byte[] data = new byte[8];
        data[0] = 0x21;
        data[1] = 0x3c;
        data[2] = 0x61;
        data[3] = 0x72;
        data[4] = 0x63;
        data[5] = 0x68;
        data[6] = 0x3e;
        data[7] = 0x0a;
        ByteArrayInputStream input = new ByteArrayInputStream(data);
        ArArchiveInputStream arInputStream = new ArArchiveInputStream(input);
        for (int i = 0; i < 8; i++) {
            int read = arInputStream.read();
            assertEquals(data[i], (byte) read);
        }
        assertEquals(-1, arInputStream.read());
        input.close();
    }

    @Test
    public void testReadByteArray() throws IOException {
        byte[] data = new byte[8];
        data[0] = 0x21;
        data[1] = 0x3c;
        data[2] = 0x61;
        data[3] = 0x72;
        data[4] = 0x63;
        data[5] = 0x68;
        data[6] = 0x3e;
        data[7] = 0x0a;
        ByteArrayInputStream input = new ByteArrayInputStream(data);
        ArArchiveInputStream arInputStream = new ArArchiveInputStream(input);
        byte[] buffer = new byte[4];
        int read = arInputStream.read(buffer);
        assertArrayEquals(new byte[]{0x21, 0x3c, 0x61, 0x72}, buffer);
        assertEquals(4, read);
        read = arInputStream.read(buffer);
        assertArrayEquals(new byte[]{0x63, 0x68, 0x3e, 0x0a}, buffer);
        assertEquals(4, read);
        read = arInputStream.read(buffer);
        assertEquals(-1, read);
        input.close();
    }

    @Test
    public void testReadByteArrayWithOffset() throws IOException {
        byte[] data = new byte[8];
        data[0] = 0x21;
        data[1] = 0x3c;
        data[2] = 0x61;
        data[3] = 0x72;
        data[4] = 0x63;
        data[5] = 0x68;
        data[6] = 0x3e;
        data[7] = 0x0a;
        ByteArrayInputStream input = new ByteArrayInputStream(data);
        ArArchiveInputStream arInputStream = new ArArchiveInputStream(input);
        byte[] buffer = new byte[4];
        int read = arInputStream.read(buffer, 1, 2);
        assertArrayEquals(new byte[]{0x00, 0x21, 0x00, 0x00}, buffer);
        assertEquals(2, read);
        read = arInputStream.read(buffer, 0, 4);
        assertArrayEquals(new byte[]{0x3c, 0x61, 0x00, 0x00}, buffer);
        assertEquals(2, read);
        read = arInputStream.read(buffer, 1, 2);
        assertArrayEquals(new byte[]{0x3c, 0x63, 0x68, 0x0a}, buffer);
        assertEquals(2, read);
        read = arInputStream.read(buffer, 1, 2);
        assertEquals(-1, read);
        input.close();
    }

    @Test
    public void testMatches() {
        byte[] signature = new byte[8];
        signature[0] = 0x21;
        signature[1] = 0x3c;
        signature[2] = 0x61;
        signature[3] = 0x72;
        signature[4] = 0x63;
        signature[5] = 0x68;
        signature[6] = 0x3e;
        signature[7] = 0x0a;
        assertTrue(ArArchiveInputStream.matches(signature, 8));
        signature[0] = 0x20;
        assertFalse(ArArchiveInputStream.matches(signature, 8));
    }

}