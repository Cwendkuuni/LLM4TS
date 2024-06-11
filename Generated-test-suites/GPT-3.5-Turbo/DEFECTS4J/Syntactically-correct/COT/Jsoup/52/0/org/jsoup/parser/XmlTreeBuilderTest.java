import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.jsoup.helper.Validate;
import org.jsoup.parser.*;

import java.util.List;

public class XmlTreeBuilderTest {

    private XmlTreeBuilder xmlTreeBuilder;

    @Before
    public void setUp() {
        xmlTreeBuilder = new XmlTreeBuilder();
    }

    @Test
    public void testInitialiseParse() {
        // Test if initialiseParse method sets the document onto the stack
        String input = "<root></root>";
        String baseUri = "";
        ParseErrorList errors = new ParseErrorList();
        
        xmlTreeBuilder.initialiseParse(input, baseUri, errors);
        
        Assert.assertEquals(1, xmlTreeBuilder.stack.size());
        Assert.assertEquals(xmlTreeBuilder.doc, xmlTreeBuilder.stack.get(0));
    }
    
    @Test
    public void testProcess() {
        // Test if process method correctly handles start tag token
        Token.StartTag startTag = new Token.StartTag();
        startTag.name("tag");
        
        boolean result = xmlTreeBuilder.process(startTag);
        
        Assert.assertTrue(result);
        Assert.assertEquals(1, xmlTreeBuilder.stack.size());
    }
    
    @Test
    public void testInsert() {
        // Test if insert method correctly inserts a comment node
        Token.Comment commentToken = new Token.Comment();
        commentToken.data("comment");
        
        xmlTreeBuilder.insert(commentToken);
        
        Assert.assertEquals(1, xmlTreeBuilder.currentElement().childNodes().size());
        Assert.assertEquals(NodeType.COMMENT, xmlTreeBuilder.currentElement().childNodes().get(0).nodeType());
        Assert.assertEquals("comment", ((Comment) xmlTreeBuilder.currentElement().childNodes().get(0)).getData());
    }
    
    @Test
    public void testInsertNode() {
        // Test if insertNode method correctly appends a node to the current element
        Token.Character characterToken = new Token.Character();
        characterToken.data("data");
        
        xmlTreeBuilder.insertNode(new TextNode(characterToken.getData(), "baseUri"));
        
        Assert.assertEquals(1, xmlTreeBuilder.currentElement().childNodes().size());
        Assert.assertEquals(NodeType.TEXT, xmlTreeBuilder.currentElement().childNodes().get(0).nodeType());
        Assert.assertEquals("data", xmlTreeBuilder.currentElement().childNodes().get(0).toString());
    }
    
    @Test
    public void testInsertNode_Doctype() {
        // Test if insertNode method correctly appends a document type node to the current element
        Token.Doctype doctype = new Token.Doctype("name", "pubId", "sysId");
        
        xmlTreeBuilder.insertNode(new DocumentType(doctype.getName(), doctype.getPublicIdentifier(), doctype.getSystemIdentifier(), "baseUri"));
        
        Assert.assertEquals(1, xmlTreeBuilder.currentElement().childNodes().size());
        Assert.assertEquals(NodeType.DOCUMENT_TYPE, xmlTreeBuilder.currentElement().childNodes().get(0).nodeType());
        DocumentType doctypeNode = (DocumentType) xmlTreeBuilder.currentElement().childNodes().get(0);
        Assert.assertEquals(doctype.getName(), doctypeNode.getName());
        Assert.assertEquals(doctype.getPublicIdentifier(), doctypeNode.getPublicIdentifier());
        Assert.assertEquals(doctype.getSystemIdentifier(), doctypeNode.getSystemIdentifier());
    }
    
    @Test
    public void testPopStackToClose() {
        // Test if popStackToClose method correctly removes the first occurrence of an element from the stack
        Element element = new Element(Tag.valueOf("tag"), "");
        xmlTreeBuilder.stack.add(element);
        xmlTreeBuilder.stack.add(new Element(Tag.valueOf("tag2"), ""));
        
        Token.EndTag endTag = new Token.EndTag();
        endTag.name("tag");
        
        xmlTreeBuilder.popStackToClose(endTag);
        
        Assert.assertEquals(1, xmlTreeBuilder.stack.size());
        Assert.assertEquals(Tag.valueOf("tag2"), xmlTreeBuilder.stack.get(0).tagName());
    }
    
    @Test
    public void testParseFragment() {
        // Test if parseFragment method correctly parses a fragment and returns the list of child nodes
        String inputFragment = "<tag>data</tag>";
        String baseUri = "";
        ParseErrorList errors = new ParseErrorList();
        
        List<Node> result = xmlTreeBuilder.parseFragment(inputFragment, baseUri, errors);
        
        Assert.assertEquals(1, result.size());
        Assert.assertEquals(NodeType.ELEMENT, result.get(0).nodeType());
        Assert.assertEquals("tag", ((Element) result.get(0)).tagName());
        Assert.assertEquals(1, result.get(0).childNodes().size());
        Assert.assertEquals(NodeType.TEXT, result.get(0).childNodes().get(0).nodeType());
        Assert.assertEquals("data", result.get(0).childNodes().get(0).toString());
    }
}