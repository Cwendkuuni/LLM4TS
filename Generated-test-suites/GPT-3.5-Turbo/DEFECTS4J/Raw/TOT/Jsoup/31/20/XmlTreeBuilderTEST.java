package org.jsoup.parser;

import org.junit.Before;
import org.junit.Test;
import org.jsoup.helper.DataUtil;

import static org.junit.Assert.*;

public class XmlTreeBuilderTest {

    private XmlTreeBuilder xmlTreeBuilder;

    @Before
    public void setUp() {
        xmlTreeBuilder = new XmlTreeBuilder();
    }

    @Test
    public void testInitialiseParse() {
        String input = "<?xml version=\"1.0\"?>\n" +
                "<root>\n" +
                "    <element1>Value 1</element1>\n" +
                "    <element2 att1=\"attribute value\">Value 2</element2>\n" +
                "</root>";
        String baseUri = "https://example.com";
        ParseErrorList errors = new ParseErrorList();
        
        xmlTreeBuilder.initialiseParse(input, baseUri, errors);
        
        assertEquals("https://example.com", xmlTreeBuilder.baseUri);
        assertEquals(3, xmlTreeBuilder.stack.size());
        assertEquals("root", xmlTreeBuilder.stack.get(0).nodeName());
        assertEquals("Value 2", xmlTreeBuilder.stack.get(1).text());
        assertEquals("element2", xmlTreeBuilder.stack.get(1).nodeName());
        assertEquals("https://example.com", xmlTreeBuilder.stack.get(1).baseUri());
        assertEquals(2, xmlTreeBuilder.doc.childNodeSize());
    }

    @Test
    public void testProcessStartTag() {
        String input = "<element att1=\"value1\" att2=\"value2\"></element>";
        xmlTreeBuilder.initialiseParse(input, "https://example.com", new ParseErrorList());
        Token token = new Token.StartTag("element", DataUtil.Attributes("att1", "value1", "att2", "value2"));

        assertTrue(xmlTreeBuilder.process(token));
        
        assertEquals("element", xmlTreeBuilder.stack.peek().nodeName());
        assertEquals("https://example.com", xmlTreeBuilder.stack.peek().baseUri());
        assertEquals("value1", xmlTreeBuilder.stack.peek().attr("att1"));
        assertEquals("value2", xmlTreeBuilder.stack.peek().attr("att2"));
    }

    @Test
    public void testProcessEndTag() {
        String input = "<element1><element2></element2></element1>";
        xmlTreeBuilder.initialiseParse(input, "https://example.com", new ParseErrorList());
        Token startTagToken = new Token.StartTag("element1", DataUtil.Attributes());
        Token endTagToken = new Token.EndTag("element1");

        xmlTreeBuilder.process(startTagToken); // push element1

        assertTrue(xmlTreeBuilder.process(endTagToken));
        
        assertEquals(0, xmlTreeBuilder.stack.size());
    }

    @Test
    public void testProcessComment() {
        String input = "<!-- This is a comment -->";
        xmlTreeBuilder.initialiseParse(input, "https://example.com", new ParseErrorList());
        Token token = new Token.Comment(" This is a comment ");

        assertTrue(xmlTreeBuilder.process(token));
        
        assertEquals(" This is a comment ", xmlTreeBuilder.stack.peek().toString());
    }

    @Test
    public void testProcessCharacter() {
        String input = "<element>Text Node</element>";
        xmlTreeBuilder.initialiseParse(input, "https://example.com", new ParseErrorList());
        Token token = new Token.Character("Text Node");

        assertTrue(xmlTreeBuilder.process(token));
        
        assertEquals("Text Node", xmlTreeBuilder.stack.peek().text());
    }

    @Test
    public void testProcessDoctype() {
        String input = "<!DOCTYPE html>";
        xmlTreeBuilder.initialiseParse(input, "https://example.com", new ParseErrorList());
        Token token = new Token.Doctype("html");

        assertTrue(xmlTreeBuilder.process(token));
        
        assertEquals("html", xmlTreeBuilder.stack.peek().toString());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testProcessUnexpectedToken() {
        String input = "<element></element>";
        xmlTreeBuilder.initialiseParse(input, "https://example.com", new ParseErrorList());
        Token token = new Token.Character("Unexpected Token");

        xmlTreeBuilder.process(token);
    }

    @Test
    public void testInsertStartTagSelfClosing() {
        String input = "<element/>";
        xmlTreeBuilder.initialiseParse(input, "https://example.com", new ParseErrorList());
        Token.StartTag startTag = new Token.StartTag("element", DataUtil.Attributes(), true);

        xmlTreeBuilder.insert(startTag);

        assertEquals(1, xmlTreeBuilder.stack.size());
        assertEquals("element", xmlTreeBuilder.stack.peek().nodeName());
        assertTrue(xmlTreeBuilder.stack.peek().isSelfClosing());
    }

    @Test
    public void testInsertStartTagNotSelfClosing() {
        String input = "<element></element>";
        xmlTreeBuilder.initialiseParse(input, "https://example.com", new ParseErrorList());
        Token.StartTag startTag = new Token.StartTag("element", DataUtil.Attributes(), false);

        xmlTreeBuilder.insert(startTag);

        assertEquals(1, xmlTreeBuilder.stack.size());
        assertEquals("element", xmlTreeBuilder.stack.peek().nodeName());
        assertFalse(xmlTreeBuilder.stack.peek().isSelfClosing());
    }

    @Test
    public void testInsertComment() {
        String input = "<!-- This is a comment -->";
        xmlTreeBuilder.initialiseParse(input, "https://example.com", new ParseErrorList());
        Token.Comment commentToken = new Token.Comment(" This is a comment ");

        xmlTreeBuilder.insert(commentToken);

        assertEquals(" This is a comment ", xmlTreeBuilder.stack.peek().toString());
    }

    @Test
    public void testInsertCharacter() {
        String input = "<element>Text Node</element>";
        xmlTreeBuilder.initialiseParse(input, "https://example.com", new ParseErrorList());
        Token.Character characterToken = new Token.Character("Text Node");

        xmlTreeBuilder.insert(characterToken);
        
        assertEquals("Text Node", xmlTreeBuilder.stack.peek().text());
    }

    @Test
    public void testInsertDoctype() {
        String input = "<!DOCTYPE html>";
        xmlTreeBuilder.initialiseParse(input, "https://example.com", new ParseErrorList());
        Token.Doctype doctypeToken = new Token.Doctype("html");

        xmlTreeBuilder.insert(doctypeToken);
        
        assertEquals("html", xmlTreeBuilder.stack.peek().toString());
    }

    @Test
    public void testPopStackToClose() {
        String input = "<element1><element2></element2></element1>";
        xmlTreeBuilder.initialiseParse(input, "https://example.com", new ParseErrorList());
        Token.StartTag startTag1 = new Token.StartTag("element1");
        Token.StartTag startTag2 = new Token.StartTag("element2");
        Token.EndTag endTag1 = new Token.EndTag("element1");

        xmlTreeBuilder.process(startTag1); // push element1
        xmlTreeBuilder.process(startTag2); // push element2

        xmlTreeBuilder.popStackToClose(endTag1);
        
        assertEquals(0, xmlTreeBuilder.stack.size());
    }

}