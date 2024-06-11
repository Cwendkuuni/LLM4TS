import org.junit.*;
import static org.junit.Assert.*;

public class Utf8Test {

    @Test
    public void testEncodedLength() {
        CharSequence sequence1 = "Hello World";
        int expected1 = 11;
        int result1 = Utf8.encodedLength(sequence1);
        assertEquals(expected1, result1);

        CharSequence sequence2 = "";
        int expected2 = 0;
        int result2 = Utf8.encodedLength(sequence2);
        assertEquals(expected2, result2);

        CharSequence sequence3 = "äöü";
        int expected3 = 6;
        int result3 = Utf8.encodedLength(sequence3);
        assertEquals(expected3, result3);
    }

    @Test
    public void testEncodedLengthGeneral() {
        CharSequence sequence1 = "Hello World";
        int start1 = 0;
        int expected1 = 11;
        int result1 = Utf8.encodedLengthGeneral(sequence1, start1);
        assertEquals(expected1, result1);

        CharSequence sequence2 = "";
        int start2 = 0;
        int expected2 = 0;
        int result2 = Utf8.encodedLengthGeneral(sequence2, start2);
        assertEquals(expected2, result2);

        CharSequence sequence3 = "äöü";
        int start3 = 0;
        int expected3 = 6;
        int result3 = Utf8.encodedLengthGeneral(sequence3, start3);
        assertEquals(expected3, result3);
    }

    @Test
    public void testIsWellFormed() {
        byte[] bytes1 = {72, 101, 108, 108, 111, 32, 87, 111, 114, 108, 100};
        boolean expected1 = true;
        boolean result1 = Utf8.isWellFormed(bytes1);
        assertEquals(expected1, result1);

        byte[] bytes2 = {};
        boolean expected2 = true;
        boolean result2 = Utf8.isWellFormed(bytes2);
        assertEquals(expected2, result2);

        byte[] bytes3 = {-61, -92, -61, -74, -61, -68};
        boolean expected3 = true;
        boolean result3 = Utf8.isWellFormed(bytes3);
        assertEquals(expected3, result3);
    }

    @Test
    public void testIsWellFormedWithOffAndLen() {
        byte[] bytes1 = {72, 101, 108, 108, 111, 32, 87, 111, 114, 108, 100};
        int off1 = 0;
        int len1 = 11;
        boolean expected1 = true;
        boolean result1 = Utf8.isWellFormed(bytes1, off1, len1);
        assertEquals(expected1, result1);

        byte[] bytes2 = {};
        int off2 = 0;
        int len2 = 0;
        boolean expected2 = true;
        boolean result2 = Utf8.isWellFormed(bytes2, off2, len2);
        assertEquals(expected2, result2);

        byte[] bytes3 = {-61, -92, -61, -74, -61, -68};
        int off3 = 0;
        int len3 = 6;
        boolean expected3 = true;
        boolean result3 = Utf8.isWellFormed(bytes3, off3, len3);
        assertEquals(expected3, result3);
    }

    @Test
    public void testIsWellFormedSlowPath() {
        byte[] bytes1 = {72, 101, 108, 108, 111, 32, 87, 111, 114, 108, 100};
        int off1 = 0;
        int end1 = 11;
        boolean expected1 = true;
        boolean result1 = Utf8.isWellFormedSlowPath(bytes1, off1, end1);
        assertEquals(expected1, result1);

        byte[] bytes2 = {};
        int off2 = 0;
        int end2 = 0;
        boolean expected2 = true;
        boolean result2 = Utf8.isWellFormedSlowPath(bytes2, off2, end2);
        assertEquals(expected2, result2);

        byte[] bytes3 = {-61, -92, -61, -74, -61, -68};
        int off3 = 0;
        int end3 = 6;
        boolean expected3 = true;
        boolean result3 = Utf8.isWellFormedSlowPath(bytes3, off3, end3);
        assertEquals(expected3, result3);
    }

}