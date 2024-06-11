import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.StringReader;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Comment;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.parser.ParseErrorList;
import org.jsoup.parser.ParseSettings;
import org.jsoup.parser.Parser;
import org.jsoup.parser.Token;
import org.junit.Before;
import org.junit.Test;

public class XmlTreeBuilderTest {
    
    private XmlTreeBuilder xmlTreeBuilder;
    private Document document;
    
    @Before
    public void setup() {
        xmlTreeBuilder = new XmlTreeBuilder();
        document = new Document("");
    }
    
    @Test
    public void testDefaultSettings() {
        ParseSettings settings = xmlTreeBuilder.defaultSettings();
        
        assertEquals(ParseSettings.preserveCase, settings);
    }
    
    @Test
    public void testParseWithReaderAndBaseUri() {
        Reader reader = new StringReader("");
        String baseUri = "";
        
        Document parsedDocument = xmlTreeBuilder.parse(reader, baseUri);
        
        assertEquals(document, parsedDocument);
    }
    
    @Test
    public void testParseWithStringAndBaseUri() {
        String input = "";
        String baseUri = "";
        
        Document parsedDocument = xmlTreeBuilder.parse(input, baseUri);
        
        assertEquals(document, parsedDocument);
    }
    
    @Test
    public void testInitialiseParse() {
        Reader reader = new StringReader("");
        String baseUri = "";
        ParseErrorList errors = ParseErrorList.noTracking();
        ParseSettings settings = ParseSettings.preserveCase;
        xmlTreeBuilder.initialiseParse(reader, baseUri, errors, settings);
        
        assertEquals(doc, xmlTreeBuilder.stack.get(0));
        assertEquals(Document.OutputSettings.Syntax.xml, xmlTreeBuilder.doc.outputSettings().syntax());
    }
    
    @Test
    public void testProcessStartTagToken() {
        Token.StartTag startTag = new Token.StartTag("");
        
        boolean result = xmlTreeBuilder.process(startTag);
        
        assertTrue(result);
    }
    
    @Test
    public void testProcessEndTagToken() {
        Token.EndTag endTag = new Token.EndTag("");
        
        boolean result = xmlTreeBuilder.process(endTag);
        
        assertTrue(result);
    }
    
    @Test
    public void testProcessCommentToken() {
        Token.Comment comment = new Token.Comment("");
        
        boolean result = xmlTreeBuilder.process(comment);
        
        assertTrue(result);
    }
    
    @Test
    public void testProcessCharacterToken() {
        Token.Character character = new Token.Character("");
        
        boolean result = xmlTreeBuilder.process(character);
        
        assertTrue(result);
    }
    
    @Test
    public void testProcessDoctypeToken() {
        Token.Doctype doctype = new Token.Doctype("");
        
        boolean result = xmlTreeBuilder.process(doctype);
        
        assertTrue(result);
    }
    
    @Test
    public void testInsertNode() {
        Node node = new Element("");
        
        xmlTreeBuilder.insertNode(node);
        
        assertEquals(node, xmlTreeBuilder.currentElement().childNode(0));
    }
    
    @Test
    public void testInsertStartElementTag() {
        Token.StartTag startTag = new Token.StartTag("");
        
        Element element = xmlTreeBuilder.insert(startTag);
        
        assertEquals(settings.normalizeTag(startTag.name()), element.tagName());
        assertTrue(xmlTreeBuilder.stack.contains(element));
    }
    
    @Test
    public void testInsertSelfClosingStartElementTag() {
        Token.StartTag startTag = new Token.StartTag("").setSelfClosing();
        
        Element element = xmlTreeBuilder.insert(startTag);
        
        assertEquals(settings.normalizeTag(startTag.name()), element.tagName());
        assertTrue(xmlTreeBuilder.stack.isEmpty());
        assertTrue(element.tag().isSelfClosing());
    }
    
    @Test
    public void testInsertCommentToken() {
        Token.Comment commentToken = new Token.Comment("");
        
        xmlTreeBuilder.insert(commentToken);
        
        assertEquals(new Comment(commentToken.getData()), xmlTreeBuilder.currentElement().childNode(0));
    }
    
    @Test
    public void testInsertCharacterToken() {
        Token.Character token = new Token.Character("");
        
        xmlTreeBuilder.insert(token);
        
        assertEquals(new TextNode(token.getData()), xmlTreeBuilder.currentElement().childNode(0));
    }
    
    @Test
    public void testInsertDoctypeToken() {
        Token.Doctype doctype = new Token.Doctype("");
        
        xmlTreeBuilder.insert(doctype);
        
        assertEquals(new DocumentType(settings.normalizeTag(doctype.getName()), doctype.getPublicIdentifier(), doctype.getSystemIdentifier()), xmlTreeBuilder.currentElement().childNode(0));
    }
    
    @Test
    public void testPopStackToClose() {
        Token.EndTag endTag = new Token.EndTag("");
        
        xmlTreeBuilder.popStackToClose(endTag);
        
        assertTrue(xmlTreeBuilder.stack.isEmpty());
    }
    
    @Test
    public void testParseFragment() {
        String inputFragment = "";
        String baseUri = "";
        ParseErrorList errors = ParseErrorList.noTracking();
        ParseSettings settings = ParseSettings.preserveCase;
        
        List<Node> nodes = xmlTreeBuilder.parseFragment(inputFragment, baseUri, errors, settings);
        
        assertEquals(document.childNodes(), nodes);
    }
    
}