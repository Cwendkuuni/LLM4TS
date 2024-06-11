
package fi.vtt.noen.mfw.bundle.probe.shared;

import org.junit.Test;
import static org.junit.Assert.*;

public class ProbeInformationTest {

    @Test
    public void testConstructorAndGetters() {
        ProbeInformation probeInfo = new ProbeInformation("targetName", "targetType", "bmClass", "bmName", "bmDescription", "probeDescription", 5, "xmlRpcUrl");
        
        assertEquals("targetName", probeInfo.getTargetName());
        assertEquals("targetType", probeInfo.getTargetType());
        assertEquals("bmClass", probeInfo.getBmClass());
        assertEquals("bmName", probeInfo.getBmName());
        assertEquals("bmDescription", probeInfo.getBmDescription());
        assertEquals("probeDescription", probeInfo.getProbeName());
        assertEquals(5, probeInfo.getPrecision());
        assertEquals("xmlRpcUrl", probeInfo.getXmlRpcUrl());
    }
    
    @Test
    public void testGetMeasureURI() {
        ProbeInformation probeInfo = new ProbeInformation("targetName", "targetType", "bmClass", "bmName", "bmDescription", "probeDescription", 5, "xmlRpcUrl");
        
        String expectedMeasureURI = "targetType:targetName:bmClass:bmName";
        assertEquals(expectedMeasureURI, probeInfo.getMeasureURI());
    }
    
    @Test
    public void testToString() {
        ProbeInformation probeInfo = new ProbeInformation("targetName", "targetType", "bmClass", "bmName", "bmDescription", "probeDescription", 5, "xmlRpcUrl");
        
        String expectedString = "ProbeInformation{targetName='targetName', targetType='targetType', bmClass='bmClass', bmName='bmName', bmDescription='bmDescription', probeDescription='probeDescription', precision=5}";
        assertEquals(expectedString, probeInfo.toString());
    }
    
    @Test
    public void testEquals() {
        ProbeInformation probeInfo1 = new ProbeInformation("targetName", "targetType", "bmClass", "bmName", "bmDescription", "probeDescription", 5, "xmlRpcUrl");
        ProbeInformation probeInfo2 = new ProbeInformation("targetName", "targetType", "bmClass", "bmName", "bmDescription", "probeDescription", 5, "xmlRpcUrl");
        ProbeInformation probeInfo3 = new ProbeInformation("otherTargetName", "targetType", "bmClass", "bmName", "bmDescription", "probeDescription", 5, "xmlRpcUrl");
        
        assertTrue(probeInfo1.equals(probeInfo2));
        assertFalse(probeInfo1.equals(probeInfo3));
    }
    
    @Test
    public void testHashCode() {
        ProbeInformation probeInfo1 = new ProbeInformation("targetName", "targetType", "bmClass", "bmName", "bmDescription", "probeDescription", 5, "xmlRpcUrl");
        ProbeInformation probeInfo2 = new ProbeInformation("targetName", "targetType", "bmClass", "bmName", "bmDescription", "probeDescription", 5, "xmlRpcUrl");
        ProbeInformation probeInfo3 = new ProbeInformation("otherTargetName", "targetType", "bmClass", "bmName", "bmDescription", "probeDescription", 5, "xmlRpcUrl");
        
        assertEquals(probeInfo1.hashCode(), probeInfo2.hashCode());
        assertNotEquals(probeInfo1.hashCode(), probeInfo3.hashCode());
    }

}
