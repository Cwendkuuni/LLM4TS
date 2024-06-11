import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ProbeInformationTest {

    private ProbeInformation probeInfo;

    @Before
    public void setup() {
        probeInfo = new ProbeInformation("targetName", "targetType", "bmClass", "bmName", "bmDescription", "probeDescription", 5, "xmlRpcUrl");
    }

    @Test
    public void testGetTargetType() {
        String expected = "targetType";
        String actual = probeInfo.getTargetType();
        assertEquals(expected, actual);
    }

    @Test
    public void testGetBmName() {
        String expected = "bmName";
        String actual = probeInfo.getBmName();
        assertEquals(expected, actual);
    }

    @Test
    public void testGetProbeName() {
        String expected = "probeDescription";
        String actual = probeInfo.getProbeName();
        assertEquals(expected, actual);
    }

    @Test
    public void testGetTargetName() {
        String expected = "targetName";
        String actual = probeInfo.getTargetName();
        assertEquals(expected, actual);
    }

    @Test
    public void testGetBmClass() {
        String expected = "bmClass";
        String actual = probeInfo.getBmClass();
        assertEquals(expected, actual);
    }

    @Test
    public void testGetBmDescription() {
        String expected = "bmDescription";
        String actual = probeInfo.getBmDescription();
        assertEquals(expected, actual);
    }

    @Test
    public void testGetPrecision() {
        int expected = 5;
        int actual = probeInfo.getPrecision();
        assertEquals(expected, actual);
    }

    @Test
    public void testGetMeasureURI() {
        String expected = "measureURI";
        String actual = probeInfo.getMeasureURI();
        //Replace with actual implementation of method Const.createMeasureURI()
        assertEquals(expected, actual);
    }

    @Test
    public void testGetXmlRpcUrl() {
        String expected = "xmlRpcUrl";
        String actual = probeInfo.getXmlRpcUrl();
        assertEquals(expected, actual);
    }

    @Test
    public void testToString() {
        String expected = "ProbeInformation{targetName='targetName', targetType='targetType', bmClass='bmClass', bmName='bmName', bmDescription='bmDescription', probeDescription='probeDescription', precision=5}";
        String actual = probeInfo.toString();
        assertEquals(expected, actual);
    }

    @Test
    public void testEquals() {
        ProbeInformation probeInfo2 = new ProbeInformation("targetName", "targetType", "bmClass", "bmName", "bmDescription", "probeDescription", 5, "xmlRpcUrl");
        boolean expected = true;
        boolean actual = probeInfo.equals(probeInfo2);
        assertEquals(expected, actual);
    }

    @Test
    public void testHashCode() {
        int expected = probeInfo.hashCode();
        int actual = probeInfo.hashCode();
        assertEquals(expected, actual);
    }
}