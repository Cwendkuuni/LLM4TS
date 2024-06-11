
package org.jsoup.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.jsoup.nodes.Comment;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.DocumentType;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.jsoup.parser.Token.Doctype;
import org.jsoup.parser.Token.EndTag;
import org.jsoup.parser.Token.StartTag;
import org.jsoup.parser.Token.Character;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

public class XmlTreeBuilderTest {
    private ParseSettings settings;
    private XmlTreeBuilder builder;

    @Before
    public void setUp() {
        settings = new ParseSettings(false, false);
        builder = new XmlTreeBuilder();
    }

    @Test
    public void testDefaultSettings() {
        ParseSettings defaultSettings = builder.defaultSettings();
        assertTrue(defaultSettings.preserveCase);
    }

    @Test
    public void testParse() {
        String input = "<tag>Content</tag>";
        String baseUri = "";
        Document document = builder.parse(input, baseUri);
        Element rootElement = document.child(0);

        assertEquals("tag", rootElement.tagName());
        assertEquals("Content", rootElement.text());
    }

    @Test
    public void testInitialiseParse() {
        String input = "<tag>Test</tag>";
        String baseUri = "";
        ParseErrorList errors = ParseErrorList.noTracking();
        builder.initialiseParse(input, baseUri, errors, settings);
        Document document = builder.getDocument();

        assertEquals(1, document.childNodeSize());
        assertEquals("tag", document.child(0).tagName());
        assertEquals("Test", document.child(0).text());
        assertEquals(Document.OutputSettings.Syntax.xml, document.outputSettings().syntax());
    }

    @Test
    public void testProcessStartTag() {
        String input = "<tag attrib=\"value\">";
        builder.initialiseParse(input, "", ParseErrorList.noTracking(), settings);

        StartTag startTag = new StartTag();
        startTag.attributes.put("attrib", "value");
        startTag.attributes.put("otherAttrib", "otherValue");
        startTag.name = "tag";

        assertTrue(builder.process(startTag));

        Element element = builder.getCurrentElement();
        assertEquals("tag", element.tagName());
        assertEquals("value", element.attr("attrib"));
        assertEquals("otherValue", element.attr("otherAttrib"));
    }

    @Test
    public void testProcessEndTag() {
        String input = "<tag>Content</tag>";
        builder.initialiseParse(input, "", ParseErrorList.noTracking(), settings);

        EndTag endTag = new EndTag();
        endTag.name = "tag";

        assertTrue(builder.process(endTag));

        Document document = builder.getDocument();
        assertEquals(1, document.childNodeSize());
        assertEquals("tag", document.child(0).tagName());
        assertEquals("Content", document.child(0).text());
    }

    @Test
    public void testProcessComment() {
        String input = "<!-- This is a comment -->";
        builder.initialiseParse(input, "", ParseErrorList.noTracking(), settings);

        Comment comment = new Comment();
        comment.data = " This is a comment ";

        assertTrue(builder.process(comment));

        Document document = builder.getDocument();
        assertEquals(1, document.childNodeSize());
        assertTrue(document.child(0) instanceof Comment);
        assertEquals(" This is a comment ", ((Comment) document.child(0)).getData());
    }

    @Test
    public void testProcessCharacter() {
        String input = "<tag>Content</tag>";
        builder.initialiseParse(input, "", ParseErrorList.noTracking(), settings);

        Character character = new Character();
        character.data = "Content";

        assertTrue(builder.process(character));

        Document document = builder.getDocument();
        assertEquals(1, document.childNodeSize());
        assertTrue(document.child(0) instanceof TextNode);
        assertEquals("Content", ((TextNode) document.child(0)).getWholeText());
    }

    @Test
    public void testProcessDoctype() {
        String input = "<!DOCTYPE html>";
        builder.initialiseParse(input, "", ParseErrorList.noTracking(), settings);

        Doctype doctype = new Doctype();
        doctype.name = "html";

        assertTrue(builder.process(doctype));

        Document document = builder.getDocument();
        assertEquals(1, document.childNodeSize());
        assertTrue(document.child(0) instanceof DocumentType);
        assertEquals("html", ((DocumentType) document.child(0)).getName());
    }

