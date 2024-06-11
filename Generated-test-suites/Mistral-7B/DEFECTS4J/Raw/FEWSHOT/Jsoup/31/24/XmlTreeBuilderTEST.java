package org.jsoup.parser;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.jsoup.nodes.*;
import org.junit.Test;

public class XmlTreeBuilderTest {

    @Test
    public void testInitialiseParse() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        builder.initialiseParse("", "", null);
        assertNotNull(builder.doc);
    }

    @Test
    public void testProcessStartTag() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Token startTag = new Token.StartTag("root", null, new Attribute("id", "1"));
        Element root = builder.insert(startTag);
        assertNotNull(root);
        assertEquals("root", root.nodeName());
        assertTrue(root.hasAttr("id"));
        assertEquals("1", root.attr("id"));
    }

    @Test
    public void testProcessEndTag() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Token startTag = new Token.StartTag("root", null, new Attribute("id", "1"));
        Element root = builder.insert(startTag);
        Token endTag = new Token.EndTag("root");
        builder.popStackToClose(endTag);
        assertFalse(root.parentNode() != null);
    }

    @Test
    public void testProcessComment() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Token commentToken = new Token.Comment("this is a comment");
        builder.insert(commentToken);
        assertEquals("this is a comment", builder.doc.body().text());
    }

    @Test
    public void testProcessCharacter() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Token characterToken = new Token.Character('a');
        builder.insert(characterToken);
        Element root = builder.doc.body();
        Text text = (Text) root.child(0);
        assertEquals("a", text.text());
    }

    @Test
    public void testProcessDoctype() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Token doctypeToken = new Token.Doctype("html", "<!DOCTYPE html SYSTEM \"about:legacy-compat\">", null, "");
        builder.insert(doctypeToken);
        DocumentType doctypeNode = builder.doc.doctype();
        assertEquals("html", doctypeNode.name());
        assertEquals("<!DOCTYPE html SYSTEM \"about:legacy-compat\">", doctypeNode.data());
        assertEquals("", doctypeNode.publicId());
        assertEquals("", doctypeNode.systemId());
    }

    @Test
    public void testPopStackToClose() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Element root = new Element("root", "");
        Element child1 = new Element("child1", "");
        root.appendChild(child1);
        builder.stack.add(root);
        Token endTag = new Token.EndTag("root");
        builder.popStackToClose(endTag);
        assertFalse(root.parentNode() != null);
    }

    @Test(expected = AssertionError.class)
    public void testUnexpectedTokenType() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Token token = new Token(-1, null);
        builder.process(token);
    }
}