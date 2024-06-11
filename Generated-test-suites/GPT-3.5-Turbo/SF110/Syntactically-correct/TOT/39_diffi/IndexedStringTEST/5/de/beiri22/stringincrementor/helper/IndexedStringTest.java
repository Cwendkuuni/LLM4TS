import org.junit.Test;
import static org.junit.Assert.*;

public class IndexedStringTest {

    @Test
    public void testCount() {
        IndexedString indexedString = new IndexedString("hello");
        assertEquals(2, indexedString.count('l'));

        indexedString = new IndexedString("");
        assertEquals(0, indexedString.count('a'));
    }

    @Test
    public void testIndexOf() {
        IndexedString indexedString = new IndexedString("hello");
        char[] target = {'l', 'o'};
        assertEquals(3, indexedString.indexOf(target));

        target = new char[] {'x', 'y'};
        assertEquals(-1, indexedString.indexOf(target));
    }

    @Test
    public void testIndexOf_EmptyString() {
        IndexedString indexedString = new IndexedString("");
        char[] target = {'l', 'o'};
        assertEquals(-1, indexedString.indexOf(target));
    }

    @Test
    public void testIndexOf_TargetLengthBiggerThanValuesLength() {
        IndexedString indexedString = new IndexedString("hello");
        char[] target = {'l', 'o', 'n', 'g', 'e', 'r'};
        assertEquals(-1, indexedString.indexOf(target));
    }

    @Test
    public void testIndexOf_SameLetterAsFirstAndLast() {
        IndexedString indexedString = new IndexedString("hello");
        char[] target = {'l', 'e'};
        assertEquals(2, indexedString.indexOf(target));
    }

}