import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class ProbeInformationTest {
    private ProbeInformation probeInformation;
    
    @Before
    public void setUp() {
        probeInformation = new ProbeInformation("Target1", "Type1", "Class1", "Name1", "Description1", "Probe1", 2, "url1");
    }
    
    @Test
    public void testGetTargetType() {
        assertEquals("Type1", probeInformation.getTargetType());
    }
    
    @Test
    public void testGetBmName() {
        assertEquals("Name1", probeInformation.getBmName());
    }
    
    @Test
    public void testGetProbeName() {
        assertEquals("Probe1", probeInformation.getProbeName());
    }
    
    @Test
    public void testGetTargetName() {
        assertEquals("Target1", probeInformation.getTargetName());
    }
    
    @Test
    public void testGetBmClass() {
        assertEquals("Class1", probeInformation.getBmClass());
    }
    
    @Test
    public void testGetBmDescription() {
        assertEquals("Description1", probeInformation.getBmDescription());
    }
    
    @Test
    public void testGetPrecision() {
        assertEquals(2, probeInformation.getPrecision());
    }
    
    @Test
    public void testGetMeasureURI() {
        assertEquals("Type1_Target1_Class1_Name1", probeInformation.getMeasureURI());
    }
    
    @Test
    public void testGetXmlRpcUrl() {
        assertEquals("url1", probeInformation.getXmlRpcUrl());
    }
    
    @Test
    public void testToString() {
        String expectedResult = "ProbeInformation{targetName='Target1', targetType='Type1', bmClass='Class1', bmName='Name1', bmDescription='Description1', probeDescription='Probe1', precision=2}";
        assertEquals(expectedResult, probeInformation.toString());
    }
    
    @Test
    public void testEquals_SameObject_ReturnsTrue() {
        assertTrue(probeInformation.equals(probeInformation));
    }
    
    @Test
    public void testEquals_NullObject_ReturnsFalse() {
        assertFalse(probeInformation.equals(null));
    }
    
    @Test
    public void testEquals_DifferentClass_ReturnsFalse() {
        assertFalse(probeInformation.equals("SomeString"));
    }
    
    @Test
    public void testEquals_EqualProbeInformation_ReturnsTrue() {
        ProbeInformation other = new ProbeInformation("Target1", "Type1", "Class1", "Name1", "Description1", "Probe1", 2, "url1");
        assertTrue(probeInformation.equals(other));
    }
    
    @Test
    public void testEquals_PrecisionNotEqual_ReturnsFalse() {
        ProbeInformation other = new ProbeInformation("Target1", "Type1", "Class1", "Name1", "Description1", "Probe1", 3, "url1");
        assertFalse(probeInformation.equals(other));
    }
    
    @Test
    public void testEquals_BmClassNotEqual_ReturnsFalse() {
        ProbeInformation other = new ProbeInformation("Target1", "Type1", "Class2", "Name1", "Description1", "Probe1", 2, "url1");
        assertFalse(probeInformation.equals(other));
    }
    
    @Test
    public void testEquals_BmDescriptionNotEqual_ReturnsFalse() {
        ProbeInformation other = new ProbeInformation("Target1", "Type1", "Class1", "Name1", "Description2", "Probe1", 2, "url1");
        assertFalse(probeInformation.equals(other));
    }
    
    @Test
    public void testEquals_BmNameNotEqual_ReturnsFalse() {
        ProbeInformation other = new ProbeInformation("Target1", "Type1", "Class1", "Name2", "Description1", "Probe1", 2, "url1");
        assertFalse(probeInformation.equals(other));
    }
    
    @Test
    public void testEquals_ProbeNameNotEqual_ReturnsFalse() {
        ProbeInformation other = new ProbeInformation("Target1", "Type1", "Class1", "Name1", "Description1", "Probe2", 2, "url1");
        assertFalse(probeInformation.equals(other));
    }
    
    @Test
    public void testEquals_TargetNameNotEqual_ReturnsFalse() {
        ProbeInformation other = new ProbeInformation("Target2", "Type1", "Class1", "Name1", "Description1", "Probe1", 2, "url1");
        assertFalse(probeInformation.equals(other));
    }
    
    @Test
    public void testEquals_TargetTypeNotEqual_ReturnsFalse() {
        ProbeInformation other = new ProbeInformation("Target1", "Type2", "Class1", "Name1", "Description1", "Probe1", 2, "url1");
        assertFalse(probeInformation.equals(other));
    }
    
    @Test
    public void testEquals_AllNullFields_ReturnsTrue() {
        ProbeInformation probe1 = new ProbeInformation(null, null, null, null, null, null, 0, null);
        ProbeInformation probe2 = new ProbeInformation(null, null, null, null, null, null, 0, null);
        assertTrue(probe1.equals(probe2));
    }
    
    @Test
    public void testHashCode_SameObject_ReturnsTrue() {
        assertEquals(probeInformation.hashCode(), probeInformation.hashCode());
    }
    
    @Test
    public void testHashCode_EqualProbeInformation_ReturnsTrue() {
        ProbeInformation other = new ProbeInformation("Target1", "Type1", "Class1", "Name1", "Description1", "Probe1", 2, "url1");
        assertEquals(probeInformation.hashCode(), other.hashCode());
    }
    
    @Test
    public void testHashCode_DifferentPrecision_ReturnsFalse() {
        ProbeInformation other = new ProbeInformation("Target1", "Type1", "Class1", "Name1", "Description1", "Probe1", 3, "url1");
        assertNotEquals(probeInformation.hashCode(), other.hashCode());
    }
    
    @Test
    public void testHashCode_DifferentBmClass_ReturnsFalse() {
        ProbeInformation other = new ProbeInformation("Target1", "Type1", "Class2", "Name1", "Description1", "Probe1", 2, "url1");
        assertNotEquals(probeInformation.hashCode(), other.hashCode());
    }
    
    @Test
    public void testHashCode_DifferentBmDescription_ReturnsFalse() {
        ProbeInformation other = new ProbeInformation("Target1", "Type1", "Class1", "Name1", "Description2", "Probe1", 2, "url1");
        assertNotEquals(probeInformation.hashCode(), other.hashCode());
    }
    
    @Test
    public void testHashCode_DifferentBmName_ReturnsFalse() {
        ProbeInformation other = new ProbeInformation("Target1", "Type1", "Class1", "Name2", "Description1", "Probe1", 2, "url1");
        assertNotEquals(probeInformation.hashCode(), other.hashCode());
    }
    
    @Test
    public void testHashCode_DifferentProbeName_ReturnsFalse() {
        ProbeInformation other = new ProbeInformation("Target1", "Type1", "Class1", "Name1", "Description1", "Probe2", 2, "url1");
        assertNotEquals(probeInformation.hashCode(), other.hashCode());
    }
    
    @Test
    public void testHashCode_DifferentTargetName_ReturnsFalse() {
        ProbeInformation other = new ProbeInformation("Target2", "Type1", "Class1", "Name1", "Description1", "Probe1", 2, "url1");
        assertNotEquals(probeInformation.hashCode(), other.hashCode());
    }
    
    @Test
    public void testHashCode_DifferentTargetType_ReturnsFalse() {
        ProbeInformation other = new ProbeInformation("Target1", "Type2", "Class1", "Name1", "Description1", "Probe1", 2, "url1");
        assertNotEquals(probeInformation.hashCode(), other.hashCode());
    }
}