import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.jsoup.parser.XmlTreeBuilder;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.jsoup.nodes.CDataNode;
import org.jsoup.nodes.Comment;
import org.jsoup.nodes.DocumentType;
import org.jsoup.nodes.XmlDeclaration;
import org.jsoup.helper.Validate;

import java.io.Reader;
import java.io.StringReader;
import java.util.List;

public class XmlTreeBuilderTest {
    private XmlTreeBuilder xmlTreeBuilder;
    
    @Before
    public void setup() {
        xmlTreeBuilder = new XmlTreeBuilder();
    }
    
    @Test
    public void testDefaultSettings() {
        ParseSettings settings = xmlTreeBuilder.defaultSettings();
        assertEquals(ParseSettings.preserveCase, settings);
    }
    
    @Test
    public void testInitialiseParse() {
        String input = "<html><body><p>Hello World!</p></body></html>";
        Reader reader = new StringReader(input);
        String baseUri = "http://example.com";
        Parser parser = new Parser(xmlTreeBuilder);
        xmlTreeBuilder.initialiseParse(reader, baseUri, parser);
        
        assertEquals(baseUri, xmlTreeBuilder.baseUri());
        assertEquals(parser, xmlTreeBuilder.parser());
        assertTrue(xmlTreeBuilder.stack().contains(xmlTreeBuilder.doc()));
        assertEquals(Document.OutputSettings.Syntax.xml, xmlTreeBuilder.doc().outputSettings().syntax());
    }
    
    @Test
    public void testParseWithReaderAndBaseUri() {
        String input = "<html><body><p>Hello World!</p></body></html>";
        Reader reader = new StringReader(input);
        String baseUri = "http://example.com";
        Document doc = xmlTreeBuilder.parse(reader, baseUri);
        
        assertNotNull(doc);
        assertEquals(baseUri, doc.baseUri());
        assertEquals(1, doc.children().size());
        Element html = doc.children().first();
        assertEquals("html", html.nodeName());
        Element body = html.children().first();
        assertEquals("body", body.nodeName());
        Element p = body.children().first();
        assertEquals("p", p.nodeName());
        TextNode text = p.children().first().text();
        assertEquals("Hello World!", text.text());
    }
    
    @Test
    public void testParseWithStringAndBaseUri() {
        String input = "<html><body><p>Hello World!</p></body></html>";
        String baseUri = "http://example.com";
        Document doc = xmlTreeBuilder.parse(input, baseUri);
        
        assertNotNull(doc);
        assertEquals(baseUri, doc.baseUri());
        assertEquals(1, doc.children().size());
        Element html = doc.children().first();
        assertEquals("html", html.nodeName());
        Element body = html.children().first();
        assertEquals("body", body.nodeName());
        Element p = body.children().first();
        assertEquals("p", p.nodeName());
        TextNode text = p.children().first().text();
        assertEquals("Hello World!", text.text());
    }
    
    @Test
    public void testProcessStartTag() {
        Token.StartTag startTag = new Token.StartTag();
        startTag.attr("name", "value");
        startTag.attr("another", "attribute");
        startTag.setSelfClosing();
        
        Element el = xmlTreeBuilder.insert(startTag);
        
        assertNotNull(el);
        assertEquals("name", el.attributes().get(0).getKey());
        assertEquals("value", el.attributes().get(0).getValue());
        assertEquals("another", el.attributes().get(1).getKey());
        assertEquals("attribute", el.attributes().get(1).getValue());
        assertTrue(el.tag().isSelfClosing());
    }
    
    @Test
    public void testInsertNode() {
        Element el = new Element("p");
        xmlTreeBuilder.insertNode(el);
        
        assertEquals(1, xmlTreeBuilder.currentElement().children().size());
        assertSame(el, xmlTreeBuilder.currentElement().children().first());
    }
    
    @Test
    public void testInsertTextNode() {
        Token.Character token = new Token.Character();
        token.setData("Text");
        xmlTreeBuilder.insert(token);
        
        assertEquals(1, xmlTreeBuilder.currentElement().childNodes().size());
        assertTrue(xmlTreeBuilder.currentElement().childNodes().first() instanceof TextNode);
        TextNode text = (TextNode) xmlTreeBuilder.currentElement().childNodes().first();
        assertEquals("Text", text.text());
    }
    
