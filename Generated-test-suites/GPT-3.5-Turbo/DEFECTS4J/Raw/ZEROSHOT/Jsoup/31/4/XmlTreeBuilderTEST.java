package org.jsoup.parser;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.*;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class XmlTreeBuilderTest {

    private XmlTreeBuilder xmlTreeBuilder;

    @Mock
    private Tokenizer tokenizerMock;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        xmlTreeBuilder = new XmlTreeBuilder();
        xmlTreeBuilder.tokenizer = tokenizerMock;
    }

    @Test
    public void shouldInsertNode() {
        Node node = mock(Node.class);
        Element currentElementMock = mock(Element.class);
        when(xmlTreeBuilder.currentElement()).thenReturn(currentElementMock);

        xmlTreeBuilder.insertNode(node);

        verify(currentElementMock).appendChild(node);
    }

    @Test
    public void shouldInsertStartTag() {
        Token.StartTag startTag = mock(Token.StartTag.class);
        when(startTag.name()).thenReturn("tag");
        when(startTag.attributes()).thenReturn(Collections.emptyMap());

        Element elementMock = mock(Element.class);
        when(xmlTreeBuilder.currentElement()).thenReturn(elementMock);

        Element result = xmlTreeBuilder.insert(startTag);

        verify(xmlTreeBuilder.tokenizer).acknowledgeSelfClosingFlag();
        verify(startTag).isSelfClosing();
        verify(xmlTreeBuilder.tokenizer).acknowledgeSelfClosingFlag();
        verify(startTag).isSelfClosing();

        verify(elementMock).appendChild(any(Element.class));
        verify(elementMock).appendChild(result);
    }

    @Test
    public void shouldInsertComment() {
        Token.Comment commentToken = mock(Token.Comment.class);
        when(commentToken.getData()).thenReturn("comment");

        Comment commentMock = mock(Comment.class);
        when(xmlTreeBuilder.baseUri).thenReturn("baseUri");
        when(xmlTreeBuilder.currentElement()).thenReturn(commentMock);

        xmlTreeBuilder.insert(commentToken);

        verify(xmlTreeBuilder.baseUri).appendChild(any(Comment.class));
        verify(xmlTreeBuilder.baseUri).appendChild(commentMock);
    }

    @Test
    public void shouldInsertCharacter() {
        Token.Character characterToken = mock(Token.Character.class);
        when(characterToken.getData()).thenReturn("character");

        TextNode textNodeMock = mock(TextNode.class);
        when(xmlTreeBuilder.baseUri).thenReturn("baseUri");
        when(xmlTreeBuilder.currentElement()).thenReturn(textNodeMock);

        xmlTreeBuilder.insert(characterToken);

        verify(xmlTreeBuilder.baseUri).appendChild(any(TextNode.class));
        verify(xmlTreeBuilder.baseUri).appendChild(textNodeMock);
    }

    @Test
    public void shouldInsertDoctype() {
        Token.Doctype doctype = mock(Token.Doctype.class);
        when(doctype.getName()).thenReturn("doctype");
        when(doctype.getPublicIdentifier()).thenReturn("publicId");
        when(doctype.getSystemIdentifier()).thenReturn("systemId");

        DocumentType documentTypeMock = mock(DocumentType.class);
        when(xmlTreeBuilder.baseUri).thenReturn("baseUri");
        when(xmlTreeBuilder.currentElement()).thenReturn(documentTypeMock);

        xmlTreeBuilder.insert(doctype);

        verify(xmlTreeBuilder.baseUri).appendChild(any(DocumentType.class));
        verify(xmlTreeBuilder.baseUri).appendChild(documentTypeMock);
    }

    @Test
    public void shouldPopStackToClose() {
        Token.EndTag endTag = mock(Token.EndTag.class);
        when(endTag.name()).thenReturn("endTag");

        Element elementMock1 = mock(Element.class);
        when(elementMock1.nodeName()).thenReturn("endTag");
        Element elementMock2 = mock(Element.class);
        Element elementMock3 = mock(Element.class);
        when(xmlTreeBuilder.stack).thenReturn(new LinkedList<>(Arrays.asList(elementMock1, elementMock2, elementMock3)));

        xmlTreeBuilder.popStackToClose(endTag);

        verify(elementMock2).remove();
        verify(elementMock3).remove();
    }

    @Test
    public void shouldNotPopStackToCloseWhenNotFound() {
        Token.EndTag endTag = mock(Token.EndTag.class);
        when(endTag.name()).thenReturn("endTag");

        Element elementMock1 = mock(Element.class);
        Element elementMock2 = mock(Element.class);
        Element elementMock3 = mock(Element.class);
        when(xmlTreeBuilder.stack).thenReturn(new LinkedList<>(Arrays.asList(elementMock1, elementMock2, elementMock3)));

        xmlTreeBuilder.popStackToClose(endTag);

        verify(elementMock1, never()).remove();
        verify(elementMock2, never()).remove();
        verify(elementMock3, never()).remove();
    }

    @Test(expected = RuntimeException.class)
    public void shouldFailForUnexpectedTokenType() {
        Token tokenMock = mock(Token.class);
        when(tokenMock.type()).thenReturn(Token.Type.Unknown);

        xmlTreeBuilder.process(tokenMock);
    }

}