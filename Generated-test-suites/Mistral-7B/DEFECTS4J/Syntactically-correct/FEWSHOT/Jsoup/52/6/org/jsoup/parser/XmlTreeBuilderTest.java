package org.jsoup.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Comment;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.DocumentType;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.parser.Token;
import org.jsoup.parser.Token.AttributeToken;
import org.jsoup.parser.Token.CommentToken;
import org.jsoup.parser.Token.DoctypeToken;
import org.jsoup.parser.Token.EndTagToken;
import org.jsoup.parser.Token.StartTagToken;
import org.jsoup.parser.Tokenizer;

public class XmlTreeBuilderTest {

    private Tokenizer tokeniser;
    private XmlTreeBuilder builder;
    private Document doc;

    @Before
    public void setUp() {
        tokeniser = new Tokenizer();
        builder = new XmlTreeBuilder();
        doc = new Document();
        builder.initialiseParse("", "", Collections.emptyList());
        builder.doc = doc;
    }

    @Test
    public void testProcessStartTag() {
        Token startTagToken = new StartTagToken("tag", null, "class", "attr", null);

        Element expectedElement = new Element("tag", "", Collections.singletonList(new Attribute("class", "attr")));

        builder.process(startTagToken);

        Element actualElement = builder.currentElement();
        assertEquals(expectedElement.nodeName(), actualElement.nodeName());
        assertEquals(expectedElement.attributes().get("class"), actualElement.attributes().get("class"));
    }

    @Test
    public void testProcessSelfClosingStartTag() {
        Token startTagToken = new StartTagToken("tag", null, null, null, true);

        Element expectedElement = new Element("tag");

        builder.process(startTagToken);

        Element actualElement = builder.currentElement();
        assertEquals(expectedElement.nodeName(), actualElement.nodeName());
        assertTrue(actualElement.hasAttrs()); // self closing tag may still have attributes in some cases
    }

    @Test
    public void testProcessEndTag() {
        Token endTagToken = new EndTagToken("tag");

        Element currentElement = new Element("tag");
        builder.stack.add(currentElement);
        builder.currentElement(currentElement);

        builder.process(endTagToken);

        assertEquals("", builder.stack.size());
    }

    @Test
    public void testProcessEndTagUnknownTag() {
        Token endTagToken = new EndTagToken("unknown");

        Element currentElement = new Element("tag");
        builder.stack.add(currentElement);
        builder.currentElement(currentElement);

        builder.process(endTagToken);

        assertSame(currentElement, builder.currentElement());
        assertEquals("unknown", currentElement.nodeName());
        assertTrue(currentElement.hasAttrs());
        assertEquals(1, builder.stack.size());
    }

    @Test
    public void testProcessComment() {
        Token commentToken = new CommentToken("comment text");

        Comment expectedComment = new Comment("comment text");

        builder.process(commentToken);

        Comment actualComment = builder.lastAdded();
        assertEquals(expectedComment.getText(), actualComment.getText());
    }

    @Test
    public void testProcessCharacter() {
        Token characterToken = new CharacterToken('c');

        TextNode expectedNode = new TextNode("c");

        builder.process(characterToken);

        Node actualNode = builder.lastAdded();
        assertEquals(expectedNode.text(), actualNode.text());
    }

    @Test
    public void testProcessDoctype() {
        Token doctypeToken = new DoctypeToken("doctype", "", "", "");

        DocumentType expectedNode = new DocumentType("doctype", "", "", "");

        builder.process(doctypeToken);

        Node actualNode = builder.lastAdded();
        assertEquals(expectedNode.nodeName(), actualNode.nodeName());
        assertEquals(expectedNode.internalSubset(), actualNode.internalSubset());
        assertEquals(expectedNode.externalSubset(), actualNode.externalSubset());
        assertEquals(expectedNode.systemId(), actualNode.systemId());
    }

    @Test
    public void testParseFragment() {
        String inputFragment = "<tag attr=\"value\" /> <tag2 attr1=\"value1\" /> <comment>comment text</comment> <text>text</text>";
        List<Node> expectedFragment = new ArrayList<>();
        expectedFragment.add(new Element("tag", null, Collections.singletonList(new Attribute("attr", "value"))));
        expectedFragment.add(new Element("tag2", null, Collections.singletonList(new Attribute("attr1", "value1"))));
        expectedFragment.add(new Comment("comment text"));
        expectedFragment.add(new TextNode("text"));

        List<Node> actualFragment = builder.parseFragment(inputFragment, "", Collections.emptyList());

        assertEquals(expectedFragment.size(), actualFragment.size());
        for (int i = 0; i < expectedFragment.size(); i++) {
            Node expectedNode = expectedFragment.get(i);
            Node actualNode = actualFragment.get(i);
            assertEquals(expectedNode.nodeName(), actualNode.nodeName());
            if (expectedNode instanceof Element) {
                Element expectedElement = (Element) expectedNode;
                Element actualElement = (Element) actualNode;
                assertEquals(expectedElement.attributes().size(), actualElement.attributes().size());
                for (Attribute attr : expectedElement.attributes()) {
                    Attribute actualAttr = actualElement.attribute(attr.getKey());
                    assertEquals(attr.getValue(), actualAttr.getValue());
                }
            }
        }
    }

