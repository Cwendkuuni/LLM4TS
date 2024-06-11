import org.junit.Before;
import org.junit.Test;
import org.jsoup.parser.*;

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
        assertEquals(1, xmlTreeBuilder.stack.size());
        assertEquals("root", xmlTreeBuilder.currentElement().nodeName());
    }
    
    @Test
    public void testInsert_InsertStartTag() {
        Token.StartTag startTag = new Token.StartTag("div", "class=something");
        Element el = xmlTreeBuilder.insert(startTag);
        
        assertNotNull(el);
        assertEquals("div", el.nodeName());
        assertEquals("class=something", el.attr("class"));
    }
    
    @Test
    public void testInsert_InsertSelfClosingStartTag() {
        Token.StartTag startTag = new Token.StartTag("img", "src=image.jpg /");
        Element el = xmlTreeBuilder.insert(startTag);
        
        assertNotNull(el);
        assertEquals("img", el.nodeName());
        assertTrue(el.tag().isKnownTag());
        assertTrue(el.tag().isSelfClosing());
    }
    
    @Test
    public void testInsert_InsertUnknownSelfClosingStartTag() {
        Token.StartTag startTag = new Token.StartTag("fakeTag", "customAttr=value /");
        Element el = xmlTreeBuilder.insert(startTag);
        
        assertNotNull(el);
        assertEquals("fakeTag", el.nodeName());
        assertFalse(el.tag().isKnownTag());
        assertTrue(el.tag().isSelfClosing());
    }
    
    @Test
    public void testInsert_InsertComment() {
        Token.Comment commentToken = new Token.Comment("This is a comment");
        xmlTreeBuilder.insert(commentToken);
        
        Node lastChild = xmlTreeBuilder.currentElement().childNode(0);
        assertTrue(lastChild instanceof Comment);
        assertEquals("This is a comment", ((Comment) lastChild).getData());
    }
    
    @Test
    public void testInsert_InsertCharacter() {
        Token.Character characterToken = new Token.Character("Some text");
        xmlTreeBuilder.insert(characterToken);

        Node lastChild = xmlTreeBuilder.currentElement().childNode(0);
        assertTrue(lastChild instanceof TextNode);
        assertEquals("Some text", ((TextNode) lastChild).getWholeText());
    }
    
    @Test
    public void testInsert_InsertDoctype() {
        Token.Doctype doctype = new Token.Doctype("html", "-//W3C//DTD HTML 4.01 Transitional//EN", "http://www.w3.org/TR/html4/loose.dtd");
        xmlTreeBuilder.insert(doctype);

        Node lastChild = xmlTreeBuilder.currentElement().childNode(0);
        assertTrue(lastChild instanceof DocumentType);
        assertEquals("html", ((DocumentType) lastChild).getName());
        assertEquals("-//W3C//DTD HTML 4.01 Transitional//EN", ((DocumentType) lastChild).getPublicIdentifier());
        assertEquals("http://www.w3.org/TR/html4/loose.dtd", ((DocumentType) lastChild).getSystemIdentifier());
    }
    
    @Test
    public void testPopStackToClose_ElementFoundAtTop() {
        Token.EndTag endTag = new Token.EndTag("div");
        Element el = new Element(Tag.valueOf("div"), "", "");

        xmlTreeBuilder.stack.push(el);
        xmlTreeBuilder.popStackToClose(endTag);

        assertTrue(xmlTreeBuilder.stack.isEmpty());
    }
    
    @Test
    public void testPopStackToClose_ElementFoundInMiddle() {
        Token.EndTag endTag = new Token.EndTag("div");
        Element el1 = new Element(Tag.valueOf("span"), "", "");
        Element el2 = new Element(Tag.valueOf("div"), "", "");

        xmlTreeBuilder.stack.push(el1);
        xmlTreeBuilder.stack.push(el2);
        xmlTreeBuilder.popStackToClose(endTag);

        assertFalse(xmlTreeBuilder.stack.isEmpty());
        assertEquals(el1, xmlTreeBuilder.stack.pop());
    }
    
    @Test
    public void testPopStackToClose_ElementNotFound() {
        Token.EndTag endTag = new Token.EndTag("div");
        Element el1 = new Element(Tag.valueOf("span"), "", "");
        Element el2 = new Element(Tag.valueOf("p"), "", "");

        xmlTreeBuilder.stack.push(el1);
        xmlTreeBuilder.stack.push(el2);
        xmlTreeBuilder.popStackToClose(endTag);

        assertFalse(xmlTreeBuilder.stack.isEmpty());
        assertEquals(el2, xmlTreeBuilder.stack.pop());
    }

}