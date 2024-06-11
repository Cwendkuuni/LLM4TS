import static org.junit.Assert.*;
import org.junit.Test;

public class ProbeInformationTest {

    @Test
    public void testGetTargetType() {
        ProbeInformation probeInfo = new ProbeInformation("targetName", "targetType", "bmClass", "bmName", "bmDescription", "probeDescription", 10, "xmlRpcUrl");
        assertEquals("targetType", probeInfo.getTargetType());
    }

    @Test
    public void testGetBmName() {
        ProbeInformation probeInfo = new ProbeInformation("targetName", "targetType", "bmClass", "bmName", "bmDescription", "probeDescription", 10, "xmlRpcUrl");
        assertEquals("bmName", probeInfo.getBmName());
    }

    @Test
    public void testGetProbeName() {
        ProbeInformation probeInfo = new ProbeInformation("targetName", "targetType", "bmClass", "bmName", "bmDescription", "probeDescription", 10, "xmlRpcUrl");
        assertEquals("probeDescription", probeInfo.getProbeName());
    }

    @Test
    public void testGetTargetName() {
        ProbeInformation probeInfo = new ProbeInformation("targetName", "targetType", "bmClass", "bmName", "bmDescription", "probeDescription", 10, "xmlRpcUrl");
        assertEquals("targetName", probeInfo.getTargetName());
    }

    @Test
    public void testGetBmClass() {
        ProbeInformation probeInfo = new ProbeInformation("targetName", "targetType", "bmClass", "bmName", "bmDescription", "probeDescription", 10, "xmlRpcUrl");
        assertEquals("bmClass", probeInfo.getBmClass());
    }

    @Test
    public void testGetBmDescription() {
        ProbeInformation probeInfo = new ProbeInformation("targetName", "targetType", "bmClass", "bmName", "bmDescription", "probeDescription", 10, "xmlRpcUrl");
        assertEquals("bmDescription", probeInfo.getBmDescription());
    }

    @Test
    public void testGetPrecision() {
        ProbeInformation probeInfo = new ProbeInformation("targetName", "targetType", "bmClass", "bmName", "bmDescription", "probeDescription", 10, "xmlRpcUrl");
        assertEquals(10, probeInfo.getPrecision());
    }

    @Test
    public void testGetMeasureURI() {
        ProbeInformation probeInfo = new ProbeInformation("targetName", "targetType", "bmClass", "bmName", "bmDescription", "probeDescription", 10, "xmlRpcUrl");
        String measureURI = "expected measure uri";
        assertEquals(measureURI, probeInfo.getMeasureURI());
    }

    @Test
    public void testGetXmlRpcUrl() {
        ProbeInformation probeInfo = new ProbeInformation("targetName", "targetType", "bmClass", "bmName", "bmDescription", "probeDescription", 10, "xmlRpcUrl");
        assertEquals("xmlRpcUrl", probeInfo.getXmlRpcUrl());
    }

    @Test
    public void testToString() {
        ProbeInformation probeInfo = new ProbeInformation("targetName", "targetType", "bmClass", "bmName", "bmDescription", "probeDescription", 10, "xmlRpcUrl");
        String expectedString = "ProbeInformation{targetName='targetName', targetType='targetType', bmClass='bmClass', bmName='bmName', bmDescription='bmDescription', probeDescription='probeDescription', precision=10}";
        assertEquals(expectedString, probeInfo.toString());
    }

    @Test
    public void testEquals_sameObject() {
        ProbeInformation probeInfo = new ProbeInformation("targetName", "targetType", "bmClass", "bmName", "bmDescription", "probeDescription", 10, "xmlRpcUrl");
        assertEquals(probeInfo, probeInfo);
    }

    @Test
    public void testEquals_nullObject() {
        ProbeInformation probeInfo = new ProbeInformation("targetName", "targetType", "bmClass", "bmName", "bmDescription", "probeDescription", 10, "xmlRpcUrl");
        assertFalse(probeInfo.equals(null));
    }

    @Test
    public void testEquals_differentClass() {
        ProbeInformation probeInfo = new ProbeInformation("targetName", "targetType", "bmClass", "bmName", "bmDescription", "probeDescription", 10, "xmlRpcUrl");
        assertFalse(probeInfo.equals(new String()));
    }

