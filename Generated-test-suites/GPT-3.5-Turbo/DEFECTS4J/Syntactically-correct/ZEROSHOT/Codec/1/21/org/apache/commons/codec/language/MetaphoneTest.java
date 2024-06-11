import org.junit.Test;

import static org.junit.Assert.assertEquals;


public class MetaphoneTest {

    @Test
    public void testMetaphone() {
        Metaphone metaphone = new Metaphone();
        assertEquals("LTHR", metaphone.metaphone("Leather"));
        assertEquals("SWTHRT", metaphone.metaphone("Sweetheart"));
        assertEquals("PHN", metaphone.metaphone("Phone"));
    }

    @Test
    public void testEncode() {
        Metaphone metaphone = new Metaphone();
        assertEquals("LTHR", metaphone.encode("Leather"));
        assertEquals("SWTHRT", metaphone.encode("Sweetheart"));
        assertEquals("PHN", metaphone.encode("Phone"));
    }

    @Test
    public void testIsMetaphoneEqual() {
        Metaphone metaphone = new Metaphone();
        assertEquals(true, metaphone.isMetaphoneEqual("Leather", "LTHR"));
        assertEquals(false, metaphone.isMetaphoneEqual("Sweetheart", "LTHR"));
        assertEquals(true, metaphone.isMetaphoneEqual("Phone", "PHN"));
    }

    @Test
    public void testGetMaxCodeLen() {
        Metaphone metaphone = new Metaphone();
        assertEquals(4, metaphone.getMaxCodeLen());
    }

    @Test
    public void testSetMaxCodeLen() {
        Metaphone metaphone = new Metaphone();
        metaphone.setMaxCodeLen(5);
        assertEquals(5, metaphone.getMaxCodeLen());
    }
}