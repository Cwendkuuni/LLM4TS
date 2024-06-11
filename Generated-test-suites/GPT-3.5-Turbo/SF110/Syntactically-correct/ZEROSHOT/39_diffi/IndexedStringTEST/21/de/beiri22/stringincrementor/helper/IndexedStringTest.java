import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class IndexedStringTest {
    private IndexedString indexedString;

    @Before
    public void setUp() {
        indexedString = new IndexedString("Hello World");
    }

    @Test
    public void testCount() {
        char c = 'o';
        int expectedResult = 2;
        int result = indexedString.count(c);
        assertEquals(expectedResult, result);
    }

    @Test
    public void testIndexOf() {
        char[] target = {'l', 'd'};
        int expectedResult = 9;
        int result = indexedString.indexOf(target);
        assertEquals(expectedResult, result);
    }
}