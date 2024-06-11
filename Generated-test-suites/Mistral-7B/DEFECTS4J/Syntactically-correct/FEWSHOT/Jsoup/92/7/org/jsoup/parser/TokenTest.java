package org.jsoup.parser;

import static org.junit.Assert.*;

import org.junit.Test;

public class TokenTest {

    @Test
    public void testDoctypeReset() {
        Token token = new Token.Doctype();
        Token resetToken = token.reset();
        assertNotSame(token, resetToken);
        assertEquals(TokenType.Doctype, resetToken.type);
    }

    @Test
    public void testDoctypeGetName() {
        Token token = new Token.Doctype();
        token.name("html");
        Token resetToken = token.reset();
        assertEquals("html", resetToken.name());
    }

    @Test
    public void testDoctypeGetPublicIdentifier() {
        Token token = new Token.Doctype();
        token.getPublicIdentifier = "public";
        Token resetToken = token.reset();
        assertNull(resetToken.getPublicIdentifier());
    }

    @Test
    public void testDoctypeGetSystemIdentifier() {
        Token token = new Token.Doctype();
        token.systemIdentifier("system.html");
        Token resetToken = token.reset();
        assertNull(resetToken.getSystemIdentifier());
    }

    @Test
    public void testDoctypeIsForceQuirks() {
        Token token = new Token.Doctype();
        token.forceQuirks = true;
        Token resetToken = token.reset();
        assertFalse(resetToken.isForceQuirks());
    }

    @Test
    public void testTagReset() {
        Token token = new Token.Tag();
        Token resetToken = token.reset();
        assertNotSame(token, resetToken);
        assertEquals(TokenType.Tag, resetToken.type);
    }

    @Test
    public void testTagName() {
        Token token = new Token.Tag();
        token.name("div");
        Token resetToken = token.reset();
        assertNull(resetToken.tagName);
    }

    @Test
    public void testTagNormalName() {
        Token token = new Token.Tag();
        token.normalName = "div";
        Token resetToken = token.reset();
        assertNull(resetToken.normalName);
    }

    @Test
    public void testTagPendingAttributeName() {
        Token token = new Token.Tag();
        token.pendingAttributeName = "class";
        Token resetToken = token.reset();
        assertNull(resetToken.pendingAttributeName);
    }

    @Test
    public void testTagPendingAttributeValue() {
        Token token = new Token.Tag();
        StringBuilder pendingAttributeValue = new StringBuilder();
        pendingAttributeValue.append("red");
        token.pendingAttributeValue = pendingAttributeValue;
        Token resetToken = token.reset();
        assertNull(resetToken.pendingAttributeValue);
        assertNull(resetToken.pendingAttributeValueS);
    }

    @Test
    public void testTagPendingAttributeValueS() {
        Token token = new Token.Tag();
        token.pendingAttributeValueS = "blue";
        Token resetToken = token.reset();
        assertNull(resetToken.pendingAttributeValueS);
    }

    @Test
    public void testTagHasEmptyAttributeValue() {
        Token token = new Token.Tag();
        token.setEmptyAttributeValue();
        Token resetToken = token.reset();
        assertFalse(resetToken.hasEmptyAttributeValue);
    }

    @Test
    public void testTagHasPendingAttributeValue() {
        Token token = new Token.Tag();
        token.hasPendingAttributeValue = true;
        Token resetToken = token.reset();
        assertFalse(resetToken.hasPendingAttributeValue);
    }

    @Test
    public void testTagIsSelfClosing() {
        Token token = new Token.Tag();
        token.selfClosing = true;
        Token resetToken = token.reset();
        assertFalse(resetToken.isSelfClosing());
    }

    @Test
    public void testTagGetAttributes() {
        Token token = new Token.Tag();
        Attributes attributes = new Attributes();
        token.attributes = attributes;
        Token resetToken = token.reset();
        assertNull(resetToken.getAttributes());
    }

    @Test
    public void testTagNewAttribute() {
        Token token = new Token.Tag();
        token.newAttribute();
        assertEquals(0, token.attributes.size());
    }

    @Test
    public void testTagFinaliseTag() {
        Token token = new Token.Tag();
        token.newAttribute();
        token.finaliseTag();
        assertEquals(1, token.attributes.size());
    }

    @Test
    public void testStartTagReset() {
        Token token = new Token.StartTag();
        Token resetToken = token.reset();
        assertNotSame(token, resetToken);
        assertEquals(TokenType.StartTag, resetToken.type);
    }

    @Test
    public void testStartTagNameAttr() {
        Token token = new Token.StartTag();
        token.nameAttr("div", new Attributes());
        Token resetToken = token.reset();
        assertNull(resetToken.tagName);
    }

    @Test
    public void testStartTagToString() {
        Token token = new Token.StartTag();
        token.nameAttr("div", new Attributes());
        assertEquals("<div>", token.toString());
    }

    @Test
    public void testEndTagReset() {
        Token token = new Token.EndTag();
        Token resetToken = token.reset();
        assertNotSame(token, resetToken);
        assertEquals(TokenType.EndTag, resetToken.type);
    }

    @Test
    public void testEndTagToString() {
        Token token = new Token.EndTag();
        assertEquals("</div>", token.toString());
    }

    @Test
    public void testCommentReset() {
        Token token = new Token.Comment();
        Token resetToken = token.reset();
        assertNotSame(token, resetToken);
        assertEquals(TokenType.Comment, resetToken.type);
    }

    @Test
    public void testCommentgetData() {
        Token token = new Token.Comment();
        token.data("This is a comment.");
        Token resetToken = token.reset();
        assertNull(resetToken.getData());
    }

    @Test
    public void testCommentToString() {
        Token token = new Token.Comment();
        token.data("This is a comment.");
        assertEquals("<!--This is a comment.-->", token.toString());
    }

    @Test
    public void testCharacterReset() {
        Token token = new Token.Character();
        Token resetToken = token.reset();
        assertNotSame(token, resetToken);
        assertEquals(TokenType.Character, resetToken.type);
    }

    @Test
    public void testCharactergetData() {
        Token token = new Token.Character();
        token.data("A");
        Token resetToken = token.reset();
        assertNull(resetToken.getData());
    }

    @Test
    public void testCharacterToString() {
        Token token = new Token.Character();
        token.data("A");
        assertEquals("A", token.toString());
    }

    @Test
    public void testCDataReset() {
        Token token = new Token.CData("<!--CDATA[A]-->");
        Token resetToken = token.reset();
        assertNotSame(token, resetToken);
        assertEquals(TokenType.Character, resetToken.type);
    }

    @Test
    public void testCDataToString() {
        Token token = new Token.CData("<!--CDATA[A]-->");
        assertEquals("<![CDATA[A]]>", token.toString());
    }

    @Test
    public void testEOFReset() {
        Token token = new Token.EOF();
        Token resetToken = token.reset();
        assertNotSame(token, resetToken);
        assertEquals(TokenType.EOF, resetToken.type);
    }
}