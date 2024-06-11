import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class TokenTest {
    Token.Doctype doctype;
    Token.StartTag startTag;
    Token.EndTag endTag;
    Token.Comment comment;
    Token.Character character;
    Token.EOF eof;

    @Before
    public void setUp() {
        doctype = new Token.Doctype();
        startTag = new Token.StartTag();
        endTag = new Token.EndTag();
        comment = new Token.Comment();
        character = new Token.Character();
        eof = new Token.EOF();
    }

    @After
    public void tearDown() {
        doctype = null;
        startTag = null;
        endTag = null;
        comment = null;
        character = null;
        eof = null;
    }

    @Test
    public void testTokenType() {
        assertEquals("Doctype", doctype.tokenType());
        assertEquals("StartTag", startTag.tokenType());
        assertEquals("EndTag", endTag.tokenType());
        assertEquals("Comment", comment.tokenType());
        assertEquals("Character", character.tokenType());
        assertEquals("EOF", eof.tokenType());
    }

    @Test
    public void testDoctypeReset() {
        doctype.name.append("html");
        doctype.pubSysKey = "key";
        doctype.publicIdentifier.append("pubId");
        doctype.systemIdentifier.append("sysId");
        doctype.forceQuirks = true;

        doctype.reset();

        assertEquals("", doctype.getName());
        assertNull(doctype.getPubSysKey());
        assertEquals("", doctype.getPublicIdentifier());
        assertEquals("", doctype.getSystemIdentifier());
        assertFalse(doctype.isForceQuirks());
    }

    @Test
    public void testStartTagReset() {
        startTag.name("div");
        startTag.normalName = "div";
        startTag.pendingAttributeName = "class";
        startTag.pendingAttributeValue.append("container");
        startTag.pendingAttributeValueS = "active";
        startTag.hasEmptyAttributeValue = true;
        startTag.hasPendingAttributeValue = true;
        startTag.selfClosing = true;
        startTag.attributes = new Token.Attributes();

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
        endTag.tagName = "div";
        endTag.normalName = "div";
        endTag.pendingAttributeName = "class";
        endTag.pendingAttributeValue.append("container");
        endTag.pendingAttributeValueS = "active";
        endTag.hasEmptyAttributeValue = true;
        endTag.hasPendingAttributeValue = true;
        endTag.selfClosing = true;
        endTag.attributes = new Token.Attributes();

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
    public void testCommentReset() {
        comment.data.append("This is a comment");
        comment.bogus = true;

        comment.reset();

        assertEquals("", comment.getData());
        assertFalse(comment.bogus);
    }

    @Test
    public void testCharacterReset() {
        character.data = "ABC";

        character.reset();

        assertNull(character.getData());
    }

    @Test
    public void testEOFReset() {
        EOF eofReset = eof.reset();

        assertSame(eof, eofReset);
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
        Token.Doctype doctype = this.doctype.asDoctype();

        assertSame(this.doctype, doctype);
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
        Token.StartTag startTag = this.startTag.asStartTag();

        assertSame(this.startTag, startTag);
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
        Token.EndTag endTag = this.endTag.asEndTag();

        assertSame(this.endTag, endTag);
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
        Token.Comment comment = this.comment.asComment();

        assertSame(this.comment, comment);
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
        Token.Character character = this.character.asCharacter();

        assertSame(this.character, character);
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
}