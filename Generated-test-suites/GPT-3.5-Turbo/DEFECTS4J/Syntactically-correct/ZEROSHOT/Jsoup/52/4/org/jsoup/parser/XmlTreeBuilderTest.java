import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.parser.ParseErrorList;
import org.jsoup.parser.Token;
import org.jsoup.parser.XmlTreeBuilder;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class XmlTreeBuilderTest {
    
    private XmlTreeBuilder xmlTreeBuilder;
    private ParseErrorList errors;
    
    @Before
    public void setUp() {
        xmlTreeBuilder = new XmlTreeBuilder();
        errors = new ParseErrorList();
    }
    
    @Test
    public void initialiseParseTest() {
        String input = "<xml><tag>content</tag></xml>";
        String baseUri = "http://example.com";
        
        xmlTreeBuilder.initialiseParse(input, baseUri, errors);
        
        Document doc = xmlTreeBuilder.getDocument();
        assertEquals("http://example.com", doc.baseUri());
        assertEquals("<xml><tag>content</tag></xml>", doc.html());
    }
    
    @Test
    public void processStartTagTest() {
        Token.StartTag startTag = new Token.StartTag("tag");
        startTag.attributes.put("attribute", "value");
        
        Document doc = xmlTreeBuilder.getDocument();
        xmlTreeBuilder.process(startTag);
        
        assertEquals("<tag attribute=\"value\"></tag>", doc.html());
    }
    
    @Test
    public void processEndTagTest() {
        Token.StartTag startTag = new Token.StartTag("tag");
        Document doc = xmlTreeBuilder.getDocument();
        xmlTreeBuilder.process(startTag);
        
        Token.EndTag endTag = new Token.EndTag("tag");
        xmlTreeBuilder.process(endTag);
        
        assertEquals("<tag></tag>", doc.html());
    }
    
    @Test
    public void processCommentTest() {
        Token.Comment comment = new Token.Comment("Comment");
        Document doc = xmlTreeBuilder.getDocument();
        xmlTreeBuilder.process(comment);
        
        assertEquals("<!--Comment-->", doc.html());
    }
    
    @Test
    public void processCharacterTest() {
        Token.Character character = new Token.Character("Character");
        Document doc = xmlTreeBuilder.getDocument();
        xmlTreeBuilder.process(character);
        
        assertEquals("Character", doc.html());
    }
    
    @Test
    public void processDoctypeTest() {
        Token.Doctype doctype = new Token.Doctype("doctype", "public", "system");
        Document doc = xmlTreeBuilder.getDocument();
        xmlTreeBuilder.process(doctype);
        
        assertEquals("<!DOCTYPE doctype PUBLIC \"public\" \"system\">", doc.html());
    }
    
    @Test
    public void insertNodeTest() {
        Document doc = xmlTreeBuilder.getDocument();
        xmlTreeBuilder.insertNode(doc);
        
        assertEquals("<html></html>", doc.html());
    }
    
    @Test
    public void insertStartTagTest() {
        Token.StartTag startTag = new Token.StartTag("tag");
        startTag.attributes.put("attribute", "value");
        
        Document doc = xmlTreeBuilder.getDocument();
        Element element = xmlTreeBuilder.insert(startTag);
        
        assertEquals("<tag attribute=\"value\"></tag>", doc.html());
        assertTrue(element.hasAttr("attribute"));
        assertEquals("value", element.attr("attribute"));
    }
    
    @Test
    public void insertCommentTest() {
        Token.Comment comment = new Token.Comment("Comment");
        Document doc = xmlTreeBuilder.getDocument();
        xmlTreeBuilder.insert(comment);
        
        assertEquals("<!--Comment-->", doc.html());
    }
    
    @Test
    public void insertCharacterTest() {
        Token.Character character = new Token.Character("Character");
        Document doc = xmlTreeBuilder.getDocument();
        xmlTreeBuilder.insert(character);
        
        assertEquals("Character", doc.html());
    }
    
    @Test
    public void insertDoctypeTest() {
        Token.Doctype doctype = new Token.Doctype("doctype", "public", "system");
        Document doc = xmlTreeBuilder.getDocument();
        xmlTreeBuilder.insert(doctype);
        
        assertEquals("<!DOCTYPE doctype PUBLIC \"public\" \"system\">", doc.html());
    }
    
    @Test
    public void popStackToCloseTest() {
        Token.StartTag startTag1 = new Token.StartTag("tag");
        xmlTreeBuilder.process(startTag1);
        
        Token.StartTag startTag2 = new Token.StartTag("tag");
        xmlTreeBuilder.process(startTag2);
        
        Token.EndTag endTag = new Token.EndTag("tag");
        xmlTreeBuilder.process(endTag);
        
        Document doc = xmlTreeBuilder.getDocument();
        
        assertEquals("<tag></tag>", doc.html());
    }
    
    @Test
    public void parseFragmentTest() {
        String inputFragment = "<tag>content</tag>";
        String baseUri = "http://example.com";
        List<Node> nodes = xmlTreeBuilder.parseFragment(inputFragment, baseUri, errors);
        
        assertEquals(1, nodes.size());
        assertTrue(nodes.get(0) instanceof Element);
        Element element = (Element) nodes.get(0);
        assertEquals("tag", element.nodeName());
        assertEquals("http://example.com", element.baseUri());
        assertEquals("content", element.text());
    }
}