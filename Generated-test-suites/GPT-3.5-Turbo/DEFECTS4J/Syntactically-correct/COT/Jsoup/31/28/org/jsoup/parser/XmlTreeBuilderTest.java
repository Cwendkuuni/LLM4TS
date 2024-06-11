
import org.jsoup.parser.*;
import org.junit.Assert;
import org.junit.Test;

public class XmlTreeBuilderTest {
    
    @Test
    public void testInitialiseParse() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        xmlTreeBuilder.initialiseParse("input", "baseUri", null);
        
        // Assert the correct initialization
        Assert.assertNotNull(xmlTreeBuilder.getStack());
        Assert.assertEquals(1, xmlTreeBuilder.getStack().size());
        Assert.assertEquals(xmlTreeBuilder.getDoc(), xmlTreeBuilder.getStack().get(0));
    }
    
    @Test
    public void testProcess_StartTag() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Token.StartTag startTag = new Token.StartTag("tag", new Attributes());
        xmlTreeBuilder.process(startTag);
        
        // Assert the correct insertion of the start tag
        Element el = xmlTreeBuilder.getStack().get(0);
        Assert.assertEquals(1, el.childNodes().size());
        Assert.assertEquals("tag", el.tagName());
    }
    
    @Test
    public void testProcess_EndTag() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Token.EndTag endTag = new Token.EndTag("tag");
        xmlTreeBuilder.process(endTag);
        
        // Assert the correct removal of the matching end tag from the stack
        Assert.assertTrue(xmlTreeBuilder.getStack().isEmpty());
    }
    
    @Test
    public void testProcess_Comment() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Token.Comment comment = new Token.Comment("comment");
        xmlTreeBuilder.process(comment);
        
        // Assert the correct insertion of the comment node
        Element el = xmlTreeBuilder.getStack().get(0);
        Assert.assertEquals(1, el.childNodes().size());
        Assert.assertTrue(el.childNode(0) instanceof Comment);
        Assert.assertEquals("comment", ((Comment) el.childNode(0)).getData());
    }
    
    @Test
    public void testProcess_Character() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Token.Character character = new Token.Character("data");
        xmlTreeBuilder.process(character);
        
        // Assert the correct insertion of the character node
        Element el = xmlTreeBuilder.getStack().get(0);
        Assert.assertEquals(1, el.childNodes().size());
        Assert.assertTrue(el.childNode(0) instanceof TextNode);
        Assert.assertEquals("data", ((TextNode) el.childNode(0)).getWholeText());
    }
    
    @Test
    public void testProcess_Doctype() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Token.Doctype doctype = new Token.Doctype("name", "publicIdentifier", "systemIdentifier");
        xmlTreeBuilder.process(doctype);
        
        // Assert the correct insertion of the doctype node
        Element el = xmlTreeBuilder.getStack().get(0);
        Assert.assertEquals(1, el.childNodes().size());
        Assert.assertTrue(el.childNode(0) instanceof DocumentType);
        DocumentType doctypeNode = (DocumentType) el.childNode(0);
        Assert.assertEquals("name", doctypeNode.getName());
        Assert.assertEquals("publicIdentifier", doctypeNode.getPublicIdentifier());
        Assert.assertEquals("systemIdentifier", doctypeNode.getSystemIdentifier());
    }
    
    @Test
    public void testInsertNode() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Node node = new Element("tag");
        xmlTreeBuilder.insertNode(node);
        
        // Assert the correct insertion of the node
        Element el = xmlTreeBuilder.getStack().get(0);
        Assert.assertEquals(1, el.childNodes().size());
        Assert.assertEquals(node, el.childNode(0));
    }
    
    @Test
    public void testInsert_StartTag() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Token.StartTag startTag = new Token.StartTag("tag", new Attributes());
        xmlTreeBuilder.insert(startTag);
        
        // Assert the correct insertion of the start tag
        Element el = xmlTreeBuilder.getStack().get(0);
        Assert.assertEquals(1, el.childNodes().size());
        Assert.assertEquals("tag", el.tagName());
    }
    
    @Test
    public void testInsert_Comment() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Token.Comment comment = new Token.Comment("comment");
        xmlTreeBuilder.insert(comment);
        
        // Assert the correct insertion of the comment node
        Element el = xmlTreeBuilder.getStack().get(0);
        Assert.assertEquals(1, el.childNodes().size());
        Assert.assertTrue(el.childNode(0) instanceof Comment);
        Assert.assertEquals("comment", ((Comment) el.childNode(0)).getData());
    }
    
    @Test
    public void testInsert_Character() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Token.Character character = new Token.Character("data");
        xmlTreeBuilder.insert(character);
        
        // Assert the correct insertion of the character node
        Element el = xmlTreeBuilder.getStack().get(0);
        Assert.assertEquals(1, el.childNodes().size());
        Assert.assertTrue(el.childNode(0) instanceof TextNode);
        Assert.assertEquals("data", ((TextNode) el.childNode(0)).getWholeText());
    }
    
    @Test
    public void testInsert_Doctype() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Token.Doctype doctype = new Token.Doctype("name", "publicIdentifier", "systemIdentifier");
        xmlTreeBuilder.insert(doctype);
        
        // Assert the correct insertion of the doctype node
        Element el = xmlTreeBuilder.getStack().get(0);
        Assert.assertEquals(1, el.childNodes().size());
        Assert.assertTrue(el.childNode(0) instanceof DocumentType);
        DocumentType doctypeNode = (DocumentType) el.childNode(0);
        Assert.assertEquals("name", doctypeNode.getName());
        Assert.assertEquals("publicIdentifier", doctypeNode.getPublicIdentifier());
        Assert.assertEquals("systemIdentifier", doctypeNode.getSystemIdentifier());
    }
    
    @Test
    public void testPopStackToClose() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Token.EndTag endTag = new Token.EndTag("tag");
        xmlTreeBuilder.insert(new Token.StartTag("div", new Attributes()));
        xmlTreeBuilder.insert(new Token.StartTag("tag", new Attributes()));
        xmlTreeBuilder.insert(new Token.StartTag("p", new Attributes()));
        xmlTreeBuilder.insert(new Token.StartTag("tag", new Attributes()));
        xmlTreeBuilder.insert(new Token.StartTag("span", new Attributes()));
        xmlTreeBuilder.process(endTag);
        
        // Assert the correct removal of the first occurrence of the end tag from the stack
        Assert.assertEquals(2, xmlTreeBuilder.getStack().size());
        Assert.assertEquals("div", xmlTreeBuilder.getStack().get(0).tagName());
        Assert.assertEquals("tag", xmlTreeBuilder.getStack().get(1).tagName());
    }
}

