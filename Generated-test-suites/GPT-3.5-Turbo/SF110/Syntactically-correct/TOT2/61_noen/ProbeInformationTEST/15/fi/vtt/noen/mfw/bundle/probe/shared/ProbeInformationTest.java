package fi.vtt.noen.mfw.bundle.probe.shared;
import org.junit.Test;
import static org.junit.Assert.*;

public class ProbeInformationTest {

    @Test
    public void testGetTargetType() {
        ProbeInformation probeInfo = new ProbeInformation("TargetName", "TargetType", "BMClass", "BMName", "BMDescription", "ProbeName", 5, "http://example.com");
        assertEquals("TargetType", probeInfo.getTargetType());
    }

    @Test
    public void testGetBmName() {
        ProbeInformation probeInfo = new ProbeInformation("TargetName", "TargetType", "BMClass", "BMName", "BMDescription", "ProbeName", 5, "http://example.com");
        assertEquals("BMName", probeInfo.getBmName());
    }

    @Test
    public void testGetProbeName() {
        ProbeInformation probeInfo = new ProbeInformation("TargetName", "TargetType", "BMClass", "BMName", "BMDescription", "ProbeName", 5, "http://example.com");
        assertEquals("ProbeName", probeInfo.getProbeName());
    }

    // additional test cases for other methods can be added here

    @Test
    public void testEquals() {
        ProbeInformation probeInfo1 = new ProbeInformation("TargetName", "TargetType", "BMClass", "BMName", "BMDescription", "ProbeName", 5, "http://example.com");
        ProbeInformation probeInfo2 = new ProbeInformation("TargetName", "TargetType", "BMClass", "BMName", "BMDescription", "ProbeName", 5, "http://example.com");
        assertTrue(probeInfo1.equals(probeInfo2));
    }

    // additional test cases for edge cases and exceptions can be added here
    
}