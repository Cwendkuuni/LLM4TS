import org.junit.Test;
import static org.junit.Assert.*;
import org.jsoup.parser.XmlTreeBuilder;
import org.jsoup.nodes.Document;

public class XmlTreeBuilderTest {

    @Test
    public void testDefaultSettings() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        assertEquals(ParseSettings.preserveCase, builder.defaultSettings());
    }

    @Test
    public void testParse() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Document doc = builder.parse("<xml></xml>", "http://example.com");
        assertNotNull(doc);
    }

    @Test
    public void testInsertNode() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        builder.initialiseParse("<xml></xml>", "http://example.com", ParseErrorList.noTracking(), ParseSettings.preserveCase);
        builder.insertNode(new TextNode("test text", "http://example.com"));
        assertNotNull(builder.currentElement().childNode(0));
    }

    // Add more test cases for other methods here

}