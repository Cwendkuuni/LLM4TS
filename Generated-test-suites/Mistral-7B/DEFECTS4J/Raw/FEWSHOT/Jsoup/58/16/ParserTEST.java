package org.jsoup.parser;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Test;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;

public class ParserTest {

    @Test
    public void testParseInput() {
        String html = "<html><body><h1>Hello World</h1></body></html>";
        Parser parser = new Parser(new HtmlTreeBuilder());
        Document doc = parser.parseInput(html, null);
        assertNotNull(doc);
        Element body = doc.body();
        Element h1 = body.firstChild();
        assertTrue(h1.tagName().equals("h1"));
        assertTrue(h1.text().equals("Hello World"));
    }

    @Test
    public void testParseInputWithErrors() {
        String html = "<html><body><script>alert('Hello World');</script></body></html>";
        Parser parser = new Parser(new HtmlTreeBuilder());
        parser.setTrackErrors(1);
        Document doc = parser.parseInput(html, null);
        List<ParseError> errors = parser.getErrors();
        assertNotNull(errors);
        assertTrue(errors.size() > 0);
        ParseError error = errors.get(0);
        assertTrue(error.getMessage().contains("script"));
        assertTrue(error.getLine() > 0);
        assertTrue(error.getColumn() > 0);
    }

    @Test
    public void testParseFragment() {
        String fragmentHtml = "<p>Hello World</p>";
        Parser parser = new Parser(new HtmlTreeBuilder());
        List<Node> nodes = parser.parseFragment(fragmentHtml, null, null);
        Node node = nodes.get(0);
        assertTrue(node.tagName().equals("p"));
        assertTrue(node.text().equals("Hello World"));
    }

    @Test
    public void testParseXmlFragment() {
        String fragmentXml = "<person><name>John Doe</name></person>";
        Parser parser = new Parser(new XmlTreeBuilder());
        List<Node> nodes = parser.parseXmlFragment(fragmentXml, null);
        Element person = (Element) nodes.get(0);
        Element name = person.child(0);
        assertTrue(name.tagName().equals("name"));
        assertTrue(name.text().equals("John Doe"));
    }

    @Test
    public void testParseBodyFragment() {
        String bodyHtml = "<body><p>Hello World</p></body>";
        Parser parser = new Parser(new HtmlTreeBuilder());
        Document doc = parser.parseBodyFragment(bodyHtml, null);
        Element p = doc.body().firstChild();
        assertTrue(p.tagName().equals("p"));
        assertTrue(p.text().equals("Hello World"));
    }

    @Test
    public void testUnescapeEntities() {
        String html = "&lt;p&gt;Hello &amp; World&lt;/p&gt;";
        String unescaped = Parser.unescapeEntities(html, false);
        assertTrue(unescaped.equals("<p>Hello & World</p>"));
    }

    @Test
    public void testParseInputWithDefaultSettings() {
        String html = "<html><body><h1>Hello World</h1></body></html>";
        Parser parser = new Parser(new HtmlTreeBuilder());
        Document doc = parser.parseInput(html, null);
        assertNotNull(doc);
        Element body = doc.body();
        Element h1 = body.firstChild();
        assertTrue(h1.tagName().equals("h1"));
        assertTrue(h1.text().equals("Hello World"));

        // check default settings
        ParseSettings settings = parser.settings();
        assertTrue(settings.isTrimWhitespace());
        assertTrue(settings.isParseErrorHandlingOff());
        assertTrue(settings.isOmitParserErrors());
        assertTrue(settings.isOmitEmptyAttributes());
        assertTrue(settings.isOmitDuplicateAttributes());
        assertTrue(settings.isOmitExtraWhitespace());
        assertTrue(settings.isOmitTagName());
        assertTrue(settings.isReplaceWithEmpty());
        assertTrue(settings.isReplaceWithSelfClosingTag());
        assertTrue(settings.isReplaceInvalidXmlCharacters());
        assertTrue(settings.isReplaceProtocolSchemes());
        assertTrue(settings.isRemoveScripts());
        assertTrue(settings.isRemoveStylesheets());
        assertTrue(settings.isRemoveDuplicateIds());
        assertTrue(settings.isRemoveDuplicateClasses());
        assertTrue(settings.isRemoveEmptyParas());
        assertTrue(settings.isRemoveEmptyAttributes());
        assertTrue(settings.isRemoveFilteredAttributes());
        assertTrue(settings.isRemoveEmptyElementText());
        assertTrue(settings.isRemoveTagTextIfEmpty());
        assertTrue(settings.isReplaceElementTextWithChildren());
        assertTrue(settings.isAddDoctype());
        assertTrue(settings.isValidateSchema());
        assertTrue(settings.isNormalizeSpaces());
        assertTrue(settings.isSelectiveEmulationEnabled());
    }

    @Test
    public void testParseInputWithCustomSettings() {
        String html = "<html><body><h1 id='foo' class='bar baz'>Hello World</h1></body></html>";
        Parser parser = new Parser(new HtmlTreeBuilder());
        ParseSettings settings = new ParseSettings();
        settings.setTrimWhitespace(false);
        settings.setParseErrorHandling(ParseSettings.ParseErrorHandling.ON_ERROR);
        settings.setOmitParserErrors(false);
        parser.settings(settings);
        Document doc = parser.parseInput(html, null);
        List<ParseError> errors = parser.getErrors();
        assertNotNull(errors);
        assertTrue(errors.size() > 0);
        ParseError error = errors.get(0);
        assertTrue(error.getMessage().contains("h1"));
        assertTrue(error.getLine() > 0);
        assertTrue(error.getColumn() > 0);

        Element h1 = doc.body().firstChild();
        assertTrue(h1.tagName().equals("h1"));
        assertTrue(h1.id().equals("foo"));
        assertTrue(h1.classNames().contains("bar"));
        assertTrue(h1.classNames().contains("baz"));
        assertTrue(h1.text().equals("Hello World"));
    }
}