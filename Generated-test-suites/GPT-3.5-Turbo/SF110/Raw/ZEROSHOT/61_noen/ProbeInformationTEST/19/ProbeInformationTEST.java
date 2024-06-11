import org.junit.Test;
import static org.junit.Assert.*;

public class ProbeInformationTest {

    ProbeInformation probeInfo;
    
    @Test
    public void testGetTargetType() {
        probeInfo = new ProbeInformation("target", "type", "class", "name", "desc", "probe", 1, "url");
        String result = probeInfo.getTargetType();
        assertEquals("type", result);
    }
    
    @Test
    public void testGetBmName() {
        probeInfo = new ProbeInformation("target", "type", "class", "name", "desc", "probe", 1, "url");
        String result = probeInfo.getBmName();
        assertEquals("name", result);
    }
    
    @Test
    public void testGetProbeName() {
        probeInfo = new ProbeInformation("target", "type", "class", "name", "desc", "probe", 1, "url");
        String result = probeInfo.getProbeName();
        assertEquals("probe", result);
    }
    
    @Test
    public void testGetTargetName() {
        probeInfo = new ProbeInformation("target", "type", "class", "name", "desc", "probe", 1, "url");
        String result = probeInfo.getTargetName();
        assertEquals("target", result);
    }
    
    @Test
    public void testGetBmClass() {
        probeInfo = new ProbeInformation("target", "type", "class", "name", "desc", "probe", 1, "url");
        String result = probeInfo.getBmClass();
        assertEquals("class", result);
    }
    
    @Test
    public void testGetBmDescription() {
        probeInfo = new ProbeInformation("target", "type", "class", "name", "desc", "probe", 1, "url");
        String result = probeInfo.getBmDescription();
        assertEquals("desc", result);
    }
    
    @Test
    public void testGetPrecision() {
        probeInfo = new ProbeInformation("target", "type", "class", "name", "desc", "probe", 1, "url");
        int result = probeInfo.getPrecision();
        assertEquals(1, result);
    }
    
    @Test
    public void testGetMeasureURI() {
        probeInfo = new ProbeInformation("target", "type", "class", "name", "desc", "probe", 1, "url");
        String result = probeInfo.getMeasureURI();
        assertEquals("target-type-class-name", result);
    }
    
    @Test
    public void testGetXmlRpcUrl() {
        probeInfo = new ProbeInformation("target", "type", "class", "name", "desc", "probe", 1, "url");
        String result = probeInfo.getXmlRpcUrl();
        assertEquals("url", result);
    }
    
    @Test
    public void testToString() {
        probeInfo = new ProbeInformation("target", "type", "class", "name", "desc", "probe", 1, "url");
        String result = probeInfo.toString();
        assertEquals("ProbeInformation{targetName='target', targetType='type', bmClass='class', bmName='name', bmDescription='desc', probeDescription='probe', precision=1}", result);
    }
    
    @Test
    public void testEquals() {
        probeInfo = new ProbeInformation("target", "type", "class", "name", "desc", "probe", 1, "url");
        
        ProbeInformation same = new ProbeInformation("target", "type", "class", "name", "desc", "probe", 1, "url");
        boolean result1 = probeInfo.equals(same);
        assertTrue(result1);
        
        ProbeInformation different1 = new ProbeInformation("target1", "type", "class", "name", "desc", "probe", 1, "url");
        boolean result2 = probeInfo.equals(different1);
        assertFalse(result2);
        
        ProbeInformation different2 = new ProbeInformation("target", "type1", "class", "name", "desc", "probe", 1, "url");
        boolean result3 = probeInfo.equals(different2);
        assertFalse(result3);
        
        ProbeInformation different3 = new ProbeInformation("target", "type", "class1", "name", "desc", "probe", 1, "url");
        boolean result4 = probeInfo.equals(different3);
        assertFalse(result4);
        
        ProbeInformation different4 = new ProbeInformation("target", "type", "class", "name1", "desc", "probe", 1, "url");
        boolean result5 = probeInfo.equals(different4);
        assertFalse(result5);
        
        ProbeInformation different5 = new ProbeInformation("target", "type", "class", "name", "desc1", "probe", 1, "url");
        boolean result6 = probeInfo.equals(different5);
        assertFalse(result6);
        
        ProbeInformation different6 = new ProbeInformation("target", "type", "class", "name", "desc", "probe1", 1, "url");
        boolean result7 = probeInfo.equals(different6);
        assertFalse(result7);
        
        ProbeInformation different7 = new ProbeInformation("target", "type", "class", "name", "desc", "probe", 2, "url");
        boolean result8 = probeInfo.equals(different7);
        assertFalse(result8);
        
        ProbeInformation different8 = new ProbeInformation("target", "type", "class", "name", "desc", "probe", 1, "url1");
        boolean result9 = probeInfo.equals(different8);
        assertFalse(result9);
    }
    
    @Test
    public void testHashCode() {
        probeInfo = new ProbeInformation("target", "type", "class", "name", "desc", "probe", 1, "url");
        int result = probeInfo.hashCode();
        assertEquals(1222477924, result);
    }
}