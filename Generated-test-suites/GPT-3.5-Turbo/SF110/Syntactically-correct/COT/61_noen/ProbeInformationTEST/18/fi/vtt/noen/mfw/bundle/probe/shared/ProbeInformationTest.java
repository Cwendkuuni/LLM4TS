import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class ProbeInformationTest {

    @Test
    public void testGetTargetType() {
        ProbeInformation probeInformation = new ProbeInformation("targetName", "targetType", "bmClass", "bmName", "bmDescription", "probeDescription", 10, "xmlRpcUrl");
        assertEquals("targetType", probeInformation.getTargetType());
    }
    
    @Test
    public void testGetBmName() {
        ProbeInformation probeInformation = new ProbeInformation("targetName", "targetType", "bmClass", "bmName", "bmDescription", "probeDescription", 10, "xmlRpcUrl");
        assertEquals("bmName", probeInformation.getBmName());
    }
    
    @Test
    public void testGetProbeName() {
        ProbeInformation probeInformation = new ProbeInformation("targetName", "targetType", "bmClass", "bmName", "bmDescription", "probeDescription", 10, "xmlRpcUrl");
        assertEquals("probeDescription", probeInformation.getProbeName());
    }
    
    @Test
    public void testGetTargetName() {
        ProbeInformation probeInformation = new ProbeInformation("targetName", "targetType", "bmClass", "bmName", "bmDescription", "probeDescription", 10, "xmlRpcUrl");
        assertEquals("targetName", probeInformation.getTargetName());
    }
    
    @Test
    public void testGetBmClass() {
        ProbeInformation probeInformation = new ProbeInformation("targetName", "targetType", "bmClass", "bmName", "bmDescription", "probeDescription", 10, "xmlRpcUrl");
        assertEquals("bmClass", probeInformation.getBmClass());
    }
    
    @Test
    public void testGetBmDescription() {
        ProbeInformation probeInformation = new ProbeInformation("targetName", "targetType", "bmClass", "bmName", "bmDescription", "probeDescription", 10, "xmlRpcUrl");
        assertEquals("bmDescription", probeInformation.getBmDescription());
    }
    
    @Test
    public void testGetPrecision() {
        ProbeInformation probeInformation = new ProbeInformation("targetName", "targetType", "bmClass", "bmName", "bmDescription", "probeDescription", 10, "xmlRpcUrl");
        assertEquals(10, probeInformation.getPrecision());
    }
    
    @Test
    public void testGetMeasureURI() {
        ProbeInformation probeInformation = new ProbeInformation("targetName", "targetType", "bmClass", "bmName", "bmDescription", "probeDescription", 10, "xmlRpcUrl");
        assertEquals("expectedMeasureURI", probeInformation.getMeasureURI());
    }
    
    @Test
    public void testGetXmlRpcUrl() {
        ProbeInformation probeInformation = new ProbeInformation("targetName", "targetType", "bmClass", "bmName", "bmDescription", "probeDescription", 10, "xmlRpcUrl");
        assertEquals("xmlRpcUrl", probeInformation.getXmlRpcUrl());
    }
    
    @Test
    public void testToString() {
        ProbeInformation probeInformation = new ProbeInformation("targetName", "targetType", "bmClass", "bmName", "bmDescription", "probeDescription", 10, "xmlRpcUrl");
        assertNotNull(probeInformation.toString());
    }
    
    @Test
    public void testEquals() {
        ProbeInformation probeInformation1 = new ProbeInformation("targetName", "targetType", "bmClass", "bmName", "bmDescription", "probeDescription", 10, "xmlRpcUrl");
        ProbeInformation probeInformation2 = new ProbeInformation("targetName", "targetType", "bmClass", "bmName", "bmDescription", "probeDescription", 10, "xmlRpcUrl");
        assertEquals(probeInformation1, probeInformation2);
    }
    
    @Test
    public void testHashCode() {
        ProbeInformation probeInformation = new ProbeInformation("targetName", "targetType", "bmClass", "bmName", "bmDescription", "probeDescription", 10, "xmlRpcUrl");
        assertNotNull(probeInformation.hashCode());
    }
}

