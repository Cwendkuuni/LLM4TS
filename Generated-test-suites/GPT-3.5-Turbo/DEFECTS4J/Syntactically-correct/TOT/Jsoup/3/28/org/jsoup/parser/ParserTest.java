package org.jsoup.parser;

import org.junit.Test;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class ParserTest {

    @Test
    public void testParse() {
        String html = "<html><head><title>Test</title></head><body><h1>Hello World</h1></body></html>";
        Document doc = Parser.parse(html, "");
        
        // Test if the document is parsed correctly
        assertTrue(doc.title().equals("Test"));
        assertTrue(doc.body().children().size() == 1);
        assertTrue(doc.body().child(0).tag().equals("h1"));
        assertTrue(doc.body().child(0).text().equals("Hello World"));
    }

    @Test
    public void testParseBodyFragment() {
        String bodyHtml = "<div><p>Test paragraph</p></div>";
        Document doc = Parser.parseBodyFragment(bodyHtml, "");
        
        // Test if the document body is parsed correctly
        assertTrue(doc.body().children().size() == 1);
        assertTrue(doc.body().child(0).tag().equals("div"));
        assertTrue(doc.body().child(0).child(0).tag().equals("p"));
        assertTrue(doc.body().child(0).child(0).text().equals("Test paragraph"));
    }
    
    @Test
    public void testParseBodyFragmentWithRelaxedParsing() {
        String bodyHtml = "<section><article>Test article</article></section>";
        Document doc = Parser.parseBodyFragment(bodyHtml, "");
        
        // Test if the document body is parsed correctly with relaxed parsing enabled
        assertTrue(doc.body().children().size() == 1);
        assertTrue(doc.body().child(0).tag().equals("section"));
        assertTrue(doc.body().child(0).child(0).tag().equals("article"));
        assertTrue(doc.body().child(0).child(0).text().equals("Test article"));
    }
    
    @Test
    public void testParseWithComment() {
        String html = "<html><!-- This is a comment --><body><h1>Hello World</h1></body></html>";
        Document doc = Parser.parse(html, "");
        
        // Test if the comment is parsed correctly
        assertTrue(doc.body().siblingIndex() == 0);
        assertTrue(doc.body().previousSibling().nodeName().equals("#comment"));
        assertTrue(doc.body().previousSibling().toString().equals("<!-- This is a comment -->"));
    }
    
    @Test
    public void testParseWithCdata() {
        String html = "<html><![CDATA[This is some CDATA]]></html>";
        Document doc = Parser.parse(html, "");
        
        // Test if the CDATA section is parsed correctly
        assertTrue(doc.body().text().equals("This is some CDATA"));
    }
    
    @Test
    public void testParseWithXmlDecl() {
        String html = "<html><?xml version=\"1.0\" encoding=\"UTF-8\"?><body><h1>Hello World</h1></body></html>";
        Document doc = Parser.parse(html, "");
        
        // Test if the XML declaration is parsed correctly
        assertTrue(doc.body().siblingIndex() == 0);
        assertTrue(doc.body().previousSibling().nodeName().equals("?xml"));
    }
    
    @Test
    public void testParseEndTag() {
        String html = "<html><body><h1>Hello World</h1></body></html>";
        Document doc = Parser.parse(html, "");
        
        // Test if the end tag is parsed correctly
        assertTrue(doc.body().child(0).nodeName().equals("h1"));
        doc.body().child(0).before("<!-- Test comment -->");
        
        // Test if the end tag is correctly invalidated by inserting a comment
        assertFalse(doc.body().child(0).nodeName().equals("h1"));
    }
    
    @Test
    public void testParseStartTag() {
        String html = "<html><body><h1>Hello World</h1></body></html>";
        Document doc = Parser.parse(html, "");
        
        // Test if the start tag is parsed correctly
        assertTrue(doc.body().child(0).nodeName().equals("h1"));
    }
    
    @Test
    public void testParseTextNode() {
        String html = "<html><body><h1>Hello World</h1></body></html>";
        Document doc = Parser.parse(html, "");
        
        // Test if the text node is parsed correctly
        assertTrue(doc.body().text().equals("Hello World"));
    }
    
    // Add more test cases for edge cases and error scenarios
    
}