    @Test
    public void testInsertComment() {
        Token.Comment commentToken = new Token.Comment();
        commentToken.setData("Comment");
        xmlTreeBuilder.insert(commentToken);
        
        assertEquals(1, xmlTreeBuilder.currentElement().childNodes().size());
        assertTrue(xmlTreeBuilder.currentElement().childNodes().first() instanceof Comment);
        Comment comment = (Comment) xmlTreeBuilder.currentElement().childNodes().first();
        assertEquals("Comment", comment.getData());
    }
    
    @Test
    public void testInsertCDATA() {
        Token.Character token = new Token.Character();
        token.setData("CDATA");
        token.setCData();
        
        xmlTreeBuilder.insert(token);
        
        assertEquals(1, xmlTreeBuilder.currentElement().childNodes().size());
        assertTrue(xmlTreeBuilder.currentElement().childNodes().first() instanceof CDataNode);
        CDataNode cdata = (CDataNode) xmlTreeBuilder.currentElement().childNodes().first();
        assertEquals("CDATA", cdata.getWholeData());
    }
    
    @Test
    public void testInsertDoctype() {
        Token.Doctype doctype = new Token.Doctype();
        doctype.setName("html");
        
        xmlTreeBuilder.insert(doctype);
        
        assertEquals(1, xmlTreeBuilder.currentElement().childNodes().size());
        assertTrue(xmlTreeBuilder.currentElement().childNodes().first() instanceof DocumentType);
        DocumentType doctypeNode = (DocumentType) xmlTreeBuilder.currentElement().childNodes().first();
        assertEquals("html", doctypeNode.attr("name"));
    }
    
    @Test
    public void testPopStackToClose() {
        Token.EndTag endTag = new Token.EndTag();
        endTag.tagName = "body";
        
        xmlTreeBuilder.insert(Token.StartTag("html"));
        xmlTreeBuilder.insert(Token.StartTag("head"));
        xmlTreeBuilder.insert(Token.StartTag("body"));
        xmlTreeBuilder.popStackToClose(endTag);
        
        assertEquals(2, xmlTreeBuilder.stack().size());
        assertEquals("html", xmlTreeBuilder.stack().get(0).tagName());
        assertEquals("head", xmlTreeBuilder.stack().get(1).tagName());
    }
    
    @Test
    public void testPopStackToCloseNotFound() {
        Token.EndTag endTag = new Token.EndTag();
        endTag.tagName = "body";
        
        xmlTreeBuilder.insert(Token.StartTag("html"));
        xmlTreeBuilder.insert(Token.StartTag("head"));
        xmlTreeBuilder.popStackToClose(endTag);
        
        assertEquals(2, xmlTreeBuilder.stack().size());
        assertEquals("html", xmlTreeBuilder.stack().get(0).tagName());
        assertEquals("head", xmlTreeBuilder.stack().get(1).tagName());
    }
    
    @Test
    public void testParseFragmentWithInputFragmentAndBaseUriAndParser() {
        String inputFragment = "<p>Hello World!</p>";
        String baseUri = "http://example.com";
        Parser parser = new Parser(xmlTreeBuilder);
        
        List<Node> nodes = xmlTreeBuilder.parseFragment(inputFragment, baseUri, parser);
        
        assertEquals(1, nodes.size());
        assertTrue(nodes.get(0) instanceof Element);
        Element p = (Element) nodes.get(0);
        assertEquals("p", p.tagName());
        TextNode text = (TextNode) p.childNodes().first();
        assertEquals("Hello World!", text.text());
    }
    
    @Test
    public void testParseFragmentWithContext() {
        String inputFragment = "<p>Hello World!</p>";
        Element context = new Element("div");
        String baseUri = "http://example.com";
        Parser parser = new Parser(xmlTreeBuilder);
        
        List<Node> nodes = xmlTreeBuilder.parseFragment(inputFragment, context, baseUri, parser);
        
        assertEquals(1, nodes.size());
        assertTrue(nodes.get(0) instanceof Element);
        Element p = (Element) nodes.get(0);
        assertEquals("p", p.tagName());
        TextNode text = (TextNode) p.childNodes().first();
        assertEquals("Hello World!", text.text());
    }
}