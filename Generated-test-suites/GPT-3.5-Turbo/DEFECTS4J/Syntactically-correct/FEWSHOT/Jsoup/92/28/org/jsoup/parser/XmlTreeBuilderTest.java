package org.jsoup.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.io.StringReader;
import java.util.List;

import org.jsoup.nodes.CDataNode;
import org.jsoup.nodes.Comment;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.DocumentType;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.jsoup.nodes.XmlDeclaration;
import org.jsoup.parser.Parser;
import org.jsoup.parser.XmlTreeBuilder;
import org.junit.Test;

public class XmlTreeBuilderTest {

    @Test
    public void testInitialiseParse() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        StringReader input = new StringReader("<root></root>");
        String baseUri = "http://example.com";
        Parser parser = new Parser(builder);
        
        builder.initialiseParse(input, baseUri, parser);
        
        assertEquals("http://example.com", builder.baseUri);
        assertEquals(parser, builder.parser);
        assertEquals(builder.doc, builder.stack.get(0));
        assertEquals(Document.OutputSettings.Syntax.xml, builder.doc.outputSettings().syntax());
    }
    
    @Test
    public void testParse_ReaderString() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        StringReader input = new StringReader("<root></root>");
        String baseUri = "http://example.com";
        
        Document document = builder.parse(input, baseUri);
        
        assertEquals("http://example.com", builder.baseUri);
        assertNull(builder.parser);
        assertEquals(builder.doc, builder.stack.get(0));
        assertEquals(Document.OutputSettings.Syntax.xml, builder.doc.outputSettings().syntax());
        assertEquals("root", document.nodeName());
    }
    
    @Test
    public void testParse_StringString() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        String input = "<root></root>";
        String baseUri = "http://example.com";
        
        Document document = builder.parse(input, baseUri);
        
        assertEquals("http://example.com", builder.baseUri);
        assertNull(builder.parser);
        assertEquals(builder.doc, builder.stack.get(0));
        assertEquals(Document.OutputSettings.Syntax.xml, builder.doc.outputSettings().syntax());
        assertEquals("root", document.nodeName());
    }
    
    @Test
    public void testProcess_StartTag() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        builder.doc = new Document("");
        builder.stack.add(builder.doc);
        
        Token.StartTag startTag = new Token.StartTag("root");
        startTag.attributes.put("attr", "value");
        
        builder.process(startTag);
        
        assertEquals("root", builder.stack.get(1).nodeName());
        assertEquals("value", builder.stack.get(1).attr("attr"));
    }
    
    @Test
    public void testProcess_EndTag() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        builder.doc = new Document("");
        builder.stack.add(builder.doc);
        
        builder.stack.add(new Element("root"));
        
        Token.EndTag endTag = new Token.EndTag("root");
        
        builder.process(endTag);
        
        assertEquals(builder.doc, builder.stack.get(0));
        assertEquals(1, builder.stack.size());
    }
    
    @Test
    public void testProcess_Comment() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        builder.doc = new Document("");
        builder.stack.add(builder.doc);
        
        Token.Comment comment = new Token.Comment("Example comment");
        
        builder.process(comment);
        
        assertEquals(builder.doc, builder.stack.get(0));
        assertEquals("Example comment", builder.doc.childNodes().get(0).outerHtml());
    }
    
    @Test
    public void testProcess_Character() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        builder.doc = new Document("");
        builder.stack.add(builder.doc);
        
        Token.Character character = new Token.Character("Example text");
        
        builder.process(character);
        
        assertEquals(builder.doc, builder.stack.get(0));
        assertEquals("Example text", builder.doc.childNodes().get(0).outerHtml());
    }
    
    @Test
    public void testProcess_Doctype() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        builder.doc = new Document("");
        builder.stack.add(builder.doc);
        
        Token.Doctype doctype = new Token.Doctype("html");
        
        builder.process(doctype);
        
        assertEquals(builder.doc, builder.stack.get(0));
        assertEquals("<!DOCTYPE html>", builder.doc.childNodes().get(0).outerHtml());
    }
    
    @Test
    public void testInsertNode() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        builder.doc = new Document("");
        builder.stack.add(builder.doc);
        
        Element parent = new Element("root");
        builder.stack.add(parent);
        
        Node node = new TextNode("Example text");
        
        builder.insertNode(node);
        
        assertEquals(node, parent.childNodes().get(0));
    }
    
    @Test
    public void testInsert_StartTag() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        builder.doc = new Document("");
        builder.stack.add(builder.doc);
        
        Token.StartTag startTag = new Token.StartTag("root");
        startTag.attributes.put("attr", "value");
        
        Element element = builder.insert(startTag);
        
        assertEquals("root", element.nodeName());
        assertEquals("value", element.attr("attr"));
        assertEquals(element, builder.stack.get(1));
    }
    
    @Test
    public void testInsert_Comment() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        builder.doc = new Document("");
        builder.stack.add(builder.doc);
        
        Token.Comment comment = new Token.Comment("Example comment");
        
        builder.insert(comment);
        
        assertEquals(builder.doc, builder.stack.get(0));
        assertEquals("Example comment", builder.doc.childNodes().get(0).outerHtml());
    }
    
    @Test
    public void testInsert_Character() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        builder.doc = new Document("");
        builder.stack.add(builder.doc);
        
        Token.Character character = new Token.Character("Example text");
        
        builder.insert(character);
        
        assertEquals(builder.doc, builder.stack.get(0));
        assertEquals("Example text", builder.doc.childNodes().get(0).outerHtml());
    }
    
    @Test
    public void testInsert_Doctype() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        builder.doc = new Document("");
        builder.stack.add(builder.doc);
        
        Token.Doctype doctype = new Token.Doctype("html");
        
        builder.insert(doctype);
        
        assertEquals(builder.doc, builder.stack.get(0));
        assertEquals("<!DOCTYPE html>", builder.doc.childNodes().get(0).outerHtml());
    }
    
    @Test
    public void testPopStackToClose() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        builder.doc = new Document("");
        builder.stack.add(builder.doc);
        builder.stack.add(new Element("root"));
        builder.stack.add(new Element("child"));
        
        Token.EndTag endTag = new Token.EndTag("root");
        
        builder.popStackToClose(endTag);
        
        assertEquals(builder.doc, builder.stack.get(0));
        assertEquals(1, builder.stack.size());
    }
    
    @Test
    public void testParseFragment_StringString() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        builder.doc = new Document("");
        builder.stack.add(builder.doc);
        
        String inputFragment = "<root></root>";
        String baseUri = "http://example.com";
        Parser parser = new Parser(builder);
        
        List<Node> nodes = builder.parseFragment(inputFragment, baseUri, parser);
        
        assertEquals(builder.doc.childNodes(), nodes);
        assertEquals("root", nodes.get(0).nodeName());
    }
    
    @Test
    public void testParseFragment_StringElementString() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        builder.doc = new Document("");
        builder.stack.add(builder.doc);
        
        String inputFragment = "<root></root>";
        Element context = new Element("context");
        String baseUri = "http://example.com";
        Parser parser = new Parser(builder);
        
        List<Node> nodes = builder.parseFragment(inputFragment, context, baseUri, parser);
        
        assertEquals(builder.doc.childNodes(), nodes);
        assertEquals("root", nodes.get(0).nodeName());
    }
}