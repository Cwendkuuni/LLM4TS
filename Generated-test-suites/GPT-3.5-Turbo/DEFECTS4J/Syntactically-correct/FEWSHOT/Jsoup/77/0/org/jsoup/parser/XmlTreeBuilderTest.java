import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertNull;
import org.junit.Test;

public class XmlTreeBuilderTest {

    @Test
    public void testDefaultSettings() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        assertEquals(ParseSettings.preserveCase, builder.defaultSettings());
    }
    
    @Test
    public void testParseReaderString() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Document doc = builder.parse(new StringReader("<root>test</root>"), "http://example.com");
        assertEquals("http://example.com", doc.baseUri());
        assertEquals("root", doc.child(0).nodeName());
        assertEquals("test", doc.child(0).text());
    }
    
    @Test
    public void testParseStringString() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Document doc = builder.parse("<root>test</root>", "http://example.com");
        assertEquals("http://example.com", doc.baseUri());
        assertEquals("root", doc.child(0).nodeName());
        assertEquals("test", doc.child(0).text());
    }
    
    @Test
    public void testInitialiseParse() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        builder.initialiseParse(new StringReader("<root>test</root>"), "http://example.com", ParseErrorList.noTracking(), ParseSettings.preserveCase);
        assertEquals(Document.OutputSettings.Syntax.xml, builder.doc.outputSettings().syntax());
    }
    
    @Test
    public void testProcessStartTag() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Tag tag = Tag.valueOf("root", builder.defaultSettings());
        Token.StartTag startTag = new Token.StartTag(tag, "<root>", null);
        Element el = builder.insert(startTag);
        assertEquals(tag, el.tag());
        assertFalse(el.tag().isKnownTag());
        assertEquals("http://example.com", el.baseUri());
    }
    
    @Test
    public void testProcessEndTag() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Tag tag = Tag.valueOf("root", builder.defaultSettings());
        Token.EndTag endTag = new Token.EndTag(tag, "</root>");
        builder.stack.add(new Element(tag, "http://example.com"));
        builder.insert(endTag);
        assertTrue(builder.stack.isEmpty());
    }
    
    @Test
    public void testProcessComment() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        String data = "comment";
        Token.Comment commentToken = new Token.Comment(data);
        builder.insert(commentToken);
        assertEquals(data, builder.currentElement().childNode(0).toString());
    }
    
    @Test
    public void testProcessCharacter() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        String data = "test";
        Token.Character token = new Token.Character(data, false);
        builder.insert(token);
        assertEquals(data, builder.currentElement().childNode(0).toString());
        
        String cData = "cdata";
        Token.Character cDataToken = new Token.Character(cData, true);
        builder.insert(cDataToken);
        assertEquals(cData, builder.currentElement().childNode(1).toString());
    }
    
    @Test
    public void testProcessDoctype() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        String name = "name";
        String publicIdentifier = "public";
        String systemIdentifier = "system";
        Token.Doctype d = new Token.Doctype(name, publicIdentifier, systemIdentifier);
        builder.insert(d);
        DocumentType doctype = (DocumentType) builder.currentElement().childNode(0);
        assertEquals(name, doctype.nodeName());
        assertEquals(publicIdentifier, doctype.publicIdentifier());
        assertEquals(systemIdentifier, doctype.systemIdentifier());
    }
    
    @Test
    public void testPopStackToClose() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Tag tag1 = Tag.valueOf("root1", builder.defaultSettings());
        Tag tag2 = Tag.valueOf("root2", builder.defaultSettings());
        
        builder.stack.add(new Element(tag1, "http://example.com"));
        builder.stack.add(new Element(tag2, "http://example.com"));
        
        Token.EndTag endTag = new Token.EndTag(tag1, "</root1>");
        builder.popStackToClose(endTag);
        
        assertEquals(1, builder.stack.size());
        assertEquals(tag2, builder.stack.get(0).tag());
    }
    
    @Test
    public void testParseFragment() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        List<Node> nodes = builder.parseFragment("<root>test</root>", "http://example.com", ParseErrorList.noTracking(), ParseSettings.preserveCase);
        
        assertEquals("http://example.com", nodes.get(0).baseUri());
        assertEquals("root", nodes.get(0).nodeName());
        assertEquals("test", nodes.get(0).childNode(0).toString());
    }

}