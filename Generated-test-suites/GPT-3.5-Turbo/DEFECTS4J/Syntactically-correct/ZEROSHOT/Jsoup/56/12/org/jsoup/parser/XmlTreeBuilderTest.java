import org.junit.Test;
import org.junit.Before;
import org.jsoup.Jsoup;
import org.jsoup.parser.TreeBuilder;
import org.jsoup.parser.XmlTreeBuilder;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import static org.junit.Assert.*;

public class XmlTreeBuilderTest {
    
    private XmlTreeBuilder xmlTreeBuilder;
    
    @Before
    public void setUp() {
        xmlTreeBuilder = new XmlTreeBuilder();
    }
    
    @Test
    public void testDefaultSettings() {
        ParseSettings settings = xmlTreeBuilder.defaultSettings();
        assertNotNull(settings);
    }
    
    @Test
    public void testParse() {
        String input = "<root><element>Text</element></root>";
        String baseUri = "http://example.com";
        Document document = xmlTreeBuilder.parse(input, baseUri);
        assertNotNull(document);
        assertEquals("http://example.com", document.baseUri());
        assertNotNull(document.select("element"));
        assertEquals(1, document.select("element").size());
        assertEquals("Text", document.select("element").get(0).text());
    }
    
    @Test
    public void testInitialiseParse() {
        String input = "<root><element>Text</element></root>";
        String baseUri = "http://example.com";
        ParseErrorList errors = ParseErrorList.noTracking();
        ParseSettings settings = ParseSettings.preserveCase;
        xmlTreeBuilder.initialiseParse(input, baseUri, errors, settings);
        assertNotNull(xmlTreeBuilder.doc);
        assertNotNull(xmlTreeBuilder.stack);
        assertEquals(1, xmlTreeBuilder.stack.size());
        assertEquals(xmlTreeBuilder.doc, xmlTreeBuilder.stack.get(0));
        assertNotNull(xmlTreeBuilder.doc.outputSettings());
        assertEquals(Document.OutputSettings.Syntax.xml, xmlTreeBuilder.doc.outputSettings().syntax());
    }
    
    @Test
    public void testProcess() {
        Token startTagToken = new Token(Token.TokenType.StartTag, "<element>");
        assertTrue(xmlTreeBuilder.process(startTagToken));
        assertNotNull(xmlTreeBuilder.currentElement());
        assertEquals("element", xmlTreeBuilder.currentElement().tagName());
        
        Token endTagToken = new Token(Token.TokenType.EndTag, "</element>");
        assertTrue(xmlTreeBuilder.process(endTagToken));
        assertNull(xmlTreeBuilder.currentElement());
        
        Token commentToken = new Token(Token.TokenType.Comment, "<!-- Comment -->");
        assertTrue(xmlTreeBuilder.process(commentToken));
        assertNotNull(xmlTreeBuilder.currentElement());
        assertEquals("comment", xmlTreeBuilder.currentElement().tagName());
        
        Token characterToken = new Token(Token.TokenType.Character, "Text");
        assertTrue(xmlTreeBuilder.process(characterToken));
        assertNotNull(xmlTreeBuilder.currentElement());
        assertEquals("text", xmlTreeBuilder.currentElement().tagName());
        
        Token doctypeToken = new Token(Token.TokenType.Doctype, "<!DOCTYPE html>");
        assertTrue(xmlTreeBuilder.process(doctypeToken));
        assertNotNull(xmlTreeBuilder.currentElement());
        assertEquals("doctype", xmlTreeBuilder.currentElement().tagName());
        
        Token eofToken = new Token(Token.TokenType.EOF, "");
        assertTrue(xmlTreeBuilder.process(eofToken));
    }
    
    @Test
    public void testInsertNode() {
        Document document = Jsoup.parse("<root></root>");
        Element root = document.select("root").first();
        xmlTreeBuilder.stack.add(root);
        
        Element child = document.createElement("child");
        xmlTreeBuilder.insertNode(child);
        
        Elements children = root.children();
        assertNotNull(children);
        assertEquals(1, children.size());
        assertEquals("child", children.get(0).tagName());
    }
    
    @Test
    public void testInsertStartTag() {
        Document document = Jsoup.parse("<root></root>");
        Element root = document.select("root").first();
        xmlTreeBuilder.stack.add(root);
        
        Token.StartTag startTag = new Token.StartTag("child");
        Element element = xmlTreeBuilder.insert(startTag);
        
        assertNotNull(element);
        assertEquals("child", element.tagName());
        assertNotNull(element.parent());
        assertEquals(root, element.parent());
        assertEquals(1, root.children().size());
        assertEquals(element, root.children().get(0));
    }
    
    @Test
    public void testInsertComment() {
        Document document = Jsoup.parse("<root></root>");
        Element root = document.select("root").first();
        xmlTreeBuilder.stack.add(root);
        
        Token.Comment commentToken = new Token.Comment("Comment");
        xmlTreeBuilder.insert(commentToken);
        
        Elements children = root.children();
        assertNotNull(children);
        assertEquals(1, children.size());
        assertEquals("#comment", children.get(0).tagName());
        assertEquals("Comment", children.get(0).data());
    }
    
    @Test
    public void testInsertCharacter() {
        Document document = Jsoup.parse("<root></root>");
        Element root = document.select("root").first();
        xmlTreeBuilder.stack.add(root);
        
        Token.Character characterToken = new Token.Character("Text");
        xmlTreeBuilder.insert(characterToken);
        
        Elements children = root.children();
        assertNotNull(children);
        assertEquals(1, children.size());
        assertEquals("#text", children.get(0).tagName());
        assertEquals("Text", children.get(0).data());
    }
    
    @Test
    public void testInsertDoctype() {
        Document document = Jsoup.parse("<root></root>");
        Element root = document.select("root").first();
        xmlTreeBuilder.stack.add(root);
        
        Token.Doctype doctypeToken = new Token.Doctype("html", "", "", "");
        xmlTreeBuilder.insert(doctypeToken);
        
        Elements children = root.children();
        assertNotNull(children);
        assertEquals(1, children.size());
        assertEquals("#doctype", children.get(0).tagName());
        assertEquals("html", children.get(0).nodeName());
    }
    
    @Test
    public void testPopStackToClose() {
        Document document = Jsoup.parse("<root><child1></child1><child2></child2></root>");
        Element root = document.select("root").first();
        Element child1 = document.select("child1").first();
        Element child2 = document.select("child2").first();
        xmlTreeBuilder.stack.add(root);
        xmlTreeBuilder.stack.add(child1);
        xmlTreeBuilder.stack.add(child2);
        
        Token.EndTag endTag = new Token.EndTag("child1");
        xmlTreeBuilder.popStackToClose(endTag);
        
        assertEquals(1, xmlTreeBuilder.stack.size());
        assertEquals(child2, xmlTreeBuilder.stack.get(0));
    }
    
    @Test
    public void testParseFragment() {
        String input = "<child>Text</child>";
        String baseUri = "http://example.com";
        ParseErrorList errors = ParseErrorList.noTracking();
        ParseSettings settings = ParseSettings.preserveCase;
        
        List<Node> nodes = xmlTreeBuilder.parseFragment(input, baseUri, errors, settings);
        
        assertNotNull(nodes);
        assertEquals(1, nodes.size());
        assertEquals("child", nodes.get(0).nodeName());
        assertEquals("Text", nodes.get(0).text());
    }
}