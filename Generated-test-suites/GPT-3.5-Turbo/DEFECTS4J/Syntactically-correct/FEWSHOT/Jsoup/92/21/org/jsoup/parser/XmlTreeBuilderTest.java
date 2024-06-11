import static org.junit.Assert.*;

import org.junit.Test;

public class XmlTreeBuilderTest {
    
    @Test
    public void testDefaultSettings() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        ParseSettings settings = builder.defaultSettings();
        
        assertEquals(ParseSettings.preserveCase, settings);
    }
    
    @Test
    public void testInitialiseParse() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Reader input = new StringReader("");
        String baseUri = "http://example.com";
        Parser parser = new Parser(null);
        
        builder.initialiseParse(input, baseUri, parser);
        
        // Assert that the document is placed onto the stack
        assertEquals(builder.doc, builder.stack.get(0));
        
        // Assert that the output settings syntax is set to xml
        assertEquals(Document.OutputSettings.Syntax.xml, builder.doc.outputSettings().syntax());
    }
    
    @Test
    public void testParseWithReaderAndBaseUri() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Reader input = new StringReader("");
        String baseUri = "http://example.com";
        
        Document doc = builder.parse(input, baseUri);
        
        assertNotNull(doc);
    }
    
    @Test
    public void testParseWithStringAndBaseUri() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        String input = "";
        String baseUri = "http://example.com";
        
        Document doc = builder.parse(input, baseUri);
        
        assertNotNull(doc);
    }
    
    @Test
    public void testProcessStartTag() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Token.StartTag startTag = new Token.StartTag("tag");
        
        Element element = builder.insert(startTag);
        
        assertNotNull(element);
        assertEquals("tag", element.tagName());
    }
    
    @Test
    public void testProcessEndTag() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Token.EndTag endTag = new Token.EndTag("tag");
        
        builder.stack.add(new Element("tag"));
        builder.popStackToClose(endTag);
        
        assertTrue(builder.stack.isEmpty());
    }
    
    @Test
    public void testProcessComment() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Token.Comment comment = new Token.Comment("This is a comment");
        
        builder.insert(comment);
        
        assertEquals("<!--This is a comment-->", builder.doc.html());
    }
    
    @Test
    public void testProcessCharacter() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Token.Character character = new Token.Character("This is a character");
        
        builder.insert(character);
        
        assertEquals("This is a character", builder.doc.html());
    }
    
    @Test
    public void testProcessDoctype() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Token.Doctype doctype = new Token.Doctype("html");
        
        builder.insert(doctype);
        
        assertEquals("<!DOCTYPE html>", builder.doc.html());
    }
    
    @Test
    public void testParseFragment() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        String inputFragment = "<div>Hello World</div>";
        String baseUri = "http://example.com";
        Parser parser = new Parser(null);
        
        List<Node> nodes = builder.parseFragment(inputFragment, baseUri, parser);
        
        assertEquals(1, nodes.size());
        assertTrue(nodes.get(0) instanceof Element);
    }
    
    @Test
    public void testParseFragmentWithContext() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        String inputFragment = "<div>Hello World</div>";
        Element context = new Element("context");
        String baseUri = "http://example.com";
        Parser parser = new Parser(null);
        
        List<Node> nodes = builder.parseFragment(inputFragment, context, baseUri, parser);
        
        assertEquals(1, nodes.size());
        assertTrue(nodes.get(0) instanceof Element);
    }
    
}