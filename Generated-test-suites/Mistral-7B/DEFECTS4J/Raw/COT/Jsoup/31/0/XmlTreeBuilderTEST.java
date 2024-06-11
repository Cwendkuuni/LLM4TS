import org.junit.Before;
import org.junit.Test;
import org.jsoup.nodes.*;
import org.jsoup.parser.XmlTreeBuilder;
import java.util.Stack;
import static org.junit.Assert.*;

public class XmlTreeBuilderTest {

 private XmlTreeBuilder xmlTreeBuilder;
 private Document doc;

 @Before
 public void setup() {
 xmlTreeBuilder = new XmlTreeBuilder();
 doc = new Document();
 xmlTreeBuilder.initialiseParse("", "", new ParseErrorList());
 xmlTreeBuilder.stack = new Stack<>();
 xmlTreeBuilder.stack.push(doc);
 }

 // Test for process method with different token types
 @Test
 public void testProcessDifferentTokenTypes() {
 Token startTag = new Token(Token.StartTag.class, "<tag>", 0, 0);
 Token endTag = new Token(Token.EndTag.class, "</tag>", 0, 0);
 Token comment = new Token(Token.Comment.class, "--comment--", 0, 0);
 Token character = new Token(Token.Character.class, "text", 0, 0);
 Token doctype = new Token(Token.Doctype.class, "<!DOCTYPE doc [ ... ]>", 0, 0);
 Token eof = new Token(Token.EOF.class, "", 0, 0);

 xmlTreeBuilder.process(startTag);
 assertTrue(xmlTreeBuilder.stack.peek() instanceof Element);

 xmlTreeBuilder.process(endTag);
 assertEquals(xmlTreeBuilder.stack.size(), 1);

 xmlTreeBuilder.process(comment);
 assertEquals(xmlTreeBuilder.stack.peek().nodeName(), "comment");

 xmlTreeBuilder.process(character);
 assertEquals(xmlTreeBuilder.stack.peek().nodeName(), "#text");

 xmlTreeBuilder.process(doctype);
 assertEquals(xmlTreeBuilder.stack.peek().nodeName(), "DocumentType");

 xmlTreeBuilder.process(eof);
 assertTrue(xmlTreeBuilder.stack.isEmpty());
 }

 // Test for insertNode method
 @Test
 public void testInsertNode() {
 Element root = new Element("root", "");
 xmlTreeBuilder.insertNode(root);
 assertEquals(xmlTreeBuilder.stack.peek(), root);
 }

 // Test for insert method with StartTag
 @Test
 public void testInsertStartTag() {
 Token startTag = new Token(Token.StartTag.class, "<tag attr='val' />", 0, 0);
 Element expected = new Element("tag", "", new Attributes() {{
 put("attr", "val");
 }});
 Element actual = xmlTreeBuilder.insert(startTag);
 assertEquals(actual, expected);
 }

 // Test for insert method with EndTag
 @Test(expected = IllegalArgumentException.class)
 public void testInsertEndTag() {
 Token endTag = new Token(Token.EndTag.class, "</tag>", 0, 0);
 xmlTreeBuilder.insert(endTag);
 }

 // Test for insert method with Comment
 @Test
 public void testInsertComment() {
 Token comment = new Token(Token.Comment.class, "--comment--", 0, 0);
 Comment expected = new Comment("--comment--", "");
 Comment actual = (Comment) xmlTreeBuilder.insert(comment);
 assertEquals(actual.data(), expected.data());
 }

 // Test for insert method with Character
 @Test
 public void testInsertCharacter() {
 Token character = new Token(Token.Character.class, "text", 0, 0);
 TextNode expected = new TextNode("text", "");
 TextNode actual = (TextNode) xmlTreeBuilder.insert(character);
 assertEquals(actual.data(), expected.data());
 }

 // Test for insert method with Doctype
 @Test
 public void testInsertDoctype() {
 Token doctype = new Token(Token.Doctype.class, "<!DOCTYPE doc [ ... ]>", 0, 0);
 DocumentType expected = new DocumentType("doc", "", "", "");
 DocumentType actual = (DocumentType) xmlTreeBuilder.insert(doctype);
 assertEquals(actual.name(), expected.name());
 }

 // Test for popStackToClose method with unknown tag
 @Test
 public void testPopStackToCloseUnknownTag() {
 xmlTreeBuilder.stack.add(new Element("tag1"));
 xmlTreeBuilder.stack.add(new Element("tag2"));
 xmlTreeBuilder.stack.add(new Element("tag3"));
 xmlTreebuilder.popStackToClose(new Token.EndTag("tag1"));
 assertEquals(xmlTreeBuilder.stack.size(), 2);
 }

 // Test for popStackToClose method with known tag
 @Test
 public void testPopStackToCloseKnownTag() {
 xmlTreeBuilder.stack.add(new Element("tag1"));
 xmlTreeBuilder.stack.add(new Element("tag2"));
 xmlTreeBuilder.stack.add(new Element("tag1"));
 xmlTreebuilder.popStackToClose(new Token.EndTag("tag1"));
 assertEquals(xmlTreeBuilder.stack.size(), 1);
 }

 // Test for popStackToClose method with no matching tag
 @Test
 public void testPopStackToCloseNoMatchingTag() {
 xmlTreeBuilder.stack.add(new Element("tag1"));
 xmlTreeBuilder.stack.add(new Element("tag2"));
 xmlTreeBuilder.popStackToClose(new Token.EndTag("tag3"));
 assertEquals(xmlTreeBuilder.stack.size(), 2);
 }
}