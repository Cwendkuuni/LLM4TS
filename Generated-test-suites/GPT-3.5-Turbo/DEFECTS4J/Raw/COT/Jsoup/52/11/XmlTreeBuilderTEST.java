
import org.junit.*;
import org.jsoup.parser.*;
import org.jsoup.nodes.*;

import java.util.List;

public class XmlTreeBuilderTest {

    // Test case for initialiseParse method
    @Test
    public void testInitialiseParse() {
        String input = "<root>Hello World</root>";
        String baseUri = "http://example.com";
        List<ParseError> errors = new ArrayList<>();
        
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        xmlTreeBuilder.initialiseParse(input, baseUri, errors);
        
        // Assert statements to check the functionality of the method
        Assert.assertEquals(baseUri, xmlTreeBuilder.baseUri);
        Assert.assertEquals(errors, xmlTreeBuilder.errors);
        Assert.assertEquals(1, xmlTreeBuilder.stack.size());
        Assert.assertEquals("http://example.com", xmlTreeBuilder.stack.get(0).baseUri());
    }

    // Test case for process method with StartTag token
    @Test
    public void testProcessStartTag() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Token.StartTag token = new Token.StartTag("div", new Attributes());
        xmlTreeBuilder.process(token);
        
        // Assert statements to check the functionality of the method
        Assert.assertEquals(1, xmlTreeBuilder.stack.size());
        Assert.assertEquals("div", xmlTreeBuilder.stack.get(0).nodeName());
    }
    
    // Test case for process method with EndTag token
    @Test
    public void testProcessEndTag() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Token.EndTag token = new Token.EndTag("div");
        xmlTreeBuilder.stack.add(new Element("div"));
        xmlTreeBuilder.process(token);
        
        // Assert statements to check the functionality of the method
        Assert.assertEquals(0, xmlTreeBuilder.stack.size());
    }
    
    // Test case for process method with Comment token
    @Test
    public void testProcessComment() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Token.Comment token = new Token.Comment("This is a comment");
        xmlTreeBuilder.process(token);
        
        // Assert statements to check the functionality of the method
        Assert.assertEquals(1, xmlTreeBuilder.stack.size());
        Assert.assertEquals("<!--This is a comment-->", xmlTreeBuilder.stack.get(0).outerHtml());
    }
    
    // Test case for process method with Character token
    @Test
    public void testProcessCharacter() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Token.Character token = new Token.Character("Hello World");
        xmlTreeBuilder.process(token);
        
        // Assert statements to check the functionality of the method
        Assert.assertEquals(1, xmlTreeBuilder.stack.size());
        Assert.assertEquals("Hello World", xmlTreeBuilder.stack.get(0).text());
    }
    
    // Test case for process method with Doctype token
    @Test
    public void testProcessDoctype() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Token.Doctype token = new Token.Doctype("html");
        xmlTreeBuilder.process(token);
        
        // Assert statements to check the functionality of the method
        Assert.assertEquals(1, xmlTreeBuilder.stack.size());
        Assert.assertTrue(xmlTreeBuilder.stack.get(0) instanceof DocumentType);
        Assert.assertEquals("<!DOCTYPE html>", xmlTreeBuilder.stack.get(0).outerHtml());
    }
    
    // Test case for insertNode method
    @Test
    public void testInsertNode() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Element parent = new Element("div");
        Element child = new Element("p");
        xmlTreeBuilder.stack.add(parent);
        xmlTreeBuilder.insertNode(child);
        
        // Assert statements to check the functionality of the method
        Assert.assertEquals(1, parent.children().size());
        Assert.assertEquals("p", parent.children().get(0).nodeName());
    }
    
    // Test case for insert method with StartTag token
    @Test
    public void testInsertStartTag() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Token.StartTag token = new Token.StartTag("div", new Attributes());
        xmlTreeBuilder.stack.add(new Element("body"));
        xmlTreeBuilder.insert(token);
        
        // Assert statements to check the functionality of the method
        Assert.assertEquals(1, xmlTreeBuilder.stack.size());
        Assert.assertEquals("div", xmlTreeBuilder.stack.get(0).nodeName());
        Assert.assertEquals("body", xmlTreeBuilder.stack.get(0).parent().nodeName());
    }
    
    // Test case for insert method with Comment token
    @Test
    public void testInsertComment() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Token.Comment commentToken = new Token.Comment("This is a comment");
        xmlTreeBuilder.insert(commentToken);
        
        // Assert statements to check the functionality of the method
        Assert.assertEquals(1, xmlTreeBuilder.stack.size());
        Assert.assertEquals("<!--This is a comment-->", xmlTreeBuilder.stack.get(0).outerHtml());
    }
    
    // Test case for insert method with Character token
    @Test
    public void testInsertCharacter() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Token.Character characterToken = new Token.Character("Hello World");
        xmlTreeBuilder.insert(characterToken);
        
        // Assert statements to check the functionality of the method
        Assert.assertEquals(1, xmlTreeBuilder.stack.size());
        Assert.assertEquals("Hello World", xmlTreeBuilder.stack.get(0).text());
    }
    
    // Test case for insert method with Doctype token
    @Test
    public void testInsertDoctype() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Token.Doctype doctypeToken = new Token.Doctype("html");
        xmlTreeBuilder.insert(doctypeToken);
        
        // Assert statements to check the functionality of the method
        Assert.assertEquals(1, xmlTreeBuilder.stack.size());
        Assert.assertTrue(xmlTreeBuilder.stack.get(0) instanceof DocumentType);
        Assert.assertEquals("<!DOCTYPE html>", xmlTreeBuilder.stack.get(0).outerHtml());
    }
    
    // Test case for popStackToClose method
    @Test
    public void testPopStackToClose() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Element div1 = new Element("div");
        Element div2 = new Element("div");
        Element div3 = new Element("div");
        xmlTreeBuilder.stack.add(div1);
        xmlTreeBuilder.stack.add(div2);
        xmlTreeBuilder.stack.add(div3);
        Token.EndTag token = new Token.EndTag("div");
        xmlTreeBuilder.popStackToClose(token);
        
        // Assert statements to check the functionality of the method
        Assert.assertEquals(2, xmlTreeBuilder.stack.size());
        Assert.assertEquals(div3, xmlTreeBuilder.stack.get(0));
        Assert.assertEquals(div2, xmlTreeBuilder.stack.get(1));
    }
    
    // Test case for parseFragment method
    @Test
    public void testParseFragment() {
        String inputFragment = "<div>Hello World</div>";
        String baseUri = "http://example.com";
        List<ParseError> errors = new ArrayList<>();
        
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        List<Node> nodes = xmlTreeBuilder.parseFragment(inputFragment, baseUri, errors);
        
        // Assert statements to check the functionality of the method
        Assert.assertEquals(1, nodes.size());
        Assert.assertTrue(nodes.get(0) instanceof Element);
        Assert.assertEquals("div", nodes.get(0).nodeName());
        Assert.assertEquals("Hello World", ((Element) nodes.get(0)).text());
    }
    
}

