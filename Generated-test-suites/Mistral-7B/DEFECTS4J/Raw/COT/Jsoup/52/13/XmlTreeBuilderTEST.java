package org.jsoup.parser;

import static org.junit.Assert.*;

import org.jsoup.nodes.*;
import org.jsoup.parser.XmlTreeBuilder;
import org.junit.Test;

import java.util.List;

/**
 * Test class for XmlTreeBuilder
 */
public class XmlTreeBuilderTest {

    @Test
    public void testInitialiseParse() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        String input = "<root><child/></root>";
        String baseUri = "http://example.com";
        ParseErrorList errors = new ParseErrorList();

        xmlTreeBuilder.initialiseParse(input, baseUri, errors);

        // Check that the document is correctly placed on the stack
        assertEquals(xmlTreeBuilder.doc, xmlTreeBuilder.stack.get(0));
    }

    @Test
    public void testProcessStartTag() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        String input = "<root><child/></root>";
        String baseUri = "http://example.com";
        ParseErrorList errors = new ParseErrorList();

        xmlTreeBuilder.initialiseParse(input, baseUri, errors);

        Token token = xmlTreeBuilder.tokeniser.next(); // assuming the first token is "<root>"
        assertEquals(true, xmlTreeBuilder.process(token));

        Element root = (Element) xmlTreeBuilder.stack.get(xmlTreeBuilder.stack.size() - 1);
        assertEquals("root", root.nodeName());

        // Check that the child element is added to the root element
        Element child = (Element) xmlTreeBuilder.stack.get(xmlTreeBuilder.stack.size() - 1);
        assertEquals("child", child.nodeName());
        assertEquals(root, child.parent());
    }

    @Test
    public void testProcessEndTag() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        String input = "<root><child/></root>";
        String baseUri = "http://example.com";
        ParseErrorList errors = new ParseErrorList();

        xmlTreeBuilder.initialiseParse(input, baseUri, errors);

        // Process start tag
        Token startTagToken = xmlTreeBuilder.tokeniser.next(); // assuming the first token is "<root>"
        xmlTreeBuilder.process(startTagToken);

        // Process child start tag
        Token childStartTagToken = xmlTreeBuilder.tokeniser.next(); // assuming the next token is "<child>"
        xmlTreeBuilder.process(childStartTagToken);

        // Process child end tag
        Token childEndTagToken = xmlTreeBuilder.tokeniser.next(); // assuming the next token is "</child>"
        xmlTreeBuilder.process(childEndTagToken);

        // Check that the child element is removed from the stack
        assertEquals(1, xmlTreeBuilder.stack.size());
        assertEquals(xmlTreeBuilder.doc, xmlTreeBuilder.stack.get(0));
    }

    @Test(expected = AssertionError.class)
    public void testProcessUnexpectedTokenType() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        String input = "<root>"; // missing closing tag
        String baseUri = "http://example.com";
        ParseErrorList errors = new ParseErrorList();

        xmlTreeBuilder.initialiseParse(input, baseUri, errors);

        // Process start tag
        Token startTagToken = xmlTreeBuilder.tokeniser.next(); // assuming the first token is "<root>"
        xmlTreeBuilder.process(startTagToken);

        // Process unexpected token
        Token unexpectedToken = new Token(-1, "Unexpected token", null); // assuming the next token is not expected
        xmlTreeBuilder.process(unexpectedToken);
    }

    @Test
    public void testParseFragment() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        String input = "<root><child/></root>";
        String baseUri = "http://example.com";
        ParseErrorList errors = new ParseErrorList();

        List<Node> tree = xmlTreeBuilder.parseFragment(input, baseUri, errors);

        // Check that the tree contains the expected nodes
        assertEquals(3, tree.size());
        Element root = (Element) tree.get(0);
        Element child = (Element) tree.get(1);
        Text text = (Text) tree.get(2);

        assertEquals("root", root.nodeName());
        assertEquals("child", child.nodeName());
        assertEquals(root, child.parent());
        assertEquals("", text.text());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseFragmentNullInput() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        String baseUri = "http://example.com";
        ParseErrorList errors = new ParseErrorList();

        xmlTreeBuilder.parseFragment(null, baseUri, errors);
    }
}