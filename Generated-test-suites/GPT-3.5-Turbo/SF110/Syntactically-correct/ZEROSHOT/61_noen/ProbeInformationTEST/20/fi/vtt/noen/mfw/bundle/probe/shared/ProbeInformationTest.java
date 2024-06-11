import org.junit.Assert;
import org.junit.Test;

public class ProbeInformationTest {
  
  @Test
  public void testGetTargetType() {
    ProbeInformation probeInformation = new ProbeInformation("targetName", "targetType", "bmClass", "bmName", "bmDescription", "probeDescription", 10, "xmlRpcUrl");
    
    Assert.assertEquals("targetType", probeInformation.getTargetType());
  }
  
  @Test
  public void testGetBmName() {
    ProbeInformation probeInformation = new ProbeInformation("targetName", "targetType", "bmClass", "bmName", "bmDescription", "probeDescription", 10, "xmlRpcUrl");
    
    Assert.assertEquals("bmName", probeInformation.getBmName());
  }
  
  @Test
  public void testGetProbeName() {
    ProbeInformation probeInformation = new ProbeInformation("targetName", "targetType", "bmClass", "bmName", "bmDescription", "probeDescription", 10, "xmlRpcUrl");
    
    Assert.assertEquals("probeDescription", probeInformation.getProbeName());
  }
  
  @Test
  public void testGetTargetName() {
    ProbeInformation probeInformation = new ProbeInformation("targetName", "targetType", "bmClass", "bmName", "bmDescription", "probeDescription", 10, "xmlRpcUrl");
    
    Assert.assertEquals("targetName", probeInformation.getTargetName());
  }
  
  @Test
  public void testGetBmClass() {
    ProbeInformation probeInformation = new ProbeInformation("targetName", "targetType", "bmClass", "bmName", "bmDescription", "probeDescription", 10, "xmlRpcUrl");
    
    Assert.assertEquals("bmClass", probeInformation.getBmClass());
  }
  
  @Test
  public void testGetBmDescription() {
    ProbeInformation probeInformation = new ProbeInformation("targetName", "targetType", "bmClass", "bmName", "bmDescription", "probeDescription", 10, "xmlRpcUrl");
    
    Assert.assertEquals("bmDescription", probeInformation.getBmDescription());
  }
  
  @Test
  public void testGetPrecision() {
    ProbeInformation probeInformation = new ProbeInformation("targetName", "targetType", "bmClass", "bmName", "bmDescription", "probeDescription", 10, "xmlRpcUrl");
    
    Assert.assertEquals(10, probeInformation.getPrecision());
  }
  
  @Test
  public void testGetMeasureURI() {
    ProbeInformation probeInformation = new ProbeInformation("targetName", "targetType", "bmClass", "bmName", "bmDescription", "probeDescription", 10, "xmlRpcUrl");
    
    Assert.assertEquals("measureURI", probeInformation.getMeasureURI());
  }
  
  @Test
  public void testGetXmlRpcUrl() {
    ProbeInformation probeInformation = new ProbeInformation("targetName", "targetType", "bmClass", "bmName", "bmDescription", "probeDescription", 10, "xmlRpcUrl");
    
    Assert.assertEquals("xmlRpcUrl", probeInformation.getXmlRpcUrl());
  }
  
  @Test
  public void testToString() {
    ProbeInformation probeInformation = new ProbeInformation("targetName", "targetType", "bmClass", "bmName", "bmDescription", "probeDescription", 10, "xmlRpcUrl");
    
    Assert.assertEquals("ProbeInformation{targetName='targetName', targetType='targetType', bmClass='bmClass', bmName='bmName', bmDescription='bmDescription', probeDescription='probeDescription', precision=10}", probeInformation.toString());
  }
  
  @Test
  public void testEquals() {
    ProbeInformation probeInformation1 = new ProbeInformation("targetName", "targetType", "bmClass", "bmName", "bmDescription", "probeDescription", 10, "xmlRpcUrl");
    ProbeInformation probeInformation2 = new ProbeInformation("targetName", "targetType", "bmClass", "bmName", "bmDescription", "probeDescription", 10, "xmlRpcUrl");
    
    Assert.assertTrue(probeInformation1.equals(probeInformation2));
  }
  
  @Test
  public void testEquals_nullObject() {
    ProbeInformation probeInformation = new ProbeInformation("targetName", "targetType", "bmClass", "bmName", "bmDescription", "probeDescription", 10, "xmlRpcUrl");
    
    Assert.assertFalse(probeInformation.equals(null));
  }
  
