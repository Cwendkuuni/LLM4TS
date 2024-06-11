package org.jsoup.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.jsoup.nodes.DocumentType;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.junit.Before;
import org.junit.Test;

public class XmlTreeBuilderTest {

    private XmlTreeBuilder xmlTreeBuilder;

    @Before
    public void setUp() {
        xmlTreeBuilder = new XmlTreeBuilder();
    }

    @Test
    public void testInitialiseParse() {
        // Test 1
        String input1 = "<root></root>";
        String baseUri1 = "http://example.com";
        ParseErrorList errors1 = new ParseErrorList();
        xmlTreeBuilder.initialiseParse(input1, baseUri1, errors1);
        Element doc1 = xmlTreeBuilder.getDocument();
        assertNotNull(doc1);
        assertEquals("root", doc1.nodeName());
        assertEquals("http://example.com", doc1.baseUri());

        // Test 2
        String input2 = "<html><head></head><body></body></html>";
        String baseUri2 = "http://example.com";
        ParseErrorList errors2 = new ParseErrorList();
        xmlTreeBuilder.initialiseParse(input2, baseUri2, errors2);
        Element doc2 = xmlTreeBuilder.getDocument();
        assertNotNull(doc2);
        assertEquals("html", doc2.nodeName());
        assertEquals("http://example.com", doc2.baseUri());
        Element head2 = doc2.getElementsByTag("head").first();
        assertNotNull(head2);
        assertEquals("head", head2.nodeName());
        Element body2 = doc2.getElementsByTag("body").first();
        assertNotNull(body2);
        assertEquals("body", body2.nodeName());
    }

    @Test
    public void testProcess() {
        // Test 1
        Token.StartTag startTag1 = Token.StartTag.html;
        xmlTreeBuilder.process(startTag1);
        Element doc1 = xmlTreeBuilder.getDocument();
        assertNotNull(doc1);
        assertEquals("html", doc1.nodeName());
        Token.EndTag endTag1 = Token.EndTag.html;
        xmlTreeBuilder.process(endTag1);

        // Test 2
        Token.StartTag startTag2 = Token.StartTag.head;
        xmlTreeBuilder.process(startTag2);
        Element head2 = doc1.getElementsByTag("head").first();
        assertNotNull(head2);
        assertEquals("head", head2.nodeName());
        Token.EndTag endTag2 = Token.EndTag.head;
        xmlTreeBuilder.process(endTag2);

        // Test 3
        Token.StartTag startTag3 = Token.StartTag.body;
        xmlTreeBuilder.process(startTag3);
        Element body3 = doc1.getElementsByTag("body").first();
        assertNotNull(body3);
        assertEquals("body", body3.nodeName());
        Token.EndTag endTag3 = Token.EndTag.body;
        xmlTreeBuilder.process(endTag3);
    }

    @Test
    public void testInsertNode() {
        // Test 1
        Element doc1 = new Element(Tag.valueOf("html"), "http://example.com");
        xmlTreeBuilder.getDocument().appendChild(doc1);
        DocumentType doctypeNode1 = new DocumentType("doctype", "", "", "http://example.com");
        xmlTreeBuilder.insertNode(doctypeNode1);
        assertEquals(1, doc1.childNodes().size());
        assertEquals(doctypeNode1, doc1.childNodes().get(0));

        // Test 2
        Element doc2 = new Element(Tag.valueOf("html"), "http://example.com");
        xmlTreeBuilder.getDocument().appendChild(doc2);
        Comment comment2 = new Comment("comment", "http://example.com");
        xmlTreeBuilder.insertNode(comment2);
        assertEquals(1, doc2.childNodes().size());
        assertEquals(comment2, doc2.childNodes().get(0));

        // Test 3
        Element doc3 = new Element(Tag.valueOf("html"), "http://example.com");
        xmlTreeBuilder.getDocument().appendChild(doc3);
        TextNode node3 = new TextNode("text", "http://example.com");
        xmlTreeBuilder.insertNode(node3);
        assertEquals(1, doc3.childNodes().size());
        assertEquals(node3, doc3.childNodes().get(0));
    }

    @Test
    public void testInsert() {
        // Test 1
        Token.StartTag startTag1 = new Token.StartTag("tag", "http://example.com");
        Element el1 = xmlTreeBuilder.insert(startTag1);
        assertEquals("tag", el1.nodeName());
        assertEquals("http://example.com", el1.baseUri());

        // Test 2
        Token.StartTag startTag2 = new Token.StartTag("selfClosingTag", "http://example.com");
        startTag2.setSelfClosing(true);
        Element el2 = xmlTreeBuilder.insert(startTag2);
        assertEquals("selfClosingTag", el2.nodeName());
        assertEquals("http://example.com", el2.baseUri());
        assertEquals(1, doc1.childNodes().size());
        assertEquals(el2, doc1.childNodes().get(0));
        assertEquals(Tag.valueOf("selfClosingTag"), el2.tag());

        // Test 3
        Token.StartTag startTag3 = new Token.StartTag("unknownTag", "http://example.com");
        Element el3 = xmlTreeBuilder.insert(startTag3);
        assertEquals("unknownTag", el3.nodeName());
        assertEquals("http://example.com", el3.baseUri());
        assertEquals(Tag.valueOf("unknownTag", true), el3.tag());
    }

    @Test
    public void testPopStackToClose() {
        // Test 1
        Token.EndTag endTag1 = new Token.EndTag("tag");
        Element firstFound1 = doc1;
        xmlTreeBuilder.popStackToClose(endTag1);
        assertEquals(0, doc1.childNodes().size());

        // Test 2
        Token.EndTag endTag2 = new Token.EndTag("tag");
        Element firstFound2 = el1;
        el1.appendChild(el2);
        xmlTreeBuilder.popStackToClose(endTag2);
        assertEquals(1, el1.children().size());
        assertEquals(el2, el1.children().get(0));

        // Test 3
        Token.EndTag endTag3 = new Token.EndTag("tag");
        Element firstFound3 = el2;
        el1.appendChild(el2);
        el1.appendChild(el3);
        xmlTreeBuilder.popStackToClose(endTag3);
        assertEquals(1, el1.children().size());
        assertEquals(el3, el1.children().get(0));
    }
}