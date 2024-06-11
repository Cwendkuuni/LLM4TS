import org.junit.Test;
import static org.junit.Assert.*;

public class ProbeInformationTest {

    @Test
    public void testGetTargetType() {
        ProbeInformation probeInformation = new ProbeInformation("Target", "Type", "bmClass", "bmName", "bmDescription", "probeDescription", 5, "xmlRpcUrl");
        
        assertEquals("Type", probeInformation.getTargetType());
    }
    
    @Test
    public void testGetBmName() {
        ProbeInformation probeInformation = new ProbeInformation("Target", "Type", "bmClass", "bmName", "bmDescription", "probeDescription", 5, "xmlRpcUrl");
        
        assertEquals("bmName", probeInformation.getBmName());
    }
    
    @Test
    public void testGetProbeName() {
        ProbeInformation probeInformation = new ProbeInformation("Target", "Type", "bmClass", "bmName", "bmDescription", "probeDescription", 5, "xmlRpcUrl");
        
        assertEquals("probeDescription", probeInformation.getProbeName());
    }
    
    @Test
    public void testGetTargetName() {
        ProbeInformation probeInformation = new ProbeInformation("Target", "Type", "bmClass", "bmName", "bmDescription", "probeDescription", 5, "xmlRpcUrl");
        
        assertEquals("Target", probeInformation.getTargetName());
    }
    
    @Test
    public void testGetBmClass() {
        ProbeInformation probeInformation = new ProbeInformation("Target", "Type", "bmClass", "bmName", "bmDescription", "probeDescription", 5, "xmlRpcUrl");
        
        assertEquals("bmClass", probeInformation.getBmClass());
    }
    
    @Test
    public void testGetBmDescription() {
        ProbeInformation probeInformation = new ProbeInformation("Target", "Type", "bmClass", "bmName", "bmDescription", "probeDescription", 5, "xmlRpcUrl");
        
        assertEquals("bmDescription", probeInformation.getBmDescription());
    }
    
    @Test
    public void testGetPrecision() {
        ProbeInformation probeInformation = new ProbeInformation("Target", "Type", "bmClass", "bmName", "bmDescription", "probeDescription", 5, "xmlRpcUrl");
        
        assertEquals(5, probeInformation.getPrecision());
    }
    
    @Test
    public void testGetMeasureURI() {
        ProbeInformation probeInformation = new ProbeInformation("Target", "Type", "bmClass", "bmName", "bmDescription", "probeDescription", 5, "xmlRpcUrl");
        
        assertEquals("Type:Target:bmClass:bmName", probeInformation.getMeasureURI());
    }
    
    @Test
    public void testGetXmlRpcUrl() {
        ProbeInformation probeInformation = new ProbeInformation("Target", "Type", "bmClass", "bmName", "bmDescription", "probeDescription", 5, "xmlRpcUrl");
        
        assertEquals("xmlRpcUrl", probeInformation.getXmlRpcUrl());
    }
    
    @Test
    public void testEquals() {
        ProbeInformation probeInformation1 = new ProbeInformation("Target", "Type", "bmClass", "bmName", "bmDescription", "probeDescription", 5, "xmlRpcUrl");
        ProbeInformation probeInformation2 = new ProbeInformation("Target", "Type", "bmClass", "bmName", "bmDescription", "probeDescription", 5, "xmlRpcUrl");
        ProbeInformation probeInformation3 = new ProbeInformation("DifferentTarget", "Type", "bmClass", "bmName", "bmDescription", "probeDescription", 5, "xmlRpcUrl");
        
        assertEquals(probeInformation1, probeInformation2); // Same objects should be equal
        assertNotEquals(probeInformation1, probeInformation3); // Different objects with different target names should not be equal
    }
    
    @Test
    public void testHashCode() {
        ProbeInformation probeInformation1 = new ProbeInformation("Target", "Type", "bmClass", "bmName", "bmDescription", "probeDescription", 5, "xmlRpcUrl");
        ProbeInformation probeInformation2 = new ProbeInformation("Target", "Type", "bmClass", "bmName", "bmDescription", "probeDescription", 5, "xmlRpcUrl");
        
        assertEquals(probeInformation1.hashCode(), probeInformation2.hashCode()); // Same objects should have the same hash code
    }

}