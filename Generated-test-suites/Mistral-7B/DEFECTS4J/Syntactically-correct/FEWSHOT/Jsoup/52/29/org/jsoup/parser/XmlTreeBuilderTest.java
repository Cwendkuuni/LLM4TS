package org.jsoup.parser;

import static org.junit.Assert.*;
import static org.jsoup.helper.Validate.*;

import org.junit.Test;
import org.jsoup.nodes.*;
import org.jsoup.parser.Token;

import java.util.List;

public class XmlTreeBuilderTest {
    private XmlTreeBuilder parser;

    @Test
    public void testParseFragment() {
        String xmlString = "<root><element attr1=\"val1\" attr2=\"val2\"/><element2/></root>";
        parser = new XmlTreeBuilder();
        List<Node> parseResult = parser.parseFragment(xmlString, null, new ParseErrorList());

        Document doc = (Document) parseResult.get(0);
        Element root = (Element) doc.child(0);

        assertEquals("root", root.nodeName());
        assertEquals(2, root.childNodeSize());

        Element element = (Element) root.child(0);
        assertEquals("element", element.nodeName());
        assertEquals(2, element.childNodeSize());

        Attribute attr1 = element.attr("attr1");
        assertEquals("val1", attr1.toString());

        Attribute attr2 = element.attr("attr2");
        assertEquals("val2", attr2.toString());

        Element element2 = (Element) root.child(1);
        assertEquals("element2", element2.nodeName());
        assertEquals(0, element2.childNodeSize());
    }

    @Test
    public void testProcessTokenStartTag() {
        parser = new XmlTreeBuilder();
        Token token = new Token(Token.StartTag.class, "<root>", null, 0, 0);
        parser.process(token);

        Document doc = parser.getDocument();
        Element root = doc.getRootElement();

        assertEquals("root", root.nodeName());
    }

    @Test
    public void testProcessTokenEndTag() {
        parser = new XmlTreeBuilder();
        Token token = new Token(Token.EndTag.class, "</root>", null, 0, 0);
        parser.process(token);

        assertNull(parser.getDocument());
    }

    @Test
    public void testProcessTokenComment() {
        parser = new XmlTreeBuilder();
        Token token = new Token(Token.Comment.class, "-- This is a comment --", null, 0, 0);
        parser.process(token);

        Comment comment = parser.getLastParsed();

        assertEquals("-- This is a comment --", comment.data());
    }

    @Test
    public void testProcessTokenCharacter() {
        parser = new XmlTreeBuilder();
        Token token = new Token(Token.Character.class, "&lt;", null, 0, 0);
        parser.process(token);

        TextNode textNode = parser.getLastParsed();

        assertEquals("<", textNode.text());
    }

    @Test
    public void testProcessTokenDoctype() {
        parser = new XmlTreeBuilder();
        String xmlDeclaration = "<!DOCTYPE root [ <!ATTLIST element attr1 CDATA #REQUIRED > ]>";
        Token token = new Token(Token.Doctype.class, xmlDeclaration, null, 0, 0);
        parser.process(token);

        DocumentType documentType = parser.getLastParsed();

        assertEquals("root", documentType.nodeName());
        assertEquals("root", documentType.name());
    }

    @Test(expected = AssertionError.class)
    public void testProcessTokenUnexpectedType() {
        parser = new XmlTreeBuilder();
        Token token = new Token(1234, null, null, 0, 0); // unexpected token type
        parser.process(token);

        // should fail with AssertionError
    }
}