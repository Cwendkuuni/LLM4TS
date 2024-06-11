import static org.junit.Assert.*;
import org.junit.Test;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import org.apache.commons.compress.archivers.ar.ArArchiveInputStream;
import org.apache.commons.compress.archivers.ar.ArArchiveEntry;

public class ArArchiveInputStreamTest {

    @Test
    public void testGetNextArEntry() throws IOException {
        String testEntryHeader = "!<arch>\n";
        String testEntryName = "testfile.txt";
        long testEntryLength = 10;

        String testInput = testEntryHeader +
                          testEntryName + "           " +
                          "1234567890" + "            " +
                          "123456" + "            " +
                          "123456" + "          " +
                          "123456" + "     " +
                          "123456" + "\n" + 
                          "/";

        ByteArrayInputStream bais = new ByteArrayInputStream(testInput.getBytes());
        ArArchiveInputStream arInputStream = new ArArchiveInputStream(bais);
        ArArchiveEntry entry = arInputStream.getNextArEntry();

        assertEquals(testEntryName, entry.getName());
        assertEquals(testEntryLength, entry.getSize());
    }

    @Test
    public void testGetNextEntry() throws IOException {
        String testEntryHeader = "!<arch>\n";
        String testEntryName = "testfile.txt";
        long testEntryLength = 10;

        String testInput = testEntryHeader +
                          testEntryName + "           " +
                          "1234567890" + "            " +
                          "123456" + "            " +
                          "123456" + "          " +
                          "123456" + "     " +
                          "123456" + "\n" + 
                          "/";

        ByteArrayInputStream bais = new ByteArrayInputStream(testInput.getBytes());
        ArArchiveInputStream arInputStream = new ArArchiveInputStream(bais);
        ArArchiveEntry entry = arInputStream.getNextEntry();

        assertEquals(testEntryName, entry.getName());
        assertEquals(testEntryLength, entry.getSize());
    }

    @Test
    public void testRead() throws IOException {
        String testData = "Hello, World!";
        ByteArrayInputStream bais = new ByteArrayInputStream(testData.getBytes());
        ArArchiveInputStream arInputStream = new ArArchiveInputStream(bais);

        int readByte = arInputStream.read();
        assertEquals('H', readByte);

        byte[] buffer = new byte[5];
        assertEquals(5, arInputStream.read(buffer));
        assertEquals("ello,", new String(buffer));

        byte[] buffer2 = new byte[20];
        assertEquals(7, arInputStream.read(buffer2, 0, 7));
        assertEquals(" World!", new String(buffer2, 0, 7));
    }

    @Test
    public void testMatches() {
        byte[] signature1 = {0x21, 0x3c, 0x61, 0x72, 0x63, 0x68, 0x3e, 0x0a};
        assertTrue(ArArchiveInputStream.matches(signature1, 8));

        byte[] signature2 = {0x21, 0x3c, 0x61, 0x72, 0x63, 0x68, 0x3e};
        assertFalse(ArArchiveInputStream.matches(signature2, 7));

        byte[] signature3 = {0x21, 0x3c, 0x61, 0x72};
        assertFalse(ArArchiveInputStream.matches(signature3, 4));
    }

    @Test(expected = IOException.class)
    public void testIOException() throws IOException {
        String testInput = "!<arch>\n";
        ByteArrayInputStream bais = new ByteArrayInputStream(testInput.getBytes());
        ArArchiveInputStream arInputStream = new ArArchiveInputStream(bais);
        arInputStream.getNextArEntry();
    }

}