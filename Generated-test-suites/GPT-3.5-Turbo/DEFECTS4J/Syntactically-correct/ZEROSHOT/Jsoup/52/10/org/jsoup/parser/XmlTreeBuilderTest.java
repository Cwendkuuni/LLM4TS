import org.jsoup.Jsoup;
import org.jsoup.helper.Validate;
import org.jsoup.nodes.*;
import org.jsoup.parser.ParseErrorList;
import org.jsoup.parser.Token;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

public class XmlTreeBuilderTest {
    
    private XmlTreeBuilder xmlTreeBuilder;
    
    @Before
    public void setUp() {
        xmlTreeBuilder = new XmlTreeBuilder();
    }
    
    @Test
    public void testInitialiseParse() {
        String input = "<root></root>";
        String baseUri = "http://example.com";
        ParseErrorList errors = new ParseErrorList();
        
        xmlTreeBuilder.initialiseParse(input, baseUri, errors);
        
        assertEquals(input, xmlTreeBuilder.getInput());
        assertEquals(baseUri, xmlTreeBuilder.getBaseUri());
        assertEquals(errors, xmlTreeBuilder.getErrors());
        assertEquals(1, xmlTreeBuilder.getStack().size());
        assertTrue(xmlTreeBuilder.getStack().contains(xmlTreeBuilder.getDoc()));
        assertEquals(Document.OutputSettings.Syntax.xml, xmlTreeBuilder.getDoc().outputSettings().syntax());
    }
    
    @Test
    public void testProcess_startTag() {
        Token.StartTag startTag = new Token.StartTag("div");
        
        assertTrue(xmlTreeBuilder.process(new Token(startTag)));
        
        assertEquals(2, xmlTreeBuilder.getStack().size());
        assertEquals(Tag.valueOf("div"), xmlTreeBuilder.getCurrentElement().tag());
        assertEquals(xmlTreeBuilder.getDoc(), xmlTreeBuilder.getStack().get(0));
    }
    
    @Test
    public void testProcess_endTag() {
        Token.StartTag startTag = new Token.StartTag("div");
        Token.EndTag endTag = new Token.EndTag("div");
        
        xmlTreeBuilder.process(new Token(startTag));
        
        assertTrue(xmlTreeBuilder.process(new Token(endTag)));
        
        assertEquals(1, xmlTreeBuilder.getStack().size());
        assertEquals(xmlTreeBuilder.getDoc(), xmlTreeBuilder.getStack().get(0));
    }
    
    @Test
    public void testProcess_comment() {
        Token.Comment comment = new Token.Comment("This is a comment");
        
        assertTrue(xmlTreeBuilder.process(new Token(comment)));
        
        assertEquals(1, xmlTreeBuilder.getCurrentElement().childNodes().size());
        assertTrue(xmlTreeBuilder.getCurrentElement().childNode(0) instanceof Comment);
    }
    
    @Test
    public void testProcess_character() {
        Token.Character character = new Token.Character("This is a character");
        
        assertTrue(xmlTreeBuilder.process(new Token(character)));
        
        assertEquals(1, xmlTreeBuilder.getCurrentElement().childNodes().size());
        assertTrue(xmlTreeBuilder.getCurrentElement().childNode(0) instanceof TextNode);
    }
    
    @Test
    public void testProcess_doctype() {
        Token.Doctype doctype = new Token.Doctype("html");
        
        assertTrue(xmlTreeBuilder.process(new Token(doctype)));
        
        assertEquals(1, xmlTreeBuilder.getCurrentElement().childNodes().size());
        assertTrue(xmlTreeBuilder.getCurrentElement().childNode(0) instanceof DocumentType);
    }
    
    @Test
    public void testInsertNode() {
        xmlTreeBuilder.insertNode(new Comment("Comment"));
        
        assertEquals(1, xmlTreeBuilder.getCurrentElement().childNodes().size());
        assertTrue(xmlTreeBuilder.getCurrentElement().childNode(0) instanceof Comment);
    }
    
