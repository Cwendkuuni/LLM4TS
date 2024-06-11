package fi.vtt.noen.mfw.bundle.probe.shared;
import org.junit.Test;
import static org.junit.Assert.*;

public class ProbeInformationTest {

    @Test
    public void testGetTargetType() {
        ProbeInformation probeInfo = new ProbeInformation("Target", "Type", "Class", "Name", "Description", "Probe", 10, "http://example.com");
        assertEquals("Type", probeInfo.getTargetType());
    }

    @Test
    public void testGetBmName() {
        ProbeInformation probeInfo = new ProbeInformation("Target", "Type", "Class", "Name", "Description", "Probe", 10, "http://example.com");
        assertEquals("Name", probeInfo.getBmName());
    }

    @Test
    public void testGetProbeName() {
        ProbeInformation probeInfo = new ProbeInformation("Target", "Type", "Class", "Name", "Description", "Probe", 10, "http://example.com");
        assertEquals("Probe", probeInfo.getProbeName());
    }

    @Test
    public void testGetTargetName() {
        ProbeInformation probeInfo = new ProbeInformation("Target", "Type", "Class", "Name", "Description", "Probe", 10, "http://example.com");
        assertEquals("Target", probeInfo.getTargetName());
    }

    @Test
    public void testGetBmClass() {
        ProbeInformation probeInfo = new ProbeInformation("Target", "Type", "Class", "Name", "Description", "Probe", 10, "http://example.com");
        assertEquals("Class", probeInfo.getBmClass());
    }

    @Test
    public void testGetBmDescription() {
        ProbeInformation probeInfo = new ProbeInformation("Target", "Type", "Class", "Name", "Description", "Probe", 10, "http://example.com");
        assertEquals("Description", probeInfo.getBmDescription());
    }

    @Test
    public void testGetPrecision() {
        ProbeInformation probeInfo = new ProbeInformation("Target", "Type", "Class", "Name", "Description", "Probe", 10, "http://example.com");
        assertEquals(10, probeInfo.getPrecision());
    }

    @Test
    public void testGetMeasureURI() {
        ProbeInformation probeInfo = new ProbeInformation("Target", "Type", "Class", "Name", "Description", "Probe", 10, "http://example.com");
        assertEquals("Type.Target.Class.Name", probeInfo.getMeasureURI());
    }

    @Test
    public void testGetXmlRpcUrl() {
        ProbeInformation probeInfo = new ProbeInformation("Target", "Type", "Class", "Name", "Description", "Probe", 10, "http://example.com");
        assertEquals("http://example.com", probeInfo.getXmlRpcUrl());
    }

    @Test
    public void testToString() {
        ProbeInformation probeInfo = new ProbeInformation("Target", "Type", "Class", "Name", "Description", "Probe", 10, "http://example.com");
        String expected = "ProbeInformation{targetName='Target', targetType='Type', bmClass='Class', bmName='Name', bmDescription='Description', probeDescription='Probe', precision=10}";
        assertEquals(expected, probeInfo.toString());
    }

    @Test
    public void testEquals() {
        ProbeInformation probeInfo1 = new ProbeInformation("Target", "Type", "Class", "Name", "Description", "Probe", 10, "http://example.com");
        ProbeInformation probeInfo2 = new ProbeInformation("Target", "Type", "Class", "Name", "Description", "Probe", 10, "http://example.com");
        assertEquals(probeInfo1, probeInfo2);
    }

    @Test
    public void testHashCode() {
        ProbeInformation probeInfo1 = new ProbeInformation("Target", "Type", "Class", "Name", "Description", "Probe", 10, "http://example.com");
        ProbeInformation probeInfo2 = new ProbeInformation("Target", "Type", "Class", "Name", "Description", "Probe", 10, "http://example.com");
        assertEquals(probeInfo1.hashCode(), probeInfo2.hashCode());
    }
}