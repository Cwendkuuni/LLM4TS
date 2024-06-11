import org.junit.Test;
import static org.junit.Assert.*;

public class ProbeInformationTest {
    
    ProbeInformation probe = new ProbeInformation("target", "type", "class", "name", "description", "probe", 5, "url");
    
    @Test
    public void testGetTargetType() {
        assertEquals("type", probe.getTargetType());
    }
    
    // Additional test cases for other methods can be added here
}