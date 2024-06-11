package org.jsoup.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class XmlTreeBuilderTest {

    @Test
    public void testInitialiseParse() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        String input = "<xml><tag></tag></xml>";
        String baseUri = "http://example.com";
        ParseErrorList errors = new ParseErrorList();
        builder.initialiseParse(input, baseUri, errors);
        
        // TODO: Check if the document is placed onto the stack
        // TODO: Check if the output settings syntax is set to xml
    }
    
    @Test
    public void testProcess() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Token startTagToken = new Token(Token.TokenType.StartTag, "tag", new Attributes());
        boolean result = builder.process(startTagToken);
        
        // TODO: Check if the start tag is inserted
        // TODO: Check the return value is true
        
        // TODO: Create tests for other token types
    }
    
    @Test
    public void testInsertNode() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Element element = new Element(Tag.valueOf("tag"), "http://example.com", new Attributes());
        builder.insertNode(element);
        
        // TODO: Check if the element is appended as a child to the current element
    }
    
    @Test
    public void testInsertStartTag() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Token.StartTag startTag = new Token.StartTag("tag", new Attributes());
        Element element = builder.insert(startTag);
        
        // TODO: Check if the element is inserted
        // TODO: Check if the tokeniser's self closing flag is acknowledged
        // TODO: Check if the tag is set to self closing if unknown
        
        // TODO: Create tests for self closing start tags and known tags
    }
    
    @Test
    public void testInsertComment() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Token.Comment comment = new Token.Comment("This is a comment", "http://example.com");
        builder.insert(comment);
        
        // TODO: Check if the comment is inserted as a child to the current element
        // TODO: Check if a xml declaration is parsed as an XmlDeclaration
    }
    
    @Test
    public void testInsertCharacter() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Token.Character character = new Token.Character("This is a character", "http://example.com");
        builder.insert(character);
        
        // TODO: Check if the character is inserted as a child to the current element
    }
    
    @Test
    public void testInsertDoctype() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Token.Doctype doctype = new Token.Doctype("html", "", "", "http://example.com");
        builder.insert(doctype);
        
        // TODO: Check if the doctype is inserted as a child to the current element
    }
    
    @Test
    public void testPopStackToClose() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Token.EndTag endTag = new Token.EndTag("tag");
        
        // TODO: Create test cases for popStackToClose method
    }
    
    @Test
    public void testParseFragment() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        String inputFragment = "<tag>This is a fragment</tag>";
        String baseUri = "http://example.com";
        ParseErrorList errors = new ParseErrorList();
        List<Node> nodes = builder.parseFragment(inputFragment, baseUri, errors);
        
        // TODO: Check if the fragment is parsed correctly
        // TODO: Check the returned list of nodes
        
    }
}