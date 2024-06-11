import org.junit.Before;
import org.junit.Test;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.XmlTreeBuilder;
import org.jsoup.parser.Parser;
import org.jsoup.parser.Token;
import org.jsoup.parser.TokenCollector;
import org.jsoup.parser.Tokenizer;
import org.jsoup.nodes.TextNode;
import org.jsoup.nodes.CDataNode;
import org.jsoup.nodes.DocumentType;
import org.jsoup.nodes.Comment;
import org.jsoup.nodes.XmlDeclaration;
import static org.junit.Assert.*;

import java.io.StringReader;
import java.io.IOException;
import java.util.List;

public class XmlTreeBuilderTest {

 private XmlTreeBuilder xmlTreeBuilder;
 private Document doc;

 @Before
 public void setup() {
 xmlTreeBuilder = new XmlTreeBuilder();
 doc = xmlTreeBuilder.parse(new StringReader(""), "");
 }

 @Test
 public void testDefaultSettings() {
 ParseSettings settings = xmlTreeBuilder.defaultSettings();
 assertEquals(ParseSettings.preserveCase, settings);
 }

 @Test
 public void testParse() throws IOException {
 String xmlInput = "<root><child>Hello World</child></root>";
 doc = xmlTreeBuilder.parse(new StringReader(xmlInput), "");
 assertNotNull(doc);
 assertEquals(1, doc.childNodes().size());
 assertEquals("root", doc.tagName());
 Element root = doc.child(0);
 assertEquals("child", root.tagName());
 assertEquals("Hello World", root.text());
 }

 @Test
 public void testParseWithBaseUri() throws IOException {
 String xmlInput = "<base href=\"/base/\"><root><child>Hello World</child></root>";
 doc = xmlTreeBuilder.parse(new StringReader(xmlInput), "/base/");
 assertNotNull(doc);
 assertEquals(1, doc.childNodes().size());
 Element root = doc.child(0);
 assertEquals("root", root.tagName());
 assertEquals("child", root.child(0).tagName());
 assertEquals("Hello World", root.child(0).text());
 assertEquals("/base/root/child", root.absoluteUrl());
 }

 @Test
 public void testProcessStartTag() throws IOException {
 String xmlInput = "<root><child/><child></child></root>";
 doc = xmlTreeBuilder.parse(new StringReader(xmlInput), "");
 Element root = doc.child(0);
 assertNotNull(root);
 assertEquals(3, doc.childNodes().size());
 assertEquals("root", root.tagName());
 Element child1 = root.child(0);
 assertEquals("child", child1.tagName());
 assertTrue(child1.hasAttrs());
 Element child2 = root.child(1);
 assertEquals("child", child2.tagName());
 assertFalse(child2.hasAttrs());
 }

 @Test
 public void testProcessEndTag() throws IOException {
 String xmlInput = "<root><child><child/></child></root>";
 doc = xmlTreeBuilder.parse(new StringReader(xmlInput), "");
 Element root = doc.child(0);
 assertEquals(3, doc.childNodes().size());
 assertEquals("root", root.tagName());
 Element child1 = root.child(0);
 assertEquals("child", child1.tagName());
 assertEquals(1, child1.childNodes().size());
 Element child2 = child1.child(0);
 assertEquals("child", child2.tagName());
 assertTrue(child2.isDetached());
 }

 @Test
 public void testProcessComment() throws IOException {
 String xmlInput = "<!-- This is a comment -->";
 doc = xmlTreeBuilder.parse(new StringReader(xmlInput), "");
 assertEquals(1, doc.childNodes().size());
 Comment comment = (Comment) doc.child(0);
 assertEquals("This is a comment", comment.data());
 }

 @Test
 public void testProcessCharacter() throws IOException {
 String xmlInput = "<root>&#x00A9;<child>Hello & World</child></root>";
 doc = xmlTreeBuilder.parse(new StringReader(xmlInput), "");
 assertEquals(1, doc.childNodes().size());
 Element root = doc.child(0);
 assertEquals("root", root.tagName());
 assertEquals(1, root.childNodes().size());
 Element child = root.child(0);
 assertEquals("child", child.tagName());
 assertEquals("Hello © World", child.text());
 }

 @Test
 public void testProcessDoctype() throws IOException {
 String xmlInput = "<!DOCTYPE root [ <!ATTLIST child foo CDATA #REQUIRED > ]><root></root>";
 doc = xmlTreeBuilder.parse(new StringReader(xmlInput), "");
 assertEquals(1, doc.childNodes().size());
 DocumentType doctype = (DocumentType) doc.child(0);
 assertEquals("root", doctype.nodeName());
 assertEquals("root", doctype.getPublicId());
 assertEquals("", doctype.getSystemId());
 assertEquals("root", doctype.getLocalName());
 List<Attribute> attributes = doctype.attributes();
 assertEquals(1, attributes.size());
 assertEquals("foo", attributes.get(0).getName());
 assertEquals("CDATA", attributes.get(0).getValue());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testProcessInvalidToken() throws IOException {
 String xmlInput = "<root><child>Hello World<";
 doc = xmlTreeBuilder.parse(new StringReader(xmlInput), "");
 fail("Expected IllegalArgumentException");
 }

 @Test
 public void testPopStackToClose() {
 doc = xmlTreeBuilder.parse(new StringReader("<root><child><child/></child></root>"), "");
 Element root = doc.child(0);
 Element child1 = root.child(0);
 Element child2 = root.child(1);
 xmlTreeBuilder.popStackToClose(child2.asEndTag());
 assertEquals(1, root.childNodes().size());
 assertFalse(child1.isDetached());
 assertTrue(child2.isDetached());
 }

 @Test
 public void testParseFragment() throws IOException {
 String xmlInput = "<root><child>Hello World</child></root>";
 List<Node> nodes = xmlTreeBuilder.parseFragment(xmlInput, "", ParseErrorList.noTracking(), ParseSettings.preserveCase);
 assertEquals(1, nodes.size());
 Element root = (Element) nodes.get(0);
 assertEquals("root", root.tagName());
 assertEquals(1, root.childNodes().size());
 Element child = root.child(0);
 assertEquals("child", child.tagName());
 assertEquals("Hello World", child.text());
 }

}