import org.junit.Test;
import static org.junit.Assert.*;

public class IndexedStringTest {

    @Test
    public void testCount() {
        IndexedString str = new IndexedString("aabbcc");
        assertEquals(2, str.count('a'));
        assertEquals(2, str.count('b'));
        assertEquals(2, str.count('c'));
    }

    @Test
    public void testConstructor() {
        IndexedString str = new IndexedString("hello");
        assertNotNull(str);
    }

    @Test
    public void testIndexOf() {
        IndexedString str = new IndexedString("Hello, World!");

        int result = str.indexOf("World".toCharArray());
        assertEquals(7, result);

        result = str.indexOf("Hello".toCharArray());
        assertEquals(0, result);

        result = str.indexOf("!".toCharArray());
        assertEquals(12, result);

        result = str.indexOf("Java".toCharArray());
        assertEquals(-1, result);
    }

    @Test
    public void testIndexOfEmptyString() {
        IndexedString str = new IndexedString("");

        int result = str.indexOf("Hello".toCharArray());
        assertEquals(-1, result);
    }

    @Test
    public void testIndexOfSingleCharacter() {
        IndexedString str = new IndexedString("a");

        int result = str.indexOf("a".toCharArray());
        assertEquals(0, result);

        result = str.indexOf("b".toCharArray());
        assertEquals(-1, result);
    }
}