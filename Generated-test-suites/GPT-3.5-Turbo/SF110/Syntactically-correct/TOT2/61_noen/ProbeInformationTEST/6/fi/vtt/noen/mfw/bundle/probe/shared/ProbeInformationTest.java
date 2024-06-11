package fi.vtt.noen.mfw.bundle.probe.shared;
import org.junit.Test;
import static org.junit.Assert.*;

public class ProbeInformationTest {
    
    @Test
    public void testGetTargetType() {
        ProbeInformation probe = new ProbeInformation("Target", "Type", "Class", "Name", "Description", "Probe", 3, "url");
        assertEquals("Type", probe.getTargetType());
    }

    @Test
    public void testGetBmName() {
        ProbeInformation probe = new ProbeInformation("Target", "Type", "Class", "Name", "Description", "Probe", 3, "url");
        assertEquals("Name", probe.getBmName());
    }

    @Test
    public void testGetProbeName() {
        ProbeInformation probe = new ProbeInformation("Target", "Type", "Class", "Name", "Description", "Probe", 3, "url");
        assertEquals("Probe", probe.getProbeName());
    }

    @Test
    public void testGetTargetName() {
        ProbeInformation probe = new ProbeInformation("Target", "Type", "Class", "Name", "Description", "Probe", 3, "url");
        assertEquals("Target", probe.getTargetName());
    }

    @Test
    public void testGetBmClass() {
        ProbeInformation probe = new ProbeInformation("Target", "Type", "Class", "Name", "Description", "Probe", 3, "url");
        assertEquals("Class", probe.getBmClass());
    }

    @Test
    public void testGetBmDescription() {
        ProbeInformation probe = new ProbeInformation("Target", "Type", "Class", "Name", "Description", "Probe", 3, "url");
        assertEquals("Description", probe.getBmDescription());
    }
    
    @Test
    public void testGetPrecision() {
        ProbeInformation probe = new ProbeInformation("Target", "Type", "Class", "Name", "Description", "Probe", 3, "url");
        assertEquals(3, probe.getPrecision());
    }

    @Test
    public void testGetMeasureURI() {
        ProbeInformation probe = new ProbeInformation("Target", "Type", "Class", "Name", "Description", "Probe", 3, "url");
        assertEquals("measureURI", probe.getMeasureURI()); // Edge case
    }

    @Test
    public void testGetXmlRpcUrl() {
        ProbeInformation probe = new ProbeInformation("Target", "Type", "Class", "Name", "Description", "Probe", 3, "url");
        assertEquals("url", probe.getXmlRpcUrl());
    }
    
    @Test
    public void testEquals() {
        ProbeInformation probe1 = new ProbeInformation("Target", "Type", "Class", "Name", "Description", "Probe", 3, "url");
        ProbeInformation probe2 = new ProbeInformation("Target", "Type", "Class", "Name", "Description", "Probe", 3, "url");
        assertEquals(probe1, probe2);
    }
    
    @Test
    public void testHashCode() {
        ProbeInformation probe = new ProbeInformation("Target", "Type", "Class", "Name", "Description", "Probe", 3, "url");
        assertNotNull(probe.hashCode());
    }
}