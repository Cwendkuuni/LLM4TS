package fi.vtt.noen.mfw.bundle.probe.shared;

import org.junit.*;

public class ProbeInformationTest {

    private ProbeInformation probeInformation;

    @Before
    public void setUp() {
        probeInformation = new ProbeInformation("targetName", "targetType", "bmClass", "bmName", "bmDescription", "probeName", 5, "xmlRpcUrl");
    }

    @Test
    public void testGetTargetType() {
        String targetType = probeInformation.getTargetType();
        Assert.assertEquals("targetType", targetType);
    }

    @Test
    public void testGetBmName() {
        String bmName = probeInformation.getBmName();
        Assert.assertEquals("bmName", bmName);
    }

    @Test
    public void testGetProbeName() {
        String probeName = probeInformation.getProbeName();
        Assert.assertEquals("probeName", probeName);
    }

    @Test
    public void testGetTargetName() {
        String targetName = probeInformation.getTargetName();
        Assert.assertEquals("targetName", targetName);
    }

    @Test
    public void testGetBmClass() {
        String bmClass = probeInformation.getBmClass();
        Assert.assertEquals("bmClass", bmClass);
    }

    @Test
    public void testGetBmDescription() {
        String bmDescription = probeInformation.getBmDescription();
        Assert.assertEquals("bmDescription", bmDescription);
    }

    @Test
    public void testGetPrecision() {
        int precision = probeInformation.getPrecision();
        Assert.assertEquals(5, precision);
    }

    @Test
    public void testGetMeasureURI() {
        String measureURI = probeInformation.getMeasureURI();
        
        // Edge Case: targetType, targetName, bmClass, bmName are null
        String expectedURI = Const.createMeasureURI(null, null, null, null);
        Assert.assertEquals(expectedURI, measureURI);
        
        // Typical Use Case
        probeInformation = new ProbeInformation("targetName", "targetType", "bmClass", "bmName", "bmDescription", "probeName", 5, "xmlRpcUrl");
        expectedURI = Const.createMeasureURI("targetType", "targetName", "bmClass", "bmName");
        Assert.assertEquals(expectedURI, measureURI);
    }

    @Test
    public void testGetXmlRpcUrl() {
        String xmlRpcUrl = probeInformation.getXmlRpcUrl();
        Assert.assertEquals("xmlRpcUrl", xmlRpcUrl);
    }

    @Test
    public void testToString() {
        String expectedString = "ProbeInformation{targetName='targetName', targetType='targetType', bmClass='bmClass', bmName='bmName', bmDescription='bmDescription', probeDescription='probeName', precision=5}";
        String actualString = probeInformation.toString();
        Assert.assertEquals(expectedString, actualString);
    }

    @Test
    public void testEquals() {
        // Reflexive Property
        Assert.assertTrue(probeInformation.equals(probeInformation));

        // Symmetric Property
        ProbeInformation probeInformation2 = new ProbeInformation("targetName", "targetType", "bmClass", "bmName", "bmDescription", "probeName", 5, "xmlRpcUrl");
        Assert.assertTrue(probeInformation.equals(probeInformation2));
        Assert.assertTrue(probeInformation2.equals(probeInformation));

        // Transitive Property
        ProbeInformation probeInformation3 = new ProbeInformation("targetName", "targetType", "bmClass", "bmName", "bmDescription", "probeName", 5, "xmlRpcUrl");
        Assert.assertTrue(probeInformation2.equals(probeInformation3));
        Assert.assertTrue(probeInformation.equals(probeInformation3));

        // Non-nullity Property
        ProbeInformation probeInformation4 = null;
        Assert.assertFalse(probeInformation.equals(probeInformation4));

        // Equality of Classes Property
        Assert.assertFalse(probeInformation.equals("ProbeInformation"));

        // Comparison of targetName
        ProbeInformation probeInformation5 = new ProbeInformation("targetName2", "targetType", "bmClass", "bmName", "bmDescription", "probeName", 5, "xmlRpcUrl");
        Assert.assertFalse(probeInformation.equals(probeInformation5));

        // Comparison of targetType
        ProbeInformation probeInformation6 = new ProbeInformation("targetName", "targetType2", "bmClass", "bmName", "bmDescription", "probeName", 5, "xmlRpcUrl");
        Assert.assertFalse(probeInformation.equals(probeInformation6));

        // Comparison of bmClass
        ProbeInformation probeInformation7 = new ProbeInformation("targetName", "targetType", "bmClass2", "bmName", "bmDescription", "probeName", 5, "xmlRpcUrl");
        Assert.assertFalse(probeInformation.equals(probeInformation7));

        // Comparison of bmName
        ProbeInformation probeInformation8 = new ProbeInformation("targetName", "targetType", "bmClass", "bmName2", "bmDescription", "probeName", 5, "xmlRpcUrl");
        Assert.assertFalse(probeInformation.equals(probeInformation8));

        // Comparison of bmDescription
        ProbeInformation probeInformation9 = new ProbeInformation("targetName", "targetType", "bmClass", "bmName", "bmDescription2", "probeName", 5, "xmlRpcUrl");
        Assert.assertFalse(probeInformation.equals(probeInformation9));

        // Comparison of probeName
        ProbeInformation probeInformation10 = new ProbeInformation("targetName", "targetType", "bmClass", "bmName", "bmDescription", "probeName2", 5, "xmlRpcUrl");
        Assert.assertFalse(probeInformation.equals(probeInformation10));

        // Comparison of precision
        ProbeInformation probeInformation11 = new ProbeInformation("targetName", "targetType", "bmClass", "bmName", "bmDescription", "probeName", 10, "xmlRpcUrl");
        Assert.assertFalse(probeInformation.equals(probeInformation11));
    }

    @Test
    public void testHashCode() {
        ProbeInformation probeInformation2 = new ProbeInformation("targetName", "targetType", "bmClass", "bmName", "bmDescription", "probeName", 5, "xmlRpcUrl");
        Assert.assertEquals(probeInformation.hashCode(), probeInformation2.hashCode());
    }
}