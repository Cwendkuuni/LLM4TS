
import org.junit.Test;
import static org.junit.Assert.*;

public class IndexedStringTest {

    @Test
    public void testCount() {
        IndexedString indexedString = new IndexedString("test");
        assertEquals(2, indexedString.count('t'));
        assertEquals(1, indexedString.count('e'));
        assertEquals(0, indexedString.count('z'));
    }

    @Test
    public void testIndexedString() {
        IndexedString indexedString = new IndexedString("test");
        assertNotNull(indexedString);
    }

    @Test
    public void testIndexOf() {
        IndexedString indexedString = new IndexedString("test");

        char[] target = {'t', 'e', 's', 't'};
        assertEquals(0, indexedString.indexOf(target));

        char[] target2 = {'e'};
        assertEquals(1, indexedString.indexOf(target2));

        char[] target3 = {'z'};
        assertEquals(-1, indexedString.indexOf(target3));
    }
}
