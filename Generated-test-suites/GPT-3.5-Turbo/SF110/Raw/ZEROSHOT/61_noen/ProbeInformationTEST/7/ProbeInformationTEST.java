import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class ProbeInformationTest {

    @Test
    public void testGetTargetType() {
        ProbeInformation probeInfo = new ProbeInformation("targetName", "targetType", "bmClass", "bmName", "bmDescription", "probeDescription", 10, "xmlRpcUrl");
        assertEquals("targetType", probeInfo.getTargetType());
    }

    @Test
    public void testGetBmName() {
        ProbeInformation probeInfo = new ProbeInformation("targetName", "targetType", "bmClass", "bmName", "bmDescription", "probeDescription", 10, "xmlRpcUrl");
        assertEquals("bmName", probeInfo.getBmName());
    }

    @Test
    public void testGetProbeName() {
        ProbeInformation probeInfo = new ProbeInformation("targetName", "targetType", "bmClass", "bmName", "bmDescription", "probeDescription", 10, "xmlRpcUrl");
        assertEquals("probeDescription", probeInfo.getProbeName());
    }

    @Test
    public void testGetTargetName() {
        ProbeInformation probeInfo = new ProbeInformation("targetName", "targetType", "bmClass", "bmName", "bmDescription", "probeDescription", 10, "xmlRpcUrl");
        assertEquals("targetName", probeInfo.getTargetName());
    }

    @Test
    public void testGetBmClass() {
        ProbeInformation probeInfo = new ProbeInformation("targetName", "targetType", "bmClass", "bmName", "bmDescription", "probeDescription", 10, "xmlRpcUrl");
        assertEquals("bmClass", probeInfo.getBmClass());
    }

    @Test
    public void testGetBmDescription() {
        ProbeInformation probeInfo = new ProbeInformation("targetName", "targetType", "bmClass", "bmName", "bmDescription", "probeDescription", 10, "xmlRpcUrl");
        assertEquals("bmDescription", probeInfo.getBmDescription());
    }

    @Test
    public void testGetPrecision() {
        ProbeInformation probeInfo = new ProbeInformation("targetName", "targetType", "bmClass", "bmName", "bmDescription", "probeDescription", 10, "xmlRpcUrl");
        assertEquals(10, probeInfo.getPrecision());
    }

    @Test
    public void testGetMeasureURI() {
        ProbeInformation probeInfo = new ProbeInformation("targetName", "targetType", "bmClass", "bmName", "bmDescription", "probeDescription", 10, "xmlRpcUrl");
        String expectedURI = "targetType:/targetName/bmClass/bmName";
        assertEquals(expectedURI, probeInfo.getMeasureURI());
    }

    @Test
    public void testGetXmlRpcUrl() {
        ProbeInformation probeInfo = new ProbeInformation("targetName", "targetType", "bmClass", "bmName", "bmDescription", "probeDescription", 10, "xmlRpcUrl");
        assertEquals("xmlRpcUrl", probeInfo.getXmlRpcUrl());
    }
    
    @Test
    public void testToString() {
        ProbeInformation probeInfo = new ProbeInformation("targetName", "targetType", "bmClass", "bmName", "bmDescription", "probeDescription", 10, "xmlRpcUrl");
        String expectedString = "ProbeInformation{targetName='targetName', targetType='targetType', bmClass='bmClass', bmName='bmName', bmDescription='bmDescription', probeDescription='probeDescription', precision=10}";
        assertEquals(expectedString, probeInfo.toString());
    }
    
    @Test
    public void testEquals() {
        ProbeInformation probeInfo1 = new ProbeInformation("targetName", "targetType", "bmClass", "bmName", "bmDescription", "probeDescription", 10, "xmlRpcUrl");
        ProbeInformation probeInfo2 = new ProbeInformation("targetName", "targetType", "bmClass", "bmName", "bmDescription", "probeDescription", 10, "xmlRpcUrl");
        assertEquals(probeInfo1, probeInfo2);
    }
    
    @Test
    public void testHashCode() {
        ProbeInformation probeInfo1 = new ProbeInformation("targetName", "targetType", "bmClass", "bmName", "bmDescription", "probeDescription", 10, "xmlRpcUrl");
        ProbeInformation probeInfo2 = new ProbeInformation("targetName", "targetType", "bmClass", "bmName", "bmDescription", "probeDescription", 10, "xmlRpcUrl");
        assertEquals(probeInfo1.hashCode(), probeInfo2.hashCode());
    }
}