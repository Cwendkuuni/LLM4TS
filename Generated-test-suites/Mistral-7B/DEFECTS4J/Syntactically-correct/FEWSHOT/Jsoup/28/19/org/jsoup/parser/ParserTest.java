package org.jsoup.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.jsoup.parser.Parser;
import org.jsoup.parser.TreeBuilder;
import org.jsoup.parser.ParseError;
import org.jsoup.parser.ParseErrorList;

public class ParserTest {

    @Test
    public void testParserInstance() {
        Parser parser = new Parser(new HtmlTreeBuilder());
        assertEquals(Parser.class, parser.getClass());
    }

    @Test
    public void testParserGetTreeBuilder() {
        Parser parser = new Parser(new HtmlTreeBuilder());
        assertEquals(new HtmlTreeBuilder(), parser.getTreeBuilder());
    }

    @Test
    public void testParserSetTreeBuilder() {
        Parser parser = new Parser(new HtmlTreeBuilder());
        TreeBuilder newTreeBuilder = new XmlTreeBuilder();
        parser.setTreeBuilder(newTreeBuilder);
        assertEquals(newTreeBuilder, parser.getTreeBuilder());
    }

    @Test
    public void testParserIsTrackErrors() {
        Parser parser = new Parser(new HtmlTreeBuilder());
        assertFalse(parser.isTrackErrors());

        Parser parserWithErrors = new Parser(new HtmlTreeBuilder());
        parserWithErrors.setTrackErrors(5);
        assertTrue(parserWithErrors.isTrackErrors());
    }

    @Test
    public void testParserSetTrackErrors() {
        Parser parser = new Parser(new HtmlTreeBuilder());
        parser.setTrackErrors(5);
        assertEquals(5, parser.getMaxErrors());
    }

    @Test
    public void testParserGetErrors() {
        Parser parser = new Parser(new HtmlTreeBuilder());
        List<ParseError> errors = Arrays.asList(new ParseError("", 0, 0), new ParseError("", 0, 1));
        assertNull(parser.getErrors());

        parser.setTrackErrors(2);
        parser.setTreeBuilder(new TreeBuilder() {
            @Override
            public Node parse(String html, int offset, List<ParseError> errors) {
                throw new RuntimeException();
            }
        });
        parser.parseInput("", "");
        assertEquals(errors, parser.getErrors());
    }

    @Test(expected = AssertionError.class)
    public void testParserParseInputNullHtml() {
        Parser parser = new Parser(new HtmlTreeBuilder());
        parser.parseInput(null, "");
    }

    @Test(expected = AssertionError.class)
    public void testParserParseInputNullBaseUri() {
        Parser parser = new Parser(new HtmlTreeBuilder());
        parser.parseInput("", null);
    }

    @Test
    public void testParserParse() {
        Parser parser = Parser.htmlParser();
        Document doc = parser.parseInput("<html><body><p>Hello World</p></body></html>", "");
        assertEquals("<html><body><p>Hello World</p></body></html>", doc.html());
        assertEquals("<html><body><p>Hello World</p></body></html>", doc.outerHtml());

        Element body = doc.body();
        assertEquals("<p>Hello World</p>", body.html());
        assertEquals("<p>Hello World</p>", body.outerHtml());
        assertEquals("Hello World", body.text());
        assertEquals(1, doc.childNodeSize());
        assertEquals(1, body.childNodeSize());

        TextNode textNode = (TextNode) body.child(0);
        assertEquals("Hello World", textNode.text());
        assertEquals("Hello World", textNode.wholeText());
    }

    @Test
    public void testParserParseFragment() {
        Parser parser = Parser.htmlParser();
        Element context = new Element("div");
        Document doc = parser.parseDocument();
        doc.body().appendChild(context);

        List<Node> nodes = parser.parseFragment("<p>Hello World</p>", context, "");
        assertEquals(1, nodes.size());
        Element node = (Element) nodes.get(0);
        assertEquals("<p>Hello World</p>", node.outerHtml());
        assertEquals("Hello World", node.text());

        context.appendChild(node);
        assertEquals(1, doc.body().childNodeSize());
        assertEquals(1, context.childNodeSize());
    }

    @Test
    public void testParserParseBodyFragment() {
        Parser parser = Parser.htmlParser();
        Document doc = parser.parseBodyFragment("<p>Hello World</p>", "");
        assertEquals("<html><body><p>Hello World</p></body></html>", doc.outerHtml());
        assertEquals("Hello World", doc.body().text());

        assertEquals(1, doc.childNodeSize());
        assertEquals(1, doc.body().childNodeSize());
    }

    @Test
    public void testParserParseBodyFragmentRelaxedDeprecated() {
        Parser parser = Parser.htmlParser();
        Document doc = parser.parseBodyFragment("<p>Hello World</p>", "");
        assertEquals("<html><body><p>Hello World</p></body></html>", doc.outerHtml());
        assertEquals("Hello World", doc.body().text());

        assertEquals(1, doc.childNodeSize());
        assertEquals(1, doc.body().childNodeSize());
    }

    @Test
    public void testParserHtmlParser() {
        Parser parser = Parser.htmlParser();
        Document doc = parser.parseInput("<html><body><p>Hello World</p></body></html>", "");
        assertEquals("<html><body><p>Hello World</p></body></html>", doc.outerHtml());
        assertEquals("Hello World", doc.body().text());

        assertEquals(1, doc.childNodeSize());
        assertEquals(1, doc.body().childNodeSize());
    }

    @Test
    public void testParserXmlParser() {
        Parser parser = Parser.xmlParser();
        Document doc = parser.parseInput("<root><element>Hello World</element></root>", "");
        assertEquals("<root><element>Hello World</element></root>", doc.outerHtml());
        assertEquals("Hello World", doc.getElementsByTag("element").get(0).text());

        assertEquals(1, doc.childNodeSize());
        assertEquals(1, doc.child(0).childNodeSize());
    }
}