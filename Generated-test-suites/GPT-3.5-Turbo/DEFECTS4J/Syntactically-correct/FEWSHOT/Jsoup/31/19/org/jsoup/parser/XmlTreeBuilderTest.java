
package org.jsoup.parser;

import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.parser.Token.Comment;
import org.jsoup.parser.Token.Character;
import org.jsoup.parser.Token.Doctype;
import org.jsoup.parser.Token.EndTag;
import org.jsoup.parser.Token.StartTag;
import org.jsoup.parser.XmlTreeBuilder;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class XmlTreeBuilderTest {
    private XmlTreeBuilder xmlTreeBuilder;

    @Before
    public void setup() {
        xmlTreeBuilder = new XmlTreeBuilder();
    }

    @Test
    public void testInitialiseParse() {
        String input = "<tag></tag>";
        String baseUri = "http://example.com";
        ParseErrorList errors = new ParseErrorList();

        xmlTreeBuilder.initialiseParse(input, baseUri, errors);

        // Verify stack contains the document
        Assert.assertEquals(1, xmlTreeBuilder.stack.size());
        Assert.assertTrue(xmlTreeBuilder.stack.contains(xmlTreeBuilder.doc));

        // Verify baseUri is set correctly
        Assert.assertEquals(baseUri, xmlTreeBuilder.baseUri);

        // Verify errors is the same object passed in
        Assert.assertEquals(errors, xmlTreeBuilder.errors);
    }

    @Test
    public void testProcess_startTag() {
        Token.StartTag startTag = new Token.StartTag();
        startTag.name("tag");

        Element element = xmlTreeBuilder.insert(startTag);

        // Verify element is inserted into the current element's children
        Assert.assertTrue(xmlTreeBuilder.currentElement().children().contains(element));

        // Verify element is added to the stack
        Assert.assertTrue(xmlTreeBuilder.stack.contains(element));
    }

    @Test
    public void testProcess_endTag() {
        Token.EndTag endTag = new Token.EndTag();
        endTag.name("tag");

        Element element1 = new Element("tag");
        Element element2 = new Element("other");

        xmlTreeBuilder.stack.push(element1);
        xmlTreeBuilder.stack.push(element2);

        xmlTreeBuilder.popStackToClose(endTag);

        // Verify element1 is removed from the stack
        Assert.assertFalse(xmlTreeBuilder.stack.contains(element1));

        // Verify element2 remains in the stack
        Assert.assertTrue(xmlTreeBuilder.stack.contains(element2));
    }

    @Test
    public void testProcess_comment() {
        Token.Comment commentToken = new Token.Comment();
        commentToken.data("comment");

        xmlTreeBuilder.insert(commentToken);

        // Verify comment node is inserted into the current element's children
        Assert.assertTrue(
            xmlTreeBuilder.currentElement().children().stream()
                .anyMatch(node -> node instanceof org.jsoup.nodes.Comment &&
                                  ((org.jsoup.nodes.Comment) node).getData().equals(commentToken.getData())));
    }

    @Test
    public void testProcess_character() {
        Token.Character characterToken = new Token.Character();
        characterToken.data("character");

        xmlTreeBuilder.insert(characterToken);

        // Verify text node is inserted into the current element's children
        Assert.assertTrue(
            xmlTreeBuilder.currentElement().children().stream()
                .anyMatch(node -> node instanceof TextNode &&
                                  ((TextNode) node).getWholeText().equals(characterToken.getData())));
    }

    @Test
    public void testProcess_doctype() {
        Token.Doctype doctype = new Token.Doctype();
        doctype.setName("doctypeName");

        xmlTreeBuilder.insert(doctype);

        // Verify doctype node is inserted into the current element's children
        Assert.assertTrue(
            xmlTreeBuilder.currentElement().children().stream()
                .anyMatch(node -> node instanceof org.jsoup.nodes.DocumentType &&
                                  ((org.jsoup.nodes.DocumentType) node).getName().equals(doctype.getName())));
    }
}

