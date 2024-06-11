import static org.junit.Assert.*;
import org.junit.*;

public class ProbeInformationTest {
    
    @Test
    public void testGetTargetType() {
        ProbeInformation obj = new ProbeInformation(null, "Type", null, null, null, null, 0, null);
        assertEquals("Type", obj.getTargetType());
    }
    
    @Test
    public void testGetBmName() {
        ProbeInformation obj = new ProbeInformation(null, null, null, "Name", null, null, 0, null);
        assertEquals("Name", obj.getBmName());
    }
    
    @Test
    public void testGetProbeName() {
        ProbeInformation obj = new ProbeInformation(null, null, null, null, null, "Probe", 0, null);
        assertEquals("Probe", obj.getProbeName());
    }
    
    @Test
    public void testGetTargetName() {
        ProbeInformation obj = new ProbeInformation("TargetName", null, null, null, null, null, 0, null);
        assertEquals("TargetName", obj.getTargetName());
    }
    
    @Test
    public void testGetBmClass() {
        ProbeInformation obj = new ProbeInformation(null, null, "BmClass", null, null, null, 0, null);
        assertEquals("BmClass", obj.getBmClass());
    }
    
    @Test
    public void testGetBmDescription() {
        ProbeInformation obj = new ProbeInformation(null, null, null, null, "BmDescription", null, 0, null);
        assertEquals("BmDescription", obj.getBmDescription());
    }
    
    @Test
    public void testGetPrecision() {
        ProbeInformation obj = new ProbeInformation(null, null, null, null, null, null, 10, null);
        assertEquals(10, obj.getPrecision());
    }
    
    @Test
    public void testGetMeasureURI() {
        ProbeInformation obj = new ProbeInformation("TargetName", "Type", "BmClass", "Name", "BmDescription", null, 0, null);
        assertEquals("measure://Type/TargetName/BmClass/Name", obj.getMeasureURI());
    }
    
    @Test
    public void testGetXmlRpcUrl() {
        ProbeInformation obj = new ProbeInformation(null, null, null, null, null, null, 0, "XmlRpcUrl");
        assertEquals("XmlRpcUrl", obj.getXmlRpcUrl());
    }
    
    @Test
    public void testToString() {
        ProbeInformation obj = new ProbeInformation("TargetName", "Type", "BmClass", "Name", "BmDescription", "Probe", 10, "XmlRpcUrl");
        assertEquals("ProbeInformation{targetName='TargetName', targetType='Type', bmClass='BmClass', bmName='Name', bmDescription='BmDescription', probeDescription='Probe', precision=10}", obj.toString());
    }
    
    @Test
    public void testEquals() {
        ProbeInformation obj1 = new ProbeInformation("TargetName", "Type", "BmClass", "Name", "BmDescription", "Probe", 10, "XmlRpcUrl");
        ProbeInformation obj2 = new ProbeInformation("TargetName", "Type", "BmClass", "Name", "BmDescription", "Probe", 10, "XmlRpcUrl");
        assertTrue(obj1.equals(obj2));
    }
    
    @Test
    public void testNotEquals() {
        ProbeInformation obj1 = new ProbeInformation("TargetName", "Type", "BmClass", "Name", "BmDescription", "Probe", 10, "XmlRpcUrl");
        ProbeInformation obj2 = new ProbeInformation("TargetName2", "Type", "BmClass", "Name", "BmDescription", "Probe", 10, "XmlRpcUrl");
        assertFalse(obj1.equals(obj2));
    }
    
    @Test
    public void testHashCode() {
        ProbeInformation obj1 = new ProbeInformation("TargetName", "Type", "BmClass", "Name", "BmDescription", "Probe", 10, "XmlRpcUrl");
        ProbeInformation obj2 = new ProbeInformation("TargetName", "Type", "BmClass", "Name", "BmDescription", "Probe", 10, "XmlRpcUrl");
        assertEquals(obj1.hashCode(), obj2.hashCode());
    }
    
    @Test
    public void testNotHashCode() {
        ProbeInformation obj1 = new ProbeInformation("TargetName", "Type", "BmClass", "Name", "BmDescription", "Probe", 10, "XmlRpcUrl");
        ProbeInformation obj2 = new ProbeInformation("TargetName2", "Type", "BmClass", "Name", "BmDescription", "Probe", 10, "XmlRpcUrl");
        assertNotEquals(obj1.hashCode(), obj2.hashCode());
    }
}