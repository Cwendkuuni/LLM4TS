import org.junit.Test;
import static org.junit.Assert.*;

public class ProbeInformationTest {

    @Test
    public void testGetTargetType() {
        ProbeInformation probeInfo = new ProbeInformation(null, "Type", null, null, null, null, 0, null);
        assertEquals("Type", probeInfo.getTargetType());
    }

    @Test
    public void testGetBmName() {
        ProbeInformation probeInfo = new ProbeInformation(null, null, null, "BmName", null, null, 0, null);
        assertEquals("BmName", probeInfo.getBmName());
    }

    @Test
    public void testGetProbeName() {
        ProbeInformation probeInfo = new ProbeInformation(null, null, null, null, null, "ProbeName", 0, null);
        assertEquals("ProbeName", probeInfo.getProbeName());
    }

    @Test
    public void testGetTargetName() {
        ProbeInformation probeInfo = new ProbeInformation("TargetName", null, null, null, null, null, 0, null);
        assertEquals("TargetName", probeInfo.getTargetName());
    }

    @Test
    public void testGetBmClass() {
        ProbeInformation probeInfo = new ProbeInformation(null, null, "BmClass", null, null, null, 0, null);
        assertEquals("BmClass", probeInfo.getBmClass());
    }

    @Test
    public void testGetBmDescription() {
        ProbeInformation probeInfo = new ProbeInformation(null, null, null, null, "BmDescription", null, 0, null);
        assertEquals("BmDescription", probeInfo.getBmDescription());
    }

    @Test
    public void testGetPrecision() {
        ProbeInformation probeInfo = new ProbeInformation(null, null, null, null, null, null, 10, null);
        assertEquals(10, probeInfo.getPrecision());
    }

    @Test
    public void testGetMeasureURI() {
        ProbeInformation probeInfo = new ProbeInformation("TargetName", "Type", "BmClass", "BmName", null, null, 0, null);
        String expectedMeasureURI = "measure://Type/TargetName/BmClass/BmName";
        assertEquals(expectedMeasureURI, probeInfo.getMeasureURI());
    }

    @Test
    public void testGetXmlRpcUrl() {
        ProbeInformation probeInfo = new ProbeInformation(null, null, null, null, null, null, 0, "http://example.com");
        assertEquals("http://example.com", probeInfo.getXmlRpcUrl());
    }

    @Test
    public void testEquals() {
        ProbeInformation probeInfo1 = new ProbeInformation("Target", "Type", "Class", "Name", "Desc", "ProbeName", 10, "URL");
        ProbeInformation probeInfo2 = new ProbeInformation("Target", "Type", "Class", "Name", "Desc", "ProbeName", 10, "URL");
        ProbeInformation probeInfo3 = new ProbeInformation("OtherTarget", "Type", "Class", "Name", "Desc", "ProbeName", 10, "URL");

        assertTrue(probeInfo1.equals(probeInfo2));
        assertFalse(probeInfo1.equals(probeInfo3));
    }

    @Test
    public void testHashCode() {
        ProbeInformation probeInfo1 = new ProbeInformation("Target", "Type", "Class", "Name", null, null, 10, null);
        ProbeInformation probeInfo2 = new ProbeInformation("Target", "Type", "Class", "Name", null, null, 10, null);

        assertEquals(probeInfo1.hashCode(), probeInfo2.hashCode());
    }

}