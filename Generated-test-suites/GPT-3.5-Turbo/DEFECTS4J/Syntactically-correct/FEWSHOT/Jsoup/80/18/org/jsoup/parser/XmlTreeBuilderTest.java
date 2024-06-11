import org.jsoup.Jsoup;
import org.jsoup.helper.Validate;
import org.jsoup.nodes.*;
import org.junit.Test;

import java.io.Reader;
import java.io.StringReader;
import java.util.List;

import static org.junit.Assert.*;

public class XmlTreeBuilderTest {

    @Test
    public void testDefaultSettings() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        ParseSettings settings = builder.defaultSettings();

        assertNotNull(settings);
        assertEquals(ParseSettings.preserveCase, settings);
    }

    @Test
    public void testParseWithReaderAndBaseUri() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        StringReader reader = new StringReader("<xml></xml>");
        String baseUri = "http://example.com";
        Document document = builder.parse(reader, baseUri);

        assertNotNull(document);
        assertEquals("http://example.com", document.baseUri());
        assertEquals(1, document.childNodes().size());
        assertTrue(document.childNodes().get(0) instanceof Element);
        assertEquals("xml", ((Element) document.childNodes().get(0)).tagName());
    }

    @Test
    public void testParseWithStringAndBaseUri() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        String input = "<xml></xml>";
        String baseUri = "http://example.com";
        Document document = builder.parse(input, baseUri);

        assertNotNull(document);
        assertEquals("http://example.com", document.baseUri());
        assertEquals(1, document.childNodes().size());
        assertTrue(document.childNodes().get(0) instanceof Element);
        assertEquals("xml", ((Element) document.childNodes().get(0)).tagName());
    }

    @Test
    public void testInitialiseParse() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        StringReader reader = new StringReader("<xml></xml>");
        String baseUri = "http://example.com";
        ParseErrorList errors = new ParseErrorList();
        ParseSettings settings = ParseSettings.preserveCase;
        builder.initialiseParse(reader, baseUri, errors, settings);

        assertEquals(1, builder.stack.size());
        assertEquals("http://example.com", builder.baseUri);
        assertEquals(settings, builder.settings);
    }

    @Test
    public void testProcessStartTag() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Token token = new Token.StartTag("xml");
        builder.stack.add(new Element(Tag.valueOf("div"), "http://example.com", new Attributes()));

        assertTrue(builder.process(token));
        assertEquals(2, builder.stack.size());
        assertTrue(builder.stack.get(1) instanceof Element);
        assertEquals("http://example.com", ((Element) builder.stack.get(1)).baseUri());
        assertEquals("xml", ((Element) builder.stack.get(1)).tagName());
    }

    @Test
    public void testProcessEndTag() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Token token = new Token.EndTag("div");
        builder.stack.add(new Element(Tag.valueOf("xml"), "http://example.com", new Attributes()));
        builder.stack.add(new Element(Tag.valueOf("div"), "http://example.com", new Attributes()));
        builder.stack.add(new Element(Tag.valueOf("span"), "http://example.com", new Attributes()));

        assertTrue(builder.process(token));
        assertEquals(2, builder.stack.size());
        assertEquals("xml", ((Element) builder.stack.get(0)).tagName());
        assertEquals("div", ((Element) builder.stack.get(1)).tagName());
    }

    @Test
    public void testProcessComment() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Token token = new Token.Comment("test comment");
        builder.stack.add(new Element(Tag.valueOf("div"), "http://example.com", new Attributes()));

        assertTrue(builder.process(token));
        assertEquals(1, builder.stack.size());
        assertEquals("http://example.com", ((Element) builder.stack.get(0)).baseUri());
        assertEquals("test comment", ((Element) builder.stack.get(0)).childNodes().get(0).toString());
    }

    @Test
    public void testProcessCharacter() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Token token = new Token.Character("test character");
        builder.stack.add(new Element(Tag.valueOf("div"), "http://example.com", new Attributes()));

        assertTrue(builder.process(token));
        assertEquals(1, builder.stack.size());
        assertEquals("http://example.com", ((Element) builder.stack.get(0)).baseUri());
        assertEquals("test character", ((Element) builder.stack.get(0)).childNodes().get(0).toString());
    }

    @Test
    public void testProcessDoctype() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Token token = new Token.Doctype("test", "public", "system");
        builder.stack.add(new Element(Tag.valueOf("div"), "http://example.com", new Attributes()));

        assertTrue(builder.process(token));
        assertEquals(1, builder.stack.size());
        assertEquals("http://example.com", ((Element) builder.stack.get(0)).baseUri());
        assertEquals("test", ((Element) builder.stack.get(0)).childNodes().get(0).toString());
        assertEquals("public", ((Element) builder.stack.get(0)).childNodes().get(0).attr("publicIdentifier"));
        assertEquals("system", ((Element) builder.stack.get(0)).childNodes().get(0).attr("systemIdentifier"));
    }

    @Test
    public void testInsertNodeElement() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Element element = new Element(Tag.valueOf("div"), "http://example.com", new Attributes());
        builder.stack.add(new Element(Tag.valueOf("body"), "http://example.com", new Attributes()));

        builder.insertNode(element);
        assertEquals(1, ((Element) builder.stack.get(0)).childNodes().size());
        assertEquals("div", ((Element) builder.stack.get(0)).childNodes().get(0).nodeName());
    }

    @Test
    public void testInsertNodeComment() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Comment comment = new Comment("test comment");
        builder.stack.add(new Element(Tag.valueOf("body"), "http://example.com", new Attributes()));

        builder.insertNode(comment);
        assertEquals(1, ((Element) builder.stack.get(0)).childNodes().size());
        assertEquals("#comment", ((Element) builder.stack.get(0)).childNodes().get(0).nodeName());
        assertEquals("test comment", ((Element) builder.stack.get(0)).childNodes().get(0).toString());
    }

    @Test
    public void testInsertNodeCharacter() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        TextNode character = new TextNode("test character");
        builder.stack.add(new Element(Tag.valueOf("body"), "http://example.com", new Attributes()));

        builder.insertNode(character);
        assertEquals(1, ((Element) builder.stack.get(0)).childNodes().size());
        assertEquals("#text", ((Element) builder.stack.get(0)).childNodes().get(0).nodeName());
        assertEquals("test character", ((Element) builder.stack.get(0)).childNodes().get(0).toString());
    }

    @Test
    public void testInsertNodeDoctype() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        DocumentType doctype = new DocumentType("test", "public", "system");
        doctype.setPubSysKey("test key");
        builder.stack.add(new Element(Tag.valueOf("body"), "http://example.com", new Attributes()));

        builder.insertNode(doctype);
        assertEquals(1, ((Element) builder.stack.get(0)).childNodes().size());
        assertEquals("!DOCTYPE", ((Element) builder.stack.get(0)).childNodes().get(0).nodeName());
        assertEquals("test", ((Element) builder.stack.get(0)).childNodes().get(0).attr("name"));
        assertEquals("test key", ((Element) builder.stack.get(0)).childNodes().get(0).attr("pubSysKey"));
        assertEquals("public", ((Element) builder.stack.get(0)).childNodes().get(0).attr("publicIdentifier"));
        assertEquals("system", ((Element) builder.stack.get(0)).childNodes().get(0).attr("systemIdentifier"));
    }

    @Test
    public void testInsertStartTag() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Token.StartTag startTag = new Token.StartTag("div");
        startTag.attributes.put("id", "testId");
        startTag.attributes.put("class", "testClass");
        startTag.attributes.put("custom", "testCustom");
        builder.stack.add(new Element(Tag.valueOf("body"), "http://example.com", new Attributes()));

        Element element = builder.insert(startTag);
        assertEquals(1, ((Element) builder.stack.get(0)).childNodes().size());
        assertEquals("div", ((Element) builder.stack.get(0)).childNodes().get(0).nodeName());
        assertEquals("testId", element.attr("id"));
        assertEquals("testClass", element.attr("class"));
        assertEquals("testCustom", element.attr("custom"));
    }

    @Test
    public void testInsertEndTag() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Token.EndTag endTag = new Token.EndTag("div");
        builder.stack.add(new Element(Tag.valueOf("body"), "http://example.com", new Attributes()));
        builder.stack.add(new Element(Tag.valueOf("div"), "http://example.com", new Attributes()));
        builder.stack.add(new Element(Tag.valueOf("span"), "http://example.com", new Attributes()));

        builder.popStackToClose(endTag);
        assertEquals(2, builder.stack.size());
        assertEquals("body", ((Element) builder.stack.get(0)).tagName());
        assertEquals("span", ((Element) builder.stack.get(1)).tagName());
    }

    @Test
    public void testPopStackToCloseNotFound() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Token.EndTag endTag = new Token.EndTag("div");
        builder.stack.add(new Element(Tag.valueOf("body"), "http://example.com", new Attributes()));
        builder.stack.add(new Element(Tag.valueOf("span"), "http://example.com", new Attributes()));

        builder.popStackToClose(endTag);
        assertEquals(2, builder.stack.size());
        assertEquals("body", ((Element) builder.stack.get(0)).tagName());
        assertEquals("span", ((Element) builder.stack.get(1)).tagName());
    }

    @Test
    public void testParseFragment() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        String inputFragment = "<xml></xml>";
        String baseUri = "http://example.com";
        ParseErrorList errors = new ParseErrorList();
        ParseSettings settings = ParseSettings.preserveCase;

        List<Node> nodes = builder.parseFragment(inputFragment, baseUri, errors, settings);
        assertEquals(1, nodes.size());
        assertTrue(nodes.get(0) instanceof Element);
        assertEquals("xml", ((Element) nodes.get(0)).tagName());
    }
}