import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class ProbeInformationTest {

    private ProbeInformation probeInfo;

    @Before
    public void setUp() {
        probeInfo = new ProbeInformation("targetName", "targetType", "bmClass", "bmName", "bmDescription", "probeDescription", 5, "xmlRpcUrl");
    }

    @Test
    public void testGetTargetType() {
        String targetType = probeInfo.getTargetType();
        assertEquals("targetType", targetType);
    }

    @Test
    public void testGetBmName() {
        String bmName = probeInfo.getBmName();
        assertEquals("bmName", bmName);
    }

    @Test
    public void testGetProbeName() {
        String probeName = probeInfo.getProbeName();
        assertEquals("probeDescription", probeName);
    }

    @Test
    public void testGetTargetName() {
        String targetName = probeInfo.getTargetName();
        assertEquals("targetName", targetName);
    }

    @Test
    public void testGetBmClass() {
        String bmClass = probeInfo.getBmClass();
        assertEquals("bmClass", bmClass);
    }

    @Test
    public void testGetBmDescription() {
        String bmDescription = probeInfo.getBmDescription();
        assertEquals("bmDescription", bmDescription);
    }

    @Test
    public void testGetPrecision() {
        int precision = probeInfo.getPrecision();
        assertEquals(5, precision);
    }

    @Test
    public void testGetMeasureURI() {
        String measureURI = probeInfo.getMeasureURI();
        assertEquals("expectedMeasureURI", measureURI);  // replace "expectedMeasureURI" with the expected value
    }

    @Test
    public void testGetXmlRpcUrl() {
        String xmlRpcUrl = probeInfo.getXmlRpcUrl();
        assertEquals("xmlRpcUrl", xmlRpcUrl);
    }

    @Test
    public void testToString() {
        String expectedToString = "ProbeInformation{targetName='targetName', targetType='targetType', bmClass='bmClass', bmName='bmName', bmDescription='bmDescription', probeDescription='probeDescription', precision=5}";
        String probeInfoString = probeInfo.toString();
        assertEquals(expectedToString, probeInfoString);
    }

    @Test
    public void testEquals_sameObject() {
        assertTrue(probeInfo.equals(probeInfo));
    }

    @Test
    public void testEquals_nullObject() {
        assertFalse(probeInfo.equals(null));
    }

    @Test
    public void testEquals_differentClass() {
        assertFalse(probeInfo.equals("someString"));
    }

    @Test
    public void testEquals_differentTargetName() {
        ProbeInformation probeInfo2 = new ProbeInformation("differentTargetName", "targetType", "bmClass", "bmName", "bmDescription", "probeDescription", 5, "xmlRpcUrl");
        assertFalse(probeInfo.equals(probeInfo2));
    }

    @Test
    public void testEquals_differentTargetType() {
        ProbeInformation probeInfo2 = new ProbeInformation("targetName", "differentTargetType", "bmClass", "bmName", "bmDescription", "probeDescription", 5, "xmlRpcUrl");
        assertFalse(probeInfo.equals(probeInfo2));
    }

    @Test
    public void testEquals_differentBmClass() {
        ProbeInformation probeInfo2 = new ProbeInformation("targetName", "targetType", "differentBmClass", "bmName", "bmDescription", "probeDescription", 5, "xmlRpcUrl");
        assertFalse(probeInfo.equals(probeInfo2));
    }

    @Test
    public void testEquals_differentBmName() {
        ProbeInformation probeInfo2 = new ProbeInformation("targetName", "targetType", "bmClass", "differentBmName", "bmDescription", "probeDescription", 5, "xmlRpcUrl");
        assertFalse(probeInfo.equals(probeInfo2));
    }

    @Test
    public void testEquals_differentBmDescription() {
        ProbeInformation probeInfo2 = new ProbeInformation("targetName", "targetType", "bmClass", "bmName", "differentBmDescription", "probeDescription", 5, "xmlRpcUrl");
        assertFalse(probeInfo.equals(probeInfo2));
    }

    @Test
    public void testEquals_differentProbeName() {
        ProbeInformation probeInfo2 = new ProbeInformation("targetName", "targetType", "bmClass", "bmName", "bmDescription", "differentProbeName", 5, "xmlRpcUrl");
        assertFalse(probeInfo.equals(probeInfo2));
    }

    @Test
    public void testEquals_differentPrecision() {
        ProbeInformation probeInfo2 = new ProbeInformation("targetName", "targetType", "bmClass", "bmName", "bmDescription", "probeDescription", 10, "xmlRpcUrl");
        assertFalse(probeInfo.equals(probeInfo2));
    }

    @Test
    public void testEquals_differentXmlRpcUrl() {
        ProbeInformation probeInfo2 = new ProbeInformation("targetName", "targetType", "bmClass", "bmName", "bmDescription", "probeDescription", 5, "differentXmlRpcUrl");
        assertFalse(probeInfo.equals(probeInfo2));
    }

    @Test
    public void testEquals_equalObject() {
        ProbeInformation probeInfo2 = new ProbeInformation("targetName", "targetType", "bmClass", "bmName", "bmDescription", "probeDescription", 5, "xmlRpcUrl");
        assertTrue(probeInfo.equals(probeInfo2));
    }

    @Test
    public void testHashCode() {
        int hashCode = probeInfo.hashCode();
        assertEquals(1234567890, hashCode);  // replace 1234567890 with the expected hash code value
    }
}