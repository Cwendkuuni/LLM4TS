import org.junit.Before;
import org.junit.Test;
import org.jsoup.parser.XmlTreeBuilder;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.nodes.Doctype;
import org.jsoup.nodes.Comment;
import org.jsoup.nodes.Attributes;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class XmlTreeBuilderTest {

 private XmlTreeBuilder xmlTreeBuilder;
 private Document doc;

 @Before
 public void setUp() {
 xmlTreeBuilder = new XmlTreeBuilder();
 doc = xmlTreeBuilder.newDocument();
 }

 @Test
 public void testStartTag() {
 // Test valid start tag
 Token startTagToken = new Token.StartTag("root", null, null);
 Element rootElement = xmlTreeBuilder.insert(startTagToken);
 assertEquals("root", rootElement.nodeName());
 assertNotNull(rootElement.attributes());
 assertEquals(0, rootElement.childNodes().size());

 // Test invalid start tag
 Token invalidStartTagToken = new Token.StartTag("", null, null);
 xmlTreeBuilder.insert(invalidStartTagToken); // Should not reach here as validate fails before insertion
 }

 @Test
 public void testEndTag() {
 // Test valid end tag
 Token endTagToken = new Token.EndTag("root");
 xmlTreeBuilder.popStackToClose(endTagToken);
 assertNull(xmlTreeBuilder.stack.peek());

 // Test invalid end tag
 Token invalidEndTagToken = new Token.EndTag("invalidTag");
 xmlTreeBuilder.popStackToClose(invalidEndTagToken); // Should not reach here as validate fails before insertion
 }

 @Test
 public void testComment() {
 // Test valid comment
 Token commentToken = new Token.Comment("This is a comment.");
 xmlTreeBuilder.insert(commentToken);
 Comment comment = (Comment) xmlTreeBuilder.stack.peek();
 assertEquals("This is a comment.", comment.data());

 // Test empty comment
 Token emptyCommentToken = new Token.Comment("");
 xmlTreeBuilder.insert(emptyCommentToken);
 assertNotNull(xmlTreeBuilder.stack.peek());
 }

 @Test
 public void testCharacter() {
 // Test valid character
 Token characterToken = new Token.Character('A');
 xmlTreeBuilder.insert(characterToken);
 TextNode characterNode = (TextNode) xmlTreeBuilder.stack.peek();
 assertEquals("#text", characterNode.nodeName());
 assertEquals('A', characterNode.text().charAt(0));

 // Test empty character
 Token emptyCharacterToken = new Token.Character(' ');
 xmlTreeBuilder.insert(emptyCharacterToken);
 TextNode emptyCharacterNode = (TextNode) xmlTreeBuilder.stack.peek();
 assertEquals("#text", emptyCharacterNode.nodeName());
 assertEquals("", emptyCharacterNode.text().toString());
 }

 @Test
 public void testDoctype() {
 // Test valid doctype
 Token doctypeToken = new Token.Doctype("html", null, null, null);
 xmlTreeBuilder.insert(doctypeToken);
 DocumentType doctypeNode = (DocumentType) xmlTreeBuilder.stack.peek();
 assertEquals("html", doctypeNode.name());
 assertNull(doctypeNode.publicIdentifier());
 assertNull(doctypeNode.systemIdentifier());

 // Test invalid doctype
 Token invalidDoctypeToken = new Token.Doctype("", null, null, null);
 xmlTreeBuilder.insert(invalidDoctypeToken); // Should not reach here as validate fails before insertion
 }

 @Test
 public void testProcessToken() {
 // Test process valid start tag
 Token startTagToken = new Token.StartTag("root", null, null);
 xmlTreeBuilder.process(startTagToken);
 Element rootElement = (Element) xmlTreeBuilder.stack.peek();
 assertEquals("root", rootElement.nodeName());
 assertNotNull(rootElement.attributes());
 assertEquals(0, rootElement.childNodes().size());

 // Test process invalid start tag
 Token invalidStartTagToken = new Token.StartTag("", null, null);
 xmlTreeBuilder.process(invalidStartTagToken); // Should not reach here as validate fails before insertion

 // Test process end tag
 Token endTagToken = new Token.EndTag("root");
 xmlTreeBuilder.process(endTagToken);
 assertNull(xmlTreeBuilder.stack.peek());

 // Test process empty character
 Token emptyCharacterToken = new Token.Character(' ');
 xmlTreeBuilder.process(emptyCharacterToken);
 TextNode emptyCharacterNode = (TextNode) xmlTreeBuilder.stack.peek();
 assertEquals("#text", emptyCharacterNode.nodeName());
 assertEquals("", emptyCharacterNode.text().toString());
 }

 @Test
 public void testPopStackToClose() {
 // Test pop valid element
 Token startTagToken = new Token.StartTag("root", null, null);
 Element rootElement = xmlTreeBuilder.insert(startTagToken);
 Token endTagToken = new Token.EndTag("root");
 xmlTreeBuilder.popStackToClose(endTagToken);
 assertNull(xmlTreeTreeBuilder.stack.peek());

 // Test pop invalid element
 Token invalidEndTagToken = new Token.EndTag("invalidTag");
 xmlTreeBuilder.popStackToClose(invalidEndTagToken); // Should not reach here as validate fails before insertion
 }
}