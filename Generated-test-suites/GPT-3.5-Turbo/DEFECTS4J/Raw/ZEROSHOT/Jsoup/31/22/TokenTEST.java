package org.jsoup.parser;

import org.junit.*;
import static org.junit.Assert.*;

public class TokenTest {

    @Test
    public void testTokenType() {
        Token token = new Token() {};
        assertEquals("Token", token.tokenType());
    }
    
    @Test
    public void testDoctypeGetName() {
        Token.Doctype doctype = new Token.Doctype();
        doctype.name.append("html");
        assertEquals("html", doctype.getName());
    }
    
    @Test
    public void testDoctypeGetPublicIdentifier() {
        Token.Doctype doctype = new Token.Doctype();
        doctype.publicIdentifier.append("-//W3C//DTD HTML 4.01 Transitional//EN");
        assertEquals("-//W3C//DTD HTML 4.01 Transitional//EN", doctype.getPublicIdentifier());
    }
    
    @Test
    public void testDoctypeGetSystemIdentifier() {
        Token.Doctype doctype = new Token.Doctype();
        doctype.systemIdentifier.append("http://www.w3.org/TR/html4/loose.dtd");
        assertEquals("http://www.w3.org/TR/html4/loose.dtd", doctype.getSystemIdentifier());
    }
    
    @Test
    public void testDoctypeIsForceQuirksFalse() {
        Token.Doctype doctype = new Token.Doctype();
        assertFalse(doctype.isForceQuirks());
    }
    
    @Test
    public void testDoctypeIsForceQuirksTrue() {
        Token.Doctype doctype = new Token.Doctype();
        doctype.forceQuirks = true;
        assertTrue(doctype.isForceQuirks());
    }
    
    @Test
    public void testTagNewAttribute() {
        Token.Tag tag = new Token.Tag() {};
        tag.newAttribute();
        assertNotNull(tag.attributes);
    }
    
    @Test
    public void testTagFinaliseTag() {
        Token.Tag tag = new Token.Tag() {};
        tag.pendingAttributeName = "class";
        tag.newAttribute();
        assertNull(tag.pendingAttributeName);
        assertEquals("", tag.pendingAttributeValue.toString());
    }
    
    @Test
    public void testTagName() {
        Token.Tag tag = new Token.Tag() {};
        tag.tagName = "div";
        assertEquals("div", tag.name());
    }
    
    @Test
    public void testTagSetName() {
        Token.Tag tag = new Token.Tag() {};
        tag.name("span");
        assertEquals("span", tag.tagName);
    }
    
    @Test
    public void testTagIsSelfClosingFalse() {
        Token.Tag tag = new Token.Tag() {};
        assertFalse(tag.isSelfClosing());
    }
    
    @Test
    public void testTagIsSelfClosingTrue() {
        Token.Tag tag = new Token.Tag() {};
        tag.selfClosing = true;
        assertTrue(tag.isSelfClosing());
    }
    
    @Test
    public void testTagGetAttributes() {
        Token.Tag tag = new Token.Tag() {};
        assertNotNull(tag.getAttributes());
    }
    
    @Test
    public void testTagAppendTagNameString() {
        Token.Tag tag = new Token.Tag() {};
        tag.appendTagName("a");
        assertEquals("a", tag.tagName);
    }
    
    @Test
    public void testTagAppendTagNameChar() {
        Token.Tag tag = new Token.Tag() {};
        tag.appendTagName('b');
        assertEquals("b", tag.tagName);
    }
    
    @Test
    public void testTagAppendAttributeNameString() {
        Token.Tag tag = new Token.Tag() {};
        tag.appendAttributeName("class");
        assertEquals("class", tag.pendingAttributeName);
    }
    
    @Test
    public void testTagAppendAttributeNameChar() {
        Token.Tag tag = new Token.Tag() {};
        tag.appendAttributeName('id');
        assertEquals("id", tag.pendingAttributeName);
    }
    
    @Test
    public void testTagAppendAttributeValueString() {
        Token.Tag tag = new Token.Tag() {};
        tag.appendAttributeValue("heading");
        assertEquals("heading", tag.pendingAttributeValue.toString());
    }
    
    @Test
    public void testTagAppendAttributeValueChar() {
        Token.Tag tag = new Token.Tag() {};
        tag.appendAttributeValue('1');
        assertEquals("1", tag.pendingAttributeValue.toString());
    }
    
    @Test
    public void testStartTagToStringWithAttributes() {
        Token.StartTag startTag = new Token.StartTag();
        startTag.tagName = "span";
        startTag.attributes = new Attributes();
        startTag.attributes.put("class", "highlight");
        assertEquals("<span class=\"highlight\">", startTag.toString());
    }
    
    @Test
    public void testStartTagToStringWithoutAttributes() {
        Token.StartTag startTag = new Token.StartTag();
        startTag.tagName = "p";
        assertEquals("<p>", startTag.toString());
    }
    
    @Test
    public void testEndTagToString() {
        Token.EndTag endTag = new Token.EndTag();
        endTag.tagName = "div";
        assertEquals("</div>", endTag.toString());
    }
    
    @Test
    public void testCommentGetData() {
        Token.Comment comment = new Token.Comment();
        comment.data.append("Lorem ipsum dolor sit amet");
        assertEquals("Lorem ipsum dolor sit amet", comment.getData());
    }
    
    @Test
    public void testCharacterGetData() {
        Token.Character character = new Token.Character("Hello, World!");
        assertEquals("Hello, World!", character.getData());
    }
    
    @Test
    public void testEOFIsEOF() {
        Token.EOF eof = new Token.EOF();
        assertTrue(eof.isEOF());
    }

    @Test
    public void testIsDoctypeFalse() {
        Token token = new Token() {};
        assertFalse(token.isDoctype());
    }
    
    @Test
    public void testAsDoctype() {
        Token token = new Token() {};
        assertFalse(token.isDoctype());
        assertNull(token.asDoctype());
    }
    
    @Test
    public void testIsStartTagFalse() {
        Token token = new Token() {};
        assertFalse(token.isStartTag());
    }
    
    @Test
    public void testAsStartTag() {
        Token token = new Token() {};
        assertFalse(token.isStartTag());
        assertNull(token.asStartTag());
    }
    
    @Test
    public void testIsEndTagFalse() {
        Token token = new Token() {};
        assertFalse(token.isEndTag());
    }
    
    @Test
    public void testAsEndTag() {
        Token token = new Token() {};
        assertFalse(token.isEndTag());
        assertNull(token.asEndTag());
    }
    
    @Test
    public void testIsCommentFalse() {
        Token token = new Token() {};
        assertFalse(token.isComment());
    }
    
    @Test
    public void testAsComment() {
        Token token = new Token() {};
        assertFalse(token.isComment());
        assertNull(token.asComment());
    }
    
    @Test
    public void testIsCharacterFalse() {
        Token token = new Token() {};
        assertFalse(token.isCharacter());
    }
    
    @Test
    public void testAsCharacter() {
        Token token = new Token() {};
        assertFalse(token.isCharacter());
        assertNull(token.asCharacter());
    }
    
    @Test
    public void testIsEOFFalse() {
        Token token = new Token() {};
        assertFalse(token.isEOF());
    }

    @Test
    public void testTokenTypeEnumValues() {
        Token.TokenType[] expected = {Token.TokenType.Doctype, Token.TokenType.StartTag, Token.TokenType.EndTag, Token.TokenType.Comment, Token.TokenType.Character, Token.TokenType.EOF};
        Token.TokenType[] actual = Token.TokenType.values();
        assertArrayEquals(expected, actual);
    }

}