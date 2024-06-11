import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.nodes.attributes.Attributes;
import org.jsoup.parser.Token;
import org.jsoup.parser.Token.Comment;
import org.jsoup.parser.Token.Character;
import org.jsoup.parser.Token.Doctype;
import org.jsoup.parser.Token.EndTag;
import org.jsoup.parser.Token.StartTag;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Stack;

public class XmlTreeBuilderTest {

 private XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
 private Document doc = new Document();

 @Test
 public void testInitializeParse() {
 Document expectedDoc = new Document();
 XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
 xmlTreeBuilder.initialiseParse("input", "baseUri", new ParseErrorList());
 xmlTreeBuilder.stack.push(expectedDoc);

 Assert.assertEquals(expectedDoc, xmlTreeBuilder.doc);
 }

 @Test
 public void testProcessStartTag() {
 Token startTagToken = new Token(StartTag.class, "tagName", null, null, null, null, null, 0, 0);
 Element expectedElement = new Element("tagName", "baseUri", new Attributes());
 XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
 xmlTreeBuilder.initialiseParse("input", "baseUri", new ParseErrorList());
 xmlTreeBuilder.stack.push(xmlTreeBuilder.doc);
 xmlTreeBuilder.process(startTagToken);

 Element actualElement = xmlTreeBuilder.stack.peek();
 Assert.assertEquals(expectedElement, actualElement);
 }

 @Test
 public void testProcessEndTag() {
 Token endTagToken = new Token(EndTag.class, "tagName", null, null, null, null, null, 0, 0);
 XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
 xmlTreeBuilder.initialiseParse("input", "baseUri", new ParseErrorList());
 xmlTreeBuilder.stack.push(xmlTreeBuilder.doc);
 xmlTreeBuilder.stack.push(new Element("tagName", "baseUri", new Attributes()));
 xmlTreeBuilder.process(endTagToken);

 Assert.assertEquals(xmlTreeBuilder.doc, xmlTreeBuilder.stack.peek());
 }

 @Test
 public void testProcessComment() {
 Token commentToken = new Token(Comment.class, "commentData", null, "baseUri", null, null, null, 0, 0);
 XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
 xmlTreeBuilder.initialiseParse("input", "baseUri", new ParseErrorList());
 xmlTreeBuilder.process(commentToken);

 Comment expectedComment = new Comment("commentData", "baseUri");
 Comment actualComment = (Comment) xmlTreeBuilder.stack.peek();
 Assert.assertEquals(expectedComment, actualComment);
 }

 @Test
 public void testProcessCharacter() {
 Token characterToken = new Token(Character.class, "characterData", null, "baseUri", null, null, null, 0, 0);
 XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
 xmlTreeBuilder.initialiseParse("input", "baseUri", new ParseErrorList());
 xmlTreeBuilder.process(characterToken);

 TextNode expectedTextNode = new TextNode("characterData", "baseUri");
 TextNode actualTextNode = (TextNode) xmlTreeBuilder.stack.peek();
 Assert.assertEquals(expectedTextNode, actualTextNode);
 }

 @Test
 public void testProcessDoctype() {
 Token doctypeToken = new Token(Doctype.class, null, "publicIdentifier", "systemIdentifier", null, null, null, 0, 0);
 XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
 xmlTreeBuilder.initialiseParse("input", "baseUri", new ParseErrorList());
 xmlTreeBuilder.process(doctypeToken);

 DocumentType expectedDocumentType = new DocumentType("doctypeName", "publicIdentifier", "systemIdentifier", "baseUri");
 DocumentType actualDocumentType = (DocumentType) xmlTreeBuilder.stack.peek();
 Assert.assertEquals(expectedDocumentType, actualDocumentType);
 }

 @Test
 public void testPopStackToClose() {
 XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
 xmlTreeBuilder.initialiseParse("input", "baseUri", new ParseErrorList());
 xmlTreeBuilder.stack.push(xmlTreeBuilder.doc);
 xmlTreeTreeBuilder.stack.push(new Element("tag1", "baseUri", new Attributes()));
 xmlTreeBuilder.stack.push(new Element("tag2", "baseUri", new Attributes()));
 xmlTreeBuilder.stack.push(new Element("tag1", "baseUri", new Attributes()));
 xmlTreeBuilder.popStackToClose(new Token(EndTag.class, "tag1", null, null, null, null, null, 0, 0));

 Assert.assertEquals(xmlTreeBuilder.doc, xmlTreeBuilder.stack.peek());
 }

 @Test(expected = AssertionError.class)
 public void testUnexpectedTokenType() {
 XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
 xmlTreeBuilder.initialiseParse("input", "baseUri", new ParseErrorList());
 xmlTreeBuilder.process(new Token(123, null, null, null, null, null, null, 0, 0));
 }

 @Test
 public void testProcessEOF() {
 XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
 xmlTreeBuilder.initialiseParse("input", "baseUri", new ParseErrorList());
 // Process all tokens until EOF is reached
 List<Token> tokenList = new ArrayList<>();
 while (xmlTreeBuilder.nextToken() != Token.EOF) {
 tokenList.add(xmlTreeBuilder.getLastParsedToken());
 }

 Assert.assertEquals(Token.EOF, xmlTreeBuilder.nextToken());
 Assert.assertEquals(xmlTreeBuilder.doc, xmlTreeBuilder.stack.peek());
 }
}