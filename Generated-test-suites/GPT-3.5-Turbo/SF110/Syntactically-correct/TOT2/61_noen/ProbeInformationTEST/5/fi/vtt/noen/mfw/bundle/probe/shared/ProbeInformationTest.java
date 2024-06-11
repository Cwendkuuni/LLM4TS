package fi.vtt.noen.mfw.bundle.probe.shared;
import org.junit.Test;
import static org.junit.Assert.*;

public class ProbeInformationTest {
    
    ProbeInformation probeInfo;
    
    @Test
    public void testGetTargetType() {
        probeInfo = new ProbeInformation("Target", "Type", "Class", "Name", "Description", "ProbeDesc", 10, "URL");
        assertEquals("Type", probeInfo.getTargetType());
    }
    
    @Test
    public void testGetBmName() {
        probeInfo = new ProbeInformation("Target", "Type", "Class", "Name", "Description", "ProbeDesc", 10, "URL");
        assertEquals("Name", probeInfo.getBmName());
    }
    
    @Test
    public void testGetProbeName() {
        probeInfo = new ProbeInformation("Target", "Type", "Class", "Name", "Description", "ProbeDesc", 10, "URL");
        assertEquals("ProbeDesc", probeInfo.getProbeName());
    }
    
    @Test
    public void testGetTargetName() {
        probeInfo = new ProbeInformation("Target", "Type", "Class", "Name", "Description", "ProbeDesc", 10, "URL");
        assertEquals("Target", probeInfo.getTargetName());
    }
    
    @Test
    public void testGetBmClass() {
        probeInfo = new ProbeInformation("Target", "Type", "Class", "Name", "Description", "ProbeDesc", 10, "URL");
        assertEquals("Class", probeInfo.getBmClass());
    }
    
    @Test
    public void testGetBmDescription() {
        probeInfo = new ProbeInformation("Target", "Type", "Class", "Name", "Description", "ProbeDesc", 10, "URL");
        assertEquals("Description", probeInfo.getBmDescription());
    }
    
    @Test
    public void testGetPrecision() {
        probeInfo = new ProbeInformation("Target", "Type", "Class", "Name", "Description", "ProbeDesc", 10, "URL");
        assertEquals(10, probeInfo.getPrecision());
    }
    
    @Test
    public void testGetMeasureURI() {
        probeInfo = new ProbeInformation("Target", "Type", "Class", "Name", "Description", "ProbeDesc", 10, "URL");
        assertEquals("measure://Type/Target/Class/Name", probeInfo.getMeasureURI());
    }
    
    @Test
    public void testGetXmlRpcUrl() {
        probeInfo = new ProbeInformation("Target", "Type", "Class", "Name", "Description", "ProbeDesc", 10, "URL");
        assertEquals("URL", probeInfo.getXmlRpcUrl());
    }
    
    // Additional test cases for equals and hashCode methods can be added here
    
}