import org.junit.Test;

import static org.junit.Assert.*;

public class ProbeInformationTest {
    
    @Test
    public void testGetTargetType() {
        ProbeInformation probe = new ProbeInformation("target", "type", "class", "name", "description", "probe", 1, "url");
        assertEquals("type", probe.getTargetType());
    }
    
    @Test
    public void testGetBmName() {
        ProbeInformation probe = new ProbeInformation("target", "type", "class", "name", "description", "probe", 1, "url");
        assertEquals("name", probe.getBmName());
    }
    
    @Test
    public void testGetProbeName() {
        ProbeInformation probe = new ProbeInformation("target", "type", "class", "name", "description", "probe", 1, "url");
        assertEquals("probe", probe.getProbeName());
    }
    
    @Test
    public void testGetTargetName() {
        ProbeInformation probe = new ProbeInformation("target", "type", "class", "name", "description", "probe", 1, "url");
        assertEquals("target", probe.getTargetName());
    }
    
    @Test
    public void testGetBmClass() {
        ProbeInformation probe = new ProbeInformation("target", "type", "class", "name", "description", "probe", 1, "url");
        assertEquals("class", probe.getBmClass());
    }
    
    @Test
    public void testGetBmDescription() {
        ProbeInformation probe = new ProbeInformation("target", "type", "class", "name", "description", "probe", 1, "url");
        assertEquals("description", probe.getBmDescription());
    }
    
    @Test
    public void testGetPrecision() {
        ProbeInformation probe = new ProbeInformation("target", "type", "class", "name", "description", "probe", 1, "url");
        assertEquals(1, probe.getPrecision());
    }
    
    @Test
    public void testGetMeasureURI() {
        ProbeInformation probe = new ProbeInformation("target", "type", "class", "name", "description", "probe", 1, "url");
        assertEquals("type#target#class#name", probe.getMeasureURI());
    }
    
    @Test
    public void testGetXmlRpcUrl() {
        ProbeInformation probe = new ProbeInformation("target", "type", "class", "name", "description", "probe", 1, "url");
        assertEquals("url", probe.getXmlRpcUrl());
    }
    
    @Test
    public void testToString() {
        ProbeInformation probe = new ProbeInformation("target", "type", "class", "name", "description", "probe", 1, "url");
        String expected = "ProbeInformation{targetName='target', targetType='type', bmClass='class', bmName='name', bmDescription='description', probeDescription='probe', precision=1}";
        assertEquals(expected, probe.toString());
    }
    
    @Test
    public void testEquals() {
        ProbeInformation probe1 = new ProbeInformation("target", "type", "class", "name", "description", "probe", 1, "url");
        ProbeInformation probe2 = new ProbeInformation("target", "type", "class", "name", "description", "probe", 1, "url");
        assertEquals(true, probe1.equals(probe2));
    }
    
    @Test
    public void testHashCode() {
        ProbeInformation probe = new ProbeInformation("target", "type", "class", "name", "description", "probe", 1, "url");
        int expected = 31 * (31 * (31 * (31 * (31 * (31 * (31 * 0 + "target".hashCode()) + "type".hashCode()) + "class".hashCode()) + "name".hashCode()) + "description".hashCode()) + "probe".hashCode()) + 1;
        assertEquals(expected, probe.hashCode());
    }
}