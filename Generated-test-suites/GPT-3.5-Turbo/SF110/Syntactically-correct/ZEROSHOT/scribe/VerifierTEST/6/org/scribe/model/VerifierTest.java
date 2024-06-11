import org.junit.Test;
import static org.junit.Assert.*;

public class VerifierTest {
    
    @Test
    public void testGetValue() {
        Verifier verifier = new Verifier("test");
        String expected = "test";
        String actual = verifier.getValue();
        assertEquals(expected, actual);
    }
    
    @Test(expected = NullPointerException.class)
    public void testNullValue() {
        Verifier verifier = new Verifier(null);
        verifier.getValue();
    }
    
    @Test
    public void testEmptyValue() {
        Verifier verifier = new Verifier("");
        String expected = "";
        String actual = verifier.getValue();
        assertEquals(expected, actual);
    }
    
    @Test
    public void testLongValue() {
        Verifier verifier = new Verifier("This is a long value");
        String expected = "This is a long value";
        String actual = verifier.getValue();
        assertEquals(expected, actual);
    }
}