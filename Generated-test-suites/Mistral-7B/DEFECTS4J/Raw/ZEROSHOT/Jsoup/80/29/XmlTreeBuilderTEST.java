package org.jsoup.parser;

import org.jsoup.Jsoup;
import org.jsoup.helper.Validate;
import org.jsoup.nodes.*;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayReader;
import java.io.Reader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

@RunWith(MockitoJUnitRunner.class)
public class XmlTreeBuilderTest {

 private XmlTreeBuilder xmlTreeBuilder;
 private ParseSettings parseSettings;
 private Element rootElement;

 @Before
 public void setup() {
 xmlTreeBuilder = new XmlTreeBuilder();
 parseSettings = xmlTreeBuilder.defaultSettings();
 rootElement = new Element("root", null, null);
 xmlTreeBuilder.insertNode(rootElement);
 }

 @Test
 public void testParse() {
 String xmlInput = "<root><child1><grandChild tag='test'/></child1></root>";
 Reader xmlInputReader = new StringReader(xmlInput);
 Document xmlDocument = xmlTreeBuilder.parse(xmlInputReader, "baseUri");
 assertEquals("<root><child1><grandChild tag='test'/></child1></root>", xmlDocument.html());
 }

 @Test
 public void testParseWithInvalidInput() {
 String invalidXmlInput = "<root<child1>";
 Reader invalidXmlInputReader = new StringReader(invalidXmlInput);
 ParseErrorList errors = Mockito.mock(ParseErrorList.class);
 assertFalse(xmlTreeBuilder.parse(invalidXmlInputReader, "baseUri", errors, parseSettings).outputSettings().isValid());
 }

 @Test
 public void testParseFragment() {
 String xmlFragment = "<child2><child3/></child2>";
 List<Node> parsedNodes = xmlTreeBuilder.parseFragment(xmlFragment, "baseUri", Mockito.mock(ParseErrorList.class), parseSettings);
 Element child2Element = (Element) parsedNodes.get(0);
 assertTrue(child2Element.childNodes().size() == 1);
 Element child3Element = (Element) child2Element.childNodes().get(0);
 assertEquals("child3", child3Element.nodeName());
 }

 @Test
 public void testProcessStartTag() {
 Token startTagToken = Mockito.mock(Token.StartTag.class);
 Mockito.when(startTagToken.asStartTag()).thenReturn(new StartTag("tag", null, null, null, null, null, false));
 xmlTreeBuilder.process(startTagToken);
 Element tagElement = (Element) xmlTreeBuilder.stack.get(xmlTreeBuilder.stack.size() - 1);
 assertEquals("tag", tagElement.nodeName());
 }

 @Test
 public void testProcessEndTag() {
 Token endTagToken = Mockito.mock(Token.EndTag.class);
 Mockito.when(endTagToken.asEndTag()).thenReturn(new EndTag("tag"));
 xmlTreeBuilder.process(endTagToken);
 Element tagElement = (Element) xmlTreeBuilder.stack.get(xmlTreeBuilder.stack.size() - 2);
 assertEquals("tag", tagElement.nodeName());
 xmlTreeBuilder.stack.remove(xmlTreeBuilder.stack.size() - 1); // remove the root element for the next test
 }

 @Test
 public void testProcessComment() {
 Token commentToken = Mockito.mock(Token.Comment.class);
 Mockito.when(commentToken.asComment()).thenReturn(new Comment("comment"));
 xmlTreeBuilder.process(commentToken);
 assertEquals(1, xmlTreeBuilder.doc.childNodes().size());
 Comment commentNode = (Comment) xmlTreeBuilder.doc.childNodes().get(0);
 assertEquals("comment", commentNode.getData());
 }

 @Test
 public void testProcessCharacter() {
 Token characterToken = Mockito.mock(Token.Character.class);
 Mockito.when(characterToken.asCharacter()).thenReturn(new TextNode("char"));
 xmlTreeBuilder.process(characterToken);
 assertEquals(1, xmlTreeBuilder.doc.childNodes().size());
 TextNode textNode = (TextNode) xmlTreeBuilder.doc.childNodes().get(0);
 assertEquals("char", textNode.text());
 }

 @Test
 public void testProcessDoctype() {
 Token doctypeToken = Mockito.mock(Token.Doctype.class);
 Mockito.when(doctypeToken.getName()).thenReturn("doctype");
 xmlTreeBuilder.process(doctypeToken);
 assertEquals(1, xmlTreeBuilder.doc.childNodes().size());
 DocumentType doctypeNode = (DocumentType) xmlTreeBuilder.doc.childNodes().get(0);
 assertEquals("doctype", doctypeNode.nodeName());
 }

 @Test
 public void testProcessUnknownTag() {
 Token startTagToken = Mockito.mock(Token.StartTag.class);
 Mockito.when(startTagToken.asStartTag()).thenReturn(new StartTag("unknownTag", null, null, null, null, null, false));
 xmlTreeBuilder.process(startTagToken);
 Element unknownTagElement = (Element) xmlTreeBuilder.stack.get(xmlTreeBuilder.stack.size() - 1);
 assertTrue(unknownTagElement.nodeName().equals("unknownTag"));
 }

 @Test
 public void testPopStackToClose() {
 xmlTreeBuilder.stack.add(rootElement);
 xmlTreeBuilder.stack.add(new Element("child"));
 xmlTreeBuilder.popStackToClose(new EndTag("child"));
 assertNull(xmlTreeBuilder.stack.get(xmlTreeBuilder.stack.size() - 1));
 }
}