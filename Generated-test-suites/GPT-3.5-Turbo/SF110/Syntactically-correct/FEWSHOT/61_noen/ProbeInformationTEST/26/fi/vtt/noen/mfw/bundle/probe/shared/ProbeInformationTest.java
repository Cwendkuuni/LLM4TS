package fi.vtt.noen.mfw.bundle.probe.shared;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class ProbeInformationTest {
    @Test
    public void testGetTargetType() {
        ProbeInformation info = new ProbeInformation("target", "type", "bmClass", "bmName", "bmDescription", "probeDescription", 2, "xmlRpcUrl");
        assertEquals("type", info.getTargetType());
    }

    @Test
    public void testGetBmName() {
        ProbeInformation info = new ProbeInformation("target", "type", "bmClass", "bmName", "bmDescription", "probeDescription", 2, "xmlRpcUrl");
        assertEquals("bmName", info.getBmName());
    }

    @Test
    public void testGetProbeName() {
        ProbeInformation info = new ProbeInformation("target", "type", "bmClass", "bmName", "bmDescription", "probeDescription", 2, "xmlRpcUrl");
        assertEquals("probeDescription", info.getProbeName());
    }

    @Test
    public void testGetTargetName() {
        ProbeInformation info = new ProbeInformation("target", "type", "bmClass", "bmName", "bmDescription", "probeDescription", 2, "xmlRpcUrl");
        assertEquals("target", info.getTargetName());
    }

    @Test
    public void testGetBmClass() {
        ProbeInformation info = new ProbeInformation("target", "type", "bmClass", "bmName", "bmDescription", "probeDescription", 2, "xmlRpcUrl");
        assertEquals("bmClass", info.getBmClass());
    }

    @Test
    public void testGetBmDescription() {
        ProbeInformation info = new ProbeInformation("target", "type", "bmClass", "bmName", "bmDescription", "probeDescription", 2, "xmlRpcUrl");
        assertEquals("bmDescription", info.getBmDescription());
    }

    @Test
    public void testGetPrecision() {
        ProbeInformation info = new ProbeInformation("target", "type", "bmClass", "bmName", "bmDescription", "probeDescription", 2, "xmlRpcUrl");
        assertEquals(2, info.getPrecision());
    }

    @Test
    public void testGetMeasureURI() {
        ProbeInformation info = new ProbeInformation("target", "type", "bmClass", "bmName", "bmDescription", "probeDescription", 2, "xmlRpcUrl");
        assertEquals("measureURI", info.getMeasureURI());
    }

    @Test
    public void testGetXmlRpcUrl() {
        ProbeInformation info = new ProbeInformation("target", "type", "bmClass", "bmName", "bmDescription", "probeDescription", 2, "xmlRpcUrl");
        assertEquals("xmlRpcUrl", info.getXmlRpcUrl());
    }

    @Test
    public void testToString() {
        ProbeInformation info = new ProbeInformation("target", "type", "bmClass", "bmName", "bmDescription", "probeDescription", 2, "xmlRpcUrl");
        assertEquals("ProbeInformation{targetName='target', targetType='type', bmClass='bmClass', bmName='bmName', bmDescription='bmDescription', probeDescription='probeDescription', precision=2}", info.toString());
    }

    @Test
    public void testEquals() {
        ProbeInformation info1 = new ProbeInformation("target", "type", "bmClass", "bmName", "bmDescription", "probeDescription", 2, "xmlRpcUrl");
        ProbeInformation info2 = new ProbeInformation("target", "type", "bmClass", "bmName", "bmDescription", "probeDescription", 2, "xmlRpcUrl");
        ProbeInformation info3 = new ProbeInformation("target1", "type1", "bmClass1", "bmName1", "bmDescription1", "probeDescription1", 1, "xmlRpcUrl1");

        assertEquals(info1, info2);
        assertEquals(info1.hashCode(), info2.hashCode());
        assertEquals(false, info1.equals(info3));
    }

    @Test
    public void testHashCode() {
        ProbeInformation info1 = new ProbeInformation("target", "type", "bmClass", "bmName", "bmDescription", "probeDescription", 2, "xmlRpcUrl");
        ProbeInformation info2 = new ProbeInformation("target", "type", "bmClass", "bmName", "bmDescription", "probeDescription", 2, "xmlRpcUrl");

        assertEquals(info1.hashCode(), info2.hashCode());
    }
}