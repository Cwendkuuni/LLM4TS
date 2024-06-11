package fi.vtt.noen.mfw.bundle.probe.shared;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class ProbeInformationTest {

    @Test
    public void testGetTargetType() {
        ProbeInformation probeInfo = new ProbeInformation("target1", "type1", "class1", "name1", "description1", "probe1", 2, "url1");
        assertEquals("type1", probeInfo.getTargetType());
    }
    
    @Test
    public void testGetBmName() {
        ProbeInformation probeInfo = new ProbeInformation("target1", "type1", "class1", "name1", "description1", "probe1", 2, "url1");
        assertEquals("name1", probeInfo.getBmName());
    }
    
    @Test
    public void testGetProbeName() {
        ProbeInformation probeInfo = new ProbeInformation("target1", "type1", "class1", "name1", "description1", "probe1", 2, "url1");
        assertEquals("probe1", probeInfo.getProbeName());
    }
    
    @Test
    public void testGetTargetName() {
        ProbeInformation probeInfo = new ProbeInformation("target1", "type1", "class1", "name1", "description1", "probe1", 2, "url1");
        assertEquals("target1", probeInfo.getTargetName());
    }
    
    @Test
    public void testGetBmClass() {
        ProbeInformation probeInfo = new ProbeInformation("target1", "type1", "class1", "name1", "description1", "probe1", 2, "url1");
        assertEquals("class1", probeInfo.getBmClass());
    }
    
    @Test
    public void testGetBmDescription() {
        ProbeInformation probeInfo = new ProbeInformation("target1", "type1", "class1", "name1", "description1", "probe1", 2, "url1");
        assertEquals("description1", probeInfo.getBmDescription());
    }
    
    @Test
    public void testGetPrecision() {
        ProbeInformation probeInfo = new ProbeInformation("target1", "type1", "class1", "name1", "description1", "probe1", 2, "url1");
        assertEquals(2, probeInfo.getPrecision());
    }
    
    @Test
    public void testGetMeasureURI() {
        ProbeInformation probeInfo = new ProbeInformation("target1", "type1", "class1", "name1", "description1", "probe1", 2, "url1");
        assertEquals("measureURI", probeInfo.getMeasureURI());
    }
    
    @Test
    public void testGetXmlRpcUrl() {
        ProbeInformation probeInfo = new ProbeInformation("target1", "type1", "class1", "name1", "description1", "probe1", 2, "url1");
        assertEquals("url1", probeInfo.getXmlRpcUrl());
    }
    
    @Test
    public void testToString() {
        ProbeInformation probeInfo = new ProbeInformation("target1", "type1", "class1", "name1", "description1", "probe1", 2, "url1");
        assertEquals("ProbeInformation{targetName='target1', targetType='type1', bmClass='class1', bmName='name1', bmDescription='description1', probeDescription='probe1', precision=2}", probeInfo.toString());
    }
    
    @Test
    public void testEquals() {
        ProbeInformation probeInfo1 = new ProbeInformation("target1", "type1", "class1", "name1", "description1", "probe1", 2, "url1");
        ProbeInformation probeInfo2 = new ProbeInformation("target1", "type1", "class1", "name1", "description1", "probe1", 2, "url1");
        ProbeInformation probeInfo3 = new ProbeInformation("target2", "type2", "class2", "name2", "description2", "probe2", 3, "url2");
        
        assertEquals(true, probeInfo1.equals(probeInfo2));
        assertEquals(false, probeInfo1.equals(probeInfo3));
    }
    
    @Test
    public void testHashCode() {
        ProbeInformation probeInfo1 = new ProbeInformation("target1", "type1", "class1", "name1", "description1", "probe1", 2, "url1");
        ProbeInformation probeInfo2 = new ProbeInformation("target1", "type1", "class1", "name1", "description1", "probe1", 2, "url1");
        ProbeInformation probeInfo3 = new ProbeInformation("target2", "type2", "class2", "name2", "description2", "probe2", 3, "url2");
        
        assertEquals(true, probeInfo1.hashCode() == probeInfo2.hashCode());
        assertEquals(false, probeInfo1.hashCode() == probeInfo3.hashCode());
    }
}