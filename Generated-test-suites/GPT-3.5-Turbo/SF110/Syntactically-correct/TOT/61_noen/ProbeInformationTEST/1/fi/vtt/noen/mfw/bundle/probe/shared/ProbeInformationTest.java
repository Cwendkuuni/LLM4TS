import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class ProbeInformationTest {

    private ProbeInformation probeInfo;
    
    @Before
    public void setUp() {
        probeInfo = new ProbeInformation("Target1", "Type1", "Class1", "Name1", "Description1", "Probe1", 1, "xmlRpcUrl1");
    }
    
    @Test
    public void testGetTargetType() {
        assertEquals("Type1", probeInfo.getTargetType());
    }
    
    @Test
    public void testGetBmName() {
        assertEquals("Name1", probeInfo.getBmName());
    }
    
    @Test
    public void testGetProbeName() {
        assertEquals("Probe1", probeInfo.getProbeName());
    }
    
    @Test
    public void testGetTargetName() {
        assertEquals("Target1", probeInfo.getTargetName());
    }
    
    @Test
    public void testGetBmClass() {
        assertEquals("Class1", probeInfo.getBmClass());
    }
    
    @Test
    public void testGetBmDescription() {
        assertEquals("Description1", probeInfo.getBmDescription());
    }
    
    @Test
    public void testGetPrecision() {
        assertEquals(1, probeInfo.getPrecision());
    }
    
    @Test
    public void testGetMeasureURI() {
        String expectedMeasureURI = "Type1_Target1_Class1_Name1";
        assertEquals(expectedMeasureURI, probeInfo.getMeasureURI());
    }
    
    @Test
    public void testGetXmlRpcUrl() {
        assertEquals("xmlRpcUrl1", probeInfo.getXmlRpcUrl());
    }
    
    @Test
    public void testToString() {
        String expectedToString = "ProbeInformation{targetName='Target1', targetType='Type1', bmClass='Class1', bmName='Name1', bmDescription='Description1', probeDescription='Probe1', precision=1}";
        assertEquals(expectedToString, probeInfo.toString());
    }
    
    @Test
    public void testEquals() {
        ProbeInformation sameProbeInfo = new ProbeInformation("Target1", "Type1", "Class1", "Name1", "Description1", "Probe1", 1, "xmlRpcUrl1");
        assertEquals(true, probeInfo.equals(sameProbeInfo));
    }
    
    @Test
    public void testHashCode() {
        ProbeInformation sameProbeInfo = new ProbeInformation("Target1", "Type1", "Class1", "Name1", "Description1", "Probe1", 1, "xmlRpcUrl1");
        assertEquals(probeInfo.hashCode(), sameProbeInfo.hashCode());
    }
}