package fi.vtt.noen.mfw.bundle.probe.shared;

import static org.junit.Assert.*;

import org.junit.Test;

public class ProbeInformationTest {

	 @Test
	    public void testGetTargetType() {
	        ProbeInformation probeInformation = new ProbeInformation("targetName", "targetType", "bmClass", "bmName", "bmDescription", "probeDescription", 1, "xmlRpcUrl");
	        assertEquals("targetType", probeInformation.getTargetType());
	    }
	 
	 @Test
	    public void testGetBmName() {
	        ProbeInformation probeInformation = new ProbeInformation("targetName", "targetType", "bmClass", "bmName", "bmDescription", "probeDescription", 1, "xmlRpcUrl");
	        assertEquals("bmName", probeInformation.getBmName());
	    }
	 
	 @Test
	    public void testGetProbeName() {
	        ProbeInformation probeInformation = new ProbeInformation("targetName", "targetType", "bmClass", "bmName", "bmDescription", "probeDescription", 1, "xmlRpcUrl");
	        assertEquals("probeDescription", probeInformation.getProbeName());
	    }
	 
	 @Test
	    public void testGetTargetName() {
	        ProbeInformation probeInformation = new ProbeInformation("targetName", "targetType", "bmClass", "bmName", "bmDescription", "probeDescription", 1, "xmlRpcUrl");
	        assertEquals("targetName", probeInformation.getTargetName());
	    }
	 
	 @Test
	    public void testGetBmClass() {
	        ProbeInformation probeInformation = new ProbeInformation("targetName", "targetType", "bmClass", "bmName", "bmDescription", "probeDescription", 1, "xmlRpcUrl");
	        assertEquals("bmClass", probeInformation.getBmClass());
	    }
	 
	 @Test
	    public void testGetBmDescription() {
	        ProbeInformation probeInformation = new ProbeInformation("targetName", "targetType", "bmClass", "bmName", "bmDescription", "probeDescription", 1, "xmlRpcUrl");
	        assertEquals("bmDescription", probeInformation.getBmDescription());
	    }
	 
	 @Test
	    public void testGetPrecision() {
	        ProbeInformation probeInformation = new ProbeInformation("targetName", "targetType", "bmClass", "bmName", "bmDescription", "probeDescription", 1, "xmlRpcUrl");
	        assertEquals(1, probeInformation.getPrecision());
	    }
	 
	 @Test
	    public void testGetMeasureURI() {
	        ProbeInformation probeInformation = new ProbeInformation("targetName", "targetType", "bmClass", "bmName", "bmDescription", "probeDescription", 1, "xmlRpcUrl");
	        String measureURI = "fi.vtt.noen.mfw.bundle.common.Const.createMeasureURI(targetType, targetName, bmClass, bmName)";
	        assertEquals(measureURI, probeInformation.getMeasureURI());
	    }
	 
	 @Test
	    public void testGetXmlRpcUrl() {
	        ProbeInformation probeInformation = new ProbeInformation("targetName", "targetType", "bmClass", "bmName", "bmDescription", "probeDescription", 1, "xmlRpcUrl");
	        assertEquals("xmlRpcUrl", probeInformation.getXmlRpcUrl());
	    }
	 
	 @Test
	 public void testEquals() {
		 ProbeInformation probeInformation = new ProbeInformation("targetName", "targetType", "bmClass", "bmName", "bmDescription", "probeDescription", 1, "xmlRpcUrl");
		 ProbeInformation probeInformation2 = new ProbeInformation("targetName", "targetType", "bmClass", "bmName", "bmDescription", "probeDescription", 1, "xmlRpcUrl");
		 assertTrue(probeInformation.equals(probeInformation2));
	 }
	 
	 @Test
	 public void testHashCode() {
		 ProbeInformation probeInformation = new ProbeInformation("targetName", "targetType", "bmClass", "bmName", "bmDescription", "probeDescription", 1, "xmlRpcUrl");
		 ProbeInformation probeInformation2 = new ProbeInformation("targetName", "targetType", "bmClass", "bmName", "bmDescription", "probeDescription", 1, "xmlRpcUrl");
		 assertEquals(probeInformation.hashCode(), probeInformation2.hashCode());
	 }
		    
}