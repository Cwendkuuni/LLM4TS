import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import org.junit.Test;

public class ProbeInformationTest {

    private ProbeInformation probeInformation;

    @Before
    public void setUp() {
        probeInformation = new ProbeInformation("TargetName", "TargetType", "BmClass", "BmName", "BmDescription", "ProbeDescription", 10, "http://example.com");
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
        assertEquals("ProbeDescription", probeInformation.getProbeName());
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
        assertEquals(10, probeInformation.getPrecision());
    }

    @Test
    public void testGetMeasureURI() {
        String expectedMeasureURI = "targetType/TargetName/BmClass/BmName";
        assertEquals(expectedMeasureURI, probeInformation.getMeasureURI());
    }

    @Test
    public void testGetXmlRpcUrl() {
        assertEquals("http://example.com", probeInformation.getXmlRpcUrl());
    }

    @Test
    public void testEquals() {
        ProbeInformation sameProbeInformation = new ProbeInformation("TargetName", "TargetType", "BmClass", "BmName", "BmDescription", "ProbeDescription", 10, "http://example.com");
        ProbeInformation differentProbeInformation = new ProbeInformation("Different", "Different", "Different", "Different", "Different", "Different", 5, "http://example.com");

        assertEquals(probeInformation, sameProbeInformation);
        assertNotEquals(probeInformation, differentProbeInformation);
    }

    @Test
    public void testHashCode() {
        ProbeInformation sameProbeInformation = new ProbeInformation("TargetName", "TargetType", "BmClass", "BmName", "BmDescription", "ProbeDescription", 10, "http://example.com");

        assertEquals(probeInformation.hashCode(), sameProbeInformation.hashCode());
    }
}