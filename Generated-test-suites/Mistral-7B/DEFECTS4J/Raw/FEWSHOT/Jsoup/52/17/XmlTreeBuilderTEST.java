package org.jsoup.parser;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import org. Hamcrest.CoreMatchers;
import org. Hamcrest.core.IsInstanceOf;
import org. Hamcrest.core.IsNotNull;
import org. Hamcrest.core.IsString;
import org.jsoup.nodes.*;
import org.jsoup.parser.Token;
import org.jsoup.parser.Token.Attribute;
import org.jsoup.parser.Token.Command;
import org.jsoup.parser.Token.EndTag;
import org.jsoup.parser.Token.StartTag;
import org.jsoup.parser.Token.Text;
import org.junit.Before;
import org.junit.Test;

public class XmlTreeBuilderTest {

 private XmlTreeBuilder builder;
 private ParseErrorList errors;

 @Before
 public void setup() {
 builder = new XmlTreeBuilder();
 errors = new ParseErrorList();
 }

 @Test
 public void testParseFragment() {
 String xmlFragment = "<root><element attr1=\"val1\" attr2='val2'><subElement/><anotherSubElement/></element></root>";
 List<Node> nodes = builder.parseFragment(xmlFragment, null, errors);
 assertEquals(1, nodes.size());
 assertEquals("root", nodes.get(0).nodeName());

 Element root = (Element) nodes.get(0);
 assertEquals(1, root.childNodes().size());

 Element element = (Element) root.childNodes().get(0);
 assertEquals("element", element.nodeName());
 assertEquals(2, element.childNodes().size());

 assertEquals("attr1", element.tagName());
 assertEquals("val1", element.attribute("attr1"));
 assertEquals("val2", element.attribute("attr2"));

 Element subElement = (Element) element.childNodes().get(0);
 assertEquals("subElement", subElement.nodeName());
 assertTrue(subElement.childNodes().isEmpty());

 Element anotherSubElement = (Element) element.childNodes().get(1);
 assertEquals("anotherSubElement", anotherSubElement.nodeName());
 assertTrue(anotherSubElement.childNodes().isEmpty());
 }

 @Test
 public void testParseFragmentWithInvalidXml() {
 String invalidXmlFragment = "<root><element><invalid-tag/><anotherElement/></element>";
 List<Node> nodes = builder.parseFragment(invalidXmlFragment, null, errors);
 // The parse should fail and we should have an error message
 assertEquals(1, errors.size());
 errors.printStackTrace();

 // We should not have any nodes
 assertEquals(0, nodes.size());
 }

 @Test
 public void testParseFragmentWithEmptyInput() {
 String emptyInputFragment = "";
 List<Node> nodes = builder.parseFragment(emptyInputFragment, null, errors);
 // We should not have any nodes
 assertEquals(0, nodes.size());
 }

 @Test
 public void testProcessStartTag() {
 Token startTagToken = new StartTag("element", null, null, null, null, null, null, null, null, null, null, -1, null, null, true);
 Element element = builder.insert(startTagToken);
 assertEquals("element", element.nodeName());
 assertEquals(null, element.baseUri());
 assertEquals(0, element.childNodes().size());
 }

 @Test
 public void testProcessEndTag() {
 Token endTagToken = new EndTag("element");
 builder.popStackToClose(endTagToken);
 assertTrue(builder.stack.isEmpty());
 }

 @Test
 public void testProcessCharacter() {
 Token characterToken = new Character('a');
 Node node = builder.insert(characterToken);
 assertEquals(Text.class, node.getClass());
 assertEquals("a", node.nodeValue());
 assertEquals(null, node.baseUri());
 }

 @Test
 public void testProcessComment() {
 Token commentToken = new Comment("<!-- This is a comment -->");
 builder.insert(commentToken);
 Comment comment = (Comment) builder.doc.childNodes().get(0);
 assertEquals("This is a comment", comment.data());
 assertEquals(null, comment.baseUri());
 }

 @Test
 public void testProcessDoctype() {
 Token doctypeToken = new Doctype("html", null, null, null);
 Node node = builder.insert(doctypeToken);
 assertEquals(DocumentType.class, node.getClass());
 assertEquals("html", node.nodeName());
 assertEquals(null, node.baseUri());
 }

 @Test
 public void testProcessUnknownTag() {
 // We should not fail if we encounter an unknown tag
 Token startTagToken = new StartTag("unknown", null, null, null, null, null, null, null, null, null, null, -1, null, null, true);
 builder.insert(startTagToken);
 }

 @Test
 public void testProcessSelfClosingTag() {
 // We should remember that we have processed a self-closing tag
 Token startTagToken = new StartTag("selfClosing", null, null, null, null, null, null, null, null, null, null, -1, null, null, true);
 Element element = builder.insert(startTagToken);
 assertEquals("selfClosing", element.nodeName());
 assertEquals(0, element.childNodes().size());

 // The tag should be self-closing
 assertEquals(true, element.isSelfClosing());
 }
}