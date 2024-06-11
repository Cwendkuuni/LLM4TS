import org.junit.Test;
import static org.junit.Assert.*;

public class ProbeInformationTest {
    
    @Test
    public void testGetTargetType() {
        ProbeInformation probeInfo = new ProbeInformation("Target1", "Type1", "Class1", "Name1", "Description1", "Probe1", 2, "url1");
        assertEquals("Type1", probeInfo.getTargetType());
    }
    
    @Test
    public void testGetBmName() {
        ProbeInformation probeInfo = new ProbeInformation("Target1", "Type1", "Class1", "Name1", "Description1", "Probe1", 2, "url1");
        assertEquals("Name1", probeInfo.getBmName());
    }
    
    @Test
    public void testGetProbeName() {
        ProbeInformation probeInfo = new ProbeInformation("Target1", "Type1", "Class1", "Name1", "Description1", "Probe1", 2, "url1");
        assertEquals("Probe1", probeInfo.getProbeName());
    }
    
    @Test
    public void testGetTargetName() {
        ProbeInformation probeInfo = new ProbeInformation("Target1", "Type1", "Class1", "Name1", "Description1", "Probe1", 2, "url1");
        assertEquals("Target1", probeInfo.getTargetName());
    }
    
    @Test
    public void testGetBmClass() {
        ProbeInformation probeInfo = new ProbeInformation("Target1", "Type1", "Class1", "Name1", "Description1", "Probe1", 2, "url1");
        assertEquals("Class1", probeInfo.getBmClass());
    }
    
    @Test
    public void testGetBmDescription() {
        ProbeInformation probeInfo = new ProbeInformation("Target1", "Type1", "Class1", "Name1", "Description1", "Probe1", 2, "url1");
        assertEquals("Description1", probeInfo.getBmDescription());
    }
    
    @Test
    public void testGetPrecision() {
        ProbeInformation probeInfo = new ProbeInformation("Target1", "Type1", "Class1", "Name1", "Description1", "Probe1", 2, "url1");
        assertEquals(2, probeInfo.getPrecision());
    }
    
    @Test
    public void testGetMeasureURI() {
        ProbeInformation probeInfo = new ProbeInformation("Target1", "Type1", "Class1", "Name1", "Description1", "Probe1", 2, "url1");
        assertEquals("Type1_Target1_Class1_Name1", probeInfo.getMeasureURI());
    }
    
    @Test
    public void testGetXmlRpcUrl() {
        ProbeInformation probeInfo = new ProbeInformation("Target1", "Type1", "Class1", "Name1", "Description1", "Probe1", 2, "url1");
        assertEquals("url1", probeInfo.getXmlRpcUrl());
    }
    
    @Test
    public void testToString() {
        ProbeInformation probeInfo = new ProbeInformation("Target1", "Type1", "Class1", "Name1", "Description1", "Probe1", 2, "url1");
        String expected = "ProbeInformation{targetName='Target1', targetType='Type1', bmClass='Class1', bmName='Name1', bmDescription='Description1', probeDescription='Probe1', precision=2}";
        assertEquals(expected, probeInfo.toString());
    }
    
    @Test
    public void testEquals() {
        ProbeInformation probeInfo1 = new ProbeInformation("Target1", "Type1", "Class1", "Name1", "Description1", "Probe1", 2, "url1");
        ProbeInformation probeInfo2 = new ProbeInformation("Target1", "Type1", "Class1", "Name1", "Description1", "Probe1", 2, "url1");
        assertTrue(probeInfo1.equals(probeInfo2));
    }
    
    @Test
    public void testNotEquals() {
        ProbeInformation probeInfo1 = new ProbeInformation("Target1", "Type1", "Class1", "Name1", "Description1", "Probe1", 2, "url1");
        ProbeInformation probeInfo2 = new ProbeInformation("Target2", "Type2", "Class2", "Name2", "Description2", "Probe2", 3, "url2");
        assertFalse(probeInfo1.equals(probeInfo2));
    }
    
    @Test
    public void testHashCode() {
        ProbeInformation probeInfo1 = new ProbeInformation("Target1", "Type1", "Class1", "Name1", "Description1", "Probe1", 2, "url1");
        ProbeInformation probeInfo2 = new ProbeInformation("Target1", "Type1", "Class1", "Name1", "Description1", "Probe1", 2, "url1");
        assertEquals(probeInfo1.hashCode(), probeInfo2.hashCode());
    }

}