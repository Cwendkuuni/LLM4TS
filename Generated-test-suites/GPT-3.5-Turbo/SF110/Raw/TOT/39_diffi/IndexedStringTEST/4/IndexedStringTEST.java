import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class IndexedStringTest {

    private IndexedString indexedString;

    @Before
    public void setUp() {
        indexedString = new IndexedString("Example string for testing");
    }

    @Test
    public void testCount() {
        assertEquals(0, indexedString.count('a'));
        assertEquals(3, indexedString.count('s'));
        assertEquals(5, indexedString.count('t'));
    }

    @Test
    public void testCountEdgeCases() {
        indexedString = new IndexedString("");
        assertEquals(0, indexedString.count('a'));

        indexedString = new IndexedString(" ");
        assertEquals(0, indexedString.count(' '));

        indexedString = new IndexedString("aaaaa");
        assertEquals(5, indexedString.count('a'));

        indexedString = new IndexedString("!@#$%^&*()");
        assertEquals(0, indexedString.count('z'));
    }

    @Test
    public void testIndexedStringCreation() {
        assertNotNull(indexedString);
    }

    @Test
    public void testIndexOf() {
        char[] target = {'s', 't', 'r', 'i', 'n', 'g'};
        assertEquals(8, indexedString.indexOf(target));
    }

    @Test
    public void testIndexOfNotFound() {
        char[] target = {'x', 'y', 'z'};
        assertEquals(-1, indexedString.indexOf(target));
    }

    @Test
    public void testIndexOfEmptyTarget() {
        char[] target = {};
        assertEquals(-1, indexedString.indexOf(target));
    }

    @Test
    public void testIndexOfEdgeCases() {
        char[] target = {'E', 'x', 'a', 'm', 'p', 'l', 'e', ' ', 's', 't', 'r', 'i', 'n', 'g', ' ', 'f', 'o', 'r', ' ', 't', 'e', 's', 't', 'i', 'n', 'g'};
        assertEquals(0, indexedString.indexOf(target));

        char[] target2 = {'f', 'o', 'r', ' ', 't', 'e', 's', 't', 'i', 'n', 'g'};
        assertEquals(16, indexedString.indexOf(target2));

        char[] target3 = {'E'};
        assertEquals(0, indexedString.indexOf(target3));
    }

}