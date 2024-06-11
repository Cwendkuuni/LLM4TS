package org.jsoup.parser;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.DocumentType;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

public class XmlTreeBuilderTest {

    private XmlTreeBuilder xmlTreeBuilder;
    private ParseErrorList errors;
    private String baseUri;

    @Before
    public void setup() {
        xmlTreeBuilder = new XmlTreeBuilder();
        errors = new ParseErrorList();
        baseUri = "https://example.com";
    }

    @Test
    public void initialiseParseTest() {
        String input = "<root></root>";
        xmlTreeBuilder.initialiseParse(input, baseUri, errors);

        List<Node> stack = xmlTreeBuilder.getStack();

        Assert.assertEquals(1, stack.size());

        Document doc = xmlTreeBuilder.getDoc();

        Assert.assertEquals(input, doc.toString());
    }

    @Test
    public void processStartTagTest() {
        String input = "<root></root>";
        xmlTreeBuilder.initialiseParse(input, baseUri, errors);

        Tag tag = Tag.valueOf("root");
        Token.StartTag startTag = new Token.StartTag(tag, "", false);
        xmlTreeBuilder.process(startTag);

        List<Node> stack = xmlTreeBuilder.getStack();

        Assert.assertEquals(1, stack.size());

        Element element = (Element) stack.get(0);

        Assert.assertEquals(tag, element.tag());
        Assert.assertEquals(0, element.children().size());
    }

    @Test
    public void processEndTagTest() {
        String input = "<root></root>";
        xmlTreeBuilder.initialiseParse(input, baseUri, errors);

        Tag tag = Tag.valueOf("root");
        Token.EndTag endTag = new Token.EndTag(tag);
        xmlTreeBuilder.process(endTag);

        List<Node> stack = xmlTreeBuilder.getStack();

        Assert.assertEquals(0, stack.size());
    }

    @Test
    public void processCommentTest() {
        String input = "<!-- comment -->";
        xmlTreeBuilder.initialiseParse(input, baseUri, errors);

        String commentData = " comment ";
        Token.Comment comment = new Token.Comment(commentData, "");
        xmlTreeBuilder.process(comment);

        List<Node> stack = xmlTreeBuilder.getStack();

        Assert.assertEquals(1, stack.size());

        Comment commentNode = (Comment) stack.get(0);

        Assert.assertEquals(commentData, commentNode.getData());
    }

    @Test
    public void processCharacterTest() {
        String input = "character";
        xmlTreeBuilder.initialiseParse(input, baseUri, errors);

        String characterData = "character";
        Token.Character character = new Token.Character(characterData);
        xmlTreeBuilder.process(character);

        List<Node> stack = xmlTreeBuilder.getStack();

        Assert.assertEquals(1, stack.size());

        TextNode textNode = (TextNode) stack.get(0);

        Assert.assertEquals(characterData, textNode.text());
    }

    @Test
    public void processDoctypeTest() {
        String input = "<!DOCTYPE html>";
        xmlTreeBuilder.initialiseParse(input, baseUri, errors);

        String name = "html";
        String publicIdentifier = "";
        String systemIdentifier = "";
        Token.Doctype doctype = new Token.Doctype(name, publicIdentifier, systemIdentifier);
        xmlTreeBuilder.process(doctype);

        List<Node> stack = xmlTreeBuilder.getStack();

        Assert.assertEquals(1, stack.size());

        DocumentType documentType = (DocumentType) stack.get(0);

        Assert.assertEquals(name, documentType.name());
        Assert.assertEquals(publicIdentifier, documentType.publicIdentifier());
        Assert.assertEquals(systemIdentifier, documentType.systemIdentifier());
    }

    @Test
    public void insertNodeTest() {
        String input = "<root></root>";
        xmlTreeBuilder.initialiseParse(input, baseUri, errors);

        Tag tag = Tag.valueOf("root");
        Token.StartTag startTag = new Token.StartTag(tag, "", false);
        xmlTreeBuilder.process(startTag);

        Element element = (Element) xmlTreeBuilder.getStack().get(0);

        String text = "text";
        TextNode textNode = new TextNode(text, baseUri);
        xmlTreeBuilder.insertNode(textNode);

        Assert.assertEquals(1, element.children().size());
        Assert.assertEquals(textNode, element.childNode(0));
    }

