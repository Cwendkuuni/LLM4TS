
package fi.vtt.noen.mfw.bundle.probe.shared;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class ProbeInformationTest {
    @Test
    public void testGetTargetType() {
        ProbeInformation probe = new ProbeInformation("targetName", "targetType", "bmClass", "bmName", "bmDescription", "probeDescription", 10, "xmlRpcUrl");
        assertEquals("targetType", probe.getTargetType());
    }
    
    @Test
    public void testGetBmName() {
        ProbeInformation probe = new ProbeInformation("targetName", "targetType", "bmClass", "bmName", "bmDescription", "probeDescription", 10, "xmlRpcUrl");
        assertEquals("bmName", probe.getBmName());
    }
    
    @Test
    public void testGetProbeName() {
        ProbeInformation probe = new ProbeInformation("targetName", "targetType", "bmClass", "bmName", "bmDescription", "probeDescription", 10, "xmlRpcUrl");
        assertEquals("probeDescription", probe.getProbeName());
    }
    
    @Test
    public void testGetTargetName() {
        ProbeInformation probe = new ProbeInformation("targetName", "targetType", "bmClass", "bmName", "bmDescription", "probeDescription", 10, "xmlRpcUrl");
        assertEquals("targetName", probe.getTargetName());
    }
    
    @Test
    public void testGetBmClass() {
        ProbeInformation probe = new ProbeInformation("targetName", "targetType", "bmClass", "bmName", "bmDescription", "probeDescription", 10, "xmlRpcUrl");
        assertEquals("bmClass", probe.getBmClass());
    }
    
    @Test
    public void testGetBmDescription() {
        ProbeInformation probe = new ProbeInformation("targetName", "targetType", "bmClass", "bmName", "bmDescription", "probeDescription", 10, "xmlRpcUrl");
        assertEquals("bmDescription", probe.getBmDescription());
    }
    
    @Test
    public void testGetPrecision() {
        ProbeInformation probe = new ProbeInformation("targetName", "targetType", "bmClass", "bmName", "bmDescription", "probeDescription", 10, "xmlRpcUrl");
        assertEquals(10, probe.getPrecision());
    }
    
    @Test
    public void testGetXmlRpcUrl() {
        ProbeInformation probe = new ProbeInformation("targetName", "targetType", "bmClass", "bmName", "bmDescription", "probeDescription", 10, "xmlRpcUrl");
        assertEquals("xmlRpcUrl", probe.getXmlRpcUrl());
    }
    
    @Test
    public void testGetMeasureURI() {
        ProbeInformation probe = new ProbeInformation("targetName", "targetType", "bmClass", "bmName", "bmDescription", "probeDescription", 10, "xmlRpcUrl");
        assertEquals("expectedMeasureURI", probe.getMeasureURI());
    }
    
    @Test
    public void testToString() {
        ProbeInformation probe = new ProbeInformation("targetName", "targetType", "bmClass", "bmName", "bmDescription", "probeDescription", 10, "xmlRpcUrl");
        String expectedString = "ProbeInformation{targetName='targetName', targetType='targetType', bmClass='bmClass', bmName='bmName', bmDescription='bmDescription', probeDescription='probeDescription', precision=10}";
        assertEquals(expectedString, probe.toString());
    }
    
    @Test
    public void testEquals() {
        ProbeInformation probe1 = new ProbeInformation("targetName1", "targetType1", "bmClass1", "bmName1", "bmDescription1", "probeDescription1", 10, "xmlRpcUrl1");
        ProbeInformation probe2 = new ProbeInformation("targetName2", "targetType2", "bmClass2", "bmName2", "bmDescription2", "probeDescription2", 10, "xmlRpcUrl2");
        ProbeInformation probe3 = new ProbeInformation("targetName1", "targetType1", "bmClass1", "bmName1", "bmDescription1", "probeDescription1", 10, "xmlRpcUrl1");
        ProbeInformation probe4 = new ProbeInformation("targetName1", "targetType1", "bmClass1", "bmName1", "bmDescription1", "probeDescription1", 20, "xmlRpcUrl1");
        
        assertTrue(probe1.equals(probe3)); // Reflexive
        assertTrue(probe3.equals(probe1)); // Symmetric
        assertTrue(probe1.equals(probe1)); // Consistent
        assertTrue(probe1.equals(probe3)); // Transitive
        
        assertTrue(!probe1.equals(probe2));
        assertTrue(!probe2.equals(probe3));
        assertTrue(!probe1.equals(probe4));
    }
    
    @Test
    public void testHashCode() {
        ProbeInformation probe1 = new ProbeInformation("targetName1", "targetType1", "bmClass1", "bmName1", "bmDescription1", "probeDescription1", 10, "xmlRpcUrl1");
        ProbeInformation probe2 = new ProbeInformation("targetName2", "targetType2", "bmClass2", "bmName2", "bmDescription2", "probeDescription2", 10, "xmlRpcUrl2");
        
        assertTrue(probe1.hashCode() != probe2.hashCode());
    }
}
