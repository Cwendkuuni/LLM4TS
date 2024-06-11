import org.junit.Before;
import org.junit.Test;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.XmlDeclaration;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class XmlDeclarationTest {

 private XmlDeclaration xmlDeclaration;
 private Document document;

 @Before
 public void setUp() {
 document = new Document();
 xmlDeclaration = new XmlDeclaration("xml", null, false);
 }

 @Test
 public void testXmlDeclarationName() {
 String name = xmlDeclaration.name();
 assertEquals("xml", name);
 }

 @Test
 public void testGetWholeDeclaration() {
 String xmlDeclarationString = xmlDeclaration.getWholeDeclaration();
 assertEquals("<xml version=\"\" encoding=\"\">", xmlDeclarationString);
 }

 @Test
 public void testGetWholeDeclarationWithVersion() {
 Map<String, Attribute> attributes = new HashMap<>();
 attributes.put("version", new Attribute("version", "1.0"));
 XmlDeclaration xmlDeclarationWithVersion = new XmlDeclaration("xml", null, false, attributes);
 String xmlDeclarationStringWithVersion = xmlDeclarationWithVersion.getWholeDeclaration();
 assertEquals("<xml version=\"1.0\" encoding=\"\">", xmlDeclarationStringWithVersion);
 }

 @Test
 public void testGetWholeDeclarationWithVersionAndEncoding() {
 Map<String, Attribute> attributes = new HashMap<>();
 attributes.put("version", new Attribute("version", "1.0"));
 attributes.put("encoding", new Attribute("encoding", "UTF-8"));
 XmlDeclaration xmlDeclarationWithVersionAndEncoding = new XmlDeclaration("xml", null, false, attributes);
 String xmlDeclarationStringWithVersionAndEncoding = xmlDeclarationWithVersionAndEncoding.getWholeDeclaration();
 assertEquals("<xml version=\"1.0\" encoding=\"UTF-8\">", xmlDeclarationStringWithVersionAndEncoding);
 }

 @Test
 public void testOuterHtmlHead() throws IOException {
 Appendable accumulator = new StringBuffer();
 xmlDeclaration.outerHtmlHead(accumulator, 0, null);
 assertEquals("<!xml version=\"\" encoding=\"\">", accumulator.toString());
 }

 @Test
 public void testOuterHtmlHeadWithVersion() throws IOException {
 Map<String, Attribute> attributes = new HashMap<>();
 attributes.put("version", new Attribute("version", "1.0"));
 xmlDeclaration.outerHtmlHead(accumulator, 0, null);
 assertEquals("<!xml version=\"1.0\" encoding=\"\">", accumulator.toString());
 }

 @Test
 public void testOuterHtmlHeadWithVersionAndEncoding() throws IOException {
 Map<String, Attribute> attributes = new HashMap<>();
 attributes.put("version", new Attribute("version", "1.0"));
 attributes.put("encoding", new Attribute("encoding", "UTF-8"));
 xmlDeclaration.outerHtmlHead(accumulator, 0, null);
 assertEquals("<!xml version=\"1.0\" encoding=\"UTF-8\">", accumulator.toString());
 }

 @Test
 public void testToString() {
 String xmlDeclarationString = xmlDeclaration.toString();
 assertEquals("<xml version=\"\" encoding=\"\">", xmlDeclarationString);
 }

 @Test
 public void testCreateProcessingInstructionDeclaration() {
 XmlDeclaration processingInstructionDeclaration = new XmlDeclaration("xml-pi", null, true);
 String xmlDeclarationString = processingInstructionDeclaration.getWholeDeclaration();
 assertEquals("<?xml-pi?>", xmlDeclarationString);
 }

 @Test
 public void testCreateDeclaration() {
 XmlDeclaration declaration = new XmlDeclaration("declaration", null, false);
 String xmlDeclarationString = declaration.getWholeDeclaration();
 assertEquals("<declaration/>", xmlDeclarationString);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testCreateDeclarationWithNullName() {
 XmlDeclaration declaration = new XmlDeclaration(null, null, false);
 }
}