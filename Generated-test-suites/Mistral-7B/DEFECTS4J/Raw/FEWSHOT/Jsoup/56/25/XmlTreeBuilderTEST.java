package org.jsoup.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Comment;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.DocumentType;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.parser.ParseError;
import org.jsoup.parser.ParseSettings;
import org.jsoup.parser.Token;
import org.jsoup.parser.Tokenizer;

import java.util.ArrayList;
import java.util.List;

public class XmlTreeBuilderTest {

    @Test
    public void testParseEmptyXml() {
        ParseSettings settings = new ParseSettings();
        Document doc = new XmlTreeBuilder().parse("", "");
        assertEquals("", doc.body().text());
        assertNull(doc.head());
    }

    @Test
    public void testParseSimpleXml() {
        ParseSettings settings = new ParseSettings();
        String xml = "<root><element attr=\"value\">Hello World!</element></root>";
        Document doc = new XmlTreeBuilder().parse(xml, "");
        Element root = doc.rootElement();
        Element element = root.child(0);
        assertEquals("root", root.tagName());
        assertEquals("element", element.tagName());
        assertEquals("value", element.attr("attr"));
        assertEquals("Hello World!", element.text());
    }

    @Test
    public void testParseComplexXmlWithEmptyElements() {
        ParseSettings settings = new ParseSettings();
        String xml = "<root><element attr1=\"value1\"><subelement attr2=\"value2\"></subelement></element></root>";
        Document doc = new XmlTreeBuilder().parse(xml, "");
        Element root = doc.rootElement();
        Element element = root.child(0);
        Element subelement = element.child(0);
        assertEquals("root", root.tagName());
        assertEquals("element", element.tagName());
        assertEquals("value1", element.attr("attr1"));
        assertEquals("subelement", subelement.tagName());
        assertEquals("value2", subelement.attr("attr2"));
        assertTrue(subelement.text().isEmpty());
    }

    @Test
    public void testParseXmlWithSelfClosingElements() {
        ParseSettings settings = new ParseSettings();
        String xml = "<root><element/><element><subelement/></element></root>";
        Document doc = new XmlTreeBuilder().parse(xml, "");
        Element root = doc.rootElement();
        Element element1 = root.child(0);
        Element element2 = root.child(1);
        assertEquals("root", root.tagName());
        assertEquals("element", element1.tagName());
        assertTrue(element1.text().isEmpty());
        assertEquals("element", element2.tagName());
        Element subelement = element2.child(0);
        assertTrue(subelement.text().isEmpty());
    }

    @Test
    public void testParseXmlWithProcessingInstruction() {
        ParseSettings settings = new ParseSettings();
        String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><root><element attr=\"value\">Hello World!</element></root>";
        Document doc = new XmlTreeBuilder().parse(xml, "");
        DocumentType doctypeNode = doc.doctype();
        assertEquals("xml", doctypeNode.name());
        assertEquals("XML", doctypeNode.tagName());
        assertEquals("1.0", doctypeNode.getPublicIdentifier());
        assertEquals(null, doctypeNode.getSystemIdentifier());
        Element root = doc.rootElement();
        Element element = root.child(0);
        assertEquals("root", root.tagName());
        assertEquals("element", element.tagName());
        assertEquals("value", element.attr("attr"));
        assertEquals("Hello World!", element.text());
    }

    @Test
    public void testParseXmlWithComment() {
        ParseSettings settings = new ParseSettings();
        String xml = "<!-- This is a comment --><root><element attr=\"value\">Hello World!</element></root>";
        Document doc = new XmlTreeBuilder().parse(xml, "");
        Comment comment = doc.body();
        assertEquals("This is a comment", comment.text());
        Element root = doc.rootElement();
        Element element = root.child(0);
        assertEquals("root", root.tagName());
        assertEquals("element", element.tagName());
        assertEquals("value", element.attr("attr"));
        assertEquals("Hello World!", element.text());
    }

    @Test
    public void testParseFragment() {
        ParseSettings settings = new ParseSettings();
        String xmlFragment = "<root><element attr=\"value\">Hello World!</element></root>";
        List<Node> nodes = new XmlTreeBuilder().parseFragment(xmlFragment, "", ParseErrorList.noTracking(), settings).stream().toList();
        Element root = (Element) nodes.get(0);
        Element element = (Element) nodes.get(1);
        assertEquals("root", root.tagName());
        assertEquals("element", element.tagName());
        assertEquals("value", element.attr("attr"));
        assertEquals("Hello World!", element.text());
    }

