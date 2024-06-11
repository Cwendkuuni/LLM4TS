import org.junit.Test;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.XmlDeclaration;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class XmlDeclarationTest {

 @Test
 public void testXmlDeclarationWithNameAndVersion() {
 String xml = "<xml version=\"1.0\" encoding=\"UTF-8\">";
 Document doc = Jsoup.parse(xml);
 XmlDeclaration declaration = (XmlDeclaration) doc.head();
 assertNotNull(declaration);
 assertEquals("xml", declaration.name());
 assertEquals("1.0", declaration.getWholeDeclaration().split("version=")[1].split("\"")[0]);
 }

 @Test
 public void testXmlDeclarationWithNameAndEncoding() {
 String xml = "<xml encoding=\"UTF-8\">";
 Document doc = Jsoup.parse(xml);
 XmlDeclaration declaration = (XmlDeclaration) doc.head();
 assertNotNull(declaration);
 assertEquals("xml", declaration.name());
 assertNull(declaration.getWholeDeclaration().split("version="));
 assertEquals("UTF-8", declaration.getWholeDeclaration().split("encoding=")[1].split("\"")[0]);
 }

 @Test
 public void testXmlDeclarationWithNameAndVersionAndEncoding() {
 String xml = "<xml version=\"1.0\" encoding=\"UTF-8\">";
 Document doc = Jsoup.parse(xml);
 XmlDeclaration declaration = (XmlDeclaration) doc.head();
 assertNotNull(declaration);
 assertEquals("xml", declaration.name());
 assertEquals("1.0", declaration.getWholeDeclaration().split("version=")[1].split("\"")[0]);
 assertEquals("UTF-8", declaration.getWholeDeclaration().split("encoding=")[1].split("\"")[0]);
 }

 @Test(expected = NullPointerException.class)
 public void testNullName() {
 String xml = "<xml version=\"1.0\" encoding=\"UTF-8\">"; // name should not be null
 Document doc = Jsoup.parse(xml);
 XmlDeclaration declaration = new XmlDeclaration(null, "", true); // but we're passing null here
 doc.insertBefore(declaration, doc.head());
 assertEquals("#declaration", declaration.nodeName()); // this test will pass but the following one will throw the expected exception
 }

 @Test
 public void testXmlDeclarationProcessingInstruction() {
 String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";
 Document doc = Jsoup.parse(xml);
 XmlDeclaration declaration = (XmlDeclaration) doc.head();
 assertNotNull(declaration);
 assertEquals("#processing-instruction-xml", declaration.nodeName());
 }

 @Test
 public void testXmlDeclarationWithEmptyName() {
 String xml = "<! xml version=\"1.0\" encoding=\"UTF-8\"?>";
 Document doc = Jsoup.parse(xml);
 XmlDeclaration declaration = (XmlDeclaration) doc.head();
 assertNotNull(declaration);
 assertEquals("#processing-instruction-xml", declaration.nodeName());
 }

 @Test
 public void testXmlDeclarationWithInvalidVersion() {
 String xml = "<xml version=\"badVersion\">";
 Document doc = Jsoup.parse(xml);
 XmlDeclaration declaration = (XmlDeclaration) doc.head();
 assertNotNull(declaration);
 assertEquals("xml", declaration.name());
 assertNull(declaration.getWholeDeclaration().split("version="));
 }

 @Test
 public void testXmlDeclarationWithInvalidEncoding() {
 String xml = "<xml encoding=\"badEncoding\">";
 Document doc = Jsoup.parse(xml);
 XmlDeclaration declaration = (XmlDeclaration) doc.head();
 assertNotNull(declaration);
 assertEquals("xml", declaration.name());
 assertNull(declaration.getWholeDeclaration().split("encoding="));
 }

 @Test
 public void testXmlDeclarationWithEmptyAttributes() {
 String xml = "<xml/>";
 Document doc = Jsoup.parse(xml);
 XmlDeclaration declaration = (XmlDeclaration) doc.head();
 assertNotNull(declaration);
 assertEquals("xml", declaration.name());
 assertNull(declaration.getWholeDeclaration().split("version="));
 assertNull(declaration.getWholeDeclaration().split("encoding="));
 }

 @Test
 public void testXmlDeclarationWithWhitespaceAttributes() {
 String xml = "<xml version=\"1.0\" encoding=\"UTF-8\" >";
 Document doc = Jsoup.parse(xml);
 XmlDeclaration declaration = (XmlDeclaration) doc.head();
 assertNotNull(declaration);
 assertEquals("xml", declaration.name());
 assertEquals("1.0", declaration.getWholeDeclaration().split("version=")[1].split("\"")[0]);
 assertEquals("UTF-8", declaration.getWholeDeclaration().split("encoding=")[1].split("\"")[0]);
 }
}