import org.junit.Before;
import org.junit.Test;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.XmlTreeBuilder;
import org.jsoup.parser.ParseSettings;
import org.jsoup.parser.ParseErrorList;
import org.jsoup.parser.Token;
import org.jsoup.parser.Token.StartTag;
import org.jsoup.parser.Token.EndTag;
import org.jsoup.parser.Token.Comment;
import org.jsoup.parser.Token.Character;
import org.jsoup.parser.Token.Doctype;
import java.io.StringReader;
import java.util.List;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class XmlTreeBuilderTest {

 private XmlTreeBuilder xmlTreeBuilder;
 private ParseSettings parseSettings;

 @Before
 public void setup() {
 xmlTreeBuilder = new XmlTreeBuilder();
 parseSettings = xmlTreeBuilder.defaultSettings();
 }

 @Test
 public void testParse() {
 String xmlInput = "<root><element attr=\"value\">Content</element></root>";
 Document doc = xmlTreeBuilder.parse(new StringReader(xmlInput), "baseUri");

 assertEquals("root", doc.tagName());
 Element root = doc.child(0);
 assertEquals("element", root.tagName());
 assertEquals("value", root.attribute("attr"));
 assertEquals("Content", root.child(0).text());
 }

 @Test
 public void testParseWithInvalidInput() {
 String invalidXmlInput = "<root<element/>";
 ParseErrorList errors = new ParseErrorList();
 Document doc = xmlTreeBuilder.parse(new StringReader(invalidXmlInput), "baseUri", errors, parseSettings);

 assertNull(doc);
 assertEquals(1, errors.size());
 assertEquals("Unexpected end tag : root", errors.get(0).getMessage());
 }

 @Test
 public void testParseFragment() {
 String xmlFragment = "<element attr=\"value\">Content</element>";
 List<Node> fragmentNodes = xmlTreeBuilder.parseFragment(xmlFragment, "baseUri", new ParseErrorList(), parseSettings);

 Element fragmentElement = (Element) fragmentNodes.get(0);
 assertEquals("element", fragmentElement.tagName());
 assertEquals("value", fragmentElement.attribute("attr"));
 assertEquals("Content", fragmentElement.child(0).text());
 }

 @Test
 public void testProcessStartTag() {
 Token startTagToken = new Token(Token.StartTag.class, "<tag attr=\"value\" />");
 Element tagElement = xmlTreeBuilder.insert(startTagToken);

 assertEquals("tag", tagElement.tagName());
 assertEquals("value", tagElement.attribute("attr"));
 assertEquals(true, tagElement.isSelfClosing());
 }

 @Test
 public void testProcessEndTag() {
 Token endTagToken = new Token(Token.EndTag.class, "</tag>");
 xmlTreeBuilder.popStackToClose(endTagToken);

 assertNull(xmlTreeBuilder.stack.peek());
 }

 @Test
 public void testProcessComment() {
 Token commentToken = new Token(Token.Comment.class, "-- This is a comment --");
 xmlTreeBuilder.insert(commentToken);

 Comment commentNode = (Comment) xmlTreeBuilder.doc.get(xmlTreeBuilder.doc.size() - 1);
 assertEquals("-- This is a comment --", commentNode.getData());
 }

 @Test
 public void testProcessCharacter() {
 Token characterToken = new Token(Token.Character.class, "Content");
 xmlTreeBuilder.insert(characterToken);

 TextNode characterNode = (TextNode) xmlTreeBuilder.doc.get(xmlTreeBuilder.doc.size() - 1);
 assertEquals("Content", characterNode.text());
 }

 @Test
 public void testProcessDoctype() {
 Token doctypeToken = new Token(Token.Doctype.class, "<!DOCTYPE tag [ <!ATTLIST tag attr CDATA #REQUIRED > ]>");
 DocumentType doctypeNode = (DocumentType) xmlTreeBuilder.insert(doctypeToken);

 assertEquals("tag", doctypeNode.nodeName());
 assertEquals("tag", doctypeNode.tagName());
 }
}