    @Test
    public void testInsertNode() {
        Document document = new Document("");
        Element rootElement = document.createElement("root");
        document.appendChild(rootElement);

        builder.setCurrentElement(rootElement);

        Element nestedElement = document.createElement("nested");
        builder.insertNode(nestedElement);

        assertEquals(1, rootElement.childNodeSize());
        assertEquals(nestedElement, rootElement.child(0));
    }

    @Test
    public void testInsertStartTag() {
        String input = "<tag attrib=\"value\">";
        builder.initialiseParse(input, "", ParseErrorList.noTracking(), settings);

        StartTag startTag = new StartTag();
        startTag.attributes.put("attrib", "value");
        startTag.attributes.put("otherAttrib", "otherValue");
        startTag.name = "tag";
        startTag.selfClosing = false;

        Element element = builder.insert(startTag);
        List<Element> stack = builder.getStack();

        assertEquals(1, stack.size());
        assertEquals(element, stack.get(0));
        assertEquals("tag", element.tagName());
        assertEquals("value", element.attr("attrib"));
        assertEquals("otherValue", element.attr("otherAttrib"));
    }

    @Test
    public void testInsertStartTagSelfClosing() {
        String input = "<tag />";
        builder.initialiseParse(input, "", ParseErrorList.noTracking(), settings);

        StartTag startTag = new StartTag();
        startTag.attributes.put("attrib", "value");
        startTag.attributes.put("otherAttrib", "otherValue");
        startTag.name = "tag";
        startTag.selfClosing= true;

        Element element = builder.insert(startTag);
        List<Element> stack = builder.getStack();

        assertEquals(0, stack.size());
        assertEquals("tag", element.tagName());
        assertEquals("value", element.attr("attrib"));
        assertEquals("otherValue", element.attr("otherAttrib"));
    }

    @Test
    public void testInsertComment() {
        String input = "<!-- This is a comment -->";
        builder.initialiseParse(input, "", ParseErrorList.noTracking(), settings);

        Comment comment = new Comment();
        comment.data = " This is a comment ";

        builder.insert(comment);

        Document document = builder.getDocument();
        assertEquals(1, document.childNodeSize());
        assertTrue(document.child(0) instanceof Comment);
        assertEquals(" This is a comment ", ((Comment) document.child(0)).getData());
    }

    @Test
    public void testInsertCharacter() {
        String input = "<tag>Content</tag>";
        builder.initialiseParse(input, "", ParseErrorList.noTracking(), settings);

        Character character = new Character();
        character.data = "Content";

        builder.insert(character);

        Document document = builder.getDocument();
        assertEquals(1, document.childNodeSize());
        assertTrue(document.child(0) instanceof TextNode);
        assertEquals("Content", ((TextNode) document.child(0)).getWholeText());
    }

    @Test
    public void testInsertDoctype() {
        String input = "<!DOCTYPE html>";
        builder.initialiseParse(input, "", ParseErrorList.noTracking(), settings);

        Doctype doctype = new Doctype();
        doctype.name = "html";

        builder.insert(doctype);

        Document document = builder.getDocument();
        assertEquals(1, document.childNodeSize());
        assertTrue(document.child(0) instanceof DocumentType);
        assertEquals("html", ((DocumentType) document.child(0)).getName());
    }

    @Test
    public void testPopStackToClose() {
        Document document = new Document("");
        Element rootElement = document.createElement("root");
        document.appendChild(rootElement);

        builder.setCurrentElement(rootElement);

        Element nestedElement1 = document.createElement("nested");
        Element nestedElement2 = document.createElement("nested");
        Element nestedElement3 = document.createElement("nested");

        builder.getStack().add(nestedElement1);
        builder.getStack().add(nestedElement2);
        builder.getStack().add(nestedElement3);

        builder.popStackToClose(new EndTag("nested"));

        List<Element> stack = builder.getStack();
        assertEquals(2, stack.size());
        assertEquals(nestedElement1, stack.get(0));
        assertEquals(nestedElement2, stack.get(1));
    }

    @Test
    public void testParseFragment() {
        String input = "<tag>Test</tag>";
        String baseUri = "";
        List<Node> nodes = builder.parseFragment(input, baseUri, ParseErrorList.noTracking(), settings);

        assertEquals(1, nodes.size());
        assertTrue(nodes.get(0) instanceof Element);
        Element element = (Element) nodes.get(0);
        assertEquals("tag", element.tagName());
        assertEquals("Test", element.text());
    }
}
