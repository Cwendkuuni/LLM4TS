import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.ParseError;
import org.jsoup.parser.Parser;
import org.jsoup.parser.TreeBuilder;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;

public class ParserTest {

    @Test
    public void testParser_constructor() {
        Parser parser = new Parser(new HtmlTreeBuilder());
        Assert.assertNotNull(parser);
    }

    @Test
    public void testParseInput_NullHtml() {
        Parser parser = new Parser(new HtmlTreeBuilder());
        try {
            parser.parseInput(null, "baseUri");
            Assert.fail();
        } catch (NullPointerException e) {
            // expected
        }
    }

    @Test
    public void testParseInput_EmptyHtml() {
        Parser parser = new Parser(new HtmlTreeBuilder());
        Document doc = parser.parseInput("", "baseUri");
        Assert.assertNotNull(doc);
        Assert.assertTrue(doc.body() == null);
    }

    @Test
    public void testParseInput_HtmlWithBaseUri() {
        Parser parser = new Parser(new HtmlTreeBuilder());
        String html = "<html><head></head><body><div id='test'>Test</div></body></html>";
        Document doc = parser.parseInput(html, "baseUri");
        Assert.assertNotNull(doc);
        Element div = doc.selectFirst("#test");
        Assert.assertNotNull(div);
        Assert.assertEquals("Test", div.text());
    }

    @Test
    public void testParseInput_HtmlWithErrors() {
        Parser parser = new Parser(new HtmlTreeBuilder());
        String html = "<html><body onload=alert('Test')><script src='malicious.js'></script></body></html>";
        List<ParseError> errors = parser.parseInput(html, "baseUri").errors();
        Assert.assertNotNull(errors);
        Assert.assertEquals(2, errors.size());
        Assert.assertEquals("Unsafe 'onload' event handler 'alert'", errors.get(0).message());
        Assert.assertEquals("Unsafe 'script' tag 'malicious.js'", errors.get(1).message());
    }

    @Test
    public void testGetTreeBuilder() {
        Parser parser = new Parser(new HtmlTreeBuilder());
        TreeBuilder treeBuilder = parser.getTreeBuilder();
        Assert.assertSame(treeBuilder, parser.treeBuilder);
    }

    @Test
    public void testSetTreeBuilder() {
        Parser parser = new Parser(new HtmlTreeBuilder());
        TreeBuilder newTreeBuilder = new XmlTreeBuilder();
        parser.setTreeBuilder(newTreeBuilder);
        Assert.assertSame(newTreeBuilder, parser.treeBuilder);
    }

    @Test
    public void testIsTrackErrors() {
        Parser parser = new Parser(new HtmlTreeBuilder());
        Assert.assertFalse(parser.isTrackErrors());

        parser.setTrackErrors(1);
        Assert.assertTrue(parser.isTrackErrors());
    }

    @Test
    public void testSetTrackErrors_Zero() {
        Parser parser = new Parser(new HtmlTreeBuilder());
        parser.setTrackErrors(0);
        Assert.assertFalse(parser.isTrackErrors());
    }

    @Test
    public void testGetErrors() {
        Parser parser = new Parser(new HtmlTreeBuilder());
        parser.setTrackErrors(1);
        String html = "<html><body onload=alert('Test')><script src='malicious.js'></script></body></html>";
        List<ParseError> errors = parser.parseInput(html, "baseUri").errors();
        parser.getErrors(); // consume errors
        List<ParseError> getErrors = parser.getErrors();
        Assert.assertNotNull(getErrors);
        Assert.assertEquals(2, getErrors.size());
        Assert.assertEquals("Unsafe 'onload' event handler 'alert'", getErrors.get(0).message());
        Assert.assertEquals("Unsafe 'script' tag 'malicious.js'", getErrors.get(1).message());
    }

    @Test
    public void testSettings() {
        Parser parser = new Parser(new HtmlTreeBuilder());
        ParseSettings settings = parser.settings();
        Assert.assertNotNull(settings);
    }

    @Test
    public void testSettings_ParseSetting() {
        Parser parser = new Parser(new HtmlTreeBuilder());
        ParseSettings settings = new ParseSettings.Builder().tagNameCaseSensitive(true).build();
        parser.settings(settings);
        Assert.assertSame(settings, parser.settings());
    }

    @Test
    public void testStaticParse() {
        String html = "<html><head></head><body><div id='test'>Test</div></body></html>";
        Document doc = Parser.parse(html, "baseUri");
        Assert.assertNotNull(doc);
        Element div = doc.selectFirst("#test");
        Assert.assertNotNull(div);
        Assert.assertEquals("Test", div.text());
    }

    @Test
    public void testStaticParseFragment() {
        String html = "<div id='test'>Test</div>";
        Element context = Jsoup.parse("<html></html>").body();
        List<Node> nodes = Parser.parseFragment(html, context, "baseUri");
        Assert.assertNotNull(nodes);
        Assert.assertEquals(1, nodes.size());
        Element div = (Element) nodes.get(0);
        Assert.assertEquals("Test", div.text());
    }

    @Test
    public void testStaticParseFragment_WithErrors() {
        String html = "<div id='test'>Test<script>alert('Malicious script')</script></div>";
        Element context = Jsoup.parse("<html></html>").body();
        List<Node> nodes = Parser.parseFragment(html, context, "baseUri");
        Assert.assertNotNull(nodes);
        Assert.assertEquals(1, nodes.size());
        Element div = (Element) nodes.get(0);
        Assert.assertEquals("Test", div.text());

        List<ParseError> errors = Parser.parseFragment(html, context, "baseUri", ParseErrorList.tracking(1)).errors();
        Assert.assertNotNull(errors);
        Assert.assertEquals(1, errors.size());
        Assert.assertEquals("Unsafe script tag 'alert(Malicious script)'", errors.get(0).message());
    }

    @Test
    public void testStaticParseXmlFragment() {
        String xml = "<xml><tag name='test'>Test</tag></xml>";
        List<Node> nodes = Parser.parseXmlFragment(xml, "baseUri");
        Assert.assertNotNull(nodes);
        Assert.assertEquals(1, nodes.size());
        Element tag = (Element) nodes.get(0);
        Assert.assertEquals("test", tag.tagName());
        Assert.assertEquals("Test", tag.text());
    }

    @Test
    public void testParseBodyFragment() {
        String bodyHtml = "<body><div id='test'>Test</div></body>";
        Document doc = Parser.parseBodyFragment(bodyHtml, "baseUri");
        Assert.assertNotNull(doc);
        Element body = doc.body();
        Assert.assertNotNull(body);
        Element div = body.selectFirst("#test");
        Assert.assertNotNull(div);
        Assert.assertEquals("Test", div.text());
    }

    @Test
    public void testUnescapeEntities() {
        String html = "&lt;p&gt;<&emsp;> Test &amp; &nbsp; &gt;<&hellip;>";
        String unescaped = Parser.unescapeEntities(html, false);
        Assert.assertEquals("<p><emsp;> Test &amp; &nbsp; ><hellip;>", unescaped);

        String attributeHtml = "&lt;p data-test='&amp; Test &amp;'&gt;";
        String unescapedAttribute = Parser.unescapeEntities(attributeHtml, true);
        Assert.assertEquals("<p data-test='& Test &'>", unescapedAttribute);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testParseBodyFragmentRelaxed() {
        String bodyHtml = "<body><div id='test'>Test</div></body>";
        Document doc = Parser.parseBodyFragmentRelaxed(bodyHtml, "baseUri");
        // Unreachable statement due to @Deprecated annotation
    }
}