  @Test
  public void testEquals_differentClass() {
    ProbeInformation probeInformation = new ProbeInformation("targetName", "targetType", "bmClass", "bmName", "bmDescription", "probeDescription", 10, "xmlRpcUrl");
    
    Assert.assertFalse(probeInformation.equals("test"));
  }
  
  @Test
  public void testEquals_differentPrecision() {
    ProbeInformation probeInformation1 = new ProbeInformation("targetName", "targetType", "bmClass", "bmName", "bmDescription", "probeDescription", 10, "xmlRpcUrl");
    ProbeInformation probeInformation2 = new ProbeInformation("targetName", "targetType", "bmClass", "bmName", "bmDescription", "probeDescription", 20, "xmlRpcUrl");
    
    Assert.assertFalse(probeInformation1.equals(probeInformation2));
  }
  
  @Test
  public void testEquals_differentBmClass() {
    ProbeInformation probeInformation1 = new ProbeInformation("targetName", "targetType", "bmClass", "bmName", "bmDescription", "probeDescription", 10, "xmlRpcUrl");
    ProbeInformation probeInformation2 = new ProbeInformation("targetName", "targetType", "bmClass2", "bmName", "bmDescription", "probeDescription", 10, "xmlRpcUrl");
    
    Assert.assertFalse(probeInformation1.equals(probeInformation2));
  }
  
  @Test
  public void testEquals_differentBmDescription() {
    ProbeInformation probeInformation1 = new ProbeInformation("targetName", "targetType", "bmClass", "bmName", "bmDescription", "probeDescription", 10, "xmlRpcUrl");
    ProbeInformation probeInformation2 = new ProbeInformation("targetName", "targetType", "bmClass", "bmName", "bmDescription2", "probeDescription", 10, "xmlRpcUrl");
    
    Assert.assertFalse(probeInformation1.equals(probeInformation2));
  }
  
  @Test
  public void testEquals_differentBmName() {
    ProbeInformation probeInformation1 = new ProbeInformation("targetName", "targetType", "bmClass", "bmName", "bmDescription", "probeDescription", 10, "xmlRpcUrl");
    ProbeInformation probeInformation2 = new ProbeInformation("targetName", "targetType", "bmClass", "bmName2", "bmDescription", "probeDescription", 10, "xmlRpcUrl");
    
    Assert.assertFalse(probeInformation1.equals(probeInformation2));
  }
  
  @Test
  public void testEquals_differentProbeName() {
    ProbeInformation probeInformation1 = new ProbeInformation("targetName", "targetType", "bmClass", "bmName", "bmDescription", "probeDescription", 10, "xmlRpcUrl");
    ProbeInformation probeInformation2 = new ProbeInformation("targetName", "targetType", "bmClass", "bmName", "bmDescription", "probeDescription2", 10, "xmlRpcUrl");
    
    Assert.assertFalse(probeInformation1.equals(probeInformation2));
  }
  
  @Test
  public void testEquals_differentTargetName() {
    ProbeInformation probeInformation1 = new ProbeInformation("targetName", "targetType", "bmClass", "bmName", "bmDescription", "probeDescription", 10, "xmlRpcUrl");
    ProbeInformation probeInformation2 = new ProbeInformation("targetName2", "targetType", "bmClass", "bmName", "bmDescription", "probeDescription", 10, "xmlRpcUrl");
    
    Assert.assertFalse(probeInformation1.equals(probeInformation2));
  }
  
  @Test
  public void testEquals_differentTargetType() {
    ProbeInformation probeInformation1 = new ProbeInformation("targetName", "targetType", "bmClass", "bmName", "bmDescription", "probeDescription", 10, "xmlRpcUrl");
    ProbeInformation probeInformation2 = new ProbeInformation("targetName", "targetType2", "bmClass", "bmName", "bmDescription", "probeDescription", 10, "xmlRpcUrl");
    
    Assert.assertFalse(probeInformation1.equals(probeInformation2));
  }
  
  @Test
  public void testHashCode() {
    ProbeInformation probeInformation = new ProbeInformation("targetName", "targetType", "bmClass", "bmName", "bmDescription", "probeDescription", 10, "xmlRpcUrl");
    
    Assert.assertEquals(1545693725, probeInformation.hashCode());
  }
}