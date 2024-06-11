import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.jsoup.parser.*;

public class XmlTreeBuilderTest {
    
    private XmlTreeBuilder xmlTreeBuilder;
    private String input;
    private String baseUri;
    private ParseErrorList errors;
    
    @Before
    public void setUp() {
        xmlTreeBuilder = new XmlTreeBuilder();
        input = "<html><head><title>Test</title></head><body><h1>Hello World</h1></body></html>";
        baseUri = "http://example.com";
        errors = new ParseErrorList();
    }
    
    // Test initialiseParse method
    
    @Test
    public void testInitialiseParse() {
        xmlTreeBuilder.initialiseParse(input, baseUri, errors);
        assertNotNull(xmlTreeBuilder.getDoc());
        assertEquals(input, xmlTreeBuilder.getInput());
        assertEquals(baseUri, xmlTreeBuilder.getBaseUri());
        assertEquals(errors, xmlTreeBuilder.getErrors());
        assertEquals(1, xmlTreeBuilder.getStack().size());
    }
    
    // Test process method
    
    @Test
    public void testProcessStartTag() {
        Token.StartTag startTag = new Token.StartTag("h1");
        boolean result = xmlTreeBuilder.process(startTag);
        assertTrue(result);
    }
    
    @Test
    public void testProcessEndTag() {
        Token.EndTag endTag = new Token.EndTag("h1");
        boolean result = xmlTreeBuilder.process(endTag);
        assertTrue(result);
    }
    
    @Test
    public void testProcessComment() {
        Token.Comment comment = new Token.Comment("This is a comment");
        boolean result = xmlTreeBuilder.process(comment);
        assertTrue(result);
    }
    
    @Test
    public void testProcessCharacter() {
        Token.Character character = new Token.Character("Hello World");
        boolean result = xmlTreeBuilder.process(character);
        assertTrue(result);
    }
    
    @Test
    public void testProcessDoctype() {
        Token.Doctype doctype = new Token.Doctype("html", "-//W3C//DTD XHTML 1.0 Strict//EN", "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd");
        boolean result = xmlTreeBuilder.process(doctype);
        assertTrue(result);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testProcessUnexpectedToken() {
        Token token = new Token(Token.TokenType.Unexpected, "");
        xmlTreeBuilder.process(token);
    }
    
    // Test insertNode method
    
    @Test
    public void testInsertNode() {
        Element element = new Element("div");
        xmlTreeBuilder.insertNode(element);
        assertEquals(element, xmlTreeBuilder.getCurrentElement().childNode(0));
    }
    
    // Test insert method
    
    @Test
    public void testInsertStartTag() {
        Token.StartTag startTag = new Token.StartTag("h1");
        Element element = xmlTreeBuilder.insert(startTag);
        assertNotNull(element);
        assertEquals("h1", element.nodeName());
        assertEquals(baseUri, element.baseUri());
        assertEquals(0, element.attributes().size());
        assertEquals(element, xmlTreeBuilder.getCurrentElement().childNode(0));
        assertFalse(startTag.isSelfClosing());
        assertTrue(xmlTreeBuilder.getStack().contains(element));
    }
    
    @Test
    public void testInsertSelfClosingTag() {
        Token.StartTag startTag = new Token.StartTag("br");
        startTag.setSelfClosing(true);
        Element element = xmlTreeBuilder.insert(startTag);
        assertNotNull(element);
        assertEquals("br", element.nodeName());
        assertTrue(element.tag().isKnownTag());
        assertTrue(element.tag().isSelfClosing());
        assertTrue(startTag.isSelfClosing());
        assertTrue(xmlTreeBuilder.getStack().isEmpty());
    }
    
    @Test
    public void testInsertComment() {
        Token.Comment comment = new Token.Comment("This is a comment");
        xmlTreeBuilder.insert(comment);
        assertEquals(comment.getData(), xmlTreeBuilder.getCurrentElement().childNode(0).outerHtml());
    }
    
    @Test
    public void testInsertCharacter() {
        Token.Character character = new Token.Character("Hello World");
        xmlTreeBuilder.insert(character);
        assertEquals(character.getData(), xmlTreeBuilder.getCurrentElement().childNode(0).outerHtml());
    }
    
    @Test
    public void testInsertDoctype() {
        Token.Doctype doctype = new Token.Doctype("html", "-//W3C//DTD XHTML 1.0 Strict//EN", "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd");
        xmlTreeBuilder.insert(doctype);
        assertEquals(doctype.getName(), xmlTreeBuilder.getCurrentElement().childNode(0).outerHtml());
    }
    
    // Test popStackToClose method
    
    @Test
    public void testPopStackToClose() {
        Element element = new Element("div");
        xmlTreeBuilder.getStack().push(element);
        xmlTreeBuilder.popStackToClose(new Token.EndTag("div"));
        assertTrue(xmlTreeBuilder.getStack().isEmpty());
    }
    
    @Test
    public void testPopStackToCloseNotFound() {
        Element element = new Element("div");
        xmlTreeBuilder.getStack().push(element);
        xmlTreeBuilder.popStackToClose(new Token.EndTag("h1"));
        assertFalse(xmlTreeBuilder.getStack().isEmpty());
    }
}