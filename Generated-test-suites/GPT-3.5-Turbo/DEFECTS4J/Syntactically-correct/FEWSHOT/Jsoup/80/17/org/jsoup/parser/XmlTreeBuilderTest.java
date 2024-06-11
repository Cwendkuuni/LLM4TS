package org.jsoup.parser;

import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.jsoup.Jsoup;
import org.jsoup.helper.Validate;
import org.jsoup.nodes.Comment;
import org.jsoup.nodes.DocumentType;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.jsoup.parser.Token.Character;
import org.jsoup.parser.Token.Comment;
import org.jsoup.parser.Token.Doctype;
import org.jsoup.parser.XmlTreeBuilder;
import org.junit.Before;
import org.junit.Test;

public class XmlTreeBuilderTest {
    private XmlTreeBuilder xmlTreeBuilder;

    @Before
    public void setup() {
        xmlTreeBuilder = new XmlTreeBuilder();
    }

    @Test
    public void testDefaultSettings() {
        assertSame(ParseSettings.preserveCase, xmlTreeBuilder.defaultSettings());
    }

    @Test
    public void testParseWithReader() {
        Reader input = mock(Reader.class);
        String baseUri = "http://www.example.com";

        Document doc = mock(Document.class);
        when(xmlTreeBuilder.parse(input, baseUri)).thenReturn(doc);

        Document result = xmlTreeBuilder.parse(input, baseUri);

        assertSame(doc, result);
    }

    @Test
    public void testParseWithString() {
        String input = "<html><body><div></div></body></html>";
        String baseUri = "http://www.example.com";

        Document doc = mock(Document.class);
        when(xmlTreeBuilder.parse(input, baseUri)).thenReturn(doc);

        Document result = xmlTreeBuilder.parse(input, baseUri);

        assertSame(doc, result);
    }

    @Test
    public void testInitialiseParse() {
        Reader input = mock(Reader.class);
        String baseUri = "http://www.example.com";
        ParseErrorList errors = mock(ParseErrorList.class);
        ParseSettings settings = mock(ParseSettings.class);

        xmlTreeBuilder.initialiseParse(input, baseUri, errors, settings);

        assertSame(doc, xmlTreeBuilder.getStack().get(0));
    }

    @Test
    public void testProcessStartTag() {
        Token.StartTag startTag = mock(Token.StartTag.class);
        Tag tag = mock(Tag.class);
        when(startTag.name()).thenReturn("div");
        when(settings.normalizeTag("div")).thenReturn(tag);

        Element el = mock(Element.class);
        when(xmlTreeBuilder.currentElement()).thenReturn(el);

        xmlTreeBuilder.process(startTag);

        verify(el).appendChild(any(Element.class));
    }

    @Test
    public void testProcessEndTag() {
        Token.EndTag endTag = mock(Token.EndTag.class);
        when(endTag.tagName()).thenReturn("div");

        Element el1 = mock(Element.class);
        when(el1.nodeName()).thenReturn("div");
        Element el2 = mock(Element.class);
        when(el2.nodeName()).thenReturn("p");

        List<Element> stack = new ArrayList<>();
        stack.add(el2);
        stack.add(el1);
        when(xmlTreeBuilder.getStack()).thenReturn(stack);

        xmlTreeBuilder.process(endTag);

        assertEquals(1, stack.size());
        assertSame(el2, stack.get(0));
    }

    @Test
    public void testProcessComment() {
        Token.Comment commentToken = mock(Token.Comment.class);
        when(commentToken.getData()).thenReturn("Test Comment");

        xmlTreeBuilder.process(commentToken);

        verify(xmlTreeBuilder).insertNode(any(Comment.class));
    }

    @Test
    public void testProcessCharacter() {
        Token.Character characterToken = mock(Token.Character.class);
        when(characterToken.getData()).thenReturn("Test Character");
        when(characterToken.isCData()).thenReturn(false);

        xmlTreeBuilder.process(characterToken);

        verify(xmlTreeBuilder).insertNode(any(TextNode.class));
    }

    @Test
    public void testProcessDoctype() {
        Token.Doctype doctype = mock(Token.Doctype.class);
        when(doctype.getName()).thenReturn("html");
        when(doctype.getPublicIdentifier()).thenReturn(null);
        when(doctype.getSystemIdentifier()).thenReturn("http://www.example.com");

        xmlTreeBuilder.process(doctype);

        verify(xmlTreeBuilder).insertNode(any(DocumentType.class));
    }

    @Test
    public void testInsertNode() {
        Node node = mock(Node.class);
        Element el = mock(Element.class);
        when(xmlTreeBuilder.currentElement()).thenReturn(el);

        xmlTreeBuilder.insertNode(node);

        verify(el).appendChild(node);
    }

    @Test
    public void testInsertStartTag() {
        Token.StartTag startTag = mock(Token.StartTag.class);
        Tag tag = mock(Tag.class);
        Attributes attributes = mock(Attributes.class);
        when(startTag.name()).thenReturn("div");
        when(settings.normalizeTag("div")).thenReturn(tag);
        when(startTag.attributes()).thenReturn(attributes);

        Element el = xmlTreeBuilder.insert(startTag);

        verify(attributes).normalize(settings);
        verify(attributes).addAll(any(Attributes.class));
        verify(xmlTreeBuilder).insertNode(any(Element.class));
        assertSame(el, xmlTreeBuilder.currentElement());
    }

    @Test
    public void testInsertComment() {
        Token.Comment commentToken = mock(Token.Comment.class);
        when(commentToken.getData()).thenReturn("Test Comment");

        xmlTreeBuilder.insert(commentToken);

        verify(xmlTreeBuilder).insertNode(any(Comment.class));
    }

    @Test
    public void testInsertCharacter() {
        Token.Character characterToken = mock(Token.Character.class);
        when(characterToken.getData()).thenReturn("Test Character");

        xmlTreeBuilder.insert(characterToken);

        verify(xmlTreeBuilder, times(1)).insertNode(any(TextNode.class));
    }

    @Test
    public void testInsertDoctype() {
        Token.Doctype doctype = mock(Token.Doctype.class);
        when(doctype.getName()).thenReturn("html");
        when(doctype.getPublicIdentifier()).thenReturn(null);
        when(doctype.getSystemIdentifier()).thenReturn("http://www.example.com");

        xmlTreeBuilder.insert(doctype);

        verify(xmlTreeBuilder, times(1)).insertNode(any(DocumentType.class));
    }

    @Test
    public void testPopStackToClose() {
        Token.EndTag endTag = mock(Token.EndTag.class);
        when(endTag.tagName()).thenReturn("div");

        Element el1 = mock(Element.class);
        when(el1.nodeName()).thenReturn("div");
        Element el2 = mock(Element.class);
        when(el2.nodeName()).thenReturn("p");

        List<Element> stack = new ArrayList<>();
        stack.add(el2);
        stack.add(el1);
        when(xmlTreeBuilder.getStack()).thenReturn(stack);

        xmlTreeBuilder.popStackToClose(endTag);

        assertEquals(1, stack.size());
        assertSame(el2, stack.get(0));
    }

    @Test
    public void testParseFragment() {
        String inputFragment = "<div></div>";
        String baseUri = "http://www.example.com";
        ParseErrorList errors = ParseErrorList.noTracking();
        when(doc.childNodes()).thenReturn(new ArrayList<>());

        List<Node> result = xmlTreeBuilder.parseFragment(inputFragment, baseUri, errors);
        
        assertEquals(0, errors.size());
        assertEquals(0, result.size());
    }
}