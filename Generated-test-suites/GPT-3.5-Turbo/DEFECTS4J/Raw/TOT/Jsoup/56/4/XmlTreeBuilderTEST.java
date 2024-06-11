import org.junit.Before;
import org.junit.Test;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.ParseErrorList;
import org.jsoup.parser.ParseSettings;
import org.jsoup.parser.XmlTreeBuilder;
import org.jsoup.parser.Token;

import static org.junit.Assert.*;

public class XmlTreeBuilderTest {
    
    private XmlTreeBuilder xmlTreeBuilder;
    private ParseSettings parseSettings;
    private ParseErrorList parseErrorList;
    
    @Before
    public void setUp() {
        xmlTreeBuilder = new XmlTreeBuilder();
        parseSettings = ParseSettings.preserveCase;
        parseErrorList = ParseErrorList.noTracking();
    }
    
    @Test
    public void testDefaultSettings() {
        assertEquals(ParseSettings.preserveCase, xmlTreeBuilder.defaultSettings());
    }
    
    @Test
    public void testParse() {
        String input = "<example>Test Input</example>";
        String baseUri = "http://www.example.com";
        
        Document document = xmlTreeBuilder.parse(input, baseUri);
        
        assertNotNull(document);
        assertEquals(input, document.toString());
        assertEquals(baseUri, document.baseUri());
    }
    
    @Test
    public void testInitialiseParse() {
        String input = "<example>Test Input</example>";
        String baseUri = "http://www.example.com";
        
        xmlTreeBuilder.initialiseParse(input, baseUri, parseErrorList, parseSettings);
        
        assertNotNull(xmlTreeBuilder.stack);
        assertEquals(1, xmlTreeBuilder.stack.size());
        assertTrue(xmlTreeBuilder.stack.get(0) instanceof Document);
        assertEquals(ParseSettings.preserveCase, xmlTreeBuilder.settings);
    }
    
    @Test
    public void testProcess_startTag() {
        Token.StartTag startTag = new Token.StartTag();
        startTag.name("example");
        
        xmlTreeBuilder.process(startTag);
        
        assertNotNull(xmlTreeBuilder.currentElement());
        assertEquals("example", xmlTreeBuilder.currentElement().tagName());
    }
    
    @Test
    public void testProcess_endTag() {
        Element element = new Element("example");
        xmlTreeBuilder.stack.add(element);
        
        Token.EndTag endTag = new Token.EndTag();
        endTag.name("example");
        
        xmlTreeBuilder.process(endTag);
        
        assertEquals(0, xmlTreeBuilder.stack.size());
    }
    
    @Test
    public void testProcess_comment() {
        Token.Comment comment = new Token.Comment();
        comment.data("Test Comment");
        
        xmlTreeBuilder.process(comment);
        
        assertNotNull(xmlTreeBuilder.currentElement());
        assertEquals("Test Comment", xmlTreeBuilder.currentElement().ownText());
    }
    
    @Test
    public void testProcess_character() {
        Token.Character character = new Token.Character();
        character.data("Test Character");
        
        xmlTreeBuilder.process(character);
        
        assertNotNull(xmlTreeBuilder.currentElement());
        assertEquals("Test Character", xmlTreeBuilder.currentElement().ownText());
    }
    
    @Test
    public void testProcess_doctype() {
        Token.Doctype doctype = new Token.Doctype();
        doctype.name("html");
        doctype.publicIdentifier("-//W3C//DTD XHTML 1.0 Strict//EN");
        doctype.systemIdentifier("http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd");
        
        xmlTreeBuilder.process(doctype);
        
        assertNotNull(xmlTreeBuilder.currentElement());
        assertTrue(xmlTreeBuilder.currentElement() instanceof DocumentType);
    }
    
    @Test
    public void testInsertNode() {
        Element element = new Element("example");
        xmlTreeBuilder.stack.add(element);
        
        Element childElement = new Element("child");
        xmlTreeBuilder.insertNode(childElement);
        
        assertEquals(childElement, xmlTreeBuilder.currentElement().child(0));
    }
    
    @Test
    public void testInsert_startTag_selfClosing() {
        Token.StartTag startTag = new Token.StartTag();
        startTag.name("example");
        startTag.selfClosing(true);
        
        Element element = xmlTreeBuilder.insert(startTag);
        
        assertNotNull(element);
        assertEquals("example", element.tagName());
        assertTrue(element.isSelfClosing());
    }
    
