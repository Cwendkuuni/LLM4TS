import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class MetaphoneTest {

    private Metaphone metaphone;

    @Before
    public void setUp() {
        metaphone = new Metaphone();
    }

    @Test
    public void testMetaphone() {
        assertEquals("KTMN", metaphone.metaphone("kateman"));
    }

    @Test
    public void testEncode() {
        assertEquals("KTMN", metaphone.encode("kateman"));
    }

    @Test
    public void testIsMetaphoneEqual() {
        assertEquals(true, metaphone.isMetaphoneEqual("kateman", "kateman"));
        assertEquals(false, metaphone.isMetaphoneEqual("kateman", "katerman"));
    }

    @Test
    public void testGetMaxCodeLen() {
        assertEquals(4, metaphone.getMaxCodeLen());
    }

    @Test
    public void testSetMaxCodeLen() {
        metaphone.setMaxCodeLen(5);
        assertEquals(5, metaphone.getMaxCodeLen());
    }
}