
import static org.junit.Assert.*;
import org.junit.Test;
import org.jsoup.parser.*;

public class XmlTreeBuilderTest {

    @Test
    public void testInitialiseParse() {
        XmlTreeBuilder treeBuilder = new XmlTreeBuilder();
        
        // Typical input
        String input = "<root></root>";
        String baseUri = "http://example.com";
        ParseErrorList errors = new ParseErrorList();
        
        treeBuilder.initialiseParse(input, baseUri, errors);
        
        // Assert that the document is placed onto the stack
        assertTrue(treeBuilder.stack.contains(treeBuilder.doc));
    }
    
    @Test
    public void testProcess() {
        XmlTreeBuilder treeBuilder = new XmlTreeBuilder();
        
        // Test StartTag
        Token startTagToken = new Token(TokenType.StartTag, "tag");
        boolean result = treeBuilder.process(startTagToken);
        
        // Assert that the token is correctly inserted
        assertTrue(treeBuilder.currentElement().hasAttr("tag"));
        assertTrue(result);
        
        // Test EndTag
        Token endTagToken = new Token(TokenType.EndTag, "tag");
        result = treeBuilder.process(endTagToken);
        
        // Assert that the stack is correctly popped
        assertFalse(treeBuilder.stack.contains(treeBuilder.currentElement()));
        assertTrue(result);
        
        // Test Character
        Token characterToken = new Token(TokenType.Character, "text");
        result = treeBuilder.process(characterToken);
        
        // Assert that the character is correctly inserted
        assertEquals(1, treeBuilder.currentElement().childNodeSize());
        assertTrue(result);
        
        // Test Comment
        Token commentToken = new Token(TokenType.Comment, "comment");
        result = treeBuilder.process(commentToken);
        
        // Assert that the comment is correctly inserted
        assertEquals(2, treeBuilder.currentElement().childNodeSize());
        assertTrue(result);
        
        // Test Doctype
        Token doctypeToken = new Token(TokenType.Doctype, "doctype");
        result = treeBuilder.process(doctypeToken);
        
        // Assert that the doctype is correctly inserted
        assertEquals(3, treeBuilder.currentElement().childNodeSize());
        assertTrue(result);
        
        // Test EOF
        Token eofToken = new Token(TokenType.EOF);
        result = treeBuilder.process(eofToken);
        
        // Assert that the result is true
        assertTrue(result);
    }

    @Test
    public void testInsertNode() {
        XmlTreeBuilder treeBuilder = new XmlTreeBuilder();

        // Create a new element
        Element element = new Element(Tag.valueOf("div"), "http://example.com");
        treeBuilder.insertNode(element);

        // Assert that the element is the child of the current element
        assertTrue(treeBuilder.currentElement().childNodeSize() == 1);
        assertTrue(treeBuilder.currentElement().childNode(0) == element);
    }
    
    @Test
    public void testInsertStartTag() {
        XmlTreeBuilder treeBuilder = new XmlTreeBuilder();
        
        // Typical start tag
        Token.StartTag startTag = new Token.StartTag("tag", "attr1=value1 attr2=value2");
        Element element = treeBuilder.insert(startTag);
        
        // Assert that the start tag is correctly inserted
        assertTrue(treeBuilder.stack.contains(element));
        assertTrue(treeBuilder.currentElement() == element);
        assertTrue(treeBuilder.currentElement().tagName().equals("tag"));
        assertTrue(treeBuilder.currentElement().hasAttr("attr1"));
        assertTrue(treeBuilder.currentElement().hasAttr("attr2"));
        
        // Self-closing start tag
        Token.StartTag selfClosingStartTag = new Token.StartTag("selfClosingTag", "attr=value");
        element = treeBuilder.insert(selfClosingStartTag);
        
        // Assert that the self-closing start tag is correctly inserted
        assertFalse(treeBuilder.stack.contains(element));
        assertTrue(treeBuilder.currentElement() == element);
        assertTrue(treeBuilder.currentElement().tagName().equals("selfClosingTag"));
        assertTrue(treeBuilder.currentElement().hasAttr("attr"));
        assertTrue(treeBuilder.currentElement().isSelfClosing());
        assertTrue(treeBuilder.currentElement().tag().isKnownTag());
        
        // Unknown self-closing start tag
        Token.StartTag unknownSelfClosingStartTag = new Token.StartTag("unknownTag", "attr=value");
        element = treeBuilder.insert(unknownSelfClosingStartTag);
        
        // Assert that the unknown self-closing start tag is correctly inserted
        assertFalse(treeBuilder.stack.contains(element));
        assertTrue(treeBuilder.currentElement() == element);
        assertTrue(treeBuilder.currentElement().tagName().equals("unknownTag"));
        assertTrue(treeBuilder.currentElement().hasAttr("attr"));
        assertTrue(treeBuilder.currentElement().isSelfClosing());
        assertFalse(treeBuilder.currentElement().tag().isKnownTag());
    }
    
    @Test
    public void testInsertComment() {
        XmlTreeBuilder treeBuilder = new XmlTreeBuilder();
        
        // Typical comment
        Token.Comment comment = new Token.Comment("comment");
        treeBuilder.insert(comment);
        
        // Assert that the comment is correctly inserted
        assertNotNull(treeBuilder.currentElement().childNode(0));
        assertTrue(treeBuilder.currentElement().childNode(0) instanceof Comment);
        assertTrue(treeBuilder.currentElement().childNode(0).outerHtml().equals("<!--comment-->"));
    }
    
    @Test
    public void testInsertCharacter() {
        XmlTreeBuilder treeBuilder = new XmlTreeBuilder();
        
        // Typical character
        Token.Character character = new Token.Character("text");
        treeBuilder.insert(character);
        
        // Assert that the character is correctly inserted
        assertNotNull(treeBuilder.currentElement().childNode(0));
        assertTrue(treeBuilder.currentElement().childNode(0) instanceof TextNode);
        assertTrue(treeBuilder.currentElement().childNode(0).outerHtml().equals("text"));
    }
    
    @Test
    public void testInsertDoctype() {
        XmlTreeBuilder treeBuilder = new XmlTreeBuilder();
        
        // Typical doctype
        Token.Doctype doctype = new Token.Doctype("html", "", "", "");
        treeBuilder.insert(doctype);
        
        // Assert that the doctype is correctly inserted
        assertNotNull(treeBuilder.currentElement().childNode(0));
        assertTrue(treeBuilder.currentElement().childNode(0) instanceof DocumentType);
        assertTrue(treeBuilder.currentElement().childNode(0).outerHtml().equals("<!DOCTYPE html>"));
    }
    
    @Test
    public void testPopStackToClose() {
        XmlTreeBuilder treeBuilder = new XmlTreeBuilder();
        
        // Create a stack of elements
        treeBuilder.stack.add(new Element(Tag.valueOf("p"), "http://example.com"));
        treeBuilder.stack.add(new Element(Tag.valueOf("div"), "http://example.com"));
        treeBuilder.stack.add(new Element(Tag.valueOf("div"), "http://example.com"));
        
        // End tag not found
        Token.EndTag endTag = new Token.EndTag("span");
        treeBuilder.popStackToClose(endTag);
        
        // Assert that the stack remains the same
        assertEquals(3, treeBuilder.stack.size());
        
        // End tag found
        endTag = new Token.EndTag("div");
        treeBuilder.popStackToClose(endTag);
        
        // Assert that the stack is correctly popped
        assertEquals(2, treeBuilder.stack.size());
    }
}

