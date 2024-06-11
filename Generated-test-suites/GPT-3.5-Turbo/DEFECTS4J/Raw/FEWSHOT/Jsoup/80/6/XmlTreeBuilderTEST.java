package org.jsoup.parser;

import org.jsoup.nodes.XmlDeclaration;
import org.jsoup.parser.XmlTreeBuilder;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;

import org.jsoup.helper.Validate;
import org.jsoup.nodes.Comment;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.DocumentType;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.parser.ParseErrorList;
import org.jsoup.parser.ParseSettings;
import org.jsoup.parser.Parser;
import org.jsoup.parser.Tag;
import org.jsoup.parser.Token;
import org.jsoup.parser.TreeBuilder;

import java.io.Reader;
import java.io.StringReader;
import java.util.List;

public class XmlTreeBuilderTest {
    private XmlTreeBuilder xmlTreeBuilder;

    @Before
    public void setUp() {
        xmlTreeBuilder = new XmlTreeBuilder();
    }

    @Test
    public void testDefaultSettings() {
        ParseSettings settings = xmlTreeBuilder.defaultSettings();
        assertTrue(settings == ParseSettings.preserveCase);
    }

    @Test
    public void testParseReaderString() {
        Reader reader = mock(Reader.class);
        String baseUri = "http://example.com";
        Document doc = xmlTreeBuilder.parse(reader, baseUri);

        verify(xmlTreeBuilder).parse(reader, baseUri);
        assertTrue(doc != null);
    }

    @Test
    public void testParseStringString() {
        String input = "<html></html>";
        String baseUri = "http://example.com";
        Document doc = xmlTreeBuilder.parse(input, baseUri);

        verify(xmlTreeBuilder).parse(new StringReader(input), baseUri);
        assertTrue(doc != null);
    }

    @Test
    public void testInitialiseParse() {
        Reader input = mock(Reader.class);
        String baseUri = "http://example.com";
        ParseErrorList errors = ParseErrorList.noTracking();
        ParseSettings settings = ParseSettings.preserveCase;

        xmlTreeBuilder.initialiseParse(input, baseUri, errors, settings);

        verify(xmlTreeBuilder).initialiseParse(input, baseUri, errors, settings);
    }

    @Test
    public void testProcess() {
        Token token = mock(Token.class);

        xmlTreeBuilder.process(token);

        verify(xmlTreeBuilder).process(token);
    }

    @Test
    public void testInsertNode() {
        Node node = mock(Node.class);

        xmlTreeBuilder.insertNode(node);

        verify(xmlTreeBuilder).insertNode(node);
    }

    @Test
    public void testInsert() {
        Token.StartTag startTag = mock(Token.StartTag.class);

        Element element = xmlTreeBuilder.insert(startTag);

        verify(xmlTreeBuilder).insert(startTag);
        assertTrue(element != null);
    }

    @Test
    public void testInsertComment() {
        Token.Comment commentToken = mock(Token.Comment.class);

        xmlTreeBuilder.insert(commentToken);

        verify(xmlTreeBuilder).insert(commentToken);
    }

    @Test
    public void testInsertCharacter() {
        Token.Character token = mock(Token.Character.class);

        xmlTreeBuilder.insert(token);

        verify(xmlTreeBuilder).insert(token);
    }

    @Test
    public void testInsertDoctype() {
        Token.Doctype d = mock(Token.Doctype.class);

        xmlTreeBuilder.insert(d);

        verify(xmlTreeBuilder).insert(d);
    }

    @Test
    public void testPopStackToClose() {
        Token.EndTag endTag = mock(Token.EndTag.class);

        xmlTreeBuilder.popStackToClose(endTag);

        verify(xmlTreeBuilder).popStackToClose(endTag);
    }

    @Test
    public void testParseFragment() {
        String inputFragment = "<html></html>";
        String baseUri = "http://example.com";
        ParseErrorList errors = ParseErrorList.noTracking();
        ParseSettings settings = ParseSettings.preserveCase;

        List<Node> nodeList = xmlTreeBuilder.parseFragment(inputFragment, baseUri, errors, settings);

        verify(xmlTreeBuilder).parseFragment(inputFragment, baseUri, errors, settings);
        assertTrue(nodeList != null);
    }
}