import static org.junit.Assert.*;

import org.jsoup.nodes.Document;
import org.jsoup.parser.ParseErrorList;
import org.junit.Test;

public class XmlTreeBuilderTest {

    @Test
    public void testParse() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        String input = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><root><child>Text</child></root>";
        String baseUri = "http://example.com";
        Document doc = xmlTreeBuilder.parse(input, baseUri);
        
        assertNotNull(doc);
        assertEquals("root", doc.child(0).tagName());
        assertEquals("child", doc.child(0).child(0).tagName());
        assertEquals("Text", doc.child(0).child(0).text());
    }

    @Test
    public void testProcess() {
        // Test process method with different types of tokens
        // Add appropriate test cases here
    }

    @Test
    public void testInsertNode() {
        // Test insertNode method with different types of nodes
        // Add appropriate test cases here
    }

    @Test
    public void testInsert() {
        // Test insert method with a StartTag token
        // Add appropriate test cases here
    }

    @Test
    public void testInsertComment() {
        // Test insert method with a Comment token
        // Add appropriate test cases here
    }

    @Test
    public void testInsertCharacter() {
        // Test insert method with a Character token
        // Add appropriate test cases here
    }

    @Test
    public void testInsertDoctype() {
        // Test insert method with a Doctype token
        // Add appropriate test cases here
    }

    @Test
    public void testPopStackToClose() {
        // Test popStackToClose method with different scenarios
        // Add appropriate test cases here
    }

    @Test
    public void testParseFragment() {
        // Test parseFragment method with a fragment of XML input
        // Add appropriate test cases here
    }

}