import org.junit.Test;
import static org.junit.Assert.*;

public class ProbeInformationTest {

    @Test
    public void testGetTargetType() {
        ProbeInformation probeInfo = new ProbeInformation("target", "type", "class", "name", "description", "probe", 1, "url");
        assertEquals("type", probeInfo.getTargetType());
    }
    
    @Test
    public void testGetBmName() {
        ProbeInformation probeInfo = new ProbeInformation("target", "type", "class", "name", "description", "probe", 1, "url");
        assertEquals("name", probeInfo.getBmName());
    }
    
    @Test
    public void testGetProbeName() {
        ProbeInformation probeInfo = new ProbeInformation("target", "type", "class", "name", "description", "probe", 1, "url");
        assertEquals("probe", probeInfo.getProbeName());
    }
    
    @Test
    public void testGetTargetName() {
        ProbeInformation probeInfo = new ProbeInformation("target", "type", "class", "name", "description", "probe", 1, "url");
        assertEquals("target", probeInfo.getTargetName());
    }
    
    @Test
    public void testGetBmClass() {
        ProbeInformation probeInfo = new ProbeInformation("target", "type", "class", "name", "description", "probe", 1, "url");
        assertEquals("class", probeInfo.getBmClass());
    }
    
    @Test
    public void testGetBmDescription() {
        ProbeInformation probeInfo = new ProbeInformation("target", "type", "class", "name", "description", "probe", 1, "url");
        assertEquals("description", probeInfo.getBmDescription());
    }
    
    @Test
    public void testGetPrecision() {
        ProbeInformation probeInfo = new ProbeInformation("target", "type", "class", "name", "description", "probe", 1, "url");
        assertEquals(1, probeInfo.getPrecision());
    }
    
    @Test
    public void testGetMeasureURI() {
        ProbeInformation probeInfo = new ProbeInformation("target", "type", "class", "name", "description", "probe", 1, "url");
        assertEquals("measureURI", probeInfo.getMeasureURI());
    }
    
    @Test
    public void testGetXmlRpcUrl() {
        ProbeInformation probeInfo = new ProbeInformation("target", "type", "class", "name", "description", "probe", 1, "url");
        assertEquals("url", probeInfo.getXmlRpcUrl());
    }
    
    @Test
    public void testEquals() {
        ProbeInformation probeInfo1 = new ProbeInformation("target", "type", "class", "name", "description", "probe", 1, "url");
        ProbeInformation probeInfo2 = new ProbeInformation("target", "type", "class", "name", "description", "probe", 1, "url");
        assertTrue(probeInfo1.equals(probeInfo2));
    }
    
    @Test
    public void testNotEquals() {
        ProbeInformation probeInfo1 = new ProbeInformation("target", "type", "class", "name", "description", "probe", 1, "url");
        ProbeInformation probeInfo2 = new ProbeInformation("target", "type", "class", "name2", "description", "probe", 1, "url");
        assertFalse(probeInfo1.equals(probeInfo2));
    }
    
    @Test
    public void testHashCode() {
        ProbeInformation probeInfo1 = new ProbeInformation("target", "type", "class", "name", "description", "probe", 1, "url");
        ProbeInformation probeInfo2 = new ProbeInformation("target", "type", "class", "name", "description", "probe", 1, "url");
        assertEquals(probeInfo1.hashCode(), probeInfo2.hashCode());
    }
    
    @Test
    public void testToString() {
        ProbeInformation probeInfo = new ProbeInformation("target", "type", "class", "name", "description", "probe", 1, "url");
        assertEquals("ProbeInformation{targetName='target', targetType='type', bmClass='class', bmName='name', bmDescription='description', probeDescription='probe', precision=1}", probeInfo.toString());
    }
}