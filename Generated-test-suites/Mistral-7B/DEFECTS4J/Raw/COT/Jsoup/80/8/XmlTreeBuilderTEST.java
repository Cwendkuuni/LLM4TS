package org.jsoup.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

import java.io.IOException;
import java.io.StringReader;
import java.util.Arrays;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.parser.XmlTreeBuilder;
import org.jsoup.parser.XmlTreeBuilder.ParseSettings;
import org.jsoup.parser.XmlTreeBuilder.Token;
import org.jsoup.parser.XmlTreeBuilder.Token.Character;
import org.jsoup.parser.XmlTreeBuilder.Token.Comment;
import org.jsoup.parser.XmlTreeBuilder.Token.Doctype;
import org.jsoup.parser.XmlTreeBuilder.Token.EndTag;
import org.jsoup.parser.XmlTreeBuilder.Token.StartTag;

import org.junit.Test;

/**
 * Test class for {@link XmlTreeBuilder}.
 */
public class XmlTreeBuilderTest {

    @Test
    public void testDefaultSettings() {
        ParseSettings settings = XmlTreeBuilder.this.defaultSettings();
        assertEquals(ParseSettings.PreserveCase, settings);
    }

    @Test
    public void testParse() throws IOException {
        String xml = "<root><child><grandChild/></child></root>";
        Document doc = new XmlTreeBuilder().parse(new StringReader(xml), "http://www.example.com");
        assertNotNull(doc);
        assertNotNull(doc.child(0));
        assertNotNull(doc.child(0).child(0));
        assertEquals("grandChild", doc.child(0).child(0).nodeName());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidBaseUri() {
        new XmlTreeBuilder().parse(new StringReader("<xml/>"), null);
    }

    @Test
    public void testProcessStartTag() throws IOException {
        String xml = "<root><child><grandChild/></child></root>";
        XmlTreeBuilder builder = new XmlTreeBuilder();
        ParseSettings settings = builder.defaultSettings();
        Element root = new Element("root", "http://www.example.com", settings.normalizeAttributes(null));
        Element child = null;
        Token token;

        builder.initialiseParse(new StringReader(xml), "http://www.example.com", ParseErrorList.noTracking(), settings);

        while ((token = builder.nextToken()) != null) {
            switch (token.type) {
                case StartTag:
                    child = builder.insert(token.asStartTag());
                    assertEquals("child", child.nodeName());
                    break;
                default:
                    break;
            }
        }

        assertEquals(2, root.childNodes().size());
        assertEquals(child, root.child(0));
    }

    @Test
    public void testProcessEndTag() throws IOException {
        String xml = "<root><child></child></root>";
        XmlTreeBuilder builder = new XmlTreeBuilder();
        ParseSettings settings = builder.defaultSettings();
        Element root = new Element("root", "http://www.example.com", settings.normalizeAttributes(null));
        Element child = null;
        Stack<Element> stack = new Stack<>();
        stack.add(root);
        Token token;

        builder.initialiseParse(new StringReader(xml), "http://www.example.com", ParseErrorList.noTracking(), settings);

        while ((token = builder.nextToken()) != null) {
            switch (token.type) {
                case StartTag:
                    child = builder.insert(token.asStartTag());
                    assertEquals("child", child.nodeName());
                    stack.add(child);
                    break;
                case EndTag:
                    builder.popStackToClose(token.asEndTag());
                    child = stack.pop();
                    assertEquals("child", child.nodeName());
                    break;
                default:
                    break;
            }
        }

        assertEquals(0, stack.size());
        assertEquals(1, root.childNodes().size());
    }

    @Test
    public void testProcessComment() throws IOException {
        String xml = "<!-- Test comment -->";
        XmlTreeBuilder builder = new XmlTreeBuilder();
        ParseSettings settings = builder.defaultSettings();
        Comment comment = null;

        builder.initialiseParse(new StringReader(xml), "http://www.example.com", ParseErrorList.noTracking(), settings);

        while ((builder.nextToken()) != null) {
            if (builder.currentToken().type == Token.Type.Comment) {
                comment = (Comment) builder.currentToken();
                break;
            }
        }

        assertNotNull(comment);
        assertEquals("Test comment", comment.getData());
    }

    @Test
    public void testProcessCharacter() throws IOException {
        String xml = "<root>&amp; < > &lt; &gt; &apos; &quot; &nbsp; &iexcl; &cent; &pound; &curren; &yen;</root>";
        XmlTreeBuilder builder = new XmlTreeBuilder();
        ParseSettings settings = builder.defaultSettings();
        List<Node> childNodes = Arrays.asList(
                new TextNode("&amp;"),
                new TextNode(" "),
                new TextNode(" < "),
                new TextNode(" &lt; "),
                new TextNode(" &gt; "),
                new TextNode(" &apos;"),
                new TextNode(" &quot; "),
                new TextNode(" "),
                new TextNode("€"),
                new TextNode("¢"),
                new TextNode("£"),
                new TextNode("¥")
        );
        Element root = new Element("root", "http://www.example.com", settings.normalizeAttributes(null));
        Element child = null;
        Token token;

        builder.initialiseParse(new StringReader(xml), "http://www.example.com", ParseErrorList.noTracking(), settings);

        while ((token = builder.nextToken()) != null) {
            switch (token.type) {
                case Character:
                    child = builder.insert(token.asCharacter());
                    assertEquals(token.getData(), child.text());
                    insertNode(root, child);
                    break;
                default:
                    break;
            }
        }

        assertEquals(childNodes.size(), root.childNodes().size());
        for (int i = 0; i < childNodes.size(); i++) {
            Node node = root.childNodes().get(i);
            assertEquals(childNodes.get(i).text(), node.text());
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidDoctypeName() throws IOException {
        new XmlTreeBuilder().parse(new StringReader("<?xml version='1.0'?><root>"), "http://www.example.com");
    }

    @Test
    public void testProcessDoctype() throws IOException {
        String xml = "<!DOCTYPE root [ <!ENTITY % myEntity SYSTEM \"file:///path/to/file\"> ]><root/>";
        XmlTreeBuilder builder = new XmlTreeBuilder();
        ParseSettings settings = builder.defaultSettings();
        DocumentType doctypeNode = null;

        builder.initialiseParse(new StringReader(xml), "http://www.example.com", ParseErrorList.noTracking(), settings);

        while ((builder.nextToken()) != null) {
            if (builder.currentToken().type == Token.Type.Doctype) {
                doctypeNode = (DocumentType) builder.currentToken();
                break;
            }
        }

        assertNotNull(doctypeNode);
        assertEquals("root", doctypeNode.nodeName());
    }

    @Test
    public void testParseFragment() throws IOException {
        String xmlFragment = "<child><grandChild/></child>";
        List<Node> nodes = new XmlTreeBuilder().parseFragment(xmlFragment, "http://www.example.com", ParseErrorList.noTracking(), ParseSettings.preserveCase);
        assertEquals(2, nodes.size());
        assertEquals("child", nodes.get(0).nodeName());
        assertEquals("grandChild", nodes.get(1).nodeName());
    }

    @Test(expected = NullPointerException.class)
    public void testNullInput() throws IOException {
        new XmlTreeBuilder().parse(null, "http://www.example.com");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testEmptyInput() throws IOException {
        String input = "";
        ParseErrorList errors = new ParseErrorList();
        ParseSettings settings = new XmlTreeBuilder().defaultSettings();
        new XmlTreeBuilder().parse(new StringReader(input), "http://www.example.com", errors, settings);
        assertEquals(1, errors.size());
    }

    @Test
    public void testParseWithErrors() throws IOException {
        String invalidXml = "<!DOCTYPE root [ <!ENTITY % myEntity SYSTEM \"file:///path/to/file\"> ]><root>\r<child><grandChild/></child>";
        ParseErrorList errors = new ParseErrorList();
        Document doc = new XmlTreeBuilder().parse(new StringReader(invalidXml), "http://www.example.com", errors, ParseSettings.preserveCase);
        assertNotNull(doc);
        assertEquals(1, errors.size());
        assertEquals(1, doc.childNodes().size());
        assertEquals("child", doc.child(0).nodeName());
    }

    @Test
    public void testProcessInvalidToken() throws IOException {
        String xml = "<root><child><grandChild/></child></root><!>";
        XmlTreeBuilder builder = new XmlTreeBuilder();
        ParseSettings settings = builder.defaultSettings();
        Element root = new Element("root", "http://www.example.com", settings.normalizeAttributes(null));
        Element child = null;
        Stack<Element> stack = new Stack<>();
        stack.add(root);
        Token token;

        builder.initialiseParse(new StringReader(xml), "http://www.example.com", ParseErrorList.noTracking(), settings);

        while ((token = builder.nextToken()) != null) {
            switch (token.type) {
                case StartTag:
                    child = builder.insert(token.asStartTag());
                    assertEquals("child", child.nodeName());
                    stack.add(child);
                    break;
                case EndTag:
                    builder.popStackToClose(token.asEndTag());
                    child = stack.pop();
                    assertEquals("child", child.nodeName());
                    break;
                case Comment:
                    // XML parsing should not accept comments after the document ends
                    assertNull(token);
                    break;
                case Character:
                    assertNull(token);
                    break;
                case Doctype:
                    // XML parsing should not accept DOCTYPE after the document ends
                    assertNull(token);
                    break;
                case EOF:
                    assertThrows(org.jsoup.parser.ParseError.class, () -> builder.nextToken());
                    break;
                default:
                    break;
            }
        }

        assertEquals(0, stack.size());
        assertEquals(1, root.childNodes().size());
    }

    private void insertNode(Element parent, Node node) {
        parent.appendChild(node);
    }
}