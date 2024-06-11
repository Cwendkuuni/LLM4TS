import org.junit.Test;
import static org.junit.Assert.*;


public class MetaphoneTest {

    private Metaphone metaphone = new Metaphone();

    @Test
    public void testMetaphone() {
        assertEquals("AN", metaphone.metaphone("AN"));
        assertEquals("SMPL", metaphone.metaphone("SAMPLE"));
        assertEquals("MSTKA", metaphone.metaphone("MISTAKE"));
        assertEquals("FNK", metaphone.metaphone("FUNK"));
        assertEquals("FNK", metaphone.metaphone("PHUNK"));
        assertEquals("WR", metaphone.metaphone("WRITING"));
        assertEquals("K", metaphone.metaphone("KNOW"));
        assertEquals("STR", metaphone.metaphone("STRING"));
        assertEquals("FSHN", metaphone.metaphone("FASHION"));
        assertEquals("TKMNK", metaphone.metaphone("TECHNOMONK"));
    }

    @Test
    public void testEncode() throws EncoderException {
        assertEquals("SMPL", metaphone.encode("SAMPLE"));
        assertEquals("MSTKA", metaphone.encode("MISTAKE"));
        assertEquals("FNK", metaphone.encode("FUNK"));
    }

    @Test
    public void testIsMetaphoneEqual() {
        assertTrue(metaphone.isMetaphoneEqual("SAMPLE", "SMPL"));
        assertFalse(metaphone.isMetaphoneEqual("FUNK", "FNK"));
    }

    @Test
    public void testGetMaxCodeLen() {
        assertEquals(4, metaphone.getMaxCodeLen());
    }

    @Test
    public void testSetMaxCodeLen() {
        metaphone.setMaxCodeLen(6);
        assertEquals(6, metaphone.getMaxCodeLen());
    }
  
}