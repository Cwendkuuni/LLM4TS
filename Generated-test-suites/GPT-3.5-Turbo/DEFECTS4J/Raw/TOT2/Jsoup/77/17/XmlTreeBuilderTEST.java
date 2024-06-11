import org.jsoup.nodes.Document;
import org.junit.Test;
import static org.junit.Assert.*;
import java.io.StringReader;

public class XmlTreeBuilderTest {

    @Test
    public void testParse() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Document document = xmlTreeBuilder.parse(new StringReader("<xml></xml>"), "http://example.com");
        assertNotNull(document);
    }

    @Test
    public void testInsert() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Document document = xmlTreeBuilder.parse(new StringReader("<xml></xml>"), "http://example.com");
        assertNotNull(document);
        // Insert element
        xmlTreeBuilder.insert(new Token.StartTag("tag", "<tag>", true));
        assertEquals(2, document.getAllElements().size()); // Check if element was inserted
    }

    @Test
    public void testInsertNode() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Document document = xmlTreeBuilder.parse(new StringReader("<xml></xml>"), "http://example.com");
        assertNotNull(document);
        xmlTreeBuilder.insertNode(new TextNode("test"));
        assertEquals(1, document.textNodes().size()); // Check if text node was inserted
    }

    // Add more test methods for other public methods in XmlTreeBuilder class

}