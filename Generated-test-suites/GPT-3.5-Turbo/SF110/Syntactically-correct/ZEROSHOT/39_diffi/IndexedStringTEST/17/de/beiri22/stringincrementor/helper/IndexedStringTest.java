import org.junit.Test;
import static org.junit.Assert.*;

public class IndexedStringTest {

    @Test
    public void testCount() {
        IndexedString indexedString = new IndexedString("abrakadabra");
        assertEquals(5, indexedString.count('a'));
        assertEquals(2, indexedString.count('b'));
        assertEquals(2, indexedString.count('r'));
        assertEquals(1, indexedString.count('k'));
        assertEquals(1, indexedString.count('d'));
    }

    @Test
    public void testIndexOf() {
        IndexedString indexedString1 = new IndexedString("abrakadabra");
        assertEquals(3, indexedString1.indexOf("aka".toCharArray()));
        assertEquals(7, indexedString1.indexOf("bra".toCharArray()));
        assertEquals(0, indexedString1.indexOf("abr".toCharArray()));
        assertEquals(-1, indexedString1.indexOf("xyz".toCharArray()));

        IndexedString indexedString2 = new IndexedString("banana");
        assertEquals(1, indexedString2.indexOf("an".toCharArray()));
        assertEquals(1, indexedString2.indexOf("ana".toCharArray()));
        assertEquals(3, indexedString2.indexOf("na".toCharArray()));
        assertEquals(-1, indexedString2.indexOf("xyz".toCharArray()));
    }
}