import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class ProbeInformationTest {

    private ProbeInformation probeInformation;
    
    @Before
    public void setUp() {
        probeInformation = new ProbeInformation("Target", "Type", "Class", "Name", "Description", "Probe", 1, "URL");
    }
    
    @Test
    public void testGetTargetType() {
        assertEquals("Type", probeInformation.getTargetType());
    }
    
    @Test
    public void testGetBmName() {
        assertEquals("Name", probeInformation.getBmName());
    }
    
    @Test
    public void testGetProbeName() {
        assertEquals("Probe", probeInformation.getProbeName());
    }
    
    @Test
    public void testGetTargetName() {
        assertEquals("Target", probeInformation.getTargetName());
    }
    
    @Test
    public void testGetBmClass() {
        assertEquals("Class", probeInformation.getBmClass());
    }
    
    @Test
    public void testGetBmDescription() {
        assertEquals("Description", probeInformation.getBmDescription());
    }
    
    @Test
    public void testGetPrecision() {
        assertEquals(1, probeInformation.getPrecision());
    }
    
    @Test
    public void testGetMeasureURI() {
        assertEquals("Type_Target_Class_Name", probeInformation.getMeasureURI());
    }
    
    @Test
    public void testGetXmlRpcUrl() {
        assertEquals("URL", probeInformation.getXmlRpcUrl());
    }
    
    @Test
    public void testEquals_equalObjects_returnsTrue() {
        ProbeInformation sameProbeInformation = new ProbeInformation("Target", "Type", "Class", "Name", "Description", "Probe", 1, "URL");
        
        assertTrue(probeInformation.equals(sameProbeInformation));
    }
    
    @Test
    public void testEquals_differentTargetName_returnsFalse() {
        ProbeInformation differentTargetName = new ProbeInformation("Different", "Type", "Class", "Name", "Description", "Probe", 1, "URL");
        
        assertFalse(probeInformation.equals(differentTargetName));
    }
    
    @Test
    public void testEquals_differentTargetType_returnsFalse() {
        ProbeInformation differentTargetType = new ProbeInformation("Target", "Different", "Class", "Name", "Description", "Probe", 1, "URL");
        
        assertFalse(probeInformation.equals(differentTargetType));
    }
    
    @Test
    public void testEquals_differentBmClass_returnsFalse() {
        ProbeInformation differentBmClass = new ProbeInformation("Target", "Type", "Different", "Name", "Description", "Probe", 1, "URL");
        
        assertFalse(probeInformation.equals(differentBmClass));
    }
    
    @Test
    public void testEquals_differentBmName_returnsFalse() {
        ProbeInformation differentBmName = new ProbeInformation("Target", "Type", "Class", "Different", "Description", "Probe", 1, "URL");
        
        assertFalse(probeInformation.equals(differentBmName));
    }
    
    @Test
    public void testEquals_differentBmDescription_returnsFalse() {
        ProbeInformation differentBmDescription = new ProbeInformation("Target", "Type", "Class", "Name", "Different", "Probe", 1, "URL");
        
        assertFalse(probeInformation.equals(differentBmDescription));
    }
    
    @Test
    public void testEquals_differentProbeName_returnsFalse() {
        ProbeInformation differentProbeName = new ProbeInformation("Target", "Type", "Class", "Name", "Description", "Different", 1, "URL");
        
        assertFalse(probeInformation.equals(differentProbeName));
    }
    
    @Test
    public void testEquals_differentPrecision_returnsFalse() {
        ProbeInformation differentPrecision = new ProbeInformation("Target", "Type", "Class", "Name", "Description", "Probe", 2, "URL");
        
        assertFalse(probeInformation.equals(differentPrecision));
    }
    
    @Test
    public void testEquals_differentXmlRpcUrl_returnsFalse() {
        ProbeInformation differentXmlRpcUrl = new ProbeInformation("Target", "Type", "Class", "Name", "Description", "Probe", 1, "Different");
        
        assertFalse(probeInformation.equals(differentXmlRpcUrl));
    }
    
    @Test
    public void testEquals_nullObject_returnsFalse() {
        assertFalse(probeInformation.equals(null));
    }
    
    @Test
    public void testEquals_differentClass_returnsFalse() {
        assertFalse(probeInformation.equals("string"));
    }
    
    @Test
    public void testHashCode_equalObjects_sameHashCodes() {
        ProbeInformation sameProbeInformation = new ProbeInformation("Target", "Type", "Class", "Name", "Description", "Probe", 1, "URL");
        
        assertEquals(probeInformation.hashCode(), sameProbeInformation.hashCode());
    }
    
    @Test
    public void testHashCode_differentTargetName_differentHashCodes() {
        ProbeInformation differentTargetName = new ProbeInformation("Different", "Type", "Class", "Name", "Description", "Probe", 1, "URL");
        
        assertNotEquals(probeInformation.hashCode(), differentTargetName.hashCode());
    }
    
    @Test
    public void testHashCode_differentTargetType_differentHashCodes() {
        ProbeInformation differentTargetType = new ProbeInformation("Target", "Different", "Class", "Name", "Description", "Probe", 1, "URL");
        
        assertNotEquals(probeInformation.hashCode(), differentTargetType.hashCode());
    }
    
    @Test
    public void testHashCode_differentBmClass_differentHashCodes() {
        ProbeInformation differentBmClass = new ProbeInformation("Target", "Type", "Different", "Name", "Description", "Probe", 1, "URL");
        
        assertNotEquals(probeInformation.hashCode(), differentBmClass.hashCode());
    }
    
    @Test
    public void testHashCode_differentBmName_differentHashCodes() {
        ProbeInformation differentBmName = new ProbeInformation("Target", "Type", "Class", "Different", "Description", "Probe", 1, "URL");
        
        assertNotEquals(probeInformation.hashCode(), differentBmName.hashCode());
    }
    
    @Test
    public void testHashCode_differentBmDescription_differentHashCodes() {
        ProbeInformation differentBmDescription = new ProbeInformation("Target", "Type", "Class", "Name", "Different", "Probe", 1, "URL");
        
        assertNotEquals(probeInformation.hashCode(), differentBmDescription.hashCode());
    }
    
    @Test
    public void testHashCode_differentProbeName_differentHashCodes() {
        ProbeInformation differentProbeName = new ProbeInformation("Target", "Type", "Class", "Name", "Description", "Different", 1, "URL");
        
        assertNotEquals(probeInformation.hashCode(), differentProbeName.hashCode());
    }
    
    @Test
    public void testHashCode_differentPrecision_differentHashCodes() {
        ProbeInformation differentPrecision = new ProbeInformation("Target", "Type", "Class", "Name", "Description", "Probe", 2, "URL");
        
        assertNotEquals(probeInformation.hashCode(), differentPrecision.hashCode());
    }
    
    @Test
    public void testHashCode_differentXmlRpcUrl_differentHashCodes() {
        ProbeInformation differentXmlRpcUrl = new ProbeInformation("Target", "Type", "Class", "Name", "Description", "Probe", 1, "Different");
        
        assertNotEquals(probeInformation.hashCode(), differentXmlRpcUrl.hashCode());
    }
    
    @Test
    public void testHashCode_nullObject_returnsZero() {
        ProbeInformation nullProbeInformation = null;
        
        assertEquals(0, nullProbeInformation.hashCode());
    }
    
    @Test
    public void testToString() {
        String expectedToString = "ProbeInformation{targetName='Target', targetType='Type', bmClass='Class', bmName='Name', bmDescription='Description', probeDescription='Probe', precision=1}";
        
        assertEquals(expectedToString, probeInformation.toString());
    }
    
}