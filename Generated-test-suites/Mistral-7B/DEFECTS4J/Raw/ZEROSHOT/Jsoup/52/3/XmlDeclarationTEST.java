import org.jsoup.nodes.XmlDeclaration;
import org.junit.Test;
import static org.junit.Assert.*;

public class XmlDeclarationTest {

 @Test
 public void testXmlDeclarationCreation() {
 XmlDeclaration declaration = new XmlDeclaration("xml", "baseUri", false);
 assertEquals("#declaration", declaration.nodeName());
 assertEquals("xml", declaration.name());
 assertEquals("<?xml>\n", declaration.getWholeDeclaration());
 }

 @Test(expected = NullPointerException.class)
 public void testXmlDeclarationNameIsNull() {
 XmlDeclaration declaration = new XmlDeclaration(null, "baseUri", false);
 fail("Expected NullPointerException but got: " + declaration);
 }

 @Test
 public void testXmlDeclarationWithVersion() {
 XmlDeclaration declaration = new XmlDeclaration("xml", "baseUri", false);
 declaration.attributes().put("version", "1.0");
 assertEquals("<?xml version=\"1.0\">\n", declaration.getWholeDeclaration());
 }

 @Test
 public void testXmlDeclarationWithEncoding() {
 XmlDeclaration declaration = new XmlDeclaration("xml", "baseUri", false);
 declaration.attributes().put("encoding", "UTF-8");
 assertEquals("<?xml encoding=\"UTF-8\">\n", declaration.getWholeDeclaration());
 }

 @Test
 public void testXmlDeclarationWithVersionAndEncoding() {
 XmlDeclaration declaration = new XmlDeclaration("xml", "baseUri", false);
 declaration.attributes().put("version", "1.0");
 declaration.attributes().put("encoding", "UTF-8");
 assertEquals("<?xml version=\"1.0\" encoding=\"UTF-8\">\n", declaration.getWholeDeclaration());
 }

 @Test
 public void testXmlDeclarationAsProcessingInstruction() {
 XmlDeclaration declaration = new XmlDeclaration("xml", "baseUri", true);
 assertEquals("#processing-instruction-xml", declaration.nodeName());
 assertEquals("<?xml>\n", declaration.getWholeDeclaration());
 }

 @Test
 public void testXmlDeclarationToString() {
 String declaration = new XmlDeclaration("xml", "baseUri", false).toString();
 assertEquals("<?xml>\n", declaration);
 }

 @Test
 public void testXmlDeclarationOuterHtmlHead() throws IOException {
 Appendable accumulator = new StringBuffer();
 XmlDeclaration declaration = new XmlDeclaration("xml", "baseUri", false);
 declaration.outerHtmlHead(accumulator, 0, null);
 assertEquals("<?xml>\n", accumulator.toString());
 }

 @Test
 public void testXmlDeclarationOuterHtmlTail() throws IOException {
 Appendable accumulator = new StringBuffer();
 XmlDeclaration declaration = new XmlDeclaration("xml", "baseUri", false);
 declaration.outerHtmlTail(accumulator, 0, null);
 assertTrue(accumulator.toString().isEmpty());
 }

}