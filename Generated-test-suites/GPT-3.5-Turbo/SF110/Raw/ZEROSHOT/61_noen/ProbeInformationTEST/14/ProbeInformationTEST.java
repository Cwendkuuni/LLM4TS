package fi.vtt.noen.mfw.bundle.probe.shared;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class ProbeInformationTest {

    private ProbeInformation probeInfo;

    @Before
    public void setUp() {
        probeInfo = new ProbeInformation("TargetName", "TargetType", "BmClass", "BmName", "BmDescription", "ProbeDescription", 10, "XmlRpcUrl");
    }
    
    @Test
    public void testGetTargetType() {
        assertEquals("TargetType", probeInfo.getTargetType());
    }
    
    @Test
    public void testGetBmName() {
        assertEquals("BmName", probeInfo.getBmName());
    }
    
    @Test
    public void testGetProbeName() {
        assertEquals("ProbeDescription", probeInfo.getProbeName());
    }
    
    @Test
    public void testGetTargetName() {
        assertEquals("TargetName", probeInfo.getTargetName());
    }
    
    @Test
    public void testGetBmClass() {
        assertEquals("BmClass", probeInfo.getBmClass());
    }
    
    @Test
    public void testGetBmDescription() {
        assertEquals("BmDescription", probeInfo.getBmDescription());
    }
    
    @Test
    public void testGetPrecision() {
        assertEquals(10, probeInfo.getPrecision());
    }
    
    @Test
    public void testGetMeasureURI() {
        String expected = "TargetType_TargetName_BmClass_BmName";
        assertEquals(expected, probeInfo.getMeasureURI());
    }
    
    @Test
    public void testGetXmlRpcUrl() {
        assertEquals("XmlRpcUrl", probeInfo.getXmlRpcUrl());
    }
    
    @Test
    public void testEquals_sameObject() {
        assertTrue(probeInfo.equals(probeInfo));
    }
    
    @Test
    public void testEquals_nullObject() {
        assertFalse(probeInfo.equals(null));
    }
    
    @Test
    public void testEquals_differentClass() {
        assertFalse(probeInfo.equals("string"));
    }
    
    @Test
    public void testEquals_equalObjects() {
        ProbeInformation probeInfo2 = new ProbeInformation("TargetName", "TargetType", "BmClass", "BmName", "BmDescription", "ProbeDescription", 10, "XmlRpcUrl");
        assertTrue(probeInfo.equals(probeInfo2));
    }
    
    @Test
    public void testEquals_unequalObjects() {
        ProbeInformation probeInfo2 = new ProbeInformation("DifferentName", "TargetType", "DifferentClass", "BmName", "BmDescription", "DifferentDescription", 5, "DifferentXmlRpcUrl");
        assertFalse(probeInfo.equals(probeInfo2));
    }
    
    @Test
    public void testHashCode() {
        ProbeInformation probeInfo2 = new ProbeInformation("TargetName", "TargetType", "BmClass", "BmName", "BmDescription", "ProbeDescription", 10, "XmlRpcUrl");
        assertEquals(probeInfo.hashCode(), probeInfo2.hashCode());
    }
}