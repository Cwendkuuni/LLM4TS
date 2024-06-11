import org.jsoup.nodes.*;
import org.jsoup.parser.*;
import org.junit.Before;
import org.junit.Test;

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
        ParseSettings defaultSettings = xmlTreeBuilder.defaultSettings();
        assertNotNull(defaultSettings);
    }

    @Test
    public void testInitialiseParse() {
        StringReader input = new StringReader("");
        String baseUri = "http://example.com";
        Parser parser = new Parser(xmlTreeBuilder);
        xmlTreeBuilder.initialiseParse(input, baseUri, parser);
        // No assertions, just checking for exceptions
    }

    @Test
    public void testParseWithReaderAndBaseUri() {
        StringReader input = new StringReader("");
        String baseUri = "http://example.com";
        Document document = xmlTreeBuilder.parse(input, baseUri);
        assertNotNull(document);
    }

    @Test
    public void testParseWithStringAndBaseUri() {
        String input = "";
        String baseUri = "http://example.com";
        Document document = xmlTreeBuilder.parse(input, baseUri);
        assertNotNull(document);
    }

    @Test
    public void testProcessWithStartTag() {
        Token token = new Token.StartTag("", "", new Attributes());
        boolean result = xmlTreeBuilder.process(token);
        assertTrue(result);
    }

    @Test
    public void testProcessWithEndTag() {
        Token token = new Token.EndTag("");
        boolean result = xmlTreeBuilder.process(token);
        assertTrue(result);
    }

    @Test
    public void testProcessWithComment() {
        Token token = new Token.Comment("");
        boolean result = xmlTreeBuilder.process(token);
        assertTrue(result);
    }

    @Test
    public void testProcessWithCharacter() {
        Token token = new Token.Character("");
        boolean result = xmlTreeBuilder.process(token);
        assertTrue(result);
    }

    @Test
    public void testProcessWithDoctype() {
        Token token = new Token.Doctype("", "", "");
        boolean result = xmlTreeBuilder.process(token);
        assertTrue(result);
    }

    @Test
    public void testInsertNode() {
        Element element = new Element(Tag.valueOf("div"), "");
        xmlTreeBuilder.insertNode(element);
        assertEquals(element, xmlTreeBuilder.currentElement().childNodes().get(0));
    }

    @Test
    public void testInsertWithStartTag() {
        Token.StartTag startTag = new Token.StartTag("div", "", new Attributes());
        Element element = xmlTreeBuilder.insert(startTag);
        assertEquals(Tag.valueOf("div"), element.tag());
        assertEquals(element, xmlTreeBuilder.currentElement().childNodes().get(0));
    }

    @Test
    public void testInsertWithCommentToken() {
        Token.Comment commentToken = new Token.Comment("");
        xmlTreeBuilder.insert(commentToken);
        Comment comment = (Comment) xmlTreeBuilder.currentElement().childNodes().get(0);
        assertNotNull(comment);
    }

    @Test
    public void testInsertWithCharacterToken() {
        Token.Character characterToken = new Token.Character("");
        xmlTreeBuilder.insert(characterToken);
        Node node = xmlTreeBuilder.currentElement().childNodes().get(0);
        assertNotNull(node);
    }

    @Test
    public void testInsertWithDoctype() {
        Token.Doctype doctype = new Token.Doctype("html", "", "");
        xmlTreeBuilder.insert(doctype);
        DocumentType documentType = (DocumentType) xmlTreeBuilder.currentElement().childNodes().get(0);
        assertNotNull(documentType);
    }

    @Test
    public void testPopStackToClose() {
        Token.EndTag endTag = new Token.EndTag("div");
        xmlTreeBuilder.stack.add(new Element(Tag.valueOf("div"), ""));
        xmlTreeBuilder.popStackToClose(endTag);
        assertTrue(xmlTreeBuilder.stack.isEmpty());
    }

    @Test
    public void testParseFragmentWithInputFragmentAndBaseUri() {
        String inputFragment = "<div></div>";
        String baseUri = "http://example.com";
        Parser parser = new Parser(xmlTreeBuilder);
        List<Node> nodes = xmlTreeBuilder.parseFragment(inputFragment, baseUri, parser);
        assertFalse(nodes.isEmpty());
    }

    @Test
    public void testParseFragmentWithInputFragmentContextAndBaseUri() {
        String inputFragment = "<div></div>";
        Element context = new Element(Tag.valueOf("div"), "");
        String baseUri = "http://example.com";
        Parser parser = new Parser(xmlTreeBuilder);
        List<Node> nodes = xmlTreeBuilder.parseFragment(inputFragment, context, baseUri, parser);
        assertFalse(nodes.isEmpty());
    }
}