import static org.junit.Assert.*;
import org.junit.*;

public class XmlTreeBuilderTest {
    
    private XmlTreeBuilder xmlTreeBuilder;
    private String input;
    private String baseUri;
    private ParseErrorList errors;
    
    @Before
    public void setUp() {
        xmlTreeBuilder = new XmlTreeBuilder();
        input = "";
        baseUri = "";
        errors = new ParseErrorList();
    }
    
    // Test case for initialiseParse method
    @Test
    public void testInitialiseParse() {
        xmlTreeBuilder.initialiseParse(input, baseUri, errors);
        
        assertEquals(baseUri, xmlTreeBuilder.getBaseUri());
        assertEquals(errors, xmlTreeBuilder.getErrors());
        assertEquals(1, xmlTreeBuilder.getStack().size());
        assertEquals(xmlTreeBuilder.getDoc(), xmlTreeBuilder.getStack().get(0));
        assertEquals(Document.OutputSettings.Syntax.xml, xmlTreeBuilder.getDoc().outputSettings().syntax());
    }
    
    // Test case for process method with StartTag token
    @Test
    public void testProcess_StartTag() {
        Token.StartTag startTag = new Token.StartTag("tag", "attr1=val1");
        
        boolean result = xmlTreeBuilder.process(startTag);
        
        assertTrue(result);
        assertEquals(1, xmlTreeBuilder.getStack().size());
        assertEquals(startTag.name(), xmlTreeBuilder.currentElement().tagName());
        assertEquals(startTag.attributes(), xmlTreeBuilder.currentElement().attributes());
    }
    
    // Test case for process method with EndTag token
    @Test
    public void testProcess_EndTag() {
        Element element1 = new Element("tag1");
        Element element2 = new Element("tag2");
        Element element3 = new Element("tag3");
        
        xmlTreeBuilder.getStack().add(element1);
        xmlTreeBuilder.getStack().add(element2);
        xmlTreeBuilder.getStack().add(element3);
        
        Token.EndTag endTag = new Token.EndTag("tag2");
        
        boolean result = xmlTreeBuilder.process(endTag);
        
        assertTrue(result);
        assertEquals(2, xmlTreeBuilder.getStack().size());
        assertFalse(xmlTreeBuilder.getStack().contains(element2));
        assertTrue(xmlTreeBuilder.getStack().contains(element1));
        assertTrue(xmlTreeBuilder.getStack().contains(element3));
    }
    
    // Test case for process method with Comment token
    @Test
    public void testProcess_Comment() {
        Token.Comment comment = new Token.Comment("test comment");
        
        xmlTreeBuilder.process(comment);
        
        assertEquals(1, xmlTreeBuilder.currentElement().childNodeSize());
        assertTrue(xmlTreeBuilder.currentElement().childNode(0) instanceof Comment);
        assertEquals(comment.getData(), xmlTreeBuilder.currentElement().childNode(0).toString());
    }
    
    // Test case for process method with Character token
    @Test
    public void testProcess_Character() {
        Token.Character character = new Token.Character("test character");
        
        xmlTreeBuilder.process(character);
        
        assertEquals(1, xmlTreeBuilder.currentElement().childNodeSize());
        assertTrue(xmlTreeBuilder.currentElement().childNode(0) instanceof TextNode);
        assertEquals(character.getData(), xmlTreeBuilder.currentElement().childNode(0).toString());
    }
    
    // Test case for process method with Doctype token
    @Test
    public void testProcess_Doctype() {
        Token.Doctype doctype = new Token.Doctype("html", "publicId", "systemId");
        
        xmlTreeBuilder.process(doctype);
        
        assertEquals(1, xmlTreeBuilder.currentElement().childNodeSize());
        assertTrue(xmlTreeBuilder.currentElement().childNode(0) instanceof DocumentType);
        assertEquals(doctype.getName(), ((DocumentType) xmlTreeBuilder.currentElement().childNode(0)).name());
        assertEquals(doctype.getPublicIdentifier(), ((DocumentType) xmlTreeBuilder.currentElement().childNode(0)).publicIdentifier());
        assertEquals(doctype.getSystemIdentifier(), ((DocumentType) xmlTreeBuilder.currentElement().childNode(0)).systemIdentifier());
    }
    
