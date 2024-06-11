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
    public void testCountMethod() {
        assertEquals(3, indexedString.count('l')); // typical use case
        assertEquals(0, indexedString.count('z')); // edge case: character not present in the string
        assertEquals(0, indexedString.count('H')); // edge case: character not present in the string
        assertEquals(1, indexedString.count('d')); // edge case: character occurs only once
    }
    
    @Test
    public void testIndexOfMethod() {
        assertEquals(8, indexedString.indexOf(new char[]{'o', 'r', 'l', 'd'})); // typical use case
        assertEquals(0, indexedString.indexOf(new char[]{'H', 'e', 'l', 'l', 'o'})); // typical use case
        assertEquals(-1, indexedString.indexOf(new char[]{'x'})); // edge case: character not present in the string
        assertEquals(-1, indexedString.indexOf(new char[]{'W', 'o', 'r', 'l', 'd', '!', '!'})); // edge case: target array is longer than string
    }
}