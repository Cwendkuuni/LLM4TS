import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertArrayEquals;

import org.junit.Before;
import org.junit.Test;

public class IndexedStringTest {

    private IndexedString indexedString;

    @Before
    public void setUp() {
        indexedString = new IndexedString("Hello, World!");
    }

    @Test
    public void testCount_withValidChar_returnsCorrectCount() {
        assertEquals(3, indexedString.count('l'));
        assertNotEquals(2, indexedString.count('o'));
    }

    @Test
    public void testCount_withInvalidChar_returnsZero() {
        assertEquals(0, indexedString.count('z'));
    }

    @Test
    public void testIndexOf_withValidTarget_returnsCorrectIndex() {
        char[] target1 = {'l', 'o'};
        assertEquals(3, indexedString.indexOf(target1));

        char[] target2 = {',', ' '};
        assertEquals(5, indexedString.indexOf(target2));
    }

    @Test
    public void testIndexOf_withInvalidTarget_returnsNegativeOne() {
        char[] target = {'x', 'y'};
        assertEquals(-1, indexedString.indexOf(target));
    }

    @Test
    public void testIndexOf_withShortTarget_returnsNegativeOne() {
        char[] target = {'H'};
        assertEquals(-1, indexedString.indexOf(target));
    }

    @Test
    public void testIndexOf_withEmptyTarget_returnsCorrectIndex() {
        char[] target = {};
        assertEquals(0, indexedString.indexOf(target));
    }

    @Test
    public void testIndexOf_withOverlappingTarget_returnsCorrectIndex() {
        char[] target = {'o', ','};
        assertEquals(-1, indexedString.indexOf(target));
    }

    @Test
    public void testIndexOf_withPartialTarget_returnsNegativeOne() {
        char[] target = {'H', 'o', 'w'};
        assertEquals(-1, indexedString.indexOf(target));
    }

    @Test
    public void testIndexOf_withSpecialCharacters_returnsCorrectIndex() {
        char[] target1 = {' ', 'W', 'o', 'r', 'l'};
        assertEquals(6, indexedString.indexOf(target1));

        char[] target2 = {'r', 'l', 'd', '!'};
        assertEquals(12, indexedString.indexOf(target2));
    }

    @Test
    public void testIndexOf_withSameCharacterTarget_returnsCorrectIndex() {
        char[] target = {'o', 'o'};
        assertEquals(-1, indexedString.indexOf(target));
    }

}