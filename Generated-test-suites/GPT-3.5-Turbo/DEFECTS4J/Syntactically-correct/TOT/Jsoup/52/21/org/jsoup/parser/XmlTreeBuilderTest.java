import org.junit.Before;
import org.junit.Test;
import org.jsoup.nodes.*;
import org.jsoup.parser.ParseErrorList;
import org.jsoup.parser.Token;

import java.util.List;

import static org.junit.Assert.*;

public class XmlTreeBuilderTest {
    private XmlTreeBuilder xmlTreeBuilder;
    private Document doc;

    @Before
    public void setUp() {
        xmlTreeBuilder = new XmlTreeBuilder();
        doc = new Document("");
    }

    @Test
    public void testInitialiseParse() {
        String input = "<root>test</root>";
        String baseUri = "";
        ParseErrorList errors = new ParseErrorList();
        xmlTreeBuilder.initialiseParse(input, baseUri, errors);

        // Assert stack contains doc and syntax is set to xml
        assertEquals(doc, xmlTreeBuilder.getStack().get(0));
        assertEquals(Document.OutputSettings.Syntax.xml, doc.outputSettings().syntax());
    }

    @Test
    public void testProcess_StartTag() {
        Element expectedElement = new Element(Tag.valueOf("div"), "");

        // Create StartTag token
        Token.StartTag startTag = new Token.StartTag();
        startTag.name("div");
        startTag.attributes(null);

        // Insert the StartTag token
        xmlTreeBuilder.process(startTag);

        // Assert current element is the expected element
        assertEquals(expectedElement, xmlTreeBuilder.currentElement());
    }

    @Test
    public void testProcess_EndTag() {
        Element elementToPop = new Element(Tag.valueOf("div"), "");
        xmlTreeBuilder.getStack().add(elementToPop);

        // Create EndTag token
        Token.EndTag endTag = new Token.EndTag();
        endTag.name("div");

        // Pop stack to close the element
        xmlTreeBuilder.process(endTag);

        // Assert element is removed from stack
        assertFalse(xmlTreeBuilder.getStack().contains(elementToPop));
    }

    @Test
    public void testProcess_Comment() {
        Comment expectedComment = new Comment("test", "");

        // Create Comment token
        Token.Comment commentToken = new Token.Comment();
        commentToken.data("test");
        commentToken.bogus(false);

        // Insert the Comment token
        xmlTreeBuilder.process(commentToken);

        // Assert current node is the expected comment
        assertTrue(xmlTreeBuilder.currentElement().childNodes().contains(expectedComment));
    }

    @Test
    public void testProcess_Character() {
        TextNode expectedTextNode = new TextNode("test", "");

        // Create Character token
        Token.Character characterToken = new Token.Character();
        characterToken.data("test");

        // Insert the Character token
        xmlTreeBuilder.process(characterToken);

        // Assert current node is the expected text node
        assertTrue(xmlTreeBuilder.currentElement().childNodes().contains(expectedTextNode));
    }

    @Test
    public void testProcess_Doctype() {
        DocumentType expectedDoctype = new DocumentType("html", "", "", "");

        // Create Doctype token
        Token.Doctype doctypeToken = new Token.Doctype();
        doctypeToken.name("html");

        // Insert the Doctype token
        xmlTreeBuilder.process(doctypeToken);

        // Assert current node is the expected doctype
        assertTrue(xmlTreeBuilder.currentElement().childNodes().contains(expectedDoctype));
    }

    @Test
    public void testInsertNode() {
        Element parentElement = new Element(Tag.valueOf("div"), "");
        Element childElement = new Element(Tag.valueOf("span"), "");
        xmlTreeBuilder.getStack().add(parentElement);
        
        // Insert the child element
        xmlTreeBuilder.insertNode(childElement);

        // Assert parent element contains the child element
        assertTrue(parentElement.childNodes().contains(childElement));
    }

    @Test
    public void testInsert_StartTag() {
        Element expectedElement = new Element(Tag.valueOf("div"), "");

        // Create StartTag token
        Token.StartTag startTag = new Token.StartTag();
        startTag.name("div");
        startTag.attributes(null);

        // Insert the StartTag token
        Element result = xmlTreeBuilder.insert(startTag);

        // Assert inserted element is the expected element
        assertEquals(expectedElement, result);
        
        // Assert tokeniser's self-closing flag is acknowledged
        assertTrue(xmlTreeBuilder.tokeniser.isSelfClosingFlagAcknowledged());
    }

