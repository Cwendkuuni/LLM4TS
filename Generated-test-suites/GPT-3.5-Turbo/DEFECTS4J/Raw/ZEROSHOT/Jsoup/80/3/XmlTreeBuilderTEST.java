package org.jsoup.parser;

import org.jsoup.Jsoup;
import org.jsoup.helper.Validate;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.select.Elements;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.List;

public class XmlTreeBuilderTest {
    @Test
    public void testDefaultSettings() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        ParseSettings settings = builder.defaultSettings();
        Assert.assertEquals(ParseSettings.preserveCase, settings);
    }

    @Test
    public void testParseWithReaderAndBaseUri() throws IOException {
        String input = "<html><body>Test</body></html>";
        Reader reader = new StringReader(input);
        String baseUri = "http://example.com";
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Document doc = builder.parse(reader, baseUri);
        Assert.assertNotNull(doc);
        Assert.assertEquals(baseUri, doc.baseUri());
        Assert.assertEquals("html", doc.child(0).tagName());
        Assert.assertEquals("body", doc.child(0).child(0).tagName());
        Assert.assertEquals("Test", doc.child(0).child(0).text());
    }

    @Test
    public void testParseWithStringAndBaseUri() throws IOException {
        String input = "<html><body>Test</body></html>";
        String baseUri = "http://example.com";
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Document doc = builder.parse(input, baseUri);
        Assert.assertNotNull(doc);
        Assert.assertEquals(baseUri, doc.baseUri());
        Assert.assertEquals("html", doc.child(0).tagName());
        Assert.assertEquals("body", doc.child(0).child(0).tagName());
        Assert.assertEquals("Test", doc.child(0).child(0).text());
    }

    @Test
    public void testInitialiseParse() {
        String input = "<html><body>Test</body></html>";
        String baseUri = "http://example.com";
        XmlTreeBuilder builder = new XmlTreeBuilder();
        builder.initialiseParse(new StringReader(input), baseUri, ParseErrorList.noTracking(), ParseSettings.preserveCase);
        Assert.assertEquals(ParseSettings.preserveCase, builder.settings);
        Assert.assertEquals("html", builder.doc.tagName());
        Assert.assertEquals("body", builder.currentElement().tagName());
    }

    @Test
    public void testProcess() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Assert.assertTrue(builder.process(new Token.StartTag("tag")));
        Assert.assertTrue(builder.process(new Token.EndTag("tag")));
        Assert.assertTrue(builder.process(new Token.Comment("comment")));
        Assert.assertTrue(builder.process(new Token.Character("character")));
        Assert.assertTrue(builder.process(new Token.Doctype("doctype")));
        Assert.assertTrue(builder.process(new Token.EOF()));
    }

    @Test
    public void testInsertNode() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Element element = new Element("element");
        builder.insertNode(element);
        Assert.assertEquals(element, builder.currentElement().child(0));
    }

    @Test
    public void testInsertStartTag() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Token.StartTag startTag = new Token.StartTag("tag");
        Element element = builder.insert(startTag);
        Assert.assertEquals("tag", element.tagName());
        Assert.assertEquals(builder.baseUri, element.baseUri());
        Assert.assertEquals(ParseSettings.preserveCase, element.settings);
        Assert.assertEquals(1, builder.stack.size());
        Assert.assertEquals(element, builder.stack.get(0));
    }

    @Test
    public void testInsertSelfClosingStartTag() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Token.StartTag startTag = new Token.StartTag("tag").selfClosing();
        Element element = builder.insert(startTag);
        Assert.assertEquals("tag", element.tagName());
        Assert.assertTrue(element.tag().isKnownTag());
        Assert.assertTrue(element.tag().isSelfClosing());
    }

    @Test
    public void testInsertComment() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Token.Comment commentToken = new Token.Comment("comment");
        builder.insert(commentToken);
        Assert.assertEquals("comment", builder.currentElement().childNode(0).outerHtml());
    }

    @Test
    public void testInsertCharacter() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Token.Character token = new Token.Character("character");
        builder.insert(token);
        Assert.assertEquals("character", builder.currentElement().childNode(0).outerHtml());
    }

    @Test
    public void testInsertDoctype() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Token.Doctype doctype = new Token.Doctype("doctype");
        builder.insert(doctype);
        Assert.assertEquals("doctype", builder.currentElement().childNode(0).outerHtml());
    }

    @Test
    public void testPopStackToClose() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Element element1 = new Element("element1");
        Element element2 = new Element("element2");
        builder.stack.add(element1);
        builder.stack.add(element2);
        builder.popStackToClose(new Token.EndTag("element1"));
        Assert.assertEquals(1, builder.stack.size());
        Assert.assertEquals(element2, builder.stack.get(0));
    }

    @Test
    public void testParseFragment() {
        String inputFragment = "<div>Test</div>";
        String baseUri = "http://example.com";
        XmlTreeBuilder builder = new XmlTreeBuilder();
        List<Node> nodes = builder.parseFragment(inputFragment, baseUri, ParseErrorList.noTracking(), ParseSettings.preserveCase);
        Assert.assertEquals(1, nodes.size());
        Assert.assertTrue(nodes.get(0) instanceof Element);
        Element element = (Element) nodes.get(0);
        Assert.assertEquals("div", element.tagName());
        Assert.assertTrue(element.parent() instanceof Document);
    }
}