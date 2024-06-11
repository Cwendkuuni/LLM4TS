import static org.junit.Assert.*;
import org.junit.Test;

public class ProbeInformationTest {

  @Test
  public void testGetTargetType() {
    ProbeInformation probeInfo = new ProbeInformation("TargetName", "TargetType", "BmClass", "BmName", "BmDescription", "ProbeDescription", 10, "http://xmlrpc.url");
    assertEquals("TargetType", probeInfo.getTargetType());
  }
  
  @Test
  public void testGetBmName() {
    ProbeInformation probeInfo = new ProbeInformation("TargetName", "TargetType", "BmClass", "BmName", "BmDescription", "ProbeDescription", 10, "http://xmlrpc.url");
    assertEquals("BmName", probeInfo.getBmName());
  }
  
  @Test
  public void testGetProbeName() {
    ProbeInformation probeInfo = new ProbeInformation("TargetName", "TargetType", "BmClass", "BmName", "BmDescription", "ProbeDescription", 10, "http://xmlrpc.url");
    assertEquals("ProbeDescription", probeInfo.getProbeName());
  }
  
  @Test
  public void testGetTargetName() {
    ProbeInformation probeInfo = new ProbeInformation("TargetName", "TargetType", "BmClass", "BmName", "BmDescription", "ProbeDescription", 10, "http://xmlrpc.url");
    assertEquals("TargetName", probeInfo.getTargetName());
  }
  
  @Test
  public void testGetBmClass() {
    ProbeInformation probeInfo = new ProbeInformation("TargetName", "TargetType", "BmClass", "BmName", "BmDescription", "ProbeDescription", 10, "http://xmlrpc.url");
    assertEquals("BmClass", probeInfo.getBmClass());
  }
  
  @Test
  public void testGetBmDescription() {
    ProbeInformation probeInfo = new ProbeInformation("TargetName", "TargetType", "BmClass", "BmName", "BmDescription", "ProbeDescription", 10, "http://xmlrpc.url");
    assertEquals("BmDescription", probeInfo.getBmDescription());
  }
  
  @Test
  public void testGetPrecision() {
    ProbeInformation probeInfo = new ProbeInformation("TargetName", "TargetType", "BmClass", "BmName", "BmDescription", "ProbeDescription", 10, "http://xmlrpc.url");
    assertEquals(10, probeInfo.getPrecision());
  }
  
  @Test
  public void testGetMeasureURI() {
    ProbeInformation probeInfo = new ProbeInformation("TargetName", "TargetType", "BmClass", "BmName", "BmDescription", "ProbeDescription", 10, "http://xmlrpc.url");
    String expected = "targetType://targetName/bmClass/bmName";
    assertEquals(expected, probeInfo.getMeasureURI());
  }
  
  @Test
  public void testGetXmlRpcUrl() {
    ProbeInformation probeInfo = new ProbeInformation("TargetName", "TargetType", "BmClass", "BmName", "BmDescription", "ProbeDescription", 10, "http://xmlrpc.url");
    assertEquals("http://xmlrpc.url", probeInfo.getXmlRpcUrl());
  }
  
  @Test
  public void testEquals() {
    ProbeInformation probeInfo1 = new ProbeInformation("TargetName", "TargetType", "BmClass", "BmName", "BmDescription", "ProbeDescription", 10, "http://xmlrpc.url");
    ProbeInformation probeInfo2 = new ProbeInformation("TargetName", "TargetType", "BmClass", "BmName", "BmDescription", "ProbeDescription", 10, "http://xmlrpc.url");
    ProbeInformation probeInfo3 = new ProbeInformation("DifferentTarget", "TargetType", "BmClass", "BmName", "BmDescription", "ProbeDescription", 10, "http://xmlrpc.url");
    assertEquals(true, probeInfo1.equals(probeInfo2));
    assertEquals(false, probeInfo1.equals(probeInfo3));
  }
  
  @Test
  public void testHashCode() {
    ProbeInformation probeInfo1 = new ProbeInformation("TargetName", "TargetType", "BmClass", "BmName", "BmDescription", "ProbeDescription", 10, "http://xmlrpc.url");
    ProbeInformation probeInfo2 = new ProbeInformation("TargetName", "TargetType", "BmClass", "BmName", "BmDescription", "ProbeDescription", 10, "http://xmlrpc.url");
    ProbeInformation probeInfo3 = new ProbeInformation("DifferentTarget", "TargetType", "BmClass", "BmName", "BmDescription", "ProbeDescription", 10, "http://xmlrpc.url");
    assertEquals(probeInfo1.hashCode(), probeInfo2.hashCode());
    assertNotEquals(probeInfo1.hashCode(), probeInfo3.hashCode());
  }

}