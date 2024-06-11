package org.jsoup.parser;

import org.jsoup.helper.Validate;
import org.jsoup.nodes.Attributes;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class TokenTest {
    @Test
    public void testTokenType() {
        Token token = new Token() {
            @Override
            Token reset() {
                return null;
            }
        };
        
        assertEquals("Token", token.tokenType());
        assertEquals("Doctype", TokenType.Doctype.name());
        assertEquals("StartTag", TokenType.StartTag.name());
        assertEquals("EndTag", TokenType.EndTag.name());
        assertEquals("Comment", TokenType.Comment.name());
        assertEquals("Character", TokenType.Character.name());
        assertEquals("EOF", TokenType.EOF.name());
        assertFalse(token.isDoctype());
        assertFalse(token.isStartTag());
        assertFalse(token.isEndTag());
        assertFalse(token.isComment());
        assertFalse(token.isCharacter());
        assertFalse(token.isCData());
        assertFalse(token.isEOF());
    }

    @Test
    public void testDoctypeToken() {
        Token.Doctype doctype = new Token.Doctype();
        doctype.name.append("html");
        doctype.pubSysKey = "public";
        doctype.publicIdentifier.append("pub");
        doctype.systemIdentifier.append("sys");
        doctype.forceQuirks = true;

        assertFalse(doctype.getName().isEmpty());
        assertFalse(doctype.getPubSysKey().isEmpty());
        assertFalse(doctype.getPublicIdentifier().isEmpty());
        assertFalse(doctype.getSystemIdentifier().isEmpty());
        assertTrue(doctype.isForceQuirks());

        Token.Doctype resetDoctype = doctype.reset();
        assertTrue(resetDoctype.name.toString().isEmpty());
        assertTrue(resetDoctype.getPubSysKey() == null);
        assertTrue(resetDoctype.publicIdentifier.toString().isEmpty());
        assertTrue(resetDoctype.systemIdentifier.toString().isEmpty());
        assertFalse(resetDoctype.isForceQuirks());
    }

    @Test
    public void testStartTagToken() {
        Token.StartTag startTag = new Token.StartTag();
        startTag.name("div");
        startTag.selfClosing = true;
        startTag.attributes = new Attributes();
        startTag.newAttribute();
        startTag.finaliseTag();

        assertFalse(startTag.name().isEmpty());
        assertFalse(startTag.normalName().isEmpty());
        assertTrue(startTag.isSelfClosing());
        assertTrue(startTag.getAttributes() != null);

        Token.StartTag resetStartTag = startTag.reset();
        assertTrue(resetStartTag.tagName == null);
        assertTrue(resetStartTag.normalName == null);
        assertFalse(resetStartTag.isSelfClosing());
        assertTrue(resetStartTag.getAttributes() == null);
    }

    @Test
    public void testEndTagToken() {
        Token.EndTag endTag = new Token.EndTag();
        endTag.name("div");

        assertFalse(endTag.name().isEmpty());

        Token.EndTag resetEndTag = endTag.reset();
        assertTrue(resetEndTag.tagName == null);
        assertTrue(resetEndTag.normalName == null);
    }

    @Test
    public void testCommentToken() {
        Token.Comment comment = new Token.Comment();
        comment.data.append("test");
        comment.bogus = true;

        assertFalse(comment.getData().isEmpty());
        assertTrue(comment.bogus);

        Token.Comment resetComment = comment.reset();
        assertTrue(resetComment.data.toString().isEmpty());
        assertFalse(resetComment.bogus);
    }

    @Test
    public void testCharacterToken() {
        Token.Character character = new Token.Character();
        character.data("test");

        assertFalse(character.getData().isEmpty());

        Token.Character resetCharacter = character.reset();
        assertTrue(resetCharacter.data == null);
    }

    @Test
    public void testCDataToken() {
        Character data = new Character().data("cdata");
        Token.CData cData = new Token.CData(data.getData());

        assertFalse(cData.getData().isEmpty());
        assertEquals("test", cData.toString());
    }

    @Test
    public void testEOFToken() {
        Token.EOF eof = new Token.EOF();

        assertFalse(eof.toString().isEmpty());

        Token.EOF resetEof = eof.reset();
        assertTrue(resetEof != null);
    }
}