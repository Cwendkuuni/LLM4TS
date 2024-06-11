package org.jsoup.parser;

import org.junit.Test;
import org.junit.Before;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.jsoup.Jsoup;
import org.jsoup.helper.Validate;
import org.jsoup.nodes.*;
import java.util.List;

public class XmlTreeBuilderTest {
    private XmlTreeBuilder xmlTreeBuilder;
    private ParseSettings parseSettings;
    private Document doc;
    private Token token;

    @Before
    public void setUp() {
        xmlTreeBuilder = new XmlTreeBuilder();
        parseSettings = ParseSettings.preserveCase;
        doc = mock(Document.class);
        token = mock(Token.class);
    }

    @Test
    public void testDefaultSettings() {
        ParseSettings defaultSettings = xmlTreeBuilder.defaultSettings();
        assertEquals(parseSettings, defaultSettings);
    }

    @Test
    public void testParse() {
        String input = "input";
        String baseUri = "baseUri";
        Document parsedDocument = xmlTreeBuilder.parse(input, baseUri);
        verify(xmlTreeBuilder).parse(input, baseUri, ParseErrorList.noTracking(), ParseSettings.preserveCase);
        assertEquals(doc, parsedDocument);
    }

    @Test
    public void testInitialiseParse() {
        String input = "input";
        String baseUri = "baseUri";
        ParseErrorList parseErrorList = mock(ParseErrorList.class);
        ParseSettings settings = ParseSettings.preserveCase;
        xmlTreeBuilder.initialiseParse(input, baseUri, parseErrorList, settings);
        verify(xmlTreeBuilder).initialiseParse(input, baseUri, parseErrorList, settings);
        verify(xmlTreeBuilder).super.initialiseParse(input, baseUri, parseErrorList, settings);
        verify(xmlTreeBuilder).stack.add(doc);
        verify(doc).outputSettings().syntax(Document.OutputSettings.Syntax.xml);
    }

    @Test
    public void testProcess() {
        xmlTreeBuilder.process(token);
        verify(xmlTreeBuilder).process(token);
        verify(xmlTreeBuilder).insert(token.asStartTag());
        verify(xmlTreeBuilder).popStackToClose(token.asEndTag());
        verify(xmlTreeBuilder).insert(token.asComment());
        verify(xmlTreeBuilder).insert(token.asCharacter());
        verify(xmlTreeBuilder).insert(token.asDoctype());
    }

    @Test
    public void testInsertNode() {
        Node node = mock(Node.class);
        Element currentElement = mock(Element.class);
        when(xmlTreeBuilder.currentElement()).thenReturn(currentElement);
        xmlTreeBuilder.insertNode(node);
        verify(xmlTreeBuilder).insertNode(node);
        verify(currentElement).appendChild(node);
    }

    @Test
    public void testInsertStartTag() {
        Token.StartTag startTag = mock(Token.StartTag.class);
        Tag tag = mock(Tag.class);
        Element el = mock(Element.class);

        when(startTag.name()).thenReturn("name");
        when(tag.valueOf(startTag.name(), parseSettings)).thenReturn(tag);
        when(tag.isKnownTag()).thenReturn(true);
        when(tag.isSelfClosing()).thenReturn(false);
        when(tag.isKnownTag()).thenReturn(true);
        
        when(xmlTreeBuilder.insert(startTag)).thenCallRealMethod();
        when(xmlTreeBuilder.baseUri).thenReturn("baseUri");
        when(xmlTreeBuilder.settings).thenReturn(parseSettings);
        when(xmlTreeBuilder.tokeniser).thenReturn(tokeniser);

        when(xmlTreeBuilder.currentElement()).thenReturn(currentElement);
        when(currentElement.getTagName()).thenReturn("tag");

        when(xmlTreeBuilder.tokeniser.acknowledgeSelfClosingFlag()).thenReturn(true);
        
        when(xmlTreeBuilder.settings.normalizeTag(el.tagName())).thenReturn("normalizedTag");
        
        xmlTreeBuilder.insert(startTag);

        verify(xmlTreeBuilder).insert(startTag);
        verify(xmlTreeBuilder).insertNode(el);
        verify(xmlTreeBuilder.tokeniser).acknowledgeSelfClosingFlag();
        verify(tag, never()).setSelfClosing();
        verify(xmlTreeBuilder.stack).add(el);
    }

