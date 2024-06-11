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
    public void initialiseParse_shouldSetDocumentOutputSettingToXml() {
        // Arrange
        String input = "<html><body>Hello World</body></html>";
        String baseUri = "http://example.com";
        ParseErrorList errors = new ParseErrorList();

        // Act
        xmlTreeBuilder.initialiseParse(input, baseUri, errors);

        // Assert
        assertEquals(Document.OutputSettings.Syntax.xml, xmlTreeBuilder.doc.outputSettings().syntax());
    }

    @Test
    public void process_shouldInsertStartTagNode() {
        // Arrange
        Token token = new Token.StartTag("div");

        // Act
        boolean result = xmlTreeBuilder.process(token);

        // Assert
        assertTrue(result);
        assertEquals(1, xmlTreeBuilder.currentElement().childNodes().size());
        assertTrue(xmlTreeBuilder.currentElement().childNodes().get(0) instanceof Element);
        assertEquals("div", xmlTreeBuilder.currentElement().childNodes().get(0).nodeName());
    }

    @Test
    public void process_shouldPopStackToCloseEndTagNode() {
        // Arrange
        Token.StartTag startTag = new Token.StartTag("div");
        xmlTreeBuilder.insert(startTag);

        Token.EndTag endTag = new Token.EndTag("div");

        // Act
        boolean result = xmlTreeBuilder.process(endTag);

        // Assert
        assertTrue(result);
        assertEquals(0, xmlTreeBuilder.stack.size());
    }

    @Test
    public void process_shouldInsertCommentNode() {
        // Arrange
        Token.Comment comment = new Token.Comment("This is a comment");

        // Act
        boolean result = xmlTreeBuilder.process(comment);

        // Assert
        assertTrue(result);
        assertEquals(1, xmlTreeBuilder.currentElement().childNodes().size());
        assertTrue(xmlTreeBuilder.currentElement().childNodes().get(0) instanceof Comment);
        assertEquals("This is a comment", ((Comment) xmlTreeBuilder.currentElement().childNodes().get(0)).getData());
    }

    @Test
    public void process_shouldInsertCharacterNode() {
        // Arrange
        Token.Character character = new Token.Character("Hello World");

        // Act
        boolean result = xmlTreeBuilder.process(character);

        // Assert
        assertTrue(result);
        assertEquals(1, xmlTreeBuilder.currentElement().childNodes().size());
        assertTrue(xmlTreeBuilder.currentElement().childNodes().get(0) instanceof TextNode);
        assertEquals("Hello World", ((TextNode) xmlTreeBuilder.currentElement().childNodes().get(0)).getWholeText());
    }

    @Test
    public void process_shouldInsertDoctypeNode() {
        // Arrange
        Token.Doctype doctype = new Token.Doctype("html", "", "", "");

        // Act
        boolean result = xmlTreeBuilder.process(doctype);

        // Assert
        assertTrue(result);
        assertEquals(1, xmlTreeBuilder.currentElement().childNodes().size());
        assertTrue(xmlTreeBuilder.currentElement().childNodes().get(0) instanceof DocumentType);
        assertEquals("html", ((DocumentType) xmlTreeBuilder.currentElement().childNodes().get(0)).getName());
        assertEquals("", ((DocumentType) xmlTreeBuilder.currentElement().childNodes().get(0)).getPublicIdentifier());
        assertEquals("", ((DocumentType) xmlTreeBuilder.currentElement().childNodes().get(0)).getSystemIdentifier());
    }

    @Test
    public void insert_shouldInsertElementNode() {
        // Arrange
        Token.StartTag startTag = new Token.StartTag("div");
        startTag.attributes.put("id", "myDiv");

        // Act
        Element element = xmlTreeBuilder.insert(startTag);

        // Assert
        assertNotNull(element);
        assertEquals("div", element.nodeName());
        assertEquals("http://example.com", element.baseUri());
        assertEquals("myDiv", element.attr("id"));
    }

    @Test
    public void insert_shouldInsertElementNodeWithSelfClosingFlag() {
        // Arrange
        Token.StartTag startTag = new Token.StartTag("br");
        startTag.selfClosing = true;

        // Act
        Element element = xmlTreeBuilder.insert(startTag);

        // Assert
        assertNotNull(element);
        assertEquals("br", element.nodeName());
        assertEquals(true, element.isSelfClosing());
    }

    @Test
    public void insert_shouldInsertCommentNodeWithXmlDeclaration() {
        // Arrange
        Token.Comment commentToken = new Token.Comment("<!DOCTYPE html>");
        commentToken.bogus = true;

        // Act
        xmlTreeBuilder.insert(commentToken);

        // Assert
        assertEquals(1, xmlTreeBuilder.currentElement().childNodes().size());
        assertTrue(xmlTreeBuilder.currentElement().childNodes().get(0) instanceof XmlDeclaration);
        assertEquals("<!DOCTYPE html>", ((XmlDeclaration) xmlTreeBuilder.currentElement().childNodes().get(0)).getWholeDeclaration());
    }

    @Test
    public void insert_shouldInsertCharacterNode() {
        // Arrange
        Token.Character character = new Token.Character("Hello World");

        // Act
        xmlTreeBuilder.insert(character);

        // Assert
        assertEquals(1, xmlTreeBuilder.currentElement().childNodes().size());
        assertTrue(xmlTreeBuilder.currentElement().childNodes().get(0) instanceof TextNode);
        assertEquals("Hello World", ((TextNode) xmlTreeBuilder.currentElement().childNodes().get(0)).getWholeText());
    }

    @Test
    public void insert_shouldInsertDoctypeNode() {
        // Arrange
        Token.Doctype doctype = new Token.Doctype("html", "", "", "");

        // Act
        xmlTreeBuilder.insert(doctype);

        // Assert
        assertEquals(1, xmlTreeBuilder.currentElement().childNodes().size());
        assertTrue(xmlTreeBuilder.currentElement().childNodes().get(0) instanceof DocumentType);
        assertEquals("html", ((DocumentType) xmlTreeBuilder.currentElement().childNodes().get(0)).getName());
        assertEquals("", ((DocumentType) xmlTreeBuilder.currentElement().childNodes().get(0)).getPublicIdentifier());
        assertEquals("", ((DocumentType) xmlTreeBuilder.currentElement().childNodes().get(0)).getSystemIdentifier());
    }

    @Test
    public void popStackToClose_shouldRemoveFirstOccurrenceOfElement() {
        // Arrange
        Token.StartTag startTag1 = new Token.StartTag("div");
        xmlTreeBuilder.insert(startTag1);

        Token.StartTag startTag2 = new Token.StartTag("span");
        xmlTreeBuilder.insert(startTag2);

        Token.EndTag endTag = new Token.EndTag("div");

        // Act
        xmlTreeBuilder.popStackToClose(endTag);

        // Assert
        assertEquals(1, xmlTreeBuilder.stack.size());
        assertEquals("span", xmlTreeBuilder.stack.get(0).nodeName());
    }

    @Test
    public void parseFragment_shouldReturnListOfNodes() {
        // Arrange
        String inputFragment = "<div id=\"myDiv\">Hello World</div>";
        String baseUri = "http://example.com";
        ParseErrorList errors = new ParseErrorList();

        // Act
        List<Node> nodes = xmlTreeBuilder.parseFragment(inputFragment, baseUri, errors);

        // Assert
        assertEquals(1, nodes.size());
        assertTrue(nodes.get(0) instanceof Element);
        assertEquals("div", nodes.get(0).nodeName());
        assertEquals("myDiv", ((Element) nodes.get(0)).attr("id"));
        assertEquals("Hello World", ((Element) nodes.get(0)).text());
    }

}