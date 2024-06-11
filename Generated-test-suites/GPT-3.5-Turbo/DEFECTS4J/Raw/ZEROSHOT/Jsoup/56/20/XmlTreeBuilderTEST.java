import org.jsoup.*;
import org.jsoup.nodes.*;
import org.jsoup.parser.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class XmlTreeBuilderTest {
    
    @Test
    public void testDefaultSettings() {
        XmlTreeBuilder treeBuilder = new XmlTreeBuilder();
        ParseSettings settings = treeBuilder.defaultSettings();
        
        assertNotNull(settings);
    }
    
    @Test
    public void testParse() {
        XmlTreeBuilder treeBuilder = new XmlTreeBuilder();
        String input = "<root><child1>value1</child1><child2>value2</child2></root>";
        String baseUri = "http://example.com";
        Document doc = treeBuilder.parse(input, baseUri);
        
        assertNotNull(doc);
        assertNotNull(doc.childNode(0));
        assertNotNull(doc.childNode(1));
    }
    
    @Test
    public void testInitialiseParse() {
        XmlTreeBuilder treeBuilder = new XmlTreeBuilder();
        String input = "<root><child1>value1</child1><child2>value2</child2></root>";
        String baseUri = "http://example.com";
        ParseErrorList errors = ParseErrorList.noTracking();
        ParseSettings settings = ParseSettings.preserveCase;
        
        treeBuilder.initialiseParse(input, baseUri, errors, settings);
        Document doc = treeBuilder.getDocument();
        
        assertNotNull(doc);
        assertNotNull(doc.outputSettings());
        assertEquals(Document.OutputSettings.Syntax.xml, doc.outputSettings().syntax());
    }
    
    @Test
    public void testProcessStartTag() {
        XmlTreeBuilder treeBuilder = new XmlTreeBuilder();
        Token token = new Token.StartTag("tag", new Attributes());
        
        boolean result = treeBuilder.process(token);
        
        assertTrue(result);
        assertNotNull(treeBuilder.currentElement());
        assertEquals("tag", treeBuilder.currentElement().tagName());
    }
    
    @Test
    public void testProcessEndTag() {
        XmlTreeBuilder treeBuilder = new XmlTreeBuilder();
        treeBuilder.insert(new Token.StartTag("tag", new Attributes()));
        Token token = new Token.EndTag("tag");
        
        boolean result = treeBuilder.process(token);
        
        assertTrue(result);
        assertNull(treeBuilder.currentElement());
    }
    
    @Test
    public void testProcessComment() {
        XmlTreeBuilder treeBuilder = new XmlTreeBuilder();
        Token token = new Token.Comment("comment");
        
        boolean result = treeBuilder.process(token);
        
        assertTrue(result);
        assertNotNull(treeBuilder.currentElement());
        assertEquals("comment", treeBuilder.currentElement().data());
    }
    
    @Test
    public void testProcessCharacter() {
        XmlTreeBuilder treeBuilder = new XmlTreeBuilder();
        Token token = new Token.Character("data");
        
        boolean result = treeBuilder.process(token);
        
        assertTrue(result);
        assertNotNull(treeBuilder.currentElement());
        assertEquals("data", treeBuilder.currentElement().data());
    }
    
    @Test
    public void testProcessDoctype() {
        XmlTreeBuilder treeBuilder = new XmlTreeBuilder();
        Token.Doctype token = new Token.Doctype("type", "publicId", "systemId");
        
        boolean result = treeBuilder.process(token);
        
        assertTrue(result);
        assertNotNull(treeBuilder.currentElement());
        assertEquals("type", treeBuilder.currentElement().tagName());
    }
    
    @Test
    public void testInsertNode() {
        XmlTreeBuilder treeBuilder = new XmlTreeBuilder();
        Element element = new Element("tag");
        treeBuilder.insertNode(element);
        
        assertNotNull(treeBuilder.currentElement());
        assertEquals(element, treeBuilder.currentElement().childNode(0));
    }
    
    @Test
    public void testInsertStartTag() {
        XmlTreeBuilder treeBuilder = new XmlTreeBuilder();
        Token.StartTag startTag = new Token.StartTag("tag", new Attributes());
        
        Element el = treeBuilder.insert(startTag);
        
        assertNotNull(el);
        assertEquals("tag", el.tagName());
    }
    
    @Test
    public void testInsertComment() {
        XmlTreeBuilder treeBuilder = new XmlTreeBuilder();
        Token.Comment commentToken = new Token.Comment("comment");
        
        treeBuilder.insert(commentToken);
        
        assertNotNull(treeBuilder.currentElement());
        assertEquals("comment", treeBuilder.currentElement().data());
    }
    
    @Test
    public void testInsertCharacter() {
        XmlTreeBuilder treeBuilder = new XmlTreeBuilder();
        Token.Character characterToken = new Token.Character("data");
        
        treeBuilder.insert(characterToken);
        
        assertNotNull(treeBuilder.currentElement());
        assertEquals("data", treeBuilder.currentElement().data());
    }
    
    @Test
    public void testInsertDoctype() {
        XmlTreeBuilder treeBuilder = new XmlTreeBuilder();
        Token.Doctype doctypeToken = new Token.Doctype("type", "publicId", "systemId");
        
        treeBuilder.insert(doctypeToken);
        
        assertNotNull(treeBuilder.currentElement());
        assertEquals("type", treeBuilder.currentElement().tagName());
    }
    
    @Test
    public void testPopStackToClose() {
        XmlTreeBuilder treeBuilder = new XmlTreeBuilder();
        treeBuilder.insert(new Token.StartTag("tag1", new Attributes()));
        treeBuilder.insert(new Token.StartTag("tag2", new Attributes()));
        treeBuilder.insert(new Token.StartTag("tag3", new Attributes()));
        Token.EndTag endTag = new Token.EndTag("tag2");
        
        treeBuilder.popStackToClose(endTag);
        
        assertNotNull(treeBuilder.currentElement());
        assertEquals("tag1", treeBuilder.currentElement().tagName());
    }
    
    @Test
    public void testParseFragment() {
        XmlTreeBuilder treeBuilder = new XmlTreeBuilder();
        String inputFragment = "<child1>value1</child1><child2>value2</child2>";
        String baseUri = "http://example.com";
        ParseErrorList errors = ParseErrorList.noTracking();
        ParseSettings settings = ParseSettings.preserveCase;
        
        List<Node> nodes = treeBuilder.parseFragment(inputFragment, baseUri, errors, settings);
        
        assertNotNull(nodes);
        assertEquals(2, nodes.size());
    }
}