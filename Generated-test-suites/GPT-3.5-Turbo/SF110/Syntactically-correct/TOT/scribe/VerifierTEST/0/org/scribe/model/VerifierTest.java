import org.junit.Test;
import static org.junit.Assert.*;

import org.scribe.model.Verifier;
import org.scribe.utils.Preconditions;

public class VerifierTest {
    
    @Test
    public void testConstructor_ValidValue() {
        String value = "validValue";
        Verifier verifier = new Verifier(value);
        assertNotNull(verifier);
        assertEquals(value, verifier.getValue());
    }
    
    @Test(expected = NullPointerException.class)
    public void testConstructor_NullValue() {
        Verifier verifier = new Verifier(null);
    }
    
    @Test
    public void testGetValue() {
        String value = "testValue";
        Verifier verifier = new Verifier(value);
        assertEquals(value, verifier.getValue());
    }
    
    @Test
    public void testGetValue_EmptyValue() {
        String value = "";
        Verifier verifier = new Verifier(value);
        assertEquals(value, verifier.getValue());
    }
    
    @Test
    public void testGetValue_LongValue() {
        StringBuilder builder = new StringBuilder();
        for(int i = 0; i < 100000; i++) {
            builder.append("a");
        }
        String value = builder.toString();
        Verifier verifier = new Verifier(value);
        assertEquals(value, verifier.getValue());
    }
}