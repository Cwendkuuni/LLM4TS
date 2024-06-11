package org.jsoup.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

import org.jsoup.nodes.CDataNode;
import org.jsoup.nodes.Comment;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.DocumentType;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.jsoup.nodes.XmlDeclaration;

import java.io.Reader;
import java.io.StringReader;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class XmlTreeBuilderTest {

    @Mock
    private Parser mockParser;
    
    private XmlTreeBuilder xmlTreeBuilder;
    private String input;
    private String baseUri;
    private Token.StartTag startTag;
    private Token.EndTag endTag;
    private Token.Comment commentToken;
    private Token.Character characterToken;
    private Token.Doctype doctype;
    private Element context;
    
    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        xmlTreeBuilder = new XmlTreeBuilder();
        input = "<tag>content</tag>";
        baseUri = "https://example.com/";
        startTag = new Token.StartTag("tag", "[]", true);
        endTag = new Token.EndTag("tag");
        commentToken = new Token.Comment("comment");
        characterToken = new Token.Character("content");
        doctype = new Token.Doctype("doctype", "", "");
        context = new Element("context");
    }
    
    @Test
    public void testDefaultSettings() {
        ParseSettings settings = xmlTreeBuilder.defaultSettings();
        assertEquals(ParseSettings.preserveCase, settings);
    }
    
    @Test
    public void testInitialiseParse() {
        Reader inputReader = new StringReader(input);
        String expectedOutput = "<tag>content</tag>";
        
        xmlTreeBuilder.initialiseParse(inputReader, baseUri, mockParser);
        
        verify(mockParser).initialiseParse(inputReader, baseUri, xmlTreeBuilder);
        assertEquals(expectedOutput, xmlTreeBuilder.doc.html());
    }
    
    @Test
    public void testParse() {
        String expectedOutput = "<tag>content</tag>";
        
        Document result = xmlTreeBuilder.parse(input, baseUri);
        
        assertEquals(expectedOutput, result.html());
    }
    
    @Test
    public void testInsertNode() {
        Node node = new Element("element");
        Element currentElement = spy(new Element("currentElement"));
        doReturn(currentElement).when(xmlTreeBuilder).currentElement();
        
        xmlTreeBuilder.insertNode(node);
        
        verify(currentElement).appendChild(node);
    }
    
    @Test
    public void testInsert_StartTag_SelfClosingKnownTag() {
        Tag tag = Tag.valueOf("tag");
        Element element = spy(new Element(tag, baseUri));
        assertEquals(tag, element.tag());
        
        Element result = xmlTreeBuilder.insert(startTag);
        
        verify(xmlTreeBuilder).insertNode(element);
        verify(xmlTreeBuilder.stack, times(0)).add(element);
        assertEquals(element, result);
    }
    
    @Test
    public void testInsert_StartTag_SelfClosingUnknownTag() {
        Tag tag = Tag.valueOf("tag");
        tag.setSelfClosing();
        Element element = spy(new Element(tag, baseUri));
        verify(tag, times(0)).setSelfClosing();
        
        Element result = xmlTreeBuilder.insert(startTag);
        
        verify(xmlTreeBuilder).insertNode(element);
        verify(xmlTreeBuilder.stack, times(0)).add(element);
        assertEquals(element, result);
        verify(tag, times(1)).setSelfClosing();
    }
    
    @Test
    public void testInsert_StartTag_NotSelfClosing() {
        Tag tag = Tag.valueOf("tag");
        Element element = spy(new Element(tag, baseUri));
        verify(tag, times(0)).setSelfClosing();
        
        Element result = xmlTreeBuilder.insert(startTag);
        
        verify(xmlTreeBuilder).insertNode(element);
        verify(xmlTreeBuilder.stack).add(element);
        assertEquals(element, result);
        verify(tag, times(0)).setSelfClosing();
    }
    
    @Test
    public void testInsert_Comment_NotBogus() {
        Comment comment = new Comment(commentToken.getData());
        Node insert = comment;
        Token.Comment notBogusCommentToken = spy(new Token.Comment("comment"));
        notBogusCommentToken.bogus = false;
        Token.Comment xmlDeclarationCommentToken = spy(new Token.Comment("xml declaration"));
        xmlDeclarationCommentToken.bogus = true;
        when(xmlDeclarationCommentToken.asXmlDeclaration()).thenReturn(new XmlDeclaration("version", "encoding", true));
        
        xmlTreeBuilder.insert(notBogusCommentToken);
        
        verify(xmlTreeBuilder).insertNode(insert);
        verify(xmlDeclarationCommentToken, times(0)).asXmlDeclaration();
    }
    
    @Test
    public void testInsert_Comment_Bogus() {
        Comment comment = new Comment(commentToken.getData());
        Node insert = comment;
        Token.Comment notBogusCommentToken = spy(new Token.Comment("comment"));
        notBogusCommentToken.bogus = false;
        Token.Comment xmlDeclarationCommentToken = spy(new Token.Comment("xml declaration"));
        xmlDeclarationCommentToken.bogus = true;
        when(xmlDeclarationCommentToken.asXmlDeclaration()).thenReturn(new XmlDeclaration("version", "encoding", true));
        
        xmlTreeBuilder.insert(xmlDeclarationCommentToken);
        
        verify(xmlTreeBuilder).insertNode(any(XmlDeclaration.class));
        verify(xmlDeclarationCommentToken, times(1)).asXmlDeclaration();
    }
    
    @Test
    public void testInsert_Character_CData() {
        Token.Character cDataToken = new Token.Character("content");
        cDataToken.dataIsCData = true;
        CDataNode cdata = spy(new CDataNode("content"));
        
        xmlTreeBuilder.insert(cDataToken);
        
        verify(xmlTreeBuilder).insertNode(cdata);
        verify(cDataToken, times(0)).getData();
    }
    
    @Test
    public void testInsert_Character_TextNode() {
        Token.Character textToken = new Token.Character("content");
        cDataToken.dataIsCData = false;
        TextNode textNode = spy(new TextNode("content"));
        
        xmlTreeBuilder.insert(textToken);
        
        verify(xmlTreeBuilder).insertNode(textNode);
        verify(textToken, times(0)).getData();
    }
    
    @Test
    public void testInsert_Doctype() {
        Token.Doctype doctypeToken = new Token.Doctype("doctype", "", "");
        DocumentType doctype = spy(new DocumentType("doctype", "", ""));
        doctype.setPubSysKey(doctypeToken.getPubSysKey());
        
        xmlTreeBuilder.insert(doctypeToken);
        
        verify(xmlTreeBuilder).insertNode(doctype);
    }
    
    @Test
    public void testPopStackToClose_NotFound() {
        Token.EndTag notFoundEndTag = new Token.EndTag("tag");
        
        xmlTreeBuilder.popStackToClose(notFoundEndTag);
        
        assertEquals(0, xmlTreeBuilder.stack.size());
    }
    
    @Test
    public void testPopStackToClose_Found() {
        Token.EndTag foundEndTag = new Token.EndTag("tag");
        Element firstFound = new Element("tag");
        xmlTreeBuilder.stack.add(new Element("other"));
        xmlTreeBuilder.stack.add(firstFound);
        
        xmlTreeBuilder.popStackToClose(foundEndTag);
        
        assertEquals(1, xmlTreeBuilder.stack.size());
        assertEquals(firstFound, xmlTreeBuilder.stack.get(0));
    }
    
    @Test
    public void testParseFragment_WithoutContext() {
        String inputFragment = "<tag>content</tag>";
        String expectedOutput = "<tag>content</tag>";
        
        List<Node> result = xmlTreeBuilder.parseFragment(inputFragment, baseUri, mockParser);
        
        verify(mockParser).initialiseParse(any(Reader.class), eq(baseUri), eq(xmlTreeBuilder));
        assertEquals(expectedOutput, xmlTreeBuilder.doc.html());
    }
    
    @Test
    public void testParseFragment_WithContext() {
        String inputFragment = "<tag>content</tag>";
        String expectedOutput = "<tag>content</tag>";
        
        List<Node> result = xmlTreeBuilder.parseFragment(inputFragment, context, baseUri, mockParser);
        
        verify(mockParser).initialiseParse(any(Reader.class), eq(baseUri), eq(xmlTreeBuilder));
        assertEquals(expectedOutput, xmlTreeBuilder.doc.html());
    }
}