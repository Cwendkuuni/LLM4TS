
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.parser.ParseErrorList;
import org.jsoup.parser.ParseSettings;
import org.jsoup.parser.Token;
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
        ParseSettings settings = xmlTreeBuilder.defaultSettings();
        assertNotNull(settings);
    }

    @Test
    public void testParseWithReaderAndBaseUri() {
        Reader input = new StringReader("<xml>data</xml>");
        String baseUri = "http://example.com";
        Document document = xmlTreeBuilder.parse(input, baseUri);
        assertNotNull(document);
    }

    @Test
    public void testParseWithStringAndBaseUri() {
        String input = "<xml>data</xml>";
        String baseUri = "http://example.com";
        Document document = xmlTreeBuilder.parse(input, baseUri);
        assertNotNull(document);
    }

    @Test
    public void testInitialiseParse() {
        Reader input = new StringReader("<xml>data</xml>");
        String baseUri = "http://example.com";
        ParseErrorList errors = ParseErrorList.noTracking();
        ParseSettings settings = ParseSettings.preserveCase;
        xmlTreeBuilder.initialiseParse(input, baseUri, errors, settings);
    }

    @Test
    public void testProcess() {
        Token token = new Token(Token.Type.StartTag);
        xmlTreeBuilder.process(token);
    }

    @Test
    public void testInsertNode() {
        xmlTreeBuilder.insertNode(new Node());
    }

    @Test
    public void testInsertStartTag() {
        Token.StartTag startTag = new Token.StartTag("tag", new ParseSettings());
        Element element = xmlTreeBuilder.insert(startTag);
        assertNotNull(element);
    }

    @Test
    public void testInsertComment() {
        Token.Comment commentToken = new Token.Comment("comment");
        xmlTreeBuilder.insert(commentToken);
    }

    @Test
    public void testInsertCharacter() {
        Token.Character characterToken = new Token.Character("data", false);
        xmlTreeBuilder.insert(characterToken);
    }

    @Test
    public void testInsertDoctype() {
        Token.Doctype doctypeToken = new Token.Doctype("doctype", "", "");
        xmlTreeBuilder.insert(doctypeToken);
    }

    @Test
    public void testPopStackToClose() {
        Token.EndTag endTag = new Token.EndTag("tag");
        xmlTreeBuilder.popStackToClose(endTag);
    }

    @Test
    public void testParseFragment() {
        String inputFragment = "<xml>data</xml>";
        String baseUri = "http://example.com";
        ParseErrorList errors = ParseErrorList.noTracking();
        ParseSettings settings = ParseSettings.preserveCase;
        List<Node> nodes = xmlTreeBuilder.parseFragment(inputFragment, baseUri, errors, settings);
        assertNotNull(nodes);
    }
}
