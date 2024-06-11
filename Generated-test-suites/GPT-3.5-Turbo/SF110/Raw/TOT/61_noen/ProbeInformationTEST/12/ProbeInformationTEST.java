import org.junit.Test;
import static org.junit.Assert.*;

public class ProbeInformationTest {

  ProbeInformation probeInfo;

  @Test
  public void testGetTargetType() {
    String targetType = "Type";
    probeInfo = new ProbeInformation(null, targetType, null, null, null, null, 0, null);
    assertEquals(targetType, probeInfo.getTargetType());
  }

  @Test
  public void testGetBmName() {
    String bmName = "Name";
    probeInfo = new ProbeInformation(null, null, null, bmName, null, null, 0, null);
    assertEquals(bmName, probeInfo.getBmName());
  }

  @Test
  public void testGetProbeName() {
    String probeName = "Name";
    probeInfo = new ProbeInformation(null, null, null, null, null, probeName, 0, null);
    assertEquals(probeName, probeInfo.getProbeName());
  }

  @Test
  public void testGetTargetName() {
    String targetName = "Name";
    probeInfo = new ProbeInformation(targetName, null, null, null, null, null, 0, null);
    assertEquals(targetName, probeInfo.getTargetName());
  }

  @Test
  public void testGetBmClass() {
    String bmClass = "ClassName";
    probeInfo = new ProbeInformation(null, null, bmClass, null, null, null, 0, null);
    assertEquals(bmClass, probeInfo.getBmClass());
  }

  @Test
  public void testGetBmDescription() {
    String bmDescription = "Description";
    probeInfo = new ProbeInformation(null, null, null, null, bmDescription, null, 0, null);
    assertEquals(bmDescription, probeInfo.getBmDescription());
  }

  @Test
  public void testGetPrecision() {
    int precision = 4;
    probeInfo = new ProbeInformation(null, null, null, null, null, null, precision, null);
    assertEquals(precision, probeInfo.getPrecision());
  }

  @Test
  public void testGetMeasureURI() {
    String targetType = "Type";
    String targetName = "Name";
    String bmClass = "ClassName";
    String bmName = "Name";
    String measureURI = Const.createMeasureURI(targetType, targetName, bmClass, bmName);

    probeInfo = new ProbeInformation(targetName, targetType, bmClass, bmName, null, null, 0, null);
    assertEquals(measureURI, probeInfo.getMeasureURI());
  }

  @Test
  public void testGetXmlRpcUrl() {
    String xmlRpcUrl = "https://example.com";
    probeInfo = new ProbeInformation(null, null, null, null, null, null, 0, xmlRpcUrl);
    assertEquals(xmlRpcUrl, probeInfo.getXmlRpcUrl());
  }

  @Test
  public void testToString() {
    String targetName = "Name";
    String targetType = "Type";
    String bmClass = "ClassName";
    String bmName = "Name";
    String bmDescription = "Description";
    String probeName = "Name";
    int precision = 4;
    String expectedResult = "ProbeInformation{targetName='" + targetName + '\'' + ", targetType='" + targetType + '\'' + ", bmClass='" + bmClass + '\'' + ", bmName='" + bmName + '\'' + ", bmDescription='" + bmDescription + '\'' + ", probeDescription='" + probeName + '\'' + ", precision=" + precision + '}';

    probeInfo = new ProbeInformation(targetName, targetType, bmClass, bmName, bmDescription, probeName, precision, null);
    assertEquals(expectedResult, probeInfo.toString());
  }

  @Test
  public void testEqualsSameObject() {
    probeInfo = new ProbeInformation(null, null, null, null, null, null, 0, null);
    assertTrue(probeInfo.equals(probeInfo));
  }

  @Test
  public void testEqualsNullObject() {
    probeInfo = new ProbeInformation(null, null, null, null, null, null, 0, null);
    assertFalse(probeInfo.equals(null));
  }

