
package fi.vtt.noen.mfw.bundle.probe.shared;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class ProbeInformationTest {

    @Test
    public void testGetTargetType() {
        ProbeInformation probeInfo = new ProbeInformation("", "type", "", "", "", "", 0, "");
        assertEquals("type", probeInfo.getTargetType());
    }

    @Test
    public void testGetBmName() {
        ProbeInformation probeInfo = new ProbeInformation("", "", "", "bmName", "", "", 0, "");
        assertEquals("bmName", probeInfo.getBmName());
    }

    @Test
    public void testGetProbeName() {
        ProbeInformation probeInfo = new ProbeInformation("", "", "", "", "", "probeName", 0, "");
        assertEquals("probeName", probeInfo.getProbeName());
    }

    @Test
    public void testGetTargetName() {
        ProbeInformation probeInfo = new ProbeInformation("targetName", "", "", "", "", "", 0, "");
        assertEquals("targetName", probeInfo.getTargetName());
    }

    @Test
    public void testGetBmClass() {
        ProbeInformation probeInfo = new ProbeInformation("", "", "bmClass", "", "", "", 0, "");
        assertEquals("bmClass", probeInfo.getBmClass());
    }

    @Test
    public void testGetBmDescription() {
        ProbeInformation probeInfo = new ProbeInformation("", "", "", "", "bmDescription", "", 0, "");
        assertEquals("bmDescription", probeInfo.getBmDescription());
    }

    @Test
    public void testGetPrecision() {
        ProbeInformation probeInfo = new ProbeInformation("", "", "", "", "", "", 10, "");
        assertEquals(10, probeInfo.getPrecision());
    }

    @Test
    public void testGetMeasureURI() {
        ProbeInformation probeInfo = new ProbeInformation("", "type", "bmClass", "bmName", "", "", 0, "");
        assertEquals("measureURI", probeInfo.getMeasureURI());
    }

    @Test
    public void testGetXmlRpcUrl() {
        ProbeInformation probeInfo = new ProbeInformation("", "", "", "", "", "", 0, "xmlRpcUrl");
        assertEquals("xmlRpcUrl", probeInfo.getXmlRpcUrl());
    }

    @Test
    public void testToString() {
        ProbeInformation probeInfo = new ProbeInformation("targetName", "type", "bmClass", "bmName", "bmDescription", "probeName", 10, "xmlRpcUrl");
        String expected = "ProbeInformation{targetName='targetName', targetType='type', bmClass='bmClass', bmName='bmName', bmDescription='bmDescription', probeDescription='probeName', precision=10}";
        assertEquals(expected, probeInfo.toString());
    }

    @Test
    public void testEquals() {
        ProbeInformation probeInfo1 = new ProbeInformation("targetName", "type", "bmClass", "bmName", "bmDescription", "probeName", 10, "xmlRpcUrl");
        ProbeInformation probeInfo2 = new ProbeInformation("targetName", "type", "bmClass", "bmName", "bmDescription", "probeName", 10, "xmlRpcUrl");
        assertEquals(true, probeInfo1.equals(probeInfo2));
        assertEquals(true, probeInfo2.equals(probeInfo1));
    }

    @Test
    public void testHashCode() {
        ProbeInformation probeInfo1 = new ProbeInformation("targetName", "type", "bmClass", "bmName", "bmDescription", "probeName", 10, "xmlRpcUrl");
        ProbeInformation probeInfo2 = new ProbeInformation("targetName", "type", "bmClass", "bmName", "bmDescription", "probeName", 10, "xmlRpcUrl");
        assertEquals(probeInfo1.hashCode(), probeInfo2.hashCode());
    }
}
