import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

import org.jsoup.parser.XmlTreeBuilder;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;

import java.io.Reader;
import java.io.StringReader;
import java.util.List;

public class XmlTreeBuilderTest {
    private XmlTreeBuilder builder;
    
    @Before
    public void setUp() {
        builder = new XmlTreeBuilder();
    }
    
    @Test
    public void testDefaultSettings() {
        ParseSettings settings = builder.defaultSettings();
        
        assertNotNull(settings);
    }
    
    @Test
    public void testInitialiseParse() {
        Reader input = new StringReader("<root></root>");
        String baseUri = "https://example.com";
        Parser parser = new Parser();
        
        builder.initialiseParse(input, baseUri, parser);
        
        assertNotNull(builder.stack);
        assertNotNull(builder.doc);
        assertEquals(Document.OutputSettings.Syntax.xml, builder.doc.outputSettings().syntax());
    }
    
    @Test
    public void testParseReaderString() {
        Reader input = new StringReader("<root></root>");
        String baseUri = "https://example.com";
        
        Document doc = builder.parse(input, baseUri);
        
        assertNotNull(doc);
    }
    
    @Test
    public void testParseStringString() {
        String input = "<root></root>";
        String baseUri = "https://example.com";
        
        Document doc = builder.parse(input, baseUri);
        
        assertNotNull(doc);
    }
    
    @Test
    public void testProcessStartTag() {
        Token.StartTag startTag = new Token.StartTag("div");
        
        Element element = builder.insert(startTag);
        
        assertNotNull(element);
        assertEquals("div", element.tagName());
    }
    
    @Test
    public void testProcessEndTag() {
        Token.EndTag endTag = new Token.EndTag("div");
        
        builder.stack.add(new Element("div"));
        builder.stack.add(new Element("span"));
        
        builder.popStackToClose(endTag);
        
        assertEquals(1, builder.stack.size());
        assertEquals("span", builder.stack.get(0).tagName());
    }
    
    @Test
    public void testProcessComment() {
        Token.Comment commentToken = new Token.Comment("This is a comment");
        
        builder.insert(commentToken);
        
        assertEquals(1, builder.currentElement().childNodes().size());
        assertEquals(Node.COMMENT_NODE, builder.currentElement().childNodes().get(0).nodeType());
    }
    
    @Test
    public void testProcessCharacter() {
        Token.Character token = new Token.Character("This is a character");
        
        builder.insert(token);
        
        assertEquals(1, builder.currentElement().childNodes().size());
        assertEquals(Node.TEXT_NODE, builder.currentElement().childNodes().get(0).nodeType());
    }
    
    @Test
    public void testProcessDoctype() {
        Token.Doctype d = new Token.Doctype("html", "", "");
        
        builder.insert(d);
        
        assertEquals(1, builder.currentElement().childNodes().size());
        assertEquals(Node.DOCUMENT_TYPE_NODE, builder.currentElement().childNodes().get(0).nodeType());
    }
    
    @Test
    public void testParseFragmentStringString() {
        String inputFragment = "<div>This is a fragment</div>";
        String baseUri = "https://example.com";
        
        List<Node> nodes = builder.parseFragment(inputFragment, baseUri, new Parser());
        
        assertNotNull(nodes);
        assertEquals(1, nodes.size());
        assertEquals(Node.ELEMENT_NODE, nodes.get(0).nodeType());
        assertEquals("div", ((Element)nodes.get(0)).tagName());
        assertEquals("This is a fragment", ((TextNode)((Element)nodes.get(0)).childNodes().get(0)).text());
    }
    
    @Test
    public void testParseFragmentStringElementString() {
        String inputFragment = "<div>This is a fragment</div>";
        Element context = new Element("span");
        String baseUri = "https://example.com";
        
        List<Node> nodes = builder.parseFragment(inputFragment, context, baseUri, new Parser());
        
        assertNotNull(nodes);
        assertEquals(1, nodes.size());
        assertEquals(Node.ELEMENT_NODE, nodes.get(0).nodeType());
        assertEquals("div", ((Element)nodes.get(0)).tagName());
        assertEquals("This is a fragment", ((TextNode)((Element)nodes.get(0)).childNodes().get(0)).text());
    }
}