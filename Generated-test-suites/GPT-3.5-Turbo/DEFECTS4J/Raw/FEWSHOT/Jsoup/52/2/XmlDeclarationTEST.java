package org.jsoup.nodes;

import org.junit.Test;

import static org.junit.Assert.*;

public class XmlDeclarationTest {
	@Test
	public void testNodeName() {
		XmlDeclaration xmlDeclaration = new XmlDeclaration("", "", true);
		assertEquals("#declaration", xmlDeclaration.nodeName());
	}

	@Test
	public void testName() {
		XmlDeclaration xmlDeclaration = new XmlDeclaration("test", "", true);
		assertEquals("test", xmlDeclaration.name());
	}

	@Test
	public void testGetWholeDeclaration() {
		XmlDeclaration xmlDeclaration = new XmlDeclaration("xml", "", true);
		assertEquals("xml", xmlDeclaration.getWholeDeclaration());
		
		xmlDeclaration = new XmlDeclaration("xml", "", true);
		xmlDeclaration.attributes.put("version", "1.0");
		assertEquals("xml version=\"1.0\"", xmlDeclaration.getWholeDeclaration());
		
		xmlDeclaration = new XmlDeclaration("xml", "", true);
		xmlDeclaration.attributes.put("encoding", "UTF-8");
		assertEquals("xml encoding=\"UTF-8\"", xmlDeclaration.getWholeDeclaration());
		
		xmlDeclaration = new XmlDeclaration("xml", "", true);
		xmlDeclaration.attributes.put("version", "1.0");
		xmlDeclaration.attributes.put("encoding", "UTF-8");
		assertEquals("xml version=\"1.0\" encoding=\"UTF-8\"", xmlDeclaration.getWholeDeclaration());
		
		xmlDeclaration = new XmlDeclaration("declaration", "", true);
		assertEquals("declaration", xmlDeclaration.getWholeDeclaration());
	}

	@Test
	public void testToString() {
		XmlDeclaration xmlDeclaration = new XmlDeclaration("test", "", true);
		assertEquals("<!test>", xmlDeclaration.toString());
	}
	
	@Test
	public void testOuterHtmlHead() {
		XmlDeclaration xmlDeclaration = new XmlDeclaration("test", "", true);
		StringBuilder builder = new StringBuilder();
		try {
			xmlDeclaration.outerHtmlHead(builder, 0, null);
		} catch (Exception e) {
			fail("Exception thrown: " + e.getMessage());
		}
		assertEquals("<!test>", builder.toString());
	}
	
	@Test
	public void testOuterHtmlTail() {
		XmlDeclaration xmlDeclaration = new XmlDeclaration("test", "", true);
		StringBuilder builder = new StringBuilder();
		try {
			xmlDeclaration.outerHtmlTail(builder, 0, null);
		} catch (Exception e) {
			fail("Exception thrown: " + e.getMessage());
		}
		assertEquals("", builder.toString());
	}
}