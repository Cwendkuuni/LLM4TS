import org.junit.Before;
import org.junit.Test;
import org.jsoup.helper.ParseErrorList;
import org.jsoup.nodes.Comment;
import org.jsoup.nodes.DocumentType;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.jsoup.parser.Token;
import org.jsoup.parser.XmlTreeBuilder;

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

        // Test if stack contains the document
        assertTrue(xmlTreeBuilder.stack.contains(xmlTreeBuilder.doc));
    }

    @Test
    public void testProcessStartTag() {
        Token.StartTag startTag = new Token.StartTag("tag", "name");
        Element element = xmlTreeBuilder.insert(startTag);

        // Test if the element is inserted onto the stack
        assertTrue(xmlTreeBuilder.stack.contains(element));
    }

    @Test
    public void testProcessEndTag() {
        Token.EndTag endTag = new Token.EndTag("tag", "name");
        xmlTreeBuilder.process(endTag);

        // Test if the element is removed from the stack
        assertFalse(xmlTreeBuilder.stack.contains(endTag));
    }

    @Test
    public void testProcessComment() {
        Token.Comment commentToken = new Token.Comment("comment data");
        xmlTreeBuilder.insert(commentToken);

        // Test if a comment node is inserted
        Node lastChild = xmlTreeBuilder.currentElement().lastChild();
        assertNotNull(lastChild);
        assertTrue(lastChild instanceof Comment);
        assertEquals("comment data", ((Comment) lastChild).getData());
    }

    @Test
    public void testProcessCharacter() {
        Token.Character characterToken = new Token.Character("character data");
        xmlTreeBuilder.insert(characterToken);

        // Test if a text node is inserted
        Node lastChild = xmlTreeBuilder.currentElement().lastChild();
        assertNotNull(lastChild);
        assertTrue(lastChild instanceof TextNode);
        assertEquals("character data", ((TextNode) lastChild).getWholeText());
    }

    @Test
    public void testProcessDoctype() {
        Token.Doctype doctype = new Token.Doctype("doctype", "public", "system");
        xmlTreeBuilder.insert(doctype);

        // Test if a document type node is inserted
        Node lastChild = xmlTreeBuilder.currentElement().lastChild();
        assertNotNull(lastChild);
        assertTrue(lastChild instanceof DocumentType);
        assertEquals("doctype", ((DocumentType) lastChild).getName());
        assertEquals("public", ((DocumentType) lastChild).getPublicIdentifier());
        assertEquals("system", ((DocumentType) lastChild).getSystemIdentifier());
    }

    @Test
    public void testPopStackToClose() {
        Token.EndTag endTag = new Token.EndTag("tag", "name");

        // Insert some elements onto the stack
        Element element1 = new Element("tag1");
        Element element2 = new Element("tag2");
        Element element3 = new Element("tag3");
        xmlTreeBuilder.stack.add(element1);
        xmlTreeBuilder.stack.add(element2);
        xmlTreeBuilder.stack.add(element3);

        xmlTreeBuilder.popStackToClose(endTag);

        // Test if the correct elements are removed from the stack
        assertFalse(xmlTreeBuilder.stack.contains(element1));
        assertFalse(xmlTreeBuilder.stack.contains(element2));
        assertTrue(xmlTreeBuilder.stack.contains(element3));
    }
}