import org.apache.commons.compress.archivers.ArchiveEntry;
import org.junit.Assert;
import org.junit.Test;

import static org.apache.commons.compress.utils.ArchiveUtils.*;

/**
 * Test class for ArchiveUtils
 */
public class ArchiveUtilsTest {

    @Test
    public void testToString() {
        ArchiveEntry entry = new ArchiveEntry("main.c");
        entry.setSize(2000);
        entry.setDirectory(false);

        String result = toString(entry);
        String expected = "- 2000 main.c";
        Assert.assertEquals(expected, result);

        ArchiveEntry directoryEntry = new ArchiveEntry("testfiles");
        directoryEntry.setSize(100);
        directoryEntry.setDirectory(true);

        result = toString(directoryEntry);
        expected = "d 100 testfiles";
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testMatchAsciiBuffer() {
        String expected = "Hello, World!";
        byte[] buffer = toAsciiBytes(expected);

        boolean result = matchAsciiBuffer(expected, buffer);
        Assert.assertTrue(result);

        result = matchAsciiBuffer(expected, new byte[0]);
        Assert.assertFalse(result);

        result = matchAsciiBuffer("Hello, World!", buffer);
        Assert.assertTrue(result);

        result = matchAsciiBuffer("Hello, World!", new byte[0]);
        Assert.assertFalse(result);
    }

    @Test(expected = RuntimeException.class)
    public void testMatchAsciiBufferWithUnsupportedEncodingException() {
        String expected = "Hello, World!";
        byte[] buffer = new byte[10];

        matchAsciiBuffer(expected, buffer, 0, buffer.length);
    }

    @Test
    public void testToAsciiString() {
        String input = "Hélô, Monde!";
        byte[] inputBytes = toAsciiBytes(input);

        String result = toAsciiString(inputBytes);
        String expected = "Helo, Monde!";
        Assert.assertEquals(expected, result);

        result = toAsciiString(new byte[0]);
        Assert.assertEquals("", result);
    }

    @Test(expected = RuntimeException.class)
    public void testToAsciiStringWithUnsupportedEncodingException() {
        byte[] inputBytes = new byte[10];
        toAsciiString(inputBytes);
    }

    @Test
    public void testIsEqual() {
        byte[] buffer1 = new byte[] {0x48, 0x65, 0x6c, 0x6c, 0x6f};
        byte[] buffer2 = new byte[] {0x48, 0x65, 0x6c, 0x6c, 0x6f};

        boolean result = isEqual(buffer1, 0, buffer1.length, buffer2, 0, buffer2.length, false);
        Assert.assertTrue(result);

        result = isEqual(buffer1, 0, buffer1.length, buffer2, 1, buffer2.length, false);
        Assert.assertFalse(result);

        result = isEqual(buffer1, 0, buffer1.length, buffer2, 0, buffer2.length, true);
        Assert.assertTrue(result);

        result = isEqual(buffer1, 0, buffer1.length, buffer2, 0, buffer2.length - 1, false);
        Assert.assertTrue(result);

        byte[] buffer3 = new byte[] {0x48, 0x65, 0x6c, 0x6c, (byte) 0x7f};
        result = isEqual(buffer1, 0, buffer1.length, buffer3, 0, buffer3.length, false);
        Assert.assertFalse(result);

        result = isEqual(buffer1, 0, buffer1.length, buffer3, 0, buffer3.length, true);
        Assert.assertTrue(result);

        byte[] buffer4 = new byte[] {0x48, 0x65, 0x6c, 0x6c, 0x6f, 0x00};
        result = isEqual(buffer1, 0, buffer1.length, buffer4, 0, buffer4.length, false);
        Assert.assertFalse(result);

        result = isEqual(buffer1, 0, buffer1.length, buffer4, 0, buffer4.length, true);
        Assert.assertTrue(result);
    }

    @Test
    public void testIsEqualNull() {
        byte[] buffer1 = new byte[] {0x48, 0x65, 0x6c, 0x6c, 0x6f};
        byte[] buffer2 = new byte[] {0x48, 0x65, 0x6c, 0x6c, 0x6f};

        boolean result = isEqualWithNull(buffer1, 0, buffer1.length, buffer2, 0, buffer2.length);
        Assert.assertTrue(result);

        result = isEqualWithNull(buffer1, 0, buffer1.length, buffer2, 1, buffer2.length);
        Assert.assertFalse(result);

        byte[] buffer3 = new byte[] {0x48, 0x65, 0x6c, 0x6c, (byte) 0x7f};
        result = isEqualWithNull(buffer1, 0, buffer1.length, buffer3, 0, buffer3.length);
        Assert.assertTrue(result);

        byte[] buffer4 = new byte[] {0x48, 0x65, 0x6c, 0x6c, 0x6f, 0x00};
        result = isEqualWithNull(buffer1, 0, buffer1.length, buffer4, 0, buffer4.length);
        Assert.assertTrue(result);

        result = isEqualWithNull(buffer1, 0, buffer1.length, buffer4, 1, buffer4.length);
        Assert.assertFalse(result);
    }

    @Test
    public void testIsArrayZero() {
        byte[] array = new byte[10];
        boolean result = isArrayZero(array, 5);
        Assert.assertFalse(result);

        result = isArrayZero(array, 10);
        Assert.assertTrue(result);
    }

    @Test
    public void testSanitize() {
        String input = "Hélô, Monde!";
        String result = sanitize(input);
        String expected = "Helo, Monde?!";
        Assert.assertEquals(expected, result);

        input = "Hello, 世界!";
        result = sanitize(input);
        expected = "Hello, ？？？！";
        Assert.assertEquals(expected, result);

        input = "Hello, World!";
        result = sanitize(input);
        expected = "Hello, World!";
        Assert.assertEquals(expected, result);

        input = "";
        result = sanitize(input);
        expected = "";
        Assert.assertEquals(expected, result);
    }
}