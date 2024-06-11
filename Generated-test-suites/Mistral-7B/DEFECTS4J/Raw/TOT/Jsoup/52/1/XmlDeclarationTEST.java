import org.junit.Test;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.XmlDeclaration;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class XmlDeclarationTest {

 @Test
 public void testXmlDeclarationWithNameAndVersion() {
 XmlDeclaration xmlDeclaration = new XmlDeclaration("xml", null, false);
 Attributes attributes = new Attributes();
 attributes.put("version", "1.0");
 xmlDeclaration.attributes = attributes;

 String expectedXmlDeclaration = "<declaration version=\"1.0\">";
 String actualXmlDeclaration = xmlDeclaration.getWholeDeclaration();

 assertEquals(expectedXmlDeclaration, actualXmlDeclaration);
 }

 @Test
 public void testXmlDeclarationWithNameAndEncoding() {
 XmlDeclaration xmlDeclaration = new XmlDeclaration("xml", null, false);
 Attributes attributes = new Attributes();
 attributes.put("encoding", "UTF-8");
 xmlDeclaration.attributes = attributes;

 String expectedXmlDeclaration = "<declaration encoding=\"UTF-8\">";
 String actualXmlDeclaration = xmlDeclaration.getWholeDeclaration();

 assertEquals(expectedXmlDeclaration, actualXmlDeclaration);
 }

 @Test
 public void testXmlDeclarationWithNameAndVersionAndEncoding() {
 XmlDeclaration xmlDeclaration = new XmlDeclaration("xml", null, false);
 Attributes attributes = new Attributes();
 attributes.put("version", "1.0");
 attributes.put("encoding", "UTF-8");
 xmlDeclaration.attributes = attributes;

 String expectedXmlDeclaration = "<declaration version=\"1.0\" encoding=\"UTF-8\">";
 String actualXmlDeclaration = xmlDeclaration.getWholeDeclaration();

 assertEquals(expectedXmlDeclaration, actualXmlDeclaration);
 }

 @Test
 public void testXmlDeclarationWithEmptyName() {
 XmlDeclaration xmlDeclaration = new XmlDeclaration(null, null, false);

 assertNull(xmlDeclaration.name);
 }

 @Test(expected = NullPointerException.class)
 public void testXmlDeclarationWithNullName() {
 XmlDeclaration xmlDeclaration = new XmlDeclaration(null, null, false);

 assertNull(xmlDeclaration.name);
 xmlDeclaration.name = null;
 }

 @Test
 public void testXmlDeclarationWithProcessingInstruction() {
 XmlDeclaration xmlDeclaration = new XmlDeclaration("xml-pi", null, true);

 String expectedXmlDeclaration = "<?xml-pi>";
 String actualXmlDeclaration = xmlDeclaration.getWholeDeclaration();

 assertEquals(expectedXmlDeclaration, actualXmlDeclaration);
 }

 @Test
 public void testOuterHtmlHead() throws IOException {
 String xmlDeclarationXml = "<declaration version=\"1.0\" encoding=\"UTF-8\">";
 Document document = Jsoup.parse("<root>" + xmlDeclarationXml + "</root>");
 XmlDeclaration xmlDeclaration = (XmlDeclaration) document.child(0);

 Appendable accum = new StringBuilder();
 xmlDeclaration.outerHtmlHead(accum, 0, null);

 String expectedXmlDeclaration = "<!DOCTYPE root [ <!ENTITY xml-decl " + xmlDeclarationXml + "> ]>\n<root>";
 String actualXmlDeclaration = accum.toString();

 assertEquals(expectedXmlDeclaration, actualXmlDeclaration);
 }

 @Test
 public void testToString() {
 XmlDeclaration xmlDeclaration = new XmlDeclaration("xml", null, false);

 String expectedXmlDeclaration = xmlDeclaration.getWholeDeclaration();
 String actualXmlDeclaration = xmlDeclaration.toString();

 assertEquals(expectedXmlDeclaration, actualXmlDeclaration);
 }

 @Test
 public void testXmlDeclarationWithMultipleAttributes() {
 XmlDeclaration xmlDeclaration = new XmlDeclaration("xml", null, false);
 Attributes attributes = new Attributes();
 attributes.put("version", "1.0");
 attributes.put("encoding", "UTF-8");
 xmlDeclaration.attributes = attributes;

 String expectedXmlDeclaration = "<declaration version=\"1.0\" encoding=\"UTF-8\">";
 String actualXmlDeclaration = xmlDeclaration.getWholeDeclaration();

 assertEquals(expectedXmlDeclaration, actualXmlDeclaration);
 }

 @Test
 public void testXmlDeclarationWithNoAttributes() {
 XmlDeclaration xmlDeclaration = new XmlDeclaration("xml", null, false);

 String expectedXmlDeclaration = "<declaration>";
 String actualXmlDeclaration = xmlDeclaration.getWholeDeclaration();

 assertEquals(expectedXmlDeclaration, actualXmlDeclaration);
 }
}