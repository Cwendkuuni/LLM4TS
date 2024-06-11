import org.junit.Assert;
import org.junit.Test;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.ParseErrorList;
import org.jsoup.parser.ParseSettings;
import org.jsoup.parser.XmlTreeBuilder;
import org.jsoup.helper.Validate;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.TextNode;
import org.jsoup.nodes.CDataNode;
import org.jsoup.nodes.DocumentType;
import org.jsoup.nodes.Comment;
import org.jsoup.nodes.Doctype;
import org.jsoup.nodes.ElementTag;

public class XmlTreeBuilderTest {

 private XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();

 @Test
 public void testDefaultSettings() {
 ParseSettings settings = xmlTreeBuilder.defaultSettings();
 Assert.assertEquals(ParseSettings.preserveCase, settings);
 }

 @Test
 public void testParseReader() throws Exception {
 String xml = "<root><element attr=\"value\">Content</element></root>";
 Reader input = new StringReader(xml);
 Document document = xmlTreeBuilder.parse(input, "http://example.com");
 Assert.assertNotNull(document);
 Assert.assertEquals("root", document.tagName());
 Element root = document.getRootElement();
 Assert.assertEquals(1, root.childElementCount());
 Element element = root.child(0);
 Assert.assertEquals("element", element.tagName());
 Assert.assertEquals("value", element.attr("attr"));
 Assert.assertEquals("Content", element.text());
 }

 @Test
 public void testParseString() throws Exception {
 String xml = "<root><element attr=\"value\">Content</element></root>";
 Document document = xmlTreeBuilder.parse(xml, "http://example.com");
 // ... same assertions as in testParseReader()
 }

 @Test
 public void testInitialiseParse() {
 Document document = new Document();
 ParseErrorList errors = ParseErrorList.noTracking();
 ParseSettings settings = ParseSettings.preserveCase;
 xmlTreeBuilder.initialiseParse(new StringReader("<root></root>"), "http://example.com", errors, settings);
 Assert.assertNotNull(document);
 Assert.assertEquals("root", document.tagName());
 Assert.assertNotNull(document.outputSettings());
 Assert.assertEquals(Document.OutputSettings.Syntax.xml, document.outputSettings().syntax());
 }

 @Test
 public void testProcessStartTag() {
 Token startTag = new Token.StartTag("element", null, new Attribute[] { new Attribute("attr", "value") });
 Element element = xmlTreeBuilder.insert(startTag);
 Assert.assertEquals("element", element.tagName());
 Assert.assertEquals("value", element.attr("attr"));
 }

 @Test
 public void testProcessEndTag() {
 Token endTag = new Token.EndTag("element");
 xmlTreeBuilder.popStackToClose(endTag);
 Element element = (Element) xmlTreeBuilder.stack.get(xmlTreeBuilder.stack.size()-1);
 Assert.assertEquals("element", element.tagName());
 xmlTreeBuilder.stack.remove(xmlTreeBuilder.stack.size()-1);
 Assert.assertEquals(xmlTreeBuilder.stack.size(), 0);
 }

 @Test
 public void testInsertComment() {
 Token commentToken = new Token.Comment("This is a comment");
 Comment comment = (Comment) xmlTreeBuilder.insert(commentToken);
 Assert.assertEquals("This is a comment", comment.text());
 }

 @Test
 public void testInsertCharacter() {
 Token characterToken = new Token.Character('A');
 TextNode character = (TextNode) xmlTreeBuilder.insert(characterToken);
 Assert.assertEquals("A", character.text());
 }

 @Test
 public void testInsertDoctype() {
 Token doctypeToken = new Token.Doctype("html", null, null, null, null, null);
 DocumentType documentType = (DocumentType) xmlTreeBuilder.insert(doctypeToken);
 Assert.assertEquals("html", documentType.tagName());
 }

 @Test(expected = AssertionError.class)
 public void testInvalidTokenType() {
 Token invalidToken = new Token(-1, null, null);
 xmlTreeBuilder.process(invalidToken);
 }

 @Test
 public void testParseFragment() throws Exception {
 String xmlFragment = "<root><element attr=\"value\">Content</element></root>";
 List<Node> nodes = xmlTreeBuilder.parseFragment(xmlFragment, "http://example.com", ParseErrorList.noTracking(), ParseSettings.preserveCase);
 Document document = new Document();
 document.appendChild(nodes.get(0));
 // ... same assertions as in testParseReader()
 }

}