
import org.junit.Test;

public class ProbeInformationTest {

    @Test
    public void testGetTargetType() {
        ProbeInformation probe = new ProbeInformation("targetName", "targetType", "bmClass", "bmName", "bmDescription", "probeDescription", 5, "xmlRpcUrl");
        String targetType = probe.getTargetType();
        assertEquals("targetType", targetType);
    }

    @Test
    public void testGetBmName() {
        ProbeInformation probe = new ProbeInformation("targetName", "targetType", "bmClass", "bmName", "bmDescription", "probeDescription", 5, "xmlRpcUrl");
        String bmName = probe.getBmName();
        assertEquals("bmName", bmName);
    }

    @Test
    public void testGetProbeName() {
        ProbeInformation probe = new ProbeInformation("targetName", "targetType", "bmClass", "bmName", "bmDescription", "probeDescription", 5, "xmlRpcUrl");
        String probeName = probe.getProbeName();
        assertEquals("probeDescription", probeName);
    }

    @Test
    public void testGetTargetName() {
        ProbeInformation probe = new ProbeInformation("targetName", "targetType", "bmClass", "bmName", "bmDescription", "probeDescription", 5, "xmlRpcUrl");
        String targetName = probe.getTargetName();
        assertEquals("targetName", targetName);
    }

    @Test
    public void testGetBmClass() {
        ProbeInformation probe = new ProbeInformation("targetName", "targetType", "bmClass", "bmName", "bmDescription", "probeDescription", 5, "xmlRpcUrl");
        String bmClass = probe.getBmClass();
        assertEquals("bmClass", bmClass);
    }

    @Test
    public void testGetBmDescription() {
        ProbeInformation probe = new ProbeInformation("targetName", "targetType", "bmClass", "bmName", "bmDescription", "probeDescription", 5, "xmlRpcUrl");
        String bmDescription = probe.getBmDescription();
        assertEquals("bmDescription", bmDescription);
    }

    @Test
    public void testGetPrecision() {
        ProbeInformation probe = new ProbeInformation("targetName", "targetType", "bmClass", "bmName", "bmDescription", "probeDescription", 5, "xmlRpcUrl");
        int precision = probe.getPrecision();
        assertEquals(5, precision);
    }

    @Test
    public void testGetMeasureURI() {
        ProbeInformation probe = new ProbeInformation("targetName", "targetType", "bmClass", "bmName", "bmDescription", "probeDescription", 5, "xmlRpcUrl");
        String measureURI = probe.getMeasureURI();
        // Perform assertion for measureURI
    }

    @Test
    public void testGetXmlRpcUrl() {
        ProbeInformation probe = new ProbeInformation("targetName", "targetType", "bmClass", "bmName", "bmDescription", "probeDescription", 5, "xmlRpcUrl");
        String xmlRpcUrl = probe.getXmlRpcUrl();
        assertEquals("xmlRpcUrl", xmlRpcUrl);
    }

    @Test
    public void testToString() {
        ProbeInformation probe = new ProbeInformation("targetName", "targetType", "bmClass", "bmName", "bmDescription", "probeDescription", 5, "xmlRpcUrl");
        String str = probe.toString();
        // Perform assertion for str
    }

    @Test
    public void testEquals() {
        // Test different combinations of ProbeInformation objects for equality
    }

    @Test
    public void testHashCode() {
        // Perform assertion for hashCode
    }
}
