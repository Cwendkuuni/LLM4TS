package org.jsoup.parser;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert. ASSERT_THROW;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import org.hamcrest.CoreMatchers;
import org.hamcrest.MatcherAssert;
import org.jsoup.Jsoup;
import org.jsoup.nodes.*;
import org.jsoup.parser.ParseSettings;
import org.jsoup.parser.Token;
import org.jsoup.parser.Tokenizer;
import org.junit.Test;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

public class XmlTreeBuilderTest {

    private XmlTreeBuilder builder = new XmlTreeBuilder();

    @Test
    public void testBasicParse() {
        String xml = "<root><child tag=\"test\" attr=\"value\"></child></root>";
        Document doc = builder.parse(new StringReader(xml), "baseUrl");

        assertEquals("root", doc.tagName());
        Element root = doc.child(0);
        assertEquals("child", root.tagName());
        assertEquals("test", root.attributes().get("tag"));
        assertEquals("value", root.attributes().get("attr"));
    }

    @Test
    public void testSelfClosingTag() {
        String xml = "<img src=\"image.png\" />";
        Document doc = builder.parse(new StringReader(xml), "baseUrl");

        Element img = doc.child(0);
        assertEquals("img", img.tagName());
        assertTrue(img.isSelfClosing());
    }

    @Test
    public void testComments() {
        String xml = "<!-- This is a comment --><root><child><!-- Another comment --></child></root>";
        Document doc = builder.parse(new StringReader(xml), "baseUrl");

        Comment comment1 = doc.child(0);
        assertEquals("This is a comment", comment1.data());
        Comment comment2 = doc.child(1);
        assertEquals("Another comment", comment2.data());
    }

    @Test
    public void testDocType() {
        String xml = "<!DOCTYPE html SYSTEM \"about:legacy-compat\">";
        Document doc = builder.parse(new StringReader(xml), "baseUrl");

        DocumentType doctype = doc.doctype();
        assertEquals("html", doctype.nodeName());
        assertEquals("about:legacy-compat", doctype.systemId());
    }

    @Test
    public void testEmptyElement() {
        String xml = "<empty />";
        Document doc = builder.parse(new StringReader(xml), "baseUrl");

        Element emptyElement = doc.child(0);
        assertNotNull(emptyElement);
        assertTrue(emptyElement.isSelfClosing());
        assertEquals("", emptyElement.outerHtml());
    }

    @Test
    public void testErrorHandling() {
        String xml = "<html><script>";

        ParseErrorList errors = new ParseErrorList();
        ParseSettings settings = new ParseSettings();

        Tokenizer tokenizer = new Tokenizer(new StringReader(xml), settings, errors, builder);
        tokenizer.nextToken(); // consume the '<'

        ASSERT_THROW.condition(() -> tokenizer.nextToken())
                .that(CoreMatchers.instanceOf(ParseError.class));

        Document doc = builder.parse(new StringReader(xml), "baseUrl");

        assertEquals(1, errors.size());
        ParseError parseError = errors.get(0);
        assertEquals("Unexpected token: START_TAG expected, found SCRIPT_TAG", parseError.getMessage());
    }

    @Test
    public void testParseFragment() {
        String xmlFragment = "<root><child tag=\"test\" attr=\"value\"></child></root>";
        List<Node> nodes = builder.parseFragment(xmlFragment, "baseUrl", new ParseErrorList(), ParseSettings.preserveCase);

        assertEquals(2, nodes.size());
        Node firstNode = nodes.get(0);
        assertEquals("root", firstNode.tagName());
        Element root = (Element) firstNode;
        assertEquals("child", root.child(0).tagName());
        assertEquals("test", root.child(0).attributes().get("tag"));
        assertEquals("value", root.child(0).attributes().get("attr"));
    }

    @Test
    public void testProcessToken() {
        String xml = "<root><child tag=\"test\" attr=\"value\"></child></root>";
        Tokenizer tokenizer = new Tokenizer(new StringReader(xml), new ParseSettings(), new ParseErrorList(), builder);

        List<Token> tokens = new ArrayList<>();
        while (tokenizer.nextToken() != null) {
            tokens.add(tokenizer.current());
        }

        assertEquals(15, tokens.size());

        for (int i = 0; i < tokens.size(); i++) {
            Token token = tokens.get(i);
            switch (token.type) {
                case EOF:
                    assertTrue(token.isMarked());
                    break;
                case StartTag:
                    assertEquals("root", token.asStartTag().name());
                    break;
                case EndTag:
                    assertEquals("root", token.asEndTag().name());
                    break;
                case Doctype:
                    assertNull(token.asDoctype().publicId());
                    assertNull(token.asDoctype().systemId());
                    break;
                case Comment:
                    assertNull(token.asComment().getData());
                    break;
                case Character:
                    assertEquals(',', token.asCharacter().data().charAt(0));
                    break;
                default:
                    throw new AssertionError("Unexpected token type: " + token.type);
            }
        }
    }

    @Test
    public void testPushAndPopStack() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Document doc = new Document();
        doc.outputSettings().syntax(Document.OutputSettings.Syntax.xml);
        builder.initialiseParse(new StringReader(""), "", new ParseErrorList(), ParseSettings.preserveCase);
        builder.stack.add(doc);

        Element element1 = new Element(Tag.valueOf("tag1"), "", null);
        builder.insert(new Token.StartTag(element1));
        builder.stack.add(element1);

        Element element2 = new Element(Tag.valueOf("tag2"), "", null);
        builder.insert(new Token.StartTag(element2));
        builder.stack.add(element2);

        builder.popStackToClose(new Token.EndTag("tag2"));

        assertEquals(2, builder.stack.size());
        assertEquals(element1, builder.stack.get(1));

        builder.popStackToClose(new Token.EndTag("tag1"));

        assertEquals(0, builder.stack.size());
    }
}