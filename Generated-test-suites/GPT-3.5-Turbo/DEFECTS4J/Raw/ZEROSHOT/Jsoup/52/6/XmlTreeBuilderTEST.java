package org.jsoup.parser;

import org.jsoup.helper.Validate;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(PowerMockRunner.class)
@PrepareForTest({TreeBuilder.class, Node.class, Document.class})

public class XmlTreeBuilderTest {

    private XmlTreeBuilder xmlTreeBuilder;
    private Token mockToken;
    private Element mockElement;
    private Node mockNode;
    private Document mockDocument;
    private Document.OutputSettings mockOutputSettings;
    private List mockStack;
    private Tag mockTag;
    private Token.StartTag mockStartTag;
    private Token.EndTag mockEndTag;
    private Token.Comment mockComment;
    private Token.Character mockCharacter;
    private Token.Doctype mockDoctype;
    private ParseErrorList mockErrorList;

    @Before
    public void setUp() {
        xmlTreeBuilder = new XmlTreeBuilder();
        mockToken = mock(Token.class);
        mockElement = mock(Element.class);
        mockNode = mock(Node.class);
        mockDocument = PowerMockito.mock(Document.class);
        mockOutputSettings = mock(Document.OutputSettings.class);
        mockStack = PowerMockito.mock(ArrayList.class);
        mockTag = PowerMockito.mock(Tag.class);
        mockStartTag = mock(Token.StartTag.class);
        mockEndTag = mock(Token.EndTag.class);
        mockComment = mock(Token.Comment.class);
        mockCharacter = mock(Token.Character.class);
        mockDoctype = mock(Token.Doctype.class);
        mockErrorList = mock(ParseErrorList.class);

        when(mockToken.type).thenReturn(Token.TokenType.StartTag);

        try {
            PowerMockito.whenNew(Document.class).withAnyArguments().thenReturn(mockDocument);
        } catch (Exception e) {
            e.printStackTrace();
        }

        when(mockTag.isKnownTag()).thenReturn(true);
    }

    @Test
    public void initialiseParse_should_call_super_initialiseParse() {
        String input = "input";
        String baseUri = "baseUri";

        xmlTreeBuilder.initialiseParse(input, baseUri, mockErrorList);

        verify((TreeBuilder)xmlTreeBuilder).initialiseParse(input, baseUri, mockErrorList);
    }

    @Test
    public void initialiseParse_should_add_document_to_stack() {
        String input = "input";
        String baseUri = "baseUri";

        xmlTreeBuilder.initialiseParse(input, baseUri, mockErrorList);

        verify(mockStack).add(mockDocument);
    }

    @Test
    public void initialiseParse_should_set_xml_syntax() {
        String input = "input";
        String baseUri = "baseUri";

        xmlTreeBuilder.initialiseParse(input, baseUri, mockErrorList);

        verify(mockOutputSettings).syntax(Document.OutputSettings.Syntax.xml);
    }

    @Test
    public void process_should_call_insert_for_start_tag_token() {
        when(mockToken.type).thenReturn(Token.TokenType.StartTag);
        when(mockToken.asStartTag()).thenReturn(mockStartTag);

        xmlTreeBuilder.process(mockToken);

        verify(xmlTreeBuilder).insert(mockStartTag);
    }

    @Test
    public void process_should_call_popStackToClose_for_end_tag_token() {
        when(mockToken.type).thenReturn(Token.TokenType.EndTag);
        when(mockToken.asEndTag()).thenReturn(mockEndTag);

        xmlTreeBuilder.process(mockToken);

        verify(xmlTreeBuilder).popStackToClose(mockEndTag);
    }

    @Test
    public void process_should_call_insert_for_comment_token() {
        when(mockToken.type).thenReturn(Token.TokenType.Comment);
        when(mockToken.asComment()).thenReturn(mockComment);

        xmlTreeBuilder.process(mockToken);

        verify(xmlTreeBuilder).insert(mockComment);
    }

    @Test
    public void process_should_call_insert_for_character_token() {
        when(mockToken.type).thenReturn(Token.TokenType.Character);
        when(mockToken.asCharacter()).thenReturn(mockCharacter);

        xmlTreeBuilder.process(mockToken);

        verify(xmlTreeBuilder).insert(mockCharacter);
    }

    @Test
    public void process_should_call_insert_for_doctype_token() {
        when(mockToken.type).thenReturn(Token.TokenType.Doctype);
        when(mockToken.asDoctype()).thenReturn(mockDoctype);

        xmlTreeBuilder.process(mockToken);

        verify(xmlTreeBuilder).insert(mockDoctype);
    }

    @Test
    public void process_should_fail_for_unknown_token() {
        when(mockToken.type).thenReturn(Token.TokenType.Unknown);

        try {
            xmlTreeBuilder.process(mockToken);
            fail("Expected exception not thrown");
        } catch (Exception ex) {
            assertEquals("Unexpected token type: " + mockToken.type, ex.getMessage());
        }
    }

