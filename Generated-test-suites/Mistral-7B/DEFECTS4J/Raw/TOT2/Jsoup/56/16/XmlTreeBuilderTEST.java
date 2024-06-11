package org.jsoup.parser;

import org.jsoup.Jsoup;
import org.jsoup.nodes.*;
import org.jsoup.helper.Validate;
import org.junit.Test;

import static org.junit.Assert.assertNotNull;

public class XmlTreeBuilderTest {
    // Method 1: defaultSettings()
    @Test
    public void testDefaultSettings() {
        ParseSettings settings = new XmlTreeBuilder().defaultSettings();
        assertNotNull(settings);
    }

    // Method 2: parse(String input, String baseUri)
    @Test
    public void testParseEmptyXml() {
        String xml = "<root/>";
        Document doc = new XmlTreeBuilder().parse(xml, "baseUri");
        assertNotNull(doc);
    }

    // Method 3: initialiseParse(String input, String baseUri, ParseErrorList errors, ParseSettings settings)
    @Test
    public void testInitialiseParse() {
        String xml = "<root><child></child></root>";
        XmlTreeBuilder builder = new XmlTreeBuilder();
        builder.initialiseParse(xml, "baseUri", null, builder.defaultSettings());
        assertNotNull(builder.doc);
    }

    // Method 4: process(Token token)
    @Test
    public void testProcessStartTag() {
        String xml = "<root><child></child></root>";
        XmlTreeBuilder builder = new XmlTreeBuilder();
        builder.initialiseParse(xml, "baseUri", null, builder.defaultSettings());
        Token token = builder.tokeniser.next();
        builder.process(token);
        assertNotNull(builder.stack.get(builder.stack.size() -1));
    }

    // Edge case 1: parse invalid XML
    @Test(expected = RuntimeException.class)
    public void testParseInvalidXml() {
        String xml = "<root><child<invalid>";
        new XmlTreeBuilder().parse(xml, "baseUri");
    }

    // Edge case 2: process invalid token
    @Test(expected = RuntimeException.class)
    public void testProcessInvalidToken() {
        String xml = "<root>";
        XmlTreeBuilder builder = new XmlTreeBuilder();
        builder.initialiseParse(xml, "baseUri", null, builder.defaultSettings());
        builder.tokeniser.next();
        builder.tokeniser.next(); // consume start tag
        Token token = new Token(Token.Type.EOF);
        builder.process(token);
    }

    // Edge case 3: process self-closing tag with invalid name
    @Test(expected = RuntimeException.class)
    public void testProcessSelfClosingInvalidTag() {
        String xml = "<foo/>";
        XmlTreeBuilder builder = new XmlTreeBuilder();
        builder.initialiseParse(xml, "baseUri", null, builder.defaultSettings());
        builder.tokeniser.next();
        builder.tokeniser.next();
        Token token = builder.tokeniser.next();
        builder.process(token);
    }

    // Edge case 4: popStackToClose with non-existent tag
    @Test
    public void testPopStackToCloseNonExistentTag() {
        String xml = "<root><child></child></root>";
        XmlTreeBuilder builder = new XmlTreeBuilder();
        builder.initialiseParse(xml, "baseUri", null, builder.defaultSettings());
        builder.tokeniser.next(); // consume <child> start tag
        builder.tokeniser.next(); // consume </child> end tag
        builder.tokeniser.next(); // consume </root> end tag
        Token endTag = builder.tokeniser.next(); // consume </root> end tag again
        builder.popStackToClose(endTag);
        assertNotNull(builder.stack.get(builder.stack.size() -1));
    }

    // Edge case 5: parseFragment with empty input
    @Test
    public void testParseFragmentEmptyInput() {
        String xml = "";
        List<Node> nodes = new XmlTreeBuilder().parseFragment(xml, "baseUri", null, builder.defaultSettings());
        assertNotNull(nodes);
        assertNotNull(nodes.get(0));
    }

    
    // All test cases
    @Test
    public void testAll() {
        // Replace this with your own test methods to cover all methods and edge cases
    }

    
}