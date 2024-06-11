import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ProbeInformationTest {

    private ProbeInformation probeInformation;

    @Before
    public void setUp() {
        probeInformation = new ProbeInformation("TargetName", "TargetType", "BmClass",
                "BmName", "BmDescription", "ProbeName", 5, "XmlRpcUrl");
    }

    @Test
    public void testGetTargetType() {
        assertEquals("TargetType", probeInformation.getTargetType());
    }

    @Test
    public void testGetBmName() {
        assertEquals("BmName", probeInformation.getBmName());
    }

    @Test
    public void testGetProbeName() {
        assertEquals("ProbeName", probeInformation.getProbeName());
    }

    @Test
    public void testGetTargetName() {
        assertEquals("TargetName", probeInformation.getTargetName());
    }

    @Test
    public void testGetBmClass() {
        assertEquals("BmClass", probeInformation.getBmClass());
    }

    @Test
    public void testGetBmDescription() {
        assertEquals("BmDescription", probeInformation.getBmDescription());
    }

    @Test
    public void testGetPrecision() {
        assertEquals(5, probeInformation.getPrecision());
    }

    @Test
    public void testGetMeasureURI() {
        assertEquals("expectedValue", probeInformation.getMeasureURI());
    }

    @Test
    public void testGetXmlRpcUrl() {
        assertEquals("XmlRpcUrl", probeInformation.getXmlRpcUrl());
    }

    @Test
    public void testToString() {
        String expected = "ProbeInformation{targetName='TargetName', targetType='TargetType', " +
                "bmClass='BmClass', bmName='BmName', bmDescription='BmDescription', " +
                "probeDescription='ProbeName', precision=5}";
        assertEquals(expected, probeInformation.toString());
    }

    @Test
    public void testEquals() {
        ProbeInformation sameProbeInformation = new ProbeInformation("TargetName", "TargetType",
                "BmClass", "BmName", "BmDescription", "ProbeName", 5, "XmlRpcUrl");
        assertEquals(true, probeInformation.equals(sameProbeInformation));
    }

    @Test
    public void testHashCode() {
        ProbeInformation sameProbeInformation = new ProbeInformation("TargetName", "TargetType",
                "BmClass", "BmName", "BmDescription", "ProbeName", 5, "XmlRpcUrl");
        assertEquals(probeInformation.hashCode(), sameProbeInformation.hashCode());
    }
}