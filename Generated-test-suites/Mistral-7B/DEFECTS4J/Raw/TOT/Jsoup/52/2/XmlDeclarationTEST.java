import org.junit.Test;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.XmlDeclaration;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class XmlDeclarationTest {

 @Test
 public void testXmlDeclarationWithNameAndVersion() {
 XmlDeclaration xmlDeclaration = new XmlDeclaration("xml", "baseUri", true);
 Attributes attributes = new Attributes();
 attributes.put("version", "1.0");
 xmlDeclaration.attributes(attributes);

 XmlDeclaration parsedXmlDeclaration = Jsoup.parse("<declaration version=\"1.0\"/>", "baseUri").xml();

 assertEquals("#declaration", parsedXmlDeclaration.nodeName());
 assertEquals("xml", xmlDeclaration.name());
 assertEquals("<?xml version=\"1.0\"?>", xmlDeclaration.getWholeDeclaration());
 }

 @Test
 public void testXmlDeclarationWithoutNameAndVersion() {
 XmlDeclaration xmlDeclaration = new XmlDeclaration(null, "baseUri", false);

 XmlDeclaration parsedXmlDeclaration = Jsoup.parse("<?/>", "baseUri").xml();

 assertEquals("#declaration", parsedXmlDeclaration.nodeName());
 assertEquals(null, xmlDeclaration.name());
 assertEquals("<?>", xmlDeclaration.getWholeDeclaration());
 }

 @Test
 public void testXmlDeclarationWithEncoding() {
 XmlDeclaration xmlDeclaration = new XmlDeclaration("xml", "baseUri", true);
 Attributes attributes = new Attributes();
 attributes.put("encoding", "UTF-8");
 xmlDeclaration.attributes(attributes);

 XmlDeclaration parsedXmlDeclaration = Jsoup.parse("<declaration encoding=\"UTF-8\"/>", "baseUri").xml();

 assertEquals("#declaration", parsedXmlDeclaration.nodeName());
 assertEquals("xml", xmlDeclaration.name());
 assertEquals("<?xml encoding=\"UTF-8\"?>", xmlDeclaration.getWholeDeclaration());
 }

 @Test
 public void testXmlDeclarationWithVersionAndEncoding() {
 XmlDeclaration xmlDeclaration = new XmlDeclaration("xml", "baseUri", true);
 Attributes attributes = new Attributes();
 attributes.put("version", "1.0");
 attributes.put("encoding", "UTF-8");
 xmlDeclaration.attributes(attributes);

 XmlDeclaration parsedXmlDeclaration = Jsoup.parse("<declaration version=\"1.0\" encoding=\"UTF-8\"/>", "baseUri").xml();

 assertEquals("#declaration", parsedXmlDeclaration.nodeName());
 assertEquals("xml", xmlDeclaration.name());
 assertEquals("<?xml version=\"1.0\" encoding=\"UTF-8\"?>", xmlDeclaration.getWholeDeclaration());
 }

 @Test
 public void testXmlDeclarationWithProcessingInstruction() {
 XmlDeclaration xmlDeclaration = new XmlDeclaration("xml-pi", "baseUri", true);

 XmlDeclaration parsedXmlDeclaration = Jsoup.parse("<?xml-pi?>", "baseUri").xml();

 assertEquals("#declaration", parsedXmlDeclaration.nodeName());
 assertEquals("xml-pi", xmlDeclaration.name());
 assertEquals("<?xml-pi?>", xmlDeclaration.getWholeDeclaration());
 }

 @Test
 public void testXmlDeclarationWithEmptyNameAndProcessingInstruction() {
 XmlDeclaration xmlDeclaration = new XmlDeclaration(null, "baseUri", true);

 XmlDeclaration parsedXmlDeclaration = Jsoup.parse("<??>", "baseUri").xml();

 assertEquals("#declaration", parsedXmlDeclaration.nodeName());
 assertEquals(null, xmlDeclaration.name());
 assertEquals("<?>", xmlDeclaration.getWholeDeclaration());
 }

 @Test
 public void testXmlDeclarationToString() {
 XmlDeclaration xmlDeclaration = new XmlDeclaration("xml", "baseUri", true);

 assertEquals("<?xml?>", xmlDeclaration.toString());
 }

 @Test
 public void testXmlDeclarationOuterHtml() {
 XmlDeclaration xmlDeclaration = new XmlDeclaration("xml", "baseUri", true);

 StringBuilder accum = new StringBuilder();
 xmlDeclaration.outerHtmlHead(accum, 0, null);
 xmlDeclaration.outerHtmlTail(accum, 0, null);

 assertEquals("<?xml?>", accum.toString());
 }

 @Test
 public void testXmlDeclarationOuterHtmlHeadWithVersionAndEncoding() {
 XmlDeclaration xmlDeclaration = new XmlDeclaration("xml", "baseUri", true);
 Attributes attributes = new Attributes();
 attributes.put("version", "1.0");
 attributes.put("encoding", "UTF-8");
 xmlDeclaration.attributes(attributes);

 StringBuilder accum = new StringBuilder();
 xmlDeclaration.outerHtmlHead(accum, 0, null);

 assertEquals("<?xml version=\"1.0\" encoding=\"UTF-8\"?>", accum.toString());
 }
}