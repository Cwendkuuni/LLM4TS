import static org.junit.Assert.*;
import org.junit.Test;

public class TokenTest {

    // Doctype tests
    @Test
    public void testDoctypeReset() {
        Token.Doctype doctype = new Token.Doctype();
        doctype.name.append("html");
        doctype.reset();
        assertEquals("", doctype.getName());
    }

    @Test
    public void testDoctypeGetPubSysKey() {
        Token.Doctype doctype = new Token.Doctype();
        doctype.pubSysKey = "PUBLIC";
        assertEquals("PUBLIC", doctype.getPubSysKey());
    }

    @Test
    public void testDoctypeGetPublicIdentifier() {
        Token.Doctype doctype = new Token.Doctype();
        doctype.publicIdentifier.append("identifier");
        assertEquals("identifier", doctype.getPublicIdentifier());
    }

    @Test
    public void testDoctypeGetSystemIdentifier() {
        Token.Doctype doctype = new Token.Doctype();
        doctype.systemIdentifier.append("identifier");
        assertEquals("identifier", doctype.getSystemIdentifier());
    }

    @Test
    public void testDoctypeIsForceQuirks() {
        Token.Doctype doctype = new Token.Doctype();
        doctype.forceQuirks = true;
        assertTrue(doctype.isForceQuirks());
    }

    // StartTag tests
    @Test
    public void testStartTagReset() {
        Token.StartTag startTag = new Token.StartTag();
        startTag.tagName = "div";
        startTag.reset();
        assertEquals(null, startTag.tagName);
    }

    @Test
    public void testStartTagName() {
        Token.StartTag startTag = new Token.StartTag();
        startTag.name("div");
        assertEquals("div", startTag.name());
    }

    @Test
    public void testStartTagNormalName() {
        Token.StartTag startTag = new Token.StartTag();
        startTag.name("div");
        assertEquals("div", startTag.normalName());
    }

    @Test
    public void testStartTagIsSelfClosing() {
        Token.StartTag startTag = new Token.StartTag();
        assertFalse(startTag.isSelfClosing());
    }

    // EndTag tests
    @Test
    public void testEndTagReset() {
        Token.EndTag endTag = new Token.EndTag();
        endTag.tagName = "div";
        endTag.reset();
        assertEquals(null, endTag.tagName);
    }

    // Comment tests
    @Test
    public void testCommentReset() {
        Token.Comment comment = new Token.Comment();
        comment.data.append("This is a comment");
        comment.reset();
        assertEquals("", comment.getData());
    }

    @Test
    public void testCommentGetData() {
        Token.Comment comment = new Token.Comment();
        comment.data.append("This is a comment");
        assertEquals("This is a comment", comment.getData());
    }

    // Character tests
    @Test
    public void testCharacterReset() {
        Token.Character character = new Token.Character();
        character.data = "This is a character";
        character.reset();
        assertEquals(null, character.getData());
    }

    @Test
    public void testCharacterGetData() {
        Token.Character character = new Token.Character();
        character.data = "This is a character";
        assertEquals("This is a character", character.getData());
    }

    // CData tests
    @Test
    public void testCDataToString() {
        Token.CData cdata = new Token.CData("This is a CDATA");
        assertEquals("<![CDATA[This is a CDATA]]>", cdata.toString());
    }
    
    // EOF tests
    @Test
    public void testEOFReset() {
        Token.EOF eof = new Token.EOF();
        assertEquals(eof, eof.reset());
    }

    // TokenType tests
    @Test
    public void testTokenTypeIsDoctype() {
        Token token = new Token.Doctype();
        assertTrue(token.isDoctype());
        assertFalse(token.isStartTag());
        assertFalse(token.isEndTag());
        assertFalse(token.isComment());
        assertFalse(token.isCharacter());
        assertFalse(token.isCData());
        assertFalse(token.isEOF());
    }

    @Test
    public void testTokenTypeIsStartTag() {
        Token token = new Token.StartTag();
        assertFalse(token.isDoctype());
        assertTrue(token.isStartTag());
        assertFalse(token.isEndTag());
        assertFalse(token.isComment());
        assertFalse(token.isCharacter());
        assertFalse(token.isCData());
        assertFalse(token.isEOF());
    }

    @Test
    public void testTokenTypeIsEndTag() {
        Token token = new Token.EndTag();
        assertFalse(token.isDoctype());
        assertFalse(token.isStartTag());
        assertTrue(token.isEndTag());
        assertFalse(token.isComment());
        assertFalse(token.isCharacter());
        assertFalse(token.isCData());
        assertFalse(token.isEOF());
    }

    @Test
    public void testTokenTypeIsComment() {
        Token token = new Token.Comment();
        assertFalse(token.isDoctype());
        assertFalse(token.isStartTag());
        assertFalse(token.isEndTag());
        assertTrue(token.isComment());
        assertFalse(token.isCharacter());
        assertFalse(token.isCData());
        assertFalse(token.isEOF());
    }

    @Test
    public void testTokenTypeIsCharacter() {
        Token token = new Token.Character();
        assertFalse(token.isDoctype());
        assertFalse(token.isStartTag());
        assertFalse(token.isEndTag());
        assertFalse(token.isComment());
        assertTrue(token.isCharacter());
        assertFalse(token.isCData());
        assertFalse(token.isEOF());
    }

    @Test
    public void testTokenTypeIsCData() {
        Token token = new Token.CData("This is a CDATA");
        assertFalse(token.isDoctype());
        assertFalse(token.isStartTag());
        assertFalse(token.isEndTag());
        assertFalse(token.isComment());
        assertFalse(token.isCharacter());
        assertTrue(token.isCData());
        assertFalse(token.isEOF());
    }

    @Test
    public void testTokenTypeIsEOF() {
        Token token = new Token.EOF();
        assertFalse(token.isDoctype());
        assertFalse(token.isStartTag());
        assertFalse(token.isEndTag());
        assertFalse(token.isComment());
        assertFalse(token.isCharacter());
        assertFalse(token.isCData());
        assertTrue(token.isEOF());
    }

}