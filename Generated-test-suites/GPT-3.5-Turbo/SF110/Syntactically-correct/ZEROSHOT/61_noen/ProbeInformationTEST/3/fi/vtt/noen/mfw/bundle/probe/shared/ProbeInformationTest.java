import org.junit.Test;
import static org.junit.Assert.*;

public class ProbeInformationTest {

    @Test
    public void testGetTargetType() {
        ProbeInformation probeInformation = new ProbeInformation("targetName", "targetType", "bmClass", "bmName", "bmDescription", "probeName", 10, "xmlRpcUrl");
        assertEquals("targetType", probeInformation.getTargetType());
    }

    @Test
    public void testGetBmName() {
        ProbeInformation probeInformation = new ProbeInformation("targetName", "targetType", "bmClass", "bmName", "bmDescription", "probeName", 10, "xmlRpcUrl");
        assertEquals("bmName", probeInformation.getBmName());
    }

    @Test
    public void testGetProbeName() {
        ProbeInformation probeInformation = new ProbeInformation("targetName", "targetType", "bmClass", "bmName", "bmDescription", "probeName", 10, "xmlRpcUrl");
        assertEquals("probeName", probeInformation.getProbeName());
    }

    @Test
    public void testGetTargetName() {
        ProbeInformation probeInformation = new ProbeInformation("targetName", "targetType", "bmClass", "bmName", "bmDescription", "probeName", 10, "xmlRpcUrl");
        assertEquals("targetName", probeInformation.getTargetName());
    }

    @Test
    public void testGetBmClass() {
        ProbeInformation probeInformation = new ProbeInformation("targetName", "targetType", "bmClass", "bmName", "bmDescription", "probeName", 10, "xmlRpcUrl");
        assertEquals("bmClass", probeInformation.getBmClass());
    }

    @Test
    public void testGetBmDescription() {
        ProbeInformation probeInformation = new ProbeInformation("targetName", "targetType", "bmClass", "bmName", "bmDescription", "probeName", 10, "xmlRpcUrl");
        assertEquals("bmDescription", probeInformation.getBmDescription());
    }

    @Test
    public void testGetPrecision() {
        ProbeInformation probeInformation = new ProbeInformation("targetName", "targetType", "bmClass", "bmName", "bmDescription", "probeName", 10, "xmlRpcUrl");
        assertEquals(10, probeInformation.getPrecision());
    }

    @Test
    public void testGetMeasureURI() {
        ProbeInformation probeInformation = new ProbeInformation("targetName", "targetType", "bmClass", "bmName", "bmDescription", "probeName", 10, "xmlRpcUrl");
        assertEquals("expectedValue", probeInformation.getMeasureURI());
    }

    @Test
    public void testGetXmlRpcUrl() {
        ProbeInformation probeInformation = new ProbeInformation("targetName", "targetType", "bmClass", "bmName", "bmDescription", "probeName", 10, "xmlRpcUrl");
        assertEquals("xmlRpcUrl", probeInformation.getXmlRpcUrl());
    }

    @Test
    public void testToString() {
        ProbeInformation probeInformation = new ProbeInformation("targetName", "targetType", "bmClass", "bmName", "bmDescription", "probeName", 10, "xmlRpcUrl");
        assertEquals("ProbeInformation{targetName='targetName', targetType='targetType', bmClass='bmClass', bmName='bmName', bmDescription='bmDescription', probeDescription='probeName', precision=10}", probeInformation.toString());
    }

    @Test
    public void testEquals() {
        ProbeInformation probeInformation1 = new ProbeInformation("targetName", "targetType", "bmClass", "bmName", "bmDescription", "probeName", 10, "xmlRpcUrl");
        ProbeInformation probeInformation2 = new ProbeInformation("targetName", "targetType", "bmClass", "bmName", "bmDescription", "probeName", 10, "xmlRpcUrl");
        assertTrue(probeInformation1.equals(probeInformation2));
    }

    @Test
    public void testEqualsWithDifferentObjects() {
        ProbeInformation probeInformation1 = new ProbeInformation("targetName", "targetType", "bmClass", "bmName", "bmDescription", "probeName", 10, "xmlRpcUrl");
        assertFalse(probeInformation1.equals(null));
        assertFalse(probeInformation1.equals("str"));
    }

    @Test
    public void testEqualsWithDifferentPrecision() {
        ProbeInformation probeInformation1 = new ProbeInformation("targetName", "targetType", "bmClass", "bmName", "bmDescription", "probeName", 10, "xmlRpcUrl");
        ProbeInformation probeInformation2 = new ProbeInformation("targetName", "targetType", "bmClass", "bmName", "bmDescription", "probeName", 20, "xmlRpcUrl");
        assertFalse(probeInformation1.equals(probeInformation2));
    }

