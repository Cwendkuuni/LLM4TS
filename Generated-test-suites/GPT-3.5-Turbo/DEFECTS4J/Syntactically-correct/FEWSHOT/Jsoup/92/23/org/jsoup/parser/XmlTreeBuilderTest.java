package org.jsoup.parser;

import org.jsoup.helper.Validate;
import org.jsoup.nodes.CDataNode;
import org.jsoup.nodes.Comment;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.DocumentType;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.jsoup.nodes.XmlDeclaration;

import java.io.Reader;
import java.io.StringReader;
import java.util.List;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class XmlTreeBuilderTest {
    
    @Test
    public void testInitialiseParse() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Reader input = new StringReader("<test></test>");
        String baseUri = "http://example.com";
        Parser parser = new Parser(xmlTreeBuilder);
        xmlTreeBuilder.initialiseParse(input, baseUri, parser);
        
        // Check if the document is placed onto the stack
        assertEquals(1, xmlTreeBuilder.stack.size());
        assertEquals(xmlTreeBuilder.doc, xmlTreeBuilder.stack.get(0));
        
        // Check if the output settings syntax is set to xml
        assertEquals(Document.OutputSettings.Syntax.xml, xmlTreeBuilder.doc.outputSettings().syntax());
    }
    
    @Test
    public void testParseReaderString() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Reader input = new StringReader("<test></test>");
        String baseUri = "http://example.com";

        Document document = xmlTreeBuilder.parse(input, baseUri);
        
        // Check if the document is parsed correctly
        assertEquals("<test></test>", document.outerHtml());
    }
    
    @Test
    public void testParseStringString() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        String input = "<test></test>";
        String baseUri = "http://example.com";
        
        Document document = xmlTreeBuilder.parse(input, baseUri);
        
        // Check if the document is parsed correctly
        assertEquals("<test></test>", document.outerHtml());
    }
    
    @Test
    public void testProcess() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Token startTagToken = new Token(Token.TokenType.StartTag);
        startTagToken.asStartTag().name("test");
        Token endTagToken = new Token(Token.TokenType.EndTag);
        endTagToken.asEndTag().tagName("test");
        Token commentToken = new Token(Token.TokenType.Comment);
        commentToken.asComment().data("test comment");
        Token characterToken = new Token(Token.TokenType.Character);
        characterToken.asCharacter().data("test character");
        Token doctypeToken = new Token(Token.TokenType.Doctype);
        doctypeToken.asDoctype().name("test doctype");
        doctypeToken.asDoctype().publicIdentifier("test public identifier");
        doctypeToken.asDoctype().systemIdentifier("test system identifier");
        
        xmlTreeBuilder.process(startTagToken);
        xmlTreeBuilder.process(endTagToken);
        xmlTreeBuilder.process(commentToken);
        xmlTreeBuilder.process(characterToken);
        xmlTreeBuilder.process(doctypeToken);
        
        // Check if the tokens are processed correctly
        assertEquals("<test></test>", xmlTreeBuilder.doc.outerHtml());
    }
    
    @Test
    public void testInsertNode() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Node node = new Element(Tag.valueOf("test"), "");
        
        xmlTreeBuilder.insertNode(node);
        
        // Check if the node is inserted correctly into the current element
        assertEquals("<test></test>", xmlTreeBuilder.currentElement().outerHtml());
    }
    
    @Test
    public void testInsertStartTag() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Token startTagToken = new Token(Token.TokenType.StartTag);
        startTagToken.asStartTag().name("test");
        
        Element element = xmlTreeBuilder.insert(startTagToken);
        
        // Check if the element is inserted correctly
        assertEquals("<test></test>", xmlTreeBuilder.doc.outerHtml());
        assertEquals("test", element.tagName());
    }
    
    @Test
    public void testInsertComment() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Token commentToken = new Token(Token.TokenType.Comment);
        commentToken.asComment().data("test comment");
        
        xmlTreeBuilder.insert(commentToken);
        
        // Check if the comment is inserted correctly
        assertEquals("<!--test comment-->", xmlTreeBuilder.doc.outerHtml());
    }
    
    @Test
    public void testInsertCharacter() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Token characterToken = new Token(Token.TokenType.Character);
        characterToken.asCharacter().data("test character");
        
        xmlTreeBuilder.insert(characterToken);
        
        // Check if the character is inserted correctly
        assertEquals("test character", xmlTreeBuilder.doc.body().text());
    }
    
    @Test
    public void testInsertDoctype() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Token doctypeToken = new Token(Token.TokenType.Doctype);
        doctypeToken.asDoctype().name("test doctype");
        doctypeToken.asDoctype().publicIdentifier("test public identifier");
        doctypeToken.asDoctype().systemIdentifier("test system identifier");
        
        xmlTreeBuilder.insert(doctypeToken);
        
        // Check if the doctype is inserted correctly
        assertEquals("<!DOCTYPE test doctype PUBLIC \"test public identifier\" \"test system identifier\">", xmlTreeBuilder.doc.outerHtml());
    }
    
    @Test
    public void testPopStackToClose() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Token startTagToken1 = new Token(Token.TokenType.StartTag);
        startTagToken1.asStartTag().name("test");
        Token startTagToken2 = new Token(Token.TokenType.StartTag);
        startTagToken2.asStartTag().name("test");
        Token endTagToken = new Token(Token.TokenType.EndTag);
        endTagToken.asEndTag().tagName("test");
        
        xmlTreeBuilder.insert(startTagToken1);
        xmlTreeBuilder.insert(startTagToken2);
        xmlTreeBuilder.popStackToClose(endTagToken);
        
        // Check if the element is correctly popped from the stack
        assertEquals(1, xmlTreeBuilder.stack.size());
        assertEquals("test", xmlTreeBuilder.stack.get(0).tagName());
    }
    
    @Test
    public void testParseFragmentStringString() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        String inputFragment = "<test></test>";
        String baseUri = "http://example.com";
        Parser parser = new Parser(xmlTreeBuilder);
        
        List<Node> nodes = xmlTreeBuilder.parseFragment(inputFragment, baseUri, parser);
        
        // Check if the fragment is parsed correctly
        assertEquals("<test></test>", xmlTreeBuilder.doc.outerHtml());
        assertEquals(1, nodes.size());
        assertEquals("test", nodes.get(0).nodeName());
    }
    
    @Test
    public void testParseFragmentStringElementString() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        String inputFragment = "<test></test>";
        Element context = new Element(Tag.valueOf("context"), "");
        String baseUri = "http://example.com";
        Parser parser = new Parser(xmlTreeBuilder);
        
        List<Node> nodes = xmlTreeBuilder.parseFragment(inputFragment, context, baseUri, parser);
        
        // Check if the fragment is parsed correctly
        assertEquals("<test></test>", xmlTreeBuilder.doc.outerHtml());
        assertEquals(1, nodes.size());
        assertEquals("test", nodes.get(0).nodeName());
    }
}