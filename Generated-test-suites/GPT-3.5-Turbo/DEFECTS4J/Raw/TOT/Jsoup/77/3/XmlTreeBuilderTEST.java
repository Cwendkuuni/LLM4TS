import org.junit.Test;
import static org.junit.Assert.*;

public class XmlTreeBuilderTest {
    
    @Test
    public void testDefaultSettings() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        ParseSettings settings = xmlTreeBuilder.defaultSettings();
        
        assertNotNull(settings);
        // Assert default settings are correct
        assertEquals(ParseSettings.preserveCase, settings);
    }
    
    @Test
    public void testParseReader() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        String xml = "<root><element></element></root>";
        StringReader reader = new StringReader(xml);
        Document document = xmlTreeBuilder.parse(reader, "http://example.com");
        
        assertNotNull(document);
        // Assert document structure
        assertEquals("root", document.child(0).nodeName());
        assertEquals("element", document.child(0).child(0).nodeName());
    }
    
    @Test
    public void testParseString() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        String xml = "<root><element></element></root>";
        Document document = xmlTreeBuilder.parse(xml, "http://example.com");
        
        assertNotNull(document);
        // Assert document structure
        assertEquals("root", document.child(0).nodeName());
        assertEquals("element", document.child(0).child(0).nodeName());
    }
    
    @Test
    public void testInitialiseParse() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        String xml = "<root><element></element></root>";
        StringReader reader = new StringReader(xml);
        ParseErrorList errors = new ParseErrorList();
        
        xmlTreeBuilder.initialiseParse(reader, "http://example.com", errors, ParseSettings.preserveCase);
        
        // Assert document stack is initialized correctly
        assertNotNull(xmlTreeBuilder.stack);
        assertEquals(xmlTreeBuilder.doc, xmlTreeBuilder.stack.get(0));
        
        // Assert output settings are updated
        assertTrue(xmlTreeBuilder.doc.outputSettings().syntax() == Document.OutputSettings.Syntax.xml);
    }
    
    @Test
    public void testProcessStartTag() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        String xml = "<root><element></element></root>";
        StringReader reader = new StringReader(xml);
        ParseErrorList errors = new ParseErrorList();
        
        xmlTreeBuilder.initialiseParse(reader, "http://example.com", errors, ParseSettings.preserveCase);
        xmlTreeBuilder.runParser();
        
        // Assert start tag is processed correctly
        assertNotNull(xmlTreeBuilder.stack);
        assertEquals("root", xmlTreeBuilder.stack.get(1).nodeName());
        assertEquals("element", xmlTreeBuilder.stack.get(1).child(0).nodeName());
    }
    
    @Test
    public void testProcessEndTag() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        String xml = "<root><element></element></root>";
        StringReader reader = new StringReader(xml);
        ParseErrorList errors = new ParseErrorList();
        
        xmlTreeBuilder.initialiseParse(reader, "http://example.com", errors, ParseSettings.preserveCase);
        xmlTreeBuilder.runParser();
        
        // Assert end tag is processed correctly
        assertNotNull(xmlTreeBuilder.stack);
        xmlTreeBuilder.process(new Token.EndTag("element"));
        assertEquals("root", xmlTreeBuilder.stack.get(1).nodeName());
    }
    
    @Test
    public void testProcessComment() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        String xml = "<root><!-- Comment --></root>";
        StringReader reader = new StringReader(xml);
        ParseErrorList errors = new ParseErrorList();
        
        xmlTreeBuilder.initialiseParse(reader, "http://example.com", errors, ParseSettings.preserveCase);
        xmlTreeBuilder.runParser();
        
        // Assert comment is processed correctly
        assertNotNull(xmlTreeBuilder.stack);
        assertEquals("root", xmlTreeBuilder.stack.get(1).nodeName());
        assertEquals(" Comment ", xmlTreeBuilder.stack.get(1).childNode(0).toString());
    }
    
    @Test
    public void testProcessCharacter() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        String xml = "<root>Text Node</root>";
        StringReader reader = new StringReader(xml);
        ParseErrorList errors = new ParseErrorList();
        
        xmlTreeBuilder.initialiseParse(reader, "http://example.com", errors, ParseSettings.preserveCase);
        xmlTreeBuilder.runParser();
        
        // Assert character token is processed correctly
        assertNotNull(xmlTreeBuilder.stack);
        assertEquals("root", xmlTreeBuilder.stack.get(1).nodeName());
        assertEquals("Text Node", xmlTreeBuilder.stack.get(1).childNode(0).toString());
    }
    
    @Test
    public void testProcessDoctype() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        String xml = "<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\"><root></root>";
        StringReader reader = new StringReader(xml);
        ParseErrorList errors = new ParseErrorList();
        
        xmlTreeBuilder.initialiseParse(reader, "http://example.com", errors, ParseSettings.preserveCase);
        xmlTreeBuilder.runParser();
        
        // Assert doctype is processed correctly
        assertNotNull(xmlTreeBuilder.stack);
        assertEquals("root", xmlTreeBuilder.stack.get(1).nodeName());
        assertNotNull(xmlTreeBuilder.doc.childNode(0));
        assertTrue(xmlTreeBuilder.doc.childNode(0).getClass() == DocumentType.class);
    }
    
    @Test
    public void testInsertNode() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        String xml = "<root></root>";
        StringReader reader = new StringReader(xml);
        ParseErrorList errors = new ParseErrorList();
        
        xmlTreeBuilder.initialiseParse(reader, "http://example.com", errors, ParseSettings.preserveCase);
        xmlTreeBuilder.runParser();
        
        // Assert node insertion is correct
        assertNotNull(xmlTreeBuilder.stack);
        Element element = xmlTreeBuilder.currentElement();
        Element child = new Element(Tag.valueOf("child"), "http://example.com", xmlTreeBuilder.defaultSettings().normalizeAttributes(new Attributes()));
        xmlTreeBuilder.insertNode(child);
        assertEquals(child, element.child(0));
    }
    
     @Test
    public void testInsertStartTag() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        String xml = "<root></root>";
        StringReader reader = new StringReader(xml);
        ParseErrorList errors = new ParseErrorList();
        
        xmlTreeBuilder.initialiseParse(reader, "http://example.com", errors, ParseSettings.preserveCase);
        xmlTreeBuilder.runParser();
        
        // Assert start tag insertion is correct
        assertNotNull(xmlTreeBuilder.stack);
        Element element = xmlTreeBuilder.currentElement();
        Token.StartTag startTag = new Token.StartTag("child", new Attributes());
        Element inserted = xmlTreeBuilder.insert(startTag);
        assertEquals("child", inserted.nodeName());
        assertEquals(inserted, element.child(0));
        assertTrue(inserted.tag().isKnownTag());
    }
    
    @Test
    public void testInsertComment() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        String xml = "<root><!-- Comment --></root>";
        StringReader reader = new StringReader(xml);
        ParseErrorList errors = new ParseErrorList();
        
        xmlTreeBuilder.initialiseParse(reader, "http://example.com", errors, ParseSettings.preserveCase);
        xmlTreeBuilder.runParser();
        
        // Assert comment insertion is correct
        assertNotNull(xmlTreeBuilder.stack);
        Element element = xmlTreeBuilder.currentElement();
        Token.Comment commentToken = new Token.Comment(" Comment ");
        xmlTreeBuilder.insert(commentToken);
        assertEquals(commentToken.getData(), element.childNode(0).toString());
    }
    
    @Test
    public void testInsertCharacterTextNode() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        String xml = "<root>Text Node</root>";
        StringReader reader = new StringReader(xml);
        ParseErrorList errors = new ParseErrorList();
        
        xmlTreeBuilder.initialiseParse(reader, "http://example.com", errors, ParseSettings.preserveCase);
        xmlTreeBuilder.runParser();
        
        // Assert character text node insertion is correct
        assertNotNull(xmlTreeBuilder.stack);
        Element element = xmlTreeBuilder.currentElement();
        Token.Character characterToken = new Token.Character("Text Node");
        xmlTreeBuilder.insert(characterToken);
        assertEquals(characterToken.getData(), element.childNode(0).toString());
    }
    
    @Test
    public void testInsertCDataNode() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        String xml = "<root><![CDATA[ CData Text ]]></root>";
        StringReader reader = new StringReader(xml);
        ParseErrorList errors = new ParseErrorList();
        
        xmlTreeBuilder.initialiseParse(reader, "http://example.com", errors, ParseSettings.preserveCase);
        xmlTreeBuilder.runParser();
        
        // Assert CDATA node insertion is correct
        assertNotNull(xmlTreeBuilder.stack);
        Element element = xmlTreeBuilder.currentElement();
        Token.Character characterToken = new Token.Character(" CData Text ");
        xmlTreeBuilder.insert(characterToken);
        assertTrue(xmlTreeBuilder.currentElement().childNode(0).getClass() == CDataNode.class);
        assertEquals(characterToken.getData(), xmlTreeBuilder.currentElement().childNode(0).toString());
    }
    
    @Test
    public void testInsertDoctype() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        String xml = "<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\"><root></root>";
        StringReader reader = new StringReader(xml);
        ParseErrorList errors = new ParseErrorList();
        
        xmlTreeBuilder.initialiseParse(reader, "http://example.com", errors, ParseSettings.preserveCase);
        xmlTreeBuilder.runParser();
        
        // Assert doctype node insertion is correct
        assertNotNull(xmlTreeBuilder.stack);
        Element element = xmlTreeBuilder.currentElement();
        Token.Doctype doctypeToken = new Token.Doctype("html", "-//W3C//DTD XHTML 1.0 Transitional//EN", "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd");
        xmlTreeBuilder.insert(doctypeToken);
        assertTrue(xmlTreeBuilder.currentElement().childNode(0).getClass() == DocumentType.class);
        assertEquals(doctypeToken.getName(), xmlTreeBuilder.currentElement().childNode(0).nodeName());
    }
    
    @Test
    public void testPopStackToClose() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        String xml = "<root><element></element></root>";
        StringReader reader = new StringReader(xml);
        ParseErrorList errors = new ParseErrorList();
        
        xmlTreeBuilder.initialiseParse(reader, "http://example.com", errors, ParseSettings.preserveCase);
        xmlTreeBuilder.runParser();
        xmlTreeBuilder.process(new Token.EndTag("element"));
        
        // Assert stack elements are correctly removed
        assertNotNull(xmlTreeBuilder.stack);
        assertEquals(1, xmlTreeBuilder.stack.size());
    }
    
    @Test
    public void testParseFragment() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        String xml = "<element></element>";
        StringReader reader = new StringReader(xml);
        ParseErrorList errors = new ParseErrorList();
        
        xmlTreeBuilder.initialiseParse(reader, "http://example.com", errors, ParseSettings.preserveCase);
        List<Node> nodes = xmlTreeBuilder.parseFragment(xml, "http://example.com", errors, ParseSettings.preserveCase);
        
        // Assert fragment parsing results in correct nodes
        assertNotNull(nodes);
        assertEquals(1, nodes.size());
        assertEquals("element", nodes.get(0).nodeName());
    }
}