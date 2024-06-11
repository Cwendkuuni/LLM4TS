import org.jsoup.nodes.Document;
import org.jsoup.parser.ParseErrorList;
import org.jsoup.parser.ParseSettings;
import org.jsoup.parser.Token;
import org.jsoup.Jsoup;
import org.jsoup.parser.Parser;
import org.jsoup.helper.Validate;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.io.Reader;
import java.io.StringReader;
import java.util.List;

public class XmlTreeBuilderTest {
    private XmlTreeBuilder xmlTreeBuilder;

    @Before
    public void setup() {
        xmlTreeBuilder = new XmlTreeBuilder();
    }

    @Test
    public void testDefaultSettings() {
        ParseSettings settings = xmlTreeBuilder.defaultSettings();
        assertNotNull(settings);
    }

    @Test
    public void testParseWithReaderAndBaseUri() {
        StringReader reader = new StringReader("xml content");
        String baseUri = "http://example.com";
        Document document = xmlTreeBuilder.parse(reader, baseUri);
        assertNotNull(document);
    }

    @Test
    public void testParseWithStringAndBaseUri() {
        String input = "xml content";
        String baseUri = "http://example.com";
        Document document = xmlTreeBuilder.parse(input, baseUri);
        assertNotNull(document);
    }

    @Test
    public void testInitialiseParse() {
        StringReader reader = new StringReader("xml content");
        String baseUri = "http://example.com";
        ParseErrorList errorList = ParseErrorList.noTracking();
        ParseSettings settings = ParseSettings.preserveCase;
        
        xmlTreeBuilder.initialiseParse(reader, baseUri, errorList, settings);
    }

    @Test
    public void testProcessWithStartTagToken() {
        Token.StartTag startTag = new Token.StartTag("tag", "attributes");
        boolean result = xmlTreeBuilder.process(startTag);
        assertTrue(result);
    }

    @Test
    public void testProcessWithEndTagToken() {
        Token.EndTag endTag = new Token.EndTag("tag");
        boolean result = xmlTreeBuilder.process(endTag);
        assertTrue(result);
    }

    @Test
    public void testProcessWithCommentToken() {
        Token.Comment comment = new Token.Comment("comment");
        boolean result = xmlTreeBuilder.process(comment);
        assertTrue(result);
    }

    @Test
    public void testProcessWithCharacterToken() {
        Token.Character character = new Token.Character("character", false);
        boolean result = xmlTreeBuilder.process(character);
        assertTrue(result);
    }

    @Test
    public void testProcessWithDoctypeToken() {
        Token.Doctype doctype = new Token.Doctype("doctype", "public", "system", "pubSysKey");
        boolean result = xmlTreeBuilder.process(doctype);
        assertTrue(result);
    }

    @Test(expected = RuntimeException.class)
    public void testProcessWithUnexpectedToken() {
        Token token = new Token();
        boolean result = xmlTreeBuilder.process(token);
        assertFalse(result);
    }

    @Test
    public void testInsertNode() {
        Document document = Jsoup.parse("");
        xmlTreeBuilder.insertNode(document);
        assertEquals(document, xmlTreeBuilder.currentElement().childNode(0));
    }

    @Test
    public void testInsertWithStartTagToken() {
        Token.StartTag startTag = new Token.StartTag("tag", "attributes");
        Element element = xmlTreeBuilder.insert(startTag);
        assertNotNull(element);
    }

    @Test
    public void testInsertWithCommentToken() {
        Token.Comment comment = new Token.Comment("comment");
        xmlTreeBuilder.insert(comment);
    }

    @Test
    public void testInsertWithCharacterToken() {
        Token.Character character = new Token.Character("character", false);
        xmlTreeBuilder.insert(character);
    }

    @Test
    public void testInsertWithDoctypeToken() {
        Token.Doctype doctype = new Token.Doctype("doctype", "public", "system", "pubSysKey");
        xmlTreeBuilder.insert(doctype);
    }

    @Test
    public void testPopStackToClose() {
        Token.EndTag endTag = new Token.EndTag("tag");
        xmlTreeBuilder.popStackToClose(endTag);
    }

    @Test
    public void testParseFragment() {
        String inputFragment = "xml fragment";
        String baseUri = "http://example.com";
        ParseErrorList errors = ParseErrorList.noTracking();
        ParseSettings settings = ParseSettings.preserveCase;

        List<Node> nodes = xmlTreeBuilder.parseFragment(inputFragment, baseUri, errors, settings);
        assertNotNull(nodes);
    }
}