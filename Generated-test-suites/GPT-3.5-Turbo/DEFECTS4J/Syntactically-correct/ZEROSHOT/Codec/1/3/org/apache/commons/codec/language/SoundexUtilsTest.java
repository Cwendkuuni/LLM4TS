import static org.junit.Assert.*;
import org.junit.Test;

public class SoundexUtilsTest {

    @Test
    public void testClean() {
        String result = SoundexUtils.clean("A1bcD!$");
        assertEquals("ABCD", result);
        
        result = SoundexUtils.clean(null);
        assertNull(result);
        
        result = SoundexUtils.clean("");
        assertEquals("", result);
    }
    
    @Test
    public void testDifference() throws EncoderException {
        StringEncoder encoder = new Soundex();
        
        int result = SoundexUtils.difference(encoder, "Hello", "Halo");
        assertEquals(3, result);
        
        result = SoundexUtils.difference(encoder, "Ball", "Bawl");
        assertEquals(2, result);
        
        result = SoundexUtils.difference(encoder, null, "Test");
        assertEquals(0, result);
        
        result = SoundexUtils.difference(encoder, "Hello", null);
        assertEquals(0, result);
    }
    
    @Test
    public void testDifferenceEncoded() {
        int result = SoundexUtils.differenceEncoded("J123", "J123");
        assertEquals(4, result);
        
        result = SoundexUtils.differenceEncoded("A5UK", "A5IK");
        assertEquals(3, result);
        
        result = SoundexUtils.differenceEncoded(null, "J123");
        assertEquals(0, result);
        
        result = SoundexUtils.differenceEncoded("J123", null);
        assertEquals(0, result);
    }
}