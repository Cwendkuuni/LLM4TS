package org.jsoup.parser;

import org.junit.Before;
import org.junit.Test;
import org.jsoup.helper.Validate;
import org.jsoup.nodes.*;
import org.jsoup.parser.Token.*;

import java.util.List;
import java.util.Stack;

import static org.junit.Assert.*;

public class XmlTreeBuilderTest {
    private XmlTreeBuilder xmlTreeBuilder;

    @Before
    public void setUp() {
        xmlTreeBuilder = new XmlTreeBuilder();
    }

    @Test
    public void testInitialiseParse() throws Exception {
        assertEquals("input", xmlTreeBuilder.input);
        assertEquals("baseUrl", xmlTreeBuilder.baseUri);
        assertNotNull(xmlTreeBuilder.errors);
        assertEquals(1, xmlTreeBuilder.stack.size());
        assertEquals("doc", xmlTreeBuilder.stack.get(0).nodeName());
        assertEquals("xml", xmlTreeBuilder.doc.outputSettings().syntax().name());
    }

    @Test
    public void testProcess_StartTag_UnknownTag() throws Exception {
        Token.StartTag startTag = new Token.StartTag("tag", new Attributes());
        xmlTreeBuilder.process(startTag);

        assertEquals("tag", xmlTreeBuilder.currentElement().nodeName());
        assertEquals(2, xmlTreeBuilder.stack.size());
        assertTrue(xmlTreeBuilder.currentElement().isTagUnknown());
    }

    @Test
    public void testProcess_StartTag_SelfClosingTag() throws Exception {
        Token.StartTag startTag = new Token.StartTag("tag", new Attributes());
        startTag.selfClosing = true;
        xmlTreeBuilder.process(startTag);

        assertEquals("tag", xmlTreeBuilder.currentElement().nodeName());
        assertEquals(1, xmlTreeBuilder.stack.size());
        assertTrue(xmlTreeBuilder.currentElement().isSelfClosing());
    }

    @Test
    public void testProcess_StartTag_NormalTag() throws Exception {
        Token.StartTag startTag = new Token.StartTag("tag", new Attributes());
        xmlTreeBuilder.process(startTag);

        assertEquals("tag", xmlTreeBuilder.currentElement().nodeName());
        assertEquals(2, xmlTreeBuilder.stack.size());
    }

    @Test
    public void testProcess_EndTag_PopStack() throws Exception {
        Element element1 = new Element(Tag.valueOf("tag1"), "");
        Element element2 = new Element(Tag.valueOf("tag2"), "");
        xmlTreeBuilder.stack.push(element1);
        xmlTreeBuilder.stack.push(element2);

        Token.EndTag endTag = new Token.EndTag("tag1");
        xmlTreeBuilder.process(endTag);

        assertEquals(1, xmlTreeBuilder.stack.size());
        assertEquals(element2, xmlTreeBuilder.currentElement());
    }

    @Test
    public void testProcess_EndTag_NoPopStack() throws Exception {
        Element element1 = new Element(Tag.valueOf("tag1"), "");
        Element element2 = new Element(Tag.valueOf("tag2"), "");
        xmlTreeBuilder.stack.push(element1);
        xmlTreeBuilder.stack.push(element2);

        Token.EndTag endTag = new Token.EndTag("tag3");
        xmlTreeBuilder.process(endTag);

        assertEquals(2, xmlTreeBuilder.stack.size());
        assertEquals(element2, xmlTreeBuilder.currentElement());
    }

    @Test
    public void testProcess_Comment() throws Exception {
        Token.Comment commentToken = new Token.Comment("comment", "");
        xmlTreeBuilder.process(commentToken);

        Node expectedComment = new Comment("comment", "");
        assertEquals(expectedComment, xmlTreeBuilder.currentElement().childNode(0));
    }

    @Test
    public void testProcess_Character() throws Exception {
        Token.Character characterToken = new Token.Character("character", "");
        xmlTreeBuilder.process(characterToken);

        Node expectedTextNode = new TextNode("character", "");
        assertEquals(expectedTextNode, xmlTreeBuilder.currentElement().childNode(0));
    }

    @Test
    public void testProcess_Doctype() throws Exception {
        Token.Doctype doctype = new Token.Doctype("name", "public", "system", "");
        xmlTreeBuilder.process(doctype);

        Node expectedDoctype = new DocumentType("name", "public", "system", "");
        assertEquals(expectedDoctype, xmlTreeBuilder.currentElement().childNode(0));
    }

    @Test
    public void testInsertNode() throws Exception {
        Element element = new Element(Tag.valueOf("tag"), "");
        xmlTreeBuilder.stack.push(element);

        Token.StartTag startTag = new Token.StartTag("tag1", new Attributes());
        xmlTreeBuilder.insert(startTag);

        assertEquals(1, xmlTreeBuilder.currentElement().childNodeSize());
        assertEquals("tag1", xmlTreeBuilder.currentElement().childNode(0).nodeName());
    }

    @Test
    public void testInsert_WithBogusComment() throws Exception {
        Element element = new Element(Tag.valueOf("tag"), "");
        xmlTreeBuilder.stack.push(element);

        Token.Comment commentToken = new Token.Comment("comment", "");
        commentToken.bogus = true;
        xmlTreeBuilder.insert(commentToken);

        Comment expectedComment = new Comment("comment", "");
        assertEquals(expectedComment, xmlTreeBuilder.currentElement().childNode(0));
    }

    @Test
    public void testInsert_WithXmlDeclaration() throws Exception {
        Element element = new Element(Tag.valueOf("tag"), "");
        xmlTreeBuilder.stack.push(element);

        Token.Comment commentToken = new Token.Comment("declarationtext", "");
        commentToken.bogus = true;
        xmlTreeBuilder.insert(commentToken);

        Node expectedXmlDeclaration = new XmlDeclaration("declarationtext", "", true);
        assertEquals(expectedXmlDeclaration, xmlTreeBuilder.currentElement().childNode(0));
    }

    @Test
    public void testParseFragment() throws Exception {
        String inputFragment = "<tag>text</tag>";
        ParseErrorList errors = new ParseErrorList();
        List<Node> nodes = xmlTreeBuilder.parseFragment(inputFragment, "baseUri", errors);

        assertEquals(1, nodes.size());
        assertTrue(nodes.get(0) instanceof Element);
        Element expectedElement = new Element(Tag.valueOf("tag"), "baseUri");
        expectedElement.appendChild(new TextNode("text", ""));
        assertEquals(expectedElement, nodes.get(0));
    }
}