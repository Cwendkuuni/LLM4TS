import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class IndexedStringTest {

    private IndexedString indexedString;

    @Before
    public void setUp() {
        indexedString = new IndexedString("abcabc");
    }

    @Test
    public void testCount() {
        assertEquals(2, indexedString.count('a'));
        assertEquals(2, indexedString.count('b'));
        assertEquals(2, indexedString.count('c'));
    }

    @Test
    public void testIndexOf() {
        assertEquals(0, indexedString.indexOf(new char[]{'a', 'b', 'c'}));
        assertEquals(1, indexedString.indexOf(new char[]{'b', 'c', 'a'}));
        assertEquals(2, indexedString.indexOf(new char[]{'c', 'a', 'b'}));
        assertEquals(-1, indexedString.indexOf(new char[]{'d', 'e', 'f'}));
    }
}