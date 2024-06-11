import org.junit.Test;
import org.jsoup.nodes.*;
import org.jsoup.parser.ParseErrorList;

import java.util.List;

import static org.junit.Assert.*;

public class XmlTreeBuilderTest {
    @Test
    public void testInitialiseParse() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        
        String input = "<html><head></head><body></body></html>";
        String baseUri = "http://example.com";
        ParseErrorList errors = new ParseErrorList();
        
        xmlTreeBuilder.initialiseParse(input, baseUri, errors);
        
        // Assertion statements
        assertTrue(xmlTreeBuilder.stack.contains(xmlTreeBuilder.doc));
        assertEquals(Document.OutputSettings.Syntax.xml, xmlTreeBuilder.doc.outputSettings().syntax());
    }

    @Test
    public void testProcess_StartTag() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Element element = new Element(Tag.valueOf("div"), "http://example.com");
        Token.StartTag startTag = new Token.StartTag("div", new Attributes());
        
        xmlTreeBuilder.stack.add(element);
        
        xmlTreeBuilder.process(startTag);
        
        // Assertion statements
        assertEquals(1, element.childNodes().size());
        assertTrue(element.childNodes().get(0) instanceof Element);
    }

    @Test
    public void testProcess_EndTag() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Element element = new Element(Tag.valueOf("div"), "http://example.com");
        Token.EndTag endTag = new Token.EndTag("div");
        
        xmlTreeBuilder.stack.add(element);
        
        xmlTreeBuilder.process(endTag);
        
        // Assertion statements
        assertEquals(0, xmlTreeBuilder.stack.size());
    }

    @Test
    public void testProcess_Comment() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Element element = new Element(Tag.valueOf("div"), "http://example.com");
        Token.Comment comment = new Token.Comment("comment");
        
        xmlTreeBuilder.stack.add(element);
        
        xmlTreeBuilder.process(comment);
        
        // Assertion statements
        assertEquals(1, element.childNodes().size());
        assertTrue(element.childNodes().get(0) instanceof Comment);
    }

    @Test
    public void testProcess_Character() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Element element = new Element(Tag.valueOf("div"), "http://example.com");
        Token.Character character = new Token.Character("character");
        
        xmlTreeBuilder.stack.add(element);
        
        xmlTreeBuilder.process(character);
        
        // Assertion statements
        assertEquals(1, element.childNodes().size());
        assertTrue(element.childNodes().get(0) instanceof TextNode);
    }

    @Test
    public void testProcess_Doctype() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Element element = new Element(Tag.valueOf("div"), "http://example.com");
        Token.Doctype doctype = new Token.Doctype("doctype", "", "", "");
        
        xmlTreeBuilder.stack.add(element);
        
        xmlTreeBuilder.process(doctype);
        
        // Assertion statements
        assertEquals(1, element.childNodes().size());
        assertTrue(element.childNodes().get(0) instanceof DocumentType);
    }

    @Test
    public void testInsertNode() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Element element = new Element(Tag.valueOf("div"), "http://example.com");
        Token.StartTag startTag = new Token.StartTag("div", new Attributes());
        
        xmlTreeBuilder.stack.add(element);
        
        xmlTreeBuilder.insert(startTag);
        
        // Assertion statements
        assertEquals(1, element.childNodes().size());
        assertTrue(element.childNodes().get(0) instanceof Element);
    }

    @Test
    public void testInsert_StartTag_SelfClosing() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Element element = new Element(Tag.valueOf("div"), "http://example.com");
        Token.StartTag startTag = new Token.StartTag("div", new Attributes(), true);
        
        xmlTreeBuilder.stack.add(element);
        
        Element result = xmlTreeBuilder.insert(startTag);
        
        // Assertion statements
        assertTrue(startTag.isSelfClosing());
        assertTrue(result.tag().isKnownTag());
        assertTrue(result.tag().isSelfClosing());
    }

    @Test
    public void testInsert_Comment() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Element element = new Element(Tag.valueOf("div"), "http://example.com");
        Token.Comment comment = new Token.Comment("comment");
        
        xmlTreeBuilder.stack.add(element);
        
        xmlTreeBuilder.insert(comment);
        
        // Assertion statements
        assertEquals(1, element.childNodes().size());
        assertTrue(element.childNodes().get(0) instanceof Comment);
    }

    @Test
    public void testInsert_Character() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Element element = new Element(Tag.valueOf("div"), "http://example.com");
        Token.Character character = new Token.Character("character");
        
        xmlTreeBuilder.stack.add(element);
        
        xmlTreeBuilder.insert(character);
        
        // Assertion statements
        assertEquals(1, element.childNodes().size());
        assertTrue(element.childNodes().get(0) instanceof TextNode);
    }

    @Test
    public void testInsert_Doctype() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Element element = new Element(Tag.valueOf("div"), "http://example.com");
        Token.Doctype doctype = new Token.Doctype("doctype", "", "", "");
        
        xmlTreeBuilder.stack.add(element);
        
        xmlTreeBuilder.insert(doctype);
        
        // Assertion statements
        assertEquals(1, element.childNodes().size());
        assertTrue(element.childNodes().get(0) instanceof DocumentType);
    }

    @Test
    public void testPopStackToClose() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Element element1 = new Element(Tag.valueOf("div"), "http://example.com");
        Element element2 = new Element(Tag.valueOf("div"), "http://example.com");
        Token.EndTag endTag = new Token.EndTag("div");
        
        xmlTreeBuilder.stack.add(element1);
        xmlTreeBuilder.stack.add(element2);
        
        xmlTreeBuilder.popStackToClose(endTag);
        
        // Assertion statements
        assertEquals(0, xmlTreeBuilder.stack.size());
    }

    @Test
    public void testParseFragment() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        
        String inputFragment = "<div></div>";
        String baseUri = "http://example.com";
        ParseErrorList errors = new ParseErrorList();
        
        List<Node> result = xmlTreeBuilder.parseFragment(inputFragment, baseUri, errors);
        
        // Assertion statements
        assertNotNull(result);
        assertEquals(1, result.size());
    }
}