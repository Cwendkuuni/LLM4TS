import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class ProbeInformationTest {
    
    private ProbeInformation probeInfo;
    
    @Before
    public void setUp() {
        probeInfo = new ProbeInformation("Target 1", "Type 1", "Class 1", "Name 1", "Description 1", "Probe 1", 10, "URL 1");
    }
    
    @Test
    public void testGetTargetType() {
        assertEquals("Type 1", probeInfo.getTargetType());
    }
    
    @Test
    public void testGetBmName() {
        assertEquals("Name 1", probeInfo.getBmName());
    }
    
    @Test
    public void testGetProbeName() {
        assertEquals("Probe 1", probeInfo.getProbeName());
    }
    
    @Test
    public void testGetTargetName() {
        assertEquals("Target 1", probeInfo.getTargetName());
    }
    
    @Test
    public void testGetBmClass() {
        assertEquals("Class 1", probeInfo.getBmClass());
    }
    
    @Test
    public void testGetBmDescription() {
        assertEquals("Description 1", probeInfo.getBmDescription());
    }
    
    @Test
    public void testGetPrecision() {
        assertEquals(10, probeInfo.getPrecision());
    }
    
    @Test
    public void testGetMeasureURI() {
        assertEquals("Type 1-Target 1-Class 1-Name 1", probeInfo.getMeasureURI());
    }
    
    @Test
    public void testGetXmlRpcUrl() {
        assertEquals("URL 1", probeInfo.getXmlRpcUrl());
    }
    
    @Test
    public void testToString() {
        String expectedString = "ProbeInformation{targetName='Target 1', targetType='Type 1', bmClass='Class 1', bmName='Name 1', bmDescription='Description 1', probeDescription='Probe 1', precision=10}";
        assertEquals(expectedString, probeInfo.toString());
    }
    
    @Test
    public void testEquals() {
        ProbeInformation sameProbeInfo = new ProbeInformation("Target 1", "Type 1", "Class 1", "Name 1", "Description 1", "Probe 1", 10, "URL 1");
        ProbeInformation differentProbeInfo = new ProbeInformation("Target 2", "Type 2", "Class 2", "Name 2", "Description 2", "Probe 2", 20, "URL 2");
        
        assertTrue(probeInfo.equals(sameProbeInfo));
        assertFalse(probeInfo.equals(differentProbeInfo));
    }
    
    @Test
    public void testHashCode() {
        int expectedHashCode = probeInfo.getTargetName().hashCode() +
                                probeInfo.getTargetType().hashCode() +
                                probeInfo.getBmClass().hashCode() +
                                probeInfo.getBmName().hashCode() +
                                probeInfo.getBmDescription().hashCode() +
                                probeInfo.getProbeName().hashCode() +
                                probeInfo.getPrecision();
        assertEquals(expectedHashCode, probeInfo.hashCode());
    }
}