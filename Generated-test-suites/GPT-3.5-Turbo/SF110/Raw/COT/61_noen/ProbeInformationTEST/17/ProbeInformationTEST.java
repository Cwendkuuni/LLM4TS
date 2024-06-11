import org.junit.Test;
import static org.junit.Assert.*;

public class ProbeInformationTest {

    @Test
    public void testGetTargetType() {
        ProbeInformation probe = new ProbeInformation("Target", "Type", "Class", "Name", "Description", "Probe", 5, "http://example.com");
        assertEquals("Type", probe.getTargetType());
    }

    @Test
    public void testGetBmName() {
        ProbeInformation probe = new ProbeInformation("Target", "Type", "Class", "Name", "Description", "Probe", 5, "http://example.com");
        assertEquals("Name", probe.getBmName());
    }

    @Test
    public void testGetProbeName() {
        ProbeInformation probe = new ProbeInformation("Target", "Type", "Class", "Name", "Description", "Probe", 5, "http://example.com");
        assertEquals("Probe", probe.getProbeName());
    }

    @Test
    public void testGetTargetName() {
        ProbeInformation probe = new ProbeInformation("Target", "Type", "Class", "Name", "Description", "Probe", 5, "http://example.com");
        assertEquals("Target", probe.getTargetName());
    }

    @Test
    public void testGetBmClass() {
        ProbeInformation probe = new ProbeInformation("Target", "Type", "Class", "Name", "Description", "Probe", 5, "http://example.com");
        assertEquals("Class", probe.getBmClass());
    }

    @Test
    public void testGetBmDescription() {
        ProbeInformation probe = new ProbeInformation("Target", "Type", "Class", "Name", "Description", "Probe", 5, "http://example.com");
        assertEquals("Description", probe.getBmDescription());
    }

    @Test
    public void testGetPrecision() {
        ProbeInformation probe = new ProbeInformation("Target", "Type", "Class", "Name", "Description", "Probe", 5, "http://example.com");
        assertEquals(5, probe.getPrecision());
    }

    @Test
    public void testGetMeasureURI() {
        ProbeInformation probe = new ProbeInformation("Target", "Type", "Class", "Name", "Description", "Probe", 5, "http://example.com");
        assertEquals("Type:Target:Class:Name", probe.getMeasureURI());
    }

    @Test
    public void testGetXmlRpcUrl() {
        ProbeInformation probe = new ProbeInformation("Target", "Type", "Class", "Name", "Description", "Probe", 5, "http://example.com");
        assertEquals("http://example.com", probe.getXmlRpcUrl());
    }

    @Test
    public void testEquals() {
        ProbeInformation probe1 = new ProbeInformation("Target", "Type", "Class", "Name", "Description", "Probe", 5, "http://example.com");
        ProbeInformation probe2 = new ProbeInformation("Target", "Type", "Class", "Name", "Description", "Probe", 5, "http://example.com");
        assertTrue(probe1.equals(probe2));
    }

    @Test
    public void testHashCode() {
        ProbeInformation probe1 = new ProbeInformation("Target", "Type", "Class", "Name", "Description", "Probe", 5, "http://example.com");
        ProbeInformation probe2 = new ProbeInformation("Target", "Type", "Class", "Name", "Description", "Probe", 5, "http://example.com");
        assertEquals(probe1.hashCode(), probe2.hashCode());
    }
}