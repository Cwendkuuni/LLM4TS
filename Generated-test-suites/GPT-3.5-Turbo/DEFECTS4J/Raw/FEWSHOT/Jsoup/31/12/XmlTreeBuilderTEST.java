package org.jsoup.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.*;

import org.jsoup.helper.Validate;
import org.jsoup.nodes.*;
import org.jsoup.parser.Token.StartTag;
import org.jsoup.parser.Token.EndTag;
import org.jsoup.parser.Token.Comment;
import org.jsoup.parser.Token.Character;
import org.jsoup.parser.Token.Doctype;
import org.jsoup.parser.XmlTreeBuilder;
import org.junit.Before;
import org.junit.Test;

public class XmlTreeBuilderTest {
    private XmlTreeBuilder xmlTreeBuilder;
    private Document doc;
    private Element element;

    @Before
    public void setUp() {
        xmlTreeBuilder = new XmlTreeBuilder();
        doc = new Document("");
        element = new Element("tag", "");
    }

    @Test
    public void testInitialiseParse() {
        String input = "";
        String baseUri = "";
        ParseErrorList errors = mock(ParseErrorList.class);

        Stack<Node> stack = new Stack<>();
        stack.add(doc);

        xmlTreeBuilder.initialiseParse(input, baseUri, errors);

        verify(errors, never()).addError(any(Tokenizer.class), any(ParseError.class));
        assertSame(stack, xmlTreeBuilder.stack);
    }

    @Test
    public void testProcess_StartTag() {
        Token token = new Token.StartTag("tag");

        xmlTreeBuilder.stack.add(element);
        xmlTreeBuilder.process(token);

        verify(xmlTreeBuilder).insert(token.asStartTag());
    }

    @Test
    public void testProcess_EndTag() {
        Token token = new Token.EndTag("tag");

        xmlTreeBuilder.process(token);

        verify(xmlTreeBuilder).popStackToClose(token.asEndTag());
    }

    @Test
    public void testProcess_Comment() {
        Token token = new Token.Comment("comment");

        xmlTreeBuilder.process(token);

        verify(xmlTreeBuilder).insert(token.asComment());
    }

    @Test
    public void testProcess_Character() {
        Token token = new Token.Character("c");

        xmlTreeBuilder.process(token);

        verify(xmlTreeBuilder).insert(token.asCharacter());
    }

    @Test
    public void testProcess_Doctype() {
        Token token = new Token.Doctype("doctype");

        xmlTreeBuilder.process(token);

        verify(xmlTreeBuilder).insert(token.asDoctype());
    }

    @Test
    public void testProcess_EOF() {
        Token token = new Token.EOF();

        xmlTreeBuilder.process(token);

        verify(xmlTreeBuilder, never()).insert(any());
        verify(xmlTreeBuilder, never()).popStackToClose(any());
        verify(xmlTreeBuilder, never()).insertNode(any());
    }

    @Test
    public void testInsertNode() {
        Node node = new Element("tag", "");

        xmlTreeBuilder.currentElement().appendChild(node);

        assertEquals(node, xmlTreeBuilder.currentElement().childNode(0));
    }

    @Test
    public void testInsert_StartTag_SelfClosing_KnownTag() {
        Token.StartTag startTag = new Token.StartTag("tag", new Attributes());

        Element el = xmlTreeBuilder.insert(startTag);

        verify(xmlTreeBuilder).insertNode(eq(el));
        verify(xmlTreeBuilder.tokeniser).acknowledgeSelfClosingFlag();
        verify(startTag, never()).setSelfClosing();
    }

    @Test
    public void testInsert_StartTag_SelfClosing_UnknownTag() {
        Tag tag = mock(Tag.class);
        when(tag.isKnownTag()).thenReturn(false);
        Token.StartTag startTag = new Token.StartTag("tag", new Attributes());

        Element el = xmlTreeBuilder.insert(startTag);

        verify(xmlTreeBuilder).insertNode(eq(el));
        verify(xmlTreeBuilder.tokeniser).acknowledgeSelfClosingFlag();
        verify(startTag).setSelfClosing();
    }

    @Test
    public void testInsert_StartTag_NotSelfClosing() {
        Token.StartTag startTag = new Token.StartTag("tag", new Attributes());

        Element el = xmlTreeBuilder.insert(startTag);

        verify(xmlTreeBuilder).insertNode(eq(el));
        assertEquals(el, xmlTreeBuilder.stack.peek());
    }

    @Test
    public void testInsert_Comment() {
        Token.Comment commentToken = new Token.Comment("comment");

        Comment comment = new Comment(commentToken.getData(), xmlTreeBuilder.baseUri);
        xmlTreeBuilder.insert(commentToken);

        verify(xmlTreeBuilder).insertNode(eq(comment));
    }

    @Test
    public void testInsert_Character() {
        Token.Character characterToken = new Token.Character("c");

        TextNode node = new TextNode(characterToken.getData(), xmlTreeBuilder.baseUri);
        xmlTreeBuilder.insert(characterToken);

        verify(xmlTreeBuilder).insertNode(eq(node));
    }

    @Test
    public void testInsert_Doctype() {
        Token.Doctype d = new Token.Doctype("doctype");

        DocumentType doctypeNode = new DocumentType(d.getName(), d.getPublicIdentifier(), d.getSystemIdentifier(), xmlTreeBuilder.baseUri);
        xmlTreeBuilder.insert(d);

        verify(xmlTreeBuilder).insertNode(eq(doctypeNode));
    }

    @Test
    public void testPopStackToClose_NotFound() {
        Token.EndTag endTag = new Token.EndTag("tag");

        xmlTreeBuilder.popStackToClose(endTag);

        verify(xmlTreeBuilder.stack, never()).descendingIterator();
    }

    @Test
    public void testPopStackToClose_Found() {
        Token.EndTag endTag = new Token.EndTag("tag");

        Element firstFound = mock(Element.class);

        Iterator<Element> it = mock(Iterator.class);
        when(it.hasNext()).thenReturn(true, false);
        when(it.next()).thenReturn(firstFound);

        when(xmlTreeBuilder.stack.descendingIterator()).thenReturn(it);

        xmlTreeBuilder.popStackToClose(endTag);

        verify(xmlTreeBuilder.stack.descendingIterator(), times(2)).remove();
        verify(xmlTreeBuilder.stack.descendingIterator()).remove();
    }
}