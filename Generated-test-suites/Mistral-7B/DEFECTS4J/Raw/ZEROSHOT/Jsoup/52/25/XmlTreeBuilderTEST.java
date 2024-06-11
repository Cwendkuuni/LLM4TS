package org.jsoup.parser;

import org.jsoup.helper.Validate;
import org.jsoup.nodes.*;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

/**
 * Test class for XmlTreeBuilder
 */
public class XmlTreeBuilderTest {

 private XmlTreeBuilder xmlTreeBuilder;
 private Document document;

 @Before
 public void setUp() {
 xmlTreeBuilder = new XmlTreeBuilder();
 document = xmlTreeBuilder.parseFragment("<root><child/><child/></root>", "", new ParseErrorList());
 }

 @Test
 public void testInitialiseParse() {
 assertEquals("root", document.tagName());
 assertNotNull(document.outputSettings());
 assertFalse(document.outputSettings().isSyntheticElementText());
 assertFalse(document.outputSettings().isSyntheticTag());
 assertFalse(document.outputSettings().isTrimWhitespace());
 assertFalse(document.outputSettings().isPrettyPrint());
 assertFalse(document.outputSettings().isParseTextAsCharacter());
 assertTrue(document.outputSettings().isWhitespaceAutomatically());
 assertFalse(document.outputSettings().isFormatted());
 assertFalse(document.outputSettings().isXmlDeclarationPresent());
 assertNull(document.outputSettings().xmlDeclaration());
 assertNull(document.outputSettings().baseUri());
 }

 @Test
 public void testProcessStartTag() {
 Element child1 = document.child(0);
 assertEquals("child", child1.tagName());
 assertNotNull(child1.attributes());
 assertEquals(0, child1.attributes().size());
 }

 @Test
 public void testProcessEndTag() {
 xmlTreeBuilder = new XmlTreeBuilder();
 Document fragmentDocument = xmlTreeBuilder.parseFragment("<root><child/></root>", "", new ParseErrorList());
 Element root = fragmentDocument.child(0);
 assertEquals("root", root.tagName());
 Element child = root.child(0);
 assertEquals("child", child.tagName());
 xmlTreeBuilder.popStackToClose(new Token.EndTag("child"));
 assertNull(root.child(0));
 }

 @Test
 public void testProcessComment() {
 Node comment = document.childNodes().get(2);
 assertEquals("--", comment.tagName());
 assertEquals("", comment.data());
 }

 @Test
 public void testProcessCharacter() {
 Node textNode = document.childNodes().get(1);
 assertEquals("", textNode.data());
 assertEquals(Node.Text, textNode.nodeType());
 }

 @Test
 public void testProcessDoctype() {
 Document docTypeNode = xmlTreeBuilder.parseFragment("<?xml version=\"1.0\" encoding=\"UTF-8\"?>", "", new ParseErrorList());
 DocumentType documentType = (DocumentType) docTypeNode.childNodes().get(0);
 assertEquals("xml", documentType.getName());
 assertNotNull(documentType.getPublicIdentifier());
 assertNotNull(documentType.getSystemIdentifier());
 }

 @Test(expected = AssertionError.class)
 public void testUnexpectedTokenType() {
 xmlTreeBuilder = new XmlTreeBuilder();
 xmlTreeBuilder.parseFragment("invalid", "", new ParseErrorList());
 }
}