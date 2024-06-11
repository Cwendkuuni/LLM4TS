import org.jsoup.parser.*;
import org.jsoup.nodes.Document;
import org.junit.Test;
import java.util.List;

public class XmlTreeBuilderTest {

    @Test
    public void testDefaultSettings() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        ParseSettings settings = xmlTreeBuilder.defaultSettings();

        // Assert that default settings are as expected
        assertEquals(ParseSettings.preserveCase, settings);
    }

    @Test
    public void testParse() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        String input = "<xml><tag>Hello</tag></xml>";
        String baseUri = "https://example.com";
        Document document = xmlTreeBuilder.parse(input, baseUri);

        // Assert that parsing produces the expected document
        assertNotNull(document);
        assertEquals("xml", document.nodeName());
        assertEquals("Hello", document.getElementsByTag("tag").text());
    }

    @Test
    public void testInsertNode() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Node node = new TextNode("Test Node", "https://example.com");
        xmlTreeBuilder.insertNode(node);

        // Assert that the node is correctly added to the current element
        assertEquals(node, xmlTreeBuilder.currentElement().childNode(0));
    }

    // Add more test cases for other methods as needed

}