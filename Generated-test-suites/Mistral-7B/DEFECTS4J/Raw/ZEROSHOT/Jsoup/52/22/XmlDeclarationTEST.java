import org.junit.Before;
import org.junit.Test;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.XmlDeclaration;
import static org.junit.Assert.*;

public class XmlDeclarationTest {

 private Document document;

 @Before
 public void setUp() {
 document = new Document();
 }

 @Test
 public void testXmlDeclarationCreation() {
 XmlDeclaration xmlDeclaration = new XmlDeclaration("xml", null, false);
 assertEquals("#declaration", xmlDeclaration.nodeName());
 assertEquals("xml", xmlDeclaration.name());
 assertNull(xmlDeclaration.getWholeDeclaration());

 XmlDeclaration xmlProcessingInstruction = new XmlDeclaration("xml", null, true);
 assertEquals("#declaration", xmlProcessingInstruction.nodeName());
 assertEquals("xml", xmlProcessingInstruction.name());
 assertEquals("<?xml?>", xmlProcessingInstruction.getWholeDeclaration());
 }

 @Test
 public void testXmlDeclarationWithAttributes() {
 XmlDeclaration xmlDeclaration = new XmlDeclaration("xml", null, false);
 xmlDeclaration.attributes().put("version", "1.0");
 xmlDeclaration.attributes().put("encoding", "UTF-8");
 assertEquals("<?xml version=\"1.0\" encoding=\"UTF-8\"?>", xmlDeclaration.getWholeDeclaration());
 }

 @Test
 public void testOuterHtmlHead() throws IOException {
 Appendable appendable = new StringBuilder();
 XmlDeclaration xmlDeclaration = new XmlDeclaration("xml", null, false);
 xmlDeclaration.outerHtmlHead(appendable, 0, null);
 assertEquals("<?xml?>", appendable.toString());

 XmlDeclaration xmlProcessingInstruction = new XmlDeclaration("xml", null, true);
 xmlProcessingInstruction.outerHtmlHead(appendable, 0, null);
 assertEquals("<?xml?>", appendable.toString());

 XmlDeclaration xmlDeclarationWithVersion = new XmlDeclaration("xml", null, false);
 xmlDeclarationWithVersion.attributes().put("version", "1.0");
 xmlDeclarationWithVersion.outerHtmlHead(appendable, 0, null);
 assertEquals("<?xml version=\"1.0\"?>", appendable.toString());

 XmlDeclaration xmlDeclarationWithEncoding = new XmlDeclaration("xml", null, false);
 xmlDeclarationWithEncoding.attributes().put("encoding", "UTF-8");
 xmlDeclarationWithEncoding.outerHtmlHead(appendable, 0, null);
 assertEquals("<?xml encoding=\"UTF-8\"?>", appendable.toString());

 XmlDeclaration xmlDeclarationWithVersionAndEncoding = new XmlDeclaration("xml", null, false);
 xmlDeclarationWithVersionAndEncoding.attributes().put("version", "1.0");
 xmlDeclarationWithVersionAndEncoding.attributes().put("encoding", "UTF-8");
 xmlDeclarationWithVersionAndEncoding.outerHtmlHead(appendable, 0, null);
 assertEquals("<?xml version=\"1.0\" encoding=\"UTF-8\"?>", appendable.toString());
 }

 @Test
 public void testOuterHtmlTail() throws IOException {
 Appendable appendable = new StringBuilder();
 XmlDeclaration xmlDeclaration = new XmlDeclaration("xml", null, false);
 xmlDeclaration.outerHtmlTail(appendable, 0, null);
 assertTrue(appendable.toString().isEmpty());

 XmlDeclaration xmlProcessingInstruction = new XmlDeclaration("xml", null, true);
 xmlProcessingInstruction.outerHtmlTail(appendable, 0, null);
 assertTrue(appendable.toString().isEmpty());
 }

 @Test
 public void testToString() {
 XmlDeclaration xmlDeclaration = new XmlDeclaration("xml", null, false);
 assertEquals("<?xml?>", xmlDeclaration.toString());

 XmlDeclaration xmlProcessingInstruction = new XmlDeclaration("xml", null, true);
 assertEquals("<?xml?>", xmlProcessingInstruction.toString());

 XmlDeclaration xmlDeclarationWithVersion = new XmlDeclaration("xml", null, false);
 xmlDeclarationWithVersion.attributes().put("version", "1.0");
 assertEquals("<?xml version=\"1.0\"?>", xmlDeclarationWithVersion.toString());

 XmlDeclaration xmlDeclarationWithEncoding = new XmlDeclaration("xml", null, false);
 xmlDeclarationWithEncoding.attributes().put("encoding", "UTF-8");
 assertEquals("<?xml encoding=\"UTF-8\"?>", xmlDeclarationWithEncoding.toString());

 XmlDeclaration xmlDeclarationWithVersionAndEncoding = new XmlDeclaration("xml", null, false);
 xmlDeclarationWithVersionAndEncoding.attributes().put("version", "1.0");
 xmlDeclarationWithVersionAndEncoding.attributes().put("encoding", "UTF-8");
 assertEquals("<?xml version=\"1.0\" encoding=\"UTF-8\"?>", xmlDeclarationWithVersionAndEncoding.toString());
 }
}