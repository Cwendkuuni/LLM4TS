
import org.junit.Test;
import static org.junit.Assert.*;

public class XmlTreeBuilderTest {
    
    @Test
    public void initialiseParse_ValidInputAndBaseUri_Success() {
        String input = "<root></root>";
        String baseUri = "https://example.com";
        ParseErrorList errors = new ParseErrorList();
        
        XmlTreeBuilder builder = new XmlTreeBuilder();
        builder.initialiseParse(input, baseUri, errors);
        
        // Assert that the document is placed onto the stack
        assertTrue(builder.stack.contains(builder.doc));
        // Assert that output settings syntax is set to XML
        assertEquals(Document.OutputSettings.Syntax.xml, builder.doc.outputSettings().syntax());
    }
    
    @Test
    public void process_StartTagToken_Success() {
        Token.StartTag startTag = new Token.StartTag("div", "class", "container");
        
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Element element = builder.insert(startTag);
        
        // Assert that the element is inserted into the current element
        assertTrue(builder.currentElement().childNodes().contains(element));
        // Assert that the element is added to the stack if not self-closing
        assertTrue(builder.stack.contains(element));
        // Assert that the tag is set to unknown if not a known tag
        if (!Tag.valueOf(startTag.name()).isKnownTag()) {
            assertTrue(Tag.valueOf(startTag.name()).isSelfClosing());
        }
    }
    
    @Test
    public void process_EndTagToken_Success() {
        Token.EndTag endTag = new Token.EndTag("div");
        
        XmlTreeBuilder builder = new XmlTreeBuilder();
        builder.stack.add(new Element("div"));
        builder.insert(endTag);
        
        // Assert that the element is removed from the stack
        assertFalse(builder.stack.contains(builder.currentElement()));
    }
    
    @Test
    public void process_CommentToken_Success() {
        Token.Comment commentToken = new Token.Comment("This is a comment");
        String baseUri = "https://example.com";
        
        XmlTreeBuilder builder = new XmlTreeBuilder();
        builder.insert(commentToken);
        
        // Assert that the comment node is inserted into the current element
        assertTrue(builder.currentElement().childNodes().get(0) instanceof Comment);
        Comment comment = (Comment) builder.currentElement().childNodes().get(0);
        // Assert that the comment node has the correct data and base URI
        assertEquals(commentToken.getData(), comment.getData());
        assertEquals(baseUri, comment.baseUri());
    }
    
    @Test
    public void process_CharacterToken_Success() {
        Token.Character characterToken = new Token.Character("abc");
        String baseUri = "https://example.com";
        
        XmlTreeBuilder builder = new XmlTreeBuilder();
        builder.insert(characterToken);
        
        // Assert that the text node is inserted into the current element
        assertTrue(builder.currentElement().childNodes().get(0) instanceof TextNode);
        TextNode textNode = (TextNode) builder.currentElement().childNodes().get(0);
        // Assert that the text node has the correct data and base URI
        assertEquals(characterToken.getData(), textNode.getWholeText());
        assertEquals(baseUri, textNode.baseUri());
    }
    
    @Test
    public void process_DoctypeToken_Success() {
        Token.Doctype doctypeToken = new Token.Doctype("html", "", "", "https://example.com");
        
        XmlTreeBuilder builder = new XmlTreeBuilder();
        builder.insert(doctypeToken);
        
        // Assert that the document type node is inserted into the current element
        assertTrue(builder.currentElement().childNodes().get(0) instanceof DocumentType);
        DocumentType doctypeNode = (DocumentType) builder.currentElement().childNodes().get(0);
        // Assert that the document type node has the correct name, public identifier, system identifier, and base URI
        assertEquals(doctypeToken.getName(), doctypeNode.getName());
        assertEquals(doctypeToken.getPublicIdentifier(), doctypeNode.getPublicIdentifier());
        assertEquals(doctypeToken.getSystemIdentifier(), doctypeNode.getSystemIdentifier());
        assertEquals(doctypeToken.getBaseUri(), doctypeNode.baseUri());
    }
    
    @Test
    public void popStackToClose_ElementExistsInStack_Success() {
        Token.EndTag endTag = new Token.EndTag("div");
        
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Element element1 = new Element("p");
        Element element2 = new Element("div");
        builder.stack.add(element1);
        builder.stack.add(element2);
        
        builder.popStackToClose(endTag);
        
        // Assert that the element is removed from the stack
        assertFalse(builder.stack.contains(element2));
    }
    
    @Test
    public void popStackToClose_ElementNotExistsInStack_Success() {
        Token.EndTag endTag = new Token.EndTag("div");
        
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Element element1 = new Element("p");
        Element element2 = new Element("span");
        builder.stack.add(element1);
        builder.stack.add(element2);
        
        builder.popStackToClose(endTag);
        
        // Assert that the stack remains unchanged
        assertTrue(builder.stack.contains(element1));
        assertTrue(builder.stack.contains(element2));
    }
    
    @Test
    public void parseFragment_ValidInputAndBaseUri_Success() {
        String input = "<a href=\"https://example.com\">Link</a>";
        String baseUri = "https://example.com";
        ParseErrorList errors = new ParseErrorList();
        
        XmlTreeBuilder builder = new XmlTreeBuilder();
        List<Node> nodes = builder.parseFragment(input, baseUri, errors);
        
        // Assert that the returned nodes include the element and its child nodes
        assertEquals(1, nodes.size());
        assertTrue(nodes.get(0) instanceof Element);
        assertEquals(1, nodes.get(0).childNodes().size());
        assertTrue(nodes.get(0).childNodes().get(0) instanceof TextNode);
    }
    
}

