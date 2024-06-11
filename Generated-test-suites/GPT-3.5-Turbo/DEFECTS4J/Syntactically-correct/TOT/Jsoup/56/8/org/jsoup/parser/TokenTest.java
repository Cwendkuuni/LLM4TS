import org.junit.Before;
import org.junit.Test;
import org.jsoup.helper.Validate;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.BooleanAttribute;

import static org.junit.Assert.*;

public class TokenTest {

    private Token.Doctype doctype;
    private Token.StartTag startTag;
    private Token.EndTag endTag;
    private Token.Comment comment;
    private Token.Character character;
    private Token.EOF eof;

    @Before
    public void setup() {
        doctype = new Token.Doctype();
        startTag = new Token.StartTag();
        endTag = new Token.EndTag();
        comment = new Token.Comment();
        character = new Token.Character();
        eof = new Token.EOF();
    }

    @Test
    public void testDoctypeReset() {
        doctype.name.append("html");
        doctype.publicIdentifier.append("public");
        doctype.systemIdentifier.append("system");
        doctype.forceQuirks = true;

        Token.Doctype resetDoctype = doctype.reset();

        assertEquals("", resetDoctype.getName());
        assertEquals("", resetDoctype.getPublicIdentifier());
        assertEquals("", resetDoctype.getSystemIdentifier());
        assertFalse(resetDoctype.isForceQuirks());
    }

    @Test
    public void testDoctypeGetName() {
        doctype.name.append("html");

        assertEquals("html", doctype.getName());
    }

    @Test
    public void testDoctypeGetPublicIdentifier() {
        doctype.publicIdentifier.append("public");

        assertEquals("public", doctype.getPublicIdentifier());
    }

    @Test
    public void testDoctypeGetSystemIdentifier() {
        doctype.systemIdentifier.append("system");

        assertEquals("system", doctype.getSystemIdentifier());
    }

    @Test
    public void testDoctypeIsForceQuirks() {
        doctype.forceQuirks = true;

        assertTrue(doctype.isForceQuirks());
    }

    @Test
    public void testStartTagReset() {
        startTag.tagName = "div";
        startTag.normalName = "div";
        startTag.pendingAttributeName = "id";
        startTag.pendingAttributeValueS = "container";
        startTag.selfClosing = true;
        Attributes attributes = new Attributes();
        attributes.put(new BooleanAttribute("hidden"));
        startTag.attributes = attributes;

        Token.StartTag resetStartTag = startTag.reset();

        assertNull(resetStartTag.tagName);
        assertNull(resetStartTag.normalName);
        assertNull(resetStartTag.pendingAttributeName);
        assertEquals("", resetStartTag.pendingAttributeValue.toString());
        assertNull(resetStartTag.pendingAttributeValueS);
        assertFalse(resetStartTag.hasEmptyAttributeValue);
        assertFalse(resetStartTag.hasPendingAttributeValue);
        assertFalse(resetStartTag.selfClosing);
        assertNull(resetStartTag.attributes);
    }

    @Test
    public void testStartTagName() {
        startTag.name("div");

        assertEquals("div", startTag.name());
    }

    @Test
    public void testStartTagIsSelfClosing() {
        startTag.selfClosing = true;

        assertTrue(startTag.isSelfClosing());
    }

    @Test
    public void testStartTagGetAttributes() {
        Attributes attributes = new Attributes();
        attributes.put(new BooleanAttribute("hidden"));
        startTag.attributes = attributes;

        assertEquals(attributes, startTag.getAttributes());
    }

    @Test
    public void testEndTagReset() {
        endTag.tagName = "div";
        endTag.normalName = "div";

        Token.EndTag resetEndTag = endTag.reset();

        assertNull(resetEndTag.tagName);
        assertNull(resetEndTag.normalName);
    }

    @Test
    public void testCommentReset() {
        comment.data.append("This is a comment");
        comment.bogus = true;

        Token.Comment resetComment = comment.reset();

        assertEquals("", resetComment.getData());
        assertFalse(resetComment.bogus);
    }

    @Test
    public void testCommentGetData() {
        comment.data.append("This is a comment");

        assertEquals("This is a comment", comment.getData());
    }

    @Test
    public void testCharacterReset() {
        character.data = "Data";

        Token.Character resetCharacter = character.reset();

        assertNull(resetCharacter.getData());
    }

    @Test
    public void testCharacterData() {
        character.data("Data");

        assertEquals("Data", character.getData());
    }

    @Test
    public void testEOFReset() {
        Token.EOF resetEOF = eof.reset();

        assertEquals(resetEOF, eof);
    }

    @Test
    public void testTokenIsDoctype() {
        assertTrue(doctype.isDoctype());
        assertFalse(startTag.isDoctype());
    }

    @Test
    public void testTokenAsDoctype() {
        assertEquals(doctype, doctype.asDoctype());
    }

    @Test
    public void testTokenIsStartTag() {
        assertTrue(startTag.isStartTag());
        assertFalse(endTag.isStartTag());
    }

    @Test
    public void testTokenAsStartTag() {
        assertEquals(startTag, startTag.asStartTag());
    }

    @Test
    public void testTokenIsEndTag() {
        assertTrue(endTag.isEndTag());
        assertFalse(comment.isEndTag());
    }

    @Test
    public void testTokenAsEndTag() {
        assertEquals(endTag, endTag.asEndTag());
    }

    @Test
    public void testTokenIsComment() {
        assertTrue(comment.isComment());
        assertFalse(character.isComment());
    }

    @Test
    public void testTokenAsComment() {
        assertEquals(comment, comment.asComment());
    }

    @Test
    public void testTokenIsCharacter() {
        assertTrue(character.isCharacter());
        assertFalse(eof.isCharacter());
    }

    @Test
    public void testTokenAsCharacter() {
        assertEquals(character, character.asCharacter());
    }

    @Test
    public void testTokenIsEOF() {
        assertTrue(eof.isEOF());
        assertFalse(doctype.isEOF());
    }

    @Test
    public void testTokenTypeValues() {
        Token.TokenType[] expectedValues = {Token.TokenType.Doctype, Token.TokenType.StartTag, Token.TokenType.EndTag,
                Token.TokenType.Comment, Token.TokenType.Character, Token.TokenType.EOF};

        assertArrayEquals(expectedValues, Token.TokenType.values());
    }
}