    // Test case for insertNode method when node is an Element
    @Test
    public void testInsertNode_Element() {
        Element element = new Element("tag");
        
        xmlTreeBuilder.insertNode(element);
        
        assertEquals(1, xmlTreeBuilder.currentElement().childNodeSize());
        assertEquals(element, xmlTreeBuilder.currentElement().childNode(0));
    }
    
    // Test case for insertNode method when node is a Comment
    @Test
    public void testInsertNode_Comment() {
        Comment comment = new Comment("test comment", "baseUri");
        
        xmlTreeBuilder.insertNode(comment);
        
        assertEquals(1, xmlTreeBuilder.currentElement().childNodeSize());
        assertTrue(xmlTreeBuilder.currentElement().childNode(0) instanceof Comment);
        assertEquals(comment.getData(), xmlTreeBuilder.currentElement().childNode(0).toString());
    }
    
    // Test case for insertNode method when node is a Character
    @Test
    public void testInsertNode_Character() {
        TextNode textNode = new TextNode("test character", "baseUri");
        
        xmlTreeBuilder.insertNode(textNode);
        
        assertEquals(1, xmlTreeBuilder.currentElement().childNodeSize());
        assertTrue(xmlTreeBuilder.currentElement().childNode(0) instanceof TextNode);
        assertEquals(textNode.getWholeText(), xmlTreeBuilder.currentElement().childNode(0).toString());
    }
    
    // Test case for insertNode method when node is a Doctype
    @Test
    public void testInsertNode_Doctype() {
        DocumentType doctype = new DocumentType("html", "publicId", "systemId", "baseUri");
        
        xmlTreeBuilder.insertNode(doctype);
        
        assertEquals(1, xmlTreeBuilder.currentElement().childNodeSize());
        assertTrue(xmlTreeBuilder.currentElement().childNode(0) instanceof DocumentType);
        assertEquals(doctype.name(), ((DocumentType) xmlTreeBuilder.currentElement().childNode(0)).name());
        assertEquals(doctype.publicIdentifier(), ((DocumentType) xmlTreeBuilder.currentElement().childNode(0)).publicIdentifier());
        assertEquals(doctype.systemIdentifier(), ((DocumentType) xmlTreeBuilder.currentElement().childNode(0)).systemIdentifier());
    }
    
    // Test case for insert method with StartTag token
    @Test
    public void testInsert_StartTag() {
        Token.StartTag startTag1 = new Token.StartTag("tag1", "attr1=val1");
        Token.StartTag startTag2 = new Token.StartTag("tag2", "attr2=val2");
        
        Element element1 = xmlTreeBuilder.insert(startTag1);
        Element element2 = xmlTreeBuilder.insert(startTag2);
        
        assertEquals(2, xmlTreeBuilder.getStack().size());
        assertTrue(xmlTreeBuilder.getStack().contains(element1));
        assertTrue(xmlTreeBuilder.getStack().contains(element2));
        
        assertFalse(element1.isSelfClosing());
        assertTrue(element2.isSelfClosing());
        
        assertEquals(startTag1.name(), element1.tagName());
        assertEquals(startTag1.attributes(), element1.attributes());
        
        assertEquals(startTag2.name(), element2.tagName());
        assertEquals(startTag2.attributes(), element2.attributes());
    }
    
    // Test case for insert method with Comment token
    @Test
    public void testInsert_Comment() {
        Token.Comment comment = new Token.Comment("test comment");
        
        xmlTreeBuilder.insert(comment);
        
        assertEquals(1, xmlTreeBuilder.currentElement().childNodeSize());
        assertTrue(xmlTreeBuilder.currentElement().childNode(0) instanceof Comment);
        assertEquals(comment.getData(), xmlTreeBuilder.currentElement().childNode(0).toString());
    }
    
