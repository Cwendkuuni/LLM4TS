import static org.junit.Assert.*;
import org.junit.Test;

public class ProbeInformationTest {

    @Test
    public void testGetTargetType() {
        ProbeInformation probeInfo = new ProbeInformation("Test", "Type", "Class", "Name", "Description", "Probe", 10, "http://example.com");
        assertEquals("Type", probeInfo.getTargetType());
    }

    @Test
    public void testGetBmName() {
        ProbeInformation probeInfo = new ProbeInformation("Test", "Type", "Class", "Name", "Description", "Probe", 10, "http://example.com");
        assertEquals("Name", probeInfo.getBmName());
    }

    @Test
    public void testGetProbeName() {
        ProbeInformation probeInfo = new ProbeInformation("Test", "Type", "Class", "Name", "Description", "Probe", 10, "http://example.com");
        assertEquals("Probe", probeInfo.getProbeName());
    }

    @Test
    public void testGetTargetName() {
        ProbeInformation probeInfo = new ProbeInformation("Test", "Type", "Class", "Name", "Description", "Probe", 10, "http://example.com");
        assertEquals("Test", probeInfo.getTargetName());
    }

    @Test
    public void testGetBmClass() {
        ProbeInformation probeInfo = new ProbeInformation("Test", "Type", "Class", "Name", "Description", "Probe", 10, "http://example.com");
        assertEquals("Class", probeInfo.getBmClass());
    }

    @Test
    public void testGetBmDescription() {
        ProbeInformation probeInfo = new ProbeInformation("Test", "Type", "Class", "Name", "Description", "Probe", 10, "http://example.com");
        assertEquals("Description", probeInfo.getBmDescription());
    }

    @Test
    public void testGetPrecision() {
        ProbeInformation probeInfo = new ProbeInformation("Test", "Type", "Class", "Name", "Description", "Probe", 10, "http://example.com");
        assertEquals(10, probeInfo.getPrecision());
    }

    @Test
    public void testGetMeasureURI() {
        ProbeInformation probeInfo = new ProbeInformation("Test", "Type", "Class", "Name", "Description", "Probe", 10, "http://example.com");
        assertEquals("Type:Test:Class:Name", probeInfo.getMeasureURI());
    }

    @Test
    public void testGetXmlRpcUrl() {
        ProbeInformation probeInfo = new ProbeInformation("Test", "Type", "Class", "Name", "Description", "Probe", 10, "http://example.com");
        assertEquals("http://example.com", probeInfo.getXmlRpcUrl());
    }

    // Add test cases for equals and hashCode methods

}