    @Test
    public void testInsert_SS_StartTag() {
        // Create StartTag token
        Token.StartTag startTag = new Token.StartTag();
        startTag.name("div");
        startTag.attributes(null);
        startTag.selfClosing(false);

        // Insert the StartTag token with selfClosing flag as false
        Element result = xmlTreeBuilder.insert(startTag);

        // Assert inserted element is not self-closing
        assertFalse(result.tag().isSelfClosing());
    }

    @Test
    public void testInsert_Comment() {
        Comment expectedComment = new Comment("test", "");

        // Create Comment token
        Token.Comment commentToken = new Token.Comment();
        commentToken.data("test");
        commentToken.bogus(true);

        // Insert the Comment token
        xmlTreeBuilder.insert(commentToken);

        // Assert current node is the expected comment
        assertTrue(xmlTreeBuilder.currentElement().childNodes().contains(expectedComment));
    }

    @Test
    public void testInsert_Comment_Bogus() {
        XmlDeclaration expectedDeclaration = new XmlDeclaration("declaration", "", true);

        // Create Comment token
        Token.Comment commentToken = new Token.Comment();
        commentToken.data("!declaration");

        // Insert the Comment token as a bogus comment
        xmlTreeBuilder.insert(commentToken);

        // Assert current node is the expected XmlDeclaration
        assertTrue(xmlTreeBuilder.currentElement().childNodes().contains(expectedDeclaration));
    }

    @Test
    public void testInsert_Character() {
        TextNode expectedTextNode = new TextNode("test", "");

        // Create Character token
        Token.Character characterToken = new Token.Character();
        characterToken.data("test");

        // Insert the Character token
        xmlTreeBuilder.insert(characterToken);

        // Assert current node is the expected text node
        assertTrue(xmlTreeBuilder.currentElement().childNodes().contains(expectedTextNode));
    }

    @Test
    public void testInsert_Doctype() {
        DocumentType expectedDoctype = new DocumentType("html", "", "", "");

        // Create Doctype token
        Token.Doctype doctypeToken = new Token.Doctype();
        doctypeToken.name("html");

        // Insert the Doctype token
        xmlTreeBuilder.insert(doctypeToken);

        // Assert current node is the expected doctype
        assertTrue(xmlTreeBuilder.currentElement().childNodes().contains(expectedDoctype));
    }

    @Test
    public void testPopStackToClose_ElementFound() {
        Element elementToPop = new Element(Tag.valueOf("div"), "");
        xmlTreeBuilder.getStack().add(elementToPop);

        // Create EndTag token
        Token.EndTag endTag = new Token.EndTag();
        endTag.name("div");

        // Pop stack to close the element
        xmlTreeBuilder.popStackToClose(endTag);

        // Assert stack does not contain the element
        assertFalse(xmlTreeBuilder.getStack().contains(elementToPop));
    }

    @Test
    public void testPopStackToClose_ElementNotFound() {
        Element elementToPop = new Element(Tag.valueOf("div"), "");
        xmlTreeBuilder.getStack().add(elementToPop);

        // Create EndTag token for a different tag name
        Token.EndTag endTag = new Token.EndTag();
        endTag.name("span");

        // Pop stack to close the element - should not remove any elements
        xmlTreeBuilder.popStackToClose(endTag);

        // Assert stack still contains the element
        assertTrue(xmlTreeBuilder.getStack().contains(elementToPop));
    }

    @Test
    public void testParseFragment() {
        String inputFragment = "<div>This is a test</div>";
        String baseUri = "";
        ParseErrorList errors = new ParseErrorList();

        List<Node> nodeList = xmlTreeBuilder.parseFragment(inputFragment, baseUri, errors);

        // Assert parsed fragment has correct number of child nodes
        assertEquals(1, nodeList.size());
        // Assert parsed fragment is a div element with correct text content
        assertTrue(nodeList.get(0) instanceof Element);
        assertTrue(((Element) nodeList.get(0)).tagName().equals("div"));
        assertTrue(((Element) nodeList.get(0)).text().equals("This is a test"));
    }
}