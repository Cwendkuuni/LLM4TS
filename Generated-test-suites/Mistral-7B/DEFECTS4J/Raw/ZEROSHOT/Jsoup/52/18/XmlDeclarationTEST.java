import org.jsoup.nodes.XmlDeclaration;
import org.junit.Test;
import static org.junit.Assert.*;

public class XmlDeclarationTest {

 @Test
 public void testXmlDeclarationName() {
 XmlDeclaration xmlDeclaration = new XmlDeclaration("xml", "", false);
 assertEquals("xml", xmlDeclaration.name());
 }

 @Test(expected = NullPointerException.class)
 public void testXmlDeclarationNameNull() {
 XmlDeclaration xmlDeclaration = new XmlDeclaration(null, "", false);
 // The test should fail with NullPointerException
 }

 @Test
 public void testGetWholeDeclarationEmptyDeclaration() {
 XmlDeclaration xmlDeclaration = new XmlDeclaration("", "", false);
 assertEquals("", xmlDeclaration.getWholeDeclaration());
 }

 @Test
 public void testGetWholeDeclarationWithVersionAndEncoding() {
 XmlDeclaration xmlDeclaration = new XmlDeclaration("xml", "1.0", "UTF-8");
 StringBuilder expected = new StringBuilder("xml version=\"1.0\" encoding=\"UTF-8\"");
 assertEquals(expected.toString(), xmlDeclaration.getWholeDeclaration());
 }

 @Test
 public void testGetWholeDeclarationWithVersion() {
 XmlDeclaration xmlDeclaration = new XmlDeclaration("xml", "1.0", "");
 StringBuilder expected = new StringBuilder("xml version=\"1.0\"");
 assertEquals(expected.toString(), xmlDeclaration.getWholeDeclaration());
 }

 @Test
 public void testGetWholeDeclarationWithEncoding() {
 XmlDeclaration xmlDeclaration = new XmlDeclaration("xml", "", "UTF-8");
 StringBuilder expected = new StringBuilder("xml encoding=\"UTF-8\"");
 assertEquals(expected.toString(), xmlDeclaration.getWholeDeclaration());
 }

 @Test
 public void testGetWholeDeclarationWithBothVersionAndEncoding() {
 XmlDeclaration xmlDeclaration = new XmlDeclaration("xml", "1.0", "UTF-8");
 StringBuilder expected = new StringBuilder("xml version=\"1.0\" encoding=\"UTF-8\"");
 assertEquals(expected.toString(), xmlDeclaration.getWholeDeclaration());
 }

 @Test
 public void testToString() {
 XmlDeclaration xmlDeclaration = new XmlDeclaration("xml", "1.0", "UTF-8");
 StringBuilder expected = new StringBuilder("<xml version=\"1.0\" encoding=\"UTF-8\">");
 assertEquals(expected.toString(), xmlDeclaration.toString());
 }

 @Test
 public void testOuterHtmlHead() throws IOException {
 Appendable accumulator = new StringBuilder();
 XmlDeclaration xmlDeclaration = new XmlDeclaration("xml", "1.0", "UTF-8");
 Document.OutputSettings settings = new Document.OutputSettings();
 xmlDeclaration.outerHtmlHead(accumulator, 0, settings);
 StringBuilder expected = new StringBuilder("<xml version=\"1.0\" encoding=\"UTF-8\">");
 assertEquals(expected.toString(), accumulator.toString());
 }

 @Test
 public void testOuterHtmlTail() throws IOException {
 Appendable accumulator = new StringBuilder();
 XmlDeclaration xmlDeclaration = new XmlDeclaration("xml", "1.0", "UTF-8");
 Document.OutputSettings settings = new Document.OutputSettings();
 xmlDeclaration.outerHtmlTail(accumulator, 0, settings);
 assertTrue(accumulator.toString().isEmpty());
 }
}