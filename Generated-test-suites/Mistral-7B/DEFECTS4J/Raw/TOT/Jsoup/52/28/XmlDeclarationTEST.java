import org.junit.Test;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.XmlDeclaration;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class XmlDeclarationTest {

 @Test
 public void testXmlDeclarationCreation() {
 XmlDeclaration xmlDeclaration = new XmlDeclaration("xml", null, false);
 assertEquals("#declaration", xmlDeclaration.nodeName());
 assertEquals("xml", xmlDeclaration.name());
 assertNull(xmlDeclaration.getWholeDeclaration());
 }

 @Test
 public void testXmlDeclarationWithVersionAttribute() {
 XmlDeclaration xmlDeclaration = new XmlDeclaration("xml", null, false);
 xmlDeclaration.attributes().put("version", "1.0");
 assertEquals("xml version=\"1.0\"", xmlDeclaration.getWholeDeclaration());
 }

 @Test
 public void testXmlDeclarationWithEncodingAttribute() {
 XmlDeclaration xmlDeclaration = new XmlDeclaration("xml", null, false);
 xmlDeclaration.attributes().put("encoding", "UTF-8");
 assertEquals("xml encoding=\"UTF-8\"", xmlDeclaration.getWholeDeclaration());
 }

 @Test
 public void testXmlDeclarationWithBothVersionAndEncodingAttributes() {
 XmlDeclaration xmlDeclaration = new XmlDeclaration("xml", null, false);
 xmlDeclaration.attributes().put("version", "1.0");
 xmlDeclaration.attributes().put("encoding", "UTF-8");
 assertEquals("xml version=\"1.0\" encoding=\"UTF-8\"", xmlDeclaration.getWholeDeclaration());
 }

 @Test
 public void testXmlDeclarationProcessingInstruction() {
 XmlDeclaration xmlDeclaration = new XmlDeclaration("xml-pi", null, true);
 assertEquals("#processing-instruction xml-pi", xmlDeclaration.nodeName());
 assertEquals("xml-pi", xmlDeclaration.name());
 assertEquals("<?xml-pi?>", xmlDeclaration.getWholeDeclaration());
 }

 @Test
 public void testXmlDeclarationToString() {
 XmlDeclaration xmlDeclaration = new XmlDeclaration("xml", null, false);
 xmlDeclaration.attributes().put("version", "1.0");
 xmlDeclaration.attributes().put("encoding", "UTF-8");
 assertEquals("<xml version=\"1.0\" encoding=\"UTF-8\"></xml>", xmlDeclaration.toString());
 }

 @Test
 public void testXmlDeclarationWithEmptyName() {
 XmlDeclaration xmlDeclaration = new XmlDeclaration(null, null, false);
 assertNull(xmlDeclaration.name());
 assertNull(xmlDeclaration.getWholeDeclaration());
 }

 @Test(expected = NullPointerException.class)
 public void testXmlDeclarationWithNullName() {
 XmlDeclaration xmlDeclaration = new XmlDeclaration(null, null, false);
 }
}