    @Test
    public void insertStartTagTest() {
        String input = "<root></root>";
        xmlTreeBuilder.initialiseParse(input, baseUri, errors);

        Tag tag = Tag.valueOf("root");
        Token.StartTag startTag = new Token.StartTag(tag, "", false);
        Element element = xmlTreeBuilder.insert(startTag);

        Assert.assertNotNull(element);
        Assert.assertEquals(tag, element.tag());
        Assert.assertNull(element.parent());
        Assert.assertEquals(0, element.children().size());
    }

    @Test
    public void insertStartTagSelfClosingTest() {
        String input = "<root/>";
        xmlTreeBuilder.initialiseParse(input, baseUri, errors);

        Tag tag = Tag.valueOf("root");
        Token.StartTag startTag = new Token.StartTag(tag, "", true);
        Element element = xmlTreeBuilder.insert(startTag);

        Assert.assertNotNull(element);
        Assert.assertEquals(tag, element.tag());
        Assert.assertNull(element.parent());
        Assert.assertEquals(0, element.children().size());

        Assert.assertTrue(tag.isSelfClosing());
    }

    @Test
    public void insertCommentTest() {
        String input = "<!-- comment -->";
        xmlTreeBuilder.initialiseParse(input, baseUri, errors);

        String commentData = " comment ";
        Token.Comment comment = new Token.Comment(commentData, "");
        xmlTreeBuilder.insert(comment);

        List<Node> stack = xmlTreeBuilder.getStack();

        Assert.assertEquals(1, stack.size());

        Comment commentNode = (Comment) stack.get(0);

        Assert.assertEquals(commentData, commentNode.getData());
    }

    @Test
    public void insertBogusCommentTest() {
        String input = "<!-- !DOCTYPE html -->";
        xmlTreeBuilder.initialiseParse(input, baseUri, errors);

        String commentData = " !DOCTYPE html ";
        Token.Comment comment = new Token.Comment(commentData, "");
        xmlTreeBuilder.insert(comment);

        List<Node> stack = xmlTreeBuilder.getStack();

        Assert.assertEquals(1, stack.size());

        XmlDeclaration xmlDeclaration = (XmlDeclaration) stack.get(0);

        Assert.assertEquals(commentData.substring(1), xmlDeclaration.toString());
    }

    @Test
    public void insertCharacterTest() {
        String input = "character";
        xmlTreeBuilder.initialiseParse(input, baseUri, errors);

        String characterData = "character";
        Token.Character character = new Token.Character(characterData);
        xmlTreeBuilder.insert(character);

        List<Node> stack = xmlTreeBuilder.getStack();

        Assert.assertEquals(1, stack.size());

        TextNode textNode = (TextNode) stack.get(0);

        Assert.assertEquals(characterData, textNode.text());
    }

    @Test
    public void insertDoctypeTest() {
        String input = "<!DOCTYPE html>";
        xmlTreeBuilder.initialiseParse(input, baseUri, errors);

        String name = "html";
        String publicIdentifier = "";
        String systemIdentifier = "";
        Token.Doctype doctype = new Token.Doctype(name, publicIdentifier, systemIdentifier);
        xmlTreeBuilder.insert(doctype);

        List<Node> stack = xmlTreeBuilder.getStack();

        Assert.assertEquals(1, stack.size());

        DocumentType documentType = (DocumentType) stack.get(0);

        Assert.assertEquals(name, documentType.name());
        Assert.assertEquals(publicIdentifier, documentType.publicIdentifier());
        Assert.assertEquals(systemIdentifier, documentType.systemIdentifier());
    }

    @Test
    public void popStackToCloseTest() {
        String input = "<root><child></child></root>";
        xmlTreeBuilder.initialiseParse(input, baseUri, errors);

        Tag childTag = Tag.valueOf("child");
        Token.EndTag endTag = new Token.EndTag(childTag);
        xmlTreeBuilder.popStackToClose(endTag);

        List<Node> stack = xmlTreeBuilder.getStack();

        Assert.assertEquals(1, stack.size());

        Element rootElement = (Element) stack.get(0);

        Assert.assertEquals("root", rootElement.tagName());
        Assert.assertEquals(0, rootElement.children().size());
    }

    @Test
    public void parseFragmentTest() {
        String inputFragment = "<child></child>";
        List<Node> nodes = xmlTreeBuilder.parseFragment(inputFragment, baseUri, errors);

        Assert.assertEquals(1, nodes.size());

        Element element = (Element) nodes.get(0);

        Assert.assertEquals("child", element.tagName());
        Assert.assertEquals(0, element.children().size());
    }
}