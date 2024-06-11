
package org.jsoup.parser;

import org.jsoup.helper.Validate;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Attributes;

import static org.junit.Assert.*;

public class TokenTest {
    @org.junit.Test
    public void testTokenType() {
        Token.Doctype doctypeToken = new Token.Doctype();
        Token.StartTag startTagToken = new Token.StartTag();
        Token.EndTag endTagToken = new Token.EndTag();
        Token.Comment commentToken = new Token.Comment();
        Token.Character characterToken = new Token.Character("");
        Token.EOF eofToken = new Token.EOF();

        assertEquals("Doctype", doctypeToken.tokenType());
        assertEquals("StartTag", startTagToken.tokenType());
        assertEquals("EndTag", endTagToken.tokenType());
        assertEquals("Comment", commentToken.tokenType());
        assertEquals("Character", characterToken.tokenType());
        assertEquals("EOF", eofToken.tokenType());
    }

    @org.junit.Test
    public void testDoctypeGetName() {
        Token.Doctype doctypeToken = new Token.Doctype();
        assertEquals("", doctypeToken.getName());

        doctypeToken.name.append("HTML");
        assertEquals("HTML", doctypeToken.getName());
    }

    @org.junit.Test
    public void testDoctypeGetPublicIdentifier() {
        Token.Doctype doctypeToken = new Token.Doctype();
        assertEquals("", doctypeToken.getPublicIdentifier());

        doctypeToken.publicIdentifier.append("public");
        assertEquals("public", doctypeToken.getPublicIdentifier());
    }

    @org.junit.Test
    public void testDoctypeGetSystemIdentifier() {
        Token.Doctype doctypeToken = new Token.Doctype();
        assertEquals("", doctypeToken.getSystemIdentifier());

        doctypeToken.systemIdentifier.append("system");
        assertEquals("system", doctypeToken.getSystemIdentifier());
    }

    @org.junit.Test
    public void testDoctypeIsForceQuirks() {
        Token.Doctype doctypeToken = new Token.Doctype();
        assertFalse(doctypeToken.isForceQuirks());

        doctypeToken.forceQuirks = true;
        assertTrue(doctypeToken.isForceQuirks());
    }

    @org.junit.Test
    public void testTagNewAttribute() {
        Token.Tag tagToken = new Token.StartTag();
        assertNull(tagToken.attributes);

        tagToken.newAttribute();
        assertNotNull(tagToken.attributes);
    }

    @org.junit.Test
    public void testTagFinaliseTag() {
        Token.Tag tagToken = new Token.StartTag();
        tagToken.pendingAttributeName = "name";
        tagToken.pendingAttributeValue = new StringBuilder("value");

        tagToken.finaliseTag();
        assertNull(tagToken.pendingAttributeName);
        assertNull(tagToken.pendingAttributeValue);
        assertNotNull(tagToken.attributes);
    }

    @org.junit.Test
    public void testTagName() {
        Token.Tag tagToken = new Token.StartTag();
        assertNull(tagToken.tagName);

        tagToken.name("div");
        assertEquals("div", tagToken.name());
    }

    @org.junit.Test
    public void testTagIsSelfClosing() {
        Token.Tag tagToken = new Token.StartTag();
        assertFalse(tagToken.isSelfClosing());

        tagToken.selfClosing = true;
        assertTrue(tagToken.isSelfClosing());
    }

    @org.junit.Test
    public void testTagGetName() {
        Token.Tag tagToken = new Token.StartTag();
        assertNull(tagToken.tagName);

        tagToken.tagName = "div";
        assertEquals("div", tagToken.name());
    }

    @org.junit.Test
    public void testTagGetAttributes() {
        Token.Tag tagToken = new Token.StartTag();
        tagToken.attributes = new Attributes();

        assertNotNull(tagToken.getAttributes());
    }

    @org.junit.Test
    public void testTagAppendTagName() {
        Token.Tag tagToken = new Token.StartTag();
        assertNull(tagToken.tagName);

        tagToken.appendTagName("div");
        assertEquals("div", tagToken.tagName);
    }

    @org.junit.Test
    public void testTagAppendTagNameChar() {
        Token.Tag tagToken = new Token.StartTag();
        assertNull(tagToken.tagName);

        tagToken.appendTagName('d');
        assertEquals("d", tagToken.tagName);
    }

    @org.junit.Test
    public void testTagAppendAttributeName() {
        Token.Tag tagToken = new Token.StartTag();
        assertNull(tagToken.pendingAttributeName);

        tagToken.appendAttributeName("class");
        assertEquals("class", tagToken.pendingAttributeName);
    }

    @org.junit.Test
    public void testTagAppendAttributeNameChar() {
        Token.Tag tagToken = new Token.StartTag();
        assertNull(tagToken.pendingAttributeName);

        tagToken.appendAttributeName('c');
        assertEquals("c", tagToken.pendingAttributeName);
    }

    @org.junit.Test
    public void testTagAppendAttributeValue() {
        Token.Tag tagToken = new Token.StartTag();
        assertNull(tagToken.pendingAttributeValue);

        tagToken.appendAttributeValue("value");
        assertEquals("value", tagToken.pendingAttributeValue.toString());
    }

    @org.junit.Test
    public void testTagAppendAttributeValueChar() {
        Token.Tag tagToken = new Token.StartTag();
        assertNull(tagToken.pendingAttributeValue);

        tagToken.appendAttributeValue('v');
        assertEquals("v", tagToken.pendingAttributeValue.toString());
    }

    @org.junit.Test
    public void testCommentGetData() {
        Token.Comment commentToken = new Token.Comment();
        assertEquals("", commentToken.getData());

        commentToken.data.append("This is a comment");
        assertEquals("This is a comment", commentToken.getData());
    }

    @org.junit.Test
    public void testCharacterGetData() {
        Token.Character characterToken = new Token.Character("");
        assertEquals("", characterToken.getData());

        characterToken = new Token.Character("data");
        assertEquals("data", characterToken.getData());
    }

    @org.junit.Test
    public void testEndTagToString() {
        Token.EndTag endTagToken = new Token.EndTag("div");
        assertEquals("</div>", endTagToken.toString());
    }

    @org.junit.Test
    public void testEOFIsEOF() {
        Token.EOF eofToken = new Token.EOF();
        assertTrue(eofToken.isEOF());
    }
}
