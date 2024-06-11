import org.junit.Test;
import static org.junit.Assert.*;

public class ProbeInformationTest {

  @Test
  public void testGetTargetType() {
    // Typical use case: target type is set and returned correctly
    ProbeInformation probeInfo = new ProbeInformation("target", "type", "class", "name", "description", "probe", 10, "url");
    String expected = "type";
    String result = probeInfo.getTargetType();
    assertEquals(expected, result);
  }

  @Test
  public void testGetBmName() {
    // Typical use case: benchmark name is set and returned correctly
    ProbeInformation probeInfo = new ProbeInformation("target", "type", "class", "name", "description", "probe", 10, "url");
    String expected = "name";
    String result = probeInfo.getBmName();
    assertEquals(expected, result);
  }

  @Test
  public void testGetProbeName() {
    // Typical use case: probe name is set and returned correctly
    ProbeInformation probeInfo = new ProbeInformation("target", "type", "class", "name", "description", "probe", 10, "url");
    String expected = "probe";
    String result = probeInfo.getProbeName();
    assertEquals(expected, result);
  }

  @Test
  public void testGetTargetName() {
    // Typical use case: target name is set and returned correctly
    ProbeInformation probeInfo = new ProbeInformation("target", "type", "class", "name", "description", "probe", 10, "url");
    String expected = "target";
    String result = probeInfo.getTargetName();
    assertEquals(expected, result);
  }

  @Test
  public void testGetBmClass() {
    // Typical use case: benchmark class is set and returned correctly
    ProbeInformation probeInfo = new ProbeInformation("target", "type", "class", "name", "description", "probe", 10, "url");
    String expected = "class";
    String result = probeInfo.getBmClass();
    assertEquals(expected, result);
  }

  @Test
  public void testGetBmDescription() {
    // Typical use case: benchmark description is set and returned correctly
    ProbeInformation probeInfo = new ProbeInformation("target", "type", "class", "name", "description", "probe", 10, "url");
    String expected = "description";
    String result = probeInfo.getBmDescription();
    assertEquals(expected, result);
  }

  @Test
  public void testGetPrecision() {
    // Typical use case: precision is set and returned correctly
    ProbeInformation probeInfo = new ProbeInformation("target", "type", "class", "name", "description", "probe", 10, "url");
    int expected = 10;
    int result = probeInfo.getPrecision();
    assertEquals(expected, result);
  }

  @Test
  public void testGetMeasureURI() {
    
    // Typical use case: measure URI is created correctly
    ProbeInformation probeInfo = new ProbeInformation("target", "type", "class", "name", "description", "probe", 10, "url");
    String expected = "expectedURI";
    String result = probeInfo.getMeasureURI();
    assertEquals(expected, result);
    
    // Edge case: zero precision
    probeInfo = new ProbeInformation("target", "type", "class", "name", "description", "probe", 0, "url");
    expected = "expectedURI";
    result = probeInfo.getMeasureURI();
    assertEquals(expected, result);

    // Edge case: negative precision
    probeInfo = new ProbeInformation("target", "type", "class", "name", "description", "probe", -10, "url");
    expected = "expectedURI";
    result = probeInfo.getMeasureURI();
    assertEquals(expected, result);
  }

  @Test
  public void testGetXmlRpcUrl() {
    // Typical use case: XML-RPC URL is set and returned correctly
    ProbeInformation probeInfo = new ProbeInformation("target", "type", "class", "name", "description", "probe", 10, "url");
    String expected = "url";
    String result = probeInfo.getXmlRpcUrl();
    assertEquals(expected, result);
  }

  @Test
  public void testToString() {
    ProbeInformation probeInfo = new ProbeInformation("target", "type", "class", "name", "description", "probe", 10, "url");
    String expected = "ProbeInformation{targetName='target', targetType='type', bmClass='class', bmName='name', bmDescription='description', probeDescription='probe', precision=10}";
    String result = probeInfo.toString();
    assertEquals(expected, result);
  }

  @Test
  public void testEquals() {
    // Typical use case: same ProbeInformation object, returns true
    ProbeInformation probeInfo = new ProbeInformation("target", "type", "class", "name", "description", "probe", 10, "url");
    boolean expected = true;
    boolean result = probeInfo.equals(probeInfo);
    assertEquals(expected, result);
    
    // Typical use case: same field values, returns true
    ProbeInformation probeInfo2 = new ProbeInformation("target", "type", "class", "name", "description", "probe", 10, "url");
    expected = true;
    result = probeInfo.equals(probeInfo2);
    assertEquals(expected, result);

    // Typical use case: different field values, returns false
    ProbeInformation probeInfo3 = new ProbeInformation("target2", "type", "class", "name", "description", "probe", 10, "url");
    expected = false;
    result = probeInfo.equals(probeInfo3);
    assertEquals(expected, result);
  }

  @Test
  public void testHashCode() {
    // Typical use case: same ProbeInformation object, returns same hash code
    ProbeInformation probeInfo = new ProbeInformation("target", "type", "class", "name", "description", "probe", 10, "url");
    int expected = probeInfo.hashCode();
    int result = probeInfo.hashCode();
    assertEquals(expected, result);
    
    // Typical use case: same field values, returns same hash code
    ProbeInformation probeInfo2 = new ProbeInformation("target", "type", "class", "name", "description", "probe", 10, "url");
    expected = probeInfo.hashCode();
    result = probeInfo2.hashCode();
    assertEquals(expected, result);

    // Typical use case: different field values, returns different hash code
    ProbeInformation probeInfo3 = new ProbeInformation("target2", "type", "class", "name", "description", "probe", 10, "url");
    expected = probeInfo.hashCode();
    result = probeInfo3.hashCode();
    assertNotEquals(expected, result);
  }
}