package org.jsoup.parser;

import org.junit.Assert;
import org.junit.Test;

public class ParserTest {
    @Test
    public void testParse() {
        String html = "<html><head><title>Test Title</title></head><body><h1>Test Heading</h1><p>Test Paragraph</p></body></html>";
        String baseUri = "http://www.example.com";
        
        Document doc = Parser.parse(html, baseUri);
        
        Assert.assertEquals("http://www.example.com", doc.baseUri());
        Assert.assertEquals("Test Title", doc.title());
        
        Element body = doc.body();
        Assert.assertEquals("body", body.tagName());
        Assert.assertEquals(2, body.children().size());
        
        Element heading = body.child(0);
        Assert.assertEquals("h1", heading.tagName());
        Assert.assertEquals("Test Heading", heading.text());
        
        Element paragraph = body.child(1);
        Assert.assertEquals("p", paragraph.tagName());
        Assert.assertEquals("Test Paragraph", paragraph.text());
    }
    
    @Test
    public void testParseBodyFragment() {
        String bodyHtml = "<h1>Test Heading</h1><p>Test Paragraph</p>";
        String baseUri = "http://www.example.com";
        
        Document doc = Parser.parseBodyFragment(bodyHtml, baseUri);
        
        Assert.assertEquals("http://www.example.com", doc.baseUri());
        
        Element body = doc.body();
        Assert.assertEquals("body", body.tagName());
        Assert.assertEquals(2, body.children().size());
        
        Element heading = body.child(0);
        Assert.assertEquals("h1", heading.tagName());
        Assert.assertEquals("Test Heading", heading.text());
        
        Element paragraph = body.child(1);
        Assert.assertEquals("p", paragraph.tagName());
        Assert.assertEquals("Test Paragraph", paragraph.text());
    }
}