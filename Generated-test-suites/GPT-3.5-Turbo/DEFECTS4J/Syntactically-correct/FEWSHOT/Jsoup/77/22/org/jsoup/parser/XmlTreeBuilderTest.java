import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.jsoup.nodes.Comment;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.DocumentType;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.parser.ParseErrorList;
import org.jsoup.parser.ParseSettings;
import org.jsoup.parser.Parser;
import org.jsoup.parser.Tag;
import org.junit.Before;
import org.junit.Test;

import java.io.StringReader;
import java.util.List;

public class XmlTreeBuilderTest {
    private XmlTreeBuilder treeBuilder;

    @Before
    public void setUp() {
        treeBuilder = new XmlTreeBuilder();
    }

    @Test
    public void testDefaultSettings() {
        ParseSettings settings = treeBuilder.defaultSettings();
        assertNotNull(settings);
        assertFalse(settings.parseRequiresStartTag());
        assertFalse(settings.preserveAttributes());
        assertTrue(settings.preserveCase());
    }

    @Test
    public void testParse_withReaderAndBaseUri() {
        String input = "<root><element attribute=\"value\">text</element></root>";
        String baseUri = "http://example.com";
        Document document = treeBuilder.parse(new StringReader(input), baseUri);
        assertNotNull(document);
        assertEquals(baseUri, document.baseUri());
    }

    @Test
    public void testParse_withInputAndBaseUri() {
        String input = "<root><element attribute=\"value\">text</element></root>";
        String baseUri = "http://example.com";
        Document document = treeBuilder.parse(input, baseUri);
        assertNotNull(document);
        assertEquals(baseUri, document.baseUri());
    }

    @Test
    public void testInitialiseParse() {
        StringReader input = new StringReader("<root><element attribute=\"value\">text</element></root>");
        String baseUri = "http://example.com";
        ParseErrorList errors = new ParseErrorList();
        ParseSettings settings = treeBuilder.defaultSettings();
        treeBuilder.initialiseParse(input, baseUri, errors, settings);
        Document document = treeBuilder.getDocument();
        assertNotNull(document);
        assertEquals(baseUri, document.baseUri());
    }

    @Test
    public void testProcess_withStartTag() {
        Token.StartTag startTag = new Token.StartTag("element");
        startTag.addAttribute("attribute", "value");
        treeBuilder.initialiseParse(new StringReader(""), "http://example.com", new ParseErrorList(), treeBuilder.defaultSettings());
        assertTrue(treeBuilder.process(startTag));
        Document document = treeBuilder.getDocument();
        assertNotNull(document);
        Element root = document.child(0);
        assertNotNull(root);
        Element element = root.child(0);
        assertNotNull(element);
        assertEquals("element", element.tagName());
        assertEquals("value", element.attr("attribute"));
    }

    @Test
    public void testProcess_withEndTag() {
        Token.StartTag startTag = new Token.StartTag("element");
        treeBuilder.initialiseParse(new StringReader(""), "http://example.com", new ParseErrorList(), treeBuilder.defaultSettings());
        assertTrue(treeBuilder.process(startTag));

        Token.EndTag endTag = new Token.EndTag("element");
        assertTrue(treeBuilder.process(endTag));
        Document document = treeBuilder.getDocument();
        assertNotNull(document);
        Element root = document.child(0);
        assertNotNull(root);
        assertTrue(root.childNodes().isEmpty());
    }

    @Test
    public void testProcess_withComment() {
        Token.Comment comment = new Token.Comment("Test comment");
        treeBuilder.initialiseParse(new StringReader(""), "http://example.com", new ParseErrorList(), treeBuilder.defaultSettings());
        assertTrue(treeBuilder.process(comment));
        Document document = treeBuilder.getDocument();
        assertNotNull(document);
        Element root = document.child(0);
        assertNotNull(root);
        Comment commentNode = (Comment) root.childNode(0);
        assertNotNull(commentNode);
        assertEquals("Test comment", commentNode.getData());
    }

    @Test
    public void testProcess_withCharacter() {
        Token.Character character = new Token.Character("test character");
        treeBuilder.initialiseParse(new StringReader(""), "http://example.com", new ParseErrorList(), treeBuilder.defaultSettings());
        assertTrue(treeBuilder.process(character));
        Document document = treeBuilder.getDocument();
        assertNotNull(document);
        Element root = document.child(0);
        assertNotNull(root);
        Node characterNode = root.childNode(0);
        assertNotNull(characterNode);
        assertEquals("test character", characterNode.toString());
    }

