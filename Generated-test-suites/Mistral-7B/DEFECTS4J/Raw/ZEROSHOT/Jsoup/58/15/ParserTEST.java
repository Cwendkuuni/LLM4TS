package org.jsoup.parser;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.ParseError;
import org.jsoup.parser.TreeBuilder;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

@RunWith(MockitoJUnitRunner.class)
public class ParserTest {

    @InjectMocks
    private Parser parser;
    @InjectMocks
    private TreeBuilder treeBuilder;

    @Before
    public void setupMocks() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testParseInputWithBaseUri() {
        String html = "<html><head></head><body><h1>Hello World</h1></body></html>";
        String baseUri = "http://example.com";
        Document document = parser.parseInput(html, baseUri);

        assertEquals("http://example.com/", document.baseUri());
        Element head = document.head();
        assertNull(head);
        Element body = document.body();
        assertEquals("Hello World", body.text());
    }

    @Test
    public void testParseInputWithoutBaseUri() {
        String html = "<html><head></head><body><h1>Hello World</h1></body></html>";
        Document document = parser.parseInput(html, null);

        assertNull(document.baseUri());
        Element head = document.head();
        assertNull(head);
        Element body = document.body();
        assertEquals("Hello World", body.text());
    }

    @Test
    public void testParseInputWithTrackErrorsEnabled() {
        String html = "<html><head></head><body><h1>Hello World</h1><script>alert('Hello')</script></body></html>";
        parser.setTrackErrors(1);
        Document document = parser.parseInput(html, null);

        List<ParseError> errors = parser.getErrors();
        assertEquals(1, errors.size());
        ParseError error = errors.get(0);
        assertEquals("Expected ';', but was '{'", error.getMessage());
        assertEquals(1, error.line());
        assertEquals(5, error.column());
        assertFalse(document.body().hasText());
    }

    @Test
    public void testParseInputWithTrackErrorsDisabled() {
        String html = "<html><head></head><body><h1>Hello World</h1></body></html>";
        parser.setTrackErrors(0);
        Document document = parser.parseInput(html, null);

        assertNull(parser.getErrors());
        assertEquals("Hello World", document.body().text());
    }

    @Test
    public void testSetTreeBuilder() {
        String html = "<html><head></head><body><h1>Hello World</h1></body></html>";
        TreeBuilder newTreeBuilder = new HtmlTreeBuilder();
        parser.setTreeBuilder(newTreeBuilder);
        Document document = parser.parseInput(html, null);

        assertEquals(newTreeBuilder, parser.getTreeBuilder());
        assertEquals(newTreeBuilder.getClass().getName(), document.toString().substring(0, 15).replace("/", "."));
    }

    @Test
    public void testParseFragment() {
        String html = "<div><p>Hello</p><p>World</p></div>";
        Element context = new Element("html");
        Document document = parser.parse(html, null);
        List<Node> nodes = parser.parseFragment(html, context, null);

        Element div = (Element) nodes.get(0);
        assertEquals(2, nodes.size());
        Element firstP = (Element) div.child(0);
        assertEquals("Hello", firstP.text());
        Element secondP = (Element) div.child(1);
        assertEquals("World", secondP.text());
    }

    @Test
    public void testParseXmlFragment() {
        String xml = "<root><element>Hello</element><element>World</element></root>";
        List<Node> nodes = parser.parseXmlFragment(xml, null);

        Element root = (Element) nodes.get(0);
        assertEquals(2, nodes.size());
        Element firstElement = (Element) root.child(0);
        assertEquals("Hello", firstElement.text());
        Element secondElement = (Element) root.child(1);
        assertEquals("World", secondElement.text());
    }

    @Test
    public void testParseBodyFragment() {
        String html = "<body><p>Hello</p><p>World</p></body>";
        Document document = parser.parseBodyFragment(html, null);

        Element body = document.body();
        assertEquals(2, body.childNodeSize());
        Element firstP = (Element) body.child(0);
        assertEquals("Hello", firstP.text());
        Element secondP = (Element) body.child(1);
        assertEquals("World", secondP.text());
    }

    @Test
    public void testUnescapeEntities() {
        String html = "&amp; &lt; &gt; &quot; &apos;";
        String result = Parser.unescapeEntities(html, false);

        assertEquals("&amp; &lt; &gt; &quot; &apos;", result);

        String result2 = Parser.unescapeEntities(html, true);
        assertEquals("and < > \" '", result2);
    }

    @Test
    public void testParseBodyFragmentRelaxed() {
        String html = "<body><p>Hello</p><p>World</p></body>";
        Document document = Parser.parseBodyFragment(html, null);

        Element body = document.body();
        assertEquals(2, body.childNodeSize());
        Element firstP = (Element) body.child(0);
        assertEquals("Hello", firstP.text());
        Element secondP = (Element) body.child(1);
        assertEquals("World", secondP.text());
    }
}