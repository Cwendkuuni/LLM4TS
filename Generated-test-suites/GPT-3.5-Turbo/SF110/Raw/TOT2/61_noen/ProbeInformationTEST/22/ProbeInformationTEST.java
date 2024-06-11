import static org.junit.Assert.*;
import org.junit.Test;

public class ProbeInformationTest {

    ProbeInformation probeInfo = new ProbeInformation("TargetName", "TargetType", "BmClass", "BmName", "BmDescription", "ProbeName", 10, "http://example.com");

    @Test
    public void testGetTargetType() {
        assertEquals("TargetType", probeInfo.getTargetType());
    }

    @Test
    public void testGetBmName() {
        assertEquals("BmName", probeInfo.getBmName());
    }

    @Test
    public void testGetProbeName() {
        assertEquals("ProbeName", probeInfo.getProbeName());
    }

    @Test
    public void testGetTargetName() {
        assertEquals("TargetName", probeInfo.getTargetName());
    }

    @Test
    public void testGetBmClass() {
        assertEquals("BmClass", probeInfo.getBmClass());
    }

    @Test
    public void testGetBmDescription() {
        assertEquals("BmDescription", probeInfo.getBmDescription());
    }

    @Test
    public void testGetPrecision() {
        assertEquals(10, probeInfo.getPrecision());
    }

    @Test
    public void testGetMeasureURI() {
        assertEquals("TargetType/TargetName/BmClass/BmName", probeInfo.getMeasureURI());
    }

    @Test
    public void testGetXmlRpcUrl() {
        assertEquals("http://example.com", probeInfo.getXmlRpcUrl());
    }

    @Test
    public void testEquals_SameObject() {
        assertTrue(probeInfo.equals(probeInfo));
    }

    @Test
    public void testEquals_SameDetails() {
        ProbeInformation sameProbeInfo = new ProbeInformation("TargetName", "TargetType", "BmClass", "BmName", "BmDescription", "ProbeName", 10, "http://example.com");
        assertTrue(probeInfo.equals(sameProbeInfo));
    }

    @Test
    public void testEquals_DifferentDetails() {
         ProbeInformation diffProbeInfo = new ProbeInformation("DiffName", "DiffType", "DiffClass", "DiffBmName", "DiffBmDescription", "DiffProbeName", 5, "http://diff.com");
        assertFalse(probeInfo.equals(diffProbeInfo));
    }

    @Test
    public void testHashCode() {
        ProbeInformation sameProbeInfo = new ProbeInformation("TargetName", "TargetType", "BmClass", "BmName", "BmDescription", "ProbeName", 10, "http://example.com");
        assertEquals(probeInfo.hashCode(), sameProbeInfo.hashCode());
    }

}