import org.junit.Assert;
import org.junit.Test;
import org.apache.commons.compress.archivers.ArchiveEntry;
import java.io.UnsupportedEncodingException;

public class ArchiveUtilsTest {

    @Test
    public void testToString() {
        ArchiveEntry entry = new ArchiveEntry();
        entry.setName("test");
        entry.setDirectory(false);
        entry.setSize(2000);
        String result = ArchiveUtils.toString(entry);
        Assert.assertEquals("-    2000 test", result);
    }

    @Test
    public void testToStringForDirectory() {
        ArchiveEntry entry = new ArchiveEntry();
        entry.setName("test");
        entry.setDirectory(true);
        entry.setSize(100);
        String result = ArchiveUtils.toString(entry);
        Assert.assertEquals("d     100 test", result);
    }

    @Test
    public void testMatchAsciiBuffer() {
        String expected = "test";
        byte[] buffer = expected.getBytes();
        boolean result = ArchiveUtils.matchAsciiBuffer(expected, buffer);
        Assert.assertTrue(result);
    }

    @Test
    public void testToAsciiBytes() {
        String inputString = "test";
        byte[] result = ArchiveUtils.toAsciiBytes(inputString);
        byte[] expected = inputString.getBytes();
        Assert.assertArrayEquals(expected, result);
    }

    @Test
    public void testToAsciiString() {
        byte[] inputBytes = new byte[] {116, 101, 115, 116};
        String result = ArchiveUtils.toAsciiString(inputBytes);
        String expected = "test";
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testToAsciiStringWithOffsetAndLength() {
        byte[] inputBytes = new byte[] {116, 101, 115, 116};
        int offset = 1;
        int length = 2;
        String result = ArchiveUtils.toAsciiString(inputBytes, offset, length);
        String expected = "es";
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testIsEqual() {
        byte[] buffer1 = new byte[] {116, 101, 115, 116};
        byte[] buffer2 = new byte[] {116, 101, 115, 116};
        boolean result = ArchiveUtils.isEqual(buffer1, buffer2);
        Assert.assertTrue(result);
    }

    @Test
    public void testIsEqualWithNull() {
        byte[] buffer1 = new byte[] {116, 101, 115, 116};
        byte[] buffer2 = new byte[] {116, 101, 115, 116};
        boolean result = ArchiveUtils.isEqualWithNull(buffer1, 0, buffer1.length, buffer2, 0, buffer2.length);
        Assert.assertTrue(result);
    }
    
    @Test
    public void testSanitize() {
        String s = "test";
        String result = ArchiveUtils.sanitize(s);
        Assert.assertEquals(s, result);
    }
}