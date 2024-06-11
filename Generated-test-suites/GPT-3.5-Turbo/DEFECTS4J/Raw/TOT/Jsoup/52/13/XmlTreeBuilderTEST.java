import org.junit.Assert;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class XmlTreeBuilderTest {

    @Test
    public void testInitialiseParse() {
        // Typical use case - input with no errors
        String input = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><root><element1>Value1</element1><element2>Value2</element2></root>";
        String baseUri = "http://example.com";
        ParseErrorList errors = new ParseErrorList();
        
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        xmlTreeBuilder.initialiseParse(input, baseUri, errors);
        assertEquals(baseUri, xmlTreeBuilder.doc.baseUri());
        assertEquals(Document.OutputSettings.Syntax.xml, xmlTreeBuilder.doc.outputSettings().syntax());
        assertTrue(errors.isEmpty());
    }

    @Test
    public void testProcess() {
        // Typical use case - start tag
        Token startTag = new Token(Tag.valueOf("div"), TokenType.StartTag);
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        boolean result = xmlTreeBuilder.process(startTag);
        assertTrue(result);
        assertEquals("div", xmlTreeBuilder.currentElement().nodeName());

        // Typical use case - end tag
        Token endTag = new Token(Tag.valueOf("div"), TokenType.EndTag);
        xmlTreeBuilder = new XmlTreeBuilder();
        boolean result = xmlTreeBuilder.process(endTag);
        assertTrue(result);
        Assert.assertNull(xmlTreeBuilder.currentElement());

        // Typical use case - comment
        Token comment = new Token(TokenType.Comment);
        xmlTreeBuilder = new XmlTreeBuilder();
        boolean result = xmlTreeBuilder.process(comment);
        assertTrue(result);
        Assert.assertTrue(xmlTreeBuilder.currentNode() instanceof Comment);

        // Typical use case - character
        Token character = new Token(TokenType.Character);
        xmlTreeBuilder = new XmlTreeBuilder();
        boolean result = xmlTreeBuilder.process(character);
        assertTrue(result);
        Assert.assertTrue(xmlTreeBuilder.currentNode() instanceof TextNode);

        // Typical use case - doctype
        Token doctype = new Token(TokenType.Doctype);
        xmlTreeBuilder = new XmlTreeBuilder();
        boolean result = xmlTreeBuilder.process(doctype);
        assertTrue(result);
        Assert.assertTrue(xmlTreeBuilder.currentNode() instanceof DocumentType);

        // Error scenario - unexpected token type
        Token invalidToken = new Token(TokenType.Unknown);
        xmlTreeBuilder = new XmlTreeBuilder();
        try {
            xmlTreeBuilder.process(invalidToken);
            Assert.fail("Expected Validate.fail() to throw an exception");
        } catch (IllegalArgumentException e) {
            assertEquals("Unexpected token type: Unknown", e.getMessage());
        }
    }

    @Test
    public void testInsertNode() {
        // Typical use case - insert element node
        Element element = new Element(Tag.valueOf("div"), "http://example.com");
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        xmlTreeBuilder.insertNode(element);
        List<Node> childNodes = xmlTreeBuilder.currentElement().childNodes();
        assertEquals(1, childNodes.size());
        assertEquals(element, childNodes.get(0));

        // Typical use case - insert comment node
        Comment comment = new Comment("Test comment", "http://example.com");
        xmlTreeBuilder = new XmlTreeBuilder();
        xmlTreeBuilder.insertNode(comment);
        childNodes = xmlTreeBuilder.currentElement().childNodes();
        assertEquals(1, childNodes.size());
        assertEquals(comment, childNodes.get(0));

        // Typical use case - insert text node
        TextNode textNode = new TextNode("Test text", "http://example.com");
        xmlTreeBuilder = new XmlTreeBuilder();
        xmlTreeBuilder.insertNode(textNode);
        childNodes = xmlTreeBuilder.currentElement().childNodes();
        assertEquals(1, childNodes.size());
        assertEquals(textNode, childNodes.get(0));

        // Typical use case - insert doctype node
        DocumentType doctype = new DocumentType("html", "-//W3C//DTD HTML 4.01//EN", "http://www.w3.org/TR/html4/strict.dtd", "http://example.com");
        xmlTreeBuilder = new XmlTreeBuilder();
        xmlTreeBuilder.insertNode(doctype);
        childNodes = xmlTreeBuilder.currentElement().childNodes();
        assertEquals(1, childNodes.size());
        assertEquals(doctype, childNodes.get(0));
    }

    @Test
    public void testInsert() {
        // Typical use case - self-closing start tag
        Token.StartTag startTag = new Token.StartTag("input", new Attributes(), true);
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Element element = xmlTreeBuilder.insert(startTag);
        assertTrue(XmlTreeBuilderTestUtils.isElementEquals(element, "input"));
        assertTrue(element.isSelfClosing());

        // Typical use case - non-self-closing start tag
        startTag = new Token.StartTag("div", new Attributes(), false);
        xmlTreeBuilder = new XmlTreeBuilder();
        element = xmlTreeBuilder.insert(startTag);
        assertTrue(XmlTreeBuilderTestUtils.isElementEquals(element, "div"));
        assertTrue(xmlTreeBuilder.stack.contains(element));

        // Error scenario - unknown start tag
        startTag = new Token.StartTag("unknown", new Attributes(), false);
        xmlTreeBuilder = new XmlTreeBuilder();
        element = xmlTreeBuilder.insert(startTag);
        assertTrue(XmlTreeBuilderTestUtils.isElementEquals(element, "unknown"));
        assertTrue(element.isSelfClosing());

        // Error scenario - unknown self-closing start tag
        startTag = new Token.StartTag("unknown", new Attributes(), true);
        xmlTreeBuilder = new XmlTreeBuilder();
        element = xmlTreeBuilder.insert(startTag);
        assertTrue(XmlTreeBuilderTestUtils.isElementEquals(element, "unknown"));
        assertTrue(element.isSelfClosing());
    }

    @Test
    public void testInsertComment() {
        // Typical use case - regular comment
        Token.Comment commentToken = new Token.Comment("Test comment");
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        xmlTreeBuilder.insert(commentToken);
        assertTrue(xmlTreeBuilder.currentNode() instanceof Comment);

        // Typical use case - xml declaration
        commentToken = new Token.Comment("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
        xmlTreeBuilder = new XmlTreeBuilder();
        xmlTreeBuilder.insert(commentToken);
        List<Node> childNodes = xmlTreeBuilder.currentElement().childNodes();
        assertEquals(1, childNodes.size());
        assertTrue(childNodes.get(0) instanceof XmlDeclaration);
    }

    @Test
    public void testInsertCharacter() {
        // Typical use case
        Token.Character characterToken = new Token.Character("Test character");
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        xmlTreeBuilder.insert(characterToken);
        assertTrue(xmlTreeBuilder.currentNode() instanceof TextNode);
    }

    @Test
    public void testInsertDoctype() {
        // Typical use case
        Token.Doctype doctype = new Token.Doctype("html", "-//W3C//DTD HTML 4.01//EN", "http://www.w3.org/TR/html4/strict.dtd");
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        xmlTreeBuilder.insert(doctype);
        assertTrue(xmlTreeBuilder.currentNode() instanceof DocumentType);
    }

    @Test
    public void testPopStackToClose() {
        // Typical use case - element with specified name exists in the stack
        Token.EndTag endTag = new Token.EndTag("div");
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Element element1 = new Element(Tag.valueOf("div"));
        Element element2 = new Element(Tag.valueOf("div"));
        Element element3 = new Element(Tag.valueOf("p"));
        xmlTreeBuilder.stack.add(element1);
        xmlTreeBuilder.stack.add(element2);
        xmlTreeBuilder.stack.add(element3);
        xmlTreeBuilder.popStackToClose(endTag);
        assertEquals(1, xmlTreeBuilder.stack.size());
        assertTrue(xmlTreeBuilder.stack.contains(element3));

        // Typical use case - element with specified name does not exist in the stack
        endTag = new Token.EndTag("span");
        xmlTreeBuilder = new XmlTreeBuilder();
        element1 = new Element(Tag.valueOf("div"));
        element2 = new Element(Tag.valueOf("div"));
        element3 = new Element(Tag.valueOf("p"));
        xmlTreeBuilder.stack.add(element1);
        xmlTreeBuilder.stack.add(element2);
        xmlTreeBuilder.stack.add(element3);
        xmlTreeBuilder.popStackToClose(endTag);
        assertEquals(3, xmlTreeBuilder.stack.size());
        assertTrue(xmlTreeBuilder.stack.contains(element1));
        assertTrue(xmlTreeBuilder.stack.contains(element2));
        assertTrue(xmlTreeBuilder.stack.contains(element3));
    }

    @Test
    public void testParseFragment() {
        // Typical use case
        String inputFragment = "<root><element1>Value1</element1><element2>Value2</element2></root>";
        String baseUri = "http://example.com";
        ParseErrorList errors = new ParseErrorList();

        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        List<Node> nodes = xmlTreeBuilder.parseFragment(inputFragment, baseUri, errors);
        assertEquals(1, nodes.size());
        assertTrue(nodes.get(0) instanceof Element);
        Element root = (Element) nodes.get(0);
        assertEquals("root", root.nodeName());
        assertEquals(2, root.childNodes().size());
        assertTrue(XmlTreeBuilderTestUtils.isElementEquals(root.childNode(0), "element1"));
        assertTrue(XmlTreeBuilderTestUtils.isElementEquals(root.childNode(1), "element2"));

        // Error scenario - malformed input
        inputFragment = "<root><element1>Value1</element2></root>";
        xmlTreeBuilder = new XmlTreeBuilder();
        nodes = xmlTreeBuilder.parseFragment(inputFragment, baseUri, errors);
        assertTrue(nodes.isEmpty());
        assertEquals(1, errors.size());
        assertEquals("Unexpected token type: EndTag", errors.get(0).toString());
    }

}