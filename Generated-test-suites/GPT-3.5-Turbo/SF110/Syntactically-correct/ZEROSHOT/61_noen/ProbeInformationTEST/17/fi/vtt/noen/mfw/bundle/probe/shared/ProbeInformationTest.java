import static org.junit.Assert.*;
import org.junit.Test;

public class ProbeInformationTest {

    ProbeInformation probe = new ProbeInformation("targetName", "targetType", "bmClass", 
    "bmName", "bmDescription", "probeDescription", 10, "xmlRpcUrl");

    @Test
    public void testGetTargetType() {
        assertEquals("targetType", probe.getTargetType());
    }

    @Test
    public void testGetBmName() {
        assertEquals("bmName", probe.getBmName());
    }

    @Test
    public void testGetProbeName() {
        assertEquals("probeDescription", probe.getProbeName());
    }

    @Test
    public void testGetTargetName() {
        assertEquals("targetName", probe.getTargetName());
    }

    @Test
    public void testGetBmClass() {
        assertEquals("bmClass", probe.getBmClass());
    }

    @Test
    public void testGetBmDescription() {
        assertEquals("bmDescription", probe.getBmDescription());
    }

    @Test
    public void testGetPrecision() {
        assertEquals(10, probe.getPrecision());
    }

    @Test
    public void testGetMeasureURI() {
        String expectedMeasureURI = "targetType:targetName:bmClass:bmName";
        assertEquals(expectedMeasureURI, probe.getMeasureURI());
    }

    @Test
    public void testGetXmlRpcUrl() {
        assertEquals("xmlRpcUrl", probe.getXmlRpcUrl());
    }

    @Test
    public void testToString() {
        String expectedToString = "ProbeInformation{targetName='targetName', targetType='targetType', bmClass='bmClass', bmName='bmName', bmDescription='bmDescription', probeDescription='probeDescription', precision=10}";
        assertEquals(expectedToString, probe.toString());
    }

    @Test
    public void testEquals() {
        ProbeInformation sameProbe = new ProbeInformation("targetName", "targetType", "bmClass", 
        "bmName", "bmDescription", "probeDescription", 10, "xmlRpcUrl");
        assertEquals(true, probe.equals(sameProbe));

        ProbeInformation differentProbe = new ProbeInformation("targetName", "targetType", "differentBmClass", 
        "bmName", "bmDescription", "probeDescription", 10, "xmlRpcUrl");
        assertEquals(false, probe.equals(differentProbe));
    }

    @Test
    public void testHashCode() {
        ProbeInformation sameProbe = new ProbeInformation("targetName", "targetType", "bmClass", 
        "bmName", "bmDescription", "probeDescription", 10, "xmlRpcUrl");

        assertEquals(probe.hashCode(), sameProbe.hashCode());
    }
}