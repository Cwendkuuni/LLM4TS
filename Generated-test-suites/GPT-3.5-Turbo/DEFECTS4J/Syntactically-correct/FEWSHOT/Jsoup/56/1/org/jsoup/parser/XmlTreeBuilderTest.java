package org.jsoup.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.jsoup.nodes.Comment;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.DocumentType;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.jsoup.parser.Token.Character;
import org.jsoup.parser.Token.Comment;
import org.jsoup.parser.Token.Doctype;
import org.jsoup.parser.Token.EndTag;
import org.jsoup.parser.Token.StartTag;
import org.junit.Before;
import org.junit.Test;

public class XmlTreeBuilderTest {

    private static final String BASE_URI = "http://www.example.com";

    private XmlTreeBuilder xmlTreeBuilder;

    @Before
    public void setUp() {
        xmlTreeBuilder = new XmlTreeBuilder();
    }

    @Test
    public void testDefaultSettings() {
        ParseSettings expected = ParseSettings.preserveCase;
        ParseSettings actual = xmlTreeBuilder.defaultSettings();
        assertEquals(expected, actual);
    }

    @Test
    public void testParse() {
        String input = "<root><child1>Text 1</child1><child2>Text 2</child2></root>";
        Document expected = Jsoup.parse(input, BASE_URI, Parser.xmlParser());
        Document actual = xmlTreeBuilder.parse(input, BASE_URI);
        assertEquals(expected, actual);
    }

    @Test
    public void testInitialiseParse() {
        String input = "<root><child1>Text 1</child1><child2>Text 2</child2></root>";
        ParseErrorList errors = new ParseErrorList();
        ParseSettings settings = ParseSettings.preserveCase;
        xmlTreeBuilder.initialiseParse(input, BASE_URI, errors, settings);

        Document expectedDoc = new Document("");
        Element expectedRoot = new Element(Tag.valueOf("root", settings), BASE_URI);
        expectedDoc.appendChild(expectedRoot);
        Element expectedChild1 = new Element(Tag.valueOf("child1", settings), BASE_URI);
        expectedChild1.appendChild(new TextNode("Text 1", BASE_URI));
        expectedRoot.appendChild(expectedChild1);
        Element expectedChild2 = new Element(Tag.valueOf("child2", settings), BASE_URI);
        expectedChild2.appendChild(new TextNode("Text 2", BASE_URI));
        expectedRoot.appendChild(expectedChild2);

        Document actualDoc = xmlTreeBuilder.doc;
        Element actualRoot = xmlTreeBuilder.currentElement();
        Element actualChild1 = actualRoot.child(0);
        Element actualChild2 = actualRoot.child(1);

        assertEquals(expectedDoc, actualDoc);
        assertEquals(expectedRoot, actualRoot);
        assertEquals(expectedChild1, actualChild1);
        assertEquals(expectedChild2, actualChild2);
    }

    @Test
    public void testProcess_StartTag() {
        Element expected = new Element(Tag.valueOf("child", xmlTreeBuilder.settings), BASE_URI);

        Token token = new StartTag("child", "");
        xmlTreeBuilder.process(token);

        Element actual = xmlTreeBuilder.currentElement();
        assertEquals(expected, actual);
    }

    @Test
    public void testProcess_EndTag() {
        Element root = new Element(Tag.valueOf("root", xmlTreeBuilder.settings), BASE_URI);
        Element child1 = new Element(Tag.valueOf("child1", xmlTreeBuilder.settings), BASE_URI);
        Element child2 = new Element(Tag.valueOf("child2", xmlTreeBuilder.settings), BASE_URI);
        root.appendChild(child1);
        root.appendChild(child2);
        xmlTreeBuilder.stack.push(root);

        Token token = new EndTag("child2");
        xmlTreeBuilder.process(token);

        Element expectedRoot = new Element(Tag.valueOf("root", xmlTreeBuilder.settings), BASE_URI);
        expectedRoot.appendChild(child1);
        Element expected = expectedRoot;
        
        Element actual = xmlTreeBuilder.currentElement();
        assertEquals(expected, actual);
    }

    @Test
    public void testProcess_Comment() {
        Comment expected = new Comment("This is a comment", BASE_URI);

        Token token = new Comment("This is a comment");
        xmlTreeBuilder.process(token);

        Node actual = xmlTreeBuilder.currentElement().childNode(0);
        assertEquals(expected, actual);
    }

    @Test
    public void testProcess_Character() {
        TextNode expected = new TextNode("This is some text", BASE_URI);

        Token token = new Character("This is some text");
        xmlTreeBuilder.process(token);

        Node actual = xmlTreeBuilder.currentElement().childNode(0);
        assertEquals(expected, actual);
    }