    @Test
    public void testProcess_withDoctype() {
        Token.Doctype doctype = new Token.Doctype("html");
        treeBuilder.initialiseParse(new StringReader(""), "http://example.com", new ParseErrorList(), treeBuilder.defaultSettings());
        assertTrue(treeBuilder.process(doctype));
        Document document = treeBuilder.getDocument();
        assertNotNull(document);
        assertEquals("<!DOCTYPE html>", document.childNodes().get(0).toString());
    }

    @Test
    public void testInsertNode_withElement() {
        Element element = new Element(Tag.valueOf("element"), "http://example.com", treeBuilder.defaultSettings().normalizeAttributes(new ParseSettings.AttributeBuilder()));
        treeBuilder.initialiseParse(new StringReader(""), "http://example.com", new ParseErrorList(), treeBuilder.defaultSettings());
        treeBuilder.insertNode(element);
        Document document = treeBuilder.getDocument();
        assertNotNull(document);
        Element root = document.child(0);
        assertNotNull(root);
        assertEquals(element, root.child(0));
    }

    @Test
    public void testInsert_withStartTag() {
        Token.StartTag startTag = new Token.StartTag("element");
        startTag.addAttribute("attribute", "value");
        treeBuilder.initialiseParse(new StringReader(""), "http://example.com", new ParseErrorList(), treeBuilder.defaultSettings());
        Element element = treeBuilder.insert(startTag);
        Document document = treeBuilder.getDocument();
        assertNotNull(document);
        Element root = document.child(0);
        assertNotNull(root);
        assertEquals(element, root.child(0));
        assertEquals("element", element.tagName());
        assertEquals("value", element.attr("attribute"));
    }

    @Test
    public void testInsert_withCommentToken() {
        Token.Comment commentToken = new Token.Comment("Test comment");
        treeBuilder.initialiseParse(new StringReader(""), "http://example.com", new ParseErrorList(), treeBuilder.defaultSettings());
        treeBuilder.insert(commentToken);
        Document document = treeBuilder.getDocument();
        assertNotNull(document);
        Element root = document.child(0);
        assertNotNull(root);
        assertEquals(commentToken.getData(), root.child(0).toString());
    }

    @Test
    public void testInsert_withCharacterToken() {
        Token.Character characterToken = new Token.Character("test character");
        treeBuilder.initialiseParse(new StringReader(""), "http://example.com", new ParseErrorList(), treeBuilder.defaultSettings());
        treeBuilder.insert(characterToken);
        Document document = treeBuilder.getDocument();
        assertNotNull(document);
        Element root = document.child(0);
        assertNotNull(root);
        assertEquals(characterToken.getData(), root.child(0).toString());
    }

    @Test
    public void testInsert_withDoctype() {
        Token.Doctype doctype = new Token.Doctype("html");
        treeBuilder.initialiseParse(new StringReader(""), "http://example.com", new ParseErrorList(), treeBuilder.defaultSettings());
        treeBuilder.insert(doctype);
        Document document = treeBuilder.getDocument();
        assertNotNull(document);
        assertEquals(doctype.asDoctype().toString(), document.childNodes().get(0).toString());
    }

    @Test
    public void testPopStackToClose() {
        treeBuilder.initialiseParse(new StringReader(""), "http://example.com", new ParseErrorList(), treeBuilder.defaultSettings());
        Token.StartTag startTag1 = new Token.StartTag("element1");
        treeBuilder.insert(startTag1);
        Token.StartTag startTag2 = new Token.StartTag("element2");
        treeBuilder.insert(startTag2);
        Token.StartTag startTag3 = new Token.StartTag("element3");
        treeBuilder.insert(startTag3);

        Token.EndTag endTag = new Token.EndTag("element2");
        treeBuilder.popStackToClose(endTag);

        Document document = treeBuilder.getDocument();
        assertNotNull(document);
        Element root = document.child(0);
        assertNotNull(root);
        assertEquals(2, root.children().size());
        assertEquals("element1", root.child(0).tagName());
        assertEquals("element3", root.child(1).tagName());
    }

    @Test
    public void testParseFragment() {
        String inputFragment = "<element attribute=\"value\">text</element>";
        String baseUri = "http://example.com";
        ParseErrorList errors = new ParseErrorList();
        ParseSettings settings = treeBuilder.defaultSettings();
        List<Node> nodes = treeBuilder.parseFragment(inputFragment, baseUri, errors, settings);
        assertNotNull(nodes);
        assertEquals(1, nodes.size());
        assertTrue(nodes.get(0) instanceof Element);
        Element element = (Element) nodes.get(0);
        assertEquals("element", element.tagName());
        assertEquals("value", element.attr("attribute"));
    }
}