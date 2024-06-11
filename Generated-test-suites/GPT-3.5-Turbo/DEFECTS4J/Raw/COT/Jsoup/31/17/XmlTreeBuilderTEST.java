
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.jsoup.helper.Validate;
import org.jsoup.nodes.*;

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
        String baseUri = "https://example.com";
        ParseErrorList errors = new ParseErrorList();
        xmlTreeBuilder.initialiseParse(input, baseUri, errors);
        assertEquals(errors.size(), 0);
    }

    @Test
    public void testProcess_StartTag() {
        Token token = new Token();
        token.type = Token.TokenType.StartTag;
        Token.StartTag startTag = new Token.StartTag();
        startTag.name("div");
        token.asStartTag(startTag);
        assertTrue(xmlTreeBuilder.process(token));
    }

    @Test
    public void testProcess_EndTag() {
        Token token = new Token();
        token.type = Token.TokenType.EndTag;
        Token.EndTag endTag = new Token.EndTag();
        endTag.name("div");
        token.asEndTag(endTag);
        assertTrue(xmlTreeBuilder.process(token));
    }

    @Test
    public void testProcess_Comment() {
        Token token = new Token();
        token.type = Token.TokenType.Comment;
        Token.Comment commentToken = new Token.Comment();
        commentToken.data("This is a comment");
        token.asComment(commentToken);
        assertTrue(xmlTreeBuilder.process(token));
    }

    @Test
    public void testProcess_Character() {
        Token token = new Token();
        token.type = Token.TokenType.Character;
        Token.Character characterToken = new Token.Character();
        characterToken.data("This is a character");
        token.asCharacter(characterToken);
        assertTrue(xmlTreeBuilder.process(token));
    }

    @Test
    public void testProcess_Doctype() {
        Token token = new Token();
        token.type = Token.TokenType.Doctype;
        Token.Doctype doctypeToken = new Token.Doctype();
        doctypeToken.setName("html");
        token.asDoctype(doctypeToken);
        assertTrue(xmlTreeBuilder.process(token));
    }

    @Test
    public void testInsertNode() {
      Element element = new Element("test", "https://example.com");
      xmlTreeBuilder.insertNode(element);
      assertEquals(xmlTreeBuilder.currentElement().childNode(0), element);
    }
    
    @Test
    public void testInsert_StartTag() {
        Token.StartTag startTag = new Token.StartTag();
        startTag.name("div");
        startTag.addAttribute("id", "test-div");
        startTag.attributes.put("class", "test-class");
        Element result = xmlTreeBuilder.insert(startTag);
        assertNotNull(result);
        assertEquals(result.nodeName(), "div");
        assertEquals(result.attr("id"), "test-div");
        assertEquals(result.attr("class"), "test-class");
        assertEquals(xmlTreeBuilder.stack.peek(), result);
    }

    @Test
    public void testInsert_Comment() {
        Token.Comment commentToken = new Token.Comment("This is a comment");
        xmlTreeBuilder.insert(commentToken);
        Comment result = (Comment) xmlTreeBuilder.currentElement().childNode(0);
        assertNotNull(result);
        assertEquals(result.getData(), "This is a comment");
    }

    @Test
    public void testInsert_Character() {
        Token.Character characterToken = new Token.Character("This is a character");
        xmlTreeBuilder.insert(characterToken);
        TextNode result = (TextNode) xmlTreeBuilder.currentElement().childNode(0);
        assertNotNull(result);
        assertEquals(result.getWholeText(), "This is a character");
    }

    @Test
    public void testInsert_Doctype() {
        Token.Doctype doctypeToken = new Token.Doctype();
        doctypeToken.setName("html");
        doctypeToken.setPublicIdentifier("-//W3C//DTD XHTML 1.0 Transitional//EN");
        doctypeToken.setSystemIdentifier("http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd");
        xmlTreeBuilder.insert(doctypeToken);
        DocumentType result = (DocumentType) xmlTreeBuilder.currentElement().childNode(0);
        assertNotNull(result);
        assertEquals(result.nodeName(), "html");
        assertEquals(result.getPublicIdentifier(), "-//W3C//DTD XHTML 1.0 Transitional//EN");
        assertEquals(result.getSystemIdentifier(), "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd");
    }
    
    @Test
    public void testPopStackToClose_ElementExists() {
      Token.EndTag endTag = new Token.EndTag();
      endTag.name("div");
      Element element = new Element("div");
      xmlTreeBuilder.stack.push(element);
      xmlTreeBuilder.popStackToClose(endTag);
      assertFalse(xmlTreeBuilder.stack.contains(element));
    }

    @Test
    public void testPopStackToClose_ElementNotExists() {
      Token.EndTag endTag = new Token.EndTag();
      endTag.name("div");
      Element element = new Element("p");
      assertFalse(xmlTreeBuilder.stack.contains(element));
      xmlTreeBuilder.popStackToClose(endTag);
      assertFalse(xmlTreeBuilder.stack.contains(element));
    }
}
