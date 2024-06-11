
import org.junit.Test;
import static org.junit.Assert.*;

public class XmlTreeBuilderTest {

    @Test
    public void testInitialiseParse() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        String input = "<root></root>";
        String baseUri = "http://example.com";
        ParseErrorList errors = new ParseErrorList();
        builder.initialiseParse(input, baseUri, errors);

        // Assert that the document is added to the stack
        assertEquals(1, builder.stack.size());
        assertEquals(builder.doc, builder.stack.get(0));

        // Assert that the syntax of the document is set to XML
        assertEquals(Document.OutputSettings.Syntax.xml, builder.doc.outputSettings().syntax());
    }

    @Test
    public void testProcess_StartTag() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Token.StartTag startTag = new Token.StartTag("tag", new Attributes(), false);
        builder.process(startTag);

        // Assert that the element is inserted to the current element's child nodes
        assertEquals(1, builder.currentElement().childNodes().size());
        assertTrue(builder.currentElement().childNodes().get(0) instanceof Element);
        assertEquals("tag", ((Element) builder.currentElement().childNodes().get(0)).nodeName());
    }

    @Test
    public void testProcess_EndTag() {
        XmlTreeBuilder builder = new XmlTreeBuilder();

        // Create a start tag to be inserted
        Token.StartTag startTag = new Token.StartTag("tag", new Attributes(), false);
        builder.process(startTag);

        // Create an end tag for the start tag
        Token.EndTag endTag = new Token.EndTag("tag");
        builder.process(endTag);

        // Assert that the element is removed from the stack
        assertTrue(builder.stack.isEmpty());
    }

    @Test
    public void testProcess_Comment() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Token.Comment commentToken = new Token.Comment("This is a comment");
        builder.process(commentToken);

        // Assert that the comment is inserted to the current element's child nodes
        assertEquals(1, builder.currentElement().childNodes().size());
        assertTrue(builder.currentElement().childNodes().get(0) instanceof Comment);
        assertEquals("This is a comment", ((Comment) builder.currentElement().childNodes().get(0)).getData());
    }

    @Test
    public void testProcess_Character() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Token.Character characterToken = new Token.Character("This is a character");
        builder.process(characterToken);

        // Assert that the character data is inserted to the current element's child nodes
        assertEquals(1, builder.currentElement().childNodes().size());
        assertTrue(builder.currentElement().childNodes().get(0) instanceof TextNode);
        assertEquals("This is a character", ((TextNode) builder.currentElement().childNodes().get(0)).getWholeText());
    }

    @Test
    public void testProcess_Doctype() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Token.Doctype doctype = new Token.Doctype("doctype", "", "", "");
        builder.process(doctype);

        // Assert that the doctype node is inserted to the current element's child nodes
        assertEquals(1, builder.currentElement().childNodes().size());
        assertTrue(builder.currentElement().childNodes().get(0) instanceof DocumentType);
        assertEquals("doctype", ((DocumentType) builder.currentElement().childNodes().get(0)).getName());
    }

    @Test
    public void testInsertNode_Element() {
        XmlTreeBuilder builder = new XmlTreeBuilder();

        // Create a parent element
        Element parentElement = new Element("parent");

        // Create a child element to be inserted
        Element childElement = new Element("child");

        builder.stack.add(parentElement);
        builder.insertNode(childElement);

        // Assert that the child element is added as a child to the parent element
        assertEquals(1, parentElement.childNodes().size());
        assertTrue(parentElement.childNodes().get(0) instanceof Element);
        assertEquals("child", ((Element) parentElement.childNodes().get(0)).nodeName());
    }

    @Test
    public void testInsert_Node_Comment() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Comment comment = new Comment("This is a comment", "http://example.com");
        builder.insertNode(comment);

        // Assert that the comment is inserted to the current element's child nodes
        assertEquals(1, builder.currentElement().childNodes().size());
        assertTrue(builder.currentElement().childNodes().get(0) instanceof Comment);
        assertEquals("This is a comment", ((Comment) builder.currentElement().childNodes().get(0)).getData());
    }

    @Test
    public void testInsertNode_Character() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        TextNode characterNode = new TextNode("This is a character", "http://example.com");
        builder.insertNode(characterNode);

        // Assert that the character data is inserted to the current element's child nodes
        assertEquals(1, builder.currentElement().childNodes().size());
        assertTrue(builder.currentElement().childNodes().get(0) instanceof TextNode);
        assertEquals("This is a character", ((TextNode) builder.currentElement().childNodes().get(0)).getWholeText());
    }

    @Test
    public void testInsertNode_Doctype() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        DocumentType doctypeNode = new DocumentType("doctype", "", "", "http://example.com");
        builder.insertNode(doctypeNode);

        // Assert that the doctype node is inserted to the current element's child nodes
        assertEquals(1, builder.currentElement().childNodes().size());
        assertTrue(builder.currentElement().childNodes().get(0) instanceof DocumentType);
        assertEquals("doctype", ((DocumentType) builder.currentElement().childNodes().get(0)).getName());
    }

    @Test
    public void testInsert_StartTag_NormalTag() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Token.StartTag startTag = new Token.StartTag("tag", new Attributes(), false);
        Element insertedElement = builder.insert(startTag);

        // Assert that the element is inserted to the current element's child nodes
        assertEquals(1, builder.currentElement().childNodes().size());
        assertTrue(builder.currentElement().childNodes().get(0) instanceof Element);
        assertEquals("tag", ((Element) builder.currentElement().childNodes().get(0)).nodeName());

        // Assert that the inserted element is returned
        assertEquals(insertedElement, builder.currentElement().childNodes().get(0));
    }

    @Test
    public void testInsert_StartTag_SelfClosingTag() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Token.StartTag startTag = new Token.StartTag("tag", new Attributes(), true);
        Element insertedElement = builder.insert(startTag);

        // Assert that the element is inserted to the current element's child nodes
        assertEquals(1, builder.currentElement().childNodes().size());
        assertTrue(builder.currentElement().childNodes().get(0) instanceof Element);
        assertEquals("tag", ((Element) builder.currentElement().childNodes().get(0)).nodeName());

        // Assert that the inserted element is returned
        assertEquals(insertedElement, builder.currentElement().childNodes().get(0));

        // Assert that the self-closing flag is acknowledged
        assertFalse(builder.tokeniser.isSelfClosing());
    }

    @Test
    public void testInsert_StartTag_UnknownTag() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Token.StartTag startTag = new Token.StartTag("unknown", new Attributes(), false);
        Element insertedElement = builder.insert(startTag);

        // Assert that the element is inserted to the current element's child nodes
        assertEquals(1, builder.currentElement().childNodes().size());
        assertTrue(builder.currentElement().childNodes().get(0) instanceof Element);
        assertEquals("unknown", ((Element) builder.currentElement().childNodes().get(0)).nodeName());

        // Assert that the inserted element is returned
        assertEquals(insertedElement, builder.currentElement().childNodes().get(0));

        // Assert that the tag is marked as self-closing for unknown tags
        assertTrue(insertedElement.tag().isSelfClosing());
    }

    @Test
    public void testInsert_Comment() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Token.Comment commentToken = new Token.Comment("This is a comment");
        builder.insert(commentToken);

        // Assert that the comment is inserted to the current element's child nodes
        assertEquals(1, builder.currentElement().childNodes().size());
        assertTrue(builder.currentElement().childNodes().get(0) instanceof Comment);
        assertEquals("This is a comment", ((Comment) builder.currentElement().childNodes().get(0)).getData());
    }

    @Test
    public void testInsert_Character() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Token.Character characterToken = new Token.Character("This is a character");
        builder.insert(characterToken);

        // Assert that the character data is inserted to the current element's child nodes
        assertEquals(1, builder.currentElement().childNodes().size());
        assertTrue(builder.currentElement().childNodes().get(0) instanceof TextNode);
        assertEquals("This is a character", ((TextNode) builder.currentElement().childNodes().get(0)).getWholeText());
    }

    @Test
    public void testInsert_Doctype() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Token.Doctype doctype = new Token.Doctype("doctype", "", "", "");
        builder.insert(doctype);

        // Assert that the doctype node is inserted to the current element's child nodes
        assertEquals(1, builder.currentElement().childNodes().size());
        assertTrue(builder.currentElement().childNodes().get(0) instanceof DocumentType);
        assertEquals("doctype", ((DocumentType) builder.currentElement().childNodes().get(0)).getName());
    }

    @Test
    public void testPopStackToClose_ElementFoundOnStack() {
        XmlTreeBuilder builder = new XmlTreeBuilder();

        // Create a parent element and a child element to be added to the stack
        Element parentElement = new Element("parent");
        Element childElement = new Element("child");

        builder.stack.add(parentElement);
        builder.stack.add(childElement);

        // Create an end tag for the child element
        Token.EndTag endTag = new Token.EndTag("child");

        // Call the popStackToClose() method
        builder.popStackToClose(endTag);

        // Assert that the child element is removed from the stack
        assertEquals(1, builder.stack.size());
        assertEquals(parentElement, builder.stack.get(0));
    }

    @Test
    public void testPopStackToClose_ElementNotFoundOnStack() {
        XmlTreeBuilder builder = new XmlTreeBuilder();

        // Create a parent element and a child element to be added to the stack
        Element parentElement = new Element("parent");
        Element childElement = new Element("child");

        builder.stack.add(parentElement);
        builder.stack.add(childElement);

        // Create an end tag for a different element
        Token.EndTag endTag = new Token.EndTag("other");

        // Call the popStackToClose() method
        builder.popStackToClose(endTag);

        // Assert that no elements are removed from the stack
        assertEquals(2, builder.stack.size());
        assertEquals(parentElement, builder.stack.get(0));
        assertEquals(childElement, builder.stack.get(1));
    }

    @Test
    public void testParseFragment() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        String inputFragment = "<tag></tag>";
        String baseUri = "http://example.com";
        ParseErrorList errors = new ParseErrorList();
        List<Node> nodes = builder.parseFragment(inputFragment, baseUri, errors);

        // Assert that the fragment is parsed and returned as a list of nodes
        assertEquals(1, nodes.size());
        assertTrue(nodes.get(0) instanceof Element);
        assertEquals("tag", ((Element) nodes.get(0)).nodeName());
    }
}
