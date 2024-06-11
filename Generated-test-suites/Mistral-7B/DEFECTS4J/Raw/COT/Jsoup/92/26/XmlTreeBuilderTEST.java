import org.junit.Assert;
import org.junit.Test;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.parser.XmlTreeBuilder;
import org.jsoup.parser.Parser;
import org.jsoup.parser.Token;
import org.jsoup.parser.Token.StartTag;
import org.jsoup.parser.Token.Comment;
import org.jsoup.parser.Token.Character;
import org.jsoup.parser.Token.Doctype;
import org.jsoup.parser.Token.EndTag;
import java.io.StringReader;
import java.util.List;

public class XmlTreeBuilderTest {

 private XmlTreeBuilder xmlTreeBuilder;
 private Parser parser;

 @Before
 public void setup() {
 xmlTreeBuilder = new XmlTreeBuilder();
 parser = new Parser(xmlTreeBuilder);
 }

 @Test
 public void testDefaultSettings() {
 Document doc = xmlTreeBuilder.defaultSettings().parse(new StringReader(""), "");
 Assert.assertEquals(xmlTreeBuilder.defaultSettings(), doc.outputSettings());
 }

 @Test
 public void testInitialiseParse() {
 Document doc = xmlTreeBuilder.parse(new StringReader("<xml></xml>"), "");
 Assert.assertEquals(xmlTreeBuilder, doc.parser());
 Assert.assertEquals(XmlTreeBuilder.class, doc.parser().getClass());
 }

 @Test
 public void testParse() {
 Document doc = xmlTreeBuilder.parse(new StringReader("<xml><a><b/><c/></a></xml>"), "");
 Element root = doc.getRootElement();
 Assert.assertEquals("xml", root.nodeName());
 Element a = root.getChild(0);
 Assert.assertEquals("a", a.nodeName());
 List<Node> children = a.children();
 Assert.assertEquals(2, children.size());
 Element b = (Element) children.get(0);
 Assert.assertEquals("b", b.nodeName());
 Assert.assertEquals(0, b.children().size());
 Element c = (Element) children.get(1);
 Assert.assertEquals("c", c.nodeName());
 Assert.assertEquals(0, c.children().size());
 }

 @Test
 public void testProcessStartTag() {
 Token startTagToken = new StartTag("a");
 xmlTreeBuilder.insert(startTagToken);
 Element a = xmlTreeBuilder.getCurrent();
 Assert.assertEquals("a", a.nodeName());
 }

 @Test
 public void testProcessEndTag() {
 Token endTagToken = new EndTag("a");
 xmlTreeBuilder.popStackToClose(endTagToken);
 Assert.assertEquals(xmlTreeBuilder.getCurrent(), null);
 }

 @Test
 public void testProcessComment() {
 Token commentToken = new Comment("<!-- comment -->");
 xmlTreeBuilder.insert(commentToken);
 Comment comment = (Comment) xmlTreeBuilder.getCurrent();
 Assert.assertEquals("comment", comment.text());
 }

 @Test
 public void testProcessCharacter() {
 Token characterToken = new Character('a');
 xmlTreeBuilder.insert(characterToken);
 TextNode textNode = (TextNode) xmlTreeBuilder.getCurrent();
 Assert.assertEquals("a", textNode.text());
 }

 @Test
 public void testProcessDoctype() {
 Token doctypeToken = new Doctype("html", "", "", "");
 xmlTreeBuilder.insert(doctypeToken);
 DocumentType documentType = (DocumentType) xmlTreeBuilder.getCurrent();
 Assert.assertEquals("html", documentType.tagName());
 }

 @Test(expected = AssertionError.class)
 public void testUnexpectedTokenType() {
 Token unexpectedToken = new Token(Token.Type.TEXT, "Unexpected", 0, 0);
 xmlTreeBuilder.process(unexpectedToken);
 }

 @Test
 public void testParseFragment() {
 String xmlFragment = "<xml><a><b/><c/></a></xml>";
 List<Node> nodes = xmlTreeBuilder.parseFragment(xmlFragment, "", "");
 Element root = (Element) nodes.get(0);
 Assert.assertEquals("xml", root.nodeName());
 Element a = (Element) nodes.get(1);
 Assert.assertEquals("a", a.nodeName());
 List<Node> children = a.children();
 Assert.assertEquals(2, children.size());
 Element b = (Element) children.get(0);
 Assert.assertEquals("b", b.nodeName());
 Assert.assertEquals(0, b.children().size());
 Element c = (Element) children.get(1);
 Assert.assertEquals("c", c.nodeName());
 Assert.assertEquals(0, c.children().size());
 }

 @Test
 public void testParseFragmentWithContext() {
 String xmlFragment = "<xml><a><b/><c/></a></xml>";
 Element context = new Element("root");
 List<Node> nodes = xmlTreeBuilder.parseFragment(xmlFragment, context, "", new Parser(xmlTreeBuilder));
 Element root = (Element) nodes.get(0);
 Assert.assertEquals("xml", root.nodeName());
 Element a = (Element) nodes.get(1);
 Assert.assertEquals("a", a.nodeName());
 List<Node> children = a.children();
 Assert.assertEquals(2, children.size());
 Element b = (Element) children.get(0);
 Assert.assertEquals("b", b.nodeName());
 Assert.assertEquals(0, b.children().size());
 Element c = (Element) children.get(1);
 Assert.assertEquals("c", c.nodeName());
 Assert.assertEquals(0, c.children().size());
 Assert.assertEquals(context, root.parent());
 }

 @Test
 public void testPopStackToCloseUnknownTag() {
 xmlTreeBuilder.insert(new StartTag("unknown"));
 xmlTreeBuilder.insert(new StartTag("unknown"));
 xmlTreeBuilder.popStackToClose(new EndTag("unknown"));
 Assert.assertEquals(xmlTreeBuilder.getCurrent(), null);
 }

 @Test(expected = AssertionError.class)
 public void testUnknownTagIsSelfClosingButNotKnownTag() {
 xmlTreeBuilder.insert(new StartTag("unknown"));
 xmlTreeBuilder.insert(new EndTag("unknown"));
 }
}