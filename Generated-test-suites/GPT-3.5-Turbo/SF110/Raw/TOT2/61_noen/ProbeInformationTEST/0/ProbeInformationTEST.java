import org.junit.Test;
import static org.junit.Assert.*;

public class ProbeInformationTest {

    @Test
    public void testGetTargetType() {
        ProbeInformation probeInfo = new ProbeInformation("TargetName", "TargetType", "BMClass", "BMName", "BMDescription", "ProbeName", 10, "http://xmlrpc.url");
        assertEquals("TargetType", probeInfo.getTargetType());
    }

    @Test
    public void testGetBmName() {
        ProbeInformation probeInfo = new ProbeInformation("TargetName", "TargetType", "BMClass", "BMName", "BMDescription", "ProbeName", 10, "http://xmlrpc.url");
        assertEquals("BMName", probeInfo.getBmName());
    }

    @Test
    public void testGetProbeName() {
        ProbeInformation probeInfo = new ProbeInformation("TargetName", "TargetType", "BMClass", "BMName", "BMDescription", "ProbeName", 10, "http://xmlrpc.url");
        assertEquals("ProbeName", probeInfo.getProbeName());
    }

    @Test
    public void testGetTargetName() {
        ProbeInformation probeInfo = new ProbeInformation("TargetName", "TargetType", "BMClass", "BMName", "BMDescription", "ProbeName", 10, "http://xmlrpc.url");
        assertEquals("TargetName", probeInfo.getTargetName());
    }

    @Test
    public void testGetBmClass() {
        ProbeInformation probeInfo = new ProbeInformation("TargetName", "TargetType", "BMClass", "BMName", "BMDescription", "ProbeName", 10, "http://xmlrpc.url");
        assertEquals("BMClass", probeInfo.getBmClass());
    }

    @Test
    public void testGetBmDescription() {
        ProbeInformation probeInfo = new ProbeInformation("TargetName", "TargetType", "BMClass", "BMName", "BMDescription", "ProbeName", 10, "http://xmlrpc.url");
        assertEquals("BMDescription", probeInfo.getBmDescription());
    }

    @Test
    public void testGetPrecision() {
        ProbeInformation probeInfo = new ProbeInformation("TargetName", "TargetType", "BMClass", "BMName", "BMDescription", "ProbeName", 10, "http://xmlrpc.url");
        assertEquals(10, probeInfo.getPrecision());
    }

    @Test
    public void testGetMeasureURI() {
        ProbeInformation probeInfo = new ProbeInformation("TargetName", "TargetType", "BMClass", "BMName", "BMDescription", "ProbeName", 10, "http://xmlrpc.url");
        assertEquals("measureURI", probeInfo.getMeasureURI());
    }

    @Test
    public void testGetXmlRpcUrl() {
        ProbeInformation probeInfo = new ProbeInformation("TargetName", "TargetType", "BMClass", "BMName", "BMDescription", "ProbeName", 10, "http://xmlrpc.url");
        assertEquals("http://xmlrpc.url", probeInfo.getXmlRpcUrl());
    }

    // Additional test cases for edge cases and exceptions can be added here
    
}