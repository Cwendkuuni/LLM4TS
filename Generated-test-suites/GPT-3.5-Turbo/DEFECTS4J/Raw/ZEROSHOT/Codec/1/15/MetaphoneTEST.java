import org.junit.Test;

import static org.junit.Assert.*;

public class MetaphoneTest {

    @Test
    public void testMetaphone() {
        Metaphone metaphone = new Metaphone();

        assertEquals("SMN", metaphone.metaphone("Simon"));
        assertEquals("RKPT", metaphone.metaphone("rocket"));
        assertEquals("AL", metaphone.metaphone("Al"));
    }

    @Test
    public void testEncode() {
        Metaphone metaphone = new Metaphone();

        assertEquals("SMN", metaphone.encode("Simon"));
        assertEquals("RKPT", metaphone.encode("rocket"));
        assertEquals("AL", metaphone.encode("Al"));
    }

    @Test
    public void testIsMetaphoneEqual() {
        Metaphone metaphone = new Metaphone();

        assertTrue(metaphone.isMetaphoneEqual("Simon", "Symon"));
        assertFalse(metaphone.isMetaphoneEqual("Simon", "rocket"));
        assertTrue(metaphone.isMetaphoneEqual("Al", "Albert"));
    }

    @Test
    public void testGetMaxCodeLen() {
        Metaphone metaphone = new Metaphone();
        metaphone.setMaxCodeLen(3);

        assertEquals(3, metaphone.getMaxCodeLen());
    }

    @Test
    public void testSetMaxCodeLen() {
        Metaphone metaphone = new Metaphone();
        metaphone.setMaxCodeLen(3);

        assertEquals(3, metaphone.getMaxCodeLen());
    }

}