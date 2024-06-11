import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class IndexedStringTest {

    private IndexedString indexedString;

    @Before
    public void setup() {
        indexedString = new IndexedString("abcde");
    }

    @Test
    public void testCount() {
        assertEquals(1, indexedString.count('a'));
        assertEquals(2, indexedString.count('e'));
        assertEquals(0, indexedString.count('z'));
    }

    @Test
    public void testIndexOf() {
        char[] target = {'a'};
        assertEquals(0, indexedString.indexOf(target));

        target = new char[]{'e'};
        assertEquals(4, indexedString.indexOf(target));

        target = new char[]{'a', 'b', 'c'};
        assertEquals(0, indexedString.indexOf(target));

        target = new char[]{'b', 'c'};
        assertEquals(1, indexedString.indexOf(target));

        target = new char[]{'e', 'a'};
        assertEquals(-1, indexedString.indexOf(target));

        target = new char[]{'z'};
        assertEquals(-1, indexedString.indexOf(target));

        target = new char[]{};
        assertEquals(-1, indexedString.indexOf(target));
    }

}