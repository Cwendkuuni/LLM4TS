import org.jsoup.parser.XmlTreeBuilder;
import org.junit.Test;
import org.jsoup.nodes.*;

public class XmlTreeBuilderTest {

    @Test
    public void testDefaultSettings() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        ParseSettings settings = xmlTreeBuilder.defaultSettings();
        assertEquals(ParseSettings.preserveCase, settings);
    }

    @Test
    public void testParse() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Document document = xmlTreeBuilder.parse("<xml></xml>", "http://example.com");
        assertNotNull(document);
    }

    // Add more test cases for other public methods

    @Test
    public void testInsertNode() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Element element = new Element(Tag.valueOf("test"), "http://example.com");
        xmlTreeBuilder.insertNode(element);
        assertEquals(element, xmlTreeBuilder.currentElement().childNode(0));
    }

    // Add more test cases for private methods

    @Test
    public void testParseFragment() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        List<Node> nodes = xmlTreeBuilder.parseFragment("<xml></xml>", "http://example.com", ParseErrorList.noTracing(), ParseSettings.preserveCase);
        assertEquals(1, nodes.size());
    }

}