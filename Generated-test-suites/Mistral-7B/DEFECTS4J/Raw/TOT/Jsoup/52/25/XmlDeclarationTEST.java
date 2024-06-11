import org.junit.Test;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.XmlDeclaration;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

public class XmlDeclarationTest {

 @Test
 public void testXmlDeclarationCreation() {
 XmlDeclaration xmlDeclaration = new XmlDeclaration("xml", "baseUri", false);
 assertEquals("#declaration", xmlDeclaration.nodeName());
 assertEquals("xml", xmlDeclaration.name());
 }

 @Test
 public void testXmlDeclarationWithVersion() {
 XmlDeclaration xmlDeclaration = new XmlDeclaration("xml", "baseUri", false);
 xmlDeclaration.attributes().put("version", "1.0");
 assertEquals("xml version=\"1.0\"", xmlDeclaration.getWholeDeclaration());
 }

 @Test
 public void testXmlDeclarationWithEncoding() {
 XmlDeclaration xmlDeclaration = new XmlDeclaration("xml", "baseUri", false);
 xmlDeclaration.attributes().put("encoding", "UTF-8");
 assertEquals("xml encoding=\"UTF-8\"", xmlDeclaration.getWholeDeclaration());
 }

 @Test
 public void testXmlDeclarationWithVersionAndEncoding() {
 XmlDeclaration xmlDeclaration = new XmlDeclaration("xml", "baseUri", false);
 xmlDeclaration.attributes().put("version", "1.0");
 xmlDeclaration.attributes().put("encoding", "UTF-8");
 assertEquals("xml version=\"1.0\" encoding=\"UTF-8\"", xmlDeclaration.getWholeDeclaration());
 }

 @Test
 public void testXmlDeclarationEmptyName() {
 assertThrows(NullPointerException.class, () -> new XmlDeclaration(null, "baseUri", false));
 }

 @Test
 public void testXmlDeclarationNullBaseUri() {
 assertThrows(NullPointerException.class, () -> new XmlDeclaration("xml", null, false));
 }

 @Test
 public void testXmlDeclarationProcessingInstruction() {
 XmlDeclaration xmlDeclaration = new XmlDeclaration("xml-pi", "baseUri", true);
 assertEquals("#processing-instruction", xmlDeclaration.nodeName());
 }

 @Test
 public void testXmlDeclarationToString() {
 XmlDeclaration xmlDeclaration = new XmlDeclaration("xml", "baseUri", false);
 assertEquals("<xml version=\"1.0\" encoding=\"UTF-8\">", xmlDeclaration.toString());
 }

 @Test
 public void testXmlDeclarationWithInvalidName() {
 assertThrows(IllegalArgumentException.class, () -> new XmlDeclaration("invalid", "baseUri", false));
 }

 @Test
 public void testXmlDeclarationWithEmptyGetWholeDeclaration() {
 XmlDeclaration xmlDeclaration = new XmlDeclaration("xml", "baseUri", false);
 assertEquals("xml", xmlDeclaration.getWholeDeclaration());
 }

 @Test
 public void testXmlDeclarationWithoutAttributes() {
 XmlDeclaration xmlDeclaration = new XmlDeclaration("xml", "baseUri", false);
 assertEquals("<xml/>", xmlDeclaration.toString());
 }

 @Test
 public void testXmlDeclarationWithInvalidAttributeName() {
 XmlDeclaration xmlDeclaration = new XmlDeclaration("xml", "baseUri", false);
 xmlDeclaration.attributes().put("invalid", "value");
 assertNull(xmlDeclaration.getWholeDeclaration());
 }

 @Test
 public void testXmlDeclarationWithEmptyAttributeValue() {
 XmlDeclaration xmlDeclaration = new XmlDeclaration("xml", "baseUri", false);
 xmlDeclaration.attributes().put("version", "");
 assertEquals("<xml version=\"\" />", xmlDeclaration.toString());
 }

 @Test
 public void testXmlDeclarationWithMixedAttributes() {
 XmlDeclaration xmlDeclaration = new XmlDeclaration("xml", "baseUri", false);
 xmlDeclaration.attributes().put("version", "1.0");
 xmlDeclaration.attributes().put("encoding", "");
 assertEquals("<xml version=\"1.0\" encoding=\"\" />", xmlDeclaration.toString());
 }
}