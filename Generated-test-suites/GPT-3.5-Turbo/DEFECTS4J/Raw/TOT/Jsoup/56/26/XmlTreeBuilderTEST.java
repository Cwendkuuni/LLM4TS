import org.junit.Before;
import org.junit.Test;
import org.jsoup.parser.XmlTreeBuilder;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.select.Elements;

import java.util.List;

import static org.junit.Assert.*;

public class XmlTreeBuilderTest {

    private XmlTreeBuilder xmlTreeBuilder;

    @Before
    public void setUp() {
        xmlTreeBuilder = new XmlTreeBuilder();
    }

    @Test
    public void testParse_method_withValidInput_shouldReturnDocumentObject() {
        // Arrange
        String input = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<note>\n" +
                "  <to>Tove</to>\n" +
                "  <from>Jani</from>\n" +
                "  <heading>Reminder</heading>\n" +
                "  <body>Don't forget me this weekend!</body>\n" +
                "</note>";

        // Act
        Document document = xmlTreeBuilder.parse(input, "");

        // Assert
        assertNotNull(document);
        assertEquals("note", document.child(0).nodeName());
        assertEquals(5, document.child(0).children().size());
    }

    @Test
    public void testParse_method_withInvalidInput_shouldReturnNull() {
        // Arrange
        String input = "<invalidxml";

        // Act
        Document document = xmlTreeBuilder.parse(input, "");

        // Assert
        assertNull(document);
    }

    @Test
    public void testInitialiseParse_method_withValidInput_shouldSetXmlSyntaxOutputSettings() {
        // Arrange
        String input = "";

        // Act
        xmlTreeBuilder.initialiseParse(input, "", null, null);

        // Assert
        assertEquals(Document.OutputSettings.Syntax.xml, xmlTreeBuilder.currentDocument().outputSettings().syntax());
    }

    @Test
    public void testProcess_method_withStartTagToken_shouldInsertElementInDocument() {
        // Arrange
        XmlTreeBuilder.Token token = new XmlTreeBuilder.Token();
        token.type = XmlTreeBuilder.Token.TokenType.StartTag;
        token.value = "<tag>";

        // Act
        boolean result = xmlTreeBuilder.process(token);

        // Assert
        assertTrue(result);
        assertEquals("tag", xmlTreeBuilder.currentElement().nodeName());
    }

    @Test
    public void testProcess_method_withEndTagToken_shouldPopElementFromStack() {
        // Arrange
        Element parentElement = new Element("parent");
        Element childElement = new Element("child");
        parentElement.appendChild(childElement);
        xmlTreeBuilder.stack.add(parentElement);

        XmlTreeBuilder.Token token = new XmlTreeBuilder.Token();
        token.type = XmlTreeBuilder.Token.TokenType.EndTag;
        token.value = "</child>";

        // Act
        boolean result = xmlTreeBuilder.process(token);

        // Assert
        assertTrue(result);
        assertEquals(0, xmlTreeBuilder.stack.size());
    }

    @Test
    public void testProcess_method_withCommentToken_shouldInsertCommentNodeInDocument() {
        // Arrange
        XmlTreeBuilder.Token token = new XmlTreeBuilder.Token();
        token.type = XmlTreeBuilder.Token.TokenType.Comment;
        token.value = "<!-- Comment -->";

        // Act
        boolean result = xmlTreeBuilder.process(token);

        // Assert
        assertTrue(result);
        List<Node> childNodes = xmlTreeBuilder.currentElement().childNodes();
        assertEquals(1, childNodes.size());
        assertEquals("<!-- Comment -->", childNodes.get(0).outerHtml());
    }

    @Test
    public void testProcess_method_withCharacterToken_shouldInsertTextNodeInDocument() {
        // Arrange
        XmlTreeBuilder.Token token = new XmlTreeBuilder.Token();
        token.type = XmlTreeBuilder.Token.TokenType.Character;
        token.value = "Text";

        // Act
        boolean result = xmlTreeBuilder.process(token);

        // Assert
        assertTrue(result);
        List<Node> childNodes = xmlTreeBuilder.currentElement().childNodes();
        assertEquals(1, childNodes.size());
        assertEquals("Text", childNodes.get(0).outerHtml());
    }

    @Test
    public void testProcess_method_withDoctypeToken_shouldInsertDoctypeNodeInDocument() {
        // Arrange
        XmlTreeBuilder.Token token = new XmlTreeBuilder.Token();
        token.type = XmlTreeBuilder.Token.TokenType.Doctype;
        token.value = "<!DOCTYPE html>";

        // Act
        boolean result = xmlTreeBuilder.process(token);

        // Assert
        assertTrue(result);
        List<Node> childNodes = xmlTreeBuilder.currentElement().childNodes();
        assertEquals(1, childNodes.size());
        assertEquals("<!DOCTYPE html>", childNodes.get(0).outerHtml());
    }

    @Test
    public void testInsertNode_method_withValidInput_shouldAppendNodeToCurrentElement() {
        // Arrange
        Element parentElement = new Element("parent");
        Element childElement = new Element("child");

        // Act
        xmlTreeBuilder.stack.add(parentElement);
        xmlTreeBuilder.insertNode(childElement);

        // Assert
        assertEquals(1, parentElement.children().size());
        assertEquals(childElement, parentElement.child(0));
    }

