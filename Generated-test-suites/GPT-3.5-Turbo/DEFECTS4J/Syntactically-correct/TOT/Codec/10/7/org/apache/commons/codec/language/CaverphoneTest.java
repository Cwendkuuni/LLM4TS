import org.junit.Test;

import static org.junit.Assert.*;

public class CaverphoneTest {

    @Test
    public void testCaverphone_emptyString() {
        String expected = "1111111111";
        Caverphone caverphone = new Caverphone();
        
        String result = caverphone.caverphone("");
        
        assertEquals(expected, result);
    }
    
    @Test
    public void testCaverphone_onlyLowerCaseAlphabets() {
        String input = "hello";
        String expected = "HL11111111";
        Caverphone caverphone = new Caverphone();
        
        String result = caverphone.caverphone(input);
        
        assertEquals(expected, result);
    }
    
    @Test
    public void testCaverphone_removeNonAlphabets() {
        String input = "He6,.ll!@#o";
        String expected = "HL11111111";
        Caverphone caverphone = new Caverphone();
        
        String result = caverphone.caverphone(input);
        
        assertEquals(expected, result);
    }
    
    @Test
    public void testCaverphone_removeFinalE() {
        String input = "take";
        String expected = "TK11111111";
        Caverphone caverphone = new Caverphone();
        
        String result = caverphone.caverphone(input);
        
        assertEquals(expected, result);
    }
    
    @Test
    public void testCaverphone_startWithOptions() {
        String input = "coughing";
        String expected = "KFN1111111";
        Caverphone caverphone = new Caverphone();
        
        String result = caverphone.caverphone(input);
        
        assertEquals(expected, result);
    }
    
    @Test
    public void testCaverphone_handleReplacements() {
        String input = "quick";
        String expected = "K2K1111111";
        Caverphone caverphone = new Caverphone();
        
        String result = caverphone.caverphone(input);
        
        assertEquals(expected, result);
    }
    
    @Test
    public void testCaverphone_handleRemovals() {
        String input = "rack";
        String expected = "RK11111111";
        Caverphone caverphone = new Caverphone();
        
        String result = caverphone.caverphone(input);
        
        assertEquals(expected, result);
    }
    
    @Test
    public void testEncode_objectNotStringType() {
        Object input = new Object();
        Caverphone caverphone = new Caverphone();
        
        try {
            caverphone.encode(input);
            fail("EncoderException should be thrown");
        } catch (EncoderException e) {
            assertEquals("Parameter supplied to Caverphone encode is not of type java.lang.String", e.getMessage());
        }
    }
    
    @Test
    public void testEncode_validString() {
        String input = "hello";
        String expected = "HL11111111";
        Caverphone caverphone = new Caverphone();
        
        String result = caverphone.encode(input);
        
        assertEquals(expected, result);
    }
    
    @Test
    public void testIsCaverphoneEqual_identicalCaverphones() {
        String input1 = "hello";
        String input2 = "hello";
        Caverphone caverphone = new Caverphone();
        
        boolean result = caverphone.isCaverphoneEqual(input1, input2);
        
        assertTrue(result);
    }
    
    @Test
    public void testIsCaverphoneEqual_differentCaverphones() {
        String input1 = "hello";
        String input2 = "hallo";
        Caverphone caverphone = new Caverphone();
        
        boolean result = caverphone.isCaverphoneEqual(input1, input2);
        
        assertFalse(result);
    }
    
    @Test
    public void testIsCaverphoneEqual_emptyString() {
        String input1 = "";
        String input2 = "";
        Caverphone caverphone = new Caverphone();
        
        boolean result = caverphone.isCaverphoneEqual(input1, input2);
        
        assertTrue(result);
    }
}