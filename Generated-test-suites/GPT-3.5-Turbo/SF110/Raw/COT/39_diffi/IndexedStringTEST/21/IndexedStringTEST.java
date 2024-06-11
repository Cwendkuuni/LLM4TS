import org.junit.Test;
import static org.junit.Assert.*;

public class IndexedStringTest {

    @Test
    public void testCount() {
        IndexedString indexedString = new IndexedString("abc");
        int count = indexedString.count('a');
        assertEquals(1, count);
    }

    @Test
    public void testConstructor() {
        IndexedString indexedString = new IndexedString("abc");
        assertNotNull(indexedString);
    }

    @Test
    public void testIndexOf() {
        IndexedString indexedString = new IndexedString("abc");

        char[] target1 = {'a', 'b'};
        int index1 = indexedString.indexOf(target1);
        assertEquals(0, index1);

        char[] target2 = {'b', 'c'};
        int index2 = indexedString.indexOf(target2);
        assertEquals(1, index2);

        char[] target3 = {'c'};
        int index3 = indexedString.indexOf(target3);
        assertEquals(2, index3);

        char[] target4 = {'d'};
        int index4 = indexedString.indexOf(target4);
        assertEquals(-1, index4);
    }
}