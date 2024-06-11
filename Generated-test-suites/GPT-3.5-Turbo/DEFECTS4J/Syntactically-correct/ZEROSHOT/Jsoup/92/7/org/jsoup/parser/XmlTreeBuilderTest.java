import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.parser.Parser;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.io.StringReader;
import java.util.List;

public class XmlTreeBuilderTest {
    
    private XmlTreeBuilder xmlTreeBuilder;
    private String xmlInput;
    
    @Before
    public void setUp() {
        xmlTreeBuilder = new XmlTreeBuilder();
        xmlInput = "<root><element1>Text 1</element1><element2>Text 2</element2></root>";
    }
    
    @Test
    public void testDefaultSettings() {
        ParseSettings settings = xmlTreeBuilder.defaultSettings();
        assertNotNull(settings);
        assertEquals(ParseSettings.preserveCase, settings);
    }
    
    @Test
    public void testInitialiseParse() {
        String baseUri = "http://www.example.com";
        Parser parser = Parser.xmlParser();
        
        xmlTreeBuilder.initialiseParse(new StringReader(xmlInput), baseUri, parser);
        
        // Verify that the document is placed on the stack
        assertEquals(1, xmlTreeBuilder.stack.size());
        assertEquals(Document.class, xmlTreeBuilder.stack.get(0).getClass());
        
        // Verify that the document's output settings are set to XML syntax
        assertEquals(Document.OutputSettings.Syntax.xml, xmlTreeBuilder.doc.outputSettings().syntax());
    }
    
    @Test
    public void testParse() {
        String baseUri = "http://www.example.com";
        
        Document document = xmlTreeBuilder.parse(new StringReader(xmlInput), baseUri);
        
        // Verify that the document is parsed correctly
        assertNotNull(document);
        assertEquals("<root>\n" +
                     " <element1>Text 1</element1>\n" +
                     " <element2>Text 2</element2>\n" +
                     "</root>",
                     document.html());
    }
    
    @Test
    public void testParseString() {
        String baseUri = "http://www.example.com";
        
        Document document = xmlTreeBuilder.parse(xmlInput, baseUri);
        
        // Verify that the document is parsed correctly
        assertNotNull(document);
        assertEquals("<root>\n" +
                     " <element1>Text 1</element1>\n" +
                     " <element2>Text 2</element2>\n" +
                     "</root>",
                     document.html());
    }
    
    @Test
    public void testProcess_StartTag() {
        Element element = new Element("element1");
        element.text("Text 1");
        Token.StartTag startTag = new Token.StartTag("element1");
        
        xmlTreeBuilder.process(startTag);
        
        // Verify that the start tag is inserted correctly
        assertEquals(element, xmlTreeBuilder.currentElement().child(0));
    }
    
    @Test
    public void testProcess_EndTag() {
        Element element = new Element("element1");
        element.text("Text 1");
        xmlTreeBuilder.stack.add(xmlTreeBuilder.doc);
        xmlTreeBuilder.stack.add(element);
        Token.EndTag endTag = new Token.EndTag("element1");
        
        xmlTreeBuilder.process(endTag);
        
        // Verify that the end tag is removed from the stack
        assertEquals(1, xmlTreeBuilder.stack.size());
        assertEquals(xmlTreeBuilder.doc, xmlTreeBuilder.stack.get(0));
    }
    
    @Test
    public void testProcess_Comment() {
        String commentData = "This is a comment";
        Token.Comment comment = new Token.Comment(commentData);
        
        xmlTreeBuilder.process(comment);
        
        // Verify that the comment is inserted correctly
        assertEquals(commentData, xmlTreeBuilder.currentElement().childNode(0).outerHtml());
    }
    
    @Test
    public void testProcess_Character_TextNode() {
        String textData = "Text 1";
        Token.Character character = new Token.Character(textData);
        
        xmlTreeBuilder.process(character);
        
        // Verify that the text node is inserted correctly
        assertEquals(textData, xmlTreeBuilder.currentElement().childNode(0).outerHtml());
    }
    
    @Test
    public void testProcess_Character_CDataNode() {
        String cdataData = "<![CDATA[Text 1]]>";
        Token.Character character = new Token.Character(cdataData);
        character.setIsCData();
        
        xmlTreeBuilder.process(character);
        
        // Verify that the CDATA node is inserted correctly
        assertEquals(cdataData, xmlTreeBuilder.currentElement().childNode(0).outerHtml());
    }
    
    @Test
    public void testProcess_Doctype() {
        String name = "html";
        String publicIdentifier = "-//W3C//DTD XHTML 1.0 Strict//EN";
        String systemIdentifier = "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd";
        Token.Doctype doctype = new Token.Doctype(name, publicIdentifier, systemIdentifier);
        
        xmlTreeBuilder.process(doctype);
        
        // Verify that the doctype node is inserted correctly
        assertEquals("<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Strict//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd\">",
                     xmlTreeBuilder.currentElement().childNode(0).outerHtml());
    }
    
