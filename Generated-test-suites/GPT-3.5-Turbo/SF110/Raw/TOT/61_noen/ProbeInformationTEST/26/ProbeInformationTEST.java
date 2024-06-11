import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class ProbeInformationTest {

    private ProbeInformation probeInformation;

    @Before
    public void setUp() {
        probeInformation = new ProbeInformation("targetName", "targetType", "bmClass", "bmName", "bmDescription", "probeDescription", 5, "http://www.example.com");
    }

    @Test
    public void getTargetType_shouldReturnTargetType() {
        assertEquals("targetType", probeInformation.getTargetType());
    }

    @Test
    public void getBmName_shouldReturnBmName() {
        assertEquals("bmName", probeInformation.getBmName());
    }

    @Test
    public void getProbeName_shouldReturnProbeName() {
        assertEquals("probeDescription", probeInformation.getProbeName());
    }

    @Test
    public void getTargetName_shouldReturnTargetName() {
        assertEquals("targetName", probeInformation.getTargetName());
    }

    @Test
    public void getBmClass_shouldReturnBmClass() {
        assertEquals("bmClass", probeInformation.getBmClass());
    }

    @Test
    public void getBmDescription_shouldReturnBmDescription() {
        assertEquals("bmDescription", probeInformation.getBmDescription());
    }

    @Test
    public void getPrecision_shouldReturnPrecision() {
        assertEquals(5, probeInformation.getPrecision());
    }

    @Test
    public void getMeasureURI_shouldReturnMeasureURI() {
        assertEquals("expectedMeasureURI", probeInformation.getMeasureURI());
    }

    @Test
    public void getXmlRpcUrl_shouldReturnXmlRpcUrl() {
        assertEquals("http://www.example.com", probeInformation.getXmlRpcUrl());
    }

    @Test
    public void toString_shouldReturnExpectedStringRepresentation() {
        String expectedString = "ProbeInformation{" +
                "targetName='targetName', " +
                "targetType='targetType', " +
                "bmClass='bmClass', " +
                "bmName='bmName', " +
                "bmDescription='bmDescription', " +
                "probeDescription='probeDescription', " +
                "precision=5" +
                "}";
        assertEquals(expectedString, probeInformation.toString());
    }

    @Test
    public void equals_shouldReturnTrueForEqualObjects() {
        ProbeInformation equalProbeInformation = new ProbeInformation("targetName", "targetType", "bmClass", "bmName", "bmDescription", "probeDescription", 5, "http://www.example.com");
        assertTrue(probeInformation.equals(equalProbeInformation));
    }

    @Test
    public void equals_shouldReturnFalseForDifferentObjects() {
        ProbeInformation differentProbeInformation = new ProbeInformation("differentTargetName", "targetType", "bmClass", "bmName", "bmDescription", "probeDescription", 5, "http://www.example.com");
        assertFalse(probeInformation.equals(differentProbeInformation));
    }

    @Test
    public void equals_shouldReturnFalseForNull() {
        assertFalse(probeInformation.equals(null));
    }

    @Test
    public void equals_shouldReturnFalseForDifferentClass() {
        assertFalse(probeInformation.equals(new Object()));
    }

    @Test
    public void hashCode_shouldReturnSameValueForEqualObjects() {
        ProbeInformation equalProbeInformation = new ProbeInformation("targetName", "targetType", "bmClass", "bmName", "bmDescription", "probeDescription", 5, "http://www.example.com");
        assertEquals(probeInformation.hashCode(), equalProbeInformation.hashCode());
    }

    @Test
    public void hashCode_shouldReturnDifferentValueForDifferentObjects() {
        ProbeInformation differentProbeInformation = new ProbeInformation("differentTargetName", "targetType", "bmClass", "bmName", "bmDescription", "probeDescription", 5, "http://www.example.com");
        assertNotEquals(probeInformation.hashCode(), differentProbeInformation.hashCode());
    }
}