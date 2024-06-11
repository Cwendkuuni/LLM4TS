import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.Reader;
import java.io.StringReader;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.helper.Validate;
import org.jsoup.nodes.CDataNode;
import org.jsoup.nodes.Comment;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.DocumentType;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.jsoup.parser.ParseErrorList;
import org.jsoup.parser.ParseSettings;
import org.jsoup.parser.Parser;
import org.jsoup.parser.Tag;
import org.jsoup.parser.Token;
import org.junit.Before;
import org.junit.Test;

public class XmlTreeBuilderTest {

    private XmlTreeBuilder xmlTreeBuilder;
    private ParseErrorList errors;

    @Before
    public void setUp() {
        xmlTreeBuilder = new XmlTreeBuilder();
        errors = ParseErrorList.noTracking();
    }

    @Test
    public void testDefaultSettings() {
        ParseSettings defaultSettings = xmlTreeBuilder.defaultSettings();
        assertTrue(defaultSettings.preserveCase());
    }

    @Test
    public void testParseWithReaderAndBaseUri() {
        Reader reader = new StringReader("<tag>Hello World!</tag>");
        String baseUri = "http://www.example.com";
        Document document = xmlTreeBuilder.parse(reader, baseUri);
        assertEquals(baseUri, document.baseUri());
        assertEquals("<html>\n <head></head>\n <body>\n  <tag>Hello World!</tag>\n </body>\n</html>", document.outerHtml());
    }

    @Test
    public void testParseWithStringAndBaseUri() {
        String input = "<tag>Hello World!</tag>";
        String baseUri = "http://www.example.com";
        Document document = xmlTreeBuilder.parse(input, baseUri);
        assertEquals(baseUri, document.baseUri());
        assertEquals("<html>\n <head></head>\n <body>\n  <tag>Hello World!</tag>\n </body>\n</html>", document.outerHtml());
    }

    @Test
    public void testInitialiseParse() {
        Reader reader = new StringReader("<tag>Hello World!</tag>");
        String baseUri = "http://www.example.com";
        ParseSettings settings = ParseSettings.preserveCase;
        xmlTreeBuilder.initialiseParse(reader, baseUri, errors, settings);
        assertEquals(baseUri, xmlTreeBuilder.baseUri());
        assertEquals(settings, xmlTreeBuilder.settings());
    }

    @Test
    public void testProcess() {
        Token.StartTag startTag = new Token.StartTag("tag", Tag.valueOf("tag"), new ParseSettings(false));
        xmlTreeBuilder.insert(startTag);
        assertEquals("<tag></tag>", xmlTreeBuilder.currentElement().outerHtml());

        Token.EndTag endTag = new Token.EndTag("tag");
        xmlTreeBuilder.popStackToClose(endTag);
        assertNull(xmlTreeBuilder.currentElement());
    }

    @Test
    public void testInsertNode() {
        Element element = new Element(Tag.valueOf("tag"), "");
        xmlTreeBuilder.insertNode(element);
        assertEquals(element, xmlTreeBuilder.currentElement().child(0));
    }

    @Test
    public void testInsertStartTag() {
        Token.StartTag startTag = new Token.StartTag("tag", Tag.valueOf("tag"), new ParseSettings(false));
        Element element = xmlTreeBuilder.insert(startTag);
        assertEquals("<tag></tag>", element.outerHtml());
        assertTrue(element.isBlock());
        assertTrue(element.isKnownTag());
    }

    @Test
    public void testInsertComment() {
        Token.Comment commentToken = new Token.Comment("Comment");
        xmlTreeBuilder.insert(commentToken);
        Comment comment = (Comment) xmlTreeBuilder.currentElement().childNode(0);
        assertEquals("Comment", comment.getData());
    }

    @Test
    public void testInsertCharacter() {
        Token.Character characterToken = new Token.Character("Text");
        xmlTreeBuilder.insert(characterToken);
        TextNode textNode = (TextNode) xmlTreeBuilder.currentElement().childNode(0);
        assertEquals("Text", textNode.getWholeText());
    }

    @Test
    public void testInsertDoctype() {
        Token.Doctype doctype = new Token.Doctype("html", "", "");
        xmlTreeBuilder.insert(doctype);
        DocumentType documentType = (DocumentType) xmlTreeBuilder.currentElement().childNode(0);
        assertEquals("html", documentType.nodeName());
    }

    @Test
    public void testPopStackToClose() {
        Token.EndTag endTag = new Token.EndTag("tag");
        Element element = new Element(Tag.valueOf("tag"), "");
        xmlTreeBuilder.stack().add(element);
        xmlTreeBuilder.popStackToClose(endTag);
        assertNull(xmlTreeBuilder.currentElement());
    }

    @Test
    public void testParseFragment() {
        String inputFragment = "<tag>Hello World!</tag>";
        String baseUri = "http://www.example.com";
        List<Node> nodes = xmlTreeBuilder.parseFragment(inputFragment, baseUri, errors, ParseSettings.preserveCase);
        assertEquals(1, nodes.size());
        assertEquals("<tag>Hello World!</tag>", nodes.get(0).outerHtml());
    }
}