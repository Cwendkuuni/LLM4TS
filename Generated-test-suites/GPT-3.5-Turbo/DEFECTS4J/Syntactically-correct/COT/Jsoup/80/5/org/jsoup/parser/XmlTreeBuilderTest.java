import org.jsoup.parser.*;
import org.jsoup.helper.Validate;
import org.jsoup.nodes.*;

import java.io.Reader;
import java.io.StringReader;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class XmlTreeBuilderTest {

    private XmlTreeBuilder xmlTreeBuilder;

    @Before
    public void setUp() {
        xmlTreeBuilder = new XmlTreeBuilder();
    }

    @Test
    public void testDefaultSettings() {
        ParseSettings settings = xmlTreeBuilder.defaultSettings();
        assertNotNull(settings);
    }

    @Test
    public void testParseWithReaderAndBaseUri() {
        Reader input = new StringReader("<xml></xml>");
        String baseUri = "http://example.com";
        Document doc = xmlTreeBuilder.parse(input, baseUri);
        assertNotNull(doc);
    }

    @Test
    public void testParseWithStringAndBaseUri() {
        String input = "<xml></xml>";
        String baseUri = "http://example.com";
        Document doc = xmlTreeBuilder.parse(input, baseUri);
        assertNotNull(doc);
    }

    @Test
    public void testInitialiseParse() {
        Reader input = new StringReader("<xml></xml>");
        String baseUri = "http://example.com";
        ParseErrorList errors = new ParseErrorList();
        ParseSettings settings = xmlTreeBuilder.defaultSettings();

        xmlTreeBuilder.initialiseParse(input, baseUri, errors, settings);

        // Check that the document is added to the stack
        List<Node> stack = xmlTreeBuilder.getStack();
        assertEquals(1, stack.size());
        assertTrue(stack.get(0) instanceof Document);

        // Check that the document's output settings syntax is set to xml
        Document doc = (Document) stack.get(0);
        assertEquals(Document.OutputSettings.Syntax.xml, doc.outputSettings().syntax());
    }

    @Test
    public void testProcess() {
        Token startTagToken = new Token.StartTag("xml");
        xmlTreeBuilder.process(startTagToken);

        // Check that the start tag is inserted into the document
        List<Node> stack = xmlTreeBuilder.getStack();
        assertEquals(2, stack.size());
        assertTrue(stack.get(1) instanceof Element);
        Element el = (Element) stack.get(1);
        assertEquals("xml", el.tagName());
    }

    @Test
    public void testInsertNode() {
        Document doc = new Document("");
        xmlTreeBuilder.setDocument(doc);
        Element parent = doc.createElement("parent");
        Element child = doc.createElement("child");
        xmlTreeBuilder.setCurrentElement(parent);
        xmlTreeBuilder.insertNode(child);

        // Check that the child element is inserted into the parent element
        assertEquals(1, parent.childNodeSize());
        assertEquals(child, parent.childNode(0));
    }

    @Test
    public void testInsertStartTag() {
        Token.StartTag startTag = new Token.StartTag("xml");
        Element el = xmlTreeBuilder.insert(startTag);

        // Check that the start tag is inserted into the document
        List<Node> stack = xmlTreeBuilder.getStack();
        assertEquals(2, stack.size());
        assertTrue(stack.get(1) instanceof Element);
        assertEquals(el, stack.get(1));

        // Check that the element has the correct tag name and attributes
        assertEquals("xml", el.tagName());
        assertTrue(el.attributes().isEmpty());
    }

    @Test
    public void testInsertStartTagWithSelfClosing() {
        Token.StartTag startTag = new Token.StartTag("xml", true);
        Element el = xmlTreeBuilder.insert(startTag);

        // Check that the start tag is inserted into the document
        List<Node> stack = xmlTreeBuilder.getStack();
        assertEquals(1, stack.size()); // Document is not added to the stack
        assertTrue(el.parentNode() instanceof Document);
        assertTrue(el.isSelfClosing());

        // Check that the element has the correct tag name and attributes
        assertEquals("xml", el.tagName());
        assertTrue(el.attributes().isEmpty());
    }

    @Test
    public void testInsertComment() {
        Token.Comment commentToken = new Token.Comment("comment");
        xmlTreeBuilder.insert(commentToken);

        // Check that the comment is inserted into the document
        List<Node> stack = xmlTreeBuilder.getStack();
        assertEquals(2, stack.size());
        assertTrue(stack.get(1) instanceof Comment);
        Comment comment = (Comment) stack.get(1);
        assertEquals("comment", comment.getData());
    }

    @Test
    public void testInsertCharacterData() {
        Token.Character characterToken = new Token.Character("data");
        xmlTreeBuilder.insert(characterToken);

        // Check that the character data is inserted into the document
        List<Node> stack = xmlTreeBuilder.getStack();
        assertEquals(2, stack.size());
        assertTrue(stack.get(1) instanceof TextNode);
        TextNode textNode = (TextNode) stack.get(1);
        assertEquals("data", textNode.getWholeText());
    }

    @Test
    public void testInsertCData() {
        Token.Character characterToken = new Token.Character("data");
        characterToken.setCData(true);
        xmlTreeBuilder.insert(characterToken);

        // Check that the cdata section is inserted into the document
        List<Node> stack = xmlTreeBuilder.getStack();
        assertEquals(2, stack.size());
        assertTrue(stack.get(1) instanceof CDataNode);
        CDataNode cdataNode = (CDataNode) stack.get(1);
        assertEquals("data", cdataNode.getWholeText());
    }

    @Test
    public void testInsertDoctype() {
        Token.Doctype doctypeToken = new Token.Doctype("doctype");
        xmlTreeBuilder.insert(doctypeToken);

        // Check that the doctype is inserted into the document
        List<Node> stack = xmlTreeBuilder.getStack();
        assertEquals(2, stack.size());
        assertTrue(stack.get(1) instanceof DocumentType);
        DocumentType doctype = (DocumentType) stack.get(1);
        assertEquals("DOCTYPE html", doctype.toString());
    }

    @Test
    public void testPopStackToClose() {
        Token.StartTag startTag = new Token.StartTag("parent");
        xmlTreeBuilder.insert(startTag);

        Token.EndTag endTag = new Token.EndTag("child");
        xmlTreeBuilder.process(endTag);

        // Check that the child element is popped from the stack
        List<Node> stack = xmlTreeBuilder.getStack();
        assertEquals(1, stack.size());
        assertTrue(stack.get(0) instanceof Element);
        Element el = (Element) stack.get(0);
        assertEquals("parent", el.tagName());
    }

    @Test
    public void testParseFragment() {
        String inputFragment = "<xml></xml>";
        String baseUri = "http://example.com";
        ParseErrorList errors = new ParseErrorList();
        ParseSettings settings = xmlTreeBuilder.defaultSettings();

        List<Node> nodes = xmlTreeBuilder.parseFragment(inputFragment, baseUri, errors, settings);

        // Check that the fragment is parsed and returned as nodes
        assertEquals(1, nodes.size());
        assertTrue(nodes.get(0) instanceof Element);
        Element el = (Element) nodes.get(0);
        assertEquals("xml", el.tagName());
    }

    // Test for potential edge cases and exception handling scenarios
    // ...

}

