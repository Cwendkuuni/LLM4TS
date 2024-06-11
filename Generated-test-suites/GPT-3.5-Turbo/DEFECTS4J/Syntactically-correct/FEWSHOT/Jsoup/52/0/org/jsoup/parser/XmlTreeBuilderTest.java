package org.jsoup.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class XmlTreeBuilderTest {
    @Test
    public void testInitialiseParse() {
        // Test case 1
        XmlTreeBuilder builder = new XmlTreeBuilder();
        String input = "<html></html>";
        String baseUri = "http://example.com/";
        ParseErrorList errors = new ParseErrorList();
        builder.initialiseParse(input, baseUri, errors);
        assertEquals(1, builder.stack.size());
        assertEquals("http://example.com/", builder.stack.get(0).baseUri());

        // Test case 2
        builder = new XmlTreeBuilder();
        input = "<body></body>";
        baseUri = "http://example.com/";
        errors = new ParseErrorList();
        builder.initialiseParse(input, baseUri, errors);
        assertEquals(1, builder.stack.size());
        assertEquals("http://example.com/", builder.stack.get(0).baseUri());

        // Test case 3
        builder = new XmlTreeBuilder();
        input = "<head></head>";
        baseUri = "http://example.com/";
        errors = new ParseErrorList();
        builder.initialiseParse(input, baseUri, errors);
        assertEquals(1, builder.stack.size());
        assertEquals("http://example.com/", builder.stack.get(0).baseUri());
    }

    @Test
    public void testProcess() {
        // Test case 1
        XmlTreeBuilder builder = new XmlTreeBuilder();
        builder.stack.add(new Element(Tag.valueOf("div"), "http://example.com/"));
        Token token = new Token(TagType.StartTag, "p");
        builder.process(token);
        assertEquals(2, builder.stack.size());

        // Test case 2
        builder = new XmlTreeBuilder();
        builder.stack.add(new Element(Tag.valueOf("div"), "http://example.com/"));
        token = new Token(TagType.EndTag, "div");
        builder.process(token);
        assertEquals(0, builder.stack.size());

        // Test case 3
        builder = new XmlTreeBuilder();
        builder.stack.add(new Element(Tag.valueOf("div"), "http://example.com/"));
        token = new Token(TagType.Comment, "comment");
        builder.process(token);
        assertEquals(1, builder.currentElement().childNodes().size());
    }

    @Test
    public void testInsertNode() {
        // Test case 1
        XmlTreeBuilder builder = new XmlTreeBuilder();
        builder.stack.add(new Element(Tag.valueOf("div"), "http://example.com/"));
        Element childNode = new Element(Tag.valueOf("p"), "http://example.com/");
        builder.insertNode(childNode);
        assertEquals(1, builder.currentElement().childNodes().size());

        // Test case 2
        builder = new XmlTreeBuilder();
        builder.stack.add(new Element(Tag.valueOf("div"), "http://example.com/"));
        Node textNode = new TextNode("text", "http://example.com/");
        builder.insertNode(textNode);
        assertEquals(1, builder.currentElement().childNodes().size());
    }

    @Test
    public void testInsert() {
        // Test case 1
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Token.StartTag startTag = new Token.StartTag("p");
        Element element = builder.insert(startTag);
        assertEquals(Tag.valueOf("p"), element.tag());

        // Test case 2
        builder = new XmlTreeBuilder();
        startTag = new Token.StartTag("br");
        element = builder.insert(startTag);
        assertEquals(Tag.valueOf("br"), element.tag());

        // Test case 3
        builder = new XmlTreeBuilder();
        startTag = new Token.StartTag("div");
        startTag.setSelfClosing(true);
        element = builder.insert(startTag);
        assertEquals(Tag.valueOf("div"), element.tag());
        assertEquals(1, element.tag().isKnownTag());

        // Test case 4
        builder = new XmlTreeBuilder();
        startTag = new Token.StartTag("unknown");
        element = builder.insert(startTag);
        assertEquals(Tag.valueOf("unknown"), element.tag());
    }

    @Test
    public void testInsertComment() {
        // Test case 1
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Token.Comment commentToken = new Token.Comment("comment", "http://example.com/");
        builder.insert(commentToken);
        assertEquals(1, builder.currentElement().childNodes().size());

        // Test case 2
        builder = new XmlTreeBuilder();
        commentToken = new Token.Comment("<!-- xml -->", "http://example.com/");
        builder.insert(commentToken);
        assertEquals(1, builder.currentElement().childNodes().size());
    }

    @Test
    public void testInsertCharacter() {
        // Test case 1
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Token.Character characterToken = new Token.Character("text");
        builder.insert(characterToken);
        assertEquals(1, builder.currentElement().childNodes().size());

        // Test case 2
        builder = new XmlTreeBuilder();
        characterToken = new Token.Character("");
        builder.insert(characterToken);
        assertEquals(1, builder.currentElement().childNodes().size());
    }

    @Test
    public void testInsertDoctype() {
        // Test case 1
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Token.Doctype doctype = new Token.Doctype("html", "", "", "http://example.com/");
        builder.insert(doctype);
        assertEquals(1, builder.currentElement().childNodes().size());

        // Test case 2
        builder = new XmlTreeBuilder();
        doctype = new Token.Doctype("", "", "", "http://example.com/");
        builder.insert(doctype);
        assertEquals(1, builder.currentElement().childNodes().size());
    }

    @Test
    public void testPopStackToClose() {
        // Test case 1
        XmlTreeBuilder builder = new XmlTreeBuilder();
        builder.stack.add(new Element(Tag.valueOf("div"), "http://example.com/"));
        builder.stack.add(new Element(Tag.valueOf("p"), "http://example.com/"));
        Token.EndTag endTag = new Token.EndTag("div");
        builder.popStackToClose(endTag);
        assertEquals(1, builder.stack.size());

        // Test case 2
        builder = new XmlTreeBuilder();
        builder.stack.add(new Element(Tag.valueOf("div"), "http://example.com/"));
        builder.stack.add(new Element(Tag.valueOf("p"), "http://example.com/"));
        endTag = new Token.EndTag("p");
        builder.popStackToClose(endTag);
        assertEquals(1, builder.stack.size());

        // Test case 3
        builder = new XmlTreeBuilder();
        builder.stack.add(new Element(Tag.valueOf("div"), "http://example.com/"));
        builder.stack.add(new Element(Tag.valueOf("p"), "http://example.com/"));
        endTag = new Token.EndTag("span");
        builder.popStackToClose(endTag);
        assertEquals(2, builder.stack.size());
    }

    @Test
    public void testParseFragment() {
        // Test case 1
        XmlTreeBuilder builder = new XmlTreeBuilder();
        String inputFragment = "<p></p>";
        String baseUri = "http://example.com/";
        ParseErrorList errors = new ParseErrorList();
        List<Node> nodes = builder.parseFragment(inputFragment, baseUri, errors);
        assertEquals(1, nodes.size());

        // Test case 2
        builder = new XmlTreeBuilder();
        inputFragment = "<div></div>";
        baseUri = "http://example.com/";
        errors = new ParseErrorList();
        nodes = builder.parseFragment(inputFragment, baseUri, errors);
        assertEquals(1, nodes.size());
    }
}