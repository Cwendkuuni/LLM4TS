
import org.junit.Test;
import static org.junit.Assert.*;

public class IndexedStringTest {

    @Test
    public void testCount() {
        IndexedString indexedString = new IndexedString("abcabc");
        
        int result = indexedString.count('a');
        
        assertEquals(2, result);
    }
    
    @Test
    public void testIndexedString() {
        String input = "abcabc";
        IndexedString indexedString = new IndexedString(input);
        
        char[] values = new char[input.length()];
        input.getChars(0, input.length(), values, 0);
        
        assertArrayEquals(values, indexedString.values);
    }
    
    @Test
    public void testIndexOf() {
        IndexedString indexedString = new IndexedString("abcabc");
        
        char[] target = {'a', 'b'};
        int result = indexedString.indexOf(target);
        
        assertEquals(0, result);
    }
    
}