    @Test
    public void testParseFragmentWithXmlDeclaration() {
        String inputFragment = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<tag attr=\"value\" /> <tag2 attr1=\"value1\" /> <comment>comment text</comment> <text>text</text>";
        List<Node> expectedFragment = new ArrayList<>();
        expectedFragment.add(new XmlDeclaration("1.0", "UTF-8", isStandaloneDoc(inputFragment)));
        expectedFragment.add(new Element("tag", null, Collections.singletonList(new Attribute("attr", "value"))));
        expectedFragment.add(new Element("tag2", null, Collections.singletonList(new Attribute("attr1", "value1"))));
        expectedFragment.add(new Comment("comment text"));
        expectedFragment.add(new TextNode("text"));

        List<Node> actualFragment = builder.parseFragment(inputFragment, "", Collections.emptyList());

        assertEquals(expectedFragment.size(), actualFragment.size());
        for (int i = 0; i < expectedFragment.size(); i++) {
            Node expectedNode = expectedFragment.get(i);
            Node actualNode = actualFragment.get(i);
            if (expectedNode instanceof XmlDeclaration) {
                XmlDeclaration expectedXmlDeclaration = (XmlDeclaration) expectedNode;
                XmlDeclaration actualXmlDeclaration = (XmlDeclaration) actualNode;
                assertEquals(expectedXmlDeclaration.version(), actualXmlDeclaration.version());
                assertEquals(expectedXmlDeclaration.encoding(), actualXmlDeclaration.encoding());
                assertEquals(expectedXmlDeclaration.isStandalone(), actualXmlDeclaration.isStandalone());
            } else {
                assertEquals(expectedNode.nodeName(), actualNode.nodeName());
                if (expectedNode instanceof Element) {
                    Element expectedElement = (Element) expectedNode;
                    Element actualElement = (Element) actualNode;
                    assertEquals(expectedElement.attributes().size(), actualElement.attributes().size());
                    for (Attribute attr : expectedElement.attributes()) {
                        Attribute actualAttr = actualElement.attribute(attr.getKey());
                        assertEquals(attr.getValue(), actualAttr.getValue());
                    }
                }
            }
        }
    }

    @Test
    public void testParseFragmentWithBogusXmlDeclaration() {
        String inputFragment = "<!DOCTYPE xml PUBLIC \"-//W3C//DTD XML 1.0//EN\" \"xml.dtd\">\n<tag attr=\"value\" /> <tag2 attr1=\"value1\" /> <comment>comment text</comment> <text>text</text>";
        List<Node> expectedFragment = new ArrayList<>();
        expectedFragment.add(new Element("tag", null, Collections.singletonList(new Attribute("attr", "value"))));
        expectedFragment.add(new Element("tag2", null, Collections.singletonList(new Attribute("attr1", "value1"))));
        expectedFragment.add(new Comment("comment text"));
        expectedFragment.add(new TextNode("text"));
        expectedFragment.add(new XmlDeclaration("xml", "", true));

        List<Node> actualFragment = builder.parseFragment(inputFragment, "", Collections.emptyList());

        assertEquals(expectedFragment.size(), actualFragment.size());
        for (int i = 0; i < expectedFragment.size(); i++) {
            Node expectedNode = expectedFragment.get(i);
            Node actualNode = actualFragment.get(i);
            if (expectedNode instanceof XmlDeclaration) {
                XmlDeclaration expectedXmlDeclaration = (XmlDeclaration) expectedNode;
                XmlDeclaration actualXmlDeclaration = (XmlDeclaration) actualNode;
                assertEquals(expectedXmlDeclaration.nodeName(), actualXmlDeclaration.nodeName());
                assertEquals(expectedXmlDeclaration.version(), actualXmlDeclaration.version());
                assertEquals(expectedXmlDeclaration.encoding(), actualXmlDeclaration.encoding());
                assertEquals(expectedXmlDeclaration.isStandalone(), actualXmlDeclaration.isStandalone());
            } else {
                assertEquals(expectedNode.nodeName(), actualNode.nodeName());
                if (expectedNode instanceof Element) {
                    Element expectedElement = (Element) expectedNode;
                    Element actualElement = (Element) actualNode;
                    assertEquals(expectedElement.attributes().size(), actualElement.attributes().size());
                    for (Attribute attr : expectedElement.attributes()) {
                        Attribute actualAttr = actualElement.attribute(attr.getKey());
                        assertEquals(attr.getValue(), actualAttr.getValue());
                    }
                }
            }
        }
    }

    private boolean isStandaloneDoc(String input) {
        // assuming the input contains a valid xml declaration at the beginning
        int versionIndex = input.indexOf("version=\"");
        int encodingIndex = input.indexOf("encoding=\"");
        return input.regionMatches(true, encodingIndex + 9, "standalone=\"yes\"");
    }
}