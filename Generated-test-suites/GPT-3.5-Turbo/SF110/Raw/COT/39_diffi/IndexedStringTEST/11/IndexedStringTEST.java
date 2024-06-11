import org.junit.Test;
import static org.junit.Assert.*;

public class IndexedStringTest {

    // Test case for the count() method
    @Test
    public void testCount() {
        IndexedString indexedString = new IndexedString("aabbbc");
        assertEquals(2, indexedString.count('a'));
        assertEquals(3, indexedString.count('b'));
        assertEquals(1, indexedString.count('c'));
        assertEquals(0, indexedString.count('d'));
    }

    // Test case for the constructor
    @Test
    public void testConstructor() {
        IndexedString indexedString = new IndexedString("aabbbc");
        char[] expectedValues = {'a', 'a', 'b', 'b', 'b', 'c'};
        assertArrayEquals(expectedValues, indexedString.values);
        int[][] expectedIndex = new int[256][];
        expectedIndex['a'] = new int[] {0, 1};
        expectedIndex['b'] = new int[] {2, 3, 4};
        expectedIndex['c'] = new int[] {5};
        assertArrayEquals(expectedIndex, indexedString.index);
    }

    // Test case for the indexOf() method
    @Test
    public void testIndexOf() {
        IndexedString indexedString = new IndexedString("aabbbc");
        assertEquals(0, indexedString.indexOf(new char[] {'a'}));
        assertEquals(2, indexedString.indexOf(new char[] {'b'}));
        assertEquals(-1, indexedString.indexOf(new char[] {'c'}));
        assertEquals(1, indexedString.indexOf(new char[] {'a', 'b'}));
        assertEquals(-1, indexedString.indexOf(new char[] {'b', 'c'}));
        assertEquals(0, indexedString.indexOf(new char[] {'a', 'a'}));
        assertEquals(2, indexedString.indexOf(new char[] {'b', 'b'}));
        assertEquals(2, indexedString.indexOf(new char[] {'b', 'b', 'b'}));
        assertEquals(-1, indexedString.indexOf(new char[] {'a', 'a', 'b'}));
    }

    // Additional test case for empty string input in constructor
    @Test
    public void testConstructorEmptyString() {
        IndexedString indexedString = new IndexedString("");
        char[] expectedValues = {};
        assertArrayEquals(expectedValues, indexedString.values);
        int[][] expectedIndex = new int[256][];
        for (int i = 0; i < 256; i++) {
            expectedIndex[i] = new int[0];
        }
        assertArrayEquals(expectedIndex, indexedString.index);
    }

    // Additional test case for null string input in constructor
    @Test(expected = NullPointerException.class)
    public void testConstructorNullString() {
        IndexedString indexedString = new IndexedString(null);
    }
}