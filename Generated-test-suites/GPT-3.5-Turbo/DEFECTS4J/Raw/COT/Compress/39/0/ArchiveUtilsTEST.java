
import org.junit.Assert;
import org.junit.Test;

public class ArchiveUtilsTest {

    @Test
    public void testToString() {
        ArchiveEntry entry = new ArchiveEntry();
        entry.setDirectory(false);
        entry.setSize(2000);
        entry.setName("main.c");
        String expected = "-    2000 main.c";
        Assert.assertEquals(expected, ArchiveUtils.toString(entry));

        entry.setDirectory(true);
        entry.setSize(100);
        entry.setName("testfiles");
        expected = "d     100 testfiles";
        Assert.assertEquals(expected, ArchiveUtils.toString(entry));
    }

    @Test
    public void testMatchAsciiBuffer() {
        String expected = "Hello";
        byte[] buffer = expected.getBytes();
        Assert.assertTrue(ArchiveUtils.matchAsciiBuffer(expected, buffer));

        expected = "World";
        buffer = expected.getBytes();
        Assert.assertFalse(ArchiveUtils.matchAsciiBuffer("Hello", buffer));
    }

    @Test
    public void testToAsciiBytes() {
        String inputString = "Hello World";
        byte[] expected = inputString.getBytes();
        Assert.assertArrayEquals(expected, ArchiveUtils.toAsciiBytes(inputString));
    }

    @Test
    public void testToAsciiString() {
        byte[] inputBytes = "Hello World".getBytes();
        String expected = new String(inputBytes);
        Assert.assertEquals(expected, ArchiveUtils.toAsciiString(inputBytes));
    }

    @Test
    public void testToAsciiStringWithOffsetAndLength() {
        byte[] inputBytes = "Hello World".getBytes();
        String expected = "Hello";
        Assert.assertEquals(expected, ArchiveUtils.toAsciiString(inputBytes, 0, 5));
    }

    @Test
    public void testIsEqual() {
        byte[] buffer1 = "Hello".getBytes();
        byte[] buffer2 = "Hello".getBytes();
        Assert.assertTrue(ArchiveUtils.isEqual(buffer1, buffer2));

        buffer1 = "Hello".getBytes();
        buffer2 = "World".getBytes();
        Assert.assertFalse(ArchiveUtils.isEqual(buffer1, buffer2));
    }

    @Test
    public void testIsEqualWithOffsetAndLength() {
        byte[] buffer1 = "Hello World".getBytes();
        byte[] buffer2 = "Hello".getBytes();
        Assert.assertTrue(ArchiveUtils.isEqual(buffer1, 0, 5, buffer2, 0, 5));

        buffer1 = "Hello World".getBytes();
        buffer2 = "World".getBytes();
        Assert.assertFalse(ArchiveUtils.isEqual(buffer1, 6, 5, buffer2, 0, 5));
    }

    @Test
    public void testIsEqualIgnoreTrailingNulls() {
        byte[] buffer1 = "Hello World".getBytes();
        byte[] buffer2 = "Hello".getBytes();
        Assert.assertTrue(ArchiveUtils.isEqual(buffer1, 0, 5, buffer2, 0, 5, true));

        buffer1 = "Hello World".getBytes();
        buffer2 = "Hello".getBytes();
        Assert.assertFalse(ArchiveUtils.isEqual(buffer1, 0, 5, buffer2, 0, 5, false));

        buffer1 = "Hello".getBytes();
        buffer2 = "Hello".getBytes();
        Assert.assertTrue(ArchiveUtils.isEqual(buffer1, 0, 5, buffer2, 0, 5, true));
        
        buffer1 = "Hello".getBytes();
        buffer2 = "Hello World".getBytes();
        Assert.assertFalse(ArchiveUtils.isEqual(buffer1, 0, 5, buffer2, 0, 5, true));
    }

    @Test
    public void testEqual() {
        byte[] buffer1 = "Hello".getBytes();
        byte[] buffer2 = "Hello".getBytes();
        Assert.assertTrue(ArchiveUtils.isEqual(buffer1, buffer2));

        buffer1 = "Hello".getBytes();
        buffer2 = "World".getBytes();
        Assert.assertFalse(ArchiveUtils.isEqual(buffer1, buffer2));
    }

    @Test
    public void testIsArrayZero() {
        byte[] a = new byte[5];
        Assert.assertTrue(ArchiveUtils.isArrayZero(a, 5));

        a = "Hello".getBytes();
        Assert.assertFalse(ArchiveUtils.isArrayZero(a, 5));
    }

    @Test
    public void testSanitize() {
        String inputString = "Hello World";
        String expected = "Hello World";
        Assert.assertEquals(expected, ArchiveUtils.sanitize(inputString));

        inputString = "\u0000Hello\u001F World";
        expected = "?Hello? World";
        Assert.assertEquals(expected, ArchiveUtils.sanitize(inputString));
    }

}
