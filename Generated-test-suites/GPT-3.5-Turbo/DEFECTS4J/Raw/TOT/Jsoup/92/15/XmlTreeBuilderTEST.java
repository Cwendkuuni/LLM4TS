import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

public class XmlTreeBuilderTest {

    @Test
    public void testDefaultSettings() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        ParseSettings settings = builder.defaultSettings();
        assertEquals("preserveCase", settings.toString());
    }

    @Test
    public void testInitialiseParse() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Reader input = new StringReader("<tag>Hello World</tag>");
        String baseUri = "https://example.com";
        Parser parser = Parser.xmlParser();
        builder.initialiseParse(input, baseUri, parser);
        assertEquals("https://example.com", builder.baseUri());
        assertEquals(Parser.xmlParser(), builder.parser());
    }

    @Test
    public void testParse() {
        String input = "<tag>Hello World</tag>";
        String baseUri = "https://example.com";
        Document document = Jsoup.parse(input, baseUri, Parser.xmlParser());

        assertNotNull(document);
        assertEquals("https://example.com", document.baseUri());
        assertEquals(0, document.children().size());
        assertEquals("tag", document.nodeName());
    }

    @Test
    public void testProcessStartTag() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Token token = new Token.StartTag("tag", "attribute", true);
        boolean result = builder.process(token);

        assertTrue(result);
        assertEquals(1, builder.doc.children().size());
        assertEquals("tag", builder.doc.children().get(0).nodeName());
        assertEquals("attribute", builder.doc.children().get(0).attr("attribute"));
    }

    @Test
    public void testProcessEndTag() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        builder.stack.push(new Element("tag1"));
        builder.stack.push(new Element("tag2"));

        Token token = new Token.EndTag("tag2");
        boolean result = builder.process(token);

        assertTrue(result);
        assertEquals(1, builder.stack.size());
        assertEquals("tag1", builder.stack.peek().nodeName());
    }

    @Test
    public void testProcessComment() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Token token = new Token.Comment("Hello World");
        boolean result = builder.process(token);

        assertTrue(result);
        assertEquals(1, builder.doc.children().size());
        assertEquals("#comment", builder.doc.children().get(0).nodeName());
        assertEquals("Hello World", builder.doc.children().get(0).toString());
    }

    @Test
    public void testProcessCharacter() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Token token = new Token.Character("Hello World");
        boolean result = builder.process(token);

        assertTrue(result);
        assertEquals(1, builder.doc.children().size());
        assertEquals("#text", builder.doc.children().get(0).nodeName());
        assertEquals("Hello World", builder.doc.children().get(0).toString());
    }

    @Test
    public void testProcessDoctype() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Token token = new Token.Doctype("html", "pub", "sys");
        boolean result = builder.process(token);

        assertTrue(result);
        assertEquals(1, builder.doc.children().size());
        assertEquals("!DOCTYPE", builder.doc.children().get(0).nodeName());
        assertEquals("html", builder.doc.children().get(0).attr("name"));
        assertEquals("pub", builder.doc.children().get(0).attr("publicId"));
        assertEquals("sys", builder.doc.children().get(0).attr("systemId"));
    }

    @Test
    public void testInsertNode() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        builder.stack.push(new Element("tag"));

        Node node = new Element("child");
        builder.insertNode(node);

        assertEquals(1, builder.stack.peek().childNodes().size());
        assertEquals("child", builder.stack.peek().childNodes().get(0).nodeName());
    }

    @Test
    public void testInsertStartTag() {
        XmlTreeBuilder builder = new XmlTreeBuilder();

        Token.StartTag startTag = new Token.StartTag("tag", "attribute", false);
        Element element = builder.insert(startTag);

        assertEquals(1, builder.doc.children().size());
        assertEquals("tag", element.nodeName());
        assertFalse(element.isSelfClosing());
        assertNull(builder.doc.children().get(0).attr("attribute"));
    }

    @Test
    public void testInsertSelfClosingStartTag() {
        XmlTreeBuilder builder = new XmlTreeBuilder();

        Token.StartTag startTag = new Token.StartTag("tag", "attribute", true);
        Element element = builder.insert(startTag);

        assertEquals(1, builder.doc.children().size());
        assertEquals("tag", element.nodeName());
        assertTrue(element.isSelfClosing());
        assertNull(builder.doc.children().get(0).attr("attribute"));
    }

    @Test
    public void testInsertCommentToken() {
        XmlTreeBuilder builder = new XmlTreeBuilder();

        Token.Comment commentToken = new Token.Comment("Hello World");
        builder.insert(commentToken);

        assertEquals(1, builder.doc.children().size());
        assertEquals("#comment", builder.doc.children().get(0).nodeName());
        assertEquals("Hello World", builder.doc.children().get(0).toString());
    }

    @Test
    public void testInsertCharacterToken() {
        XmlTreeBuilder builder = new XmlTreeBuilder();

        Token.Character characterToken = new Token.Character("Hello World");
        builder.insert(characterToken);

        assertEquals(1, builder.doc.children().size());
        assertEquals("#text", builder.doc.children().get(0).nodeName());
        assertEquals("Hello World", builder.doc.children().get(0).toString());
    }

    @Test
    public void testInsertDoctypeToken() {
        XmlTreeBuilder builder = new XmlTreeBuilder();

        Token.Doctype doctypeToken = new Token.Doctype("html", "pub", "sys");
        builder.insert(doctypeToken);

        assertEquals(1, builder.doc.children().size());
        assertEquals("!DOCTYPE", builder.doc.children().get(0).nodeName());
        assertEquals("html", builder.doc.children().get(0).attr("name"));
        assertEquals("pub", builder.doc.children().get(0).attr("publicId"));
        assertEquals("sys", builder.doc.children().get(0).attr("systemId"));
    }

    @Test
    public void testPopStackToClose() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        builder.stack.push(new Element("tag1"));
        builder.stack.push(new Element("tag2"));
        builder.stack.push(new Element("tag3"));

        Token.EndTag endTag = new Token.EndTag("tag2");
        builder.popStackToClose(endTag);

        assertEquals(2, builder.stack.size());
        assertEquals("tag1", builder.stack.get(0).nodeName());
        assertEquals("tag3", builder.stack.get(1).nodeName());
    }

    @Test
    public void testParseFragment() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        String inputFragment = "<tag>Hello World</tag>";
        String baseUri = "https://example.com";
        Parser parser = Parser.xmlParser();

        List<Node> nodes = builder.parseFragment(inputFragment, baseUri, parser);

        assertEquals(1, nodes.size());
        assertEquals("tag", nodes.get(0).nodeName());
        assertEquals("Hello World", nodes.get(0).toString());
    }

    @Test
    public void testParseFragmentWithContext() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        String inputFragment = "<tag>Hello World</tag>";
        Element context = new Element("context");
        String baseUri = "https://example.com";
        Parser parser = Parser.xmlParser();

        List<Node> nodes = builder.parseFragment(inputFragment, context, baseUri, parser);

        assertEquals(1, nodes.size());
        assertEquals("tag", nodes.get(0).nodeName());
        assertEquals("Hello World", nodes.get(0).toString());
        assertEquals(1, context.childNodes().size());
        assertEquals("tag", context.childNodes().get(0).nodeName());
    }
    
    // Add more test cases

}