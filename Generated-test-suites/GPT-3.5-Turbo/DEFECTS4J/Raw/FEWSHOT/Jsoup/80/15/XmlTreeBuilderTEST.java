package org.jsoup.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.Reader;
import java.io.StringReader;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.helper.Validate;
import org.jsoup.nodes.CDataNode;
import org.jsoup.nodes.Comment;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.DocumentType;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.jsoup.parser.Token.StartTag;
import org.jsoup.parser.XmlTreeBuilder;
import org.junit.Test;

public class XmlTreeBuilderTest {
    
    @Test
    public void testParse_withReaderAndBaseUri_returnsDocument() {
        Reader reader = new StringReader("<root><element></element></root>");
        String baseUri = "http://example.com/";
        Document doc = new XmlTreeBuilder().parse(reader, baseUri);
        
        assertEquals("root", doc.nodeName());
        assertEquals(baseUri, doc.baseUri());
        assertEquals(1, doc.childNodeSize());
        
        Element root = doc.child(0);
        assertEquals("element", root.nodeName());
        assertEquals(baseUri, root.baseUri());
    }
    
    @Test
    public void testParse_withStringAndBaseUri_returnsDocument() {
        String input = "<root><element></element></root>";
        String baseUri = "http://example.com/";
        Document doc = new XmlTreeBuilder().parse(input, baseUri);
        
        assertEquals("root", doc.nodeName());
        assertEquals(baseUri, doc.baseUri());
        assertEquals(1, doc.childNodeSize());
        
        Element root = doc.child(0);
        assertEquals("element", root.nodeName());
        assertEquals(baseUri, root.baseUri());
    }
    
    @Test
    public void testInitialiseParse_setsOutputSettingsSyntaxToXml() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        xmlTreeBuilder.initialiseParse(new StringReader(""), "http://example.com/", new ParseErrorList(), new ParseSettings());
        
