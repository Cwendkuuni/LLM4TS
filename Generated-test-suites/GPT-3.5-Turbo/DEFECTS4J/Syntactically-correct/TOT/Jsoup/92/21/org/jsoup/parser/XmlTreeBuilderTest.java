import org.junit.Before;
import org.junit.Test;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.select.Elements;

import java.io.Reader;
import java.io.StringReader;
import java.util.List;

import static org.junit.Assert.*;

public class XmlTreeBuilderTest {

    private XmlTreeBuilder xmlTreeBuilder;

    @Before
    public void setUp() {
        xmlTreeBuilder = new XmlTreeBuilder();
    }

    @Test
    public void testParse_ValidXmlInput_ReturnsDocumentObject() {
        // Arrange
        String input = "<root><child></child></root>";
        String baseUri = "http://example.com";

        // Act
        Document document = xmlTreeBuilder.parse(input, baseUri);

        // Assert
        assertNotNull(document);
        assertEquals("http://example.com", document.baseUri());
    }

    @Test
    public void testParse_InvalidXmlInput_ReturnsNull() {
        // Arrange
        String input = "<root><child></root>";
        String baseUri = "http://example.com";

        // Act
        Document document = xmlTreeBuilder.parse(input, baseUri);

        // Assert
        assertNull(document);
    }

    @Test
    public void testInsertNode_AddTextNodeToCurrentElement_SuccessfullyAppendsNodeToCurrentElement() {
        // Arrange
        Element element = new Element("tag");
        xmlTreeBuilder.currentElement().appendChild(element);
        Node expectedNode = new TextNode("text");

        // Act
        xmlTreeBuilder.insertNode(expectedNode);
        Elements childElements = xmlTreeBuilder.currentElement().children();

        // Assert
        assertEquals(1, childElements.size());
        assertEquals(expectedNode, childElements.get(0));
    }

    @Test
    public void testInsert_StartTagWithSelfClosingTrue_ReturnsElementWithSelfClosingTag() {
        // Arrange
        Token.StartTag startTag = new Token.StartTag("tag", true);

        // Act
        Element element = xmlTreeBuilder.insert(startTag);

        // Assert
        assertNotNull(element);
        assertTrue(element.tag().isSelfClosing());
    }

    @Test
    public void testInsert_CommentTokenWithBogusTrue_ReturnsXmlDeclarationElement() {
        // Arrange
        Token.Comment commentToken = new Token.Comment("<!--?xml version=\"1.0\" encoding=\"UTF-8\"?-->");

        // Act
        xmlTreeBuilder.insert(commentToken);
        Node insertedNode = xmlTreeBuilder.currentElement().childNode(0);

        // Assert
        assertNotNull(insertedNode);
        assertTrue(insertedNode instanceof XmlDeclaration);
    }

    @Test
    public void testInsert_CharacterTokenWithCDataTrue_ReturnsCDataNode() {
        // Arrange
        Token.Character cDataToken = new Token.Character("[CDATA]");

        // Act
        xmlTreeBuilder.insert(cDataToken);
        Node insertedNode = xmlTreeBuilder.currentElement().childNode(0);

        // Assert
        assertNotNull(insertedNode);
        assertTrue(insertedNode instanceof CDataNode);
    }

    @Test
    public void testInsert_EndTag_FindsAndRemovesMatchingElementFromStack() {
        // Arrange
        Token.EndTag endTag = new Token.EndTag("child");
        Element parentElement = new Element("root");
        Element childElement = new Element("child");
        parentElement.appendChild(childElement);
        xmlTreeBuilder.stack.add(parentElement);
        xmlTreeBuilder.stack.add(childElement);

        // Act
        xmlTreeBuilder.popStackToClose(endTag);

        // Assert
        assertEquals(1, xmlTreeBuilder.stack.size());
        assertEquals(parentElement, xmlTreeBuilder.stack.get(0));
    }

    @Test
    public void testParseFragment_ValidInputFragment_ReturnsListOfNodes() {
        // Arrange
        String inputFragment = "<child1></child1><child2></child2>";
        String baseUri = "http://example.com";

        // Act
        List<Node> nodes = xmlTreeBuilder.parseFragment(inputFragment, baseUri, new Parser(xmlTreeBuilder));

        // Assert
        assertNotNull(nodes);
        assertEquals(2, nodes.size());
        assertTrue(nodes.get(0) instanceof Element);
        assertTrue(nodes.get(1) instanceof Element);
    }

    @Test
    public void testParseFragment_InvalidInputFragment_ReturnsEmptyListOfNodes() {
        // Arrange
        String inputFragment = "<child1></child1><child2></child2";
        String baseUri = "http://example.com";

        // Act
        List<Node> nodes = xmlTreeBuilder.parseFragment(inputFragment, baseUri, new Parser(xmlTreeBuilder));

        // Assert
        assertNotNull(nodes);
        assertTrue(nodes.isEmpty());
    }
}