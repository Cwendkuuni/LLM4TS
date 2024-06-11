import org.jsoup.Jsoup;
import org.jsoup.nodes.*;
import org.jsoup.parser.ParseErrorList;
import org.jsoup.parser.ParseSettings;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

public class XmlTreeBuilderTest {

    private static XmlTreeBuilder xmlTreeBuilder;

    @BeforeClass
    public static void setUp() {
        xmlTreeBuilder = new XmlTreeBuilder();
    }

    @Test
    public void testDefaultSettings() {
        ParseSettings settings = xmlTreeBuilder.defaultSettings();
        assertNotNull(settings);
        assertEquals(ParseSettings.preserveCase, settings);
    }

    @Test
    public void testParse() {
        String input = "<root><element1/><element2></element2><element3 attribute=\"value\">TextNode</element3></root>";
        String baseUri = "http://example.com";

        Document doc = xmlTreeBuilder.parse(input, baseUri);

        assertEquals("<root><element1 /><element2></element2><element3 attribute=\"value\">TextNode</element3></root>", doc.outerHtml());
        assertEquals(baseUri, doc.baseUri());
    }

    @Test
    public void testInitialiseParse() {
        String input = "<root><element1/><element2></element2><element3 attribute=\"value\">TextNode</element3></root>";
        String baseUri = "http://example.com";
        ParseErrorList errors = new ParseErrorList();
        ParseSettings settings = new ParseSettings(true);

        xmlTreeBuilder.initialiseParse(input, baseUri, errors, settings);

        Document doc = xmlTreeBuilder.getDocument();
        assertNotNull(doc);

        List<Node> childNodes = doc.childNodes();
        assertEquals(1, childNodes.size());
        assertTrue(childNodes.get(0) instanceof Element);
        assertEquals("root", childNodes.get(0).nodeName());

        Document.OutputSettings.Syntax syntax = doc.outputSettings().syntax();
        assertEquals(Document.OutputSettings.Syntax.xml, syntax);
    }

    @Test
    public void testProcess() {
        Token startTagToken = new Token.StartTag("element", "name", true);
        Token endTagToken = new Token.EndTag("element");
        Token commentToken = new Token.Comment("This is a comment");
        Token characterToken = new Token.Character("Some text");
        Token doctypeToken = new Token.Doctype("html", "publicId", "systemId");

        boolean result1 = xmlTreeBuilder.process(startTagToken);
        boolean result2 = xmlTreeBuilder.process(endTagToken);
        boolean result3 = xmlTreeBuilder.process(commentToken);
        boolean result4 = xmlTreeBuilder.process(characterToken);
        boolean result5 = xmlTreeBuilder.process(doctypeToken);

        assertTrue(result1);
        assertTrue(result2);
        assertTrue(result3);
        assertTrue(result4);
        assertTrue(result5);
    }

    @Test
    public void testInsertNode() {
        Document doc = new Document("");
        Element parentElement = doc.createElement("parent");
        Element childElement = doc.createElement("child");

        xmlTreeBuilder.setCurrentElement(parentElement);
        xmlTreeBuilder.insertNode(childElement);

        assertEquals(1, parentElement.childNodeSize());
        assertEquals(childElement, parentElement.childNode(0));
    }

    @Test
    public void testInsert() {
        String commentData = "This is a comment";
        Token.Comment commentToken1 = new Token.Comment(commentData);
        Token.Comment commentToken2 = new Token.Comment("<!DOCTYPE html>");

        xmlTreeBuilder.insert(commentToken1);
        xmlTreeBuilder.insert(commentToken2);

        Document doc = xmlTreeBuilder.getDocument();
        assertNotNull(doc);

        List<Node> childNodes = doc.childNodes();
        assertEquals(2, childNodes.size());
        assertTrue(childNodes.get(0) instanceof Comment);
        assertTrue(childNodes.get(1) instanceof XmlDeclaration);

        Comment commentNode = (Comment) childNodes.get(0);
        XmlDeclaration declarationNode = (XmlDeclaration) childNodes.get(1);

        assertEquals(commentData, commentNode.getData());
        assertEquals("<?DOCTYPE html>", declarationNode.getWholeDeclaration());
    }

    @Test
    public void testInsertCharacter() {
        String textData = "Some text";
        Token.Character characterToken = new Token.Character(textData);

        xmlTreeBuilder.insert(characterToken);

        Document doc = xmlTreeBuilder.getDocument();
        assertNotNull(doc);

        List<Node> childNodes = doc.childNodes();
        assertEquals(1, childNodes.size());
        assertTrue(childNodes.get(0) instanceof TextNode);

        TextNode textNode = (TextNode) childNodes.get(0);
        assertEquals(textData, textNode.getWholeText());
    }

    @Test
    public void testInsertDoctype() {
        String name = "html";
        String publicId = "-//W3C//DTD HTML 4.01 Transitional//EN";
        String systemId = "http://www.w3.org/TR/html4/loose.dtd";
        Token.Doctype doctypeToken = new Token.Doctype(name, publicId, systemId);

        xmlTreeBuilder.insert(doctypeToken);

        Document doc = xmlTreeBuilder.getDocument();
        assertNotNull(doc);

        List<Node> childNodes = doc.childNodes();
        assertEquals(1, childNodes.size());
        assertTrue(childNodes.get(0) instanceof DocumentType);

        DocumentType doctypeNode = (DocumentType) childNodes.get(0);
        assertEquals(name, doctypeNode.nodeName());
        assertEquals(publicId, doctypeNode.publicId());
        assertEquals(systemId, doctypeNode.systemId());
    }

    @Test
    public void testPopStackToClose() {
        Document doc = new Document("");
        Element parentElement = doc.createElement("parent");
        Element childElement1 = doc.createElement("child");
        Element childElement2 = doc.createElement("child");

        xmlTreeBuilder.setCurrentElement(parentElement);
        xmlTreeBuilder.stack.add(childElement1);
        xmlTreeBuilder.stack.add(childElement2);

        Token.EndTag endTagToken = new Token.EndTag("child");
        xmlTreeBuilder.popStackToClose(endTagToken);

        assertEquals(1, xmlTreeBuilder.stack.size());
        assertEquals(childElement1, xmlTreeBuilder.stack.get(0));
    }

    @Test
    public void testParseFragment() {
        String inputFragment = "<element1/><element2></element2><element3 attribute=\"value\">TextNode</element3>";
        String baseUri = "http://example.com";
        ParseErrorList errors = new ParseErrorList();
        ParseSettings settings = new ParseSettings(true);

        List<Node> nodes = xmlTreeBuilder.parseFragment(inputFragment, baseUri, errors, settings);

        assertEquals(1, nodes.size());
        assertTrue(nodes.get(0) instanceof Element);
        assertEquals("element1", nodes.get(0).nodeName());
    }

}