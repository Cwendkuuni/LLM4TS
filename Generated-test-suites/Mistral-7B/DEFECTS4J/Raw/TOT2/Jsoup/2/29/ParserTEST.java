package org.jsoup.parser;

import org.junit.Assert;
import org.junit.Test;
import org.jsoup.nodes.*;

import static org.jsoup.parser.Parser.parse;

public class ParserTest {
    @Test
    public void testParseHtml() {
        String html = "<html><head><title>Test</title></head><body><h1>Hello World</h1></body></html>";
        Document doc = parse(html, "http://localhost");
        Element root = doc.body();
        Element title = root.getFirstChild();
        Element h1 = title.getNextSibling();

        Assert.assertEquals("Test", title.text());
        Assert.assertEquals("Hello World", h1.text());
    }
}