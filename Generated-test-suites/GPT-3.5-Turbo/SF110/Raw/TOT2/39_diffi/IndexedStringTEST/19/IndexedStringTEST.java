
import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class IndexedStringTest {

    private IndexedString indexedString;
    
    @Before
    public void setUp() {
        indexedString = new IndexedString("Hello");
    }

    @Test
    public void testCount() {
        assertEquals(2, indexedString.count('l'));
    }

    @Test
    public void testIndexOf() {
        char[] target = {'l', 'o'};
        assertEquals(3, indexedString.indexOf(target));
    }

    // Edge case test for indexOf()
    @Test
    public void testIndexOfEdgeCase() {
        char[] target = {'o'};
        assertEquals(4, indexedString.indexOf(target));
    }
    
}
