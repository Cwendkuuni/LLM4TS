import org.junit.Test;
import static org.junit.Assert.*;

public class XmlTreeBuilderTest {

    @Test
    public void testDefaultSettings() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        ParseSettings settings = xmlTreeBuilder.defaultSettings();
        
        assertEquals(ParseSettings.preserveCase, settings);
    }
    
    @Test
    public void testParseWithReaderAndBaseUri() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Document document = xmlTreeBuilder.parse(new StringReader("<root></root>"), "http://example.com");
        
        assertNotNull(document);
        assertEquals("http://example.com", document.location());
    }
    
    @Test
    public void testParseWithStringAndBaseUri() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Document document = xmlTreeBuilder.parse("<root></root>", "http://example.com");
        
        assertNotNull(document);
        assertEquals("http://example.com", document.location());
    }
    
    @Test
    public void testInitialiseParse() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Reader reader = new StringReader("<root></root>");
        ParseErrorList errors = ParseErrorList.noTracking();
        ParseSettings settings = ParseSettings.preserveCase;
        
        xmlTreeBuilder.initialiseParse(reader, "http://example.com", errors, settings);
        
        assertNotNull(xmlTreeBuilder.currentElement());
        assertEquals(Document.OutputSettings.Syntax.xml, xmlTreeBuilder.doc.outputSettings().syntax());
    }
    
    @Test
    public void testProcessStartTag() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Token.StartTag startTag = new Token.StartTag("element", "attr1=value1");
        
        Element element = xmlTreeBuilder.insert(startTag);
        
        assertNotNull(element);
        assertEquals("element", element.tagName());
        assertTrue(element.hasAttr("attr1"));
        assertEquals("value1", element.attr("attr1"));
    }
    
    @Test
    public void testProcessSelfClosingStartTag() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Token.StartTag selfClosingTag = new Token.StartTag("element", true, "attr1=value1");
        
        Element element = xmlTreeBuilder.insert(selfClosingTag);
        
        assertNotNull(element);
        assertEquals("element", element.tagName());
        assertTrue(element.hasAttr("attr1"));
        assertEquals("value1", element.attr("attr1"));
        assertTrue(element.tag().isSelfClosing());
    }
    
    @Test
    public void testProcessComment() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Token.Comment comment = new Token.Comment("This is a comment");
        
        xmlTreeBuilder.insert(comment);
        
        Node node = xmlTreeBuilder.currentElement().childNode(0);
        assertNotNull(node);
        assertTrue(node instanceof Comment);
        assertEquals("This is a comment", ((Comment) node).getData());
    }
    
    @Test
    public void testProcessCharacter() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Token.Character character = new Token.Character("This is some character data");
        
        xmlTreeBuilder.insert(character);
        
        Node node = xmlTreeBuilder.currentElement().childNode(0);
        assertNotNull(node);
        assertTrue(node instanceof TextNode);
        assertEquals("This is some character data", ((TextNode) node).getWholeText());
    }
    
    @Test
    public void testProcessDoctype() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Token.Doctype doctype = new Token.Doctype("html", "publicId", "systemId");
        
        xmlTreeBuilder.insert(doctype);
        
        Node node = xmlTreeBuilder.currentElement().childNode(0);
        assertNotNull(node);
        assertTrue(node instanceof DocumentType);
        assertEquals("html", ((DocumentType) node).name());
        assertEquals("publicId", ((DocumentType) node).publicId());
        assertEquals("systemId", ((DocumentType) node).systemId());
    }
    
    @Test
    public void testPopStackToClose() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Token.EndTag endTag1 = new Token.EndTag("element");
        Token.EndTag endTag2 = new Token.EndTag("element");
        Token.EndTag endTag3 = new Token.EndTag("element");
        
        // Insert elements
        xmlTreeBuilder.insert(new Token.StartTag("element"));
        xmlTreeBuilder.insert(new Token.StartTag("element"));
        xmlTreeBuilder.insert(new Token.StartTag("element"));
        
        // Pop elements
        xmlTreeBuilder.popStackToClose(endTag1);
        
        assertEquals(2, xmlTreeBuilder.stack.size());
        
        xmlTreeBuilder.popStackToClose(endTag2);
        
        assertEquals(1, xmlTreeBuilder.stack.size());
        
        xmlTreeBuilder.popStackToClose(endTag3);
        
        assertEquals(0, xmlTreeBuilder.stack.size());
    }
    
    @Test
    public void testParseFragment() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        String inputFragment = "<root><element1/><element2/></root>";
        ParseErrorList errors = ParseErrorList.noTracking();
        ParseSettings settings = ParseSettings.preserveCase;
        
        List<Node> nodes = xmlTreeBuilder.parseFragment(inputFragment, "http://example.com", errors, settings);
        
        assertNotNull(nodes);
        assertEquals(1, nodes.size());
        assertTrue(nodes.get(0) instanceof Element);
        assertEquals("root", ((Element) nodes.get(0)).tagName());
        assertEquals(2, ((Element) nodes.get(0)).childNodeSize());
    }
    
}