import org.junit.Before;
import org.junit.Test;
import org.jsoup.parser.XmlTreeBuilder;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Doctype;
import org.jsoup.nodes.Comment;

import java.util.Stack;

import static org.junit.Assert.*;

public class XmlTreeBuilderTest {

 private XmlTreeBuilder xmlTreeBuilder;
 private Document doc;

 @Before
 public void setUp() {
 xmlTreeBuilder = new XmlTreeBuilder();
 doc = xmlTreeBuilder.parse("<xml><tag attr='value'><subtag/><comment>This is a comment</comment></tag></xml>", null, null);
 }

 @Test
 public void testStartTag() {
 Token startTag = new Token.StartTag("tag", null);
 Element expectedElement = new Element("tag", null, null);
 Element actualElement = xmlTreeBuilder.insert(startTag);
 assertEquals(expectedElement.nodeName(), actualElement.nodeName());
 assertEquals(expectedElement.baseUri(), actualElement.baseUri());
 assertArrayEquals(expectedElement.attributes().toArray(), actualElement.attributes().toArray());
 }

 @Test
 public void testEndTag() {
 Token endTag = new Token.EndTag("tag");
 xmlTreeBuilder.popStackToClose(endTag);
 assertFalse(doc.childNodes().hasNext());
 }

 @Test
 public void testComment() {
 Token commentToken = new Token.Comment("This is a comment");
 xmlTreeBuilder.insert(commentToken);
 Comment expectedComment = new Comment("This is a comment", null);
 Comment actualComment = (Comment) doc.childNodes().next();
 assertEquals(expectedComment.data(), actualComment.data());
 }

 @Test
 public void testCharacter() {
 Token characterToken = new Token.Character('A');
 xmlTreeBuilder.insert(characterToken);
 TextNode expectedTextNode = new TextNode("A", null);
 TextNode actualTextNode = (TextNode) doc.childNodes().next();
 assertEquals(expectedTextNode.data(), actualTextNode.data());
 }

 @Test
 public void testDoctype() {
 Token doctypeToken = new Token.Doctype("xml", null, null, null);
 xmlTreeBuilder.insert(doctypeToken);
 DocumentType expectedDocumentType = new DocumentType("xml", null, null, null);
 DocumentType actualDocumentType = (DocumentType) doc.childNodes().next();
 assertEquals(expectedDocumentType.name(), actualDocumentType.name());
 }

 @Test(expected = AssertionError.class)
 public void testUnexpectedTokenType() {
 Token unexpectedToken = new Token(123); // Unexpected token type
 xmlTreeBuilder.process(unexpectedToken);
 }

 @Test
 public void testProcessSelfClosingTag() {
 Token startTag = new Token.StartTag("selfClosingTag", null);
 startTag.setSelfClosing(true);
 Element expectedElement = new Element("selfClosingTag", null, null);
 expectedElement.setSelfClosing(true);
 Element actualElement = xmlTreebuilder.insert(startTag);
 assertEquals(expectedElement.nodeName(), actualElement.nodeName());
 assertEquals(expectedElement.baseUri(), actualElement.baseUri());
 assertArrayEquals(expectedElement.attributes().toArray(), actualElement.attributes().toArray());
 }

 @Test
 public void testUnknownTag() {
 Token startTag = new Token.StartTag("unknownTag", null);
 Element expectedElement = new Element("unknownTag", null, null);
 expectedElement.setSelfClosing(true);
 Element actualElement = xmlTreebuilder.insert(startTag);
 assertEquals(expectedElement.nodeName(), actualElement.nodeName());
 assertEquals(expectedElement.baseUri(), actualElement.baseUri());
 assertArrayEquals(expectedElement.attributes().toArray(), actualElement.attributes().toArray());
 }
}