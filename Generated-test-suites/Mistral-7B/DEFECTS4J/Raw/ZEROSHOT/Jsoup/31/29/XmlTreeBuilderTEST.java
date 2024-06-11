import org.junit.After;
import org.junit.Before;
import org.jsoup.parser.XmlTreeBuilder;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.nodes.DocumentType;
import org.jsoup.nodes.Comment;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Token;
import org.jsoup.nodes.Token.StartTag;
import org.jsoup.nodes.Token.EndTag;
import org.jsoup.nodes.Token.Character;
import org.jsoup.nodes.Token.Doctype;
import org.junit.Assert;

import java.util.Iterator;
import java.util.Stack;

public class XmlTreeBuilderTest {

 private XmlTreeBuilder xmlTreeBuilder;
 private Document doc;

 @Before
 public void setUp() {
 xmlTreeBuilder = new XmlTreeBuilder();
 doc = xmlTreeBuilder.parse("<root><element1><element2></element2></element1></root>");
 }

 @After
 public void tearDown() {
 xmlTreeBuilder = null;
 doc = null;
 }

 @org.junit.Test
 public void testInitialiseParse() {
 Stack<Element> stack = xmlTreeBuilder.stack;
 Element rootElement = doc.child(0);
 Assert.assertEquals("root", rootElement.nodeName());
 Assert.assertEquals(xmlTreeBuilder.doc, stack.peek());
 }

 @org.junit.Test
 public void testProcessStartTag() {
 StartTag startTag = new StartTag("element1", null);
 Element expectedElement = new Element("element1", null);
 Element actualElement = xmlTreeBuilder.insert(startTag);
 Assert.assertEquals(expectedElement, actualElement);
 }

 @org.junit.Test
 public void testProcessSelfClosingStartTag() {
 StartTag startTag = new StartTag("tagName", null).setSelfClosing(true);
 Element expectedElement = new Element("tagName", null);
 Element actualElement = xmlTreeBuilder.insert(startTag);
 Assert.assertEquals(expectedElement, actualElement);
 }

 @org.junit.Test
 public void testProcessEndTag() {
 String tagName = "element1";
 Element parentElement = doc.child(0);
 Token endTagToken = new Token(Token.Type.EndTag, tagName, null);
 xmlTreeBuilder.popStackToClose(endTagToken.asEndTag());
 Assert.assertEquals(0, parentElement.childSize());
 }

 @org.junit.Test(expected = AssertionError.class)
 public void testProcessUnknownTokenType() {
 Token unknownToken = new Token(123, null, null);
 xmlTreeBuilder.process(unknownToken);
 }

 @org.junit.Test
 public void testProcessComment() {
 String commentText = "test comment";
 Comment expectedComment = new Comment(commentText, null);
 Comment actualComment = xmlTreeBuilder.insert(new Token.Comment(commentText));
 Assert.assertEquals(expectedComment, actualComment);
 }

 @org.junit.Test
 public void testProcessCharacter() {
 String characterText = "test character";
 TextNode expectedCharacterNode = new TextNode(characterText, null);
 TextNode actualCharacterNode = (TextNode) doc.child(2);
 xmlTreeBuilder.insert(new Token.Character(characterText));
 Assert.assertEquals(expectedCharacterNode.text(), actualCharacterNode.text());
 }

 @org.junit.Test
 public void testProcessDoctype() {
 String doctypeName = "html";
 String publicIdentifier = "public id";
 String systemIdentifier = "system id";
 DocumentType expectedDoctypeNode = new DocumentType(doctypeName, publicIdentifier, systemIdentifier, null);
 DocumentType actualDoctypeNode = (DocumentType) doc.child(0);
 Token doctypeToken = new Token.Doctype(doctypeName, publicIdentifier, systemIdentifier);
 xmlTreeBuilder.insert(doctypeToken);
 Assert.assertEquals(expectedDoctypeNode.name(), actualDoctypeNode.name());
 Assert.assertEquals(expectedDoctypeNode.publicId(), actualDoctypeNode.publicId());
 Assert.assertEquals(expectedDoctypeNode.systemId(), actualDoctypeNode.systemId());
 }
}