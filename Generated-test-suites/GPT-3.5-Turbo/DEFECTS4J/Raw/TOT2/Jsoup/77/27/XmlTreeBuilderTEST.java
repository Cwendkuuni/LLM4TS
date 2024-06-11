import org.jsoup.nodes.Document;
import org.jsoup.parser.XmlTreeBuilder;
import org.junit.Test;

import java.io.StringReader;

import static org.junit.Assert.assertEquals;

public class XmlTreeBuilderTest {

    @Test
    public void testDefaultSettings() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        assertEquals(ParseSettings.preserveCase, xmlTreeBuilder.defaultSettings());
    }

    @Test
    public void testParseWithStringReader() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Document doc = xmlTreeBuilder.parse(new StringReader("<html></html>"), "http://example.com");
        // Add assertions here for the parsing result
    }

    @Test
    public void testInsertNode() {
        // Test the insertion of a node into the document tree
        // Add necessary setup and assertions here
    }

    // Add more test cases for other public methods in XmlTreeBuilder class

    @Test
    public void testParseFragment() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        String inputFragment = "<div>Hello world</div>";
        List<Node> nodes = xmlTreeBuilder.parseFragment(inputFragment, "http://example.com", ParseErrorList.noTracking(), ParseSettings.preserveCase);
        // Add assertions here for the parsed fragment nodes
    }

}