import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class MetaphoneTest {
  
    private Metaphone metaphone;
  
    @Before
    public void setUp() {
        metaphone = new Metaphone();
    }
  
    @Test
    public void testMetaphone_typicalCase() {
        assertEquals("SMT", metaphone.metaphone("Smith"));
        assertEquals("ALFPTLSA", metaphone.metaphone("alphabetical"));
    }
  
    @Test
    public void testMetaphone_edgeCase() {
        assertEquals("SMT", metaphone.metaphone("s"));
        assertEquals("J", metaphone.metaphone("j"));
        assertEquals("O", metaphone.metaphone("O"));
        assertEquals("KTSN", metaphone.metaphone("XkT12$sN"));
    }
  
    @Test
    public void testMetaphone_errorCase() {
        assertNull(metaphone.metaphone(null));
        assertEquals("", metaphone.metaphone(""));
        assertEquals("AE", metaphone.metaphone("AEIOU"));
    }
  
    @Test
    public void testEncode_typicalCase() throws EncoderException {
        assertEquals("SM0", metaphone.encode("Smith"));
        assertEquals("ALFPTLSA", metaphone.encode("alphabetical"));
    }
  
    @Test(expected = EncoderException.class)
    public void testEncode_errorCase() throws EncoderException {
        metaphone.encode(123);
    }
  
    @Test
    public void testIsMetaphoneEqual_typicalCase() {
        assertTrue(metaphone.isMetaphoneEqual("Smith", "Smath"));
        assertFalse(metaphone.isMetaphoneEqual("alphabetical", "alphabet"));
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