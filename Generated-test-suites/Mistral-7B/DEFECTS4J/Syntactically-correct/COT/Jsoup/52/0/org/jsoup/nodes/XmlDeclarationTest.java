import static org.junit.Assert.*;
import org.jsoup.nodes.XmlDeclaration;
import org.jsoup.nodes.Attributes;
import org.jsoup.helper.Validate;
import org.junit.Test;

public class XmlDeclarationTest {

 private static final String TEST_XML_DECLARATION = "xml version=\"1.0\" encoding=\"UTF-8\"";
 private static final String TEST_PROCESSING_INSTruction_XML_DECLARATION = "<?xml-processing-instruction version=\"1.0\" encoding=\"UTF-8\"?>";

 @Test
 public void testXmlDeclarationConstructor() {
 XmlDeclaration xmlDeclaration = new XmlDeclaration("xml", null, false);
 assertEquals("#declaration", xmlDeclaration.nodeName());
 assertEquals("xml", xmlDeclaration.name());
 assertEquals(TEST_XML_DECLARATION, xmlDeclaration.getWholeDeclaration());
 }

 @Test
 public void testXmlDeclarationConstructorWithProcessingInstruction() {
 XmlDeclaration xmlDeclaration = new XmlDeclaration("xml", null, true);
 assertEquals("#declaration", xmlDeclaration.nodeName());
 assertEquals("xml", xmlDeclaration.name());
 assertFalse(xmlDeclaration.getWholeDeclaration().startsWith("!"));
 }

 @Test
 public void testGetWholeDeclarationWithEmptyAttributes() {
 XmlDeclaration xmlDeclaration = new XmlDeclaration("xml", null, false);
 assertEquals(TEST_XML_DECLARATION, xmlDeclaration.getWholeDeclaration());
 }

 @Test
 public void testGetWholeDeclarationWithVersionAttribute() {
 XmlDeclaration xmlDeclaration = new XmlDeclaration("xml", null, false);
 Attributes attributes = new Attributes();
 attributes.put("version", "1.1");
 xmlDeclaration.setAttributes(attributes);
 assertEquals("xml version=\"1.1\"", xmlDeclaration.getWholeDeclaration());
 }

 @Test
 public void testGetWholeDeclarationWithEncodingAttribute() {
 XmlDeclaration xmlDeclaration = new XmlDeclaration("xml", null, false);
 Attributes attributes = new Attributes();
 attributes.put("encoding", "UTF-16");
 xmlDeclaration.setAttributes(attributes);
 assertEquals("xml version=\"1.0\" encoding=\"UTF-16\"", xmlDeclaration.getWholeDeclaration());
 }

 @Test
 public void testGetWholeDeclarationWithVersionAndEncodingAttributes() {
 XmlDeclaration xmlDeclaration = new XmlDeclaration("xml", null, false);
 Attributes attributes = new Attributes();
 attributes.put("version", "1.2");
 attributes.put("encoding", "UTF-16");
 xmlDeclaration.setAttributes(attributes);
 assertEquals("xml version=\"1.2\" encoding=\"UTF-16\"", xmlDeclaration.getWholeDeclaration());
 }

 @Test
 public void testGetWholeDeclarationWithNoName() {
 Validate.expectedException(NullPointerException.class, () -> new XmlDeclaration(null, null, false));
 }

 @Test
 public void testSetAttributesWithNullAttributes() {
 Validate.expectedException(NullPointerException.class, () -> new XmlDeclaration("xml", null, false).setAttributes(null));
 }

 @Test
 public void testOuterHtmlHead() throws IOException {
 XmlDeclaration xmlDeclaration = new XmlDeclaration("xml", null, false);
 Appendable accum = new StringBuilder();
 xmlDeclaration.outerHtmlHead(accum, 0, null);
 assertEquals("<xml version=\"1.0\">", accum.toString());
 }

 @Test
 public void testOuterHtmlHeadWithProcessingInstruction() throws IOException {
 XmlDeclaration xmlDeclaration = new XmlDeclaration("xml", null, true);
 Appendable accum = new StringBuilder();
 xmlDeclaration.outerHtmlHead(accum, 0, null);
 assertEquals("<?xml version=\"1.0\"?>", accum.toString());
 }

 @Test
 public void testOuterHtmlTail() throws IOException {
 XmlDeclaration xmlDeclaration = new XmlDeclaration("xml", null, false);
 Appendable accum = new StringBuilder();
 xmlDeclaration.outerHtmlTail(accum, 0, null);
 assertTrue(accum.toString().isEmpty());
 }

 @Test
 public void testToString() {
 XmlDeclaration xmlDeclaration = new XmlDeclaration("xml", null, false);
 assertEquals(TEST_XML_DECLARATION, xmlDeclaration.toString());
 }
}