
import org.jsoup.parser.*;
import org.jsoup.nodes.*;
import org.jsoup.select.Elements;
import org.junit.*;

import static org.junit.Assert.*;

public class XmlTreeBuilderTest {

    private XmlTreeBuilder xmlTreeBuilder;

    @Before
    public void setUp() {
        xmlTreeBuilder = new XmlTreeBuilder();
    }

    @Test
    public void testDefaultSettings() {
        // Default settings should return ParseSettings.preserveCase
        assertEquals(ParseSettings.preserveCase, xmlTreeBuilder.defaultSettings());
    }
    
    @Test
    public void testParse() {
        String input = "<root><child1>text1</child1><child2>text2</child2></root>";
        String baseUri = "http://example.com";
        Document expected = Jsoup.parse(input, baseUri, Parser.xmlParser());

        Document actual = xmlTreeBuilder.parse(input, baseUri);
        assertEquals(expected, actual);
    }
    
    @Test
    public void testInitialiseParse() {
        String input = "<html><head><title>Test</title></head><body>Test Body</body></html>";
        String baseUri = "http://example.com";
        ParseErrorList errors = new ParseErrorList();
        ParseSettings settings = ParseSettings.preserveCase;

        xmlTreeBuilder.initialiseParse(input, baseUri, errors, settings);

        assertEquals(settings, xmlTreeBuilder.settings);
        assertEquals(baseUri, xmlTreeBuilder.baseUri);
        assertEquals(input, xmlTreeBuilder.input);
        assertEquals(errors, xmlTreeBuilder.errors);
        assertEquals(1, xmlTreeBuilder.stack.size());
        assertTrue(xmlTreeBuilder.stack.contains(xmlTreeBuilder.doc));
        assertEquals(Document.OutputSettings.Syntax.xml, xmlTreeBuilder.doc.outputSettings().syntax());
    }

    @Test
    public void testProcessStartTag() {
        xmlTreeBuilder.stack.add(new Element(Tag.valueOf("root"), "", ParseSettings.preserveCase));
        Token.StartTag startTag = new Token.StartTag("child", "attribute1=value1", false);

        Element expectedElement = new Element(Tag.valueOf("child"), "", ParseSettings.preserveCase);
        expectedElement.attributes().put("attribute1", "value1");

        xmlTreeBuilder.process(startTag);

        assertEquals(2, xmlTreeBuilder.stack.size());
        assertTrue(xmlTreeBuilder.stack.contains(expectedElement));
        assertTrue(xmlTreeBuilder.currentElement().childNodes().contains(expectedElement));
    }

    @Test
    public void testProcessEndTag() {
        Element element = new Element(Tag.valueOf("child"), "", ParseSettings.preserveCase);
        xmlTreeBuilder.stack.add(element);
        Token.EndTag endTag = new Token.EndTag("child");

        xmlTreeBuilder.process(endTag);

        assertEquals(1, xmlTreeBuilder.stack.size());
        assertFalse(xmlTreeBuilder.stack.contains(element));
        assertFalse(xmlTreeBuilder.currentElement().childNodes().contains(element));
    }

    @Test
    public void testProcessComment() {
        String commentData = "This is a comment";
        Token.Comment comment = new Token.Comment(commentData);

        xmlTreeBuilder.process(comment);

        Element expectedParentElement = xmlTreeBuilder.currentElement();
        Comment expectedComment = new Comment(commentData, "");
        assertTrue(expectedParentElement.childNodes().contains(expectedComment));
    }

    @Test
    public void testProcessCharacter() {
        String characterData = "This is a text node";
        Token.Character characterToken = new Token.Character(characterData);

        xmlTreeBuilder.process(characterToken);

        Element expectedParentElement = xmlTreeBuilder.currentElement();
        TextNode expectedTextNode = new TextNode(characterData, "");
        assertTrue(expectedParentElement.childNodes().contains(expectedTextNode));
    }

    @Test
    public void testProcessDoctype() {
        String doctypeName = "html";
        String publicIdentifier = "-//W3C//DTD HTML 4.01//EN";
        String systemIdentifier = "http://www.w3.org/TR/html4/strict.dtd";
        Token.Doctype doctype = new Token.Doctype(doctypeName, publicIdentifier, systemIdentifier);

        xmlTreeBuilder.process(doctype);

        Element expectedParentElement = xmlTreeBuilder.currentElement();
        DocumentType expectedDoctype = new DocumentType(doctypeName, publicIdentifier, systemIdentifier, "");
        assertTrue(expectedParentElement.childNodes().contains(expectedDoctype));
    }

