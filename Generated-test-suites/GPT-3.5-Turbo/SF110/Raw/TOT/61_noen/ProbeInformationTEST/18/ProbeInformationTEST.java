import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class ProbeInformationTest {

    private ProbeInformation probeInfo;

    @Before
    public void setUp() {
        probeInfo = new ProbeInformation("Target1", "Type1", "BMClass1", "BMName1", "BMDescription1", "ProbeDescription1", 2, "http://xmlrpcurl.com");
    }

    @Test
    public void testGetTargetType() {
        assertEquals("Type1", probeInfo.getTargetType());
    }

    @Test
    public void testGetBmName() {
        assertEquals("BMName1", probeInfo.getBmName());
    }

    @Test
    public void testGetProbeName() {
        assertEquals("ProbeDescription1", probeInfo.getProbeName());
    }

    @Test
    public void testGetTargetName() {
        assertEquals("Target1", probeInfo.getTargetName());
    }

    @Test
    public void testGetBmClass() {
        assertEquals("BMClass1", probeInfo.getBmClass());
    }

    @Test
    public void testGetBmDescription() {
        assertEquals("BMDescription1", probeInfo.getBmDescription());
    }

    @Test
    public void testGetPrecision() {
        assertEquals(2, probeInfo.getPrecision());
    }

    @Test
    public void testGetMeasureURI() {
        assertEquals("URI expected value", probeInfo.getMeasureURI());
    }

    @Test
    public void testGetXmlRpcUrl() {
        assertEquals("http://xmlrpcurl.com", probeInfo.getXmlRpcUrl());
    }

    @Test
    public void testEquals_sameObject() {
        assertTrue(probeInfo.equals(probeInfo));
    }

    @Test
    public void testEquals_differentClass() {
        assertFalse(probeInfo.equals(new Object()));
    }

    @Test
    public void testEquals_differentPrecision() {
        ProbeInformation probeInfo2 = new ProbeInformation("Target1", "Type1", "BMClass1", "BMName1", "BMDescription1", "ProbeDescription1", 3, "http://xmlrpcurl.com");
        assertFalse(probeInfo.equals(probeInfo2));
    }

    @Test
    public void testEquals_nullBMClass() {
        ProbeInformation probeInfo2 = new ProbeInformation("Target1", "Type1", null, "BMName1", "BMDescription1", "ProbeDescription1", 2, "http://xmlrpcurl.com");
        assertFalse(probeInfo.equals(probeInfo2));
    }

    @Test
    public void testEquals_differentBMClass() {
        ProbeInformation probeInfo2 = new ProbeInformation("Target1", "Type1", "DifferentBMClass", "BMName1", "BMDescription1", "ProbeDescription1", 2, "http://xmlrpcurl.com");
        assertFalse(probeInfo.equals(probeInfo2));
    }

    @Test
    public void testEquals_nullBMDescription() {
        ProbeInformation probeInfo2 = new ProbeInformation("Target1", "Type1", "BMClass1", "BMName1", null, "ProbeDescription1", 2, "http://xmlrpcurl.com");
        assertFalse(probeInfo.equals(probeInfo2));
    }

    @Test
    public void testEquals_differentBMDescription() {
        ProbeInformation probeInfo2 = new ProbeInformation("Target1", "Type1", "BMClass1", "BMName1", "DifferentBMDescription", "ProbeDescription1", 2, "http://xmlrpcurl.com");
        assertFalse(probeInfo.equals(probeInfo2));
    }

    @Test
    public void testEquals_nullBMName() {
        ProbeInformation probeInfo2 = new ProbeInformation("Target1", "Type1", "BMClass1", null, "BMDescription1", "ProbeDescription1", 2, "http://xmlrpcurl.com");
        assertFalse(probeInfo.equals(probeInfo2));
    }

    @Test
    public void testEquals_differentBMName() {
        ProbeInformation probeInfo2 = new ProbeInformation("Target1", "Type1", "BMClass1", "DifferentBMName", "BMDescription1", "ProbeDescription1", 2, "http://xmlrpcurl.com");
        assertFalse(probeInfo.equals(probeInfo2));
    }

    @Test
    public void testEquals_nullProbeName() {
        ProbeInformation probeInfo2 = new ProbeInformation("Target1", "Type1", "BMClass1", "BMName1", "BMDescription1", null, 2, "http://xmlrpcurl.com");
        assertFalse(probeInfo.equals(probeInfo2));
    }

    @Test
    public void testEquals_differentProbeName() {
        ProbeInformation probeInfo2 = new ProbeInformation("Target1", "Type1", "BMClass1", "BMName1", "BMDescription1", "DifferentProbeName", 2, "http://xmlrpcurl.com");
        assertFalse(probeInfo.equals(probeInfo2));
    }

    @Test
    public void testEquals_nullTargetName() {
        ProbeInformation probeInfo2 = new ProbeInformation(null, "Type1", "BMClass1", "BMName1", "BMDescription1", "ProbeDescription1", 2, "http://xmlrpcurl.com");
        assertFalse(probeInfo.equals(probeInfo2));
    }

    @Test
    public void testEquals_differentTargetName() {
        ProbeInformation probeInfo2 = new ProbeInformation("DifferentTargetName", "Type1", "BMClass1", "BMName1", "BMDescription1", "ProbeDescription1", 2, "http://xmlrpcurl.com");
        assertFalse(probeInfo.equals(probeInfo2));
    }

    @Test
    public void testEquals_nullTargetType() {
        ProbeInformation probeInfo2 = new ProbeInformation("Target1", null, "BMClass1", "BMName1", "BMDescription1", "ProbeDescription1", 2, "http://xmlrpcurl.com");
        assertFalse(probeInfo.equals(probeInfo2));
    }

    @Test
    public void testEquals_differentTargetType() {
        ProbeInformation probeInfo2 = new ProbeInformation("Target1", "DifferentType", "BMClass1", "BMName1", "BMDescription1", "ProbeDescription1", 2, "http://xmlrpcurl.com");
        assertFalse(probeInfo.equals(probeInfo2));
    }

    @Test
    public void testHashCode_sameObject() {
        assertEquals(probeInfo.hashCode(), probeInfo.hashCode());
    }

    @Test
    public void testHashCode_equalObjects() {
        ProbeInformation probeInfo2 = new ProbeInformation("Target1", "Type1", "BMClass1", "BMName1", "BMDescription1", "ProbeDescription1", 2, "http://xmlrpcurl.com");
        assertEquals(probeInfo.hashCode(), probeInfo2.hashCode());
    }

    @Test
    public void testHashCode_differentPrecision() {
        ProbeInformation probeInfo2 = new ProbeInformation("Target1", "Type1", "BMClass1", "BMName1", "BMDescription1", "ProbeDescription1", 3, "http://xmlrpcurl.com");
        assertNotEquals(probeInfo.hashCode(), probeInfo2.hashCode());
    }

    @Test
    public void testHashCode_nullBMClass() {
        ProbeInformation probeInfo2 = new ProbeInformation("Target1", "Type1", null, "BMName1", "BMDescription1", "ProbeDescription1", 2, "http://xmlrpcurl.com");
        assertNotEquals(probeInfo.hashCode(), probeInfo2.hashCode());
    }

    @Test
    public void testHashCode_differentBMClass() {
        ProbeInformation probeInfo2 = new ProbeInformation("Target1", "Type1", "DifferentBMClass", "BMName1", "BMDescription1", "ProbeDescription1", 2, "http://xmlrpcurl.com");
        assertNotEquals(probeInfo.hashCode(), probeInfo2.hashCode());
    }

    @Test
    public void testHashCode_nullBMDescription() {
        ProbeInformation probeInfo2 = new ProbeInformation("Target1", "Type1", "BMClass1", "BMName1", null, "ProbeDescription1", 2, "http://xmlrpcurl.com");
        assertNotEquals(probeInfo.hashCode(), probeInfo2.hashCode());
    }

    @Test
    public void testHashCode_differentBMDescription() {
        ProbeInformation probeInfo2 = new ProbeInformation("Target1", "Type1", "BMClass1", "BMName1", "DifferentBMDescription", "ProbeDescription1", 2, "http://xmlrpcurl.com");
        assertNotEquals(probeInfo.hashCode(), probeInfo2.hashCode());
    }

    @Test
    public void testHashCode_nullBMName() {
        ProbeInformation probeInfo2 = new ProbeInformation("Target1", "Type1", "BMClass1", null, "BMDescription1", "ProbeDescription1", 2, "http://xmlrpcurl.com");
        assertNotEquals(probeInfo.hashCode(), probeInfo2.hashCode());
    }

    @Test
    public void testHashCode_differentBMName() {
        ProbeInformation probeInfo2 = new ProbeInformation("Target1", "Type1", "BMClass1", "DifferentBMName", "BMDescription1", "ProbeDescription1", 2, "http://xmlrpcurl.com");
        assertNotEquals(probeInfo.hashCode(), probeInfo2.hashCode());
    }

    @Test
    public void testHashCode_nullProbeName() {
        ProbeInformation probeInfo2 = new ProbeInformation("Target1", "Type1", "BMClass1", "BMName1", "BMDescription1", null, 2, "http://xmlrpcurl.com");
        assertNotEquals(probeInfo.hashCode(), probeInfo2.hashCode());
    }

    @Test
    public void testHashCode_differentProbeName() {
        ProbeInformation probeInfo2 = new ProbeInformation("Target1", "Type1", "BMClass1", "BMName1", "BMDescription1", "DifferentProbeName", 2, "http://xmlrpcurl.com");
        assertNotEquals(probeInfo.hashCode(), probeInfo2.hashCode());
    }

    @Test
    public void testHashCode_nullTargetName() {
        ProbeInformation probeInfo2 = new ProbeInformation(null, "Type1", "BMClass1", "BMName1", "BMDescription1", "ProbeDescription1", 2, "http://xmlrpcurl.com");
        assertNotEquals(probeInfo.hashCode(), probeInfo2.hashCode());
    }

    @Test
    public void testHashCode_differentTargetName() {
        ProbeInformation probeInfo2 = new ProbeInformation("DifferentTargetName", "Type1", "BMClass1", "BMName1", "BMDescription1", "ProbeDescription1", 2, "http://xmlrpcurl.com");
        assertNotEquals(probeInfo.hashCode(), probeInfo2.hashCode());
    }

    @Test
    public void testHashCode_nullTargetType() {
        ProbeInformation probeInfo2 = new ProbeInformation("Target1", null, "BMClass1", "BMName1", "BMDescription1", "ProbeDescription1", 2, "http://xmlrpcurl.com");
        assertNotEquals(probeInfo.hashCode(), probeInfo2.hashCode());
    }

    @Test
    public void testHashCode_differentTargetType() {
        ProbeInformation probeInfo2 = new ProbeInformation("Target1", "DifferentType", "BMClass1", "BMName1", "BMDescription1", "ProbeDescription1", 2, "http://xmlrpcurl.com");
        assertNotEquals(probeInfo.hashCode(), probeInfo2.hashCode());
    }
}