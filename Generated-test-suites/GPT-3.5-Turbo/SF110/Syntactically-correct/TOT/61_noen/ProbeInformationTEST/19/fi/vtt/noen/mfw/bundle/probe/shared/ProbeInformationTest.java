import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class ProbeInformationTest {
    private ProbeInformation probe;

    @Before
    public void setUp() {
        probe = new ProbeInformation("targetName", "targetType", "bmClass", "bmName", "bmDescription", "probeDescription", 5, "xmlRpcUrl");
    }

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
        assertEquals(5, probe.getPrecision());
    }

    @Test
    public void testGetMeasureURI() {
        String expectedMeasureURI = "createMeasureURI(targetType, targetName, bmClass, bmName)";
        assertEquals(expectedMeasureURI, probe.getMeasureURI());
    }

    @Test
    public void testGetXmlRpcUrl() {
        assertEquals("xmlRpcUrl", probe.getXmlRpcUrl());
    }

    @Test
    public void testToString() {
        String expectedString = "ProbeInformation{targetName='targetName', targetType='targetType', bmClass='bmClass', bmName='bmName', bmDescription='bmDescription', probeDescription='probeDescription', precision=5}";
        assertEquals(expectedString, probe.toString());
    }

    @Test
    public void testEquals_SameObject() {
        assertEquals(probe, probe);
    }

    @Test
    public void testEquals_NullObject() {
        assertNotEquals(probe, null);
    }

    @Test
    public void testEquals_DifferentClassObject() {
        assertNotEquals(probe, new Object());
    }

    @Test
    public void testEquals_EqualObjects() {
        ProbeInformation equalProbe = new ProbeInformation("targetName", "targetType", "bmClass", "bmName", "bmDescription", "probeDescription", 5, "xmlRpcUrl");
        assertEquals(probe, equalProbe);
    }

    @Test
    public void testEquals_DifferentTargetName() {
        ProbeInformation differentProbe = new ProbeInformation("differentName", "targetType", "bmClass", "bmName", "bmDescription", "probeDescription", 5, "xmlRpcUrl");
        assertNotEquals(probe, differentProbe);
    }

    @Test
    public void testEquals_DifferentTargetType() {
        ProbeInformation differentProbe = new ProbeInformation("targetName", "differentType", "bmClass", "bmName", "bmDescription", "probeDescription", 5, "xmlRpcUrl");
        assertNotEquals(probe, differentProbe);
    }

    // Add more edge cases and error scenarios for other methods if required

}