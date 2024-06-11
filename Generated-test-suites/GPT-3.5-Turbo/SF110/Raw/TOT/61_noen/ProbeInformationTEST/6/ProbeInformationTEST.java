import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class ProbeInformationTest {

    private ProbeInformation probeInfo;

    @Before
    public void setUp(){
        probeInfo = new ProbeInformation("Target1", "Type1", "Class1", "Name1", "Description1", "Probe1", 10, "https://example.com");
    }

    @After
    public void tearDown(){
        probeInfo = null;
    }

    // Test case for getTargetType() method
    @Test
    public void testGetTargetType(){
        assertEquals("Type1", probeInfo.getTargetType());
    }

    // Test case for getBmName() method
    @Test
    public void testGetBmName(){
        assertEquals("Name1", probeInfo.getBmName());
    }

    // Test case for getProbeName() method
    @Test
    public void testGetProbeName(){
        assertEquals("Probe1", probeInfo.getProbeName());
    }

    // Test case for getTargetName() method
    @Test
    public void testGetTargetName(){
        assertEquals("Target1", probeInfo.getTargetName());
    }

    // Test case for getBmClass() method
    @Test
    public void testGetBmClass(){
        assertEquals("Class1", probeInfo.getBmClass());
    }

    // Test case for getBmDescription() method
    @Test
    public void testGetBmDescription(){
        assertEquals("Description1", probeInfo.getBmDescription());
    }

    // Test case for getPrecision() method
    @Test
    public void testGetPrecision(){
        assertEquals(10, probeInfo.getPrecision());
    }

    // Test case for getMeasureURI() method
    @Test
    public void testGetMeasureURI(){
        assertEquals("Type1:Target1:Class1:Name1", probeInfo.getMeasureURI());
    }

    // Test case for getXmlRpcUrl() method
    @Test
    public void testGetXmlRpcUrl(){
        assertEquals("https://example.com", probeInfo.getXmlRpcUrl());
    }

    // Test case for toString() method
    @Test
    public void testToString(){
        String expected = "ProbeInformation{targetName='Target1', targetType='Type1', bmClass='Class1', bmName='Name1', bmDescription='Description1', probeDescription='Probe1', precision=10}";
        assertEquals(expected, probeInfo.toString());
    }

    // Test case for equals() method when two objects are equal
    @Test
    public void testEqualsEqualObjects(){
        ProbeInformation probeInfo2 = new ProbeInformation("Target1", "Type1", "Class1", "Name1", "Description1", "Probe1", 10, "https://example.com");
        assertTrue(probeInfo.equals(probeInfo2));
    }

    // Test case for equals() method when two objects are not equal
    @Test
    public void testEqualsNotEqualObjects(){
        ProbeInformation probeInfo2 = new ProbeInformation("Target2", "Type2", "Class2", "Name2", "Description2", "Probe2", 20, "https://example.org");
        assertFalse(probeInfo.equals(probeInfo2));
    }

    // Test case for equals() method when comparing with null
    @Test
    public void testEqualsWithNull(){
        assertFalse(probeInfo.equals(null));
    }

    // Test case for equals() method when comparing with a different class object
    @Test
    public void testEqualsWithDifferentClass(){
        assertFalse(probeInfo.equals("ProbeInfo"));
    }

    // Test case for hashCode() method
    @Test
    public void testHashCode(){
        int expected = "Target1".hashCode();
        expected = 31 * expected + "Type1".hashCode();
        expected = 31 * expected + "Class1".hashCode();
        expected = 31 * expected + "Name1".hashCode();
        expected = 31 * expected + "Description1".hashCode();
        expected = 31 * expected + "Probe1".hashCode();
        expected = 31 * expected + 10;
        expected = 31 * expected + "https://example.com".hashCode();
        assertEquals(expected, probeInfo.hashCode());
    }
}