import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;

public class ParserTest {

    @Test
    public void testParseInput() {
        // Arrange
        Parser parser = new Parser(new TreeBuilder());
        String html = "<html><body><h1>Hello, World!</h1></body></html>";
        String baseUri = "https://example.com/";

        // Act
        Document document = parser.parseInput(html, baseUri);

        // Assert
        Assert.assertNotNull(document);
        Assert.assertEquals(html, document.outerHtml());
    }

    @Test
    public void testGetTreeBuilder() {
        // Arrange
        Parser parser = new Parser(new TreeBuilder());

        // Act
        TreeBuilder treeBuilder = parser.getTreeBuilder();

        // Assert
        Assert.assertNotNull(treeBuilder);
    }

    @Test
    public void testSetTreeBuilder() {
        // Arrange
        Parser parser = new Parser(new TreeBuilder());
        TreeBuilder treeBuilder = new TreeBuilder();

        // Act
        parser.setTreeBuilder(treeBuilder);

        // Assert
        Assert.assertEquals(treeBuilder, parser.getTreeBuilder());
    }

    @Test
    public void testIsTrackErrors() {
        // Arrange
        Parser parser = new Parser(new TreeBuilder());

        // Act
        boolean trackErrors = parser.isTrackErrors();

        // Assert
        Assert.assertFalse(trackErrors);
    }

    @Test
    public void testSetTrackErrors() {
        // Arrange
        Parser parser = new Parser(new TreeBuilder());
        int maxErrors = 5;

        // Act
        parser.setTrackErrors(maxErrors);

        // Assert
        Assert.assertTrue(parser.isTrackErrors());
        Assert.assertEquals(maxErrors, parser.getTrackErrors());
    }

    @Test
    public void testGetErrors() {
        // Arrange
        Parser parser = new Parser(new TreeBuilder());

        // Act
        List<ParseError> errors = parser.getErrors();

        // Assert
        Assert.assertNotNull(errors);
        Assert.assertTrue(errors.isEmpty());
    }

    @Test
    public void testSettings() {
        // Arrange
        Parser parser = new Parser(new TreeBuilder());
        ParseSettings settings = new ParseSettings();

        // Act
        parser.settings(settings);

        // Assert
        Assert.assertEquals(settings, parser.settings());
    }

    @Test
    public void testParse() {
        // Arrange
        String html = "<html><body><h1>Hello, World!</h1></body></html>";
        String baseUri = "https://example.com/";

        // Act
        Document document = Parser.parse(html, baseUri);

        // Assert
        Assert.assertNotNull(document);
        Assert.assertEquals(html, document.outerHtml());
    }

    @Test
    public void testParseFragment() {
        // Arrange
        Element context = Jsoup.parse("<div></div>").body();
        String fragmentHtml = "<h1>Heading</h1>";
        String baseUri = "https://example.com/";

        // Act
        List<Node> nodes = Parser.parseFragment(fragmentHtml, context, baseUri);

        // Assert
        Assert.assertNotNull(nodes);
        Assert.assertFalse(nodes.isEmpty());
        Assert.assertEquals(fragmentHtml, nodes.get(0).outerHtml());
    }

    @Test
    public void testParseXmlFragment() {
        // Arrange
        String fragmentXml = "<root><h1>Heading</h1></root>";
        String baseUri = "https://example.com/";

        // Act
        List<Node> nodes = Parser.parseXmlFragment(fragmentXml, baseUri);

        // Assert
        Assert.assertNotNull(nodes);
        Assert.assertFalse(nodes.isEmpty());
        Assert.assertEquals(fragmentXml, nodes.get(0).outerHtml());
    }

    @Test
    public void testParseBodyFragment() {
        // Arrange
        String bodyHtml = "<h1>Heading</h1>";
        String baseUri = "https://example.com/";

        // Act
        Document document = Parser.parseBodyFragment(bodyHtml, baseUri);

        // Assert
        Assert.assertNotNull(document);
        Assert.assertEquals(bodyHtml, document.body().html());
    }

    @Test
    public void testUnescapeEntities() {
        // Arrange
        String escapedString = "&lt;div&gt;Hello, World!&lt;/div&gt;";

        // Act
        String unescapedString = Parser.unescapeEntities(escapedString, true);

        // Assert
        Assert.assertEquals("<div>Hello, World!</div>", unescapedString);
    }

    @Test
    public void testParseBodyFragmentRelaxed() {
        // Arrange
        String bodyHtml = "<h1>Heading</h1>";
        String baseUri = "https://example.com/";

        // Act
        Document document = Parser.parseBodyFragmentRelaxed(bodyHtml, baseUri);

        // Assert
        Assert.assertNotNull(document);
        Assert.assertEquals(bodyHtml, document.outerHtml());
    }

    @Test
    public void testHtmlParser() {
        // Arrange

        // Act
        Parser parser = Parser.htmlParser();

        // Assert
        Assert.assertNotNull(parser);
        Assert.assertTrue(parser.getTreeBuilder() instanceof HtmlTreeBuilder);
    }

    @Test
    public void testXmlParser() {
        // Arrange

        // Act
        Parser parser = Parser.xmlParser();

        // Assert
        Assert.assertNotNull(parser);
        Assert.assertTrue(parser.getTreeBuilder() instanceof XmlTreeBuilder);
    }
}