import org.junit.Test;
import static org.junit.Assert.*;

public class ProbeInformationTest {
    
    @Test
    public void testGetTargetType() {
        ProbeInformation probeInfo = new ProbeInformation("Target1", "Type1", "Class1", "Name1", "Desc1", "Probe1", 2, "http://xmlrpcurl1.com");
        assertEquals("Type1", probeInfo.getTargetType());
    }
    
    @Test
    public void testGetBmName() {
        ProbeInformation probeInfo = new ProbeInformation("Target1", "Type1", "Class1", "Name1", "Desc1", "Probe1", 2, "http://xmlrpcurl1.com");
        assertEquals("Name1", probeInfo.getBmName());
    }
    
    @Test
    public void testGetProbeName() {
        ProbeInformation probeInfo = new ProbeInformation("Target1", "Type1", "Class1", "Name1", "Desc1", "Probe1", 2, "http://xmlrpcurl1.com");
        assertEquals("Probe1", probeInfo.getProbeName());
    }
    
    @Test
    public void testGetTargetName() {
        ProbeInformation probeInfo = new ProbeInformation("Target1", "Type1", "Class1", "Name1", "Desc1", "Probe1", 2, "http://xmlrpcurl1.com");
        assertEquals("Target1", probeInfo.getTargetName());
    }
    
    @Test
    public void testGetBmClass() {
        ProbeInformation probeInfo = new ProbeInformation("Target1", "Type1", "Class1", "Name1", "Desc1", "Probe1", 2, "http://xmlrpcurl1.com");
        assertEquals("Class1", probeInfo.getBmClass());
    }
    
    @Test
    public void testGetBmDescription() {
        ProbeInformation probeInfo = new ProbeInformation("Target1", "Type1", "Class1", "Name1", "Desc1", "Probe1", 2, "http://xmlrpcurl1.com");
        assertEquals("Desc1", probeInfo.getBmDescription());
    }
    
    @Test
    public void testGetPrecision() {
        ProbeInformation probeInfo = new ProbeInformation("Target1", "Type1", "Class1", "Name1", "Desc1", "Probe1", 2, "http://xmlrpcurl1.com");
        assertEquals(2, probeInfo.getPrecision());
    }
    
    @Test
    public void testGetMeasureURI() {
        ProbeInformation probeInfo = new ProbeInformation("Target1", "Type1", "Class1", "Name1", "Desc1", "Probe1", 2, "http://xmlrpcurl1.com");
        String expected = "Type1_Target1_Class1_Name1";
        assertEquals(expected, probeInfo.getMeasureURI());
    }
    
    @Test
    public void testGetXmlRpcUrl() {
        ProbeInformation probeInfo = new ProbeInformation("Target1", "Type1", "Class1", "Name1", "Desc1", "Probe1", 2, "http://xmlrpcurl1.com");
        assertEquals("http://xmlrpcurl1.com", probeInfo.getXmlRpcUrl());
    }
    
    @Test
    public void testEquals() {
        ProbeInformation probeInfo1 = new ProbeInformation("Target1", "Type1", "Class1", "Name1", "Desc1", "Probe1", 2, "http://xmlrpcurl1.com");
        ProbeInformation probeInfo2 = new ProbeInformation("Target1", "Type1", "Class1", "Name1", "Desc1", "Probe1", 2, "http://xmlrpcurl1.com");
        assertTrue(probeInfo1.equals(probeInfo2));
    }
    
    @Test
    public void testNotEquals() {
        ProbeInformation probeInfo1 = new ProbeInformation("Target1", "Type1", "Class1", "Name1", "Desc1", "Probe1", 2, "http://xmlrpcurl1.com");
        ProbeInformation probeInfo2 = new ProbeInformation("Target2", "Type2", "Class2", "Name2", "Desc2", "Probe2", 3, "http://xmlrpcurl2.com");
        assertFalse(probeInfo1.equals(probeInfo2));
    }
    
    @Test
    public void testHashCode() {
        ProbeInformation probeInfo1 = new ProbeInformation("Target1", "Type1", "Class1", "Name1", "Desc1", "Probe1", 2, "http://xmlrpcurl1.com");
        ProbeInformation probeInfo2 = new ProbeInformation("Target1", "Type1", "Class1", "Name1", "Desc1", "Probe1", 2, "http://xmlrpcurl1.com");
        assertEquals(probeInfo1.hashCode(), probeInfo2.hashCode());
    }
}