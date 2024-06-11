import org.jsoup.helper.W3CDom;
import org.jsoup.nodes.Comment;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.DocumentType;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.parser.ParseErrorList;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class XmlTreeBuilderTest {
    private XmlTreeBuilder xmlTreeBuilder;
    private ParseErrorList errorList;

    @Before
    public void setUp() {
        xmlTreeBuilder = new XmlTreeBuilder();
        errorList = new ParseErrorList();
    }

    @Test
    public void testInitialiseParse() {
        String input = "<root></root>";
        String baseUri = "https://example.com";
        xmlTreeBuilder.initialiseParse(input, baseUri, errorList);

        assertEquals(baseUri, xmlTreeBuilder.baseUri());
        assertNotNull(xmlTreeBuilder.doc);
        assertEquals("root", xmlTreeBuilder.currentElement().nodeName());
    }

    @Test
    public void testProcess_StartTag() {
        Token.StartTag startTag = new Token.StartTag("div", "<div>", null);
        Element element = xmlTreeBuilder.process(startTag);

        assertNotNull(element);
        assertEquals("div", element.nodeName());
        assertEquals(xmlTreeBuilder.currentElement().childNode(0), element);
    }

    @Test
    public void testProcess_EndTag() {
        Token.EndTag endTag = new Token.EndTag("div");
        xmlTreeBuilder.process(new Token.StartTag("div", "<div>", null));
        xmlTreeBuilder.process(endTag);

        assertNull(xmlTreeBuilder.currentElement());
    }

    @Test
    public void testProcess_Comment() {
        Token.Comment comment = new Token.Comment("This is a comment");
        xmlTreeBuilder.process(comment);

        Node childNode = xmlTreeBuilder.currentElement().childNode(0);
        assertTrue(childNode instanceof Comment);
        assertEquals("This is a comment", ((Comment) childNode).getData());
    }

    @Test
    public void testProcess_Character() {
        Token.Character character = new Token.Character("Text Content");
        xmlTreeBuilder.process(character);

        Node childNode = xmlTreeBuilder.currentElement().childNode(0);
        assertEquals("Text Content", childNode.outerHtml());
    }

    @Test
    public void testProcess_Doctype() {
        Token.Doctype doctype = new Token.Doctype("html", "", "", "");
        xmlTreeBuilder.process(doctype);

        Node childNode = xmlTreeBuilder.currentElement().childNode(0);
        assertTrue(childNode instanceof DocumentType);
        DocumentType documentType = (DocumentType) childNode;
        assertEquals("html", documentType.getName());
        assertEquals("", documentType.getPublicIdentifier());
        assertEquals("", documentType.getSystemIdentifier());
    }

    @Test
    public void testInsert_StartTag() {
        Token.StartTag startTag = new Token.StartTag("div", "<div>", null);
        Element element = xmlTreeBuilder.insert(startTag);

        assertNotNull(element);
        assertEquals("div", element.nodeName());
        assertEquals(xmlTreeBuilder.currentElement().childNode(0), element);
    }

    @Test
    public void testInsert_Comment() {
        Token.Comment comment = new Token.Comment("This is a comment");
        xmlTreeBuilder.insert(comment);

        Node childNode = xmlTreeBuilder.currentElement().childNode(0);
        assertTrue(childNode instanceof Comment);
        assertEquals("This is a comment", ((Comment) childNode).getData());
    }

    @Test
    public void testInsert_Character() {
        Token.Character character = new Token.Character("Text Content");
        xmlTreeBuilder.insert(character);

        Node childNode = xmlTreeBuilder.currentElement().childNode(0);
        assertEquals("Text Content", childNode.outerHtml());
    }

    @Test
    public void testInsert_Doctype() {
        Token.Doctype doctype = new Token.Doctype("html", "", "", "");
        xmlTreeBuilder.insert(doctype);

        Node childNode = xmlTreeBuilder.currentElement().childNode(0);
        assertTrue(childNode instanceof DocumentType);
        DocumentType documentType = (DocumentType) childNode;
        assertEquals("html", documentType.getName());
        assertEquals("", documentType.getPublicIdentifier());
        assertEquals("", documentType.getSystemIdentifier());
    }

    @Test
    public void testPopStackToClose_ExistingTag() {
        Element element1 = new Element("div");
        Element element2 = new Element("p");
        Element element3 = new Element("div");

        xmlTreeBuilder.stack.push(element1);
        xmlTreeBuilder.stack.push(element2);
        xmlTreeBuilder.stack.push(element3);

        assertEquals(3, xmlTreeBuilder.stack.size());

        Token.EndTag endTag = new Token.EndTag("div");
        xmlTreeBuilder.popStackToClose(endTag);

        assertEquals(1, xmlTreeBuilder.stack.size());
        assertEquals(element2, xmlTreeBuilder.stack.getLast());
    }

    @Test
    public void testPopStackToClose_NotExistingTag() {
        Element element1 = new Element("div");
        Element element2 = new Element("p");
        Element element3 = new Element("span");

        xmlTreeBuilder.stack.push(element1);
        xmlTreeBuilder.stack.push(element2);
        xmlTreeBuilder.stack.push(element3);

        assertEquals(3, xmlTreeBuilder.stack.size());

        Token.EndTag endTag = new Token.EndTag("a");
        xmlTreeBuilder.popStackToClose(endTag);

        assertEquals(3, xmlTreeBuilder.stack.size());
    }

    @Test
    public void testInsertNode() {
        Element element = new Element("div");
        xmlTreeBuilder.insertNode(element);

        assertEquals(xmlTreeBuilder.currentElement().childNode(0), element);
    }

    @Test
    public void testXmlTreeBuilderInheritsTreeBuilder() {
        assertTrue(xmlTreeBuilder instanceof TreeBuilder);
    }
}