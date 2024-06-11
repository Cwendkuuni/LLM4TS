import org.junit.*;
import static org.junit.Assert.*;

public class XmlTreeBuilderTest {

    @Test
    public void testInitialiseParse() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        builder.initialiseParse("<root></root>", "http://example.com", new ParseErrorList());
        assertEquals("http://example.com", builder.baseUri);

        // Test that the document is added to the stack
        assertFalse(builder.stack.isEmpty());
        assertEquals(builder.doc, builder.stack.get(0));
        
        // Test that the document's output settings are set to XML syntax
        assertEquals(Document.OutputSettings.Syntax.xml, builder.doc.outputSettings().syntax());
    }

    @Test
    public void testProcess_StartTag() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        builder.initialiseParse("<root></root>", "http://example.com", new ParseErrorList());

        // Test that a start tag is inserted correctly
        Token.StartTag startTag = new Token.StartTag("tag", new Attributes());
        builder.process(startTag);
        assertEquals(1, builder.doc.childNodeSize());
        assertEquals("tag", builder.doc.childNode(0).nodeName());
    }
    
    @Test
    public void testProcess_EndTag() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        builder.initialiseParse("<root></root>", "http://example.com", new ParseErrorList());

        // Add a start tag to the stack
        Token.StartTag startTag = new Token.StartTag("tag", new Attributes());
        builder.process(startTag);

        // Test that an end tag removes the corresponding element from the stack
        Token.EndTag endTag = new Token.EndTag("tag");
        builder.process(endTag);
        assertEquals(0, builder.stack.size());
    }

    @Test
    public void testProcess_Comment() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        builder.initialiseParse("<root></root>", "http://example.com", new ParseErrorList());

        // Test that a comment is inserted correctly
        Token.Comment comment = new Token.Comment("This is a comment");
        builder.process(comment);
        assertEquals(1, builder.doc.childNodeSize());
        assertEquals(NodeType.COMMENT, builder.doc.childNode(0).nodeType());
        assertEquals("This is a comment", ((Comment) builder.doc.childNode(0)).getData());
    }

    @Test
    public void testProcess_Character() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        builder.initialiseParse("<root></root>", "http://example.com", new ParseErrorList());

        // Test that a character is inserted correctly
        Token.Character character = new Token.Character("a");
        builder.process(character);
        assertEquals(1, builder.doc.childNodeSize());
        assertEquals(NodeType.TEXT, builder.doc.childNode(0).nodeType());
        assertEquals("a", ((TextNode) builder.doc.childNode(0)).getWholeText());
    }

    @Test
    public void testProcess_Doctype() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        builder.initialiseParse("<root></root>", "http://example.com", new ParseErrorList());

        // Test that a doctype is inserted correctly
        Token.Doctype doctype = new Token.Doctype("html");
        builder.process(doctype);
        assertEquals(1, builder.doc.childNodeSize());
        assertEquals(NodeType.DOCTYPE, builder.doc.childNode(0).nodeType());
        assertEquals("html", ((DocumentType) builder.doc.childNode(0)).getName());
    }

    @Test
    public void testInsertNode_Element() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        builder.initialiseParse("<root></root>", "http://example.com", new ParseErrorList());

        // Create an element
        Element element = new Element(Tag.valueOf("tag"), "http://example.com");

        // Test that the element is inserted correctly
        builder.insertNode(element);
        assertEquals(1, builder.doc.childNodeSize());
        assertEquals(NodeType.ELEMENT, builder.doc.childNode(0).nodeType());
        assertEquals("tag", builder.doc.childNode(0).nodeName());
    }

    @Test
    public void testInsert_StartTag() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        builder.initialiseParse("<root></root>", "http://example.com", new ParseErrorList());

        // Test that a start tag is inserted correctly
        Token.StartTag startTag = new Token.StartTag("tag", new Attributes());
        Element element = builder.insert(startTag);
        assertEquals(1, builder.doc.childNodeSize());
        assertEquals(NodeType.ELEMENT, builder.doc.childNode(0).nodeType());
        assertEquals("tag", builder.doc.childNode(0).nodeName());
        assertEquals("tag", element.nodeName());
    }

    @Test
    public void testInsert_SelfClosingTag() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        builder.initialiseParse("<root></root>", "http://example.com", new ParseErrorList());

        // Test that a self-closing tag is inserted correctly
        Attributes attributes = new Attributes();
        attributes.put("key", "value");
        Token.StartTag startTag = new Token.StartTag("tag", attributes);
        startTag.selfClosing = true;
        Element element = builder.insert(startTag);
        assertEquals(1, builder.doc.childNodeSize());
        assertEquals(NodeType.ELEMENT, builder.doc.childNode(0).nodeType());
        assertEquals("tag", builder.doc.childNode(0).nodeName());
        assertEquals("tag", element.nodeName());
        assertTrue(element.tag().isKnownTag());
        assertTrue(element.tag().isSelfClosing());
    }

    @Test
    public void testInsert_Comment() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        builder.initialiseParse("<root></root>", "http://example.com", new ParseErrorList());

        // Test that a comment is inserted correctly
        Token.Comment comment = new Token.Comment("This is a comment");
        builder.insert(comment);
        assertEquals(1, builder.doc.childNodeSize());
        assertEquals(NodeType.COMMENT, builder.doc.childNode(0).nodeType());
        assertEquals("This is a comment", ((Comment) builder.doc.childNode(0)).getData());
    }

    @Test
    public void testInsert_Character() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        builder.initialiseParse("<root></root>", "http://example.com", new ParseErrorList());

        // Test that a character is inserted correctly
        Token.Character character = new Token.Character("a");
        builder.insert(character);
        assertEquals(1, builder.doc.childNodeSize());
        assertEquals(NodeType.TEXT, builder.doc.childNode(0).nodeType());
        assertEquals("a", ((TextNode) builder.doc.childNode(0)).getWholeText());
    }

    @Test
    public void testInsert_Doctype() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        builder.initialiseParse("<root></root>", "http://example.com", new ParseErrorList());

        // Test that a doctype is inserted correctly
        Token.Doctype doctype = new Token.Doctype("html");
        builder.insert(doctype);
        assertEquals(1, builder.doc.childNodeSize());
        assertEquals(NodeType.DOCTYPE, builder.doc.childNode(0).nodeType());
        assertEquals("html", ((DocumentType) builder.doc.childNode(0)).getName());
    }

    @Test
    public void testPopStackToClose_ElementNotFound() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        builder.initialiseParse("<root></root>", "http://example.com", new ParseErrorList());

        // Create an end tag with a name that does not exist in the stack
        Token.EndTag endTag = new Token.EndTag("tag");

        // Test that no elements are removed from the stack
        int stackSize = builder.stack.size();
        builder.popStackToClose(endTag);
        assertEquals(stackSize, builder.stack.size());
    }

    @Test
    public void testPopStackToClose_ElementFound() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        builder.initialiseParse("<root></root>", "http://example.com", new ParseErrorList());

        // Add an element to the stack
        Element element = new Element(Tag.valueOf("tag"), "http://example.com");
        builder.stack.add(element);

        // Create an end tag with the name of the added element
        Token.EndTag endTag = new Token.EndTag("tag");

        // Test that the element is removed from the stack
        builder.popStackToClose(endTag);
        assertEquals(0, builder.stack.size());
    }

    @Test
    public void testParseFragment() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        List<Node> nodes = builder.parseFragment("<tag>content</tag>", "http://example.com", new ParseErrorList());

        // Test that a fragment is parsed correctly
        assertEquals(1, nodes.size());
        Node node = nodes.get(0);
        assertEquals(NodeType.ELEMENT, node.nodeType());
        assertEquals("tag", node.nodeName());
    }

}