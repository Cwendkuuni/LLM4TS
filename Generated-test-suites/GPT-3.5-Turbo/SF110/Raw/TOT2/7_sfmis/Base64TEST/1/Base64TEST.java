import org.junit.Test;
import static org.junit.Assert.*;

public class Base64Test {
    
    @Test
    public void testAltBase64ToByteArray(){
        byte[] result = Base64.altBase64ToByteArray("dGVzdA==");
        byte[] expected = new byte[]{116, 101, 115, 116};
        assertArrayEquals(expected, result);
    }
    
    @Test
    public void testBase64ToByteArray(){
        byte[] result = Base64.base64ToByteArray("dGVzdA==");
        byte[] expected = new byte[]{116, 101, 115, 116};
        assertArrayEquals(expected, result);
    }
    
    @Test
    public void test_$23183_IllegalCharacter(){
        try{
            Base64._$23183('!', Base64._$23166);
            fail("Expected IllegalArgumentException for illegal character");
        }catch(IllegalArgumentException e){
            // expected
        }
    }
    
    @Test
    public void testByteArrayToAltBase64(){
        byte[] input = new byte[]{116, 101, 115, 116};
        String result = Base64.byteArrayToAltBase64(input);
        String expected = "dGVzdA==";
        assertEquals(expected, result);
    }
    
    @Test
    public void testByteArrayToBase64(){
        byte[] input = new byte[]{116, 101, 115, 116};
        String result = Base64.byteArrayToBase64(input);
        String expected = "dGVzdA==";
        assertEquals(expected, result);
    }
}