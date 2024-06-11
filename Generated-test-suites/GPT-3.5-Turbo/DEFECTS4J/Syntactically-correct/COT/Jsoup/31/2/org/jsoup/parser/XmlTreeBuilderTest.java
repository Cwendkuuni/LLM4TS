
import org.junit.Test;
import static org.junit.Assert.*;

public class XmlTreeBuilderTest {
    
    @Test
    public void testInitialiseParse() {
        // Test case for initialiseParse() method
        
        // create an instance of XmlTreeBuilder
        XmlTreeBuilder builder = new XmlTreeBuilder();
        
        // initialize input, baseUri, and errors variables
        String input = "<root></root>";
        String baseUri = "http://www.example.com";
        ParseErrorList errors = new ParseErrorList();
        
        // call initialiseParse() method
        builder.initialiseParse(input, baseUri, errors);
        
        // check if stack has the document at the top
        assertEquals(builder.stack.get(0), builder.doc);
    }
    
    @Test
    public void testProcess_StartTag() {
        // Test case for process() method when token type is StartTag
        
        // create an instance of XmlTreeBuilder
        XmlTreeBuilder builder = new XmlTreeBuilder();
        
        // create a StartTag token
        Token startTag = new Token.StartTag("div", new Attributes());
        
        // call process() method
        boolean result = builder.process(startTag);
        
        // check if the start tag is inserted into the element
        assertEquals("div", builder.stack.get(0).nodeName());
        assertTrue(result);
    }
    
    @Test
    public void testProcess_EndTag() {
        // Test case for process() method when token type is EndTag
        
        // create an instance of XmlTreeBuilder
        XmlTreeBuilder builder = new XmlTreeBuilder();
        
        // add a start tag to the stack
        Element div = new Element(Tag.valueOf("div"), "http://www.example.com");
        builder.stack.add(div);
        
        // create an EndTag token
        Token endTag = new Token.EndTag("div");
        
        // call process() method
        boolean result = builder.process(endTag);
        
        // check if the end tag is removed from the stack
        assertEquals(0, builder.stack.size());
        assertTrue(result);
    }
    
    @Test
    public void testProcess_Comment() {
        // Test case for process() method when token type is Comment
        
        // create an instance of XmlTreeBuilder
        XmlTreeBuilder builder = new XmlTreeBuilder();
        
        // create a Comment token
        Token.Comment comment = new Token.Comment("sample comment");
        
        // call process() method
        boolean result = builder.process(comment);
        
        // check if the comment is inserted into the document
        assertTrue(builder.doc.childNodes().get(0) instanceof Comment);
        assertTrue(result);
    }
    
    @Test
    public void testProcess_Character() {
        // Test case for process() method when token type is Character
        
        // create an instance of XmlTreeBuilder
        XmlTreeBuilder builder = new XmlTreeBuilder();
        
        // create a Character token
        Token.Character character = new Token.Character("sample character");
        
        // call process() method
        boolean result = builder.process(character);
        
        // check if the character is inserted into the document
        assertTrue(builder.doc.childNodes().get(0) instanceof TextNode);
        assertTrue(result);
    }
    
    @Test
    public void testProcess_Doctype() {
        // Test case for process() method when token type is Doctype
        
        // create an instance of XmlTreeBuilder
        XmlTreeBuilder builder = new XmlTreeBuilder();
        
        // create a Doctype token
        Token.Doctype doctype = new Token.Doctype("html", "", "", "http://www.example.com");
        
        // call process() method
        boolean result = builder.process(doctype);
        
        // check if the doctype is inserted into the document
        assertTrue(builder.doc.childNodes().get(0) instanceof DocumentType);
        assertTrue(result);
    }
    
    @Test
    public void testInsert_StartTag() {
        // Test case for insert() method with StartTag token
        
        // create an instance of XmlTreeBuilder
        XmlTreeBuilder builder = new XmlTreeBuilder();
        
        // create a StartTag token
        Token.StartTag startTag = new Token.StartTag("div", new Attributes());
        
        // call insert() method
        Element element = builder.insert(startTag);
        
        // check if the start tag is inserted into the element
        assertEquals(startTag.name(), element.nodeName());
        assertEquals(builder.doc.childNodes().get(0), element);
        
        // check if the element is added to the stack
        assertEquals(1, builder.stack.size());
        assertEquals(element, builder.stack.get(0));
    }
    
    @Test
    public void testInsert_Comment() {
        // Test case for insert() method with Comment token
        
        // create an instance of XmlTreeBuilder
        XmlTreeBuilder builder = new XmlTreeBuilder();
        
        // create a Comment token
        Token.Comment comment = new Token.Comment("sample comment");
        
        // call insert() method
        builder.insert(comment);
        
        // check if the comment is inserted into the document
        assertTrue(builder.doc.childNodes().get(0) instanceof Comment);
    }
    
    @Test
    public void testInsert_Character() {
        // Test case for insert() method with Character token
        
        // create an instance of XmlTreeBuilder
        XmlTreeBuilder builder = new XmlTreeBuilder();
        
        // create a Character token
        Token.Character character = new Token.Character("sample character");
        
        // call insert() method
        builder.insert(character);
        
        // check if the character is inserted into the document
        assertTrue(builder.doc.childNodes().get(0) instanceof TextNode);
    }
    
    @Test
    public void testInsert_Doctype() {
        // Test case for insert() method with Doctype token
        
        // create an instance of XmlTreeBuilder
        XmlTreeBuilder builder = new XmlTreeBuilder();
        
        // create a Doctype token
        Token.Doctype doctype = new Token.Doctype("html", "", "", "http://www.example.com");
        
        // call insert() method
        builder.insert(doctype);
        
        // check if the doctype is inserted into the document
        assertTrue(builder.doc.childNodes().get(0) instanceof DocumentType);
    }
    
    @Test
    public void testPopStackToClose() {
        // Test case for popStackToClose() method
        
        // create an instance of XmlTreeBuilder
        XmlTreeBuilder builder = new XmlTreeBuilder();
        
        // add elements to the stack
        Element div1 = new Element(Tag.valueOf("div"), "http://www.example.com");
        Element div2 = new Element(Tag.valueOf("div"), "http://www.example.com");
        builder.stack.add(div1);
        builder.stack.add(div2);
        
        // create an EndTag token
        Token.EndTag endTag = new Token.EndTag("div");
        
        // call popStackToClose() method
        builder.popStackToClose(endTag);
        
        // check if the first occurrence of "div" in the stack is removed
        assertEquals(1, builder.stack.size());
        assertEquals(div2, builder.stack.get(0));
    }
    
    @Test
    public void testInsertNode() {
        // Test case for insertNode() method
        
        // create an instance of XmlTreeBuilder
        XmlTreeBuilder builder = new XmlTreeBuilder();
        
        // add an element to the stack
        Element div = new Element(Tag.valueOf("div"), "http://www.example.com");
        builder.stack.add(div);
        
        // create a node
        Node node = new Comment("sample comment", "http://www.example.com");
        
        // call insertNode() method
        builder.insertNode(node);
        
        // check if the node is inserted into the current element
        assertEquals(node, builder.currentElement().childNodes().get(0));
    }
    
}
