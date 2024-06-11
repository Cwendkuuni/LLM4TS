import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class ProbeInformationTest {

    private ProbeInformation probeInfo;
    private String targetName;
    private String targetType;
    private String bmClass;
    private String bmName;
    private String bmDescription;
    private String probeName;
    private int precision;
    private String xmlRpcUrl;

    @Before
    public void setUp() {
        targetName = "Target1";
        targetType = "Type1";
        bmClass = "Class1";
        bmName = "Name1";
        bmDescription = "Description1";
        probeName = "Probe1";
        precision = 5;
        xmlRpcUrl = "http://example.com";

        probeInfo = new ProbeInformation(targetName, targetType, bmClass, bmName, bmDescription, probeName, precision, xmlRpcUrl);
    }

    @Test
    public void testGetTargetType() {
        assertEquals(targetType, probeInfo.getTargetType());
    }

    @Test
    public void testGetBmName() {
        assertEquals(bmName, probeInfo.getBmName());
    }

    @Test
    public void testGetProbeName() {
        assertEquals(probeName, probeInfo.getProbeName());
    }

    @Test
    public void testGetTargetName() {
        assertEquals(targetName, probeInfo.getTargetName());
    }

    @Test
    public void testGetBmClass() {
        assertEquals(bmClass, probeInfo.getBmClass());
    }

    @Test
    public void testGetBmDescription() {
        assertEquals(bmDescription, probeInfo.getBmDescription());
    }

    @Test
    public void testGetPrecision() {
        assertEquals(precision, probeInfo.getPrecision());
    }

    @Test
    public void testGetMeasureURI() {
        String expectedMeasureURI = "Type1_Target1_Class1_Name1";
        assertEquals(expectedMeasureURI, probeInfo.getMeasureURI());
    }

    @Test
    public void testGetXmlRpcUrl() {
        assertEquals(xmlRpcUrl, probeInfo.getXmlRpcUrl());
    }

    @Test
    public void testToString() {
        String expectedString = "ProbeInformation{targetName='" + targetName + "', targetType='" + targetType + "', bmClass='" + bmClass + "', bmName='" + bmName + "', bmDescription='" + bmDescription + "', probeDescription='" + probeName + "', precision=" + precision + "}";
        assertEquals(expectedString, probeInfo.toString());
    }

    @Test
    public void testEquals_SameObject() {
        assertTrue(probeInfo.equals(probeInfo));
    }

    @Test
    public void testEquals_NullObject() {
        assertFalse(probeInfo.equals(null));
    }

    @Test
    public void testEquals_DifferentClass() {
        assertFalse(probeInfo.equals("dummy"));
    }

    @Test
    public void testEquals_EqualObjects() {
        ProbeInformation other = new ProbeInformation(targetName, targetType, bmClass, bmName, bmDescription, probeName, precision, xmlRpcUrl);
        assertTrue(probeInfo.equals(other));
    }

    @Test
    public void testEquals_DifferentTargetName() {
        ProbeInformation other = new ProbeInformation("DifferentTarget", targetType, bmClass, bmName, bmDescription, probeName, precision, xmlRpcUrl);
        assertFalse(probeInfo.equals(other));
    }

    @Test
    public void testEquals_DifferentTargetType() {
        ProbeInformation other = new ProbeInformation(targetName, "DifferentType", bmClass, bmName, bmDescription, probeName, precision, xmlRpcUrl);
        assertFalse(probeInfo.equals(other));
    }

    @Test
    public void testEquals_DifferentBmClass() {
        ProbeInformation other = new ProbeInformation(targetName, targetType, "DifferentClass", bmName, bmDescription, probeName, precision, xmlRpcUrl);
        assertFalse(probeInfo.equals(other));
    }

    @Test
    public void testEquals_DifferentBmName() {
        ProbeInformation other = new ProbeInformation(targetName, targetType, bmClass, "DifferentName", bmDescription, probeName, precision, xmlRpcUrl);
        assertFalse(probeInfo.equals(other));
    }

    @Test
    public void testEquals_DifferentBmDescription() {
        ProbeInformation other = new ProbeInformation(targetName, targetType, bmClass, bmName, "DifferentDescription", probeName, precision, xmlRpcUrl);
        assertFalse(probeInfo.equals(other));
    }

    @Test
    public void testEquals_DifferentProbeName() {
        ProbeInformation other = new ProbeInformation(targetName, targetType, bmClass, bmName, bmDescription, "DifferentProbe", precision, xmlRpcUrl);
        assertFalse(probeInfo.equals(other));
    }

    @Test
    public void testEquals_DifferentPrecision() {
        ProbeInformation other = new ProbeInformation(targetName, targetType, bmClass, bmName, bmDescription, probeName, 10, xmlRpcUrl);
        assertFalse(probeInfo.equals(other));
    }

    @Test
    public void testEquals_DifferentXmlRpcUrl() {
        ProbeInformation other = new ProbeInformation(targetName, targetType, bmClass, bmName, bmDescription, probeName, precision, "http://other-example.com");
        assertFalse(probeInfo.equals(other));
    }

    @Test
    public void testHashCode() {
        ProbeInformation other = new ProbeInformation(targetName, targetType, bmClass, bmName, bmDescription, probeName, precision, xmlRpcUrl);
        assertEquals(probeInfo.hashCode(), other.hashCode());
    }
}