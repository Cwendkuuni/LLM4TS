import org.apache.commons.compress.archivers.ArchiveEntry;
import org.junit.Assert;
import org.junit.Test;

public class ArchiveUtilsTest {

    

    @Test
    public void testToStringMethod() {
        ArchiveEntry archiveEntry = new ArchiveEntry("main.c");
        ArchiveUtils au = new ArchiveUtils();
        String actual = au.toString(archiveEntry);
        String expected = "- 2000 main.c";
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testToStringMethodWithDirectory() {
        ArchiveEntry archiveEntry = new ArchiveEntry("testfiles");
        ArchiveUtils au = new ArchiveUtils();
        String actual = au.toString(archiveEntry);
        String expected = "d 100 testfiles";
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testMatchAsciiBufferMethod() {
        String expected = "test";
        byte[] buffer = new byte[4];
        Arrays.fill(buffer, (byte) 't');
        boolean actual = ArchiveUtils.matchAsciiBuffer(expected, buffer, 0, buffer.length);
        Assert.assertTrue(actual);
    }

    @Test
    public void testMatchAsciiBufferMethodWithMismatch() {
        String expected = "test";
        byte[] buffer = new byte[4];
        Arrays.fill(buffer, (byte) 'f');
        boolean actual = ArchiveUtils.matchAsciiBuffer(expected, buffer, 0, buffer.length);
        Assert.assertFalse(actual);
    }

    @Test(expected = UnsupportedEncodingException.class)
    public void testToAsciiBytesWithInvalidEncoding() {
        String inputString = "test";
        ArchiveUtils.toAsciiBytes(inputString); // UnsupportedEncodingException should be thrown
    }

    @Test
    public void testToAsciiStringMethod() {
        byte[] inputBytes = new byte[]{0x65, 0x6e, 0x63, 0x6f, 0x6d};
        String actual = ArchiveUtils.toAsciiString(inputBytes);
        String expected = "test";
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testIsEqualMethod() {
        byte[] buffer1 = new byte[]{(byte) 0x65, (byte) 0x6e, (byte) 0x63, (byte) 0x6f, (byte) 0x6d};
        byte[] buffer2 = new byte[]{(byte) 0x65, (byte) 0x6e, (byte) 0x63, (byte) 0x6f, (byte) 0x6d};
        boolean actual = ArchiveUtils.isEqual(buffer1, buffer2);
        Assert.assertTrue(actual);
    }

    @Test
    public void testIsEqualMethodWithMismatch() {
        byte[] buffer1 = new byte[]{(byte) 0x65, (byte) 0x6e, (byte) 0x63, (byte) 0x6f, (byte) 0x6d};
        byte[] buffer2 = new byte[]{(byte) 0x65, (byte) 0x6e, (byte) 0x63, (byte) 0x6f, (byte) 0x6e};
        boolean actual = ArchiveUtils.isEqual(buffer1, buffer2);
        Assert.assertFalse(actual);
    }

    @Test
    public void testIsEqualWithNullMethod() {
        byte[] buffer1 = new byte[]{0x65, 0x6e, 0x63, 0x6f, 0x6d, 0x0};
        byte[] buffer2 = new byte[]{0x65, 0x6e, 0x63, 0x6f, 0x6d, 0x0};
        boolean actual = ArchiveUtils.isEqualWithNull(buffer1, buffer2);
        Assert.assertTrue(actual);
    }

    @Test
    public void testIsEqualWithNullMethodWithNoTrailingNulls() {
        byte[] buffer1 = new byte[]{0x65, 0x6e, 0x63, 0x6f, 0x6d};
        byte[] buffer2 = new byte[]{0x65, 0x6e, 0x63, 0x6f, 0x6d, 0x0};
        boolean actual = ArchiveUtils.isEqualWithNull(buffer1, buffer2);
        Assert.assertFalse(actual);
    }

    @Test
    public void testIsArrayZeroMethod() {
        byte[] a = new byte[5];
        boolean actual = ArchiveUtils.isArrayZero(a, 5);
        Assert.assertFalse(actual);
    }

    @Test
    public void testIsArrayZeroMethodWithZeroArray() {
        byte[] a = new byte[0];
        boolean actual = ArchiveUtils.isArrayZero(a, 0);
        Assert.assertTrue(actual);
    }

    @Test
    public void testSanitizeMethod() {
        String s = "test\u0001\u0002\u001FarchivedFile.zip";
        String actual = ArchiveUtils.sanitize(s);
        String expected = "test?????archivedFile.zip";
        Assert.assertEquals(expected, actual);
    }

    

}