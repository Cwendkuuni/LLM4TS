
import org.junit.Test;
import static org.junit.Assert.*;

public class ProbeInformationTest {

    @Test
    public void testGetTargetType() {
        ProbeInformation probeInfo = new ProbeInformation("target", "type", "bmClass", "bmName", "bmDesc", "probeDesc", 1, "xmlRpcUrl");
        assertEquals("type", probeInfo.getTargetType());
    }
    
    @Test
    public void testGetBmName() {
        ProbeInformation probeInfo = new ProbeInformation("target", "type", "bmClass", "bmName", "bmDesc", "probeDesc", 1, "xmlRpcUrl");
        assertEquals("bmName", probeInfo.getBmName());
    }
    
    @Test
    public void testGetProbeName() {
        ProbeInformation probeInfo = new ProbeInformation("target", "type", "bmClass", "bmName", "bmDesc", "probeDesc", 1, "xmlRpcUrl");
        assertEquals("probeDesc", probeInfo.getProbeName());
    }
    
    @Test
    public void testGetTargetName() {
        ProbeInformation probeInfo = new ProbeInformation("target", "type", "bmClass", "bmName", "bmDesc", "probeDesc", 1, "xmlRpcUrl");
        assertEquals("target", probeInfo.getTargetName());
    }
    
    @Test
    public void testGetBmClass() {
        ProbeInformation probeInfo = new ProbeInformation("target", "type", "bmClass", "bmName", "bmDesc", "probeDesc", 1, "xmlRpcUrl");
        assertEquals("bmClass", probeInfo.getBmClass());
    }
    
    @Test
    public void testGetBmDescription() {
        ProbeInformation probeInfo = new ProbeInformation("target", "type", "bmClass", "bmName", "bmDesc", "probeDesc", 1, "xmlRpcUrl");
        assertEquals("bmDesc", probeInfo.getBmDescription());
    }
    
    @Test
    public void testGetPrecision() {
        ProbeInformation probeInfo = new ProbeInformation("target", "type", "bmClass", "bmName", "bmDesc", "probeDesc", 1, "xmlRpcUrl");
        assertEquals(1, probeInfo.getPrecision());
    }
    
    @Test
    public void testGetMeasureURI() {
        ProbeInformation probeInfo = new ProbeInformation("target", "type", "bmClass", "bmName", "bmDesc", "probeDesc", 1, "xmlRpcUrl");
        String expected = "fi.vtt.noen.mfw.bundle.common.Const.createMeasureURI(type, target, bmClass, bmName)";
        assertEquals(expected, probeInfo.getMeasureURI());
    }
    
    @Test
    public void testGetXmlRpcUrl() {
        ProbeInformation probeInfo = new ProbeInformation("target", "type", "bmClass", "bmName", "bmDesc", "probeDesc", 1, "xmlRpcUrl");
        assertEquals("xmlRpcUrl", probeInfo.getXmlRpcUrl());
    }
    
    @Test
    public void testToString() {
        ProbeInformation probeInfo = new ProbeInformation("target", "type", "bmClass", "bmName", "bmDesc", "probeDesc", 1, "xmlRpcUrl");
        String expected = "ProbeInformation{targetName='target', targetType='type', bmClass='bmClass', bmName='bmName', bmDescription='bmDesc', probeDescription='probeDesc', precision=1}";
        assertEquals(expected, probeInfo.toString());
    }
    
    @Test
    public void testEquals() {
        ProbeInformation probeInfo1 = new ProbeInformation("target", "type", "bmClass", "bmName", "bmDesc", "probeDesc", 1, "xmlRpcUrl");
        ProbeInformation probeInfo2 = new ProbeInformation("target", "type", "bmClass", "bmName", "bmDesc", "probeDesc", 1, "xmlRpcUrl");
        assertTrue(probeInfo1.equals(probeInfo2));
    }
    
    @Test
    public void testHashCode() {
        ProbeInformation probeInfo1 = new ProbeInformation("target", "type", "bmClass", "bmName", "bmDesc", "probeDesc", 1, "xmlRpcUrl");
        ProbeInformation probeInfo2 = new ProbeInformation("target", "type", "bmClass", "bmName", "bmDesc", "probeDesc", 1, "xmlRpcUrl");
        assertEquals(probeInfo1.hashCode(), probeInfo2.hashCode());
    }
}
