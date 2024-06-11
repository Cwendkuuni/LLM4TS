package org.jsoup.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;
import org.jsoup.parser.Tag;
import org.junit.Test;

public class ParserTest {
    
    @Test
    public void testParse() {
        Document document = Parser.parse("<html><head><title>Test</title></head><body><h1>Hello World</h1></body></html>", "");
        Element html = document.body().parent();
        
        assertTrue(html.tagName().equals("html"));
        assertTrue(html.children().size() == 2);
        assertTrue(html.child(0).tagName().equals("head"));
        assertTrue(html.child(1).tagName().equals("body"));
        
        Element head = html.child(0);
        assertTrue(head.children().size() == 1);
        assertTrue(head.child(0).tagName().equals("title"));
        assertTrue(head.child(0).text().equals("Test"));
        
        Element body = html.child(1);
        assertTrue(body.children().size() == 1);
        assertTrue(body.child(0).tagName().equals("h1"));
        assertTrue(body.child(0).text().equals("Hello World"));
    }
    
    @Test
    public void testParseBodyFragment() {
        Document document = Parser.parseBodyFragment("<h1>Hello World</h1><p>This is a paragraph</p>", "");
        Element body = document.body();
        
        assertTrue(body.children().size() == 2);
        assertTrue(body.child(0).tagName().equals("h1"));
        assertTrue(body.child(0).text().equals("Hello World"));
        assertTrue(body.child(1).tagName().equals("p"));
        assertTrue(body.child(1).text().equals("This is a paragraph"));
    }
    
    @Test
    public void testParseAttribute() {
        String html = "<div class=\"container\" id=\"main\"><p class=\"paragraph\">This is a paragraph</p></div>";
        Document document = Parser.parse(html, "");
        Element div = document.body().child(0);
        
        Attributes attributes = div.attributes();
        assertTrue(attributes.size() == 2);
        assertTrue(attributes.get("class").equals("container"));
        assertTrue(attributes.get("id").equals("main"));
        
        Element p = div.child(0);
        attributes = p.attributes();
        assertTrue(attributes.size() == 1);
        assertTrue(attributes.get("class").equals("paragraph"));
    }
    
    @Test
    public void testParseTextNode() {
        Document document = Parser.parse("<h1>Hello World</h1>", "");
        Element h1 = document.body().child(0);
        
        assertTrue(h1.tagName().equals("h1"));
        assertTrue(h1.text().equals("Hello World"));
    }
    
    @Test
    public void testParseCdata() {
        String html = "<script><![CDATA[function sayHello() {console.log('Hello World');}]]></script>";
        Document document = Parser.parse(html, "");
        Element script = document.body().child(0);
        
        assertTrue(script.tagName().equals("script"));
        assertTrue(script.childNodeSize() == 1);
        assertTrue(script.childNode(0).toString().equals("function sayHello() {console.log('Hello World');}"));
    }
    
    @Test
    public void testParseComment() {
        String html = "<h1>Hello World</h1><!--This is a comment-->";
        Document document = Parser.parse(html, "");
        Element h1 = document.body().child(0);
        
        assertTrue(h1.tagName().equals("h1"));
        assertTrue(h1.text().equals("Hello World"));
        assertTrue(h1.nextSibling().nodeName().equals("#comment"));
        assertTrue(h1.nextSibling().toString().equals("<!--This is a comment-->"));
    }
    
    @Test
    public void testParseXmlDecl() {
        String html = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>";
        Document document = Parser.parse(html, "");
        
        assertTrue(document.toString().equals("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>"));
    }
    
    @Test
    public void testParseEndTag() {
        String html = "<div><p>This is a paragraph</p></div>";
        Document document = Parser.parse(html, "");
        Element div = document.body().child(0);
        
        assertTrue(div.tagName().equals("div"));
        assertTrue(div.children().size() == 1);
        
        Element p = div.child(0);
        assertTrue(p.tagName().equals("p"));
        assertTrue(p.text().equals("This is a paragraph"));
    }
    
    @Test
    public void testPopStackToClose() {
        Document document = Parser.parse("<html><head></head><body><h1>Hello World</h1></body></html>", "");
        Element html = document.body().parent();
        
        assertTrue(html.tagName().equals("html"));
        assertTrue(html.children().size() == 2);
        assertTrue(html.child(0).tagName().equals("head"));
        assertTrue(html.child(0).children().size() == 0);
        assertTrue(html.child(1).tagName().equals("body"));
        assertTrue(html.child(1).children().size() == 1);
        assertTrue(html.child(1).child(0).tagName().equals("h1"));
        assertTrue(html.child(1).child(0).text().equals("Hello World"));
    }
    
    @Test
    public void testStackHasValidParent() {
        Document document = Parser.parse("<html><head></head><body><h1>Hello World</h1></body></html>", "");
        Element body = document.body();
        
        assertTrue(body.parent().tagName().equals("html"));
    }

}