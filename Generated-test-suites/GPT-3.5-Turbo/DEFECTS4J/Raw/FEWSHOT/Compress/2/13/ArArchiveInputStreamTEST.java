import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.ar.ArArchiveEntry;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import java.io.ByteArrayInputStream;
import java.io.IOException;

public class ArArchiveInputStreamTest {

    private ArArchiveInputStream arInputStream;
    private byte[] arFileContent;

    @Before
    public void setUp() {
        arFileContent = getArFileContent();
        arInputStream = new ArArchiveInputStream(new ByteArrayInputStream(arFileContent));
    }

    @Test
    public void testGetNextArEntry() throws IOException {
        ArArchiveEntry entry1 = arInputStream.getNextArEntry();
        assertEquals("file1.txt", entry1.getName());
        assertEquals(10, entry1.getSize());

        ArArchiveEntry entry2 = arInputStream.getNextArEntry();
        assertEquals("file2.txt", entry2.getName());
        assertEquals(20, entry2.getSize());

        ArArchiveEntry entry3 = arInputStream.getNextArEntry();
        assertNull(entry3);
    }

    @Test
    public void testGetNextEntry() throws IOException {
        ArchiveEntry entry1 = arInputStream.getNextEntry();
        assertNotNull(entry1);
        assertEquals("file1.txt", entry1.getName());
        assertEquals(10, entry1.getSize());

        ArchiveEntry entry2 = arInputStream.getNextEntry();
        assertNotNull(entry2);
        assertEquals("file2.txt", entry2.getName());
        assertEquals(20, entry2.getSize());

        ArchiveEntry entry3 = arInputStream.getNextEntry();
        assertNull(entry3);
    }

    @Test
    public void testClose() throws IOException {
        assertFalse(arInputStream.isClosed());
        arInputStream.close();
        assertTrue(arInputStream.isClosed());
    }

    private byte[] getArFileContent() {
        // Create an example AR file content
        byte[] content = new byte[64];
        content[0] = 0x21;
        content[1] = 0x3c;
        content[2] = 0x61;
        content[3] = 0x72;
        content[4] = 0x63;
        content[5] = 0x68;
        content[6] = 0x3e;
        content[7] = 0x0a;

        // Create first entry
        content[8] = 'f';
        content[9] = 'i';
        content[10] = 'l';
        content[11] = 'e';
        content[12] = '1';
        content[13] = '.';
        content[14] = 't';
        content[15] = 'x';
        content[16] = 't';
        content[17] = ' ';
        content[18] = ' ';
        content[19] = (byte) 0x00;
        content[20] = (byte) 0x00;
        content[21] = (byte) 0x00;
        content[22] = (byte) 0x00;
        content[23] = (byte) 0x00;
        content[24] = (byte) 0x00;
        content[25] = (byte) 0x00;
        content[26] = (byte) 0x0a;

        // Create second entry
        content[27] = 'f';
        content[28] = 'i';
        content[29] = 'l';
        content[30] = 'e';
        content[31] = '2';
        content[32] = '.';
        content[33] = 't';
        content[34] = 'x';
        content[35] = 't';
        content[36] = ' ';
        content[37] = ' ';
        content[38] = (byte) 0x00;
        content[39] = (byte) 0x00;
        content[40] = (byte) 0x00;
        content[41] = (byte) 0x01;
        content[42] = (byte) 0x00;
        content[43] = (byte) 0x00;
        content[44] = (byte) 0x00;
        content[45] = (byte) 0x0a;

        // Add AR entry trailer
        content[46] = '=';
        content[47] = '\n';
        content[48] = '\n';
        content[49] = '\n';
        content[50] = '\n';
        content[51] = '\n';
        content[52] = '\n';
        content[53] = '\n';
        content[54] = '=';
        content[55] = '=';
        content[56] = '=';
        content[57] = '=';
        content[58] = '=';
        content[59] = '=';
        content[60] = '=';
        content[61] = '=';
        content[62] = '\n';
        content[63] = '\n';

        return content;
    }
}