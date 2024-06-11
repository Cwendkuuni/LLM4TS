import org.junit.Test;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.parser.XmlTreeBuilder;
import org.jsoup.parse.DocumentStart;
import org.jsoup.parse.ElementStart;
import org.jsoup.parse.EndTag;
import org.jsoup.parse.Comment;
import org.jsoup.parse.Character;
import org.jsoup.parse.Doctype;
import org.jsoup.nodes.DocumentType;
import org.jsoup.helper.Validate;
import java.util.ArrayList;
import java.util.List;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class XmlTreeBuilderTest {

 private XmlTreeBuilder xmlTreeBuilder;
 private Document doc;

 @Before
 public void setup() {
 xmlTreeBuilder = new XmlTreeBuilder();
 doc = Jsoup.parse("", XmlTreeBuilder.class.getCanonicalName());
 }

 @Test
 public void testInitialiseParse() {
 xmlTreeBuilder.initialiseParse("", "", new ParseErrorList());
 assertEquals(XmlTreeBuilder.class.getCanonicalName(), doc.outputSettings().syntax().name());
 assertEquals(doc, xmlTreeBuilder.stack.get(0));
 }

 @Test
 public void testProcessStartTag() {
 Token startTag = new ElementStart("tag", null);
 xmlTreeBuilder.process(startTag);
 Element element = new Element("tag");
 assertEquals(element, xmlTreeBuilder.currentElement());
 }

 @Test
 public void testProcessEndTag() {
 Token endTag = new EndTag("tag");
 xmlTreeBuilder.process(endTag);
 Element element = (Element) xmlTreeBuilder.stack.get(xmlTreeBuilder.stack.size() - 1);
 assertEquals("tag", element.nodeName());
 xmlTreeBuilder.stack.remove(xmlTreeBuilder.stack.size() - 1);
 assertNotNull(xmlTreeBuilder.stack.size() > 0);
 }

 @Test
 public void testProcessComment() {
 Token commentToken = new Comment("#comment");
 xmlTreeBuilder.process(commentToken);
 Comment comment = new Comment("#comment");
 assertEquals(comment, xmlTreeBuilder.lastAddedNode());
 }

 @Test
 public void testProcessCharacter() {
 Token characterToken = new Character('c');
 xmlTreeBuilder.process(characterToken);
 TextNode textNode = new TextNode("c", "");
 assertEquals(textNode, xmlTreeBuilder.lastAddedNode());
 }

 @Test
 public void testProcessDoctype() {
 Token doctypeToken = new Doctype("doctype", null, null, null);
 xmlTreeBuilder.process(doctypeToken);
 DocumentType documentTypeNode = new DocumentType("doctype", null, null, "");
 assertEquals(documentTypeNode, xmlTreeBuilder.lastAddedNode());
 }

 @Test(expected = AssertionError.class)
 public void testUnexpectedTokenType() {
 Token unexpectedToken = new Token(1);
 xmlTreeBuilder.process(unexpectedToken);
 }

 @Test
 public void testInsertNode() {
 Element element = new Element("tag");
 xmlTreeBuilder.insertNode(element);
 Element expectedElement = (Element) xmlTreeBuilder.currentElement();
 assertEquals(element, expectedElement);
 }

 @Test
 public void testPopStackToClose() {
 Element parentElement = new Element("parent");
 Element childElement = new Element("child");
 xmlTreeBuilder.stack.add(parentElement);
 xmlTreeBuilder.stack.add(childElement);
 xmlTreeBuilder.popStackToClose(new EndTag("child"));
 assertEquals(parentElement, xmlTreeBuilder.currentElement());
 }

 @Test
 public void testParseFragment() {
 String inputFragment = "<tag><child/><comment>!DOCTYPE html SYSTEM \"about:legacy-compat\"/></tag>";
 List<Node> nodes = xmlTreeBuilder.parseFragment(inputFragment, "", new ParseErrorList());
 Element tag = (Element) nodes.get(0);
 Element child = (Element) tag.children().get(0);
 Comment comment = (Comment) tag.children().get(1);
 assertEquals(3, nodes.size());
 assertEquals("tag", tag.nodeName());
 assertTrue(child.nodeName().equals("child"));
 assertEquals("#comment", comment.data());
 }

 @Test(expected = AssertionError.class)
 public void testInvalidCommentProcessing() {
 String inputFragment = "<!--comment-->";
 xmlTreeBuilder.parseFragment(inputFragment, "", new ParseErrorList());
 Validate.fail("Should have thrown an AssertionError but didn't");
 }
}