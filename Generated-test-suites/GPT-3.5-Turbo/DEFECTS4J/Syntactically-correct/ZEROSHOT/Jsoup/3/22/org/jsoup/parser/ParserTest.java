package org.jsoup.parser;

import org.apache.commons.lang.Validate;
import org.jsoup.Jsoup;
import org.jsoup.nodes.*;
import org.junit.Assert;
import org.junit.Test;

import java.util.LinkedList;

public class ParserTest {

    @Test
    public void testParse() {
        String html = "<html><head><title>This is a title</title></head><body><h1>Welcome to my website</h1></body></html>";
        String baseUri = "https://example.com";
        Document document = Parser.parse(html, baseUri);

        Assert.assertEquals("https://example.com", document.baseUri());
        Assert.assertEquals(1, document.head().children().size());
        Assert.assertEquals(1, document.body().children().size());
    }

    @Test
    public void testParseBodyFragment() {
        String bodyHtml = "<h1>Welcome to my website</h1>";
        String baseUri = "https://example.com";
        Document document = Parser.parseBodyFragment(bodyHtml, baseUri);

        Assert.assertEquals("https://example.com", document.baseUri());
        Assert.assertEquals(1, document.body().children().size());
    }

    @Test
    public void testParseComment() {
        Parser parser = new Parser("<!-- This is a comment -->", "https://example.com", false);
        LinkedList<Element> stack = parser.getStack();
        TokenQueue tq = parser.getTq();

        parser.parseComment();

        Element element = stack.getLast();
        Assert.assertTrue(element instanceof Comment);
        Assert.assertEquals("https://example.com", ((Comment) element).baseUri());
        Assert.assertEquals(" This is a comment ", ((Comment) element).getData());

        Assert.assertTrue(tq.isEmpty());
    }

    @Test
    public void testParseXmlDecl() {
        Parser parser = new Parser("<?xml version=\"1.0\" encoding=\"UTF-8\"?>", "https://example.com", false);
        LinkedList<Element> stack = parser.getStack();
        TokenQueue tq = parser.getTq();

        parser.parseXmlDecl();

        Element element = stack.getLast();
        Assert.assertTrue(element instanceof XmlDeclaration);
        Assert.assertEquals("https://example.com", ((XmlDeclaration) element).baseUri());
        Assert.assertEquals("<?xml version=\"1.0\" encoding=\"UTF-8\"?>", ((XmlDeclaration) element).getData());
        Assert.assertTrue(((XmlDeclaration) element).isProcessingInstruction());

        Assert.assertTrue(tq.isEmpty());
    }

    @Test
    public void testParseEndTag() {
        Parser parser = new Parser("</title>", "https://example.com", false);
        LinkedList<Element> stack = parser.getStack();
        TokenQueue tq = parser.getTq();
        Tag titleTag = parser.getTitleTag();

        stack.add(new Element(titleTag, "https://example.com"));

        parser.parseEndTag();

        Assert.assertEquals(0, stack.size());
        Assert.assertTrue(tq.isEmpty());
    }

    @Test
    public void testParseStartTag() {
        Parser parser = new Parser("<h1>Welcome</h1>", "https://example.com", false);
        LinkedList<Element> stack = parser.getStack();
        TokenQueue tq = parser.getTq();
        Tag h1Tag = Tag.valueOf("h1");

        parser.parseStartTag();

        Element element = stack.getLast();
        Assert.assertTrue(element instanceof Element);
        Assert.assertEquals(h1Tag, element.tag());
        Assert.assertEquals("https://example.com", element.baseUri());
        Assert.assertEquals("Welcome", element.text());

        Assert.assertTrue(tq.isEmpty());
    }

    @Test
    public void testParseAttribute() {
        Parser parser = new Parser("id=\"title\"", "https://example.com", false);
        TokenQueue tq = parser.getTq();

        Attribute attribute = parser.parseAttribute();

        Assert.assertEquals("id", attribute.getKey());
        Assert.assertEquals("title", attribute.getValue());

        Assert.assertTrue(parser.stackIsEmpty());
        Assert.assertTrue(tq.isEmpty());
    }