    @Test
    public void testInsert_startTag() {
        Token.StartTag startTag = new Token.StartTag("div");
        
        Element element = xmlTreeBuilder.insert(startTag);
        
        assertEquals(2, xmlTreeBuilder.getStack().size());
        assertEquals(Tag.valueOf("div"), element.tag());
        assertEquals(element, xmlTreeBuilder.getStack().get(1));
        assertFalse(element.tag().isSelfClosing());
    }
    
    @Test
    public void testInsert_startTag_selfClosing() {
        Token.StartTag startTag = new Token.StartTag("div");
        startTag.selfClosing = true;
        
        Element element = xmlTreeBuilder.insert(startTag);
        
        assertEquals(1, xmlTreeBuilder.getStack().size());
        assertTrue(element.tag().isSelfClosing());
    }
    
    @Test
    public void testInsert_comment() {
        Token.Comment commentToken = new Token.Comment("This is a comment");
        
        xmlTreeBuilder.insert(commentToken);
        
        assertEquals(1, xmlTreeBuilder.getCurrentElement().childNodes().size());
        assertTrue(xmlTreeBuilder.getCurrentElement().childNode(0) instanceof Comment);
    }
    
    @Test
    public void testInsert_character() {
        Token.Character characterToken = new Token.Character("This is a character");
        
        xmlTreeBuilder.insert(characterToken);
        
        assertEquals(1, xmlTreeBuilder.getCurrentElement().childNodes().size());
        assertTrue(xmlTreeBuilder.getCurrentElement().childNode(0) instanceof TextNode);
    }
    
    @Test
    public void testInsert_doctype() {
        Token.Doctype doctype = new Token.Doctype("html");
        
        xmlTreeBuilder.insert(doctype);
        
        assertEquals(1, xmlTreeBuilder.getCurrentElement().childNodes().size());
        assertTrue(xmlTreeBuilder.getCurrentElement().childNode(0) instanceof DocumentType);
    }
    
    @Test
    public void testPopStackToClose_found() {
        Token.StartTag startTag1 = new Token.StartTag("div");
        Token.StartTag startTag2 = new Token.StartTag("span");
        Token.StartTag startTag3 = new Token.StartTag("div");
        Token.EndTag endTag = new Token.EndTag("div");
        
        xmlTreeBuilder.process(new Token(startTag1));
        xmlTreeBuilder.process(new Token(startTag2));
        xmlTreeBuilder.process(new Token(startTag3));
        
        xmlTreeBuilder.popStackToClose(endTag);
        
        assertEquals(2, xmlTreeBuilder.getStack().size());
        assertEquals(Tag.valueOf("span"), xmlTreeBuilder.getCurrentElement().tag());
    }
    
    @Test
    public void testPopStackToClose_notFound() {
        Token.StartTag startTag1 = new Token.StartTag("div");
        Token.StartTag startTag2 = new Token.StartTag("span");
        Token.StartTag startTag3 = new Token.StartTag("div");
        Token.EndTag endTag = new Token.EndTag("p");
        
        xmlTreeBuilder.process(new Token(startTag1));
        xmlTreeBuilder.process(new Token(startTag2));
        xmlTreeBuilder.process(new Token(startTag3));
        
        xmlTreeBuilder.popStackToClose(endTag);
        
        assertEquals(3, xmlTreeBuilder.getStack().size());
        assertEquals(Tag.valueOf("div"), xmlTreeBuilder.getCurrentElement().tag());
    }
    
    @Test
    public void testParseFragment() {
        String inputFragment = "<div><span></span></div>";
        String baseUri = "http://example.com";
        ParseErrorList errors = new ParseErrorList();
        
        List<Node> nodes = xmlTreeBuilder.parseFragment(inputFragment, baseUri, errors);
        
        assertEquals(1, nodes.size());
        assertTrue(nodes.get(0) instanceof Element);
        assertEquals(Tag.valueOf("div"), ((Element) nodes.get(0)).tag());
    }
}