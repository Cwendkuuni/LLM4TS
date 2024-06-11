import org.junit.Test;
import static org.junit.Assert.*;

public class ProbeInformationTest {

    @Test
    public void testGetTargetType() {
        ProbeInformation probeInformation = new ProbeInformation("Target1", "Type1", "Class1", "Name1", "Description1", "Probe1", 2, "url1");
        assertEquals("Type1", probeInformation.getTargetType());
    }
    
    @Test
    public void testGetBmName() {
        ProbeInformation probeInformation = new ProbeInformation("Target1", "Type1", "Class1", "Name1", "Description1", "Probe1", 2, "url1");
        assertEquals("Name1", probeInformation.getBmName());
    }
    
    @Test
    public void testGetProbeName() {
        ProbeInformation probeInformation = new ProbeInformation("Target1", "Type1", "Class1", "Name1", "Description1", "Probe1", 2, "url1");
        assertEquals("Probe1", probeInformation.getProbeName());
    }
    
    @Test
    public void testGetTargetName() {
        ProbeInformation probeInformation = new ProbeInformation("Target1", "Type1", "Class1", "Name1", "Description1", "Probe1", 2, "url1");
        assertEquals("Target1", probeInformation.getTargetName());
    }
    
    @Test
    public void testGetBmClass() {
        ProbeInformation probeInformation = new ProbeInformation("Target1", "Type1", "Class1", "Name1", "Description1", "Probe1", 2, "url1");
        assertEquals("Class1", probeInformation.getBmClass());
    }
    
    @Test
    public void testGetBmDescription() {
        ProbeInformation probeInformation = new ProbeInformation("Target1", "Type1", "Class1", "Name1", "Description1", "Probe1", 2, "url1");
        assertEquals("Description1", probeInformation.getBmDescription());
    }
    
    @Test
    public void testGetPrecision() {
        ProbeInformation probeInformation = new ProbeInformation("Target1", "Type1", "Class1", "Name1", "Description1", "Probe1", 2, "url1");
        assertEquals(2, probeInformation.getPrecision());
    }
    
    @Test
    public void testGetMeasureURI() {
        ProbeInformation probeInformation = new ProbeInformation("Target1", "Type1", "Class1", "Name1", "Description1", "Probe1", 2, "url1");
        String measureURI = "custom measure uri"; //replace this with the actual expected measure uri based on inputs
        assertEquals(measureURI, probeInformation.getMeasureURI());
    }
    
    @Test
    public void testGetXmlRpcUrl() {
        ProbeInformation probeInformation = new ProbeInformation("Target1", "Type1", "Class1", "Name1", "Description1", "Probe1", 2, "url1");
        assertEquals("url1", probeInformation.getXmlRpcUrl());
    }
    
    @Test
    public void testEquals() {
        ProbeInformation probeInformation1 = new ProbeInformation("Target1", "Type1", "Class1", "Name1", "Description1", "Probe1", 2, "url1");
        ProbeInformation probeInformation2 = new ProbeInformation("Target1", "Type1", "Class1", "Name1", "Description1", "Probe1", 2, "url1");
        assertTrue(probeInformation1.equals(probeInformation2));
    }
    
    @Test
    public void testNotEquals() {
        ProbeInformation probeInformation1 = new ProbeInformation("Target1", "Type1", "Class1", "Name1", "Description1", "Probe1", 2, "url1");
        ProbeInformation probeInformation2 = new ProbeInformation("Target2", "Type2", "Class2", "Name2", "Description2", "Probe2", 3, "url2");
        assertFalse(probeInformation1.equals(probeInformation2));
    }
    
    @Test
    public void testHashCode() {
        ProbeInformation probeInformation1 = new ProbeInformation("Target1", "Type1", "Class1", "Name1", "Description1", "Probe1", 2, "url1");
        ProbeInformation probeInformation2 = new ProbeInformation("Target1", "Type1", "Class1", "Name1", "Description1", "Probe1", 2, "url1");
        assertEquals(probeInformation1.hashCode(), probeInformation2.hashCode());
    }
}