    // Test case for insert method with Character token
    @Test
    public void testInsert_Character() {
        Token.Character character = new Token.Character("test character");
        
        xmlTreeBuilder.insert(character);
        
        assertEquals(1, xmlTreeBuilder.currentElement().childNodeSize());
        assertTrue(xmlTreeBuilder.currentElement().childNode(0) instanceof TextNode);
        assertEquals(character.getData(), xmlTreeBuilder.currentElement().childNode(0).toString());
    }
    
    // Test case for insert method with Doctype token
    @Test
    public void testInsert_Doctype() {
        Token.Doctype doctype = new Token.Doctype("html", "publicId", "systemId");
        
        xmlTreeBuilder.insert(doctype);
        
        assertEquals(1, xmlTreeBuilder.currentElement().childNodeSize());
        assertTrue(xmlTreeBuilder.currentElement().childNode(0) instanceof DocumentType);
        assertEquals(doctype.getName(), ((DocumentType) xmlTreeBuilder.currentElement().childNode(0)).name());
        assertEquals(doctype.getPublicIdentifier(), ((DocumentType) xmlTreeBuilder.currentElement().childNode(0)).publicIdentifier());
        assertEquals(doctype.getSystemIdentifier(), ((DocumentType) xmlTreeBuilder.currentElement().childNode(0)).systemIdentifier());
    }
    
    // Test case for popStackToClose method when end tag is found in the stack
    @Test
    public void testPopStackToClose_TagFound() {
        Element element1 = new Element("tag1");
        Element element2 = new Element("tag2");
        Element element3 = new Element("tag3");
        
        xmlTreeBuilder.getStack().add(element1);
        xmlTreeBuilder.getStack().add(element2);
        xmlTreeBuilder.getStack().add(element3);
        
        Token.EndTag endTag = new Token.EndTag("tag2");
        
        xmlTreeBuilder.popStackToClose(endTag);
        
        assertEquals(2, xmlTreeBuilder.getStack().size());
        assertFalse(xmlTreeBuilder.getStack().contains(element2));
        assertTrue(xmlTreeBuilder.getStack().contains(element1));
        assertTrue(xmlTreeBuilder.getStack().contains(element3));
    }
    
    // Test case for popStackToClose method when end tag is not found in the stack
    @Test
    public void testPopStackToClose_TagNotFound() {
        Element element1 = new Element("tag1");
        Element element2 = new Element("tag2");
        Element element3 = new Element("tag3");
        
        xmlTreeBuilder.getStack().add(element1);
        xmlTreeBuilder.getStack().add(element2);
        xmlTreeBuilder.getStack().add(element3);
        
        Token.EndTag endTag = new Token.EndTag("tag4");
        
        xmlTreeBuilder.popStackToClose(endTag);
        
        assertEquals(3, xmlTreeBuilder.getStack().size());
        assertTrue(xmlTreeBuilder.getStack().contains(element1));
        assertTrue(xmlTreeBuilder.getStack().contains(element2));
        assertTrue(xmlTreeBuilder.getStack().contains(element3));
    }
    
    // Test case for parseFragment method
    @Test
    public void testParseFragment() {
        String inputFragment = "<tag>Content</tag>";
        
        Element element = new Element("tag");
        TextNode textNode = new TextNode("Content");
        element.appendChild(textNode);
        xmlTreeBuilder.getStack().add(element);
        
        List<Node> nodes = xmlTreeBuilder.parseFragment(inputFragment, baseUri, errors);
        
        assertEquals(1, nodes.size());
        assertEquals(element.toString(), nodes.get(0).toString());
        assertEquals(element.childNodes(), nodes.get(0).childNodes());
    }
}