    @Test(expected = RuntimeException.class)
    public void testParseInvalidXml() {
        ParseSettings settings = new ParseSettings();
        String xml = "<root<element attr=\"value\">Hello World!</element>";
        new XmlTreeBuilder().parse(xml, "");
    }

    @Test
    public void testPopStackToClose() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Element root = new Element("root");
        Element element1 = new Element("element1");
        Element element2 = new Element("element2");
        xmlTreeBuilder.stack.add(root);
        xmlTreeBuilder.stack.add(element1);
        xmlTreeBuilder.stack.add(element2);

        xmlTreeBuilder.popStackToClose(new Token.EndTag("element1"));

        assertEquals(2, xmlTreeBuilder.stack.size());
        assertSame(root, xmlTreeBuilder.stack.get(0));
        assertSame(element2, xmlTreeBuilder.stack.get(1));
    }

    @Test
    public void testPopStackToCloseWithMissingTag() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Element root = new Element("root");
        Element element1 = new Element("element1");
        Element element2 = new Element("element2");
        xmlTreeBuilder.stack.add(root);
        xmlTreeBuilder.stack.add(element1);
        xmlTreeBuilder.stack.add(element2);

        xmlTreeBuilder.popStackToClose(new Token.EndTag("element3"));

        assertEquals(3, xmlTreeBuilder.stack.size());
        assertSame(root, xmlTreeBuilder.stack.get(0));
        assertSame(element1, xmlTreeBuilder.stack.get(1));
        assertSame(element2, xmlTreeBuilder.stack.get(2));
    }

    @Test
    public void testProcessCharacter() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        String input = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<root><element>Hello World!</element></root>";
        Tokenizer tokenizer = new Tokenizer(input, "");
        Token token = tokenizer.nextToken();

        xmlTreeBuilder.process(token);

        assertEquals(Token.StartTag.class, token.getClass());
        assertEquals("xml", ((Token.StartTag) token).name());
        token = tokenizer.nextToken();

        xmlTreeBuilder.process(token);

        assertEquals(Token.Comment.class, token.getClass());
        assertEquals("XML", ((Token.Comment) token).tagName());
        token = tokenizer.nextToken();

        xmlTreeBuilder.process(token);

        assertEquals(Token.StartTag.class, token.getClass());
        assertEquals("root", ((Token.StartTag) token).name());
        token = tokenizer.nextToken();

        xmlTreeBuilder.process(token);

        assertEquals(Token.EndTag.class, token.getClass());
        assertEquals("root", ((Token.EndTag) token).name());
        token = tokenizer.nextToken();

        xmlTreeBuilder.process(token);

        assertEquals(Token.StartTag.class, token.getClass());
        assertEquals("element", ((Token.StartTag) token).name());
        token = tokenizer.nextToken();

        xmlTreeBuilder.process(token);

        assertEquals(Token.EndTag.class, token.getClass());
        assertEquals("element", ((Token.EndTag) token).name());
        token = tokenizer.nextToken();

        assertEquals(Token.EOF.class, token.getClass());
    }

    @Test
    public void testProcessCommentWithBogusXmlDeclaration() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        String input = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><!-- This is a comment --><root><element>Hello World!</element></root>";
        Tokenizer tokenizer = new Tokenizer(input, "");
        Token token = tokenizer.nextToken();

        xmlTreeBuilder.process(token);

        assertEquals(Token.StartTag.class, token.getClass());
        assertEquals("xml", ((Token.StartTag) token).name());
        token = tokenizer.nextToken();

        xmlTreeBuilder.process(token);

        assertEquals(Token.Comment.class, token.getClass());
        assertEquals("<!-- This is a comment -->", token.asComment().getData());
        token = tokenizer.nextToken();

        xmlTreeBuilder.process(token);

        assertEquals(Token.StartTag.class, token.getClass());
        assertEquals("root", ((Token.StartTag) token).name());
        token = tokenizer.nextToken();

        xmlTreeBuilder.process(token);

        assertEquals(Token.StartTag.class, token.getClass());
        assertEquals("element", ((Token.StartTag) token).name());
        token = tokenizer.nextToken();

        xmlTreeBuilder.process(token);

        assertEquals(Token.EndTag.class, token.getClass());
        assertEquals("element", ((Token.EndTag) token).name());
        token = tokenizer.nextToken();

        assertEquals(Token.EOF.class, token.getClass());
    }
}