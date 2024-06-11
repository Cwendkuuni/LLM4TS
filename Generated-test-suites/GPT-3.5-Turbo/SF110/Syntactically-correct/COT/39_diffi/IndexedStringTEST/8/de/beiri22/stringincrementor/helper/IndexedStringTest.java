import org.junit.Test;
import static org.junit.Assert.*;

public class IndexedStringTest {

    @Test
    public void testCount() {
        IndexedString indexedString = new IndexedString("abcabc");
        int count = indexedString.count('a');
        assertEquals(2, count);
    }

    @Test
    public void testConstructor() {
        IndexedString indexedString = new IndexedString("abcabc");
        assertNotNull(indexedString);
    }

    @Test
    public void testIndexOf() {
        IndexedString indexedString = new IndexedString("abcabc");
        int index = indexedString.indexOf(new char[]{'a', 'b', 'c'});
        assertEquals(0, index);
    }

    @Test
    public void testIndexOf_NotFound() {
        IndexedString indexedString = new IndexedString("abcabc");
        int index = indexedString.indexOf(new char[]{'d', 'e', 'f'});
        assertEquals(-1, index);
    }

    @Test
    public void testIndexOf_MultipleOccurrences() {
        IndexedString indexedString = new IndexedString("abcabc");
        int index = indexedString.indexOf(new char[]{'b', 'c'});
        assertEquals(1, index);
    }

    @Test
    public void testCount_ZeroLengthString() {
        IndexedString indexedString = new IndexedString("");
        int count = indexedString.count('a');
        assertEquals(0, count);
    }

    @Test
    public void testCount_NoOccurrences() {
        IndexedString indexedString = new IndexedString("abcabc");
        int count = indexedString.count('d');
        assertEquals(0, count);
    }

    @Test
    public void testConstructor_NullString() {
        try {
            IndexedString indexedString = new IndexedString(null);
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            // Expected exception
        }
    }

    @Test
    public void testIndexOf_EmptyTarget() {
        IndexedString indexedString = new IndexedString("abcabc");
        int index = indexedString.indexOf(new char[]{});
        assertEquals(-1, index);
    }

    @Test
    public void testIndexOf_LongTargetNotFound() {
        IndexedString indexedString = new IndexedString("abcabc");
        int index = indexedString.indexOf(new char[]{'a', 'b', 'c', 'd', 'e', 'f'});
        assertEquals(-1, index);
    }
}