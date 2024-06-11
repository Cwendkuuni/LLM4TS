import org.jsoup.nodes.*;
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
    public void testInitialiseParse() {
        // Typical case
        StringReader input = new StringReader("<xml></xml>");
        String baseUri = "https://example.com";
        Parser parser = new Parser(xmlTreeBuilder);
        xmlTreeBuilder.initialiseParse(input, baseUri, parser);
        // Assert stack size is 2 (document and root element)
        assertEquals(2, xmlTreeBuilder.stack.size());

        // Edge case - empty input
        input = new StringReader("");
        xmlTreeBuilder.initialiseParse(input, baseUri, parser);
        // Assert stack size is 2 (document and root element)
        assertEquals(2, xmlTreeBuilder.stack.size());

        // Error scenario - null input
        input = null;
        try {
            xmlTreeBuilder.initialiseParse(input, baseUri, parser);
            fail("NullPointerException should have been thrown");
        } catch (NullPointerException e) {
            // Expected exception thrown, test passed
        }
    }

    @Test
    public void testParse() {
        // Typical case
        StringReader input = new StringReader("<xml></xml>");
        String baseUri = "https://example.com";
        Document document = xmlTreeBuilder.parse(input, baseUri);
        // Assert document has one child node
        assertEquals(1, document.childNodes().size());

        // Edge case - empty input
        input = new StringReader("");
        document = xmlTreeBuilder.parse(input, baseUri);
        // Assert document has no child nodes
        assertTrue(document.childNodes().isEmpty());

        // Error scenario - null input
        input = null;
        try {
            xmlTreeBuilder.parse(input, baseUri);
            fail("NullPointerException should have been thrown");
        } catch (NullPointerException e) {
            // Expected exception thrown, test passed
        }
    }

    @Test
    public void testParse_String() {
        // Typical case
        String input = "<xml></xml>";
        String baseUri = "https://example.com";
        Document document = xmlTreeBuilder.parse(input, baseUri);
        // Assert document has one child node
        assertEquals(1, document.childNodes().size());

        // Edge case - empty input
        input = "";
        document = xmlTreeBuilder.parse(input, baseUri);
        // Assert document has no child nodes
        assertTrue(document.childNodes().isEmpty());

        // Error scenario - null input
        input = null;
        try {
            xmlTreeBuilder.parse(input, baseUri);
            fail("NullPointerException should have been thrown");
        } catch (NullPointerException e) {
            // Expected exception thrown, test passed
        }
    }

    @Test
    public void testProcess() {
        // Test specific scenarios of the process() method not covered by other methods

        // StartTag case
        Token.StartTag startTag = new Token.StartTag("");
        Element element = xmlTreeBuilder.insert(startTag);
        // Assert the element is inserted and stack size is increased by 1
        assertEquals(1, element.siblingIndex());
        assertEquals(1, xmlTreeBuilder.stack.size());

        // EndTag case
        Token.EndTag endTag = new Token.EndTag("");
        xmlTreeBuilder.popStackToClose(endTag);
        // Assert stack size is decreased by 1
        assertEquals(0, xmlTreeBuilder.stack.size());

        // Comment case
        Token.Comment comment = new Token.Comment("");
        xmlTreeBuilder.insert(comment);
        // Assert document has one child node
        assertEquals(1, xmlTreeBuilder.doc.childNodes().size());

        // Character case
        Token.Character character = new Token.Character("");
        xmlTreeBuilder.insert(character);
        // Assert document has one child node
        assertEquals(1, xmlTreeBuilder.doc.childNodes().size());

        // Doctype case
        Token.Doctype doctype = new Token.Doctype("");
        xmlTreeBuilder.insert(doctype);
        // Assert document has one child node
        assertEquals(1, xmlTreeBuilder.doc.childNodes().size());

        // EOF case
        Token.EOF eof = new Token.EOF();
        xmlTreeBuilder.process(eof);
        // Assert document has no child nodes
        assertTrue(xmlTreeBuilder.doc.childNodes().isEmpty());
    }

    @Test
    public void testInsertNode() {
        // Test inserting various node types

        // TextNode case
        xmlTreeBuilder.currentElement().appendChild(new TextNode("text"));
        // Assert current element has one child node
        assertEquals(1, xmlTreeBuilder.currentElement().childNodeSize());

        // Comment case
        xmlTreeBuilder.currentElement().appendChild(new Comment("comment"));
        // Assert current element has two child nodes
        assertEquals(2, xmlTreeBuilder.currentElement().childNodeSize());

        // CDataNode case
        xmlTreeBuilder.currentElement().appendChild(new CDataNode("cdata"));
        // Assert current element has three child nodes
        assertEquals(3, xmlTreeBuilder.currentElement().childNodeSize());

        // XmlDeclaration case
        xmlTreeBuilder.currentElement().appendChild(new XmlDeclaration("version", "encoding", "data"));
        // Assert current element has four child nodes
        assertEquals(4, xmlTreeBuilder.currentElement().childNodeSize());

        // Element case
        Element element = new Element(Tag.valueOf("name"), "baseUri", xmlTreeBuilder.defaultSettings());
        xmlTreeBuilder.currentElement().appendChild(element);
        // Assert current element has five child nodes
        assertEquals(5, xmlTreeBuilder.currentElement().childNodeSize());
    }

    @Test
    public void testInsert_StartTag() {
        // Typical case
        Token.StartTag startTag = new Token.StartTag("xml");
        Element element = xmlTreeBuilder.insert(startTag);
        // Assert element is inserted and stack size is increased by 1
        assertEquals("xml", element.tagName());
        assertEquals(1, element.siblingIndex());
        assertEquals(1, xmlTreeBuilder.stack.size());

        // Edge case - self closing tag
        startTag.setSelfClosing();
        element = xmlTreeBuilder.insert(startTag);
        // Assert self closing tag is identified correctly and stack size remains the same
        assertTrue(element.tag().isSelfClosing());
        assertEquals(1, xmlTreeBuilder.stack.size());

        // Error scenario - unknown tag
        startTag.setName("unknown");
        element = xmlTreeBuilder.insert(startTag);
        // Assert unknown tag is inserted and stack size is increased by 1
        assertFalse(element.tag().isKnownTag());
        assertEquals(2, xmlTreeBuilder.stack.size());
    }

    @Test
    public void testInsert_Comment() {
        // Typical case
        Token.Comment commentToken = new Token.Comment("comment");
        xmlTreeBuilder.insert(commentToken);
        // Assert document has one child node
        assertEquals(1, xmlTreeBuilder.doc.childNodes().size());

        // Error scenario - comment is a bogus XmlDeclaration
        commentToken.setData("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
        xmlTreeBuilder.insert(commentToken);
        // Assert document has two child nodes as XmlDeclaration should be inserted
        assertEquals(2, xmlTreeBuilder.doc.childNodes().size());
    }

    @Test
    public void testInsert_Character() {
        // Typical case - text node
        Token.Character textCharacter = new Token.Character("text");
        xmlTreeBuilder.insert(textCharacter);
        // Assert document has one child node
        assertEquals(1, xmlTreeBuilder.doc.childNodes().size());

        // Typical case - CDATA node
        Token.Character cdataCharacter = new Token.Character("<![CDATA[cdata]]>");
        xmlTreeBuilder.insert(cdataCharacter);
        // Assert document has two child nodes
        assertEquals(2, xmlTreeBuilder.doc.childNodes().size());
    }

    @Test
    public void testInsert_Doctype() {
        // Typical case
        Token.Doctype doctype = new Token.Doctype("html");
        xmlTreeBuilder.insert(doctype);
        // Assert document has one child node
        assertEquals(1, xmlTreeBuilder.doc.childNodes().size());
    }

    @Test
    public void testPopStackToClose() {
        
        // Setup stack
        xmlTreeBuilder.stack.add(new Element(Tag.valueOf("a"), "baseUri", xmlTreeBuilder.defaultSettings()));
        xmlTreeBuilder.stack.add(new Element(Tag.valueOf("b"), "baseUri", xmlTreeBuilder.defaultSettings()));
        xmlTreeBuilder.stack.add(new Element(Tag.valueOf("c"), "baseUri", xmlTreeBuilder.defaultSettings()));

        // Typical case - element found in stack
        Token.EndTag endTag = new Token.EndTag("b");
        xmlTreeBuilder.popStackToClose(endTag);
        // Assert stack size is decreased by 1
        assertEquals(2, xmlTreeBuilder.stack.size());

        // Edge case - element not found in stack
        endTag = new Token.EndTag("d");
        xmlTreeBuilder.popStackToClose(endTag);
        // Assert stack size remains the same
        assertEquals(2, xmlTreeBuilder.stack.size());
    }

    @Test
    public void testParseFragment() {

        // Typical case
        String inputFragment = "<div>Element</div>";
        String baseUri = "https://example.com";
        Parser parser = new Parser(xmlTreeBuilder);
        xmlTreeBuilder.initialiseParse(new StringReader(inputFragment), baseUri, parser);
        xmlTreeBuilder.runParser();
        // Assert document has one child node
        assertEquals(1, xmlTreeBuilder.doc.childNodes().size());

        // Error scenario - null input
        inputFragment = null;
        try {
            xmlTreeBuilder.parseFragment(inputFragment, baseUri, parser);
            fail("NullPointerException should have been thrown");
        } catch (NullPointerException e) {
            // Expected exception thrown, test passed
        }
    }

    @Test
    public void testParseFragment_withContextElement() {

        // Typical case
        String inputFragment = "<div>Element</div>";
        Element contextElement = new Element(Tag.valueOf("parent"), "baseUri", xmlTreeBuilder.defaultSettings());
        String baseUri = "https://example.com";
        Parser parser = new Parser(xmlTreeBuilder);
        List<Node> nodes = xmlTreeBuilder.parseFragment(inputFragment, contextElement, baseUri, parser);
        // Assert nodes list has one node
        assertEquals(1, nodes.size());

        // Error scenario - null input
        inputFragment = null;
        try {
            xmlTreeBuilder.parseFragment(inputFragment, contextElement, baseUri, parser);
            fail("NullPointerException should have been thrown");
        } catch (NullPointerException e) {
            // Expected exception thrown, test passed
        }
    }
}