    @Test
    public void testEquals_sameFields() {
        ProbeInformation probeInfo1 = new ProbeInformation("targetName", "targetType", "bmClass", "bmName", "bmDescription", "probeDescription", 10, "xmlRpcUrl");
        ProbeInformation probeInfo2 = new ProbeInformation("targetName", "targetType", "bmClass", "bmName", "bmDescription", "probeDescription", 10, "xmlRpcUrl");
        assertEquals(probeInfo1, probeInfo2);
    }

    @Test
    public void testEquals_differentTargetName() {
        ProbeInformation probeInfo1 = new ProbeInformation("targetName1", "targetType", "bmClass", "bmName", "bmDescription", "probeDescription", 10, "xmlRpcUrl");
        ProbeInformation probeInfo2 = new ProbeInformation("targetName2", "targetType", "bmClass", "bmName", "bmDescription", "probeDescription", 10, "xmlRpcUrl");
        assertNotEquals(probeInfo1, probeInfo2);
    }

    @Test
    public void testEquals_differentTargetType() {
        ProbeInformation probeInfo1 = new ProbeInformation("targetName", "targetType1", "bmClass", "bmName", "bmDescription", "probeDescription", 10, "xmlRpcUrl");
        ProbeInformation probeInfo2 = new ProbeInformation("targetName", "targetType2", "bmClass", "bmName", "bmDescription", "probeDescription", 10, "xmlRpcUrl");
        assertNotEquals(probeInfo1, probeInfo2);
    }

    @Test
    public void testEquals_differentBmClass() {
        ProbeInformation probeInfo1 = new ProbeInformation("targetName", "targetType", "bmClass1", "bmName", "bmDescription", "probeDescription", 10, "xmlRpcUrl");
        ProbeInformation probeInfo2 = new ProbeInformation("targetName", "targetType", "bmClass2", "bmName", "bmDescription", "probeDescription", 10, "xmlRpcUrl");
        assertNotEquals(probeInfo1, probeInfo2);
    }

    @Test
    public void testEquals_differentBmName() {
        ProbeInformation probeInfo1 = new ProbeInformation("targetName", "targetType", "bmClass", "bmName1", "bmDescription", "probeDescription", 10, "xmlRpcUrl");
        ProbeInformation probeInfo2 = new ProbeInformation("targetName", "targetType", "bmClass", "bmName2", "bmDescription", "probeDescription", 10, "xmlRpcUrl");
        assertNotEquals(probeInfo1, probeInfo2);
    }

    @Test
    public void testEquals_differentBmDescription() {
        ProbeInformation probeInfo1 = new ProbeInformation("targetName", "targetType", "bmClass", "bmName", "bmDescription1", "probeDescription", 10, "xmlRpcUrl");
        ProbeInformation probeInfo2 = new ProbeInformation("targetName", "targetType", "bmClass", "bmName", "bmDescription2", "probeDescription", 10, "xmlRpcUrl");
        assertNotEquals(probeInfo1, probeInfo2);
    }

    @Test
    public void testEquals_differentProbeName() {
        ProbeInformation probeInfo1 = new ProbeInformation("targetName", "targetType", "bmClass", "bmName", "bmDescription", "probeDescription1", 10, "xmlRpcUrl");
        ProbeInformation probeInfo2 = new ProbeInformation("targetName", "targetType", "bmClass", "bmName", "bmDescription", "probeDescription2", 10, "xmlRpcUrl");
        assertNotEquals(probeInfo1, probeInfo2);
    }

    @Test
    public void testEquals_differentPrecision() {
        ProbeInformation probeInfo1 = new ProbeInformation("targetName", "targetType", "bmClass", "bmName", "bmDescription", "probeDescription", 10, "xmlRpcUrl");
        ProbeInformation probeInfo2 = new ProbeInformation("targetName", "targetType", "bmClass", "bmName", "bmDescription", "probeDescription", 20, "xmlRpcUrl");
        assertNotEquals(probeInfo1, probeInfo2);
    }

    @Test
    public void testHashCode_sameObject() {
        ProbeInformation probeInfo = new ProbeInformation("targetName", "targetType", "bmClass", "bmName", "bmDescription", "probeDescription", 10, "xmlRpcUrl");
        assertEquals(probeInfo.hashCode(), probeInfo.hashCode());
    }

