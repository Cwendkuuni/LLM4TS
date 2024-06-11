package org.jsoup.parser;

import org.jsoup.nodes.Comment;
import org.jsoup.nodes.DocumentType;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.Tag;
import org.jsoup.nodes.TextNode;
import org.jsoup.parser.Token.*;
import org.jsoup.helper.Validate;
import org.jsoup.parser.XmlTreeBuilder;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Iterator;

public class XmlTreeBuilderTest {
    private XmlTreeBuilder xmlTreeBuilder;

    @Before
    public void setup() {
        xmlTreeBuilder = new XmlTreeBuilder();
    }

    @Test
    public void testInitialiseParse() {
        // Arrange
        String input = "";
        String baseUri = "";
        ParseErrorList errors = new ParseErrorList();

        // Act
        xmlTreeBuilder.initialiseParse(input, baseUri, errors);

        // Assert
        Assert.assertEquals(xmlTreeBuilder.stack.size(), 1);
        Assert.assertEquals(xmlTreeBuilder.stack.get(0), xmlTreeBuilder.doc);
    }

    @Test
    public void testProcessStartTag() {
        // Arrange
        Token.StartTag startTag = new Token.StartTag("tag", null);
        Tag tag = Tag.valueOf(startTag.name());
        Element expectedElement = new Element(tag, xmlTreeBuilder.baseUri, startTag.attributes);

        // Act
        Element actualElement = xmlTreeBuilder.insert(startTag);

        // Assert
        Assert.assertEquals(actualElement, expectedElement);
        Assert.assertEquals(xmlTreeBuilder.stack.size(), 1);
        Assert.assertEquals(xmlTreeBuilder.stack.get(0), expectedElement);
    }

    @Test
    public void testProcessEndTag() {
        // Arrange
        Token.EndTag endTag = new Token.EndTag("tag");
        Element element = new Element(Tag.valueOf("tag"), xmlTreeBuilder.baseUri, null);
        xmlTreeBuilder.stack.add(element);

        // Act
        xmlTreeBuilder.popStackToClose(endTag);

        // Assert
        Assert.assertEquals(xmlTreeBuilder.stack.size(), 0);
    }

    @Test
    public void testProcessComment() {
        // Arrange
        Token.Comment commentToken = new Token.Comment("comment");
        Comment expectedComment = new Comment(commentToken.getData(), xmlTreeBuilder.baseUri);

        // Act
        xmlTreeBuilder.insert(commentToken);

        // Assert
        Assert.assertEquals(xmlTreeBuilder.stack.get(0).childNode(0), expectedComment);
    }

    @Test
    public void testProcessCharacter() {
        // Arrange
        Token.Character characterToken = new Token.Character("character");
        Node expectedNode = new TextNode(characterToken.getData(), xmlTreeBuilder.baseUri);

        // Act
        xmlTreeBuilder.insert(characterToken);

        // Assert
        Assert.assertEquals(xmlTreeBuilder.stack.get(0).childNode(0), expectedNode);
    }

    @Test
    public void testProcessDoctype() {
        // Arrange
        Token.Doctype doctypeToken = new Token.Doctype("name", "public", "system");
        DocumentType expectedDoctype = new DocumentType(doctypeToken.getName(), doctypeToken.getPublicIdentifier(), doctypeToken.getSystemIdentifier(), xmlTreeBuilder.baseUri);

        // Act
        xmlTreeBuilder.insert(doctypeToken);

        // Assert
        Assert.assertEquals(xmlTreeBuilder.stack.get(0).childNode(0), expectedDoctype);
    }

    @Test
    public void testPopStackToClose() {
        // Arrange
        Token.EndTag endTag = new Token.EndTag("tag");
        Element element1 = new Element(Tag.valueOf("tag"), xmlTreeBuilder.baseUri, null);
        Element element2 = new Element(Tag.valueOf("otherTag"), xmlTreeBuilder.baseUri, null);
        xmlTreeBuilder.stack.add(element1);
        xmlTreeBuilder.stack.add(element2);

        // Act
        xmlTreeBuilder.popStackToClose(endTag);

        // Assert
        Assert.assertEquals(xmlTreeBuilder.stack.size(), 1);
        Assert.assertEquals(xmlTreeBuilder.stack.get(0), element2);
    }
}