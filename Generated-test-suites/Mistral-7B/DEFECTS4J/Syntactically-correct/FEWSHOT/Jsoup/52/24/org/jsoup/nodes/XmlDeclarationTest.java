package org.jsoup.nodes;

import static org.junit.Assert.assertEquals;
import staticorg.junit.Assert.assertTrue;

import org.junit.Test;

public class XmlDeclarationTest {

@Test
public void testXmlDeclarationWithNameAndBaseUri() {
XmlDeclaration xmlDeclaration = new XmlDeclaration("xml", "http://www.example.com", false);
assertEquals("#declaration", xmlDeclaration.nodeName());
assertEquals("xml", xmlDeclaration.name());
assertEquals("http://www.example.com", xmlDeclaration.baseUri());
}

@Test
public void testXmlDeclarationWithNameAndBaseUriAndProcessingInstruction() {
XmlDeclaration xmlDeclaration = new XmlDeclaration("xml", "http://www.example.com", true);
assertEquals("#declaration", xmlDeclaration.nodeName());
assertEquals("xml", xmlDeclaration.name());
assertEquals("http://www.example.com", xmlDeclaration.baseUri());
assertTrue(xmlDeclaration.isProcessingInstruction);
}

@Test
public void testXmlDeclarationWithEmptyNameAndBaseUri() {
XmlDeclaration xmlDeclaration = new XmlDeclaration("", "http://www.example.com", false);
assertEquals("#declaration", xmlDeclaration.nodeName());
assertNull(xmlDeclaration.name());
assertEquals("http://www.example.com", xmlDeclaration.baseUri());
}

@Test
public void testXmlDeclarationWithNameAndAttributes() {
XmlDeclaration xmlDeclaration = new XmlDeclaration("xml", "http://www.example.com", false)
.attr("version", "1.0")
.attr("encoding", "UTF-8");
assertEquals("#declaration", xmlDeclaration.nodeName());
assertEquals("xml version=\"1.0\" encoding=\"UTF-8\"", xmlDeclaration.getWholeDeclaration());
}

@Test
public void testXmlDeclarationWithProcessingInstructionAndAttributes() {
XmlDeclaration xmlDeclaration = new XmlDeclaration("xml", "http://www.example.com", true)
.attr("version", "1.0")
.attr("encoding", "UTF-8");
assertEquals("#declaration", xmlDeclaration.nodeName());
assertEquals("!-- xml version=\"1.0\" encoding=\"UTF-8\"--", xmlDeclaration.getWholeDeclaration());
}

@Test
public void testXmlDeclarationToString() {
XmlDeclaration xmlDeclaration = new XmlDeclaration("xml", "http://www.example.com", false);
assertEquals("<xml version=\"1.0\" encoding=\"UTF-8\">", xmlDeclaration.toString());
}
}