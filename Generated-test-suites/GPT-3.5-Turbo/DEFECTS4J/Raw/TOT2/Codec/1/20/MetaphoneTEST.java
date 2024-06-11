import static org.junit.Assert.*;
import org.junit.Test;

public class MetaphoneTest {

    Metaphone metaphone = new Metaphone();

    @Test
    public void testMetaphone() {
        String result = metaphone.metaphone("hello");
        assertEquals("HLO", result);
    }

    @Test
    public void testEncodeObject() {
        Object result = metaphone.encode("world");
        assertEquals("WRLD", result);
    }

    @Test
    public void testEncodeString() {
        String result = metaphone.encode("coding");
        assertEquals("KTNK", result);
    }

    @Test
    public void testIsMetaphoneEqual() {
        boolean equal = metaphone.isMetaphoneEqual("hello", "halo");
        assertFalse(equal);
    }

    @Test
    public void testMaxCodeLen() {
        assertEquals(4, metaphone.getMaxCodeLen());
    }

    @Test
    public void testSetMaxCodeLen() {
        metaphone.setMaxCodeLen(3);
        assertEquals(3, metaphone.getMaxCodeLen());
    }

    @Test
    public void testMetaphoneEmptyString() {
        String result = metaphone.metaphone("");
        assertEquals("", result);
    }

    @Test
    public void testMetaphoneSingleCharacter() {
        String result = metaphone.metaphone("a");
        assertEquals("A", result);
    }

    @Test
    public void testMetaphoneSpecialCharacters() {
        String result = metaphone.metaphone("$#@!*");
        assertEquals("", result);
    }

    @Test
    public void testIsMetaphoneEqualEmptyStrings() {
        boolean equal = metaphone.isMetaphoneEqual("", "");
        assertTrue(equal);
    }

}