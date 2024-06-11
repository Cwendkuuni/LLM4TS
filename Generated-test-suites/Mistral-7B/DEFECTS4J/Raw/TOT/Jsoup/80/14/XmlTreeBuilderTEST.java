import org.junit.Before;
import org.junit.Test;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.ParseErrorList;
import org.jsoup.parser.ParseSettings;
import org.jsoup.parser.XmlTreeBuilder;
import org.jsoup.helper.Validate;

import java.io.StringReader;
import java.util.List;

public class XmlTreeBuilderTest {

 private XmlTreeBuilder xmlTreeBuilder;

 @Before
 public void setup() {
 xmlTreeBuilder = new XmlTreeBuilder();
 }

 @Test
 public void testParseEmptyXml() {
 String xml = "<xml/>";
 Document doc = xmlTreeBuilder.parse(new StringReader(xml), "http://test.com");
 Validate.assertEquals(1, doc.childNodes().size());
 Validate.assertEquals("xml", doc.tagName());
 }

 @Test
 public void testParseXmlWithRootElement() {
 String xml = "<root><child>Hello World</child></root>";
 Document doc = xmlTreeBuilder.parse(new StringReader(xml), "http://test.com");
 Validate.assertEquals("root", doc.tagName());
 Validate.assertEquals(1, doc.childNodes().size());
 Validate.assertEquals("child", doc.childNodes().get(0).tagName());
 Validate.assertEquals("Hello World", doc.childNodes().get(0).text());
 }

 @Test
 public void testParseXmlWithSelfClosingTag() {
 String xml = "<img src=\"image.jpg\" />";
 Document doc = xmlTreeBuilder.parse(new StringReader(xml), "http://test.com");
 Validate.assertEquals(1, doc.childNodes().size());
 Validate.assertEquals("img", doc.childNodes().get(0).tagName());
 }

 @Test
 public void testParseXmlWithComment() {
 String xml = "<!-- This is a comment -->";
 Document doc = xmlTreeBuilder.parse(new StringReader(xml), "http://test.com");
 Validate.assertEquals(1, doc.childNodes().size());
 Validate.assertTrue(doc.childNodes().get(0) instanceof Comment);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseXmlWithInvalidStartTag() {
 String xml = "<invalid>";
 ParseErrorList errors = new ParseErrorList();
 Document doc = xmlTreeBuilder.parse(new StringReader(xml), "http://test.com", errors, ParseSettings.preserveCase);
 Validate.assertTrue(errors.size() > 0);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseXmlWithMissingEndTag() {
 String xml = "<root><child>";
 ParseErrorList errors = new ParseErrorList();
 Document doc = xmlTreeBuilder.parse(new StringReader(xml), "http://test.com", errors, ParseSettings.preserveCase);
 Validate.assertTrue(errors.size() > 0);
 }

 @Test
 public void testParseXmlWithDoctype() {
 String xml = "<!DOCTYPE html SYSTEM \"about:legacy-compat\">";
 Document doc = xmlTreeBuilder.parse(new StringReader(xml), "http://test.com");
 Validate.assertEquals(1, doc.childNodes().size());
 Validate.assertTrue(doc.childNodes().get(0) instanceof DocumentType);
 }

 @Test
 public void testParseXmlWithCDataSection() {
 String xml = "<![CDATA[ <script type=\"text/javascript\">function greet() { alert(\"Hello, World!\"); } ]]>";
 Document doc = xmlTreeBuilder.parse(new StringReader(xml), "http://test.com");
 Validate.assertEquals(1, doc.childNodes().size());
 Validate.assertTrue(doc.childNodes().get(0) instanceof CDataNode);
 }
}