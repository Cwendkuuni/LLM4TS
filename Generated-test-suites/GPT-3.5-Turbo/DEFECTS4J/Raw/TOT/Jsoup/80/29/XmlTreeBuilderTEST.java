import org.jsoup.nodes.*;
import org.jsoup.parser.*;

import org.junit.Test;
import static org.junit.Assert.*;

public class XmlTreeBuilderTest {

    @Test
    public void testDefaultSettings() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        ParseSettings settings = builder.defaultSettings();
        
        assertNotNull(settings);
        assertTrue(settings.isPreserveCase());
    }
    
    @Test
    public void testParseWithReader() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Reader reader = new StringReader("<root><child>Test</child></root>");
        String baseUri = "http://example.com";
        
        Document doc = builder.parse(reader, baseUri);
        
        assertNotNull(doc);
        assertEquals("root", doc.nodeName());
        assertEquals("child", doc.child(0).nodeName());
        assertEquals("Test", doc.text());
    }
    
    @Test
    public void testParseWithString() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        String input = "<root><child>Test</child></root>";
        String baseUri = "http://example.com";
        
        Document doc = builder.parse(input, baseUri);
        
        assertNotNull(doc);
        assertEquals("root", doc.nodeName());
        assertEquals("child", doc.child(0).nodeName());
        assertEquals("Test", doc.text());
    }
    
    @Test
    public void testInitialiseParse() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Reader reader = new StringReader("<root><child>Test</child></root>");
        String baseUri = "http://example.com";
        ParseErrorList errors = new ParseErrorList();
        ParseSettings settings = builder.defaultSettings();
        
        builder.initialiseParse(reader, baseUri, errors, settings);
        
        assertNotNull(builder.stack);
        assertEquals(1, builder.stack.size());
        assertEquals("root", builder.stack.get(0).nodeName());
        assertTrue(builder.stack.get(0) instanceof Document);
        
        assertNotNull(builder.doc);
        assertEquals("root", builder.doc.nodeName());
        
        Document.OutputSettings.Syntax syntax = builder.doc.outputSettings().syntax();
        assertEquals(Document.OutputSettings.Syntax.xml, syntax);
    }
    
    @Test
    public void testProcessStartTag() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Token.StartTag startTag = new Token.StartTag("root", null);
        
        Element el = builder.insert(startTag);
        
        assertNotNull(el);
        assertEquals("root", el.nodeName());
        assertEquals("root", builder.currentElement().nodeName());
        assertEquals("root", builder.doc.nodeName());
    }
    
    @Test
    public void testProcessSelfClosingStartTag() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Token.StartTag startTag = new Token.StartTag("root", null, true);
        
        Element el = builder.insert(startTag);
        
        assertNotNull(el);
        assertEquals("root", el.nodeName());
        assertEquals("root", builder.currentElement().nodeName());
        assertEquals("root", builder.doc.nodeName());
        assertTrue(el.tag().isSelfClosing());
    }
    
    @Test
    public void testProcessEndTag() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Token.StartTag startTag = new Token.StartTag("root", null);
        
        builder.insert(startTag);
        
        Token.EndTag endTag = new Token.EndTag("root");
        builder.popStackToClose(endTag);
        
        assertEquals(0, builder.stack.size());
    }
    
    @Test
    public void testProcessComment() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Token.Comment comment = new Token.Comment("test");
        
        builder.insert(comment);
        
        assertEquals(1, builder.currentElement().childNodeSize());
        Comment insertedComment = (Comment) builder.currentElement().childNode(0);
        assertEquals("test", insertedComment.getData());
    }
    
    @Test
    public void testProcessCharacter() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Token.Character character = new Token.Character("test");
        
        builder.insert(character);
        
        assertEquals(1, builder.currentElement().childNodeSize());
        TextNode insertedText = (TextNode) builder.currentElement().childNode(0);
        assertEquals("test", insertedText.getWholeText());
    }
    
    @Test
    public void testProcessCData() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Token.Character character = new Token.Character("test");
        character.setCData(true);
        
        builder.insert(character);
        
        assertEquals(1, builder.currentElement().childNodeSize());
        CDataNode insertedText = (CDataNode) builder.currentElement().childNode(0);
        assertEquals("test", insertedText.getWholeText());
    }
    
    @Test
    public void testProcessDoctype() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Token.Doctype doctype = new Token.Doctype("html", "publicId", "systemId");
        
        builder.insert(doctype);
        
        assertEquals(1, builder.currentElement().childNodeSize());
        DocumentType insertedDoctype = (DocumentType) builder.currentElement().childNode(0);
        assertEquals("html", insertedDoctype.nodeName());
        assertEquals("publicId", insertedDoctype.attr("PUBLIC"));
        assertEquals("systemId", insertedDoctype.attr("SYSTEM"));
    }
    
    @Test
    public void testPopStackToCloseNotFound() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Token.StartTag startTag = new Token.StartTag("root", null);
        
        builder.insert(startTag);
        
        Token.EndTag endTag = new Token.EndTag("notfound");
        builder.popStackToClose(endTag);
        
        assertEquals(1, builder.stack.size());
    }
    
    @Test
    public void testPopStackToCloseFound() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Token.StartTag startTag1 = new Token.StartTag("root", null);
        Token.StartTag startTag2 = new Token.StartTag("child", null);
        
        builder.insert(startTag1);
        builder.insert(startTag2);
        
        Token.EndTag endTag = new Token.EndTag("root");
        builder.popStackToClose(endTag);
        
        assertEquals(1, builder.stack.size());
        assertEquals("child", builder.stack.get(0).nodeName());
    }
    
    @Test
    public void testParseFragment() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        String inputFragment = "<child>Test</child>";
        String baseUri = "http://example.com";
        ParseErrorList errors = new ParseErrorList();
        ParseSettings settings = builder.defaultSettings();
        
        List<Node> nodes = builder.parseFragment(inputFragment, baseUri, errors, settings);
        
        assertNotNull(nodes);
        assertEquals(1, nodes.size());
        assertEquals("child", nodes.get(0).nodeName());
        assertEquals("Test", nodes.get(0).text());
    }

}