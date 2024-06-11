import org.junit.Test;

public class MetaphoneTest {

    @Test
    public void testMetaphone() {
        Metaphone metaphone = new Metaphone();
        
        // Basic test cases
        String result1 = metaphone.metaphone("test");
        assert(result1.equals("TST"));
        
        String result2 = metaphone.metaphone("hello");
        assert(result2.equals("HL"));
        
        // Edge cases and exceptions
        String result3 = metaphone.metaphone("A");
        assert(result3.equals("A"));
        
        String result4 = metaphone.metaphone("Z");
        assert(result4.equals("S"));
        
        String result5 = metaphone.metaphone(null);
        assert(result5.equals(""));
        
        String result6 = metaphone.metaphone("123");
        assert(result6.equals(""));
    }
    
    @Test
    public void testEncode() throws EncoderException {
        Metaphone metaphone = new Metaphone();
        
        // Basic test cases
        String result1 = (String) metaphone.encode("test");
        assert(result1.equals("TST"));
        
        String result2 = (String) metaphone.encode("hello");
        assert(result2.equals("HL"));
        
        // Edge cases and exceptions
        try {
            String result3 = (String) metaphone.encode(null);
        } catch (EncoderException e) {
            assert(e.getMessage().equals("Parameter supplied to Metaphone encode is not of type java.lang.String"));
        }
    }
    
    @Test
    public void testIsMetaphoneEqual() {
        Metaphone metaphone = new Metaphone();
        
        // Basic test cases
        boolean result1 = metaphone.isMetaphoneEqual("test", "tEst");
        assert(result1 == true);
        
        boolean result2 = metaphone.isMetaphoneEqual("hello", "HL");
        assert(result2 == false);
        
        // Edge cases and exceptions
        boolean result3 = metaphone.isMetaphoneEqual("hello", null);
        assert(result3 == false);
    }

}