    @Test
    public void testEqualsWithDifferentBmClass() {
        ProbeInformation probeInformation1 = new ProbeInformation("targetName", "targetType", "bmClass1", "bmName", "bmDescription", "probeName", 10, "xmlRpcUrl");
        ProbeInformation probeInformation2 = new ProbeInformation("targetName", "targetType", "bmClass2", "bmName", "bmDescription", "probeName", 10, "xmlRpcUrl");
        assertFalse(probeInformation1.equals(probeInformation2));
    }

    @Test
    public void testEqualsWithDifferentBmDescription() {
        ProbeInformation probeInformation1 = new ProbeInformation("targetName", "targetType", "bmClass", "bmName", "bmDescription1", "probeName", 10, "xmlRpcUrl");
        ProbeInformation probeInformation2 = new ProbeInformation("targetName", "targetType", "bmClass", "bmName", "bmDescription2", "probeName", 10, "xmlRpcUrl");
        assertFalse(probeInformation1.equals(probeInformation2));
    }

    @Test
    public void testEqualsWithDifferentBmName() {
        ProbeInformation probeInformation1 = new ProbeInformation("targetName", "targetType", "bmClass", "bmName1", "bmDescription", "probeName", 10, "xmlRpcUrl");
        ProbeInformation probeInformation2 = new ProbeInformation("targetName", "targetType", "bmClass", "bmName2", "bmDescription", "probeName", 10, "xmlRpcUrl");
        assertFalse(probeInformation1.equals(probeInformation2));
    }

    @Test
    public void testEqualsWithDifferentProbeName() {
        ProbeInformation probeInformation1 = new ProbeInformation("targetName", "targetType", "bmClass", "bmName", "bmDescription", "probeName1", 10, "xmlRpcUrl");
        ProbeInformation probeInformation2 = new ProbeInformation("targetName", "targetType", "bmClass", "bmName", "bmDescription", "probeName2", 10, "xmlRpcUrl");
        assertFalse(probeInformation1.equals(probeInformation2));
    }

    @Test
    public void testEqualsWithDifferentTargetName() {
        ProbeInformation probeInformation1 = new ProbeInformation("targetName1", "targetType", "bmClass", "bmName", "bmDescription", "probeName", 10, "xmlRpcUrl");
        ProbeInformation probeInformation2 = new ProbeInformation("targetName2", "targetType", "bmClass", "bmName", "bmDescription", "probeName", 10, "xmlRpcUrl");
        assertFalse(probeInformation1.equals(probeInformation2));
    }

    @Test
    public void testEqualsWithDifferentTargetType() {
        ProbeInformation probeInformation1 = new ProbeInformation("targetName", "targetType1", "bmClass", "bmName", "bmDescription", "probeName", 10, "xmlRpcUrl");
        ProbeInformation probeInformation2 = new ProbeInformation("targetName", "targetType2", "bmClass", "bmName", "bmDescription", "probeName", 10, "xmlRpcUrl");
        assertFalse(probeInformation1.equals(probeInformation2));
    }

    @Test
    public void testEqualsWithBothNull() {
        ProbeInformation probeInformation1 = new ProbeInformation(null, null, null, null, null, null, 10, null);
        ProbeInformation probeInformation2 = new ProbeInformation(null, null, null, null, null, null, 10, null);
        assertTrue(probeInformation1.equals(probeInformation2));
    }

    @Test
    public void testEqualsWithMixedNull() {
        ProbeInformation probeInformation1 = new ProbeInformation(null, null, "bmClass", "bmName", "bmDescription", "probeName", 10, null);
        ProbeInformation probeInformation2 = new ProbeInformation(null, null, "bmClass", "bmName", "bmDescription", "probeName", 10, "xmlRpcUrl");
        assertFalse(probeInformation1.equals(probeInformation2));
    }

    @Test
    public void testHashCode() {
        ProbeInformation probeInformation1 = new ProbeInformation("targetName", "targetType", "bmClass", "bmName", "bmDescription", "probeName", 10, "xmlRpcUrl");
        ProbeInformation probeInformation2 = new ProbeInformation("targetName", "targetType", "bmClass", "bmName", "bmDescription", "probeName", 10, "xmlRpcUrl");
        assertEquals(probeInformation1.hashCode(), probeInformation2.hashCode());
    }

}