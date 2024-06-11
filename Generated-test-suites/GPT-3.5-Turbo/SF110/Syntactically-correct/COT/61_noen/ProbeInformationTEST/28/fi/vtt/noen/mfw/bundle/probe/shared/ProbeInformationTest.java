import org.junit.Assert;
import org.junit.Test;

public class ProbeInformationTest {
      
    @Test
    public void testGetTargetType() {
        ProbeInformation probe = new ProbeInformation("targetName", "targetType", "bmClass", "bmName", "bmDescription", "probeName", 10, "xmlRpcUrl");
        Assert.assertEquals("targetType", probe.getTargetType());
    }
    
    @Test
    public void testGetBmName() {
        ProbeInformation probe = new ProbeInformation("targetName", "targetType", "bmClass", "bmName", "bmDescription", "probeName", 10, "xmlRpcUrl");
        Assert.assertEquals("bmName", probe.getBmName());
    }
    
    @Test
    public void testGetProbeName() {
        ProbeInformation probe = new ProbeInformation("targetName", "targetType", "bmClass", "bmName", "bmDescription", "probeName", 10, "xmlRpcUrl");
        Assert.assertEquals("probeName", probe.getProbeName());
    }
    
    @Test
    public void testGetTargetName() {
        ProbeInformation probe = new ProbeInformation("targetName", "targetType", "bmClass", "bmName", "bmDescription", "probeName", 10, "xmlRpcUrl");
        Assert.assertEquals("targetName", probe.getTargetName());
    }
    
    @Test
    public void testGetBmClass() {
        ProbeInformation probe = new ProbeInformation("targetName", "targetType", "bmClass", "bmName", "bmDescription", "probeName", 10, "xmlRpcUrl");
        Assert.assertEquals("bmClass", probe.getBmClass());
    }
    
    @Test
    public void testGetBmDescription() {
        ProbeInformation probe = new ProbeInformation("targetName", "targetType", "bmClass", "bmName", "bmDescription", "probeName", 10, "xmlRpcUrl");
        Assert.assertEquals("bmDescription", probe.getBmDescription());
    }
    
    @Test
    public void testGetPrecision() {
        ProbeInformation probe = new ProbeInformation("targetName", "targetType", "bmClass", "bmName", "bmDescription", "probeName", 10, "xmlRpcUrl");
        Assert.assertEquals(10, probe.getPrecision());
    }
    
    @Test
    public void testGetMeasureURI() {
        ProbeInformation probe = new ProbeInformation("targetName", "targetType", "bmClass", "bmName", "bmDescription", "probeName", 10, "xmlRpcUrl");
        Assert.assertEquals("measureURI", probe.getMeasureURI());
    }
    
    @Test
    public void testGetXmlRpcUrl() {
        ProbeInformation probe = new ProbeInformation("targetName", "targetType", "bmClass", "bmName", "bmDescription", "probeName", 10, "xmlRpcUrl");
        Assert.assertEquals("xmlRpcUrl", probe.getXmlRpcUrl());
    }
    
    @Test
    public void testToString() {
        ProbeInformation probe = new ProbeInformation("targetName", "targetType", "bmClass", "bmName", "bmDescription", "probeName", 10, "xmlRpcUrl");
        Assert.assertEquals("ProbeInformation{targetName='targetName', targetType='targetType', bmClass='bmClass', bmName='bmName', bmDescription='bmDescription', probeDescription='probeName', precision=10}", probe.toString());
    }
    
    @Test
    public void testEquals() {
        ProbeInformation probe1 = new ProbeInformation("targetName", "targetType", "bmClass", "bmName", "bmDescription", "probeName", 10, "xmlRpcUrl");
        ProbeInformation probe2 = new ProbeInformation("targetName", "targetType", "bmClass", "bmName", "bmDescription", "probeName", 10, "xmlRpcUrl");
        Assert.assertTrue(probe1.equals(probe2));
    }
    
    @Test
    public void testHashCode() {
        ProbeInformation probe = new ProbeInformation("targetName", "targetType", "bmClass", "bmName", "bmDescription", "probeName", 10, "xmlRpcUrl");
        Assert.assertEquals(probe.hashCode(), probe.hashCode());
    }
}