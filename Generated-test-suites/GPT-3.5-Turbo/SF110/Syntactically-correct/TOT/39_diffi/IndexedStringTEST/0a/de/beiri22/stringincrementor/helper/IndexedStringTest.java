import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class IndexedStringTest {

    private IndexedString indexedString;
    
    @Before
    public void setUp() {
        indexedString = new IndexedString("Hello World");
    }

    @Test
    public void testCount() {
        int count = indexedString.count('o');
        assertEquals(2, count);
    }

    @Test
    public void testCount_withNonExistCharacter() {
        int count = indexedString.count('z');
        assertEquals(0, count);
    }

    @Test
    public void testConstructor() {
        String s = indexedString.toString();
        assertEquals("Hello World", s);
    }

    @Test
    public void testIndexOf() {
        int index = indexedString.indexOf("World".toCharArray());
        assertEquals(6, index);
    }

    @Test
    public void testIndexOf_withEmptyTarget() {
        int index = indexedString.indexOf("".toCharArray());
        assertEquals(-1, index);
    }

    @Test
    public void testIndexOf_withNonExistTarget() {
        int index = indexedString.indexOf("foo".toCharArray());
        assertEquals(-1, index);
    }

    @Test
    public void testIndexOf_withTargetLargerThanValues() {
        int index = indexedString.indexOf("Hello World!!".toCharArray());
        assertEquals(-1, index);
    }
}