package fi.vtt.noen.mfw.bundle.probe.shared;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class ProbeInformationTest {
    @Test
    public void testGetTargetType() {
        ProbeInformation probeInformation = new ProbeInformation("Target1", "Type1", "bmClass1", "bmName1", "bmDescription1", "probeDescription1", 1, "xmlRpcUrl1");
        assertEquals("Type1", probeInformation.getTargetType());
    }

    @Test
    public void testGetBmName() {
        ProbeInformation probeInformation = new ProbeInformation("Target1", "Type1", "bmClass1", "bmName1", "bmDescription1", "probeDescription1", 1, "xmlRpcUrl1");
        assertEquals("bmName1", probeInformation.getBmName());
    }

    @Test
    public void testGetProbeName() {
        ProbeInformation probeInformation = new ProbeInformation("Target1", "Type1", "bmClass1", "bmName1", "bmDescription1", "probeDescription1", 1, "xmlRpcUrl1");
        assertEquals("probeDescription1", probeInformation.getProbeName());
    }

    @Test
    public void testGetTargetName() {
        ProbeInformation probeInformation = new ProbeInformation("Target1", "Type1", "bmClass1", "bmName1", "bmDescription1", "probeDescription1", 1, "xmlRpcUrl1");
        assertEquals("Target1", probeInformation.getTargetName());
    }

    @Test
    public void testGetBmClass() {
        ProbeInformation probeInformation = new ProbeInformation("Target1", "Type1", "bmClass1", "bmName1", "bmDescription1", "probeDescription1", 1, "xmlRpcUrl1");
        assertEquals("bmClass1", probeInformation.getBmClass());
    }

    @Test
    public void testGetBmDescription() {
        ProbeInformation probeInformation = new ProbeInformation("Target1", "Type1", "bmClass1", "bmName1", "bmDescription1", "probeDescription1", 1, "xmlRpcUrl1");
        assertEquals("bmDescription1", probeInformation.getBmDescription());
    }

    @Test
    public void testGetPrecision() {
        ProbeInformation probeInformation = new ProbeInformation("Target1", "Type1", "bmClass1", "bmName1", "bmDescription1", "probeDescription1", 1, "xmlRpcUrl1");
        assertEquals(1, probeInformation.getPrecision());
    }

    @Test
    public void testGetMeasureURI() {
        ProbeInformation probeInformation = new ProbeInformation("Target1", "Type1", "bmClass1", "bmName1", "bmDescription1", "probeDescription1", 1, "xmlRpcUrl1");
        assertEquals("Type1_Target1_bmClass1_bmName1", probeInformation.getMeasureURI());
    }

    @Test
    public void testGetXmlRpcUrl() {
        ProbeInformation probeInformation = new ProbeInformation("Target1", "Type1", "bmClass1", "bmName1", "bmDescription1", "probeDescription1", 1, "xmlRpcUrl1");
        assertEquals("xmlRpcUrl1", probeInformation.getXmlRpcUrl());
    }

    @Test
    public void testToString() {
        ProbeInformation probeInformation = new ProbeInformation("Target1", "Type1", "bmClass1", "bmName1", "bmDescription1", "probeDescription1", 1, "xmlRpcUrl1");
        assertEquals("ProbeInformation{targetName='Target1', targetType='Type1', bmClass='bmClass1', bmName='bmName1', bmDescription='bmDescription1', probeDescription='probeDescription1', precision=1}", probeInformation.toString());
    }

    @Test
    public void testEquals() {
        ProbeInformation probeInformation1 = new ProbeInformation("Target1", "Type1", "bmClass1", "bmName1", "bmDescription1", "probeDescription1", 1, "xmlRpcUrl1");
        ProbeInformation probeInformation2 = new ProbeInformation("Target1", "Type1", "bmClass1", "bmName1", "bmDescription1", "probeDescription1", 1, "xmlRpcUrl1");
        assertEquals(true, probeInformation1.equals(probeInformation2));
    }

    @Test
    public void testHashCode() {
        ProbeInformation probeInformation1 = new ProbeInformation("Target1", "Type1", "bmClass1", "bmName1", "bmDescription1", "probeDescription1", 1, "xmlRpcUrl1");
        ProbeInformation probeInformation2 = new ProbeInformation("Target1", "Type1", "bmClass1", "bmName1", "bmDescription1", "probeDescription1", 1, "xmlRpcUrl1");
        assertEquals(probeInformation1.hashCode(), probeInformation2.hashCode());
    }
}