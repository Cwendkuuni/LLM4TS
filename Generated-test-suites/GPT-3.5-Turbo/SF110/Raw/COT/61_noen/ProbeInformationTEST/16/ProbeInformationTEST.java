
import org.junit.Assert;
import org.junit.Test;

public class ProbeInformationTest {

    @Test
    public void testGetTargetType() {
        ProbeInformation probeInfo = new ProbeInformation("targetName", "targetType", "bmClass", "bmName", "bmDescription", "probeDescription", 5, "xmlRpcUrl");
        String targetType = probeInfo.getTargetType();
        Assert.assertEquals("targetType", targetType);
    }
    
    // Additional test cases ...

    @Test
    public void testToString() {
        ProbeInformation probeInfo = new ProbeInformation("targetName", "targetType", "bmClass", "bmName", "bmDescription", "probeDescription", 5, "xmlRpcUrl");
        String toString = probeInfo.toString();
        Assert.assertEquals("expectedToString", toString);
    }
  
}