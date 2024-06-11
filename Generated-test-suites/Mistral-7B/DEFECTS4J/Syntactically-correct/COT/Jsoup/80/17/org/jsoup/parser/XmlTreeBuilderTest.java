import org.jsoup.Jsoup;
import org.jsoup.helper.Validate;
import org.jsoup.nodes.*;
import org.junit.Assert;
import org.junit.Test;
import java.io.StringReader;
import java.util.List;

public class XmlTreeBuilderTest {

 private XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();

 @Test
 public void testDefaultSettings() {
 ParseSettings settings = xmlTreeBuilder.defaultSettings();
 Assert.assertEquals(ParseSettings.PreserveCase.class, settings.getClass());
 }

 @Test
 public void testParseWithReaderAndBaseUri() throws Exception {
 String xmlInput = "<xml><tag attr=\"value\"></tag></xml>";
 Reader input = new StringReader(xmlInput);
 String baseUri = "http://example.com";
 Document doc = xmlTreeBuilder.parse(input, baseUri);
 // Perform assertions on the document object
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseWithNullBaseUri() {
 String xmlInput = "<xml><tag attr=\"value\"></tag></xml>";
 Reader input = new StringReader(xmlInput);
 Document doc = xmlTreeBuilder.parse(input, null);
 }

 @Test
 public void testParseWithStringInputAndBaseUri() throws Exception {
 String xmlInput = "<xml><tag attr=\"value\"></tag></xml>";
 String baseUri = "http://example.com";
 Document doc = xmlTreeBuilder.parse(xmlInput, baseUri);
 // Perform assertions on the document object
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseWithNullInput() {
 String baseUri = "http://example.com";
 Document doc = xmlTreeBuilder.parse(null, baseUri);
 }

 @Test
 public void testProcessStartTag() throws Exception {
 String xmlInput = "<xml><tag attr=\"value\"></tag></xml>";
 Reader input = new StringReader(xmlInput);
 String baseUri = "http://example.com";
 Document doc = xmlTreeBuilder.parse(input, baseUri);
 Token startTagToken = doc.tokenizer().select("tag").first();
 Element tag = xmlTreeBuilder.insert(startTagToken.asStartTag());
 Assert.assertEquals("tag", tag.tagName());
 }

 @Test
 public void testProcessEndTag() throws Exception {
 String xmlInput = "<xml><tag><subtag/></tag></xml>";
 Reader input = new StringReader(xmlInput);
 String baseUri = "http://example.com";
 Document doc = xmlTreeBuilder.parse(input, baseUri);
 Token endTagToken = doc.tokenizer().select("tag").last().nextSibling();
 xmlTreeBuilder.popStackToClose(endTagToken.asEndTag());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testProcessUnknownEndTag() throws Exception {
 String xmlInput = "<xml><tag><unknownTag/></tag></xml>";
 Reader input = new StringReader(xmlInput);
 String baseUri = "http://example.com";
 Document doc = xmlTreeBuilder.parse(input, baseUri);
 Token endTagToken = doc.tokenizer().select("tag").last().nextSibling();
 xmlTreeBuilder.popStackToClose(endTagToken.asEndTag());
 }

 @Test
 public void testProcessComment() throws Exception {
 String xmlInput = "<xml><!-- This is a comment --></xml>";
 Reader input = new StringReader(xmlInput);
 String baseUri = "http://example.com";
 Document doc = xmlTreeBuilder.parse(input, baseUri);
 Comment comment = (Comment) doc.childNodes().get(0);
 Assert.assertEquals("This is a comment", comment.getData());
 }

 @Test
 public void testProcessCharacter() throws Exception {
 String xmlInput = "<xml>Text<xml>";
 Reader input = new StringReader(xmlInput);
 String baseUri = "http://example.com";
 Document doc = xmlTreeBuilder.parse(input, baseUri);
 TextNode textNode = (TextNode) doc.childNodes().get(0);
 Assert.assertEquals("Text", textNode.text());
 }

 @Test
 public void testProcessDoctype() throws Exception {
 String xmlInput = "<!DOCTYPE xml SYSTEM \"http://example.com/xml.dtd\">";
 Reader input = new StringReader(xmlInput);
 String baseUri = "http://example.com";
 Document doc = xmlTreeBuilder.parse(input, baseUri);
 DocumentType doctypeNode = (DocumentType) doc.childNodes().get(0);
 Assert.assertEquals("xml", doctypeNode.nodeName());
 Assert.assertEquals("http://example.com/xml.dtd", doctypeNode.getSystemIdentifier());
 }

 @Test
 public void testParseFragment() throws Exception {
 String xmlFragment = "<tag attr=\"value\"><subtag/></tag>";
 String baseUri = "http://example.com";
 List<Node> nodes = xmlTreeBuilder.parseFragment(xmlFragment, baseUri, ParseErrorList.noTracking(), ParseSettings.preserveCase);
 Element tag = (Element) nodes.get(0);
 Assert.assertEquals("tag", tag.tagName());
 }

}