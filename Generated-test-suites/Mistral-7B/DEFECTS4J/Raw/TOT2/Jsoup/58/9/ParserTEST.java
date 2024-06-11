package org.jsoup.parser;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.junit.Assert;
import org.junit.Test;

public class ParserTest {

    @Test
    public void testParseInput() {
        // Basic test case
        String html = "<html><head><title>Test</title></head><body></body></html>";
        String baseUri = "https://example.com";
        Parser parser = new Parser(new HtmlTreeBuilder());
        Document doc = parser.parseInput(html, baseUri);
        Assert.assertNotNull(doc);

        // Edge case 1: Empty input
        parser = new Parser(new HtmlTreeBuilder());
        doc = parser.parseInput("", baseUri);
        Assert.assertNull(doc);

        // Edge case 2: Null input
        parser = new Parser(new HtmlTreeBuilder());
        Assert.assertThrows(IllegalArgumentException.class, () -> parser.parseInput(null, baseUri));

        // Edge case 3: Input with parse errors
        parser = new Parser(new HtmlTreeBuilder());
        String htmlWithErrors = "<html><head><title>Test</title></head><body><script>document.write('Hello, World!')</script></body></html>";
        ParserErrorList errors = ParseErrorList.tracking(1);
        doc = parser.parseInput(htmlWithErrors, baseUri, errors);
        Assert.assertEquals(1, errors.size());
        Assert.assertEquals("malformed document start", errors.get(0).getMessage());
    }

    // Test other methods here...
}