import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class ProbeInformationTest {

    @Test
    public void testGetTargetType() {
        // Typical use case
        ProbeInformation probe = new ProbeInformation("Target 1", "Type 1", "Class 1", "Name 1", "Description 1", "Probe 1", 2, "url");
        String expected = "Type 1";
        assertEquals(expected, probe.getTargetType());
    }

    @Test
    public void testGetBmName() {
        // Typical use case
        ProbeInformation probe = new ProbeInformation("Target 2", "Type 2", "Class 2", "Name 2", "Description 2", "Probe 2", 3, "url");
        String expected = "Name 2";
        assertEquals(expected, probe.getBmName());
    }

    @Test
    public void testGetProbeName() {
        // Edge case: Probe name is null
        ProbeInformation probe = new ProbeInformation("Target 3", "Type 3", "Class 3", "Name 3", "Description 3", null, 4, "url");
        String expected = null;
        assertEquals(expected, probe.getProbeName());
    }

    @Test
    public void testGetTargetName() {
        // Typical use case
        ProbeInformation probe = new ProbeInformation("Target 4", "Type 4", "Class 4", "Name 4", "Description 4", "Probe 4", 5, "url");
        String expected = "Target 4";
        assertEquals(expected, probe.getTargetName());
    }

    @Test
    public void testGetBmClass() {
        // Edge case: BM class is empty string
        ProbeInformation probe = new ProbeInformation("Target 5", "Type 5", "", "Name 5", "Description 5", "Probe 5", 6, "url");
        String expected = "";
        assertEquals(expected, probe.getBmClass());
    }

    @Test
    public void testGetBmDescription() {
        // Error scenario: BM description is too long
        ProbeInformation probe = new ProbeInformation("Target 6", "Type 6", "Class 6", "Name 6", "This is a very long description that exceeds the maximum character limit", "Probe 6", 7, "url");
        String expected = "This is a very long description that exceeds the maximum character limit";
        assertEquals(expected, probe.getBmDescription());
    }

    @Test
    public void testGetPrecision() {
        // Typical use case
        ProbeInformation probe = new ProbeInformation("Target 7", "Type 7", "Class 7", "Name 7", "Description 7", "Probe 7", 8, "url");
        int expected = 8;
        assertEquals(expected, probe.getPrecision());
    }

    @Test
    public void testGetMeasureURI() {
        // Typical use case
        ProbeInformation probe = new ProbeInformation("Target 8", "Type 8", "Class 8", "Name 8", "Description 8", "Probe 8", 9, "url");
        String expected = "measure:Type 8:Target 8:Class 8:Name 8";
        assertEquals(expected, probe.getMeasureURI());
    }

    @Test
    public void testGetXmlRpcUrl() {
        // Error scenario: XML-RPC URL is null
        ProbeInformation probe = new ProbeInformation("Target 9", "Type 9", "Class 9", "Name 9", "Description 9", "Probe 9", 10, null);
        String expected = null;
        assertEquals(expected, probe.getXmlRpcUrl());
    }

    @Test
    public void testToString() {
        // Typical use case
        ProbeInformation probe = new ProbeInformation("Target 10", "Type 10", "Class 10", "Name 10", "Description 10", "Probe 10", 11, "url");
        String expected = "ProbeInformation{targetName='Target 10', targetType='Type 10', bmClass='Class 10', bmName='Name 10', bmDescription='Description 10', probeDescription='Probe 10', precision=11}";
        assertEquals(expected, probe.toString());
    }

    @Test
    public void testEquals() {
        // Typical use case: Two ProbeInformation objects with same attribute values
        ProbeInformation probe1 = new ProbeInformation("Target 11", "Type 11", "Class 11", "Name 11", "Description 11", "Probe 11", 12, "url");
        ProbeInformation probe2 = new ProbeInformation("Target 11", "Type 11", "Class 11", "Name 11", "Description 11", "Probe 11", 12, "url");
        assertEquals(probe1, probe2);
    }

    @Test
    public void testHashCode() {
        // Typical use case: Two ProbeInformation objects with same attribute values
        ProbeInformation probe1 = new ProbeInformation("Target 12", "Type 12", "Class 12", "Name 12", "Description 12", "Probe 12", 13, "url");
        ProbeInformation probe2 = new ProbeInformation("Target 12", "Type 12", "Class 12", "Name 12", "Description 12", "Probe 12", 13, "url");
        assertEquals(probe1.hashCode(), probe2.hashCode());
    }
}