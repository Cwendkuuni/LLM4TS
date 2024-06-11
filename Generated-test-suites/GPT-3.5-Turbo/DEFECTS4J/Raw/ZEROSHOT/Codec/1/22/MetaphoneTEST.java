import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class MetaphoneTest {

    private Metaphone metaphone;

    @Before
    public void setUp() {
        metaphone = new Metaphone();
    }

    @Test
    public void testMetaphone() {
        assertEquals("A", metaphone.metaphone("A"));
        assertEquals("KNSW", metaphone.metaphone("KNOWS"));
        assertEquals("XOP", metaphone.metaphone("SHOP"));
        assertEquals("", metaphone.metaphone(""));
    }

    @Test
    public void testEncode() {
        assertEquals("A", metaphone.encode((Object) "A"));
        assertEquals("KNSW", metaphone.encode((Object) "KNOWS"));
        assertEquals("", metaphone.encode((Object) ""));
    }

    @Test
    public void testEncodeString() {
        assertEquals("A", metaphone.encode("A"));
        assertEquals("KNSW", metaphone.encode("KNOWS"));
        assertEquals("", metaphone.encode(""));
    }

    @Test
    public void testIsMetaphoneEqual() {
        assertTrue(metaphone.isMetaphoneEqual("A", "A"));
        assertFalse(metaphone.isMetaphoneEqual("A", "B"));
        assertTrue(metaphone.isMetaphoneEqual("KNOWS", "NOSE"));
        assertFalse(metaphone.isMetaphoneEqual("", "A"));
    }

    @Test
    public void testGetMaxCodeLen() {
        assertEquals(4, metaphone.getMaxCodeLen());
    }

    @Test
    public void testSetMaxCodeLen() {
        metaphone.setMaxCodeLen(2);
        assertEquals(2, metaphone.getMaxCodeLen());
    }
}