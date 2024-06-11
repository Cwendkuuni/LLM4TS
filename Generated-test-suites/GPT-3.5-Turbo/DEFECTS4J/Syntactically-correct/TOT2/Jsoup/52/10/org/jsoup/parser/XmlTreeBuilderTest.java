import org.jsoup.nodes.Document;
import org.jsoup.parser.XmlTreeBuilder;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class XmlTreeBuilderTest {

    @Test
    public void testInitialiseParse() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        String input = "<html><head></head><body></body></html>";
        String baseUri = "http://example.com";
        xmlTreeBuilder.initialiseParse(input, baseUri, null);
        Document doc = xmlTreeBuilder.getDocument();
        assertEquals("http://example.com", doc.baseUri());
    }

    @Test
    public void testProcess() {
        // Test process method with different token types
        // Add test cases here
    }

    @Test
    public void testInsertNode() {
        // Test insertNode method
        // Add test cases here
    }

    @Test
    public void testInsertStartTag() {
        // Test insert method with StartTag token type
        // Add test cases here
    }

    @Test
    public void testInsertComment() {
        // Test insert method with Comment token type
        // Add test cases here
    }

    @Test
    public void testInsertCharacter() {
        // Test insert method with Character token type
        // Add test cases here
    }

    @Test
    public void testInsertDoctype() {
        // Test insert method with Doctype token type
        // Add test cases here
    }

    @Test
    public void testPopStackToClose() {
        // Test popStackToClose method
        // Add test cases here
    }

    @Test
    public void testParseFragment() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        String inputFragment = "<div><p>Test</p></div>";
        String baseUri = "http://example.com";
        xmlTreeBuilder.initialiseParse(inputFragment, baseUri, null);
        assertEquals(2, xmlTreeBuilder.parseFragment(inputFragment, baseUri, null).size());
    }
}