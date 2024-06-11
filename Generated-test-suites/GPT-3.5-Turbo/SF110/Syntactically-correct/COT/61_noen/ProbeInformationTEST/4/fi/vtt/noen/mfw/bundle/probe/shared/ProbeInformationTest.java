
import org.junit.Assert;
import org.junit.Test;

public class ProbeInformationTest {

    @Test
    public void testGetTargetType() {
        ProbeInformation probeInfo = new ProbeInformation("targetName", "targetType", "bmClass",
                "bmName", "bmDescription", "probeDescription", 10, "xmlRpcUrl");
        Assert.assertEquals("targetType", probeInfo.getTargetType());
    }

    @Test
    public void testGetBmName() {
        ProbeInformation probeInfo = new ProbeInformation("targetName", "targetType", "bmClass",
                "bmName", "bmDescription", "probeDescription", 10, "xmlRpcUrl");
        Assert.assertEquals("bmName", probeInfo.getBmName());
    }

    @Test
    public void testGetProbeName() {
        ProbeInformation probeInfo = new ProbeInformation("targetName", "targetType", "bmClass",
                "bmName", "bmDescription", "probeDescription", 10, "xmlRpcUrl");
        Assert.assertEquals("probeDescription", probeInfo.getProbeName());
    }

    @Test
    public void testGetTargetName() {
        ProbeInformation probeInfo = new ProbeInformation("targetName", "targetType", "bmClass",
                "bmName", "bmDescription", "probeDescription", 10, "xmlRpcUrl");
        Assert.assertEquals("targetName", probeInfo.getTargetName());
    }

    @Test
    public void testGetBmClass() {
        ProbeInformation probeInfo = new ProbeInformation("targetName", "targetType", "bmClass",
                "bmName", "bmDescription", "probeDescription", 10, "xmlRpcUrl");
        Assert.assertEquals("bmClass", probeInfo.getBmClass());
    }

    @Test
    public void testGetBmDescription() {
        ProbeInformation probeInfo = new ProbeInformation("targetName", "targetType", "bmClass",
                "bmName", "bmDescription", "probeDescription", 10, "xmlRpcUrl");
        Assert.assertEquals("bmDescription", probeInfo.getBmDescription());
    }

    @Test
    public void testGetPrecision() {
        ProbeInformation probeInfo = new ProbeInformation("targetName", "targetType", "bmClass",
                "bmName", "bmDescription", "probeDescription", 10, "xmlRpcUrl");
        Assert.assertEquals(10, probeInfo.getPrecision());
    }

    @Test
    public void testGetMeasureURI() {
        ProbeInformation probeInfo = new ProbeInformation("targetName", "targetType", "bmClass",
                "bmName", "bmDescription", "probeDescription", 10, "xmlRpcUrl");
        Assert.assertEquals("targetType/targetName/bmClass/bmName", probeInfo.getMeasureURI());
    }

    @Test
    public void testGetXmlRpcUrl() {
        ProbeInformation probeInfo = new ProbeInformation("targetName", "targetType", "bmClass",
                "bmName", "bmDescription", "probeDescription", 10, "xmlRpcUrl");
        Assert.assertEquals("xmlRpcUrl", probeInfo.getXmlRpcUrl());
    }

    @Test
    public void testEquals() {
        ProbeInformation probeInfo1 = new ProbeInformation("targetName", "targetType", "bmClass",
                "bmName", "bmDescription", "probeDescription", 10, "xmlRpcUrl");
        ProbeInformation probeInfo2 = new ProbeInformation("targetName", "targetType", "bmClass",
                "bmName", "bmDescription", "probeDescription", 10, "xmlRpcUrl");
        ProbeInformation probeInfo3 = new ProbeInformation("differentName", "targetType", "bmClass",
                "bmName", "bmDescription", "probeDescription", 10, "xmlRpcUrl");

        Assert.assertEquals(probeInfo1, probeInfo2);
        Assert.assertNotEquals(probeInfo1, probeInfo3);
    }

    @Test
    public void testHashCode() {
        ProbeInformation probeInfo1 = new ProbeInformation("targetName", "targetType", "bmClass",
                "bmName", "bmDescription", "probeDescription", 10, "xmlRpcUrl");
        ProbeInformation probeInfo2 = new ProbeInformation("targetName", "targetType", "bmClass",
                "bmName", "bmDescription", "probeDescription", 10, "xmlRpcUrl");

        Assert.assertEquals(probeInfo1.hashCode(), probeInfo2.hashCode());
    }
}