    @Test
    public void testInsertComment() {
        Token.Comment commentToken = mock(Token.Comment.class);
        Comment comment = mock(Comment.class);
        Node insert = mock(Node.class);
        Document doc = mock(Document.class);

        when(commentToken.getData()).thenReturn("data");
        when(commentToken.getBogus()).thenReturn(true);
        when(commentToken.getData()).thenReturn("data");
        when(Jsoup.parse("<" + "data" + ">", baseUri, Parser.xmlParser())).thenReturn(doc);
        when(doc.child(0)).thenReturn(el);

        when(xmlTreeBuilder.insert(commentToken)).thenCallRealMethod();
        when(xmlTreeBuilder.tokeniser).thenReturn(tokeniser);
        when(xmlTreeBuilder.baseUri).thenReturn("baseUri");
        
        xmlTreeBuilder.insert(commentToken);

        verify(xmlTreeBuilder).insert(commentToken);
        verify(xmlTreeBuilder).insertNode(insert);
    }

    @Test
    public void testInsertCharacter() {
        Token.Character characterToken = mock(Token.Character.class);
        Node node = mock(Node.class);

        when(characterToken.getData()).thenReturn("data");

        when(xmlTreeBuilder.insert(characterToken)).thenCallRealMethod();
        when(xmlTreeBuilder.baseUri).thenReturn("baseUri");

        xmlTreeBuilder.insert(characterToken);

        verify(xmlTreeBuilder).insert(characterToken);
        verify(xmlTreeBuilder).insertNode(node);
    }

    @Test
    public void testInsertDoctype() {
        Token.Doctype d = mock(Token.Doctype.class);
        DocumentType doctypeNode = mock(DocumentType.class);

        when(d.getName()).thenReturn("name");
        when(xmlTreeBuilder.insert(d)).thenCallRealMethod();
        when(xmlTreeBuilder.baseUri).thenReturn("baseUri");

        xmlTreeBuilder.insert(d);

        verify(xmlTreeBuilder).insert(d);
        verify(xmlTreeBuilder).insertNode(doctypeNode);
    }

    @Test
    public void testPopStackToClose() {
        Token.EndTag endTag = mock(Token.EndTag.class);
        Element element = mock(Element.class);
        when(endTag.name()).thenReturn("name");
        when(xmlTreeBuilder.popStackToClose(endTag)).thenCallRealMethod();
        when(xmlTreeBuilder.stack.size()).thenReturn(1);
        when(xmlTreeBuilder.stack.get(0)).thenReturn(element);
        when(element.nodeName()).thenReturn("nodeName");
        when(xmlTreeBuilder.stack.get(0)).thenReturn(element);

        xmlTreeBuilder.popStackToClose(endTag);

        verify(xmlTreeBuilder).popStackToClose(endTag);
        verify(xmlTreeBuilder.stack).remove(0);
    }

    @Test
    public void testParseFragment() {
        String inputFragment = "inputFragment";
        String baseUri = "baseUri";
        ParseErrorList errors = mock(ParseErrorList.class);
        ParseSettings settings = ParseSettings.DEFAULT;
        List<Node> nodes = mock(List.class);

        when(xmlTreeBuilder.parseFragment(inputFragment, baseUri, errors, settings)).thenCallRealMethod();
        when(xmlTreeBuilder.baseUri).thenReturn(baseUri);

        xmlTreeBuilder.parseFragment(inputFragment, baseUri, errors, settings);

        verify(xmlTreeBuilder).parseFragment(inputFragment, baseUri, errors, settings);
        verify(xmlTreeBuilder).runParser();
        verify(nodes).addAll(doc.childNodes());
    }
}