import org.junit.Test;
import org.apache.commons.codec.EncoderException;

public class CaverphoneTest {

    @Test
    public void testCaverphone() {
        Caverphone caverphone = new Caverphone();
        
        // Test empty string
        String caverphoneCode = caverphone.caverphone("");
        assert caverphoneCode.equals("1111111111");
        
        // Test lowercase conversion and removing non-alphabetic characters
        caverphoneCode = caverphone.caverphone("A+b-C.d");
        assert caverphoneCode.equals("AAAABAAAAA");
        
        // Test start options
        caverphoneCode = caverphone.caverphone("cough");
        assert caverphoneCode.equals("CO2F");
        
        caverphoneCode = caverphone.caverphone("rough");
        assert caverphoneCode.equals("RO2F");
        
        caverphoneCode = caverphone.caverphone("tough");
        assert caverphoneCode.equals("TO2F");
        
        caverphoneCode = caverphone.caverphone("enough");
        assert caverphoneCode.equals("ENOU2F");
        
        caverphoneCode = caverphone.caverphone("trough");
        assert caverphoneCode.equals("TROU2F");
        
        caverphoneCode = caverphone.caverphone("gn");
        assert caverphoneCode.equals("2N");
        
        caverphoneCode = caverphone.caverphone("mb");
        assert caverphoneCode.equals("M2");
        
        // Test replacements
        caverphoneCode = caverphone.caverphone("cq");
        assert caverphoneCode.equals("2Q");
        
        caverphoneCode = caverphone.caverphone("ci");
        assert caverphoneCode.equals("SI");
        
        caverphoneCode = caverphone.caverphone("ce");
        assert caverphoneCode.equals("SE");
        
        caverphoneCode = caverphone.caverphone("cy");
        assert caverphoneCode.equals("SY");
        
        caverphoneCode = caverphone.caverphone("tch");
        assert caverphoneCode.equals("2CH");
        
        caverphoneCode = caverphone.caverphone("c");
        assert caverphoneCode.equals("K");
        
        caverphoneCode = caverphone.caverphone("q");
        assert caverphoneCode.equals("K");
        
        caverphoneCode = caverphone.caverphone("x");
        assert caverphoneCode.equals("K");
        
        caverphoneCode = caverphone.caverphone("v");
        assert caverphoneCode.equals("F");
        
        caverphoneCode = caverphone.caverphone("dg");
        assert caverphoneCode.equals("2G");
        
        caverphoneCode = caverphone.caverphone("tio");
        assert caverphoneCode.equals("SIO");
        
        caverphoneCode = caverphone.caverphone("tia");
        assert caverphoneCode.equals("SIA");
        
        caverphoneCode = caverphone.caverphone("d");
        assert caverphoneCode.equals("T");
        
        caverphoneCode = caverphone.caverphone("ph");
        assert caverphoneCode.equals("FH");
        
        caverphoneCode = caverphone.caverphone("b");
        assert caverphoneCode.equals("P");
        
        caverphoneCode = caverphone.caverphone("sh");
        assert caverphoneCode.equals("S2");
        
        caverphoneCode = caverphone.caverphone("z");
        assert caverphoneCode.equals("S");
        
        caverphoneCode = caverphone.caverphone("aeiou");
        assert caverphoneCode.equals("A3333");
        
        caverphoneCode = caverphone.caverphone("j");
        assert caverphoneCode.equals("Y"); 
       
        caverphoneCode = caverphone.caverphone("y3");
        assert caverphoneCode.equals("Y3");   
        
        caverphoneCode = caverphone.caverphone("y");
        assert caverphoneCode.equals("A");     
       
        caverphoneCode = caverphone.caverphone("3gh3");
        assert caverphoneCode.equals("3KH3");
       
        caverphoneCode = caverphone.caverphone("gh");
        assert caverphoneCode.equals("22");
        
        caverphoneCode = caverphone.caverphone("g");
        assert caverphoneCode.equals("K");
       
        caverphoneCode = caverphone.caverphone("s+");
        assert caverphoneCode.equals("S");
       
        caverphoneCode = caverphone.caverphone("t+");
        assert caverphoneCode.equals("T");
       
        caverphoneCode = caverphone.caverphone("p+");
        assert caverphoneCode.equals("P");
       
        caverphoneCode = caverphone.caverphone("k+");
        assert caverphoneCode.equals("K");
       
        caverphoneCode = caverphone.caverphone("f+");
        assert caverphoneCode.equals("F");
       
        caverphoneCode = caverphone.caverphone("m+");
        assert caverphoneCode.equals("M");
       
        caverphoneCode = caverphone.caverphone("n+");
        assert caverphoneCode.equals("N");
       
        caverphoneCode = caverphone.caverphone("w3");
        assert caverphoneCode.equals("W3");
       
        caverphoneCode = caverphone.caverphone("wh3");
        assert caverphoneCode.equals("WH3");
       
        caverphoneCode = caverphone.caverphone("w");
        assert caverphoneCode.equals("2");
        
        caverphoneCode = caverphone.caverphone("h");
        assert caverphoneCode.equals("A");
        
        caverphoneCode = caverphone.caverphone("r3");
        assert caverphoneCode.equals("R3");
        
        caverphoneCode = caverphone.caverphone("r");
        assert caverphoneCode.equals("2");
        
        caverphoneCode = caverphone.caverphone("l3");
        assert caverphoneCode.equals("L3");
        
        caverphoneCode = caverphone.caverphone("l");
        assert caverphoneCode.equals("2");
        
        // Test removals
        caverphoneCode = caverphone.caverphone("2");
        assert caverphoneCode.equals("");
        
        caverphoneCode = caverphone.caverphone("3");
        assert caverphoneCode.equals("A");
        
        // Test adding 1's
        caverphoneCode = caverphone.caverphone("1234567890");
        assert caverphoneCode.equals("12345678901111111111");
        
        caverphoneCode = caverphone.caverphone("12345");
        assert caverphoneCode.equals("1234500000");
        
        // Test code length
        caverphoneCode = caverphone.caverphone("abcdefghijklmnopqrstuvwxyz");
        assert caverphoneCode.length() == 10;
        
    }
    
    @Test
    public void testEncode() {
        Caverphone caverphone = new Caverphone();
        
        // Test empty string
        String caverphoneCode = caverphone.encode("");
        assert caverphoneCode.equals("1111111111");
        
        // Test lowercase conversion and removing non-alphabetic characters
        caverphoneCode = caverphone.encode("A+b-C.d");
        assert caverphoneCode.equals("AAAABAAAAA");
        
    }
    
    @Test
    public void testIsCaverphoneEqual() {
        Caverphone caverphone = new Caverphone();
        
        // Test identical caverphones
        boolean isEqual = caverphone.isCaverphoneEqual("abc", "abc");
        assert isEqual;
        
        // Test different caverphones
        isEqual = caverphone.isCaverphoneEqual("abc", "def");
        assert !isEqual;
    }
}