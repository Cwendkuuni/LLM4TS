import org.jsoup.helper.Validate;
import org.jsoup.nodes.*;
import org.jsoup.parser.ParseErrorList;
import org.jsoup.parser.Token;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

public class XmlTreeBuilderTest {

    // Test case for initialiseParse() method
    @Test
    public void testInitialiseParse() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        String input = "<root><child></child></root>";
        String baseUri = "www.example.com";
        ParseErrorList errors = new ParseErrorList();

        xmlTreeBuilder.initialiseParse(input, baseUri, errors);

        // Assert stack is initialized with document element
        assertEquals(1, xmlTreeBuilder.stack.size());
        assertEquals("root", xmlTreeBuilder.stack.get(0).nodeName());

        // Assert document's output settings are set to XML
        assertEquals(Document.OutputSettings.Syntax.xml, xmlTreeBuilder.doc.outputSettings().syntax());
    }

    // Test case for process() method with Token.StartTag
    @Test
    public void testProcessStartTag() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Token.StartTag startTag = new Token.StartTag("div");
        startTag.attributes.put("id", "123");
        startTag.attributes.put("class", "test");

        xmlTreeBuilder.stack.push(new Element("root"));
        xmlTreeBuilder.process(startTag);

        // Assert element is inserted into the current element
        assertEquals("div", xmlTreeBuilder.currentElement().childNode(0).nodeName());
        assertEquals("123", xmlTreeBuilder.currentElement().childNode(0).attr("id"));
        assertEquals("test", xmlTreeBuilder.currentElement().childNode(0).attr("class"));
    }

    // Test case for process() method with Token.EndTag
    @Test
    public void testProcessEndTag() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Token.EndTag endTag = new Token.EndTag("div");

        Element element1 = new Element("div");
        Element element2 = new Element("span");
        Element element3 = new Element("div");

        xmlTreeBuilder.stack.push(element1);
        xmlTreeBuilder.stack.push(element2);
        xmlTreeBuilder.stack.push(element3);

        xmlTreeBuilder.process(endTag);

        // Assert elements are popped from stack until the first occurrence of the end tag is found
        assertEquals(1, xmlTreeBuilder.stack.size());
        assertEquals("span", xmlTreeBuilder.stack.get(0).nodeName());
    }

    // Test case for process() method with Token.Comment
    @Test
    public void testProcessComment() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Token.Comment comment = new Token.Comment("This is a comment");

        xmlTreeBuilder.process(comment);

        // Assert comment node is inserted into the current element
        assertEquals("This is a comment", xmlTreeBuilder.currentElement().childNode(0).toString());
        assertTrue(xmlTreeBuilder.currentElement().childNode(0) instanceof Comment);
    }

    // Test case for process() method with Token.Character
    @Test
    public void testProcessCharacter() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Token.Character character = new Token.Character("Hello, World!");

        xmlTreeBuilder.process(character);

        // Assert text node is inserted into the current element
        assertEquals("Hello, World!", xmlTreeBuilder.currentElement().childNode(0).toString());
        assertTrue(xmlTreeBuilder.currentElement().childNode(0) instanceof TextNode);
    }

    // Test case for process() method with Token.Doctype
    @Test
    public void testProcessDoctype() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Token.Doctype doctype = new Token.Doctype("html", "", "", "");

        xmlTreeBuilder.process(doctype);

        // Assert doctype node is inserted into the current element
        assertEquals("html", xmlTreeBuilder.currentElement().childNode(0).nodeName());
        assertTrue(xmlTreeBuilder.currentElement().childNode(0) instanceof DocumentType);
    }

    // Test case for insert() method with self-closing start tag
    @Test
    public void testInsertSelfClosingStartTag() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Token.StartTag startTag = new Token.StartTag("img");
        startTag.attributes.put("src", "image.jpg");

        xmlTreeBuilder.stack.push(new Element("root"));
        xmlTreeBuilder.insert(startTag);

        // Assert element is inserted into the current element and acknowledged as self-closing
        assertTrue(xmlTreeBuilder.tokeniser.selfClosingFlagAcknowledged);
        assertTrue(xmlTreeBuilder.currentElement().childNode(0) instanceof Element);
        assertTrue(xmlTreeBuilder.currentElement().childNode(0).nodeName().equals("img"));
        assertEquals("image.jpg", xmlTreeBuilder.currentElement().childNode(0).attr("src"));
        assertTrue(xmlTreeBuilder.currentElement().childNode(0).tag().isKnownTag());
    }

    // Test case for insert() method with non-self-closing start tag
    @Test
    public void testInsertNonSelfClosingStartTag() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Token.StartTag startTag = new Token.StartTag("div");
        startTag.attributes.put("class", "container");

        xmlTreeBuilder.stack.push(new Element("root"));
        xmlTreeBuilder.insert(startTag);

        // Assert element is inserted into the current element and added to the stack
        assertFalse(xmlTreeBuilder.tokeniser.selfClosingFlagAcknowledged);
        assertTrue(xmlTreeBuilder.currentElement().childNode(0) instanceof Element);
        assertTrue(xmlTreeBuilder.currentElement().childNode(0).nodeName().equals("div"));
        assertEquals("container", xmlTreeBuilder.currentElement().childNode(0).attr("class"));
        assertTrue(xmlTreeBuilder.stack.contains(xmlTreeBuilder.currentElement().childNode(0)));
    }

    // Test case for insert() method with comment token containing an XML declaration
    @Test
    public void testInsertCommentWithXmlDeclaration() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Token.Comment comment = new Token.Comment("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");

        xmlTreeBuilder.insert(comment);

        // Assert XmlDeclaration node is inserted into the current element
        assertEquals("xml", xmlTreeBuilder.currentElement().childNode(0).nodeName());
        assertTrue(xmlTreeBuilder.currentElement().childNode(0) instanceof XmlDeclaration);
        assertTrue(((XmlDeclaration) xmlTreeBuilder.currentElement().childNode(0)).isProcessingInstruction());
    }

    // Test case for insert() method with comment token not containing an XML declaration
    @Test
    public void testInsertCommentWithoutXmlDeclaration() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Token.Comment comment = new Token.Comment("This is a comment");

        xmlTreeBuilder.insert(comment);

        // Assert comment node is inserted into the current element
        assertEquals("This is a comment", xmlTreeBuilder.currentElement().childNode(0).toString());
        assertTrue(xmlTreeBuilder.currentElement().childNode(0) instanceof Comment);
    }

    // Test case for insert() method with character token
    @Test
    public void testInsertCharacter() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Token.Character character = new Token.Character("Hello, World!");

        xmlTreeBuilder.insert(character);

        // Assert text node is inserted into the current element
        assertEquals("Hello, World!", xmlTreeBuilder.currentElement().childNode(0).toString());
        assertTrue(xmlTreeBuilder.currentElement().childNode(0) instanceof TextNode);
    }

    // Test case for insert() method with doctype token
    @Test
    public void testInsertDoctype() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Token.Doctype doctype = new Token.Doctype("html", "", "", "");

        xmlTreeBuilder.insert(doctype);

        // Assert doctype node is inserted into the current element
        assertEquals("html", xmlTreeBuilder.currentElement().childNode(0).nodeName());
        assertTrue(xmlTreeBuilder.currentElement().childNode(0) instanceof DocumentType);
    }

    // Test case for popStackToClose() method with existing end tag
    @Test
    public void testPopStackToCloseExistingEndTag() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Token.EndTag endTag = new Token.EndTag("div");
        Element div1 = new Element("div");
        Element div2 = new Element("div");
        Element div3 = new Element("div");
        xmlTreeBuilder.stack.push(div1);
        xmlTreeBuilder.stack.push(div2);
        xmlTreeBuilder.stack.push(div3);

        xmlTreeBuilder.popStackToClose(endTag);

        // Assert elements on stack are removed until the first occurrence of the end tag is found
        assertEquals(1, xmlTreeBuilder.stack.size());
        assertEquals(div2, xmlTreeBuilder.stack.get(0));
    }

    // Test case for popStackToClose() method without existing end tag
    @Test
    public void testPopStackToCloseNonExistingEndTag() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Token.EndTag endTag = new Token.EndTag("div");
        Element span = new Element("span");
        Element p = new Element("p");
        Element a = new Element("a");
        xmlTreeBuilder.stack.push(span);
        xmlTreeBuilder.stack.push(p);
        xmlTreeBuilder.stack.push(a);

        xmlTreeBuilder.popStackToClose(endTag);

        // Assert elements on stack are not removed as end tag is not found
        assertEquals(3, xmlTreeBuilder.stack.size());
        assertTrue(xmlTreeBuilder.stack.contains(span));
        assertTrue(xmlTreeBuilder.stack.contains(p));
        assertTrue(xmlTreeBuilder.stack.contains(a));
    }

    // Test case for parseFragment() method
    @Test
    public void testParseFragment() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        String inputFragment = "<div><span></span><p></p></div>";
        String baseUri = "www.example.com";
        ParseErrorList errors = new ParseErrorList();

        List<Node> nodes = xmlTreeBuilder.parseFragment(inputFragment, baseUri, errors);

        // Assert fragment is parsed and nodes are returned
        assertEquals(1, nodes.size());
        assertTrue(nodes.get(0) instanceof Element);
    }

}