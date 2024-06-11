import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class MetaphoneTest {

    private Metaphone metaphone;

    @Before
    public void setup() {
        metaphone = new Metaphone();
    }

    @Test
    public void testMetaphone() {
        assertEquals("MN", metaphone.metaphone("MN"));
        assertEquals("MTFN", metaphone.metaphone("METAPHON"));
        assertEquals("KMH", metaphone.metaphone("CHROME"));
        assertEquals("TKSN", metaphone.metaphone("TEXACAN"));
    }

    @Test
    public void testEncode() {
        assertEquals("MN", metaphone.encode("MN"));
        assertEquals("MTFN", metaphone.encode("METAPHON"));
        assertEquals("KMH", metaphone.encode("CHROME"));
        assertEquals("TKSN", metaphone.encode("TEXACAN"));
    }

    @Test
    public void testIsMetaphoneEqual() {
        assertTrue(metaphone.isMetaphoneEqual("MN", "MN"));
        assertTrue(metaphone.isMetaphoneEqual("MTFN", "METAPHON"));
        assertTrue(metaphone.isMetaphoneEqual("KMH", "CHROME"));
        assertTrue(metaphone.isMetaphoneEqual("TKSN", "TEXACAN"));
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