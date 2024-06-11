
import org.junit.Assert;
import org.junit.Test;

public class ProbeInformationTest {

    @Test
    public void testGetTargetType(){
        ProbeInformation probeInformation = new ProbeInformation("targetName", "targetType", "bmClass", "bmName", "bmDescription", "probeDescription", 1, "xmlRpcUrl");
        String expected = "targetType";
        String actual = probeInformation.getTargetType();
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testGetBmName(){
        ProbeInformation probeInformation = new ProbeInformation("targetName", "targetType", "bmClass", "bmName", "bmDescription", "probeDescription", 1, "xmlRpcUrl");
        String expected = "bmName";
        String actual = probeInformation.getBmName();
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testGetProbeName(){
        ProbeInformation probeInformation = new ProbeInformation("targetName", "targetType", "bmClass", "bmName", "bmDescription", "probeDescription", 1, "xmlRpcUrl");
        String expected = "probeDescription";
        String actual = probeInformation.getProbeName();
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testGetTargetName(){
        ProbeInformation probeInformation = new ProbeInformation("targetName", "targetType", "bmClass", "bmName", "bmDescription", "probeDescription", 1, "xmlRpcUrl");
        String expected = "targetName";
        String actual = probeInformation.getTargetName();
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testGetBmClass(){
        ProbeInformation probeInformation = new ProbeInformation("targetName", "targetType", "bmClass", "bmName", "bmDescription", "probeDescription", 1, "xmlRpcUrl");
        String expected = "bmClass";
        String actual = probeInformation.getBmClass();
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testGetBmDescription(){
        ProbeInformation probeInformation = new ProbeInformation("targetName", "targetType", "bmClass", "bmName", "bmDescription", "probeDescription", 1, "xmlRpcUrl");
        String expected = "bmDescription";
        String actual = probeInformation.getBmDescription();
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testGetPrecision(){
        ProbeInformation probeInformation = new ProbeInformation("targetName", "targetType", "bmClass", "bmName", "bmDescription", "probeDescription", 1, "xmlRpcUrl");
        int expected = 1;
        int actual = probeInformation.getPrecision();
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testGetMeasureURI(){
        ProbeInformation probeInformation = new ProbeInformation("targetName", "targetType", "bmClass", "bmName", "bmDescription", "probeDescription", 1, "xmlRpcUrl");
        String expected = "measureURI";
        String actual = probeInformation.getMeasureURI();
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testGetXmlRpcUrl(){
        ProbeInformation probeInformation = new ProbeInformation("targetName", "targetType", "bmClass", "bmName", "bmDescription", "probeDescription", 1, "xmlRpcUrl");
        String expected = "xmlRpcUrl";
        String actual = probeInformation.getXmlRpcUrl();
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testToString(){
        ProbeInformation probeInformation = new ProbeInformation("targetName", "targetType", "bmClass", "bmName", "bmDescription", "probeDescription", 1, "xmlRpcUrl");
        String expected = "ProbeInformation{targetName='targetName', targetType='targetType', bmClass='bmClass', bmName='bmName', bmDescription='bmDescription', probeDescription='probeDescription', precision=1}";
        String actual = probeInformation.toString();
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testEquals(){
        ProbeInformation probeInformation1 = new ProbeInformation("targetName", "targetType", "bmClass", "bmName", "bmDescription", "probeDescription", 1, "xmlRpcUrl");
        ProbeInformation probeInformation2 = new ProbeInformation("targetName", "targetType", "bmClass", "bmName", "bmDescription", "probeDescription", 1, "xmlRpcUrl");
        Assert.assertEquals(probeInformation1, probeInformation2);
    }

    @Test
    public void testEquals_NullInput(){
        ProbeInformation probeInformation = new ProbeInformation("targetName", "targetType", "bmClass", "bmName", "bmDescription", "probeDescription", 1, "xmlRpcUrl");
        Assert.assertNotEquals(probeInformation, null);
    }

    @Test
    public void testEquals_DifferentClass(){
        ProbeInformation probeInformation = new ProbeInformation("targetName", "targetType", "bmClass", "bmName", "bmDescription", "probeDescription", 1, "xmlRpcUrl");
        Integer integer = new Integer(1);
        Assert.assertNotEquals(probeInformation, integer);
    }

    @Test
    public void testEquals_DifferentPrecision(){
        ProbeInformation probeInformation1 = new ProbeInformation("targetName", "targetType", "bmClass", "bmName", "bmDescription", "probeDescription", 1, "xmlRpcUrl");
        ProbeInformation probeInformation2 = new ProbeInformation("targetName", "targetType", "bmClass", "bmName", "bmDescription", "probeDescription", 2, "xmlRpcUrl");
        Assert.assertNotEquals(probeInformation1, probeInformation2);
    }

    @Test
    public void testEquals_DifferentBmClass(){
        ProbeInformation probeInformation1 = new ProbeInformation("targetName", "targetType", "bmClass1", "bmName", "bmDescription", "probeDescription", 1, "xmlRpcUrl");
        ProbeInformation probeInformation2 = new ProbeInformation("targetName", "targetType", "bmClass2", "bmName", "bmDescription", "probeDescription", 1, "xmlRpcUrl");
        Assert.assertNotEquals(probeInformation1, probeInformation2);
    }

    // continue with other test cases for remaining methods

}
