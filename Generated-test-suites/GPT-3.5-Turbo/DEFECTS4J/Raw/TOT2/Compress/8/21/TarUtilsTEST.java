import static org.junit.Assert.*;
import org.junit.Test;

public class TarUtilsTest {

    @Test
    public void testParseOctal() {
        // Typical use case
        byte[] buffer = "123".getBytes();
        assertEquals(83, TarUtils.parseOctal(buffer, 0, buffer.length));
        
        // Edge case: buffer is all NULs
        byte[] nullBuffer = "\0\0\0".getBytes();
        assertEquals(0, TarUtils.parseOctal(nullBuffer, 0, nullBuffer.length));
        
        // Error scenario: invalid byte
        byte[] errorBuffer = "12A3".getBytes();
        try {
            TarUtils.parseOctal(errorBuffer, 0, errorBuffer.length);
            fail("Should have thrown an IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testParseName() {
        // Typical use case
        byte[] buffer = "file.txt\0\0\0".getBytes();
        assertEquals("file.txt", TarUtils.parseName(buffer, 0, buffer.length));
        
        // Edge case: buffer length exceeded
        byte[] longBuffer = "file.txt\0\0\0".getBytes();
        assertEquals("file.txt", TarUtils.parseName(longBuffer, 0, 8));
    }

    @Test
    public void testFormatNameBytes() {
        // Typical use case
        String name = "file.txt";
        byte[] buffer = new byte[10];
        TarUtils.formatNameBytes(name, buffer, 0, 8);
        assertEquals("file.txt\0\0\0", new String(buffer));
        
        // Edge case: name longer than buffer
        String longName = "longfilename.txt";
        TarUtils.formatNameBytes(longName, buffer, 0, 8);
        assertEquals("longfile\0", new String(buffer));
    }

    @Test
    public void testComputeCheckSum() {
        // Typical use case
        byte[] buffer = "Hello".getBytes();
        assertEquals(500, TarUtils.computeCheckSum(buffer));
        
        // Edge case: buffer is empty
        byte[] emptyBuffer = "".getBytes();
        assertEquals(0, TarUtils.computeCheckSum(emptyBuffer));
    }
}