    @Test
    public void testInsertNode() {
        Element element = new Element("element1");
        element.text("Text 1");
        xmlTreeBuilder.stack.add(element);
        
        Element node = new Element("element2");
        node.text("Text 2");
        xmlTreeBuilder.insertNode(node);
        
        // Verify that the node is inserted correctly
        assertEquals(node, xmlTreeBuilder.currentElement().child(0));
    }
    
    @Test
    public void testInsert_StartTag() {
        String tagName = "element1";
        Token.StartTag startTag = new Token.StartTag(tagName);
        
        Element element = xmlTreeBuilder.insert(startTag);
        
        // Verify that the element is inserted correctly
        assertNotNull(element);
        assertEquals(tagName, element.tagName());
        assertEquals(xmlTreeBuilder.baseUri, element.baseUri());
    }
    
    @Test
    public void testInsert_Comment_NotBogus() {
        String commentData = "This is a comment";
        Token.Comment comment = new Token.Comment(commentData);
        
        xmlTreeBuilder.insert(comment);
        
        // Verify that the comment is inserted correctly
        assertEquals(commentData, xmlTreeBuilder.currentElement().childNode(0).outerHtml());
    }
    
    @Test
    public void testInsert_Comment_BogusAsXmlDeclaration() {
        String commentData = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";
        Token.Comment comment = new Token.Comment(commentData);
        comment.setIsBogus();
        
        xmlTreeBuilder.insert(comment);
        
        // Verify that the XML declaration is inserted correctly
        assertEquals(commentData, xmlTreeBuilder.currentElement().childNode(0).outerHtml());
    }
    
    @Test
    public void testInsert_Character_TextNode() {
        String textData = "Text 1";
        Token.Character character = new Token.Character(textData);
        
        xmlTreeBuilder.insert(character);
        
        // Verify that the text node is inserted correctly
        assertEquals(textData, xmlTreeBuilder.currentElement().childNode(0).outerHtml());
    }
    
    @Test
    public void testInsert_Character_CDataNode() {
        String cdataData = "<![CDATA[Text 1]]>";
        Token.Character character = new Token.Character(cdataData);
        character.setIsCData();
        
        xmlTreeBuilder.insert(character);
        
        // Verify that the CDATA node is inserted correctly
        assertEquals(cdataData, xmlTreeBuilder.currentElement().childNode(0).outerHtml());
    }
    
    @Test
    public void testInsert_Doctype() {
        String name = "html";
        String publicIdentifier = "-//W3C//DTD XHTML 1.0 Strict//EN";
        String systemIdentifier = "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd";
        Token.Doctype doctype = new Token.Doctype(name, publicIdentifier, systemIdentifier);
        
        xmlTreeBuilder.insert(doctype);
        
        // Verify that the doctype node is inserted correctly
        assertEquals("<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Strict//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd\">",
                     xmlTreeBuilder.currentElement().childNode(0).outerHtml());
    }
    
    @Test
    public void testPopStackToClose_ElementFound() {
        Element element1 = new Element("element1");
        Element element2 = new Element("element2");
        xmlTreeBuilder.stack.add(element1);
        xmlTreeBuilder.stack.add(element2);
        Token.EndTag endTag = new Token.EndTag("element2");
        
        xmlTreeBuilder.popStackToClose(endTag);
        
        // Verify that the element is removed from the stack
        assertEquals(1, xmlTreeBuilder.stack.size());
        assertEquals(element1, xmlTreeBuilder.stack.get(0));
    }
    
    @Test
    public void testPopStackToClose_ElementNotFound() {
        Element element = new Element("element");
        xmlTreeBuilder.stack.add(element);
        Token.EndTag endTag = new Token.EndTag("unknown");
        
        xmlTreeBuilder.popStackToClose(endTag);
        
        // Verify that the stack is not modified
        assertEquals(1, xmlTreeBuilder.stack.size());
        assertEquals(element, xmlTreeBuilder.stack.get(0));
    }
    
    @Test
    public void testParseFragment_String() {
        String inputFragment = "<element1>Text 1</element1>";
        String baseUri = "http://www.example.com";
        Parser parser = Parser.xmlParser();
        
        List<Node> nodes = xmlTreeBuilder.parseFragment(inputFragment, baseUri, parser);
        
        // Verify that the fragment is parsed correctly
        assertNotNull(nodes);
        assertEquals(1, nodes.size());
        assertEquals("[#document, <element1>Text 1</element1>]", 
                     xmlTreeBuilder.doc.childNodes().toString());
    }
    
    @Test
    public void testParseFragment_Element() {
        String inputFragment = "<element1>Text 1</element1>";
        Element context = new Element("root");
        String baseUri = "http://www.example.com";
        Parser parser = Parser.xmlParser();
        
        List<Node> nodes = xmlTreeBuilder.parseFragment(inputFragment, context, baseUri, parser);
        
        // Verify that the fragment is parsed correctly
        assertNotNull(nodes);
        assertEquals(1, nodes.size());
        assertEquals("[<element1>Text 1</element1>]", 
                     context.childNodes().toString());
    }
}