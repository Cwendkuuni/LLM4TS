import org.junit.Test;

public class VerifierTest {

    @Test
    public void testGetValue() {
        Verifier verifier = new Verifier("testValue");
        String value = verifier.getValue();
        assertEquals("testValue", value);
    }
    
}