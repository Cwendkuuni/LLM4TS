import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.jsoup.parser.XmlTreeBuilder;
import org.jsoup.Jsoup;
import org.jsoup.helper.Validate;
import org.jsoup.nodes.*;
import java.util.List;

public class XmlTreeBuilderTest {
    
    private XmlTreeBuilder xmlTreeBuilder;
    
    @Before
    public void setUp() {
        xmlTreeBuilder = new XmlTreeBuilder();
    }
    
    @Test
    public void testDefaultSettings() {
        ParseSettings expected = ParseSettings.preserveCase;
        ParseSettings result = xmlTreeBuilder.defaultSettings();
        assertEquals(expected, result);
    }
    
    @Test
    public void testParse() {
        String input = "<xml><element>Text</element></xml>";
        String baseUri = "http://example.com";
        Document expected = Jsoup.parse(input, baseUri, Parser.xmlParser());
        Document result = xmlTreeBuilder.parse(input, baseUri);
        assertEquals(expected, result);
    }
    
    @Test
    public void testInitialiseParse() {
        String input = "<xml><element>Text</element></xml>";
        String baseUri = "http://example.com";
        ParseErrorList errors = ParseErrorList.noTracking();
        ParseSettings settings = ParseSettings.preserveCase;
        
        xmlTreeBuilder.initialiseParse(input, baseUri, errors, settings);
        
        assertEquals(baseUri, xmlTreeBuilder.baseUri);
        assertEquals(errors, xmlTreeBuilder.errors);
        assertEquals(settings, xmlTreeBuilder.settings);
        assertEquals(Document.OutputSettings.Syntax.xml, xmlTreeBuilder.doc.outputSettings().syntax());
    }
    
    @Test
    public void testProcess_StartTag() {
        Token token = new Token.StartTag("element");
        Element expected = new Element(Tag.valueOf("element"));
        
        boolean result = xmlTreeBuilder.process(token);
        Element resultElement = xmlTreeBuilder.currentElement().child(0);
        
        assertTrue(result);
        assertEquals(expected, resultElement);
    }
    
    @Test
    public void testProcess_EndTag() {
        Token.StartTag startTag = new Token.StartTag("element");
        xmlTreeBuilder.process(startTag);
        
        Token token = new Token.EndTag("element");
        
        boolean result = xmlTreeBuilder.process(token);
        Element resultElement = xmlTreeBuilder.currentElement().child(0);
        
        assertTrue(result);
        assertNull(resultElement); // Element should have been removed from stack
    }
    
    @Test
    public void testProcess_Comment() {
        String commentData = "This is a comment";
        Token token = new Token.Comment(commentData);
        Comment expected = new Comment(commentData);
        
        boolean result = xmlTreeBuilder.process(token);
        Comment resultComment = (Comment) xmlTreeBuilder.currentElement().childNode(0);
        
        assertTrue(result);
        assertEquals(expected, resultComment);
    }
    
    @Test
    public void testProcess_Character() {
        String characterData = "Text";
        Token token = new Token.Character(characterData);
        TextNode expected = new TextNode(characterData);
        
        boolean result = xmlTreeBuilder.process(token);
        TextNode resultNode = (TextNode) xmlTreeBuilder.currentElement().childNode(0);
        
        assertTrue(result);
        assertEquals(expected, resultNode);
    }
    
    @Test
    public void testInsertNode() {
        Node node = new Element(Tag.valueOf("element"));
        Element expected = (Element) node;
        
        xmlTreeBuilder.insertNode(node);
        Element result = xmlTreeBuilder.currentElement().child(0);
        
        assertEquals(expected, result);
    }
    
    @Test
    public void testInsert_StartTag() {
        Token.StartTag startTag = new Token.StartTag("element");
        Element expected = new Element(Tag.valueOf("element"));
        
        Element result = xmlTreeBuilder.insert(startTag);
        
        assertEquals(expected, result);
    }
    
    @Test
    public void testInsert_Comment() {
        String commentData = "This is a comment";
        Token.Comment token = new Token.Comment(commentData);
        Comment expected = new Comment(commentData);
        
        xmlTreeBuilder.insert(token);
        Comment result = (Comment) xmlTreeBuilder.currentElement().childNode(0);
        
        assertEquals(expected, result);
    }
    
    @Test
    public void testInsert_Character() {
        String characterData = "Text";
        Token.Character token = new Token.Character(characterData);
        TextNode expected = new TextNode(characterData);
        
        xmlTreeBuilder.insert(token);
        TextNode result = (TextNode) xmlTreeBuilder.currentElement().childNode(0);
        
        assertEquals(expected, result);
    }
    
    @Test
    public void testInsert_Doctype() {
        String doctypeName = "DOCTYPE";
        String publicIdentifier = "PUBLIC";
        String systemIdentifier = "SYSTEM";
        Token.Doctype doctype = new Token.Doctype(doctypeName, publicIdentifier, systemIdentifier);
        DocumentType expected = new DocumentType(doctypeName, publicIdentifier, systemIdentifier);
        
        xmlTreeBuilder.insert(doctype);
        DocumentType result = (DocumentType) xmlTreeBuilder.currentElement().childNode(0);
        
        assertEquals(expected, result);
    }
    
    @Test
    public void testPopStackToClose() {
        Token.StartTag startTag1 = new Token.StartTag("element");
        xmlTreeBuilder.insert(startTag1);
        
        Token.StartTag startTag2 = new Token.StartTag("element");
        xmlTreeBuilder.insert(startTag2);
        
        Token.EndTag endTag = new Token.EndTag("element");
        
        xmlTreeBuilder.popStackToClose(endTag);
        
        Element resultElement = xmlTreeBuilder.currentElement().child(0);
        
        assertNull(resultElement); // Both elements with the tag name should have been removed from stack
    }
    
    @Test
    public void testParseFragment() {
        String inputFragment = "<element>Text</element>";
        String baseUri = "http://example.com";
        ParseErrorList errors = ParseErrorList.noTracking();
        ParseSettings settings = ParseSettings.preserveCase;
        List<Node> expected = Jsoup.parseFragment(inputFragment, baseUri, Parser.xmlParser());
        
        List<Node> result = xmlTreeBuilder.parseFragment(inputFragment, baseUri, errors, settings);
        
        assertEquals(expected, result);
    }
}