import org.junit.Assert;
import org.junit.Test;

public class Utf8Test {

    @Test
    public void testEncodedLength_WithEmptySequence_ReturnsZero() {
        CharSequence sequence = "";
        int expected = 0;
        int actual = Utf8.encodedLength(sequence);
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testEncodedLength_WithAsciiCharacters_ReturnsUtf8Length() {
        CharSequence sequence = "abcdef";
        int expected = 6;
        int actual = Utf8.encodedLength(sequence);
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testEncodedLength_WithNonAsciiCharacters_ReturnsUtf8Length() {
        CharSequence sequence = "abcüçğı";
        int expected = 10;
        int actual = Utf8.encodedLength(sequence);
        Assert.assertEquals(expected, actual);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testEncodedLength_WithUtf8LengthExceedingIntRange_ThrowsIllegalArgumentException() {
        CharSequence sequence = "a" + new String(new char[Integer.MAX_VALUE]);
        Utf8.encodedLength(sequence);
    }

    @Test
    public void testIsWellFormed_WithEmptyBytes_ReturnsTrue() {
        byte[] bytes = {};
        boolean expected = true;
        boolean actual = Utf8.isWellFormed(bytes);
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testIsWellFormed_WithWellFormedBytes_ReturnsTrue() {
        byte[] bytes = {(byte) 0xEF, (byte) 0xBB, (byte) 0xBF}; // UTF-8 BOM
        boolean expected = true;
        boolean actual = Utf8.isWellFormed(bytes);
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testIsWellFormed_WithMalformedBytes_ReturnsFalse() {
        byte[] bytes = {(byte) 0xC3, (byte) 0x28}; // Invalid UTF-8 sequence
        boolean expected = false;
        boolean actual = Utf8.isWellFormed(bytes);
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testIsWellFormed_WithPartialBytes_ReturnsFalse() {
        byte[] bytes = {(byte) 0xC3}; // Incomplete UTF-8 sequence
        boolean expected = false;
        boolean actual = Utf8.isWellFormed(bytes);
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testIsWellFormed_WithOffsetAndLength_WithWellFormedBytes_ReturnsTrue() {
        byte[] bytes = {(byte) 0xEF, (byte) 0xBB, (byte) 0xBF}; // UTF-8 BOM
        int off = 1;
        int len = 2;
        boolean expected = true;
        boolean actual = Utf8.isWellFormed(bytes, off, len);
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testIsWellFormed_WithOffsetAndLength_WithMalformedBytes_ReturnsFalse() {
        byte[] bytes = {(byte) 0xC3, (byte) 0x28}; // Invalid UTF-8 sequence
        int off = 0;
        int len = 2;
        boolean expected = false;
        boolean actual = Utf8.isWellFormed(bytes, off, len);
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testIsWellFormed_WithOffsetAndLength_WithPartialBytes_ReturnsFalse() {
        byte[] bytes = {(byte) 0xC3}; // Incomplete UTF-8 sequence
        int off = 0;
        int len = 1;
        boolean expected = false;
        boolean actual = Utf8.isWellFormed(bytes, off, len);
        Assert.assertEquals(expected, actual);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIsWellFormed_WithInvalidOffsetAndLength_ThrowsIllegalArgumentException() {
        byte[] bytes = {(byte) 0xEF, (byte) 0xBB, (byte) 0xBF}; // UTF-8 BOM
        int off = -1;
        int len = 4;
        Utf8.isWellFormed(bytes, off, len);
    }

    @Test
    public void testEncodedLengthGeneral_WithAsciiCharacters_ReturnsUtf8Length() {
        CharSequence sequence = "abcdef";
        int start = 0;
        int expected = 6;
        int actual = Utf8.encodedLengthGeneral(sequence, start);
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testEncodedLengthGeneral_WithNonAsciiCharacters_ReturnsUtf8Length() {
        CharSequence sequence = "abcüçğı";
        int start = 0;
        int expected = 10;
        int actual = Utf8.encodedLengthGeneral(sequence, start);
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testEncodedLengthGeneral_WithNonAsciiCharactersAndStartIndex_ReturnsUtf8Length() {
        CharSequence sequence = "abcüçğı";
        int start = 3;
        int expected = 7;
        int actual = Utf8.encodedLengthGeneral(sequence, start);
        Assert.assertEquals(expected, actual);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testEncodedLengthGeneral_WithInvalidStartIndex_ThrowsIllegalArgumentException() {
        CharSequence sequence = "abcdef";
        int start = -1;
        Utf8.encodedLengthGeneral(sequence, start);
    }

    @Test
    public void testIsWellFormedSlowPath_WithWellFormedBytes_ReturnsTrue() {
        byte[] bytes = {(byte) 0xEF, (byte) 0xBB, (byte) 0xBF}; // UTF-8 BOM
        int off = 0;
        int end = 3;
        boolean expected = true;
        boolean actual = Utf8.isWellFormedSlowPath(bytes, off, end);
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testIsWellFormedSlowPath_WithMalformedBytes_ReturnsFalse() {
        byte[] bytes = {(byte) 0xC3, (byte) 0x28}; // Invalid UTF-8 sequence
        int off = 0;
        int end = 2;
        boolean expected = false;
        boolean actual = Utf8.isWellFormedSlowPath(bytes, off, end);
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testIsWellFormedSlowPath_WithPartialBytes_ReturnsFalse() {
        byte[] bytes = {(byte) 0xC3}; // Incomplete UTF-8 sequence
        int off = 0;
        int end = 1;
        boolean expected = false;
        boolean actual = Utf8.isWellFormedSlowPath(bytes, off, end);
        Assert.assertEquals(expected, actual);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIsWellFormedSlowPath_WithInvalidOffsetAndLength_ThrowsIllegalArgumentException() {
        byte[] bytes = {(byte) 0xEF, (byte) 0xBB, (byte) 0xBF}; // UTF-8 BOM
        int off = -1;
        int end = 4;
        Utf8.isWellFormedSlowPath(bytes, off, end);
    }

}