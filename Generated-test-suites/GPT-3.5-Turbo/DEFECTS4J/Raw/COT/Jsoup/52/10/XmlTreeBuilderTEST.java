import org.jsoup.helper.Validate;
import org.jsoup.nodes.*;
import org.jsoup.parser.ParseErrorList;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

public class XmlTreeBuilderTest {
    private XmlTreeBuilder xmlTreeBuilder;

    @Before
    public void setUp() {
        xmlTreeBuilder = new XmlTreeBuilder();
    }

    @Test
    public void testInitialiseParse() {
        // Test if the document is placed onto the stack
        String input = "<html><body></body></html>";
        String baseUri = "http://example.com";
        ParseErrorList errors = new ParseErrorList();
        xmlTreeBuilder.initialiseParse(input, baseUri, errors);
        assertEquals(1, xmlTreeBuilder.getStack().size());
        assertEquals("html", xmlTreeBuilder.getCurrentElement().nodeName());
    }

    @Test
    public void testProcess_StartTag() {
        // Test if the start tag is correctly inserted
        String input = "<div></div>";
        xmlTreeBuilder.initialiseParse(input, "", new ParseErrorList());

        Token.StartTag startTag = new Token.StartTag();
        startTag.name("div");

        boolean result = xmlTreeBuilder.process(new Token(startTag));
        assertTrue(result);
        assertEquals(2, xmlTreeBuilder.getStack().size());
        assertEquals("div", xmlTreeBuilder.getCurrentElement().nodeName());
    }

    @Test
    public void testProcess_EndTag() {
        // Test if the corresponding start tag is correctly popped from the stack
        String input = "<div></div>";
        xmlTreeBuilder.initialiseParse(input, "", new ParseErrorList());

        Token.StartTag startTag = new Token.StartTag();
        startTag.name("div");
        xmlTreeBuilder.process(new Token(startTag));

        Token.EndTag endTag = new Token.EndTag();
        endTag.name("div");

        boolean result = xmlTreeBuilder.process(new Token(endTag));
        assertTrue(result);
        assertEquals(1, xmlTreeBuilder.getStack().size());
        assertEquals("html", xmlTreeBuilder.getCurrentElement().nodeName());
    }

    @Test
    public void testProcess_Comment() {
        // Test if the comment is correctly inserted
        String input = "<!-- Test comment -->";
        xmlTreeBuilder.initialiseParse(input, "", new ParseErrorList());

        Token.Comment comment = new Token.Comment();
        comment.data("Test comment");

        boolean result = xmlTreeBuilder.process(new Token(comment));
        assertTrue(result);
        assertEquals(2, xmlTreeBuilder.getCurrentElement().siblingIndex());
        assertEquals("<!-- Test comment -->", xmlTreeBuilder.getDocument().body().childNode(1).outerHtml());
    }

    @Test
    public void testProcess_Character() {
        // Test if the character is correctly inserted
        String input = "Test character";
        xmlTreeBuilder.initialiseParse(input, "", new ParseErrorList());

        Token.Character character = new Token.Character();
        character.data("Test character");

        boolean result = xmlTreeBuilder.process(new Token(character));
        assertTrue(result);
        assertEquals(2, xmlTreeBuilder.getCurrentElement().siblingIndex());
        assertEquals("Test character", xmlTreeBuilder.getDocument().body().childNode(1).outerHtml());
    }

    @Test
    public void testProcess_Doctype() {
        // Test if the doctype is correctly inserted
        String input = "<!DOCTYPE html>";
        xmlTreeBuilder.initialiseParse(input, "", new ParseErrorList());

        Token.Doctype doctype = new Token.Doctype();
        doctype.name("html");

        boolean result = xmlTreeBuilder.process(new Token(doctype));
        assertTrue(result);
        assertEquals(2, xmlTreeBuilder.getCurrentElement().siblingIndex());
        assertEquals("<!DOCTYPE html>", xmlTreeBuilder.getDocument().childNode(1).outerHtml());
    }

