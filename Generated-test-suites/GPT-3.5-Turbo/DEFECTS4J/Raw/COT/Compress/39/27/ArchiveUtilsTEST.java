import org.junit.Test;
import org.junit.Assert;
import java.nio.charset.StandardCharsets;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import org.apache.commons.compress.archivers.ArchiveEntry;

public class ArchiveUtilsTest {

    @Test
    public void testToString(){
        ArchiveEntry entry = new ArchiveEntry(); // Need to create an instance of ArchiveEntry
        entry.setDirectory(true);
        entry.setSize(100);
        entry.setName("testfiles");
        String expected = "d     100 testfiles";
        String actual = ArchiveUtils.toString(entry);
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testMatchAsciiBuffer(){
        String expected = "test";
        byte[] buffer = expected.getBytes(StandardCharsets.US_ASCII);
        boolean actual = ArchiveUtils.matchAsciiBuffer(expected, buffer);
        Assert.assertTrue(actual);
    }

    @Test
    public void testToAsciiBytes(){
        String inputString = "test";
        byte[] expected = inputString.getBytes(StandardCharsets.US_ASCII);
        byte[] actual = ArchiveUtils.toAsciiBytes(inputString);
        Assert.assertArrayEquals(expected, actual);
    }

    @Test
    public void testToAsciiString(){
        byte[] inputBytes = "test".getBytes(StandardCharsets.US_ASCII);
        String expected = "test";
        String actual = ArchiveUtils.toAsciiString(inputBytes);
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testToAsciiString(){
        byte[] inputBytes = "test".getBytes(StandardCharsets.US_ASCII);
        int offset = 0;
        int length = inputBytes.length;
        String expected = "test";
        String actual = ArchiveUtils.toAsciiString(inputBytes, offset, length);
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testIsEqual(){
        byte[] buffer1 = "test".getBytes(StandardCharsets.US_ASCII);
        int offset1 = 0;
        int length1 = buffer1.length;
        byte[] buffer2 = "test".getBytes(StandardCharsets.US_ASCII);
        int offset2 = 0;
        int length2 = buffer2.length;
        boolean expected = true;
        boolean actual = ArchiveUtils.isEqual(buffer1, offset1, length1, buffer2, offset2, length2);
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testIsEqual(){
        byte[] buffer1 = "test".getBytes(StandardCharsets.US_ASCII);
        byte[] buffer2 = "test".getBytes(StandardCharsets.US_ASCII);
        boolean expected = true;
        boolean actual = ArchiveUtils.isEqual(buffer1, buffer2);
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testIsEqualWithNull(){
        byte[] buffer1 = "test".getBytes(StandardCharsets.US_ASCII);
        int offset1 = 0;
        int length1 = buffer1.length;
        byte[] buffer2 = "test".getBytes(StandardCharsets.US_ASCII);
        int offset2 = 0;
        int length2 = buffer2.length;
        boolean expected = true;
        boolean actual = ArchiveUtils.isEqualWithNull(buffer1, offset1, length1, buffer2, offset2, length2);
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testIsArrayZero(){
        byte[] a = new byte[10];
        int size = 10;
        boolean expected = true;
        boolean actual = ArchiveUtils.isArrayZero(a, size);
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testSanitize() {
        String s = "test";
        String expected = "test";
        String actual = ArchiveUtils.sanitize(s);
        Assert.assertEquals(expected, actual);
    }

}