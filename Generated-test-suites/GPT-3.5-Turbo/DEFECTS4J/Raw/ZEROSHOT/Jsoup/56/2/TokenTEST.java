package org.jsoup.parser;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class TokenTest {
    
    Doctype doctype;
    StartTag startTag;
    EndTag endTag;
    Comment comment;
    Character character;
    EOF eof;

    @Before
    public void setup() {
        doctype = new Doctype();
        startTag = new StartTag();
        endTag = new EndTag();
        comment = new Comment();
        character = new Character();
        eof = new EOF();
    }

    @Test
    public void testDoctypeReset() {
        Doctype doctypeReset = doctype.reset();
        assertEquals("", doctypeReset.name.toString());
        assertEquals("", doctypeReset.publicIdentifier.toString());
        assertEquals("", doctypeReset.systemIdentifier.toString());
        assertFalse(doctypeReset.forceQuirks);
    }

    @Test
    public void testStartTagReset() {
        StartTag startTagReset = startTag.reset();
        assertNull(startTagReset.tagName);
        assertNull(startTagReset.normalName);
        assertNull(startTagReset.pendingAttributeName);
        assertEquals("", startTagReset.pendingAttributeValue.toString());
        assertNull(startTagReset.pendingAttributeValueS);
        assertFalse(startTagReset.hasEmptyAttributeValue);
        assertFalse(startTagReset.hasPendingAttributeValue);
        assertFalse(startTagReset.selfClosing);
        assertNull(startTagReset.attributes);
    }

    @Test
    public void testEndTagReset() {
        EndTag endTagReset = endTag.reset();
        assertNull(endTagReset.tagName);
        assertNull(endTagReset.normalName);
        assertNull(endTagReset.pendingAttributeName);
        assertEquals("", endTagReset.pendingAttributeValue.toString());
        assertNull(endTagReset.pendingAttributeValueS);
        assertFalse(endTagReset.hasEmptyAttributeValue);
        assertFalse(endTagReset.hasPendingAttributeValue);
        assertFalse(endTagReset.selfClosing);
        assertNull(endTagReset.attributes);
    }

    @Test
    public void testCommentReset() {
        Comment commentReset = comment.reset();
        assertEquals("", commentReset.data.toString());
        assertFalse(commentReset.bogus);
    }

    @Test
    public void testCharacterReset() {
        Character characterReset = character.reset();
        assertNull(characterReset.data);
    }

    @Test
    public void testEOFReset() {
        EOF eofReset = eof.reset();
        assertNotNull(eofReset);
    }

    @Test
    public void testDoctypeGetName() {
        doctype.name.append("DOCTYPE");
        String name = doctype.getName();
        assertEquals("DOCTYPE", name);
    }

    @Test
    public void testDoctypeGetPublicIdentifier() {
        doctype.publicIdentifier.append("public");
        String publicIdentifier = doctype.getPublicIdentifier();
        assertEquals("public", publicIdentifier);
    }

    @Test
    public void testDoctypeGetSystemIdentifier() {
        doctype.systemIdentifier.append("system");
        String systemIdentifier = doctype.getSystemIdentifier();
        assertEquals("system", systemIdentifier);
    }

    @Test
    public void testDoctypeIsForceQuirks() {
        assertFalse(doctype.isForceQuirks());
        doctype.forceQuirks = true;
        assertTrue(doctype.isForceQuirks());
    }

    @Test
    public void testStartTagName() {
        startTag.tagName = "div";
        startTag.normalName = "div";
        String name = startTag.name();
        String normalName = startTag.normalName();
        assertEquals("div", name);
        assertEquals("div", normalName);
    }

    @Test
    public void testStartTagIsSelfClosing() {
        assertFalse(startTag.isSelfClosing());
        startTag.selfClosing = true;
        assertTrue(startTag.isSelfClosing());
    }

    @Test
    public void testStartTagGetAttributes() {
        assertNull(startTag.getAttributes());
        startTag.attributes = new Attributes();
        assertNotNull(startTag.getAttributes());
    }

    @Test
    public void testEndTagName() {
        endTag.tagName = "div";
        String name = endTag.name();
        assertEquals("div", name);
    }

    @Test
    public void testEndTagToString() {
        endTag.tagName = "div";
        String tagString = endTag.toString();
        assertEquals("</div>", tagString);
    }

    @Test
    public void testCommentGetData() {
        comment.data.append("This is a comment");
        String data = comment.getData();
        assertEquals("This is a comment", data);
    }

    @Test
    public void testCommentToString() {
        comment.data.append("This is a comment");
        String commentString = comment.toString();
        assertEquals("<!--This is a comment-->", commentString);
    }

    @Test
    public void testCharacterGetData() {
        character.data = "This is a character";
        String data = character.getData();
        assertEquals("This is a character", data);
    }

    @Test
    public void testCharacterToString() {
        character.data = "This is a character";
        String characterString = character.toString();
        assertEquals("This is a character", characterString);
    }

    @Test
    public void testIsDoctype() {
        assertTrue(doctype.isDoctype());
        assertFalse(startTag.isDoctype());
        assertFalse(endTag.isDoctype());
        assertFalse(comment.isDoctype());
        assertFalse(character.isDoctype());
        assertFalse(eof.isDoctype());
    }

    @Test
    public void testAsDoctype() {
        Doctype doctype = new Doctype();
        Token token = doctype;
        Doctype convertedDoctype = token.asDoctype();
        assertNotNull(convertedDoctype);
    }

    @Test
    public void testIsStartTag() {
        assertFalse(doctype.isStartTag());
        assertTrue(startTag.isStartTag());
        assertFalse(endTag.isStartTag());
        assertFalse(comment.isStartTag());
        assertFalse(character.isStartTag());
        assertFalse(eof.isStartTag());
    }

    @Test
    public void testAsStartTag() {
        StartTag startTag = new StartTag();
        Token token = startTag;
        StartTag convertedStartTag = token.asStartTag();
        assertNotNull(convertedStartTag);
    }

    @Test
    public void testIsEndTag() {
        assertFalse(doctype.isEndTag());
        assertFalse(startTag.isEndTag());
        assertTrue(endTag.isEndTag());
        assertFalse(comment.isEndTag());
        assertFalse(character.isEndTag());
        assertFalse(eof.isEndTag());
    }

    @Test
    public void testAsEndTag() {
        EndTag endTag = new EndTag();
        Token token = endTag;
        EndTag convertedEndTag = token.asEndTag();
        assertNotNull(convertedEndTag);
    }

    @Test
    public void testIsComment() {
        assertFalse(doctype.isComment());
        assertFalse(startTag.isComment());
        assertFalse(endTag.isComment());
        assertTrue(comment.isComment());
        assertFalse(character.isComment());
        assertFalse(eof.isComment());
    }

    @Test
    public void testAsComment() {
        Comment comment = new Comment();
        Token token = comment;
        Comment convertedComment = token.asComment();
        assertNotNull(convertedComment);
    }

    @Test
    public void testIsCharacter() {
        assertFalse(doctype.isCharacter());
        assertFalse(startTag.isCharacter());
        assertFalse(endTag.isCharacter());
        assertFalse(comment.isCharacter());
        assertTrue(character.isCharacter());
        assertFalse(eof.isCharacter());
    }

    @Test
    public void testAsCharacter() {
        Character character = new Character();
        Token token = character;
        Character convertedCharacter = token.asCharacter();
        assertNotNull(convertedCharacter);
    }

    @Test
    public void testIsEOF() {
        assertFalse(doctype.isEOF());
        assertFalse(startTag.isEOF());
        assertFalse(endTag.isEOF());
        assertFalse(comment.isEOF());
        assertFalse(character.isEOF());
        assertTrue(eof.isEOF());
    }

    @Test
    public void testTokenTypeEnum() {
        assertEquals("Doctype", Token.TokenType.Doctype.toString());
        assertEquals("StartTag", Token.TokenType.StartTag.toString());
        assertEquals("EndTag", Token.TokenType.EndTag.toString());
        assertEquals("Comment", Token.TokenType.Comment.toString());
        assertEquals("Character", Token.TokenType.Character.toString());
        assertEquals("EOF", Token.TokenType.EOF.toString());
    }
}