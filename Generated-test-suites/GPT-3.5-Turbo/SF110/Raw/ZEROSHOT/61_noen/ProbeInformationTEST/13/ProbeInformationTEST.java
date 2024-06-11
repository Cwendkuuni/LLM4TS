import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

public class ProbeInformationTest {

    @Test
    public void testGetTargetType() {
        ProbeInformation probe = new ProbeInformation("targetName", "targetType", "bmClass", "bmName", "bmDescription", "probeName", 10, "xmlRpcUrl");
        assertEquals("targetType", probe.getTargetType());
    }

    @Test
    public void testGetBmName() {
        ProbeInformation probe = new ProbeInformation("targetName", "targetType", "bmClass", "bmName", "bmDescription", "probeName", 10, "xmlRpcUrl");
        assertEquals("bmName", probe.getBmName());
    }

    @Test
    public void testGetProbeName() {
        ProbeInformation probe = new ProbeInformation("targetName", "targetType", "bmClass", "bmName", "bmDescription", "probeName", 10, "xmlRpcUrl");
        assertEquals("probeName", probe.getProbeName());
    }

    @Test
    public void testGetTargetName() {
        ProbeInformation probe = new ProbeInformation("targetName", "targetType", "bmClass", "bmName", "bmDescription", "probeName", 10, "xmlRpcUrl");
        assertEquals("targetName", probe.getTargetName());
    }

    @Test
    public void testGetBmClass() {
        ProbeInformation probe = new ProbeInformation("targetName", "targetType", "bmClass", "bmName", "bmDescription", "probeName", 10, "xmlRpcUrl");
        assertEquals("bmClass", probe.getBmClass());
    }

    @Test
    public void testGetBmDescription() {
        ProbeInformation probe = new ProbeInformation("targetName", "targetType", "bmClass", "bmName", "bmDescription", "probeName", 10, "xmlRpcUrl");
        assertEquals("bmDescription", probe.getBmDescription());
    }

    @Test
    public void testGetPrecision() {
        ProbeInformation probe = new ProbeInformation("targetName", "targetType", "bmClass", "bmName", "bmDescription", "probeName", 10, "xmlRpcUrl");
        assertEquals(10, probe.getPrecision());
    }

    @Test
    public void testGetMeasureURI() {
        ProbeInformation probe = new ProbeInformation("targetName", "targetType", "bmClass", "bmName", "bmDescription", "probeName", 10, "xmlRpcUrl");
        assertEquals("measureURI", probe.getMeasureURI());
    }

    @Test
    public void testGetXmlRpcUrl() {
        ProbeInformation probe = new ProbeInformation("targetName", "targetType", "bmClass", "bmName", "bmDescription", "probeName", 10, "xmlRpcUrl");
        assertEquals("xmlRpcUrl", probe.getXmlRpcUrl());
    }

    @Test
    public void testToString() {
        ProbeInformation probe = new ProbeInformation("targetName", "targetType", "bmClass", "bmName", "bmDescription", "probeName", 10, "xmlRpcUrl");
        String expected = "ProbeInformation{targetName='targetName', targetType='targetType', bmClass='bmClass', bmName='bmName', bmDescription='bmDescription', probeDescription='probeName', precision=10}";
        assertEquals(expected, probe.toString());
    }

    @Test
    public void testEquals() {
        ProbeInformation probe1 = new ProbeInformation("targetName", "targetType", "bmClass", "bmName", "bmDescription", "probeName", 10, "xmlRpcUrl");
        ProbeInformation probe2 = new ProbeInformation("targetName", "targetType", "bmClass", "bmName", "bmDescription", "probeName", 10, "xmlRpcUrl");
        assertTrue(probe1.equals(probe2));
    }

    @Test
    public void testEqualsDifferentPrecision() {
        ProbeInformation probe1 = new ProbeInformation("targetName", "targetType", "bmClass", "bmName", "bmDescription", "probeName", 10, "xmlRpcUrl");
        ProbeInformation probe2 = new ProbeInformation("targetName", "targetType", "bmClass", "bmName", "bmDescription", "probeName", 5, "xmlRpcUrl");
        assertFalse(probe1.equals(probe2));
    }

    @Test
    public void testEqualsNullBmClass() {
        ProbeInformation probe1 = new ProbeInformation("targetName", "targetType", null, "bmName", "bmDescription", "probeName", 10, "xmlRpcUrl");
        ProbeInformation probe2 = new ProbeInformation("targetName", "targetType", null, "bmName", "bmDescription", "probeName", 10, "xmlRpcUrl");
        assertTrue(probe1.equals(probe2));
    }

    @Test
    public void testEqualsNullBmDescription() {
        ProbeInformation probe1 = new ProbeInformation("targetName", "targetType", "bmClass", "bmName", null, "probeName", 10, "xmlRpcUrl");
        ProbeInformation probe2 = new ProbeInformation("targetName", "targetType", "bmClass", "bmName", null, "probeName", 10, "xmlRpcUrl");
        assertTrue(probe1.equals(probe2));
    }

    @Test
    public void testEqualsNullBmName() {
        ProbeInformation probe1 = new ProbeInformation("targetName", "targetType", "bmClass", null, "bmDescription", "probeName", 10, "xmlRpcUrl");
        ProbeInformation probe2 = new ProbeInformation("targetName", "targetType", "bmClass", null, "bmDescription", "probeName", 10, "xmlRpcUrl");
        assertTrue(probe1.equals(probe2));
    }

    @Test
    public void testEqualsNullProbeName() {
        ProbeInformation probe1 = new ProbeInformation("targetName", "targetType", "bmClass", "bmName", "bmDescription", null, 10, "xmlRpcUrl");
        ProbeInformation probe2 = new ProbeInformation("targetName", "targetType", "bmClass", "bmName", "bmDescription", null, 10, "xmlRpcUrl");
        assertTrue(probe1.equals(probe2));
    }

    @Test
    public void testEqualsNullTargetName() {
        ProbeInformation probe1 = new ProbeInformation(null, "targetType", "bmClass", "bmName", "bmDescription", "probeName", 10, "xmlRpcUrl");
        ProbeInformation probe2 = new ProbeInformation(null, "targetType", "bmClass", "bmName", "bmDescription", "probeName", 10, "xmlRpcUrl");
        assertTrue(probe1.equals(probe2));
    }

    @Test
    public void testEqualsNullTargetType() {
        ProbeInformation probe1 = new ProbeInformation("targetName", null, "bmClass", "bmName", "bmDescription", "probeName", 10, "xmlRpcUrl");
        ProbeInformation probe2 = new ProbeInformation("targetName", null, "bmClass", "bmName", "bmDescription", "probeName", 10, "xmlRpcUrl");
        assertTrue(probe1.equals(probe2));
    }

    @Test
    public void testHashCode() {
        ProbeInformation probe1 = new ProbeInformation("targetName", "targetType", "bmClass", "bmName", "bmDescription", "probeName", 10, "xmlRpcUrl");
        ProbeInformation probe2 = new ProbeInformation("targetName", "targetType", "bmClass", "bmName", "bmDescription", "probeName", 10, "xmlRpcUrl");
        assertEquals(probe1.hashCode(), probe2.hashCode());
    }
}