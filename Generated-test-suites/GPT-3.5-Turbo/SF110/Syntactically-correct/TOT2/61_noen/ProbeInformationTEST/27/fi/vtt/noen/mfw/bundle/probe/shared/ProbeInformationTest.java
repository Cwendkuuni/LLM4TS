package fi.vtt.noen.mfw.bundle.probe.shared;
import static org.junit.Assert.*;
import org.junit.Test;

public class ProbeInformationTest {

    @Test
    public void testGetTargetType() {
        ProbeInformation probeInfo = new ProbeInformation("target1", "type1", "class1", "name1", "desc1", "probe1", 5, "url1");
        assertEquals("type1", probeInfo.getTargetType());
    }

    @Test
    public void testGetBmName() {
        ProbeInformation probeInfo = new ProbeInformation("target1", "type1", "class1", "name1", "desc1", "probe1", 5, "url1");
        assertEquals("name1", probeInfo.getBmName());
    }

    @Test
    public void testGetProbeName() {
        ProbeInformation probeInfo = new ProbeInformation("target1", "type1", "class1", "name1", "desc1", "probe1", 5, "url1");
        assertEquals("probe1", probeInfo.getProbeName());
    }

    @Test
    public void testGetTargetName() {
        ProbeInformation probeInfo = new ProbeInformation("target1", "type1", "class1", "name1", "desc1", "probe1", 5, "url1");
        assertEquals("target1", probeInfo.getTargetName());
    }

    @Test
    public void testGetBmClass() {
        ProbeInformation probeInfo = new ProbeInformation("target1", "type1", "class1", "name1", "desc1", "probe1", 5, "url1");
        assertEquals("class1", probeInfo.getBmClass());
    }

    @Test
    public void testGetBmDescription() {
        ProbeInformation probeInfo = new ProbeInformation("target1", "type1", "class1", "name1", "desc1", "probe1", 5, "url1");
        assertEquals("desc1", probeInfo.getBmDescription());
    }

    @Test
    public void testGetPrecision() {
        ProbeInformation probeInfo = new ProbeInformation("target1", "type1", "class1", "name1", "desc1", "probe1", 5, "url1");
        assertEquals(5, probeInfo.getPrecision());
    }

    @Test
    public void testGetMeasureURI() {
        ProbeInformation probeInfo = new ProbeInformation("target1", "type1", "class1", "name1", "desc1", "probe1", 5, "url1");
        assertEquals("type1/target1/class1/name1", probeInfo.getMeasureURI());
    }

    @Test
    public void testGetXmlRpcUrl() {
        ProbeInformation probeInfo = new ProbeInformation("target1", "type1", "class1", "name1", "desc1", "probe1", 5, "url1");
        assertEquals("url1", probeInfo.getXmlRpcUrl());
    }

    // Test equals method
    @Test
    public void testEquals() {
        ProbeInformation probeInfo1 = new ProbeInformation("target1", "type1", "class1", "name1", "desc1", "probe1", 5, "url1");
        ProbeInformation probeInfo2 = new ProbeInformation("target1", "type1", "class1", "name1", "desc1", "probe1", 5, "url1");
        assertTrue(probeInfo1.equals(probeInfo2));
    }

    // Test hashCode method
    @Test
    public void testHashCode() {
        ProbeInformation probeInfo = new ProbeInformation("target1", "type1", "class1", "name1", "desc1", "probe1", 5, "url1");
        assertEquals(probeInfo.hashCode(), probeInfo.hashCode());
    }
}