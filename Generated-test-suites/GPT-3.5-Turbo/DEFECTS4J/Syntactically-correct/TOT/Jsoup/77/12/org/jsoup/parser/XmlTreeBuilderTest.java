import org.jsoup.nodes.Document;
import org.jsoup.parser.ParseErrorList;
import org.jsoup.parser.ParseSettings;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.Reader;
import java.io.StringReader;
import java.util.List;

public class XmlTreeBuilderTest {
    
    private XmlTreeBuilder xmlTreeBuilder;
    private String baseUri;
    private ParseSettings settings;

    @Before
    public void setUp() {
        xmlTreeBuilder = new XmlTreeBuilder();
        baseUri = "https://example.com";
        settings = ParseSettings.preserveCase;
    }
    
    @Test
    public void testDefaultSettings() {
        ParseSettings defaultSettings = xmlTreeBuilder.defaultSettings();
        Assert.assertEquals(ParseSettings.preserveCase, defaultSettings);
    }
    
    @Test
    public void testParseReaderBaseUri() {
        String xml = "<div><p>Hello, World!</p></div>";
        Reader reader = new StringReader(xml);
        Document document = xmlTreeBuilder.parse(reader, baseUri);
        Assert.assertNotNull(document);
        Assert.assertEquals(baseUri, document.baseUri());
        Assert.assertEquals("div", document.select("div").first().tagName());
        Assert.assertEquals("p", document.select("p").first().tagName());
        Assert.assertEquals("Hello, World!", document.select("p").first().text());
    }
    
    @Test
    public void testParseStringBaseUri() {
        String xml = "<div><p>Hello, World!</p></div>";
        Document document = xmlTreeBuilder.parse(xml, baseUri);
        Assert.assertNotNull(document);
        Assert.assertEquals(baseUri, document.baseUri());
        Assert.assertEquals("div", document.select("div").first().tagName());
        Assert.assertEquals("p", document.select("p").first().tagName());
        Assert.assertEquals("Hello, World!", document.select("p").first().text());
    }
    
    @Test
    public void testInitialiseParse() {
        String xml = "<div><p>Hello, World!</p></div>";
        Reader reader = new StringReader(xml);
        ParseErrorList errors = ParseErrorList.noTracking();
        xmlTreeBuilder.initialiseParse(reader, baseUri, errors, settings);
        Assert.assertNotNull(xmlTreeBuilder.stack);
        Assert.assertEquals(1, xmlTreeBuilder.stack.size());
        Assert.assertEquals("div", xmlTreeBuilder.stack.get(0).tagName());
        Assert.assertEquals(settings, xmlTreeBuilder.stack.get(0).baseUri());
    }
    
    @Test
    public void testProcessStartTagToken() {
        String xml = "<div><p>Hello, World!</p></div>";
        Reader reader = new StringReader(xml);
        ParseErrorList errors = ParseErrorList.noTracking();
        xmlTreeBuilder.initialiseParse(reader, baseUri, errors, settings);
        Token token = new Token.StartTag("p");
        xmlTreeBuilder.process(token);
        Assert.assertEquals(2, xmlTreeBuilder.stack.size());
        Assert.assertEquals("p", xmlTreeBuilder.currentElement().tagName());
    }
    
    @Test
    public void testProcessEndTagToken() {
        String xml = "<div><p>Hello, World!</p></div>";
        Reader reader = new StringReader(xml);
        ParseErrorList errors = ParseErrorList.noTracking();
        xmlTreeBuilder.initialiseParse(reader, baseUri, errors, settings);
        Token startToken = new Token.StartTag("p");
        xmlTreeBuilder.process(startToken);
        Token endToken = new Token.EndTag("p");
        xmlTreeBuilder.process(endToken);
        Assert.assertEquals(1, xmlTreeBuilder.stack.size());
        Assert.assertEquals("div", xmlTreeBuilder.currentElement().tagName());
    }
    
    @Test
    public void testProcessCommentToken() {
        String xml = "<!-- This is a comment -->";
        Reader reader = new StringReader(xml);
        ParseErrorList errors = ParseErrorList.noTracking();
        xmlTreeBuilder.initialiseParse(reader, baseUri, errors, settings);
        Token token = new Token.Comment("This is a comment");
        xmlTreeBuilder.process(token);
        Assert.assertEquals(1, xmlTreeBuilder.stack.size());
        Assert.assertEquals("<!-- This is a comment -->", xmlTreeBuilder.currentElement().html());
    }
    
    @Test
    public void testProcessCharacterToken() {
        String xml = "<p>Hello, World!</p>";
        Reader reader = new StringReader(xml);
        ParseErrorList errors = ParseErrorList.noTracking();
        xmlTreeBuilder.initialiseParse(reader, baseUri, errors, settings);
        Token token = new Token.Character("Hello, World!");
        xmlTreeBuilder.process(token);
        Assert.assertEquals(1, xmlTreeBuilder.stack.size());
        Assert.assertEquals("Hello, World!", xmlTreeBuilder.currentElement().text());
    }
    
    @Test
    public void testProcessDoctypeToken() {
        String xml = "<!DOCTYPE html>";
        Reader reader = new StringReader(xml);
        ParseErrorList errors = ParseErrorList.noTracking();
        xmlTreeBuilder.initialiseParse(reader, baseUri, errors, settings);
        Token token = new Token.Doctype("html");
        xmlTreeBuilder.process(token);
        Assert.assertEquals(1, xmlTreeBuilder.stack.size());
        Assert.assertEquals("!DOCTYPE html", xmlTreeBuilder.currentElement().toString());
    }
    