    @Test
    public void testHashCode_sameFields() {
        ProbeInformation probeInfo1 = new ProbeInformation("targetName", "targetType", "bmClass", "bmName", "bmDescription", "probeDescription", 10, "xmlRpcUrl");
        ProbeInformation probeInfo2 = new ProbeInformation("targetName", "targetType", "bmClass", "bmName", "bmDescription", "probeDescription", 10, "xmlRpcUrl");
        assertEquals(probeInfo1.hashCode(), probeInfo2.hashCode());
    }

    @Test
    public void testHashCode_differentTargetName() {
        ProbeInformation probeInfo1 = new ProbeInformation("targetName1", "targetType", "bmClass", "bmName", "bmDescription", "probeDescription", 10, "xmlRpcUrl");
        ProbeInformation probeInfo2 = new ProbeInformation("targetName2", "targetType", "bmClass", "bmName", "bmDescription", "probeDescription", 10, "xmlRpcUrl");
        assertNotEquals(probeInfo1.hashCode(), probeInfo2.hashCode());
    }

    @Test
    public void testHashCode_differentTargetType() {
        ProbeInformation probeInfo1 = new ProbeInformation("targetName", "targetType1", "bmClass", "bmName", "bmDescription", "probeDescription", 10, "xmlRpcUrl");
        ProbeInformation probeInfo2 = new ProbeInformation("targetName", "targetType2", "bmClass", "bmName", "bmDescription", "probeDescription", 10, "xmlRpcUrl");
        assertNotEquals(probeInfo1.hashCode(), probeInfo2.hashCode());
    }

    @Test
    public void testHashCode_differentBmClass() {
        ProbeInformation probeInfo1 = new ProbeInformation("targetName", "targetType", "bmClass1", "bmName", "bmDescription", "probeDescription", 10, "xmlRpcUrl");
        ProbeInformation probeInfo2 = new ProbeInformation("targetName", "targetType", "bmClass2", "bmName", "bmDescription", "probeDescription", 10, "xmlRpcUrl");
        assertNotEquals(probeInfo1.hashCode(), probeInfo2.hashCode());
    }

    @Test
    public void testHashCode_differentBmName() {
        ProbeInformation probeInfo1 = new ProbeInformation("targetName", "targetType", "bmClass", "bmName1", "bmDescription", "probeDescription", 10, "xmlRpcUrl");
        ProbeInformation probeInfo2 = new ProbeInformation("targetName", "targetType", "bmClass", "bmName2", "bmDescription", "probeDescription", 10, "xmlRpcUrl");
        assertNotEquals(probeInfo1.hashCode(), probeInfo2.hashCode());
    }

    @Test
    public void testHashCode_differentBmDescription() {
        ProbeInformation probeInfo1 = new ProbeInformation("targetName", "targetType", "bmClass", "bmName", "bmDescription1", "probeDescription", 10, "xmlRpcUrl");
        ProbeInformation probeInfo2 = new ProbeInformation("targetName", "targetType", "bmClass", "bmName", "bmDescription2", "probeDescription", 10, "xmlRpcUrl");
        assertNotEquals(probeInfo1.hashCode(), probeInfo2.hashCode());
    }

    @Test
    public void testHashCode_differentProbeName() {
        ProbeInformation probeInfo1 = new ProbeInformation("targetName", "targetType", "bmClass", "bmName", "bmDescription", "probeDescription1", 10, "xmlRpcUrl");
        ProbeInformation probeInfo2 = new ProbeInformation("targetName", "targetType", "bmClass", "bmName", "bmDescription", "probeDescription2", 10, "xmlRpcUrl");
        assertNotEquals(probeInfo1.hashCode(), probeInfo2.hashCode());
    }

    @Test
    public void testHashCode_differentPrecision() {
        ProbeInformation probeInfo1 = new ProbeInformation("targetName", "targetType", "bmClass", "bmName", "bmDescription", "probeDescription", 10, "xmlRpcUrl");
        ProbeInformation probeInfo2 = new ProbeInformation("targetName", "targetType", "bmClass", "bmName", "bmDescription", "probeDescription", 20, "xmlRpcUrl");
        assertNotEquals(probeInfo1.hashCode(), probeInfo2.hashCode());
    }
}