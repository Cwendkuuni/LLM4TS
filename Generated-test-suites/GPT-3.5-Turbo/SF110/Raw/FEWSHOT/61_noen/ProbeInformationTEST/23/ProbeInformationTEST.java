package fi.vtt.noen.mfw.bundle.probe.shared;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class ProbeInformationTest {
    
    @Test
    public void testGetTargetType() {
        ProbeInformation probeInformation = new ProbeInformation("targetName", "targetType", "bmClass", "bmName", "bmDescription", "probeDescription", 5, "xmlRpcUrl");
        String targetType = probeInformation.getTargetType();
        assertEquals("targetType", targetType);
    }
    
    @Test
    public void testGetBmName() {
        ProbeInformation probeInformation = new ProbeInformation("targetName", "targetType", "bmClass", "bmName", "bmDescription", "probeDescription", 5, "xmlRpcUrl");
        String bmName = probeInformation.getBmName();
        assertEquals("bmName", bmName);
    }
    
    @Test
    public void testGetProbeName() {
        ProbeInformation probeInformation = new ProbeInformation("targetName", "targetType", "bmClass", "bmName", "bmDescription", "probeDescription", 5, "xmlRpcUrl");
        String probeName = probeInformation.getProbeName();
        assertEquals("probeDescription", probeName);
    }
    
    @Test
    public void testGetTargetName() {
        ProbeInformation probeInformation = new ProbeInformation("targetName", "targetType", "bmClass", "bmName", "bmDescription", "probeDescription", 5, "xmlRpcUrl");
        String targetName = probeInformation.getTargetName();
        assertEquals("targetName", targetName);
    }
    
    @Test
    public void testGetBmClass() {
        ProbeInformation probeInformation = new ProbeInformation("targetName", "targetType", "bmClass", "bmName", "bmDescription", "probeDescription", 5, "xmlRpcUrl");
        String bmClass = probeInformation.getBmClass();
        assertEquals("bmClass", bmClass);
    }
    
    @Test
    public void testGetBmDescription() {
        ProbeInformation probeInformation = new ProbeInformation("targetName", "targetType", "bmClass", "bmName", "bmDescription", "probeDescription", 5, "xmlRpcUrl");
        String bmDescription = probeInformation.getBmDescription();
        assertEquals("bmDescription", bmDescription);
    }
    
    @Test
    public void testGetPrecision() {
        ProbeInformation probeInformation = new ProbeInformation("targetName", "targetType", "bmClass", "bmName", "bmDescription", "probeDescription", 5, "xmlRpcUrl");
        int precision = probeInformation.getPrecision();
        assertEquals(5, precision);
    }
    
    @Test
    public void testGetMeasureURI() {
        ProbeInformation probeInformation = new ProbeInformation("targetName", "targetType", "bmClass", "bmName", "bmDescription", "probeDescription", 5, "xmlRpcUrl");
        String measureURI = probeInformation.getMeasureURI();
        assertEquals("measureURI", measureURI);
    }
    
    @Test
    public void testGetXmlRpcUrl() {
        ProbeInformation probeInformation = new ProbeInformation("targetName", "targetType", "bmClass", "bmName", "bmDescription", "probeDescription", 5, "xmlRpcUrl");
        String xmlRpcUrl = probeInformation.getXmlRpcUrl();
        assertEquals("xmlRpcUrl", xmlRpcUrl);
    }
    
    @Test
    public void testToString() {
        ProbeInformation probeInformation = new ProbeInformation("targetName", "targetType", "bmClass", "bmName", "bmDescription", "probeDescription", 5, "xmlRpcUrl");
        String str = probeInformation.toString();
        assertEquals("ProbeInformation{targetName='targetName', targetType='targetType', bmClass='bmClass', bmName='bmName', bmDescription='bmDescription', probeDescription='probeDescription', precision=5}", str);
    }
    
    @Test
    public void testEquals() {
        ProbeInformation probeInformation1 = new ProbeInformation("targetName", "targetType", "bmClass", "bmName", "bmDescription", "probeDescription", 5, "xmlRpcUrl");
        ProbeInformation probeInformation2 = new ProbeInformation("targetName", "targetType", "bmClass", "bmName", "bmDescription", "probeDescription", 5, "xmlRpcUrl");
        boolean equals = probeInformation1.equals(probeInformation2);
        assertEquals(true, equals);
    }
    
    @Test
    public void testHashCode() {
        ProbeInformation probeInformation = new ProbeInformation("targetName", "targetType", "bmClass", "bmName", "bmDescription", "probeDescription", 5, "xmlRpcUrl");
        int hashCode = probeInformation.hashCode();
        assertEquals(1234, hashCode);
    }
}