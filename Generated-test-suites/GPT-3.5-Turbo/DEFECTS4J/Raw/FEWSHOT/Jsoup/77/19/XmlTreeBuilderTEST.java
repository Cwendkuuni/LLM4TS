
package org.jsoup.parser;

import org.jsoup.Jsoup;
import org.jsoup.helper.Validate;
import org.jsoup.nodes.*;
import org.junit.Test;

import java.io.StringReader;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;

public class XmlTreeBuilderTest {
    @Test
    public void testDefaultSettings() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        ParseSettings settings = builder.defaultSettings();
        assertEquals(ParseSettings.preserveCase, settings);
    }

    @Test
    public void testParseReaderString() {
        StringReader input = new StringReader("<html><body><h1>Hello World</h1></body></html>");
        String baseUri = "http://www.example.com";
        Document document = (new XmlTreeBuilder()).parse(input, baseUri);
        assertEquals("<html><head></head><body><h1>Hello World</h1></body></html>", document.toString());
    }

    @Test
    public void testParseStringString() {
        String input = "<html><body><h1>Hello World</h1></body></html>";
        String baseUri = "http://www.example.com";
        Document document = (new XmlTreeBuilder()).parse(input, baseUri);
        assertEquals("<html><head></head><body><h1>Hello World</h1></body></html>", document.toString());
    }

    @Test
    public void testInitialiseParse() {
        StringReader input = new StringReader("<html><body><h1>Hello World</h1></body></html>");
        String baseUri = "http://www.example.com";
        ParseErrorList errors = ParseErrorList.noTracking();
        ParseSettings settings = ParseSettings.preserveCase;
        
        XmlTreeBuilder builder = new XmlTreeBuilder();
        builder.initialiseParse(input, baseUri, errors, settings);
        
        assertEquals(baseUri, builder.baseUri());
        assertEquals(errors, builder.errors);
        assertEquals(settings, builder.settings);
        assertEquals(Document.OutputSettings.Syntax.xml, builder.doc.outputSettings().syntax());
        assertEquals(1, builder.stack.size());
        assertEquals(builder.doc, builder.stack.get(0));
    }

    @Test
    public void testProcess() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        builder.initialiseParse(new StringReader("<html><body><h1>Hello World</h1></body></html>"), "", ParseErrorList.noTracking(), ParseSettings.preserveCase);
        
        Token startTag = new Token.StartTag("h1");
        assertTrue(builder.process(startTag));
        assertTrue(builder.stack.get(0) instanceof Element);

        Token endTag = new Token.EndTag("h1");
        assertTrue(builder.process(endTag));
        assertTrue(builder.stack.isEmpty());

        Token comment = new Token.Comment("Sample comment");
        assertTrue(builder.process(comment));

        Token character = new Token.Character("Sample character");
        assertTrue(builder.process(character));

        Token doctype = new Token.Doctype("html");
        assertTrue(builder.process(doctype));

        Token eof = new Token.EOF();
        assertTrue(builder.process(eof));

        Token unknown = new Token.Unknown();
        assertFalse(builder.process(unknown));
    }
    
    @Test
    public void testInsertNode() {
        Document document = new Document("");
        Element element = new Element(Tag.valueOf("html"), "");
        
        XmlTreeBuilder builder = new XmlTreeBuilder();
        builder.doc = document;
        builder.stack.add(element);
        
        TextNode textNode = new TextNode("Hello World");
        builder.insertNode(textNode);
        
        assertEquals(1, element.childNodes().size());
        assertEquals(textNode, element.childNodes().get(0));
    }
    
    @Test
    public void testInsertStartTag() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Element element = builder.insert(new Token.StartTag("div"));
        
        assertEquals("div", element.nodeName());
        assertEquals("", element.baseUri());
        assertTrue(element.attributes().isEmpty());
        assertTrue(builder.stack.get(0) instanceof Element);
    }
    
    @Test
    public void testInsertComment() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Comment comment = new Comment("Sample comment");
        builder.insert(comment);
        
        assertEquals(comment, builder.stack.get(0).childNode(0));
    }
    
    @Test
    public void testInsertCharacter() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        TextNode textNode = new TextNode("Hello World");
        builder.insert(new Token.Character(textNode.getWholeText()));
        
        assertEquals(textNode, builder.stack.get(0).childNode(0));
    }
    
    @Test
    public void testInsertDoctype() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Token.Doctype doctype = new Token.Doctype("html");
        builder.insert(doctype);
        
        assertEquals(doctype.getName(), builder.stack.get(0).childNode(0).nodeName());
    }

    @Test
    public void testPopStackToClose() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Element div1 = new Element(Tag.valueOf("div"), "");
        div1.appendChild(new Element(Tag.valueOf("p"), ""));
        div1.appendChild(new Element(Tag.valueOf("span"), ""));
        Element div2 = new Element(Tag.valueOf("div"), "");
        div2.appendChild(new Element(Tag.valueOf("p"), ""));
        div2.appendChild(new Element(Tag.valueOf("span"), ""));
        
        builder.stack.add(div1);
        builder.stack.add(div2);
        
        Token.EndTag endTag = new Token.EndTag("div");
        builder.popStackToClose(endTag);
        
        assertEquals(1, builder.stack.size());
        assertEquals(div1, builder.stack.get(0));
    }
    
    @Test
    public void testParseFragment() {
        String inputFragment = "<p>Hello World</p>";
        String baseUri = "http://www.example.com";
        
        XmlTreeBuilder builder = new XmlTreeBuilder();
        List<Node> nodes = builder.parseFragment(inputFragment, baseUri, ParseErrorList.noTracking(), ParseSettings.preserveCase);
        
        assertEquals(1, nodes.size());
        assertTrue(nodes.get(0) instanceof Element);
        assertEquals("p", nodes.get(0).nodeName());
        assertEquals("Hello World", ((Element) nodes.get(0)).text());
    }
}
