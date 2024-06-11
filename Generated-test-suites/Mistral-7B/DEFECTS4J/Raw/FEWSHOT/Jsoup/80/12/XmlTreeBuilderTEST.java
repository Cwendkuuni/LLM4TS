package org.jsoup.parser;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;

import org.jsoup.Jsoup;
import org.jsoup.nodes.*;
import org.jsoup.helper.Validate;
import org.jsoup.parser.Token;
import org.jsoup.parser.Tokenizer;
import org.junit.Test;

public class XmlTreeBuilderTest {

    @Test
    public void testParse() {
        String xml = "<root><element attr='value'><subElement attr2='value2'></subElement></element></root>";
        Document doc = XmlTreeBuilder.parse(new StringReader(xml), "");
        assertNotNull(doc);
        Element root = doc.rootElement();
        assertEquals("root", root.nodeName());
        List<Element> elements = root.children();
        assertEquals(1, elements.size());
        Element element = elements.get(0);
        assertEquals("element", element.nodeName());
        List<Node> subElements = element.children();
        assertEquals(1, subElements.size());
        Element subElement = (Element) subElements.get(0);
        assertEquals("subElement", subElement.nodeName());
        assertNull(subElement.attr("attr2"));
        assertEquals("value2", subElement.attr("attr2").orElse(""));
        assertEquals("value", element.attr("attr"));
    }

    @Test
    public void testParseWithEmptyDocument() {
        Document doc = XmlTreeBuilder.parse(new StringReader(""), "");
        assertNotNull(doc);
        assertEquals(0, doc.childNodes().size());
    }

    @Test
    public void testParseWithEmptyInputFragment() {
        List<Node> nodes = XmlTreeBuilder.parseFragment("", "", ParseErrorList.noTracking(), ParseSettings.preserveCase).childNodes();
        assertEquals(0, nodes.size());
    }

    @Test
    public void testProcessStartTag() {
        Tokenizer tokenizer = new Tokenizer("<?xml version='1.0' encoding='UTF-8'?><root><element attr='value'><subElement attr2='value2'></subElement></element></root>");
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        xmlTreeBuilder.initialiseParse(tokenizer.input(), "", ParseErrorList.noTracking(), ParseSettings.preserveCase);

        Token token = null;
        while ((token = tokenizer.next()) != null) {
            xmlTreeBuilder.process(token);
        }

        Element root = xmlTreeBuilder.doc.rootElement();
        assertEquals("root", root.nodeName());
        List<Element> elements = root.children();
        assertEquals(1, elements.size());
        Element element = elements.get(0);
        assertEquals("element", element.nodeName());
        List<Node> subElements = element.children();
        assertEquals(1, subElements.size());
        Element subElement = (Element) subElements.get(0);
        assertEquals("subElement", subElement.nodeName());
        assertNull(subElement.attr("attr2"));
        assertEquals("value2", subElement.attr("attr2").orElse(""));
        assertEquals("value", element.attr("attr"));
    }

    @Test
    public void testProcessEndTag() {
        Tokenizer tokenizer = new Tokenizer("<root><element><subElement></subElement></element></root>");
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        xmlTreeBuilder.initialiseParse(tokenizer.input(), "", ParseErrorList.noTracking(), ParseSettings.preserveCase);

        Token token = null;
        while ((token = tokenizer.next()) != null) {
            xmlTreeBuilder.process(token);
        }

        assertEquals(2, xmlTreeBuilder.stack.size());
        XmlTreeBuilder.XmlTreeBuilderStack stack = xmlTreeBuilder.stack;
        Element root = (Element) stack.get(0);
        Element element = (Element) stack.get(1);

        xmlTreeBuilder.popStackToClose(new Token.EndTag("subElement"));
        assertEquals(1, stack.size());
        assertEquals(root, stack.get(0));
    }

    @Test
    public void testProcessComment() {
        Tokenizer tokenizer = new Tokenizer("<root><!--comment--></root>");
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        xmlTreeBuilder.initialiseParse(tokenizer.input(), "", ParseErrorList.noTracking(), ParseSettings.preserveCase);

        Token token = null;
        while ((token = tokenizer.next()) != null) {
            xmlTreeBuilder.process(token);
        }

        List<Node> comments = xmlTreeBuilder.doc.childNodes();
        assertEquals(1, comments.size());
        Comment comment = (Comment) comments.get(0);
        assertEquals("comment", comment.nodeName());
        assertEquals("", comment.text());
    }

    @Test
    public void testProcessCharacter() {
        Tokenizer tokenizer = new Tokenizer("<?xml version='1.0' encoding='UTF-8'?><root><element><![CDATA[abcd]]></element></root>");
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        xmlTreeBuilder.initialiseParse(tokenizer.input(), "", ParseErrorList.noTracking(), ParseSettings.preserveCase);

        Token token = null;
        while ((token = tokenizer.next()) != null) {
            xmlTreeBuilder.process(token);
        }

        List<Node> textNodes = xmlTreeBuilder.doc.childNodes();
        assertEquals(1, textNodes.size());
        TextNode textNode = (TextNode) textNodes.get(0);
        assertEquals("abcd", textNode.text());
    }

    @Test
    public void testProcessDoctype() {
        Tokenizer tokenizer = new Tokenizer("<!DOCTYPE root [ <!ATTLIST element attr CDATA #REQUIRED > ] ><root></root>");
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        xmlTreeBuilder.initialiseParse(tokenizer.input(), "", ParseErrorList.noTracking(), ParseSettings.preserveCase);

        Token token = null;
        while ((token = tokenizer.next()) != null) {
            xmlTreeBuilder.process(token);
        }

        Document doc = xmlTreeBuilder.doc;
        DocumentType documentType = doc.doctype();
        assertEquals("root", documentType.nodeName());
        assertEquals("", documentType.getPublicIdentifier());
        assertEquals("", documentType.getSystemIdentifier());
        assertEquals(null, documentType.getPubSysKey());
        List<Attribute> attributes = documentType.attributes();
        assertEquals(1, attributes.size());
        Attribute attribute = attributes.get(0);
        assertEquals("element", attribute.getName());
        assertEquals("CDATA", attribute.getType());
        assertEquals("attr", attribute.getName());
        assertEquals("REQUIRED", attribute.getValue());
    }

    @Test(expected = AssertionError.class)
    public void testProcessUnexpectedTokenType() {
        Tokenizer tokenizer = new Tokenizer("<root><![CDATA[abcd]]></root>");
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        xmlTreeBuilder.initialiseParse(tokenizer.input(), "", ParseErrorList.noTracking(), ParseSettings.preserveCase);

        Token token = null;
        while ((token = tokenizer.next()) != null) {
            xmlTreeBuilder.process(token);
        }

        Validate.fail("Unexpected token type: " + Token.TokenType.CDATA); // This assertion should fail
    }
}