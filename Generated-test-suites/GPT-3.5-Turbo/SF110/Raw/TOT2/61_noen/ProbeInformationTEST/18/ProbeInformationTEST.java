import org.junit.Test;
import static org.junit.Assert.*;

public class ProbeInformationTest {

    private ProbeInformation probeInformation;

    @Before
    public void setUp() {
        probeInformation = new ProbeInformation("TargetName", "TargetType", "BMClass", "BMName", "BMDescription", "ProbeDescription", 5, "http://example.com");
    }

    @Test
    public void testGetTargetType() {
        assertEquals("TargetType", probeInformation.getTargetType());
    }

    @Test
    public void testGetBmName() {
        assertEquals("BMName", probeInformation.getBmName());
    }

    @Test
    public void testGetProbeName() {
        assertEquals("ProbeDescription", probeInformation.getProbeName());
    }

    @Test
    public void testGetTargetName() {
        assertEquals("TargetName", probeInformation.getTargetName());
    }

    @Test
    public void testGetBmClass() {
        assertEquals("BMClass", probeInformation.getBmClass());
    }

    @Test
    public void testGetBmDescription() {
        assertEquals("BMDescription", probeInformation.getBmDescription());
    }

    @Test
    public void testGetPrecision() {
        assertEquals(5, probeInformation.getPrecision());
    }

    @Test
    public void testGetMeasureURI() {
        assertEquals("http://example.com", probeInformation.getMeasureURI());
    }

    @Test
    public void testGetXmlRpcUrl() {
        assertEquals("http://example.com", probeInformation.getXmlRpcUrl());
    }

    @Test
    public void testToString() {
        String expected = "ProbeInformation{targetName='TargetName', targetType='TargetType', bmClass='BMClass', bmName='BMName', bmDescription='BMDescription', probeDescription='ProbeDescription', precision=5}";
        assertEquals(expected, probeInformation.toString());
    }

    @Test
    public void testEquals() {
        ProbeInformation sameProbeInformation = new ProbeInformation("TargetName", "TargetType", "BMClass", "BMName", "BMDescription", "ProbeDescription", 5, "http://example.com");
        assertEquals(probeInformation, sameProbeInformation);
    }

    @Test
    public void testHashCode() {
        ProbeInformation sameProbeInformation = new ProbeInformation("TargetName", "TargetType", "BMClass", "BMName", "BMDescription", "ProbeDescription", 5, "http://example.com");
        assertEquals(probeInformation.hashCode(), sameProbeInformation.hashCode());
    }
}