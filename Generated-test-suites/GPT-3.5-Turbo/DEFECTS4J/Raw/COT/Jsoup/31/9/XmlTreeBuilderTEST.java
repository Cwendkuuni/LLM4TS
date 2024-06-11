import org.jsoup.nodes.Document;
import org.jsoup.Connection;

import org.jsoup.parser.XmlTreeBuilder;

import static org.junit.Assert.*;
import org.junit.Test;

public class XmlTreeBuilderTest {
    private XmlTreeBuilder builder = new XmlTreeBuilder();
    private String input = "<xml><tag1>data1</tag1><tag2>data2</tag2></xml>";
    
    @Test
    public void testInitialiseParse() {
        String baseUri = "http://example.com";
        ParseErrorList errors = new ParseErrorList();
        builder.initialiseParse(input, baseUri, errors);
        
        assertEquals(1, builder.stack.size());
        assertEquals(Document.class, builder.stack.get(0).getClass());
        assertEquals(baseUri, builder.baseUri);
        assertEquals(errors, builder.getErrors());
    }
    
    @Test
    public void testProcessStartTag() {
        Token.StartTag startTag = new Token.StartTag("tag");
        builder.process(startTag);
        
        assertEquals(1, builder.stack.size());
        assertEquals(Element.class, builder.stack.get(0).getClass());
        assertEquals("tag", builder.currentElement().nodeName());
    }
    
    @Test
    public void testProcessEndTag() {
        Token.EndTag endTag = new Token.EndTag("tag");
        builder.stack.push(new Element("tag"));
        builder.process(endTag);
        
        assertEquals(0, builder.stack.size());
    }
    
    @Test
    public void testProcessComment() {
        Token.Comment comment = new Token.Comment("comment");
        builder.process(comment);
        
        assertEquals(1, builder.currentElement().childNodes().size());
        assertEquals(Comment.class, builder.currentElement().childNodes().get(0).getClass());
        assertEquals("comment", builder.currentElement().childNodes().get(0).toString());
    }
    
    @Test
    public void testProcessCharacter() {
        Token.Character character = new Token.Character("data");
        builder.process(character);
        
        assertEquals(1, builder.currentElement().childNodes().size());
        assertEquals(TextNode.class, builder.currentElement().childNodes().get(0).getClass());
        assertEquals("data", builder.currentElement().childNodes().get(0).toString());
    }
    
    @Test
    public void testProcessDoctype() {
        Token.Doctype doctype = new Token.Doctype("html");
        builder.process(doctype);
        
        assertEquals(1, builder.currentElement().childNodes().size());
        assertEquals(DocumentType.class, builder.currentElement().childNodes().get(0).getClass());
        assertEquals("html", builder.currentElement().childNodes().get(0).toString());
    }
    
    @Test
    public void testInsert() {
        Token.StartTag startTag = new Token.StartTag("tag");
        builder.insert(startTag);
        
        assertEquals(1, builder.stack.size());
        assertEquals(Element.class, builder.stack.get(0).getClass());
        assertEquals("tag", builder.currentElement().nodeName());
    }
    
    @Test
    public void testInsertComment() {
        Token.Comment comment = new Token.Comment("comment");
        builder.insert(comment);
        
        assertEquals(1, builder.currentElement().childNodes().size());
        assertEquals(Comment.class, builder.currentElement().childNodes().get(0).getClass());
        assertEquals("comment", builder.currentElement().childNodes().get(0).toString());
    }
    
    @Test
    public void testInsertCharacter() {
        Token.Character character = new Token.Character("data");
        builder.insert(character);
        
        assertEquals(1, builder.currentElement().childNodes().size());
        assertEquals(TextNode.class, builder.currentElement().childNodes().get(0).getClass());
        assertEquals("data", builder.currentElement().childNodes().get(0).toString());
    }
    
    @Test
    public void testInsertDoctype() {
        Token.Doctype doctype = new Token.Doctype("html");
        builder.insert(doctype);
        
        assertEquals(1, builder.currentElement().childNodes().size());
        assertEquals(DocumentType.class, builder.currentElement().childNodes().get(0).getClass());
        assertEquals("html", builder.currentElement().childNodes().get(0).toString());
    }
    
    @Test
    public void testPopStackToClose() {
        Token.EndTag endTag = new Token.EndTag("tag");
        Element elem1 = new Element("tag");
        Element elem2 = new Element("tag");
        builder.stack.push(elem1);
        builder.stack.push(elem2);
        builder.process(endTag);
        
        assertEquals(0, builder.stack.size());
    }
}