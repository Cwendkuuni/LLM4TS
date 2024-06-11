import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class IndexedStringTest {

    private IndexedString indexedString;

    @Before
    public void setUp() {
        indexedString = new IndexedString("hello world");
    }

    @Test
    public void testCount() {
        char[] values = {'h', 'e', 'l', 'l', 'o', ' ', 'w', 'o', 'r', 'l', 'd'};
        assertEquals(1, indexedString.count('h'));
        assertEquals(3, indexedString.count('l'));
        assertEquals(2, indexedString.count('o'));
    }

    @Test
    public void testIndexOf() {
        char[] target1 = {'l', 'l', 'o'};
        char[] target2 = {'w', 'o', 'r'};
        char[] target3 = {'d', 'l', 'o'};
        char[] target4 = {'x', 'y', 'z'};

        assertEquals(2, indexedString.indexOf(target1));
        assertEquals(6, indexedString.indexOf(target2));
        assertEquals(-1, indexedString.indexOf(target3));
        assertEquals(-1, indexedString.indexOf(target4));
    }

    @Test
    public void testConstructor() {
        IndexedString emptyString = new IndexedString("");
        IndexedString repeatedCharacterString = new IndexedString("aaaaaab");
        IndexedString specialCharacterString = new IndexedString("!@#$%^&*()");

        assertNotNull(emptyString);
        assertNotNull(repeatedCharacterString);
        assertNotNull(specialCharacterString);
    }

}