import org.junit.Before;
import org.junit.Test;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Doctype;
import org.jsoup.nodes.Comment;
import org.jsoup.nodes.XmlDeclaration;
import org.jsoup.parser.ParseSettings;
import org.jsoup.parser.ParseErrorList;
import org.jsoup.parser.Token;
import org.jsoup.parser.TreeBuilder;
import org.jsoup.parser.Tokenizer;
import java.util.List;
import java.util.ArrayList;

import static org.junit.Assert.*;

public class XmlTreeBuilderTest {

 private XmlTreeBuilder builder;
 private Document doc;
 private ParseSettings settings;
 private ParseErrorList errors;
 private Tokenizer tokeniser;

 @Before
 public void setUp() {
 builder = new XmlTreeBuilder();
 doc = null;
 settings = builder.defaultSettings();
 errors = new ParseErrorList();
 tokeniser = new Tokenizer();
 }

 @Test
 public void testDefaultSettings() {
 ParseSettings actual = builder.defaultSettings();
 assertEquals(ParseSettings.preserveCase, actual);
 }

 @Test
 public void testParse() {
 String xml = "<root><child></child></root>";
 doc = builder.parse(xml, null);
 assertNotNull(doc);
 assertEquals(1, doc.childNodeSize());
 Element root = doc.child(0);
 assertEquals("root", root.tagName());
 assertEquals(1, root.childNodeSize());
 Element child = root.child(0);
 assertEquals("child", child.tagName());
 assertTrue(child.isEmpty());
 }

 @Test
 public void testProcessStartTag() {
 String xml = "<root><child></child></root>";
 doc = builder.parse(xml, null);
 Stack<Element> stack = builder.stack;
 Element root = doc.child(0);
 stack.push(root);
 Token startTag = new Token.StartTag("child");
 assertFalse(stack.isEmpty());
 assertEquals(root, stack.peek());
 builder.process(startTag);
 assertEquals(root, stack.size() -1);
 Element child = doc.child(0).child(0);
 assertEquals("child", child.tagName());
 assertTrue(child.isEmpty());
 }

 @Test
 public void testProcessEndTag() {
 String xml = "<root><child></child></root>";
 doc = builder.parse(xml, null);
 Stack<Element> stack = builder.stack;
 Element root = doc.child(0);
 Element child = doc.child(0).child(0);
 stack.push(root);
 stack.push(child);
 Token endTag = new Token.EndTag("child");
 builder.process(endTag);
 assertEquals(root, stack.size() -1);
 }

 @Test(expected = AssertionError.class)
 public void testProcessUnknownTag() {
 String xml = "<root><unknownTag></unknownTag></root>";
 doc = builder.parse(xml, null);
 Stack<Element> stack = builder.stack;
 Element root = doc.child(0);
 stack.push(root);
 Token startTag = new Token.StartTag("unknownTag");
 builder.process(startTag);
 fail("Unexpected token type: " + startTag.type);
 }

 @Test
 public void testProcessComment() {
 String xml = "<!-- This is a comment -->";
 doc = builder.parse(xml, null);
 Stack<Element> stack = builder.stack;
 Comment comment = (Comment) doc.childNodes().get(0);
 assertEquals("This is a comment", comment.data());
 assertEquals(0, stack.size());
 }

 @Test
 public void testProcessCharacter() {
 String xml = "<root>abcd</root>";
 doc = builder.parse(xml, null);
 Stack<Element> stack = builder.stack;
 TextNode textNode = (TextNode) doc.childNodes().get(1);
 assertEquals("abcd", textNode.text());
 assertEquals(0, stack.size());
 }

 @Test
 public void testProcessDoctype() {
 String xml = "<!DOCTYPE root [ <!ATTLIST mytag attr CDATA #REQUIRED> ]><root></root>";
 doc = builder.parse(xml, null);
 Stack<Element> stack = builder.stack;
 DocumentType doctypeNode = (DocumentType) doc.childNodes().get(0);
 assertEquals("root", doctypeNode.nodeName());
 assertNotNull(doctypeNode.getPublicIdentifier());
 assertNotNull(doctypeNode.getSystemIdentifier());
 assertEquals(0, stack.size());
 }

 @Test
 public void testPopStackToClose() {
 String xml = "<root><child><grandchild></grandchild></child></root>";
 doc = builder.parse(xml, null);
 Stack<Element> stack = builder.stack;
 Element grandchild = doc.child(0).child(0).child(0);
 builder.popStackToClose(new Token.EndTag("grandchild"));
 assertEquals(doc.child(0).child(0).size() -1, stack.size());
 }

 @Test(expected = IllegalStateException.class)
 public void testPopStackToCloseUnknownTag() {
 String xml = "<root><child><grandchild></grandchild></child></root>";
 doc = builder.parse(xml, null);
 Stack<Element> stack = builder.stack;
 builder.popStackToClose(new Token.EndTag("unknownTag"));
 }

 @Test
 public void testParseFragment() {
 String xml = "<root><child></child></root>";
 List<Node> nodes = builder.parseFragment(xml, null, errors, settings);
 assertEquals(2, nodes.size());
 assertEquals(Element.class, nodes.get(0).getClass());
 assertEquals(Element.class, nodes.get(1).getClass());
 }

 @Test
 public void testParseFragmentWithError() {
 String xml = "<root><child><grandchild></grandchild></child></root>";
 ParseErrorList errors = new ParseErrorList();
 List<Node> nodes = builder.parseFragment("<root><child><grandchild>", null, errors, settings);
 assertEquals(1, nodes.size());
 assertEquals(Element.class, nodes.get(0).getClass());
 assertTrue(errors.size() > 0);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseFragmentNullInput() {
 List<Node> nodes = builder.parseFragment(null, null, errors, settings);
 }
}