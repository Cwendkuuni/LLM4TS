import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class IndexedStringTest {

    private IndexedString indexedString;

    @Before
    public void setUp() {
        indexedString = new IndexedString("abcABCabc");
    }

    @Test
    public void testCount_ValidCharacter_ReturnsCorrectCount() {
        assertEquals(3, indexedString.count('a'));
        assertEquals(2, indexedString.count('b'));
        assertEquals(2, indexedString.count('c'));
    }

    @Test
    public void testCount_InvalidCharacter_ReturnsZero() {
        assertEquals(0, indexedString.count('d'));
        assertEquals(0, indexedString.count('x'));
        assertEquals(0, indexedString.count('1'));
    }

    @Test
    public void testCount_EmptyString_ReturnsZero() {
        indexedString = new IndexedString("");
        assertEquals(0, indexedString.count('a'));
        assertEquals(0, indexedString.count('b'));
        assertEquals(0, indexedString.count('c'));
    }

    @Test
    public void testIndexOf_FoundTarget_ReturnsCorrectIndex() {
        char[] target = {'A', 'b', 'c'};
        assertEquals(3, indexedString.indexOf(target));

        target = new char[]{'c'};
        assertEquals(2, indexedString.indexOf(target));

        target = new char[]{'c', 'A'};
        assertEquals(5, indexedString.indexOf(target));
    }

    @Test
    public void testIndexOf_NotFoundTarget_ReturnsNegativeOne() {
        char[] target = {'x'};
        assertEquals(-1, indexedString.indexOf(target));

        target = new char[]{'A', 'B', 'C'};
        assertEquals(-1, indexedString.indexOf(target));

        target = new char[]{'a', 'b', 'c', 'A'};
        assertEquals(-1, indexedString.indexOf(target));
    }

    @Test
    public void testIndexOf_EmptyTarget_ReturnsNegativeOne() {
        char[] target = {};
        assertEquals(-1, indexedString.indexOf(target));
    }
}