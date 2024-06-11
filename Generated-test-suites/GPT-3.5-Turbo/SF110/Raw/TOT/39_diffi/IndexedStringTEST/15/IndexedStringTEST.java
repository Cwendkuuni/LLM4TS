import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class IndexedStringTest {
    private IndexedString indexedString;

    @BeforeEach
    void setUp() {
        indexedString = new IndexedString("hello, world!");
    }

    @Test
    void testCount() {
        assertEquals(3, indexedString.count('l'));
        assertEquals(2, indexedString.count('o'));
    }

    @Test
    void testConstructor_ValidString() {
        assertDoesNotThrow(() -> new IndexedString(""));
        assertDoesNotThrow(() -> new IndexedString("a"));
        assertDoesNotThrow(() -> new IndexedString("test"));
    }

    @Test
    void testConstructor_NullString() {
        assertThrows(NullPointerException.class, () -> new IndexedString(null));
    }

    @Test
    void testIndexOf_Found() {
        assertEquals(2, indexedString.indexOf(new char[]{'l', 'o', 'r'}));
        assertEquals(7, indexedString.indexOf(new char[]{'w', 'o', 'r'}));
    }

    @Test
    void testIndexOf_NotFound() {
        assertEquals(-1, indexedString.indexOf(new char[]{'x', 'y', 'z'}));
        assertEquals(-1, indexedString.indexOf(new char[]{'l', 'w', 'd'}));
    }

    @Test
    void testIndexOf_EmptyArray() {
        assertEquals(-1, indexedString.indexOf(new char[]{}));
    }

    @Test
    void testIndexOf_LongerArrayThanSource() {
        assertEquals(-1, indexedString.indexOf(new char[]{'w', 'o', 'r', 'l', 'd', '!'}));
    }

    @Test
    void testIndexOf_SourceContainsSpecialCharacters() {
        indexedString = new IndexedString("h&llo, w^rld!");
        assertEquals(2, indexedString.indexOf(new char[]{'&', '^'}));
        assertEquals(-1, indexedString.indexOf(new char[]{'&', '@'}));
    }
}