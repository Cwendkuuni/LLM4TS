import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class ProbeInformationTest {

    @Test
    public void testGetTargetType() {
        ProbeInformation probeInformation = new ProbeInformation("TestTarget", "Type", "Class", "Name", "Description", "ProbeDescription", 5, "http://example.com");
        assertEquals("Type", probeInformation.getTargetType());
    }

    @Test
    public void testGetBmName() {
        ProbeInformation probeInformation = new ProbeInformation("TestTarget", "Type", "Class", "Name", "Description", "ProbeDescription", 5, "http://example.com");
        assertEquals("Name", probeInformation.getBmName());
    }

    @Test
    public void testGetProbeName() {
        ProbeInformation probeInformation = new ProbeInformation("TestTarget", "Type", "Class", "Name", "Description", "ProbeDescription", 5, "http://example.com");
        assertEquals("ProbeDescription", probeInformation.getProbeName());
    }

    @Test
    public void testGetTargetName() {
        ProbeInformation probeInformation = new ProbeInformation("TestTarget", "Type", "Class", "Name", "Description", "ProbeDescription", 5, "http://example.com");
        assertEquals("TestTarget", probeInformation.getTargetName());
    }

    @Test
    public void testGetBmClass() {
        ProbeInformation probeInformation = new ProbeInformation("TestTarget", "Type", "Class", "Name", "Description", "ProbeDescription", 5, "http://example.com");
        assertEquals("Class", probeInformation.getBmClass());
    }

    @Test
    public void testGetBmDescription() {
        ProbeInformation probeInformation = new ProbeInformation("TestTarget", "Type", "Class", "Name", "Description", "ProbeDescription", 5, "http://example.com");
        assertEquals("Description", probeInformation.getBmDescription());
    }

    @Test
    public void testGetPrecision() {
        ProbeInformation probeInformation = new ProbeInformation("TestTarget", "Type", "Class", "Name", "Description", "ProbeDescription", 5, "http://example.com");
        assertEquals(5, probeInformation.getPrecision());
    }

    @Test
    public void testGetMeasureURI() {
        ProbeInformation probeInformation = new ProbeInformation("TestTarget", "Type", "Class", "Name", "Description", "ProbeDescription", 5, "http://example.com");
        assertEquals("Type/TestTarget/Class/Name", probeInformation.getMeasureURI());
    }

    @Test
    public void testGetXmlRpcUrl() {
        ProbeInformation probeInformation = new ProbeInformation("TestTarget", "Type", "Class", "Name", "Description", "ProbeDescription", 5, "http://example.com");
        assertEquals("http://example.com", probeInformation.getXmlRpcUrl());
    }
    
    // Additional edge cases and exception handling scenarios can be tested here

}