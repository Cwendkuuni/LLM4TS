import org.junit.Before;
import org.junit.Test;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.XmlTreeBuilder;
import org.jsoup.parser.ParseError;
import org.jsoup.parser.ParseSettings;
import org.jsoup.parser.Token;
import org.jsoup.parser.TokenQueue;
import org.jsoup.helper.Validate;
import java.io.StringReader;
import java.util.List;

public class XmlTreeBuilderTest {

 private XmlTreeBuilder xmlTreeBuilder;
 private Document document;

 @Before
 public void setUp() {
 xmlTreeBuilder = new XmlTreeBuilder();
 document = xmlTreeBuilder.parse(new StringReader("<xml><root><child><grandchild>Hello World</grandchild></child></root></xml>"), "http://test.com");
 }

 @Test
 public void testParseValidXml() {
 // Test parsing valid XML
 Validate.isTrue(document.body().hasChild(0) && document.body().child(0).nodeName().equals("root") && document.body().child(0).child(0).nodeName().equals("child") && document.body().child(0).child(0).child(0).nodeName().equals("grandchild"));
 Validate.isTrue(document.body().child(0).child(0).child(0).node().isText());
 Validate.isTrue(document.body().child(0).child(0).child(0).child(0).toString().equals("Hello World"));
 }

 @Test(expected = ParseError.class)
 public void testParseInvalidXmlStartTag() {
 // Test parsing invalid XML with incorrect start tag
 String invalidXml = "<xml<root><child><grandchild>Hello World</grandchild></child></root>";
 document = null;
 try {
 document = xmlTreeBuilder.parse(new StringReader(invalidXml), "http://test.com");
 } catch (ParseError e) {
 // Expected error
 Validate.isTrue(e.getMessage().contains("Unexpected end tag : root"));
 }
 Validate.isNull(document);
 }

 @Test(expected = ParseError.class)
 public void testParseInvalidXmlEndTag() {
 // Test parsing invalid XML with incorrect end tag
 String invalidXml = "<xml><root><child><grandchild>Hello World</grandchild></child></root></root>";
 document = null;
 try {
 document = xmlTreeBuilder.parse(new StringReader(invalidXml), "http://test.com");
 } catch (ParseError e) {
 // Expected error
 Validate.isTrue(e.getMessage().contains("Unexpected end tag : root"));
 }
 Validate.isNull(document);
 }

 @Test(expected = ParseError.class)
 public void testParseInvalidXmlUnknownTag() {
 // Test parsing invalid XML with unknown tag
 String invalidXml = "<xml><root><unknown></root></xml>";
 document = null;
 try {
 document = xmlTreeBuilder.parse(new StringReader(invalidXml), "http://test.com");
 } catch (ParseError e) {
 // Expected error
 Validate.isTrue(e.getMessage().contains("Unknown tag : unknown"));
 }
 Validate.isNull(document);
 }

 @Test
 public void testParseXmlWithComments() {
 // Test parsing XML with comments
 String xmlWithComments = "<xml><root><child><grandchild>Hello World</grandchild></child><!-- comment --></root></xml>";
 document = xmlTreeBuilder.parse(new StringReader(xmlWithComments), "http://test.com");
 Element comment = document.body().child(0).child(0).child(1);
 Validate.isTrue(comment.nodeName().equals("#comment"));
 Validate.isTrue(comment.child(0).toString().equals(" comment "));
 }

 @Test
 public void testParseXmlWithCdata() {
 // Test parsing XML with CDATA
 String xmlWithCdata = "<xml><root><child><grandchild><![CDATA[Hello World]]></grandchild></child></root></xml>";
 document = xmlTreeBuilder.parse(new StringReader(xmlWithCdata), "http://test.com");
 Element cdata = document.body().child(0).child(0).child(0).child(0);
 Validate.isTrue(cdata.nodeName().equals("CDATA"));
 Validate.isTrue(cdata.child(0).toString().equals("Hello World"));
 }

 @Test
 public void testParseXmlWithDoctype() {
 // Test parsing XML with DOCTYPE
 String xmlWithDoctype = "<!DOCTYPE xml PUBLIC \"-//W3C//DTD XML 1.0 Transitional//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">\n<xml><root><child><grandchild>Hello World</grandchild></child></root></xml>";
 document = xmlTreeBuilder.parse(new StringReader(xmlWithDoctype), "http://test.com");
 DocumentType doctype = document.doctype();
 Validate.isTrue(doctype != null && doctype.toString().equals("xml"));
 }
}