import org.junit.Test;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.parser.XmlTreeBuilder;
import org.jsoup.parser.Token;
import org.jsoup.parser.Token.Attribute;
import org.jsoup.parser.Token.Comment;
import org.jsoup.parser.Token.Doctype;
import org.jsoup.parser.Token.EndTag;
import org.jsoup.parser.Token.StartTag;
import org.jsoup.parser.Tokenizer;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class XmlTreeBuilderTest {

 @Test
 public void testInitialiseParse() {
 XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
 xmlTreeBuilder.initialiseParse("", "", null);
 assertEquals(Document.OutputSettings.Syntax.xml, xmlTreeBuilder.doc.outputSettings().syntax());
 }

 @Test
 public void testProcessStartTag() {
 XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
 Token startTag = new Token.StartTag("root", null, null);
 xmlTreeBuilder.process(startTag);
 Element root = xmlTreeBuilder.currentElement();
 assertEquals("root", root.nodeName());
 assertNotNull(root.baseUri());
 }

 @Test
 public void testProcessEndTag() {
 XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
 Element root = new Element("root", null);
 xmlTreeBuilder.insertNode(root);
 xmlTreeTreeBuilder.stack.add(root);
 Token endTag = new Token.EndTag("root");
 xmlTreeBuilder.process(endTag);
 assertNull(xmlTreeBuilder.stack.get(xmlTreeBuilder.stack.size() - 1));
 }

 @Test
 public void testProcessComment() {
 XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
 Token comment = new Token.Comment("-- This is a comment");
 xmlTreeBuilder.process(comment);
 Comment commentNode = (Comment) xmlTreeBuilder.currentNode();
 assertEquals("-- This is a comment", commentNode.data());
 }

 @Test
 public void testProcessCharacter() {
 XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
 Token character = new Token.Character('a');
 xmlTreeBuilder.process(character);
 TextNode textNode = (TextNode) xmlTreeBuilder.currentNode();
 assertEquals("a", textNode.data());
 }

 @Test
 public void testProcessDoctype() {
 XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
 Doctype doctype = new Doctype("html", "", "", null);
 xmlTreeBuilder.process(new Token.Doctype("html"));
 DocumentType documentTypeNode = (DocumentType) xmlTreeBuilder.currentNode();
 assertEquals("html", documentTypeNode.nodeName());
 assertNotNull(documentTypeNode.publicIdentifier());
 assertNull(documentTypeNode.systemIdentifier());
 }

 @Test
 public void testPopStackToClose() {
 XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
 Element root = new Element("root", null);
 xmlTreeBuilder.insertNode(root);
 xmlTreeBuilder.stack.add(root);
 xmlTreeBuilder.stack.add(new Element("child", null));
 xmlTreeBuilder.popStackToClose(new Token.EndTag("child"));
 assertEquals(root, xmlTreeBuilder.stack.get(xmlTreeBuilder.stack.size() - 1));
 }

 @Test(expected = AssertionError.class)
 public void testValidateUnexpectedTokenType() {
 XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
 Token invalidToken = new Token(-1, "Invalid token");
 xmlTreeBuilder.process(invalidToken);
 }

 @Test
 public void testParseFragment() {
 XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
 String inputFragment = "<root><child></child></root>";
 List<Node> nodes = xmlTreeBuilder.parseFragment(inputFragment, "", null);
 Element root = (Element) nodes.get(0);
 assertEquals("root", root.nodeName());
 List<Node> childNodes = root.childNodes();
 assertEquals(1, childNodes.size());
 Element child = (Element) childNodes.get(0);
 assertEquals("child", child.nodeName());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseFragmentNullInput() {
 XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
 List<Node> nodes = xmlTreeBuilder.parseFragment(null, "", null);
 }

 @Test
 public void testParseFragmentEmptyInput() {
 XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
 List<Node> nodes = xmlTreeBuilder.parseFragment("", "", null);
 assertNotNull(nodes);
 assertFalse(nodes.isEmpty());
 }

 @Test(expected = NullPointerException.class)
 public void testParseFragmentNullBaseUri() {
 XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
 List<Node> nodes = xmlTreeBuilder.parseFragment("<root></root>", null, null);
 }

 @Test
 public void testParseFragmentWithErrors() {
 XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
 ParseErrorList errors = new ParseErrorList();
 List<Node> nodes = xmlTreeBuilder.parseFragment("<root><child><x></x>", "", errors);
 assertEquals(1, errors.size());
 assertEquals("1[Unexpected char 0x3C: < at line 1 column 4 path=null]", errors.get(0).toString());
 }
}