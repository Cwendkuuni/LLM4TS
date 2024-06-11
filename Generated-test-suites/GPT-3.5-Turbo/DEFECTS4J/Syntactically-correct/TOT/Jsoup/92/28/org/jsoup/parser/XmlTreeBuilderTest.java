import org.junit.Before;
import org.junit.Test;

import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import org.jsoup.helper.Validate;
import org.jsoup.nodes.*;
import org.jsoup.parser.*;
import org.jsoup.select.Selector;

import java.io.Reader;
import java.io.StringReader;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class XmlTreeBuilderTest {

    @Mock
    private Parser mockParser;
    private XmlTreeBuilder xmlTreeBuilder;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        xmlTreeBuilder = Mockito.spy(new XmlTreeBuilder());
    }

    @Test
    public void defaultSettings_ShouldReturnPreserveCaseSettings() {
        ParseSettings settings = xmlTreeBuilder.defaultSettings();
        assertEquals(ParseSettings.preserveCase, settings);
    }

    @Test
    public void initialiseParse_ShouldAddDocumentToStack() {
        Reader input = new StringReader("");
        String baseUri = "";
        xmlTreeBuilder.initialiseParse(input, baseUri, mockParser);

        assertTrue(xmlTreeBuilder.stack.contains(xmlTreeBuilder.doc));
    }

    @Test
    public void initialiseParse_ShouldSetSyntaxToXml() {
        Reader input = new StringReader("");
        String baseUri = "";
        xmlTreeBuilder.initialiseParse(input, baseUri, mockParser);

        assertEquals(Document.OutputSettings.Syntax.xml, xmlTreeBuilder.doc.outputSettings().syntax());
    }

    @Test
    public void parse_ShouldCallParseWithReaderAndBaseUriAndParser() {
        Reader input = new StringReader("");
        String baseUri = "";
        xmlTreeBuilder.parse(input, baseUri);

        verify(xmlTreeBuilder).parse(input, baseUri, mockParser);
    }

    @Test
    public void parse_ShouldCallParseWithStringAndBaseUriAndParser() {
        String input = "";
        String baseUri = "";
        xmlTreeBuilder.parse(input, baseUri);

        verify(xmlTreeBuilder).parse(any(StringReader.class), eq(baseUri), eq(mockParser));
    }

    @Test
    public void process_ShouldCallInsertForStartTag() {
        Token token = new Token.StartTag("");
        xmlTreeBuilder.process(token);

        verify(xmlTreeBuilder).insert(token.asStartTag());
    }

    @Test
    public void process_ShouldCallPopStackToCloseForEndTag() {
        Token token = new Token.EndTag("");
        xmlTreeBuilder.process(token);

        verify(xmlTreeBuilder).popStackToClose(token.asEndTag());
    }

    @Test
    public void process_ShouldCallInsertForComment() {
        Token token = new Token.Comment("");
        xmlTreeBuilder.process(token);

        verify(xmlTreeBuilder).insert(token.asComment());
    }

    @Test
    public void process_ShouldCallInsertForCharacter() {
        Token token = new Token.Character("");
        xmlTreeBuilder.process(token);

        verify(xmlTreeBuilder).insert(token.asCharacter());
    }

    @Test
    public void process_ShouldCallInsertForDoctype() {
        Token token = new Token.Doctype("");
        xmlTreeBuilder.process(token);

        verify(xmlTreeBuilder).insert(token.asDoctype());
    }

    @Test(expected = IllegalArgumentException.class)
    public void process_ShouldFailForUnexpectedTokenType() {
        Token token = new Token(TokenType.EOF);
        xmlTreeBuilder.process(token);
    }

    @Test
    public void insertNode_ShouldAppendNodeToCurrentElement() {
        Node node = Mockito.mock(Node.class);

        doNothing().when(xmlTreeBuilder).currentElement();
        xmlTreeBuilder.insertNode(node);

        verify(xmlTreeBuilder.currentElement()).appendChild(node);
    }

    @Test
    public void insert_ShouldCallInsertNodeWithCreatedElement() {
        Tag tag = Mockito.mock(Tag.class);
        Attribute attribute = Mockito.mock(Attribute.class);
        Token.StartTag startTag = new Token.StartTag("element").attributes(Arrays.asList(attribute));
        Element element = Mockito.mock(Element.class);

        when(Mockito.spy(Tag.valueOf(startTag.name(), xmlTreeBuilder.settings))).thenReturn(tag);
        when(xmlTreeBuilder.settings.normalizeAttributes(startTag.attributes)).thenReturn(Arrays.asList(attribute));
        doReturn(element).when(xmlTreeBuilder).insertNode(Mockito.any(Node.class));

        Element result = xmlTreeBuilder.insert(startTag);

        verify(xmlTreeBuilder).insertNode(element);
        assertEquals(element, result);
    }

    @Test
    public void insert_ShouldSetTagSelfClosingIfUnknownTag() {
        Tag tag = Mockito.spy(Tag.valueOf("unknown", xmlTreeBuilder.settings));
        Token.StartTag startTag = new Token.StartTag("unknown");

        doReturn(tag).when(Mockito.spy(Tag.valueOf(startTag.name(), xmlTreeBuilder.settings))).thenReturn(tag);

        Element result = xmlTreeBuilder.insert(startTag);

        verify(tag).setSelfClosing();
    }

    @Test
    public void insert_ShouldAddElementToStackIfNotSelfClosing() {
        Tag tag = Mockito.mock(Tag.class);
        Token.StartTag startTag = new Token.StartTag("element");

        when(Mockito.spy(Tag.valueOf(startTag.name(), xmlTreeBuilder.settings))).thenReturn(tag);

        Element result = xmlTreeBuilder.insert(startTag);

        assertTrue(xmlTreeBuilder.stack.contains(result));
    }

    @Test
    public void insert_ShouldReturnElementIfSelfClosing() {
        Tag tag = Mockito.spy(Tag.valueOf("selfClosing", xmlTreeBuilder.settings));
        Token.StartTag startTag = new Token.StartTag("selfClosing").selfClosing();

        doReturn(tag).when(Mockito.spy(Tag.valueOf(startTag.name(), xmlTreeBuilder.settings))).thenReturn(tag);

        Element result = xmlTreeBuilder.insert(startTag);

        assertEquals(tag, result.tag());
    }

    @Test
    public void insert_ShouldInsertCommentIfBogusAndXmlDeclaration() {
        Token.Comment commentToken = new Token.Comment("<![CDATA[XML declaration]]>");

        doNothing().when(xmlTreeBuilder).insertNode(any(Node.class));
        doReturn(true).when(commentToken).bogus();
        doReturn(true).when(commentToken).isXmlDeclaration();

        xmlTreeBuilder.insert(commentToken);

        verify(xmlTreeBuilder).insertNode(any(XmlDeclaration.class));
    }

    @Test
    public void insert_ShouldInsertCommentIfNotBogus() {
        String commentData = "This is a comment";
        Token.Comment commentToken = new Token.Comment(commentData);

        doNothing().when(xmlTreeBuilder).insertNode(any(Node.class));
        doReturn(false).when(commentToken).bogus();

        xmlTreeBuilder.insert(commentToken);

        verify(xmlTreeBuilder).insertNode(any(Comment.class));
    }

    @Test
    public void insert_ShouldInsertTextIfCharacterIsCData() {
        String characterData = "This is a CDATA section";
        Token.Character characterToken = new Token.Character(characterData, true);

        doNothing().when(xmlTreeBuilder).insertNode(any(Node.class));

        xmlTreeBuilder.insert(characterToken);

        verify(xmlTreeBuilder).insertNode(any(CDataNode.class));
    }

    @Test
    public void insert_ShouldInsertTextNodeIfCharacterIsNotCData() {
        String characterData = "This is a character node";
        Token.Character characterToken = new Token.Character(characterData, false);

        doNothing().when(xmlTreeBuilder).insertNode(any(Node.class));

        xmlTreeBuilder.insert(characterToken);

        verify(xmlTreeBuilder).insertNode(any(TextNode.class));
    }

    @Test
    public void insert_ShouldInsertDoctype() {
        String doctypeName = "html";
        Token.Doctype doctypeToken = new Token.Doctype(doctypeName, "", "");

        doNothing().when(xmlTreeBuilder).insertNode(any(Node.class));

        xmlTreeBuilder.insert(doctypeToken);

        verify(xmlTreeBuilder).insertNode(any(DocumentType.class));
    }

    @Test
    public void popStackToClose_ShouldPopElementsUntilFirstOccurrenceOfTagName() {
        String endTagName = "tag";
        Token.EndTag endTagToken = new Token.EndTag(endTagName);
        Element element1 = Mockito.mock(Element.class);
        Element element2 = Mockito.mock(Element.class);
        Element element3 = Mockito.mock(Element.class);

        when(element1.nodeName()).thenReturn("element1");
        when(element2.nodeName()).thenReturn(endTagName);
        when(Mockito.spy(element2.nodeName())).thenReturn(endTagName);
        when(element3.nodeName()).thenReturn("element3");

        xmlTreeBuilder.stack = Arrays.asList(element1, element2, element3);
        xmlTreeBuilder.popStackToClose(endTagToken);

        assertFalse(xmlTreeBuilder.stack.contains(element2));
        assertTrue(xmlTreeBuilder.stack.contains(element3));
    }

    @Test
    public void popStackToClose_ShouldNotPopElementsIfTagNameNotFound() {
        String endTagName = "tag";
        Token.EndTag endTagToken = new Token.EndTag(endTagName);
        Element element1 = Mockito.mock(Element.class);
        Element element2 = Mockito.mock(Element.class);
        Element element3 = Mockito.mock(Element.class);

        when(element1.nodeName()).thenReturn("element1");
        when(Mockito.spy(element1.nodeName())).thenReturn("element1");
        when(Mockito.spy(element2.nodeName())).thenReturn("element2");
        when(Mockito.spy(element3.nodeName())).thenReturn("element3");

        xmlTreeBuilder.stack = Arrays.asList(element1, element2, element3);
        xmlTreeBuilder.popStackToClose(endTagToken);

        assertTrue(xmlTreeBuilder.stack.contains(element1));
        assertTrue(xmlTreeBuilder.stack.contains(element2));
        assertTrue(xmlTreeBuilder.stack.contains(element3));
    }

    @Test
    public void parseFragment_ShouldInitialiseParseWithInputFragmentAndBaseUriAndParser() {
        String inputFragment = "";
        String baseUri = "";
        Parser parser = Mockito.mock(Parser.class);

        initParseFragmentMocks(inputFragment, baseUri, parser);
        xmlTreeBuilder.parseFragment(inputFragment, baseUri, parser);

        verify(xmlTreeBuilder).initialiseParse(any(StringReader.class), eq(baseUri), eq(parser));
    }

    @Test
    public void parseFragment_ShouldRunParser() {
        String inputFragment = "";
        String baseUri = "";
        Parser parser = Mockito.mock(Parser.class);
        InOrder inOrder = inOrder(xmlTreeBuilder);

        initParseFragmentMocks(inputFragment, baseUri, parser);
        xmlTreeBuilder.parseFragment(inputFragment, baseUri, parser);

        inOrder.verify(xmlTreeBuilder).runParser();
    }

    @Test
    public void parseFragment_ShouldReturnDocumentChildNodes() {
        String inputFragment = "";
        String baseUri = "";
        Parser parser = Mockito.mock(Parser.class);
        List<Node> childNodes = Arrays.asList(Mockito.mock(Node.class));

        initParseFragmentMocks(inputFragment, baseUri, parser);
        when(xmlTreeBuilder.doc.childNodes()).thenReturn(childNodes);

        List<Node> result = xmlTreeBuilder.parseFragment(inputFragment, baseUri, parser);

        assertEquals(childNodes, result);
    }

    @Test
    public void parseFragmentWithElement_ShouldCallParseFragment() {
        String inputFragment = "";
        Element context = Mockito.mock(Element.class);
        String baseUri = "";
        Parser parser = Mockito.mock(Parser.class);

        initParseFragmentMocks(inputFragment, baseUri, parser);
        xmlTreeBuilder.parseFragment(inputFragment, context, baseUri, parser);

        verify(xmlTreeBuilder).parseFragment(inputFragment, baseUri, parser);
    }

    private void initParseFragmentMocks(String inputFragment, String baseUri, Parser parser) {
        Mockito.doCallRealMethod().when(xmlTreeBuilder).parseFragment(inputFragment, baseUri);
        Mockito.doCallRealMethod().when(xmlTreeBuilder).parseFragment(inputFragment, baseUri, parser);
        Mockito.when(xmlTreeBuilder.doc.childNodes()).thenReturn(Arrays.asList(Mockito.mock(Node.class)));
    }
}