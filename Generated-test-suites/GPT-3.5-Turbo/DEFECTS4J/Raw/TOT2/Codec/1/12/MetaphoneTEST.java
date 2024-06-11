import org.junit.Test;
import static org.junit.Assert.*;

public class MetaphoneTest {

    private Metaphone metaphone;

    @Before
    public void setUp() {
        metaphone = new Metaphone();
    }

    @Test
    public void testMetaphoneTypicalCase() {
        assertEquals("MTFN", metaphone.metaphone("Metaphone"));
    }

    @Test
    public void testMetaphoneEmptyString() {
        assertEquals("", metaphone.metaphone(""));
    }

    @Test
    public void testMetaphoneSingleCharacter() {
        assertEquals("A", metaphone.metaphone("a"));
    }

    @Test
    public void testEncodeObjectCorrectType() throws EncoderException {
        Object encoded = metaphone.encode("Test");
        assertTrue(encoded instanceof String);
        assertEquals("TST", encoded);
    }

    @Test(expected = EncoderException.class)
    public void testEncodeObjectIncorrectType() throws EncoderException {
        metaphone.encode(123);
    }

    @Test
    public void testEncodeString() {
        assertEquals("MTFN", metaphone.encode("Metaphone"));
    }

    @Test
    public void testIsMetaphoneEqualTrue() {
        assertTrue(metaphone.isMetaphoneEqual("hello", "helo"));
    }

    @Test
    public void testIsMetaphoneEqualFalse() {
        assertFalse(metaphone.isMetaphoneEqual("hello", "world"));
    }

    @Test
    public void testSetAndGetMaxCodeLen() {
        metaphone.setMaxCodeLen(6);
        assertEquals(6, metaphone.getMaxCodeLen());
    }
}