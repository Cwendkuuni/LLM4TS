import org.junit.Assert;
import org.junit.Test;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.nodes.Comment;
import org.jsoup.nodes.DocumentType;
import org.jsoup.parser.Token;
import org.jsoup.parser.Tokenizer;
import org.jsoup.helper.Validate;
import org.jsoup.parser.XmlTreeBuilder;

public class XmlTreeBuilderTest {

 private XmlTreeBuilder xmlTreeBuilder;
 private Tokenizer tokeniser;

 @SuppressWarnings("unchecked")
 @Before
 public void setUp() {
 xmlTreeBuilder = new XmlTreeBuilder();
 tokeniser = new Tokenizer();
 }

 // Test for initialiseParse method
 @Test
 public void testInitialiseParse() {
 xmlTreeBuilder.initialiseParse("test.xml", null, null);
 Assert.assertNotNull(xmlTreeBuilder.doc);
 }

 // Test for process method with StartTag
 @Test
 public void testProcessStartTag() {
 Token startTag = new Token(Token.StartTag.class, "<tag name='test'>", 1, 1);
 xmlTreeBuilder.process(startTag);
 Element expectedElement = new Element("tag", null, null);
 Assert.assertEquals(expectedElement, xmlTreeBuilder.currentElement());
 }

 // Test for process method with EndTag
 @Test
 public void testProcessEndTag() {
 Token endTag = new Token(Token.EndTag.class, "</tag>", 1, 1);
 xmlTreeBuilder.stack.add(new Element("tag", null, null));
 xmlTreeBuilder.process(endTag);
 Assert.assertEquals(xmlTreeBuilder.stack.size(), 0);
 }

 // Test for process method with Comment
 @Test
 public void testProcessComment() {
 Token commentToken = new Token(Token.Comment.class, "-- This is a comment --", 1, 1);
 xmlTreeBuilder.process(commentToken);
 Comment expectedComment = new Comment("-- This is a comment --", null);
 Assert.assertEquals(expectedComment, xmlTreeBuilder.currentNode());
 }

 // Test for process method with Character
 @Test
 public void testProcessCharacter() {
 Token characterToken = new Token(Token.Character.class, "test", 1, 1);
 xmlTreeBuilder.process(characterToken);
 TextNode expectedTextNode = new TextNode("test", null);
 Assert.assertEquals(expectedTextNode, xmlTreeBuilder.currentNode());
 }

 // Test for process method with Doctype
 @Test
 public void testProcessDoctype() {
 Token doctypeToken = new Token(Token.Doctype.class, "<!DOCTYPE test [ ]>", 1, 1);
 xmlTreeBuilder.process(doctypeToken);
 DocumentType expectedDocumentType = new DocumentType("test", null, null, null);
 Assert.assertEquals(expectedDocumentType, xmlTreeBuilder.currentNode());
 }

 // Test for popStackToClose method with exist tag in stack
 @Test
 public void testPopStackToCloseWithExistTag() {
 xmlTreeBuilder.stack.add(new Element("tag1", null, null));
 xmlTreeBuilder.stack.add(new Element("tag2", null, null));
 xmlTreeBuilder.popStackToClose(new Token(Token.EndTag.class, "</tag1>", 1, 1));
 Assert.assertEquals(xmlTreeBuilder.stack.size(), 1);
 }

 // Test for popStackToClose method with not exist tag in stack
 @Test(expected = AssertionError.class)
 public void testPopStackToCloseWithNotExistTag() {
 xmlTreeBuilder.stack.add(new Element("tag1", null, null));
 xmlTreeBuilder.stack.add(new Element("tag2", null, null));
 xmlTreeBuilder.popStackToClose(new Token(Token.EndTag.class, "</tag3>", 1, 1));
 }
}