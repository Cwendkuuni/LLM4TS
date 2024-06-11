
package org.jsoup.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.jsoup.Jsoup;
import org.jsoup.helper.Validate;
import org.jsoup.nodes.*;
import org.junit.Before;
import org.junit.Test;

public class XmlTreeBuilderTest {

    private XmlTreeBuilder xmlTreeBuilder;
    private String input;
    private String baseUri;

    @Before
    public void setup() {
        xmlTreeBuilder = new XmlTreeBuilder();
        input = "<xml><tag1>value1</tag1><tag2>value2</tag2></xml>";
        baseUri = "https://www.example.com";
    }

    @Test
    public void testDefaultSettings() {
        ParseSettings settings = xmlTreeBuilder.defaultSettings();
        
        assertTrue(settings.preserveCase());
    }

    @Test
    public void testParse() {
        Document document = xmlTreeBuilder.parse(input, baseUri);
        
        assertEquals("xml", document.nodeName());
        assertEquals(1, document.children().size());
        assertTrue(document.child(0) instanceof Element);
        Element rootElement = (Element) document.child(0);
        assertEquals("tag1", rootElement.child(0).nodeName());
        assertEquals("value1", rootElement.child(0).text());
        assertEquals("tag2", rootElement.child(1).nodeName());
        assertEquals("value2", rootElement.child(1).text());
    }

    @Test
    public void testInitialiseParse() {
        ParseErrorList errors = new ParseErrorList();
        xmlTreeBuilder.initialiseParse(input, baseUri, errors, xmlTreeBuilder.defaultSettings());
        
        assertEquals(1, xmlTreeBuilder.stack.size());
        assertTrue(xmlTreeBuilder.stack.get(0) instanceof Document);
        assertEquals(Document.OutputSettings.Syntax.xml, xmlTreeBuilder.doc.outputSettings().syntax());
    }

    @Test
    public void testProcessStartTag() {
        xmlTreeBuilder.initialiseParse(input, baseUri, ParseErrorList.noTracking(), xmlTreeBuilder.defaultSettings());
        Token.StartTag startTag = new Token.StartTag("tag", "tag", new Attributes());
        
        Element element = xmlTreeBuilder.insert(startTag);
        
        assertTrue(element instanceof Element);
        assertEquals("tag", element.nodeName());
        assertTrue(xmlTreeBuilder.tokeniser.isSelfClosing());
    }

    @Test
    public void testProcessEndTag() {
        xmlTreeBuilder.initialiseParse(input, baseUri, ParseErrorList.noTracking(), xmlTreeBuilder.defaultSettings());
        Element element1 = new Element("tag1", baseUri);
        Element element2 = new Element("tag2", baseUri);
        xmlTreeBuilder.stack.add(element1);
        xmlTreeBuilder.stack.add(element2);
        Token.EndTag endTag = new Token.EndTag("tag2");
        
        xmlTreeBuilder.popStackToClose(endTag);
        
        assertEquals(1, xmlTreeBuilder.stack.size());
        assertTrue(xmlTreeBuilder.stack.get(0) instanceof Element);
        assertEquals(element1, xmlTreeBuilder.stack.get(0));
    }

    @Test
    public void testProcessComment() {
        xmlTreeBuilder.initialiseParse(input, baseUri, ParseErrorList.noTracking(), xmlTreeBuilder.defaultSettings());
        Token.Comment commentToken = new Token.Comment("<!- Comment -->");
        
        xmlTreeBuilder.insert(commentToken);
        
        assertEquals(1, xmlTreeBuilder.doc.childNodes().size());
        assertTrue(xmlTreeBuilder.doc.childNode(0) instanceof Comment);
        Comment comment = (Comment) xmlTreeBuilder.doc.childNode(0);
        assertEquals("<!- Comment -->", comment.getData());
    }

    @Test
    public void testProcessCharacter() {
        xmlTreeBuilder.initialiseParse(input, baseUri, ParseErrorList.noTracking(), xmlTreeBuilder.defaultSettings());
        Token.Character characterToken = new Token.Character("value");
        
        xmlTreeBuilder.insert(characterToken);
        
        assertEquals(1, xmlTreeBuilder.doc.childNodes().size());
        assertTrue(xmlTreeBuilder.doc.childNode(0) instanceof TextNode);
        TextNode textNode = (TextNode) xmlTreeBuilder.doc.childNode(0);
        assertEquals("value", textNode.text());
    }

    @Test
    public void testProcessDoctype() {
        xmlTreeBuilder.initialiseParse(input, baseUri, ParseErrorList.noTracking(), xmlTreeBuilder.defaultSettings());
        Token.Doctype doctype = new Token.Doctype("doctype", "", "", baseUri);
        
        xmlTreeBuilder.insert(doctype);
        
        assertEquals(1, xmlTreeBuilder.doc.childNodes().size());
        assertTrue(xmlTreeBuilder.doc.childNode(0) instanceof DocumentType);
        DocumentType documentType = (DocumentType) xmlTreeBuilder.doc.childNode(0);
        assertEquals("doctype", documentType.getName());
    }

