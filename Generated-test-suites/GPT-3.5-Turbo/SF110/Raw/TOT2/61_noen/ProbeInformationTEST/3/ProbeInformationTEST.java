import org.junit.Assert;
import org.junit.Test;

public class ProbeInformationTest {

    private ProbeInformation probeInfo;

    @Before
    public void setUp() {
        probeInfo = new ProbeInformation("target", "type", "class", "name", "description", "probe", 10, "url");
    }

    @Test
    public void testGetTargetType() {
        Assert.assertEquals("type", probeInfo.getTargetType());
    }

    @Test
    public void testGetBmName() {
        Assert.assertEquals("name", probeInfo.getBmName());
    }

    @Test
    public void testGetProbeName() {
        Assert.assertEquals("probe", probeInfo.getProbeName());
    }

    // Add more basic test cases for other methods

}