    @Test
    public void testInsertNode() {
        Element element1 = new Element(Tag.valueOf("element1"), "", ParseSettings.preserveCase);
        Element element2 = new Element(Tag.valueOf("element2"), "", ParseSettings.preserveCase);
        xmlTreeBuilder.stack.add(element1);

        xmlTreeBuilder.insertNode(element2);

        assertTrue(element1.childNodes().contains(element2));
    }

    @Test
    public void testInsertStartTagSelfClosing() {
        String tagName = "selfClosingTag";
        String attribute = "attribute=value";
        boolean selfClosing = true;

        Token.StartTag startTag = new Token.StartTag(tagName, attribute, selfClosing);

        Element expectedElement = new Element(Tag.valueOf(tagName), "", ParseSettings.preserveCase);
        expectedElement.attributes().put("attribute", "value");

        xmlTreeBuilder.insert(startTag);

        assertTrue(xmlTreeBuilder.currentElement().childNodes().contains(expectedElement));
        assertTrue(expectedElement.isSelfClosing());
    }

    @Test
    public void testInsertStartTagNonSelfClosing() {
        String tagName = "nonSelfClosingTag";
        String attribute = "attribute=value";
        boolean selfClosing = false;

        Token.StartTag startTag = new Token.StartTag(tagName, attribute, selfClosing);

        Element expectedElement = new Element(Tag.valueOf(tagName), "", ParseSettings.preserveCase);
        expectedElement.attributes().put("attribute", "value");

        xmlTreeBuilder.insert(startTag);

        assertTrue(xmlTreeBuilder.currentElement().childNodes().contains(expectedElement));
        assertFalse(expectedElement.isSelfClosing());
        assertTrue(xmlTreeBuilder.stack.contains(expectedElement));
    }

    @Test
    public void testInsertCommentBogus() {
        String commentData = "<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">";
        Token.Comment comment = new Token.Comment(commentData);
        Comment expectedComment = new Comment(comment.getData(), "");

        xmlTreeBuilder.insert(comment);

        assertTrue(xmlTreeBuilder.currentElement().childNodes().contains(expectedComment));
    }

    @Test
    public void testInsertCommentNormal() {
        String commentData = "<!-- This is a comment -->";
        Token.Comment comment = new Token.Comment(commentData);
        Comment expectedComment = new Comment(comment.getData(), "");

        xmlTreeBuilder.insert(comment);

        assertTrue(xmlTreeBuilder.currentElement().childNodes().contains(expectedComment));
    }

    @Test
    public void testInsertCharacter() {
        String characterData = "This is a text node";
        Token.Character characterToken = new Token.Character(characterData);
        TextNode expectedTextNode = new TextNode(characterData, "");

        xmlTreeBuilder.insert(characterToken);

        assertTrue(xmlTreeBuilder.currentElement().childNodes().contains(expectedTextNode));
    }

    @Test
    public void testInsertDoctype() {
        String doctypeName = "html";
        String publicIdentifier = "-//W3C//DTD XHTML 1.0 Transitional//EN";
        String systemIdentifier = "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd";
        Token.Doctype doctype = new Token.Doctype(doctypeName, publicIdentifier, systemIdentifier);
        DocumentType expectedDoctype = new DocumentType(doctype.getName(), doctype.getPublicIdentifier(), doctype.getSystemIdentifier(), "");

        xmlTreeBuilder.insert(doctype);

        assertTrue(xmlTreeBuilder.currentElement().childNodes().contains(expectedDoctype));
    }

    @Test
    public void testPopStackToClose() {
        Element element1 = new Element(Tag.valueOf("element1"), "", ParseSettings.preserveCase);
        Element element2 = new Element(Tag.valueOf("element2"), "", ParseSettings.preserveCase);
        Element element3 = new Element(Tag.valueOf("element3"), "", ParseSettings.preserveCase);
        xmlTreeBuilder.stack.add(element1);
        xmlTreeBuilder.stack.add(element2);
        xmlTreeBuilder.stack.add(element3);

        Token.EndTag endTag = new Token.EndTag("element2");

        xmlTreeBuilder.popStackToClose(endTag);

        assertEquals(2, xmlTreeBuilder.stack.size());
        assertFalse(xmlTreeBuilder.stack.contains(element2));
        assertFalse(xmlTreeBuilder.currentElement().childNodes().contains(element2));
    }
    
    @Test
    public void testParseFragment() {
        String inputFragment = "<child1>text1</child1><child2>text2</child2>";
        String baseUri = "http://example.com";
        ParseErrorList errors = new ParseErrorList();
        ParseSettings settings = ParseSettings.preserveCase;

        List<Node> expected = Jsoup.parse(inputFragment, baseUri, Parser.xmlParser()).childNodes();

        List<Node> actual = xmlTreeBuilder.parseFragment(inputFragment, baseUri, errors, settings);
        assertEquals(expected, actual);
    }
}

