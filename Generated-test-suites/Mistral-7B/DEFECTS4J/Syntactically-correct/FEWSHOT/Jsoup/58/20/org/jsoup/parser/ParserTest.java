package org.jsoup.parser;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertSame;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.jsoup.parser.ParseError;
import org.jsoup.parser.ParseErrorList;
import org.jsoup.parser.ParseSettings;
import org.jsoup.parser.TreeBuilder;

public class ParserTest {

    @Test
    public void testNewParserWithTreeBuilder() {
        TreeBuilder treeBuilder = new HtmlTreeBuilder();
        Parser parser = new Parser(treeBuilder);
        assertSame(treeBuilder, parser.getTreeBuilder());
    }

    @Test
    public void testParseInput() {
        TreeBuilder treeBuilder = new HtmlTreeBuilder();
        Parser parser = new Parser(treeBuilder);
        Document document = parser.parseInput("<html><body></body></html>", null);
        assertNotNull(document);
        assertEquals(1, document.numChildren());
        Element body = document.body();
        assertEquals(0, body.numChildren());

        ParseErrorList errors = parser.getErrors();
        assertNull(errors);
    }

    @Test
    public void testTrackErrorsAndGetErrors() {
        TreeBuilder treeBuilder = new HtmlTreeBuilder();
        Parser parser = new Parser(treeBuilder);
        parser.setTrackErrors(3);
        ParseErrorList errors = parser.getErrors();
        assertNull(errors);

        Document document = parser.parseInput("<html><body><script>1+1</script></body></html>", null);
        errors = parser.getErrors();
        assertNotNull(errors);
        assertEquals(1, errors.size());
        ParseError error = errors.get(0);
        assertEquals(1, error.getLine());
        assertEquals(5, error.getColumn());
        assertEquals("Error parsing beginning of '<script>" +
                "1+1</script>' at line 2 column 1", error.getMessage());
    }

    @Test
    public void testSetTrackErrors() {
        TreeBuilder treeBuilder = new HtmlTreeBuilder();
        Parser parser = new Parser(treeBuilder);
        assertFalse(parser.isTrackErrors());

        parser.setTrackErrors(3);
        assertTrue(parser.isTrackErrors());

        parser.setTrackErrors(0);
        assertFalse(parser.isTrackErrors());
    }

    @Test
    public void testParseStaticFunctions() {
        Document document = Parser.parse("<html><body></body></html>", null);
        assertNotNull(document);
        assertEquals(1, document.numChildren());
        Element body = document.body();
        assertEquals(0, body.numChildren());

        ParseErrorList errors = Parser.getErrors();
        assertNull(errors);

        List<Node> nodes = Parser.parseFragment("<p>Hello world</p>", null, null);
        assertNotNull(nodes);
        assertEquals(1, nodes.size());
        Node node = nodes.get(0);
        assertEquals(Node.ELEMENT_NODE, node.nodeType());
        Element element = (Element) node;
        assertEquals("p", element.tagName());

        nodes = Parser.parseFragment("<p>Hello <world></world> world</p>", null, null);
        assertNotNull(nodes);
        assertEquals(3, nodes.size());
        node = nodes.get(0);
        assertEquals(Node.ELEMENT_NODE, node.nodeType());
        element = (Element) node;
        assertEquals("p", element.tagName());

        node = nodes.get(1);
        assertEquals(Node.ELEMENT_NODE, node.nodeType());
        element = (Element) node;
        assertEquals("world", element.tagName());

        node = nodes.get(2);
        assertEquals(Node.TEXT_NODE, node.nodeType());
        TextNode textNode = (TextNode) node;
        assertEquals("world", textNode.text());

        errors = Parser.parseFragment("<p>Hello <world></world> world", null, null, 3);
        assertNotNull(errors);
        assertEquals(1, errors.size());
        ParseError error = errors.get(0);
        assertEquals(2, error.getLine());
        assertEquals(9, error.getColumn());
        assertEquals("Error parsing '<world>' at line 2 column 9", error.getMessage());
    }

