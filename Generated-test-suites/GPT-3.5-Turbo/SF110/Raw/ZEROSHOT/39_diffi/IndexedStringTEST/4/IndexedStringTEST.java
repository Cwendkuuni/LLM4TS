import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import de.beiri22.stringincrementor.helper.IndexedString;

public class IndexedStringTest {
    
    private IndexedString indexedString;
    
    @Before
    public void setUp() {
        indexedString = new IndexedString("test");
    }

    @Test
    public void testIndexOf() {
        assertEquals(-1, indexedString.indexOf("hello".toCharArray()));
        assertEquals(0, indexedString.indexOf("te".toCharArray()));
        assertEquals(1, indexedString.indexOf("es".toCharArray()));
        assertEquals(2, indexedString.indexOf("st".toCharArray()));
        assertEquals(-1, indexedString.indexOf("xyz".toCharArray()));
    }
    
    @Test
    public void testCount() {
        assertEquals(2, indexedString.count('t'));
        assertEquals(1, indexedString.count('e'));
        assertEquals(1, indexedString.count('s'));
        assertEquals(0, indexedString.count('a'));
    }

}