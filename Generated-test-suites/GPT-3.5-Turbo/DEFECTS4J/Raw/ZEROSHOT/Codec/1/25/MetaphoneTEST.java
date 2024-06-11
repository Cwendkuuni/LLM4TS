import org.junit.Test;
import static org.junit.Assert.*;

public class MetaphoneTest {

    @Test
    public void testMetaphone() {
        Metaphone metaphone = new Metaphone();

        String result1 = metaphone.metaphone("hello");
        assertEquals("HL", result1);

        String result2 = metaphone.metaphone("world");
        assertEquals("WRLT", result2);

        String result3 = metaphone.metaphone("Java");
        assertEquals("JF", result3);

        String result4 = metaphone.metaphone("test");
        assertEquals("TST", result4);
    }

    @Test
    public void testEncode() {
        Metaphone metaphone = new Metaphone();

        Object result1 = metaphone.encode("hello");
        assertEquals("HL", result1);

        Object result2 = metaphone.encode("world");
        assertEquals("WRLT", result2);

        Object result3 = metaphone.encode("Java");
        assertEquals("JF", result3);

        Object result4 = metaphone.encode("test");
        assertEquals("TST", result4);
    }

    @Test
    public void testIsMetaphoneEqual() {
        Metaphone metaphone = new Metaphone();

        boolean result1 = metaphone.isMetaphoneEqual("hello", "helo");
        assertTrue(result1);

        boolean result2 = metaphone.isMetaphoneEqual("world", "worl");
        assertTrue(result2);

        boolean result3 = metaphone.isMetaphoneEqual("Java", "jva");
        assertFalse(result3);

        boolean result4 = metaphone.isMetaphoneEqual("test", "tset");
        assertTrue(result4);
    }
}