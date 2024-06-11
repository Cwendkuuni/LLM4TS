package org.jsoup.parser;

import static org.junit.Assert.*;

import org.jsoup.nodes.Attributes;
import org.jsoup.parser.Token.Attribute;
import org.jsoup.parser.Token.Character;
import org.jsoup.parser.Token.Comment;
import org.jsoup.parser.Token.Doctype;
import org.jsoup.parser.Token.EndTag;
import org.jsoup.parser.Token.StartTag;

public class TokenTest {

    @Test
    public void testDoctype() {
        Doctype doctype = new Doctype();

        doctype.getName().equals("doctype");
        doctype.getPublicIdentifier().equals("");
        doctype.getSystemIdentifier().equals("");
        assertFalse(doctype.isForceQuirks());

        Doctype doctype1 = new Doctype();
        doctype1.getName().append("html");
        doctype1.getPublicIdentifier().append("Public");
        doctype1.getSystemIdentifier().append("/path/to/file.html");
        doctype1.isForceQuirks(true);

        doctype.getName().equals("doctype");
        doctype.getPublicIdentifier().equals("Public");
        doctype.getSystemIdentifier().equals("/path/to/file.html");
        assertTrue(doctype.isForceQuirks());
    }

    @Test
    public void testTag() {
        StartTag startTag = new StartTag("div");

        startTag.name().equals("div");
        assertFalse(startTag.isSelfClosing());
        assertNull(startTag.getAttributes());

        startTag.newAttribute();
        startTag.appendAttributeName("class").appendAttributeValue("test");
        Attribute classAttr = startTag.getAttributes().get("class");
        assertEquals("test", classAttr.getValue());

        endTag = new EndTag("div");
        endTag.name().equals("div");
        assertFalse(endTag.isSelfClosing());
        assertNull(endTag.getAttributes());

        Comment comment = new Comment();
        comment.getData().equals("");
        assertEquals("<!---->", comment.toString());

        Character character = new Character("A");
        character.getData().equals("A");
        assertEquals("A", character.toString());
    }

    @Test
    public void testTokenTypes() {
        TokenTokenType = TokenType.Character;
        assertFalse(new Character("").isDoctype());
        assertFalse(new Character("").isStartTag());
        assertFalse(new Character("").isEndTag());
        assertFalse(new Character("").isComment());
        assertFalse(new Character("").isEOF());

        TokenType = TokenType.Doctype;
        assertFalse(new Doctype().isCharacter());
        assertFalse(new Doctype().isStartTag());
        assertFalse(new Doctype().isEndTag());
        assertFalse(new Doctype().isComment());
        assertFalse(new Doctype().isEOF());

        TokenType = TokenType.StartTag;
        assertFalse(new StartTag().isCharacter());
        assertFalse(new StartTag().isDoctype());
        assertFalse(new StartTag().isEndTag());
        assertFalse(new StartTag().isComment());
        assertFalse(new StartTag().isEOF());

        TokenType = TokenType.EndTag;
        assertFalse(new EndTag().isCharacter());
        assertFalse(new EndTag().isDoctype());
        assertFalse(new EndTag().isStartTag());
        assertFalse(new EndTag().isComment());
        assertFalse(new EndTag().isEOF());

        TokenType = TokenType.Comment;
        assertFalse(new Comment().isCharacter());
        assertFalse(new Comment().isDoctype());
        assertFalse(new Comment().isStartTag());
        assertFalse(new Comment().isEndTag());
        assertFalse(new Comment().isEOF());

        TokenType = TokenType.Character;
        assertTrue(new Character("").isCharacter());
        assertFalse(new Character("").isDoctype());
        assertFalse(new Character("").isStartTag());
        assertFalse(new Character("").isEndTag());
        assertFalse(new Character("").isComment());
        assertFalse(new Character("").isEOF());

        TokenType = TokenType.EOF;
        assertFalse(new EOF().isCharacter());
        assertFalse(new EOF().isDoctype());
        assertFalse(new EOF().isStartTag());
        assertFalse(new EOF().isEndTag());
        assertTrue(new EOF().isEOF());
    }
}