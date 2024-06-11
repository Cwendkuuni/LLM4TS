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

    // Add test methods for other public methods in ProbeInformation class

    @Test
    public void testGetMeasureURI_edgeCase() {
        ProbeInformation probeInfo = new ProbeInformation("Target", "Type", "Class", "Name", "Description", "Probe", 10, "http://example.com");
        assertEquals("Type/Target/Class/Name", probeInfo.getMeasureURI());
    }

    @Test
    public void testEquals_nullInput() {
        ProbeInformation probeInfo1 = new ProbeInformation("Target", "Type", "Class", "Name", "Description", "Probe", 10, "http://example.com");
        ProbeInformation probeInfo2 = null;
        assertFalse(probeInfo1.equals(probeInfo2));
    }

    @Test
    public void testHashCode_edgeCase() {
        ProbeInformation probeInfo = new ProbeInformation("Target", "Type", "Class", "Name", "Description", "Probe", 10, "http://example.com");
        assertEquals(815315946, probeInfo.hashCode());
    }

}