    @Test
    public void testParseTextNode() {
        Parser parser = new Parser("This is a text node", "https://example.com", false);
        LinkedList<Element> stack = parser.getStack();
        TokenQueue tq = parser.getTq();

        parser.parseTextNode();

        Element element = stack.getLast();
        Assert.assertTrue(element instanceof TextNode);
        Assert.assertEquals("https://example.com", element.baseUri());
        Assert.assertEquals("This is a text node", element.text());

        Assert.assertTrue(tq.isEmpty());
    }

    @Test
    public void testParseCdata() {
        Parser parser = new Parser("<![CDATA[This is a CDATA section]]>", "https://example.com", false);
        LinkedList<Element> stack = parser.getStack();
        TokenQueue tq = parser.getTq();

        parser.parseCdata();

        Element element = stack.getLast();
        Assert.assertTrue(element instanceof TextNode);
        Assert.assertEquals("https://example.com", element.baseUri());
        Assert.assertEquals("<![CDATA[This is a CDATA section]]>", element.outerHtml());
        Assert.assertEquals("This is a CDATA section", element.text());

        Assert.assertTrue(tq.isEmpty());
    }

    @Test
    public void testAddChildToParent() {
        Parser parser = new Parser("<h1>Welcome</h1>", "https://example.com", false);
        LinkedList<Element> stack = parser.getStack();
        Tag h1Tag = Tag.valueOf("h1");
        Element parent = new Element(Tag.valueOf("body"), "https://example.com");
        stack.add(parent);

        Element child = new Element(h1Tag, "https://example.com");
        parser.addChildToParent(child, false);

        Assert.assertEquals(child, parent.children().get(0));
        Assert.assertEquals(1, parent.children().size());
        Assert.assertEquals(0, stack.size());
    }

    @Test
    public void testStackHasValidParent() {
        Parser parser = new Parser("<p><strong>Text</strong></p>", "https://example.com", false);
        LinkedList<Element> stack = parser.getStack();
        Tag pTag = Tag.valueOf("p");
        Tag strongTag = Tag.valueOf("strong");

        Element parent = new Element(pTag, "https://example.com");
        stack.add(parent);

        boolean validParent = parser.stackHasValidParent(strongTag);

        Assert.assertTrue(validParent);
    }

    @Test
    public void testPopStackToSuitableContainer() {
        Parser parser = new Parser("<h1><strong>Text</strong></h1>", "https://example.com", false);
        LinkedList<Element> stack = parser.getStack();
        Tag h1Tag = Tag.valueOf("h1");
        Tag strongTag = Tag.valueOf("strong");

        Element h1 = new Element(h1Tag, "https://example.com");
        Element strong = new Element(strongTag, "https://example.com");
        stack.add(h1);
        stack.add(strong);

        Element result = parser.popStackToSuitableContainer(strongTag);

        Assert.assertEquals(h1, result);
        Assert.assertEquals(1, stack.size());
        Assert.assertEquals(strong, stack.getLast());
    }

    @Test
    public void testPopStackToClose() {
        Parser parser = new Parser("</h1></body></html>", "https://example.com", false);
        LinkedList<Element> stack = parser.getStack();
        TokenQueue tq = parser.getTq();
        Tag h1Tag = Tag.valueOf("h1");
        Tag bodyTag = Tag.valueOf("body");
        Tag htmlTag = Tag.valueOf("html");

        stack.add(new Element(h1Tag, "https://example.com"));
        stack.add(new Element(bodyTag, "https://example.com"));
        stack.add(new Element(htmlTag, "https://example.com"));

        Element result = parser.popStackToClose(h1Tag);

        Assert.assertEquals(0, stack.size());
        Assert.assertEquals(1, tq.length());
        Assert.assertEquals(">", tq.toString());
        Assert.assertEquals(htmlTag, result.tag());
    }

    @Test
    public void testLast() {
        Parser parser = new Parser("</h1></body></html>", "https://example.com", false);
        LinkedList<Element> stack = parser.getStack();
        Tag htmlTag = Tag.valueOf("html");

        stack.add(new Element(htmlTag, "https://example.com"));

        Element result = parser.last();

        Assert.assertEquals(htmlTag, result.tag());
    }
}