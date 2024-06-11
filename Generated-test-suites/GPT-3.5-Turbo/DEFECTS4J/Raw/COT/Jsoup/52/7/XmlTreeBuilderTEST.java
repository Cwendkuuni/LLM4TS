
import org.jsoup.Jsoup;
import org.jsoup.parser.ParseErrorList;
import org.jsoup.parser.Token;

import org.jsoup.nodes.Comment;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.DocumentType;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;

public class XmlTreeBuilderTest {
    // Test case for initialiseParse method
    @Test
    public void testInitialiseParse() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        String input = "<xml><tag>content</tag></xml>";
        String baseUri = "http://example.com";
        ParseErrorList errors = new ParseErrorList();
        
        builder.initialiseParse(input, baseUri, errors);
        
        Assert.assertEquals("XmlTreeBuilder should add the document to the stack", builder.getStack().get(0), builder.getDoc());
        Assert.assertEquals("XmlTreeBuilder should set the syntax of the document to xml", Document.OutputSettings.Syntax.xml, builder.getDoc().outputSettings().syntax());
    }
    
    // Test case for process method - StartTag token
    @Test
    public void testProcess_StartTag() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Token.StartTag startTag = new Token.StartTag("tag", null);
        
        Assert.assertTrue("XmlTreeBuilder should insert the start tag element", builder.process(startTag));
        Assert.assertEquals("XmlTreeBuilder should add the element to the stack", builder.getCurrentElement(), builder.getStack().get(builder.getStack().size() - 1));
    }
    
    // Test case for process method - EndTag token
    @Test
    public void testProcess_EndTag() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Token.EndTag endTag = new Token.EndTag("tag");
        
        Assert.assertTrue("XmlTreeBuilder should pop the stack elements", builder.process(endTag));
        Assert.assertFalse("XmlTreeBuilder stack should not contain the popped element", builder.getStack().contains(builder.getCurrentElement()));
    }
    
    // Test case for process method - Comment token
    @Test
    public void testProcess_Comment() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Token.Comment commentToken = new Token.Comment("This is a comment");
        
        Assert.assertTrue("XmlTreeBuilder should insert the comment as a comment node", builder.process(commentToken));
        Assert.assertTrue("XmlTreeBuilder should append the comment node to the current element", builder.getCurrentElement().childNode(0) instanceof Comment);
    }
    
    // Test case for process method - Character token
    @Test
    public void testProcess_Character() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Token.Character characterToken = new Token.Character("content");
        
        Assert.assertTrue("XmlTreeBuilder should insert the character data as a text node", builder.process(characterToken));
        Assert.assertTrue("XmlTreeBuilder should append the text node to the current element", builder.getCurrentElement().childNode(0) instanceof TextNode);
    }
    
    // Test case for process method - Doctype token
    @Test
    public void testProcess_Doctype() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Token.Doctype doctypeToken = new Token.Doctype("html", "publicId", "systemId");
        
        Assert.assertTrue("XmlTreeBuilder should insert the doctype as a document type node", builder.process(doctypeToken));
        Assert.assertTrue("XmlTreeBuilder should append the document type node to the current element", builder.getCurrentElement().childNode(0) instanceof DocumentType);
    }
    
    // Test case for insertNode method - Element node
    @Test
    public void testInsertNode_Element() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Element element = new Element("tag");
        builder.getStack().add(new Element("parent"));
        builder.insertNode(element);
        
        Assert.assertTrue("XmlTreeBuilder should append the element to the current element", builder.getStack().get(builder.getStack().size() - 1).childNode(0) instanceof Element);
    }
    
    // Test case for insertNode method - Comment node
    @Test
    public void testInsertNode_Comment() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Comment comment = new Comment("This is a comment");
        builder.getStack().add(new Element("parent"));
        builder.insertNode(comment);
        
        Assert.assertTrue("XmlTreeBuilder should append the comment to the current element", builder.getStack().get(builder.getStack().size() - 1).childNode(0) instanceof Comment);
    }
    
    // Test case for insertNode method - TextNode node
    @Test
    public void testInsertNode_TextNode() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        TextNode textNode = new TextNode("content");
        builder.getStack().add(new Element("parent"));
        builder.insertNode(textNode);
        
        Assert.assertTrue("XmlTreeBuilder should append the text node to the current element", builder.getStack().get(builder.getStack().size() - 1).childNode(0) instanceof TextNode);
    }
    
    // Test case for insertNode method - DocumentType node
    @Test
    public void testInsertNode_DocumentType() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        DocumentType doctype = new DocumentType("html", "publicId", "systemId");
        builder.getStack().add(new Element("parent"));
        builder.insertNode(doctype);
        
        Assert.assertTrue("XmlTreeBuilder should append the document type to the current element", builder.getStack().get(builder.getStack().size() - 1).childNode(0) instanceof DocumentType);
    }
    
    // Test case for insert method - StartTag token
    @Test
    public void testInsert_StartTag() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Token.StartTag startTag = new Token.StartTag("tag", null);
        
        Assert.assertNotNull("XmlTreeBuilder should return an element when inserting a start tag", builder.insert(startTag));
        Assert.assertTrue("XmlTreeBuilder should append the created element to the current element", builder.getCurrentElement().childNode(0) instanceof Element);
    }
    
    // Test case for insert method - Comment token (bogus)
    @Test
    public void testInsert_Comment_Bogus() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Token.Comment commentToken = new Token.Comment("<!DOCTYPE html>");
        
        Assert.assertTrue("XmlTreeBuilder should insert the comment as a comment node", builder.process(commentToken));
        Assert.assertTrue("XmlTreeBuilder should parse the comment as an XmlDeclaration", 
            builder.getCurrentElement().childNode(0) instanceof XmlDeclaration &&
            ((XmlDeclaration)builder.getCurrentElement().childNode(0)).getData().equals("DOCTYPE html") &&
            ((XmlDeclaration)builder.getCurrentElement().childNode(0)).getBaseUri().equals(builder.getBaseUri()));
    }
    
    // Test case for insert method - Character token
    @Test
    public void testInsert_Character() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Token.Character characterToken = new Token.Character("content");
        
        Assert.assertTrue("XmlTreeBuilder should insert the character data as a text node", builder.process(characterToken));
        Assert.assertEquals("XmlTreeBuilder should append the text node to the current element", "content", 
            ((TextNode)builder.getCurrentElement().childNode(0)).getWholeText());
    }
    
    // Test case for insert method - Doctype token
    @Test
    public void testInsert_Doctype() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Token.Doctype doctypeToken = new Token.Doctype("html", "publicId", "systemId");
        
        Assert.assertTrue("XmlTreeBuilder should insert the doctype as a document type node", builder.process(doctypeToken));
        DocumentType docType = (DocumentType)builder.getCurrentElement().childNode(0);
        Assert.assertEquals("XmlTreeBuilder should append the document type node to the current element", "html", docType.getName());
        Assert.assertEquals("XmlTreeBuilder should set the public identifier of the document type", "publicId", docType.getPublicIdentifier());
        Assert.assertEquals("XmlTreeBuilder should set the system identifier of the document type", "systemId", docType.getSystemIdentifier());
    }
    
    // Test case for popStackToClose method
    @Test
    public void testPopStackToClose() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Element element1 = new Element("tag");
        element1.appendChild(new Element("child1"));
        element1.appendChild(new Element("child2"));
        Element element2 = new Element("tag");
        element2.appendChild(new Element("child1"));
        
        builder.getStack().add(element1);
        builder.getStack().add(element2);
        builder.getStack().add(new Element("tag"));
        
        Token.EndTag endTag = new Token.EndTag("tag");
        builder.popStackToClose(endTag);
        
        Assert.assertEquals("XmlTreeBuilder should remove the first occurrence of the element from the stack", 1, builder.getStack().size());
    }
    
    // Test case for parseFragment method
    @Test
    public void testParseFragment() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        String input = "<tag>content</tag>";
        String baseUri = "http://example.com";
        ParseErrorList errors = new ParseErrorList();
        
        List<Node> nodes = builder.parseFragment(input, baseUri, errors);
        
        Assert.assertEquals("XmlTreeBuilder should return a list of child nodes of the parsed fragment", 1, nodes.size());
        Assert.assertTrue("XmlTreeBuilder should parse the input as an element", nodes.get(0) instanceof Element);
    }
}
