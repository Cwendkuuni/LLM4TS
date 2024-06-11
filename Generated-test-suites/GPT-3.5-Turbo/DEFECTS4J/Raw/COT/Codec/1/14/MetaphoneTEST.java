import org.junit.Test;
import static org.junit.Assert.*;

public class MetaphoneTest {

    @Test
    public void testMetaphone(){
        Metaphone metaphone = new Metaphone();
        assertEquals("NK", metaphone.metaphone("KNOCK"));
        assertEquals("ALP", metaphone.metaphone("ALPHABET"));
        assertEquals("FKS", metaphone.metaphone("PHOENIX"));
    }
    
    @Test
    public void testEncode() throws EncoderException {
        Metaphone metaphone = new Metaphone();
        assertEquals("NK", metaphone.encode("KNOCK"));
        assertEquals("ALP", metaphone.encode("ALPHABET"));
        assertEquals("FKS", metaphone.encode("PHOENIX"));
    }
    
    @Test
    public void testIsMetaphoneEqual(){
        Metaphone metaphone = new Metaphone();
        assertTrue(metaphone.isMetaphoneEqual("KNOCK", "NOK"));
        assertFalse(metaphone.isMetaphoneEqual("ALPHABET", "ELPHBET"));
        assertFalse(metaphone.isMetaphoneEqual("PHOENIX", "PENX"));
    }
    
    @Test
    public void testGetMaxCodeLen(){
        Metaphone metaphone = new Metaphone();
        assertEquals(4, metaphone.getMaxCodeLen());
        metaphone.setMaxCodeLen(3);
        assertEquals(3, metaphone.getMaxCodeLen());
    }

}