    @Test
    public void testInsertNode() {
        xmlTreeBuilder.initialiseParse(input, baseUri, ParseErrorList.noTracking(), xmlTreeBuilder.defaultSettings());
        Element element = new Element("tag", baseUri);
        
        xmlTreeBuilder.insertNode(element);
        
        assertEquals(1, xmlTreeBuilder.currentElement().children().size());
        assertEquals(element, xmlTreeBuilder.currentElement().child(0));
    }

    @Test
    public void testInsertStartTag() {
        xmlTreeBuilder.initialiseParse(input, baseUri, ParseErrorList.noTracking(), xmlTreeBuilder.defaultSettings());
        Token.StartTag startTag = new Token.StartTag("tag", "tag", new Attributes());
        
        Element element = xmlTreeBuilder.insert(startTag);
        
        assertTrue(element instanceof Element);
        assertEquals("tag", element.nodeName());

        xmlTreeBuilder.tokeniser.acknowledgeSelfClosingFlag();
        assertTrue(startTag.isSelfClosing());
        assertTrue(element.tag().isSelfClosing());
    }

    @Test
    public void testInsertComment() {
        xmlTreeBuilder.initialiseParse(input, baseUri, ParseErrorList.noTracking(), xmlTreeBuilder.defaultSettings());
        Token.Comment commentToken = new Token.Comment("<!- Comment -->");
        
        xmlTreeBuilder.insert(commentToken);
        
        assertEquals(1, xmlTreeBuilder.doc.childNodes().size());
        assertTrue(xmlTreeBuilder.doc.childNode(0) instanceof Comment);
        Comment comment = (Comment) xmlTreeBuilder.doc.childNode(0);
        assertEquals("<!- Comment -->", comment.getData());
    }

    @Test
    public void testInsertCharacter() {
        xmlTreeBuilder.initialiseParse(input, baseUri, ParseErrorList.noTracking(), xmlTreeBuilder.defaultSettings());
        Token.Character characterToken = new Token.Character("value");
        
        xmlTreeBuilder.insert(characterToken);
        
        assertEquals(1, xmlTreeBuilder.doc.childNodes().size());
        assertTrue(xmlTreeBuilder.doc.childNode(0) instanceof TextNode);
        TextNode textNode = (TextNode) xmlTreeBuilder.doc.childNode(0);
        assertEquals("value", textNode.text());
    }

    @Test
    public void testInsertDoctype() {
        xmlTreeBuilder.initialiseParse(input, baseUri, ParseErrorList.noTracking(), xmlTreeBuilder.defaultSettings());
        Token.Doctype doctype = new Token.Doctype("doctype", "", "", baseUri);
        
        xmlTreeBuilder.insert(doctype);
        
        assertEquals(1, xmlTreeBuilder.doc.childNodes().size());
        assertTrue(xmlTreeBuilder.doc.childNode(0) instanceof DocumentType);
        DocumentType documentType = (DocumentType) xmlTreeBuilder.doc.childNode(0);
        assertEquals("doctype", documentType.getName());
    }

    @Test
    public void testPopStackToClose() {
        xmlTreeBuilder.initialiseParse(input, baseUri, ParseErrorList.noTracking(), xmlTreeBuilder.defaultSettings());
        Element element1 = new Element("tag1", baseUri);
        Element element2 = new Element("tag2", baseUri);
        xmlTreeBuilder.stack.add(element1);
        xmlTreeBuilder.stack.add(element2);
        Token.EndTag endTag = new Token.EndTag("tag2");

        xmlTreeBuilder.popStackToClose(endTag);

        assertEquals(1, xmlTreeBuilder.stack.size());
        assertTrue(xmlTreeBuilder.stack.get(0) instanceof Element);
        assertEquals(element1, xmlTreeBuilder.stack.get(0));
    }

    @Test
    public void testParseFragment() {
        List<Node> nodes = xmlTreeBuilder.parseFragment(input, baseUri, ParseErrorList.noTracking(), xmlTreeBuilder.defaultSettings());

        assertEquals(1, nodes.size());
        assertTrue(nodes.get(0) instanceof Element);
        Element element = (Element) nodes.get(0);
        assertEquals("xml", element.nodeName());
        assertEquals(2, element.children().size());
        assertEquals("tag1", element.child(0).nodeName());
        assertEquals("value1", element.child(0).text());
        assertEquals("tag2", element.child(1).nodeName());
        assertEquals("value2", element.child(1).text());
    }
}
