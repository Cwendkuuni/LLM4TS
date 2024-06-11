import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class CaverphoneTest {
    
    private Caverphone caverphone;
    
    @Before
    public void setUp() {
        caverphone = new Caverphone();
    }
    
    @Test
    public void testCaverphone_validInput() {
        String expected = "FKR1111111";
        String result = caverphone.caverphone("faker");
        assertEquals(expected, result);
    }
    
    @Test
    public void testCaverphone_emptyInput() {
        String expected = "1111111111";
        String result = caverphone.caverphone("");
        assertEquals(expected, result);
    }
    
    @Test
    public void testCaverphone_nullInput() {
        String expected = "1111111111";
        String result = caverphone.caverphone(null);
        assertEquals(expected, result);
    }
    
    @Test
    public void testCaverphone_edgeCase() {
        String expected = "RNNNKTKSNA";
        String result = caverphone.caverphone("RRnK!5t? sNa");
        assertEquals(expected, result);
    }
    
    @Test
    public void testEncode_validInput() throws EncoderException {
        String expected = "FKR1111111";
        String result = (String) caverphone.encode("faker");
        assertEquals(expected, result);
    }
    
    @Test(expected = EncoderException.class)
    public void testEncode_nonStringInput() throws EncoderException {
        caverphone.encode(new Object());
    }
    
    @Test
    public void testEncode_nullInput() {
        String expected = "1111111111";
        String result = caverphone.encode(null);
        assertEquals(expected, result);
    }
    
    @Test
    public void testIsCaverphoneEqual_equalStrings() {
        String str1 = "test";
        String str2 = "test";
        assertTrue(caverphone.isCaverphoneEqual(str1, str2));
    }
    
    @Test
    public void testIsCaverphoneEqual_notEqualStrings() {
        String str1 = "test1";
        String str2 = "test2";
        assertFalse(caverphone.isCaverphoneEqual(str1, str2));
    }
    
    @Test
    public void testIsCaverphoneEqual_emptyStrings() {
        String str1 = "";
        String str2 = "";
        assertTrue(caverphone.isCaverphoneEqual(str1, str2));
    }
    
    @Test
    public void testIsCaverphoneEqual_nullStrings() {
        String str1 = null;
        String str2 = null;
        assertTrue(caverphone.isCaverphoneEqual(str1, str2));
    }
}