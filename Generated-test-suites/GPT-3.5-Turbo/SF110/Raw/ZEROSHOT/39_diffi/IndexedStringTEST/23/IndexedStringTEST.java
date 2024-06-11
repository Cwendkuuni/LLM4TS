import org.junit.Test;
import static org.junit.Assert.*;

public class IndexedStringTest {

    @Test
    public void testCount() {
        IndexedString indexedString = new IndexedString("ABCAB");
        assertEquals(2, indexedString.count('A'));
        assertEquals(2, indexedString.count('B'));
        assertEquals(1, indexedString.count('C'));
        assertEquals(0, indexedString.count('D'));
    }
    
    @Test
    public void testIndexOf() {
        IndexedString indexedString = new IndexedString("ABCAB");
        assertEquals(0, indexedString.indexOf(new char[]{'A','B'}));
        assertEquals(1, indexedString.indexOf(new char[]{'B','C'}));
        assertEquals(-1, indexedString.indexOf(new char[]{'C','A'}));
        assertEquals(-1, indexedString.indexOf(new char[]{'D','E'}));
    }

}