    @Test
    public void testInsert_startTag_notSelfClosing_knownTag() {
        Token.StartTag startTag = new Token.StartTag();
        startTag.name("div");
        startTag.selfClosing(false);
        
        Element element = xmlTreeBuilder.insert(startTag);
        
        assertNotNull(element);
        assertEquals("div", element.tagName());
        assertTrue(element.isBlock());
        assertFalse(element.isSelfClosing());
    }
    
    @Test
    public void testInsert_startTag_notSelfClosing_unknownTag() {
        Token.StartTag startTag = new Token.StartTag();
        startTag.name("example");
        startTag.selfClosing(false);
        
        Element element = xmlTreeBuilder.insert(startTag);
        
        assertNotNull(element);
        assertEquals("example", element.tagName());
        assertTrue(element.isBlock());
        assertFalse(element.isSelfClosing());
    }
    
   @Test
    public void testInsert_comment() {
        Token.Comment commentToken = new Token.Comment();
        commentToken.data("Test Comment");
        
        xmlTreeBuilder.insert(commentToken);
        
        assertNotNull(xmlTreeBuilder.currentElement());
        assertEquals("Test Comment", xmlTreeBuilder.currentElement().ownText());
    }
    
    @Test
    public void testInsert_comment_bogus() {
        Token.Comment commentToken = new Token.Comment();
        commentToken.data("!DOCTYPE html");
        commentToken.bogus(true);
        
        xmlTreeBuilder.insert(commentToken);
        
        assertNotNull(xmlTreeBuilder.currentElement());
        assertEquals("!DOCTYPE html", xmlTreeBuilder.currentElement().ownText());
        assertTrue(xmlTreeBuilder.currentElement() instanceof XmlDeclaration);
    }
    
    @Test
    public void testInsert_character() {
        Token.Character characterToken = new Token.Character();
        characterToken.data("Test Character");
        
        xmlTreeBuilder.insert(characterToken);
        
        assertNotNull(xmlTreeBuilder.currentElement());
        assertEquals("Test Character", xmlTreeBuilder.currentElement().ownText());
        assertTrue(xmlTreeBuilder.currentElement() instanceof TextNode);
    }
    
    @Test
    public void testInsert_doctype() {
        Token.Doctype doctype = new Token.Doctype();
        doctype.name("html");
        doctype.publicIdentifier("-//W3C//DTD XHTML 1.0 Strict//EN");
        doctype.systemIdentifier("http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd");
        
        xmlTreeBuilder.insert(doctype);
        
        assertNotNull(xmlTreeBuilder.currentElement());
        assertTrue(xmlTreeBuilder.currentElement() instanceof DocumentType);
    }
    
    @Test
    public void testPopStackToClose_notFound() {
        Element element = new Element("example");
        xmlTreeBuilder.stack.add(element);
        
        Token.EndTag endTag = new Token.EndTag();
        endTag.name("test");
        
        xmlTreeBuilder.popStackToClose(endTag);
        
        assertEquals(1, xmlTreeBuilder.stack.size());
    }
    
    @Test
    public void testPopStackToClose_found() {
        Element exampleElement1 = new Element("example");
        Element exampleElement2 = new Element("example");
        
        xmlTreeBuilder.stack.add(exampleElement1);
        xmlTreeBuilder.stack.add(exampleElement2);
        
        Token.EndTag endTag = new Token.EndTag();
        endTag.name("example");
        
        xmlTreeBuilder.popStackToClose(endTag);
        
        assertEquals(0, xmlTreeBuilder.stack.size());
    }
    
    @Test
    public void testParseFragment() {
        String inputFragment = "<example>Test Input</example>";
        String baseUri = "http://www.example.com";
        
        xmlTreeBuilder.parseFragment(inputFragment, baseUri, parseErrorList, parseSettings);
        
        assertNotNull(xmlTreeBuilder.stack);
        assertEquals(1, xmlTreeBuilder.stack.size());
        assertTrue(xmlTreeBuilder.stack.get(0) instanceof Document);
        assertNotNull(xmlTreeBuilder.doc.childNodes());
    }
}