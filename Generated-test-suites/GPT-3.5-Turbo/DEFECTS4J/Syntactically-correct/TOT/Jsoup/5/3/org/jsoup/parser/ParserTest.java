package org.jsoup.parser;

import org.junit.Test;
import org.junit.Assert;

public class ParserTest {

    @Test
    public void testParse() {
        // Typical use case
        String html = "<html><head><title>Test</title></head><body><h1>Hello</h1></body></html>";
        String baseUri = "https://www.example.com";
        Document doc = Parser.parse(html, baseUri);
        
        Assert.assertNotNull(doc);
        Assert.assertEquals("https://www.example.com", doc.baseUri());
        Assert.assertEquals("Test", doc.title());
        Assert.assertEquals("Hello", doc.body().select("h1").text());
        
        // Edge case
        html = "<html><body></body></html>";
        baseUri = "https://www.example.com";
        doc = Parser.parse(html, baseUri);
        
        Assert.assertNotNull(doc);
        Assert.assertEquals("https://www.example.com", doc.baseUri());
        Assert.assertEquals("", doc.title());
        Assert.assertEquals("", doc.body().text());
        
        // Error scenario
        html = "<html><body></html>"; // Missing closing </body> tag
        baseUri = "https://www.example.com";
        doc = Parser.parse(html, baseUri);
        
        Assert.assertNotNull(doc);
        Assert.assertEquals("https://www.example.com", doc.baseUri());
        Assert.assertEquals("", doc.title());
        Assert.assertEquals("", doc.body().text());
    }

    @Test
    public void testParseBodyFragment() {
        // Typical use case
        String bodyHtml = "<h1>Hello</h1>";
        String baseUri = "https://www.example.com";
        Document doc = Parser.parseBodyFragment(bodyHtml, baseUri);
        
        Assert.assertNotNull(doc);
        Assert.assertEquals("https://www.example.com", doc.baseUri());
        Assert.assertEquals("", doc.title());
        Assert.assertEquals("Hello", doc.body().select("h1").text());
        
        // Edge case
        bodyHtml = "";
        baseUri = "https://www.example.com";
        doc = Parser.parseBodyFragment(bodyHtml, baseUri);
        
        Assert.assertNotNull(doc);
        Assert.assertEquals("https://www.example.com", doc.baseUri());
        Assert.assertEquals("", doc.title());
        Assert.assertEquals("", doc.body().text());
        
        // Error scenario
        bodyHtml = "<h1>Hello</h2>"; // Invalid closing tag </h2>
        baseUri = "https://www.example.com";
        doc = Parser.parseBodyFragment(bodyHtml, baseUri);
        
        Assert.assertNotNull(doc);
        Assert.assertEquals("https://www.example.com", doc.baseUri());
        Assert.assertEquals("", doc.title());
        Assert.assertEquals("Hello", doc.body().text()); // <h2> is treated as text
    }

    @Test
    public void testParseBodyFragmentRelaxed() {
        // Typical use case
        String bodyHtml = "<h1>Hello</h2>"; // <h2> is missing closing tag
        String baseUri = "https://www.example.com";
        Document doc = Parser.parseBodyFragmentRelaxed(bodyHtml, baseUri);
        
        Assert.assertNotNull(doc);
        Assert.assertEquals("https://www.example.com", doc.baseUri());
        Assert.assertEquals("", doc.title());
        Assert.assertEquals("Hello", doc.body().text()); // <h2> is treated as text
        
        // Edge case
        bodyHtml = "";
        baseUri = "https://www.example.com";
        doc = Parser.parseBodyFragmentRelaxed(bodyHtml, baseUri);
        
        Assert.assertNotNull(doc);
        Assert.assertEquals("https://www.example.com", doc.baseUri());
        Assert.assertEquals("", doc.title());
        Assert.assertEquals("", doc.body().text());
        
        // Error scenario
        bodyHtml = "<h1>Hello</h1>"; // Valid HTML, no relaxed parsing needed
        baseUri = "https://www.example.com";
        doc = Parser.parseBodyFragmentRelaxed(bodyHtml, baseUri);
        
        Assert.assertNotNull(doc);
        Assert.assertEquals("https://www.example.com", doc.baseUri());
        Assert.assertEquals("", doc.title());
        Assert.assertEquals("Hello", doc.body().text());
    }

}