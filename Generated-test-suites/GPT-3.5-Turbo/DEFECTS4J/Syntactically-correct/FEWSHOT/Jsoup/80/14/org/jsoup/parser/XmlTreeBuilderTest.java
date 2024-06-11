package org.jsoup.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.parser.Token.StartTag;
import org.jsoup.parser.Token.EndTag;
import org.jsoup.parser.Token.Comment;
import org.jsoup.parser.Token.Character;
import org.jsoup.parser.Token.Doctype;
import org.jsoup.parser.XmlTreeBuilder;
import org.jsoup.parser.XmlTreeBuilder.ParseErrorList;
import org.jsoup.parser.XmlTreeBuilder.ParseSettings;
import org.junit.Test;

import java.io.Reader;
import java.io.StringReader;
import java.util.List;

public class XmlTreeBuilderTest {
    @Test
    public void testDefaultSettings() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        ParseSettings settings = builder.defaultSettings();
        assertTrue(settings.preserveCase);
    }

    @Test
    public void testParseReaderString() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        String input = "<root><child></child></root>";
        Reader reader = new StringReader(input);
        String baseUri = "";
        Document document = builder.parse(reader, baseUri);
        
        assertEquals("root", document.child(0).nodeName());
        assertEquals("child", document.child(0).child(0).nodeName());
    }

    @Test
    public void testParseStringString() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        String input = "<root><child></child></root>";
        String baseUri = "";
        Document document = builder.parse(input, baseUri);
        
        assertEquals("root", document.child(0).nodeName());
        assertEquals("child", document.child(0).child(0).nodeName());
    }

    @Test
    public void testInitialiseParse() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        String input = "<root><child></child></root>";
        Reader reader = new StringReader(input);
        String baseUri = "";
        ParseErrorList errors = ParseErrorList.noTracking();
        ParseSettings settings = builder.defaultSettings();
        builder.initialiseParse(reader, baseUri, errors, settings);
        
        assertEquals(0, errors.size());
    }

    @Test
    public void testProcess_StartTag() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Token.StartTag startTag = new Token.StartTag();
        startTag.name("root");
        Element element = builder.insert(startTag);
        
        assertEquals("root", element.nodeName());
    }

    @Test
    public void testProcess_EndTag() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Token.EndTag endTag = new Token.EndTag();
        endTag.tagName("root");
        builder.insert(endTag);
        
        assertEquals(0, builder.stack.size());
    }

    @Test
    public void testProcess_Comment() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Token.Comment comment = new Token.Comment();
        comment.data("test comment");
        builder.insert(comment);
        
        assertEquals("test comment", builder.currentElement().childNode(0).toString());
    }

    @Test
    public void testProcess_Character() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Token.Character character = new Token.Character();
        character.data("test character");
        builder.insert(character);
        
        assertEquals("test character", builder.currentElement().childNode(0).toString());
    }

    @Test
    public void testProcess_Doctype() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Token.Doctype doctype = new Token.Doctype();
        doctype.name("html");
        builder.insert(doctype);
        
        assertEquals("html", builder.currentElement().childNode(0).toString());
    }

    @Test
    public void testInsertNode() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Node node = new Element("child");
        builder.insertNode(node);
        
        assertEquals("child", builder.currentElement().childNode(0).nodeName());
    }

    @Test
    public void testInsert_StartTag() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Token.StartTag startTag = new Token.StartTag();
        startTag.name("child");
        Element element = builder.insert(startTag);
        
        assertEquals("child", element.nodeName());
    }

    @Test
    public void testInsert_Comment() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Token.Comment comment = new Token.Comment();
        comment.data("test comment");
        builder.insert(comment);
        
        assertEquals("test comment", builder.currentElement().childNode(0).toString());
    }

    @Test
    public void testInsert_Character() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Token.Character character = new Token.Character();
        character.data("test character");
        builder.insert(character);
        
        assertEquals("test character", builder.currentElement().childNode(0).toString());
    }

    @Test
    public void testInsert_Doctype() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Token.Doctype doctype = new Token.Doctype();
        doctype.name("html");
        builder.insert(doctype);
        
        assertEquals("html", builder.currentElement().childNode(0).toString());
    }

    @Test
    public void testPopStackToClose() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Token.EndTag endTag = new Token.EndTag();
        endTag.tagName("root");
        builder.insert(endTag);
        
        assertEquals(0, builder.stack.size());
    }

    @Test
    public void testParseFragment() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        String inputFragment = "<child></child>";
        String baseUri = "";
        ParseErrorList errors = ParseErrorList.noTracking();
        ParseSettings settings = builder.defaultSettings();
        List<Node> nodes = builder.parseFragment(inputFragment, baseUri, errors, settings);
        
        assertTrue(nodes.get(0) instanceof Element);
        assertEquals("child", nodes.get(0).nodeName());
    }
}