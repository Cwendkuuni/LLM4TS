import org.junit.Test;
import static org.junit.Assert.*;

public class ProbeInformationTest {

    // Test getTargetType() method
    @Test
    public void testGetTargetType() {
        ProbeInformation probeInfo = new ProbeInformation("target", "type", "bmClass", "bmName", "bmDescription", "probeName", 5, "xmlRpcUrl");
        String expectedResult = "type";
        String actualResult = probeInfo.getTargetType();
        assertEquals(expectedResult, actualResult);
    }

    // Test getBmName() method
    @Test
    public void testGetBmName() {
        ProbeInformation probeInfo = new ProbeInformation("target", "type", "bmClass", "bmName", "bmDescription", "probeName", 5, "xmlRpcUrl");
        String expectedResult = "bmName";
        String actualResult = probeInfo.getBmName();
        assertEquals(expectedResult, actualResult);
    }

    // Test getProbeName() method
    @Test
    public void testGetProbeName() {
        ProbeInformation probeInfo = new ProbeInformation("target", "type", "bmClass", "bmName", "bmDescription", "probeName", 5, "xmlRpcUrl");
        String expectedResult = "probeName";
        String actualResult = probeInfo.getProbeName();
        assertEquals(expectedResult, actualResult);
    }

    // Test getTargetName() method
    @Test
    public void testGetTargetName() {
        ProbeInformation probeInfo = new ProbeInformation("target", "type", "bmClass", "bmName", "bmDescription", "probeName", 5, "xmlRpcUrl");
        String expectedResult = "target";
        String actualResult = probeInfo.getTargetName();
        assertEquals(expectedResult, actualResult);
    }

    // Test getBmClass() method
    @Test
    public void testGetBmClass() {
        ProbeInformation probeInfo = new ProbeInformation("target", "type", "bmClass", "bmName", "bmDescription", "probeName", 5, "xmlRpcUrl");
        String expectedResult = "bmClass";
        String actualResult = probeInfo.getBmClass();
        assertEquals(expectedResult, actualResult);
    }

    // Test getBmDescription() method
    @Test
    public void testGetBmDescription() {
        ProbeInformation probeInfo = new ProbeInformation("target", "type", "bmClass", "bmName", "bmDescription", "probeName", 5, "xmlRpcUrl");
        String expectedResult = "bmDescription";
        String actualResult = probeInfo.getBmDescription();
        assertEquals(expectedResult, actualResult);
    }

    // Test getPrecision() method
    @Test
    public void testGetPrecision() {
        ProbeInformation probeInfo = new ProbeInformation("target", "type", "bmClass", "bmName", "bmDescription", "probeName", 5, "xmlRpcUrl");
        int expectedResult = 5;
        int actualResult = probeInfo.getPrecision();
        assertEquals(expectedResult, actualResult);
    }

    // Test getMeasureURI() method
    @Test
    public void testGetMeasureURI() {
        ProbeInformation probeInfo = new ProbeInformation("target", "type", "bmClass", "bmName", "bmDescription", "probeName", 5, "xmlRpcUrl");
        String expectedResult = "uri";
        String actualResult = probeInfo.getMeasureURI();
        assertEquals(expectedResult, actualResult);
    }

    // Test getXmlRpcUrl() method
    @Test
    public void testGetXmlRpcUrl() {
        ProbeInformation probeInfo = new ProbeInformation("target", "type", "bmClass", "bmName", "bmDescription", "probeName", 5, "xmlRpcUrl");
        String expectedResult = "xmlRpcUrl";
        String actualResult = probeInfo.getXmlRpcUrl();
        assertEquals(expectedResult, actualResult);
    }

    // Test toString() method
    @Test
    public void testToString() {
        ProbeInformation probeInfo = new ProbeInformation("target", "type", "bmClass", "bmName", "bmDescription", "probeName", 5, "xmlRpcUrl");
        String expectedResult = "ProbeInformation{targetName='target', targetType='type', bmClass='bmClass', bmName='bmName', bmDescription='bmDescription', probeDescription='probeName', precision=5}";
        String actualResult = probeInfo.toString();
        assertEquals(expectedResult, actualResult);
    }

    // Test equals() method with equal objects
    @Test
    public void testEqualsEqualObjects() {
        ProbeInformation probeInfo1 = new ProbeInformation("target", "type", "bmClass", "bmName", "bmDescription", "probeName", 5, "xmlRpcUrl");
        ProbeInformation probeInfo2 = new ProbeInformation("target", "type", "bmClass", "bmName", "bmDescription", "probeName", 5, "xmlRpcUrl");
        boolean expectedResult = true;
        boolean actualResult = probeInfo1.equals(probeInfo2);
        assertEquals(expectedResult, actualResult);
    }

