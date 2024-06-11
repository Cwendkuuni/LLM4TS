
package org.jsoup.parser;

import org.jsoup.Jsoup;
import org.jsoup.helper.Validate;
import org.jsoup.nodes.*;

import java.io.Reader;
import java.io.StringReader;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class XmlTreeBuilderTest {

    private XmlTreeBuilder xmlTreeBuilder;

    @Before
    public void setup() {
        xmlTreeBuilder = new XmlTreeBuilder();
    }

    @Test
    public void parse_Reader_validInput_returnsDocumentObject() {
        // Arrange
        String input = "<root></root>";
        String baseUri = "http://example.com";
        Reader reader = new StringReader(input);

        // Act
        Document document = xmlTreeBuilder.parse(reader, baseUri);

        // Assert
        assertNotNull(document);
        assertEquals("root", document.child(0).nodeName());
    }

    @Test
    public void parse_Reader_emptyInput_returnsEmptyDocumentObject() {
        // Arrange
        String input = "";
        String baseUri = "http://example.com";
        Reader reader = new StringReader(input);

        // Act
        Document document = xmlTreeBuilder.parse(reader, baseUri);

        // Assert
        assertNotNull(document);
        assertEquals(0, document.childNodeSize());
    }

    @Test
    public void parse_String_validInput_returnsDocumentObject() {
        // Arrange
        String input = "<root></root>";
        String baseUri = "http://example.com";

        // Act
        Document document = xmlTreeBuilder.parse(input, baseUri);

        // Assert
        assertNotNull(document);
        assertEquals("root", document.child(0).nodeName());
    }

    @Test
    public void parse_String_emptyInput_returnsEmptyDocumentObject() {
        // Arrange
        String input = "";
        String baseUri = "http://example.com";

        // Act
        Document document = xmlTreeBuilder.parse(input, baseUri);

        // Assert
        assertNotNull(document);
        assertEquals(0, document.childNodeSize());
    }

    @Test
    public void initialiseParse_validInput_initializesParsing() {
        // Arrange
        String input = "<root></root>";
        String baseUri = "http://example.com";
        Reader reader = new StringReader(input);
        ParseErrorList errors = ParseErrorList.noTracking();
        ParseSettings settings = ParseSettings.preserveCase;

        // Act
        xmlTreeBuilder.initialiseParse(reader, baseUri, errors, settings);

        // Assert
        assertEquals(baseUri, xmlTreeBuilder.baseUri);
        assertEquals(errors, xmlTreeBuilder.errors);
        assertEquals(settings, xmlTreeBuilder.settings);
        assertNotNull(xmlTreeBuilder.stack);
        assertNotNull(xmlTreeBuilder.doc);
        assertEquals(0, xmlTreeBuilder.stack.size());
    }

    @Test
    public void process_validStartTagToken_includesElementInDocument() {
        // Arrange
        Token startTagToken = new Token.StartTag("div");

        // Act
        xmlTreeBuilder.process(startTagToken);

        // Assert
        assertNotNull(xmlTreeBuilder.currentElement().selectFirst("div"));
    }

    @Test
    public void process_validEndTagToken_closesMatchingOpenElement() {
        // Arrange
        Token startTagToken1 = new Token.StartTag("div");
        Token startTagToken2 = new Token.StartTag("p");
        Token endTagToken = new Token.EndTag("div");

        // Act
        xmlTreeBuilder.process(startTagToken1);
        xmlTreeBuilder.process(startTagToken2);
        xmlTreeBuilder.process(endTagToken);

        // Assert
        assertNull(xmlTreeBuilder.currentElement().selectFirst("div"));
        assertNotNull(xmlTreeBuilder.currentElement().selectFirst("p"));
    }

    @Test
    public void process_validCommentToken_includesCommentInDocument() {
        // Arrange
        Token commentToken = new Token.Comment("This is a comment");

        // Act
        xmlTreeBuilder.process(commentToken);

        // Assert
        assertNotNull(xmlTreeBuilder.currentElement().selectFirst("comment"));
    }

    @Test
    public void process_validCharacterToken_includesCharacterInDocument() {
        // Arrange
        Token characterToken = new Token.Character("Hello, World!");

        // Act
        xmlTreeBuilder.process(characterToken);

        // Assert
        assertNotNull(xmlTreeBuilder.currentElement().text());
        assertEquals("Hello, World!", xmlTreeBuilder.currentElement().text());
    }

    @Test
    public void process_validDoctypeToken_includesDoctypeInDocument() {
        // Arrange
        Token.Doctype doctypeToken = new Token.Doctype("html");

        // Act
        xmlTreeBuilder.process(doctypeToken);

        // Assert
        assertNotNull(xmlTreeBuilder.currentElement().selectFirst("doctype"));
    }

    @Test
    public void process_invalidToken_throwsException() {
        // Arrange
        Token invalidToken = new Token.StartTag("");

        // Act & Assert
        try {
            xmlTreeBuilder.process(invalidToken);
            fail("Expected ValidateException to be thrown");
        } catch (Validate.ValidateException e) {
            assertEquals("Unexpected token type: StartTag", e.getMessage());
        }
    }

    @Test
    public void insertNode_validNode_addsNodeToCurrentElement() {
        // Arrange
        Node node = new TextNode("Hello, World!");

        // Act
        xmlTreeBuilder.insertNode(node);

        // Assert
        assertNotNull(xmlTreeBuilder.currentElement().text());
        assertEquals("Hello, World!", xmlTreeBuilder.currentElement().text());
    }

    @Test
    public void insert_validStartTagToken_includesElementInDocument() {
        // Arrange
        Token.StartTag startTagToken = new Token.StartTag("div");

        // Act
        Element element = xmlTreeBuilder.insert(startTagToken);

        // Assert
        assertEquals(startTagToken.name(), element.nodeName());
        assertNotNull(xmlTreeBuilder.currentElement().selectFirst("div"));
    }

    @Test
    public void insert_validCommentToken_includesCommentInDocument() {
        // Arrange
        Token.Comment commentToken = new Token.Comment("This is a comment");

        // Act
        xmlTreeBuilder.insert(commentToken);

        // Assert
        assertNotNull(xmlTreeBuilder.currentElement().selectFirst("comment"));
    }

    @Test
    public void insert_validCharacterToken_includesCharacterInDocument() {
        // Arrange
        Token.Character characterToken = new Token.Character("Hello, World!");

        // Act
        xmlTreeBuilder.insert(characterToken);

        // Assert
        assertNotNull(xmlTreeBuilder.currentElement().text());
        assertEquals("Hello, World!", xmlTreeBuilder.currentElement().text());
    }

    @Test
    public void insert_validDoctypeToken_includesDoctypeInDocument() {
        // Arrange
        Token.Doctype doctypeToken = new Token.Doctype("html");

        // Act
        xmlTreeBuilder.insert(doctypeToken);

        // Assert
        assertNotNull(xmlTreeBuilder.currentElement().selectFirst("doctype"));
    }

    @Test
    public void popStackToClose_validEndTag_closesMatchingOpenElement() {
        // Arrange
        Token.StartTag startTagToken1 = new Token.StartTag("div");
        Token.StartTag startTagToken2 = new Token.StartTag("p");
        Token.EndTag endTagToken = new Token.EndTag("div");

        // Act
        xmlTreeBuilder.process(startTagToken1);
        xmlTreeBuilder.process(startTagToken2);
        xmlTreeBuilder.popStackToClose(endTagToken);

        // Assert
        assertNull(xmlTreeBuilder.currentElement().selectFirst("div"));
        assertNotNull(xmlTreeBuilder.currentElement().selectFirst("p"));
    }

    @Test
    public void popStackToClose_invalidEndTag_doesNothing() {
        // Arrange
        Token.StartTag startTagToken1 = new Token.StartTag("div");
        Token.StartTag startTagToken2 = new Token.StartTag("p");
        Token.EndTag endTagToken = new Token.EndTag("span");

        // Act
        xmlTreeBuilder.process(startTagToken1);
        xmlTreeBuilder.process(startTagToken2);
        xmlTreeBuilder.popStackToClose(endTagToken);

        // Assert
        assertNotNull(xmlTreeBuilder.currentElement().selectFirst("div"));
        assertNotNull(xmlTreeBuilder.currentElement().selectFirst("p"));
    }

    @Test
    public void parseFragment_validInput_returnsListofChildNodes() {
        // Arrange
        String inputFragment = "<div><p></p></div>";
        String baseUri = "http://example.com";
        ParseErrorList errors = ParseErrorList.noTracking();
        ParseSettings settings = ParseSettings.preserveCase;

        // Act
        List<Node> childNodes = xmlTreeBuilder.parseFragment(inputFragment, baseUri, errors, settings);

        // Assert
        assertEquals(1, childNodes.size());
        assertEquals("div", childNodes.get(0).nodeName());
    }
}
