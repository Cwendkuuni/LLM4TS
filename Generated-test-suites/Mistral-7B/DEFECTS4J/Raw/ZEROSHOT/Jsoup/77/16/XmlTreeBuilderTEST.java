package org.jsoup.parser;

import org.jsoup.Jsoup;
import org.jsoup.helper.Validate;
import org.jsoup.nodes.*;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import java.io.StringReader;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(JUnit4.class)
public class XmlTreeBuilderTest {

 private XmlTreeBuilder xmlTreeBuilder;

 @Before
 public void setup() {
 xmlTreeBuilder = new XmlTreeBuilder();
 }

 @Test
 public void testParse() {
 String xmlInput = "<root><element attribute=\"value\">Content</element></root>";
 Document document = xmlTreeBuilder.parse(new StringReader(xmlInput), null);
 assertEquals("root", document.tagName());
 assertNotNull(document.childNodes());
 assertEquals(1, document.childNodes().size());
 }

 @Test
 public void testParseWithEmptyInput() {
 String xmlInput = "";
 Document document = xmlTreeBuilder.parse(new StringReader(xmlInput), null);
 assertNotNull(document);
 assertEquals("", document.outerHtml());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseWithNullInput() {
 xmlTreeBuilder.parse(null, null);
 }

 @Test
 public void testParseFragment() {
 String xmlFragment = "<element attribute=\"value\">Content</element>";
 List<Node> nodes = xmlTreeBuilder.parseFragment(xmlFragment, null, ParseErrorList.noTracking(), ParseSettings.preserveCase);
 assertEquals(1, nodes.size());
 assertEquals("element", nodes.get(0).nodeName());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseFragmentWithNullInput() {
 xmlTreeBuilder.parseFragment(null, null, ParseErrorList.noTracking(), ParseSettings.preserveCase);
 }

 @Test
 public void testProcessStartTag() {
 Token startTagToken = new Token.StartTag("element", null, null, null, null, null, null, 0, -1, 0, 0, null);
 Element element = xmlTreeBuilder.insert(startTagToken);
 assertEquals("element", element.tagName());
 }

 @Test
 public void testProcessEndTag() {
 String xmlInput = "<root><element><subElement/></element></root>";
 Document document = xmlTreeBuilder.parse(new StringReader(xmlInput), null);
 Token endTagToken = new Token.EndTag("element", null, null, null, null, null, null, 0, 0, 0, 0, null);
 xmlTreeBuilder.popStackToClose(endTagToken);
 List<Node> nodes = document.childNodes();
 assertEquals(1, nodes.size());
 Element root = (Element) nodes.get(0);
 assertEquals(1, root.childNodes().size());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testProcessEndTagWithUnknownTag() {
 String xmlInput = "<root><unknown></root>";
 Document document = xmlTreeBuilder.parse(new StringReader(xmlInput), null);
 Token endTagToken = new Token.EndTag("unknown", null, null, null, null, null, null, 0, 0, 0, 0, null);
 xmlTreeBuilder.popStackToClose(endTagToken);
 }
}