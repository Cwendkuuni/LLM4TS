import org.junit.Test;
import static org.junit.Assert.*;

public class IndexedStringTest {
    
    // Test for constructor
    @Test
    public void testConstructor() {
        String input = "hello";
        IndexedString indexedString = new IndexedString(input);
        assertNotNull(indexedString);
    }
    
    // Test for indexOf method
    @Test
    public void testIndexOf() {
        IndexedString indexedString = new IndexedString("hello");
        char[] target = {'e', 'l', 'l'};
        assertEquals(1, indexedString.indexOf(target));
    }
    
    // Test for edge case of indexOf method
    @Test
    public void testIndexOfEdgeCase() {
        IndexedString indexedString = new IndexedString("hello");
        char[] target = {'o'};
        assertEquals(4, indexedString.indexOf(target));
    }
    
    // Test for error scenario of empty target in indexOf method
    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void testIndexOfEmptyTarget() {
        IndexedString indexedString = new IndexedString("hello");
        char[] target = {};
        indexedString.indexOf(target);
    }
    
}