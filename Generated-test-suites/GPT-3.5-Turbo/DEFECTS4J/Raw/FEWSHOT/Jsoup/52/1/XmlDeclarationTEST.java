package org.jsoup.nodes;

import org.jsoup.helper.Validate;

import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

public class XmlDeclarationTest {
	
	@Test
	public void testConstructor() {
		XmlDeclaration xmlDecl = new XmlDeclaration("xml", "http://www.example.com", true);
		assertNotNull(xmlDecl);
		assertEquals("xml", xmlDecl.name());
		assertTrue(xmlDecl.isProcessingInstruction);
	}
	
	@Test(expected = NullPointerException.class)
	public void testConstructorWithNullName() {
		new XmlDeclaration(null, "http://www.example.com", true);
	}
	
	@Test
	public void testNodeName() {
		XmlDeclaration xmlDecl = new XmlDeclaration("xml", "http://www.example.com", true);
		assertEquals("#declaration", xmlDecl.nodeName());
	}
	
	@Test
	public void testGetWholeDeclaration() {
		XmlDeclaration xmlDecl = new XmlDeclaration("xml", "http://www.example.com", true);
		assertEquals("xml version=\"1.0\"", xmlDecl.getWholeDeclaration());
		
		xmlDecl = new XmlDeclaration("xml", "http://www.example.com", true);
		xmlDecl.attributes.put("version", "2.0");
		assertEquals("xml version=\"2.0\"", xmlDecl.getWholeDeclaration());
		
		xmlDecl = new XmlDeclaration("xml", "http://www.example.com", true);
		xmlDecl.attributes.put("version", "1.0");
		xmlDecl.attributes.put("encoding", "UTF-8");
		assertEquals("xml version=\"1.0\" encoding=\"UTF-8\"", xmlDecl.getWholeDeclaration());
	}
	
	@Test
	public void testOuterHtmlHead() throws IOException {
		XmlDeclaration xmlDecl = new XmlDeclaration("xml", "http://www.example.com", true);
		StringBuilder sb = new StringBuilder();
		xmlDecl.outerHtmlHead(sb, 0, new Document.OutputSettings());
		assertEquals("<!xml version=\"1.0\">", sb.toString());
		
		sb.setLength(0);
		xmlDecl.isProcessingInstruction = false;
		xmlDecl.outerHtmlHead(sb, 0, new Document.OutputSettings());
		assertEquals("<?xml version=\"1.0\">", sb.toString());
	}
	
	@Test
	public void testOuterHtmlTail() throws IOException {
		XmlDeclaration xmlDecl = new XmlDeclaration("xml", "http://www.example.com", true);
		StringBuilder sb = new StringBuilder();
		xmlDecl.outerHtmlTail(sb, 0, new Document.OutputSettings());
		assertEquals("", sb.toString());
	}
	
	@Test
	public void testToString() {
		XmlDeclaration xmlDecl = new XmlDeclaration("xml", "http://www.example.com", true);
		assertEquals("<!xml version=\"1.0\">", xmlDecl.toString());
		
		xmlDecl.isProcessingInstruction = false;
		assertEquals("<?xml version=\"1.0\">", xmlDecl.toString());
	}
	
}