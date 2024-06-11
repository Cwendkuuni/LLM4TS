import org.jsoup.parser.*;
import org.jsoup.nodes.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class XmlTreeBuilderTest {

    @Test
    public void testInitialiseParse() {
        // test the initialiseParse method
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        ParseErrorList errors = new ParseErrorList();
        xmlTreeBuilder.initialiseParse("input", "baseUri", errors);
        
        // verify that the document is placed onto the stack
        assertEquals(xmlTreeBuilder.stack.peek(), xmlTreeBuilder.doc);
    }

    @Test
    public void testProcess() {
        // test the process method with different token types
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Token token = new Token(Token.TokenType.StartTag, "<div>");
        assertTrue(xmlTreeBuilder.process(token));
        
        token = new Token(Token.TokenType.Comment, "<!-- comment -->");
        assertTrue(xmlTreeBuilder.process(token));
        
        token = new Token(Token.TokenType.EOF, "");
        assertTrue(xmlTreeBuilder.process(token));
    }

    @Test
    public void testInsertNode() {
        // test the insertNode method
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Element element = new Element("div");
        xmlTreeBuilder.insertNode(element);
        
        // verify that the element is appended to the current element
        assertEquals(xmlTreeBuilder.currentElement().childNode(0), element);
    }

    @Test
    public void testInsertStartTag() {
        // test the insert method for StartTag
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Token.StartTag startTag = new Token.StartTag("div", new Attributes());
        Element element = xmlTreeBuilder.insert(startTag);
        
        // verify that the element is added to the stack
        assertTrue(xmlTreeBuilder.stack.contains(element));
    }

    @Test
    public void testInsertComment() {
        // test the insert method for Comment
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Token.Comment commentToken = new Token.Comment("comment");
        xmlTreeBuilder.insert(commentToken);
        
        // verify that the comment is inserted correctly
        assertTrue(xmlTreeBuilder.currentElement().childNode(0) instanceof Comment);
    }

    @Test
    public void testInsertCharacter() {
        // test the insert method for Character
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Token.Character characterToken = new Token.Character("data");
        xmlTreeBuilder.insert(characterToken);
        
        // verify that the character is inserted correctly
        assertTrue(xmlTreeBuilder.currentElement().childNode(0) instanceof TextNode);
    }

    @Test
    public void testInsertDoctype() {
        // test the insert method for Doctype
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Token.Doctype doctype = new Token.Doctype("html", "", "", "");
        xmlTreeBuilder.insert(doctype);
        
        // verify that the doctype is inserted correctly
        assertTrue(xmlTreeBuilder.currentElement().childNode(0) instanceof DocumentType);
    }

    @Test
    public void testPopStackToClose() {
        // test the popStackToClose method
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        
        // push an element onto the stack
        Element element = new Element("div");
        xmlTreeBuilder.stack.push(element);
        
        // create an EndTag token with the same name as the element
        Token.EndTag endTag = new Token.EndTag("div");
        xmlTreeBuilder.popStackToClose(endTag);
        
        // verify that the element is removed from the stack
        assertFalse(xmlTreeBuilder.stack.contains(element));
    }
}