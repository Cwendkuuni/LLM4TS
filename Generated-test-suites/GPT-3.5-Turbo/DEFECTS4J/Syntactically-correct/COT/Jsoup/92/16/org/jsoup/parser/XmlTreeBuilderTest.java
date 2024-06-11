
import static org.junit.Assert.*;
import org.junit.Test;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import java.io.StringReader;
import java.util.List;

public class XmlTreeBuilderTest {
    
    @Test
    public void testDefaultSettings() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        ParseSettings parseSettings = xmlTreeBuilder.defaultSettings();
        
        assertEquals(ParseSettings.preserveCase, parseSettings);
    }
    
    @Test
    public void testInitialiseParse() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        StringReader input = new StringReader("<test>test</test>");
        String baseUri = "https://www.example.com";
        Parser parser = Parser.xmlParser();
        
        xmlTreeBuilder.initialiseParse(input, baseUri, parser);
        
        assertEquals(ParseSettings.preserveCase, xmlTreeBuilder.defaultSettings());
        assertEquals(baseUri, xmlTreeBuilder.baseUri);
        assertEquals(null, xmlTreeBuilder.currentElement());
        assertTrue(xmlTreeBuilder.doc.outputSettings().syntax() == Document.OutputSettings.Syntax.xml);
    }
    
    @Test
    public void testParse() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        StringReader input = new StringReader("<test>test</test>");
        String baseUri = "https://www.example.com";
        
        Document document = xmlTreeBuilder.parse(input, baseUri);
        
        assertNotNull(document);
        assertEquals(baseUri, document.baseUri());
    }
    
    @Test
    public void testParseString() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        String input = "<test>test</test>";
        String baseUri = "https://www.example.com";
        
        Document document = xmlTreeBuilder.parse(input, baseUri);
        
        assertNotNull(document);
        assertEquals(baseUri, document.baseUri());
    }
    
    @Test
    public void testProcess() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Token token = new Token();
        token.type = Token.TokenType.StartTag;
        token.name = "start";
        
        assertTrue(xmlTreeBuilder.process(token));
        assertEquals("start", xmlTreeBuilder.currentElement().nodeName());
    }
    
    @Test
    public void testInsertNode() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Element element = new Element("test");
        xmlTreeBuilder.stack.push(element);
        Node node = new Node();
        node.nodeName = "testNode";
        
        xmlTreeBuilder.insertNode(node);
        
        assertSame(node, xmlTreeBuilder.currentElement().childNodes().get(0));
    }
    
    @Test
    public void testInsertStartTag() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Token.StartTag startTag = new Token.StartTag();
        startTag.name = "testTag";
        startTag.attributes = new Attributes();
        
        Element element = xmlTreeBuilder.insert(startTag);
        
        assertNotNull(element);
        assertEquals("testTag", element.nodeName());
        assertTrue(element.isBlock());
        assertFalse(element.isInline());
    }
    
    @Test
    public void testInsertComment() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Token.Comment commentToken = new Token.Comment();
        commentToken.data = "test comment";
        
        xmlTreeBuilder.insert(commentToken);
        
        assertEquals("test comment", xmlTreeBuilder.currentElement().childNodes().get(0).toString());
    }
    
    @Test
    public void testInsertCharacter() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Token.Character characterToken = new Token.Character();
        characterToken.data = "test";
        
        xmlTreeBuilder.insert(characterToken);
        
        assertEquals("test", xmlTreeBuilder.currentElement().childNodes().get(0).toString());
    }
    
    @Test
    public void testInsertDoctype() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Token.Doctype doctype = new Token.Doctype();
        doctype.setName("testDocType");
        
        xmlTreeBuilder.insert(doctype);
        
        assertEquals("<!DOCTYPE testDocType>", xmlTreeBuilder.currentElement().childNodes().get(0).toString());
    }
    
    @Test
    public void testPopStackToClose() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        xmlTreeBuilder.stack.push(new Element("test", "test"));
        xmlTreeBuilder.stack.push(new Element("p", "p"));
        xmlTreeBuilder.stack.push(new Element("test", "test"));
        
        Token.EndTag endTag = new Token.EndTag();
        endTag.tagName = "test";
        xmlTreeBuilder.popStackToClose(endTag);
        
        assertEquals(1, xmlTreeBuilder.stack.size());
        assertEquals("p", xmlTreeBuilder.currentElement().nodeName());
    }
    
    @Test
    public void testParseFragment() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        String inputFragment = "<p>test</p>";
        String baseUri = "https://www.example.com";
        Parser parser = Parser.xmlParser();
        
        List<Node> nodes = xmlTreeBuilder.parseFragment(inputFragment, baseUri, parser);
        
        assertNotNull(nodes);
        assertEquals(1, nodes.size());
        assertEquals("p", nodes.get(0).nodeName());
        assertEquals(baseUri, xmlTreeBuilder.baseUri);
    }
    
    @Test
    public void testParseFragmentWithContext() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        String inputFragment = "<p>test</p>";
        Element context = new Element("div");
        String baseUri = "https://www.example.com";
        Parser parser = Parser.xmlParser();
        
        List<Node> nodes = xmlTreeBuilder.parseFragment(inputFragment, context, baseUri, parser);
        
        assertNotNull(nodes);
        assertEquals(1, nodes.size());
        assertEquals("p", nodes.get(0).nodeName());
        assertEquals(context, xmlTreeBuilder.currentElement());
        assertEquals(baseUri, xmlTreeBuilder.baseUri);
    }
}
