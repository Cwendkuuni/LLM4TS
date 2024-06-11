package org.jsoup.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;

import java.util.List;

public class XmlTreeBuilderTest {

    @Mock
    private Token.StartTag startTagMock;
    @Mock
    private Token.EndTag endTagMock;
    @Mock
    private Token.Comment commentMock;
    @Mock
    private Token.Character characterMock;
    @Mock
    private Token.Doctype doctypeMock;

    @Spy
    private TreeBuilderSettings settingsSpy;

    private XmlTreeBuilder xmlTreeBuilder;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        xmlTreeBuilder = spy(new XmlTreeBuilder());
    }

    @Test
    public void testDefaultSettings() {
        ParseSettings defaultSettings = xmlTreeBuilder.defaultSettings();

        assertEquals(ParseSettings.preserveCase, defaultSettings);
    }

    @Test
    public void testParse() {
        String input = "<html></html>";
        String baseUri = "http://example.com";
        Document documentMock = mock(Document.class);

        when(xmlTreeBuilder.parse(input, baseUri, ParseErrorList.noTracking(), ParseSettings.preserveCase)).thenReturn(documentMock);

        Document document = xmlTreeBuilder.parse(input, baseUri);

        assertEquals(documentMock, document);
        verify(xmlTreeBuilder).parse(input, baseUri, ParseErrorList.noTracking(), ParseSettings.preserveCase);
    }

    @Test
    public void testInitialiseParse() {
        String input = "<html></html>";
        String baseUri = "http://example.com";
        ParseErrorList errorsMock = mock(ParseErrorList.class);
        ParseSettings settings = ParseSettings.preserveCase;

        xmlTreeBuilder.initialiseParse(input, baseUri, errorsMock, settings);

        verify(xmlTreeBuilder).super.initialiseParse(input, baseUri, errorsMock, settings);
        verify(xmlTreeBuilder).stack.add(any(Document.class));
        verify(xmlTreeBuilder.doc).outputSettings().syntax(Document.OutputSettings.Syntax.xml);
    }

    @Test
    public void testProcess_StartTag() {
        xmlTreeBuilder.process(startTagMock);

        verify(xmlTreeBuilder).insert(startTagMock.asStartTag());
    }

    @Test
    public void testProcess_EndTag() {
        xmlTreeBuilder.process(endTagMock);

        verify(xmlTreeBuilder).popStackToClose(endTagMock.asEndTag());
    }

    @Test
    public void testProcess_Comment() {
        xmlTreeBuilder.process(commentMock);

        verify(xmlTreeBuilder).insert(commentMock.asComment());
    }

    @Test
    public void testProcess_Character() {
        xmlTreeBuilder.process(characterMock);

        verify(xmlTreeBuilder).insert(characterMock.asCharacter());
    }

    @Test
    public void testProcess_Doctype() {
        xmlTreeBuilder.process(doctypeMock);

        verify(xmlTreeBuilder).insert(doctypeMock.asDoctype());
    }

    @Test
    public void testInsertNode() {
        Node nodeMock = mock(Node.class);
        Element currentElementMock = mock(Element.class);

        when(xmlTreeBuilder.currentElement()).thenReturn(currentElementMock);

        xmlTreeBuilder.insertNode(nodeMock);

        verify(currentElementMock).appendChild(nodeMock);
    }

    @Test
    public void testInsert_StartTag() {
        Token.StartTag startTag = new Token.StartTag("tag");
        startTag.attributes().put("attr", "value");
        Tag tagMock = mock(Tag.class);
        Element elementMock = mock(Element.class);

        when(startTagMock.name()).thenReturn("tag");
        when(xmlTreeBuilder.settings).thenReturn(settingsSpy);
        when(settingsSpy.normalizeAttributes(startTag.attributes())).thenReturn(startTag.attributes());
        when(Tag.valueOf(startTag.name(), settingsSpy)).thenReturn(tagMock);
        when(xmlTreeBuilder.currentElement()).thenReturn(elementMock);

        Element element = xmlTreeBuilder.insert(startTag);

        assertEquals(elementMock, element);
        verify(tagMock).setSelfClosing();
        verify(xmlTreeBuilder.tokeniser).acknowledgeSelfClosingFlag();
        verify(settingsSpy).normalizeAttributes(startTag.attributes());
        verify(elementMock).appendElement(startTag.name());
    }

    @Test
    public void testInsert_Comment() {
        Token.Comment comment = new Token.Comment("comment");
        Comment commentMock = mock(Comment.class);

        when(commentMock.getData()).thenReturn("comment");
        when(xmlTreeBuilder.baseUri).thenReturn("http://example.com");
        when(xmlTreeBuilder.insertNode(commentMock)).thenReturn(null);

        xmlTreeBuilder.insert(comment);

        verify(xmlTreeBuilder).insertNode(commentMock);
    }

    @Test
    public void testInsert_Character() {
        Token.Character character = new Token.Character("character");
        TextNode textNodeMock = mock(TextNode.class);

        when(characterMock.getData()).thenReturn("character");
        when(xmlTreeBuilder.baseUri).thenReturn("http://example.com");
        when(xmlTreeBuilder.insertNode(textNodeMock)).thenReturn(null);

        xmlTreeBuilder.insert(character);

        verify(xmlTreeBuilder).insertNode(textNodeMock);
    }

    @Test
    public void testInsert_Doctype() {
        Token.Doctype doctype = new Token.Doctype("doctype");
        DocumentType documentTypeMock = mock(DocumentType.class);

        when(doctypeMock.getName()).thenReturn("doctype");
        when(xmlTreeBuilder.baseUri).thenReturn("http://example.com");
        when(xmlTreeBuilder.insertNode(documentTypeMock)).thenReturn(null);

        xmlTreeBuilder.insert(doctype);

        verify(xmlTreeBuilder).insertNode(documentTypeMock);
    }

    @Test
    public void testPopStackToClose() {
        Token.EndTag endTag = new Token.EndTag("tag");
        Element elementMock = mock(Element.class);

        when(endTagMock.name()).thenReturn("tag");
        when(xmlTreeBuilder.stack.size()).thenReturn(2);
        when(xmlTreeBuilder.stack.get(1)).thenReturn(elementMock);

        xmlTreeBuilder.popStackToClose(endTag);

        verify(xmlTreeBuilder.stack).remove(1);
    }

    @Test
    public void testParseFragment() {
        String inputFragment = "<div></div>";
        String baseUri = "http://example.com";
        ParseErrorList errorsMock = mock(ParseErrorList.class);
        List<Node> nodesMock = mock(List.class);

        when(xmlTreeBuilder.initialiseParse(inputFragment, baseUri, errorsMock, ParseSettings.preserveCase)).thenReturn(null);
        when(xmlTreeBuilder.runParser()).thenReturn(null);
        when(xmlTreeBuilder.doc).thenReturn(null);
        when(xmlTreeBuilder.doc.childNodes()).thenReturn(nodesMock);

        List<Node> nodes = xmlTreeBuilder.parseFragment(inputFragment, baseUri, errorsMock, ParseSettings.preserveCase);

        assertEquals(nodesMock, nodes);
        verify(xmlTreeBuilder).initialiseParse(inputFragment, baseUri, errorsMock, ParseSettings.preserveCase);
        verify(xmlTreeBuilder).runParser();
        verify(xmlTreeBuilder.doc).childNodes();
    }
}