import org.junit.Test;
import static org.junit.Assert.*;

public class IndexedStringTest {

    @Test
    public void testCount() {
        IndexedString indexedString = new IndexedString("aaa");
        assertEquals(3, indexedString.count('a'));
        assertEquals(0, indexedString.count('b'));
    }

    @Test
    public void testConstructor() {
        IndexedString indexedString = new IndexedString("hello");
        char[] expectedValues = {'h', 'e', 'l', 'l', 'o'};
        int[][] expectedIndex = new int[256][];
        expectedIndex['h'] = new int[1];
        expectedIndex['h'][0] = 0;
        expectedIndex['e'] = new int[1];
        expectedIndex['e'][0] = 1;
        expectedIndex['l'] = new int[2];
        expectedIndex['l'][0] = 2;
        expectedIndex['l'][1] = 3;
        expectedIndex['o'] = new int[1];
        expectedIndex['o'][0] = 4;

        assertArrayEquals(expectedValues, indexedString.values);
        assertArrayEquals(expectedIndex, indexedString.index);
    }

    @Test
    public void testIndexOf() {
        IndexedString indexedString = new IndexedString("hello");
        char[] target = {'l', 'l', 'o'};
        assertEquals(2, indexedString.indexOf(target));

        char[] target2 = {'a', 'b', 'c'};
        assertEquals(-1, indexedString.indexOf(target2));
    }
}