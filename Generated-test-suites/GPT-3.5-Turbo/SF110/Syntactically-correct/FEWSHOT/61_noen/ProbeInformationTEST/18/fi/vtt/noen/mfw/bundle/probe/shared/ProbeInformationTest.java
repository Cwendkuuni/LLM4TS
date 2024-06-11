package fi.vtt.noen.mfw.bundle.probe.shared;

import static org.junit.Assert.*;

import org.junit.Test;

public class ProbeInformationTest {
    @Test
    public void testGetTargetType() {
        ProbeInformation probeInfo = new ProbeInformation("target", "type", "bmClass", "bmName", "bmDescription", "probeName", 5, "xmlRpcUrl");
        assertEquals("type", probeInfo.getTargetType());
    }
    
    @Test
    public void testGetBmName() {
        ProbeInformation probeInfo = new ProbeInformation("target", "type", "bmClass", "bmName", "bmDescription", "probeName", 5, "xmlRpcUrl");
        assertEquals("bmName", probeInfo.getBmName());
    }
    
    @Test
    public void testGetProbeName() {
        ProbeInformation probeInfo = new ProbeInformation("target", "type", "bmClass", "bmName", "bmDescription", "probeName", 5, "xmlRpcUrl");
        assertEquals("probeName", probeInfo.getProbeName());
    }
    
    @Test
    public void testGetTargetName() {
        ProbeInformation probeInfo = new ProbeInformation("target", "type", "bmClass", "bmName", "bmDescription", "probeName", 5, "xmlRpcUrl");
        assertEquals("target", probeInfo.getTargetName());
    }
    
    @Test
    public void testGetBmClass() {
        ProbeInformation probeInfo = new ProbeInformation("target", "type", "bmClass", "bmName", "bmDescription", "probeName", 5, "xmlRpcUrl");
        assertEquals("bmClass", probeInfo.getBmClass());
    }
    
    @Test
    public void testGetBmDescription() {
        ProbeInformation probeInfo = new ProbeInformation("target", "type", "bmClass", "bmName", "bmDescription", "probeName", 5, "xmlRpcUrl");
        assertEquals("bmDescription", probeInfo.getBmDescription());
    }
    
    @Test
    public void testGetPrecision() {
        ProbeInformation probeInfo = new ProbeInformation("target", "type", "bmClass", "bmName", "bmDescription", "probeName", 5, "xmlRpcUrl");
        assertEquals(5, probeInfo.getPrecision());
    }
    
    @Test
    public void testGetMeasureURI() {
        ProbeInformation probeInfo = new ProbeInformation("target", "type", "bmClass", "bmName", "bmDescription", "probeName", 5, "xmlRpcUrl");
        assertEquals("measureURI", probeInfo.getMeasureURI());
    }
    
    @Test
    public void testGetXmlRpcUrl() {
        ProbeInformation probeInfo = new ProbeInformation("target", "type", "bmClass", "bmName", "bmDescription", "probeName", 5, "xmlRpcUrl");
        assertEquals("xmlRpcUrl", probeInfo.getXmlRpcUrl());
    }
    
    @Test
    public void testToString() {
        ProbeInformation probeInfo = new ProbeInformation("target", "type", "bmClass", "bmName", "bmDescription", "probeName", 5, "xmlRpcUrl");
        assertEquals("ProbeInformation{targetName='target', targetType='type', bmClass='bmClass', bmName='bmName', bmDescription='bmDescription', probeDescription='probeName', precision=5}", probeInfo.toString());
    }
    
    @Test
    public void testEquals() {
        ProbeInformation probeInfo1 = new ProbeInformation("target", "type", "bmClass", "bmName", "bmDescription", "probeName", 5, "xmlRpcUrl");
        ProbeInformation probeInfo2 = new ProbeInformation("target", "type", "bmClass", "bmName", "bmDescription", "probeName", 5, "xmlRpcUrl");
        ProbeInformation probeInfo3 = new ProbeInformation("target", "type", "bmClass", "bmName", "bmDescription", "probeName", 6, "xmlRpcUrl");
        assertTrue(probeInfo1.equals(probeInfo2));
        assertFalse(probeInfo1.equals(probeInfo3));
    }
    
    @Test
    public void testHashCode() {
        ProbeInformation probeInfo1 = new ProbeInformation("target", "type", "bmClass", "bmName", "bmDescription", "probeName", 5, "xmlRpcUrl");
        ProbeInformation probeInfo2 = new ProbeInformation("target", "type", "bmClass", "bmName", "bmDescription", "probeName", 5, "xmlRpcUrl");
        assertTrue(probeInfo1.hashCode() == probeInfo2.hashCode());
    }
}