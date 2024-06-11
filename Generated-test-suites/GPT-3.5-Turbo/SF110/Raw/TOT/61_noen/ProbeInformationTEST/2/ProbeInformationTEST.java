
import org.junit.Assert;
import org.junit.Test;

public class ProbeInformationTest {
    
    private ProbeInformation probeInformation;
    
    // Test cases for the constructor
    
    @Test
    public void testConstructorCreatesObject() {
        probeInformation = new ProbeInformation("targetName", "targetType", "bmClass", "bmName", "bmDescription", "probeName", 10, "xmlRpcUrl");
        
        Assert.assertNotNull(probeInformation);
    }
    
    @Test
    public void testConstructorInitializesFieldsCorrectly() {
        probeInformation = new ProbeInformation("targetName", "targetType", "bmClass", "bmName", "bmDescription", "probeName", 10, "xmlRpcUrl");
        
        Assert.assertEquals("targetType", probeInformation.getTargetType());
        Assert.assertEquals("bmName", probeInformation.getBmName());
        Assert.assertEquals("probeName", probeInformation.getProbeName());
        Assert.assertEquals("targetName", probeInformation.getTargetName());
        Assert.assertEquals("bmClass", probeInformation.getBmClass());
        Assert.assertEquals("bmDescription", probeInformation.getBmDescription());
        Assert.assertEquals(10, probeInformation.getPrecision());
        Assert.assertEquals("targetType_targetName_bmClass_bmName", probeInformation.getMeasureURI());
        Assert.assertEquals("xmlRpcUrl", probeInformation.getXmlRpcUrl());
        Assert.assertEquals("ProbeInformation{targetName='targetName', targetType='targetType', bmClass='bmClass', bmName='bmName', bmDescription='bmDescription', probeDescription='probeName', precision=10}", probeInformation.toString());
    }
    
    // Test cases for equals method
    
    @Test
    public void testEqualsReturnsTrueForSameObject() {
        ProbeInformation info1 = new ProbeInformation("targetName", "targetType", "bmClass", "bmName", "bmDescription", "probeName", 10, "xmlRpcUrl");
        
        Assert.assertTrue(info1.equals(info1));
    }
    
    @Test
    public void testEqualsReturnsFalseForNullObject() {
        ProbeInformation info1 = new ProbeInformation("targetName", "targetType", "bmClass", "bmName", "bmDescription", "probeName", 10, "xmlRpcUrl");
        
        Assert.assertFalse(info1.equals(null));
    }
    
    @Test
    public void testEqualsReturnsFalseForDifferentClasses() {
        ProbeInformation info1 = new ProbeInformation("targetName", "targetType", "bmClass", "bmName", "bmDescription", "probeName", 10, "xmlRpcUrl");
        String string = "ProbeInformation";
        
        Assert.assertFalse(info1.equals(string));
    }
    
    @Test
    public void testEqualsReturnsFalseForDifferentPrecision() {
        ProbeInformation info1 = new ProbeInformation("targetName", "targetType", "bmClass", "bmName", "bmDescription", "probeName", 10, "xmlRpcUrl");
        ProbeInformation info2 = new ProbeInformation("targetName", "targetType", "bmClass", "bmName", "bmDescription", "probeName", 20, "xmlRpcUrl");
        
        Assert.assertFalse(info1.equals(info2));
    }
    
    @Test
    public void testEqualsReturnsFalseForDifferentBmClass() {
        ProbeInformation info1 = new ProbeInformation("targetName", "targetType", "bmClass1", "bmName", "bmDescription", "probeName", 10, "xmlRpcUrl");
        ProbeInformation info2 = new ProbeInformation("targetName", "targetType", "bmClass2", "bmName", "bmDescription", "probeName", 10, "xmlRpcUrl");
        
        Assert.assertFalse(info1.equals(info2));
    }
    
    @Test
    public void testEqualsReturnsFalseForDifferentBmName() {
        ProbeInformation info1 = new ProbeInformation("targetName", "targetType", "bmClass", "bmName1", "bmDescription", "probeName", 10, "xmlRpcUrl");
        ProbeInformation info2 = new ProbeInformation("targetName", "targetType", "bmClass", "bmName2", "bmDescription", "probeName", 10, "xmlRpcUrl");
        
        Assert.assertFalse(info1.equals(info2));
    }
    
    @Test
    public void testEqualsReturnsFalseForDifferentBmDescription() {
        ProbeInformation info1 = new ProbeInformation("targetName", "targetType", "bmClass", "bmName", "bmDescription1", "probeName", 10, "xmlRpcUrl");
        ProbeInformation info2 = new ProbeInformation("targetName", "targetType", "bmClass", "bmName", "bmDescription2", "probeName", 10, "xmlRpcUrl");
        
        Assert.assertFalse(info1.equals(info2));
    }
    
    @Test
    public void testEqualsReturnsFalseForDifferentProbeName() {
        ProbeInformation info1 = new ProbeInformation("targetName", "targetType", "bmClass", "bmName", "bmDescription", "probeName1", 10, "xmlRpcUrl");
        ProbeInformation info2 = new ProbeInformation("targetName", "targetType", "bmClass", "bmName", "bmDescription", "probeName2", 10, "xmlRpcUrl");
        
        Assert.assertFalse(info1.equals(info2));
    }
    
