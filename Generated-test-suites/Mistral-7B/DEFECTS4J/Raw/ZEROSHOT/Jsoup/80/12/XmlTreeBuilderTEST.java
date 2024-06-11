package org.jsoup.parser;

import org.jsoup.Jsoup;
import org.jsoup.helper.Validate;
import org.jsoup.nodes.*;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.modules.junit4.PowerMockRunner;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

@RunWith(PowerMockRunner.class)
public class XmlTreeBuilderTest {

 private XmlTreeBuilder xmlTreeBuilder;
 private ParseSettings parseSettings;
 private ParseErrorList errorList;
 private Reader input;

 @Before
 public void setup() {
 xmlTreeBuilder = new XmlTreeBuilder();
 parseSettings = xmlTreeBuilder.defaultSettings();
 errorList = new ParseErrorList();
 }

 @Test
 public void testParse() {
 String xmlString = "<root><child1>test1</child1><child2><grandChild>test2</grandChild></child2></root>";
 input = new StringReader(xmlString);
 Document document = xmlTreeBuilder.parse(input, "baseUri");
 Element rootElement = document.rootElement();
 Element child1Element = rootElement.child(0);
 Element child2Element = rootElement.child(1);
 Element grandChildElement = child2Element.child(0);

 Validate.assertEquals("root", rootElement.tagName());
 Validate.assertEquals("child1", child1Element.tagName());
 Validate.assertEquals("test1", child1Element.text());
 Validate.assertEquals("child2", child2Element.tagName());
 Validate.assertEquals("grandChild", grandChildElement.tagName());
 Validate.assertEquals("test2", grandChildElement.text());
 }

 @Test(expected = AssertionError.class)
 public void testUnknownTag() {
 String xmlString = "<unknownTag/>";
 input = new StringReader(xmlString);
 xmlTreeBuilder.parse(input, "baseUri");
 }

 @Test
 public void testSelfClosingTag() {
 String xmlString = "<img src=\"image.jpg\" />";
 input = new StringReader(xmlString);
 Document document = xmlTreeBuilder.parse(input, "baseUri");
 Element imgElement = document.select("img").first();

 Validate.assertEquals("img", imgElement.tagName());
 Validate.assertTrue(imgElement.hasAttr("src"));
 }

 @Test
 public void testComment() {
 String xmlString = "<!-- This is a comment -->";
 input = new StringReader(xmlString);
 Document document = xmlTreeBuilder.parse(input, "baseUri");

 Comment comment = document.select("--.*").first();
 Validate.assertNotNull(comment);
 }

 @Test
 public void testCData() {
 String xmlString = "<![CDATA[This is a CData]]>";
 input = new StringReader(xmlString);
 Document document = xmlTreeBuilder.parse(input, "baseUri");
 CDataNode cDataNode = (CDataNode) document.select("![CDATA[*]]").first();
 Validate.assertNotNull(cDataNode);
 Validate.assertEquals("This is a CData", cDataNode.getData());
 }

 @Test
 public void testDoctype() {
 String xmlString = "<!DOCTYPE html SYSTEM \"about:legacy-compat\">";
 input = new StringReader(xmlString);
 Document document = xmlTreeBuilder.parse(input, "baseUri");
 DocumentType doctypeNode = document.doctype();

 Validate.assertEquals("html", doctypeNode.name());
 Validate.assertNull(doctypeNode.publicId());
 Validate.assertNull(doctypeNode.systemId());
 }

 @Test
 public void testParseFragment() {
 String xmlFragment = "<root><child1>test1</child1><child2><grandChild>test2</grandChild></child2></root>";
 List<Node> nodes = xmlTreeBuilder.parseFragment(xmlFragment, "baseUri", errorList, parseSettings);
 Document document = new Document();
 document.inputSettings().setBaseUri("baseUri");
 for (Node node : nodes) {
 document.append(node);
 }
 Element rootElement = document.rootElement();
 Element child1Element = rootElement.child(0);
 Element child2Element = rootElement.child(1);
 Element grandChildElement = child2Element.child(0);

 Validate.assertEquals("root", rootElement.tagName());
 Validate.assertEquals("child1", child1Element.tagName());
 Validate.assertEquals("test1", child1Element.text());
 Validate.assertEquals("child2", child2Element.tagName());
 Validate.assertEquals("grandChild", grandChildElement.tagName());
 Validate.assertEquals("test2", grandChildElement.text());
 }

 @Test
 public void testProcessToken() {
 PowerMockito.mockStatic(Jsoup.class);
 PowerMockito.when(Jsoup.parse("<test/>", ArgumentMatchers.anyString(), ArgumentMatchers.eq(Parser.xmlParser()))).thenReturn(Mockito.mock(Document.class));

 Token token = new Token(Token.Type.StartTag, "<test/>", 0, 0);
 xmlTreeBuilder = PowerMockito.spy(new XmlTreeBuilder());
 xmlTreeBuilder.process(token);

 Element testElement = Mockito.verify(XmlTreeBuilder.class, Mockito.times(1)).insert(ArgumentMatchito.argThat(t -> t instanceof Token.StartTag && t.name().equals("test")));
 Validate.assertNotNull(testElement);
 }

 @Test
 public void testPopStackToClose() {
 Element rootElement = Mockito.mock(Element.class);
 Element childElement = Mockito.mock(Element.class);
 PowerMockito.when(rootElement.nodeName()).thenReturn("root");
 PowerMockito.when(childElement.nodeName()).thenReturn("child");
 Stack<Element> stack = Mockito.mock(Stack.class);
 PowerMockito.when(stack.get(0)).thenReturn(rootElement);
 PowerMockito.when(stack.get(1)).thenReturn(childElement);
 PowerMockito.when(stack.size()).thenReturn(2);
 PowerMockito.when(stack.remove(ArgumentMatchers.eq(1))).thenReturn(null);

 xmlTreeBuilder = PowerMockito.spy(new XmlTreeBuilder());
 xmlTreeBuilder.stack = stack;
 xmlTreeBuilder.popStackToClose(Mockito.mock(Token.EndTag.class));

 Validate.verify(stack, Mockito.times(1)).remove(ArgumentMatchers.eq(childElement));
 }
}