    // Test equals() method with different precision
    @Test
    public void testEqualsDifferentPrecision() {
        ProbeInformation probeInfo1 = new ProbeInformation("target", "type", "bmClass", "bmName", "bmDescription", "probeName", 5, "xmlRpcUrl");
        ProbeInformation probeInfo2 = new ProbeInformation("target", "type", "bmClass", "bmName", "bmDescription", "probeName", 10, "xmlRpcUrl");
        boolean expectedResult = false;
        boolean actualResult = probeInfo1.equals(probeInfo2);
        assertEquals(expectedResult, actualResult);
    }

    // Test equals() method with different bmClass
    @Test
    public void testEqualsDifferentBmClass() {
        ProbeInformation probeInfo1 = new ProbeInformation("target", "type", "bmClass", "bmName", "bmDescription", "probeName", 5, "xmlRpcUrl");
        ProbeInformation probeInfo2 = new ProbeInformation("target", "type", "anotherBmClass", "bmName", "bmDescription", "probeName", 5, "xmlRpcUrl");
        boolean expectedResult = false;
        boolean actualResult = probeInfo1.equals(probeInfo2);
        assertEquals(expectedResult, actualResult);
    }

    // Test equals() method with different bmDescription
    @Test
    public void testEqualsDifferentBmDescription() {
        ProbeInformation probeInfo1 = new ProbeInformation("target", "type", "bmClass", "bmName", "bmDescription", "probeName", 5, "xmlRpcUrl");
        ProbeInformation probeInfo2 = new ProbeInformation("target", "type", "bmClass", "bmName", "anotherBmDescription", "probeName", 5, "xmlRpcUrl");
        boolean expectedResult = false;
        boolean actualResult = probeInfo1.equals(probeInfo2);
        assertEquals(expectedResult, actualResult);
    }

    // Test equals() method with different bmName
    @Test
    public void testEqualsDifferentBmName() {
        ProbeInformation probeInfo1 = new ProbeInformation("target", "type", "bmClass", "bmName", "bmDescription", "probeName", 5, "xmlRpcUrl");
        ProbeInformation probeInfo2 = new ProbeInformation("target", "type", "bmClass", "anotherBmName", "bmDescription", "probeName", 5, "xmlRpcUrl");
        boolean expectedResult = false;
        boolean actualResult = probeInfo1.equals(probeInfo2);
        assertEquals(expectedResult, actualResult);
    }

    // Test equals() method with different probeName
    @Test
    public void testEqualsDifferentProbeName() {
        ProbeInformation probeInfo1 = new ProbeInformation("target", "type", "bmClass", "bmName", "bmDescription", "probeName", 5, "xmlRpcUrl");
        ProbeInformation probeInfo2 = new ProbeInformation("target", "type", "bmClass", "bmName", "bmDescription", "anotherProbeName", 5, "xmlRpcUrl");
        boolean expectedResult = false;
        boolean actualResult = probeInfo1.equals(probeInfo2);
        assertEquals(expectedResult, actualResult);
    }

    // Test equals() method with different targetName
    @Test
    public void testEqualsDifferentTargetName() {
        ProbeInformation probeInfo1 = new ProbeInformation("target", "type", "bmClass", "bmName", "bmDescription", "probeName", 5, "xmlRpcUrl");
        ProbeInformation probeInfo2 = new ProbeInformation("anotherTarget", "type", "bmClass", "bmName", "bmDescription", "probeName", 5, "xmlRpcUrl");
        boolean expectedResult = false;
        boolean actualResult = probeInfo1.equals(probeInfo2);
        assertEquals(expectedResult, actualResult);
    }

    // Test equals() method with different targetType
    @Test
    public void testEqualsDifferentTargetType() {
        ProbeInformation probeInfo1 = new ProbeInformation("target", "type", "bmClass", "bmName", "bmDescription", "probeName", 5, "xmlRpcUrl");
        ProbeInformation probeInfo2 = new ProbeInformation("target", "anotherType", "bmClass", "bmName", "bmDescription", "probeName", 5, "xmlRpcUrl");
        boolean expectedResult = false;
        boolean actualResult = probeInfo1.equals(probeInfo2);
        assertEquals(expectedResult, actualResult);
    }

    // Test equals() method with null object
    @Test
    public void testEqualsNullObject() {
        ProbeInformation probeInfo = new ProbeInformation("target", "type", "bmClass", "bmName", "bmDescription", "probeName", 5, "xmlRpcUrl");
        boolean expectedResult = false;
        boolean actualResult = probeInfo.equals(null);
        assertEquals(expectedResult, actualResult);
    }

    // Test equals() method with different class object
    @Test
    public void testEqualsDifferentClassObject() {
        ProbeInformation probeInfo = new ProbeInformation("target", "type", "bmClass", "bmName", "bmDescription", "probeName", 5, "xmlRpcUrl");
        Object obj = new Object();
        boolean expectedResult = false;
        boolean actualResult = probeInfo.equals(obj);
        assertEquals(expectedResult, actualResult);
    }
}