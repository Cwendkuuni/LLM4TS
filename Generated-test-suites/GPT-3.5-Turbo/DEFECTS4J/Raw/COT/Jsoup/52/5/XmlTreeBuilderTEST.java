
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

public class XmlTreeBuilderTest {

    @Test
    public void testInitialiseParse() {
        // Given
        String input = "<xml></xml>";
        String baseUri = "http://example.com";
        ParseErrorList errors = new ParseErrorList();

        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();

        // When
        xmlTreeBuilder.initialiseParse(input, baseUri, errors);

        // Then
        assertEquals(input, xmlTreeBuilder.getInput());
        assertEquals(baseUri, xmlTreeBuilder.getBaseUri());
        assertEquals(errors, xmlTreeBuilder.getErrors());
    }

    @Test
    public void testProcess_StartTag() {
        // Given
        Token token = new Token.StartTag("tag", "");
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();

        // When
        boolean result = xmlTreeBuilder.process(token);

        // Then
        assertTrue(result);
        // Additional assertions to check if the tag was inserted correctly
    }

    @Test
    public void testProcess_EndTag() {
        // Given
        Token token = new Token.EndTag("tag");
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();

        // When
        boolean result = xmlTreeBuilder.process(token);

        // Then
        assertTrue(result);
        // Additional assertions to check if the stack was updated correctly
    }

    @Test
    public void testProcess_Comment() {
        // Given
        Token token = new Token.Comment("comment");
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();

        // When
        boolean result = xmlTreeBuilder.process(token);

        // Then
        assertTrue(result);
        // Additional assertions to check if the comment was inserted correctly
    }

    @Test
    public void testProcess_Character() {
        // Given
        Token token = new Token.Character("character");
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();

        // When
        boolean result = xmlTreeBuilder.process(token);

        // Then
        assertTrue(result);
        // Additional assertions to check if the character was inserted correctly
    }

    @Test
    public void testProcess_Doctype() {
        // Given
        Token token = new Token.Doctype("name", "public", "system");
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();

        // When
        boolean result = xmlTreeBuilder.process(token);

        // Then
        assertTrue(result);
        // Additional assertions to check if the doctype was inserted correctly
    }

    @Test
    public void testInsertNode() {
        // Given
        Element element = new Element("tag");
        Node node = new TextNode("text");
        element.appendChild(node);
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        xmlTreeBuilder.stack.add(element);

        // When
        xmlTreeBuilder.insertNode(node);

        // Then
        assertEquals(2, element.childNodeSize());
        assertEquals(node, element.childNode(1));
    }

    @Test
    public void testInsert_StartTag() {
        // Given
        Token.StartTag startTag = new Token.StartTag("tag", "attribute");
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();

        // When
        Element element = xmlTreeBuilder.insert(startTag);

        // Then
        assertNotNull(element);
        assertEquals(startTag.name(), element.nodeName());
        // Additional assertions to check if the element was inserted correctly
    }

    @Test
    public void testInsert_Comment() {
        // Given
        Token.Comment commentToken = new Token.Comment("comment");
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();

        // When
        xmlTreeBuilder.insert(commentToken);

        // Then
        // Additional assertions to check if the comment was inserted correctly
    }

    @Test
    public void testInsert_Character() {
        // Given
        Token.Character characterToken = new Token.Character("character");
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();

        // When
        xmlTreeBuilder.insert(characterToken);

        // Then
        // Additional assertions to check if the character was inserted correctly
    }

    @Test
    public void testInsert_Doctype() {
        // Given
        Token.Doctype doctypeToken = new Token.Doctype("name", "public", "system");
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();

        // When
        xmlTreeBuilder.insert(doctypeToken);

        // Then
        // Additional assertions to check if the doctype was inserted correctly
    }

    @Test
    public void testPopStackToClose_ElementFound() {
        // Given
        Token.EndTag endTag = new Token.EndTag("tag");
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();

        // When
        xmlTreeBuilder.stack.add(new Element("tag"));
        xmlTreeBuilder.popStackToClose(endTag);

        // Then
        // Additional assertions to check if the correct elements were removed from the stack
    }

    @Test
    public void testPopStackToClose_ElementNotFound() {
        // Given
        Token.EndTag endTag = new Token.EndTag("tag");
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();

        // When
        xmlTreeBuilder.stack.add(new Element("otherTag"));
        xmlTreeBuilder.popStackToClose(endTag);

        // Then
        // Additional assertions to check if the stack remains unchanged
    }

    @Test
    public void testParseFragment() {
        // Given
        String inputFragment = "<tag></tag>";
        String baseUri = "http://example.com";
        ParseErrorList errors = new ParseErrorList();
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();

        // When
        List<Node> nodes = xmlTreeBuilder.parseFragment(inputFragment, baseUri, errors);

        // Then
        assertNotNull(nodes);
        // Additional assertions to check if the nodes were parsed correctly
    }
}

