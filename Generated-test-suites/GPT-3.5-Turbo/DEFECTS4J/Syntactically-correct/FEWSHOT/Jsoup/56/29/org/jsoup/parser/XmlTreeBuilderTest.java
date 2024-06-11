package org.jsoup.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;

import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.helper.Validate;
import org.jsoup.nodes.Comment;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Document.OutputSettings.Syntax;
import org.jsoup.nodes.DocumentType;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.jsoup.parser.CharacterReader;

import org.junit.BeforeClass;
import org.junit.Test;

public class XmlTreeBuilderTest {
    private static XmlTreeBuilder builder;
    private static Comment comment;
    private static Document document;
    private static DocumentType doctypeNode;
    private static Element element;
    private static Syntax outputSettingsSyntax;
    private static Token.Character characterToken;
    private static Token.Comment commentToken;
    private static Token.Doctype doctype;
    private static Token.EndTag endTag;
    private static Token.StartTag startTag;

    @BeforeClass
    public static void setUp() {
        builder = new XmlTreeBuilder();
        comment = mock(Comment.class);
        document = mock(Document.class);
        doctypeNode = mock(DocumentType.class);
        element = mock(Element.class);
        outputSettingsSyntax = Syntax.xml;
        characterToken = mock(Token.Character.class);
        commentToken = mock(Token.Comment.class);
        doctype = mock(Token.Doctype.class);
        endTag = mock(Token.EndTag.class);
        startTag = mock(Token.StartTag.class);
    }

    @Test
    public void testDefaultSettings() {
        ParseSettings expected = ParseSettings.preserveCase;
        ParseSettings actual = builder.defaultSettings();
        assertEquals(expected, actual);
    }

    @Test
    public void testParseWithBaseUri() {
        String input = "";
        String baseUri = "";
        builder.parse(input, baseUri);
    }

    @Test
    public void testInitialiseParse() {
        String input = "";
        String baseUri = "";
        ParseErrorList errors = new ParseErrorList();
        ParseSettings settings = ParseSettings.preserveCase;
        builder.initialiseParse(input, baseUri, errors, settings);
    }

    @Test
    public void testProcessStartTag() {
        builder.insert(startTag);
    }

    @Test
    public void testProcessEndTag() {
        builder.popStackToClose(endTag);
    }

    @Test
    public void testProcessComment() {
        builder.insert(commentToken);
    }

    @Test
    public void testProcessCharacter() {
        builder.insert(characterToken);
    }

    @Test
    public void testProcessDoctype() {
        builder.insert(doctype);
    }

    @Test
    public void testInsertNode() {
        builder.insertNode(node);
    }

    @Test
    public void testInsert() {
        builder.insert(startTag);
    }
    
    @Test
    public void testInsertComment() {
        builder.insert(commentToken);
    }
    
    @Test
    public void testInsertCharacter() {
        builder.insert(characterToken);
    }
    
    @Test
    public void testInsertDoctype() {
        builder.insert(doctype);
    }
    
    @Test
    public void testPopStackToClose() {
        builder.popStackToClose(endTag);
    }

    @Test
    public void testParseFragment() {
        String inputFragment = "";
        String baseUri = "";
        ParseErrorList errors = new ParseErrorList();
        ParseSettings settings = ParseSettings.preserveCase;
        builder.parseFragment(inputFragment, baseUri, errors, settings);
    }

}