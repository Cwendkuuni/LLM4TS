import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.cli.Options;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.ParseException;
import org.junit.Before;
import org.junit.Test;

public class XmlTreeBuilderTest {

    private XmlTreeBuilder xmlTreeBuilder;

    @Before
    public void setUp() {
        xmlTreeBuilder = new XmlTreeBuilder();
    }

    @Test
    public void testInitialiseParse() {
        String input = "<root>";
        String baseUri = "https://www.example.com";
        ParseErrorList errors = new ParseErrorList();
        xmlTreeBuilder.initialiseParse(input, baseUri, errors);
        assertEquals("https://www.example.com", xmlTreeBuilder.baseUri);
        assertEquals(1, xmlTreeBuilder.stack.size());
        assertTrue(xmlTreeBuilder.stack.contains(xmlTreeBuilder.doc));
        assertEquals(Document.OutputSettings.Syntax.xml, xmlTreeBuilder.doc.outputSettings().syntax());
    }

    @Test
    public void testProcessWithStartTagToken() {
        Token token = new Token.StartTag("div");
        assertTrue(xmlTreeBuilder.process(token));
        assertEquals(1, xmlTreeBuilder.stack.size());
        assertTrue(xmlTreeBuilder.stack.get(0).nodeName().equals("div"));
    }

    @Test
    public void testProcessWithEndTagToken() {
        Token token = new Token.EndTag("div");
        xmlTreeBuilder.stack.add(new Element("div"));
        assertTrue(xmlTreeBuilder.process(token));
        assertEquals(0, xmlTreeBuilder.stack.size());
    }

    @Test
    public void testProcessWithInvalidToken() {
        Token token = new Token.Invalid();
        try {
            xmlTreeBuilder.process(token);
            fail("Expected Validate.fail() to be called");
        } catch (RuntimeException e) {
            assertEquals("Unexpected token type: " + token.type, e.getMessage());
        }
    }

    @Test
    public void testInsertNode() {
        Element element = new Element("div");
        xmlTreeBuilder.elements.add(element);
        Node node = new TextNode("Hello world");
        xmlTreeBuilder.insertNode(node);
        assertEquals(1, element.childNodes().size());
        assertTrue(element.childNode(0).equals(node));
    }

    @Test
    public void testInsertWithStartTag() {
        Token.StartTag startTag = new Token.StartTag("div");
        startTag.attributes.put("class", "container");
        Element element = xmlTreeBuilder.insert(startTag);
        assertTrue(element.nodeName().equals("div"));
        assertTrue(element.attributes().get("class").equals("container"));
        assertTrue(xmlTreeBuilder.stack.contains(element));
    }

    @Test
    public void testInsertWithSelfClosingStartTag() {
        Token.StartTag startTag = new Token.StartTag("img");
        startTag.selfClosing = true;
        Element element = xmlTreeBuilder.insert(startTag);
        assertTrue(element.nodeName().equals("img"));
        assertTrue(xmlTreeBuilder.stack.isEmpty());
        assertTrue(startTag.selfClosing);
    }

    @Test
    public void testInsertWithCommentToken() {
        Token.Comment commentToken = new Token.Comment("This is a comment");
        xmlTreeBuilder.insert(commentToken);
        assertEquals(1, xmlTreeBuilder.doc.childNodes().size());
        assertTrue(xmlTreeBuilder.doc.childNode(0) instanceof Comment);
        assertEquals("This is a comment", ((Comment) xmlTreeBuilder.doc.childNode(0)).getData());
    }

    @Test
    public void testInsertWithCharacterToken() {
        Token.Character characterToken = new Token.Character("Hello world");
        xmlTreeBuilder.insert(characterToken);
        assertEquals(1, xmlTreeBuilder.doc.childNodes().size());
        assertTrue(xmlTreeBuilder.doc.childNode(0) instanceof TextNode);
        assertEquals("Hello world", ((TextNode) xmlTreeBuilder.doc.childNode(0)).getWholeText());
    }

    @Test
    public void testInsertWithDoctypeToken() {
        Token.Doctype doctypeToken = new Token.Doctype("html");
        doctypeToken.publicIdentifier = "-//W3C//DTD HTML 4.01 Transitional//EN";
        doctypeToken.systemIdentifier = "http://www.w3.org/TR/html4/loose.dtd";
        xmlTreeBuilder.insert(doctypeToken);
        assertEquals(1, xmlTreeBuilder.doc.childNodes().size());
        assertTrue(xmlTreeBuilder.doc.childNode(0) instanceof DocumentType);
        DocumentType doctypeNode = (DocumentType) xmlTreeBuilder.doc.childNode(0);
        assertEquals("html", doctypeNode.getName());
        assertEquals("-//W3C//DTD HTML 4.01 Transitional//EN", doctypeNode.getPublicIdentifier());
        assertEquals("http://www.w3.org/TR/html4/loose.dtd", doctypeNode.getSystemIdentifier());
    }

    @Test
    public void testPopStackToClose() {
        Token.EndTag endTag = new Token.EndTag("div");
        xmlTreeBuilder.stack.add(new Element("div"));
        xmlTreeBuilder.popStackToClose(endTag);
        assertTrue(xmlTreeBuilder.stack.isEmpty());
    }

    @Test
    public void testPopStackToCloseWithUnknownTag() {
        Token.EndTag endTag = new Token.EndTag("div");
        xmlTreeBuilder.stack.add(new Element("span"));
        xmlTreeBuilder.stack.add(new Element("div"));
        xmlTreeBuilder.popStackToClose(endTag);
        assertFalse(xmlTreeBuilder.stack.isEmpty());
        assertTrue(xmlTreeBuilder.stack.get(0).nodeName().equals("span"));
    }

    @Test
    public void testParseFragment() {
        String inputFragment = "<div>Content</div>";
        String baseUri = "https://www.example.com";
        ParseErrorList errors = new ParseErrorList();
        List<Node> nodes = xmlTreeBuilder.parseFragment(inputFragment, baseUri, errors);
        assertEquals(1, nodes.size());
        Node node = nodes.get(0);
        assertTrue(node instanceof Element);
        Element element = (Element) node;
        assertTrue(element.nodeName().equals("div"));
        assertTrue(element.childNodes().size() == 1);
        assertTrue(element.childNode(0) instanceof TextNode);
        TextNode textNode = (TextNode) element.childNode(0);
        assertTrue(textNode.getWholeText().equals("Content"));
    }

}