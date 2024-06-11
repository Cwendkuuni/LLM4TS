
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class IndexedStringTest {

    @Test
    public void testCount() {
        IndexedString indexedString = new IndexedString("banana");
        assertEquals(3, indexedString.count('a'));
        assertEquals(1, indexedString.count('b'));
        assertEquals(0, indexedString.count('c'));
    }

    @Test
    public void testIndexOf() {
        IndexedString indexedString = new IndexedString("banana");
        assertEquals(0, indexedString.indexOf("ba".toCharArray()));
        assertEquals(1, indexedString.indexOf("an".toCharArray()));
        assertEquals(3, indexedString.indexOf("na".toCharArray()));
        assertEquals(-1, indexedString.indexOf("xyz".toCharArray()));
    }
  
    @Test
    public void testConstructor() {
        IndexedString indexedString = new IndexedString("hello");
        char[] values = indexedString.values;
        assertEquals('h', values[0]);
        assertEquals('e', values[1]);
        assertEquals('l', values[2]);
        assertEquals('l', values[3]);
        assertEquals('o', values[4]);
    }

}