    @Test
    public void testParseBodyFragment() {
        Document document = Parser.parseBodyFragment("<body><p>Hello world</p></body>", null);
        assertNotNull(document);
        assertEquals(1, document.numChildren());
        Element body = document.body();
        assertEquals(1, body.numChildren());
        Element p = body.getFirstChild();
        assertEquals("p", p.tagName());

        List<Node> nodes = Parser.parseFragment("<p>Hello <world></world> world</p>", p, null);
        assertNotNull(nodes);
        assertEquals(3, nodes.size());
        Node node = nodes.get(0);
        assertEquals(Node.ELEMENT_NODE, node.nodeType());
        Element element = (Element) node;
        assertEquals("p", element.tagName());

        node = nodes.get(1);
        assertEquals(Node.ELEMENT_NODE, node.nodeType());
        element = (Element) node;
        assertEquals("world", element.tagName());

        node = nodes.get(2);
        assertEquals(Node.TEXT_NODE, node.nodeType());
        TextNode textNode = (TextNode) node;
        assertEquals("world", textNode.text());

        document.body().appendChild(nodes.get(1));
        document.body().removeChild(p);

        nodes = Parser.parseFragment("<p>Hello <world></world> world</p>", null, null);
        assertNotNull(nodes);
        assertEquals(3, nodes.size());
        node = nodes.get(0);
        assertEquals(Node.ELEMENT_NODE, node.nodeType());
        element = (Element) node;
        assertEquals("p", element.tagName());

        node = nodes.get(1);
        assertEquals(Node.ELEMENT_NODE, node.nodeType());
        element = (Element) node;
        assertEquals("world", element.tagName());

        node = nodes.get(2);
        assertEquals(Node.TEXT_NODE, node.nodeType());
        textNode = (TextNode) node;
        assertEquals("world", textNode.text());

        ErrorCollector errorCollector = new ErrorCollector();
        List<Node> nodesWithError = Parser.parseFragment("<p>Hello <world></world> world</p>", null, null, errorCollector);
        assertNotNull(nodesWithError);
        assertEquals(3, nodesWithError.size());
        assertEquals(1, errorCollector.size());
        ParseError error = errorCollector.getError();
        assertEquals(2, error.getLine());
        assertEquals(9, error.getColumn());
        assertEquals("Error parsing '<world>' at line 2 column 9", error.getMessage());
    }

    @Test
    public void testParseXmlFragment() {
        List<Node> nodes = Parser.parseXmlFragment("<root><child/></root>", null);
        assertNotNull(nodes);
        assertEquals(2, nodes.size());
        Element root = (Element) nodes.get(0);
        assertEquals("root", root.tagName());
        Element child = (Element) nodes.get(1);
        assertEquals("child", child.tagName());
    }

    @Test
    public void testUnescapeEntities() {
        String input = "&amp; &lt; &gt; &quot; &apos; &nbsp; &iexcl; &cent; &pound; &curren; &yen; &euro; &reg; &degree; &plusmn; &sup2; &sup3; &acute; &micro; &para; &middot; &cedil; &sect; &uml; &copy; &ordf; &laquo; &not; &shy; &regstercedil; &macr; &deg; &sup1; &sup4; &ordm; &raquo;";
        String output = Parser.unescapeEntities(input, false);
        assertEquals("&amp; &lt; &gt; &quot; &apos; &nbsp; &#161; &#162; &#163; &#164; &#165; &#166; &#167; &#168; &#169; &#174; &#176; &#178; &#180; &#181; &#182; &#183; &#185; &#186; &#188; &#189; &#191; &#192; &#193; &#194; &#195; &#196; &#197; &#198; &#199; &reg; &deg; &#224; &#226; &#228; &#229; &#231; &#232; &#233; &#247;", output);

        input = "&amp;lt; &amp;gt; &amp;quot; &amp;apos; &amp;#160; &iexcl; &cent; &pound; &curren; &yen; &euro; &reg; &degree; &plusmn; &sup2; &sup3; &acute; &micro; &para; &middot; &cedil; &sect; &uml; &copy; &ordf; &laquo; &not; &shy; &regstercedil; &macr; &deg; &sup1; &sup4; &ordm; &raquo;";
        output = Parser.unescapeEntities(input, true);
        assertEquals("< & > \" ' &quot; &lt; &gt; &copy; &#160; &#161; &#162; &#163; &#164; &#165; &#166; &#167; &#168; &#169; &#174; &#176; &#178; &#180; &#181; &#182; &#183; &#185; &#186; &#188; &#189; &#191; &#192; &#193; &#194; &#195; &#196; &#197; &#198; &#199; &#174; &deg; &#224; &#226; &#228; &#229; &#231; &#232; &#233; &#247;", output);
    }

    @Test
    public void testParseBodyFragmentRelaxed() {
        Document document = Parser.parseBodyFragmentRelaxed("<body><p>Hello world</p></body>", null);
        assertNotNull(document);
        assertEquals(1, document.numChildren());
        Element body = document.body();
        assertEquals(1, body.numChildren());
        Element p = body.getFirstChild();
        assertEquals("p", p.tagName());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testParseBodyFragmentRelaxedError() {
        Parser.parseBodyFragmentRelaxed("<script>1+1</script>", null);
    }

    static class ErrorCollector implements ParseErrorList {
        private final List<ParseError> errors = new ArrayList<>();

        @Override
        public void add(ParseError error) {
            errors.add(error);
        }

        public int size() {
            return errors.size();
        }

        public ParseError getError() {
            if (size() > 0) {
                return errors.get(0);
            } else {
                throw new IndexOutOfBoundsException();
            }
        }

        public void clear() {
            errors.clear();
        }
    }
}