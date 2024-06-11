import org.junit.Assert;
import org.junit.Test;

public class ProbeInformationTest {
    
    @Test
    public void testGetTargetType() {
        ProbeInformation probe = new ProbeInformation("targetName", "targetType", "bmClass", "bmName", "bmDescription", "probeDescription", 1, "xmlRpcUrl");
        
        String targetType = probe.getTargetType();
        
        Assert.assertEquals("targetType", targetType);
    }
    
    @Test
    public void testGetBmName() {
        ProbeInformation probe = new ProbeInformation("targetName", "targetType", "bmClass", "bmName", "bmDescription", "probeDescription", 1, "xmlRpcUrl");
        
        String bmName = probe.getBmName();
        
        Assert.assertEquals("bmName", bmName);
    }
    
    @Test
    public void testGetProbeName() {
        ProbeInformation probe = new ProbeInformation("targetName", "targetType", "bmClass", "bmName", "bmDescription", "probeDescription", 1, "xmlRpcUrl");
        
        String probeName = probe.getProbeName();
        
        Assert.assertEquals("probeDescription", probeName);
    }
    
    @Test
    public void testGetTargetName() {
        ProbeInformation probe = new ProbeInformation("targetName", "targetType", "bmClass", "bmName", "bmDescription", "probeDescription", 1, "xmlRpcUrl");
        
        String targetName = probe.getTargetName();
        
        Assert.assertEquals("targetName", targetName);
    }
    
    @Test
    public void testGetBmClass() {
        ProbeInformation probe = new ProbeInformation("targetName", "targetType", "bmClass", "bmName", "bmDescription", "probeDescription", 1, "xmlRpcUrl");
        
        String bmClass = probe.getBmClass();
        
        Assert.assertEquals("bmClass", bmClass);
    }
    
    @Test
    public void testGetBmDescription() {
        ProbeInformation probe = new ProbeInformation("targetName", "targetType", "bmClass", "bmName", "bmDescription", "probeDescription", 1, "xmlRpcUrl");
        
        String bmDescription = probe.getBmDescription();
        
        Assert.assertEquals("bmDescription", bmDescription);
    }
    
    @Test
    public void testGetPrecision() {
        ProbeInformation probe = new ProbeInformation("targetName", "targetType", "bmClass", "bmName", "bmDescription", "probeDescription", 1, "xmlRpcUrl");
        
        int precision = probe.getPrecision();
        
        Assert.assertEquals(1, precision);
    }
    
    @Test
    public void testGetMeasureURI() {
        ProbeInformation probe = new ProbeInformation("targetName", "targetType", "bmClass", "bmName", "bmDescription", "probeDescription", 1, "xmlRpcUrl");
        
        String measureURI = probe.getMeasureURI();
        
        Assert.assertEquals("measureURI", measureURI);
    }
    
    @Test
    public void testGetXmlRpcUrl() {
        ProbeInformation probe = new ProbeInformation("targetName", "targetType", "bmClass", "bmName", "bmDescription", "probeDescription", 1, "xmlRpcUrl");
        
        String xmlRpcUrl = probe.getXmlRpcUrl();
        
        Assert.assertEquals("xmlRpcUrl", xmlRpcUrl);
    }
    
    @Test
    public void testToString() {
        ProbeInformation probe = new ProbeInformation("targetName", "targetType", "bmClass", "bmName", "bmDescription", "probeDescription", 1, "xmlRpcUrl");
        
        String expected = "ProbeInformation{targetName='targetName', targetType='targetType', bmClass='bmClass', bmName='bmName', bmDescription='bmDescription', probeDescription='probeDescription', precision=1}";
        String actual = probe.toString();
        
        Assert.assertEquals(expected, actual);
    }
    
    @Test
    public void testEquals() {
        ProbeInformation probe1 = new ProbeInformation("targetName", "targetType", "bmClass", "bmName", "bmDescription", "probeDescription", 1, "xmlRpcUrl");
        ProbeInformation probe2 = new ProbeInformation("targetName", "targetType", "bmClass", "bmName", "bmDescription", "probeDescription", 1, "xmlRpcUrl");
        
        Assert.assertTrue(probe1.equals(probe2));
    }
    
    @Test
    public void testHashCode() {
        ProbeInformation probe1 = new ProbeInformation("targetName", "targetType", "bmClass", "bmName", "bmDescription", "probeDescription", 1, "xmlRpcUrl");
        ProbeInformation probe2 = new ProbeInformation("targetName", "targetType", "bmClass", "bmName", "bmDescription", "probeDescription", 1, "xmlRpcUrl");
        
        Assert.assertEquals(probe1.hashCode(), probe2.hashCode());
    }
}