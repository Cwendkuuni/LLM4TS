import org.junit.Test;
import org.jsoup.Jsoup;
import org.jsoup.helper.Validate;
import org.jsoup.nodes.*;
import static org.junit.Assert.*;

public class XmlTreeBuilderTest {

 private XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();

 @Test
 public void testDefaultSettings() {
 ParseSettings settings = xmlTreeBuilder.defaultSettings();
 assertEquals(ParseSettings.preserveCase, settings);
 }

 @Test
 public void testParseWithReader() throws Exception {
 String xmlString = "<xml><tag attr=\"value\"></tag></xml>";
 Reader input = new StringReader(xmlString);
 String baseUri = "http://example.com";
 Document document = xmlTreeBuilder.parse(input, baseUri);
 assertNotNull(document);
 Element root = document.getRootElement();
 assertEquals("xml", root.tagName());
 }

 @Test
 public void testParseWithString() throws Exception {
 String xmlString = "<xml><tag attr=\"value\"></tag></xml>";
 Document document = xmlTreeBuilder.parse(xmlString, "http://example.com");
 assertNotNull(document);
 Element root = document.getRootElement();
 assertEquals("xml", root.tagName());
 }

 @Test
 public void testInitialiseParse() throws Exception {
 String xmlString = "<xml><tag attr=\"value\"></tag></xml>";
 String baseUri = "http://example.com";
 ParseErrorList errors = ParseErrorList.noTracking();
 ParseSettings settings = ParseSettings.preserveCase;
 XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
 xmlTreeBuilder.initialiseParse(new StringReader(xmlString), baseUri, errors, settings);
 assertEquals(ParseSettings.preserveCase, xmlTreeBuilder.settings());
 assertNotNull(xmlTreeBuilder.doc);
 }

 @Test
 public void testProcessStartTag() throws Exception {
 String xmlString = "<xml><tag attr=\"value\"></tag></xml>";
 String baseUri = "http://example.com";
 XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
 xmlTreeBuilder.initialiseParse(new StringReader(xmlString), baseUri, ParseErrorList.noTracking(), ParseSettings.preserveCase);
 Token startTagToken = new Token.StartTag("tag", null, null, null, null, null, null, null, null, null);
 Element expectedElement = new Element("tag", baseUri, null);
 assertEquals(expectedElement, xmlTreeBuilder.insert(startTagToken));
 }

 @Test
 public void testProcessEndTag() {
 String xmlString = "<xml><tag></tag></xml>";
 XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
 xmlTreeBuilder.initialiseParse(new StringReader(xmlString), "http://example.com", ParseErrorList.noTracking(), ParseSettings.preserveCase);
 Token endTagToken = new Token.EndTag("tag");
 xmlTreeBuilder.popStackToClose(endTagToken);
 assertEquals(0, xmlTreeBuilder.stack.size());
 }

 @Test(expected = AssertionError.class)
 public void testProcessUnknownTag() throws Exception {
 String xmlString = "<xml><unknown></xml>";
 XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
 xmlTreeBuilder.initialiseParse(new StringReader(xmlString), "http://example.com", ParseErrorList.noTracking(), ParseSettings.preserveCase);
 Token startTagToken = new Token.StartTag("unknown", null, null, null, null, null, null, null, null, null);
 xmlTreeBuilder.insert(startTagToken);
 }

 @Test
 public void testProcessComment() throws Exception {
 String xmlString = "<xml><!-- comment --></xml>";
 XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
 xmlTreeBuilder.initialiseParse(new StringReader(xmlString), "http://example.com", ParseErrorList.noTracking(), ParseSettings.preserveCase);
 Token commentToken = new Token.Comment("comment");
 Comment expectedComment = new Comment("comment");
 assertEquals(expectedComment, xmlTreeBuilder.insert(commentToken));
 }

 @Test
 public void testProcessCharacter() throws Exception {
 String xmlString = "<xml><![CDATA[character]]></xml>";
 XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
 xmlTreeBuilder.initialiseParse(new StringReader(xmlString), "http://example.com", ParseErrorList.noTracking(), ParseSettings.preserveCase);
 Token characterToken = new Token.Character('c', "character");
 TextNode expectedTextNode = new TextNode("character");
 assertEquals(expectedTextNode, xmlTreeBuilder.insert(characterToken));
 }

 @Test
 public void testProcessDoctype() throws Exception {
 String xmlString = "<!DOCTYPE xml [<!ATTLIST xml foo CDATA #REQUIRED>]>";
 XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
 xmlTreeBuilder.initialiseParse(new StringReader(xmlString), "http://example.com", ParseErrorList.noTracking(), ParseSettings.preserveCase);
 Token doctypeToken = new Token.Doctype("xml");
 DocumentType expectedDocumentType = new DocumentType("xml", null, null);
 assertEquals(expectedDocumentType, xmlTreeBuilder.insert(doctypeToken));
 }

 @Test(expected = AssertionError.class)
 public void testProcessUnexpectedTokenType() throws Exception {
 String xmlString = "<xml>"; // missing closing tag
 XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
 xmlTreeBuilder.initialiseParse(new StringReader(xmlString), "http://example.com", ParseErrorList.noTracking(), ParseSettings.preserveCase);
 Token token = new Token(Token.Type.EOF);
 xmlTreeBuilder.process(token);
 }

 @Test
 public void testParseFragment() throws Exception {
 String xmlFragment = "<tag attr=\"value\"></tag>";
 String baseUri = "http://example.com";
 List<Node> expectedNodes = Arrays.asList(new Element("tag", baseUri, null), new TextNode(""));
 List<Node> nodes = xmlTreeBuilder.parseFragment(xmlFragment, baseUri, ParseErrorList.noTracking(), ParseSettings.preserveCase);
 assertEquals(expectedNodes, nodes);
 }
}