import org.junit.Test;
import static org.junit.Assert.*;

public class VerifierTest {

    @Test
    public void testGetValue() {
        String expected = "abcd1234";
        Verifier verifier = new Verifier(expected);
        
        String actual = verifier.getValue();
        
        assertEquals(expected, actual);
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
        
        String actual = verifier.getValue();
        
        assertEquals(value, actual);
    }
    
    @Test
    public void testLongValue() {
        String value = "abcdefghijklmnopqrstuvwxyz";
        Verifier verifier = new Verifier(value);
        
        String actual = verifier.getValue();
        
        assertEquals(value, actual);
    }
}