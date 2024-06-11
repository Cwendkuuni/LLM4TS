
package fi.vtt.noen.mfw.bundle.probe.shared;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class ProbeInformationTest {

	@Test
	public void testGetTargetType() {
		ProbeInformation probe = new ProbeInformation("target", "type", "bmClass", "bmName", "bmDescription",
				"probeDescription", 5, "xmlRpcUrl");
		assertEquals("type", probe.getTargetType());
	}

	@Test
	public void testGetBmName() {
		ProbeInformation probe = new ProbeInformation("target", "type", "bmClass", "bmName", "bmDescription",
				"probeDescription", 5, "xmlRpcUrl");
		assertEquals("bmName", probe.getBmName());
	}

	@Test
	public void testGetProbeName() {
		ProbeInformation probe = new ProbeInformation("target", "type", "bmClass", "bmName", "bmDescription",
				"probeDescription", 5, "xmlRpcUrl");
		assertEquals("probeDescription", probe.getProbeName());
	}

	@Test
	public void testGetTargetName() {
		ProbeInformation probe = new ProbeInformation("target", "type", "bmClass", "bmName", "bmDescription",
				"probeDescription", 5, "xmlRpcUrl");
		assertEquals("target", probe.getTargetName());
	}

	@Test
	public void testGetBmClass() {
		ProbeInformation probe = new ProbeInformation("target", "type", "bmClass", "bmName", "bmDescription",
				"probeDescription", 5, "xmlRpcUrl");
		assertEquals("bmClass", probe.getBmClass());
	}

	@Test
	public void testGetBmDescription() {
		ProbeInformation probe = new ProbeInformation("target", "type", "bmClass", "bmName", "bmDescription",
				"probeDescription", 5, "xmlRpcUrl");
		assertEquals("bmDescription", probe.getBmDescription());
	}

	@Test
	public void testGetPrecision() {
		ProbeInformation probe = new ProbeInformation("target", "type", "bmClass", "bmName", "bmDescription",
				"probeDescription", 5, "xmlRpcUrl");
		assertEquals(5, probe.getPrecision());
	}

	@Test
	public void testGetMeasureURI() {
		ProbeInformation probe = new ProbeInformation("target", "type", "bmClass", "bmName", "bmDescription",
				"probeDescription", 5, "xmlRpcUrl");
		assertEquals("type://target/bmClass/bmName", probe.getMeasureURI());
	}

	@Test
	public void testGetXmlRpcUrl() {
		ProbeInformation probe = new ProbeInformation("target", "type", "bmClass", "bmName", "bmDescription",
				"probeDescription", 5, "xmlRpcUrl");
		assertEquals("xmlRpcUrl", probe.getXmlRpcUrl());
	}

	@Test
	public void testToString() {
		ProbeInformation probe = new ProbeInformation("target", "type", "bmClass", "bmName", "bmDescription",
				"probeDescription", 5, "xmlRpcUrl");
		String expected = "ProbeInformation{targetName='target', targetType='type', bmClass='bmClass', bmName='bmName', bmDescription='bmDescription', probeDescription='probeDescription', precision=5}";
		assertEquals(expected, probe.toString());
	}

	@Test
	public void testEquals() {
		ProbeInformation probe1 = new ProbeInformation("target", "type", "bmClass", "bmName", "bmDescription",
				"probeDescription", 5, "xmlRpcUrl");
		ProbeInformation probe2 = new ProbeInformation("target", "type", "bmClass", "bmName", "bmDescription",
				"probeDescription", 5, "xmlRpcUrl");
		ProbeInformation probe3 = new ProbeInformation("target", "type", "bmClass", "bmName", "bmDescription",
				"probeDescription", 6, "xmlRpcUrl");

		assertEquals(probe1, probe2);
		assertNotEquals(probe1, probe3);
	}

	@Test
	public void testHashCode() {
		ProbeInformation probe1 = new ProbeInformation("target", "type", "bmClass", "bmName", "bmDescription",
				"probeDescription", 5, "xmlRpcUrl");
		ProbeInformation probe2 = new ProbeInformation("target", "type", "bmClass", "bmName", "bmDescription",
				"probeDescription", 5, "xmlRpcUrl");

		assertEquals(probe1.hashCode(), probe2.hashCode());
	}

	@Test
	public void testNotNull() {
		ProbeInformation probe = new ProbeInformation("target", "type", "bmClass", "bmName", "bmDescription",
				"probeDescription", 5, "xmlRpcUrl");
		assertNotNull(probe);
	}

	@Test
	public void testNull() {
		ProbeInformation probe = null;
		assertNull(probe);
	}
}

