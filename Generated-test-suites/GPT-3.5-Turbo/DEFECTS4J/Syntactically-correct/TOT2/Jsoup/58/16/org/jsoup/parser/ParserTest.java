package org.jsoup.parser;

import static org.junit.Assert.*;
import org.junit.Test;

public class ParserTest {

    @Test
    public void testParseInput() {
        // Test basic functionality of parseInput
        Parser parser = new Parser(new HtmlTreeBuilder());
        String html = "<html><body><p>Hello, World!</p></body></html>";
        String baseUri = "http://example.com";
        Document doc = parser.parseInput(html, baseUri);
        assertNotNull(doc);
    }

    @Test
    public void testGetTreeBuilder() {
        // Test getTreeBuilder method
        Parser parser = new Parser(new HtmlTreeBuilder());
        assertEquals(parser.getTreeBuilder().getClass(), HtmlTreeBuilder.class);
    }

    @Test
    public void testSetTreeBuilder() {
        // Test setTreeBuilder method
        Parser parser = new Parser(new HtmlTreeBuilder());
        parser.setTreeBuilder(new XmlTreeBuilder());
        assertEquals(parser.getTreeBuilder().getClass(), XmlTreeBuilder.class);
    }

    // Add more test cases for other methods

}