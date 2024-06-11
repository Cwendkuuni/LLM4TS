package fi.vtt.noen.mfw.bundle.probe.shared;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class ProbeInformationTest {
    
    @Test
    public void testGetTargetType() {
        ProbeInformation probeInfo = new ProbeInformation("TargetName", "TargetType", "BmClass", "BmName", "BmDescription", "ProbeDescription", 5, "http://example.com");
        assertEquals("TargetType", probeInfo.getTargetType());
    }
    
    @Test
    public void testGetBmName() {
        ProbeInformation probeInfo = new ProbeInformation("TargetName", "TargetType", "BmClass", "BmName", "BmDescription", "ProbeDescription", 5, "http://example.com");
        assertEquals("BmName", probeInfo.getBmName());
    }
    
    @Test
    public void testGetProbeName() {
        ProbeInformation probeInfo = new ProbeInformation("TargetName", "TargetType", "BmClass", "BmName", "BmDescription", "ProbeDescription", 5, "http://example.com");
        assertEquals("ProbeDescription", probeInfo.getProbeName());
    }
    
    @Test
    public void testGetTargetName() {
        ProbeInformation probeInfo = new ProbeInformation("TargetName", "TargetType", "BmClass", "BmName", "BmDescription", "ProbeDescription", 5, "http://example.com");
        assertEquals("TargetName", probeInfo.getTargetName());
    }
    
    @Test
    public void testGetBmClass() {
        ProbeInformation probeInfo = new ProbeInformation("TargetName", "TargetType", "BmClass", "BmName", "BmDescription", "ProbeDescription", 5, "http://example.com");
        assertEquals("BmClass", probeInfo.getBmClass());
    }
    
    @Test
    public void testGetBmDescription() {
        ProbeInformation probeInfo = new ProbeInformation("TargetName", "TargetType", "BmClass", "BmName", "BmDescription", "ProbeDescription", 5, "http://example.com");
        assertEquals("BmDescription", probeInfo.getBmDescription());
    }
    
    @Test
    public void testGetPrecision() {
        ProbeInformation probeInfo = new ProbeInformation("TargetName", "TargetType", "BmClass", "BmName", "BmDescription", "ProbeDescription", 5, "http://example.com");
        assertEquals(5, probeInfo.getPrecision());
    }
    
    @Test
    public void testGetMeasureURI() {
        ProbeInformation probeInfo = new ProbeInformation("TargetName", "TargetType", "BmClass", "BmName", "BmDescription", "ProbeDescription", 5, "http://example.com");
        String expectedMeasureURI = "measureuri://" + probeInfo.getTargetType() + "/" + probeInfo.getTargetName() + "/" + probeInfo.getBmClass() + "/" + probeInfo.getBmName();
        assertEquals(expectedMeasureURI, probeInfo.getMeasureURI());
    }
    
    @Test
    public void testGetXmlRpcUrl() {
        ProbeInformation probeInfo = new ProbeInformation("TargetName", "TargetType", "BmClass", "BmName", "BmDescription", "ProbeDescription", 5, "http://example.com");
        assertEquals("http://example.com", probeInfo.getXmlRpcUrl());
    }
    
}