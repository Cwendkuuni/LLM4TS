
package org.jsoup.parser;

import org.jsoup.helper.Validate;
import org.jsoup.nodes.*;
import org.junit.Test;

import static org.junit.Assert.*;

import java.io.StringReader;
import java.util.List;

public class XmlTreeBuilderTest {

    @Test
    public void testDefaultSettings() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        ParseSettings settings = builder.defaultSettings();
        
        assertNotNull(settings);
        // Add more assertions for default settings here
    }
    
    @Test
    public void testParseReaderString() {
        String input = "<root><child1></child1><child2></child2></root>";
        StringReader reader = new StringReader(input);
        String baseUri = "http://example.com";
        
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Document document = builder.parse(reader, baseUri);
        
        assertNotNull(document);
        // Add more assertions for parsing from Reader with baseUri here
    }
    
    @Test
    public void testParseStringString() {
        String input = "<root><child1></child1><child2></child2></root>";
        String baseUri = "http://example.com";
        
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Document document = builder.parse(input, baseUri);
        
        assertNotNull(document);
        // Add more assertions for parsing from string with baseUri here
    }
    
    @Test
    public void testInitialiseParse() {
        String input = "<root><child1></child1><child2></child2></root>";
        StringReader reader = new StringReader(input);
        String baseUri = "http://example.com";
        ParseErrorList errors = ParseErrorList.noTracking();
        ParseSettings settings = ParseSettings.preserveCase;
        
        XmlTreeBuilder builder = new XmlTreeBuilder();
        builder.initialiseParse(reader, baseUri, errors, settings);
        
        // Add assertions for initialiseParse method here
    }
    
    @Test
    public void testProcess() {
        Token startTagToken = new Token.StartTag("child");
        Token endTagToken = new Token.EndTag("child");
        Token commentToken = new Token.Comment("test comment");
        Token characterToken = new Token.Character("test character");
        Token doctypeToken = new Token.Doctype("html");
        Token eofToken = new Token.EOF();
        
        XmlTreeBuilder builder = new XmlTreeBuilder();
        
        assertTrue(builder.process(startTagToken));
        assertTrue(builder.process(endTagToken));
        assertTrue(builder.process(commentToken));
        assertTrue(builder.process(characterToken));
        assertTrue(builder.process(doctypeToken));
        assertTrue(builder.process(eofToken));
        
        // Add assertions for process method here
    }
    
    @Test
    public void testInsertNode() {
        Node node = new Element("child");
        Element parentElement = new Element("parent");
        
        XmlTreeBuilder builder = new XmlTreeBuilder();
        builder.insertNode(node, parentElement);
        
        assertTrue(parentElement.childNodes().contains(node));
    }
    
    @Test
    public void testInsertStartTag() {
        Token.StartTag startTag = new Token.StartTag("child");
        startTag.attributes().put("attr1", "value1");
        startTag.attributes().put("attr2", "value2");
        
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Element element = builder.insert(startTag);
        
        assertNotNull(element);
        assertEquals("child", element.nodeName());
        assertTrue(element.attributes().containsKey("attr1"));
        assertEquals("value1", element.attributes().get("attr1"));
        assertTrue(element.attributes().containsKey("attr2"));
        assertEquals("value2", element.attributes().get("attr2"));
    }
    
    @Test
    public void testInsertComment() {
        Token.Comment commentToken = new Token.Comment("test comment");
        
        XmlTreeBuilder builder = new XmlTreeBuilder();
        builder.insert(commentToken);
        
        // Add assertions for insert comment here
    }
    
    @Test
    public void testInsertCharacter() {
        Token.Character characterToken = new Token.Character("test character");
        
        XmlTreeBuilder builder = new XmlTreeBuilder();
        builder.insert(characterToken);
        
        // Add assertions for insert character here
    }
    
    @Test
    public void testInsertDoctype() {
        Token.Doctype doctypeToken = new Token.Doctype("html");
        
        XmlTreeBuilder builder = new XmlTreeBuilder();
        builder.insert(doctypeToken);
        
        // Add assertions for insert doctype here
    }
    
    @Test
    public void testPopStackToClose() {
        Token.EndTag endTag = new Token.EndTag("child");
        Element element1 = new Element("child");
        Element element2 = new Element("child");
        Element element3 = new Element("parent");
        
        XmlTreeBuilder builder = new XmlTreeBuilder();
        builder.stack.add(element1);
        builder.stack.add(element2);
        builder.stack.add(element3);
        
        builder.popStackToClose(endTag);
        
        assertFalse(builder.stack.contains(element1));
        assertFalse(builder.stack.contains(element2));
        assertTrue(builder.stack.contains(element3));
    }
    
    @Test
    public void testParseFragment() {
        String inputFragment = "<child1></child1><child2></child2>";
        String baseUri = "http://example.com";
        ParseErrorList errors = ParseErrorList.noTracking();
        ParseSettings settings = ParseSettings.preserveCase;
        
        XmlTreeBuilder builder = new XmlTreeBuilder();
        List<Node> nodes = builder.parseFragment(inputFragment, baseUri, errors, settings);
        
        assertNotNull(nodes);
        // Add more assertions for parseFragment method here
    }
}
