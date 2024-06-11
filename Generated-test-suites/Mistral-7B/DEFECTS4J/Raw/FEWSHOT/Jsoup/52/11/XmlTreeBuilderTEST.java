package org.jsoup.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.jsoup.nodes.*;
import org.jsoup.parser.Token;
import org.jsoup.parser.Tokenizer;

import java.util.List;

public class XmlTreeBuilderTest {
    private XmlTreeBuilder builder;
    private Tokenizer tokenizer;
    private Document doc;

    @SuppressWarnings("unchecked")
    @Before
    public void setUp() {
        builder = new XmlTreeBuilder();
        tokenizer = new Tokenizer();
        doc = new Document();
        builder.initialiseParse("", "", new ParseErrorList());
        builder.doc = doc;
    }

    @Test
    public void testProcessStartTag() {
        Token token = new Token(Token.StartTag, "<root/>");
        builder.process(token);
        Element root = (Element) builder.stack.get(builder.stack.size() - 1);
        assertEquals("root", root.tagName());
        assertEquals("", root.baseUri());
        assertEquals(0, root.attributes().size());
        assertTrue(root.isSelfClosing());
    }

    @Test
    public void testProcessStartTagWithAttributes() {
        Token token = new Token(Token.StartTag, "<element attr1='value1' attr2='value2'/>");
        builder.process(token);
        Element element = (Element) builder.stack.get(builder.stack.size() - 1);
        assertEquals("element", element.tagName());
        assertEquals("", element.baseUri());
        Map<String, Attribute> attributes = element.attributes();
        assertEquals(2, attributes.size());
        assertEquals("value1", attributes.get("attr1").getValue());
        assertEquals("value2", attributes.get("attr2").getValue());
        assertFalse(element.isSelfClosing());
    }

    @Test
    public void testProcessEndTag() {
        Element root = new Element("root", "", null);
        builder.stack.add(root);
        Token token = new Token(Token.EndTag, "</root>");
        builder.popStackToClose(token);
        assertNull(builder.stack.get(builder.stack.size() - 1));
    }

    @Test
    public void testProcessEndTagUnknownTag() {
        Element root = new Element("root", "", null);
        builder.stack.add(root);
        Token token = new Token(Token.EndTag, "</unknown>");
        builder.popStackToClose(token);
        assertEquals(root, builder.stack.get(builder.stack.size() - 1));
    }

    @Test
    public void testProcessComment() {
        Token token = new Token(Token.Comment, "<!-- comment -->");
        builder.process(token);
        Comment comment = (Comment) builder.stack.get(builder.stack.size() - 1);
        assertEquals("comment", comment.data());
        assertEquals("", comment.baseUri());
    }

    @Test
    public void testProcessCharacter() {
        Token token = new Token(Token.Character, "text");
        builder.process(token);
        TextNode textNode = (TextNode) builder.stack.get(builder.stack.size() - 1);
        assertEquals("text", textNode.text());
        assertEquals("", textNode.baseUri());
    }

    @Test
    public void testProcessDoctype() {
        Token token = new Token(Token.Doctype, "<!DOCTYPE html SYSTEM \"about:legacy-compat\">");
        builder.process(token);
        DocumentType doctypeNode = (DocumentType) builder.stack.get(builder.stack.size() - 1);
        assertEquals("html", doctypeNode.name());
        assertEquals("about:legacy-compat", doctypeNode.publicIdentifier());
        assertEquals(null, doctypeNode.systemIdentifier());
        assertEquals("", doctypeNode.baseUri());
    }

    @Test
    public void testParseFragment() {
        String xml = "<root><element attr1='value1' attr2='value2'></element></root>";
        List<Node> nodes = builder.parseFragment(xml, "", new ParseErrorList());
        Element root = (Element) nodes.get(0);
        assertEquals("root", root.tagName());
        assertEquals(1, root.childNodes().size());
        Element element = (Element) root.childNodes().get(0);
        assertEquals("element", element.tagName());
        assertEquals(2, element.childNodes().size());
        TextNode textNode1 = (TextNode) element.childNodes().get(0);
        assertEquals("", textNode1.baseUri());
        assertEquals("", textNode1.text());
        TextNode textNode2 = (TextNode) element.childNodes().get(1);
        assertEquals("", textNode2.baseUri());
        assertEquals("", textNode2.text());
    }
}