    @Test
    public void insertNode_should_call_appendChild_on_current_element() {
        when(xmlTreeBuilder.currentElement()).thenReturn(mockElement);

        xmlTreeBuilder.insertNode(mockNode);

        verify(mockElement).appendChild(mockNode);
    }

    @Test
    public void insert_should_call_insertNode_with_comment_when_comment_is_bogus() {
        String data = "!xmlDeclaration";
        when(mockComment.getData()).thenReturn(data);
        when(mockComment.getData()).thenReturn(data);
        when(mockComment.baseUri()).thenReturn("baseUri");
        when(mockComment.getData()).thenReturn(data);
        when(mockComment.getData()).thenReturn(data);
        when(mockComment.bogus).thenReturn(true);

        xmlTreeBuilder.insert(mockComment);

        verify(xmlTreeBuilder).insertNode(any(Comment.class));
    }

    @Test
    public void insert_should_not_call_insertNode_with_comment_when_comment_is_not_bogus() {
        String data = "data";
        when(mockComment.getData()).thenReturn(data);
        when(mockComment.getData()).thenReturn(data);
        when(mockComment.baseUri()).thenReturn("baseUri");
        when(mockComment.getData()).thenReturn(data);
        when(mockComment.getData()).thenReturn(data);
        when(mockComment.bogus).thenReturn(false);

        xmlTreeBuilder.insert(mockComment);

        verify(xmlTreeBuilder, times(0)).insertNode(any(Comment.class));
    }

    @Test
    public void insert_should_call_insertNode_with_text_node_when_comment_is_not_bogus() {
        String data = "data";
        when(mockCharacter.getData()).thenReturn(data);

        xmlTreeBuilder.insert(mockCharacter);

        verify(xmlTreeBuilder).insertNode(any(TextNode.class));
    }

    @Test
    public void insert_should_call_insertNode_with_doctype_when_comment_is_not_bogus() {
        when(mockDoctype.getName()).thenReturn("name");
        when(mockDoctype.getPublicIdentifier()).thenReturn("publicIdentifier");
        when(mockDoctype.getSystemIdentifier()).thenReturn("systemIdentifier");

        xmlTreeBuilder.insert(mockDoctype);

        verify(xmlTreeBuilder).insertNode(any(DocumentType.class));
    }

    @Test
    public void popStackToClose_should_remove_element_from_stack() {
        Element firstFound = mock(Element.class);
        when(firstFound.nodeName()).thenReturn("nodeName");
        when(mockEndTag.name()).thenReturn("nodeName");
        when(mockStack.size()).thenReturn(1);
        when(mockStack.get(0)).thenReturn(firstFound);

        xmlTreeBuilder.popStackToClose(mockEndTag);

        verify(mockStack, times(2)).remove(anyInt());
    }

    @Test
    public void popStackToClose_should_not_remove_any_element_from_stack() {
        when(mockEndTag.name()).thenReturn("nodeName");
        when(mockStack.size()).thenReturn(0);

        xmlTreeBuilder.popStackToClose(mockEndTag);

        verify(mockStack, times(0)).remove(anyInt());
    }

    @Test
    public void parseFragment_should_call_initialiseParse_with_inputFragment_baseUri_and_errors_parameters() {
        String inputFragment = "inputFragment";
        String baseUri = "baseUri";

        xmlTreeBuilder.parseFragment(inputFragment, baseUri, mockErrorList);

        verify(xmlTreeBuilder).initialiseParse(inputFragment, baseUri, mockErrorList);
    }

    @Test
    public void parseFragment_should_call_runParser() {
        String inputFragment = "inputFragment";
        String baseUri = "baseUri";

        xmlTreeBuilder.parseFragment(inputFragment, baseUri, mockErrorList);

        verify(xmlTreeBuilder).runParser();
    }

    @Test
    public void parseFragment_should_return_doc_childNodes() {
        String inputFragment = "inputFragment";
        String baseUri = "baseUri";
        List<Node> childNodes = new ArrayList<>();

        when(mockDocument.childNodes()).thenReturn(childNodes);

        List<Node> result = xmlTreeBuilder.parseFragment(inputFragment, baseUri, mockErrorList);

        assertEquals(childNodes, result);
    }

    @Test
    public void insert_should_return_newly_created_element() {
        String tagName = "tagName";
        String baseUri = "baseUri";

        when(mockToken.name()).thenReturn(tagName);
        when(mockStartTag.attributes).thenReturn(mockAttributes);
        when(mockTag.isKnownTag()).thenReturn(false);

        Element result = xmlTreeBuilder.insert(mockStartTag);

        assertNotNull(result);
        assertEquals(tagName, result.tagName());
    }
}