    @Test
    public void testInsert_method_withStartTagToken_shouldInsertElementInDocument() {
        // Arrange
        XmlTreeBuilder.Token.StartTag startTagToken = new XmlTreeBuilder.Token.StartTag();
        startTagToken.name = "tag";
        startTagToken.attributes = "attr=\"value\"";

        // Act
        Element element = xmlTreeBuilder.insert(startTagToken);

        // Assert
        assertEquals("tag", element.nodeName());
        assertEquals("value", element.attr("attr"));
    }

    @Test
    public void testInsert_method_withSelfClosingStartTagToken_shouldInsertSelfClosingElementInDocument() {
        // Arrange
        XmlTreeBuilder.Token.StartTag selfClosingStartTagToken = new XmlTreeBuilder.Token.StartTag();
        selfClosingStartTagToken.name = "tag";
        selfClosingStartTagToken.attributes = "attr=\"value\"";
        selfClosingStartTagToken.selfClosing = true;

        // Act
        Element element = xmlTreeBuilder.insert(selfClosingStartTagToken);

        // Assert
        assertEquals("tag", element.nodeName());
        assertEquals("value", element.attr("attr"));
        assertTrue(element.isSelfClosing());
    }

    @Test
    public void testInsert_method_withCommentToken_shouldInsertCommentNodeInDocument() {
        // Arrange
        XmlTreeBuilder.Token.Comment commentToken = new XmlTreeBuilder.Token.Comment();
        commentToken.data = "Comment";

        // Act
        xmlTreeBuilder.insert(commentToken);

        // Assert
        List<Node> childNodes = xmlTreeBuilder.currentElement().childNodes();
        assertEquals(1, childNodes.size());
        assertEquals("<!-- Comment -->", childNodes.get(0).outerHtml());
    }

    @Test
    public void testInsert_method_withBogusCommentToken_shouldInsertDeclarationNodeInDocument() {
        // Arrange
        XmlTreeBuilder.Token.Comment bogusCommentToken = new XmlTreeBuilder.Token.Comment();
        bogusCommentToken.data = "!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" " +
                "\"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">";

        // Act
        xmlTreeBuilder.insert(bogusCommentToken);

        // Assert
        List<Node> childNodes = xmlTreeBuilder.currentElement().childNodes();
        assertEquals(1, childNodes.size());
        assertEquals("<!DOCTYPE html>", childNodes.get(0).outerHtml());
    }

    @Test
    public void testInsert_method_withCharacterToken_shouldInsertTextNodeInDocument() {
        // Arrange
        XmlTreeBuilder.Token.Character characterToken = new XmlTreeBuilder.Token.Character();
        characterToken.data = "Text";

        // Act
        xmlTreeBuilder.insert(characterToken);

        // Assert
        List<Node> childNodes = xmlTreeBuilder.currentElement().childNodes();
        assertEquals(1, childNodes.size());
        assertEquals("Text", childNodes.get(0).outerHtml());
    }

    @Test
    public void testInsert_method_withDoctypeToken_shouldInsertDoctypeNodeInDocument() {
        // Arrange
        XmlTreeBuilder.Token.Doctype doctypeToken = new XmlTreeBuilder.Token.Doctype();
        doctypeToken.name = "html";

        // Act
        xmlTreeBuilder.insert(doctypeToken);

        // Assert
        List<Node> childNodes = xmlTreeBuilder.currentElement().childNodes();
        assertEquals(1, childNodes.size());
        assertEquals("<!DOCTYPE html>", childNodes.get(0).outerHtml());
    }

    @Test
    public void testPopStackToClose_method_withValidEndTagToken_shouldPopElementsFromStack() {
        // Arrange
        Element parentElement = new Element("parent");
        Element childElement = new Element("child");
        parentElement.appendChild(childElement);
        xmlTreeBuilder.stack.add(parentElement);
        xmlTreeBuilder.stack.add(childElement);

        XmlTreeBuilder.Token.EndTag endTagToken = new XmlTreeBuilder.Token.EndTag();
        endTagToken.value = "</child>";

        // Act
        xmlTreeBuilder.popStackToClose(endTagToken);

        // Assert
        assertEquals(1, xmlTreeBuilder.stack.size());
        assertEquals("parent", xmlTreeBuilder.stack.get(0).nodeName());
    }

    @Test
    public void testParseFragment_method_withValidInput_shouldReturnListOfNodes() {
        // Arrange
        String input = "<div><p>Paragraph</p><h1>Heading</h1></div>";

        // Act
        List<Node> nodes = xmlTreeBuilder.parseFragment(input, "", null, null);

        // Assert
        assertEquals(1, nodes.size());
        assertTrue(nodes.get(0) instanceof Element);
        assertEquals("div", nodes.get(0).nodeName());

        Element divElement = (Element) nodes.get(0);
        Elements childElements = divElement.children();
        assertEquals(2, childElements.size());
        assertEquals("p", childElements.get(0).nodeName());
        assertEquals("h1", childElements.get(1).nodeName());
        assertEquals("Paragraph", childElements.get(0).text());
        assertEquals("Heading", childElements.get(1).text());
    }

}