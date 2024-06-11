import org.jsoup.nodes.*;
import org.jsoup.parser.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class XmlTreeBuilderTest {

    @Test
    public void testInitialiseParse() {
        String input = "<xml><tag1>data</tag1></xml>";
        String baseUri = "http://example.com";
        ParseErrorList errors = new ParseErrorList();
        
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        xmlTreeBuilder.initialiseParse(input, baseUri, errors);
        
        Document doc = xmlTreeBuilder.getDocument();
        assertNotNull(doc);
        assertEquals("http://example.com", doc.baseUri());
        assertEquals(Document.OutputSettings.Syntax.xml, doc.outputSettings().syntax());
        
        List<Node> childNodes = doc.childNodes();
        assertEquals(1, childNodes.size());
        assertTrue(childNodes.get(0) instanceof Element);
        Element element1 = (Element) childNodes.get(0);
        assertEquals("xml", element1.nodeName());
        
        List<Node> element1ChildNodes = element1.childNodes();
        assertEquals(1, element1ChildNodes.size());
        assertTrue(element1ChildNodes.get(0) instanceof TextNode);
        TextNode textNode = (TextNode) element1ChildNodes.get(0);
        assertEquals("data", textNode.getWholeText());
    }
    
    @Test
    public void testProcess_StartTag() {
        Token.StartTag startTag = new Token.StartTag("tag2");
        startTag.attributes.put("attr1", "value1");
        
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        xmlTreeBuilder.initialiseParse("<xml></xml>", "http://example.com", new ParseErrorList());
        
        Element element = xmlTreeBuilder.insert(startTag);
        
        assertNotNull(element);
        assertEquals("tag2", element.nodeName());
        assertEquals("http://example.com", element.baseUri());
        
        List<Attribute> attributes = element.attributes().asList();
        assertEquals(1, attributes.size());
        assertEquals("attr1", attributes.get(0).getKey());
        assertEquals("value1", attributes.get(0).getValue());
        
        List<Node> childNodes = xmlTreeBuilder.getDocument().childNodes();
        assertEquals(2, childNodes.size());
        assertTrue(childNodes.get(1) instanceof Element);
        Element insertedElement = (Element) childNodes.get(1);
        assertEquals("tag2", insertedElement.nodeName());
    }
    
    @Test
    public void testProcess_StartTag_SelfClosing() {
        Token.StartTag startTag = new Token.StartTag("tag3");
        startTag.selfClosing = true;
        
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        xmlTreeBuilder.initialiseParse("<xml></xml>", "http://example.com", new ParseErrorList());
        
        Element element = xmlTreeBuilder.insert(startTag);
        
        assertNotNull(element);
        assertEquals("tag3", element.nodeName());
        assertTrue(element.isSelfClosing());
        
        List<Attribute> attributes = element.attributes().asList();
        assertEquals(0, attributes.size());
        
        List<Node> childNodes = xmlTreeBuilder.getDocument().childNodes();
        assertEquals(2, childNodes.size());
        assertTrue(childNodes.get(1) instanceof Element);
        Element insertedElement = (Element) childNodes.get(1);
        assertEquals("tag3", insertedElement.nodeName());
        assertTrue(insertedElement.isSelfClosing());
    }
    
    @Test
    public void testProcess_StartTag_UnknownTag() {
        Token.StartTag startTag = new Token.StartTag("unknown");
        
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        xmlTreeBuilder.initialiseParse("<xml></xml>", "http://example.com", new ParseErrorList());
        
        Element element = xmlTreeBuilder.insert(startTag);
        
        assertNotNull(element);
        assertEquals("unknown", element.nodeName());
        
        List<Node> childNodes = xmlTreeBuilder.getDocument().childNodes();
        assertEquals(2, childNodes.size());
        assertTrue(childNodes.get(1) instanceof Element);
        Element insertedElement = (Element) childNodes.get(1);
        assertEquals("unknown", insertedElement.nodeName());
        
        assertTrue(insertedElement.tag().isKnownTag());
    }
    
    @Test
    public void testProcess_EndTag() {
        Token.EndTag endTag = new Token.EndTag("tag1");
        
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        xmlTreeBuilder.initialiseParse("<xml><tag1>data</tag1></xml>", "http://example.com", new ParseErrorList());
        
        xmlTreeBuilder.insert(new Token.StartTag("tag1"));
        Element element = xmlTreeBuilder.insert(endTag);
        
        List<Node> childNodes = element.childNodes();
        assertEquals(1, childNodes.size());
        assertTrue(childNodes.get(0) instanceof TextNode);
        TextNode textNode = (TextNode) childNodes.get(0);
        assertEquals("data", textNode.getWholeText());
        
        childNodes = xmlTreeBuilder.getDocument().childNodes();
        assertEquals(1, childNodes.size());
        assertTrue(childNodes.get(0) instanceof Element);
        Element insertedElement = (Element) childNodes.get(0);
        assertEquals("xml", insertedElement.nodeName());
    }
    
    @Test
    public void testProcess_Comment() {
        Token.Comment commentToken = new Token.Comment("comment");
        
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        xmlTreeBuilder.initialiseParse("<xml></xml>", "http://example.com", new ParseErrorList());
        
        xmlTreeBuilder.insert(commentToken);
        
        List<Node> childNodes = xmlTreeBuilder.getDocument().childNodes();
        assertEquals(2, childNodes.size());
        assertTrue(childNodes.get(1) instanceof Comment);
        Comment insertedComment = (Comment) childNodes.get(1);
        assertEquals("comment", insertedComment.getData());
    }
    
    @Test
    public void testProcess_Comment_WithDeclaration() {
        Token.Comment commentToken = new Token.Comment("<!--?xml version=\"1.0\" encoding=\"UTF-8\"?-->");
        
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        xmlTreeBuilder.initialiseParse("<xml></xml>", "http://example.com", new ParseErrorList());
        
        xmlTreeBuilder.insert(commentToken);
        
        List<Node> childNodes = xmlTreeBuilder.getDocument().childNodes();
        assertEquals(2, childNodes.size());
        assertTrue(childNodes.get(1) instanceof XmlDeclaration);
        XmlDeclaration insertedDeclaration = (XmlDeclaration) childNodes.get(1);
        assertEquals("?xml version=\"1.0\" encoding=\"UTF-8\"?", insertedDeclaration.getDeclaration());
        assertEquals("http://example.com", insertedDeclaration.baseUri());
        assertEquals("<!?", insertedDeclaration.getData());
    }
    
    @Test
    public void testProcess_Character() {
        Token.Character characterToken = new Token.Character("data");
        
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        xmlTreeBuilder.initialiseParse("<xml></xml>", "http://example.com", new ParseErrorList());
        
        xmlTreeBuilder.insert(characterToken);
        
        List<Node> childNodes = xmlTreeBuilder.getDocument().childNodes();
        assertEquals(2, childNodes.size());
        assertTrue(childNodes.get(1) instanceof TextNode);
        TextNode insertedTextNode = (TextNode) childNodes.get(1);
        assertEquals("data", insertedTextNode.getWholeText());
    }
    
    @Test
    public void testProcess_Doctype() {
        Token.Doctype doctype = new Token.Doctype("html", "", "", "about:legacy-compat");
        
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        xmlTreeBuilder.initialiseParse("<xml></xml>", "http://example.com", new ParseErrorList());
        
        xmlTreeBuilder.insert(doctype);
        
        List<Node> childNodes = xmlTreeBuilder.getDocument().childNodes();
        assertEquals(2, childNodes.size());
        assertTrue(childNodes.get(1) instanceof DocumentType);
        DocumentType insertedDoctype = (DocumentType) childNodes.get(1);
        assertEquals("html", insertedDoctype.getName());
        assertEquals("", insertedDoctype.getPublicIdentifier());
        assertEquals("", insertedDoctype.getSystemIdentifier());
        assertEquals("about:legacy-compat", insertedDoctype.baseUri());
    }
    
    @Test
    public void testPopStackToClose_TagFound() {
        Token.EndTag endTag = new Token.EndTag("tag2");
        
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        xmlTreeBuilder.stack.add(new Element(Tag.valueOf("tag1"), "", new Attributes()));
        xmlTreeBuilder.stack.add(new Element(Tag.valueOf("tag2"), "", new Attributes()));
        xmlTreeBuilder.stack.add(new Element(Tag.valueOf("tag3"), "", new Attributes()));
        
        xmlTreeBuilder.popStackToClose(endTag);
        
        assertEquals(2, xmlTreeBuilder.stack.size());
        assertEquals("tag1", xmlTreeBuilder.stack.get(0).nodeName());
        assertEquals("tag3", xmlTreeBuilder.stack.get(1).nodeName());
    }
    
    @Test
    public void testPopStackToClose_TagNotFound() {
        Token.EndTag endTag = new Token.EndTag("tag4");
        
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        xmlTreeBuilder.stack.add(new Element(Tag.valueOf("tag1"), "", new Attributes()));
        xmlTreeBuilder.stack.add(new Element(Tag.valueOf("tag2"), "", new Attributes()));
        xmlTreeBuilder.stack.add(new Element(Tag.valueOf("tag3"), "", new Attributes()));
        
        xmlTreeBuilder.popStackToClose(endTag);
        
        assertEquals(3, xmlTreeBuilder.stack.size());
        assertEquals("tag1", xmlTreeBuilder.stack.get(0).nodeName());
        assertEquals("tag2", xmlTreeBuilder.stack.get(1).nodeName());
        assertEquals("tag3", xmlTreeBuilder.stack.get(2).nodeName());
    }
    
    @Test
    public void testParseFragment() {
        String inputFragment = "<tag1></tag1><tag2/><tag3>data</tag3>";
        String baseUri = "http://example.com";
        ParseErrorList errors = new ParseErrorList();
        
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        List<Node> childNodes = xmlTreeBuilder.parseFragment(inputFragment, baseUri, errors);
        
        assertEquals(3, childNodes.size());
        assertTrue(childNodes.get(0) instanceof Element);
        assertTrue(childNodes.get(1) instanceof Element);
        assertTrue(childNodes.get(2) instanceof Element);
        
        Element element1 = (Element) childNodes.get(0);
        assertEquals("tag1", element1.nodeName());
        
        Element element2 = (Element) childNodes.get(1);
        assertEquals("tag2", element2.nodeName());
        assertTrue(element2.isSelfClosing());
        
        Element element3 = (Element) childNodes.get(2);
        assertEquals("tag3", element3.nodeName());
        assertFalse(element3.isSelfClosing());
        
        List<Node> element3ChildNodes = element3.childNodes();
        assertEquals(1, element3ChildNodes.size());
        assertTrue(element3ChildNodes.get(0) instanceof TextNode);
        TextNode textNode = (TextNode) element3ChildNodes.get(0);
        assertEquals("data", textNode.getWholeText());
    }
}