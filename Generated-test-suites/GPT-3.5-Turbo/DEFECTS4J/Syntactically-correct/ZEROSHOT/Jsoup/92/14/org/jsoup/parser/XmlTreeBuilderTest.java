import org.junit.Before;
import org.junit.Test;
import org.jsoup.parser.*;

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
    public void testInitialiseParse() {
        Reader input = new StringReader("<xml></xml>");
        String baseUri = "http://example.com";
        Parser parser = new Parser(xmlTreeBuilder);
        xmlTreeBuilder.initialiseParse(input, baseUri, parser);
        assertNotNull(xmlTreeBuilder.doc);
        assertEquals(xmlTreeBuilder.doc.baseUri(), baseUri);
        assertEquals(xmlTreeBuilder.doc.outputSettings().syntax(), Document.OutputSettings.Syntax.xml);
    }

    @Test
    public void testParseWithReaderAndBaseUri() {
        Reader input = new StringReader("<xml></xml>");
        String baseUri = "http://example.com";
        Document doc = xmlTreeBuilder.parse(input, baseUri);
        assertNotNull(doc);
        assertEquals(baseUri, doc.baseUri());
    }

    @Test
    public void testParseWithStringAndBaseUri() {
        String input = "<xml></xml>";
        String baseUri = "http://example.com";
        Document doc = xmlTreeBuilder.parse(input, baseUri);
        assertNotNull(doc);
        assertEquals(baseUri, doc.baseUri());
    }

    @Test
    public void testProcessStartTag() {
        Token.StartTag startTag = new Token.StartTag("div");
        Element element = xmlTreeBuilder.insert(startTag);
        assertNotNull(element);
        assertEquals("div", element.tagName());
    }

    @Test
    public void testProcessEndTag() {
        Token.EndTag endTag = new Token.EndTag("div");
        xmlTreeBuilder.stack.push(xmlTreeBuilder.doc.createElement("div"));
        xmlTreeBuilder.insert(endTag);
        assertTrue(xmlTreeBuilder.stack.isEmpty());
    }

    @Test
    public void testProcessComment() {
        Token.Comment comment = new Token.Comment("Test comment");
        xmlTreeBuilder.insert(comment);
        assertEquals("<!--Test comment-->", xmlTreeBuilder.doc.body().html());
    }

    @Test
    public void testProcessCharacter() {
        Token.Character character = new Token.Character("Test character");
        xmlTreeBuilder.insert(character);
        assertEquals("Test character", xmlTreeBuilder.doc.body().html());
    }

    @Test
    public void testProcessDoctype() {
        Token.Doctype doctype = new Token.Doctype("html");
        xmlTreeBuilder.insert(doctype);
        assertEquals("<!DOCTYPE html>", xmlTreeBuilder.doc.html());
    }

    @Test
    public void testPopStackToClose() {
        Token.EndTag endTag = new Token.EndTag("div");
        xmlTreeBuilder.stack.push(xmlTreeBuilder.doc.createElement("div"));
        xmlTreeBuilder.popStackToClose(endTag);
        assertTrue(xmlTreeBuilder.stack.isEmpty());
    }

    @Test
    public void testParseFragmentWithoutContext() {
        String inputFragment = "<div></div>";
        Parser parser = new Parser(xmlTreeBuilder);
        List<Node> nodes = xmlTreeBuilder.parseFragment(inputFragment, "http://example.com", parser);
        assertEquals(1, nodes.size());
        Element element = xmlTreeBuilder.doc.selectFirst("div");
        assertNotNull(element);
    }

    @Test
    public void testParseFragmentWithContext() {
        String inputFragment = "<div></div>";
        Element context = xmlTreeBuilder.doc.createElement("div");
        Parser parser = new Parser(xmlTreeBuilder);
        List<Node> nodes = xmlTreeBuilder.parseFragment(inputFragment, context, "http://example.com", parser);
        assertEquals(1, nodes.size());
        Element element = xmlTreeBuilder.doc.selectFirst("div");
        assertNotNull(element);
    }
}

