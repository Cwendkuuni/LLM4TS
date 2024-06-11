
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class ProbeInformationTest {
  private ProbeInformation probeInfo;

  @Before
  public void setUp() {
    probeInfo = new ProbeInformation("Target1", "Type1", "Class1", "Name1", "Description1", "Probe1", 2, "url1");
  }

  @Test
  public void testGetTargetType() {
    String targetType = probeInfo.getTargetType();

    Assert.assertEquals("Type1", targetType);
  }

  @Test
  public void testGetBmName() {
    String bmName = probeInfo.getBmName();

    Assert.assertEquals("Name1", bmName);
  }

  @Test
  public void testGetProbeName() {
    String probeName = probeInfo.getProbeName();

    Assert.assertEquals("Probe1", probeName);
  }

  @Test
  public void testGetTargetName() {
    String targetName = probeInfo.getTargetName();

    Assert.assertEquals("Target1", targetName);
  }

  @Test
  public void testGetBmClass() {
    String bmClass = probeInfo.getBmClass();

    Assert.assertEquals("Class1", bmClass);
  }

  @Test
  public void testGetBmDescription() {
    String bmDescription = probeInfo.getBmDescription();

    Assert.assertEquals("Description1", bmDescription);
  }

  @Test
  public void testGetPrecision() {
    int precision = probeInfo.getPrecision();

    Assert.assertEquals(2, precision);
  }

  @Test
  public void testGetMeasureURI() {
    String measureURI = probeInfo.getMeasureURI();

    Assert.assertEquals("Type1_Target1_Class1_Name1", measureURI);
  }

  @Test
  public void testGetXmlRpcUrl() {
    String xmlRpcUrl = probeInfo.getXmlRpcUrl();

    Assert.assertEquals("url1", xmlRpcUrl);
  }

  @Test
  public void testToString() {
    String expected = "ProbeInformation{targetName='Target1', targetType='Type1', bmClass='Class1', bmName='Name1', bmDescription='Description1', probeDescription='Probe1', precision=2}";

    String probeInfoStr = probeInfo.toString();

    Assert.assertEquals(expected, probeInfoStr);
  }

  @Test
  public void testEquals() {
    ProbeInformation anotherProbeInfo = new ProbeInformation("Target1", "Type1", "Class1", "Name1", "Description1", "Probe1", 2, "url1");

    boolean isEqual = probeInfo.equals(anotherProbeInfo);

    Assert.assertTrue(isEqual);
  }

  @Test
  public void testEqualsWithDifferentObject() {
    boolean isEqual = probeInfo.equals("dummyString");

    Assert.assertFalse(isEqual);
  }

  @Test
  public void testEqualsWithDifferentPrecision() {
    ProbeInformation diffPrecision = new ProbeInformation("Target1", "Type1", "Class1", "Name1", "Description1", "Probe1", 3, "url1");

    boolean isEqual = probeInfo.equals(diffPrecision);

    Assert.assertFalse(isEqual);
  }

  @Test
  public void testEqualsWithDifferentBmClass() {
    ProbeInformation diffBmClass = new ProbeInformation("Target1", "Type1", "Class2", "Name1", "Description1", "Probe1", 2, "url1");

    boolean isEqual = probeInfo.equals(diffBmClass);

    Assert.assertFalse(isEqual);
  }

  @Test
  public void testEqualsWithDifferentBmDescription() {
    ProbeInformation diffBmDesc = new ProbeInformation("Target1", "Type1", "Class1", "Name1", "Description2", "Probe1", 2, "url1");

    boolean isEqual = probeInfo.equals(diffBmDesc);

    Assert.assertFalse(isEqual);
  }

  @Test
  public void testEqualsWithDifferentBmName() {
    ProbeInformation diffBmName = new ProbeInformation("Target1", "Type1", "Class1", "Name2", "Description1", "Probe1", 2, "url1");

    boolean isEqual = probeInfo.equals(diffBmName);

    Assert.assertFalse(isEqual);
  }

  @Test
  public void testEqualsWithDifferentProbeName() {
    ProbeInformation diffProbeName = new ProbeInformation("Target1", "Type1", "Class1", "Name1", "Description1", "Probe2", 2, "url1");

    boolean isEqual = probeInfo.equals(diffProbeName);

    Assert.assertFalse(isEqual);
  }

  @Test
  public void testEqualsWithDifferentTargetName() {
    ProbeInformation diffTargetName = new ProbeInformation("Target2", "Type1", "Class1", "Name1", "Description1", "Probe1", 2, "url1");

    boolean isEqual = probeInfo.equals(diffTargetName);

    Assert.assertFalse(isEqual);
  }

  @Test
  public void testEqualsWithDifferentTargetType() {
    ProbeInformation diffTargetType = new ProbeInformation("Target1", "Type2", "Class1", "Name1", "Description1", "Probe1", 2, "url1");

    boolean isEqual = probeInfo.equals(diffTargetType);

    Assert.assertFalse(isEqual);
  }

  @Test
  public void testHashCode() {
    int expectedHashCode = 3057966;

    int hashCode = probeInfo.hashCode();

    Assert.assertEquals(expectedHashCode, hashCode);
  }
}
