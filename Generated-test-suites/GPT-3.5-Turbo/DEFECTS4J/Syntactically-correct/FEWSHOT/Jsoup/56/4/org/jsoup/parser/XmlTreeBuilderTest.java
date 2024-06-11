package org.jsoup.parser;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.jsoup.nodes.Comment;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.DocumentType;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.jsoup.parser.Token.Character;
import org.jsoup.parser.Token.Comment;
import org.jsoup.parser.Token.Doctype;
import org.jsoup.parser.Token.EndTag;
import org.jsoup.parser.Token.StartTag;
import org.junit.Before;
import org.junit.Test;

public class XmlTreeBuilderTest {
    
    private XmlTreeBuilder builder;
    
    @Before
    public void setUp() {
        builder = new XmlTreeBuilder();
    }
    
    @Test
    public void testParse() {
        String input = "<root></root>";
        String baseUri = "https://example.com";
        Document doc = builder.parse(input, baseUri);
        
        assertEquals("root", doc.nodeName());
        assertEquals(baseUri, doc.baseUri());
    }
    
    @Test
    public void testInitialiseParse() {
        String input = "<root></root>";
        String baseUri = "https://example.com";
        ParseErrorList errors = new ParseErrorList();
        ParseSettings settings = new ParseSettings();
        
        builder.initialiseParse(input, baseUri, errors, settings);
        
        assertEquals(input, builder.tokeniser.input());
        assertEquals(baseUri, builder.baseUri());
        assertEquals(errors, builder.errors);
        assertEquals(settings, builder.settings);
        assertEquals(builder.doc, builder.stack.get(0));
        assertEquals(Document.OutputSettings.Syntax.xml, builder.doc.outputSettings().syntax());
    }
    
    @Test
    public void testProcess_StartTag() {
        StartTag startTag = new StartTag("foo");
        Element el = builder.insert(startTag);
        
        assertEquals("foo", el.nodeName());
        assertEquals(builder.baseUri(), el.baseUri());
        assertEquals(builder.settings.normalizeAttributes(startTag.attributes), el.attributes());
        assertEquals(el, builder.currentElement());
        assertEquals(1, builder.stack.size());
    }
    
    @Test
    public void testProcess_EndTag() {
        EndTag endTag = new EndTag("foo");
        Element el = new Element("foo");
        builder.stack.add(el);
        
        builder.process(endTag);
        
        assertEquals(0, builder.stack.size());
    }
    
    @Test
    public void testProcess_Comment() {
        Comment commentToken = new Comment("test comment");
        builder.insert(commentToken);
        
        List<Node> nodes = builder.doc.childNodes();
        assertEquals(1, nodes.size());
        assertEquals(Node.COMMENT_NODE, nodes.get(0).nodeType());
        
        Comment comment = (Comment) nodes.get(0);
        assertEquals("test comment", comment.getData());
        assertEquals(builder.baseUri(), comment.baseUri());
    }
    
    @Test
    public void testProcess_Character() {
        Character characterToken = new Character("test character");
        builder.insert(characterToken);
        
        List<Node> nodes = builder.doc.childNodes();
        assertEquals(1, nodes.size());
        assertEquals(Node.TEXT_NODE, nodes.get(0).nodeType());
        
        TextNode textNode = (TextNode) nodes.get(0);
        assertEquals("test character", textNode.text());
        assertEquals(builder.baseUri(), textNode.baseUri());
    }
    
    @Test
    public void testProcess_Doctype() {
        Doctype doctype = new Doctype("html", "", "", "");
        builder.insert(doctype);
        
        List<Node> nodes = builder.doc.childNodes();
        assertEquals(1, nodes.size());
        assertEquals(Node.DOCUMENT_TYPE_NODE, nodes.get(0).nodeType());
        
        DocumentType doctypeNode = (DocumentType) nodes.get(0);
        assertEquals("html", doctypeNode.name());
        assertEquals("", doctypeNode.publicId());
        assertEquals("", doctypeNode.systemId());
        assertEquals(builder.baseUri(), doctypeNode.baseUri());
    }
    
    @Test
    public void testPopStackToClose() {
        Element el1 = new Element("foo");
        Element el2 = new Element("bar");
        Element el3 = new Element("baz");
        
        builder.stack.add(el1);
        builder.stack.add(el2);
        builder.stack.add(el3);
        
        EndTag endTag = new EndTag("bar");
        builder.popStackToClose(endTag);
        
        assertEquals(1, builder.stack.size());
        assertEquals(el1, builder.stack.get(0));
    }
    
    @Test
    public void testParseFragment() {
        String inputFragment = "<div>test</div>";
        String baseUri = "https://example.com";
        ParseErrorList errors = new ParseErrorList();
        ParseSettings settings = new ParseSettings();
        
        List<Node> nodes = builder.parseFragment(inputFragment, baseUri, errors, settings);
        
        assertEquals(1, nodes.size());
        assertEquals("div", nodes.get(0).nodeName());
        assertEquals(baseUri, nodes.get(0).baseUri());
    }
}