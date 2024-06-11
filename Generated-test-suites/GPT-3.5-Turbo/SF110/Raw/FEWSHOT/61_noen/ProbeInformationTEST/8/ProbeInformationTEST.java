package fi.vtt.noen.mfw.bundle.probe.shared;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class ProbeInformationTest {

    @Test
    public void testConstructorAndGetMethods() {
        ProbeInformation probeInformation = new ProbeInformation("target", "type", "class", "name", "description", "probe", 1, "xml");
        assertEquals("target", probeInformation.getTargetName());
        assertEquals("type", probeInformation.getTargetType());
        assertEquals("class", probeInformation.getBmClass());
        assertEquals("name", probeInformation.getBmName());
        assertEquals("description", probeInformation.getBmDescription());
        assertEquals("probe", probeInformation.getProbeName());
        assertEquals(1, probeInformation.getPrecision());
        assertEquals("xml", probeInformation.getXmlRpcUrl());
    }
    
    @Test
    public void testGetMeasureURI() {
        ProbeInformation probeInformation = new ProbeInformation("target", "type", "class", "name", "description", "probe", 1, "xml");
        assertEquals("measureURI", probeInformation.getMeasureURI());
    }
    
    @Test
    public void testToString() {
        ProbeInformation probeInformation = new ProbeInformation("target", "type", "class", "name", "description", "probe", 1, "xml");
        assertEquals("Expected toString() output", probeInformation.toString());
    }
    
    @Test
    public void testEquals() {
        ProbeInformation probe1 = new ProbeInformation("target", "type", "class", "name", "description", "probe", 1, "xml");
        ProbeInformation probe2 = new ProbeInformation("target", "type", "class", "name", "description", "probe", 1, "xml");
        ProbeInformation probe3 = new ProbeInformation("target1", "type", "class", "name", "description", "probe", 1, "xml");
        
        assertEquals(probe1, probe1);
        assertNotEquals(probe1, null);
        assertNotEquals(probe1, new Object());
        assertEquals(probe1, probe2);
        assertNotEquals(probe1, probe3);
    }
    
    @Test
    public void testHashCode() {
        ProbeInformation probe1 = new ProbeInformation("target", "type", "class", "name", "description", "probe", 1, "xml");
        ProbeInformation probe2 = new ProbeInformation("target", "type", "class", "name", "description", "probe", 1, "xml");
        
        assertEquals(probe1.hashCode(), probe2.hashCode());
    }
    
}