
import org.junit.Test;
import static org.junit.Assert.*;

public class IndexedStringTest {

    @Test
    public void testCount() {
        IndexedString indexedString = new IndexedString("my_string");
        int count = indexedString.count('s');
        assertEquals(1, count);
    }

    @Test
    public void testConstructor() {
        IndexedString indexedString = new IndexedString("my_string");
        assertNotNull(indexedString);
    }

    @Test
    public void testIndexOf() {
        IndexedString indexedString = new IndexedString("my_string");
        int index = indexedString.indexOf("str".toCharArray());
        assertEquals(3, index);
    }
    
    @Test
    public void testIndexOfNotFound() {
        IndexedString indexedString = new IndexedString("my_string");
        int index = indexedString.indexOf("abc".toCharArray());
        assertEquals(-1, index);
    }

    // Additional test cases for edge cases and exception handling
    
    @Test
    public void testConstructorWithEmptyString() {
        IndexedString indexedString = new IndexedString("");
        assertNotNull(indexedString);
    }

    @Test(expected = NullPointerException.class)
    public void testConstructorWithNullString() {
        IndexedString indexedString = new IndexedString(null);
    }

    @Test
    public void testIndexOfWithEmptyTargetArray() {
        IndexedString indexedString = new IndexedString("my_string");
        int index = indexedString.indexOf("".toCharArray());
        assertEquals(0, index);
    }
    
    @Test(expected = NullPointerException.class)
    public void testIndexOfWithNullTargetArray() {
        IndexedString indexedString = new IndexedString("my_string");
        int index = indexedString.indexOf(null);
    }

    @Test
    public void testIndexOfWithNonexistentCharacter() {
        IndexedString indexedString = new IndexedString("my_string");
        int index = indexedString.indexOf("z".toCharArray());
        assertEquals(-1, index);
    }
    
    @Test
    public void testIndexOfWithLongerTargetArray() {
        IndexedString indexedString = new IndexedString("my_string");
        int index = indexedString.indexOf("stringstring".toCharArray());
        assertEquals(-1, index);
    }
    
    @Test
    public void testIndexOfWithShorterTargetArray() {
        IndexedString indexedString = new IndexedString("my_string");
        int index = indexedString.indexOf("stri".toCharArray());
        assertEquals(-1, index);
    }
}
