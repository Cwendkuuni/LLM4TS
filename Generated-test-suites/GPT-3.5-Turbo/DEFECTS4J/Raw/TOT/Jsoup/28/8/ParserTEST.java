import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.Assert;
import org.junit.Test;
import org.jsoup.parser.ParseError;
import org.jsoup.parser.ParseErrorList;
import org.jsoup.parser.Parser;
import org.jsoup.parser.TreeBuilder;

import java.util.List;

public class ParserTest {

    @Test
    public void testParseInput() {
        // Typical Use Case: Parsing valid HTML with a valid baseUri
        String html = "<html><body><h1>Title</h1><p>Paragraph</p></body></html>";
        String baseUri = "http://example.com";
        
        // Initialize the Parser
        Parser parser = new Parser(new TreeBuilder());
        
        // Parse the input and retrieve the Document
        Document doc = parser.parseInput(html, baseUri);
        
        // Assertions
        Assert.assertEquals(html, doc.html());
    }

    @Test
    public void testGetTreeBuilder() {
        // Typical Use Case: Getting the current TreeBuilder
        Parser parser = new Parser(new TreeBuilder());
        
        // Get the TreeBuilder
        TreeBuilder treeBuilder = parser.getTreeBuilder();
        
        // Assertions
        Assert.assertEquals(TreeBuilder.class, treeBuilder.getClass());
    }

    @Test
    public void testSetTreeBuilder() {
        // Typical Use Case: Setting a new TreeBuilder
        Parser parser = new Parser(new TreeBuilder());
        
        // Set a new TreeBuilder
        TreeBuilder newTreeBuilder = new TreeBuilder();
        parser.setTreeBuilder(newTreeBuilder);
        
        // Assertions
        Assert.assertEquals(newTreeBuilder, parser.getTreeBuilder());
    }

    @Test
    public void testIsTrackErrors() {
        // Typical Use Case: Track errors is enabled
        Parser parser = new Parser(new TreeBuilder());
        parser.setTrackErrors(10);
        
        // Check if error tracking is enabled
        boolean isTrackErrors = parser.isTrackErrors();
        
        // Assertions
        Assert.assertTrue(isTrackErrors);
    }

    @Test
    public void testSetTrackErrors() {
        // Typical Use Case: Enable error tracking
        Parser parser = new Parser(new TreeBuilder());
        
        // Enable error tracking with a maximum of 10 errors
        parser.setTrackErrors(10);
        
        // Assertions
        Assert.assertTrue(parser.isTrackErrors());
    }

    @Test
    public void testGetErrors() {
        // Typical Use Case: Retrieve parse errors
        Parser parser = new Parser(new TreeBuilder());
        parser.parseInput("<html>invalid</html>", "http://example.com");
        
        // Retrieve parse errors
        List<ParseError> errors = parser.getErrors();
        
        // Assertions
        Assert.assertFalse(errors.isEmpty());
    }

    @Test
    public void testParse() {
        // Typical Use Case: Parsing valid HTML with a valid baseUri
        String html = "<html><body><h1>Title</h1><p>Paragraph</p></body></html>";
        String baseUri = "http://example.com";
        
        // Parse the input and retrieve the Document
        Document doc = Parser.parse(html, baseUri);
        
        // Assertions
        Assert.assertEquals(html, doc.html());
    }

    @Test
    public void testParseFragment() {
        // Typical Use Case: Parsing a fragment of HTML with a valid baseUri and context
        String fragmentHtml = "<div>Fragment</div>";
        Document doc = Jsoup.parse("<html><body><div id=\"context\"></div></body></html>");
        Element context = doc.getElementById("context");
        String baseUri = "http://example.com";
        
        // Parse the fragment and retrieve the list of nodes
        List<Node> nodes = Parser.parseFragment(fragmentHtml, context, baseUri);
        
        // Assertions
        Assert.assertEquals(1, nodes.size());
    }

    @Test
    public void testParseBodyFragment() {
        // Typical Use Case: Parsing a fragment of HTML into the body of a Document
        String bodyHtml = "<p>Paragraph</p>";
        String baseUri = "http://example.com";
        
        // Parse the body fragment and retrieve the Document
        Document doc = Parser.parseBodyFragment(bodyHtml, baseUri);
        
        // Assertions
        Assert.assertEquals(bodyHtml, doc.body().html());
    }

    @Test
    public void testParseBodyFragmentRelaxed() {
        // Typical Use Case: Parsing a fragment of HTML with a deprecated method
        String bodyHtml = "<p>Paragraph</p>";
        String baseUri = "http://example.com";
        
        // Parse the body fragment using the deprecated method
        Document doc = Parser.parseBodyFragmentRelaxed(bodyHtml, baseUri);
        
        // Assertions
        Assert.assertEquals(bodyHtml, doc.html());
    }

    @Test
    public void testHtmlParser() {
        // Typical Use Case: Creating a new HTML parser
        Parser parser = Parser.htmlParser();
        
        // Assertions
        Assert.assertEquals(HtmlTreeBuilder.class, parser.getTreeBuilder().getClass());
    }

    @Test
    public void testXmlParser() {
        // Typical Use Case: Creating a new XML parser
        Parser parser = Parser.xmlParser();
        
        // Assertions
        Assert.assertEquals(XmlTreeBuilder.class, parser.getTreeBuilder().getClass());
    }
}