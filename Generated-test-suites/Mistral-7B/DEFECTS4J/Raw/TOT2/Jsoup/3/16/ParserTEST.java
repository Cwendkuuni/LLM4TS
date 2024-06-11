package org.jsoup.parser;

import org.junit.Assert;
import org.junit.Test;
import org.jsoup.nodes.*;

public class ParserTest {
    @Test
    public void testParse() {
        // Given
        String html = "<html><head><title>Test</title></head><body><h1>Hello World</h1></body></html>";
        String baseUri = "http://example.com";

        // When
        Parser parser = new Parser(html, baseUri, false);
        Document doc = parser.parse();

        // Then
        Element htmlNode = doc.rootElement();
        Assert.assertEquals(htmlTag, htmlNode.tag());
        Element headNode = htmlNode.child(0);
        Assert.assertEquals(headTag, headNode.tag());
        Element titleNode = headNode.child(0);
        Assert.assertEquals(titleTag, titleNode.tag());
        Assert.assertEquals("Test", titleNode.text());
        Element bodyNode = htmlNode.child(1);
        Assert.assertEquals(bodyTag, bodyNode.tag());
        Element h1Node = bodyNode.child(0);
        Assert.assertEquals(h1Tag, h1Node.tag());
        Assert.assertEquals("Hello World", h1Node.text());
    }

    @Test
    public void testParseBodyFragment() {
        // Given
        String bodyHtml = "<h1>Hello World</h1>";
        String baseUri = "http://example.com";

        // When
        Document doc = Parser.parseBodyFragment(bodyHtml, baseUri);

        // Then
        Element bodyNode = doc.body();
        Assert.assertEquals(bodyTag, bodyNode.tag());
        Element h1Node = bodyNode.child(0);
        Assert.assertEquals(h1Tag, h1Node.tag());
        Assert.assertEquals("Hello World", h1Node.text());
    }

    @Test
    public void testParseXmlDecl() {
        // Given
        String html = "<!DOCTYPE html SYSTEM \"about:legacy-compat\">";
        String baseUri = "http://example.com";

        // When
        Parser parser = new Parser(html, baseUri, false);
        Document doc = parser.parse();

        // Then
        Element xmlDeclarationNode = doc.child(0);
        Assert.assertTrue(xmlDeclarationNode instanceof XmlDeclaration);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseNullHtml() {
        // Given
        String html = null;
        String baseUri = "http://example.com";

        // When
        Parser parser = new Parser(html, baseUri, false);

        // Then
        // An exception is expected
    }
}