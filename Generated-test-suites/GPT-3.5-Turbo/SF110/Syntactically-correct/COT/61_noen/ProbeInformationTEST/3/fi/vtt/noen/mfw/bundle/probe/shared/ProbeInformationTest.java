import org.junit.*;
import static org.junit.Assert.*;

public class ProbeInformationTest {

    ProbeInformation probeInfo;

    @Before
    public void setUp() {
        probeInfo = new ProbeInformation("TargetName", "TargetType", "BmClass", "BmName", "BmDescription", "ProbeName", 10, "XmlRpcUrl");
    }

    @Test
    public void testGetTargetType() {
        String targetType = probeInfo.getTargetType();
        assertEquals("TargetType", targetType);
    }

    @Test
    public void testGetBmName() {
        String bmName = probeInfo.getBmName();
        assertEquals("BmName", bmName);
    }

    @Test
    public void testGetProbeName() {
        String probeName = probeInfo.getProbeName();
        assertEquals("ProbeName", probeName);
    }

    @Test
    public void testGetTargetName() {
        String targetName = probeInfo.getTargetName();
        assertEquals("TargetName", targetName);
    }

    @Test
    public void testGetBmClass() {
        String bmClass = probeInfo.getBmClass();
        assertEquals("BmClass", bmClass);
    }

    @Test
    public void testGetBmDescription() {
        String bmDescription = probeInfo.getBmDescription();
        assertEquals("BmDescription", bmDescription);
    }

    @Test
    public void testGetPrecision() {
        int precision = probeInfo.getPrecision();
        assertEquals(10, precision);
    }

    @Test
    public void testGetMeasureURI() {
        String measureURI = probeInfo.getMeasureURI();
        assertEquals("expectedMeasureURI", measureURI);
    }

    @Test
    public void testGetXmlRpcUrl() {
        String xmlRpcUrl = probeInfo.getXmlRpcUrl();
        assertEquals("XmlRpcUrl", xmlRpcUrl);
    }

    @Test
    public void testEquals() {
        ProbeInformation sameProbeInfo = new ProbeInformation("TargetName", "TargetType", "BmClass", "BmName", "BmDescription", "ProbeName", 10, "XmlRpcUrl");
        assertTrue(probeInfo.equals(sameProbeInfo));

        ProbeInformation differentProbeInfo = new ProbeInformation("DifferentTargetName", "TargetType", "BmClass", "BmName", "BmDescription", "ProbeName", 10, "XmlRpcUrl");
        assertFalse(probeInfo.equals(differentProbeInfo));
    }

    @Test
    public void testHashCode() {
        ProbeInformation sameProbeInfo = new ProbeInformation("TargetName", "TargetType", "BmClass", "BmName", "BmDescription", "ProbeName", 10, "XmlRpcUrl");
        assertEquals(probeInfo.hashCode(), sameProbeInfo.hashCode());

        ProbeInformation differentProbeInfo = new ProbeInformation("DifferentTargetName", "TargetType", "BmClass", "BmName", "BmDescription", "ProbeName", 10, "XmlRpcUrl");
        assertNotEquals(probeInfo.hashCode(), differentProbeInfo.hashCode());
    }
}

