package fi.vtt.noen.mfw.bundle.probe.shared;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class ProbeInformationTest {
    
    @Test
    public void testConstructorAndGetters() {
        ProbeInformation probeInfo = new ProbeInformation("target1", "type1", "bmClass1", "bmName1", "bmDescription1", "probeDescription1", 1, "xmlRpcUrl1");
        
        assertEquals("target1", probeInfo.getTargetName());
        assertEquals("type1", probeInfo.getTargetType());
        assertEquals("bmClass1", probeInfo.getBmClass());
        assertEquals("bmName1", probeInfo.getBmName());
        assertEquals("bmDescription1", probeInfo.getBmDescription());
        assertEquals("probeDescription1", probeInfo.getProbeName());
        assertEquals(1, probeInfo.getPrecision());
        assertEquals("xmlRpcUrl1", probeInfo.getXmlRpcUrl());
    }
    
    @Test
    public void testGetMeasureURI() {
        ProbeInformation probeInfo = new ProbeInformation("target1", "type1", "bmClass1", "bmName1", "bmDescription1", "probeDescription1", 1, "xmlRpcUrl1");
        
        assertEquals("type1_target1_bmClass1_bmName1", probeInfo.getMeasureURI());
    }
    
    @Test
    public void testToString() {
        ProbeInformation probeInfo = new ProbeInformation("target1", "type1", "bmClass1", "bmName1", "bmDescription1", "probeDescription1", 1, "xmlRpcUrl1");
        
        String expected = "ProbeInformation{targetName='target1', targetType='type1', bmClass='bmClass1', bmName='bmName1', bmDescription='bmDescription1', probeDescription='probeDescription1', precision=1}";
        assertEquals(expected, probeInfo.toString());
    }
    
    @Test
    public void testEquals() {
        ProbeInformation probeInfo1 = new ProbeInformation("target1", "type1", "bmClass1", "bmName1", "bmDescription1", "probeDescription1", 1, "xmlRpcUrl1");
        
        ProbeInformation probeInfo2 = new ProbeInformation("target1", "type1", "bmClass1", "bmName1", "bmDescription1", "probeDescription1", 1, "xmlRpcUrl1");
        assertEquals(probeInfo1, probeInfo2);
        
        ProbeInformation probeInfo3 = new ProbeInformation("target2", "type1", "bmClass1", "bmName1", "bmDescription1", "probeDescription1", 1, "xmlRpcUrl1");
        assertNotEquals(probeInfo1, probeInfo3);
        
        ProbeInformation probeInfo4 = new ProbeInformation("target1", "type2", "bmClass1", "bmName1", "bmDescription1", "probeDescription1", 1, "xmlRpcUrl1");
        assertNotEquals(probeInfo1, probeInfo4);
        
        ProbeInformation probeInfo5 = new ProbeInformation("target1", "type1", "bmClass2", "bmName1", "bmDescription1", "probeDescription1", 1, "xmlRpcUrl1");
        assertNotEquals(probeInfo1, probeInfo5);
        
        ProbeInformation probeInfo6 = new ProbeInformation("target1", "type1", "bmClass1", "bmName2", "bmDescription1", "probeDescription1", 1, "xmlRpcUrl1");
        assertNotEquals(probeInfo1, probeInfo6);
        
        ProbeInformation probeInfo7 = new ProbeInformation("target1", "type1", "bmClass1", "bmName1", "bmDescription2", "probeDescription1", 1, "xmlRpcUrl1");
        assertNotEquals(probeInfo1, probeInfo7);
        
        ProbeInformation probeInfo8 = new ProbeInformation("target1", "type1", "bmClass1", "bmName1", "bmDescription1", "probeDescription2", 1, "xmlRpcUrl1");
        assertNotEquals(probeInfo1, probeInfo8);
        
        ProbeInformation probeInfo9 = new ProbeInformation("target1", "type1", "bmClass1", "bmName1", "bmDescription1", "probeDescription1", 2, "xmlRpcUrl1");
        assertNotEquals(probeInfo1, probeInfo9);
        
        ProbeInformation probeInfo10 = new ProbeInformation("target1", "type1", "bmClass1", "bmName1", "bmDescription1", "probeDescription1", 1, "xmlRpcUrl2");
        assertNotEquals(probeInfo1, probeInfo10);
    }
    
    @Test
    public void testHashCode() {
        ProbeInformation probeInfo1 = new ProbeInformation("target1", "type1", "bmClass1", "bmName1", "bmDescription1", "probeDescription1", 1, "xmlRpcUrl1");
        ProbeInformation probeInfo2 = new ProbeInformation("target1", "type1", "bmClass1", "bmName1", "bmDescription1", "probeDescription1", 1, "xmlRpcUrl1");
        
        assertEquals(probeInfo1.hashCode(), probeInfo2.hashCode());
    }
}