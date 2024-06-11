import org.jsoup.Jsoup;
import org.jsoup.helper.Validate;
import org.jsoup.nodes.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class XmlTreeBuilderTest {

 @Test
 public void testDefaultSettings() {
 XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
 ParseSettings settings = xmlTreeBuilder.defaultSettings();
 assertEquals(ParseSettings.PreserveCase.class, settings.getClass());
 }

 @Test
 public void testParseReader() {
 String xml = "<root><element>Hello World</element></root>";
 XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
 Document doc = xmlTreeBuilder.parse(new StringReader(xml), "baseUrl");
 Element rootElement = doc.rootElement();
 Element element = rootElement.child(0);
 assertEquals("root", rootElement.tagName());
 assertEquals("element", element.tagName());
 assertEquals("Hello World", element.text());
 }

 @Test
 public void testParseString() {
 String xml = "<root><element>Hello World</element></root>";
 XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
 Document doc = xmlTreeBuilder.parse(xml, "baseUrl");
 Element rootElement = doc.rootElement();
 Element element = rootElement.child(0);
 assertEquals("root", rootElement.tagName());
 assertEquals("element", element.tagName());
 assertEquals("Hello World", element.text());
 }

 @Test
 public void testInitialiseParse() {
 XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
 Reader reader = new StringReader("<root><element>Hello World</element></root>");
 xmlTreeBuilder.initialiseParse(reader, "baseUrl", ParseErrorList.noTracking(), ParseSettings.preserveCase);
 assertEquals(reader, xmlTreeBuilder.input());
 assertEquals("baseUrl", xmlTreeBuilder.baseUri());
 assertNotNull(xmlTreeBuilder.doc);
 }

 @Test
 public void testProcessStartTag() {
 XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
 Token startTag = new Token.StartTag("element");
 Element element = xmlTreeBuilder.insert(startTag);
 assertEquals("element", element.tagName());
 assertTrue(stack.contains(element));
 }

 @Test
 public void testProcessEndTag() {
 XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
 Token startTag = new Token.StartTag("element");
 Token endTag = new Token.EndTag("element");
 xmlTreeBuilder.stack.add(new Element("root"));;
 xmlTreeBuilder.insert(startTag);
 xmlTreeBuilder.popStackToClose(endTag);
 assertEquals(0, xmlTreeBuilder.stack.size());
 }

 @Test
 public void testProcessComment() {
 XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
 Token commentToken = new Token.Comment("This is a comment");
 xmlTreeBuilder.insert(commentToken);
 Comment comment = (Comment) xmlTreeBuilder.currentElement().child(0);
 assertEquals("This is a comment", comment.data());
 }

 @Test
 public void testProcessCharacter() {
 XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
 Token characterToken = new Token.Character('A');
 xmlTreeBuilder.insert(characterToken);
 TextNode textNode = (TextNode) xmlTreeBuilder.currentElement().child(0);
 assertEquals("A", textNode.text());
 }

 @Test
 public void testProcessDoctype() {
 XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
 Token doctypeToken = new Token.Doctype("html", null, null);
 xmlTreeBuilder.insert(doctypeToken);
 DocumentType documentTypeNode = (DocumentType) xmlTreeBuilder.currentElement().child(0);
 assertEquals("html", documentTypeNode.tagName());
 }

 @Test(expected = AssertionError.class)
 public void testProcessUnexpectedTokenType() {
 XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
 Token token = new Token(123, "Unexpected");
 xmlTreeBuilder.process(token);
 }

 @Test
 public void testInsertNode() {
 XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
 Element element = new Element("element");
 xmlTreeBuilder.insertNode(element);
 assertEquals(element, xmlTreeBuilder.currentElement().child(0));
 }

 @Test
 public void testParseFragment() {
 XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
 String xmlFragment = "<fragment><element>Hello World</element></fragment>";
 List<Node> nodes = xmlTreeBuilder.parseFragment(xmlFragment, "baseUrl", ParseErrorList.noTracking(), ParseSettings.preserveCase);
 assertEquals(2, nodes.size());
 Element fragmentRoot = (Element) nodes.get(0);
 Element element = (Element) nodes.get(1);
 assertEquals("fragment", fragmentRoot.tagName());
 assertEquals("element", element.tagName());
 assertEquals("Hello World", element.text());
 }
}