import org.apache.commons.compress.archivers.tar.TarUtils;
import org.junit.Test;
import static org.junit.Assert.*;

public class TarUtilsTest {

    @Test
    public void testParseOctal() {
        byte[] buffer = new byte[]{(byte) '0', '7', '5', '5', '0'};
        long result = TarUtils.parseOctal(buffer, 0, buffer.length);
        assertEquals(017550L, result);

        byte[] invalidBuffer = new byte[]{(byte) '0', '8'}; // Invalid octal digit
        try {
            TarUtils.parseOctal(invalidBuffer, 0, invalidBuffer.length);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testParseName() {
        byte[] buffer = new byte[]{(byte) 'm', 'y', '.', 'j', 'a', 'r', '.', 't', 'a', 'r', 0};
        String result = TarUtils.parseName(buffer, 0, buffer.length);
        assertEquals("my.jar.tar", result);

        byte[] truncatedBuffer = new byte[]{(byte) 'm', 'y', 0};
        String truncatedResult = TarUtils.parseName(truncatedBuffer, 0, truncatedBuffer.length);
        assertEquals("my", truncatedResult);
    }

    @Test
    public void testFormatNameBytes() {
        String name = "my.jar.tar";
        byte[] buffer = new byte[12];
        int offset = TarUtils.formatNameBytes(name, buffer, 0, buffer.length);
        assertArrayEquals(new byte[]{'m', 'y', '.', 'j', 'a', 'r', '.', 't', 'a', 'r', 0, 0, 0, 0, 0, 0, 0}, buffer);
        assertEquals(12, offset);
    }

    @Test
    public void testFormatUnsignedOctalString() {
        long value = 12345;
        byte[] buffer = new byte[8];
        TarUtils.formatUnsignedOctalString(value, buffer, 0, buffer.length);
        assertArrayEquals(new byte[]{'0', '1', '7', '7', '5', '5', '5', '5'}, buffer);
    }

    @Test
    public void testFormatOctalBytes() {
        long value = 12345;
        byte[] buffer = new byte[10];
        int offset = TarUtils.formatOctalBytes(value, buffer, 0, buffer.length);
        assertArrayEquals(new byte[]{'0', '1', '7', '7', '5', '5', '5', '5', ' ', '0'}, buffer);
        assertEquals(10, offset);
    }

    @Test
    public void testComputeCheckSum() {
        byte[] buf = new byte[]{(byte) 0x75, (byte) 0x72, (byte) 0x6e, (byte) 0x63, (byte) 0x68, (byte) 0x64, (byte) 0x73, 0};
        long checksum = TarUtils.computeChecksum(buf);
        assertEquals(0352664277550324405L, checksum);
    }
}