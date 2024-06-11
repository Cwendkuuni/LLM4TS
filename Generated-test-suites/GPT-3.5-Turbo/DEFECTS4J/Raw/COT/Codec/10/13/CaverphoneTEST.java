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
    public void testCaverphone() {
        String result = caverphone.caverphone("test");
        assertEquals("t1st111111", result);
    }
    
    @Test(expected = NullPointerException.class)
    public void testCaverphone_Null() {
        caverphone.caverphone(null);
    }
    
    @Test
    public void testCaverphone_EmptyString() {
        String result = caverphone.caverphone("");
        assertEquals("1111111111", result);
    }
    
    @Test
    public void testCaverphone_Uppercase() {
        String result = caverphone.caverphone("TEST");
        assertEquals("t1st111111", result);
    }
    
    @Test
    public void testCaverphone_IllegalCharacters() {
        String result = caverphone.caverphone("t$e*s&t");
        assertEquals("tst1111111", result);
    }
    
    @Test
    public void testCaverphone_FinalE() {
        String result = caverphone.caverphone("apple");
        assertEquals("apl11111111", result);
    }
    
    @Test
    public void testCaverphone_StartOptions() {
        String result = caverphone.caverphone("cough");
        assertEquals("cou2f11111", result);
        
        result = caverphone.caverphone("rough");
        assertEquals("rou2f11111", result);
        
        result = caverphone.caverphone("tough");
        assertEquals("tou2f11111", result);
        
        result = caverphone.caverphone("enough");
        assertEquals("enou2f1111", result);
        
        result = caverphone.caverphone("trough");
        assertEquals("trou2f1111", result);
        
        result = caverphone.caverphone("gnash");
        assertEquals("2nash11111", result);
    }
    
    @Test
    public void testCaverphone_End() {
        String result = caverphone.caverphone("lamb");
        assertEquals("lam21111111", result);
    }
    
    @Test
    public void testCaverphone_Replacements() {
        String result = caverphone.caverphone("cylinder");
        assertEquals("sylindh13", result);
        
        result = caverphone.caverphone("queen");
        assertEquals("k23n111111", result);
        
        result = caverphone.caverphone("xerox");
        assertEquals("k123ks1111", result);
        
        result = caverphone.caverphone("vase");
        assertEquals("fase111111", result);
        
        result = caverphone.caverphone("dog");
        assertEquals("t21", result);
        
        result = caverphone.caverphone("tion");
        assertEquals("sion", result);
        
        result = caverphone.caverphone("damage");
        assertEquals("t1m1k1", result);
        
        result = caverphone.caverphone("phoenix");
        assertEquals("f13n1ks", result);
        
        result = caverphone.caverphone("boy");
        assertEquals("poy", result);
        
        result = caverphone.caverphone("shop");
        assertEquals("sop11", result);
        
        result = caverphone.caverphone("zoom");
        assertEquals("som", result);
        
        result = caverphone.caverphone("apple");
        assertEquals("apl11111111", result);
        
        result = caverphone.caverphone("tough");
        assertEquals("tou2f11111", result);
        
        result = caverphone.caverphone("cough");
        assertEquals("cou2f11111", result);
        
        result = caverphone.caverphone("enough");
        assertEquals("enou2f1111", result);
    }
    
    @Test
    public void testCaverphone_Removals() {
        String result = caverphone.caverphone("ejaculate");
        assertEquals("iIklt", result);
        
        result = caverphone.caverphone("eve");
        assertEquals("3141", result);
        
        result = caverphone.caverphone("why");
        assertEquals("Ai3111", result);
    }
    
    @Test
    public void testEncode_Object() throws EncoderException {
        String result = (String) caverphone.encode((Object) "test");
        assertEquals("t1st111111", result);
    }
    
    @Test(expected = EncoderException.class)
    public void testEncode_Object_NotString() throws EncoderException {
        caverphone.encode((Object) 123);
    }
    
    @Test
    public void testEncode_String() {
        String result = caverphone.encode("test");
        assertEquals("t1st111111", result);
    }
    
    @Test
    public void testIsCaverphoneEqual() {
        boolean result = caverphone.isCaverphoneEqual("test", "tst");
        assertTrue(result);
        
        result = caverphone.isCaverphoneEqual("test", "abc");
        assertFalse(result);
    }
}