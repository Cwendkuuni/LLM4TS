import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class ProbeInformationTest {

    @Test
    public void getTargetTypeTest() {
        ProbeInformation probe = new ProbeInformation("", "type", "", "", "", "", 0, "");
        assertEquals("type", probe.getTargetType());
    }

    @Test
    public void getBmNameTest() {
        ProbeInformation probe = new ProbeInformation("", "", "", "name", "", "", 0, "");
        assertEquals("name", probe.getBmName());
    }

    @Test
    public void getProbeNameTest() {
        ProbeInformation probe = new ProbeInformation("", "", "", "", "", "probe", 0, "");
        assertEquals("probe", probe.getProbeName());
    }

    @Test
    public void getTargetNameTest() {
        ProbeInformation probe = new ProbeInformation("target", "", "", "", "", "", 0, "");
        assertEquals("target", probe.getTargetName());
    }

    @Test
    public void getBmClassTest() {
        ProbeInformation probe = new ProbeInformation("", "", "class", "", "", "", 0, "");
        assertEquals("class", probe.getBmClass());
    }

    @Test
    public void getBmDescriptionTest() {
        ProbeInformation probe = new ProbeInformation("", "", "", "", "description", "", 0, "");
        assertEquals("description", probe.getBmDescription());
    }

    @Test
    public void getPrecisionTest() {
        ProbeInformation probe = new ProbeInformation("", "", "", "", "", "", 10, "");
        assertEquals(10, probe.getPrecision());
    }

    @Test
    public void getMeasureURITest() {
        ProbeInformation probe = new ProbeInformation("target", "type", "class", "name", "", "", 0, "");
        assertEquals("uri", probe.getMeasureURI());
    }

    @Test
    public void getXmlRpcUrlTest() {
        ProbeInformation probe = new ProbeInformation("", "", "", "", "", "", 0, "url");
        assertEquals("url", probe.getXmlRpcUrl());
    }

    @Test
    public void toStringTest() {
        ProbeInformation probe = new ProbeInformation("target", "type", "class", "name", "", "probe", 10, "");
        assertEquals("ProbeInformation{targetName='target', targetType='type', bmClass='class', bmName='name', bmDescription='', probeDescription='probe', precision=10}", probe.toString());
    }

    @Test
    public void equalsTest() {
        ProbeInformation probe1 = new ProbeInformation("target", "type", "class", "name", "", "probe", 10, "");
        ProbeInformation probe2 = new ProbeInformation("target", "type", "class", "name", "", "probe", 10, "");
        assertEquals(true, probe1.equals(probe2));
    }

    @Test
    public void hashCodeTest() {
        ProbeInformation probe = new ProbeInformation("target", "type", "class", "name", "", "probe", 10, "");
        assertEquals(2084231541, probe.hashCode());
    }
}