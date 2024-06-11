package org.jsoup.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Comment;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Doctype;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.parser.Token;
import org.jsoup.parser.Token.CommentToken;
import org.jsoup.parser.Token.CharacterToken;
import org.jsoup.parser.Token.DoctypeToken;
import org.jsoup.parser.Token.EndTag;
import org.jsoup.parser.Token.StartTag;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class XmlTreeBuilderTest {

    @Test
    public void testXmlTreeBuilderDefaultSettings() {
        String xmlString = "<root><child></child><anotherChild></anotherChild></root>";
        Document xmlDoc = Jsoup.parse(xmlString, Parser.xmlParser());

        XmlTreeBuilder builder = new XmlTreeBuilder();
        Document parsedXml = builder.parse(xmlString, "");

        assertEquals(xmlDoc.rootElement(), parsedXml.rootElement());
    }

    @Test
    public void testXmlTreeBuilderParseWithBaseUri() {
        String xmlString = "<root xmlns:xsi='http://www.w3.org/2001/XMLSchema-instance' xmlns:xsd='http://www.w3.org/2001/XMLSchema'><child xsi:type='xsd:string'><text>Hello World</text></child></root>";
        String baseUri = "http://example.com/";

        Document xmlDoc = Jsoup.parse(xmlString, Parser.xmlParser(), baseUri);

        XmlTreeBuilder builder = new XmlTreeBuilder();
        Document parsedXml = builder.parse(xmlString, baseUri);

        assertEquals(xmlDoc.rootElement().tagName(), parsedXml.rootElement().tagName());
        assertEquals(xmlDoc.rootElement().baseUri(), parsedXml.rootElement().baseUri());
        assertEquals(xmlDoc.rootElement().attributes().size(), parsedXml.rootElement().attributes().size());
        for (Attribute attr : xmlDoc.rootElement().attributes()) {
            Attribute parsedAttr = parsedXml.rootElement().attribute(attr.key());
            assertEquals(attr.key(), parsedAttr.key());
            assertEquals(attr.value(), parsedAttr.value());
        }
    }

    @Test
    public void testXmlTreeBuilderParseWithParseErrorList() {
        String xmlString = "<root><child><text>Hello World</text><</child></root>";
        Document xmlDoc = Jsoup.parse(xmlString, Parser.xmlParser(), ParseErrorList.empty());
        ParseErrorList errors = new ParseErrorList();

        XmlTreeBuilder builder = new XmlTreeBuilder();
        Document parsedXml = builder.parse(xmlString, "", errors);

        assertEquals(xmlDoc.rootElement(), parsedXml.rootElement());
        assertEquals(errors.size(), xmlDoc.outputSettings().settings().parseErrorCount());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testXmlTreeBuilderNullInput() {
        new XmlTreeBuilder().parse(null, "");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testXmlTreeBuilderNullBaseUri() {
        new XmlTreeBuilder().parse("", null);
    }

    @Test(expected = NullPointerException.class)
    public void testXmlTreeBuilderNullParseErrorList() {
        new XmlTreeBuilder().parse("", "", null);
    }

    @Test
    public void testInsertStartTag() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        String xmlString = "<root><child></child><anotherChild></anotherChild></root>";

        Element rootElement = new Element("root", "");
        builder.insert(new StartTag("root"));
        assertSame(rootElement, builder.currentElement());

        Element childElement = new Element("child", "");
        builder.insert(new StartTag("child"));
        assertSame(childElement, builder.currentElement());

        Element anotherChildElement = new Element("anotherChild", "");
        builder.insert(new StartTag("anotherChild"));
        assertSame(anotherChildElement, builder.currentElement());
    }

    @Test
    public void testInsertEndTag() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        String xmlString = "<root><child><text>Hello World</text></child></root>";

        Element rootElement = new Element("root", "");
        Element childElement = new Element("child", "");
        builder.insert(new StartTag("root"));
        builder.insert(new StartTag("child"));
        builder.insert(new CharacterToken(' '));
        builder.insert(new TextNode("Hello World"));
        builder.insert(new EndTag("child"));
        assertSame(childElement, builder.currentElement());
        assertSame(rootElement, builder.parent());
    }

    @Test
    public void testInsertComment() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        String xmlString = "<root><!-- This is a comment --><child></child></root>";

        Element rootElement = new Element("root", "");
        Comment comment = new Comment("This is a comment");
        builder.insert(new CommentToken("<!-- This is a comment -->"));
        assertSame(comment, builder.currentElement());
        assertSame(rootElement, builder.parent());
    }

    @Test
    public void testInsertCharacter() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        String xmlString = "<root><text>Hello World</text></root>";

        Element rootElement = new Element("root", "");
        TextNode textNode = new TextNode("Hello World");
        builder.insert(new CharacterToken('H'));
        builder.insert(new CharacterToken('e'));
        builder.insert(new CharacterToken('l'));
        builder.insert(new CharacterToken('l'));
        builder.insert(new CharacterToken('o'));
        builder.insert(new TextNode(" World"));
        assertSame(textNode, builder.currentElement());
        assertSame(rootElement, builder.parent());
    }

    @Test
    public void testInsertDoctype() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        String xmlString = "<!DOCTYPE root [ <!ATTLIST child attr CDATA #REQUIRED > ]>";

        Document doctypeDocument = Jsoup.parse("<!DOCTYPE root [ <!ATTLIST child attr CDATA #REQUIRED > ]>", "", Parser.xmlParser());

        DocumentType doctypeNode = new DocumentType("root", null, null, "");
        builder.insert(new DoctypeToken("<!DOCTYPE root [ <!ATTLIST child attr CDATA #REQUIRED > ]>"));
        assertSame(doctypeNode, builder.currentElement());
    }

    @Test
    public void testPopStackToCloseEndTag() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        String xmlString = "<root><child></child><anotherChild></anotherChild></root>";

        Element rootElement = new Element("root", "");
        Element childElement = new Element("child", "");
        Element anotherChildElement = new Element("anotherChild", "");

        builder.insert(new StartTag("root"));
        builder.insert(new StartTag("child"));
        builder.insert(new EndTag("child"));
        builder.insert(new StartTag("anotherChild"));

        List<Element> stack = builder.stack;
        assertEquals(3, stack.size());
        assertSame(anotherChildElement, stack.get(0));
        assertSame(childElement, stack.get(1));
        assertSame(rootElement, stack.get(2));

        builder.popStackToClose(new EndTag("child"));
        assertEquals(2, stack.size());
        assertSame(anotherChildElement, stack.get(0));
        assertSame(rootElement, stack.get(1));
    }

    @Test
    public void testParseFragment() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        String xmlFragment = "<root><child><text>Hello World</text></child></root>";

        List<Node> expectedNodes = new ArrayList<>();
        expectedNodes.add(new Element("root"));
        expectedNodes.add(new Element("child"));
        expectedNodes.add(new TextNode("Hello World"));

        List<Node> parsedNodes = builder.parseFragment(xmlFragment, "", ParseErrorList.noTracking(), ParseSettings.preserveCase);
        assertEquals(expectedNodes.size(), parsedNodes.size());
        for (int i = 0; i < expectedNodes.size(); i++) {
            Node expectedNode = expectedNodes.get(i);
            Node parsedNode = parsedNodes.get(i);
            assertSame(expectedNode.getClass(), parsedNode.getClass());
            if (expectedNode instanceof Element) {
                assertEquals(((Element) expectedNode).tagName(), ((Element) parsedNode).tagName());
                assertEquals(((Element) expectedNode).baseUri(), ((Element) parsedNode).baseUri());
                assertEquals(((Element) expectedNode).attributes().size(), ((Element) parsedNode).attributes().size());
                for (Attribute attr : ((Element) expectedNode).attributes()) {
                    Attribute parsedAttr = ((Element) parsedNode).attribute(attr.key());
                    assertEquals(attr.key(), parsedAttr.key());
                    assertEquals(attr.value(), parsedAttr.value());
                }
            }
        }
    }
}