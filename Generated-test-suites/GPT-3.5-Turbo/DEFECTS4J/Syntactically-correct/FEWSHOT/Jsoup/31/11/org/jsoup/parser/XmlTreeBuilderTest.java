package org.jsoup.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

import org.jsoup.nodes.Comment;
import org.jsoup.nodes.DocumentType;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.jsoup.parser.Token.Character;
import org.jsoup.parser.Token.Comment;
import org.jsoup.parser.Token.Doctype;
import org.jsoup.parser.Token.EndTag;
import org.jsoup.parser.Token.StartTag;
import org.junit.Before;
import org.junit.Test;

public class XmlTreeBuilderTest {

    private XmlTreeBuilder xmlTreeBuilder;

    @Before
    public void setup() {
        xmlTreeBuilder = spy(new XmlTreeBuilder());
    }

    @Test
    public void testInitialiseParse() {
        String input = "input";
        String baseUri = "baseUri";
        ParseErrorList errors = mock(ParseErrorList.class);

        xmlTreeBuilder.initialiseParse(input, baseUri, errors);

        verify(xmlTreeBuilder).super.initialiseParse(input, baseUri, errors);
        verify(xmlTreeBuilder.stack).add(xmlTreeBuilder.doc);
    }

    @Test
    public void testProcessStartTag() {
        StartTag startTag = new StartTag("tag", null);
        Element el = mock(Element.class);
        when(xmlTreeBuilder.insert(startTag)).thenReturn(el);

        boolean result = xmlTreeBuilder.process(startTag);

        verify(xmlTreeBuilder).insert(startTag);
        verify(xmlTreeBuilder.tokeniser).acknowledgeSelfClosingFlag();
        verify(xmlTreeBuilder.stack).add(el);
        assertEquals(true, result);
    }

    @Test
    public void testProcessEndTag() {
        EndTag endTag = new EndTag("tag");
        xmlTreeBuilder.stack.add(mock(Element.class));

        boolean result = xmlTreeBuilder.process(endTag);

        verify(xmlTreeBuilder).popStackToClose(endTag);
        assertEquals(true, result);
    }

    @Test
    public void testProcessComment() {
        Comment comment = new Comment("data", null);
        Comment commentNode = mock(Comment.class);
        when(xmlTreeBuilder.createCommentNode(comment)).thenReturn(commentNode);

        boolean result = xmlTreeBuilder.process(comment);

        verify(xmlTreeBuilder).createCommentNode(comment);
        verify(xmlTreeBuilder).insertNode(commentNode);
        assertEquals(true, result);
    }

    @Test
    public void testProcessCharacter() {
        Character character = new Character("data");
        TextNode characterNode = mock(TextNode.class);
        when(xmlTreeBuilder.createCharacterNode(character)).thenReturn(characterNode);

        boolean result = xmlTreeBuilder.process(character);

        verify(xmlTreeBuilder).createCharacterNode(character);
        verify(xmlTreeBuilder).insertNode(characterNode);
        assertEquals(true, result);
    }

    @Test
    public void testProcessDoctype() {
        Doctype doctype = new Doctype("name", "publicId", "systemId");
        DocumentType doctypeNode = mock(DocumentType.class);
        when(xmlTreeBuilder.createDoctypeNode(doctype)).thenReturn(doctypeNode);

        boolean result = xmlTreeBuilder.process(doctype);

        verify(xmlTreeBuilder).createDoctypeNode(doctype);
        verify(xmlTreeBuilder).insertNode(doctypeNode);
        assertEquals(true, result);
    }

    @Test
    public void testProcessEOF() {
        boolean result = xmlTreeBuilder.process(new Token.EOF());

        assertEquals(true, result);
    }

    @Test
    public void testInsertStartTag() {
        StartTag startTag = new StartTag("tag", null);
        Tag tag = mock(Tag.class);
        Element el = mock(Element.class);
        when(xmlTreeBuilder.createTag(tag, startTag.attributes)).thenReturn(el);
        when(tag.isKnownTag()).thenReturn(true);

        Element result = xmlTreeBuilder.insert(startTag);

        verify(xmlTreeBuilder).createTag(tag, startTag.attributes);
        verify(xmlTreeBuilder).insertNode(el);
        verify(tag, never()).setSelfClosing();
        verify(xmlTreeBuilder.tokeniser, never()).acknowledgeSelfClosingFlag();
        assertEquals(el, result);
    }

    @Test
    public void testInsertStartTagIsSelfClosing() {
        StartTag startTag = new StartTag("tag", null);
        Tag tag = mock(Tag.class);
        Element el = mock(Element.class);
        when(xmlTreeBuilder.createTag(tag, startTag.attributes)).thenReturn(el);
        startTag.setSelfClosing(true);
        when(tag.isKnownTag()).thenReturn(false);

        Element result = xmlTreeBuilder.insert(startTag);

        verify(xmlTreeBuilder).createTag(tag, startTag.attributes);
        verify(xmlTreeBuilder).insertNode(el);
        verify(tag).setSelfClosing();
        verify(xmlTreeBuilder.tokeniser).acknowledgeSelfClosingFlag();
        assertEquals(el, result);
    }

    @Test
    public void testInsertComment() {
        Comment comment = new Comment("data", null);
        Comment commentNode = mock(Comment.class);
        when(xmlTreeBuilder.createCommentNode(comment)).thenReturn(commentNode);

        xmlTreeBuilder.insert(comment);

        verify(xmlTreeBuilder).createCommentNode(comment);
        verify(xmlTreeBuilder).insertNode(commentNode);
    }

    @Test
    public void testInsertCharacter() {
        Character character = new Character("data");
        TextNode characterNode = mock(TextNode.class);
        when(xmlTreeBuilder.createCharacterNode(character)).thenReturn(characterNode);

        xmlTreeBuilder.insert(character);

        verify(xmlTreeBuilder).createCharacterNode(character);
        verify(xmlTreeBuilder).insertNode(characterNode);
    }

    @Test
    public void testInsertDoctype() {
        Doctype doctype = new Doctype("name", "publicId", "systemId");
        DocumentType doctypeNode = mock(DocumentType.class);
        when(xmlTreeBuilder.createDoctypeNode(doctype)).thenReturn(doctypeNode);

        xmlTreeBuilder.insert(doctype);

        verify(xmlTreeBuilder).createDoctypeNode(doctype);
        verify(xmlTreeBuilder).insertNode(doctypeNode);
    }

    @Test
    public void testPopStackToClose() {
        EndTag endTag = new EndTag("tag");
        Element element1 = mock(Element.class);
        Element element2 = mock(Element.class);
        xmlTreeBuilder.stack.add(element1);
        xmlTreeBuilder.stack.add(element2);

        xmlTreeBuilder.popStackToClose(endTag);

        verify(xmlTreeBuilder.stack).descendingIterator();
        verify(xmlTreeBuilder.stack, times(2)).remove();
        verify(element2).nodeName();
        verify(element1).nodeName();
    }
}