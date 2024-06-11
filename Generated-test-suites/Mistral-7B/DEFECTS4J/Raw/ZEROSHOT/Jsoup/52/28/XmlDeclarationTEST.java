import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.XmlDeclaration;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class XmlDeclarationTest {

 private XmlDeclaration xmlDeclaration;
 private Attributes attributes;

 @Before
 public void setUp() {
 attributes = new Attributes();
 xmlDeclaration = new XmlDeclaration("xml", "", false);
 }

 @Test
 public void testName() {
 String name = xmlDeclaration.name();
 assertEquals("xml", name);
 }

 @Test
 public void testGetWholeDeclaration() {
 String expectedDeclaration = "<xml version=\"1.0\" encoding=\"UTF-8\">";
 attributes.put("version", "1.0");
 attributes.put("encoding", "UTF-8");
 xmlDeclaration = new XmlDeclaration("xml", "", false, attributes);
 String actualDeclaration = xmlDeclaration.getWholeDeclaration();
 assertEquals(expectedDeclaration, actualDeclaration);
 }

 @Test
 public void testGetWholeDeclarationWithoutVersionAndEncoding() {
 String expectedDeclaration = "xml";
 String actualDeclaration = xmlDeclaration.getWholeDeclaration();
 assertEquals(expectedDeclaration, actualDeclaration);
 }

 @Test
 public void testToString() throws IOException {
 Document document = new Document();
 Appendable accumulator = document.outputSettings().buffer();
 xmlDeclaration.outerHtmlHead(accumulator, 0, document.outputSettings());
 xmlDeclaration.outerHtmlTail(accumulator, 0, document.outputSettings());
 String expectedHtml = "<!xml>";
 String actualHtml = xmlDeclaration.toString();
 assertEquals(expectedHtml, actualHtml);
 }

 @Test
 public void testIsProcessingInstruction() {
 boolean isProcessingInstruction = xmlDeclaration.isProcessingInstruction;
 assertFalse(isProcessingInstruction);

 xmlDeclaration = new XmlDeclaration("<?xml", "", true);
 isProcessingInstruction = xmlDeclaration.isProcessingInstruction;
 assertTrue(isProcessingInstruction);
 }

 @Test
 public void testNodeName() {
 String nodeName = xmlDeclaration.nodeName();
 assertEquals("#declaration", nodeName);
 }
}