        assertEquals(Document.OutputSettings.Syntax.xml, xmlTreeBuilder.doc.outputSettings().syntax());
    }
    
    @Test
    public void testProcess_withStartTagToken_insertsElementToStack() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Element expectedElement = new Element(Tag.valueOf("element"), "http://example.com/", new ParseSettings().normalizeAttributes(new StartTag("element")));
        
        xmlTreeBuilder.process(new Token.StartTag("element"));
        
        assertEquals(expectedElement, xmlTreeBuilder.currentElement().child(0));
    }
    
    @Test
    public void testProcess_withEndTagToken_removesElementFromStack() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Element root = new Element(Tag.valueOf("root"), "http://example.com/", new ParseSettings().normalizeAttributes(new StartTag("root")));
        Element element = new Element(Tag.valueOf("element"), "http://example.com/", new ParseSettings().normalizeAttributes(new StartTag("element")));
        root.appendChild(element);
        xmlTreeBuilder.stack.add(root);
        xmlTreeBuilder.stack.add(element);
        
        xmlTreeBuilder.process(new Token.EndTag("root"));
        
        assertEquals(root, xmlTreeBuilder.stack.get(0));
        assertEquals(1, xmlTreeBuilder.stack.size());
    }
    
    @Test
    public void testProcess_withCommentToken_insertsCommentNode() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Comment expectedComment = new Comment("comment");
        
        xmlTreeBuilder.process(new Token.Comment("comment"));
        
        assertEquals(expectedComment, xmlTreeBuilder.currentElement().child(0));
    }
    
    @Test
    public void testProcess_withCharacterToken_insertsTextNode() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        TextNode expectedText = new TextNode("text");
        
        xmlTreeBuilder.process(new Token.Character("text", false));
        
        assertEquals(expectedText, xmlTreeBuilder.currentElement().child(0));
    }
    
    @Test
    public void testProcess_withCDATACharacterToken_insertsCDataNode() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        CDataNode expectedCData = new CDataNode("cdata");
        
        xmlTreeBuilder.process(new Token.Character("cdata", true));
        
        assertEquals(expectedCData, xmlTreeBuilder.currentElement().child(0));
    }
    
    @Test
    public void testProcess_withDoctypeToken_insertsDocumentTypeNode() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        DocumentType expectedDoctype = new DocumentType("doctype", "", "");
        
        xmlTreeBuilder.process(new Token.Doctype("doctype", "", ""));
        
        assertEquals(expectedDoctype, xmlTreeBuilder.currentElement().child(0));
    }
    
    @Test
    public void testInsertNode_appendsNodeToCurrentElement() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Element root = new Element(Tag.valueOf("root"), "http://example.com/");
        xmlTreeBuilder.stack.add(root);
        Node node = new Element(Tag.valueOf("element"), "http://example.com/");
        
        xmlTreeBuilder.insertNode(node);
        
        assertEquals(node, xmlTreeBuilder.currentElement().child(0));
    }
    
    @Test
    public void testInsert_withStartTag_createsElementAndInsertsNode() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Element expectedElement = new Element(Tag.valueOf("element"), "http://example.com/", new ParseSettings().normalizeAttributes(new StartTag("element")));
        
        xmlTreeBuilder.insert(new Token.StartTag("element"));
        
        assertEquals(expectedElement, xmlTreeBuilder.currentElement().child(0));
    }
    
    @Test
    public void testInsert_withSelfClosingStartTag_createsElementAndInsertsNodeAndSetsTagSelfClosing() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Element expectedElement = new Element(Tag.valueOf("element"), "http://example.com/", new ParseSettings().normalizeAttributes(new StartTag("element")));
        expectedElement.tag().setSelfClosing();
        
        xmlTreeBuilder.insert(new Token.StartTag("element", "", true));
        
        assertEquals(expectedElement, xmlTreeBuilder.currentElement().child(0));
    }
    
    @Test
    public void testInsert_withCommentToken_insertsCommentNode() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Comment expectedComment = new Comment("comment");
        
        xmlTreeBuilder.insert(new Token.Comment("comment"));
        
        assertEquals(expectedComment, xmlTreeBuilder.currentElement().child(0));
    }
    
    @Test
    public void testInsert_withBogusCommentToken_insertsXmlDeclarationNode() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Comment commentToken = new Comment("<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\"");
        Comment expectedComment = new Comment("<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\">");
        
        xmlTreeBuilder.insert(commentToken);
        
        assertEquals(expectedComment, xmlTreeBuilder.currentElement().child(0));
    }
    
    @Test
    public void testInsert_withCharacterToken_insertsTextNode() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        TextNode expectedText = new TextNode("text");
        
        xmlTreeBuilder.insert(new Token.Character("text"));
        
        assertEquals(expectedText, xmlTreeBuilder.currentElement().child(0));
    }
    
    @Test
    public void testInsert_withCDATACharacterToken_insertsCDataNode() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        CDataNode expectedCData = new CDataNode("cdata");
        
        xmlTreeBuilder.insert(new Token.Character("cdata", true));
        
        assertEquals(expectedCData, xmlTreeBuilder.currentElement().child(0));
    }
    
    @Test
    public void testInsert_withDoctypeToken_insertsDocumentTypeNode() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        DocumentType expectedDoctype = new DocumentType("doctype", "", "");
        
        xmlTreeBuilder.insert(new Token.Doctype("doctype", "", ""));
        
        assertEquals(expectedDoctype, xmlTreeBuilder.currentElement().child(0));
    }
    
    @Test
    public void testPopStackToClose_withEndTag_removesElementFromStack() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Element element1 = new Element(Tag.valueOf("element"), "http://example.com/");
        Element element2 = new Element(Tag.valueOf("element"), "http://example.com/");
        xmlTreeBuilder.stack.add(element1);
        xmlTreeBuilder.stack.add(element2);
        
        xmlTreeBuilder.popStackToClose(new Token.EndTag("element"));
        
        assertEquals(element1, xmlTreeBuilder.stack.get(0));
        assertEquals(1, xmlTreeBuilder.stack.size());
    }
    
    @Test
    public void testPopStackToClose_withEndTag_notInStack_doesNotChangeStack() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Element element1 = new Element(Tag.valueOf("element"), "http://example.com/");
        Element element2 = new Element(Tag.valueOf("element"), "http://example.com/");
        xmlTreeBuilder.stack.add(element1);
        
        xmlTreeBuilder.popStackToClose(new Token.EndTag("element"));
        
        assertEquals(element1, xmlTreeBuilder.stack.get(0));
        assertEquals(1, xmlTreeBuilder.stack.size());
    }
    
    @Test
    public void testParseFragment_parsesInputFragmentAndReturnsList() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        String inputFragment = "<element>text</element>";
        String baseUri = "http://example.com/";
        
        List<Node> nodes = xmlTreeBuilder.parseFragment(inputFragment, baseUri, new ParseErrorList(), new ParseSettings());
        
        assertEquals(1, nodes.size());
        assertTrue(nodes.get(0) instanceof Element);
        assertEquals("element", nodes.get(0).nodeName());
        assertEquals(baseUri, nodes.get(0).baseUri());
    }
    
    @Test
    public void testDefaultSettings_returnsParseSettingsPreserveCase() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        ParseSettings expectedSettings = ParseSettings.preserveCase;
        
        ParseSettings actualSettings = xmlTreeBuilder.defaultSettings();
        
        assertEquals(expectedSettings, actualSettings);
    }
}