    @Test
    public void testProcess_Doctype() {
        String name = "html";
        String publicId = "-//W3C//DTD HTML 4.01 Transitional//EN";
        String systemId = "http://www.w3.org/TR/html4/loose.dtd";
        DocumentType expected = new DocumentType(name, publicId, systemId, BASE_URI);

        Token token = new Doctype(name, publicId, systemId);
        xmlTreeBuilder.process(token);

        Node actual = xmlTreeBuilder.currentElement().childNode(0);
        assertEquals(expected, actual);
    }

    @Test
    public void testInsertNode() {
        Element element = new Element(Tag.valueOf("child", xmlTreeBuilder.settings), BASE_URI);

        xmlTreeBuilder.insertNode(element);

        Element expected = element;
        Element actual = xmlTreeBuilder.currentElement();
        assertEquals(expected, actual);
    }

    @Test
    public void testInsert_StartTag() {
        StartTag startTag = new StartTag("child", "");
        Tag tag = Tag.valueOf(startTag.name(), xmlTreeBuilder.settings);
        Element expected = new Element(tag, BASE_URI);

        Element actual = xmlTreeBuilder.insert(startTag);

        assertEquals(expected, actual);
        assertTrue(startTag.isSelfClosing());
        assertTrue(tag.isKnownTag());
        assertTrue(tag.isSelfClosing());
    }

    @Test
    public void testInsert_Comment() {
        Comment commentToken = new Comment("This is a comment");

        xmlTreeBuilder.insert(commentToken);

        Comment expected = new Comment(commentToken.getData(), BASE_URI);
        Comment actual = (Comment)xmlTreeBuilder.currentElement().childNode(0);
        assertEquals(expected, actual);
    }

    @Test
    public void testInsert_Character() {
        Character characterToken = new Character("This is some text");

        xmlTreeBuilder.insert(characterToken);

        TextNode expected = new TextNode(characterToken.getData(), BASE_URI);
        TextNode actual = (TextNode)xmlTreeBuilder.currentElement().childNode(0);
        assertEquals(expected, actual);
    }

    @Test
    public void testInsert_Doctype() {
        String name = "html";
        String publicId = "-//W3C//DTD HTML 4.01 Transitional//EN";
        String systemId = "http://www.w3.org/TR/html4/loose.dtd";
        Doctype doctype = new Doctype(name, publicId, systemId);

        xmlTreeBuilder.insert(doctype);

        DocumentType expected = new DocumentType(name, publicId, systemId, BASE_URI);
        DocumentType actual = (DocumentType)xmlTreeBuilder.currentElement().childNode(0);
        assertEquals(expected, actual);
    }

    @Test
    public void testPopStackToClose_ElementFound() {
        Element root = new Element(Tag.valueOf("root", xmlTreeBuilder.settings), BASE_URI);
        Element child1 = new Element(Tag.valueOf("child1", xmlTreeBuilder.settings), BASE_URI);
        Element child2 = new Element(Tag.valueOf("child2", xmlTreeBuilder.settings), BASE_URI);
        root.appendChild(child1);
        root.appendChild(child2);
        xmlTreeBuilder.stack.push(root);

        Token.EndTag endTag = new EndTag("child2");
        xmlTreeBuilder.popStackToClose(endTag);

        Element expectedRoot = new Element(Tag.valueOf("root", xmlTreeBuilder.settings), BASE_URI);
        expectedRoot.appendChild(child1);
        Element expected = expectedRoot;

        Element actual = xmlTreeBuilder.currentElement();
        assertEquals(expected, actual);
    }

    @Test
    public void testPopStackToClose_ElementNotFound() {
        Element root = new Element(Tag.valueOf("root", xmlTreeBuilder.settings), BASE_URI);
        xmlTreeBuilder.stack.push(root);

        Token.EndTag endTag = new EndTag("child");
        xmlTreeBuilder.popStackToClose(endTag);

        Element expected = root;
        Element actual = xmlTreeBuilder.currentElement();
        assertEquals(expected, actual);
    }

    @Test
    public void testParseFragment() {
        String inputFragment = "<child1>Text 1</child1><child2>Text 2</child2>";
        ParseErrorList errors = new ParseErrorList();
        ParseSettings settings = ParseSettings.preserveCase;
        List<Node> expected = Jsoup.parseBodyFragment(inputFragment, BASE_URI, Parser.xmlParser()).childNodes();

        List<Node> actual = xmlTreeBuilder.parseFragment(inputFragment, BASE_URI, errors, settings);
        assertEquals(expected, actual);
    }

}