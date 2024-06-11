import org.junit.Test;

public class VerifierTest {
    
    @Test
    public void testGetValue() {
        String value = "test";
        Verifier verifier = new Verifier(value);
        
        String retrievedValue = verifier.getValue();
        
        assertEquals(value, retrievedValue);
    }

    @Test
    public void testGetValueWithNull() {
        Verifier verifier = new Verifier(null);
        
        String retrievedValue = verifier.getValue();
        
        assertNull(retrievedValue);
    }
}