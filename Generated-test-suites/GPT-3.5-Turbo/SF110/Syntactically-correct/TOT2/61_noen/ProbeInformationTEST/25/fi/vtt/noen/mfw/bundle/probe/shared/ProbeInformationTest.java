package fi.vtt.noen.mfw.bundle.probe.shared;

import org.junit.Test;
import static org.junit.Assert.*;

public class ProbeInformationTest {
    
    @Test
    public void testGetTargetType() {
        ProbeInformation probeInfo = new ProbeInformation("target1", "type1", "class1", "name1", "desc1", "probe1", 5, "http://example.com");
        assertEquals("type1", probeInfo.getTargetType());
    }
    
    @Test
    public void testGetBmName() {
        ProbeInformation probeInfo = new ProbeInformation("target1", "type1", "class1", "name1", "desc1", "probe1", 5, "http://example.com");
        assertEquals("name1", probeInfo.getBmName());
    }
    
    @Test
    public void testGetProbeName() {
        ProbeInformation probeInfo = new ProbeInformation("target1", "type1", "class1", "name1", "desc1", "probe1", 5, "http://example.com");
        assertEquals("probe1", probeInfo.getProbeName());
    }
    
    @Test
    public void testGetTargetName() {
        ProbeInformation probeInfo = new ProbeInformation("target1", "type1", "class1", "name1", "desc1", "probe1", 5, "http://example.com");
        assertEquals("target1", probeInfo.getTargetName());
    }
    
    @Test
    public void testGetBmClass() {
        ProbeInformation probeInfo = new ProbeInformation("target1", "type1", "class1", "name1", "desc1", "probe1", 5, "http://example.com");
        assertEquals("class1", probeInfo.getBmClass());
    }
    
    @Test
    public void testGetBmDescription() {
        ProbeInformation probeInfo = new ProbeInformation("target1", "type1", "class1", "name1", "desc1", "probe1", 5, "http://example.com");
        assertEquals("desc1", probeInfo.getBmDescription());
    }
    
    @Test
    public void testGetPrecision() {
        ProbeInformation probeInfo = new ProbeInformation("target1", "type1", "class1", "name1", "desc1", "probe1", 5, "http://example.com");
        assertEquals(5, probeInfo.getPrecision());
    }
    
    @Test
    public void testGetMeasureURI() {
        ProbeInformation probeInfo = new ProbeInformation("target1", "type1", "class1", "name1", "desc1", "probe1", 5, "http://example.com");
        assertEquals("/type1/target1/class1/name1", probeInfo.getMeasureURI());
    }
    
    @Test
    public void testGetXmlRpcUrl() {
        ProbeInformation probeInfo = new ProbeInformation("target1", "type1", "class1", "name1", "desc1", "probe1", 5, "http://example.com");
        assertEquals("http://example.com", probeInfo.getXmlRpcUrl());
    }
    
    // Note: Exception handling scenarios and edge cases should also be tested
    
}