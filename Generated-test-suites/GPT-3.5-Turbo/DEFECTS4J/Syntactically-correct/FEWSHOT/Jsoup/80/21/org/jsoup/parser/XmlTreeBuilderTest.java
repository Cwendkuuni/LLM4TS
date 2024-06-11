package org.jsoup.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotEquals;

import org.junit.Test;

public class XmlTreeBuilderTest {
    private XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
    
    @Test
    public void testDefaultSettings() {
        assertEquals(ParseSettings.preserveCase, xmlTreeBuilder.defaultSettings());
    }
    
    @Test
    public void testParseWithReaderAndBaseUri() {
        Reader reader = new StringReader("<tag></tag>");
        String baseUri = "https://www.example.com";
        Document document = xmlTreeBuilder.parse(reader, baseUri);
        
        assertNotNull(document);
        assertEquals(baseUri, document.baseUri());
        assertEquals(1, document.childNodeSize());
        assertTrue(document.childNode(0) instanceof Element);
        assertEquals("tag", document.childNode(0).nodeName());
    }
    
    @Test
    public void testParseWithStringAndBaseUri() {
        String input = "<tag></tag>";
        String baseUri = "https://www.example.com";
        Document document = xmlTreeBuilder.parse(input, baseUri);
        
        assertNotNull(document);
        assertEquals(baseUri, document.baseUri());
        assertEquals(1, document.childNodeSize());
        assertTrue(document.childNode(0) instanceof Element);
        assertEquals("tag", document.childNode(0).nodeName());
    }
    
    @Test
    public void testInitialiseParse() {
        Reader input = new StringReader("<tag></tag>");
        String baseUri = "https://www.example.com";
        ParseErrorList errors = ParseErrorList.noTracking();
        ParseSettings settings = ParseSettings.preserveCase;
        
        xmlTreeBuilder.initialiseParse(input, baseUri, errors, settings);
        
        assertEquals(1, xmlTreeBuilder.stack.size());
        assertTrue(xmlTreeBuilder.stack.get(0) instanceof Document);
        assertEquals(Document.OutputSettings.Syntax.xml, xmlTreeBuilder.doc.outputSettings().syntax());
    }
    
    @Test
    public void testProcessStartTag() {
        Token.StartTag startTag = new Token.StartTag("tag");
        startTag.attributes.put("attr1", "value1");
        startTag.attributes.put("attr2", "value2");
        
        Element element = xmlTreeBuilder.insert(startTag);
        
        assertNotNull(element);
        assertEquals("tag", element.tagName());
        assertEquals(2, element.attributes().size());
        assertEquals("value1", element.attributes().get("attr1"));
        assertEquals("value2", element.attributes().get("attr2"));
    }
    
    @Test
    public void testProcessEndTag() {
        Element element = new Element(Tag.valueOf("tag"), "https://www.example.com");
        xmlTreeBuilder.stack.add(element);
        
        Token.EndTag endTag = new Token.EndTag("tag");
        xmlTreeBuilder.popStackToClose(endTag);
        
        assertEquals(0, xmlTreeBuilder.stack.size());
    }
    
    @Test
    public void testProcessComment() {
        Token.Comment commentToken = new Token.Comment("Test comment");
        xmlTreeBuilder.insert(commentToken);
        
        assertTrue(xmlTreeBuilder.currentElement().childNode(0) instanceof Comment);
        Comment comment = (Comment) xmlTreeBuilder.currentElement().childNode(0);
        assertEquals("Test comment", comment.getData());
    }
    
    @Test
    public void testProcessCharacter() {
        Token.Character charToken = new Token.Character("Test character");
        xmlTreeBuilder.insert(charToken);
        
        assertTrue(xmlTreeBuilder.currentElement().childNode(0) instanceof TextNode);
        TextNode textNode = (TextNode) xmlTreeBuilder.currentElement().childNode(0);
        assertEquals("Test character", textNode.getWholeText());
        assertFalse(textNode.isCData());
    }
    
    @Test
    public void testProcessCharacterCData() {
        Token.Character charToken = new Token.Character("Test character");
        charToken.isCData = true;
        xmlTreeBuilder.insert(charToken);
        
        assertTrue(xmlTreeBuilder.currentElement().childNode(0) instanceof CDataNode);
        CDataNode cDataNode = (CDataNode) xmlTreeBuilder.currentElement().childNode(0);
        assertEquals("Test character", cDataNode.getWholeText());
        assertTrue(cDataNode.isCData());
    }
    
    @Test
    public void testProcessDoctype() {
        Token.Doctype doctypeToken = new Token.Doctype("html", "", "");
        xmlTreeBuilder.insert(doctypeToken);
        
        assertTrue(xmlTreeBuilder.currentElement().childNode(0) instanceof DocumentType);
        DocumentType doctype = (DocumentType) xmlTreeBuilder.currentElement().childNode(0);
        assertEquals("html", doctype.name());
        assertEquals("", doctype.publicIdentifier());
        assertEquals("", doctype.systemIdentifier());
        assertNotEquals("", doctype.getPubSysKey());
    }
    
    @Test
    public void testInsertNode() {
        Element element = new Element(Tag.valueOf("tag"), "https://www.example.com");
        xmlTreeBuilder.stack.add(element);
        
        Node node = new Element(Tag.valueOf("child"), "https://www.example.com");
        xmlTreeBuilder.insertNode(node);
        
        assertEquals(1, xmlTreeBuilder.currentElement().childNodes().size());
        assertEquals("tag", xmlTreeBuilder.currentElement().childNodes().get(0).nodeName());
        Element childElement = (Element) xmlTreeBuilder.currentElement().childNodes().get(0);
        assertEquals("child", childElement.tagName());
    }
    
    @Test
    public void testParseFragment() {
        String inputFragment = "<div>Test fragment</div>";
        String baseUri = "https://www.example.com";
        ParseErrorList errors = ParseErrorList.noTracking();
        ParseSettings settings = ParseSettings.preserveCase;
        
        List<Node> nodes = xmlTreeBuilder.parseFragment(inputFragment, baseUri, errors, settings);
        
        assertEquals(1, nodes.size());
        assertTrue(nodes.get(0) instanceof Element);
        Element element = (Element) nodes.get(0);
        assertEquals("div", element.tagName());
        assertEquals("Test fragment", element.ownText());
    }
}