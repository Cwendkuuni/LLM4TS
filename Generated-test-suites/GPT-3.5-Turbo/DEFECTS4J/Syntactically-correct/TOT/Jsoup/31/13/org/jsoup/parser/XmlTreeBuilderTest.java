
package org.jsoup.parser;

import org.jsoup.nodes.Comment;
import org.jsoup.nodes.DocumentType;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class XmlTreeBuilderTest {

    private XmlTreeBuilder xmlTreeBuilder;

    @Before
    public void setUp() {
        xmlTreeBuilder = new XmlTreeBuilder();
    }

    @Test
    public void initialiseParse_shouldAddDocumentToStack() {
        // Arrange
        String input = "<root></root>";
        String baseUri = "http://example.com";
        ParseErrorList errors = new ParseErrorList();

        // Act
        xmlTreeBuilder.initialiseParse(input, baseUri, errors);

        // Assert
        assertEquals(1, xmlTreeBuilder.stack.size());
        assertEquals("root", xmlTreeBuilder.stack.get(0).nodeName());
        assertEquals(baseUri, xmlTreeBuilder.stack.get(0).baseUri());
    }

    @Test
    public void process_shouldInsertStartTagToStack() {
        // Arrange
        Token token = new Token.StartTag("tag", new Attributes());

        // Act
        boolean result = xmlTreeBuilder.process(token);

        // Assert
        assertTrue(result);
        assertEquals(1, xmlTreeBuilder.stack.size());
        assertEquals("tag", xmlTreeBuilder.stack.get(0).nodeName());
    }

    @Test
    public void process_shouldPopStackToCloseMatchingEndTag() {
        // Arrange
        Token.StartTag startTag = new Token.StartTag("tag", new Attributes());
        Token.EndTag endTag = new Token.EndTag("tag");
        xmlTreeBuilder.stack.push(new Element("tag", "http://example.com"));

        // Act
        xmlTreeBuilder.insert(startTag);
        xmlTreeBuilder.process(endTag);

        // Assert
        assertEquals(0, xmlTreeBuilder.stack.size());
    }

    @Test
    public void process_shouldInsertCommentToStack() {
        // Arrange
        Token.Comment commentToken = new Token.Comment("This is a comment");

        // Act
        xmlTreeBuilder.insert(commentToken);

        // Assert
        assertEquals(1, xmlTreeBuilder.currentElement().childNodeSize());
        assertEquals("This is a comment", ((Comment) xmlTreeBuilder.currentElement().childNode(0)).getData());
    }

    @Test
    public void process_shouldInsertCharacterToStack() {
        // Arrange
        Token.Character characterToken = new Token.Character("This is a character");

        // Act
        xmlTreeBuilder.insert(characterToken);

        // Assert
        assertEquals(1, xmlTreeBuilder.currentElement().childNodeSize());
        assertEquals("This is a character", xmlTreeBuilder.currentElement().childNode(0).outerHtml());
    }

    @Test
    public void process_shouldInsertDoctypeToStack() {
        // Arrange
        Token.Doctype doctype = new Token.Doctype("html", "", "", "http://example.com");

        // Act
        xmlTreeBuilder.insert(doctype);

        // Assert
        assertEquals(1, xmlTreeBuilder.currentElement().childNodeSize());
        assertTrue(xmlTreeBuilder.currentElement().childNode(0) instanceof DocumentType);
        assertEquals("html", ((DocumentType) xmlTreeBuilder.currentElement().childNode(0)).getName());
    }

    @Test(expected = IllegalStateException.class)
    public void process_shouldThrowExceptionForInvalidToken() {
        // Arrange
        Token invalidToken = new Token();

        // Act
        xmlTreeBuilder.process(invalidToken);
    }
}

