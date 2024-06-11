import org.junit.Test;
import static org.junit.Assert.*;

public class VerifierTest {

    @Test
    public void testGetValue() {
        String value = "testValue";
        Verifier verifier = new Verifier(value);
        
        assertEquals(value, verifier.getValue());
    }

    @Test(expected = NullPointerException.class)
    public void testNullValue() {
        String value = null;
        Verifier verifier = new Verifier(value);
    }
    
    @Test
    public void testEmptyValue() {
        String value = "";
        Verifier verifier = new Verifier(value);
        
        assertEquals(value, verifier.getValue());
    }
    
    @Test
    public void testSpecialCharactersValue() {
        String value = "$%&";
        Verifier verifier = new Verifier(value);
        
        assertEquals(value, verifier.getValue());
    }
    
    @Test
    public void testLongValue() {
        String value = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz1234567890";
        Verifier verifier = new Verifier(value);
        
        assertEquals(value, verifier.getValue());
    }

}