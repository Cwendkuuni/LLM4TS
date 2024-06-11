import org.junit.Test;
import org.scribe.model.Verifier;
import org.scribe.utils.Preconditions;

public class VerifierTest {

    @Test
    public void testConstructor() {
        String value = "testValue";
        Verifier verifier = new Verifier(value);
        
        Preconditions.checkNotNull(verifier.getValue(), "Verifier value is null");
        assert(verifier.getValue().equals(value));
    }
    
    @Test
    public void testGetValue() {
        String value = "testValue";
        Verifier verifier = new Verifier(value);
        
        assert(verifier.getValue().equals(value));
    }
}