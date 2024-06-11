
import org.junit.Test;
import static org.junit.Assert.*;

public class IndexedStringTest {
    
    @Test
    public void testCount() {
        IndexedString indexedString = new IndexedString("hello");
        
        int count = indexedString.count('l');
        assertEquals(2, count);
    }
    
    @Test
    public void testIndexOf() {
        IndexedString indexedString = new IndexedString("hello");
        
        int index = indexedString.indexOf("el".toCharArray());
        assertEquals(1, index);
        
        int index2 = indexedString.indexOf("ll".toCharArray());
        assertEquals(2, index2);
        
        int index3 = indexedString.indexOf("h".toCharArray());
        assertEquals(0, index3);
        
        int index4 = indexedString.indexOf("he".toCharArray());
        assertEquals(0, index4);
        
        int index5 = indexedString.indexOf("o".toCharArray());
        assertEquals(4, index5);
        
        int index6 = indexedString.indexOf("lllll".toCharArray());
        assertEquals(-1, index6);
        
        int index7 = indexedString.indexOf("z".toCharArray());
        assertEquals(-1, index7);
    }
}
