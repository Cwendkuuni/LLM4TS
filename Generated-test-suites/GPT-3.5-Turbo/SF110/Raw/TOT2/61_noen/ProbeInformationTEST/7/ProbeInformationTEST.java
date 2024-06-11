import static org.junit.Assert.*;
import org.junit.Test;

public class ProbeInformationTest {
    
    private ProbeInformation probeInfo;

    @Before
    public void setUp() {
        probeInfo = new ProbeInformation("Target", "Type", "Class", "Name", "Description", "Probe", 5, "http://example.com");
    }

    @Test
    public void testGetTargetType() {
        assertEquals("Type", probeInfo.getTargetType());
    }

    @Test
    public void testGetBmName() {
        assertEquals("Name", probeInfo.getBmName());
    }

    @Test
    public void testGetProbeName() {
        assertEquals("Probe", probeInfo.getProbeName());
    }

    @Test
    public void testGetTargetName() {
        assertEquals("Target", probeInfo.getTargetName());
    }

    @Test
    public void testGetBmClass() {
        assertEquals("Class", probeInfo.getBmClass());
    }

    @Test
    public void testGetBmDescription() {
        assertEquals("Description", probeInfo.getBmDescription());
    }

    @Test
    public void testGetPrecision() {
        assertEquals(5, probeInfo.getPrecision());
    }

    @Test
    public void testGetMeasureURI() {
        assertEquals("Type/Target/Class/Name", probeInfo.getMeasureURI());
    }

    @Test
    public void testGetXmlRpcUrl() {
        assertEquals("http://example.com", probeInfo.getXmlRpcUrl());
    }

    @Test
    public void testToString() {
        assertEquals("ProbeInformation{targetName='Target', targetType='Type', bmClass='Class', bmName='Name', bmDescription='Description', probeDescription='Probe', precision=5}", probeInfo.toString());
    }

    @Test
    public void testEquals() {
        ProbeInformation sameProbeInfo = new ProbeInformation("Target", "Type", "Class", "Name", "Description", "Probe", 5, "http://example.com");
        ProbeInformation differentProbeInfo = new ProbeInformation("Different", "Type", "Class", "Name", "Description", "Probe", 5, "http://example.com");

        assertTrue(probeInfo.equals(sameProbeInfo));
        assertFalse(probeInfo.equals(differentProbeInfo));
    }

    @Test
    public void testHashCode() {
        ProbeInformation sameProbeInfo = new ProbeInformation("Target", "Type", "Class", "Name", "Description", "Probe", 5, "http://example.com");
        assertEquals(probeInfo.hashCode(), sameProbeInfo.hashCode());
    }
}