    @Test
    public void testEqualsReturnsFalseForDifferentTargetName() {
        ProbeInformation info1 = new ProbeInformation("targetName1", "targetType", "bmClass", "bmName", "bmDescription", "probeName", 10, "xmlRpcUrl");
        ProbeInformation info2 = new ProbeInformation("targetName2", "targetType", "bmClass", "bmName", "bmDescription", "probeName", 10, "xmlRpcUrl");
        
        Assert.assertFalse(info1.equals(info2));
    }
    
    @Test
    public void testEqualsReturnsFalseForDifferentTargetType() {
        ProbeInformation info1 = new ProbeInformation("targetName", "targetType1", "bmClass", "bmName", "bmDescription", "probeName", 10, "xmlRpcUrl");
        ProbeInformation info2 = new ProbeInformation("targetName", "targetType2", "bmClass", "bmName", "bmDescription", "probeName", 10, "xmlRpcUrl");
        
        Assert.assertFalse(info1.equals(info2));
    }
    
    @Test
    public void testEqualsReturnsTrueForSameTargetType() {
        ProbeInformation info1 = new ProbeInformation("targetName", "targetType", "bmClass", "bmName", "bmDescription", "probeName", 10, "xmlRpcUrl");
        ProbeInformation info2 = new ProbeInformation("targetName", "targetType", "bmClass", "bmName", "bmDescription", "probeName", 10, "xmlRpcUrl");
        
        Assert.assertTrue(info1.equals(info2));
    }
    
    // Test cases for hashCode method
    
    @Test
    public void testHashCodeIsSameForSameObjects() {
        ProbeInformation info1 = new ProbeInformation("targetName", "targetType", "bmClass", "bmName", "bmDescription", "probeName", 10, "xmlRpcUrl");
        ProbeInformation info2 = new ProbeInformation("targetName", "targetType", "bmClass", "bmName", "bmDescription", "probeName", 10, "xmlRpcUrl");
        
        Assert.assertEquals(info1.hashCode(), info2.hashCode());
    }
    
    @Test
    public void testHashCodeIsDifferentForDifferentPrecision() {
        ProbeInformation info1 = new ProbeInformation("targetName", "targetType", "bmClass", "bmName", "bmDescription", "probeName", 10, "xmlRpcUrl");
        ProbeInformation info2 = new ProbeInformation("targetName", "targetType", "bmClass", "bmName", "bmDescription", "probeName", 20, "xmlRpcUrl");
        
        Assert.assertNotEquals(info1.hashCode(), info2.hashCode());
    }
    
    @Test
    public void testHashCodeIsDifferentForDifferentBmClass() {
        ProbeInformation info1 = new ProbeInformation("targetName", "targetType", "bmClass1", "bmName", "bmDescription", "probeName", 10, "xmlRpcUrl");
        ProbeInformation info2 = new ProbeInformation("targetName", "targetType", "bmClass2", "bmName", "bmDescription", "probeName", 10, "xmlRpcUrl");
        
        Assert.assertNotEquals(info1.hashCode(), info2.hashCode());
    }
    
    @Test
    public void testHashCodeIsDifferentForDifferentBmName() {
        ProbeInformation info1 = new ProbeInformation("targetName", "targetType", "bmClass", "bmName1", "bmDescription", "probeName", 10, "xmlRpcUrl");
        ProbeInformation info2 = new ProbeInformation("targetName", "targetType", "bmClass", "bmName2", "bmDescription", "probeName", 10, "xmlRpcUrl");
        
        Assert.assertNotEquals(info1.hashCode(), info2.hashCode());
    }
    
    @Test
    public void testHashCodeIsDifferentForDifferentBmDescription() {
        ProbeInformation info1 = new ProbeInformation("targetName", "targetType", "bmClass", "bmName", "bmDescription1", "probeName", 10, "xmlRpcUrl");
        ProbeInformation info2 = new ProbeInformation("targetName", "targetType", "bmClass", "bmName", "bmDescription2", "probeName", 10, "xmlRpcUrl");
        
        Assert.assertNotEquals(info1.hashCode(), info2.hashCode());
    }
    
    @Test
    public void testHashCodeIsDifferentForDifferentProbeName() {
        ProbeInformation info1 = new ProbeInformation("targetName", "targetType", "bmClass", "bmName", "bmDescription", "probeName1", 10, "xmlRpcUrl");
        ProbeInformation info2 = new ProbeInformation("targetName", "targetType", "bmClass", "bmName", "bmDescription", "probeName2", 10, "xmlRpcUrl");
        
        Assert.assertNotEquals(info1.hashCode(), info2.hashCode());
    }
    
    @Test
    public void testHashCodeIsDifferentForDifferentTargetName() {
        ProbeInformation info1 = new ProbeInformation("targetName1", "targetType", "bmClass", "bmName", "bmDescription", "probeName", 10, "xmlRpcUrl");
        ProbeInformation info2 = new ProbeInformation("targetName2", "targetType", "bmClass", "bmName", "bmDescription", "probeName", 10, "xmlRpcUrl");
        
        Assert.assertNotEquals(info1.hashCode(), info2.hashCode());
    }
    
    @Test
    public void testHashCodeIsDifferentForDifferentTargetType() {
        ProbeInformation info1 = new ProbeInformation("targetName", "targetType1", "bmClass", "bmName", "bmDescription", "probeName", 10, "xmlRpcUrl");
        ProbeInformation info2 = new ProbeInformation("targetName", "targetType2", "bmClass", "bmName", "bmDescription", "probeName", 10, "xmlRpcUrl");
        
        Assert.assertNotEquals(info1.hashCode(), info2.hashCode());
    }
}

