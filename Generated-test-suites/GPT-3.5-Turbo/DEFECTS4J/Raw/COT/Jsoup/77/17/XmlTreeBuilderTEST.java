
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.parser.ParseErrorList;
import org.jsoup.parser.ParseSettings;
import org.jsoup.parser.StringReader;
import org.jsoup.parser.Token;
import org.jsoup.parser.XmlTreeBuilder;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.io.Reader;
import java.util.List;

public class XmlTreeBuilderTest {
    private XmlTreeBuilder xmlTreeBuilder;
    
    @Before
    public void setUp() {
        xmlTreeBuilder = new XmlTreeBuilder();
    }
    
    @Test
    public void testParse_Reader_String() {
        // given
        Reader input = new StringReader("<xml>sample</xml>");
        String baseUri = "http://example.com";
        
        // when
        Document result = xmlTreeBuilder.parse(input, baseUri);
        
        // then
        assertNotNull(result);
        assertEquals("sample", result.text());
    }
    
    @Test
    public void testParse_String_String() {
        // given
        String input = "<xml>sample</xml>";
        String baseUri = "http://example.com";
        
        // when
        Document result = xmlTreeBuilder.parse(input, baseUri);
        
        // then
        assertNotNull(result);
        assertEquals("sample", result.text());
    }
    
    @Test
    public void testInitialiseParse_Reader_String_ParseErrorList_ParseSettings() {
        // given
        Reader input = new StringReader("<xml>sample</xml>");
        String baseUri = "http://example.com";
        ParseErrorList errors = new ParseErrorList();
        ParseSettings settings = ParseSettings.preserveCase;
        
        // when
        xmlTreeBuilder.initialiseParse(input, baseUri, errors, settings);
        
        // then
        assertEquals(input, xmlTreeBuilder.input);
        assertEquals(baseUri, xmlTreeBuilder.baseUri);
        assertEquals(errors, xmlTreeBuilder.errors);
        assertEquals(settings, xmlTreeBuilder.settings);
        assertEquals(xmlTreeBuilder.doc, xmlTreeBuilder.stack.get(0));
        assertEquals(Document.OutputSettings.Syntax.xml, xmlTreeBuilder.doc.outputSettings().syntax());
    }
    
    @Test
    public void testProcess() {
        // given
        Token token = new Token.StartTag("xml");
        
        // when
        boolean result = xmlTreeBuilder.process(token);
        
        // then
        assertTrue(result);
        assertEquals("<xml>", xmlTreeBuilder.currentElement().toString());
    }
    
    @Test
    public void testInsertNode() {
        // given
        Node node = new TextNode("sample");
        Element currentElement = new Element(Tag.valueOf("test"), "http://example.com");
        xmlTreeBuilder.stack.add(currentElement);
        
        // when
        xmlTreeBuilder.insertNode(node);
        
        // then
        assertEquals(node, currentElement.childNode(0));
    }
    
    @Test
    public void testInsert_StartTag() {
        // given
        Token.StartTag startTag = new Token.StartTag("xml");
        
        // when
        Element result = xmlTreeBuilder.insert(startTag);
        
        // then
        assertEquals("<xml>", result.toString());
        assertEquals(result, xmlTreeBuilder.currentElement());
        assertEquals(result, xmlTreeBuilder.stack.get(xmlTreeBuilder.stack.size()-1));
    }
    
    @Test
    public void testInsert_Comment() {
        // given
        Token.Comment comment = new Token.Comment("sample comment");
        
        // when
        xmlTreeBuilder.insert(comment);
        
        // then
        assertEquals("<!--sample comment-->", xmlTreeBuilder.currentElement().toString());
    }
    
    @Test
    public void testInsert_Character() {
        // given
        Token.Character character = new Token.Character("sample");
        
        // when
        xmlTreeBuilder.insert(character);
        
        // then
        assertEquals(new TextNode("sample"), xmlTreeBuilder.currentElement().childNode(0));
    }
    
    @Test
    public void testInsert_Doctype() {
        // given
        Token.Doctype doctype = new Token.Doctype("html", "", "");
        
        // when
        xmlTreeBuilder.insert(doctype);
        
        // then
        assertEquals("<!DOCTYPE html>", xmlTreeBuilder.currentElement().toString());
    }
    
    @Test
    public void testPopStackToClose() {
        // given
        Token.EndTag endTag = new Token.EndTag("xml");
        Element element = new Element(Tag.valueOf("xml"), "http://example.com");
        xmlTreeBuilder.stack.add(element);
        
        // when
        xmlTreeBuilder.popStackToClose(endTag);
        
        // then
        assertEquals(0, xmlTreeBuilder.stack.size());
    }
    
    @Test
    public void testParseFragment() {
        // given
        String inputFragment = "<xml>sample</xml>";
        String baseUri = "http://example.com";
        ParseErrorList errors = new ParseErrorList();
        ParseSettings settings = ParseSettings.preserveCase;
        
        // when
        List<Node> result = xmlTreeBuilder.parseFragment(inputFragment, baseUri, errors, settings);
        
        // then
        assertEquals("sample", result.get(0).toString());
    }
}

