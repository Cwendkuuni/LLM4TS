
package fi.vtt.noen.mfw.bundle.probe.shared;

import org.junit.Assert;
import org.junit.Test;

public class ProbeInformationTest {
    
    @Test
    public void testGetTargetType() {
        ProbeInformation probe = new ProbeInformation("target", "type", "bmClass", "bmName", "bmDescription", "probeDescription", 1, "url");
        Assert.assertEquals("type", probe.getTargetType());
    }
    
    @Test
    public void testGetBmName() {
        ProbeInformation probe = new ProbeInformation("target", "type", "bmClass", "bmName", "bmDescription", "probeDescription", 1, "url");
        Assert.assertEquals("bmName", probe.getBmName());
    }
    
    @Test
    public void testGetProbeName() {
        ProbeInformation probe = new ProbeInformation("target", "type", "bmClass", "bmName", "bmDescription", "probeDescription", 1, "url");
        Assert.assertEquals("probeDescription", probe.getProbeName());
    }
    
    @Test
    public void testGetTargetName() {
        ProbeInformation probe = new ProbeInformation("target", "type", "bmClass", "bmName", "bmDescription", "probeDescription", 1, "url");
        Assert.assertEquals("target", probe.getTargetName());
    }
    
    @Test
    public void testGetBmClass() {
        ProbeInformation probe = new ProbeInformation("target", "type", "bmClass", "bmName", "bmDescription", "probeDescription", 1, "url");
        Assert.assertEquals("bmClass", probe.getBmClass());
    }
    
    @Test
    public void testGetBmDescription() {
        ProbeInformation probe = new ProbeInformation("target", "type", "bmClass", "bmName", "bmDescription", "probeDescription", 1, "url");
        Assert.assertEquals("bmDescription", probe.getBmDescription());
    }
    
    @Test
    public void testGetPrecision() {
        ProbeInformation probe = new ProbeInformation("target", "type", "bmClass", "bmName", "bmDescription", "probeDescription", 1, "url");
        Assert.assertEquals(1, probe.getPrecision());
    }
    
    @Test
    public void testGetMeasureURI() {
        ProbeInformation probe = new ProbeInformation("target", "type", "bmClass", "bmName", "bmDescription", "probeDescription", 1, "url");
        Assert.assertEquals("measureURI", probe.getMeasureURI()); // replace measureURI with the expected value
    }
    
    @Test
    public void testGetXmlRpcUrl() {
        ProbeInformation probe = new ProbeInformation("target", "type", "bmClass", "bmName", "bmDescription", "probeDescription", 1, "url");
        Assert.assertEquals("url", probe.getXmlRpcUrl());
    }
    
    @Test
    public void testToString() {
        ProbeInformation probe = new ProbeInformation("target", "type", "bmClass", "bmName", "bmDescription", "probeDescription", 1, "url");
        Assert.assertEquals("ProbeInformation{targetName='target', targetType='type', bmClass='bmClass', bmName='bmName', bmDescription='bmDescription', probeDescription='probeDescription', precision=1}", probe.toString());
    }
    
    @Test
    public void testEquals() {
        ProbeInformation probe1 = new ProbeInformation("target", "type", "bmClass", "bmName", "bmDescription", "probeDescription", 1, "url");
        ProbeInformation probe2 = new ProbeInformation("target", "type", "bmClass", "bmName", "bmDescription", "probeDescription", 1, "url");
        Assert.assertTrue(probe1.equals(probe2));
    }
    
    @Test
    public void testEquals_NullObject() {
        ProbeInformation probe = new ProbeInformation("target", "type", "bmClass", "bmName", "bmDescription", "probeDescription", 1, "url");
        Assert.assertFalse(probe.equals(null));
    }
    
    @Test
    public void testEquals_DifferentClass() {
        ProbeInformation probe = new ProbeInformation("target", "type", "bmClass", "bmName", "bmDescription", "probeDescription", 1, "url");
        Assert.assertFalse(probe.equals("ProbeInformation"));
    }
    
    @Test
    public void testHashCode() {
        ProbeInformation probe1 = new ProbeInformation("target", "type", "bmClass", "bmName", "bmDescription", "probeDescription", 1, "url");
        ProbeInformation probe2 = new ProbeInformation("target", "type", "bmClass", "bmName", "bmDescription", "probeDescription", 1, "url");
        Assert.assertEquals(probe1.hashCode(), probe2.hashCode());
    }
}
