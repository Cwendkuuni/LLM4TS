import org.junit.Before;
import org.junit.Test;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.XmlDeclaration;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class XmlDeclarationTest {

 private XmlDeclaration xmlDeclaration;
 private Document document;

 @Before
 public void setUp() {
 document = new Document();
 xmlDeclaration = new XmlDeclaration("xml", document.baseUri(), false);
 }

 @Test
 public void testXmlDeclarationName() {
 assertEquals("#declaration", xmlDeclaration.nodeName());
 }

 @Test
 public void testXmlDeclarationNameWhenDeclarationIsXmlWithAttributes() {
 String xmlDeclarationName = new XmlDeclaration("xml", document.baseUri(), false).name();
 assertEquals("xml", xmlDeclarationName);
 String xmlDeclarationWithVersion = new XmlDeclaration("xml", document.baseUri(), false)
 .attributes(new Attribute("version", "1.0"), new Attribute("encoding", "UTF-8")).name();
 assertEquals("xml version=\"1.0\" encoding=\"UTF-8\"", xmlDeclarationWithVersion);
 }

 @Test
 public void testXmlDeclarationNameWhenProcessingInstruction() {
 XmlDeclaration processingInstruction = new XmlDeclaration("<?xml", document.baseUri(), true);
 assertEquals("#processing-instruction", processingInstruction.nodeName());
 }

 @Test
 public void testGetWholeDeclarationWhenDeclarationIsXml() {
 String xmlDeclaration = this.xmlDeclaration.getWholeDeclaration();
 assertEquals("xml", xmlDeclaration);
 }

 @Test
 public void testGetWholeDeclarationWhenDeclarationIsProcessingInstruction() {
 XmlDeclaration processingInstruction = new XmlDeclaration("<?xml", document.baseUri(), true);
 String processingInstructionDeclaration = processingInstruction.getWholeDeclaration();
 assertEquals("<?xml", processingInstructionDeclaration);
 }

 @Test
 public void testGetWholeDeclarationWithVersionAndEncodingAttributes() {
 XmlDeclaration xmlDeclarationWithVersionAndEncoding = new XmlDeclaration("xml", document.baseUri(), false)
 .attributes(new Attribute("version", "1.0"), new Attribute("encoding", "UTF-8"));
 String xmlDeclarationWithVersionAndEncodingString = xmlDeclarationWithVersionAndEncoding.getWholeDeclaration();
 assertEquals("xml version=\"1.0\" encoding=\"UTF-8\"", xmlDeclarationWithVersionAndEncodingString);
 }

 @Test
 public void testToStringMethodReturnsOuterHtml() {
 String expected = "<declaration xml:base=\"\">";
 assertEquals(expected, xmlDeclaration.toString());
 }

 @Test
 public void testToStringMethodReturnsCorrectOuterHtmlWhenDeclarationHasVersionAndEncodingAttributes() {
 String expected = "<declaration xml:base=\"\">xml version=\"1.0\" encoding=\"UTF-8\"";
 XmlDeclaration xmlDeclarationWithVersionAndEncoding = new XmlDeclaration("xml", document.baseUri(), false)
 .attributes(new Attribute("version", "1.0"), new Attribute("encoding", "UTF-8"));
 assertEquals(expected, xmlDeclarationWithVersionAndEncoding.toString());
 }

 @Test
 public void testToStringMethodReturnsCorrectOuterHtmlForProcessingInstruction() {
 String expected = "<?xml xml:base=\"\">";
 XmlDeclaration processingInstruction = new XmlDeclaration("<?xml", document.baseUri(), true);
 assertEquals(expected, processingInstruction.toString());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testConstructorThrowsIllegalArgumentExceptionWhenNameIsNull() {
 XmlDeclaration xmlDeclarationWithNullName = new XmlDeclaration(null, document.baseUri(), false);
 }

 @Test
 public void testOuterHtmlHeadMethod() throws IOException {
 StringBuilder expectedOutput = new StringBuilder();
 expectedOutput.append("<").append(xmlDeclaration.isProcessingInstruction ? "!" : "?")
 .append(xmlDeclaration.getWholeDeclaration())
 .append(">");
 Element xmlDeclarationElement = document.createElement("declaration");
 xmlDeclarationElement.outerHtmlHead(expectedOutput, 0, document.outputSettings());
 assertEquals(expectedOutput.toString(), xmlDeclarationElement.outerHtml());
 }

 @Test
 public void testOuterHtmlTailMethod() throws IOException {
 StringBuilder expectedOutput = new StringBuilder();
 xmlDeclarationElement.outerHtmlTail(expectedOutput, 0, document.outputSettings());
 assertNull(expectedOutput.toString());
 }
}