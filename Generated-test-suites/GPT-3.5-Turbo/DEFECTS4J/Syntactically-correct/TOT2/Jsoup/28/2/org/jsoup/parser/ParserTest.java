import org.junit.Test;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class ParserTest {

    @Test
    public void testParseInput() {
        // Create mock objects
        TreeBuilder treeBuilder = mock(TreeBuilder.class);
        ParseErrorList errors = mock(ParseErrorList.class);
        when(errors.size()).thenReturn(0); // Mock for successful parse
        when(treeBuilder.parse(anyString(), anyString(), any(ParseErrorList.class))).thenReturn(new Document());

        // Create Parser instance
        Parser parser = new Parser(treeBuilder);

        // Test parseInput method
        Document doc = parser.parseInput("<html></html>", "http://example.com");
        assertNotNull(doc);
    }

    // Add similar test methods for other public methods in Parser class
    
    @Test
    public void testParseFragmentWithNullContext() {
        // Create mock objects
        Element context = null;
        HtmlTreeBuilder treeBuilder = mock(HtmlTreeBuilder.class);
        ParseErrorList errors = mock(ParseErrorList.class);
        when(errors.size()).thenReturn(0); // Mock for successful parse
        when(treeBuilder.parseFragment(anyString(), any(Element.class), anyString(), any(ParseErrorList.class))).thenReturn(List.of(new Node()));

        // Test parseFragment method with null context
        List<Node> nodeList = Parser.parseFragment("<div></div>", context, "http://example.com");
        assertEquals(1, nodeList.size());
    }

    // Add similar test methods for other static parse methods in Parser class

    @Test
    public void testHtmlParser() {
        // Test htmlParser method
        Parser parser = Parser.htmlParser();
        assertNotNull(parser);
        assertTrue(parser.getTreeBuilder() instanceof HtmlTreeBuilder);
    }

    @Test
    public void testXmlParser() {
        // Test xmlParser method
        Parser parser = Parser.xmlParser();
        assertNotNull(parser);
        assertTrue(parser.getTreeBuilder() instanceof XmlTreeBuilder);
    }

    // Add test methods for edge cases and exception scenarios

}