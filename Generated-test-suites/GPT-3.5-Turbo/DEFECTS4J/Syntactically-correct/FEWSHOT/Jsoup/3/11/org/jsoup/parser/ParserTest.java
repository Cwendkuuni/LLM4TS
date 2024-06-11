package org.jsoup.parser;

import static org.junit.Assert.*;
import org.junit.Test;
import org.junit.Before;
import org.apache.commons.lang.Validate;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.TextNode;
import org.jsoup.nodes.Comment;
import org.jsoup.nodes.DataNode;
import org.jsoup.nodes.XmlDeclaration;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Attribute;
import org.jsoup.parser.TokenQueue;
import org.jsoup.parser.Tag;

import java.util.LinkedList;

public class ParserTest {
    private Parser parser;
    private String html;
    private String baseUri;
    private boolean isBodyFragment;
    private Document doc;

    @Before
    public void setUp() {
        html = "<html><body><h1>Test</h1><p>Hello world</p></body></html>";
        baseUri = "https://www.example.com";
        isBodyFragment = false;
        parser = new Parser(html, baseUri, isBodyFragment);
        doc = new Document(baseUri);
    }

    @Test
    public void testParser() {
        Document result = parser.parse();
        assertEquals(doc, result);
    }

    @Test
    public void testParse() {
        Document result = parser.parse();
        assertEquals(doc, result);
    }

    @Test
    public void testParseBodyFragment() {
        isBodyFragment = true;
        parser = new Parser(html, baseUri, isBodyFragment);
        Document result = parser.parse();
        assertEquals(doc, result);
    }

    @Test
    public void testParseComment() {
        parser.parseComment();
        Element lastElement = doc.body().select("h1").first();
        Comment comment = new Comment("", baseUri);
        lastElement.appendChild(comment);
        assertEquals(comment.toString(), lastElement.childNodes().last().toString());
    }

    @Test
    public void testParseXmlDecl() {
        parser.parseXmlDecl();
        Element lastElement = doc.body().select("h1").last();
        XmlDeclaration decl = new XmlDeclaration("", baseUri, false);
        lastElement.appendChild(decl);
        assertEquals(decl.toString(), lastElement.childNodes().last().toString());
    }

    @Test
    public void testParseEndTag() {
        parser.parseEndTag();
        Element lastElement = doc.body().select("h1").last();
        String tagName = "h1";
        Tag tag = Tag.valueOf(tagName);
        Element elToClose = null;
        for (int i = parser.stack.size() -1; i > 0; i--) {
            Element el = parser.stack.get(i);
            Tag elTag = el.tag();
            if (elTag.equals(tag)) {
                elToClose = el;
                break;
            }
        }
        assertEquals(elToClose, lastElement);
    }

    @Test
    public void testParseStartTag() {
        parser.parseStartTag();
        Element lastElement = doc.body().select("h1").first();
        String tagName = "h1";
        Tag tag = Tag.valueOf(tagName);
        Attributes attributes = new Attributes();
        Element child = new Element(tag, baseUri, attributes);
        boolean isEmptyElement = tag.isEmpty();
        parser.addChildToParent(child, isEmptyElement);
        assertEquals(child, lastElement.childNodes().last());
    }

    @Test
    public void testParseTextNode() {
        parser.parseTextNode();
        Element lastElement = doc.body().select("h1").first();
        String text = "Test";
        TextNode textNode = TextNode.createFromEncoded(text, baseUri);
        lastElement.appendChild(textNode);
        assertEquals(textNode.toString(), lastElement.childNodes().last().toString());
    }

    @Test
    public void testParseCdata() {
        parser.parseCdata();
        Element lastElement = doc.body().select("h1").first();
        String rawText = "";
        TextNode textNode = new TextNode(rawText, baseUri);
        lastElement.appendChild(textNode);
        assertEquals(textNode.toString(), lastElement.childNodes().last().toString());
    }

    @Test
    public void testAddChildToParent() {
        String tagName = "h1";
        Tag tag = Tag.valueOf(tagName);
        Attributes attributes = new Attributes();
        Element child = new Element(tag, baseUri, attributes);
        boolean isEmptyElement = tag.isEmpty();
        Element parent = parser.addChildToParent(child, isEmptyElement);
        assertEquals(parent, doc.body());
    }

    @Test
    public void testPopStackToSuitableContainer() {
        String tagName = "h1";
        Tag tag = Tag.valueOf(tagName);
        Element element = parser.popStackToSuitableContainer(tag);
        assertEquals(element, doc.body());
    }

    @Test
    public void testStackHasValidParent() {
        String tagName = "h1";
        Tag tag = Tag.valueOf(tagName);
        boolean result = parser.stackHasValidParent(tag);
        assertTrue(result);
    }

    @Test
    public void testPopStackToClose() {
        String tagName = "h1";
        Tag tag = Tag.valueOf(tagName);
        Element element = parser.popStackToClose(tag);
        assertEquals(element, doc.body().select(tagName).first());
    }

    @Test
    public void testLast() {
        Element element = parser.last();
        assertEquals(element, doc.body());
    }
}