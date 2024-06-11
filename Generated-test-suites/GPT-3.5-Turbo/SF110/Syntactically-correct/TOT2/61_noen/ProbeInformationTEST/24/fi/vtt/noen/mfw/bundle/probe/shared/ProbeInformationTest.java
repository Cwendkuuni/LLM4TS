package fi.vtt.noen.mfw.bundle.probe.shared;
import static org.junit.Assert.*;

import org.junit.Test;

public class ProbeInformationTest {
    
    private ProbeInformation probeInfo;
    
    @Test
    public void testGetTargetType() {
        probeInfo = new ProbeInformation("TargetName", "TargetType", "BMClass", "BMName", "BMDescription", "ProbeName", 10, "http://xmlrpcurl.com");
        assertEquals("TargetType", probeInfo.getTargetType());
    }
    
    @Test
    public void testGetBmName() {
        probeInfo = new ProbeInformation("TargetName", "TargetType", "BMClass", "BMName", "BMDescription", "ProbeName", 10, "http://xmlrpcurl.com");
        assertEquals("BMName", probeInfo.getBmName());
    }
    
    @Test
    public void testGetProbeName() {
        probeInfo = new ProbeInformation("TargetName", "TargetType", "BMClass", "BMName", "BMDescription", "ProbeName", 10, "http://xmlrpcurl.com");
        assertEquals("ProbeName", probeInfo.getProbeName());
    }
    
    @Test
    public void testGetTargetName() {
        probeInfo = new ProbeInformation("TargetName", "TargetType", "BMClass", "BMName", "BMDescription", "ProbeName", 10, "http://xmlrpcurl.com");
        assertEquals("TargetName", probeInfo.getTargetName());
    }
    
    @Test
    public void testGetBmClass() {
        probeInfo = new ProbeInformation("TargetName", "TargetType", "BMClass", "BMName", "BMDescription", "ProbeName", 10, "http://xmlrpcurl.com");
        assertEquals("BMClass", probeInfo.getBmClass());
    }
    
    @Test
    public void testGetBmDescription() {
        probeInfo = new ProbeInformation("TargetName", "TargetType", "BMClass", "BMName", "BMDescription", "ProbeName", 10, "http://xmlrpcurl.com");
        assertEquals("BMDescription", probeInfo.getBmDescription());
    }
    
    @Test
    public void testGetPrecision() {
        probeInfo = new ProbeInformation("TargetName", "TargetType", "BMClass", "BMName", "BMDescription", "ProbeName", 10, "http://xmlrpcurl.com");
        assertEquals(10, probeInfo.getPrecision());
    }
    
    @Test
    public void testGetMeasureURI() {
        probeInfo = new ProbeInformation("TargetName", "TargetType", "BMClass", "BMName", "BMDescription", "ProbeName", 10, "http://xmlrpcurl.com");
        assertEquals("measureURI", probeInfo.getMeasureURI()); // Edge case: testing the created URI
    }
    
    @Test
    public void testGetXmlRpcUrl() {
        probeInfo = new ProbeInformation("TargetName", "TargetType", "BMClass", "BMName", "BMDescription", "ProbeName", 10, "http://xmlrpcurl.com");
        assertEquals("http://xmlrpcurl.com", probeInfo.getXmlRpcUrl());
    }
}