import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.Before;
import org.junit.Test;

import java.io.StringReader;

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
        assertTrue(settings.preserveCase());
    }

    @Test
    public void testInitialiseParse() {
        Reader input = new StringReader("<root></root>");
        String baseUri = "https://example.com";
        Parser parser = new Parser(xmlTreeBuilder);

        xmlTreeBuilder.initialiseParse(input, baseUri, parser);

        Document doc = xmlTreeBuilder.getDocument();
        assertEquals(baseUri, doc.baseUri());
        assertTrue(doc.outputSettings().syntax() == Document.OutputSettings.Syntax.xml);
    }

    @Test
    public void testParseReaderString() {
        Reader input = new StringReader("<root></root>");
        String baseUri = "https://example.com";

        Document doc = xmlTreeBuilder.parse(input, baseUri);

        assertEquals(baseUri, doc.baseUri());
        assertTrue(doc.outputSettings().syntax() == Document.OutputSettings.Syntax.xml);
        assertTrue(doc.body().children().isEmpty());
    }

    @Test
    public void testParseStringString() {
        String input = "<root></root>";
        String baseUri = "https://example.com";

        Document doc = xmlTreeBuilder.parse(input, baseUri);

        assertEquals(baseUri, doc.baseUri());
        assertTrue(doc.outputSettings().syntax() == Document.OutputSettings.Syntax.xml);
        assertTrue(doc.body().children().isEmpty());
    }

    @Test
    public void testProcessStartTag() {
        Token.StartTag startTag = new Token.StartTag("tag", "name", false);
        
        xmlTreeBuilder.process(startTag);

        Document doc = xmlTreeBuilder.getDocument();
        assertEquals(1, doc.children().size());
        assertEquals("tag", doc.child(0).nodeName());
    }

    @Test
    public void testProcessEndTag() {
        Token.EndTag endTag = new Token.EndTag("tag");

        xmlTreeBuilder.process(endTag);

        Document doc = xmlTreeBuilder.getDocument();
        assertTrue(doc.children().isEmpty());
    }

    @Test
    public void testProcessComment() {
        Token.Comment comment = new Token.Comment("This is a comment");

        xmlTreeBuilder.process(comment);

        Document doc = xmlTreeBuilder.getDocument();
        assertEquals(1, doc.childNodes().size());
        assertTrue(doc.childNode(0) instanceof Comment);
        assertEquals("This is a comment", ((Comment) doc.childNode(0)).getData());
    }

    @Test
    public void testProcessCharacter() {
        Token.Character character = new Token.Character("This is a character");

        xmlTreeBuilder.process(character);

        Document doc = xmlTreeBuilder.getDocument();
        assertEquals(1, doc.childNodes().size());
        assertTrue(doc.childNode(0) instanceof TextNode);
        assertEquals("This is a character", ((TextNode) doc.childNode(0)).getWholeText());
    }

    @Test
    public void testProcessDoctype() {
        Token.Doctype doctype = new Token.Doctype("html", "", "");

        xmlTreeBuilder.process(doctype);

        Document doc = xmlTreeBuilder.getDocument();
        assertEquals(1, doc.childNodes().size());
        assertTrue(doc.childNode(0) instanceof DocumentType);
        DocumentType documentType = (DocumentType) doc.childNode(0);
        assertEquals("html", documentType.getName());
        assertEquals("", documentType.getPublicIdentifier());
        assertEquals("", documentType.getSystemIdentifier());
        assertEquals("", documentType.getPubSysKey());
    }

    @Test
    public void testPopStackToClose() {
        xmlTreeBuilder.process(new Token.StartTag("tag1", "name1", false));
        xmlTreeBuilder.process(new Token.StartTag("tag2", "name2", false));
        xmlTreeBuilder.process(new Token.StartTag("tag3", "name3", false));
        xmlTreeBuilder.process(new Token.EndTag("tag2"));
        
        Document doc = xmlTreeBuilder.getDocument();
        assertEquals(1, doc.children().size());
        assertEquals("tag1", doc.child(0).nodeName());
    }

    @Test
    public void testParseFragment() {
        String inputFragment = "<tag>content</tag>";
        String baseUri = "https://example.com";

        List<Node> nodes = xmlTreeBuilder.parseFragment(inputFragment, baseUri, new Parser(xmlTreeBuilder));

        assertEquals(1, nodes.size());
        assertTrue(nodes.get(0) instanceof Element);
        Element element = (Element) nodes.get(0);
        assertEquals("tag", element.nodeName());
        assertEquals("content", element.text());
    }
}