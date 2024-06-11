package fi.vtt.noen.mfw.bundle.probe.shared;
import static org.junit.Assert.*;
import org.junit.Test;

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
        assertEquals("type/target/class/name", probe.getMeasureURI());
    }
    
    @Test
    public void testGetXmlRpcUrl() {
        ProbeInformation probe = new ProbeInformation("target", "type", "class", "name", "description", "probe", 1, "url");
        assertEquals("url", probe.getXmlRpcUrl());
    }
    
    // Test equals method
    @Test
    public void testEquals() {
        ProbeInformation probe1 = new ProbeInformation("target", "type", "class", "name", "description", "probe", 1, "url");
        ProbeInformation probe2 = new ProbeInformation("target", "type", "class", "name", "description", "probe", 1, "url");
        assertTrue(probe1.equals(probe2));
    }
    
    // Test hashCode method
    @Test
    public void testHashCode() {
        ProbeInformation probe1 = new ProbeInformation("target", "type", "class", "name", "description", "probe", 1, "url");
        ProbeInformation probe2 = new ProbeInformation("target", "type", "class", "name", "description", "probe", 1, "url");
        assertEquals(probe1.hashCode(), probe2.hashCode());
    }
}