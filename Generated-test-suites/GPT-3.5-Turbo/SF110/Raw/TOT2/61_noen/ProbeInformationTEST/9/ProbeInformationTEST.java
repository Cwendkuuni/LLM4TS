import org.junit.Test;
import static org.junit.Assert.*;

public class ProbeInformationTest {

    ProbeInformation probe = new ProbeInformation("Target1", "Type1", "Class1", "Name1", "Description1", "Probe1", 10, "http://example.com");

    @Test
    public void testGetTargetType() {
        assertEquals("Type1", probe.getTargetType());
    }

    @Test
    public void testGetBmName() {
        assertEquals("Name1", probe.getBmName());
    }

    @Test
    public void testGetProbeName() {
        assertEquals("Probe1", probe.getProbeName());
    }

    @Test
    public void testGetTargetName() {
        assertEquals("Target1", probe.getTargetName());
    }

    @Test
    public void testGetBmClass() {
        assertEquals("Class1", probe.getBmClass());
    }

    @Test
    public void testGetBmDescription() {
        assertEquals("Description1", probe.getBmDescription());
    }

    @Test
    public void testGetPrecision() {
        assertEquals(10, probe.getPrecision());
    }

    @Test
    public void testGetMeasureURI() {
        assertEquals("Type1:Target1:Class1:Name1", probe.getMeasureURI());
    }

    @Test
    public void testGetXmlRpcUrl() {
        assertEquals("http://example.com", probe.getXmlRpcUrl());
    }

    @Test
    public void testEquals() {
        ProbeInformation probe2 = new ProbeInformation("Target1", "Type1", "Class1", "Name1", "Description1", "Probe1", 10, "http://example.com");
        assertTrue(probe.equals(probe2));
    }

    @Test
    public void testHashCode() {
        ProbeInformation probe2 = new ProbeInformation("Target1", "Type1", "Class1", "Name1", "Description1", "Probe1", 10, "http://example.com");
        assertEquals(probe.hashCode(), probe2.hashCode());
    }
}