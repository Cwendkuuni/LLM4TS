package fi.vtt.noen.mfw.bundle.probe.shared;
import org.junit.Test;
import static org.junit.Assert.*;

public class ProbeInformationTest {

    @Test
    public void testGetTargetType() {
        ProbeInformation probeInfo = new ProbeInformation("target", "type", "class", "name", "description", "probe", 5, "url");
        assertEquals("type", probeInfo.getTargetType());
    }
    
    @Test
    public void testGetBmName() {
        ProbeInformation probeInfo = new ProbeInformation("target", "type", "class", "name", "description", "probe", 5, "url");
        assertEquals("name", probeInfo.getBmName());
    }
    
    @Test
    public void testGetProbeName() {
        ProbeInformation probeInfo = new ProbeInformation("target", "type", "class", "name", "description", "probe", 5, "url");
        assertEquals("probe", probeInfo.getProbeName());
    }
    
    @Test
    public void testGetTargetName() {
        ProbeInformation probeInfo = new ProbeInformation("target", "type", "class", "name", "description", "probe", 5, "url");
        assertEquals("target", probeInfo.getTargetName());
    }
    
    @Test
    public void testGetBmClass() {
        ProbeInformation probeInfo = new ProbeInformation("target", "type", "class", "name", "description", "probe", 5, "url");
        assertEquals("class", probeInfo.getBmClass());
    }
    
    @Test
    public void testGetBmDescription() {
        ProbeInformation probeInfo = new ProbeInformation("target", "type", "class", "name", "description", "probe", 5, "url");
        assertEquals("description", probeInfo.getBmDescription());
    }
    
    @Test
    public void testGetPrecision() {
        ProbeInformation probeInfo = new ProbeInformation("target", "type", "class", "name", "description", "probe", 5, "url");
        assertEquals(5, probeInfo.getPrecision());
    }
    
    @Test
    public void testGetMeasureURI() {
        ProbeInformation probeInfo = new ProbeInformation("target", "type", "class", "name", "description", "probe", 5, "url");
        assertEquals("type/target/class/name", probeInfo.getMeasureURI());
    }
    
    @Test
    public void testGetXmlRpcUrl() {
        ProbeInformation probeInfo = new ProbeInformation("target", "type", "class", "name", "description", "probe", 5, "url");
        assertEquals("url", probeInfo.getXmlRpcUrl());
    }
    
    @Test
    public void testEquals() {
        ProbeInformation probeInfo1 = new ProbeInformation("target", "type", "class", "name", "description", "probe", 5, "url");
        ProbeInformation probeInfo2 = new ProbeInformation("target", "type", "class", "name", "description", "probe", 5, "url");
        assertTrue(probeInfo1.equals(probeInfo2));
    }
    
    @Test
    public void testHashCode() {
        ProbeInformation probeInfo1 = new ProbeInformation("target", "type", "class", "name", "description", "probe", 5, "url");
        ProbeInformation probeInfo2 = new ProbeInformation("target", "type", "class", "name", "description", "probe", 5, "url");
        assertEquals(probeInfo1.hashCode(), probeInfo2.hashCode());
    }
}