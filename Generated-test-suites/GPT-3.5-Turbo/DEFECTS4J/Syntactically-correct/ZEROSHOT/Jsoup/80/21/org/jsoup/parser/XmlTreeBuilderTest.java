package org.jsoup.parser;

import org.junit.Test;

import static org.junit.Assert.*;

public class XmlTreeBuilderTest {

    @Test
    public void testDefaultSettings() {
        // Arrange
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();

        // Act
        ParseSettings settings = xmlTreeBuilder.defaultSettings();

        // Assert
        assertEquals(ParseSettings.preserveCase, settings);
    }

    @Test
    public void testParseWithReaderAndBaseUri() {
        // Arrange
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();

        // Act
        Document document = xmlTreeBuilder.parse(null, "");

        // Assert
        assertNull(document);
    }

    @Test
    public void testParseWithStringAndBaseUri() {
        // Arrange
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();

        // Act
        Document document = xmlTreeBuilder.parse("", "");

        // Assert
        assertNull(document);
    }

    @Test
    public void testInitialiseParse() {
        // Arrange
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();

        // Act
        xmlTreeBuilder.initialiseParse(null, "", null, null);

        // Assert
        // No assertion as this method doesn't return anything
    }

    @Test
    public void testProcessWithStartTagToken() {
        // Arrange
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Token token = new Token(Token.TokenType.StartTag);

        // Act
        boolean result = xmlTreeBuilder.process(token);

        // Assert
        assertTrue(result);
    }

    @Test
    public void testProcessWithEndTagToken() {
        // Arrange
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Token token = new Token(Token.TokenType.EndTag);

        // Act
        boolean result = xmlTreeBuilder.process(token);

        // Assert
        assertTrue(result);
    }

    @Test
    public void testProcessWithCommentToken() {
        // Arrange
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Token token = new Token(Token.TokenType.Comment);

        // Act
        boolean result = xmlTreeBuilder.process(token);

        // Assert
        assertTrue(result);
    }

    @Test
    public void testProcessWithCharacterToken() {
        // Arrange
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Token token = new Token(Token.TokenType.Character);

        // Act
        boolean result = xmlTreeBuilder.process(token);

        // Assert
        assertTrue(result);
    }

    @Test
    public void testProcessWithDoctypeToken() {
        // Arrange
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Token token = new Token(Token.TokenType.Doctype);

        // Act
        boolean result = xmlTreeBuilder.process(token);

        // Assert
        assertTrue(result);
    }

    @Test(expected = AssertionError.class)
    public void testProcessWithUnexpectedTokenType() {
        // Arrange
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Token token = new Token(Token.TokenType.EOF);

        // Act
        xmlTreeBuilder.process(token);
    }

    @Test
    public void testInsertNode() {
        // Arrange
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Node node = new Node();

        // Act
        xmlTreeBuilder.insertNode(node);

        // Assert
        assertEquals(1, xmlTreeBuilder.currentElement().childNodes().size());
        assertEquals(node, xmlTreeBuilder.currentElement().childNodes().get(0));
    }

    @Test
    public void testInsertWithTagSelfClosing() {
        // Arrange
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Token.StartTag startTag = new Token.StartTag();
        startTag.setSelfClosing(true);

        // Act
        Element element = xmlTreeBuilder.insert(startTag);

        // Assert
        assertTrue(element.tag().isSelfClosing());
    }

    @Test
    public void testInsertWithTagNotSelfClosing() {
        // Arrange
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Token.StartTag startTag = new Token.StartTag();
        startTag.setSelfClosing(false);

        // Act
        Element element = xmlTreeBuilder.insert(startTag);

        // Assert
        assertFalse(element.tag().isSelfClosing());
    }

    @Test
    public void testInsertWithCommentToken() {
        // Arrange
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Token.Comment commentToken = new Token.Comment();

        // Act
        xmlTreeBuilder.insert(commentToken);

        // Assert
        assertEquals(1, xmlTreeBuilder.currentElement().childNodes().size());
        assertTrue(xmlTreeBuilder.currentElement().childNodes().get(0) instanceof Comment);
    }

    @Test
    public void testInsertWithCharacterToken() {
        // Arrange
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Token.Character characterToken = new Token.Character();

        // Act
        xmlTreeBuilder.insert(characterToken);

        // Assert
        assertEquals(1, xmlTreeBuilder.currentElement().childNodes().size());
        assertTrue(xmlTreeBuilder.currentElement().childNodes().get(0) instanceof TextNode);
    }

    @Test
    public void testInsertWithDoctypeToken() {
        // Arrange
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Token.Doctype doctypeToken = new Token.Doctype();

        // Act
        xmlTreeBuilder.insert(doctypeToken);

        // Assert
        assertEquals(1, xmlTreeBuilder.currentElement().childNodes().size());
        assertTrue(xmlTreeBuilder.currentElement().childNodes().get(0) instanceof DocumentType);
    }

    @Test
    public void testPopStackToClose() {
        // Arrange
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Token.EndTag endTag = new Token.EndTag();
        endTag.setTagName("");

        // Act
        xmlTreeBuilder.popStackToClose(endTag);

        // Assert
        // No assertion as this method doesn't return anything
    }

    @Test
    public void testParseFragment() {
        // Arrange
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        String inputFragment = "";
        String baseUri = "";

        // Act
        List<Node> nodes = xmlTreeBuilder.parseFragment(inputFragment, baseUri, null, null);

        // Assert
        assertNotNull(nodes);
    }
}