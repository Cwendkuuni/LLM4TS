import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.parser.Token;
import org.jsoup.parser.Token.Character;
import org.jsoup.parser.Token.Comment;
import org.jsoup.parser.Token.Doctype;
import org.jsoup.parser.Token.EndTag;
import org.jsoup.parser.Token.StartTag;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class XmlTreeBuilderTest {

 private Document doc;

 @org.junit.Before
 public void setUp() {
 doc = new XmlTreeBuilder().parse("<root><tag attr='value' /><comment>test</comment></root>");
 }

 // Testing the overridden method in XmlTreeBuilder class
 @Test
 public void testInitialiseParse() {
 assertNotNull(doc);
 }

 // Testing the process method with StartTag
 @Test
 public void testProcessStartTag() {
 Token startTag = new Token(StartTag.class, "<tag", null);
 XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
 xmlTreeBuilder.process(startTag);
 Element expectedElement = new Element("tag", null, null);
 assertEquals(expectedElement, xmlTreeBuilder.stack.peek());
 }

 // Testing the process method with EndTag
 @Test
 public void testProcessEndTag() {
 Token endTag = new Token(EndTag.class, "</tag>", null);
 XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
 Element rootElement = new Element("root", null, null);
 xmlTreeBuilder.stack.push(rootElement);
 xmlTreeBuilder.process(endTag);
 assertEquals(null, xmlTreeBuilder.stack.peek());
 }

 // Testing the process method with Comment
 @Test
 public void testProcessComment() {
 Token comment = new Token(Comment.class, "", null);
 XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
 xmlTreeBuilder.process(comment);
 Comment expectedComment = new Comment("", null);
 assertEquals(expectedComment, xmlTreeBuilder.stack.peek());
 }

 // Testing the process method with Character
 @Test
 public void testProcessCharacter() {
 Token character = new Token(Character.class, "a", null);
 XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
 TextNode expectedTextNode = new TextNode("a", null);
 xmlTreeBuilder.process(character);
 assertEquals(expectedTextNode, xmlTreeBuilder.stack.peek());
 }

 // Testing the process method with Doctype
 @Test
 public void testProcessDoctype() {
 Token doctype = new Token(Doctype.class, "<!DOCTYPE root [", null);
 XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
 DocumentType expectedDocumentType = new DocumentType("root", null, null, null);
 xmlTreeBuilder.process(doctype);
 assertEquals(expectedDocumentType, xmlTreeBuilder.stack.peek());
 }

 // Testing the edge case for popStackToClose method when the tag is not found in the stack
 @Test(expected = AssertionError.class)
 public void testPopStackToCloseWhenTagNotFound() {
 XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
 Token endTag = new Token(EndTag.class, "</tag>", null);
 xmlTreeBuilder.popStackToClose(endTag);
 }

 // Testing the edge case for popStackToClose method when the stack is empty
 @Test(expected = AssertionError.class)
 public void testPopStackToCloseWhenStackIsEmpty() {
 XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
 xmlTreeBuilder.popStackToClose(new Token(EndTag.class, "</tag>", null));
 }
}