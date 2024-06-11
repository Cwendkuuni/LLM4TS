import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.Test;

public class IndexedStringTest {

    private IndexedString indexedString;
    private final char[] testTarget = {'t', 'e', 's', 't'};

    @Before
    public void setUp() {
        indexedString = new IndexedString("test string");
    }

    @Test
    public void testCount() {
        int count = indexedString.count('t');
        assertEquals(3, count);
    }

    @Test
    public void testIndexOf() {
        int index = indexedString.indexOf(testTarget);
        assertEquals(0, index);
    }

    @Test
    public void testIndexOf_InvalidTarget() {
        char[] invalidTarget = {'s', 't', 'r', 'i', 'n', 'g'};
        int index = indexedString.indexOf(invalidTarget);
        assertEquals(-1, index);
    }
}