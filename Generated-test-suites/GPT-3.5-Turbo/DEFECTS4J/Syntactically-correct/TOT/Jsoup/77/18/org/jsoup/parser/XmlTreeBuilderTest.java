import org.jsoup.Jsoup;
import org.jsoup.helper.Validate;
import org.jsoup.nodes.*;
import org.jsoup.parser.ParseErrorList;
import org.jsoup.parser.ParseSettings;
import org.jsoup.parser.Parser;
import org.junit.Before;
import org.junit.Test;

import java.io.Reader;
import java.io.StringReader;
import java.util.List;

import static org.junit.Assert.*;

public class XmlTreeBuilderTest {

    private XmlTreeBuilder xmlTreeBuilder;

    @Before
    public void setUp() {
        xmlTreeBuilder = new XmlTreeBuilder();
    }

    @Test
    public void testDefaultSettings() {
        ParseSettings expectedSettings = ParseSettings.preserveCase;
        ParseSettings actualSettings = xmlTreeBuilder.defaultSettings();
        assertEquals(expectedSettings, actualSettings);
    }

    @Test
    public void testParseWithReaderAndBaseUri() {
        String input = "<root><tag>Text</tag></root>";
        String baseUri = "https://example.com";
        Reader reader = new StringReader(input);

        Document expectedDocument = Jsoup.parse(input, baseUri, Parser.xmlParser());
        Document actualDocument = xmlTreeBuilder.parse(reader, baseUri);

        assertEquals(expectedDocument, actualDocument);
    }

    @Test
    public void testParseWithStringAndBaseUri() {
        String input = "<root><tag>Text</tag></root>";
        String baseUri = "https://example.com";

        Document expectedDocument = Jsoup.parse(input, baseUri, Parser.xmlParser());
        Document actualDocument = xmlTreeBuilder.parse(input, baseUri);

        assertEquals(expectedDocument, actualDocument);
    }

    @Test
    public void testInitialiseParse() {
        String input = "<root><tag>Text</tag></root>";
        String baseUri = "https://example.com";
        Reader reader = new StringReader(input);
        ParseErrorList errors = ParseErrorList.noTracking();
        ParseSettings settings = ParseSettings.preserveCase;

        Document doc = new Document(baseUri);
        xmlTreeBuilder.setDoc(doc);
        xmlTreeBuilder.initialiseParse(reader, baseUri, errors, settings);

        assertEquals(doc, xmlTreeBuilder.getCurrentElement());
        assertEquals(Document.OutputSettings.Syntax.xml, doc.outputSettings().syntax());
    }

    @Test
    public void testProcessStartTag() {
        Tag tag = Tag.valueOf("tag", ParseSettings.preserveCase);
        Attributes attributes = new Attributes();
        attributes.put("attr", "value");
        Token.StartTag startTag = new Token.StartTag(tag, attributes);
        Element el = new Element(tag, "", attributes);
        xmlTreeBuilder.setStack(new Element("root"));
        xmlTreeBuilder.setBaseUri("");
        xmlTreeBuilder.setSettings(ParseSettings.preserveCase);

        Element expectedElement = xmlTreeBuilder.insert(startTag);
        Element actualElement = xmlTreeBuilder.getCurrentElement();

        assertEquals(expectedElement, actualElement);
        assertTrue(startTag.isSelfClosing());
        assertTrue(tag.isKnownTag());
    }

    @Test
    public void testProcessEndTag() {
        Tag tag = Tag.valueOf("tag", ParseSettings.preserveCase);
        xmlTreeBuilder.setStack(new Element("root"));
        xmlTreeBuilder.setBaseUri("");
        xmlTreeBuilder.setSettings(ParseSettings.preserveCase);
        Token.EndTag endTag = new Token.EndTag(tag);
        xmlTreeBuilder.process(endTag);

        Element element = xmlTreeBuilder.getCurrentElement();
        assertEquals("root", element.nodeName());
    }

    @Test
    public void testProcessComment() {
        String commentString = "This is a comment";
        Token.Comment comment = new Token.Comment(commentString);
        xmlTreeBuilder.setStack(new Element("root"));
        xmlTreeBuilder.setBaseUri("");
        xmlTreeBuilder.insert(comment);

        List<Node> childNodes = xmlTreeBuilder.getCurrentElement().childNodes();
        assertEquals(1, childNodes.size());
        assertTrue(childNodes.get(0) instanceof Comment);
        assertEquals(commentString, ((Comment) childNodes.get(0)).getData());
    }

    @Test
    public void testProcessCharacter() {
        String data = "This is a character";
        Token.Character character = new Token.Character(data);
        xmlTreeBuilder.setStack(new Element("root"));
        xmlTreeBuilder.setBaseUri("");
        xmlTreeBuilder.insert(character);

        List<Node> childNodes = xmlTreeBuilder.getCurrentElement().childNodes();
        assertEquals(1, childNodes.size());
        assertTrue(childNodes.get(0) instanceof TextNode);
        assertEquals(data, ((TextNode) childNodes.get(0)).getWholeText());
    }

    @Test
    public void testInsertDoctype() {
        String name = "doctype";
        String publicIdentifier = "-//W3C//DTD XHTML 1.0 Strict//EN";
        String systemIdentifier = "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd";
        Token.Doctype doctype = new Token.Doctype(name, publicIdentifier, systemIdentifier);
        xmlTreeBuilder.setStack(new Element("root"));
        xmlTreeBuilder.setBaseUri("");
        xmlTreeBuilder.insert(doctype);

        List<Node> childNodes = xmlTreeBuilder.getCurrentElement().childNodes();
        assertEquals(1, childNodes.size());
        assertTrue(childNodes.get(0) instanceof DocumentType);
        DocumentType documentType = (DocumentType) childNodes.get(0);
        assertEquals(name, documentType.nodeName());
        assertEquals(publicIdentifier, documentType.publicIdentifier());
        assertEquals(systemIdentifier, documentType.systemIdentifier());
    }

    @Test
    public void testPopStackToClose() {
        xmlTreeBuilder.setStack(new Element("root"));
        Element element1 = new Element("tag");
        Element element2 = new Element("tag");
        Element element3 = new Element("tag");
        Element element4 = new Element("tag");
        xmlTreeBuilder.getStack().add(element1);
        xmlTreeBuilder.getStack().add(element2);
        xmlTreeBuilder.getStack().add(element3);
        xmlTreeBuilder.getStack().add(element4);

        Token.EndTag endTag = new Token.EndTag("tag");
        xmlTreeBuilder.popStackToClose(endTag);

        List<Element> stack = xmlTreeBuilder.getStack();
        assertEquals(1, stack.size());
        assertEquals(element1, stack.get(0));
    }

    @Test
    public void testParseFragment() {
        String inputFragment = "<tag>Text</tag>";
        String baseUri = "https://example.com";
        ParseErrorList errors = ParseErrorList.noTracking();
        ParseSettings settings = ParseSettings.preserveCase;

        List<Node> expectedNodes = Jsoup.parseBodyFragment(inputFragment, baseUri).childNodes();
        List<Node> actualNodes = xmlTreeBuilder.parseFragment(inputFragment, baseUri, errors, settings);

        assertEquals(expectedNodes, actualNodes);
    }
}