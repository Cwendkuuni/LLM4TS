import org.jsoup.Jsoup;
import org.jsoup.helper.Validate;
import org.jsoup.nodes.*;
import org.junit.Test;

import java.io.StringReader;
import java.util.List;

import static org.junit.Assert.*;

public class XmlTreeBuilderTest {

    @Test
    public void testDefaultSettings() {
        XmlTreeBuilder treeBuilder = new XmlTreeBuilder();
        ParseSettings settings = treeBuilder.defaultSettings();
        
        assertNotNull(settings);
        assertEquals(ParseSettings.preserveCase, settings);
    }
    
    @Test
    public void testParseWithReaderAndBaseUri() {
        XmlTreeBuilder treeBuilder = new XmlTreeBuilder();
        Document doc = treeBuilder.parse(new StringReader(""), "http://www.example.com");
        
        assertNotNull(doc);
        assertEquals("http://www.example.com", doc.baseUri());
    }

    @Test
    public void testParseWithStringAndBaseUri() {
        XmlTreeBuilder treeBuilder = new XmlTreeBuilder();
        Document doc = treeBuilder.parse("", "http://www.example.com");

        assertNotNull(doc);
        assertEquals("http://www.example.com", doc.baseUri());
    }
    
    @Test
    public void testInitialiseParse() {
        XmlTreeBuilder treeBuilder = new XmlTreeBuilder();
        Document doc = new Document("");
        treeBuilder.initialiseParse(new StringReader(""), "http://www.example.com", ParseErrorList.noTracking(), ParseSettings.preserveCase);
        
        assertNotNull(treeBuilder.stack);
        assertEquals(doc, treeBuilder.stack.get(0));
        assertEquals(Document.OutputSettings.Syntax.xml, doc.outputSettings().syntax());
    }
    
    @Test
    public void testProcessStartTag() {
        XmlTreeBuilder treeBuilder = new XmlTreeBuilder();
        Element el = treeBuilder.insert(new Token.StartTag("div"));

        assertNotNull(el);
        assertEquals("div", el.nodeName());
    }

    @Test
    public void testProcessEndTag() {
        XmlTreeBuilder treeBuilder = new XmlTreeBuilder();
        treeBuilder.insert(new Token.StartTag("div"));
        treeBuilder.insert(new Token.EndTag("div"));

        assertEquals(0, treeBuilder.stack.size());
    }
    
    @Test
    public void testProcessComment() {
        XmlTreeBuilder treeBuilder = new XmlTreeBuilder();
        treeBuilder.insert(new Token.Comment("Sample comment"));

        assertEquals(1, treeBuilder.currentElement().childNodes().size());
        assertTrue(treeBuilder.currentElement().childNodes().get(0) instanceof Comment);
        assertEquals("Sample comment", ((Comment) treeBuilder.currentElement().childNodes().get(0)).getData());
    }

    @Test
    public void testProcessCharacter() {
        XmlTreeBuilder treeBuilder = new XmlTreeBuilder();
        treeBuilder.insert(new Token.Character("Sample character"));

        assertEquals(1, treeBuilder.currentElement().childNodes().size());
        assertTrue(treeBuilder.currentElement().childNodes().get(0) instanceof TextNode);
        assertEquals("Sample character", ((TextNode) treeBuilder.currentElement().childNodes().get(0)).text());
    }

    @Test
    public void testProcessDoctype() {
        XmlTreeBuilder treeBuilder = new XmlTreeBuilder();
        treeBuilder.insert(new Token.Doctype("html", "pubId", "sysId"));

        assertEquals(1, treeBuilder.currentElement().childNodes().size());
        assertTrue(treeBuilder.currentElement().childNodes().get(0) instanceof DocumentType);
        DocumentType doctype = (DocumentType) treeBuilder.currentElement().childNodes().get(0);
        assertEquals("html", doctype.nodeName());
        assertEquals("pubId", doctype.attr("publicid"));
        assertEquals("sysId", doctype.attr("systemid"));
    }

    @Test
    public void testInsertNode() {
        XmlTreeBuilder treeBuilder = new XmlTreeBuilder();
        Element el = new Element(Tag.valueOf("div"), "http://www.example.com", ParseSettings.preserveCase);
        treeBuilder.insertNode(el);

        assertEquals(1, treeBuilder.currentElement().childNodes().size());
        assertSame(el, treeBuilder.currentElement().childNodes().get(0));
    }

    @Test
    public void testParseFragment() {
        XmlTreeBuilder treeBuilder = new XmlTreeBuilder();
        List<Node> nodes = treeBuilder.parseFragment("<p>Test</p>", "http://www.example.com", ParseErrorList.noTracking(), ParseSettings.preserveCase);

        assertEquals(1, nodes.size());
        assertTrue(nodes.get(0) instanceof Element);
        Element el = (Element) nodes.get(0);
        assertEquals("p", el.nodeName());
        assertEquals("Test", el.text());
    }
}