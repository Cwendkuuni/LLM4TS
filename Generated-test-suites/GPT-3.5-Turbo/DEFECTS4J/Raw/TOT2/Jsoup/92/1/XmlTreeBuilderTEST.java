import org.jsoup.nodes.Document;
import org.junit.Before;
import org.junit.Test;

import java.io.StringReader;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class XmlTreeBuilderTest {

    private XmlTreeBuilder xmlTreeBuilder;

    @Before
    public void setup() {
        xmlTreeBuilder = new XmlTreeBuilder();
    }

    @Test
    public void testParseReaderStringParser() {
        Document doc = xmlTreeBuilder.parse(new StringReader("<html></html>"), "http://example.com");
        assertNotNull(doc);
    }

    @Test
    public void testInsertNode() {
        // TODO: Insert edge case for node insertion
    }

    @Test
    public void testInsertStartElementTag() {
        // TODO: Insert edge cases for start element tag
    }

    @Test
    public void testInsertComment() {
        // TODO: Insert edge cases for comment insertion
    }

    @Test
    public void testInsertCharacter() {
        // TODO: Insert edge cases for character insertion
    }

    @Test
    public void testInsertDoctype() {
        // TODO: Insert edge cases for doctype insertion
    }

    @Test
    public void testPopStackToClose() {
        // TODO: Insert edge case for stack popping
    }

    @Test
    public void testParseFragment() {
        List<Node> nodes = xmlTreeBuilder.parseFragment("<p>Test</p>", "http://example.com", new Parser(xmlTreeBuilder));
        assertEquals(1, nodes.size());
    }

}