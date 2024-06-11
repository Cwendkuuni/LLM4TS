import org.junit.Assert;
import org.junit.Test;
import org.jsoup.nodes.Comment;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.parser.Token;
import org.jsoup.parser.Tokenizer;
import org.jsoup.parser.XmlTreeBuilder;

public class XmlTreeBuilderTest {

 private XmlTreeBuilder xmlTreeBuilder;
 private Document doc;
 private Tokenizer tokeniser;

 @Before
 public void setup() {
 xmlTreeBuilder = new XmlTreeBuilder();
 doc = xmlTreeBuilder.parse("<test><comment>Hello World</comment></test>");
 tokeniser = xmlTreeBuilder.tokeniser();
 }

 @Test
 public void testInitialiseParse() {
 xmlTreeBuilder = new XmlTreeBuilder();
 xmlTreeBuilder.initialiseParse("", "", null);
 Assert.assertNotNull(xmlTreeBuilder.doc);
 }

 @Test
 public void testProcessStartTag() {
 Token startTagToken = new Token.StartTag("test", null);
 xmlTreeBuilder.process(startTagToken);
 Element testElement = xmlTreeBuilder.currentElement();
 Assert.assertEquals("test", testElement.nodeName());
 }

 @Test
 public void testProcessEndTag() {
 Token endTagToken = new Token.EndTag("test");
 xmlTreeBuilder.process(endTagToken);
 Assert.assertEquals(0, xmlTreeBuilder.stack.size());
 }

 @Test
 public void testProcessComment() {
 Token commentToken = new Token.Comment("Hello World");
 xmlTreeBuilder.process(commentToken);
 Comment comment = (Comment) xmlTreeBuilder.currentNode();
 Assert.assertEquals("Hello World", comment.getData());
 }

 @Test
 public void testProcessCharacter() {
 Token characterToken = new Token.Character('H');
 xmlTreeBuilder.process(characterToken);
 Node node = xmlTreeBuilder.currentNode();
 Assert.assertTrue(node instanceof TextNode);
 Assert.assertEquals("H", ((TextNode) node).text());
 }

 @Test
 public void testProcessDoctype() {
 Token doctypeToken = new Token.Doctype("html", "", "");
 xmlTreeBuilder.process(doctypeToken);
 DocumentType doctypeNode = (DocumentType) xmlTreeBuilder.currentNode();
 Assert.assertEquals("html", doctypeNode.getName());
 }

 @Test(expected = AssertionError.class)
 public void testUnexpectedTokenType() {
 Token unexpectedToken = new Token(-1, "UnexpectedToken");
 xmlTreeBuilder.process(unexpectedToken);
 }

 @Test
 public void testPopStackToClose() {
 xmlTreeBuilder.insert(new Token.StartTag("test"));
 xmlTreeBuilder.insert(new Token.EndTag("test"));
 xmlTreeBuilder.popStackToClose(new Token.EndTag("test"));
 Assert.assertEquals(0, xmlTreeBuilder.stack.size());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testPopStackToCloseUnknownTag() {
 xmlTreeBuilder.insert(new Token.StartTag("unknown"));
 xmlTreeBuilder.popStackToClose(new Token.EndTag("unknown"));
 }
}