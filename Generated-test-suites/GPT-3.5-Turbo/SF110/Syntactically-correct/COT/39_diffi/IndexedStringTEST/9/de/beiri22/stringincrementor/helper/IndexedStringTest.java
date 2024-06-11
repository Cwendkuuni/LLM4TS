
import static org.junit.Assert.*;
import org.junit.Test;

public class IndexedStringTest {

    @Test
    public void testCount() {
        IndexedString indexedString = new IndexedString("test string");
        char c = 't';
        int expectedCount = 2;
        int actualCount = indexedString.count(c);
        assertEquals(expectedCount, actualCount);
    }
    
    @Test
    public void testIndexedString() {
        String s = "test string";
        IndexedString indexedString = new IndexedString(s);
        assertNotNull(indexedString);
    }
    
    @Test
    public void testIndexOf() {
        IndexedString indexedString = new IndexedString("Test String");
        char[] target = {'S', 't', 'r', 'i', 'n', 'g'};
        int expectedIndex = 5;
        int actualIndex = indexedString.indexOf(target);
        assertEquals(expectedIndex, actualIndex);
    }
    
}
