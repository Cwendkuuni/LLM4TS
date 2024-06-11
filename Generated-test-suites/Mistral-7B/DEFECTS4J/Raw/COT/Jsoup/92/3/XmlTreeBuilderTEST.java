import org.jsoup.helper.Validate;
import org.jsoup.nodes.*;
import org.jsoup.parser.ParseSettings;
import org.jsoup.parser.XmlTreeBuilder;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

class XmlTreeBuilderTest {

 @Test
 void testDefaultSettings() {
 XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
 ParseSettings settings = xmlTreeBuilder.defaultSettings();
 assertEquals(ParseSettings.PreserveCase.class, settings.getClass());
 }

 @Test
 void testInitialiseParse() {
 XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
 Reader input = new StringReader("<xml/>");
 String baseUri = "http://example.com";
 Parser parser = new Parser(xmlTreeBuilder);
 xmlTreeBuilder.initialiseParse(input, baseUri, parser);
 assertEquals(XmlTreeBuilder.class, xmlTreeBuilder.getClass());
 assertEquals(Document.class, xmlTreeBuilder.doc.getClass());
 assertEquals(baseUri, xmlTreeBuilder.doc.baseUri());
 }

 @Test
 void testParse() {
 XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
 String xmlString = "<xml><tag/></xml>";
 Document document = xmlTreeBuilder.parse(new StringReader(xmlString), "http://example.com");
 Element rootElement = document.getRootElement();
 assertEquals("xml", rootElement.tagName());
 }

 @Test
 void testProcessStartTag() {
 XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
 Tag tag = Tag.valueOf("tag", xmlTreeBuilder.defaultSettings());
 Element element = new Element(tag, "http://example.com");
 Token startTag = new Token.StartTag("tag");
 xmlTreeBuilder.insert(startTag);
 assertEquals(element, xmlTreeBuilder.currentElement());
 }

 @Test
 void testProcessEndTag() {
 XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
 String tagName = "tag";
 Element element = new Element(Tag.valueOf(tagName, xmlTreeBuilder.defaultSettings()), "http://example.com");
 xmlTreeBuilder.stack.add(element);
 Token endTag = new Token.EndTag(tagName);
 xmlTreeBuilder.popStackToClose(endTag);
 assertFalse(xmlTreeBuilder.stack.contains(element));
 }

 @Test
 void testProcessComment() {
 XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
 String commentData = "This is a comment";
 Token commentToken = new Token.Comment(commentData);
 xmlTreeBuilder.insert(commentToken);
 Comment comment = xmlTreeBuilder.currentElement().child(0);
 assertEquals(commentData, comment.text());
 }

 @Test
 void testProcessCharacter() {
 XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
 String characterData = "This is a character";
 Token characterToken = new Token.Character('C', characterData);
 xmlTreeBuilder.insert(characterToken);
 TextNode characterNode = (TextNode) xmlTreeBuilder.currentElement().child(0);
 assertEquals(characterData, characterNode.text());
 }

 @Test
 void testProcessDoctype() {
 XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
 DocumentType documentType = new DocumentType("html", null, null);
 Token doctypeToken = new Token.Doctype(documentType);
 xmlTreeBuilder.insert(doctypeToken);
 assertEquals(documentType, xmlTreeBuilder.currentElement());
 }

 @Test
 void testPopStackToCloseNonExistingTag() {
 XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
 String tagName = "tag";
 xmlTreeBuilder.stack.add(new Element(Tag.valueOf(tagName, xmlTreeBuilder.defaultSettings()), "http://example.com"));
 Token endTag = new Token.EndTag(tagName);
 xmlTreeBuilder.popStackToClose(endTag);
 assertFalse(xmlTreeBuilder.stack.isEmpty());
 }

 @Test
 void testParseFragment() {
 XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
 String fragment = "<tag><subtag/></tag>";
 List<Node> nodes = xmlTreeBuilder.parseFragment(fragment, "http://example.com", new Parser(xmlTreeBuilder));
 Element rootElement = (Element) nodes.get(0);
 assertEquals("tag", rootElement.tagName());
 List<Node> childNodes = rootElement.childNodes();
 assertEquals(1, childNodes.size());
 Element subtagElement = (Element) childNodes.get(0);
 assertEquals("subtag", subtagElement.tagName());
 }

 @Test
 void testParseFragmentWithContext() {
 XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
 String contextTagName = "context";
 String fragment = "<" + contextTagName + "><tag/></" + contextTagName + ">";
 Element context = new Element(Tag.valueOf(contextTagName, xmlTreeBuilder.defaultSettings()), "http://example.com");
 List<Node> nodes = xmlTreeBuilder.parseFragment(fragment, context, "http://example.com", new Parser(xmlTreeBuilder));
 Element rootElement = (Element) nodes.get(0);
 assertEquals("tag", rootElement.tagName());
 }

 @Test
 void testValidateUnexpectedTokenType() {
 XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
 Token unexpectedToken = new Token(123, "");
 assertThrows(Validate.MalformedXmlException.class, () -> xmlTreeBuilder.process(unexpectedToken));
 }
}