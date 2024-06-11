import org.junit.Test;
import static org.junit.Assert.*;

public class IndexedStringTest {

    @Test
    public void testCount() {
        IndexedString indexedString = new IndexedString("Hello World");
        assertEquals(0, indexedString.count('A')); // Testing for a character that does not exist in the string
        assertEquals(3, indexedString.count('l')); // Testing for a character that occurs multiple times in the string
    }

    @Test
    public void testIndexOf() {
        IndexedString indexedString = new IndexedString("Hello World");
        char[] target1 = {'H', 'e', 'l', 'l', 'o'};
        assertEquals(0, indexedString.indexOf(target1)); // Testing for a target that exists at the beginning of the string
        char[] target2 = {'W', 'o', 'r', 'l', 'd'};
        assertEquals(6, indexedString.indexOf(target2)); // Testing for a target that exists at the end of the string
        char[] target3 = {'W', 'o', 'r', 'l', 'd', '!'};
        assertEquals(-1, indexedString.indexOf(target3)); // Testing for a target that does not exist in the string
    }
}