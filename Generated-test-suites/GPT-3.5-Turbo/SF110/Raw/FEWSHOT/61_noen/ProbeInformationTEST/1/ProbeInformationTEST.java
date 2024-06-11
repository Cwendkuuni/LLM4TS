package fi.vtt.noen.mfw.bundle.probe.shared;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class ProbeInformationTest {
    @Test
    public void testGetTargetType() {
        ProbeInformation probeInformation = new ProbeInformation("targetName1", "targetType1", "bmClass1", "bmName1", "bmDescription1", "probeDescription1", 5, "xmlRpcUrl1");
        assertEquals("targetType1", probeInformation.getTargetType());
    }

    @Test
    public void testGetBmName() {
        ProbeInformation probeInformation = new ProbeInformation("targetName2", "targetType2", "bmClass2", "bmName2", "bmDescription2", "probeDescription2", 7, "xmlRpcUrl2");
        assertEquals("bmName2", probeInformation.getBmName());
    }

    @Test
    public void testGetProbeName() {
        ProbeInformation probeInformation = new ProbeInformation("targetName3", "targetType3", "bmClass3", "bmName3", "bmDescription3", "probeDescription3", 10, "xmlRpcUrl3");
        assertEquals("probeDescription3", probeInformation.getProbeName());
    }

    @Test
    public void testGetTargetName() {
        ProbeInformation probeInformation = new ProbeInformation("targetName4", "targetType4", "bmClass4", "bmName4", "bmDescription4", "probeDescription4", 8, "xmlRpcUrl4");
        assertEquals("targetName4", probeInformation.getTargetName());
    }

    @Test
    public void testGetBmClass() {
        ProbeInformation probeInformation = new ProbeInformation("targetName5", "targetType5", "bmClass5", "bmName5", "bmDescription5", "probeDescription5", 3, "xmlRpcUrl5");
        assertEquals("bmClass5", probeInformation.getBmClass());
    }

    @Test
    public void testGetBmDescription() {
        ProbeInformation probeInformation = new ProbeInformation("targetName6", "targetType6", "bmClass6", "bmName6", "bmDescription6", "probeDescription6", 6, "xmlRpcUrl6");
        assertEquals("bmDescription6", probeInformation.getBmDescription());
    }

    @Test
    public void testGetPrecision() {
        ProbeInformation probeInformation = new ProbeInformation("targetName7", "targetType7", "bmClass7", "bmName7", "bmDescription7", "probeDescription7", 9, "xmlRpcUrl7");
        assertEquals(9, probeInformation.getPrecision());
    }

    @Test
    public void testGetMeasureURI() {
        ProbeInformation probeInformation = new ProbeInformation("targetName8", "targetType8", "bmClass8", "bmName8", "bmDescription8", "probeDescription8", 4, "xmlRpcUrl8");
        assertNotNull(probeInformation.getMeasureURI());
    }

    @Test
    public void testGetXmlRpcUrl() {
        ProbeInformation probeInformation = new ProbeInformation("targetName9", "targetType9", "bmClass9", "bmName9", "bmDescription9", "probeDescription9", 2, "xmlRpcUrl9");
        assertEquals("xmlRpcUrl9", probeInformation.getXmlRpcUrl());
    }

    @Test
    public void testToString() {
        ProbeInformation probeInformation = new ProbeInformation("targetName10", "targetType10", "bmClass10", "bmName10", "bmDescription10", "probeDescription10", 1, "xmlRpcUrl10");
        assertEquals("ProbeInformation{targetName='targetName10', targetType='targetType10', bmClass='bmClass10', bmName='bmName10', bmDescription='bmDescription10', probeDescription='probeDescription10', precision=1}", probeInformation.toString());
    }

    @Test
    public void testEquals() {
        ProbeInformation probeInformation1 = new ProbeInformation("targetName11", "targetType11", "bmClass11", "bmName11", "bmDescription11", "probeDescription11", 2, "xmlRpcUrl11");
        ProbeInformation probeInformation2 = new ProbeInformation("targetName11", "targetType11", "bmClass11", "bmName11", "bmDescription11", "probeDescription11", 2, "xmlRpcUrl11");
        assertEquals(probeInformation1, probeInformation2);
    }

    @Test
    public void testHashCode() {
        ProbeInformation probeInformation = new ProbeInformation("targetName12", "targetType12", "bmClass12", "bmName12", "bmDescription12", "probeDescription12", 3, "xmlRpcUrl12");
        assertNotNull(probeInformation.hashCode());
    }
}