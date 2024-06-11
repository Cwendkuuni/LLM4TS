import org.jsoup.parser.*;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class XmlTreeBuilderTest {

    private XmlTreeBuilder xmlTreeBuilder;

    @Before
    public void setUp() {
        xmlTreeBuilder = new XmlTreeBuilder();
    }

    @Test
    public void testDefaultSettings() {
        ParseSettings settings = xmlTreeBuilder.defaultSettings();
        assertEquals(ParseSettings.preserveCase, settings);
    }

    @Test
    public void testParse() {
        String input = "<root><element>Content</element></root>";
        String baseUri = "https://www.example.com";
        Document doc = xmlTreeBuilder.parse(input, baseUri);
        assertEquals(input, doc.outerHtml());
        assertEquals(baseUri, doc.baseUri());
    }

    @Test
    public void testInitialiseParse() {
        String input = "<root><element>Content</element></root>";
        String baseUri = "https://www.example.com";
        ParseErrorList errors = ParseErrorList.noTracking();
        ParseSettings settings = ParseSettings.preserveCase;
        xmlTreeBuilder.initialiseParse(input, baseUri, errors, settings);
        assertTrue(xmlTreeBuilder.getCurrentElement() instanceof Document);
        assertEquals(Document.OutputSettings.Syntax.xml, xmlTreeBuilder.getDocument().outputSettings().syntax());
    }

    @Test
    public void testProcess() {
        Token.StartTag startTag = new Token.StartTag("element");
        xmlTreeBuilder.insert(startTag);
        assertEquals("element", xmlTreeBuilder.getCurrentElement().nodeName());

        Token.EndTag endTag = new Token.EndTag("element");
        xmlTreeBuilder.popStackToClose(endTag);
        assertTrue(xmlTreeBuilder.getStack().isEmpty());

        Token.Comment commentToken = new Token.Comment("This is a comment");
        xmlTreeBuilder.insert(commentToken);
        assertEquals("<!--This is a comment-->", xmlTreeBuilder.getDocument().outerHtml());

        Token.Character characterToken = new Token.Character("Content");
        xmlTreeBuilder.insert(characterToken);
        assertEquals("Content", xmlTreeBuilder.getCurrentElement().text());

        Token.Doctype doctype = new Token.Doctype("html");
        xmlTreeBuilder.insert(doctype);
        assertTrue(xmlTreeBuilder.getDocument().childNode(0) instanceof DocumentType);
    }

    @Test
    public void testInsertNode() {
        Element element = new Element("element");
        xmlTreeBuilder.setCurrentElement(new Element("root"));
        xmlTreeBuilder.insertNode(element);
        assertEquals(1, xmlTreeBuilder.getCurrentElement().childNodeSize());
        assertEquals(element, xmlTreeBuilder.getCurrentElement().childNode(0));
    }

    @Test
    public void testInsertStartTag() {
        Token.StartTag startTag = new Token.StartTag("element");
        Element element = xmlTreeBuilder.insert(startTag);
        assertEquals("element", element.nodeName());

        assertTrue(startTag.isSelfClosing());
        TokenizerState tokenizerState = xmlTreeBuilder.getTokeniser().getState();
        assertEquals(TokenizerState.Default, tokenizerState);
    }

    @Test
    public void testInsertComment() {
        Token.Comment commentToken = new Token.Comment("This is a comment");
        xmlTreeBuilder.insert(commentToken);
        assertEquals("<!--This is a comment-->", xmlTreeBuilder.getDocument().outerHtml());
    }

    @Test
    public void testInsertCharacter() {
        Token.Character characterToken = new Token.Character("Content");
        xmlTreeBuilder.insert(characterToken);
        assertEquals("Content", xmlTreeBuilder.getCurrentElement().text());
    }

    @Test
    public void testInsertDoctype() {
        Token.Doctype doctype = new Token.Doctype("html");
        xmlTreeBuilder.insert(doctype);
        assertTrue(xmlTreeBuilder.getDocument().childNode(0) instanceof DocumentType);
    }

    @Test
    public void testPopStackToClose() {
        Token.EndTag endTag = new Token.EndTag("element");
        xmlTreeBuilder.setCurrentElement(new Element("root"));
        xmlTreeBuilder.getStack().add(new Element("element"));
        xmlTreeBuilder.popStackToClose(endTag);
        assertTrue(xmlTreeBuilder.getStack().isEmpty());
    }

    @Test
    public void testParseFragment() {
        String inputFragment = "<element>Content</element>";
        String baseUri = "https://www.example.com";
        ParseErrorList errors = ParseErrorList.noTracking();
        ParseSettings settings = ParseSettings.preserveCase;
        List<Node> nodes = xmlTreeBuilder.parseFragment(inputFragment, baseUri, errors, settings);
        assertEquals(1, nodes.size());
        assertEquals(inputFragment, nodes.get(0).outerHtml());
    }
}