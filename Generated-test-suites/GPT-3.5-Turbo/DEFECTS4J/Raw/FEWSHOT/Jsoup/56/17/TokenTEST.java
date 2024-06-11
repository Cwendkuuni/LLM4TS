package org.jsoup.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.BooleanAttribute;
import org.junit.Test;

public class TokenTest {
    @Test
    public void testTokenType() {
        Token.Doctype doctype = new Token.Doctype();
        assertEquals("Doctype", doctype.tokenType());

        Token.StartTag startTag = new Token.StartTag();
        assertEquals("StartTag", startTag.tokenType());

        Token.EndTag endTag = new Token.EndTag();
        assertEquals("EndTag", endTag.tokenType());

        Token.Comment comment = new Token.Comment();
        assertEquals("Comment", comment.tokenType());

        Token.Character character = new Token.Character();
        assertEquals("Character", character.tokenType());

        Token.EOF eof = new Token.EOF();
        assertEquals("EOF", eof.tokenType());
    }

    @Test
    public void testReset() {
        Token.Doctype doctype = new Token.Doctype();
        doctype.reset();
        assertEquals("", doctype.getName());
        assertEquals("", doctype.getPublicIdentifier());
        assertEquals("", doctype.getSystemIdentifier());
        assertFalse(doctype.isForceQuirks());

        Token.StartTag startTag = new Token.StartTag();
        startTag.reset();
        assertNull(startTag.name());
        assertNull(startTag.normalName());
        assertNull(startTag.pendingAttributeName);
        assertEquals("", startTag.pendingAttributeValue.toString());
        assertNull(startTag.pendingAttributeValueS);
        assertFalse(startTag.hasEmptyAttributeValue);
        assertFalse(startTag.hasPendingAttributeValue);
        assertFalse(startTag.isSelfClosing());
        assertNull(startTag.getAttributes());

        Token.EndTag endTag = new Token.EndTag();
        endTag.reset();
        assertNull(endTag.name());
        assertNull(endTag.normalName());
        assertNull(endTag.pendingAttributeName);
        assertEquals("", endTag.pendingAttributeValue.toString());
        assertNull(endTag.pendingAttributeValueS);
        assertFalse(endTag.hasEmptyAttributeValue);
        assertFalse(endTag.hasPendingAttributeValue);
        assertFalse(endTag.isSelfClosing());
        assertNull(endTag.getAttributes());

        Token.Comment comment = new Token.Comment();
        comment.reset();
        assertEquals("", comment.getData());
        assertFalse(comment.bogus);

        Token.Character character = new Token.Character();
        character.reset();
        assertNull(character.getData());
    }

    @Test
    public void testStartTag() {
        Token.StartTag startTag = new Token.StartTag();
        startTag.nameAttr("div", null);
        assertEquals("div", startTag.toString());

        Attributes attributes = new Attributes();
        attributes.put(new Attribute("id", "test"));
        attributes.put(new Attribute("class", "container"));
        startTag.nameAttr("div", attributes);
        assertEquals("<div id=\"test\" class=\"container\">", startTag.toString());
    }

    @Test
    public void testEndTag() {
        Token.EndTag endTag = new Token.EndTag();
        endTag.name("div");
        assertEquals("</div>", endTag.toString());
    }

    @Test
    public void testDoctype() {
        Token.Doctype doctype = new Token.Doctype();
        doctype.reset();
        assertEquals("", doctype.getName());
        assertEquals("", doctype.getPublicIdentifier());
        assertEquals("", doctype.getSystemIdentifier());
        assertFalse(doctype.isForceQuirks());

        doctype.getName();
        doctype.getPublicIdentifier();
        doctype.getSystemIdentifier();
        doctype.isForceQuirks();
    }

    @Test
    public void testComment() {
        Token.Comment comment = new Token.Comment();
        comment.reset();
        assertEquals("", comment.getData());
        assertFalse(comment.bogus);

        comment.getData();
    }

    @Test
    public void testCharacter() {
        Token.Character character = new Token.Character();
        character.reset();
        assertNull(character.getData());

        character.data("Some data");
        assertEquals("Some data", character.toString());
    }

    @Test
    public void testEOF() {
        Token.EOF eof = new Token.EOF();
    }
}