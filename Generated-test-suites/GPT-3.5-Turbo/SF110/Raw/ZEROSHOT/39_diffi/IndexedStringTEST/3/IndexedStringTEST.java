import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class IndexedStringTest {

    private IndexedString indexedString;

    @Before
    public void setUp() {
        indexedString = new IndexedString("hello world");
    }

    @Test
    public void testCount() {
        int count = indexedString.count('o');
        assertEquals(2, count);

        count = indexedString.count('l');
        assertEquals(3, count);

        count = indexedString.count('z');
        assertEquals(0, count);
    }

    @Test
    public void testIndexOf() {
        int index = indexedString.indexOf("wor".toCharArray());
        assertEquals(6, index);

        index = indexedString.indexOf("ello".toCharArray());
        assertEquals(1, index);

        index = indexedString.indexOf("abc".toCharArray());
        assertEquals(-1, index);
    }
}