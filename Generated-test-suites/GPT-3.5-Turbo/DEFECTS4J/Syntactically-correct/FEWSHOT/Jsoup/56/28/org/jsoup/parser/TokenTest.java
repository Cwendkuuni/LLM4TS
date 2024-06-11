
package org.jsoup.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Attributes;
import org.junit.Test;

public class TokenTest {

    @Test
    public void testDoctypeReset() {
        Token.Doctype doctype = new Token.Doctype();
        doctype.name.append("html");
        doctype.publicIdentifier.append("-//W3C//DTD HTML 4.01 Transitional//EN");
        doctype.systemIdentifier.append("http://www.w3.org/TR/html4/loose.dtd");
        doctype.forceQuirks = true;

        doctype.reset();

        assertEquals("", doctype.name.toString());
        assertEquals("", doctype.publicIdentifier.toString());
        assertEquals("", doctype.systemIdentifier.toString());
        assertFalse(doctype.forceQuirks);
    }

    @Test
    public void testDoctypeGetters() {
        Token.Doctype doctype = new Token.Doctype();
        assertNull(doctype.getName());
        assertNull(doctype.getPublicIdentifier());
        assertNull(doctype.getSystemIdentifier());
        assertFalse(doctype.isForceQuirks());

        doctype.name.append("html");
        doctype.publicIdentifier.append("-//W3C//DTD HTML 4.01 Transitional//EN");
        doctype.systemIdentifier.append("http://www.w3.org/TR/html4/loose.dtd");
        doctype.forceQuirks = true;

        assertEquals("html", doctype.getName());
        assertEquals("-//W3C//DTD HTML 4.01 Transitional//EN", doctype.getPublicIdentifier());
        assertEquals("http://www.w3.org/TR/html4/loose.dtd", doctype.getSystemIdentifier());
        assertTrue(doctype.isForceQuirks());
    }

    @Test
    public void testStartTagReset() {
        Token.StartTag startTag = new Token.StartTag();
        startTag.tagName = "div";
        startTag.normalName = "div";
        startTag.pendingAttributeName = "class";
        startTag.pendingAttributeValue.append("container");
        startTag.pendingAttributeValueS = null;
        startTag.hasEmptyAttributeValue = false;
        startTag.hasPendingAttributeValue = true;
        startTag.selfClosing = false;
        startTag.attributes = new Attributes();
        Attribute attribute1 = new Attribute("class", "container");
        Attribute attribute2 = new Attribute("id", "main");
        startTag.attributes.put(attribute1);
        startTag.attributes.put(attribute2);

        startTag.reset();

        assertNull(startTag.tagName);
        assertNull(startTag.normalName);
        assertNull(startTag.pendingAttributeName);
        assertEquals("", startTag.pendingAttributeValue.toString());
        assertNull(startTag.pendingAttributeValueS);
        assertFalse(startTag.hasEmptyAttributeValue);
        assertFalse(startTag.hasPendingAttributeValue);
        assertFalse(startTag.selfClosing);
        assertNull(startTag.attributes);
    }

    @Test
    public void testEndTagReset() {
        Token.EndTag endTag = new Token.EndTag();
        endTag.tagName = "div";
        endTag.normalName = "div";
        endTag.pendingAttributeName = "class";
        endTag.pendingAttributeValue.append("container");
        endTag.pendingAttributeValueS = null;
        endTag.hasEmptyAttributeValue = false;
        endTag.hasPendingAttributeValue = true;
        endTag.selfClosing = false;
        endTag.attributes = new Attributes();
        Attribute attribute1 = new Attribute("class", "container");
        Attribute attribute2 = new Attribute("id", "main");
        endTag.attributes.put(attribute1);
        endTag.attributes.put(attribute2);

        endTag.reset();

        assertNull(endTag.tagName);
        assertNull(endTag.normalName);
        assertNull(endTag.pendingAttributeName);
        assertEquals("", endTag.pendingAttributeValue.toString());
        assertNull(endTag.pendingAttributeValueS);
        assertFalse(endTag.hasEmptyAttributeValue);
        assertFalse(endTag.hasPendingAttributeValue);
        assertFalse(endTag.selfClosing);
        assertNull(endTag.attributes);
    }

    @Test
    public void testStartTagNameAttr() {
        Token.StartTag startTag = new Token.StartTag();
        Attributes attributes = new Attributes();
        Attribute attribute1 = new Attribute("class", "container");
        Attribute attribute2 = new Attribute("id", "main");
        attributes.put(attribute1);
        attributes.put(attribute2);

        startTag.nameAttr("div", attributes);

        assertEquals("div", startTag.tagName);
        assertEquals("div", startTag.normalName);
        assertEquals("class", startTag.pendingAttributeName);
        assertEquals("container", startTag.pendingAttributeValue.toString());
        assertNull(startTag.pendingAttributeValueS);
        assertFalse(startTag.hasEmptyAttributeValue);
        assertTrue(startTag.hasPendingAttributeValue);
        assertFalse(startTag.selfClosing);
        assertNotNull(startTag.attributes);
        assertEquals(2, startTag.attributes.size());
        assertTrue(startTag.attributes.hasKey("class"));
        assertTrue(startTag.attributes.hasKey("id"));
    }

    @Test
    public void testStartTagToString() {
        Token.StartTag startTag1 = new Token.StartTag();
        assertEquals("<>", startTag1.toString());

        Token.StartTag startTag2 = new Token.StartTag();
        startTag2.tagName = "div";
        startTag2.normalName = "div";
        startTag2.attributes = new Attributes();
        Attribute attribute1 = new Attribute("class", "container");
        Attribute attribute2 = new Attribute("id", "main");
        startTag2.attributes.put(attribute1);
        startTag2.attributes.put(attribute2);
        assertEquals("<div class=\"container\" id=\"main\">", startTag2.toString());
    }

    @Test
    public void testEndTagToString() {
        Token.EndTag endTag = new Token.EndTag();
        endTag.tagName = "div";
        endTag.normalName = "div";
        assertEquals("</div>", endTag.toString());
    }

    @Test
    public void testCommentReset() {
        Token.Comment comment = new Token.Comment();
        comment.data.append("This is a comment");
        comment.bogus = true;

        comment.reset();

        assertEquals("", comment.data.toString());
        assertFalse(comment.bogus);
    }

    @Test
    public void testCommentGetData() {
        Token.Comment comment = new Token.Comment();
        assertNull(comment.getData());

        comment.data.append("This is a comment");

        assertEquals("This is a comment", comment.getData());
    }

    @Test
    public void testCharacterReset() {
        Token.Character character = new Token.Character();
        character.data("This is some text");

        character.reset();

        assertNull(character.getData());
    }

    @Test
    public void testCharacterGetData() {
        Token.Character character = new Token.Character();
        assertNull(character.getData());

        character.data("This is some text");

        assertEquals("This is some text", character.getData());
    }

    @Test
    public void testEOFReset() {
        Token.EOF eof = new Token.EOF();

        eof.reset();

        assertNotNull(eof);
    }
}
