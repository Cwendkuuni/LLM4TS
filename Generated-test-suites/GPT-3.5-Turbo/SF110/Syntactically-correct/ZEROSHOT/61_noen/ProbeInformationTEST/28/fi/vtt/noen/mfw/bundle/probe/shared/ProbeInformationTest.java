import org.junit.Test;
import static org.junit.Assert.*;

public class ProbeInformationTest {

    @Test
    public void testGetTargetType() {
        ProbeInformation probeInfo = new ProbeInformation("", "type", "", "", "", "", 0, "");
        assertEquals("type", probeInfo.getTargetType());
    }
    
    @Test
    public void testGetBmName() {
        ProbeInformation probeInfo = new ProbeInformation("", "", "", "name", "", "", 0, "");
        assertEquals("name", probeInfo.getBmName());
    }
    
    @Test
    public void testGetProbeName() {
        ProbeInformation probeInfo = new ProbeInformation("", "", "", "", "", "probe", 0, "");
        assertEquals("probe", probeInfo.getProbeName());
    }
    
    @Test
    public void testGetTargetName() {
        ProbeInformation probeInfo = new ProbeInformation("target", "", "", "", "", "", 0, "");
        assertEquals("target", probeInfo.getTargetName());
    }
    
    @Test
    public void testGetBmClass() {
        ProbeInformation probeInfo = new ProbeInformation("", "", "class", "", "", "", 0, "");
        assertEquals("class", probeInfo.getBmClass());
    }
    
    @Test
    public void testGetBmDescription() {
        ProbeInformation probeInfo = new ProbeInformation("", "", "", "", "description", "", 0, "");
        assertEquals("description", probeInfo.getBmDescription());
    }
    
    @Test
    public void testGetPrecision() {
        ProbeInformation probeInfo = new ProbeInformation("", "", "", "", "", "", 10, "");
        assertEquals(10, probeInfo.getPrecision());
    }
    
    @Test
    public void testGetMeasureURI() {
        ProbeInformation probeInfo = new ProbeInformation("target", "type", "class", "name", "", "", 0, "");
        assertEquals("measureURI", probeInfo.getMeasureURI()); // Replace "measureURI" with expected value
    }
    
    @Test
    public void testGetXmlRpcUrl() {
        ProbeInformation probeInfo = new ProbeInformation("", "", "", "", "", "", 0, "url");
        assertEquals("url", probeInfo.getXmlRpcUrl());
    }
    
    @Test
    public void testToString() {
        ProbeInformation probeInfo = new ProbeInformation("target", "type", "class", "name", "", "", 0, "");
        String expected = "ProbeInformation{targetName='target', targetType='type', bmClass='class', bmName='name', bmDescription='', probeDescription='', precision=0}";
        assertEquals(expected, probeInfo.toString());
    }
    
    @Test
    public void testEquals() {
        ProbeInformation probeInfo1 = new ProbeInformation("target", "type", "class", "name", "", "", 0, "");
        ProbeInformation probeInfo2 = new ProbeInformation("target", "type", "class", "name", "", "", 0, "");
        assertTrue(probeInfo1.equals(probeInfo2));
    }
    
    @Test
    public void testHashCode() {
        ProbeInformation probeInfo = new ProbeInformation("target", "type", "class", "name", "", "", 0, "");
        int expected = probeInfo.getTargetName().hashCode();
        expected = 31 * expected + probeInfo.getTargetType().hashCode();
        expected = 31 * expected + probeInfo.getBmClass().hashCode();
        expected = 31 * expected + probeInfo.getBmName().hashCode();
        expected = 31 * expected + probeInfo.getBmDescription().hashCode();
        expected = 31 * expected + probeInfo.getProbeName().hashCode();
        expected = 31 * expected + probeInfo.getPrecision();
        assertEquals(expected, probeInfo.hashCode());
    }
}