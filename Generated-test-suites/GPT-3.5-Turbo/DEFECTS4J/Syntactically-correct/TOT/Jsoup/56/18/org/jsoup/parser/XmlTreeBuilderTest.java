package org.jsoup.parser;

import org.junit.Before;
import org.junit.Test;

import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import static org.junit.Assert.*;

@RunWith(MockitoJUnitRunner.class)
public class XmlTreeBuilderTest {

    private XmlTreeBuilder xmlTreeBuilder;

    @Before
    public void setup() {
        xmlTreeBuilder = new XmlTreeBuilder();
    }

    @Test
    public void testDefaultSettings_ReturnsPreserveCase() {
        assertEquals(ParseSettings.preserveCase, xmlTreeBuilder.defaultSettings());
    }

    @Test
    public void testParse_ReturnsDocument() {
        String input = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><book><title>Mockingjay</title><author>Suzanne Collins</author></book>";
        String baseUri = "http://example.com";
        Document document = xmlTreeBuilder.parse(input, baseUri);
        assertNotNull(document);
    }

    @Test
    public void testParseFragment_ReturnsListOfNodes() {
        String inputFragment = "<title>Mockingjay</title><author>Suzanne Collins</author>";
        String baseUri = "http://example.com";
        List<Node> nodes = xmlTreeBuilder.parseFragment(inputFragment, baseUri, new ParseErrorList(), ParseSettings.preserveCase);
        assertNotNull(nodes);
        assertEquals(2, nodes.size());
        assertTrue(nodes.get(0) instanceof Element);
        assertTrue(nodes.get(1) instanceof Element);
    }

    @Test
    public void testInsertNode_InsertsNodeToCurrentElement() {
        Element element = new Element(Tag.valueOf("div"), "http://example.com", ParseSettings.preserveCase);
        TextNode node = new TextNode("Hello World", "http://example.com");
        xmlTreeBuilder.setCurrentElement(element);
        xmlTreeBuilder.insertNode(node);
        assertEquals(node, element.childNode(0));
    }

    @Test
    public void testInsert_StartTagWithSelfClosing_UpdatesTagAndStack() {
        Token.StartTag startTag = new Token.StartTag("div");
        startTag.setSelfClosing();
        Element element = xmlTreeBuilder.insert(startTag);
        assertEquals("div", element.nodeName());
        assertTrue(element.tag().isSelfClosing());
        assertTrue(xmlTreeBuilder.getStack().isEmpty());
    }

    @Test
    public void testInsert_StartTagWithoutSelfClosing_UpdatesTagAndStack() {
        Token.StartTag startTag = new Token.StartTag("div");
        Element element = xmlTreeBuilder.insert(startTag);
        assertEquals("div", element.nodeName());
        assertFalse(element.tag().isSelfClosing());
        assertEquals(1, xmlTreeBuilder.getStack().size());
        assertEquals(element, xmlTreeBuilder.getStack().get(0));
    }

    @Test
    public void testInsert_CommentToken_UpdatesStack() {
        Token.Comment commentToken = new Token.Comment("This is a comment");
        Comment comment = new Comment(commentToken.getData(), "http://example.com");
        xmlTreeBuilder.insert(commentToken);
        assertEquals(comment, xmlTreeBuilder.getCurrentElement().childNode(0));
    }

    @Test
    public void testInsert_CharacterToken_UpdatesStack() {
        Token.Character characterToken = new Token.Character("Hello World");
        TextNode textNode = new TextNode(characterToken.getData(), "http://example.com");
        xmlTreeBuilder.insert(characterToken);
        assertEquals(textNode, xmlTreeBuilder.getCurrentElement().childNode(0));
    }

    @Test
    public void testInsert_DoctypeToken_UpdatesStack() {
        Token.Doctype doctypeToken = new Token.Doctype("html", "", "", "http://example.com");
        DocumentType documentType = new DocumentType(doctypeToken.getName(), doctypeToken.getPublicIdentifier(), doctypeToken.getSystemIdentifier(), "http://example.com");
        xmlTreeBuilder.insert(doctypeToken);
        assertEquals(documentType, xmlTreeBuilder.getCurrentElement().childNode(0));
    }

    @Test
    public void testPopStackToClose_ElementFoundInStack_RemovesElementFromStack() {
        Token.EndTag endTag = new Token.EndTag("div");
        Element element = new Element(Tag.valueOf("div"), "http://example.com", ParseSettings.preserveCase);
        element.appendChild(new TextNode("Hello World", "http://example.com"));
        xmlTreeBuilder.getStack().push(element);
        xmlTreeBuilder.getStack().push(new Element(Tag.valueOf("span"), "http://example.com", ParseSettings.preserveCase));
        xmlTreeBuilder.getStack().push(new Element(Tag.valueOf("div"), "http://example.com", ParseSettings.preserveCase));
        xmlTreeBuilder.popStackToClose(endTag);
        assertEquals(1, xmlTreeBuilder.getStack().size());
        assertEquals("span", xmlTreeBuilder.getStack().get(0).nodeName());
        assertEquals("Hello World", xmlTreeBuilder.getStack().get(0).childNode(0).outerHtml());
    }

    @Test
    public void testPopStackToClose_ElementNotFoundInStack_DoesNotRemoveElementFromStack() {
        Token.EndTag endTag = new Token.EndTag("div");
        xmlTreeBuilder.getStack().push(new Element(Tag.valueOf("span"), "http://example.com", ParseSettings.preserveCase));
        xmlTreeBuilder.getStack().push(new Element(Tag.valueOf("p"), "http://example.com", ParseSettings.preserveCase));
        xmlTreeBuilder.popStackToClose(endTag);
        assertEquals(2, xmlTreeBuilder.getStack().size());
    }

}