    @Test
    public void testInsertNode() {
        // Test if the node is correctly appended to the current element
        String input = "<div></div>";
        xmlTreeBuilder.initialiseParse(input, "", new ParseErrorList());

        Element element = new Element(Tag.valueOf("p"), "");
        xmlTreeBuilder.insertNode(element);

        assertEquals(2, xmlTreeBuilder.getCurrentElement().siblingIndex());
        assertEquals("p", xmlTreeBuilder.getCurrentElement().nodeName());
    }

    @Test
    public void testInsert_StartTag() {
        // Test if the start tag is correctly inserted and self-closed tags are acknowledged
        String input = "<img />";
        xmlTreeBuilder.initialiseParse(input, "", new ParseErrorList());

        Token.StartTag startTag = new Token.StartTag();
        startTag.name("img");
        startTag.selfClosing(true);

        Element element = xmlTreeBuilder.insert(startTag);

        assertEquals(2, xmlTreeBuilder.getStack().size());
        assertEquals("img", xmlTreeBuilder.getCurrentElement().nodeName());
        assertEquals(element, xmlTreeBuilder.getCurrentElement());
        assertTrue(element.tag().isSelfClosing());
    }

    @Test
    public void testInsert_Comment() {
        // Test if the comment is correctly inserted
        String input = "<!-- Test comment -->";
        xmlTreeBuilder.initialiseParse(input, "", new ParseErrorList());

        Token.Comment comment = new Token.Comment();
        comment.data("Test comment");

        xmlTreeBuilder.insert(comment);
        assertEquals(2, xmlTreeBuilder.getCurrentElement().siblingIndex());
        assertEquals("<!-- Test comment -->", xmlTreeBuilder.getDocument().body().childNode(1).outerHtml());
    }

    @Test
    public void testInsert_Character() {
        // Test if the character is correctly inserted
        String input = "Test character";
        xmlTreeBuilder.initialiseParse(input, "", new ParseErrorList());

        Token.Character character = new Token.Character();
        character.data("Test character");

        xmlTreeBuilder.insert(character);
        assertEquals(2, xmlTreeBuilder.getCurrentElement().siblingIndex());
        assertEquals("Test character", xmlTreeBuilder.getDocument().body().childNode(1).outerHtml());
    }

    @Test
    public void testInsert_Doctype() {
        // Test if the doctype is correctly inserted
        String input = "<!DOCTYPE html>";
        xmlTreeBuilder.initialiseParse(input, "", new ParseErrorList());

        Token.Doctype doctype = new Token.Doctype();
        doctype.name("html");

        xmlTreeBuilder.insert(doctype);
        assertEquals(2, xmlTreeBuilder.getCurrentElement().siblingIndex());
        assertEquals("<!DOCTYPE html>", xmlTreeBuilder.getDocument().childNode(1).outerHtml());
    }

    @Test
    public void testPopStackToClose() {
        // Test if the elements with the same name are correctly removed from the stack
        String input = "<div><p></p><p></p></div>";
        xmlTreeBuilder.initialiseParse(input, "", new ParseErrorList());

        Token.EndTag endTag = new Token.EndTag();
        endTag.name("p");

        xmlTreeBuilder.popStackToClose(endTag);
        assertEquals(2, xmlTreeBuilder.getStack().size());
        assertEquals("div", xmlTreeBuilder.getCurrentElement().nodeName());
        assertEquals(1, xmlTreeBuilder.getCurrentElement().children().size());
        assertEquals("p", xmlTreeBuilder.getCurrentElement().child(0).nodeName());
    }

    @Test
    public void testParseFragment() {
        // Test if the input fragment is correctly parsed and returned as a list of child nodes
        String inputFragment = "<p>Test fragment</p>";
        String baseUri = "http://example.com";
        ParseErrorList errors = new ParseErrorList();
        List<Node> nodes = xmlTreeBuilder.parseFragment(inputFragment, baseUri, errors);

        assertEquals(1, nodes.size());
        assertTrue(nodes.get(0) instanceof Element);
        assertEquals("p", ((Element) nodes.get(0)).nodeName());
        assertEquals("Test fragment", ((Element) nodes.get(0)).text());
    }
}