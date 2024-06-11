package org.jsoup.parser;

import org.junit.Test;
import org.junit.Before;
import org.jsoup.nodes.Document;

import static org.junit.Assert.*;

public class ParserTest {
    
    private String html;
    private String baseUri;
    
    @Before
    public void setup() {
        html = "<html><head><title>Test Title</title></head><body><h1>Test Header</h1></body></html>";
        baseUri = "https://www.example.com";
    }
    
    @Test
    public void testParse() {
        Document doc = Parser.parse(html, baseUri);
        
        assertNotNull(doc);
        assertEquals("Test Title", doc.title());
        assertEquals("Test Header", doc.body().select("h1").text());
    }
    
    @Test
    public void testParseBodyFragment() {
        String bodyHtml = "<h2>Test Subheader</h2>";
        Document doc = Parser.parseBodyFragment(bodyHtml, baseUri);
        
        assertNotNull(doc);
        assertEquals("Test Subheader", doc.body().select("h2").text());
    }
    
    @Test
    public void testParseComment() {
        String commentHtml = "<!-- Test Comment -->";
        Document doc = Parser.parseBodyFragment(commentHtml, baseUri);
        
        assertNotNull(doc);
        assertEquals("<!-- Test Comment -->", doc.html());
    }
    
    @Test
    public void testParseCdata() {
        String cdataHtml = "<![CDATA[Test CDATA]]>";
        Document doc = Parser.parseBodyFragment(cdataHtml, baseUri);
        
        assertNotNull(doc);
        assertEquals("<![CDATA[Test CDATA]]>", doc.html());
    }
    
    @Test
    public void testParseXmlDecl() {
        String xmlDeclHtml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";
        Document doc = Parser.parseBodyFragment(xmlDeclHtml, baseUri);
        
        assertNotNull(doc);
        assertEquals("<?xml version=\"1.0\" encoding=\"UTF-8\"?>", doc.html());
    }
    
    @Test
    public void testParseEndTag() {
        String endTagHtml = "</head>";
        Document doc = Parser.parseBodyFragment(endTagHtml, baseUri);
        
        assertNotNull(doc);
        assertEquals("</head>", doc.html());
    }
    
    @Test
    public void testParseStartTag() {
        String startTagHtml = "<h3>Test Heading 3</h3>";
        Document doc = Parser.parseBodyFragment(startTagHtml, baseUri);
        
        assertNotNull(doc);
        assertEquals("<h3>Test Heading 3</h3>", doc.html());
    }
    
    @Test
    public void testParseTextNode() {
        String textNodeHtml = "Test Text Node";
        Document doc = Parser.parseBodyFragment(textNodeHtml, baseUri);
        
        assertNotNull(doc);
        assertEquals("Test Text Node", doc.html());
    }
    
    @Test
    public void testParseAttribute() {
        String attributeHtml = "<div id=\"testId\" class=\"testClass\"></div>";
        Document doc = Parser.parseBodyFragment(attributeHtml, baseUri);
        
        assertNotNull(doc);
        assertEquals("<div id=\"testId\" class=\"testClass\"></div>", doc.html());
    }
    
    @Test
    public void testPopStackToSuitableContainer() {
        // TODO: Add test case
    }
    
    @Test
    public void testPopStackToClose() {
        // TODO: Add test case
    }
}