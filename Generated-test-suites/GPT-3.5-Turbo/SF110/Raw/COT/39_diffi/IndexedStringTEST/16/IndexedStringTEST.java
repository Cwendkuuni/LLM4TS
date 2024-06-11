
import org.junit.Test;
import static org.junit.Assert.*;

public class IndexedStringTest {

    @Test
    public void testCount() {
        IndexedString indexedString = new IndexedString("aabbaa");
        assertEquals(4, indexedString.count('a'));
        assertEquals(2, indexedString.count('b'));
        assertEquals(0, indexedString.count('c'));
    }

    @Test
    public void testIndexOf() {
        IndexedString indexedString = new IndexedString("aabbaa");
        char[] target = {'a', 'b'};
        assertEquals(1, indexedString.indexOf(target));
        char[] target2 = {'b', 'a'};
        assertEquals(3, indexedString.indexOf(target2));
        char[] target3 = {'c', 'd'};
        assertEquals(-1, indexedString.indexOf(target3));
    }
}
