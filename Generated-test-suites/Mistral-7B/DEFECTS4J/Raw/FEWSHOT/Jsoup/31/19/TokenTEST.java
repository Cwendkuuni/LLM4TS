package org.jsoup.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import org.junit.Test;

import org.jsoup.nodes.Attributes;

public class TokenTest {

    @Test
    public void testDoctype() {
        Token doctype = new Token.Doctype();
        assertEquals("Doctype", doctype.tokenType());

        TokenType expectedType = TokenType.Doctype;
        Token doctype1 = new Token.Doctype();
        Token doctype2 = new Token.Doctype();
        Token doctype3 = new Token.Doctype();

        assertEquals(expectedType, doctype1.type);
        assertEquals(expectedType, doctype2.type);
        assertEquals(expectedType, doctype3.type);

        Token doctype4 = new Token.Doctype();
        assertNotSame(doctype, doctype4);

        Token doctype5 = new Token.Doctype();
        assertNotSame(doctype1, doctype5);
        assertNotSame(doctype2, doctype5);
        assertNotSame(doctype3, doctype5);
        assertNotSame(doctype4, doctype5);

        Token doctype6 = new Token.Doctype();
        assertEquals(doctype6, doctype6);
    }

    @Test
    public void testStartTag() {
        Token startTag = new Token.StartTag();
        assertEquals("StartTag", startTag.tokenType());

        TokenType expectedType = TokenType.StartTag;
        Token startTag1 = new Token.StartTag();
        Token startTag2 = new Token.StartTag();
        Token startTag3 = new Token.StartTag();

        assertEquals(expectedType, startTag1.type);
        assertEquals(expectedType, startTag2.type);
        assertEquals(expectedType, startTag3.type);

        Token startTag4 = new Token.StartTag();
        assertNotSame(startTag, startTag4);

        Token startTag5 = new Token.StartTag();
        assertNotSame(startTag1, startTag5);
        assertNotSame(startTag2, startTag5);
        assertNotSame(startTag3, startTag5);
        assertNotSame(startTag4, startTag5);

        Token startTag6 = new Token.StartTag();
        assertEquals(startTag6, startTag6);

        Token startTag7 = new Token.StartTag("tagName");
        assertEquals("StartTag", startTag7.tokenType());
        assertEquals("tagName", startTag7.name());

        Token startTag8 = new Token.StartTag("tagName", null);
        assertEquals("StartTag", startTag8.tokenType());
        assertEquals("tagName", startTag8.name());

        Token startTag9 = new Token.StartTag("tagName", new Attributes());
        assertEquals("StartTag", startTag9.tokenType());
        assertEquals("tagName", startTag9.name());
        assertNull(startTag9.attributes);
    }

    @Test
    public void testEndTag() {
        Token endTag = new Token.EndTag();
        assertEquals("EndTag", endTag.tokenType());

        TokenType expectedType = TokenType.EndTag;
        Token endTag1 = new Token.EndTag();
        Token endTag2 = new Token.EndTag();
        Token endTag3 = new Token.EndTag();

        assertEquals(expectedType, endTag1.type);
        assertEquals(expectedType, endTag2.type);
        assertEquals(expectedType, endTag3.type);

        Token endTag4 = new Token.EndTag();
        assertNotSame(endTag, endTag4);

        Token endTag5 = new Token.EndTag();
        assertNotSame(endTag1, endTag5);
        assertNotSame(endTag2, endTag5);
        assertNotSame(endTag3, endTag5);
        assertNotSame(endTag4, endTag5);

        Token endTag6 = new Token.EndTag();
        assertEquals(endTag6, endTag6);

        Token endTag7 = new Token.EndTag("tagName");
        assertEquals("EndTag", endTag7.tokenType());
        assertEquals("tagName", endTag7.name());

        Token endTag8 = new Token.EndTag("tagName");
        assertEquals("EndTag", endTag8.tokenType());
        assertEquals("tagName", endTag8.name());
    }

    @Test
    public void testComment() {
        Token comment = new Token.Comment();
        assertEquals("Comment", comment.tokenType());

        TokenType expectedType = TokenType.Comment;
        Token comment1 = new Token.Comment();
        Token comment2 = new Token.Comment();
        Token comment3 = new Token.Comment();

        assertEquals(expectedType, comment1.type);
        assertEquals(expectedType, comment2.type);
        assertEquals(expectedType, comment3.type);

        Token comment4 = new Token.Comment();
        assertNotSame(comment, comment4);

        Token comment5 = new Token.Comment();
        assertNotSame(comment1, comment5);
        assertNotSame(comment2, comment5);
        assertNotSame(comment3, comment5);
        assertNotSame(comment4, comment5);

        Token comment6 = new Token.Comment();
        assertEquals(comment6, comment6);

        Token comment7 = new Token.Comment();
        StringBuilder data = new StringBuilder("data");
        comment7.asComment().data.append(data);
        assertEquals("Comment", comment7.tokenType());
        assertEquals(data.toString(), comment7.getData());
    }

    @Test
    public void testCharacter() {
        Token character = new Token.Character("data");
        assertEquals("Character", character.tokenType());

        TokenType expectedType = TokenType.Character;
        Token character1 = new Token.Character("data");
        Token character2 = new Token.Character("data");
        Token character3 = new Token.Character("data");

        assertEquals(expectedType, character1.type);
        assertEquals(expectedType, character2.type);
        assertEquals(expectedType, character3.type);

        Token character4 = new Token.Character("data");
        assertNotSame(character, character4);

        Token character5 = new Token.Character("data");
        assertNotSame(character1, character5);
        assertNotSame(character2, character5);
        assertNotSame(character3, character5);
        assertNotSame(character4, character5);

        Token character6 = new Token.Character("data");
        assertEquals(character6, character6);
    }

    @Test
    public void testEOF() {
        Token eof = new Token.EOF();
        assertEquals("EOF", eof.tokenType());

        TokenType expectedType = TokenType.EOF;
        Token eof1 = new Token.EOF();
        Token eof2 = new Token.EOF();
        Token eof3 = new Token.EOF();

        assertEquals(expectedType, eof1.type);
        assertEquals(expectedType, eof2.type);
        assertEquals(expectedType, eof3.type);

        Token eof4 = new Token.EOF();
        assertNotSame(eof, eof4);

        Token eof5 = new Token.EOF();
        assertNotSame(eof1, eof5);
        assertNotSame(eof2, eof5);
        assertNotSame(eof3, eof5);
        assertNotSame(eof4, eof5);

        Token eof6 = new Token.EOF();
        assertEquals(eof6, eof6);
    }
}