
import org.junit.Test;
import static org.junit.Assert.*;

public class ProbeInformationTest {
    private ProbeInformation probeInformation;

    @Test
    public void testGetTargetType() {
        probeInformation = new ProbeInformation("targetName", "targetType", "bmClass", "bmName", "bmDescription", "probeDescription", 1, "xmlRpcUrl");
        assertEquals("targetType", probeInformation.getTargetType());
    }
    
    @Test
    public void testGetBmName() {
        // Write your test case here
    }
    
    @Test
    public void testGetProbeName() {
        // Write your test case here
    }
    
    @Test
    public void testGetTargetName() {
        // Write your test case here
    }
    
    @Test
    public void testGetBmClass() {
        // Write your test case here
    }
    
    @Test
    public void testGetBmDescription() {
        // Write your test case here
    }
    
    @Test
    public void testGetPrecision() {
        // Write your test case here
    }
    
    @Test
    public void testGetMeasureURI() {
        // Write your test case here
    }
    
    @Test
    public void testGetXmlRpcUrl() {
        // Write your test case here
    }
    
    @Test
    public void testToString() {
        // Write your test case here
    }
    
    @Test
    public void testEquals() {
        // Write your test case here
    }
    
    @Test
    public void testHashCode() {
        // Write your test case here
    }
    
    @Test
    public void testEqualsWithNull() {
        probeInformation = new ProbeInformation("targetName", "targetType", "bmClass", "bmName", "bmDescription", "probeDescription", 1, "xmlRpcUrl");
        assertFalse(probeInformation.equals(null));
    }
    
    
}
