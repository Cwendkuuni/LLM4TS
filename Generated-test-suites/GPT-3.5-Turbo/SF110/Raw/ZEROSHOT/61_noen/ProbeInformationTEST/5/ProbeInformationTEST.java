import org.junit.Test;
import static org.junit.Assert.*;

public class ProbeInformationTest {

    @Test
    public void testGetTargetType() {
        ProbeInformation probeInfo = new ProbeInformation("TestTarget", "TestType", "TestBmClass", "TestBmName", "TestBmDescription", "TestProbeName", 5, "TestXmlRpcUrl");
        assertEquals("TestType", probeInfo.getTargetType());
    }

    @Test
    public void testGetBmName() {
        ProbeInformation probeInfo = new ProbeInformation("TestTarget", "TestType", "TestBmClass", "TestBmName", "TestBmDescription", "TestProbeName", 5, "TestXmlRpcUrl");
        assertEquals("TestBmName", probeInfo.getBmName());
    }

    @Test
    public void testGetProbeName() {
        ProbeInformation probeInfo = new ProbeInformation("TestTarget", "TestType", "TestBmClass", "TestBmName", "TestBmDescription", "TestProbeName", 5, "TestXmlRpcUrl");
        assertEquals("TestProbeName", probeInfo.getProbeName());
    }

    @Test
    public void testGetTargetName() {
        ProbeInformation probeInfo = new ProbeInformation("TestTarget", "TestType", "TestBmClass", "TestBmName", "TestBmDescription", "TestProbeName", 5, "TestXmlRpcUrl");
        assertEquals("TestTarget", probeInfo.getTargetName());
    }

    @Test
    public void testGetBmClass() {
        ProbeInformation probeInfo = new ProbeInformation("TestTarget", "TestType", "TestBmClass", "TestBmName", "TestBmDescription", "TestProbeName", 5, "TestXmlRpcUrl");
        assertEquals("TestBmClass", probeInfo.getBmClass());
    }

    @Test
    public void testGetBmDescription() {
        ProbeInformation probeInfo = new ProbeInformation("TestTarget", "TestType", "TestBmClass", "TestBmName", "TestBmDescription", "TestProbeName", 5, "TestXmlRpcUrl");
        assertEquals("TestBmDescription", probeInfo.getBmDescription());
    }

    @Test
    public void testGetPrecision() {
        ProbeInformation probeInfo = new ProbeInformation("TestTarget", "TestType", "TestBmClass", "TestBmName", "TestBmDescription", "TestProbeName", 5, "TestXmlRpcUrl");
        assertEquals(5, probeInfo.getPrecision());
    }

    @Test
    public void testGetMeasureURI() {
        ProbeInformation probeInfo = new ProbeInformation("TestTarget", "TestType", "TestBmClass", "TestBmName", "TestBmDescription", "TestProbeName", 5, "TestXmlRpcUrl");
        String expectedURI = "TestType:TestTarget:TestBmClass:TestBmName";
        assertEquals(expectedURI, probeInfo.getMeasureURI());
    }

    @Test
    public void testGetXmlRpcUrl() {
        ProbeInformation probeInfo = new ProbeInformation("TestTarget", "TestType", "TestBmClass", "TestBmName", "TestBmDescription", "TestProbeName", 5, "TestXmlRpcUrl");
        assertEquals("TestXmlRpcUrl", probeInfo.getXmlRpcUrl());
    }
    
    @Test
    public void testToString() {
        ProbeInformation probeInfo = new ProbeInformation("TestTarget", "TestType", "TestBmClass", "TestBmName", "TestBmDescription", "TestProbeName", 5, "TestXmlRpcUrl");
        String expectedString = "ProbeInformation{targetName='TestTarget', targetType='TestType', bmClass='TestBmClass', bmName='TestBmName', bmDescription='TestBmDescription', probeDescription='TestProbeName', precision=5}";
        assertEquals(expectedString, probeInfo.toString());
    }
    
    @Test
    public void testEquals() {
        ProbeInformation probeInfo1 = new ProbeInformation("Target1", "Type1", "Class1", "Name1", "Desc1", "Probe1", 5, "Url1");
        ProbeInformation probeInfo2 = new ProbeInformation("Target2", "Type2", "Class2", "Name2", "Desc2", "Probe2", 10, "Url2");
        ProbeInformation probeInfo3 = new ProbeInformation("Target3", "Type3", "Class3", "Name3", "Desc3", "Probe3", 20, "Url3");
        ProbeInformation probeInfo4 = probeInfo1;
        
        assertEquals(probeInfo1, probeInfo1);
        assertNotEquals(probeInfo1, probeInfo2);
        assertNotEquals(probeInfo1, probeInfo3);
        assertEquals(probeInfo1, probeInfo4);
    }
    
    @Test
    public void testHashCode() {
        ProbeInformation probeInfo1 = new ProbeInformation("Target1", "Type1", "Class1", "Name1", "Desc1", "Probe1", 5, "Url1");
        ProbeInformation probeInfo2 = new ProbeInformation("Target2", "Type2", "Class2", "Name2", "Desc2", "Probe2", 10, "Url2");
        ProbeInformation probeInfo3 = new ProbeInformation("Target3", "Type3", "Class3", "Name3", "Desc3", "Probe3", 20, "Url3");
        ProbeInformation probeInfo4 = probeInfo1;
        
        assertEquals(probeInfo1.hashCode(), probeInfo1.hashCode());
        assertNotEquals(probeInfo1.hashCode(), probeInfo2.hashCode());
        assertNotEquals(probeInfo1.hashCode(), probeInfo3.hashCode());
        assertEquals(probeInfo1.hashCode(), probeInfo4.hashCode());
    }
}