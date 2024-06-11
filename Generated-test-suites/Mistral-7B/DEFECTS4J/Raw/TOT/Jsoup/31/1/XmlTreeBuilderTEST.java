import org.junit.Before;
import org.junit.Test;
import org.jsoup.parser.XmlTreeBuilder;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.nodes.Doctype;
import org.jsoup.nodes.Comment;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Token;
import org.jsoup.helper.Validate;
import org.jsoup.helper.ElementFilter;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class XmlTreeBuilderTest {

 private XmlTreeBuilder xmlTreeBuilder;
 private Document doc;

 @Before
 public void setUp() {
 xmlTreeBuilder = new XmlTreeBuilder();
 doc = xmlTreeBuilder.parse("<root><tag attr='value'><subtag/><comment>Test Comment</comment></tag></root>");
 }

 @Test
 public void testStartTag() {
 // Test valid start tag
 Token startTag = new Token( Token.StartTag.class, "<tag", new Attributes() );
 Element element = xmlTreeBuilder.insert(startTag);
 assertEquals("tag", element.nodeName());
 assertEquals("value", element.attr("attr"));

 // Test unknown start tag
 startTag = new Token( Token.StartTag.class, "<unknown>", new Attributes() );
 xmlTreeBuilder.insert(startTag);
 assertNull(xmlTreeBuilder.getCurrentElement());
 }

 @Test
 public void testEndTag() {
 // Test valid end tag
 Token endTag = new Token( Token.EndTag.class, "</tag>" );
 xmlTreeBuilder.popStackToClose(endTag);
 assertNull(xmlTreeBuilder.getCurrentElement());

 // Test non-existent end tag
 endTag = new Token( Token.EndTag.class, "</subtag>" );
 xmlTreeBuilder.popStackToClose(endTag);
 assertNull(xmlTreeBuilder.getCurrentElement());
 }

 @Test
 public void testComment() {
 // Test valid comment
 Token commentToken = new Token( Token.Comment.class, "-- Test Comment --" );
 xmlTreeBuilder.insert(commentToken);
 Element comment = doc.child(1);
 assertEquals("Test Comment", comment.text());

 // Test empty comment
 commentToken = new Token( Token.Comment.class, "--" );
 xmlTreeBuilder.insert(commentToken);
 assertNull(xmlTreeBuilder.getCurrentElement());
 }

 @Test
 public void testCharacter() {
 // Test valid character
 Token characterToken = new Token( Token.Character.class, "Test Character" );
 xmlTreeBuilder.insert(characterToken);
 Element character = doc.child(1);
 assertEquals("Test Character", character.text());

 // Test empty character
 characterToken = new Token( Token.Character.class, "" );
 xmlTreeBuilder.insert(characterToken);
 assertNull(xmlTreeBuilder.getCurrentElement());
 }

 @Test
 public void testDoctype() {
 // Test valid doctype
 Token doctypeToken = new Token( Token.Doctype.class, "<!DOCTYPE root SYSTEM \"test.dtd\">" );
 xmlTreeBuilder.insert(doctypeToken);
 DocumentType doctype = doc.child(0);
 assertEquals("root", doctype.name());
 assertEquals("test.dtd", doctype.systemIdentifier());

 // Test invalid doctype
 doctypeToken = new Token( Token.Doctype.class, "<!DOCTYPE root PUBLIC \"test.dtd\" \"test.dtd\">" );
 xmlTreeBuilder.insert(doctypeToken);
 assertNull(xmlTreeBuilder.getCurrentElement());
 }

 @Test
 public void testSelfClosingTag() {
 // Test valid self-closing tag
 Token startTag = new Token( Token.StartTag.class, "<tag/>" );
 Element element = xmlTreeBuilder.insert(startTag);
 assertEquals("tag", element.nodeName());
 assertNull(element.childNodes().iterator().next());

 // Test invalid self-closing tag
 startTag = new Token( Token.StartTag.class, "<unknown/>" );
 xmlTreeBuilder.insert(startTag);
 assertNull(xmlTreeBuilder.getCurrentElement());
 }

 @Test
 public void testProcessStartTagWithChildren() {
 // Test valid start tag with children
 Element parent = doc.getElementsByTag("root").first();
 Token startTag = new Token( Token.StartTag.class, "<tag attr='value'><subtag/><comment>Test Comment</comment></tag>", new Attributes() );
 Element child = xmlTreeBuilder.insert(startTag);
 parent.appendChild(child);
 assertEquals(2, parent.childNodes().size());
 assertEquals("tag", child.nodeName());
 assertEquals("value", child.attr("attr"));
 assertEquals("Test Comment", parent.child(1).text());
 }

 @Test
 public void testProcessEndTagWithEmptyStack() {
 // Test end tag with empty stack
 Token endTag = new Token( Token.EndTag.class, "</tag>" );
 xmlTreeBuilder.popStackToClose(endTag);
 assertNull(xmlTreeBuilder.getCurrentElement());
 }

 @Test(expected = AssertionError.class)
 public void testInvalidTokenType() {
 // Test invalid token type
 Token invalidToken = new Token( 123, "" );
 xmlTreeBuilder.process(invalidToken);
 }
}