package fi.vtt.noen.mfw.bundle.probe.shared;

import static org.junit.Assert.assertEquals;
import org.junit.Test;

public class ProbeInformationTest {

    @Test
    public void testGetTargetType() {
        ProbeInformation probeInfo = new ProbeInformation("TargetName", "TargetType", "BmClass", "BmName", "BmDescription", "ProbeDescription", 10, "http://example.com");
        assertEquals("TargetType", probeInfo.getTargetType());
    }

    @Test
    public void testGetBmName() {
        ProbeInformation probeInfo = new ProbeInformation("TargetName", "TargetType", "BmClass", "BmName", "BmDescription", "ProbeDescription", 10, "http://example.com");
        assertEquals("BmName", probeInfo.getBmName());
    }

    // More test cases for other methods can be added here

}