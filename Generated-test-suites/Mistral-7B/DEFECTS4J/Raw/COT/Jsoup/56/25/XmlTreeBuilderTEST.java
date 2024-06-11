import org.jsoup.Jsoup;
import org.jsoup.helper.Validate;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.parser.ParseErrorList;
import org.jsoup.parser.ParseSettings;
import org.jsoup.parser.Token;
import org.jsoup.parser.Token.Comment;
import org.jsoup.parser.Token.Character;
import org.jsoup.parser.Token.Doctype;
import org.jsoup.parser.Token.EndTag;
import org.jsoup.parser.Token.StartTag;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

public class XmlTreeBuilderTest {

 private XmlTreeBuilder xmlTreeBuilder;
 private Document doc;

 @BeforeEach
 public void setup() {
 xmlTreeBuilder = new XmlTreeBuilder();
 doc = Jsoup.parse("<root><elem attr='test'/><elem2 attr2='test2'/></root>", "", ParseErrorList.noTracking(), XmlTreeBuilder.ParseSettings.preserveCase);
 xmlTreeBuilder.initialiseParse(doc.body().text(), "", ParseErrorList.noTracking(), XmlTreeBuilder.ParseSettings.preserveCase);
 }

 @Test
 public void testDefaultSettings() {
 ParseSettings settings = xmlTreeBuilder.defaultSettings();
 assertEquals(ParseSettings.preserveCase, settings);
 }

 @Test
 public void testParse() {
 Document parsedDoc = xmlTreeBuilder.parse("<root><elem attr='test'/><elem2 attr2='test2'/></root>", "");
 assertEquals(doc, parsedDoc);
 }

 @Test
 public void testProcessStartTag() {
 Token startTagToken = new Token(Token.StartTag.class, "<elem attr='test'>");
 Element expectedElement = new Element("elem", "", new AttributeSpec("attr", "test"));
 Element actualElement = xmlTreeBuilder.insert(startTagToken);
 assertEquals(expectedElement, actualElement);
 }

 @Test
 public void testProcessEndTag() {
 Token endTagToken = new Token(Token.EndTag.class, "</elem>");
 xmlTreeBuilder.popStackToClose(endTagToken);
 assertEquals(2, xmlTreeBuilder.stack.size());
 }

 @Test
 public void testProcessComment() {
 Token commentToken = new Token(Token.Comment.class, "--comment--");
 Comment expectedComment = new Comment("--comment--", "");
 Comment actualComment = (Comment) xmlTreeBuilder.insert(commentToken);
 assertEquals(expectedComment.getData(), actualComment.getData());
 }

 @Test
 public void testProcessCharacter() {
 Token characterToken = new Token(Token.Character.class, 'A');
 TextNode expectedTextNode = new TextNode("A", "");
 TextNode actualTextNode = (TextNode) xmlTreeBuilder.insert(characterToken);
 assertEquals(expectedTextNode.getData(), actualTextNode.getData());
 }

 @Test
 public void testProcessDoctype() {
 Token doctypeToken = new Token(Token.Doctype.class, "<!DOCTYPE doc [ <!ATTLIST elem attr CDATA #REQUIRED> ]>");
 DocumentType expectedDocumentType = new DocumentType("doc", null, null, "");
 DocumentType actualDocumentType = (DocumentType) xmlTreeBuilder.insert(doctypeToken);
 assertEquals(expectedDocumentType.getNodeName(), actualDocumentType.getNodeName());
 }

 @Test
 public void testPopStackToCloseUnknownTag() {
 Token endTagToken = new Token(Token.EndTag.class, "</unknown>");
 xmlTreeBuilder.popStackToClose(endTagToken);
 assertTrue(xmlTreeBuilder.stack.isEmpty());
 }

 @Test
 public void testPopStackToCloseUnknownTagSelfClosing() {
 Token startTagToken = new Token(Token.StartTag.class, "<unknown/>");
 xmlTreeBuilder.initialiseParse("", "", ParseErrorList.noTracking(), XmlTreeBuilder.ParseSettings.preserveCase);
 xmlTreeBuilder.tokeniser.feed(startTagToken);
 xmlTreeBuilder.tokeniser.feed(endTagToken);
 xmlTreeBuilder.runParser();
 assertTrue(xmlTreeBuilder.stack.isEmpty());
 }

 @Test
 public void testParseFragmentEmptyInput() {
 List<Node> nodes = xmlTreeBuilder.parseFragment("", "", ParseErrorList.noTracking(), XmlTreeBuilder.ParseSettings.preserveCase);
 assertTrue(nodes.isEmpty());
 }

 @Test
 public void testParseFragmentInvalidInput() {
 ParseErrorList errors = new ParseErrorList();
 List<Node> nodes = xmlTreeBuilder.parseFragment("<?xml v='1.0'>", "", errors, XmlTreeBuilder.ParseSettings.preserveCase);
 assertEquals(1, errors.size());
 assertNotNull(nodes.get(0));
 assertTrue(nodes.get(0) instanceof TextNode);
 assertEquals("&lt;?xml v='1.0'&gt;", nodes.get(0).outerHtml());
 }

 @Test
 public void testParseFragmentValidInput() {
 List<Node> nodes = xmlTreeBuilder.parseFragment("<root><elem attr='test'><elem2 attr2='test2'/></elem></root>", "", ParseErrorList.noTracking(), XmlTreeBuilder.ParseSettings.preserveCase);
 assertEquals(3, nodes.size());
 Element root = (Element) nodes.get(0);
 assertEquals("root", root.nodeName());
 Element elem = (Element) nodes.get(1);
 assertEquals("elem", elem.nodeName());
 Element elem2 = (Element) nodes.get(2);
 assertEquals("elem2", elem2.nodeName());
 }
}