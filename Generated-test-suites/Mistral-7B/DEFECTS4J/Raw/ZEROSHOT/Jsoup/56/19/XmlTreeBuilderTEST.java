package org.jsoup.parser;

import org.junit.Before;
import org.junit.Test;
import org.jsoup.Jsoup;
import org.jsoup.helper.Validate;
import org.jsoup.nodes.*;
import org.jsoup.parser.ParseErrorList;
import org.jsoup.parser.ParseSettings;
import org.junit.Assert.*;

import java.util.List;

import static org.junit.Assert.*;

public class XmlTreeBuilderTest {

 private XmlTreeBuilder xmlTreeBuilder;
 @Before
 public void setup() {
 xmlTreeBuilder = new XmlTreeBuilder();
 }

 @Test
 public void testParse() {
 String xmlString = "<root><child1><child2></child2></child1></root>";
 Document document = xmlTreeBuilder.parse(xmlString, null);
 Element rootElement = document.getRootElement();
 assertEquals("root", rootElement.tagName());
 List<Node> rootChildren = rootElement.children();
 assertEquals(1, rootChildren.size());
 Element child1Element = (Element) rootChildren.get(0);
 assertEquals("child1", child1Element.tagName());
 List<Node> child1Children = child1Element.children();
 assertEquals(1, child1Children.size());
 Element child2Element = (Element) child1Children.get(0);
 assertEquals("child2", child2Element.tagName());
 assertTrue(child2Element.children().isEmpty());
 }

 @Test(expected = AssertionError.class)
 public void testUnexpectedTokenTypeWithComment() {
 String xmlString = "<!DOCTYPE xml SYSTEM \"public.dtd\">";
 Token token = xmlTreeBuilder.tokenizer().next();
 xmlTreeBuilder.process(token);
 }

 @Test(expected = AssertionError.class)
 public void testUnexpectedTokenTypeWithDOCTYPE() {
 String xmlString = "<!DOCTYPE xml SYSTEM \"public.dtd\">";
 Document document = xmlTreeBuilder.parse(xmlString, null);
 }

 @Test
 public void testParseFragment() {
 String xmlFragment = "<root><child1><child2></child2></child1></root>";
 List<Node> nodes = xmlTreeBuilder.parseFragment(xmlFragment, null, ParseErrorList.noTracking(), ParseSettings.preserveCase);
 Element rootElement = (Element) nodes.get(0);
 assertEquals("root", rootElement.tagName());
 List<Node> rootChildren = rootElement.children();
 assertEquals(1, rootChildren.size());
 Element child1Element = (Element) rootChildren.get(0);
 assertEquals("child1", child1Element.tagName());
 List<Node> child1Children = child1Element.children();
 assertEquals(1, child1Children.size());
 Element child2Element = (Element) child1Children.get(0);
 assertEquals("child2", child2Element.tagName());
 assertTrue(child2Element.children().isEmpty());
 }

 @Test(expected = AssertionError.class)
 public void testParseFragmentWithInvalidInput() {
 String xmlFragment = "<root>";
 List<Node> nodes = xmlTreeBuilder.parseFragment(xmlFragment, null, ParseErrorList.noTracking(), ParseSettings.preserveCase);
 }

 @Test
 public void testPopStackToClose() {
 xmlTreeBuilder.parse("<root><child1><child2></child2></child1></root>", null);
 xmlTreeBuilder.popStackToClose(new Token.EndTag("child1"));
 Element rootElement = xmlTreeBuilder.doc.getRootElement();
 List<Node> rootChildren = rootElement.children();
 assertEquals(0, rootChildren.size());
 }

 @Test
 public void testPopStackToCloseWithUnknownTag() {
 xmlTreeBuilder.parse("<root><unknownTag></unknownTag></root>", null);
 xmlTreeBuilder.popStackToClose(new Token.EndTag("unknownTag"));
 Element rootElement = xmlTreeBuilder.doc.getRootElement();
 List<Node> rootChildren = rootElement.children();
 assertEquals(0, rootChildren.size());
 }

 @Test
 public void testPopStackToCloseWithEmptyStack() {
 xmlTreeBuilder.parse("<root></root>", null);
 xmlTreeBuilder.popStackToClose(new Token.EndTag("root"));
 Element rootElement = xmlTreeBuilder.doc.getRootElement();
 List<Node> rootChildren = rootElement.children();
 assertEquals(0, rootChildren.size());
 }
}