import org.junit.Assert;
import org.junit.Test;

public class ProbeInformationTest {

    @Test
    public void testGetTargetType() {
        ProbeInformation probeInformation = new ProbeInformation("target", "type", "class", "name", "description", "probe", 1, "url");
        Assert.assertEquals("type", probeInformation.getTargetType());
    }

    @Test
    public void testGetBmName() {
        ProbeInformation probeInformation = new ProbeInformation("target", "type", "class", "name", "description", "probe", 1, "url");
        Assert.assertEquals("name", probeInformation.getBmName());
    }

    @Test
    public void testGetProbeName() {
        ProbeInformation probeInformation = new ProbeInformation("target", "type", "class", "name", "description", "probe", 1, "url");
        Assert.assertEquals("probe", probeInformation.getProbeName());
    }

    @Test
    public void testGetTargetName() {
        ProbeInformation probeInformation = new ProbeInformation("target", "type", "class", "name", "description", "probe", 1, "url");
        Assert.assertEquals("target", probeInformation.getTargetName());
    }

    @Test
    public void testGetBmClass() {
        ProbeInformation probeInformation = new ProbeInformation("target", "type", "class", "name", "description", "probe", 1, "url");
        Assert.assertEquals("class", probeInformation.getBmClass());
    }

    @Test
    public void testGetBmDescription() {
        ProbeInformation probeInformation = new ProbeInformation("target", "type", "class", "name", "description", "probe", 1, "url");
        Assert.assertEquals("description", probeInformation.getBmDescription());
    }

    @Test
    public void testGetPrecision() {
        ProbeInformation probeInformation = new ProbeInformation("target", "type", "class", "name", "description", "probe", 1, "url");
        Assert.assertEquals(1, probeInformation.getPrecision());
    }

    @Test
    public void testGetMeasureURI() {
        ProbeInformation probeInformation = new ProbeInformation("target", "type", "class", "name", "description", "probe", 1, "url");
        Assert.assertEquals("expectedURI", probeInformation.getMeasureURI());
    }

    @Test
    public void testGetXmlRpcUrl() {
        ProbeInformation probeInformation = new ProbeInformation("target", "type", "class", "name", "description", "probe", 1, "url");
        Assert.assertEquals("url", probeInformation.getXmlRpcUrl());
    }

    @Test
    public void testToString() {
        ProbeInformation probeInformation = new ProbeInformation("target", "type", "class", "name", "description", "probe", 1, "url");
        Assert.assertEquals("expectedToString", probeInformation.toString());
    }

    @Test
    public void testEquals() {
        ProbeInformation probeInformation1 = new ProbeInformation("target", "type", "class", "name", "description", "probe", 1, "url");
        ProbeInformation probeInformation2 = new ProbeInformation("target", "type", "class", "name", "description", "probe", 1, "url");
        Assert.assertEquals(probeInformation1, probeInformation2);
    }

    @Test
    public void testHashCode() {
        ProbeInformation probeInformation = new ProbeInformation("target", "type", "class", "name", "description", "probe", 1, "url");
        Assert.assertEquals("expectedHashCode", probeInformation.hashCode());
    }
}

