package org.jsoup.parser;

import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.safety.Whitelist;
import org.jsoup.select.Elements;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class XmlTreeBuilderTest {

    private XmlTreeBuilder xmlTreeBuilder;
    private ParseErrorList parseErrorList;
    private String input;
    private String baseUri;
    private Document doc;

    @Before
    public void setup() {
        xmlTreeBuilder = new XmlTreeBuilder();
        parseErrorList = new ParseErrorList();
        input = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" +
                "<root>" +
                "<child1 attribute1=\"value1\"></child1>" +
                "<child2 attribute2=\"value2\"></child2>" +
                "</root>";
        baseUri = "http://example.com/";
        doc = Jsoup.parse(input);
    }

    @Test
    public void initialiseParse_shouldAddDocumentToStack() {
        xmlTreeBuilder.initialiseParse(input, baseUri, parseErrorList);
        Assert.assertEquals(doc, xmlTreeBuilder.stack.peek());
    }

    @Test
    public void process_givenStartTag_shouldInsertElement() {
        Token.StartTag startTag = new Token.StartTag("child1");
        Tag tag = Tag.valueOf(startTag.name());
        Element el = new Element(tag, baseUri, startTag.attributes);

        xmlTreeBuilder.process(startTag);

        Assert.assertTrue(xmlTreeBuilder.stack.contains(el));
    }

    @Test
    public void process_givenEndTag_shouldPopStackToClose() {
        Token.EndTag endTag = new Token.EndTag("child1");
        Element child1Element = new Element(Tag.valueOf("child1"), baseUri);
        xmlTreeBuilder.stack.push(child1Element);

        xmlTreeBuilder.process(endTag);

        Assert.assertEquals(0, xmlTreeBuilder.stack.size());
    }

    @Test
    public void process_givenComment_shouldInsertCommentNode() {
        Token.Comment commentToken = new Token.Comment("This is a comment");
        Comment comment = new Comment(commentToken.getData(), baseUri);

        xmlTreeBuilder.process(commentToken);

        Elements comments = doc.select("comment");
        Assert.assertEquals(comment, comments.first());
    }

    @Test
    public void process_givenCharacter_shouldInsertTextNode() {
        Token.Character characterToken = new Token.Character("Some text");
        Node node = new TextNode(characterToken.getData(), baseUri);

        xmlTreeBuilder.process(characterToken);

        Elements textNodes = doc.select("text");
        Assert.assertEquals(node, textNodes.first());
    }

    @Test
    public void process_givenDoctype_shouldInsertDoctypeNode() {
        Token.Doctype doctype = new Token.Doctype("html", "", "", baseUri);
        DocumentType doctypeNode = new DocumentType(doctype.getName(), doctype.getPublicIdentifier(), doctype.getSystemIdentifier(), baseUri);

        xmlTreeBuilder.process(doctype);

        Elements doctypes = doc.select("doctype");
        Assert.assertEquals(doctypeNode, doctypes.first());
    }

    @Test(expected = IllegalStateException.class)
    public void process_givenUnexpectedTokenType_shouldThrowException() {
        Token token = new Token(Token.TokenType.Character);

        xmlTreeBuilder.process(token);
    }

    @Test
    public void insertNode_givenNode_shouldAppendNodeToCurrentElement() {
        Element parent = doc.createElement("parent");
        xmlTreeBuilder.stack.push(parent);

        Element child = doc.createElement("child");
        xmlTreeBuilder.insertNode(child);

        Assert.assertTrue(parent.childNodes().contains(child));
    }

    @Test
    public void insert_givenStartTag_shouldInsertElement() {
        Token.StartTag startTag = new Token.StartTag("child1");
        Tag tag = Tag.valueOf(startTag.name());
        Element el = new Element(tag, baseUri, startTag.attributes);

        Element result = xmlTreeBuilder.insert(startTag);

        Assert.assertTrue(xmlTreeBuilder.stack.contains(el));
        Assert.assertEquals(el, result);
    }

    @Test
    public void insert_givenSelfClosingStartTag_shouldAcknowledgeSelfClosingFlagAndSetTagSelfClosing() {
        Token.StartTag startTag = new Token.StartTag("child1", true);
        Tag tag = Tag.valueOf(startTag.name());
        Element el = new Element(tag, baseUri, startTag.attributes);
        tag.setSelfClosing();

        Element result = xmlTreeBuilder.insert(startTag);

        Assert.assertTrue(xmlTreeBuilder.stack.contains(el));
        Assert.assertTrue(tag.isSelfClosing());
        Assert.assertEquals(el, result);
    }

    @Test
    public void insert_givenCommentToken_shouldInsertCommentNode() {
        Token.Comment commentToken = new Token.Comment("This is a comment");
        Comment comment = new Comment(commentToken.getData(), baseUri);

        xmlTreeBuilder.insert(commentToken);

        Elements comments = doc.select("comment");
        Assert.assertEquals(comment, comments.first());
    }

    @Test
    public void insert_givenCharacterToken_shouldInsertTextNode() {
        Token.Character characterToken = new Token.Character("Some text");
        Node node = new TextNode(characterToken.getData(), baseUri);

        xmlTreeBuilder.insert(characterToken);

        Elements textNodes = doc.select("text");
        Assert.assertEquals(node, textNodes.first());
    }

    @Test
    public void insert_givenDoctype_shouldInsertDoctypeNode() {
        Token.Doctype doctype = new Token.Doctype("html", "", "", baseUri);
        DocumentType doctypeNode = new DocumentType(doctype.getName(), doctype.getPublicIdentifier(), doctype.getSystemIdentifier(), baseUri);

        xmlTreeBuilder.insert(doctype);

        Elements doctypes = doc.select("doctype");
        Assert.assertEquals(doctypeNode, doctypes.first());
    }

    @Test
    public void popStackToClose_givenExistingElementOnStack_shouldRemoveFirstOccurrence() {
        Token.EndTag endTag = new Token.EndTag("child2");
        Element child1Element = new Element(Tag.valueOf("child1"), baseUri);
        Element child2Element = new Element(Tag.valueOf("child2"), baseUri);
        xmlTreeBuilder.stack.push(child1Element);
        xmlTreeBuilder.stack.push(child2Element);

        xmlTreeBuilder.popStackToClose(endTag);

        Assert.assertEquals(1, xmlTreeBuilder.stack.size());
        Assert.assertTrue(xmlTreeBuilder.stack.contains(child1Element));
        Assert.assertFalse(xmlTreeBuilder.stack.contains(child2Element));
    }

    @Test
    public void popStackToClose_givenNonExistingElementOnStack_shouldSkip() {
        Token.EndTag endTag = new Token.EndTag("child2");
        Element child1Element = new Element(Tag.valueOf("child1"), baseUri);
        xmlTreeBuilder.stack.push(child1Element);

        xmlTreeBuilder.popStackToClose(endTag);

        Assert.assertEquals(1, xmlTreeBuilder.stack.size());
        Assert.assertTrue(xmlTreeBuilder.stack.contains(child1Element));
    }
    
    @Test
    public void parse_shouldReturnParsedDocument() {
        Document parsedDoc = Jsoup.parse(input);
        Document xmlDoc = xmlTreeBuilder.parse(input, baseUri);
        
        Assert.assertEquals(parsedDoc.html(), xmlDoc.html());
    }
}