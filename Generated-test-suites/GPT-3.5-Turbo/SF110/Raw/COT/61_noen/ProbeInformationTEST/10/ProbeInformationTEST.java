
package fi.vtt.noen.mfw.bundle.probe.shared;

import org.junit.Test;
import static org.junit.Assert.*;

public class ProbeInformationTest {

    @Test
    public void testGetTargetType() {
        ProbeInformation probeInfo = new ProbeInformation("Target", "Type", "Class", "Name", "Description",
                "Probe Description", 10, "XML-RPC URL");
        assertEquals("Type", probeInfo.getTargetType());
    }

    @Test
    public void testGetBmName() {
        ProbeInformation probeInfo = new ProbeInformation("Target", "Type", "Class", "Name", "Description",
                "Probe Description", 10, "XML-RPC URL");
        assertEquals("Name", probeInfo.getBmName());
    }

    @Test
    public void testGetProbeName() {
        ProbeInformation probeInfo = new ProbeInformation("Target", "Type", "Class", "Name", "Description",
                "Probe Description", 10, "XML-RPC URL");
        assertEquals("Probe Description", probeInfo.getProbeName());
    }

    @Test
    public void testGetTargetName() {
        ProbeInformation probeInfo = new ProbeInformation("Target", "Type", "Class", "Name", "Description",
                "Probe Description", 10, "XML-RPC URL");
        assertEquals("Target", probeInfo.getTargetName());
    }

    @Test
    public void testGetBmClass() {
        ProbeInformation probeInfo = new ProbeInformation("Target", "Type", "Class", "Name", "Description",
                "Probe Description", 10, "XML-RPC URL");
        assertEquals("Class", probeInfo.getBmClass());
    }

    @Test
    public void testGetBmDescription() {
        ProbeInformation probeInfo = new ProbeInformation("Target", "Type", "Class", "Name", "Description",
                "Probe Description", 10, "XML-RPC URL");
        assertEquals("Description", probeInfo.getBmDescription());
    }

    @Test
    public void testGetPrecision() {
        ProbeInformation probeInfo = new ProbeInformation("Target", "Type", "Class", "Name", "Description",
                "Probe Description", 10, "XML-RPC URL");
        assertEquals(10, probeInfo.getPrecision());
    }

    @Test
    public void testGetMeasureURI() {
        ProbeInformation probeInfo = new ProbeInformation("Target", "Type", "Class", "Name", "Description",
                "Probe Description", 10, "XML-RPC URL");
        assertEquals("Type:Target:Class:Name", probeInfo.getMeasureURI());
    }

    @Test
    public void testGetXmlRpcUrl() {
        ProbeInformation probeInfo = new ProbeInformation("Target", "Type", "Class", "Name", "Description",
                "Probe Description", 10, "XML-RPC URL");
        assertEquals("XML-RPC URL", probeInfo.getXmlRpcUrl());
    }
}
