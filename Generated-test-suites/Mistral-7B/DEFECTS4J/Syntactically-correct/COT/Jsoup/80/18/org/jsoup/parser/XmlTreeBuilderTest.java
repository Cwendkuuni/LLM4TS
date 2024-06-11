import org.junit.Assert;
import org.junit.Test;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.ParseSettings;
import org.jsoup.parser.XmlTreeBuilder;
import org.jsoup.parser.Token;
import org.jsoup.parser.Token.Comment;
import org.jsoup.parser.Token.Character;
import org.jsoup.parser.Token.Doctype;
import org.jsoup.parser.Token.EndTag;
import org.jsoup.parser.Token.StartTag;
import org.jsoup.parser.Tokenizer;

public class XmlTreeBuilderTest {

 private XmlTreeBuilder builder;
 private Document doc;

 @Before
 public void setUp() {
 builder = new XmlTreeBuilder();
 doc = Jsoup.parse("<root><element><subElement/></element></root>", "", Parser.xmlParser());
 }

 @Test
 public void testDefaultSettings() {
 ParseSettings settings = builder.defaultSettings();
 Assert.assertEquals(ParseSettings.preserveCase, settings);
 }

 @Test
 public void testParse() {
 String xmlInput = "<root><element><subElement/></element></root>";
 doc = builder.parse(new StringReader(xmlInput), "");
 Assert.assertEquals(xmlInput, doc.html());
 }

 @Test
 public void testParseWithBaseUri() {
 String xmlInput = "<baseUrl><root><element><subElement/></element></root></baseUrl>";
 doc = builder.parse(new StringReader(xmlInput), "http://test.com");
 Assert.assertEquals("<root><element><subElement/></element></root>", doc.html());
 Assert.assertEquals("http://test.com", doc.baseUri().toString());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNullInput() {
 builder.parse(null, "");
 }

 @Test
 public void testInitialiseParse() {
 builder = new XmlTreeBuilder();
 Tokenizer tokenizer = new Tokenizer();
 tokenizer.reset();
 Token token = tokenizer.nextToken();
 builder.initialiseParse(new StringReader(""), "", ParseErrorList.noTracking(), ParseSettings.preserveCase);
 Assert.assertNotNull(builder.doc);
 Assert.assertEquals(builder.doc, doc);
 }

 @Test
 public void testProcessStartTag() {
 Element root = doc.rootElement();
 Token token = new Token(Token.StartTag.class, "<element>", null, 1, 1);
 builder.process(token);
 Element element = root.child(0);
 Assert.assertEquals("<element/>", element.html());
 }

 @Test
 public void testProcessEndTag() {
 Element root = doc.rootElement();
 Element element = doc.getElementsByTag("element").first();
 Token token = new Token(Token.EndTag.class, "</element>", null, 1, 1);
 builder.process(token);
 element.remove();
 }

 @Test
 public void testProcessComment() {
 String commentData = "This is a comment";
 Token token = new Token(Token.Comment.class, "", commentData, 1, 1);
 builder.process(token);
 Comment comment = (Comment) doc.child(0);
 Assert.assertEquals(commentData, comment.data());
 }

 @Test
 public void testProcessCharacter() {
 String characterData = "This is a character";
 Token token = new Token(Token.Character.class, characterData, null, 1, 1);
 builder.process(token);
 TextNode textNode = (TextNode) doc.child(1);
 Assert.assertEquals(characterData, textNode.text());
 }

 @Test
 public void testProcessDoctype() {
 String doctypeName = "html";
 String publicIdentifier = "public";
 String systemIdentifier = "system";
 Token token = new Token(Token.Doctype.class, doctypeName, publicIdentifier, systemIdentifier, null);
 builder.process(token);
 DocumentType documentType = (DocumentType) doc.child(0);
 Assert.assertEquals(doctypeName, documentType.nodeName());
 Assert.assertEquals(publicIdentifier, documentType.publicId());
 Assert.assertEquals(systemIdentifier, documentType.systemId());
 }

 @Test
 public void testPopStackToClose() {
 Element root = doc.rootElement();
 Element element = root.addElement("element");
 Element subElement = element.addElement("subElement");
 Token endTag = new Token(Token.EndTag.class, "</subElement>", null, 1, 1);
 builder.popStackToClose(endTag);
 Assert.assertEquals(0, stack.size());
 Assert.assertNull(subElement.parent());
 }

 @Test
 public void testParseFragment() {
 String xmlFragment = "<fragment><element><subElement/></element></fragment>";
 List<Node> nodes = builder.parseFragment(xmlFragment, "", ParseErrorList.noTracking(), ParseSettings.preserveCase);
 Assert.assertEquals(3, nodes.size());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNullBaseUri() {
 builder.parse(new StringReader("<root></root>"), null);
 }

 @Test
 public void testNullSettings() {
 builder.parse(new StringReader("<root></root>"), "", null);
 }
}