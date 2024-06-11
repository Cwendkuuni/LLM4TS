import static org.junit.Assert.*;
import org.junit.Test;

public class IndexedStringTest {

    @Test
    public void testCount() {
        IndexedString indexedString = new IndexedString("abcabcdabcde");
        // Check count of each character
        assertEquals(0, indexedString.count('z'));
        assertEquals(1, indexedString.count('a'));
        assertEquals(2, indexedString.count('b'));
        assertEquals(3, indexedString.count('c'));
        assertEquals(4, indexedString.count('d'));
        assertEquals(1, indexedString.count('e'));
    }

    @Test
    public void testIndexOf() {
        IndexedString indexedString = new IndexedString("abcabcdabcde");
        // Check index of character sequence
        assertEquals(0, indexedString.indexOf("abc".toCharArray()));
        assertEquals(3, indexedString.indexOf("abcd".toCharArray()));
        assertEquals(7, indexedString.indexOf("abcde".toCharArray()));
        assertEquals(-1, indexedString.indexOf("xyz".toCharArray()));
    }

    @Test
    public void testEmptyString() {
        IndexedString indexedString = new IndexedString("");
        // Check count of each character in empty string
        assertEquals(0, indexedString.count('a'));
        // Check index of character sequence in empty string
        assertEquals(-1, indexedString.indexOf("abc".toCharArray()));
    }

    @Test
    public void testSingleCharacterString() {
        IndexedString indexedString = new IndexedString("a");
        // Check count of each character in single character string
        assertEquals(1, indexedString.count('a'));
        // Check index of character sequence in single character string
        assertEquals(-1, indexedString.indexOf("abc".toCharArray()));
    }

    @Test
    public void testLongString() {
        String longString = "abcdefghijklmnopqrstuvwxyz";
        IndexedString indexedString = new IndexedString(longString);
        // Check count of each character in long string
        for (char c = 'a'; c <= 'z'; c++) {
            assertEquals(1, indexedString.count(c));
        }
        // Check index of character sequence in long string
        assertEquals(0, indexedString.indexOf(longString.toCharArray()));
    }
}