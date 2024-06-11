import org.jsoup.nodes.Document;
import org.jsoup.parser.XmlTreeBuilder;
import org.junit.Test;
import java.io.StringReader;
import java.util.List;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class XmlTreeBuilderTest {

    @Test
    public void testParseFromString() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Document doc = xmlTreeBuilder.parse("<xml><tag>Hello</tag></xml>", "http://example.com");
        assertEquals("http://example.com", doc.baseUri());
        assertEquals("xml", doc.child(0).nodeName());
        assertEquals("Hello", doc.select("tag").text());
    }

    @Test
    public void testParseFromReader() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Document doc = xmlTreeBuilder.parse(new StringReader("<xml><tag>World</tag></xml>"), "http://example.com");
        assertEquals("http://example.com", doc.baseUri());
        assertEquals("xml", doc.child(0).nodeName());
        assertEquals("World", doc.select("tag").text());
    }

    // Add more test cases for other public methods in XmlTreeBuilder class

    @Test
    public void testEdgeCasesAndExceptions() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();

        // Edge case: Testing with an empty input
        List<Node> nodes = xmlTreeBuilder.parseFragment("", "http://example.com", ParseErrorList.noTracking(), ParseSettings.preserveCase);
        assertTrue(nodes.isEmpty());

        // Exception handling: Testing with a malformed XML input
        try {
            xmlTreeBuilder.parse("<xml><tag></xml>", "http://example.com");
        } catch (Exception e) {
            assertTrue(e instanceof RuntimeException);
        }
    }
}