    @Test
    public void testInsertNode() {
        String xml = "<div>Hello, <em>World!</em></div>";
        Reader reader = new StringReader(xml);
        ParseErrorList errors = ParseErrorList.noTracking();
        xmlTreeBuilder.initialiseParse(reader, baseUri, errors, settings);
        Token startToken = new Token.StartTag("p");
        xmlTreeBuilder.process(startToken);
        Assert.assertEquals(2, xmlTreeBuilder.currentElement().childNodeSize());
        Token endToken = new Token.EndTag("p");
        xmlTreeBuilder.process(endToken);
        Assert.assertEquals(1, xmlTreeBuilder.currentElement().childNodeSize());
    }
    
    @Test
    public void testInsertStartTagToken() {
        String xml = "<div><p>Hello, World!</p></div>";
        Reader reader = new StringReader(xml);
        ParseErrorList errors = ParseErrorList.noTracking();
        xmlTreeBuilder.initialiseParse(reader, baseUri, errors, settings);
        Token.StartTag startTag = new Token.StartTag("p");
        xmlTreeBuilder.insert(startTag);
        Assert.assertEquals(2, xmlTreeBuilder.stack.size());
        Assert.assertEquals("p", xmlTreeBuilder.currentElement().tagName());
    }
    
    @Test
    public void testInsertSelfClosingTagToken() {
        String xml = "<div><img src=\"image.jpg\"></div>";
        Reader reader = new StringReader(xml);
        ParseErrorList errors = ParseErrorList.noTracking();
        xmlTreeBuilder.initialiseParse(reader, baseUri, errors, settings);
        Token.StartTag startTag = new Token.StartTag("img");
        startTag.selfClosing = true;
        xmlTreeBuilder.insert(startTag);
        Assert.assertEquals(1, xmlTreeBuilder.stack.size());
        Assert.assertTrue(xmlTreeBuilder.currentElement().tag().isSelfClosing());
    }
    
    @Test
    public void testInsertCommentToken() {
        String xml = "<div><!-- This is a comment --></div>";
        Reader reader = new StringReader(xml);
        ParseErrorList errors = ParseErrorList.noTracking();
        xmlTreeBuilder.initialiseParse(reader, baseUri, errors, settings);
        Token.Comment commentToken = new Token.Comment("This is a comment");
        xmlTreeBuilder.insert(commentToken);
        Assert.assertEquals(1, xmlTreeBuilder.stack.size());
        Assert.assertEquals("<!-- This is a comment -->", xmlTreeBuilder.currentElement().childNode(0).toString());
    }
    
    @Test
    public void testInsertCharacterToken() {
        String xml = "<p>Hello, World!</p>";
        Reader reader = new StringReader(xml);
        ParseErrorList errors = ParseErrorList.noTracking();
        xmlTreeBuilder.initialiseParse(reader, baseUri, errors, settings);
        Token.Character characterToken = new Token.Character("Hello, World!");
        xmlTreeBuilder.insert(characterToken);
        Assert.assertEquals(1, xmlTreeBuilder.stack.size());
        Assert.assertEquals("Hello, World!", xmlTreeBuilder.currentElement().text());
    }
    
    @Test
    public void testInsertDoctypeToken() {
        String xml = "<!DOCTYPE html>";
        Reader reader = new StringReader(xml);
        ParseErrorList errors = ParseErrorList.noTracking();
        xmlTreeBuilder.initialiseParse(reader, baseUri, errors, settings);
        Token.Doctype doctypeToken = new Token.Doctype("html");
        doctypeToken.setPublicIdentifier(null);
        doctypeToken.setSystemIdentifier(null);
        xmlTreeBuilder.insert(doctypeToken);
        Assert.assertEquals(1, xmlTreeBuilder.stack.size());
        Assert.assertEquals("!DOCTYPE html", xmlTreeBuilder.currentElement().toString());
    }
    
    @Test
    public void testPopStackToClose() {
        String xml = "<div><p>Hello, World!</p></div>";
        Reader reader = new StringReader(xml);
        ParseErrorList errors = ParseErrorList.noTracking();
        xmlTreeBuilder.initialiseParse(reader, baseUri, errors, settings);
        Token.StartTag startTag = new Token.StartTag("p");
        xmlTreeBuilder.process(startTag);
        Token.EndTag endTag = new Token.EndTag("div");
        xmlTreeBuilder.popStackToClose(endTag);
        Assert.assertEquals(1, xmlTreeBuilder.stack.size());
        Assert.assertEquals("p", xmlTreeBuilder.currentElement().tagName());
    }
    
    @Test
    public void testParseFragment() {
        String xmlFragment = "<p>Hello, World!</p>";
        ParseErrorList errors = ParseErrorList.noTracking();
        List<Node> nodes = xmlTreeBuilder.parseFragment(xmlFragment, baseUri, errors, settings);
        Assert.assertEquals(1, nodes.size());
        Assert.assertEquals("p", nodes.get(0).nodeName());
        Assert.assertEquals("Hello, World!", ((Element) nodes.get(0)).text());
    }
}