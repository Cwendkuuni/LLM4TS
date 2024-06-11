
import org.junit.Test;
import org.jsoup.nodes.*;
import java.util.List;
import static org.junit.Assert.*;

public class XmlTreeBuilderTest {

    @Test
    public void testInitialiseParse() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        String input = "<xml><tag></tag></xml>";
        String baseUri = "http://example.com";
        ParseErrorList errors = new ParseErrorList();
        
        xmlTreeBuilder.initialiseParse(input, baseUri, errors);
        
        // Check that the document is placed onto the stack
        assertSame(xmlTreeBuilder.doc, xmlTreeBuilder.stack.get(0));
        
        // Check that the document output settings are set to XML syntax
        assertEquals(Document.OutputSettings.Syntax.xml, xmlTreeBuilder.doc.outputSettings().syntax());
    }
    
    @Test
    public void testProcess() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Token token = new Token(Tag.valueOf("tag"), Token.TokenType.StartTag);
        
        boolean result = xmlTreeBuilder.process(token);
        
        assertTrue(result);
        assertEquals(1, xmlTreeBuilder.currentElement().children().size());
    }

    @Test
    public void testInsertNode() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Element element = new Element(Tag.valueOf("tag"), "http://example.com");
        
        xmlTreeBuilder.insertNode(element);

        assertSame(element, xmlTreeBuilder.currentElement().children().get(0));
    }

    @Test
    public void testInsert() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Token.Comment commentToken = new Token.Comment("This is a comment");
        
        xmlTreeBuilder.insert(commentToken);
        
        assertTrue(xmlTreeBuilder.currentElement().childNode(0) instanceof Comment);
    }

    @Test
    public void testInsertNode_CharacterToken() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Token.Character characterToken = new Token.Character("This is a character token");
        
        xmlTreeBuilder.insert(characterToken);
        
        assertTrue(xmlTreeBuilder.currentElement().childNode(0) instanceof TextNode);
    }

    @Test
    public void testInsertNode_Doctype() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Token.Doctype doctype = new Token.Doctype("html", "-//W3C//DTD HTML 4.01//EN", "http://www.w3.org/TR/html4/strict.dtd");
        
        xmlTreeBuilder.insert(doctype);
        
        assertTrue(xmlTreeBuilder.currentElement().childNode(0) instanceof DocumentType);
    }

    @Test
    public void testPopStackToClose() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Element element1 = new Element(Tag.valueOf("tag1"), "http://example.com");
        Element element2 = new Element(Tag.valueOf("tag2"), "http://example.com");
        xmlTreeBuilder.stack.add(element1);
        xmlTreeBuilder.stack.add(element2);
        Token.EndTag endTag = new Token.EndTag("tag2");
        
        xmlTreeBuilder.popStackToClose(endTag);

        assertSame(element1, xmlTreeBuilder.stack.get(0));
        assertEquals(1, xmlTreeBuilder.stack.size());
    }

    @Test
    public void testParseFragment() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        String inputFragment = "<tag></tag>";
        String baseUri = "http://example.com";
        ParseErrorList errors = new ParseErrorList();
        
        List<Node> nodes = xmlTreeBuilder.parseFragment(inputFragment, baseUri, errors);

        assertEquals(1, nodes.size());
        assertTrue(nodes.get(0) instanceof Element);
    }
}

