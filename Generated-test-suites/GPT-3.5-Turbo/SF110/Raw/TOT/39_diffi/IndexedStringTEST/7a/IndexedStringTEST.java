import org.junit.Test;
import static org.junit.Assert.*;

public class IndexedStringTest {
    
    @Test
    public void testCount_typicalCase() {
        IndexedString indexedString = new IndexedString("abcabc");
        int countA = indexedString.count('a');
        int countB = indexedString.count('b');
        int countC = indexedString.count('c');
        
        assertEquals(2, countA);
        assertEquals(2, countB);
        assertEquals(2, countC);
    }
    
    @Test
    public void testCount_emptyString() {
        IndexedString indexedString = new IndexedString("");
        int countA = indexedString.count('a');
        
        assertEquals(0, countA);
    }
    
    @Test
    public void testIndexOf_typicalCase() {
        IndexedString indexedString = new IndexedString("hello");
        int index1 = indexedString.indexOf(new char[]{'h', 'e'});
        int index2 = indexedString.indexOf(new char[]{'e', 'l', 'l'});
        int index3 = indexedString.indexOf(new char[]{'o'});
        
        assertEquals(0, index1);
        assertEquals(1, index2);
        assertEquals(4, index3);
    }
    
    @Test
    public void testIndexOf_emptyTargetArray() {
        IndexedString indexedString = new IndexedString("hello");
        int index = indexedString.indexOf(new char[]{});
        
        assertEquals(-1, index);
    }
    
    @Test
    public void testIndexOf_emptyIndexedString() {
        IndexedString indexedString = new IndexedString("");
        int index = indexedString.indexOf(new char[]{'a'});
        
        assertEquals(-1, index);
    }
    
    @Test
    public void testIndexOf_targetLengthGreaterThanIndexedStringLength() {
        IndexedString indexedString = new IndexedString("hello");
        int index = indexedString.indexOf(new char[]{'h', 'e', 'l', 'l', 'o', 'o'});
        
        assertEquals(-1, index);
    }
}