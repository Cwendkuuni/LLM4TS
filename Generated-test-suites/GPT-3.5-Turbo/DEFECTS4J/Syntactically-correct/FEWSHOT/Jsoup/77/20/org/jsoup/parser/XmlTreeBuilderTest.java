package org.jsoup.parser;

import org.jsoup.Jsoup;
import org.jsoup.helper.Validate;
import org.jsoup.nodes.*;

import java.io.StringReader;
import java.util.List;

import static org.junit.Assert.*;
import org.junit.Test;

public class XmlTreeBuilderTest {
    @Test
    public void testDefaultSettings() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        ParseSettings parseSettings = xmlTreeBuilder.defaultSettings();
        
        assertNotNull(parseSettings);
        assertEquals(ParseSettings.preserveCase, parseSettings);
    }
    
    @Test
    public void testParseWithReaderAndBaseUri() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        MockReader mockReader = new MockReader();
        MockDocument mockDocument = new MockDocument();
        Document result = xmlTreeBuilder.parse(mockReader, "http://example.com");
        
        assertNotNull(result);
        assertEquals(mockDocument, result);
    }
    
    @Test
    public void testParseWithStringAndBaseUri() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        MockDocument mockDocument = new MockDocument();
        Document result = xmlTreeBuilder.parse("input", "http://example.com");
        
        assertNotNull(result);
        assertEquals(mockDocument, result);
    }
    
    @Test
    public void testInitialiseParse() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        MockReader mockReader = new MockReader();
        MockDocument mockDocument = new MockDocument();
        xmlTreeBuilder.initialiseParse(mockReader, "http://example.com", ParseErrorList.noTracking(), ParseSettings.preserveCase);
        
        assertTrue(xmlTreeBuilder.getStack().contains(mockDocument));
        assertEquals(Document.OutputSettings.Syntax.xml, mockDocument.getOutputSettings().syntax());
    }
    
    @Test
    public void testProcess() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        MockToken mockToken = new MockToken(Token.TokenType.StartTag);
        MockElement mockElement = new MockElement();
        xmlTreeBuilder.process(mockToken);
        
        assertTrue(mockElement.isApplied);
        assertTrue(xmlTreeBuilder.isProcessResult());
        assertEquals("Unexpected token type: " + mockToken.type, xmlTreeBuilder.getErrorMessage());
    }
    
    @Test
    public void testInsertNode() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        MockNode mockNode = new MockNode();
        MockElement mockElement = new MockElement();
        xmlTreeBuilder.insertNode(mockNode);
        
        assertTrue(mockElement.isNodeAppended);
    }
    
    @Test
    public void testInsertStartTag() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        MockStartTag mockStartTag = new MockStartTag("example");
        MockElement mockElement = new MockElement();
        Element result = xmlTreeBuilder.insert(mockStartTag);
        
        assertNotNull(result);
        assertTrue(mockElement.isApplied);
    }
    
    @Test
    public void testInsertComment() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        MockComment mockComment = new MockComment("comment");
        MockNode mockNode = new MockNode();
        xmlTreeBuilder.insert(mockComment);
        
        assertTrue(mockNode.isCommentAppended);
    }
    
    @Test
    public void testInsertCharacter() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        MockCharacter mockCharacter = new MockCharacter("data");
        MockNode mockNode = new MockNode();
        xmlTreeBuilder.insert(mockCharacter);
        
        assertTrue(mockNode.isCharacterAppended);
    }
    
    @Test
    public void testInsertDoctype() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        MockDoctype mockDoctype = new MockDoctype("name", "publicId", "systemId");
        MockNode mockNode = new MockNode();
        xmlTreeBuilder.insert(mockDoctype);
        
        assertTrue(mockNode.isDoctypeAppended);
    }
    
    @Test
    public void testPopStackToClose() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        MockEndTag mockEndTag = new MockEndTag("example");
        MockElement mockElement = new MockElement();
        xmlTreeBuilder.getStack().add(mockElement);
        xmlTreeBuilder.getStack().add(new MockElement());
        xmlTreeBuilder.popStackToClose(mockEndTag);
        
        assertFalse(xmlTreeBuilder.getStack().contains(mockElement));
    }
    
    @Test
    public void testParseFragment() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        MockDocument mockDocument = new MockDocument();
        List<Node> result = xmlTreeBuilder.parseFragment("inputFragment", "http://example.com", ParseErrorList.noTracking(), ParseSettings.preserveCase);
        
        assertNotNull(result);
        assertEquals(mockDocument.childNodes(), result);
    }
}