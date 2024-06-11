import org.junit.Assert;
import org.junit.Test;

public class ArchiveUtilsTest {

    @Test
    public void testToString() {
        ArchiveEntry entry1 = new ArchiveEntry("file1", false, 100);
        ArchiveEntry entry2 = new ArchiveEntry("dir1", true, 2000);
        
        String expected1 = "-     100 file1";
        String expected2 = "d    2000 dir1";
        
        String result1 = ArchiveUtils.toString(entry1);
        String result2 = ArchiveUtils.toString(entry2);
        
        Assert.assertEquals(expected1, result1);
        Assert.assertEquals(expected2, result2);
    }

    @Test
    public void testMatchAsciiBuffer_withOffsetAndLength() {
        String expected = "test";
        byte[] buffer = "test".getBytes();
        
        int offset = 0;
        int length = 4;
        
        boolean result = ArchiveUtils.matchAsciiBuffer(expected, buffer, offset, length);
        
        Assert.assertTrue(result);
    }

    @Test
    public void testMatchAsciiBuffer_withoutOffsetAndLength() {
        String expected = "test";
        byte[] buffer = "test".getBytes();
        
        boolean result = ArchiveUtils.matchAsciiBuffer(expected, buffer);
        
        Assert.assertTrue(result);
    }

    @Test
    public void testToAsciiBytes() {
        String inputString = "test";
        
        byte[] expected = "test".getBytes();
        
        byte[] result = ArchiveUtils.toAsciiBytes(inputString);
        
        Assert.assertArrayEquals(expected, result);
    }

    @Test
    public void testToAsciiString_withOffsetAndLength() {
        byte[] inputBytes = "test".getBytes();
        
        int offset = 0;
        int length = 4;
        
        String expected = "test";
        
        String result = ArchiveUtils.toAsciiString(inputBytes, offset, length);
        
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testToAsciiString_withoutOffsetAndLength() {
        byte[] inputBytes = "test".getBytes();
        
        String expected = "test";
        
        String result = ArchiveUtils.toAsciiString(inputBytes);
        
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testIsEqual_withOffsetAndLength() {
        byte[] buffer1 = "test1".getBytes();
        byte[] buffer2 = "test2".getBytes();
        
        int offset1 = 0;
        int length1 = 4;
        
        int offset2 = 0;
        int length2 = 5;
        
        boolean result = ArchiveUtils.isEqual(buffer1, offset1, length1, buffer2, offset2, length2);
        
        Assert.assertFalse(result);
    }

    @Test
    public void testIsEqual_withoutOffsetAndLength() {
        byte[] buffer1 = "test".getBytes();
        byte[] buffer2 = "test".getBytes();
        
        boolean result = ArchiveUtils.isEqual(buffer1, buffer2);
        
        Assert.assertTrue(result);
    }
    
    @Test
    public void testIsEqual_ignoreTrailingNulls() {
        byte[] buffer1 = "test\0\0".getBytes();
        byte[] buffer2 = "test\0".getBytes();
        
        boolean result = ArchiveUtils.isEqual(buffer1, buffer2, true);
        
        Assert.assertTrue(result);
    }

    @Test
    public void testIsArrayZero() {
        byte[] array = {0, 0, 0};
        int size = 3;
        
        boolean result = ArchiveUtils.isArrayZero(array, size);
        
        Assert.assertTrue(result);
    }

    @Test
    public void testSanitize() {
        String s = "\u0400test\u0400";
        
        String expected = "?test?";
        
        String result = ArchiveUtils.sanitize(s);
        
        Assert.assertEquals(expected, result);
    }
}