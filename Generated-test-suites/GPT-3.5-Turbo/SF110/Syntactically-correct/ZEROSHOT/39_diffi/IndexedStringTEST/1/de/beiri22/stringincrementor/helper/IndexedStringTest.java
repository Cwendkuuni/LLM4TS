import static org.junit.Assert.*;
import org.junit.Test;

public class IndexedStringTest {
    
    @Test
    public void testCount() {
        IndexedString indexedString = new IndexedString("hello");
        int expected = 2;
        int actual = indexedString.count('l');
        assertEquals(expected, actual);
    }
    
    @Test
    public void testCount_NoOccurrence() {
        IndexedString indexedString = new IndexedString("hello");
        int expected = 0;
        int actual = indexedString.count('z');
        assertEquals(expected, actual);
    }
    
    @Test
    public void testCount_EmptyString() {
        IndexedString indexedString = new IndexedString("");
        int expected = 0;
        int actual = indexedString.count('a');
        assertEquals(expected, actual);
    }
    
    @Test
    public void testCount_SingleCharacter() {
        IndexedString indexedString = new IndexedString("a");
        int expected = 1;
        int actual = indexedString.count('a');
        assertEquals(expected, actual);
    }
    
    @Test
    public void testIndexOf() {
        IndexedString indexedString = new IndexedString("hello");
        int expected = 2;
        int actual = indexedString.indexOf(new char[] {'l', 'l'});
        assertEquals(expected, actual);
    }
    
    @Test
    public void testIndexOf_FirstCharacterMismatch() {
        IndexedString indexedString = new IndexedString("hello");
        int expected = -1;
        int actual = indexedString.indexOf(new char[] {'h', 'l'});
        assertEquals(expected, actual);
    }
    
    @Test
    public void testIndexOf_IncompleteMatch() {
        IndexedString indexedString = new IndexedString("hello");
        int expected = -1;
        int actual = indexedString.indexOf(new char[] {'l', 'l', 'o'});
        assertEquals(expected, actual);
    }
    
    @Test
    public void testIndexOf_EmptyTarget() {
        IndexedString indexedString = new IndexedString("hello");
        int expected = -1;
        int actual = indexedString.indexOf(new char[] {});
        assertEquals(expected, actual);
    }
    
    @Test
    public void testIndexOf_EmptyIndexedString() {
        IndexedString indexedString = new IndexedString("");
        int expected = -1;
        int actual = indexedString.indexOf(new char[] {'a'});
        assertEquals(expected, actual);
    }
}