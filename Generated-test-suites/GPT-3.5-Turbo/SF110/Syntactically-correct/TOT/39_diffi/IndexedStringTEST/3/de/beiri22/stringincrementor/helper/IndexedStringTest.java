import org.junit.Test;
import static org.junit.Assert.*;

public class IndexedStringTest {

    @Test
    public void testCount() {
        IndexedString indexedString = new IndexedString("abcabcabc");
        
        int count = indexedString.count('a');
        
        assertEquals(3, count);
    }
    
    @Test
    public void testCountNoMatch() {
        IndexedString indexedString = new IndexedString("abcabcabc");
        
        int count = indexedString.count('z');
        
        assertEquals(0, count);
    }
    
    @Test
    public void testIndexOf() {
        IndexedString indexedString = new IndexedString("abcabcabc");
        
        int index = indexedString.indexOf("abc".toCharArray());
        
        assertEquals(0, index);
    }
    
    @Test
    public void testIndexOfNoMatch() {
        IndexedString indexedString = new IndexedString("abcabcabc");
        
        int index = indexedString.indexOf("def".toCharArray());
        
        assertEquals(-1, index);
    }
    
    @Test
    public void testIndexOfEdgeCase() {
        IndexedString indexedString = new IndexedString("");
        
        int index = indexedString.indexOf("abc".toCharArray());
        
        assertEquals(-1, index);
    }
    
    @Test
    public void testIndexOfErrorScenario() {
        IndexedString indexedString = new IndexedString(null);
        
        int index = indexedString.indexOf("abc".toCharArray());
        
        assertEquals(-1, index);
    }
}