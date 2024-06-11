package org.jsoup.parser;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

public class XmlTreeBuilderTest {

    private XmlTreeBuilder xmlTreeBuilder;

    @Before
    public void setUp() {
        xmlTreeBuilder = new XmlTreeBuilder();
    }

    @Test
    public void testInitialiseParse() {
        String input = "<html><body><h1>Test</h1></body></html>";
        String baseUri = "https://example.com";
        ParseErrorList errors = new ParseErrorList();

        xmlTreeBuilder.initialiseParse(input, baseUri, errors);

        assertEquals(1, xmlTreeBuilder.stack.size());
        assertEquals(baseUri, xmlTreeBuilder.baseUri);
        assertEquals(errors, xmlTreeBuilder.errors);
    }

    @Test
    public void testProcess_StartTag() {
        Token token = mock(Token.class);
        Token.StartTag startTag = mock(Token.StartTag.class);
        when(token.type).thenReturn(Token.TokenType.StartTag);
        when(token.asStartTag()).thenReturn(startTag);

        xmlTreeBuilder.process(token);

        verify(xmlTreeBuilder).insert(startTag);
    }

    @Test
    public void testProcess_EndTag() {
        Token token = mock(Token.class);
        Token.EndTag endTag = mock(Token.EndTag.class);
        when(token.type).thenReturn(Token.TokenType.EndTag);
        when(token.asEndTag()).thenReturn(endTag);

        xmlTreeBuilder.process(token);

        verify(xmlTreeBuilder).popStackToClose(endTag);
    }

    @Test
    public void testProcess_Comment() {
        Token token = mock(Token.class);
        Token.Comment comment = mock(Token.Comment.class);
        when(token.type).thenReturn(Token.TokenType.Comment);
        when(token.asComment()).thenReturn(comment);

        xmlTreeBuilder.process(token);

        verify(xmlTreeBuilder).insert(comment);
    }

    @Test
    public void testProcess_Character() {
        Token token = mock(Token.class);
        Token.Character character = mock(Token.Character.class);
        when(token.type).thenReturn(Token.TokenType.Character);
        when(token.asCharacter()).thenReturn(character);

        xmlTreeBuilder.process(token);

        verify(xmlTreeBuilder).insert(character);
    }

    @Test
    public void testProcess_Doctype() {
        Token token = mock(Token.class);
        Token.Doctype doctype = mock(Token.Doctype.class);
        when(token.type).thenReturn(Token.TokenType.Doctype);
        when(token.asDoctype()).thenReturn(doctype);

        xmlTreeBuilder.process(token);

        verify(xmlTreeBuilder).insert(doctype);
    }

    @Test
    public void testInsert_StartTag() {
        Token.StartTag startTag = mock(Token.StartTag.class);
        Tag tag = mock(Tag.class);
        Element el = mock(Element.class);
        when(startTag.name()).thenReturn("test");
        when(startTag.attributes()).thenReturn("id=test");
        when(Tag.valueOf(startTag.name())).thenReturn(tag);
        when(new Element(tag, xmlTreeBuilder.baseUri, startTag.attributes)).thenReturn(el);

        Element result = xmlTreeBuilder.insert(startTag);

        verify(xmlTreeBuilder).insertNode(el);
        assertEquals(el, result);
    }

    @Test
    public void testInsert_StartTag_SelfClosing() {
        Token.StartTag startTag = mock(Token.StartTag.class);
        Tag tag = mock(Tag.class);
        Element el = mock(Element.class);
        when(startTag.name()).thenReturn("test");
        when(startTag.attributes()).thenReturn("");
        when(Tag.valueOf(startTag.name())).thenReturn(tag);
        when(new Element(tag, xmlTreeBuilder.baseUri, startTag.attributes)).thenReturn(el);
        when(startTag.isSelfClosing()).thenReturn(true);

        Element result = xmlTreeBuilder.insert(startTag);

        verify(xmlTreeBuilder).insertNode(el);
        verify(xmlTreeBuilder.tokeniser).acknowledgeSelfClosingFlag();
        verify(tag).setSelfClosing();
        assertEquals(el, result);
    }

    @Test
    public void testInsert_Comment() {
        Token.Comment commentToken = mock(Token.Comment.class);
        Comment comment = mock(Comment.class);
        when(commentToken.getData()).thenReturn("test");
        when(new Comment(commentToken.getData(), xmlTreeBuilder.baseUri)).thenReturn(comment);

        xmlTreeBuilder.insert(commentToken);

        verify(xmlTreeBuilder).insertNode(comment);
    }

    @Test
    public void testInsert_Character() {
        Token.Character characterToken = mock(Token.Character.class);
        TextNode node = mock(TextNode.class);
        when(characterToken.getData()).thenReturn("test");
        when(new TextNode(characterToken.getData(), xmlTreeBuilder.baseUri)).thenReturn(node);

        xmlTreeBuilder.insert(characterToken);

        verify(xmlTreeBuilder).insertNode(node);
    }

    @Test
    public void testInsert_Doctype() {
        Token.Doctype doctype = mock(Token.Doctype.class);
        DocumentType doctypeNode = mock(DocumentType.class);
        when(doctype.getName()).thenReturn("test");
        when(doctype.getPublicIdentifier()).thenReturn("public");
        when(doctype.getSystemIdentifier()).thenReturn("system");
        when(new DocumentType(doctype.getName(), doctype.getPublicIdentifier(), doctype.getSystemIdentifier(), xmlTreeBuilder.baseUri)).thenReturn(doctypeNode);

        xmlTreeBuilder.insert(doctype);

        verify(xmlTreeBuilder).insertNode(doctypeNode);
    }

    @Test
    public void testPopStackToClose_NotFound() {
        Token.EndTag endTag = mock(Token.EndTag.class);
        when(endTag.name()).thenReturn("test");

        xmlTreeBuilder.popStackToClose(endTag);

        verify(xmlTreeBuilder, never()).insertNode(any(Node.class));
    }

    @Test
    public void testPopStackToClose_Found() {
        Token.EndTag endTag = mock(Token.EndTag.class);
        Element element = mock(Element.class);
        Iterator<Element> iterator = mock(Iterator.class);
        when(endTag.name()).thenReturn("test");
        xmlTreeBuilder.stack.push(element);
        when(xmlTreeBuilder.stack.descendingIterator()).thenReturn(iterator);
        when(iterator.hasNext()).thenReturn(true, false);
        when(iterator.next()).thenReturn(element);

        xmlTreeBuilder.popStackToClose(endTag);

        verify(xmlTreeBuilder).insertNode(any(Node.class));
        verify(iterator).remove();
    }
}