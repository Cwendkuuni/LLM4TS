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
 public void testParseWithReaderAndBaseUri() throws Exception {
 String xmlString = "<xml><tag attr=\"value\"></tag></xml>";
 XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
 Document xmlDoc = xmlTreeBuilder.parse(new StringReader(xmlString), "baseUri");
 Element rootElement = xmlDoc.rootElement();
 assertEquals("xml", rootElement.tagName());
 assertEquals("baseUri", rootElement.baseUri());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseWithNullBaseUri() {
 XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
 try {
 xmlTreeBuilder.parse(new StringReader("<xml></xml>"), null);
 fail("Expected IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 // expected
 }
 }

 @Test
 public void testInitialiseParse() {
 XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
 String xmlString = "<xml><tag attr=\"value\"></tag></xml>";
 Reader input = new StringReader(xmlString);
 ParseErrorList errors = ParseErrorList.noTracking();
 ParseSettings settings = ParseSettings.preserveCase;
 xmlTreeBuilder.initialiseParse(input, "baseUri", errors, settings);
 assertEquals("baseUri", xmlTreeBuilder.doc.baseUri());
 }

 @Test
 public void testProcessStartTag() throws Exception {
 XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
 String xmlString = "<xml><tag attr=\"value\"></tag></xml>";
 Reader input = new StringReader(xmlString);
 String baseUri = "baseUri";
 ParseErrorList errors = ParseErrorList.noTracking();
 ParseSettings settings = ParseSettings.preserveCase;
 xmlTreeBuilder.initialiseParse(input, baseUri, errors, settings);
 xmlTreeBuilder.parse(token -> {
 switch (token.type) {
 case StartTag:
 assertEquals("tag", token.asStartTag().tagName());
 break;
 default:
 fail("Unexpected token type: " + token.type);
 }
 xmlTreeBuilder.runParser();
 return true;
 });
 }

 @Test
 public void testProcessEndTag() throws Exception {
 XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
 String xmlString = "<xml><tag><tag2></tag2></tag></xml>";
 Reader input = new StringReader(xmlString);
 String baseUri = "baseUri";
 ParseErrorList errors = ParseErrorList.noTracking();
 ParseSettings settings = ParseSettings.preserveCase;
 xmlTreeBuilder.initialiseParse(input, baseUri, errors, settings);
 xmlTreeBuilder.parse(token -> {
 if (token.type == EndTag) {
 assertEquals("tag2", token.asEndTag().tagName());
 }
 xmlTreeBuilder.runParser();
 return true;
 });
 }

 @Test
 public void testProcessComment() throws Exception {
 XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
 String xmlString = "<xml><!-- comment --></xml>";
 Reader input = new StringReader(xmlString);
 String baseUri = "baseUri";
 ParseErrorList errors = ParseErrorList.noTracking();
 ParseSettings settings = ParseSettings.preserveCase;
 xmlTreeBuilder.initialiseParse(input, baseUri, errors, settings);
 xmlTreeBuilder.parse(token -> {
 if (token.type == Comment) {
 assertEquals("comment", token.asComment().text());
 }
 xmlTreeBuilder.runParser();
 return true;
 });
 }

 @Test
 public void testProcessCharacter() throws Exception {
 XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
 String xmlString = "<xml>abcd<eof></xml>";
 Reader input = new StringReader(xmlString);
 String baseUri = "baseUri";
 ParseErrorList errors = ParseErrorList.noTracking();
 ParseSettings settings = ParseSettings.preserveCase;
 xmlTreeBuilder.initialiseParse(input, baseUri, errors, settings);
 xmlTreeBuilder.parse(token -> {
 if (token.type == Character) {
 assertEquals("abc", token.asCharacter().data());
 }
 xmlTreeBuilder.runParser();
 return true;
 });
 }

 @Test
 public void testProcessDoctype() throws Exception {
 XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
 String xmlString = "<!DOCTYPE xml PUBLIC \"-//W3C//DTD XML 1.0//EN\" \"xml.dtd\">";
 Reader input = new StringReader(xmlString);
 String baseUri = "baseUri";
 ParseErrorList errors = ParseErrorList.noTracking();
 ParseSettings settings = ParseSettings.preserveCase;
 xmlTreeBuilder.initialiseParse(input, baseUri, errors, settings);
 DocumentType doctype = (DocumentType) xmlTreeBuilder.doc.childNodes().get(0);
 assertEquals("xml", doctype.tagName());
 assertEquals("-//W3C//DTD XML 1.0//EN", doctype.publicId());
 assertEquals("xml.dtd", doctype.systemId());
 }

 @Test
 public void testPopStackToClose() throws Exception {
 XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
 String xmlString = "<xml><tag><tag2></tag2></tag></xml>";
 Reader input = new StringReader(xmlString);
 String baseUri = "baseUri";
 ParseErrorList errors = ParseErrorList.noTracking();
 ParseSettings settings = ParseSettings.preserveCase;
 xmlTreeBuilder.initialiseParse(input, baseUri, errors, settings);
 xmlTreeBuilder.parse(token -> {
 if (token.type == EndTag && token.asEndTag().tagName().equals("tag2")) {
 xmlTreeBuilder.popStackToClose(token.asEndTag());
 }
 xmlTreeBuilder.runParser();
 return true;
 });
 int stackSizeBeforePop = xmlTreeBuilder.stack.size();
 xmlTreeBuilder.parse(token -> {
 if (token.type == EndTag && token.asEndTag().tagName().equals("tag")) {
 assertEquals(stackSizeBeforePop - 1, xmlTreeBuilder.stack.size());
 }
 xmlTreeBuilder.runParser();
 return true;
 });
 }

 @Test
 public void testParseFragment() throws Exception {
 XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
 String xmlFragment = "<tag attr=\"value\"/><tag2/>";
 Document fragmentDoc = xmlTreeBuilder.parseFragment(xmlFragment, "baseUri", ParseErrorList.noTracking(), ParseSettings.preserveCase);
 List<Node> childNodes = fragmentDoc.childNodes();
 assertEquals(2, childNodes.size());
 }

}