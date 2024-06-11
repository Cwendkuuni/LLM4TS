import org.junit.Test;
import static org.junit.Assert.*;

public class ParserTest {

    @Test
    public void testParseInput() {
        // Basic test case
        Parser parser = new Parser(new TreeBuilder());
        Document document = parser.parseInput("<html><body><h1>Hello, World!</h1></body></html>", "");
        assertNotNull(document);
        
        // Edge case: empty input
        parser = new Parser(new TreeBuilder());
        document = parser.parseInput("", "");
        assertNotNull(document);
        
        // Exception test case: invalid HTML
        parser = new Parser(new TreeBuilder());
        try {
            document = parser.parseInput("<html><body><h1>Hello, World!<h1></body></html>", "");
            fail("Expected an exception");
        } catch (Exception e) {
            // Exception should be thrown
        }
        
        // Exception test case: null input
        parser = new Parser(new TreeBuilder());
        try {
            document = parser.parseInput(null, "");
            fail("Expected an exception");
        } catch (Exception e) {
            // Exception should be thrown
        }
    }

    @Test
    public void testGetTreeBuilder() {
        Parser parser = new Parser(new TreeBuilder());
        TreeBuilder treeBuilder = parser.getTreeBuilder();
        assertNotNull(treeBuilder);
    }

    @Test
    public void testSetTreeBuilder() {
        Parser parser = new Parser(new TreeBuilder());
        TreeBuilder treeBuilder = new TreeBuilder();
        parser.setTreeBuilder(treeBuilder);
        assertSame(treeBuilder, parser.getTreeBuilder());
    }

    @Test
    public void testIsTrackErrors() {
        Parser parser = new Parser(new TreeBuilder());
        
        // Default value is false
        assertFalse(parser.isTrackErrors());
        
        parser.setTrackErrors(5);
        
        // After setting track errors, value should be true
        assertTrue(parser.isTrackErrors());
    }

    @Test
    public void testSetTrackErrors() {
        Parser parser = new Parser(new TreeBuilder());
        parser.setTrackErrors(5);
        
        // After setting track errors, value should be 5
        assertEquals(5, parser.getTrackErrors());
    }

    @Test
    public void testGetErrors() {
        Parser parser = new Parser(new TreeBuilder());
        
        // Before parsing, errors should be empty
        assertTrue(parser.getErrors().isEmpty());
        
        // After parsing, errors should contain parse errors
        parser.parseInput("<html><body><h1>Hello, World!<h1></body></html>", "");
        assertFalse(parser.getErrors().isEmpty());
    }

    @Test
    public void testSettings() {
        Parser parser = new Parser(new TreeBuilder());
        ParseSettings settings = new ParseSettings();
        parser.settings(settings);
        assertSame(settings, parser.settings());
    }

    @Test
    public void testParse() {
        // Basic test case
        Document document = Parser.parse("<html><body><h1>Hello, World!</h1></body></html>", "");
        assertNotNull(document);
        
        // Edge case: empty input
        document = Parser.parse("", "");
        assertNotNull(document);
        
        // Exception test case: invalid HTML
        try {
            document = Parser.parse("<html><body><h1>Hello, World!<h1></body></html>", "");
            fail("Expected an exception");
        } catch (Exception e) {
            // Exception should be thrown
        }
        
        // Exception test case: null input
        try {
            document = Parser.parse(null, "");
            fail("Expected an exception");
        } catch (Exception e) {
            // Exception should be thrown
        }
    }

    @Test
    public void testParseFragment() {
        // Basic test case
        Element context = new Element("div");
        List<Node> nodeList = Parser.parseFragment("<h1>Hello, World!</h1>", context, "");
        assertNotNull(nodeList);
        
        // Edge case: empty input
        nodeList = Parser.parseFragment("", context, "");
        assertNotNull(nodeList);
        
        // Exception test case: invalid HTML
        try {
            nodeList = Parser.parseFragment("<h1>Hello, World!<h1>", context, "");
            fail("Expected an exception");
        } catch (Exception e) {
            // Exception should be thrown
        }
        
        // Exception test case: null input
        try {
            nodeList = Parser.parseFragment(null, context, "");
            fail("Expected an exception");
        } catch (Exception e) {
            // Exception should be thrown
        }
    }

    @Test
    public void testParseXmlFragment() {
        // Basic test case
        List<Node> nodeList = Parser.parseXmlFragment("<h1>Hello, World!</h1>", "");
        assertNotNull(nodeList);
        
        // Edge case: empty input
        nodeList = Parser.parseXmlFragment("", "");
        assertNotNull(nodeList);
        
        // Exception test case: invalid XML
        try {
            nodeList = Parser.parseXmlFragment("<h1>Hello, World!<h1>", "");
            fail("Expected an exception");
        } catch (Exception e) {
            // Exception should be thrown
        }
        
        // Exception test case: null input
        try {
            nodeList = Parser.parseXmlFragment(null, "");
            fail("Expected an exception");
        } catch (Exception e) {
            // Exception should be thrown
        }
    }

    @Test
    public void testParseBodyFragment() {
        // Basic test case
        Document document = Parser.parseBodyFragment("<h1>Hello, World!</h1>", "");
        assertNotNull(document);
        
        // Edge case: empty input
        document = Parser.parseBodyFragment("", "");
        assertNotNull(document);
        
        // Exception test case: invalid HTML
        try {
            document = Parser.parseBodyFragment("<h1>Hello, World!<h1>", "");
            fail("Expected an exception");
        } catch (Exception e) {
            // Exception should be thrown
        }
        
        // Exception test case: null input
        try {
            document = Parser.parseBodyFragment(null, "");
            fail("Expected an exception");
        } catch (Exception e) {
            // Exception should be thrown
        }
    }

    @Test
    public void testUnescapeEntities() {
        // Basic test case
        String unescaped = Parser.unescapeEntities("&lt;h1&gt;Hello, World!&lt;/h1&gt;", true);
        assertEquals("<h1>Hello, World!</h1>", unescaped);
        
        // Edge case: empty string
        unescaped = Parser.unescapeEntities("", true);
        assertEquals("", unescaped);
        
        // Exception test case: null input
        try {
            unescaped = Parser.unescapeEntities(null, true);
            fail("Expected an exception");
        } catch (Exception e) {
            // Exception should be thrown
        }
    }

    @Test
    public void testParseBodyFragmentRelaxed() {
        // Basic test case
        Document document = Parser.parseBodyFragmentRelaxed("<h1>Hello, World!</h1>", "");
        assertNotNull(document);
        
        // Edge case: empty input
        document = Parser.parseBodyFragmentRelaxed("", "");
        assertNotNull(document);
        
        // Exception test case: invalid HTML
        try {
            document = Parser.parseBodyFragmentRelaxed("<h1>Hello, World!<h1>", "");
            fail("Expected an exception");
        } catch (Exception e) {
            // Exception should be thrown
        }
        
        // Exception test case: null input
        try {
            document = Parser.parseBodyFragmentRelaxed(null, "");
            fail("Expected an exception");
        } catch (Exception e) {
            // Exception should be thrown
        }
    }

    @Test
    public void testHtmlParser() {
        Parser parser = Parser.htmlParser();
        assertNotNull(parser);
    }

    @Test
    public void testXmlParser() {
        Parser parser = Parser.xmlParser();
        assertNotNull(parser);
    }
}