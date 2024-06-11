package org.jsoup.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

import org.jsoup.helper.Validate;
import org.jsoup.nodes.Comment;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.DocumentType;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class XmlTreeBuilderTest {

    private XmlTreeBuilder xmlTreeBuilder;
    private Token mockToken;
    private Element mockElement;
    private Attribute mockAttribute;

    @Before
    public void setUp() {
        xmlTreeBuilder = new XmlTreeBuilder();
        mockToken = mock(Token.class);
        mockElement = mock(Element.class);
        mockAttribute = mock(Attribute.class);
    }

    @Test
    public void testUpdateInitialiseParse() {
        String input = "<testInput>";
        String baseUri = "testBaseUri";
        ParseErrorList errors = new ParseErrorList();
        Document doc = mock(Document.class);

        xmlTreeBuilder.initialiseParse(input, baseUri, errors);
        verify(doc).outputSettings().syntax(Document.OutputSettings.Syntax.xml);
        verify(xmlTreeBuilder.stack).add(doc);
    }

    @Test
    public void testProcessStartTag() {
        when(mockToken.type).thenReturn(Token.TokenType.StartTag);
        StartTag mockStartTag = mock(StartTag.class);
        when(mockToken.asStartTag()).thenReturn(mockStartTag);

        Element element = xmlTreeBuilder.insert(mockStartTag);

        verify(xmlTreeBuilder).insertNode(mockElement);
        verify(xmlTreeBuilder.tokeniser).acknowledgeSelfClosingFlag();
        verify(xmlTreeBuilder.stack).add(mockElement);
        verify(mockStartTag).isSelfClosing();
        verify(mockStartTag).name();
        verify(mockStartTag).attributes();
        verify(mockElement).setSelfClosing();
    }

    @Test
    public void testProcessEndTag() {
        when(mockToken.type).thenReturn(Token.TokenType.EndTag);
        EndTag mockEndTag = mock(EndTag.class);
        when(mockToken.asEndTag()).thenReturn(mockEndTag);
        String name = "testName";
        when(mockEndTag.name()).thenReturn(name);
        List<Element> stack = spy(new ArrayList<>());
        xmlTreeBuilder.stack = stack;

        xmlTreeBuilder.popStackToClose(mockEndTag);

        verify(xmlTreeBuilder.stack, times(2)).size();
        verify(xmlTreeBuilder.stack, times(2)).get(anyInt());
        verify(xmlTreeBuilder.stack).remove(anyInt());
        verify(mockEndTag, atLeastOnce()).name();
        verify(mockElement, never()).nodeName();
    }

    @Test
    public void testProcessComment() {
        when(mockToken.type).thenReturn(Token.TokenType.Comment);
        Comment mockComment = mock(Comment.class);
        when(mockToken.asComment()).thenReturn(mockComment);
        String data = "testData";
        when(mockComment.getData()).thenReturn(data);
        String baseUri = "testBaseUri";
        when(mockComment.baseUri()).thenReturn(baseUri);
        String tagName = "!testTagName";
        XmlDeclaration xmlDeclaration = mock(XmlDeclaration.class);
        when(xmlDeclaration.transitionKey()).thenReturn(tagName);
        whenNew(XmlDeclaration.class).withArguments(any(), any(), anyBoolean()).thenReturn(xmlDeclaration);

        xmlTreeBuilder.insert(mockComment);

        verify(xmlTreeBuilder).insertNode(xmlDeclaration);
        verify(mockComment, atLeastOnce()).getData();
        verify(mockComment).baseUri();
        verify(xmlDeclaration).transitionKey();
        verify(xmlDeclaration).setSelfClosing();
    }

    @Test
    public void testProcessCharacter() {
        when(mockToken.type).thenReturn(Token.TokenType.Character);
        Character mockCharacter = mock(Character.class);
        when(mockToken.asCharacter()).thenReturn(mockCharacter);
        String data = "testData";
        when(mockCharacter.getData()).thenReturn(data);
        String baseUri = "testBaseUri";
        when(mockCharacter.baseUri()).thenReturn(baseUri);

        xmlTreeBuilder.insert(mockCharacter);

        verify(xmlTreeBuilder).insertNode(mock(Element.class));
        verify(mockCharacter).getData();
        verify(mockCharacter).baseUri();
    }

    @Test
    public void testProcessDoctype() {
        when(mockToken.type).thenReturn(Token.TokenType.Doctype);
        Doctype mockDoctype = mock(Doctype.class);
        when(mockToken.asDoctype()).thenReturn(mockDoctype);
        String name = "testName";
        when(mockDoctype.getName()).thenReturn(name);
        String publicIdentifier = "testPublicIdentifier";
        when(mockDoctype.getPublicIdentifier()).thenReturn(publicIdentifier);
        String systemIdentifier = "testSystemIdentifier";
        when(mockDoctype.getSystemIdentifier()).thenReturn(systemIdentifier);
        String baseUri = "testBaseUri";
        when(mockDoctype.baseUri()).thenReturn(baseUri);

        xmlTreeBuilder.insert(mockDoctype);

        verify(xmlTreeBuilder).insertNode(mock(DocumentType.class));
        verify(mockDoctype).getName();
        verify(mockDoctype).getPublicIdentifier();
        verify(mockDoctype).getSystemIdentifier();
        verify(mockDoctype).baseUri();
    }

    @Test
    public void testParseFragment() {
        String inputFragment = "<testInputFragment>";
        String baseUri = "testBaseUri";
        ParseErrorList errors = new ParseErrorList();
        Document mockDoc = mock(Document.class);
        Node mockNode = mock(Node.class);
        when(mockDoc.childNodes()).thenReturn(new ArrayList<>());
        doReturn(mockDoc).when(xmlTreeBuilder).doc;
        when(xmlTreeBuilder.runParser()).thenReturn(null);
        List<Node> expectedChildNodes = new ArrayList<>();
        expectedChildNodes.add(mockNode);
        when(mockDoc.childNodes()).thenReturn(expectedChildNodes);

        List<Node> actualChildNodes = xmlTreeBuilder.parseFragment(inputFragment, baseUri, errors);

        assertEquals(expectedChildNodes, actualChildNodes);
        verify(xmlTreeBuilder).initialiseParse(inputFragment, baseUri, errors);
        verify(xmlTreeBuilder).runParser();
        verify(mockDoc).childNodes();
    }
}