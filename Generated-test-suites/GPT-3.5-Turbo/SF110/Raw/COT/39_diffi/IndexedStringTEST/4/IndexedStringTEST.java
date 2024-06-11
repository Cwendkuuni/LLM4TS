
import org.junit.Test;
import static org.junit.Assert.*;

public class IndexedStringTest {

    @Test
    public void testCount() {
        IndexedString indexedString = new IndexedString("abcde");
        assertEquals(1, indexedString.count('a'));
        assertEquals(1, indexedString.count('b'));
        assertEquals(1, indexedString.count('c'));
        assertEquals(1, indexedString.count('d'));
        assertEquals(1, indexedString.count('e'));
        assertEquals(0, indexedString.count('f'));
    }

    @Test
    public void testConstructor() {
        IndexedString indexedString = new IndexedString("abcdebcd");
        assertArrayEquals(new char[]{'a', 'b', 'c', 'd', 'e', 'b', 'c', 'd'}, indexedString.values);
        assertEquals(0, indexedString.index['a'][0]);
        assertEquals(1, indexedString.index['b'][0]);
        assertEquals(5, indexedString.index['b'][1]);
        assertEquals(2, indexedString.index['c'][0]);
        assertEquals(6, indexedString.index['c'][1]);
        assertEquals(3, indexedString.index['d'][0]);
        assertEquals(7, indexedString.index['d'][1]);
        assertEquals(4, indexedString.index['e'][0]);
        assertEquals(0, indexedString.index['f'].length);
    }

    @Test
    public void testIndexOf() {
        IndexedString indexedString = new IndexedString("abcdebcd");
        assertEquals(0, indexedString.indexOf(new char[]{'a'}));
        assertEquals(1, indexedString.indexOf(new char[]{'b'}));
        assertEquals(2, indexedString.indexOf(new char[]{'c'}));
        assertEquals(3, indexedString.indexOf(new char[]{'d'}));
        assertEquals(4, indexedString.indexOf(new char[]{'e'}));
        assertEquals(0, indexedString.indexOf(new char[]{'a', 'b'}));
        assertEquals(5, indexedString.indexOf(new char[]{'b', 'c'}));
        assertEquals(6, indexedString.indexOf(new char[]{'c', 'd'}));
        assertEquals(-1, indexedString.indexOf(new char[]{'d', 'e'}));
        assertEquals(1, indexedString.indexOf(new char[]{'b', 'c', 'd'}));
        assertEquals(0, indexedString.indexOf(new char[]{'a', 'b', 'c'}));
        assertEquals(2, indexedString.indexOf(new char[]{'c', 'd', 'e'}));
        assertEquals(-1, indexedString.indexOf(new char[]{'d', 'e', 'f'}));
    }

    @Test
    public void testIndexOf_EmptyTarget() {
        IndexedString indexedString = new IndexedString("abcdebcd");
        assertEquals(-1, indexedString.indexOf(new char[0]));
    }

    @Test
    public void testIndexOf_LongTarget() {
        IndexedString indexedString = new IndexedString("abcdebcd");
        assertEquals(-1, indexedString.indexOf(new char[]{'a', 'b', 'c', 'd', 'e', 'b', 'c', 'd'}));
    }
}
