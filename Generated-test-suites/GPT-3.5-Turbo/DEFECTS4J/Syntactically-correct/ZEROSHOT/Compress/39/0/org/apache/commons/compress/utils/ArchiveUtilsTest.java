import org.junit.Test;
import static org.junit.Assert.*;

public class ArchiveUtilsTest {

    @Test
    public void testToString() {
        ArchiveEntry entry = new ArchiveEntry();
        entry.setDirectory(false);
        entry.setSize(2000);
        entry.setName("main.c");

        String expected = "-    2000 main.c";
        String result = ArchiveUtils.toString(entry);

        assertEquals(expected, result);
    }

    @Test
    public void testMatchAsciiBuffer() {
        byte[] buffer = new byte[10];
        String expected = "test";
        buffer[0] = (byte) 't';
        buffer[1] = (byte) 'e';
        buffer[2] = (byte) 's';
        buffer[3] = (byte) 't';

        boolean result = ArchiveUtils.matchAsciiBuffer(expected, buffer);

        assertTrue(result);
    }

    @Test
    public void testToAsciiBytes() {
        String inputString = "test";
        byte[] expected = {(byte) 't', (byte) 'e', (byte) 's', (byte) 't'};

        byte[] result = ArchiveUtils.toAsciiBytes(inputString);

        assertArrayEquals(expected, result);
    }

    @Test
    public void testToAsciiString() {
        byte[] inputBytes = {(byte) 't', (byte) 'e', (byte) 's', (byte) 't'};
        String expected = "test";

        String result = ArchiveUtils.toAsciiString(inputBytes);

        assertEquals(expected, result);
    }

    @Test
    public void testIsEqual() {
        byte[] buffer1 = {(byte) 't', (byte) 'e', (byte) 's', (byte) 't'};
        byte[] buffer2 = {(byte) 't', (byte) 'e', (byte) 's', (byte) 't'};

        boolean result = ArchiveUtils.isEqual(buffer1, buffer2);

        assertTrue(result);
    }

    @Test
    public void testSanitize() {
        String inputString = "test\0file";
        String expected = "test?file";

        String result = ArchiveUtils.sanitize(inputString);

        assertEquals(expected, result);
    }

}