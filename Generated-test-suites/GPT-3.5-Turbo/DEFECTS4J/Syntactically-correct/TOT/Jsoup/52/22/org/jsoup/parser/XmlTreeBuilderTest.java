import org.jsoup.parser.*;
import org.jsoup.nodes.*;
import org.jsoup.helper.*;
import org.junit.Before;
import org.junit.Test;
import java.util.List;
import static org.junit.Assert.*;

public class XmlTreeBuilderTest {

    private XmlTreeBuilder xmlTreeBuilder;
    private ParseErrorList errors;

    @Before
    public void setUp() {
        xmlTreeBuilder = new XmlTreeBuilder();
        errors = new ParseErrorList();
    }

    // test initialiseParse method
    @Test
    public void testInitialiseParse() {
        xmlTreeBuilder.initialiseParse("<html><head></head><body></body></html>", "", errors);
        
        assertEquals("Document should be added to the stack", 1, xmlTreeBuilder.stack.size());
        assertEquals("Output syntax should be set to XML", Document.OutputSettings.Syntax.xml, xmlTreeBuilder.doc.outputSettings().syntax());
    }

    // test process method for StartTag token
    @Test
    public void testProcessStartTag() {
        Token token = new Token.StartTag("div", null, false);
        
        xmlTreeBuilder.stack.add(new Element("body", ""));
        xmlTreeBuilder.process(token);
        
        assertEquals("Element should be inserted to the current element", 1, xmlTreeBuilder.currentElement().children().size());
        assertEquals("Stack size should be updated", 2, xmlTreeBuilder.stack.size());
    }

    // test process method for EndTag token
    @Test
    public void testProcessEndTag() {
        Token token = new Token.EndTag("div");
        
        xmlTreeBuilder.stack.add(new Element("div", ""));
        xmlTreeBuilder.stack.add(new Element("body", ""));
        xmlTreeBuilder.process(token);
        
        assertEquals("Stack size should be reduced", 1, xmlTreeBuilder.stack.size());
    }

    // test process method for Comment token
    @Test
    public void testProcessComment() {
        Token.Comment token = new Token.Comment("comment");
        
        xmlTreeBuilder.stack.add(new Element("body", ""));
        xmlTreeBuilder.process(token);
        
        assertEquals("Comment node should be inserted to the current element", 1, xmlTreeBuilder.currentElement().childNodes().size());
    }

    // test process method for Character token
    @Test
    public void testProcessCharacter() {
        Token.Character token = new Token.Character("A");
        
        xmlTreeBuilder.stack.add(new Element("body", ""));
        xmlTreeBuilder.process(token);
        
        assertEquals("Character node should be inserted to the current element", 1, xmlTreeBuilder.currentElement().childNodes().size());
    }

    // test process method for Doctype token
    @Test
    public void testProcessDoctype() {
        Token.Doctype token = new Token.Doctype("html", "", "", "");
        
        xmlTreeBuilder.process(token);
        
        assertEquals("Doctype node should be inserted to the document", 1, xmlTreeBuilder.doc.childNodes().size());
    }

    // test insertNode method
    @Test
    public void testInsertNode() {
        TextNode textNode = new TextNode("text", "");
        
        xmlTreeBuilder.stack.add(new Element("body", ""));
        xmlTreeBuilder.insertNode(textNode);
        
        assertTrue("Text node should be inserted to the current element", xmlTreeBuilder.currentElement().hasText());
    }

    // test insert method for StartTag token
    @Test
    public void testInsertStartTag() {
        Token.StartTag startTag = new Token.StartTag("div", null, false);
        
        xmlTreeBuilder.stack.add(new Element("body", ""));
        Element element = xmlTreeBuilder.insert(startTag);
        
        assertEquals("Element should be inserted to the current element", 1, xmlTreeBuilder.currentElement().children().size());
        assertFalse("Tag should not be self-closing", element.tag().isSelfClosing());
        assertTrue("Element should be pushed onto the stack", xmlTreeBuilder.stack.contains(element));
    }

    // test insert method for Comment token
    @Test
    public void testInsertComment() {
        Token.Comment comment = new Token.Comment("comment");
        
        xmlTreeBuilder.stack.add(new Element("body", ""));
        xmlTreeBuilder.insert(comment);
        
        assertEquals("Comment node should be inserted to the current element", 1, xmlTreeBuilder.currentElement().childNodes().size());
    }

    // test insert method for Character token
    @Test
    public void testInsertCharacter() {
        Token.Character character = new Token.Character("A");
        
        xmlTreeBuilder.stack.add(new Element("body", ""));
        xmlTreeBuilder.insert(character);
    
        assertEquals("Character node should be inserted to the current element", 1, xmlTreeBuilder.currentElement().childNodes().size());
    }

    // test insert method for Doctype token
    @Test
    public void testInsertDoctype() {
        Token.Doctype doctype = new Token.Doctype("html", "", "", "");
        
        xmlTreeBuilder.stack.add(new Element("body", ""));
        xmlTreeBuilder.insert(doctype);
        
        assertEquals("Doctype node should be inserted to the current element", 1, xmlTreeBuilder.currentElement().childNodes().size());
    }

    // test popStackToClose method
    @Test
    public void testPopStackToClose() {
        Token.EndTag endTag = new Token.EndTag("div");
        
        xmlTreeBuilder.stack.add(new Element("span", ""));
        xmlTreeBuilder.stack.add(new Element("div", ""));
        xmlTreeBuilder.stack.add(new Element("body", ""));
        xmlTreeBuilder.popStackToClose(endTag);
        
        assertFalse("Elements with the same name should be removed from the stack", xmlTreeBuilder.stack.contains(new Element("div", "")));
        assertTrue("Other elements should remain on the stack", xmlTreeBuilder.stack.contains(new Element("span", "")));
        assertTrue("Other elements should remain on the stack", xmlTreeBuilder.stack.contains(new Element("body", "")));
    }

    // test parseFragment method
    @Test
    public void testParseFragment() {
        List<Node> nodes = xmlTreeBuilder.parseFragment("<div></div>", "", errors);
        
        assertNotNull("List of nodes should not be null", nodes);
        assertEquals("Parsed fragment should contain one node", 1, nodes.size());
    }

}