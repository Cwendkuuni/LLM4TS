import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import de.beiri22.stringincrementor.helper.IndexedString;

public class IndexedStringTest {
    private IndexedString indexedString;
    
    @Before
    public void setUp() {
        indexedString = new IndexedString("example");
    }
    
    // Test case for count() method
    @Test
    public void testCount() {
        assertEquals(1, indexedString.count('x'));
        assertEquals(2, indexedString.count('e'));
        assertEquals(0, indexedString.count('z'));
    }
    
    // Test case for indexOf() method
    @Test
    public void testIndexOf() {
        assertEquals(0, indexedString.indexOf("example".toCharArray()));
        assertEquals(1, indexedString.indexOf("xample".toCharArray()));
        assertEquals(2, indexedString.indexOf("amp".toCharArray()));
        assertEquals(-1, indexedString.indexOf("xyz".toCharArray()));
    }
}