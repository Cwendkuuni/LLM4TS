import org.junit.Test;

public class ProbeInformationTest {

    @Test
    public void testGetTargetType() {
        ProbeInformation probeInfo = new ProbeInformation("targetName", "targetType", "bmClass", "bmName", "bmDescription", "probeName", 10, "xmlRpcUrl");
        String targetType = probeInfo.getTargetType();
        // Check if the returned value is correct
        assertEquals("targetType", targetType);
    }
    
    @Test
    public void testGetBmName() {
        ProbeInformation probeInfo = new ProbeInformation("targetName", "targetType", "bmClass", "bmName", "bmDescription", "probeName", 10, "xmlRpcUrl");
        String bmName = probeInfo.getBmName();
        // Check if the returned value is correct
        assertEquals("bmName", bmName);
    }
    
    @Test
    public void testGetProbeName() {
        ProbeInformation probeInfo = new ProbeInformation("targetName", "targetType", "bmClass", "bmName", "bmDescription", "probeName", 10, "xmlRpcUrl");
        String probeName = probeInfo.getProbeName();
        // Check if the returned value is correct
        assertEquals("probeName", probeName);
    }
    
    @Test
    public void testGetTargetName() {
        ProbeInformation probeInfo = new ProbeInformation("targetName", "targetType", "bmClass", "bmName", "bmDescription", "probeName", 10, "xmlRpcUrl");
        String targetName = probeInfo.getTargetName();
        // Check if the returned value is correct
        assertEquals("targetName", targetName);
    }
    
    @Test
    public void testGetBmClass() {
        ProbeInformation probeInfo = new ProbeInformation("targetName", "targetType", "bmClass", "bmName", "bmDescription", "probeName", 10, "xmlRpcUrl");
        String bmClass = probeInfo.getBmClass();
        // Check if the returned value is correct
        assertEquals("bmClass", bmClass);
    }
    
    @Test
    public void testGetBmDescription() {
        ProbeInformation probeInfo = new ProbeInformation("targetName", "targetType", "bmClass", "bmName", "bmDescription", "probeName", 10, "xmlRpcUrl");
        String bmDescription = probeInfo.getBmDescription();
        // Check if the returned value is correct
        assertEquals("bmDescription", bmDescription);
    }
    
    @Test
    public void testGetPrecision() {
        ProbeInformation probeInfo = new ProbeInformation("targetName", "targetType", "bmClass", "bmName", "bmDescription", "probeName", 10, "xmlRpcUrl");
        int precision = probeInfo.getPrecision();
        // Check if the returned value is correct
        assertEquals(10, precision);
    }
    
    @Test
    public void testGetMeasureURI() {
        ProbeInformation probeInfo = new ProbeInformation("targetName", "targetType", "bmClass", "bmName", "bmDescription", "probeName", 10, "xmlRpcUrl");
        String measureURI = probeInfo.getMeasureURI();
        // Check if the returned value is correct
        assertEquals("expectedResult", measureURI);
    }
    
    @Test
    public void testGetXmlRpcUrl() {
        ProbeInformation probeInfo = new ProbeInformation("targetName", "targetType", "bmClass", "bmName", "bmDescription", "probeName", 10, "xmlRpcUrl");
        String xmlRpcUrl = probeInfo.getXmlRpcUrl();
        // Check if the returned value is correct
        assertEquals("xmlRpcUrl", xmlRpcUrl);
    }
}