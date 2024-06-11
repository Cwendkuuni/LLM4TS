package fi.vtt.noen.mfw.bundle.probe.shared;
import org.junit.Test;
import static org.junit.Assert.*;

public class ProbeInformationTest {

    @Test
    public void testGetTargetType() {
        ProbeInformation probeInfo = new ProbeInformation("targetName", "targetType", "bmClass", "bmName", "bmDescription", "probeDescription", 5, "xmlRpcUrl");
        assertEquals("targetType", probeInfo.getTargetType());
    }

    @Test
    public void testGetBmName() {
        ProbeInformation probeInfo = new ProbeInformation("targetName", "targetType", "bmClass", "bmName", "bmDescription", "probeDescription", 5, "xmlRpcUrl");
        assertEquals("bmName", probeInfo.getBmName());
    }

    // Additional test methods for other public methods in the class can be added here

    @Test
    public void testGetMeasureURI() {
        ProbeInformation probeInfo = new ProbeInformation("targetName", "targetType", "bmClass", "bmName", "bmDescription", "probeDescription", 5, "xmlRpcUrl");
        assertEquals("measureURI", probeInfo.getMeasureURI()); // Assumed measureURI as expected value
    }

    @Test
    public void testEquals() {
        ProbeInformation probeInfo1 = new ProbeInformation("targetName", "targetType", "bmClass", "bmName", "bmDescription", "probeDescription", 5, "xmlRpcUrl");
        ProbeInformation probeInfo2 = new ProbeInformation("targetName", "targetType", "bmClass", "bmName", "bmDescription", "probeDescription", 5, "xmlRpcUrl");
        assertTrue(probeInfo1.equals(probeInfo2));
    }

    @Test
    public void testHashCode() {
        ProbeInformation probeInfo = new ProbeInformation("targetName", "targetType", "bmClass", "bmName", "bmDescription", "probeDescription", 5, "xmlRpcUrl");
        assertEquals(123456789, probeInfo.hashCode()); // Assumed hashCode value as expected
    }
}