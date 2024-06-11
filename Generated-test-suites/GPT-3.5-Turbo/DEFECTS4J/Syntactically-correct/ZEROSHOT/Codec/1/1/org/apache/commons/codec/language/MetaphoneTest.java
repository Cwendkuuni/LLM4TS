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
        String result = metaphone.metaphone("hello");
        assertEquals("HL", result);
    }

    @Test
    public void testEncode() throws EncoderException {
        Object result = metaphone.encode("world");
        assertEquals("WRLD", result);
    }

    @Test
    public void testEncode() {
        String result = metaphone.encode("example");
        assertEquals("EKSMP", result);
    }

    @Test
    public void testIsMetaphoneEqual() {
        boolean result = metaphone.isMetaphoneEqual("hello", "hilo");
        assertEquals(true, result);
    }

    @Test
    public void testGetMaxCodeLen() {
        int result = metaphone.getMaxCodeLen();
        assertEquals(4, result);
    }

    @Test
    public void testSetMaxCodeLen() {
        metaphone.setMaxCodeLen(5);
        int result = metaphone.getMaxCodeLen();
        assertEquals(5, result);
    }

}