package org.jsoup.parser;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.jsoup.nodes.Document;

import static org.junit.Assert.*;

public class ParserTest {
    
    @Test
    public void testParse_withValidHtml_shouldReturnDocument() {
        // Arrange
        String html = "<html><head><title>Test Page</title></head><body><h1>Hello World!</h1></body></html>";
        String baseUri = "http://example.com";
        
        // Act
        Document document = Parser.parse(html, baseUri);
        
        // Assert
        assertNotNull(document);
        assertEquals("Test Page", document.title());
    }
    
    @Test
    public void testParseBodyFragment_withValidHtml_shouldReturnDocumentWithBody() {
        // Arrange
        String bodyHtml = "<h1>Hello World!</h1>";
        String baseUri = "http://example.com";
        
        // Act
        Document document = Parser.parseBodyFragment(bodyHtml, baseUri);
        
        // Assert
        assertNotNull(document);
        assertTrue(document.body().hasText());
        assertEquals("Hello World!", document.body().text());
    }
    
    @Test
    public void testParse_withInvalidHtml_shouldReturnDocumentWithAllText() {
        // Arrange
        String html = "<h1>Hello World!";
        String baseUri = "http://example.com";
        
        // Act
        Document document = Parser.parse(html, baseUri);
        
        // Assert
        assertNotNull(document);
        assertTrue(document.hasText());
        assertEquals("<h1>Hello World!", document.text());
    }

}