  @Test
  public void testEqualsDifferentClass() {
    probeInfo = new ProbeInformation(null, null, null, null, null, null, 0, null);
    assertFalse(probeInfo.equals(new Object()));
  }

  @Test
  public void testEqualsDifferentPrecision() {
    int precision = 4;
    probeInfo = new ProbeInformation(null, null, null, null, null, null, precision, null);
    ProbeInformation other = new ProbeInformation(null, null, null, null, null, null, precision + 1, null);
    assertFalse(probeInfo.equals(other));
  }

  @Test
  public void testEqualsDifferentBmClass() {
    String bmClass = "ClassName";
    probeInfo = new ProbeInformation(null, null, bmClass, null, null, null, 0, null);
    ProbeInformation other = new ProbeInformation(null, null, bmClass + "other", null, null, null, 0, null);
    assertFalse(probeInfo.equals(other));
  }

  @Test
  public void testEqualsDifferentBmDescription() {
    String bmDescription = "Description";
    probeInfo = new ProbeInformation(null, null, null, null, bmDescription, null, 0, null);
    ProbeInformation other = new ProbeInformation(null, null, null, null, bmDescription + "other", null, 0, null);
    assertFalse(probeInfo.equals(other));
  }

  @Test
  public void testEqualsDifferentBmName() {
    String bmName = "Name";
    probeInfo = new ProbeInformation(null, null, null, bmName, null, null, 0, null);
    ProbeInformation other = new ProbeInformation(null, null, null, bmName + "other", null, null, 0, null);
    assertFalse(probeInfo.equals(other));
  }

  @Test
  public void testEqualsDifferentProbeName() {
    String probeName = "Name";
    probeInfo = new ProbeInformation(null, null, null, null, null, probeName, 0, null);
    ProbeInformation other = new ProbeInformation(null, null, null, null, null, probeName + "other", 0, null);
    assertFalse(probeInfo.equals(other));
  }

  @Test
  public void testEqualsDifferentTargetName() {
    String targetName = "Name";
    probeInfo = new ProbeInformation(targetName, null, null, null, null, null, 0, null);
    ProbeInformation other = new ProbeInformation(targetName + "other", null, null, null, null, null, 0, null);
    assertFalse(probeInfo.equals(other));
  }

  @Test
  public void testEqualsDifferentTargetType() {
    String targetType = "Type";
    probeInfo = new ProbeInformation(null, targetType, null, null, null, null, 0, null);
    ProbeInformation other = new ProbeInformation(null, targetType + "other", null, null, null, null, 0, null);
    assertFalse(probeInfo.equals(other));
  }

  @Test
  public void testEqualsSameObjects() {
    String targetName = "Name";
    String targetType = "Type";
    String bmClass = "ClassName";
    String bmName = "Name";
    String bmDescription = "Description";
    String probeName = "Name";
    int precision = 4;

    probeInfo = new ProbeInformation(targetName, targetType, bmClass, bmName, bmDescription, probeName, precision, null);
    ProbeInformation same = new ProbeInformation(targetName, targetType, bmClass, bmName, bmDescription, probeName, precision, null);
    assertTrue(probeInfo.equals(same));
    assertTrue(same.equals(probeInfo));
  }

  @Test
  public void testHashCode() {
    String targetName = "Name";
    String targetType = "Type";
    String bmClass = "ClassName";
    String bmName = "Name";
    String bmDescription = "Description";
    String probeName = "Name";
    int precision = 4;

    int expectedHashCode = 31 * (31 * (31 * (31 * (31 * (31 * (31 * 0 + targetName.hashCode()) + targetType.hashCode()) + bmClass.hashCode()) + bmName.hashCode()) + bmDescription.hashCode()) + probeName.hashCode()) + precision;

    probeInfo = new ProbeInformation(targetName, targetType, bmClass, bmName, bmDescription, probeName, precision, null);
    assertEquals(expectedHashCode, probeInfo.hashCode());
  }
}