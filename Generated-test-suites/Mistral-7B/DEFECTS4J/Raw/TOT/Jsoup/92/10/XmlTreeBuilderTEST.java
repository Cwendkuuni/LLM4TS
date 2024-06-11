import org.junit.Before;
import org.junit.Test;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.XmlTreeBuilder;
import java.io.IOException;
import java.util.List;

public class XmlTreeBuilderTest {

 private XmlTreeBuilder xmlTreeBuilder;

 @Before
 public void setUp() {
 xmlTreeBuilder = new XmlTreeBuilder();
 }

 @Test
 public void testParseEmptyXml() throws IOException {
 String xml = "<>";
 Document doc = xmlTreeBuilder.parse(new StringReader(xml));
 assertNotNull(doc);
 assertFalse(doc.body().childNodes().isEmpty());
 }

 @Test
 public void testParseSimpleXml() throws IOException {
 String xml = "<book><title>Title</title><author>Author</author></book>";
 Document doc = xmlTreeBuilder.parse(new StringReader(xml));
 Element root = doc.rootElement();
 assertEquals("book", root.tagName());
 Element title = root.getElementsByTag("title").first();
 assertEquals("Title", title.getText());
 Element author = root.getElementsByTag("author").first();
 assertEquals("Author", author.getText());
 }

 @Test
 public void testParseXmlWithSelfClosingTag() throws IOException {
 String xml = "<book><title><![CDATA[This is CDATA]]></title></book>";
 Document doc = xmlTreeBuilder.parse(new StringReader(xml));
 Element root = doc.rootElement();
 Element title = root.getElementsByTag("title").first();
 assertTrue(title.childNodes().size() == 1);
 CDataNode cdataNode = (CDataNode) title.childNodes().get(0);
 assertEquals("This is CDATA", cdataNode.getData());
 }

 @Test
 public void testParseXmlWithDoctype() throws IOException {
 String xml = "<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Strict//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd\">\n" +
 "<html xmlns=\"http://www.w3.org/1999/xhtml\">\n" +
 "<head>\n" +
 "<title>Title</title>\n" +
 "</head>\n" +
 "<body>\n" +
 "<p>Content</p>\n" +
 "</body>\n" +
 "</html>";
 Document doc = xmlTreeBuilder.parse(new StringReader(xml));
 DocumentType doctype = doc.doctype();
 assertEquals("html", doctype.name());
 assertEquals("html", doctype.nodeName());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseInvalidXml() throws IOException {
 String xml = "<book<title>"; // invalid xml
 Document doc = xmlTreeBuilder.parse(new StringReader(xml));
 fail("Expected an IllegalArgumentException");
 }

 @Test
 public void testParseFragment() throws IOException {
 String xml = "<book><title>Title</title></book>";
 Document doc = Jsoup.parse(xml);
 List<Node> nodes = xmlTreeBuilder.parseFragment(xml, null, null, null);
 assertEquals(1, nodes.size());
 Element root = (Element) nodes.get(0);
 assertEquals("book", root.tagName());
 }
}